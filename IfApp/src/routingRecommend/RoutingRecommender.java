package routingRecommend;

import java.util.ArrayList;
import java.util.List;

import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.Signal;

import ScriptEditor.InternetRuntime;

public class RoutingRecommender {
	
	private InternetRuntime rt;
	
	public RoutingRecommender(){
		rt = new InternetRuntime();
	}
	
	public void RecomRoutingBaseFrom(String fromAppID, String accessToken){
		//根据ID找到此App的文件
		List<Application> toAppList = new ArrayList<Application>();
		
		String appXmlString = rt.getAppDetail(fromAppID, accessToken);
		
		AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
		List<Signal> requests = appXmlParser.getRequests();
		
		//Parser requestHandleApp for each request
		for(Signal signal:requests){
			List<String> runatList = appXmlParser.getRequestListenerForRequest(signal.name());
			for(String runatAppID : runatList){
				String toAppXmlString = rt.getAppDetail(runatAppID, accessToken);
				AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
				Application application = toAppXmlString.creatApplication();
				toAppList.add(application);
			}
		}
		
	}
	public void recomRoutingBaseTo(String accessToken){
		//get 用户的所有app
		
		List<Application> allApps = new ArrayList<Application>();
		
		List<String> allAppsIdList = rt.getApps(accessToken);
		
		for(String appId : allAppsIdList){
			String appXmlString = rt.getAppDetail(appId, accessToken);
			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
			Application application = appXmlParser.createApplication();
			
		}
	}
}
