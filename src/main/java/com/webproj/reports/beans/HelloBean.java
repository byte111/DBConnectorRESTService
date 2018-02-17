package com.webproj.reports.beans;


public class HelloBean  {

	/*
	@Override
	public String sayhello() {
		return "Hello from helloBean!!";
	}*/
	
	static
	{
		System.out.println("bean loaded ");
	}
	private String val;
	public String getVal()
	{
		System.out.println("getting hello bean value ");
		return val;
	}
	
	public void setVal(String val)
	{
		System.out.println("setting hello bean value ");
		this.val = val;
	}
}
