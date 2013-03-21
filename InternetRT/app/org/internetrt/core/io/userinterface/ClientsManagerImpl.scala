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
import scala.collection.mutable.Queue
import org.internetrt.core.siblings.ClusterManager

abstract class ClientsManagerImpl extends ClientsManager {
  import global.clusterManager
  import scala.collection.mutable.Map
  val clients: Map[String, UserConnector] = Map.empty

  def join(uid: String, driver: ClientDriver) {

    val connector = clients.get(uid) match {
      case Some(c) => c
      case None => {
        val c = new UserConnector(uid, clusterManager)
        clients += (uid -> c)
        c
      }
    };

    connector.register(driver);

    // Notify the main node
    clusterManager.getNodeRef(uid) match {
      case Some(node) => {
        node.join(uid, driver.status)
      }
      case None => None
    }
  }

  def sendevent(uid: String, msg: String, allowedStatus: Seq[String]) {
    try {
      // Choice the right node
      clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          node.sendevent(uid, msg, allowedStatus)
        }
        case None => {
          val connector = clients.get(uid).get;
          connector.output(msg, allowedStatus);
        }
      }

    } catch {
      case e: NoSuchElementException => throw new InvalidStatusException("User " + uid + " don't have alive clients")
    }
  }

  def ask(uid: String, msg: String, allowedStatus: Seq[String]): Future[String] = {
    try {
      // Choice the right node
      clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          node.ask(uid, msg, allowedStatus)
        }
        case None => {
          val connector = clients.get(uid).get;
          connector.userInputReader.ask(msg, allowedStatus);
        }
      }
      
    } catch {
      case e: NoSuchElementException => throw new InvalidStatusException("User " + uid + " don't have alive clients")
    }
  }
  def response(uid: String, msg: String, msgID: String) = {
    try {
      // Choice the right node
      clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          node.response(uid, msg, msgID)
        }
        case None => {
          val connector = clients.get(uid).get;
          connector.userInputReader.response(msg, msgID);
        }
      }

    } catch {
      case e: Throwable => {
        System.out.println("[ClientsManager:response] Error on response!:" + uid);
      }
    }
  }
}

class UserConnector(id: String, cluster: ClusterManager) {

  import scala.collection.mutable.ListBuffer;
  import scala.collection.mutable.Map;
  import scala.collection.Seq;

  def uid = id
  def clusterManager = cluster

  val clients = ListBuffer.empty[ClientDriver];
  val delayedMessages = Queue.empty[(String, Seq[String], Option[String])]

  //  object Tick {}
  //  val softStateUpdater = system.scheduler.schedule(ClientStatus.TimeOut, ClientStatus.TimeOut,
  //    system.actorOf(Props(new Actor {
  //      def receive = {
  //        case Tick => clients --= clients.filter(_.status == ClientStatus.Dead.toString())
  //      }
  //    })), Tick)
  //    
  lazy val userInputReader = new UserInputReader(this)

  def register(client: ClientDriver) {
    clients += client;
    delayedMessages.dequeueFirst(x => true) match {
      case Some((msg, allowedStatus, msgID)) => output(msg, allowedStatus, msgID)
    }

  }
  //  def unregister(client: ClientDriver) {
  //    clients -= client;
  //  }

  def output(msg: String, allowedStatus: Seq[String], msgID: Option[String] = None) {

    for (c <- clients) {
      if (allowedStatus.contains(c.status))
        c.response(msg, msgID)
    }

    // clear the dead clients here
    clients --= clients.filter(_.status == ClientStatus.Dead.toString())
    if (clients.size == 0)
      delayedMessages += ((msg, allowedStatus, msgID))

  }
}

