package org.internetrt.driver.userinterface

import org.internetrt.core.io.userinterface.ClientDriver
import play.api.libs.iteratee._
import play.api.libs.iteratee.Concurrent._
import org.omg.PortableInterceptor.ACTIVE
import org.internetrt.core.io.userinterface.ClientStatus

class MobileAppClientDriver(channel: Channel[String]) extends ClientDriver{

  def response(data: String, msgID: Option[String]) {
    channel.push(data);

  }
  
  override def status():String={
    ClientStatus.Active.toString
  }
}