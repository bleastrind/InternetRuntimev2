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


class ClientsManagerImpl extends ClientsManager{
  //import global.clusterControl
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
      connector.userInputReader.response(msg, msgID);
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
      connector.userInputReader.ask(msg, allowedStatus);
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
  lazy val userInputReader = new UserInputReader(this)

  def register(client: ClientDriver) {
    clients += client;
  }
  def unregister(client: ClientDriver) {
    clients -= client;
  }

  def output(msg: String, allowedStatus: Seq[String], msgID: Option[String] = None) {
    for (c <- clients) {
      if (allowedStatus.contains(c.clientstatus))
        c.response(msg, msgID)
    }
  }
}

