package org.internetrt.sdk.util
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq

class RoutingGenerator (signalXmlString:String, appXmlString:String){
  
	val signalXml = scala.xml.XML.loadString(signalXmlString);
	val appXml = scala.xml.XML.loadString(appXmlString);
	
	def generateRouting(signal:String, from:String, to:String) = {
	  val signalNode =  generateSignalNode(signal, from, to)
	  val RequestListenerNodes  =  generateRequestListenerNodes(from,to)

	  (<Routing>{signalNode}{RequestListenerNodes}</Routing>).toString()
	}
	
	def generateSignalNode(signalName:String, from:String, to:String): NodeSeq = 
	{
	  val signalNode = 
	    <signal><from>{from}</from><user></user><name>{signalName}</name>{  (signalXml \ "vars" )}</signal>
	 scala.xml.NodeSeq.fromSeq(signalNode)
	}
	
	def generateRequestListenerNodes(from:String, to:String): NodeSeq = {
	  val RequestListener = appXml \ "SignalHanlders" \ "RequestListener" 
	  val DescriptionNode = RequestListener \ "Description"
	  val URLNode = RequestListener \ "URL"
	  val AdapterNode = RequestListener \ "Adapter"
	  
	  <RequestListener type = {RequestListener \ "@type"} runat = {to}>{DescriptionNode}{URLNode}{AdapterNode}</RequestListener>
	}

}