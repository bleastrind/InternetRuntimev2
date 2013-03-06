package org.internetrt.driver;

import org.internetrt.exceptions.ConsideredException;

import play.mvc.Result;
import play.mvc.Controller;

public class ConfigController extends Controller {

	/**
	 * The API to get applications'name for a specified user
	 * 
	 * @param accessToken
	 * @return
	 */
	public static Result appsList() {
		try {
			String accessToken = request().queryString().get("accessToken")[0];

			scala.collection.Seq<String> a = org.internetrt.SiteInternetRuntime
					.getApplications(accessToken);
			;
			java.util.List<String> javaapps = scala.collection.JavaConversions
					.seqAsJavaList(a);

			StringBuilder applicationString = new StringBuilder();
			int i = 0;
			for (String app : javaapps) {
				if (i != 0) {
					applicationString.append(",");
				}
				applicationString.append(app);
				i++;
			}
			return ok("{applications:" + applicationString.toString() + "}");
		} catch (Exception e) {
			return badRequest(e.getMessage());
		}
	}

	/**
	 * The API to get detailed content is as specified application's xml file
	 * 
	 * @param appID
	 * @return
	 */
	public static Result appDetail(String appID) {
		String accessToken = request().queryString().get("accessToken")[0];
		String appDetailString = org.internetrt.SiteInternetRuntime
				.getApplicationDetail(appID, accessToken).xml().toString();
		return ok("{appDetail:\"" + appDetailString + "\"}");
	}

	public static Result installApplication() {
		String accessToken = request().queryString().get("accessToken")[0];
		String xml = request().queryString().get("xml")[0];
		Boolean success = org.internetrt.SiteInternetRuntime
				.installApplication(accessToken, xml);
		return ok(success.toString());
	}

	public static Result confirmRouting() {
		String accessToken = request().queryString().get("accessToken")[0];
		String xml = request().queryString().get("xml")[0];
		Boolean success = org.internetrt.SiteInternetRuntime.confirmRouting(
				accessToken, xml);
		return ok(success.toString());
	}
}
