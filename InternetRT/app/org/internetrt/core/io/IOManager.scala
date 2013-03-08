package org.internetrt.core.io
import scala.concurrent.Future
import org.internetrt.core.InternetRuntime


  trait IOManager {
    val global:InternetRuntime
    def sendToUrl(uid:String,url:String,msg:String)
    def sendToClient(uid:String,msg:String,allowedStatus:Seq[String])
    def readFromClient(uid:String,msg:String,allowedStatus:Seq[String]):Future[String]
  }
