package com.webproj.reports.exception;

public class TradingPrtnerException extends Throwable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TradingPrtnerException()
	{
		
	}
	
	public TradingPrtnerException(String msg)
	{
		super(msg);
		System.out.println("Exception while creating a trading partners. Details: " + msg);
	} 
	
}
