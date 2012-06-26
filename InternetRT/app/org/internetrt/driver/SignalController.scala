package org.internetrt.driver
import play.api.mvc._
import org.internetrt.SiteInternetRuntime
import org.internetrt._;

object SignalController extends Controller {

	def initfromthirdpart(signalname:String) = init(signalname,"thirdpart")
	
	def initfromclient(signalname:String) = init(signalname,"client")
	
	private def init(signalname:String,Type:String)=Action{
	  request=>
	  val response = Type match{
	    case "thirdpart" => SiteInternetRuntime.initActionFromThirdPart(
	        request.queryString.get(CONSTS.ACCESSTOKEN).getOrElse(Seq.empty).head,
	        signalname,
	        request.queryString,
	        null);
	    case "client" => SiteInternetRuntime.initActionFromUserinterface(
	        request.session.get(CONSTS.SESSIONUID).get,
	        signalname,
	        request.queryString,
	        null);
	        
	  }
	  Ok(response.getResponse)
	}
	
	def initOptionfromthirdpart(signalname:String) = initOption(signalname,"thirdpart")
	
	def initOptionfromclient(signalname:String) = initOption(signalname,"client")
	
	private def initOption(signalname:String, Type:String)= Action{
	   request=>
	  val response = Type match{
	    case "thirdpart" => SiteInternetRuntime.initActionOptionsFromThirdPart(
	        request.queryString.get(CONSTS.ACCESSTOKEN).getOrElse(Seq.empty).head,
	        signalname,
	        request.queryString,
	        null);
	    case "client" => SiteInternetRuntime.initActionOptionsFromUserinterface(
	        request.session.get(CONSTS.SESSIONUID).get,
	        signalname,
	        request.queryString,
	        null);
	        
	  }
	    
	  val resultxml = 
	    <Options>
		  {scala.xml.NodeSeq.fromSeq( 
		    response.map(entry =>
		    <entry><key>{entry._1}</key>
		    	<value>{entry._2}</value>
		    </entry>).toSeq)
		  }	    
		</Options>
	  
	  if(request.queryString.get("format") match {
	    case Some(list) => list.head == "json"
	    case _ => false
	  }){
	    Ok(net.liftweb.json.Xml.toJson(resultxml).toString())
	  }else
	     Ok(resultxml)
	}

	def registerSignal(signalname:String)= Action{
	  request=>
	    Ok(SiteInternetRuntime.registerSignal(signalname,request.body.asXml.toString()).toString());
	}
	
	def getSignalDef(signalname:String)= Action{
	    Ok(SiteInternetRuntime.getSignalDefination(signalname));
	}
}