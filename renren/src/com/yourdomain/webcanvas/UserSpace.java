package com.yourdomain.webcanvas;

import java.util.Set;

public class UserSpace {
	public String sessionKey,renrenUserId;
	public Set<String> message;
	public UserSpace(String sessionKey,String renrenUserId)
	{
		this.sessionKey = sessionKey;
		this.renrenUserId = renrenUserId;
	}
	
	public UserSpace(String sessionKey,String renrenUserId,Set<String> message)
	{
		this.sessionKey = sessionKey;
		this.renrenUserId = renrenUserId;
		this.message = message;
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
}
