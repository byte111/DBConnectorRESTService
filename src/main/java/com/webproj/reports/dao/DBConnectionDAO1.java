package com.webproj.reports.dao;


/*
 * 
 * This class uses namedjdbcTemplate
 * 
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.webproj.constants.DBConnectorConstants;
import com.webproj.reports.jaxb.UserProfileDetails;

public class DBConnectionDAO1 {
	
	private static NamedParameterJdbcTemplate namedjdbcTemplate;
	private static DBConnectionDAO1 daoObj = null;
	
	
	public static DBConnectionDAO1 getInstance()
	{
		if(daoObj!=null)
		{
			return daoObj;
		}
		else
		{
			return new DBConnectionDAO1();
		}
	}
	
	private DBConnectionDAO1()
	{
		if(namedjdbcTemplate == null)
			try {
				namedjdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
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
	
	public UserProfileDetails getUserProf(String compid) throws Exception
	{
		UserProfileDetails userprof = null;
		try {

			String query = DBConnectorConstants.USERCREDGETQUERYBYUSERID_NAMED;
			/*			userprof = jdbcTemplate.queryForObject(query, new Object[]{compid}, new RowMapper<UserProfileDetails>(){

				@Override
				public UserProfileDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserProfileDetails userprof = new UserProfileDetails();
					userprof.setCompid(rs.getString("COMPID"));
					userprof.setCompname(rs.getString("COMPNAME"));
					userprof.setAddress(rs.getString("ADDRESS"));
					userprof.setRetention(rs.getInt("RETENTION"));
					return userprof;
				}});
				
				
*/
			
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
	
	public static void main(String args[])
	{
		
		DBConnectionDAO1  obj = new DBConnectionDAO1();
		try {
			System.out.println(obj.getUserProf("dev").getAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
