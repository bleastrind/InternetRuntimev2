package cn.edu.act.internetos.appmarket.service;

import java.util.*;
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

    public static void addAppSave(String name, String information, String installUrl)
    {
		AppDao appdao = new AppDao();
		App app = new App(name, information, installUrl);
		appdao.save(app);     
		
    }
}