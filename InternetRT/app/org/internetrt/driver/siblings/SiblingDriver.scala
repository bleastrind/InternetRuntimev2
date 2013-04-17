package org.internetrt.driver.siblings

import org.internetrt.core.io.userinterface.ClientDriver
import org.internetrt.core.io.userinterface.ClientStatus
import org.internetrt.core.siblings.NodeRef
import org.internetrt.util.Debuger

class SiblingDriver(uid:String,func:(String, Option[String]) => Unit,status:String = ClientStatus.Active.toString()) extends ClientDriver{
  setStatus(status);
  def response(data: String, msgID: Option[String] = None) = {
    func(data,msgID)
  }
}

object SiblingDriver{
  var driverCache: Map[String, SiblingDriver] = Map.empty
  
  /**
   * Create a new driver of the uid and set the old driver dead 
   */
  def activeNewNode(uid: String,func:(String, Option[String])=> Unit,status:String = ClientStatus.Active.toString()): SiblingDriver = {
    driverCache.get(uid) map( _.setStatus(ClientStatus.Dead.toString()))
     
    val node = new SiblingDriver(uid,func,status)
    driverCache += (uid -> node)
    
    Debuger.debug("[SiblingDriver]drivers size:"+driverCache.size)
    node
  }
}