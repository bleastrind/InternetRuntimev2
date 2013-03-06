package com.yourdomain.webcanvas;

import java.util.Set;

public class UserSpace {
	public String sessionKey,renrenUserId,Token,Refresh;
	public Set<String> message;
	public UserSpace(String sessionKey,String renrenUserId,Set<String> message,String token)
	{
		this.sessionKey = sessionKey;
		this.renrenUserId = renrenUserId;
		this.message = message;
		this.Token = token;
	}
	
	public String getSessionKey()
	{
		return sessionKey;
	}
	
	public String getRenrenUserId()
	{
		return renrenUserId;
	}
	
	public void updateSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	
	public void updateMessage(Set<String> message)
	{
		this.message = message;
	}
	
	public Set<String> getMessage()
	{
		return message;
	}
	
	public void setToken(String Token){
		this.Token = Token;
	}
	
	public String getToken(){
		return this.Token;
	}
	
	public void setRefresh(String Refresh){
		this.Refresh = Refresh;
	}
	
	public String getRefresh(){
		return this.Refresh;
	}
}
