package com.webproj.constants;

public class DBConnectorConstants {
	
	/******************************* USER PROFILE QUERY ***************************************/
	
	public static final String USERPROFINSERTQUERY="INSERT INTO USERPROFILE (COMPID,COMPNAME,ADDRESS,RETENTION) VALUES(?,?,?,?)";
	public static final String USERPROFGETQUERYBYUSERID="SELECT * FROM USERPROFILE WHERE COMPID=?";

}
