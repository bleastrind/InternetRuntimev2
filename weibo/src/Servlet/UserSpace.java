package Servlet;
import java.util.ArrayList;
import java.util.List;

import weibo4j.model.Status;

public class UserSpace {
	public String sessionKey;
	public List<Status> message;
	public String token;
	public List<String> msg ;
	public UserSpace(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	public UserSpace(String sessionKey,List<Status> message,String token,List<String> msg)
	{
		this.sessionKey = sessionKey;
		this.message = message;
		this.token = token;
		this.msg = msg;
	}
	
	public String getSessionKey()
	{
		return sessionKey;
	}
	
	public void updateSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	
	public void updateMessage(List<Status> message)
	{
		this.message = message;
	}
	
	public List<Status> getMessage()
	{
		return message;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getToken(){
		return token;
	}
}

