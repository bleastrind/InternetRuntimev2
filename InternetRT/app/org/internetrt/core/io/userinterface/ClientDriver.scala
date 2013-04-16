package org.internetrt.core.io.userinterface

import scala.concurrent.duration._
/**
 * TODO use enum
 */
object ClientStatus extends Enumeration {
  type Status = Value
  val Active = Value("Active")
  val Background = Value("Background")
  val Sleep = Value("Sleep")
  val Dead = Value("Dead")

  val All = Seq(Active, Background, Sleep, Dead)
  val TimeOut = 10 second
}

trait ClientDriver {

  private var lasttime = System.currentTimeMillis();
  private var clientstatus: String = ClientStatus.Active.toString();
  
  /**
   * Keep the status up to date
   * If not touch it within TimeOut, the status will become Dead
   */
  def touch(){
    lasttime = System.currentTimeMillis();
  }
  
  def setStatus(status:String) = {
    clientstatus = status;
  }
  

  def status():String = {
    if( !isValid || System.currentTimeMillis() - lasttime > ClientStatus.TimeOut.toMillis )
      clientstatus = ClientStatus.Dead.toString()

    clientstatus
  }
  def response(data: String, msgID: Option[String] = None)
  
  def isValid():Boolean = true
}
