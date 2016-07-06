package com.webproj.reports.beans;

public class HelloBean implements IHelloBO {


	@Override
	public String sayhello() {
		return "Hello from helloBean!!";
	}
}
