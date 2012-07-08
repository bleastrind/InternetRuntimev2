package config;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.InternetRTConfig;

public class properties {
	static {
		try {
			InternetRTConfig config = new InternetRTConfig();
			config.updatePropertiy("appID", "b781c1c6-2fbd-4d25-a3f7-3f3eaf133d51");
			config.updatePropertiy("appSecret",
					"bb5419ed-b35c-4cb5-9786-8eb364d5c81b");
			config.updatePropertiy("redirect_URI",
					"http://217.0.0.1:9001/Application/loginUser"); //Play 1.0 and Play 2.0 will conflict on session, if domain is same & port is different
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
