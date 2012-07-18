package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.DescribedListenerConfig;
import org.internetrt.sdk.util.ListenerConfig;
import org.internetrt.sdk.util.Signal;

import cn.edu.act.internetos.appmarket.service.TermToJson;

public class RoutingRecommender {
	
	private InternetRT rt = config.properties.irt;

	public List<scala.Tuple2<Signal,DescribedListenerConfig>> getPossibleRoutings(String fromAppID, String accessToken){
		
		String appXmlString = rt.getAppDetail(fromAppID, accessToken);
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa"+appXmlString);
		
		List<scala.Tuple2<Signal,DescribedListenerConfig>> a = new ArrayList<scala.Tuple2<Signal,DescribedListenerConfig>>();
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		
		addNewListeners(accessToken, appXmlParser, a);
		addNewSignalSource(accessToken, appXmlParser,a);

		return a;
	}
	private void addNewSignalSource(String accessToken, AppXmlParser appXmlParser,
			List<scala.Tuple2<Signal,DescribedListenerConfig>> a) {
		List<DescribedListenerConfig> listeners = appXmlParser.getListeners();
		for(DescribedListenerConfig config: listeners){
			List<String> allAppsList = rt.getApps(accessToken);

			for(String appID: allAppsList){
				String fromAppXmlString = rt.getAppDetail(appID, accessToken);
				AppXmlParser fromAppXmlParser = new AppXmlParser(fromAppXmlString);
				List<Signal> signals = fromAppXmlParser.getMatchedRequestSignals(config);
				for(Signal signal:signals){
					a.add(new scala.Tuple2<Signal,DescribedListenerConfig>(signal,config));
				}
			}
		}
		
	}
	private void addNewListeners(String accessToken, AppXmlParser appXmlParser,
			List<scala.Tuple2<Signal,DescribedListenerConfig>> a) {
		List<Signal> requests = appXmlParser.getRequests();

		//Parser requestHandleApp for each request
		for(Signal signal:requests){
			
			String signalName = signal.name();
			System.out.println("signal"+signalName);
			List<String> allAppsList = rt.getApps(accessToken);
			
			for(String appID: allAppsList){
				String toAppXmlString = rt.getAppDetail(appID, accessToken);
				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
				List<DescribedListenerConfig> listenerList = toAppXmlParser.getMatchedListeners(signalName);
				for(DescribedListenerConfig config:listenerList){
					a.add(new scala.Tuple2(signal,config));
				}
			}
		}
	}
//	public String recomRoutingBaseTo(String toAppId, String accessToken){
//		//get 鐢ㄦ埛鐨勬墍鏈塧pp
//		String result = null;
//		
//		JSONArray fromAppList = new JSONArray();
//		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
//		List<String> exceptedSignals = appXmlParser.getExceptedSignals();
//		for(String exceptedSignal : exceptedSignals){
//		}
//		
////		List<String> allAppsIdList = rt.getApps(accessToken);
////		
////		System.out.println("allAppsIdList length "+allAppsIdList.size());
////		
////		for(String appId : allAppsIdList){
////			String appXmlString = rt.getAppDetail(appId, accessToken);
////			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
////			
////			System.out.println("appXmlStringaaaaaaaaaaaaaa"+appXmlString);
////			
////			String fromAppIdBaseTo = appXmlParser.getAppIdBaseRunat(toAppId);
////			
////			System.out.println("fromAppIdBaseTobbbbbbbbbbbbb"+fromAppIdBaseTo);
////			
////			if(fromAppIdBaseTo.length()>0){
////				List<String> signalsNameList = new ArrayList<String>();
////				signalsNameList = appXmlParser.getSignalNameBaseRunat(toAppId);
////				JSONArray signalsNameArray = new JSONArray(signalsNameList);
////				Application application = appXmlParser.createApplication();
//				JSONObject appJson = TermToJson.ApplicationToJson(application);
//				JSONObject temp = TermToJson.appAndSignalListToJson(appJson, signalsNameArray);
//				fromAppList.put(temp);
//					result = TermToJson.jsonArrayToJsonObject("fromAppList", fromAppList).toString();
//			}
//		}
//		
//		result = fromAppList.toString();
//		
//		return result;
//	}
}
