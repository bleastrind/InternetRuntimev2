package org.internetrt.driver
import play.api.mvc._
import org.internetrt.SiteInternetRuntime
import org.internetrt._;

object AppRegister extends Controller {

	def register=Action{
	  request=>
		val email = request.queryString.get("email").getOrElse(Seq.empty).head;
		val response = SiteInternetRuntime.registerApp(email);
		Ok("{id:\""+response._1 + "\",secret:\""+response._2+"\"}")
	}
	
	def queryApp() = Action {
    request =>
      {
        val appID = request.queryString.get("appID").get.head;
        val resultxml = SiteUserInterface.queryApp(appID).xml;
        if (request.queryString.get("format") match {
          case Some(list) => list.head == "json"
          case _ => false
        }) {
          import net.liftweb.json._;
          import net.liftweb.json.JsonAST._;
          Ok(Printer.pretty(JsonAST.render(Xml.toJson(resultxml))))
        } else
          Ok(resultxml)
      }
  }
}