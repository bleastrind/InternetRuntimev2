package com.yourdomain.webcanvas;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.AccessToken;
import com.renren.api.client.param.impl.SessionKey;
import com.yourdomain.webcanvas.config.AppConfig;

public class FeedStub implements Runnable{
	public static Set<String> message = new HashSet<String>();
	public static Queue<UserSpace> up = new LinkedList<UserSpace>();
	
	public Boolean addFeedUser(String sessionKey,String token){
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		JSONArray feedInfo = new JSONArray();
		String renrenUserId;
		try {
			
			renrenUserId = String.valueOf(apiClient.getUserService().getLoggedInUser(new AccessToken(sessionKey)));
		} catch (Exception err){
			err.printStackTrace();
			return false;
		}
		try {
			feedInfo = apiClient.getFeedService().getFeed("10", Integer.parseInt(renrenUserId), 1, 30,new AccessToken(sessionKey)); //可能拿不到
		} catch (Exception err){
			err.printStackTrace();
			return false;
		}
		Set<String>	messages = new HashSet<String>();
		if (feedInfo != null && feedInfo.size()>0) {
			for (int i=0;i<feedInfo.size();i++)
			{
				JSONObject currentFeed = (JSONObject) feedInfo.get(i);
				assert (currentFeed != null);
				String message = (String) currentFeed.get("message");
				messages.add(message);		
			}
		}
		UserSpace t = new UserSpace(sessionKey,renrenUserId,messages,token);
		if (!up.offer(t)) return false;
		ApiInitListener.User.put(config.properties.irt.getUserIdByToken(token),t);
		
		return true;
	}
	
	public void publish(String message,String sessionkey){
		ThreadPublish tp = new ThreadPublish();
		tp.add(message, sessionkey);
		Thread t = new Thread(tp);
		t.start();
	}
	
	public void run()
	{
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		int defaultsleeptime = 100000;
		int sleeptime = defaultsleeptime;
			while (true) {
				try{
					getInfo(apiClient);
					sleeptime = defaultsleeptime;
					Thread.sleep(sleeptime);
				} catch(Exception e) {
					sleeptime = sleeptime * 2;
				}	
		}
	}

	private void getInfo(RenrenApiClient apiClient) {
		if (up.size()!=0){
			Set<String>	messages = new HashSet<String>();
			UserSpace t = up.poll();
			String sessionKey = t.getSessionKey();
			String renrenUserId = t.getRenrenUserId();
			message = t.getMessage();
			
			messages.addAll(message); //保证接收过来的信息也在用户空间中
			//refreshToken()			  //刷新token 保证token有效
			
			JSONArray feedInfo = new JSONArray();
			try {
			feedInfo = apiClient.getFeedService().getFeed("10", Integer.parseInt(renrenUserId), 1, 10,new AccessToken(sessionKey));
						
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
							map.put("from",URLEncoder.encode("renren"));
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
			if (!up.offer(t)) System.out.println("[FeedStub : run] Memery Out");
			} catch(Exception e){
				//获取info错误，用户退出
			}
		}
	}
	
	public String refreshToken(String refresh_token){
		
		String response = null;
		
		String Client_id = AppConfig.APP_ID;
		String Client_secret = AppConfig.APP_SECRET;
		String Url = "https://graph.renren.com/oauth/token?"+
		    "grant_type=refresh_token&"+
		    "refresh_token="+refresh_token+"&"+
		    "client_id="+Client_id+"&"+
		    "client_secret="+Client_secret;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod (Url);
		
		try{
			client.executeMethod(method);
			if(method.getStatusCode()== HttpStatus.SC_OK){
				response = method.getResponseBodyAsString();
			}
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Exception happend when processing Http Post:"+Url+"\n"+e);
		}finally{
			method.releaseConnection();
		}
		System.out.println("[FeedStub : run]"+response);
		
		net.sf.json.JSONArray jsonarray = net.sf.json.JSONArray.fromObject(response);
		net.sf.json.JSONObject jsonobject = jsonarray.getJSONObject(0);
		
		return (String) jsonobject.get("access_token");
	}
}
