package org.internetrt.sdk.util
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.internetrt.sdk.exceptions.FormatErrorException

object RoutingInstanceXmlParser
{  
	def ROUTING_INSTANCE_ID_KEY = "r_ID"
	  
	def HASHKEY = "hashdata"

	def getTo(listener:ListenerConfig ): String =  RoutingXmlParser.getTo(listener)
	
	def getListenerType(listener:ListenerConfig): String = RoutingXmlParser.getListenerType(listener)
	
	def getListenerUrl(listener:ListenerConfig): String = RoutingXmlParser.getListenerUrl(listener)
	
	def getParamsAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   paramsAdapter(listener)
	}		
	
	def paramsAdapter(listener:ListenerConfig): Map[String,DataAdapter] = RoutingXmlParser.paramsAdapter(listener)
	
	def getHeadersAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	  headersAdapter(listener)
	}
	
	def headersAdapter(listener:ListenerConfig): Map[String,DataAdapter] = RoutingXmlParser.headersAdapter(listener)
	
    def getAnchorAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	  anchorAdapter(listener)
	}
	
	def anchorAdapter(listener:ListenerConfig): Map[String,DataAdapter] = RoutingXmlParser.anchorAdapter(listener)
	
	def getBodyAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   bodyAdapter(listener)
	}
	
	def bodyAdapter(listener:ListenerConfig): Map[String,DataAdapter] = RoutingXmlParser.bodyAdapter(listener)
	
	def getRequiredFormats(listener:ListenerConfig):java.util.List[ListenerDataFormat] = RoutingXmlParser.getRequiredFormats(listener)
	
	
}
class RoutingInstanceXmlParser(xml:String) 
{
	
    val xmlFile = scala.xml.XML.loadString(xml);
  
	def getExtData():GlobalData = {
	  GlobalData(Map(RoutingInstanceXmlParser.ROUTING_INSTANCE_ID_KEY -> (xmlFile \ "id" head).text ))
	}
	
	def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text;
	  FormatErrorException.checkNonEmptyTerm(from,"signal.from")
	  return from.toString();
    }
	
	def getParamsAdapter(): java.util.Map[String,DataAdapter] =  RoutingInstanceXmlParser.getParamsAdapter(getRequestListener)
	
	def getHeadersAdapter(): java.util.Map[String,DataAdapter] = RoutingInstanceXmlParser.getHeadersAdapter(getRequestListener)
	
	def getBodyAdapter(): java.util.Map[String,DataAdapter] = RoutingInstanceXmlParser.getBodyAdapter(getRequestListener)
	
	def paramsAdapter() = RoutingInstanceXmlParser.paramsAdapter(getRequestListener)
	
	def headersAdapter() = RoutingInstanceXmlParser.headersAdapter(getRequestListener)
	
	def bodyAdapter() = RoutingInstanceXmlParser.bodyAdapter(getRequestListener)
	
	
	//rxp is short for RoutingXmlParser
	var rxp = new RoutingXmlParser(xml);
	
	def getRequestListener(): ListenerConfig = rxp.getRequestListener()
	
	def getEventListeners(): java.util.List[ListenerConfig] = rxp.getEventListeners()
	
	
	//Instance id , special one
	def getRoutingInstanceId () ={
	  val routingInstanceId = ( xmlFile \ "id").text
	  FormatErrorException.checkNonEmptyTerm(routingInstanceId, "routingInstanceId")
	  routingInstanceId
	}
}