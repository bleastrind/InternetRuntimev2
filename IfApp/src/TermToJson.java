import java.util.List;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.Signal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.portable.ApplicationException;

public class TermToJson 
{
	
//	public static Application createApplication(String param)
//	{
//		Application application = Application()
//		ArrayList<Signal> signals = new ArrayList<Signal>();
//		
//		Document doc = null;
//		try {
//			doc = DocumentHelper.parseText(param);
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Element root = doc.getRootElement();			//根节点
//		Iterator itRoot = root.elementIterator();
//			
//		Element second;										//二级节点
//		Iterator itSec;
//			
//		while(itRoot.hasNext())
//		{
//			second = (Element) itRoot.next();
//			if(second.getName()=="Name")
//			{
//				application.setAppName(second.getText());
//			}
//			else if(second.getName()=="Signals")
//			{
//				Element third;
//				Iterator itThird;
//				itSec = second.elementIterator();
//				
//				while(itSec.hasNext())
//				{
//					third = (Element)itSec.next();
//					itThird = third.elementIterator();
//						
//					Element fourth;
//					while(itThird.hasNext())
//					{
//						Signal signal = new Signal();
//						fourth = (Element)itThird.next();
//						if(fourth.getName()=="Signalname")
//						{
//							String text1 = fourth.getText();
//							 signal.setSignalName(text1);
////							System.out.println(text1);
//						}
//						fourth = (Element)itThird.next();
//						 if(fourth.getName()=="Description")
//						{
//							String text2	 = fourth.getText();
////							System.out.println(text2);
//							signal.setDescription(text2);
//						}
//						 signals.add(signal);
//						 application.setSignals(signals);
//					}
//				}
//			}
//		}
//			
//			return application;
//	}
	
	public static JSONObject ApplicationToJson(Application application)
	{
		String appName = application.name();
		String appID = application.ID();
		List<Signal> signals= application.signals();
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
		String signalName = signal.name();
		String description = signal.description();
		String  require = signal.require();
		JSONObject signalObject = new JSONObject();
		try {
			signalObject.put("signalName", signalName);
			signalObject.put("signalDescription", description);
			signalObject.put("signalRequire", require);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signalObject;
	}
	
}
