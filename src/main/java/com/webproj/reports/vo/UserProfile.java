package com.webproj.reports.vo;

public class UserProfile {

	String compname;
	String compid;
	String address;
	int retention;
	
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
