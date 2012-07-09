package org.internetrt.sdk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InternetRTConfig {

	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			System.out.println("No config.properties");
		}
	}
	
	private Map<String,String> dynamicProperties = new HashMap<String,String>();
	
	public String getValue(String key){
		if(dynamicProperties.containsKey(key))
			return dynamicProperties.get(key);
		else if(props.containsKey(key))
			return props.getProperty(key);
		else
			return null;
	}

    public void updatePropertiy(String key,String value) {    
    	dynamicProperties.put(key, value);
    } 
    
    public boolean containsAllProperties(Iterable<String> keys){
    	for(String k:keys){
    		if(getValue(k) == null)
    			return false;
    	}
    	return true;
    }
}
