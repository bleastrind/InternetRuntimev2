package config;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.InternetRTConfig;

public class properties {
	public static String appID = "cd648a93-3291-4973-a9c4-6a04c0a4c7b1";
	public static String appSecret ="2ad18c67-5db4-43e5-8ae5-8d92997fd89c";
	static {
		try {
			InternetRTConfig config = new InternetRTConfig();

			config.updatePropertiy("appID", appID);
			config.updatePropertiy("appSecret",
					appSecret);
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
		} catch (Exception e) {
			System.out.println("Internet Runtime Creation Failure!");
			e.printStackTrace();
		}
	}
	public static InternetRT irt;

//	public static String BaseUrl = "http://localhost:9000";
//	public static String AppInstallUrl = "http://localhost:9000";
//	public static String accessTokenURL = "http://localhost:9000/oauth/accesstoken";
//	public static String redirectUrl = "http://localhost:9001";
//	public static String codeURL = BaseUrl
//			+ "/oauth/authorize?appID=a6d2decd-868b-4a01-ab67-f94d946e9e08&redirect_uri="
//			+ redirectUrl;
}
