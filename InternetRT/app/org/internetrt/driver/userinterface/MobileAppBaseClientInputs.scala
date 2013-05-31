package org.internetrt.driver.userinterface

import play.api.mvc._
import play.api.libs.iteratee._
import org.internetrt.SiteUserInterface
import org.internetrt.core.io.userinterface.ClientStatus

object MobileAppBaseClientInputs {
  def login(username: String, password: String) = WebSocket.using[String] { request =>

  

    // Send a single 'Hello!' message

    val UserID = SiteUserInterface.login(username, password)

    val (out, channel) = Concurrent.broadcast[String]

    val driver = new MobileAppClientDriver(channel)
  // Log events to the console
    val in = Iteratee.foreach[String](println).mapDone { _ =>
      println("Disconnected")
      driver.setStatus(ClientStatus.Dead.toString)
    }
    SiteUserInterface.clientsManager.join(UserID, driver)

    (in, out)
  }

}