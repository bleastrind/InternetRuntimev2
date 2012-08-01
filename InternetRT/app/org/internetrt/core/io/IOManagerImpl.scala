package org.internetrt.core.io
import akka.dispatch.Future
import org.internetrt.core.io.userinterface.ClientsManager
import org.internetrt.core.io.userinterface.ClientStatus
import java.net.URL
import org.apache.commons.lang.NotImplementedException
import org.internetrt.util.URI
import org.internetrt.sdk.util.HttpHelper

	
	abstract class IOManagerImpl extends IOManager{
		 val clientsdriver = ClientsManager /**TODO direct ref: 3/3  */
		 
		 val defaultAllowedStatus = Seq(ClientStatus.Active.toString())
//		 
//		 object broswerClientManager extends BroswerClientManager{
//		    val authCenter = global.authCenter 
//		 }
		 
	     def sendToClient(uid:String,msg:String,allowedStatus:Seq[String])={
	       clientsdriver.sendevent(uid,msg,allowedStatus)
	     }
		 
		 /**TODO if timeout ,do it in database*/
	     def readFromClient(uid:String,msg:String,allowedStatus:Seq[String]=defaultAllowedStatus):Future[String]={
	        clientsdriver.ask(uid,msg,allowedStatus)
	     }
	     
	     def sendToUrl(uid:String,url:String,msg:String){
	        import net.liftweb.json._;
	        import net.liftweb.json.JsonAST._;
	        System.out.println("[IOManagerImpl:sendToUrl]:" + url)
	       val u = new URI(url); 
	       u.protocal.toLowerCase() match{
	         case "http" => HttpHelper.httpClientGet(url)
	         case "osclient" => sendToClient(uid,
	             (Printer.pretty(JsonAST.render(Xml.toJson(<name>{u.content}</name><query>{u.query}</query><data>{msg}</data>)))),
	             Seq(ClientStatus.Active.toString()))
	         case _ => throw new NotImplementedException()
	       }
	     }
	}


