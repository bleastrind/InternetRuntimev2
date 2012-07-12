package com.yourdomain.webcanvas;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;
import org.internetrt.sdk.InternetRT;

public class messageReciver extends HttpServlet {
	// private Map<String,UserSpace> map = ApiInitListener.feedstub.map;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		String Message = request.getParameter("message");
		String rid = request.getParameter("rid");
		System.out.println(rid);
		InternetRT irt = config.properties.irt;
		try {
			String userid = irt.getUserIdByToken(irt.setAccessTokenWithCode(irt.getAuthCodeByRoutingInstanceID(rid)));
			System.out.print("userid" + userid);
			UserSpace us = ApiInitListener.User.get(userid);
			System.out.print(us.getSessionKey());
			ApiInitListener.feedstub.publish(Message, us.getSessionKey());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
