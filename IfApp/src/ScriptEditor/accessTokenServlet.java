package ScriptEditor;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.portable.ApplicationException;

import com.sun.corba.se.spi.protocol.ForwardException;


public class accessTokenServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public accessTokenServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//{id:"c481fba3-866d-4c4e-ae36-6acd4262017a",secret:"f490c634-34bd-44fd-9a70-8de447103e50"}
		
		System.out.println("accessTokenServletAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		String code = null;
		String appID = "c481fba3-866d-4c4e-ae36-6acd4262017a";
		String appSecret = "f490c634-34bd-44fd-9a70-8de447103e50";
		
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID=c481fba3-866d-4c4e-ae36-6acd4262017a&redirect_uri=http://localhost:8080/IfApp/servlet/accessTokenServlet");
		}
		else {
			code = request.getParameter("code");
			System.out.println("CODE"+code);
			InternetRuntime rt = new InternetRuntime();
			String accessTokenString = rt.getAccessToken(code, appID, appSecret);
			System.out.println(accessTokenString);
			HttpSession session = request.getSession();
			session.setAttribute("accessToken", accessTokenString);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/start.html");
			rd.forward(request, response);
		}
	}
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
