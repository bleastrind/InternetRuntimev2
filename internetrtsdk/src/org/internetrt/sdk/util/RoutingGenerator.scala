package org.internetrt.sdk.util
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq

object RoutingGenerator{
  	def generateRouting(signalname:String,listener: ListenerConfig):String = {
	  <Routing><signal><name>{signalname}</name></signal>{listener.node}</Routing> toString
	}
}

class RoutingGenerator (signalXmlString:String, appXmlString:String){
  
	val signalXml = scala.xml.XML.loadString(signalXmlString);
	val appXml = scala.xml.XML.loadString(appXmlString);
	
	def generateRouting(signal:String, from:String, to:String) = {
	  val signalNode =  generateSignalNode(signal, from, to)
	  val RequestListenerNodes  =  generateRequestListenerNodes(from,to)
	  val EventListenerNodes = appXml \ "SignalHanlders" \ "EventListener"
	  (<Routing>{signalNode}{RequestListenerNodes}{EventListenerNodes}</Routing>).toString()
	}
	
	private def generateSignalNode(signalName:String, from:String, to:String): NodeSeq = 
	{
	  val signalNode = 
	    <signal><from>{from}</from><user></user><name>{signalName}</name>{  (signalXml \ "vars" )}</signal>
	 scala.xml.NodeSeq.fromSeq(signalNode)
	}
	
	private def generateRequestListenerNodes(from:String, to:String): NodeSeq = {
	  val RequestListener = appXml \ "SignalHanlders" \ "RequestListener" 
	  val DescriptionNode = RequestListener \ "Description"
	  val URLNode = RequestListener \ "URL"
	  val AdapterNode = RequestListener \ "Adapter"
	  
	  <RequestListener type = {RequestListener \ "@type"} runat = {to}>{DescriptionNode}{URLNode}{AdapterNode}</RequestListener>
	}

}