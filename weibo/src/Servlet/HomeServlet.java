package Servlet;

import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weibo4j.Account;
import weibo4j.Comments;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.org.json.JSONObject;

public class HomeServlet extends HttpServlet{
	
		@SuppressWarnings("deprecation")
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, java.io.IOException {
			String Auth=(String)request.getParameter("signed_request");
			String Token=(String)request.getSession().getAttribute("accessToken");
			System.out.println(Token);
			Oauth oauth = new Oauth();
			try{
				System.out.print(Auth);
				oauth.parseSignedRequest(Auth);
				String sessionKey = oauth.getToken();
				System.out.println("weibo"+sessionKey);
				request.setAttribute("sessionkeyfromweibo", sessionKey);
				System.out.println("weibo"+sessionKey);
				Initer.stub.suspend();
				Initer.feedstub.addFeedUser(sessionKey,Token);
				Initer.stub.resume();
			} catch(Exception err){
				System.out.print("err");
			}
			
			RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/home.jsp");
			welcomeDispatcher.forward(request, response);		
		}

}
