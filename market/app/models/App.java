package models;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
/*
 * App is a class of the Application and for AppDao;
 */
public class App {
	//Fields	
	private String id;
	private String name;
	private String information;
	private String installUrl;
	private String updated;
	private String updateUrl;
	private String secret;
	private String decription;
	private String logourl;
	//Construcors	
	public App(String id, String name, String information, String installUrl,String updated,String updateUrl,String secret, String logourl)
	{
		this.id = id;
		this.name = name;
		this.information = information;
		this.installUrl = installUrl;
		this.updated = updated;
		this.updateUrl = updateUrl;
		this.secret = secret;
		this.logourl = logourl;
	}
	
	
	//Property accessors	
	public String getLogourl()
	{
		return this.logourl;
	}	
	public String getId()
	{
		return this.id;
	}	
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}	
	public String getInformation()
	{
		return this.information;
	}
	public void setInformation(String information)
	{
		this.information = information;
	}
	public String getInstallUrl()
	{
		return this.installUrl;
	}
	public void setInstallUrl(String intallUrl)
	{
		this.installUrl = intallUrl;
	}
	
	public String getUpdateUrl()
	{
		return this.updateUrl;
	}
	
	public void setUpdateUrl(String url)
	{
		this.updateUrl = url;
	}
	
	public String getUpdated()
	{
		return this.updated;
	}
	
	public void setUpdated(String updated)
	{
		this.updated = updated;
	}
	
	public void setSecret(String secret)
	{
		this.secret = secret;
	}
	
	public String getSecret()
	{
		return this.secret;
	}
	
	public String getDecription(){
		return this.decription;
	}
	
	public void setDecription(String decription){
		this.decription = decription;
	}
}
