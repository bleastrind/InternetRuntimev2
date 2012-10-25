package Servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpException;

import org.internetrt.sdk.InternetRT;

import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class MessageReciver extends HttpServlet {
	//private Map<String,UserSpace> map = ApiInitListener.feedstub.map;
	protected void doGet(HttpServletRequest request,HttpServletResponse response) {
		System.out.print("message");
		String Message =  URLDecoder.decode(request.getParameter("message"));
		String rid = request.getParameter("rid");
		String from = URLDecoder.decode(request.getParameter("from"));
		System.out.println(from);
		if (!from.equals("sina")){
			InternetRT irt = config.properties.irt;
			try {
				String userid = irt.getUserIdByToken(irt.setAccessTokenWithCode(irt.getAuthCodeByRoutingInstanceID(rid)));
				UserSpace us = Initer.User.get(userid);
				if (us!=null){
					us.msg.add(Message);
					Initer.feedstub.publish(Message,us.getSessionKey());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("can't publish");
				e.printStackTrace();
			}
		}
	}
}

