package ScriptEditor;
import java.util.List;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.internetrt.sdk.util.Application;
import org.internetrt.sdk.util.Signal;

import org.omg.CORBA.portable.ApplicationException;

public class TermToJson 
{
	
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
	
	public static JSONObject stringToJson(String key, String value){
		JSONObject result = new JSONObject();
		try {
			result.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject signalAndAppListToJson(String signalName, JSONArray toAppList)
	{
		JSONObject result = new JSONObject();
		try {
			result.put("signalName", signalName);
			result.put("toAppList", toAppList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject appAndSignalListToJson(JSONObject application, JSONArray signalsNameList){
		JSONObject result = new JSONObject();
		try {
			result.put("signalNameList", signalsNameList);
			result.put("application", application);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static JSONObject jsonArrayToJsonObject(String arrayName, JSONArray list){
		JSONObject result = new JSONObject();
		try {
			result.put(arrayName, list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
