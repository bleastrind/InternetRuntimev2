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
 
	//Construcors	
	public App(String id, String name, String information, String installUrl)
	{
		this.id = id;
		this.name = name;
		this.information = information;
		this.installUrl = installUrl;
	}
	
	public App(String name, String information, String installUrl)
	{
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.information = information;
		this.installUrl = installUrl;
	}
	
	//Property accessors	
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
}
