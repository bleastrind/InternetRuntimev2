package org.internetrt.sdk.util
import scala.xml.XML$
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
case class ListenerConfig(node:scala.xml.Node){}

class RoutingXmlParser(xml:String)  {
 
     val xmlFile = scala.xml.XML.loadString(xml);


    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text
	  return from.toString();
    }
    
    def getTo(listener:ListenerConfig = null): String = {
	  val signalListener = if (listener == null) getRequestListener.node else listener.node;
	  val requestType = signalListener \ "@runat";
	  requestType.toString();
	}
    

//	def getMap() : java.util.Map[String, String]= {
//	  val map = scala.collection.mutable.Map.empty[String, String];
//	  val adapter = xmlFile \ "Adapter"
//	  adapter \ "mapper" foreach{(mapper)=>
//	    val key = mapper \ "key"
//	   val fromParam = key \ "@from"
//	   val toParam = key \ "@to"
//	   map += (fromParam.toString() -> toParam.toString());
//	  }
//	  return scala.collection.JavaConversions.asMap(map);
//	}
	
	
	def getReqType(listener:ListenerConfig = null): String = {
	  
	  val signalListener = if (listener == null) getRequestListener.node else listener.node;
	  val requestType = signalListener \ "@type";
	  requestType.toString();

	}
	
	def getReqUrl(listener:ListenerConfig = null): String = {
	  val signalListener = if (listener == null) getRequestListener.node else listener.node;
	  val requestUrl = (signalListener \ "url").text;
	  requestUrl.toString();
	}
	
	def getParamsFormat(listener:ListenerConfig = null): java.util.Map[String,String] = {
	   val signalListener = if (listener == null) getRequestListener.node else listener.node;
	   val params = signalListener \ "Adapter" \"params" \ "param" map{(param)=>
	      ( param \ "key" text )-> (param \ "value" text)
	   }
	   Map(params:_*);
	}
	
	def getHeadersFormat(listener:ListenerConfig = null): java.util.Map[String,String] = {
	   val signalListener = if (listener == null) getRequestListener.node else listener.node;
	   val params = signalListener \ "Adapter" \"headers" \ "header" map{(header)=>
	     ( header \ "key" text )-> (header \ "value" text)
	   }
	   Map(params:_*);
	}
	   
	   def getBodyFormat(listener:ListenerConfig = null): java.util.Map[String,String] = {
	   val signalListener = if (listener == null) getRequestListener.node else listener.node;
	   val params =signalListener \ "Adapter" \"body" map{(header)=>
	     ( header \ "key" text )-> (header \ "value" text)
	   }
	   Map(params:_*);
	}


	   def getRequestListener() = {
	     ListenerConfig(xmlFile\ "RequestListener" head)
	   }
	   
	   def getEventListeners():java.util.List[ListenerConfig] = {
	     val listeners = xmlFile\ "EventListener" map (node => ListenerConfig(node));
	     scala.collection.JavaConversions.seqAsJavaList(listeners)
	   }

	
	  def getRoutingInstanceId () ={

	    ( xmlFile \ "id").text
	   }
}
