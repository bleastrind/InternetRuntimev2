package org.internetrt.driver.siblings

import org.internetrt.core.io.userinterface.ClientDriver
import org.internetrt.core.io.userinterface.ClientStatus
import org.internetrt.core.siblings.NodeRef

class SiblingDriver(uid:String,func:(String, Option[String]) => Unit,status:String = ClientStatus.Active.toString()) extends ClientDriver{
  setStatus(status);
  def response(data: String, msgID: Option[String] = None) = {
    func(data,msgID)
  }
}