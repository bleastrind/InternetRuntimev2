package com.yourdomain.webcanvas;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;
import org.internetrt.sdk.InternetRT;

import com.renren.api.client.RenrenApiClient;

public class messageReciver extends HttpServlet {
	//private Map<String,UserSpace> map = ApiInitListener.feedstub.map;
	protected void doGet(HttpServletRequest request,HttpServletResponse response) {
		String UserId = request.getParameter("uid");
		String Message = request.getParameter("message");
		String rid = request.getParameter("rid");
		
		//UserSpace us = map.get(UserId);
		//us.getSessionKey();
		InternetRT irt = new InternetRT();
		String accesstoken;
		try {
			accesstoken = irt.getAuthCodeByRoutingInstanceID(rid);
			UserSpace us = ApiInitListener.User.get(accesstoken);
			ApiInitListener.feedstub.publish(Message,us.getSessionKey());
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
