package Servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

public class FeedStub implements Runnable {
	public static String sessionKey;
	public static Queue<UserSpace> up = new LinkedList<UserSpace>();
	public Boolean flag;

	public void addFeedUser(String sessionKey, String Token) {
		try {
			System.out.println("[FeedStub : addFeedUser]: " + "add user");
			System.out
					.println("[FeedStub : addFeedUser]: " + "Token: " + Token);
			System.out.println("[FeedStub : addFeedUser]: "
					+ WeiboConfig.getValue("baseURL"));
			System.out.println("[FeedStub : addFeedUser]: SessionKey"
					+ sessionKey);
			Timeline tl = new Timeline();
			tl.setToken(sessionKey);
			StatusWapper sw = tl.getUserTimeline();
			List<Status> status = sw.getStatuses();
			System.out.println("0");
			UserSpace us = new UserSpace(sessionKey, status, Token,
					new ArrayList<String>());
			System.out.println("1");
			flag = up.offer(us);
			System.out.println("2");
			Initer.User.put(config.properties.irt.getUserIdByToken(Token), us);
			System.out.println("3");
		} catch (Exception err) {
			System.out
					.print("[FeedStub : addFeedUser]: " + "add feed user err");
			err.printStackTrace();
		}
	}

	public void publish(String Message, String token) throws WeiboException {
		try {
			publishMessage(Message, token);
		} catch (Exception err) {

		}
	}

	private void publishMessage(String Message, String token)
			throws WeiboException {
		System.out.println("[FeedStub:publish]");
		Timeline tl = new Timeline();
		tl.setToken(token);
		tl.UpdateStatus(Message);
	}

	@Override
	public void run() {
		System.out.println("[FeedStub : run]: " + "run");
		int defaultsleeptime = 100000;
		int sleeptime = defaultsleeptime;
		while (true) {
			try {
				getInfo();
				sleeptime = defaultsleeptime;
				Thread.sleep(sleeptime / (up.size() + 1));
			} catch (Exception e) {
				sleeptime = sleeptime * 2;
			}
		}

	}

	private void getInfo() {
		if (up.size() != 0) {
			UserSpace us = up.poll();
			List<Status> status = us.getMessage();
			List<Status> sts = new LinkedList<Status>();
			String sessionKey = us.getSessionKey();
			Timeline tl = new Timeline();
			tl.setToken(sessionKey);
			Boolean flag = true;
			try {
				sts = tl.getUserTimeline().getStatuses();
			} catch (WeiboException err) {
				flag = false;
				err.printStackTrace();
			} catch (Exception err) {
				err.printStackTrace();
				try {
					sts = tl.getUserTimeline().getStatuses();
				} catch (Exception e) {
					flag = false;
				}
			} finally {
				if (flag) {
					for (Status x : sts) {
						System.out.println("[FeedStub : run]: " + x.getText());
						if ((!status.contains(x))
								&& (!us.msg.contains(x.getText()))) {
							try {
								System.out.println("[Sina]:" + x.getText());
								Map<String, String> map = new HashMap();
								map.put("message", URLEncoder.encode(x
										.getText()));
								map.put("from", URLEncoder.encode("sina"));
								Thread thread = new Thread(new Send(us
										.getToken(), "updatestatus", map));
								thread.start();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						status.add(x);
					}
					UserSpace ut = new UserSpace(sessionKey, status, us
							.getToken(), us.msg);
					up.add(ut);
				}
			}
		}
	}

}
