package com.webproj.reports.exception;

public class UserProfileInsertException extends Throwable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserProfileInsertException()
	{
		super();
	}
	
	public UserProfileInsertException(String msg)
	{
		super(msg);
		System.out.println("Exception while creating a user profile. Details: "+msg);
	}

}
