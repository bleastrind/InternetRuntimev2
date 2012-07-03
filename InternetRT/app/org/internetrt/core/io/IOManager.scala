package org.internetrt.core.io
import akka.dispatch.Future
import org.internetrt.core.InternetRuntime


  trait IOManager {
    val global:InternetRuntime
    def sendToClient(uid:String,msg:String,allowedStatus:Seq[String])
    def readFromClient(uid:String,msg:String,allowedStatus:Seq[String]):Future[String]
  }
