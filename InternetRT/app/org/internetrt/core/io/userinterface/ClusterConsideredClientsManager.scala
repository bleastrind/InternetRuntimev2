package org.internetrt.core.io.userinterface

import scala.concurrent.Future

trait ClusterConsideredClientsManager extends ClientsManager{
  
  abstract override def join(uid: String, driver: ClientDriver) {
	  super.join(uid, driver)    
	  global.clusterManager.getNodeRef(uid) match {
	      case Some(node) => {
	        node.join(uid, driver.status)
	      }
	      case None => None
	    }
  }
  
  abstract override def sendevent(uid: String, msg: String, allowedStatus: Seq[String]) {
     // Choice the right node
      global.clusterManager.getNodeRef(uid) match {
        case Some(node) => {
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