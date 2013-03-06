package cn.edu.act.internetos.appmarket.service;

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

import models.*;


public class RoutingRecommender {
	
	private InternetRT rt = config.properties.irt;

	public List<scala.Tuple3<String,Signal,DescribedListenerConfig>> getPossibleRoutings(String fromAppID, String accessToken){
		
		String appXmlString = rt.getAppDetail(fromAppID, accessToken);
		
		List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a = new ArrayList<scala.Tuple3<String,Signal,DescribedListenerConfig>>();
		List<scala.Tuple3<String,Signal,DescribedListenerConfig>> ans = new ArrayList<scala.Tuple3<String,Signal,DescribedListenerConfig>>();
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		
		addNewListeners(accessToken, appXmlParser, a);
		addNewSignalSource(accessToken, appXmlParser,a);
		return a;
	}
	
	public List<scala.Tuple3<String,Signal,DescribedListenerConfig>> getUserRoutings(String accessToken){
		
		List<App> applist = AppService.getUserApps(accessToken);
		List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a = new ArrayList<scala.Tuple3<String,Signal,DescribedListenerConfig>>();
		for (App app:applist){
			String appXmlString = rt.getAppDetail(app.getId(), accessToken);
			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
			addNewListeners(accessToken, appXmlParser, a);
			addNewSignalSource(accessToken, appXmlParser,a);
		}
		return a;
	}
	
	private void addNewSignalSource(String accessToken, AppXmlParser appXmlParser,
			List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a) {
		List<DescribedListenerConfig> listeners = appXmlParser.getListeners();
		
		for(DescribedListenerConfig config: listeners){
			List<String> allAppsList = rt.getApps(accessToken);
			
			for(String appID: allAppsList){
				String fromAppXmlString = rt.getAppDetail(appID, accessToken);
				AppXmlParser fromAppXmlParser = new AppXmlParser(fromAppXmlString);
				System.out.println("[RoutingRecommender : addNewSignalSource]:"+config.node());
				List<Signal> signals = fromAppXmlParser.getMatchedRequestSignals(config);
				for(Signal signal:signals){
					a.add(new scala.Tuple3<String, Signal,DescribedListenerConfig>(fromAppXmlParser.getAppName(),signal,config));
				}
			}
		}
		
	}
	private void addNewListeners(String accessToken, AppXmlParser appXmlParser,
			List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a) {
		List<Signal> requests = appXmlParser.getSignals();

		//Parser requestHandleApp for each request
		for(Signal signal:requests){
			
			String signalName = signal.name();
			System.out.println("[RoutingRecommender : addNewListeners]: "+"signal"+signalName);
			List<String> allAppsList = rt.getApps(accessToken);
			
			for(String appID: allAppsList){
				String toAppXmlString = rt.getAppDetail(appID, accessToken);
				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
				List<DescribedListenerConfig> listenerList = toAppXmlParser.getMatchedListeners(signalName);
				for(DescribedListenerConfig config:listenerList){
					a.add(new scala.Tuple3(appXmlParser.getAppName(),signal,config));
				}
			}
		}
	}
}
