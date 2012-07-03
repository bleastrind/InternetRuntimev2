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

import org.internetrt.sdk.util.AppXmlParser;

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
	
	public void setAccessTokenWithCode(String code) throws HttpException, IOException {
		GetMethod getMethod = new GetMethod(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
						new PostParameter("appID", InternetRTConfig
								.getValue("appID")),
						new PostParameter("appSecret", InternetRTConfig
								.getValue("appSecret")),
						new PostParameter("authtoken", code)}));
		System.out.println(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("authtoken", code)}));
		int statusCode = client.executeMethod(getMethod);
		   if (statusCode != HttpStatus.SC_OK) {
		    System.err.println("Method failed: "
		      + getMethod.getStatusLine());
		   }
		   byte[] responseBody = getMethod.getResponseBody();
		   String response = new String(responseBody);
		   System.out.print(response);
		   JSONObject json = JSONObject.fromObject(response);
		
		   accesstoken = (String) json.get("access_token");
	}
	
	public String getAuthCodeByRoutingInstanceID(String rid) throws HttpException, IOException{
		GetMethod getMethod = new GetMethod(InternetRTConfig.getValue("routingInstanceURl")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("rid", rid)}));
		System.out.println(InternetRTConfig.getValue("accessTokenURL")+"?"+generatorParam2String(new PostParameter[] {
				new PostParameter("appID", InternetRTConfig
						.getValue("appID")),
				new PostParameter("appSecret", InternetRTConfig
						.getValue("appSecret")),
				new PostParameter("rid", rid)}));
		int statusCode = client.executeMethod(getMethod);
		   if (statusCode != HttpStatus.SC_OK) {
		    System.err.println("Method failed: "
		      + getMethod.getStatusLine());
		   }
		   byte[] responseBody = getMethod.getResponseBody();
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
	
	public static void main(String args[]) throws IOException, Exception{
		
		/*InternetRT irt = new InternetRT();
		irt.setAccessTokenWithCode("955b7313-b420-4da5-81c3-b7facad4942f");
		System.out.print(irt.accesstoken);
		irt.send("weibo","share",new HashMap());		*/
		
		//AppXmlParser parser = new AppXmlParser("RoutingInstance(7c633bbb-9138-41b4-a436-d8e88a7f43e5,<RoutingInstance> </RoutingInstance>)");
		
		InternetRT irt = new InternetRT();
		String token = "95aead89-d85e-43ce-bf28-5665d6601e24";
		irt.setToken(token);
		irt.send("weibo","share",new HashMap());
	
		
		/*InternetRT irt = new InternetRT();
		String accesstoken = irt.getAuthCodeByRoutingInstanceID(rid);
		*/
	}
	
}
