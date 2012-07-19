package com.yourdomain.webcanvas;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		String code = null;
		String appID = config.properties.appID;	
		String appSecret = config.properties.appSecret;
		request.setAttribute("appId", com.yourdomain.webcanvas.config.AppConfig.APP_ID);
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID="+appID+"&redirect_uri=http://apps.renren.com/vinsiademo/welcome");
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
}