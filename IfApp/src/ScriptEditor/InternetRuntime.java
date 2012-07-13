package ScriptEditor;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.internetrt.sdk.util.RootApplication;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.java_cup.internal.internal_error;


public class InternetRuntime {
	
	private List<String> parserXmlsIDString (String str)
	{
		List<String> applicationsIDList = new ArrayList<String>();
		int start = str.indexOf(':')+1;
		int end = str.length() - 1;
		String subString = str.substring(start,end);
		String[] tempStrings =subString.split(",");
		for(String o:tempStrings)
		{
			applicationsIDList.add(o);
		}
		return applicationsIDList;
	}
	
	private String replaceEnter(String str){
		Pattern pattern = Pattern.compile("\n");
		Matcher matcher = pattern.matcher(str);
		String result = matcher.replaceAll("");
		return result;
	}
	
	public String httpClientGet(String requestUrl)
	{
		byte[] responseBody = null;
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(requestUrl);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBody();
			
		} catch (HttpException e) {
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		String result = new String(responseBody);
		return result;
	}
	
	
	//Post 上去的数据的 Content-Type 是 application/form-url-encoded:Map[String,Seq[String]]
	//处理的时候请注意
	public  String httpClientPost(String url, Map<String, String>params)
	{
		String response = null;
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod (url);
		//设置Http Post数据
		if(params != null){
			HttpMethodParams p = new HttpMethodParams();
			for(Map.Entry<String, String> entry: params.entrySet())
			{
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try{
			client.executeMethod(method);
			if(method.getStatusCode()== HttpStatus.SC_OK){
				response = method.getResponseBodyAsString();
			}
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("执行Http Post请求"+url+"时，发生异常！"+e);
		}finally{
			method.releaseConnection();
		}
		
		return response;
	}
	
	
	public List<String> getApps (String accessToken)
	{
		String param = "accessToken="+accessToken;
		List<String> applicationsIDList = new ArrayList<String>();
		String requestUrl = "http://localhost:9000/config/apps"+"?"+param;
		String xmlsID = httpClientGet(requestUrl);
		applicationsIDList = parserXmlsIDString(xmlsID);
		return applicationsIDList;
	}
	
	public String getAppDetail (String appID, String accessToken)
	{
		String param = "accessToken="+accessToken;
		
		String requestUrl = "http://localhost:9000/config/apps/"+appID+"?"+param;
		
		String result = httpClientGet(requestUrl);
		int i = result.indexOf("appDetail:")+("appDetail:").length()+1;
		String xmlString = result.substring(i,result.length()-2);
		return xmlString;
	}
	
	public String getAccessToken (String code,String appID, String appSecret)
	{
		String requestUrl = "http://localhost:9000/oauth/accesstoken?authtoken="+code+"&appID="+appID+"&appSecret="+appSecret;
		String result = httpClientGet(requestUrl);
		String[] aa = result.split(",");
		String[] a = aa[0].split(":");
		String b = a[1].substring(1,a[1].length()-1);
		return b;
	}
	
	public String getSignalDefination(String signalName){
		String requestUrl = "http://localhost:9000/signal/querydef/"+signalName;
		String result = httpClientGet(requestUrl);
		return result;
	}
	
	public void ConfirmRouting(String accessToken, String routing){
		
		System.out.println("BEFORE "+routing);
		
		routing = replaceEnter(routing);
		
		System.out.println("AFTER "+routing);
		
		String requestHost = "http://localhost:9000/config/confirmrouting?"+"accessToken="+accessToken+"&"+"xml=";
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+routing;
		
		try{
			xmlString = URLEncoder.encode(xmlString, "utf-8");
		}catch(Exception e){
			System.out.println(e);
		}
		
		String requestUrl = requestHost + xmlString;
		
		System.out.println("REQUESTURL: "+requestUrl);
		
		String resultString = httpClientGet(requestUrl);
		System.out.println("ConfirmRouting returns "+resultString);
	}
	
	
	public RootApplication registerRootApp(String name, List<String> accessRequests){
		String requestUrlString = "http://localhost:9000/auth/appregister?email=fdsf";
		String result = httpClientGet(requestUrlString);
		System.out.println("registerRootApp returns "+result);
		
		String[] splitStrings = result.split(",");
		
		String idString = null;
		String secretString = null;
		
		for(String str:splitStrings){
			int indexOfId = str.indexOf("id");
			int indexOfSecret = str.indexOf("secret");
			
			if(indexOfId >= 0)
			{
				int start = str.indexOf(":")+2;
				int end = str.length()-1;
				idString = str.substring(start,end);
			}
			else if(indexOfSecret >= 0)
			{
				int start = str.indexOf(":")+2;
				int end = str.length()-1;
				secretString = str.substring(start,end);
			}
		}
		
		RootApplication rootApplication = new RootApplication(name,idString,secretString,accessRequests);
		
		return rootApplication;
	}
	
	public void installRootApp(String rootAppXmlString){
		
		String requestHost = "http://localhost:9000/clients/installapp?xml=";
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+rootAppXmlString;
		
		try{
			xmlString = URLEncoder.encode(xmlString, "utf-8");
		}catch(Exception e){
			System.out.println(e);
		}
		
		String requestUrl = requestHost + xmlString;
		
		System.out.println("REQUESTURL: "+requestUrl);
		
		String resultString = httpClientGet(requestUrl);
		System.out.println("installRootApp returns "+resultString);
	}
	
	public void userLogin(String username, String password)
	{
		String requestUrl = "http://localhost:9000/clients/login?username="+username+"&password="+password;
		String result = httpClientGet(requestUrl);
		System.out.println("userLogin method returns userID "+result);
	}
}
