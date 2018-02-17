package com.webproj.reports.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import com.webproj.reports.jaxb.UserProfileDetails;

public class MappingQueryTEst extends MappingSqlQuery<UserProfileDetails>{

	public MappingQueryTEst(DataSource ds) {
		
        super(ds, "select COMPID, COMPNAME, ADDRESS FROM USERPROFILE WHERE  COMPID = ?  ");
        super.declareParameter(new SqlParameter("COMPID", Types.VARCHAR));
        //super.declareParameter(new SqlParameter("ADDRESS", Types.VARCHAR));
        
    }
	public MappingQueryTEst(){
		
	}
	
	@Override
	protected UserProfileDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserProfileDetails userobj  = new UserProfileDetails();
		userobj.setCompid(rs.getString("COMPID")+"");
		userobj.setCompname(rs.getString("COMPNAME"));
		userobj.setAddress(rs.getString("ADDRESS"));
		return userobj;
	}
	
	
	private static BasicDataSource getDataSource() throws Exception
	{

		BasicDataSource datasource;
		ApplicationContext context = new ClassPathXmlApplicationContext("../applicationContext.xml");
		
			//new FileSystemXmlApplicationContext ("C:\\devwrkspc\\pwd\\sts_wrspc\\myworkspc\\DBConnector\\src\\main\\webapp\\WEB-INF\\applicationContext.xml");
		datasource = (BasicDataSource) context.getBean("dataSource");
		
		try {				
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception in getting datasource");
		}

		return datasource;
	}
	
	public UserProfileDetails getUserDetails1(final String compid)
	{
		try{
			MappingQueryTEst obj = new  MappingQueryTEst(MappingQueryTEst.getDataSource());
			return obj.findObject(new Object[]{compid});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public UserProfileDetails getUserDetails2(final String compid,String address)
	{
		try{
			MappingQueryTEst obj = new  MappingQueryTEst(MappingQueryTEst.getDataSource());
			return obj.findObject(new Object[]{compid,address});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		try {
			MappingQueryTEst obj = new  MappingQueryTEst(MappingQueryTEst.getDataSource());
		
			System.out.println(obj.findObject(new Object[]{"dev","BLR"}));
			//obj.findObject("dev");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
