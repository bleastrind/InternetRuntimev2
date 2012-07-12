package com.yourdomain.webcanvas;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.renren.api.client.RenrenApiConfig;
import com.yourdomain.webcanvas.config.AppConfig;

public class ApiInitListener implements ServletContextListener{
	
	public static Map<String,UserSpace> User = new HashMap();
	public static FeedStub feedstub = new FeedStub();

	//@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//Nothing to do
	}

	//@Override
	public void contextInitialized(ServletContextEvent arg0) {
		RenrenApiConfig.renrenApiKey = AppConfig.API_KEY;
		RenrenApiConfig.renrenApiSecret = AppConfig.APP_SECRET;
		Thread stub = new Thread(feedstub);
		stub.start();
	}
}