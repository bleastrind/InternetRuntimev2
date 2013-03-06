package com.yourdomain.webcanvas;

import java.util.LinkedList;
import java.util.Queue;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.AccessToken;
import com.renren.api.client.param.impl.SessionKey;

public class ThreadPublish implements Runnable{
	public String message,sessionkey;
	public void add(String message,String sessionkey){
		this.message = message;
		this.sessionkey = sessionkey;
	}

	@Override
	public void run() {
		while (!tp()){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean tp(){
		RenrenApiClient apiClient = RenrenApiClient.getInstance();
		
		try {
			
			apiClient.getFeedService().publicFeed("人人stub", "人人stub发送", "http://apps.renren.com/irtshare/welcome", "", "", "", "", message, new AccessToken(sessionkey));
		} catch(Exception err){
			err.printStackTrace();
			return false;
		}
		
		return true;
	}
}
