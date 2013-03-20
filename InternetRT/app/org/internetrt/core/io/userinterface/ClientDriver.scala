package org.internetrt.core.io.userinterface

trait ClientDriver {

  var onClientDistory: ClientDriver => Unit = null;
  var clientstatus: String = ClientStatus.Active.toString();
  def response(data: String, msgID: Option[String] = None)

}
