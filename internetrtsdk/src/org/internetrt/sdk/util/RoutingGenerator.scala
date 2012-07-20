package org.internetrt.sdk.util
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq
 
class RoutingGenerator (signalXmlString:String, appXmlString:String){
  
	val signalXml = scala.xml.XML.loadString(signalXmlString);
	val appXml = scala.xml.XML.loadString(appXmlString);
	
	def generateRouting(signal:String, from:String, to:String, routingID: String, userID:String) = {
	  val signalNode =  generateSignalNode(signal, from, to, userID)
	  val RequestListenerNodes  =  generateRequestListenerNodes()
	  val EventListenerNodes = appXml \ "SignalHanlders" \ "EventListener"
	  (<Routing id = "{routingID}">
	      {signalNode}{RequestListenerNodes}{EventListenerNodes}
	     </Routing>).toString()
	}
	
	private def generateSignalNode(signalName:String, from:String, to:String, userID:String): NodeSeq = 
	{
	  val signalNode = 
	    <Signal>
		  	<from>{from}</from>
	  		<name>{signalName}</name>
	  		{  (signalXml \ "vars" )}
	  		</Signal>
	 scala.xml.NodeSeq.fromSeq(signalNode)
	}
	
	def generateRequestListenerNodes() = {
		scala.xml.NodeSeq.fromSeq( (appXml \ "SignalHanlders" \ "RequestListener" map{  (RequestListener) =>
		   RequestListener filter(RequestListener => ( RequestListener \ "Adapter" \ "Signalname").text == (signalXml \ "name").text) 
	    }).flatten)
	}
	
}

object FreeRoutingGenerator{
  	def generateRouting(signalname:String,appid:String,listener: ListenerConfig):String = {
	  <Routing><Signal><from>{appid}</from><name>{signalname}</name></Signal>{listener.node}</Routing> toString
	}
}
