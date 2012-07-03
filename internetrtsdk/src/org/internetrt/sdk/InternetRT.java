package org.internetrt.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.internetrt.sdk.util.*;

import weibo4j.http.AccessToken;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.util.InternetRTConfig;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author sinaWeibo
 * 
 */

public class InternetRT implements java.io.Serializable {

	private static final long serialVersionUID = 4282616848978535016L;

	public HttpClient client = new HttpClient();
	private String accesstoken;
	/**
	 * Sets token information
	 * 
	 * @param token
	 */
	public synchronized void setToken(String token) {
		accesstoken = token;
	}
	
	private byte[] httpClientForRT(String requestUrl)
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
		return responseBody;
	}
	
	
	public String setAccessTokenWithCode(String code) throws HttpException, IOException {
		   System.out.println(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("authtoken", code)}));
		   byte[] responseBody = httpClientForRT(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
						new PostParameter("appID", InternetRTConfig
								.getValue("appID")),
						new PostParameter("appSecret", InternetRTConfig
								.getValue("appSecret")),
						new PostParameter("authtoken", code)}));
		   String response = new String(responseBody);
		   System.out.print(response);
		   JSONObject json = JSONObject.fromObject(response);
		   accesstoken = (String) json.get("access_token");
		   return accesstoken;
	}
	
	public String getAuthCodeByRoutingInstanceID(String rid) throws HttpException, IOException{
		System.out.println(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("rid", rid)}));
		   byte[] responseBody = httpClientForRT((InternetRTConfig.getValue("routingInstanceURl")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("rid", rid)})));
		   String response = new String(responseBody);
		   System.out.print(response);
		   JSONObject json = JSONObject.fromObject(response);
		setToken((String) json.get("access_token"));
		return (String) json.get("access_token");
	}
	
	public String authorize(String response_type) throws WeiboException {
		return InternetRTConfig.getValue("authorizeURL").trim() + "?client_id="
				+ InternetRTConfig.getValue("client_ID").trim() + "&redirect_uri="
				+ InternetRTConfig.getValue("redirect_URI").trim()
				+ "&response_type=" + response_type;
	}
	
	public void send(String from, String signalName, Map<String,String> sourceMap) throws IOException{

		//解析xml
		//sourceMap to targetMap
		String xml = initActionFromThirdPart(accesstoken,signalName,sourceMap);
		System.out.println(xml);
		AppXmlParser parser = new AppXmlParser(xml);
		URL url = new URL(parser.getUrl(signalName)+generatorParamString(Adapter(sourceMap)));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.getOutputStream();
		//发出请求
	}
	
	private Map<String,String> Adapter(Map<String,String> sourceMap){
		return  sourceMap;
	}
	
	public static String generatorParamString(Map<String, String> parameters) {
        StringBuffer params = new StringBuffer();
        if (parameters != null) {
            for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
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
                if (iter.hasNext()) params.append("&");
            }
        }
        return params.toString();
    }
	
	public String getAccessToken (String code,String appID, String appSecret)
	{
		String requestUrl = "http://localhost:9000/oauth/accesstoken?authtoken="+code+"&appID="+appID+"&appSecret="+appSecret;
		byte[]responseBody = httpClientForRT(requestUrl);
		String result = new String(responseBody);
		String[] aa = result.split(",");
		String[] a = aa[0].split(":");
		String b = a[1].substring(1,a[1].length()-1);
		return b;
	}
	
	public String generatorParam2String(PostParameter[] postParameters){
		String param="";
		int i = 0;
		for (PostParameter entry : postParameters){
			if (i!=0) param+="&"+entry.getName()+"="+entry.getValue().toString();
			else param+=entry.getName()+"="+entry.getValue().toString();
			i++;
		}
		return param;
	}
	
	public String initActionFromThirdPart(String AccessToken,String signalname,Map<String, String> parameters) throws IOException{
		
		System.out.println(generatorParamString(parameters));
		URL url = new URL(InternetRTConfig.getValue("baseURL")+"/signal/init/thirdpart/"+signalname+"?"+"access_token="
				+AccessToken+"&"+generatorParamString(parameters));
		System.out.println(url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//conn.
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String str = "",t = null;
		while ((t = br.readLine())!=null){
			str+=t+"\n";
		}
		return str;
	}
	
}
