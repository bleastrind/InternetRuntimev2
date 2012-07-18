package Servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import org.internetrt.sdk.InternetRT;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

public class FeedStub implements Runnable{
	public static String sessionKey;
	public static Queue<UserSpace> up = new PriorityBlockingQueue<UserSpace>();
	public static Queue<UserSpace> upt = new PriorityBlockingQueue<UserSpace>();
	public static Weibo weibo = new Weibo();
	
	public void addFeedUser(String sessionKey,String Token)
	{
		synchronized(this){
			try{ 
				System.out.println("add user");
				System.out.println(Token);
				weibo.setToken(sessionKey);
				StatusWapper sw = new Timeline().getUserTimeline();
				List<Status> status = sw.getStatuses();
				UserSpace us = new UserSpace(sessionKey,status,Token);
				up.add(us);
				Initer.User.put(config.properties.irt.getUserIdByToken(Token), us);
			} catch (Exception err){
				System.out.print("add err");
			}
		}
	}
	
	public void publish(String Message,String token) throws WeiboException{
		synchronized(this){
			weibo.setToken(token);
			Timeline tl = new Timeline();
			tl.UpdateStatus(Message);
		}
	}
	
	@Override
	public void run() {
		System.out.println("run");
		while (true){
			synchronized(this){
				if (up.size()!=0){
					UserSpace us = up.poll();
					List<Status> status = us.getMessage();
					List<Status> sts = new LinkedList<Status>();
					Timeline tl = new Timeline();
					try {sts = tl.getUserTimeline().getStatuses();}
					catch (Exception err) {}
					String sessionKey = us.getSessionKey();
					weibo.setToken(sessionKey);
					for (Status x :sts){	
						if (!status.contains(x)) {						
							System.out.println("*******"+x.getText());
							try {
								System.out.println("token:"+us.getToken());
								Map<String,String> map = new HashMap();
								map.put("message", x.getText());
								config.properties.irt.send(us.getToken(),"updateStatus", map);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try{
								
							} catch (Exception err){
								
							}
							status.add(x);
						}
					}
					UserSpace ut = new UserSpace(sessionKey,status,us.getToken());
					upt.add(ut);
				}
				up = upt;
				upt = new PriorityBlockingQueue<UserSpace>();
			}
			try{
				Thread.sleep(50000);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
	
