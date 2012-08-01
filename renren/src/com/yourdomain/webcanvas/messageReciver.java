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
		try{
		String Message = request.getParameter("message");
		String rid = request.getParameter("rid");
		System.out.println("[messageReciver : doGet]: "+"rid: "+rid);
		InternetRT irt = config.properties.irt;
		String userid = irt.getUserIdByToken(irt.setAccessTokenWithCode(irt.getAuthCodeByRoutingInstanceID(rid)));
		System.out.print("[messageReciver : doGet]: "+"userid: " + userid);

		UserSpace us = ApiInitListener.User.get(userid);
		System.out.print("[messageReciver : doGet]: "+"userSessionKey: "+us.getSessionKey());
		ApiInitListener.feedstub.publish(Message, us.getSessionKey());
		} catch(Exception err){
			System.out.println(err);
		}
	}
}
