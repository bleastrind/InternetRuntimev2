package org.internetrt.core.io.userinterface
import java.util.UUID
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.actor.Props
import ClientStubActor._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Await
import org.internetrt.exceptions.InvalidStatusException
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * TODO use enum
 */
object ClientStatus extends Enumeration {
  type Status = Value
  val Active = Value("Active")
  val Background = Value("Background")
  val Sleep = Value("Sleep")
  val Dead = Value("Dead")
  val WaitingHeartBeat = Value("waiting")
}
trait ClientDriver {

  var onClientDistory: ClientDriver => Unit = null;
  var clientstatus: String = ClientStatus.Active.toString();
  def response(data: String, msgID: Option[String] = None)

}

object ClientsManager {
  import scala.collection.mutable.Map
  val clients: Map[String, UserConnector] = Map.empty

  def join(uid: String, driver: ClientDriver) {
    val connector = clients.get(uid) match {
      case Some(c) => c
      case None => {
        val c = new UserConnector(uid)
        clients += (uid -> c)
        c 
      }
    };

    connector.register(driver);
    driver.onClientDistory = connector.unregister;
  }

  def response(uid: String, msg: String, msgID: String) = {
    try {
      val connector = clients.get(uid).get;
      connector.response(msg, msgID);
    } catch {
      case e:Throwable => {
        System.out.println("[ClientsManager:response] Error on response!:" + uid);
      }
    }
  }
  def sendevent(uid: String, msg: String, allowedStatus: Seq[String]) {
    try {
      val connector = clients.get(uid).get;
      connector.output(msg, allowedStatus);
    } catch {
      case e: NoSuchElementException => throw new InvalidStatusException("User " + uid + " don't have alive clients")
    }
  }
  def ask(uid: String, msg: String, allowedStatus: Seq[String]): Future[String] = {
    try {
      val connector = clients.get(uid).get;
      connector.ask(msg, allowedStatus);
    } catch {
      case e: NoSuchElementException => throw new InvalidStatusException("User " + uid + " don't have alive clients")
    }
  }
}

class UserConnector(uid: String) {
  import scala.collection.mutable.ListBuffer;
  import scala.collection.mutable.Map;
  import scala.collection.Seq;

  val clients = ListBuffer.empty[ClientDriver];

  def register(client: ClientDriver) {
    clients += client;
  }
  def unregister(client: ClientDriver) {
    clients -= client;
  }
  def response(msg: String, msgid: String): Boolean = {
    implicit val timeout = Timeout(2 seconds) //It's almost sync request to give response

    val result = (akka.pattern.ask(ClientStubActor.ref) ? Response(msg, msgid)).recover {
      case e => false
    }
    Await.result(result.mapTo[Boolean], timeout.duration);
  }
  def ask(msg: String, allowedStatus: Seq[String]): Future[String] = {
    implicit val timeout = Timeout(10 seconds)

    val msgID = Counter.count();

    val result = (akka.pattern.ask(ClientStubActor.ref) ? Request(msgID)).mapTo[String]; //Record the callback actorref

    output(msg, allowedStatus, Some(msgID)); // Send message to clients

    result
  }
  def output(msg: String, allowedStatus: Seq[String], msgID: Option[String] = None) {
    for (c <- clients) {
      if (allowedStatus.contains(c.clientstatus))
        c.response(msg, msgID)
    }
  }
}

class ClientStubActor extends Actor {
  /**TODO check whether multi thread sync &  confliction work well here*/
  import collection.JavaConversions._
  val waitingMessages: scala.collection.mutable.ConcurrentMap[String, ActorRef] = new java.util.concurrent.ConcurrentHashMap[String, ActorRef]()

  def receive = {
    case Request(msgID) => {

      waitingMessages += (msgID -> sender)
    }
    case Response(msg, msgID) => {

      waitingMessages.get(msgID) match {
        case Some(actor) => {
          actor ! msg
          waitingMessages -= msgID
          /**TODO check whether work here*/
          sender ! true
        }
        case _ => sender ! false
      }
    }
  }
}

object ClientStubActor {
  case class Request(msgID: String)
  case class Response(msg: String, msgID: String)
  lazy val system = ActorSystem("clientsmessagepusher")
  lazy val ref = system.actorOf(Props[ClientStubActor])

  val clientsdriver = ClientsManager
  /**TODO direct ref: 2/3  */
}

object Counter {

  /** TODO check whether it's the right way to avoid id conflict*/
  import scala.concurrent.stm._
  val countervar = Ref.apply(0)
  def count() = {
    atomic {
      implicit txn =>
        val counter = countervar.get(txn);
        countervar.set(counter + 1)
        counter.toString()
    }
  }
}