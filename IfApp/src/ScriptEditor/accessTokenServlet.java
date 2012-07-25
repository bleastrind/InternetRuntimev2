package ScriptEditor;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.internetrt.sdk.InternetRT;

/**
 * Servlet implementation class accessTokenServlet
 */
@WebServlet("/accessTokenServlet")
public class accessTokenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public accessTokenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String code = null;
		String appID = "927259c0-1d4b-4170-826a-8dea43a7a7a7";
		String appSecret = "6337a390-0dda-4b02-9a13-b5fd175bb1c1";
		InternetRT rt = config.properties.irt;
		
		if(request.getParameter("code") == null){
			
			String codeCallBack = rt.getAuthCodeUrl();
			response.sendRedirect(codeCallBack );
		}
		else {
			code = request.getParameter("code");
			System.out.println("[accessTokenServlet : doGet]: "+"CODE"+code);
			
			
			
			String accessTokenString = rt.getAccessToken(code, appID, appSecret);
			
			System.out.println("[accessTokenServlet : doGet]: "+"accessTokenString"+accessTokenString);
			
			HttpSession session = request.getSession();
			session.setAttribute("accessToken", accessTokenString);
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/start.html");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
