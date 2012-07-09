package controllers;

import cn.edu.act.internetos.appmarket.service.*;
import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class AdminController extends Controller {

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
        render("AdminService/addApp.html");
    }

    public static void addAppSave(String name, String information, String installUrl)
    {
		AdminService.addAppSave(name, information, installUrl);
        welcome();
    }
}