package org.internetrt.core.io.userinterface

import scala.concurrent.Future
import org.internetrt.core.InternetRuntime

trait ClientsManager {
  val global:InternetRuntime
  
  def join(uid: String, driver: ClientDriver);

  def response(uid: String, msg: String, msgID: String);
  def sendevent(uid: String, msg: String, allowedStatus: Seq[String]);
  def ask(uid: String, msg: String, allowedStatus: Seq[String]): Future[String];
}