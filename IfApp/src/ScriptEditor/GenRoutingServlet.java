package ScriptEditor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.RoutingGenerator;

/**
 * Servlet implementation class GenRoutingServlet
 */
@WebServlet("/GenRoutingServlet")
public class GenRoutingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenRoutingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String trigger = request.getParameter("trigger");
		String triggerChannel = request.getParameter("triggerChannel");
		String actionChannel = request.getParameter("actionChannel");
		
		System.out.println("TRIGGER: "+trigger);
		System.out.println("TRIGGERCHANNEL: "+triggerChannel);
		System.out.println("ACTIONCHANNEL: "+actionChannel);
		
		InternetRT rt = config.properties.irt;
		
		HttpSession session = request.getSession();
		String accessToken = session.getAttribute("accessToken").toString();
		
		String signalXml = rt.getSignalDefination(trigger);
		String appXml = rt.getAppDetail(actionChannel, accessToken);
		
		System.out.println("SIGNALXMLSTRING"+signalXml);
		System.out.println("APPXML"+appXml);
		
		String routingID = UUID.randomUUID().toString();
		String userID = rt.getUserIdByToken(accessToken);
		  
		RoutingGenerator routingGenerator = new RoutingGenerator(signalXml, appXml);
		String routingXml = routingGenerator.generateRouting(trigger, triggerChannel, actionChannel, routingID, userID);
		
		String resultString = TermToJson.stringToJson("routingXml", routingXml).toString();
		
		PrintWriter out = response.getWriter();
		out.write(resultString);
		out.flush();
		out.close();
	}

}
