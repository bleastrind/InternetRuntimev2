package org.internetrt.sdk.util
import scala.xml.XML
import scala.xml.Node
import scala.xml.NodeSeq

class RoutingGenerator {
	def generateRouting(signal:String, from:String, to:String):String = {
	  val signalNode =  generateSignalNode(signal, from, to)
	  val RequestListenerNodes  =  generateRequestListenerNodes(from,to)

	  (<Routing>
			{signalNode}
	  		{RequestListenerNodes}
	</Routing>).toString()
	}
	
	def generateSignalNode(signalName:String, from:String, to:String): NodeSeq = 
	{
	 // val xmlString= org.internetrt.driver.SignalController.getSignalDef(signalName)
	  val xmlString = scala.xml.XML.loadFile("Signal.xml").toString();
	  val xml = scala.xml.XML.loadString(xmlString)
	  val signalNode = 
	    <signal>
			  <from>{from}</from>
			  <name>{signalName}</name>
  			{  (xml \ "vars" )}
		</signal>
	 scala.xml.NodeSeq.fromSeq(signalNode)
	}
	
	def generateRequestListenerNodes(from:String, to:String): NodeSeq = {
	  // val appXmlString =org.internetrt.driver.ConfigController.appDetail(id: String)
	  val appXmlString = scala.xml.XML.loadFile("renrenApplication.xml").toString()
	  val appXml = scala.xml.XML.loadString(appXmlString)
	  val RequestListener = appXml \ "SignalHanlders" \ "RequestListener" 
	  val DescriptionNode = RequestListener \ "Description"
	  val URLNode = RequestListener \ "URL"
	  val AdapterNode = RequestListener \ "Adapter"
	  
	  <RequestListener type = {RequestListener \ "@type"} runat = {to}>
	  {DescriptionNode}
	  {URLNode}
	  {AdapterNode}
	  </RequestListener>
	}

}