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

		String code = null;
		String appID = "6735ee88-7e0c-4adc-8a30-76138b89a17c";
		String appSecret = "91ff02fd-c2c9-48e9-8f0f-6c91fb45d8e7";
		
		if(request.getParameter("code") == null){
			response.sendRedirect("http://localhost:9000/oauth/authorize?appID=6735ee88-7e0c-4adc-8a30-76138b89a17c&redirect_uri=http://localhost:8080/IfApp/servlet/accessTokenServlet");
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
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA");

		InternetRuntime rt = new InternetRuntime();
	//	String accessToken = rt.getAccessToken();
	//	List<String> appIDList  = rt.getApps(accessToken);
		
		JSONArray applications= new JSONArray();
		JSONObject appsObject = new JSONObject();
		
	//	for(String str: appIDList)
		{
	//		String xmlString = rt.getAppDetail(str, accessToken);
	//		AppXmlParser appXmlParser = new AppXmlParser(xmlString);
	//		Application application  = appXmlParser.createApplication();
			
	//		JSONObject appObject = TermToJson.ApplicationToJson(application);
			
		//	applications.put(appObject);
		}
		
		try {
			appsObject.put("applications", applications);
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		String result = appsObject.toString();
		System.out.println(result);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write(result);
		out.flush();
		out.close();
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
