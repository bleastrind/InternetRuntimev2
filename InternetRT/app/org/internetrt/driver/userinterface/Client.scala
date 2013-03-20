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
import scala.concurrent.duration._
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


object Client extends Controller {
  var clients = Map.empty[String, PushEnumerator[String]]

  def sendMessage(user: String, data: String) {
    clients(user).push(data);
  }

  def response = Action{
    request=>
      val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
      val msg = request.body.asText.getOrElse("")
      val success = request.queryString.get(CONSTS.MSGID) match{ 
        case Some(x::xs) => ClientMessageActor.clientsManager.response(uid,msg,x)
        case _ => false
      }
      
      Ok(success.toString())      
  }
  def test = Action {
    request=>
    val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
    
    implicit val timeout = Timeout(5.seconds)
    ClientMessageActor.ref ! Message(uid, "test");
    import play.api.templates.Html
    Ok(Html("""<a href="http://www.baidu.com">"""+uid+"""</a>"""))
  }
  def tt() = {
    Thread.sleep(10000)
    "sfd"
  }
  
  def send = Action{
    Ok
  }
  
  def getLongPollingResult(request:Request[AnyContent],wrapper:(String=>Result))={
      val uid = request.session.get(CONSTS.SESSIONUID).getOrElse(CONSTS.ANONYMOUS);
      val cid = request.queryString.get(CONSTS.CLIENTID) match {
        case Some(list)=>list.head //get the first client id
        case None=> UUID.randomUUID().toString() // else a new one
      }
      val status = request.queryString.get(CONSTS.CLIENTSTATUS) match{
        case Some(list)=>list.head //get the first status
        case None=> ClientStatus.Active.toString()
      }
      
      implicit val timeout = Timeout(5.seconds)

      //import play.api.Play.current;
      import scala.concurrent.ExecutionContext.Implicits.global
      val result = ClientMessageActor.ref ? Join(uid, cid ,status) recover{
        case e:AskTimeoutException => {

           "{cid:\""+cid + "\"}" // The client script can request with the cid next time s.t. it can set the status of the channel
        }
      }

      Async {
        ClientMessageActor.ref ! Quit()
        
        result.mapTo[String]
          .map(i => wrapper(i))
      }   
  }
  def longpolling = Action{
    request =>
      getLongPollingResult(request,i => Ok(i))
  }
  def longpollingjsonp = Action {
    request =>
      val callback = request.queryString.get("callback").map(s => s.head).get
      getLongPollingResult(request,i => Ok(callback + "(" + i + ")"))
  }
}
class PageJavaScriptSlimClientDriver(cid:String) extends ClientDriver{
	var channel:ActorRef = null
	
	def response(data:String, msgID:Option[String]){
	  channel ! "{cid:\""+cid+"\",data:"+data+ (msgID match {
	    case Some(id)=>","+CONSTS.MSGID+":"+id
	    case _=>""
	  })+"}"
	}
}

class ClientMessageActor extends Actor {
	import scala.collection.mutable
	import ClientStatus._

  val pagedrivers = mutable.Map.empty[String,PageJavaScriptSlimClientDriver]
  

  def receive = {

    case Join(uid,cid,clientStatus) => {
        
    	//get the unique channel
      val clientDriver = pagedrivers.get(cid) match{
        case Some(c)=> c
        case None=>{
           //create new one
    	  val c = new PageJavaScriptSlimClientDriver(cid)
    	  pagedrivers += (cid -> c)
    	  
    	  //register the channel as one of the users("uid")
    	  clientsManager.join(uid,c)
    	  
    	  c // return the newly created driver         
        }
      }
       //reset the actor to response
        /**TODO CHECK IF THIS IS CHANGED IN CLIENTSMANAGER's USERCONNECTOR*/
    	clientDriver.channel = sender
      
    	/**TODO CHECK IF THIS IS CHANGED IN CLIENTSMANAGER's USERCONNECTOR*/
    	//reset the status of the channel
    	clientDriver.clientstatus = clientStatus;
    	
      Logger.info("New member joined:"+sender)
      Logger.info("size:"+pagedrivers.size)

    }

    case Quit() => {
      Logger.info("Member has disconnected: "+sender)
      
      //TODO how to find the actually changed actor?
      for(p <- pagedrivers){
        if (p._2.channel.isTerminated){
          if(p._2.clientstatus == ClientStatus.WaitingHeartBeat.toString()){
            p._2.onClientDistory(p._2)
            p._2.clientstatus = ClientStatus.Dead.toString()
          }
          else 
        	p._2.clientstatus = ClientStatus.WaitingHeartBeat.toString()
        }
      }
      val terminated = pagedrivers.filter(p=>p._2.clientstatus == ClientStatus.Dead.toString()).map(p=>p._1)
      pagedrivers --= terminated;
    }

    case Message(uid, msg) => {
      Logger.info("Got message, send it to:" + uid)
      clientsManager.sendevent(uid, msg, Seq(ClientStatus.Active.toString()))
    }

  }

}

object ClientMessageActor {

  trait Event
  case class Join(uid:String,cid:String,clientstatus:String) extends Event
  case class Quit() extends Event
  case class Message(uid:String, msg: String) extends Event
  lazy val system = ActorSystem("clientsmessagepusher")
  lazy val ref = system.actorOf(Props[ClientMessageActor])
  
  val clientsManager = SiteUserInterface.clientsManager 
}