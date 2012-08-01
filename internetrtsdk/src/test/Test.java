package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.internetrt.sdk.InternetRT;
import org.internetrt.sdk.InternetRTConfig;
import org.internetrt.sdk.exceptions.DataNotEnoughException;
import org.internetrt.sdk.util.AppXmlParser;
import org.internetrt.sdk.util.DescribedListenerConfig;
import org.internetrt.sdk.util.FreeRoutingGenerator;
import org.internetrt.sdk.util.ListenerConfig;
import org.internetrt.sdk.util.RoutingXmlParser;
import org.internetrt.sdk.util.Signal;


public class Test {

	/**
	 * @param args
	 * @throws DataNotEnoughException 
	 */
	public static void main(String[] args) throws DataNotEnoughException {
		// TODO Auto-generated method stub
		List<RoutingChoice> s= generateChoieces(new TestRoutingPredicate().getPossibleRoutings("",""));
		for(RoutingChoice sa:s){
			System.out.println("[Test : main]: "+sa.getSignalName()+"@"+sa.getSignalAppName()+":"+sa.getListenerAppName()+" "+sa.getListenerDescription());
			RoutingXmlParser parser = new RoutingXmlParser(sa.getRouting());
			System.out.println("[Test : main]: "+"Routing:"+sa.getRouting());
			for(ListenerConfig config:parser.getEventListeners()){
				System.out.println("[Test : main]: "+config.node());
			}
		}
		InternetRT irt = new InternetRT();
		String appID = "5c71fed6-04b5-44d8-afe9-73516f59060f";
		String appSecret ="8d9389fa-6fc9-4f27-856d-74587c71e677";
			try {
				InternetRTConfig config = new InternetRTConfig();

				config.updatePropertiy("appID", appID);
				config.updatePropertiy("appSecret",
						appSecret);
				config.updatePropertiy("redirect_URI",
						"http://localhost:9001/Application/loginUser"); //Play 1.0 and Play 2.0 will conflict on session, if domain is same & port is different
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
		Map<String,String> map = new HashMap();
		map.put("message", "value");
		String token = "93a134ba-52ce-4f54-9e7d-ad79bbb5f378";
		try {
			irt.send(token, "updatestatus", map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static class TestRoutingPredicate{
		private String sina = "<Application><Name>sina</Name><AppID>19046b74-b3af-4d55-949e-2f857dbd1b6b</AppID><AppOwner></AppOwner><Signals><Request runat=\"userInterface\"><Signalname>updataStatus</Signalname><Description>Share request. Response should share the signal to somewhere.</Description><Require>Response should share the signal to somewhere</Require></Request><Request runat=\"userInterface\"><Signalname>share</Signalname><Description>Share request. Response should share the signal to somewhere.</Description><Require>Response should share the signal to somewhere</Require></Request></Signals><SignalHanlders><RequestListener runat=\"19046b74-b3af-4d55-949e-2f857dbd1b6b\" type=\"httpget\"><Description>Share response.</Description><URL id=\"1\">http://127.0.0.1/weibot/message</URL><Adapter><Signalname id=\"1\">updataStatus</Signalname><params><param><key>message</key><value><var></var></value></param><param><key>rid</key><value><var><ID></ID></var></value></param></params></Adapter></RequestListener><RequestListener runat=\"19046b74-b3af-4d55-949e-2f857dbd1b6b\" type=\"httpget\"><Description>Share response.</Description><URL id=\"1\">http://v.t.sina.com.cn/share/share.php</URL><Adapter><Signalname id=\"1\">share</Signalname><params><param><key>url</key><value><var><newkey>link</newkey></var></value></param></params><headers></headers></Adapter></RequestListener></SignalHanlders></Application>";
		private String renren = "<Application><Name>renren</Name><AppID>c2cdb47a-2372-4602-9860-9cb231c6b5ad</AppID><AppOwner></AppOwner><Signals><Request runat=\"userInterface\"><Signalname>updataStatus</Signalname><Description>Share request. Response should share the signal to somewhere.</Description><Require>Response should share the signal to somewhere</Require></Request><Request runat=\"userInterface\"><Signalname>share</Signalname><Description>Share request. Response should share the signal to somewhere.</Description><Require>Response should share the signal to somewhere</Require></Request></Signals><SignalHanlders><RequestListener runat=\"c2cdb47a-2372-4602-9860-9cb231c6b5ad\" type=\"httpget\"><Description>Share response.</Description><URL id=\"1\">http://127.0.0.1/renrent/message</URL><Adapter><Signalname id=\"1\">updataStatus</Signalname><params><param><key>message</key><value><var></var></value></param><param><key>rid</key><value><var><ID></ID></var></value></param></params></Adapter></RequestListener><RequestListener runat=\"c2cdb47a-2372-4602-9860-9cb231c6b5ad\" type=\"httpget\"><Description>Share response.</Description><URL id=\"1\">http://www.connect.renren.com/share/sharer</URL><Adapter><Signalname id=\"1\">share</Signalname><params><param><key>url</key><value><var></var></value></param></params><headers></headers></Adapter></RequestListener></SignalHanlders></Application>";
		public List<scala.Tuple3<String,Signal,DescribedListenerConfig>> getPossibleRoutings(String fromAppID, String accessToken){
			
			String appXmlString = sina;
			
			List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a = new ArrayList<scala.Tuple3<String,Signal,DescribedListenerConfig>>();
			AppXmlParser appXmlParser = new AppXmlParser(appXmlString);
			
			addNewListeners(accessToken, appXmlParser, a);
			addNewSignalSource(accessToken, appXmlParser,a);

			return a;
		}
		private void addNewSignalSource(String accessToken, AppXmlParser appXmlParser,
				List<scala.Tuple3<String,Signal,DescribedListenerConfig>> a) {
			List<DescribedListenerConfig> listeners = appXmlParser.getListeners();
			
			for(DescribedListenerConfig config: listeners){
				String[] allAppsList = new String[]{renren,sina};
				
				for(String app: allAppsList){
					String fromAppXmlString = app;
					AppXmlParser fromAppXmlParser = new AppXmlParser(fromAppXmlString);
					System.out.println("[Test : addNewSignalSource]: "+config.node());
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
				System.out.println("[Test : addNewListeners]: "+"signal"+signalName);
				String[] allAppsList = new String[]{renren,sina};
				
				for(String app: allAppsList){
					String toAppXmlString = app;
					AppXmlParser toAppXmlParser = new AppXmlParser(toAppXmlString);
					List<DescribedListenerConfig> listenerList = toAppXmlParser.getMatchedListeners(signalName);
					for(DescribedListenerConfig config:listenerList){
						a.add(new scala.Tuple3(appXmlParser.getAppName(),signal,config));
					}
				}
			}
		}
	}
	private static List<RoutingChoice> generateChoieces(List<scala.Tuple3<String,Signal,DescribedListenerConfig>> possibleRoutings){
		List<RoutingChoice> res = new ArrayList<RoutingChoice>();
		for(scala.Tuple3<String,Signal,DescribedListenerConfig> data:possibleRoutings){
			String routing = FreeRoutingGenerator.generateRouting(data._2().name(),data._1(),data._3());
			String signalName = data._2().name();
			String signaldes = data._2().description();
			String signalApp = data._1();
			String listenerApp = data._3().appName();
			String listenerDes = data._3().description();
			res.add(new RoutingChoice(signalName,signalApp,signaldes,listenerApp,listenerDes,routing));
		}
		return res;
	}
	
}


