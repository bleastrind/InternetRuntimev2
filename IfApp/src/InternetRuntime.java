
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class InternetRuntime {
	
	public String httpClientForRT(String requestUrl)
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
	
	public List<String> getApps (String accessToken)
	{
		String param = "accessToken="+accessToken;
		List<String> applicationsIDList = new ArrayList<String>();
		String requestUrl = "http://localhost:9000/config/apps"+"?"+param;
		String xmlsID = httpClientForRT(requestUrl);
		applicationsIDList = parserXmlsIDString(xmlsID);
		return applicationsIDList;
	}
	
	public String getAppDetail (String appID, String accessToken)
	{
		String param = "accessToken="+accessToken;
		
		String requestUrl = "http://localhost:9000/config/apps/"+appID+"?"+param;
		String result = httpClientForRT(requestUrl);
		int i = result.indexOf("appDetail:")+("appDetail:").length()+1;
		String xmlString = result.substring(i,result.length()-2);
		return xmlString;
	}
	
	public String getAccessToken (String code,String appID, String appSecret)
	{
		String requestUrl = "http://localhost:9000/oauth/accesstoken?authtoken="+code+"&appID="+appID+"&appSecret="+appSecret;
		String result = httpClientForRT(requestUrl);
		String[] aa = result.split(",");
		String[] a = aa[0].split(":");
		String b = a[1].substring(1,a[1].length()-1);
		return b;
	}
	
	public String getSignalDefination(String signalName){
		String requestUrl = "http://localhost:9000/signal/querydef/"+signalName;
		String result = httpClientForRT(requestUrl);
		return result;
	}
	
	private String replaceEnter(String str){
		Pattern pattern = Pattern.compile("\n");
		Matcher matcher = pattern.matcher(str);
		String result = matcher.replaceAll("");
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
		
		String resultString = httpClientForRT(requestUrl);
		System.out.println("ConfirmRouting returns "+resultString);
	}
}
