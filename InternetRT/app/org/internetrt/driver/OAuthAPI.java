package org.internetrt.driver;


import org.internetrt.*;
import org.internetrt.core.security.*;
import play.mvc.Result;
import play.mvc.Controller;

public class OAuthAPI extends Controller {
	

	/**
	 * The API for global workflow type auth
	 * @param appSecret
	 * @param workflowID
	 * @return
	 */

	public static Result authorizebyworkflow(){
		String appid = request().queryString().get("appID")[0];
		String secret = request().queryString().get("appSecret")[0];
		String rid = request().queryString().get("rid")[0];
		String code = SiteInternetRuntime.getAuthcodeForActionFlow(appid,secret,rid);
		return ok("{code:\""+code+"\"}");
	}
	
	public static Result token(){
		String authtoken = request().queryString().get("authtoken")[0];
		String appID = request().queryString().get("appID")[0];
		String appSecret = request().queryString().get("appSecret")[0];
		AccessToken accesstoken = SiteInternetRuntime.getAccessTokenByAuthtoken(appID, authtoken, appSecret);
		return ok("{access_token:\""+accesstoken.value()+
				"\",expires_in: \""+accesstoken.expire()+"\""+
				",refresh_token=\""+accesstoken.refresh()+"\"}");
	}
	
	public static Result getUserIdByToken(String accessToken){
		String userId = SiteInternetRuntime.getUserIDByAccessToken(accessToken);
		return ok("{user_id:\""+userId+"\"}");
	}
	
}
