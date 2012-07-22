package controllers;

import cn.edu.act.internetos.appmarket.service.*;
import play.*;
import play.libs.*;
import play.mvc.*;
import play.cache.*;
import play.mvc.Http.*;
import java.util.*;
import models.*;

import java.io.IOException;
import java.lang.*;

import org.apache.commons.httpclient.HttpException;
import org.internetrt.sdk.*;
import org.internetrt.sdk.util.*;

import config.properties;

public class AppController extends Controller {

	public static String getAccessToken() {
		System.out.println("[AppController : getAccessToken]: "+session.get("token"));
		return session.get("token");
	}

	@Before(only = { "listAllApp", "deleteUserApp", "addUserAppSave" })
	public static void checkUser() {
		System.out.println("[AppController : checkUser]: "+"checkUser");
		String token = getAccessToken();
		if (token == null) {
			System.out.println("[AppController : checkUser]: "+properties.irt.getAuthCodeUrl());
			Controller.redirect(properties.irt.getAuthCodeUrl());
		}
	}

	public static void index() {
		render("AppService/index.html");
	}

	public static void listAllApps() {
		List<App> applist = AppService.getAllApps();

		//Boolean flag = AppService.market(getAccessToken());
		
		for (App app:applist){
			AppXmlParser parser = new AppXmlParser(app.getInformation());
		}
		Boolean flag = false;
//		if (getAccessToken()!=null)
//			flag = AppService.market(getAccessToken());
//		else 
//			Controller.redirect(properties.irt.getAuthCodeUrl());
		render("AppService/listAllApps.html", applist, flag);
	}

	public static void listAllApp() {
		String token = getAccessToken();
		List<App> applist = AppService.getUserApps(token);
		// List<AppConfig> configlist = AppService.getAllConfig(user);
		// TODO:nick name
		render("AppService/listAllApp.html", applist, token);
	}

	public static void addUserApp() {
		List<App> applist = AppService.getAllApps();
		render("AppService/addUserApp.html", applist);
	}

	public static void addUserAppSave(String id, String name,
			String information, String installUrl, String updated,
			String updateUrl, String secret) {
		String token = getAccessToken();
		System.out.println("[AppController : addUserAppSave]: "+"AppID"+id);
		
		System.out.println("[AppController : addUserAppSave]: "+"accessToken"+token);
		App app = new App(id, name, information, installUrl, updated,
				updateUrl, secret);
		if (AppService.addUserApp(app, token)){
			RoutingRecomController.index(id);}
		else
			render("AppService/addfail.html");
	}

	public static void deleteUserApp(App app) {
		String token = getAccessToken();
		AppService.deleteUserApp(app, token);
		render("AppService/deletesuccess.html");
	}

	public static void listApp(App app) {
		render("AppService/listApp.html", app);
	}

	public static void installConfig(String userId, String appId, String config) {
		AppService.setConfig(userId, appId, config);
		render("AppService/SetConfigSuccess.html", request.body);
	}

	public static void installMarket() {
		String token = getAccessToken();
		// AppService.addUserApp(app, token);
	}

	/*
	 * public static void installConfigXML(){
	 * AppService.setConfigXML(request.body); DocumentBuilderFactory factory =
	 * DocumentBuilderFactory.newInstance(); DocumentBuilder builder =
	 * factory.newDocumentBuilder(); Document document =
	 * builder.parse(requestBody); String config = serialize(document); }
	 */

}