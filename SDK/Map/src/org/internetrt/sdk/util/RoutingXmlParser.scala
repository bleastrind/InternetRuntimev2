package org.internetrt.sdk.util

class RoutingXmlParser(xml:String){
 
     val xmlFile = scala.xml.XML.loadString(xml);

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
	
	def getParamsFormat(): java.util.Map[String,String] = {
	   val map = scala.collection.mutable.Map.empty[String, String];
	   val params = xmlFile \ "RequestListener" \ "Adapter" \"params" \ "param" foreach{(param)=>
	   map += ((param \ "key").text-> (param \ "value").text)
	   }
	    return scala.collection.JavaConversions.asMap(map);
	}
	
	def getHeadersFormat(): java.util.Map[String,String] = {
	   val map = scala.collection.mutable.Map.empty[String, String];
	   val params = xmlFile\ "RequestListener" \ "Adapter" \"headers" \ "header" foreach{(header)=>
	   map += ((header \ "key").text-> (header \ "value").text)
	   }
	    return scala.collection.JavaConversions.asMap(map);
	}
	   
	   def getBodyFormat(): java.util.Map[String,String] = {
	   val map = scala.collection.mutable.Map.empty[String, String];
	   val params = xmlFile \ "RequestListener" \ "Adapter" \"body" foreach{(body)=>
	   map += ((body \ "key").text-> (body \ "value").text)
	   }
	    return scala.collection.JavaConversions.asMap(map);
	}
}
