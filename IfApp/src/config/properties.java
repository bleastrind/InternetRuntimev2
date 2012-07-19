package config;
import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.InternetRTConfig;

public class properties {
	public static String appID = "12dd85a3-5ae1-434b-82ef-26cf5186386f";
	public static String appSecret ="ecb54b2b-6c31-41c6-bc77-e8f43115cce2";
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
			System.out.println(InternetRT.class);
			irt = InternetRT.create(config);
			
			System.out.println("321");

		} catch (Exception e) {
			System.out.println("Internet Runtime Creation Failure!");
			e.printStackTrace();
		}
	}
	public static InternetRT irt;

}
