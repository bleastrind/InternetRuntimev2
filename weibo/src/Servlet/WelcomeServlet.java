package Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.internetrt.sdk.InternetRT;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		String code = null;
		String appID = config.properties.appID;	
		String appSecret = config.properties.appSecret;
		System.out.println("post code1:"+code);
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID="+appID+"&redirect_uri=http://apps.weibo.com/vinsiatest/weibot/welcome");
		} else 	{
		code = request.getParameter("code");
		System.out.println("post code2:"+code);
		String accessTokenString = config.properties.irt.getAccessToken(code, appID, appSecret);
		System.out.println(accessTokenString);
		HttpSession session = request.getSession();
		session.setAttribute("accessToken", accessTokenString);
		
		RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/welcome.jsp");
		welcomeDispatcher.forward(request, response);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		String code = null;
		String appID = config.properties.appID;	
		String appSecret = config.properties.appSecret;
		System.out.println("get code:"+code);
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID="+appID+"&redirect_uri=http://apps.weibo.com/vinsiatest/weibot/welcome");
		} else 	{
		code = request.getParameter("code");
		System.out.println("get code:"+code);
		String accessTokenString = config.properties.irt.getAccessToken(code, appID, appSecret);
		System.out.println(accessTokenString);
		HttpSession session = request.getSession();
		session.setAttribute("accessToken", accessTokenString);
		
		RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/welcome.jsp");
		welcomeDispatcher.forward(request, response);
		}
	}
}
