package org.internetrt.core.model
import org.internetrt.core.signalsystem.Signal

case class Application(xml:scala.xml.Elem){
  def id = (xml \\ "AppID").text
  def appOwner = (xml \\ "AppOwner").text
  def accessRequests = (xml \\ "AccessRequest" map (node => node match {
    case <AccessRequest>{text}</AccessRequest> => Some(text.text)
    case _ => None 
  })).flatten
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