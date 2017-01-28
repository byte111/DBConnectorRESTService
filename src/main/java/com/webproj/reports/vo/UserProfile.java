package com.webproj.reports.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class UserProfile {
	String compname;
	String compid;
	String address;
	int retention;
	
	@XmlElement
	private UserCredentials usercreds;
	
	public UserProfile()
	{
		
	}
	
	public UserProfile(String compname,String compid,String address,int retention)
	{
		this.compname = compname ;
		this.compid = compid ; 
		this.address = address;
		this.retention = retention ; 
	}
	
	public UserCredentials getUserCreds()
	{
		return usercreds;
	}
	
	public void setUsercreds(UserCredentials usercreds)
	{
		this.usercreds = usercreds;
	}
	
	public String getCompname() {
		return compname;
	}
	public String getCompid() {
		return compid;
	}
	public String getAddress() {
		return address;
	}
	public int getRetention() {
		return retention;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public void setCompid(String compid) {
		this.compid = compid;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setRetention(int retention) {
		this.retention = retention;
	}

	@Override
	public String toString()
	{
		System.out.println("["
									+ "compname=" + this.compname
									+ "compid=" + this.compid
									+ "address=" + this.address
									+ "retention=" + this.retention
									+"]");
		return address;
		
	}
}
