package Servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;

public class LoginServlet extends HomeServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		
		String appID = config.properties.appID;	
		String appSecret = config.properties.appSecret;
		
		String code = null;
		
		if(request.getParameter("code") == null)
		{
			response.sendRedirect("http://internetrt.org:9000/oauth/authorize?appID="+appID+"&redirect_uri=http://other.internetrt.org:8080/weibot/login");
		}
		else {
			code = request.getParameter("code");
			String accessTokenString = config.properties.irt.getAccessToken(code, appID, appSecret);
			
			HttpSession session = request.getSession();
			session.setAttribute("accessToken", accessTokenString);
		
			RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/login.jsp");
			welcomeDispatcher.forward(request, response);	
		}
	}
}