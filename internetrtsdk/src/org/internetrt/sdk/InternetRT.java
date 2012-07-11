package org.internetrt.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import scala.Tuple2;

import org.internetrt.sdk.util.*;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author sinaWeibo
 * 
 */
public class InternetRT {
	public static class Props{
		public static String APPID = "appID";
		public static String APPSECRET = "appSecret";
		public static String REDIRECTURL = "redirect_URI";
		public static String BASEURL = "baseURL";
		public static String ACCESSTOKENURL = "accessTokenURL";
		public static String ROUTINGINSTANCE = "routingInstanceURl";
		public static String AUTHURL = "authorizeURL";
	}
	public static InternetRT create(InternetRTConfig config) throws Exception{
		String values = "appID,appSecret,redirect_URI,baseURL,accessTokenURL,routingInstanceURl,authorizeURL";
		if(!config.containsAllProperties(Arrays.asList(values.split(","))))
			throw new Exception("The configuration don't have all the properties of:"+values);
		InternetRT rt = new InternetRT();
		rt.internetRTConfig = config;
		return rt;
	}
	
	private InternetRTConfig internetRTConfig ; 
	
	// Forbidden new a InternetRT with not well prepared config
	private InternetRT(){}	
	
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

	public String getAuthCodeUrl(){
		return internetRTConfig.getValue(Props.AUTHURL)
		+ "?appID=" + internetRTConfig.getValue(Props.APPID)
		+"&redirect_uri="+ internetRTConfig.getValue(Props.REDIRECTURL);
	}

	public String setAccessTokenWithCode(String code) throws HttpException,
			IOException {
		String response  = httpClientGet(internetRTConfig
				.getValue("accessTokenURL")
				+ "?"
				+ generatorParam2String(new Tuple2[] {
						new Tuple2("appID",
								internetRTConfig.getValue("appID")),
						new Tuple2("appSecret",
								internetRTConfig.getValue("appSecret")),
						new Tuple2("authtoken", code) }));
		System.out.print(response);
		JSONObject json = JSONObject.fromObject(response);
		String accesstoken = (String) json.get("access_token");
		return accesstoken;
	}

	public String getAuthCodeByRoutingInstanceID(String rid)
			throws HttpException, IOException {
		String response =  httpClientGet(internetRTConfig.getValue("baseURL")+"/oauth/tokenbyrid"
				+ "?"
				+ generatorParam2String(new Tuple2[] {
						new Tuple2("appID", internetRTConfig
								.getValue("appID")),
						new Tuple2("appSecret", internetRTConfig
								.getValue("appSecret")),
						new Tuple2("rid", rid) }));
		System.out.print(response);
		JSONObject json = JSONObject.fromObject(response);
		return (String) json.get("code");
	}

	public String authorize(String response_type) {
		return internetRTConfig.getValue("authorizeURL").trim() + "?client_id="
				+ internetRTConfig.getValue("client_ID").trim()
				+ "&redirect_uri="
				+ internetRTConfig.getValue("redirect_URI").trim()
				+ "&response_type=" + response_type;
	}

	public void send(String accesstoken, String from, String signalName,
			Map<String, String> sourceMap) throws IOException {

		String xml = initActionFromThirdPart(accesstoken, signalName, sourceMap);
		System.out.println(xml);
		RoutingXmlParser parser = new RoutingXmlParser(xml);
		sourceMap.put("rid", parser.getRoutingInstanceId());
		System.out.println(parser.getReqUrl()+"?"+ generatorParamString(Adapter(sourceMap)));
		String url = new String(parser.getReqUrl()
				+ "?"+generatorParamString(Adapter(sourceMap)));
		System.out.println("weibo"+httpClientGet(url));
	}

	private Map<String, String> Adapter(Map<String, String> sourceMap) {
		return sourceMap;
	}

	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext())
					params.append("&");
			}
		}
		return params.toString();
	}

	public String generatorParam2String(Tuple2[] postParameters) {
		String param = "";
		int i = 0;
		for (Tuple2 entry : postParameters) {
			if (i != 0)
				param += "&" + entry._1() + "="
						+ entry._2().toString();
			else
				param += entry._1() + "=" + entry._2().toString();
			i++;
		}
		return param;
	}

	public String initActionFromThirdPart(String AccessToken,
			String signalname, Map<String, String> parameters)
			throws IOException {

		System.out.println(generatorParamString(parameters));
		URL url = new URL(internetRTConfig.getValue("baseURL")
				+ "/signal/init/thirdpart/" + signalname + "?"
				+ "access_token=" + AccessToken + "&"
				+ generatorParamString(parameters));
		System.out.println(url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// conn.

		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String str = "", t = null;
		while ((t = br.readLine()) != null) {
			str += t + "\n";
		}
		return str;
	}

	public String httpClientPost(String url, Map<String, String> params) {
		String response = null;
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		// Set Http Post Data
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				response = method.getResponseBodyAsString();
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Http Post:" + url + "\terror:" + e);
		} finally {
			method.releaseConnection();
		}

		return response;
	}

	public List<String> getApps(String accessToken) {
		String param = "accessToken=" + accessToken;
		List<String> applicationsIDList = new ArrayList<String>();
		String requestUrl = internetRTConfig.getValue("baseURL")+"/config/apps" + "?" + param;
		String xmlsID = httpClientGet(requestUrl);
		applicationsIDList = parserXmlsIDString(xmlsID);
		return applicationsIDList;
	}

	public String getAppDetail(String appID, String accessToken) {
		String param = "accessToken=" + accessToken;

		String requestUrl = internetRTConfig.getValue("baseURL")+"/config/apps/" + appID + "?"
				+ param;
		String result = httpClientGet(requestUrl);
		int i = result.indexOf("appDetail:") + ("appDetail:").length() + 1;
		String xmlString = result.substring(i, result.length() - 2);
		return xmlString;
	}

	public String getAccessToken(String code, String appID, String appSecret) {
		String requestUrl = internetRTConfig.getValue("baseURL")+"/oauth/accesstoken?authtoken="
				+ code + "&appID=" + appID + "&appSecret=" + appSecret;
		String result = httpClientGet(requestUrl);
		String[] aa = result.split(",");
		String[] a = aa[0].split(":");
		String b = a[1].substring(1, a[1].length() - 1);
		return b;
	}

	public String getSignalDefination(String signalName) {
		String requestUrl = internetRTConfig.getValue("baseURL")+"/signal/querydef/"
				+ signalName;
		String result = httpClientGet(requestUrl);
		return result;
	}

	public void ConfirmRouting(String accessToken, String routing) {

		System.out.println("BEFORE " + routing);

		routing = replaceEnter(routing);

		System.out.println("AFTER " + routing);

		String requestHost = internetRTConfig.getValue("baseURL")+"/config/confirmrouting?"
				+ "accessToken=" + accessToken + "&" + "xml=";
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ routing;

		try {
			xmlString = URLEncoder.encode(xmlString, "utf-8");
		} catch (Exception e) {
			System.out.println(e);
		}

		String requestUrl = requestHost + xmlString;

		System.out.println("REQUESTURL: " + requestUrl);

		String resultString = httpClientGet(requestUrl);
		System.out.println("ConfirmRouting returns " + resultString);
	}

	public boolean installApp(String token,String xml){
		Map<String,String> parameters = new HashMap();
		parameters.put("accessToken", token);
		parameters.put("xml", xml);
		System.out.println("******************"+internetRTConfig.getValue("baseURL")+
				"/config/installapp?"+
				generatorParamString(parameters));
		return Boolean.parseBoolean(
				httpClientGet(
						internetRTConfig.getValue("baseURL")+
						"/config/installapp?"+
						generatorParamString(parameters)
						)
				);
	
	}
	
	public Map<String,String> appregister(String email){
		Map<String,String> map = new HashMap();
		String result = httpClientGet(internetRTConfig.getValue("baseURL")+"/auth/appregister?email="+email);
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		map.put("id",(String) json.get("id"));
		map.put("secret", (String)json.get("secret"));
		return map;
	}
	
	public  String getUserIdByToken(String Token){
		
		String result = httpClientGet(internetRTConfig.getValue("baseURL")+"/oauth/getuserid/"+Token);
		JSONObject json = JSONObject.fromObject(result);
		System.out.println(json.get("user_id"));
		return (String) json.get("user_id");
	}
	public static void main(String args[]) throws Exception {
			InternetRT irt ;
			InternetRTConfig config = new InternetRTConfig();

			config.updatePropertiy("appID", "cd648a93-3291-4973-a9c4-6a04c0a4c7b1");
			config.updatePropertiy("appSecret",
					"2ad18c67-5db4-43e5-8ae5-8d92997fd89c");
			config.updatePropertiy("redirect_URI",
					"http://127.0.0.1:9001/Application/loginUser"); //Play 1.0 and Play 2.0 will conflict on session, if domain is same & port is different
			config.updatePropertiy("baseURL", "http://localhost:9000");
			config.updatePropertiy("accessTokenURL",
					"http://localhost:9000/oauth/accesstoken");
			config.updatePropertiy("routingInstanceURl",
					"http://localhost:9000/oauth/workflow");
		config.updatePropertiy("authorizeURL",
					"http://localhost:9000/oauth/authorize");
		
		irt = InternetRT.create(config);
		Map<String,String> map = new HashMap();
		map.put("message", "Œ“ «Ω© ¨∑€");
		String token = irt.setAccessTokenWithCode("e5c28f61-c8a6-412c-8438-c44f5e563796");
		System.out.println(irt.getUserIdByToken(token));
		//irt.ConfirmRouting(token, "");
		System.out.println(token);
		irt.send(token, "cd648a93-3291-4973-a9c4-6a04c0a4c7b1", "share", map);
		System.out.println(irt.getAuthCodeByRoutingInstanceID("0ef9666b-7380-4db3-ac28-6ee614142222"));
	}

}
