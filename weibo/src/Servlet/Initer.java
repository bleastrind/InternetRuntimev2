package Servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Initer implements ServletContextListener {
	
	public static FeedStub feedstub = new FeedStub();
	public static Thread stub;
	public static Map<String,UserSpace> User = new HashMap();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		stub = new Thread(feedstub);
		stub.start();
	}

}
