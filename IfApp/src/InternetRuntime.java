
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class InternetRuntime {
	
	public ArrayList<String> getApps (String accessToken)
	{
		ArrayList<String> applications = new ArrayList<String>();
		List files = new ArrayList<String> ();
		files.add("renrenApplication.xml");
		files.add("sinaApplication.xml");
		files.add("tencentApplication.xml");
		
		HttpClient httpClient = new HttpClient();
		
		for(Object file : files)
		{
			String param = "fileName="+file.toString();
			GetMethod getMethod = new GetMethod("http://localhost:8080/IfApp/servlet/GenXmlStringServlet"+"?"+param);

			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				    new DefaultHttpMethodRetryHandler());
			 try {
				   int statusCode = httpClient.executeMethod(getMethod);
				   if (statusCode != HttpStatus.SC_OK) {
				    System.err.println("Method failed: "
				      + getMethod.getStatusLine());
				   }
				   byte[] responseBody = getMethod.getResponseBody();
				   
				   String xmlString = new String(responseBody);
				   applications.add(xmlString);
				  } catch (HttpException e) {
				   System.out.println("Please check your provided http address!");
				   e.printStackTrace();
				  } catch (IOException e) {
				   e.printStackTrace();
				  } finally {
				   getMethod.releaseConnection();
				  }
		}
		return applications;
	}

}
