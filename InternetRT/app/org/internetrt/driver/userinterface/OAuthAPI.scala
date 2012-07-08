package org.internetrt.driver.userinterface
import play.api.mvc.Controller
import play.api.mvc.Action
import org.internetrt.CONSTS
import org.internetrt.SiteInternetRuntime
import org.internetrt.SiteUserInterface
import play.api.templates.Html

object OAuthAPI extends Controller{
	def authorize()=Action{
	  request=>
	    
	    System.out.println("OAuth:"+request.session)
	    
		request.session.get(CONSTS.SESSIONUID) match{
		  case Some(userID)=>{
//		    if(request.method == "GET"){
				val appID = request.queryString.get("appID").get.head;
				val redirect_uri = request.queryString.get("redirect_uri").get.head;
				val code= SiteUserInterface.getAuthcodeForServerFlow(appID,userID,redirect_uri);
				
				if(code != null){
					System.out.println("Redirect URL:"+redirect_uri);
					Redirect(redirect_uri+"?code="+code);
					//Ok(Html("<a href='http://localhost:9001'>back to old url</a>"))
				}
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
		    Ok(views.html.login(request.uri))
		  }
		}
	}
}