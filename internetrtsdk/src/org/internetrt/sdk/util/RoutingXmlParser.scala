package org.internetrt.sdk.util

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class RoutingXmlParser(xml:String)  {
 
     val xmlFile = scala.xml.XML.loadString(xml);

    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text
	  return from.toString();
    }
    
    def getTo(id:String = null): String = {
	  val signalListener = getListener(id);
	  val requestType = signalListener \ "@runat";
	  requestType.toString();
	}

	def getEventListenerIds(): java.util.List[String] = {
	  val ids = xmlFile \ "EventListener" map (node => node \ "@id" text)
	  scala.collection.JavaConversions.asList(ids)
	}
    
	def getReqType(id:String = null): String = {
	  
	  val RequestListener = getListener(id);
	  val requestType = RequestListener \ "@type";
	  requestType.toString();
	}
	
	def getReqUrl(id:String = null): String = {
	  val requestListener = getListener(id);
	  val requestUrl = (requestListener \ "url").text;
	  requestUrl.toString();
	}
	
	def getParamsFormat(id:String = null): java.util.Map[String,String] = {
	   val requestListener = getListener(id);
	   val params = requestListener \ "Adapter" \"params" \ "param" map{(param)=>
	      ( param \ "key" text )-> (param \ "value" text)
	   }
	   Map(params:_*);
	}
	
	def getHeadersFormat(id:String = null): java.util.Map[String,String] = {
	   val requestListener = getListener(id);
	   val params = requestListener \ "Adapter" \"headers" \ "header" map{(header)=>
	     ( header \ "key" text )-> (header \ "value" text)
	   }
	   Map(params:_*);
	}
	   
	   def getBodyFormat(id:String = null): java.util.Map[String,String] = {
	   val requestListener = getListener(id);
	   val params =requestListener \ "Adapter" \"body" map{(header)=>
	     ( header \ "key" text )-> (header \ "value" text)
	   }
	   Map(params:_*);
	}
	   
	   private def getRequestListener() = {
	     xmlFile\ "RequestListener" 
	   }
	   
	   private def getEventListener(id:String) = {
	     xmlFile\ "EventListener" filter (node => (node \ "@id" text) == id);
	   }
	   
	   private def getListener(id:String = null) = {
	     if( id == null )
	       getRequestListener();
	     else
	       getEventListener(id);	     
	   }
}
