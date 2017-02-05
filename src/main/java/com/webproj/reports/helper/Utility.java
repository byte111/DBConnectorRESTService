package com.webproj.reports.helper;

import java.util.UUID;

public class Utility {

	
	public static String getFirstTimePassword() throws Exception
	{
		try {
			return  UUID.randomUUID().toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception while generatig random password.");		
		}		
	
	}
}
