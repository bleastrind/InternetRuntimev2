import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppXmlParser 
{
	
	public static Application  createApplication(String param)
	{
		Application application = new Application();
		ArrayList<Signal> signals = new ArrayList<Signal>();
		
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(param);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();			//根节点
		Iterator itRoot = root.elementIterator();
			
		Element second;										//二级节点
		Iterator itSec;
			
		while(itRoot.hasNext())
		{
			second = (Element) itRoot.next();
			if(second.getName()=="Name")
			{
				application.setAppName(second.getText());
			}
			else if(second.getName()=="Signals")
			{
				Element third;
				Iterator itThird;
				itSec = second.elementIterator();
				
				while(itSec.hasNext())
				{
					third = (Element)itSec.next();
					itThird = third.elementIterator();
						
					Element fourth;
					while(itThird.hasNext())
					{
						Signal signal = new Signal();
						fourth = (Element)itThird.next();
						if(fourth.getName()=="Signalname")
						{
							String text1 = fourth.getText();
							 signal.setSignalName(text1);
//							System.out.println(text1);
						}
						fourth = (Element)itThird.next();
						 if(fourth.getName()=="Description")
						{
							String text2	 = fourth.getText();
//							System.out.println(text2);
							signal.setDescription(text2);
						}
						 signals.add(signal);
						 application.setSignals(signals);
					}
				}
			}
		}
			
			return application;
	}
	
	public static JSONObject ApplicationToJson(Application application)
	{
		String appName = application.getAppName();
		String appID = application.getAppId();
		ArrayList<Signal> signals= application.getSignals();
		JSONObject applicationObject = new JSONObject();
		try {
			applicationObject.put("appName", appName);
			
			JSONArray signalArray = new JSONArray();
			for(int j = 0; j<signals.size();j++)
			{
				Signal tempSignal = signals.get(j);
				signalArray.put(SignalToJson(tempSignal));
			}
			
			applicationObject.put("appName", appName);
			applicationObject.put("signals", signalArray);
			applicationObject.put("appID",appID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return applicationObject;
	}
	
	public static JSONObject SignalToJson(Signal signal)
	{
		String signalName = signal.getSignalName();
		String description = signal.getDescription();
		JSONObject signalObject = new JSONObject();
		try {
			signalObject.put("signalName", signalName);
			signalObject.put("signalDescription", description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signalObject;
	}
	
}
