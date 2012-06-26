package org.internetrt.sdk.util

class RoutingXmlParser  {
 
     val xmlFile = scala.xml.XML.loadFile("Routing.xml");

    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text
	  return from.toString();
    }
    
    def getTo(): String = {
	  val signalListener = xmlFile \ "RequestListener"
	  val requestType = signalListener \ "@runat";
	  return requestType.toString();
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
	
	def getReqType(): String = {
	  val RequestListener = xmlFile \ "RequestListener"
	  val requestType = RequestListener \ "@type";
	  return requestType.toString();
	}
	
	def getReqUrl(): String = {
	  val RequestListener = xmlFile \ "RequestListener";
	  val requestUrl = (RequestListener \ "url").text;
	  return requestUrl.toString();
	}
}
