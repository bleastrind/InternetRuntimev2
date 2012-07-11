package org.internetrt.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
	


	public String getAuthCodeUrl(){
		return internetRTConfig.getValue(Props.AUTHURL)
		+ "?appID=" + internetRTConfig.getValue(Props.APPID)
		+"&redirect_uri="+ internetRTConfig.getValue(Props.REDIRECTURL);
	}

	public String setAccessTokenWithCode(String code) throws HttpException,
			IOException {
		System.out.println(internetRTConfig.getValue("accessTokenURL")
				+ "?"
				+ HttpHelper.generatorParamString(new Pair[] {
						new Pair("appID", internetRTConfig
								.getValue("appID")),
						new Pair("appSecret", internetRTConfig
								.getValue("appSecret")),
						new Pair("authtoken", code) }));
		String response  = HttpHelper.httpClientGet(internetRTConfig
				.getValue("accessTokenURL")
				+ "?"
				+ HttpHelper.generatorParamString(new Pair[] {
						new Pair("appID",
								internetRTConfig.getValue("appID")),
						new Pair("appSecret",
								internetRTConfig.getValue("appSecret")),
						new Pair("authtoken", code) }));
		System.out.print(response);
		JSONObject json = JSONObject.fromObject(response);
		String accesstoken = (String) json.get("access_token");
		return accesstoken;
	}

	public String getAuthCodeByRoutingInstanceID(String rid)
			throws HttpException, IOException {
		System.out.println(internetRTConfig.getValue("accessTokenURL")
				+ "?"
				+ HttpHelper.generatorParamString(new Pair[] {
						new Pair("appID", internetRTConfig
								.getValue("appID")),
						new Pair("appSecret", internetRTConfig
								.getValue("appSecret")),
						new Pair("rid", rid) }));
		String response =  HttpHelper.httpClientGet((internetRTConfig
				.getValue("routingInstanceURl") + "?" + HttpHelper.generatorParamString(new Pair[] {
				new Pair("appID", internetRTConfig.getValue("appID")),
				new Pair("appSecret",
						internetRTConfig.getValue("appSecret")),
				new Pair("rid", rid) })));
		System.out.print(response);
		JSONObject json = JSONObject.fromObject(response);
		return (String) json.get("access_token");
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
		String urlstr = new ListenerRequestGenerator(xml).generateSignalListenerUrl(adapter(sourceMap),null);
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
	}
	

	private Map<String, String> adapter(Map<String, String> sourceMap) {
		return sourceMap;
	}

	public String initActionFromThirdPart(String AccessToken,
			String signalname, Map<String, String> parameters)
			throws IOException {

		System.out.println(HttpHelper.generatorParamString(parameters));
		String url = internetRTConfig.getValue("baseURL")
				+ "/signal/init/thirdpart/" + signalname + "?"
				+ "access_token=" + AccessToken + "&"
				+ HttpHelper.generatorParamString(parameters);
		System.out.println(url);
		
		return HttpHelper.httpClientGet(url);
	}


	public List<String> getApps(String accessToken) {
		String param = "accessToken=" + accessToken;
		List<String> applicationsIDList = new ArrayList<String>();
		String requestUrl = "http://localhost:9000/config/apps" + "?" + param;
		String xmlsID = HttpHelper.httpClientGet(requestUrl);
		applicationsIDList = parserXmlsIDString(xmlsID);
		return applicationsIDList;
	}

	public String getAppDetail(String appID, String accessToken) {
		String param = "accessToken=" + accessToken;

		String requestUrl = "http://localhost:9000/config/apps/" + appID + "?"
				+ param;
		String result = HttpHelper.httpClientGet(requestUrl);
		int i = result.indexOf("appDetail:") + ("appDetail:").length() + 1;
		String xmlString = result.substring(i, result.length() - 2);
		return xmlString;
	}

	public String getAccessToken(String code, String appID, String appSecret) {
		String requestUrl = "http://localhost:9000/oauth/accesstoken?authtoken="
				+ code + "&appID=" + appID + "&appSecret=" + appSecret;
		String result = HttpHelper.httpClientGet(requestUrl);
		String[] aa = result.split(",");
		String[] a = aa[0].split(":");
		String b = a[1].substring(1, a[1].length() - 1);
		return b;
	}

	public String getSignalDefination(String signalName) {
		String requestUrl = "http://localhost:9000/signal/querydef/"
				+ signalName;
		String result = HttpHelper.httpClientGet(requestUrl);
		return result;
	}

	public void ConfirmRouting(String accessToken, String routing) {

		System.out.println("BEFORE " + routing);

		routing = replaceEnter(routing);

		System.out.println("AFTER " + routing);

		String requestHost = "http://localhost:9000/config/confirmrouting?"
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

		String resultString = HttpHelper.httpClientGet(requestUrl);
		System.out.println("ConfirmRouting returns " + resultString);
	}

	public boolean installApp(String token,String xml){
		Map<String,String> parameters = new HashMap();
		parameters.put("accessToken", token);
		parameters.put("xml", xml);
		System.out.println("******************"+internetRTConfig.getValue("baseURL")+
				"/config/installapp?"+
				HttpHelper.generatorParamString(parameters));
		return Boolean.parseBoolean(
				HttpHelper.httpClientGet(
						internetRTConfig.getValue("baseURL")+
						"/config/installapp?"+
						HttpHelper.generatorParamString(parameters)
						)
				);
	
	}
	
	public Map<String,String> appregister(String email){
		Map<String,String> map = new HashMap();
		String result = HttpHelper.httpClientGet(internetRTConfig.getValue("baseURL")+"/auth/appregister?email="+email);
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		map.put("id",(String) json.get("id"));
		map.put("secret", (String)json.get("secret"));
		return map;
	}
	
	public static void main(String args[]) {

	}

}
