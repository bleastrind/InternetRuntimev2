package com.yourdomain.webcanvas;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;


import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.SessionKey;
import com.renren.api.client.services.UserService;
import com.yourdomain.webcanvas.config.AppConfig;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
	
	public String client_ID = AppConfig.API_KEY;
	public String client_SECRET = AppConfig.APP_SECRET;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		
		String code = request.getParameter("code");
		String Token=(String)request.getSession().getAttribute("accessToken");
		System.out.println("accessToken"+Token);
		String requestUrl =  "https://graph.renren.com/oauth/token?grant_type=authorization_code&client_id="+client_ID+"&redirect_uri=http://other.internetrt.org:8080/renrent/home&client_secret="+client_SECRET+"&code="+code;
		String result = httpClientPost (requestUrl);
		
		System.out.println("[HomeServlet : doPost]"+result);
		try{
		JSONObject json = JSONObject.fromString(result);
		String sessionKey = (String) json.get("access_token");
		System.out.println("[HomeServlet : doPost]: "+"renrenSessionKey: "+sessionKey);
		request.setAttribute("sessionkeyfromrenren", sessionKey);
		System.out.println("[HomeServlet : doPost]: "+"renrenSessionKey"+sessionKey);
		ApiInitListener.feedstub.addFeedUser(sessionKey, Token);
		} catch (Exception err){
			err.printStackTrace();
			System.out.println("[HomeServlet : doPost] json串问题");
		}
		
		RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/home.jsp");
		welcomeDispatcher.forward(request, response);
	}
	
	public static String httpClientPost(String url)
	{
		String response = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod (url);
		try{
			client.executeMethod(method);
			if(method.getStatusCode()== HttpStatus.SC_OK){
				response = method.getResponseBodyAsString();
			}
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Exception happend when processing Http Post:"+url+"\n"+e);
		}finally{
			method.releaseConnection();
		}
		
		return response;
	}
}