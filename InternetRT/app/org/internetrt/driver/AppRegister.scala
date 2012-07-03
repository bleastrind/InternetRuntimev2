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
}