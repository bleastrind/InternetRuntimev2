package org.internetrt.driver.userinterface
import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.SiteInternetRuntime
import org.internetrt.SiteUserInterface

object OAuthAPI extends Controller{
	def authorize()=Action{
	  request=>
		request.session.get(CONSTS.SESSIONUID) match{
		  case Some(userID)=>{
//		    if(request.method == "GET"){
				val appID = request.queryString.get("appID").get.head;
				val redirect_uri = request.queryString.get("redirect_uri").get.head;
				val code= SiteUserInterface.getAuthcodeForServerFlow(appID,userID,redirect_uri);
				
				if(code != null)
					Redirect(redirect_uri+"?code="+code);
				else
				  Ok(views.html.auth())
//		    }
//		    else if(request.method == "POST"){
//		       val appID = request.body.asFormUrlEncoded.get.get("appID").head;
//		       val redirect_uri = request.body.asFormUrlEncoded.get.get("redirect_uri").head;
//		       val accessRequests = request.body.asFormUrlEncoded.get.get("accessRequests").head;
//		    }		    
		  }
		  case None =>{
		    Ok(views.html.login())
		  }
		}
	}
}