package org.internetrt.driver
import play.api.mvc._
import org.internetrt.SiteInternetRuntime
import org.internetrt._;

object SignalController extends Controller {

	def init(signalname:String)=Action{
	  request=>
	  val response = SiteInternetRuntime.initActionFromThirdPart(
	        request.queryString.get(CONSTS.ACCESSTOKEN).getOrElse(Seq.empty).head,
	        signalname,
	        request.queryString,
	        request.queryString.mapValues( seq => seq.headOption.getOrElse("")));

	  Ok(response.getResponse)
	}
	
	def initOption(signalname:String)= Action{
	   request=>
	  val response = SiteInternetRuntime.initActionOptionsFromThirdPart(
	        request.queryString.get(CONSTS.ACCESSTOKEN).getOrElse(Seq.empty).head,
	        signalname,
	        request.queryString,
	        request.queryString.mapValues( seq => seq.headOption.getOrElse("")));

	  val resultxml = scala.xml.Utility.trim(
	    <Options>
		  {scala.xml.NodeSeq.fromSeq( 
		    response.map(entry =>
		    <entry><key>{entry._1}</key>
		    	<value>{entry._2}</value>
		    </entry>).toSeq)
		  }	    
		</Options>)
	  
	  if(request.queryString.get("format") match {
	    case Some(list) => list.head == "json"
	    case _ => false
	  }){
	    import net.liftweb.json._;
	    import net.liftweb.json.JsonAST._;
	    Ok(Printer.pretty(JsonAST.render(Xml.toJson(resultxml))))
	  }else
	     Ok(resultxml)
	}

	def registerSignal(signalname:String)= Action{
	  request=>
	    val map = request.body.asFormUrlEncoded
	    val first = map.get("xml")
	    Ok(SiteInternetRuntime.registerSignal(signalname,first.head).toString()).withHeaders("Access-Control-Allow-Origin" -> "*");
	}
	
	def getSignalDef(signalname:String)= Action{
	    Ok(SiteInternetRuntime.getSignalDefination(signalname));
	}
}