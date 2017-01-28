package com.webproj.constants;

public class DBConnectorConstants {
	
	/******************************* USER PROFILE QUERY ***************************************/
	
	public static final String USERPROFINSERTQUERY="INSERT INTO USERPROFILE (COMPID,COMPNAME,ADDRESS,RETENTION) VALUES(?,?,?,?)";
	public static final String USERPROFGETQUERYBYUSERID="SELECT * FROM USERPROFILE WHERE COMPID=?";
	public static final String USERPROFGETALLQUERY=" SELECT b.*  " +
													" FROM   (SELECT a.* , rownum AS rnum " +
													" FROM   (SELECT *  FROM   userprofile ) a " + 
													" WHERE rownum <=  ?) b " +
													" WHERE  rnum >=  ? ";
	public static final String USERCREDINSERTQUERY="INSERT INTO USERCREDS(USERNAME,PASSWORD,CREATED,MODIFIED) VALUES(?,?,?,?)";
	public static final String USERCREDGETQUERYBYUSERID="SELECT * FROM USERCREDS WHERE USERNAME=?";
	
	
	/******************************* TRADINGPARTNERS QUERY ***************************************/

	
	public static final String TARDPARTINSERTQUERY="INSERT INTO TRADINGPARTNERS(PARTNERSHIPID,OWNERCOMPID,PARTNERCOMPID,CHARGES,CREATED,MODIFIED) VALUES(?,?,?,?,?,?)";
	public static final String TARDPARTSELECTQUERY=	"SELECT * FROM TRADINGPARTNERS WHERE OWNERCOMPID=?";

}
