package org.internetrt.sdk.util
import java.util.ArrayList


class AppXmlParser (xml:String){
  //val xmlFile = scala.xml.XML.loadFile("renrenApplication.txt");
  val xmlFile = scala.xml.XML.loadString(xml)
  
  def createApplication() =  {
     Application((xmlFile \ "Name").text, (xmlFile \ "AppID").text, getRequests()) 
  }
  
  
  def getUrl(signal:String): String= {
    var result = new String
    val RequestListener = xmlFile \ "SignalHanlders"
    RequestListener \ "RequestListener" foreach{(RequestListener)=>
      if(matchSignal((RequestListener\"Adapter"\"Signalname").text, signal) )
      {
    	  result = (RequestListener \ "URL").text
      }
    }
    return result
  }
  
  def matchSignal(p:String, Name:String):Boolean = p match {
   case Name => true
   case _ => false
 }
  
//  def getMap (signal:String): java.util.Map[String, String] = {
//      val maps = scala.collection.mutable.Map.empty[String, String];
//       val RequestListener = xmlFile \ "SignalHanlders"
//        RequestListener \ "RequestListener" foreach{(RequestListener)=>
//      if(matchSignal((RequestListener\"MatchRule"\"Signalname").text, signal) )
//      {
//    	  val Adapter = RequestListener \ "Adapter"
//    	  val converter = Adapter \ "converter"
//    	  converter \ "map" foreach{(map)=>
//    	   val fromParam = map \ "@from"
//    	   val toParam = map \ "@to"
//    	   maps += (fromParam.toString() -> toParam.toString());
//    	    }
//    	  }
//      }
//       return scala.collection.JavaConversions.asMap(maps);
//  }
  
  def getAppName(): String = {
		  (xmlFile \ "Name").text
  }
  
  def getRequests(): java.util.List[Signal] = 
  {
    val Signals = xmlFile \ "Signals" \ "Request" map{(Request)=>
      val Signalname = (Request \ "Signalname").text
      val Description = (Request \ "Description").text
      val Require = (Request \ "Require").text
      Signal(Signalname, Description, Require)
    }
    scala.collection.JavaConversions.asList[Signal](Signals)
  }
}