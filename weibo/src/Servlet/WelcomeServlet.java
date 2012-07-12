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
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID=6eb76f97-6772-412b-9ad7-0c9374f372ee&redirect_uri=http://apps.weibo.com/vinsiatest/weibot/welcome");
		} else 	{
		code = request.getParameter("code");
		String accessTokenString = config.properties.irt.getAccessToken(code, appID, appSecret);
		System.out.println(accessTokenString);
		HttpSession session = request.getSession();
		session.setAttribute("accessToken", accessTokenString);
		
		RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/welcome.jsp");
		welcomeDispatcher.forward(request, response);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String code = null;
		String appID = config.properties.appID;	
		String appSecret = config.properties.appSecret;
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID=6735ee88-7e0c-4adc-8a30-76138b89a17c&redirect_uri=http://localhost:8080/IfApp/servlet/accessTokenServlet");
		}
		else {
			code = request.getParameter("code");
			System.out.println("CODE"+code);
			String accessTokenString = config.properties.irt.getAccessToken(code, appID, appSecret);
			System.out.println(accessTokenString);
			HttpSession session = request.getSession();
			session.setAttribute("accessToken", accessTokenString);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/start.html");
			rd.forward(request, response);
		}
	}
}
