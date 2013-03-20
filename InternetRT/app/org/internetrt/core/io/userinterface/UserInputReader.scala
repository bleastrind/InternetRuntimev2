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

class UserInputReader(connector:UserConnector) {
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

    connector.output(msg, allowedStatus, Some(msgID)); // Send message to clients

    result
  }
}

class ClientStubActor extends Actor {
  /**TODO check whether multi thread sync &  confliction work well here*/
  import collection.JavaConversions._
  val waitingMessages: scala.collection.concurrent.Map[String, ActorRef] = new java.util.concurrent.ConcurrentHashMap[String, ActorRef]()

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