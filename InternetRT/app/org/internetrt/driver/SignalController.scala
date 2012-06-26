package org.internetrt.driver
import play.api.mvc._
import org.internetrt.SiteInternetRuntime
import org.internetrt._;

object SignalController extends Controller {
	def init(signalname:String,Type:String)=Action{
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
	
	def initOption(signalname:String, Type:String)= Action{
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