package ScriptEditor;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.internetrt.sdk.InternetRT;

/**
 * Servlet implementation class RestoreRoutingServlet
 */
@WebServlet("/RestoreRoutingServlet")
public class RestoreRoutingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestoreRoutingServlet() {
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
		String routing = request.getParameter("routing");
		InternetRT rt = config.properties.irt;
		
		System.out.println("[RestoreRoutingServlet : doPost]: "+"routingXmlString: "+routing);
		
		HttpSession session = request.getSession();
		String accessToken = session.getAttribute("accessToken").toString();
		
		rt.ConfirmRouting(accessToken,routing);
	}

}
