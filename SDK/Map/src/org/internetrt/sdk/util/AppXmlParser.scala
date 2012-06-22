package org.internetrt.sdk.util

class AppXmlParser( xml:String) {
 
     val xmlFile = scala.xml.XML.loadString(xml) 

    def getFrom(): String = {
      val signal = xmlFile \ "signal";
	  val from = (signal \ "from").text
	  println(from);
	  return from.toString();
    }
    
    def getTo(): String = {
	  val signalListener = xmlFile \ "signalListener"
	  val requestType = signalListener \ "@runat";
	  return requestType.toString();
	}
    
	def getMap() : java.util.Map[String, String]= {
	  val map = scala.collection.mutable.Map.empty[String, String];
	  val adapter = xmlFile \ "adapter"
	  val mapper = adapter \ "mapper"
	  val key = mapper \ "key"
	  val fromParam = key \ "@from"
	  val toParam = key \ "@to"
	  map += (fromParam.toString() -> toParam.toString());
	  return scala.collection.JavaConversions.asMap(map);
	}
	
	def getReqType(): String = {
	  val signalListener = xmlFile \ "signalListener"
	  val requestType = signalListener \ "@type";
	  return requestType.toString();
	}
	
	def getReqUrl(): String = {
	  val signalListener = xmlFile \ "signalListener";
	  val requestUrl = (signalListener \ "url").text;
	  return requestUrl.toString();
	}
}
