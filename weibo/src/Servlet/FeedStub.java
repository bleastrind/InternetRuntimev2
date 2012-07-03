package Servlet;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import weibo4j.Comments;
import weibo4j.Weibo;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

public class FeedStub implements Runnable{
	public static String sessionKey;
	public static Queue<UserSpace> up = new PriorityBlockingQueue<UserSpace>();
	public static Queue<UserSpace> upt = new PriorityBlockingQueue<UserSpace>();
	public static Weibo weibo = new Weibo();

	public void addFeedUser(String sessionKey)
	{
		synchronized(this){
			try{ 
				System.out.println("add user");
				weibo.setToken(sessionKey);
				CommentWapper cw = new Comments().getCommentByMe();
				List<Comment> comments = cw.getComments();
				UserSpace us = new UserSpace(sessionKey,comments);
				up.add(us);
			} catch (Exception err){
				System.out.print("add err");
			}
		}
	}
	
	public void addFeedUser(String sessionKey,String token)
	{
		synchronized(this){
			try{ 
				System.out.println("add user");
				weibo.setToken(sessionKey);
				CommentWapper cw = new Comments().getCommentByMe();
				List<Comment> comments = cw.getComments();
				UserSpace us = new UserSpace(sessionKey,comments,token);
				up.add(us);
			} catch (Exception err){
				System.out.print("add err");
			}
		}
	}
	
	public void sendMessage(String uid,String to,String message) throws Exception{
		URL url = new URL("http://127.0.0.1/messageTransfer/transfer/message?uid="+uid+
				"&from=Sina"+"&to="+to+"&message="+message);
		HttpURLConnection urlConn = null;
		urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestProperty("accept", "*/*"); 
		urlConn.setRequestProperty("connection", "Keep-Alive"); 
        urlConn.setRequestMethod("GET");
        urlConn.setConnectTimeout(5000);// （单位：毫秒）jdk
        urlConn.setReadTimeout(5000);// （单位：毫秒）jdk 1.5换成这个,读操作超时
        urlConn.setDoOutput(true);
        urlConn.connect();
	}
	
	@Override
	public void run() {
		System.out.println("run");
		while (true){
			synchronized(this){
				if (up.size()!=0){
					UserSpace us = up.poll();
					List<Comment> comments = us.getMessage();
					List<Comment> cms = new LinkedList<Comment>();
					Comments cm = new Comments();
					List<Comment> cmt = new LinkedList<Comment>(); ;
					try {cmt = cm.getCommentByMe().getComments();}
					catch (Exception err) {}
					String sessionKey = us.getSessionKey();
					weibo.setToken(sessionKey);
					for (Comment x :cmt){	
						if (!comments.contains(x)) {
							//System.out.println(x.getText());
							/*
							InternetRT irt = new InternetRT();
							irt.setToken(us.getToken());
							irt.send("weibo","share",newHashMap());
							*/
							try{
								
							} catch (Exception err){
								
							}
					        comments.add(x);
						}
					}
					UserSpace ut = new UserSpace(sessionKey,comments);
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
	
