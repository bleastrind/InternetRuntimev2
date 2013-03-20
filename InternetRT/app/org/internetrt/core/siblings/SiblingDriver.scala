package org.internetrt.core.siblings

import org.internetrt.core.io.userinterface.ClientDriver
import org.internetrt.core.io.userinterface.ClientStatus

class SiblingDriver(driver:ClientDriver) extends ClientDriver{
  var onClientDistory: ClientDriver => Unit = null;
  var clientstatus: String = ClientStatus.Active.toString();
  def response(data: String, msgID: Option[String] = None) = {
    // shoud  
    driver.response(data, msgID)
  }
}