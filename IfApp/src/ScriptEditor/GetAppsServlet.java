package ScriptEditor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;

/**
 * Servlet implementation class GetAppsServlet
 */
@WebServlet("/GetAppsServlet")
public class GetAppsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAppsServlet() {
        super();
        // TODO Auto-generated constructor stub 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		InternetRT rt = config.properties.irt;
		HttpSession session = request.getSession();
		String accessToken = session.getAttribute("accessToken").toString();
		List<String> appIDList  = rt.getApps(accessToken);
		System.out.println("[GetAppsServlet : doGet]: "+"appsIDLIist"+appIDList);
		
		JSONArray applications= new JSONArray();
		JSONObject appsObject = new JSONObject();
		
		for(String str: appIDList)
		{
			String xmlString = rt.getAppDetail(str, accessToken);
			
			System.out.println("[GetAppsServlet : doGet]: "+"appXmlString"+xmlString);
			
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
		System.out.println("[GetAppsServlet : doGet]: "+"resultJsonString"+result);
		PrintWriter out = response.getWriter();
		out.write(result);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
