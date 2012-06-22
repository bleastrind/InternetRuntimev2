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
}