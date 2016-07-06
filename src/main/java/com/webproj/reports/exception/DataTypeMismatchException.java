package com.webproj.reports.exception;

public class DataTypeMismatchException extends Throwable{
	
	public DataTypeMismatchException()
	{
		super();
	}
	
	public DataTypeMismatchException(String msg)
	{
		
		super(msg);
		System.out.println("DataTypeMismatchException "+msg);
		
	}
}
