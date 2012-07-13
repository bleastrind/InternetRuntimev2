package routingRecommend;

import java.util.ArrayList;
import java.util.List;


import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.Signal;
import org.json.JSONArray;
import org.json.JSONObject;


import ScriptEditor.InternetRuntime;
import ScriptEditor.TermToJson;

public class RoutingRecommender {
	
	private InternetRuntime rt;
	
	public RoutingRecommender(){
		rt = new InternetRuntime();
	}

	public String RecomRoutingBaseFrom(String fromAppID, String accessToken){
		//根据ID找到此App的文件
		String resultString = null;
		
		JSONArray resultArray = new JSONArray();
		
		String appXmlString = rt.getAppDetail(fromAppID, accessToken);
		
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		List<Signal> requests = appXmlParser.getRequests();

		//Parser requestHandleApp for each request
		for(Signal signal:requests){
			
			String signalName = signal.name();
			
			List<String> runatList = appXmlParser.getRequestListenerForRequest(signalName);
			JSONArray toAppList= new JSONArray();
			
			for(String runatAppID : runatList){
				
				System.out.println("RUNATAPPID"+runatAppID);
				
				String toAppXmlString = rt.getAppDetail(runatAppID, accessToken);
				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
				Application application = toAppXmlParser.createApplication();
				JSONObject applicationJsonObject = TermToJson.ApplicationToJson(application);
				toAppList.put(applicationJsonObject);
			}
			
			if(runatList.size()>0){
				JSONObject tempObject = TermToJson.signalAndAppListToJson(signalName, toAppList);
				resultString = (resultArray.put(tempObject)).toString();
			}
		}
		return resultString;
	}
	public String recomRoutingBaseTo(String toAppId, String accessToken){
		//get 用户的所有app
		String result = null;
		
		JSONArray fromAppList = new JSONArray();
		
		List<String> allAppsIdList = rt.getApps(accessToken);
		
		System.out.println("allAppsIdList length "+allAppsIdList.size());
		
		for(String appId : allAppsIdList){
			String appXmlString = rt.getAppDetail(appId, accessToken);
			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
			
			System.out.println("appXmlStringaaaaaaaaaaaaaa"+appXmlString);
			
			String fromAppIdBaseTo = appXmlParser.getAppIdBaseRunat(toAppId);
			
			System.out.println("fromAppIdBaseTobbbbbbbbbbbbb"+fromAppIdBaseTo);
			
			if(fromAppIdBaseTo.length()>0){
				List<String> signalsNameList = new ArrayList<String>();
				signalsNameList = appXmlParser.getSignalNameBaseRunat(toAppId);
				JSONArray signalsNameArray = new JSONArray(signalsNameList);
				Application application = appXmlParser.createApplication();
				JSONObject appJson = TermToJson.ApplicationToJson(application);
				JSONObject temp = TermToJson.appAndSignalListToJson(appJson, signalsNameArray);
				fromAppList.put(temp);
//				result = TermToJson.jsonArrayToJsonObject("fromAppList", fromAppList).toString();
			}
		}
		
		result = fromAppList.toString();
		
		return result;
	}
}
