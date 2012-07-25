package com.yourdomain.webcanvas;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.SessionKey;

public class FeedStub implements Runnable{
	public static JSONArray feedInfo;
	public static String sessionKey,renrenUserId;
	public static Set<String> message = new HashSet<String>();
	public static Queue<UserSpace> up = new PriorityBlockingQueue<UserSpace>();
	
	public void addFeedUser(String sessionKey,String renrenUserId,String token){
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		JSONArray feedInfo = apiClient.getFeedService().getFeed("10", Integer.parseInt(renrenUserId), 1, 10,new SessionKey(sessionKey));
		Set<String>	messages = new HashSet<String>();
		if (feedInfo != null && feedInfo.size()>0) {
			for (int i=0;i<feedInfo.size();i++)
			{
				JSONObject currentFeed = (JSONObject) feedInfo.get(i);
				if (currentFeed != null){
					String message = (String) currentFeed.get("message");
					messages.add(message);
				}
			}
		}
		UserSpace t = new UserSpace(sessionKey,renrenUserId,messages,token);
		up.add(t);
		ApiInitListener.User.put(config.properties.irt.getUserIdByToken(token),
				t);
	}
	
	public void publish(String message,String sessionkey){
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		apiClient.getFeedService().publicFeed("weibo", message, "nihao", "", "", "", "", "转发自微博", new SessionKey(sessionkey));
	}
	
	public void run()
	{
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		while (true) {
			if (up.size()!=0){
				Set<String>	messages = new HashSet<String>();
				UserSpace t = up.poll();
				sessionKey = t.getSessionKey();
				renrenUserId = t.getRenrenUserId();
				message = t.getMessage();
				feedInfo = apiClient.getFeedService().getFeed("10", Integer.parseInt(renrenUserId), 1, 10,new SessionKey(sessionKey));
				if (feedInfo != null && feedInfo.size()>0) {
				
					for (int i=0;i<feedInfo.size();i++)
					{
						JSONObject currentFeed = (JSONObject) feedInfo.get(i);

						if (currentFeed != null){
							String message = (String) currentFeed.get("message");
							messages.add(message);
							System.out.println("[FeedStub : run]: "+"messsage: "+message);
							if (!this.message.contains(message)){
								Map<String,String> map = new HashMap();
								map.put("message", URLEncoder.encode(message));
								try {
									config.properties.irt.send(t.getToken(),"updatestatus", map);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("[FeedStub : run]: "+"messsage: "+message);
							}
						}
					}
				}
				t.updateMessage(messages);
				up.add(t);
			}
			try{
				Thread.sleep(50000);
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
