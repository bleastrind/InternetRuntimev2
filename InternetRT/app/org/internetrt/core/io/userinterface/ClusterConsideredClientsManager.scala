package org.internetrt.core.io.userinterface

import scala.concurrent.Future
import org.internetrt.util.Debuger

trait ClusterConsideredClientsManager extends ClientsManager{
  
  abstract override def join(uid: String, driver: ClientDriver) {
	  super.join(uid, driver)    
	  //org.internetrt.util.Debuger.debug("[ClusterConsideredClientsMananger:join] Driver joined:"+driver);
	  global.clusterManager.getNodeRef(uid) match {
	      case Some(node) => {
	        
	    	Debuger.debug("[ClusterConsideredClientsManager:join]:join other node for uid:"+uid);
	        node.join(uid, driver.status)
	      }
	      case None => None
	    }
  }
  
  abstract override def sendevent(uid: String, msg: String, allowedStatus: Seq[String]) {
     // Choice the right node
	  Debuger.debug("[ClusterConsideredClientsManager:sendevent]"+uid);
      global.clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          Debuger.debug("[ClusterConsideredClientsManager:sendevent]:sendevent to other node for uid:"+uid);
          node.sendevent(uid, msg, allowedStatus)
        }
        case None =>super.sendevent(uid, msg, allowedStatus)
      }
  }
  
  abstract override def ask(uid: String, msg: String, allowedStatus: Seq[String]): Future[String] = {
      global.clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          node.ask(uid, msg, allowedStatus)
        }
        case None =>super.ask(uid, msg, allowedStatus)
      }    
  }
  
  abstract override def response(uid: String, msg: String, msgID: String) = {
       global.clusterManager.getNodeRef(uid) match {
        case Some(node) => {
          node.response(uid, msg, msgID)
        }
        case None =>super.response(uid, msg, msgID)
      }   
  }
}