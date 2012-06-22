package org.internetrt.core.model
import org.internetrt.core.signalsystem.Signal

case class Application(xml:scala.xml.Elem){
  def id = (xml \\ "AppID").text
  def appOwner = (xml \\ "AppOwner").text
  def accessRequests = Seq((xml \\ "AccessRequests").toString())
}
//abstract class Application {
//	def requests:Seq[Any]
//	def responses:Seq[Any]
//}
//
//class Request{
//  def matchSignal(s:Signal)=true
//}
//
//class Response{
//  
//}