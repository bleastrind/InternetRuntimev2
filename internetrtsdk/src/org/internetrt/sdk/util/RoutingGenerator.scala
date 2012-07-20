package org.internetrt.sdk.util
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq
import org.internetrt.sdk.exceptions.FormatErrorException
 
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
		  val signalName = ( RequestListener \ "Adapter" \ "Signalname").text
		    if (signalName == null || signalName == "")
		  {
			  throw new FormatErrorException("Error in App Xml: RequestListener.Adapter.Signalname not set!");
		  }
		  val signal = (signalXml \ "name").text
		  if (signal == null || signal == "")
		  {
			  throw new FormatErrorException("Error in Signal Xml: signal Xml signalName not set!");
		  }
		   RequestListener filter(RequestListener => signalName == signal) 
	    }).flatten)
	}
	
}

object FreeRoutingGenerator{
  	def generateRouting(signalname:String,appid:String,listener: ListenerConfig):String = {
	  <Routing><Signal><from>{appid}</from><name>{signalname}</name></Signal>{listener.node}</Routing> toString
	}
}
