package ScriptEditor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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


public class GetAppsServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetAppsServlet() {
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
		
		System.out.println("Coming into get apps servlet");

		InternetRuntime rt = new InternetRuntime();
		HttpSession session = request.getSession();
		String accessToken = session.getAttribute("accessToken").toString();
		List<String> appIDList  = rt.getApps(accessToken);
		System.out.println("appsIDLIist"+appIDList);
		
		JSONArray applications= new JSONArray();
		JSONObject appsObject = new JSONObject();
		
		for(String str: appIDList)
		{
			String xmlString = rt.getAppDetail(str, accessToken);
			
			System.out.println(xmlString);
			
			AppXmlParser appXmlParser = new AppXmlParser(xmlString);
			
			Application application  = appXmlParser.createApplication();
			
			
			JSONObject appObject = TermToJson.ApplicationToJson(application);
			
			
			applications.put(appObject);
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
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
