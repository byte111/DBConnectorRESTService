package com.webproj.reports.dao;



/*
 * 
 * This class uses simple jdbc insert 
 * 
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.sun.research.ws.wadl.ObjectFactory;
import com.webproj.constants.DBConnectorConstants;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.UserProfileDetails;

public class DBConnectionDAO2 {
	
	private static DBConnectionDAO2 daoObj = null;
	private JdbcTemplate jdbcTemplate;
	
	
	public static DBConnectionDAO2 getInstance()
	{
		if(daoObj!=null)
		{
			return daoObj;
		}
		else
		{
			return new DBConnectionDAO2();
		}
	}
	
	private DBConnectionDAO2()
	{
		if(jdbcTemplate == null)
			try {
				jdbcTemplate = new JdbcTemplate(getDataSource());
				
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
	
	/*public UserProfileDetails getUserProf(String compid) throws Exception
	{
		UserProfileDetails userprof = null;
		try {

			String query = DBConnectorConstants.USERCREDGETQUERYBYUSERID_NAMED;
						userprof = jdbcTemplate.queryForObject(query, new Object[]{compid}, new RowMapper<UserProfileDetails>(){

				@Override
				public UserProfileDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserProfileDetails userprof = new UserProfileDetails();
					userprof.setCompid(rs.getString("COMPID"));
					userprof.setCompname(rs.getString("COMPNAME"));
					userprof.setAddress(rs.getString("ADDRESS"));
					userprof.setRetention(rs.getInt("RETENTION"));
					return userprof;
				}});
				
				

			
		 SqlParameterSource namedParameters = new MapSqlParameterSource("COMPID", compid);
			
			userprof = namedjdbcTemplate.queryForObject(query, namedParameters, new RowMapper<UserProfileDetails>() {

				@Override
				public UserProfileDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserProfileDetails userobj = new UserProfileDetails();
					userobj.setCompid(rs.getString("COMPID"));
					userobj.setCompname(rs.getString("COMPNAME"));
					userobj.setAddress(rs.getString("ADDRESS"));
					userobj.setRetention(rs.getInt("RETENTION"));
					return userobj;
				}
			});

		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception("Error in getting user profile : " + e.getMessage());
		}

		return userprof;
	}
	*/
	
	 public void insertTradingPartnership(Partners partners) throws TradingPrtnerException, Exception
	{
		String query = DBConnectorConstants.TARDPARTINSERTQUERY;
		try 
		{	
			SimpleJdbcInsert simplejdbcInsert  = new SimpleJdbcInsert(getDataSource()).withTableName("TRADINGPARTNERS");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("PARTNERSHIPID", partners.getPartnershipid());
			//params.put("OWNERCOMPID", partners.getOwneruser().getCompid());
		//	params.put("PARTNERCOMPID", partners.getPartneruser().getCompid());	
		//	params.put("CHARGES", partners.getCharges());
			params.put("CREATED", 	new java.sql.Date(System.currentTimeMillis()));
		//	params.put("MODIFIED", 	new java.sql.Date(System.currentTimeMillis()));
			
			System.out.println(simplejdbcInsert.execute(params) > 0 ? "Data inserted successfully" : "Error in data inserted.");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[])
	{
		DBConnectionDAO2  obj = new DBConnectionDAO2();
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
		}
	}

}
