package com.webproj.reports.dao;
/*
 * 
 * This class used simplejdbccall
 */
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import com.webproj.reports.jaxb.UserProfileDetails;

public class DBConnectionDAO3 {
	private static DBConnectionDAO3 daoObj = null;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall simpleJdbcCall;
	
	public static DBConnectionDAO3 getInstance()
	{
		if(daoObj!=null)
		{
			return daoObj;
		}
		else
		{
			return new DBConnectionDAO3();
		}
	}
	
	private DBConnectionDAO3()
	{
		if(jdbcTemplate == null)
			try {
				jdbcTemplate = new JdbcTemplate(getDataSource());
				simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("getDBUSERBycompid");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private BasicDataSource getDataSource() throws Exception
	{

		BasicDataSource datasource;
		ApplicationContext context =
	    		new ClassPathXmlApplicationContext("../applicationContext.xml");
		
		datasource = (BasicDataSource) context.getBean("dataSource");
		
		try {				
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception in getting datasource");
		}

		return datasource;
	}
	
	public UserProfileDetails callTestProcedure(String compid)
	{
		try
		{
		MapSqlParameterSource in = new MapSqlParameterSource();
		in.addValue("I_COMPID", compid.trim());
		Map out = simpleJdbcCall.execute(in);
		System.out.println("COMP NAME =" + (String)out.get("O_COMPNAME"));
		System.out.println("ADDRESS =" + (String)out.get("O_ADDRESS"));
		UserProfileDetails outputobj = new UserProfileDetails();
		outputobj.setCompid(compid);
		outputobj.setAddress((String)out.get("O_ADDRESS"));
		outputobj.setCompname((String)out.get("O_ADDRESS"));
		return outputobj ;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static void main(String args[])
	{
		/*DBConectionDAO3  obj = new DBConectionDAO3();
		try {
			Partners partners = new Partners();
			partners.setPartnershipid(111);
			
			obj.insertTradingPartnership(partners);
			//System.out.println(obj.getUserProf("dev").getAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TradingPrtnerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
