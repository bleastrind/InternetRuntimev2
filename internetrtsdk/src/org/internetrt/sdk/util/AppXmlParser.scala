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
  
  def getMatchedListeners(signalName:String) = {
     scala.collection.JavaConversions.asList[ListenerConfig](
        xmlFile \ "SignalHanlders" 
           map(signalListener => ListenerConfig(signalListener))
	  	   filter ( config =>  (config.node \\ "Adapter" \ "Signalname").text == signalName)
	  	)
  }
  
  def getListeners() = {
     scala.collection.JavaConversions.asList[ListenerConfig](
        xmlFile \ "SignalHanlders" 
           map(signalListener => ListenerConfig(signalListener))
	  	)
  }
  
  def getExceptedSignals() = {
     scala.collection.JavaConversions.asList[String](
        xmlFile \"SignalHanlders" \\ "Adapter" \ "Signalname" map (node => node text)
     )
  }
  
  def getAppIdBaseRunat(runatAppId : String) :String = {
    
    var result = new String
	  xmlFile \  "SignalHanlders"\ "RequestListener" foreach{
      (requestListener)=> 
        if((requestListener \ "@runat").text == runatAppId){
           result =   (xmlFile \ "AppID").text
        }
    }
    return result
  }
  
  def getSignalNameBaseRunat(runatAppId : String) = {
    scala.collection.JavaConversions.asList[String]((xmlFile \ "SignalHanlders"\ "RequestListener" map{
	  	  (requestListener) =>
	  	    if(  (requestListener \ "@runat").text == runatAppId)
	  	    {
	  	        (requestListener \ "Adapter" \ "Signalname").text
	  	    }
	  	    else
	  	    {
	  	      null
	  	    }
	  	    
	  	}) filter (n => n != null))
  }
  
}