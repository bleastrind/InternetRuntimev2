import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Application
{
	private String appName = null;
	private long appId = 0;
	private  ArrayList<Signal> signals = new ArrayList();
	
	public String getAppName() {
		return appName;
	}

	public ArrayList<Signal> getSignals() {
		return signals;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setSignals(ArrayList<Signal> signals) {
		this.signals = signals;
	}


}