package ScriptEditor;
import sun.security.util.Password;


public class InstallUtil {
	public void registerUser(String username, String password)
	{
		InternetRuntime rt = new InternetRuntime();
		String requestUrl = "http://localhost:9000/clients/register?username="+username+"&password="+password;
		String result = rt.httpClientGet(requestUrl);
		System.out.println("registerUser method returns "+result);
	}
	
	public void userLogin(String username, String password)
	{
		InternetRuntime rt = new InternetRuntime();
		String requestUrl = "http://localhost:9000/clients/login?username="+username+"&password="+password;
		String result = rt.httpClientGet(requestUrl);
		System.out.println("userLogin method returns userID "+result);
	}
	
	public void registerApp ()
	{
		InternetRuntime rt = new InternetRuntime();
		String requestUrl = "http://localhost:9000/auth/appregister?email=fdsf";
		String result = rt.httpClientGet(requestUrl);
		System.out.println("registerApp method returns id and secret "+result);
	}
	
	public void installApp()
	{
		
	}
}
