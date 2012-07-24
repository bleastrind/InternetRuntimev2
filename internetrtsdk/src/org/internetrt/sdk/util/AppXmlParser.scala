package org.internetrt.sdk.util
import java.util.ArrayList
import org.internetrt.sdk.exceptions.FormatErrorException

class AppXmlParser (xml:String){
  val xmlFile = scala.xml.XML.loadString(xml)
  
  def getAppID():String = {
    val   appID= (xmlFile \ "AppID").text
      throwFormatException(appID,"appID")
    return appID
  }
  
  def throwFormatException(term: String, description: String){
     if (term == null || term == "")
      {
	    throw new FormatErrorException("Error in App Xml: "+description+" not set!");
      }
  }
  
  def createApplication() =  {
      val appName = getAppName()
      val   appID= (xmlFile \ "AppID").text
      throwFormatException(appID,"appID")
     Application(appName,appID , getSignals()) 
  }
  
  def getUrl(signal:String): String= {
    assert(signal != null&&signal!="")
    var result = new String
    val RequestListener = xmlFile \ "SignalHanlders"
    RequestListener \ "RequestListener" foreach{(RequestListener)=>
      val signalName = (RequestListener\"Adapter"\"Signalname").text
      throwFormatException(signalName,"RequestListener.Adapter.Signalname")
      if(signalName == signal )
      {
    	  result = (RequestListener \ "URL").text
    	  throwFormatException(result,"RequestListener.URL")
      }
    }
    return result
  }
 
  def getAppName(): String = {
		  (xmlFile \ "Name").text
  }
  
  private def _getSignals()= 
  {
    val RequestSignals = xmlFile \ "Signals" \ "Request" map{(Request)=>
      val Signalname = (Request \ "Signalname").text
       throwFormatException(Signalname,"Request.Signalname")
      val Description = (Request \ "Description").text
      val Require = (Request \ "Require").text
      val from = (Request \ "@runat").text
      throwFormatException(from,"Request.@runat")
      val AppID = (xmlFile \ "AppID").text
      throwFormatException(AppID,"AppID")
      val From = if( from == "" ) AppID else from
      val Type = "Request"
      Signal(Signalname, Description, Require, From, Type)
    }
    val EventSignals = xmlFile \ "Signals" \ "Event" map{(Request)=>
      val Signalname = (Request \ "Signalname").text
       throwFormatException(Signalname,"Request.Signalname")
      val Description = (Request \ "Description").text
      val Require = (Request \ "Require").text
      val from = (Request \ "@runat").text
      throwFormatException(from,"Request.@runat")
       val AppID = (xmlFile \ "AppID").text
      throwFormatException(AppID,"AppID")
      val From = if( from == "" ) AppID else from
      val Type = "Event"
      Signal(Signalname, Description, Require, From, Type)
    }
    (RequestSignals ++ EventSignals)
  }
  
  def getSignals():java.util.List[Signal]={
    scala.collection.JavaConversions.asList[Signal](_getSignals) 
  }
  
  def getMatchedRequestSignals(config:ListenerConfig) = {
    val signalName =  (config.node \\ "Adapter" \ "Signalname").text
    throwFormatException(signalName,"Adapter.Signalname")
    val Signals = _getSignals() filter(
      signal =>signalName == signal.name
   )
    scala.collection.JavaConversions.asList[Signal](Signals)
  }
  
  def getMatchedListeners(signalName:String) = {
	 assert(signalName != null&&signalName!="")
     val appName = xmlFile \ "Name" text;
     scala.collection.JavaConversions.asList[DescribedListenerConfig](
        xmlFile \ "SignalHanlders" \ "RequestListener" ++ xmlFile \ "SignalHanlders" \ "EventListener" 
           map(signalListener => DescribedListenerConfig(appName,signalListener \ "Description" text,signalListener))
	  	   filter ( config =>  (config.node \\ "Adapter" \ "Signalname").text == (signalName))
	  	)
  }
  
  def getListeners() = {
     val appName = xmlFile \ "Name" text;
     scala.collection.JavaConversions.asList[DescribedListenerConfig](
      xmlFile \ "SignalHanlders" \ "RequestListener" ++ xmlFile \ "SignalHanlders" \ "EventListener" 
           map(signalListener => DescribedListenerConfig(appName,signalListener \ "Description" text,signalListener))
	  	)
  }
  
  def getExceptedSignals() = {
     scala.collection.JavaConversions.asList[String](
        xmlFile \"SignalHanlders" \\ "Adapter" \ "Signalname" map (node => node text)
     )
  }
  
  def getAppIdBaseRunat(runatAppId : String) :String = {
    assert(runatAppId!=null && runatAppId!="")
    var result = new String
	  xmlFile \  "SignalHanlders"\ "RequestListener" foreach{
      (requestListener)=> 
        val runat = (requestListener \ "@runat").text
        throwFormatException(runat, "requestListener.@runat")
        if( runat == runatAppId){
           result =   (xmlFile \ "AppID").text
            throwFormatException(result, "AppID")
        }
    }
    return result
  }
  
  def getSignalNameBaseRunat(runatAppId : String) = {
     assert(runatAppId!=null && runatAppId!="")
    scala.collection.JavaConversions.asList[String]((xmlFile \ "SignalHanlders"\ "RequestListener" map{
	  	  (requestListener) =>
	  	      val runat = (requestListener \ "@runat").text
        throwFormatException(runat, "requestListener.@runat")
	  	    if(  runat == runatAppId)
	  	    {
	  	      throwFormatException( (requestListener \ "Adapter" \ "Signalname").text,"requestListener.Adapter.Signalname")
	  	        (requestListener \ "Adapter" \ "Signalname").text
	  	    }
	  	    else
	  	    {
	  	      null
	  	    }
	  	    
	  	}) filter (n => n != null))
  }
  
}