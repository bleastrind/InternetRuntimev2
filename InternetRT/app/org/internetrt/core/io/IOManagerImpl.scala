package org.internetrt.core.io
import akka.dispatch.Future
import org.internetrt.core.io.userinterface.ClientsManager
import org.internetrt.core.io.userinterface.ClientStatus

	
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
	     
//	     def readFromClient(uid:String,msg:String,allowedStatus:Seq[String],externalCallback:EndPoint){
//	       
//	     }
	}


