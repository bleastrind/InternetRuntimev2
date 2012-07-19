package org.internetrt.sdk;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class InternetRTConfig {

	private Properties props = new Properties(); 
	private Map<String,String> dynamicProperties = new HashMap<String,String>();
	
	public InternetRTConfig(String propertiefile){
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiefile));
		} catch (Exception e) {
			System.out.println("No config.properties");
		}
	}
	public InternetRTConfig(){}
	
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
