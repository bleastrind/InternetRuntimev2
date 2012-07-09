package controllers;

import cn.edu.act.internetos.appmarket.service.*;
import play.*;
import play.cache.Cache;
import play.mvc.*;
import java.util.*;
import models.*;

public class AdminController extends Controller {

	public static String getAccessToken(){
		return Cache.get(session.getId() + "-token", String.class);
	}
	
    public static void welcome() {   
		List<App> applist = AppService.getAllApps();
        render("AdminService/welcome.html", applist);
		//return ok(AdminService.welcome.render(applist));
    }

    public static void editApp(App app){
        render("AdminService/editApp.html", app);
	   //return ok(AdminService.editApp.render(app));
    }

    public static void editAppSave(App app, String name, String information, String installUrl){
		AdminService.saveApp(app, name, information, installUrl);
        welcome();       
    }

    public static void deleteApp(App app, String appId){
		app.setId(appId);
		AdminService.deleteApp(app);
		welcome();
    }

    public static void addApp()
    {
		//return ok(AdminService.addApp.render());
    	Map<String,String> map = AdminService.appregister();
    	String id = map.get("id");
    	String secret = map.get("secret");
        render("AdminService/addApp.html",id,secret);
    }

    public static void addAppSave(String id,String name, String information, String installUrl,String email,String updated,String updateUrl,String secret)
    {
		AdminService.addAppSave(id,name, information, installUrl,email,updated,updateUrl,secret);
        welcome();
    }
}