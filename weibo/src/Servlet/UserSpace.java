package Servlet;
import java.util.List;

import weibo4j.model.Comment;

public class UserSpace {
	public String sessionKey;
	public List<Comment> message;
	public String token;
	
	public UserSpace(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	
	public UserSpace(String sessionKey,List<Comment> message)
	{
		this.sessionKey = sessionKey;
		this.message = message;
	}
	
	public UserSpace(String sessionKey,List<Comment> message,String token)
	{
		this.sessionKey = sessionKey;
		this.message = message;
		this.token = token;
	}
	
	public String getSessionKey()
	{
		return sessionKey;
	}
	
	public void updateSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
	}
	
	public void updateMessage(List<Comment> message)
	{
		this.message = message;
	}
	
	public List<Comment> getMessage()
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

