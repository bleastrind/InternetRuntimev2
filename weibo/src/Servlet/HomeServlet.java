package Servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import weibo4j.Account;
import weibo4j.Comments;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

public class HomeServlet extends HttpServlet{
		
	public String CLIENT_ID = WeiboConfig.getValue("client_ID");
	public String CLIENT_SECRET = WeiboConfig.getValue("client_SERCRET");
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, java.io.IOException {
			
			String code=(String)request.getParameter("code");
			String Token=(String)request.getSession().getAttribute("accessToken");
			System.out.println("[HomeServlet : doPost]: "+Token);
			String requestUrl = "https://api.weibo.com/oauth2/access_token?client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&grant_type=authorization_code&redirect_uri=http://other.internetrt.org:8080/weibot/home&code="+code;
			String result = httpClientPost (requestUrl);
			
			System.out.println("[HomeServlet : doPost]"+result);
			try {
				JSONObject json = new JSONObject(result);
				String sessionKey = (String) json.get("access_token");
				System.out.println("[HomeServlet : doPost]: "+"weiboSessionKey: "+sessionKey);
				request.setAttribute("sessionkeyfromweibo", sessionKey);
				System.out.println("[HomeServlet : doPost]: "+"weiboSessionKey"+sessionKey);
				Initer.feedstub.addFeedUser(sessionKey,Token);
			} catch (JSONException e) {
				System.out.println("[HomeServlet : doPost] json串问题");
				e.printStackTrace();
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
