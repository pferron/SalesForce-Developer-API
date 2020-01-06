package com.axiomatics.data;

public class Connection {
	
	private String URL				= "";
	private String client_id		= "";
	private String client_secret	= "";
	private String username			= "";
	private String password			= "";
	private String query			= "";
	private String condition		= "";
	
	public String getCondition()
	{
	    return this.condition;
	}
	public void setCondition(String condition)
	{
	     this.condition = condition;
	}
	
	public String getQuery()
	{
	    return this.query;
	}
	public void setQuery(String query)
	{
	     this.query = query;
	}
	
	public String getPassword()
	{
	    return this.password;
	}
	public void setPassword(String password)
	{
	     this.password = password;
	}
	
	public String getUserName()
	{
	    return this.username;
	}
	public void setUserName(String username)
	{
	     this.username = username;
	}
	
	public String getURL()
	{
	    return this.URL;
	}
	public void setURL(String URL)
	{
	     this.URL = URL;
	}
	
	public String getClientID()
	{
	    return this.client_id;
	}
	public void setClientID(String client_id)
	{
	     this.client_id = client_id;
	}
	
	public String getClientSecret()
	{
	    return this.client_secret;
	}
	public void setClientSecret(String client_secret)
	{
	     this.client_secret = client_secret;
	}
	

}
