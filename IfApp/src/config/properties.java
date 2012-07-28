package config;
import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.InternetRTConfig;

public class properties {
	public static String appID = "bd19541f-7996-4d05-be56-1c0b5b10317a";   //canssadra in 3.145 "12dd85a3-5ae1-434b-82ef-26cf5186386f";
	public static String appSecret = "b50e83b5-aa48-4a1f-b2d3-954d6c45783a"; //"ecb54b2b-6c31-41c6-bc77-e8f43115cce2";
	static {
		try {
			InternetRTConfig config = new InternetRTConfig();

			config.updatePropertiy("appID", appID);
			config.updatePropertiy("appSecret",
					appSecret);
			config.updatePropertiy("redirect_URI",
					"http://internetrt.org:8080/IfApp/accessTokenServlet"); //Play 1.0 and Play 2.0 will conflict on session, if domain is same & port is different
			config.updatePropertiy("baseURL", "http://internetrt.org:9000");
			config.updatePropertiy("accessTokenURL",
					"http://internetrt.org:9000/oauth/accesstoken");
			config.updatePropertiy("routingInstanceURl",
					"http://internetrt.org:9000/oauth/workflow");
			config.updatePropertiy("authorizeURL",
					"http://internetrt.org:9000/oauth/authorize");
			System.out.println(InternetRT.class);
			irt = InternetRT.create(config);
		} catch (Exception e) {
			System.out.println("Internet Runtime Creation Failure!");
			e.printStackTrace();
		}
	}
	public static InternetRT irt;

}
