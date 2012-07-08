package models;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
/*
 * AppConfig is a config which is different between users.
 */
public class AppConfig {
	//Fields	
	public String appId;
	public String config;
	//public String information;
 
	//Construcors	
	public AppConfig(String appId, String config)
	{
		this.appId = appId;
		this.config = config;
	}

	//Property accessors	
	public String getAppId()
	{
		return this.appId;
	}	
	public void setAppId(String appId)
	{
		this.appId = appId;
	}	
	public String getConfig()
	{
		return this.config;
	}
	public void setConfig(String config)
	{
		this.config = config;
	}	
	/*
	public String getInformation()
	{
		return this.information;
	}
	public void setInformation(String information)
	{
		this.information = information;
	}
	*/
}
