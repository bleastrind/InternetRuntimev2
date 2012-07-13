package org.internetrt.driver.userinterface
import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.SiteInternetRuntime
import org.internetrt.SiteUserInterface
import org.internetrt.sdk.util.ListenerRequestGenerator
import scala.collection.JavaConversions._;
import org.internetrt.sdk.util.RoutingXmlParser

object SignalAPI extends Controller{
	def init(signalname:String) = Action{
	  request =>
	  val response = SiteUserInterface.initActionFromUserinterface(
	        request.session.get(CONSTS.SESSIONUID).get,
	        signalname,
	        request.queryString,
	        request.queryString.mapValues( seq => seq.headOption.getOrElse("")));
	  
	  if(request.queryString.get("format") match {
	    case Some(list) => list.head == "browserredirect"
	    case _ => false
	  }){
	      val urlGenerator = new ListenerRequestGenerator(new RoutingXmlParser(response.getResponse))
	      //TODO the data should be Map[String,Map[String]]
	      val url = urlGenerator.generateSignalListenerUrl(request.queryString.mapValues( seq => seq.headOption.getOrElse("")),null)
		  Redirect(url)
	  }else
		  Ok(response.getResponse)
	}
	
	def initOption(signalname:String)= Action{
	   request=>
	  val response = SiteUserInterface.initActionOptionsFromUserinterface(//TODO  move to interface
	        request.session.get(CONSTS.SESSIONUID).get,
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
}