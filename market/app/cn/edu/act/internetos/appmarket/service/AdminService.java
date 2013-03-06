package cn.edu.act.internetos.appmarket.service;

import java.util.*;

import config.properties;
import models.*;

public class AdminService{

    public static void saveApp(App app, String name, String information, String installUrl){
		AppDao appdao = new AppDao();
		app.setName(name);
		app.setInformation(information);
		app.setInstallUrl(installUrl);
		appdao.save(app);        
    }

    public static void deleteApp(App app){
		AppDao appdao = new AppDao();
		appdao.delete(app);
    }
    
    public static Map<String,String> appregister()
    {
    	return properties.irt.appregister("123");
    }

    public static void addAppSave(String id,String name, String information, String installUrl,String email,String updated,String updateUrl,String secret, String logourl)
    {
		AppDao appdao = new AppDao();
		try{
			System.out.println(installUrl);
			System.out.println(updateUrl);			
			App app = new App(id,name, information, installUrl,updated,updateUrl,secret,logourl);
			System.out.println(app.getId());
			System.out.println(app.getInstallUrl());
			System.out.println(app.getUpdateUrl());
			System.out.println(app.getUpdated());
			System.out.println(app.getName());
			appdao.save(app); 
		} catch(Exception err){
			err.printStackTrace();
		}
		    
		
    }
}