package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.ListenerConfig;
import org.internetrt.sdk.util.Signal;

import cn.edu.act.internetos.appmarket.service.TermToJson;



public class RoutingRecommender {
	
	private InternetRT rt = config.properties.irt;

	public String RecomRouting (String appID, String accessToken){
		String result = null;
		
		return null;
	}
	public String RecomRoutingBaseFrom(String fromAppID, String accessToken){
		
		String resultString = null;
		
		JSONArray resultArray = new JSONArray();
		
		String appXmlString = rt.getAppDetail(fromAppID, accessToken);
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa"+appXmlString);
		
		List<Entry<String,ListenerConfig>> a = new ArrayList<Entry<String,ListenerConfig>>();
		
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		List<Signal> requests = appXmlParser.getRequests();

		//Parser requestHandleApp for each request
		for(Signal signal:requests){
			
			String signalName = signal.name();
			System.out.println("signal"+signalName);
			List<String> allAppsList = rt.getApps(accessToken);
			
			for(String appID: allAppsList){
				String toAppXmlString = rt.getAppDetail(appID, accessToken);
				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
				List<ListenerConfig> listenerList = appXmlParser.getMatchedListeners(signalName);
				for(ListenerConfig config:listenerList){
					a.add(new Entry(signalName,config));
				}
			}
		}
//			List<String> runatList = appXmlParser.getRequestListenerForRequest(signalName);
//			JSONArray toAppList= new JSONArray();
//			
//			for(String runatAppID : runatList){
//				
//				System.out.println("RUNATAPPID"+runatAppID);
//				
//				String toAppXmlString = rt.getAppDetail(runatAppID, accessToken);
//				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
//				Application application = toAppXmlParser.createApplication();
//				JSONObject applicationJsonObject = TermToJson.ApplicationToJson(application);
//				toAppList.put(applicationJsonObject);
//			}
			
			if(runatList.size()>0){
				JSONObject tempObject = TermToJson.signalAndAppListToJson(signalName, toAppList);
				resultString = (resultArray.put(tempObject)).toString();
			}
		}
		return resultString;
	}
	public String recomRoutingBaseTo(String toAppId, String accessToken){
		//get 鐢ㄦ埛鐨勬墍鏈塧pp
		String result = null;
		
		JSONArray fromAppList = new JSONArray();
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		List<String> exceptedSignals = appXmlParser.getExceptedSignals();
		for(String exceptedSignal : exceptedSignals){
		}
		
//		List<String> allAppsIdList = rt.getApps(accessToken);
//		
//		System.out.println("allAppsIdList length "+allAppsIdList.size());
//		
//		for(String appId : allAppsIdList){
//			String appXmlString = rt.getAppDetail(appId, accessToken);
//			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
//			
//			System.out.println("appXmlStringaaaaaaaaaaaaaa"+appXmlString);
//			
//			String fromAppIdBaseTo = appXmlParser.getAppIdBaseRunat(toAppId);
//			
//			System.out.println("fromAppIdBaseTobbbbbbbbbbbbb"+fromAppIdBaseTo);
//			
//			if(fromAppIdBaseTo.length()>0){
//				List<String> signalsNameList = new ArrayList<String>();
//				signalsNameList = appXmlParser.getSignalNameBaseRunat(toAppId);
//				JSONArray signalsNameArray = new JSONArray(signalsNameList);
//				Application application = appXmlParser.createApplication();
//				JSONObject appJson = TermToJson.ApplicationToJson(application);
//				JSONObject temp = TermToJson.appAndSignalListToJson(appJson, signalsNameArray);
//				fromAppList.put(temp);
////				result = TermToJson.jsonArrayToJsonObject("fromAppList", fromAppList).toString();
			}
		}
		
		result = fromAppList.toString();
		
		return result;
	}
}
