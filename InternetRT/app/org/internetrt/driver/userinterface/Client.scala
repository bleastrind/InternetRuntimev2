package org.internetrt.driver.userinterface
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import play.api.libs.Comet
import play.api.libs.iteratee.PushEnumerator
import play.api._
import akka.actor._
import akka.actor.Actor._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import scala.concurrent.
  duration._
import play.api._
import play.api.mvc._
import play.api.libs._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import akka.util.Timeout
import akka.pattern.ask
import ClientMessageActor._
import scala.concurrent.Await
import java.util.concurrent.TimeoutException
import akka.pattern.AskTimeoutException
import java.util.UUID
import scala.collection.mutable.ListBuffer
import org.internetrt.core.io.userinterface.ClientDriver
import org.internetrt.CONSTS
import org.internetrt.core.io.userinterface.ClientsManager
import org.internetrt.core.io.userinterface.ClientStatus
import org.internetrt.core.io.userinterface.ClientDriver
import org.internetrt.SiteUserInterface
import java.util.concurrent.Executors
import org.internetrt.util.Debuger

object Client extends Controller {
  //  var clients = Map.empty[String, PushEnumerator[String]]
  //
  //  def sendMessage(user: String, data: String) {
  //    clients(user).push(data);
  //  }

  def response = Action {
    request =>
      val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
      val msg = request.body.asText.getOrElse("")
      val success = request.queryString.get(CONSTS.MSGID) match {
        case Some(list) => ClientMessageActor.clientsManager.response(uid, msg, list.head)
        case None => false
      }

      Ok(success.toString())
  }
  def test = Action {
    request =>

      //ClientMessageActor.ref ! Test()

      val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
      import net.liftweb.json._;
      import net.liftweb.json.JsonAST._;
      //import net.liftweb.json.Printer._;
      org.internetrt.util.Debuger.debug(uid);
      implicit val timeout = Timeout(5.seconds)
      SiteUserInterface.sendEvent(uid, compact(JsonAST.render(Xml.toJson(<value><name>u.c"ontent</name><query>u.query</query><data>msg</data></value>))), Seq(ClientStatus.Active.toString()))
      //SiteUserInterface.sendEvent(uid, compact(JsonAST.render(Xml.toJson(<value><name>u.c"ontent</name><query>u.query</query><data>msg</data></value>))), Seq(ClientStatus.Dead.toString()))

      import play.api.templates.Html
      Ok(Html("""<a href="http://www.baidu.com">""" + uid + """</a>"""))
  }
  def tt() = {
    Thread.sleep(10000)
    "sfd"
  }

  def send = Action {
    Ok
  }

  def getLongPollingResult(request: Request[AnyContent], wrapper: (String => Result)) = {
    val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
    val cid = request.queryString.get(CONSTS.CLIENTID) match {
      case Some(list) => list.head //get the first client id
      case None => UUID.randomUUID().toString() // else a new one
    }
    val status = request.queryString.get(CONSTS.CLIENTSTATUS) match {
      case Some(list) => list.head //get the first status
      case None => ClientStatus.Active.toString()
    }

    implicit val timeout = Timeout(5.seconds)

    //import play.api.Play.current;
    import scala.concurrent.ExecutionContext.Implicits.global
    val result = ClientMessageActor.ref ? Join(uid, cid, status) recover {
      case e: AskTimeoutException => {

        "{cid:\"" + cid + "\"}" // The client script can request with the cid next time s.t. it can set the status of the channel
      }
    }

    Async {
      result.mapTo[String]
        .map(i => wrapper(i))
    }
  }
  def longpolling = Action {
    request =>
      getLongPollingResult(request, i => Ok(i))
  }
  def longpollingjsonp = Action {
    request =>
      val callback = request.queryString.get("callback").map(s => s.head).get
      getLongPollingResult(request, i => Ok(callback + "(" + i + ")"))
  }
}
class PageJavaScriptSlimClientDriver(cid: String, channel: ActorRef) extends ClientDriver {
  //var channel:ActorRef = null

  def response(data: String, msgID: Option[String]) {
    Debuger.debug("[Client] Ready To Output :"+ data+ "  channel :"+channel)
    Debuger.assert(!channel.isTerminated, "[Client] Ready To Output :"+ data+ "  channel terminated:"+channel.isTerminated);

    channel ! "{cid:\"" + cid + "\",data:" + data + (msgID match {
      case Some(id) => "," + CONSTS.MSGID + ":" + id
      case _ => ""
    }) + "}"

  }

  override def isValid() = !channel.isTerminated
}

class ClientMessageActor extends Actor {
  import scala.collection.mutable
  import ClientStatus._

  val scheduler = Executors.newCachedThreadPool()

  def async(f: => Unit) {
    scheduler.execute(new Runnable {
      def run = f
    })
  }

  def receive = {

    case Join(uid, cid, clientStatus) => {

      //async {
        //get the unique channel
        val clientDriver = new PageJavaScriptSlimClientDriver(cid, sender)


        Debuger.assert(clientDriver.isValid,"*[Warning:Client] new join clientDriver is not valid");
        clientsManager.join(uid, clientDriver)

        //clientDriver.touch();
        //clientDriver.setStatus(clientStatus);

        Debuger.debug("[Client:Actor Receive]New member joined:" + sender + "  Clientstatus:"+ clientDriver.isValid)
        
      //}
    }

    case Message(uid, msg) => {
      async {
        Logger.info("Got message, send it to:" + uid)
        clientsManager.sendevent(uid, msg, Seq(ClientStatus.Active.toString()))
      }
    }

    case Test() => {
      //Damn!! No Parallel, MessageQueue onebyone!
      async {
        org.internetrt.util.Debuger.debug("Test");
        Thread.sleep(10000);
      }
    }

  }

}

object ClientMessageActor {

  trait Event
  case class Join(uid: String, cid: String, clientstatus: String) extends Event
  case class Test() extends Event
  case class Message(uid: String, msg: String) extends Event
  lazy val system = ActorSystem("clientsmessagepusher")
  lazy val ref = system.actorOf(Props[ClientMessageActor])

  val clientsManager = SiteUserInterface.clientsManager
}