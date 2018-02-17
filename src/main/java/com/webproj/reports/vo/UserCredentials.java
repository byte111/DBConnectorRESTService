package com.webproj.reports.vo;


public class UserCredentials 
{
	private String username;
	private String password;
	private String created;
	private String modified;
	
	
	public UserCredentials()
	{
		
	}
	
	public UserCredentials(String username,
							String password,
							String created,
							String modified) 
	{
		
		this.username = username;
		this.password = password;
		this.created = created;
		this.modified = modified ;
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	
	

}
