package com.yourdomain.webcanvas;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiClient;
import com.renren.api.client.param.impl.SessionKey;
import com.renren.api.client.services.UserService;
import com.yourdomain.webcanvas.config.AppConfig;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, java.io.IOException {
		
 		String isAuth=(String)request.getParameter("xn_sig_added");
	    if(isAuth.equals("0")){
	        response.sendRedirect("auth");
	        return;
	    }
	    
		String sessionKey = request.getParameter("xn_sig_session_key");
		String renrenUserId = request.getParameter("xn_sig_user");
		String Token = (String) request.getSession().getAttribute("accessToken");
		
		if (sessionKey != null && renrenUserId != null && Token != null) {						
			RenrenApiClient apiClient = RenrenApiClient.getInstance();
		
			JSONArray userInfo = apiClient .getUserService().getInfo(renrenUserId,new SessionKey(sessionKey));
		 	
			if (userInfo != null && userInfo.size() > 0) {
				JSONObject currentUser = (JSONObject) userInfo.get(0);
				if (currentUser != null) {
					String userName = (String) currentUser.get("name");
					String userHead = (String) currentUser.get("headurl");
					request.setAttribute("userName", userName);
					request.setAttribute("userHead", userHead);
				}
			}
			
			try {
				System.out.println("add user");
				System.out.println(sessionKey+" "+renrenUserId+" "+Token);
				System.out.println(config.properties.irt.getUserIdByToken(Token));
				ApiInitListener.feedstub.addFeedUser(sessionKey, renrenUserId,Token);
			} catch(Exception err){
				err.printStackTrace();
				//没有code
			}
			
			
			
		}
		
		request.setAttribute("appId", AppConfig.APP_ID);
		RequestDispatcher welcomeDispatcher = request.getRequestDispatcher("/views/home.jsp");
		welcomeDispatcher.forward(request, response);
	}
}