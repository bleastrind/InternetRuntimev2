package org.internetrt.sdk.util
import scala.xml.XML$
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
case class ListenerConfig(node:scala.xml.Node){}
case class GlobalData(map:Map[String,String]){}
case class DataAdapter(node:scala.xml.Node){}

object RoutingXmlParser{
  
	def ROUTING_INSTANCE_ID_KEY = "r_ID"
  
    def getTo(listener:ListenerConfig ): String = {
	  val signalListener = listener.node;
	  val requestType = signalListener \ "@runat";
	  requestType.toString();
	}
    
	
	def getReqType(listener:ListenerConfig): String = {
	  
	  val signalListener = listener.node;
	  val requestType = signalListener \ "@type";
	  requestType.toString();

	}
	
	def getReqUrl(listener:ListenerConfig): String = {
	  val signalListener = listener.node;
	  val requestUrl = (signalListener \ "URL").text;
	  requestUrl.toString();
	}
	
	def getParamsAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   paramsAdapter(listener)
	}	
	
	def paramsAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params = signalListener \ "Adapter" \"params" \ "param" map{(param)=>
	      ( param \ "key" text )-> DataAdapter(param \ "value" head)
	   }
	   Map(params:_*);
	}
	
	def getHeadersAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	  headersAdapter(listener)
	}
	def headersAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params = signalListener \ "Adapter" \"headers" \ "header" map{(header)=>
	     ( header \ "key" text )-> DataAdapter(header \ "value" head)
	   }
	   Map(params:_*);
	} 
	def getBodyAdapter(listener:ListenerConfig): java.util.Map[String,DataAdapter] = {
	   bodyAdapter(listener)
	}	     
	   
	def bodyAdapter(listener:ListenerConfig): Map[String,DataAdapter] = {
	   val signalListener = listener.node;
	   val params =signalListener \ "Adapter" \"body" map{(header)=>
	     ( header \ "key" text )-> DataAdapter(header \ "value" head)
	   }
	   Map(params:_*);
	}	  
}

class RoutingXmlParser(xml:String)  {
 
     val xmlFile = scala.xml.XML.loadString(xml);

     def getExtData():GlobalData = {
       GlobalData(Map(RoutingXmlParser.ROUTING_INSTANCE_ID_KEY -> (xmlFile \ "id" head).text ))
     }

    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text
	  return from.toString();
    }
    
    def getTo(): String = {
	  RoutingXmlParser.getTo(getRequestListener)
	}
	
	def getReqType(): String = {
	  RoutingXmlParser.getReqType(getRequestListener)
	}
	
	def getReqUrl(): String = {
	  RoutingXmlParser.getReqUrl(getRequestListener)
	}
	
	def getParamsAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getParamsAdapter(getRequestListener)
	}
	
	def getHeadersAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getHeadersAdapter(getRequestListener)
	}
	   
	def getBodyAdapter(): java.util.Map[String,DataAdapter] = {
	   RoutingXmlParser.getBodyAdapter(getRequestListener)
	}	
	
	def paramsAdapter() = {
	   RoutingXmlParser.paramsAdapter(getRequestListener)
	}
	
	def headersAdapter() = {
	   RoutingXmlParser.headersAdapter(getRequestListener)
	}
	   
	def bodyAdapter()= {
	   RoutingXmlParser.bodyAdapter(getRequestListener)
	}


	   def getRequestListener():ListenerConfig = {
	   try{
	     ListenerConfig(xmlFile\ "RequestListener" head)
	     }catch{
	        case _ => null
	     }
	   }
	   
	   def getEventListeners():java.util.List[ListenerConfig] = {
	     val listeners = xmlFile\ "EventListener" map (node => ListenerConfig(node));
	     scala.collection.JavaConversions.seqAsJavaList(listeners)
	   }

	
	  def getRoutingInstanceId () ={

	    ( xmlFile \ "id").text
	   }
}
