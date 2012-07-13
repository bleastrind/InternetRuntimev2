package cn.edu.act.internetos.appmarket.service;


import config.properties;
import java.io.IOException;
import java.util.*;
import models.*;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.internetrt.sdk.InternetRT;


public class AppService{
	
	public static List<App> getUserApps(String token){		
		InternetRT irt = properties.irt;
		List<String> apps = irt.getApps(token);
		List<App> userapps = new ArrayList();
		AppDao appdao = new AppDao();
		for (String app:apps){
			System.out.println("app id :"+app);
			App p = appdao.findById(app);
			if (p!=null) userapps.add(p);
		}
		
		return userapps;
	}
	
	public static boolean market(String token){
		InternetRT irt = properties.irt;
		try {
			System.out.print("market");
		irt.getAppDetail(config.properties.appID, token);
		System.out.println("yes market");
		return true;
		} catch(Exception err){
			System.out.println("no market");
			return false;
		}
	}
	
	public static List<AppConfig> getAllConfig(String token){
		/*UserSpaceDao ueserspacedao = new UserSpaceDao();
		UserSpace userspace = userspacedao.getUserSpace(user);	
		return userspace.getAppConfigs();	*/
		return null;
	}
	
	
    public static List<App> getAllApps()  
	{
        AppDao appdao = new AppDao();	
        List<App> applist = appdao.getAllApps();     
        return applist;		  
    }
	
	public static boolean addUserApp(App app, String token)
	{
		return properties.irt.installApp(token, app.getInformation());
	}
	
	public static void deleteUserApp(App app, String token)
	{
		
	}
	

	
	public static void setConfig(String userId, String appId, String config)
	{
		/*UserSpaceDao userspacedao = new UserSpaceDao();
		UserSpace userspace = userspacedao.getUserSpace(userId);
		List<AppConfig> temp = userspace.getAppConfigs();
		for (AppConfig appConfig: userspace.getAppConfigs())
			if (appConfig.getAppId().equals(appId))
				temp.remove(appConfig);
		temp.add(new AppConfig(appId, config));
		userspace.setAppConfigs(temp);
		userspacedao.save(userspace);	*/	
	}
}