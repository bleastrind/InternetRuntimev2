package Servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpException;

import org.internetrt.sdk.InternetRT;

import weibo4j.model.WeiboException;

public class MessageReciver extends HttpServlet {
	//private Map<String,UserSpace> map = ApiInitListener.feedstub.map;
	protected void doGet(HttpServletRequest request,HttpServletResponse response) {
		System.out.print("message");
		String Message = request.getParameter("message");
		String rid = request.getParameter("rid");
		InternetRT irt = config.properties.irt;
		try {
		String userid = irt.getUserIdByToken(irt.setAccessTokenWithCode(irt.getAuthCodeByRoutingInstanceID(rid)));
			UserSpace us = Initer.User.get(userid);
			Initer.feedstub.publish(Message,us.getSessionKey());
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			System.out.println("can't publish");
			e.printStackTrace();
		}
	}
}

