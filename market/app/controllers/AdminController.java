package controllers;

import cn.edu.act.internetos.appmarket.service.*;
import play.*;
import play.cache.Cache;
import play.mvc.*;
import java.util.*;

import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.DescribedListenerConfig;
import org.internetrt.sdk.util.Signal;

import models.*;

public class AdminController extends Controller {

	public static String getAccessToken(){
		return session.get("token");
	}
	
    public static void welcome() {   
		List<App> applist = AppService.getAllApps();
		List<App> apps = new ArrayList();
		//Boolean flag = AppService.market(getAccessToken());
		for (App app:applist){
			AppXmlParser parser = new AppXmlParser(app.getInformation());
			List<Signal> signals = parser.getSignals();
			app.setDecription("能够发出的信号:");
			for (Signal signal:signals){
				app.setDecription(app.getDecription()+signal.name()+" ");
			}
			app.setDecription(app.getDecription()+"能够接收的信号:");
			List<DescribedListenerConfig> listeners = parser.getListeners();
			
			for (DescribedListenerConfig listen:listeners){
				app.setDecription(app.getDecription()+listen.description()+" ");
			}
		}
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
    
    public static void addAppSave(String id,String name, String AccessRequest, String installUrl,String email,String updated,String updateUrl,String secret)
    {
    	System.out.println("listener"+Controller.request.params.getAll("ListenerSignalname0"));
    	String information = "<Application><Name>"+name+"</Name><AppID>"+id +
    			"</AppID><AccessRequests><AccessRequest>"+AccessRequest+"</AccessRequest></AccessRequests></Application>";
    	System.out.println("id:"+id+"secret:"+secret+"information:"+information);
    	//AdminService.addAppSave(id,name, information, installUrl,email,updated,updateUrl,secret);
        welcome();
    }
    
    public static void addAppXml()
    {
		//return ok(AdminService.addApp.render());
    	Map<String,String> map = AdminService.appregister();
    	String id = map.get("id");
    	String secret = map.get("secret");
        render("AdminService/addAppXml.html",id,secret);
    }
    
    public static void addAppSaveXml(String id, String information,String secret,String email,String installUrl,String updateurl)
    {
    	AppXmlParser parser = new AppXmlParser(information);
    	if (!"".equals(updateurl)&&updateurl!=null) 
    		AdminService.addAppSave(id,parser.getAppName(), information, installUrl,email,"true",updateurl,secret);
    	else 
    		AdminService.addAppSave(id,parser.getAppName(), information, installUrl,email,"false","",secret);
    	welcome();
    }
}