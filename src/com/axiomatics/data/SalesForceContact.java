package com.axiomatics.data;

public class SalesForceContact {
	
	private String firstName			= null;
	private String lastName				= null;
	private String email 				= null;
	
	public String getFirstName()
	{
	    return this.firstName;
	}
	public void setFirstName(String firstName)
	{
	     this.firstName = firstName;
	}
	
	public String getLastName()
	{
	    return this.lastName;
	}
	public void setLastName(String lastName)
	{
	     this.lastName = lastName;
	}
	
	public String getEmail()
	{
	    return this.email;
	}
	public void setEmail(String email)
	{
	     this.email = email;
	}

}
