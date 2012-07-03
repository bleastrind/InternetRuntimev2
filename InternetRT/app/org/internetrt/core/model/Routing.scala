package org.internetrt.core.model
import org.internetrt.core.signalsystem.Signal
import org.internetrt.core.signalsystem.SignalResponse
import org.internetrt.core.signalsystem.ObjectResponse
import java.util.UUID

//TODO provide a tool set to help retrieve&consume the information in the structure
case class Routing(userID:String,xml:scala.xml.Elem) {
  
  //def getNextOutput(state:String, input:Signal):SignalResponse = new ObjectResponse("Stub")
}

case class RoutingInstance(userID:String,xml:scala.xml.Elem){
  val id:String = xml \ "id" text// It's a random id, used to pass along the workflow, s.t. user can identify the ligal running workflow
  //  val timestamp:Int
//  val state:String
//  def acceptRequest(s:Signal)
//  def getOutputAdapter={}
//  def getCurrentUser:String
}
