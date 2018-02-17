package com.webproj.reports.dao;

/*
 * 
 * This class uses jdbcTemplate
 * 
 * used for testing transactions
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.webproj.constants.DBConnectorConstants;
import com.webproj.reports.beans.HelloBean;
import com.webproj.reports.exception.CustomSQLErrorCodesTranslator;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.exception.UserProfileInsertException;
import com.webproj.reports.helper.Utility;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.UserProfileDetails;
import com.webproj.reports.vo.BillingMO;
import com.webproj.reports.vo.UserCredentials;
import com.webproj.reports.vo.UserProfile;


public class DBConnectionDAO4 {
	
	/*@Autowired
	HelloBean helloBO;*/

	private  JdbcTemplate jdbcTemplate= null;
	private static DBConnectionDAO4 daoObj = null;
	private PlatformTransactionManager transactionManager = null;
	private DBConnectionDAO4()
	{
		
		try {
			jdbcTemplate = getJdbcTemplateInstance();
			//jdbcTemplate.setExceptionTranslator(getCustomExceptionExeption());
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	


	private  BasicDataSource getDataSource() throws Exception{
		
		try {	
		BasicDataSource datasource = new BasicDataSource();
		
		//ApplicationContext context =  		new ClassPathXmlApplicationContext("../applicationContext.xml");
		
		ApplicationContext context = new FileSystemXmlApplicationContext("C:\\devwrkspc\\pwd\\sts_wrspc\\myworkspc\\DBConnector\\src\\main\\webapp\\WEB-INF\\applicationContext.xml");
		
		datasource =  (BasicDataSource) context.getBean("dataSource1");
		System.out.println("datasource.getDefaultAutoCommit() = " + datasource.getDefaultAutoCommit());
		
		transactionManager = (PlatformTransactionManager) context.getBean("TestTransactionManager");
				
		
			/*datasource.setDriver(new oracle.jdbc.driver.OracleDriver());
			datasource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
			datasource.setUsername("SYSTEM");
			datasource.setPassword("dev");*/
		return datasource;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception in getting datasource");
		}

		
	}

	public static DBConnectionDAO4 getInstance()
	{
		if(daoObj!=null)
		{
			return daoObj;
		}
		else
		{
			return new DBConnectionDAO4();
		}
	}



	public  JdbcTemplate getJdbcTemplateInstance() throws Exception
	{
		if(jdbcTemplate == null)
		{
			getJDBCTemplate();
		}

		return jdbcTemplate;

	}


	/**
	 * @throws Exception
	 */
	private  void getJDBCTemplate() throws Exception
	{
		try 
		{		
			jdbcTemplate = new JdbcTemplate(getDataSource());

		} 
		catch (Exception e) 
		{
			throw new Exception("Excveption in creating jdbc template : "+e);
		}		
	}

	public static void removeInstance()
	{
		if(daoObj != null)
		{
			daoObj = null;
		}			
	}


	
	public UserProfile insertUserProfile(UserProfile userprofile) throws UserProfileInsertException, Exception
	{
		UserProfile userprofileget = null;
		UserCredentials usercredsget = null;

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	
		TransactionStatus status = transactionManager.getTransaction(def);
		status.setRollbackOnly();
		System.out.println("status = status.isRollbackOnly() " + status.isRollbackOnly());
		try 
		{
			
			if(userprofile == null)
			{
				throw new UserProfileInsertException("Cannot insert null in DB.");
			}

			String query = DBConnectorConstants.USERPROFINSERTQUERY;

			int rows = jdbcTemplate.update(query, new Object[]{
					userprofile.getCompid(),
					userprofile.getCompname(),
					userprofile.getAddress(),
					userprofile.getRetention()					
			});

			if(1/0 > 0) throw new Exception("Forgotten hero infinity");
			
			rows += jdbcTemplate.update(DBConnectorConstants.USERCREDINSERTQUERY, new Object[]{
					userprofile.getCompid(),
					userprofile.getUserCreds().getPassword(),
					new java.sql.Date(System.currentTimeMillis()),
					new java.sql.Date(System.currentTimeMillis())
			});

			if(checkIfCreateProfileSuccess(rows))
			{

				userprofileget = jdbcTemplate.queryForObject(DBConnectorConstants.USERPROFGETQUERYBYUSERID, 
						new Object[]{userprofile.getCompid()}, 
						new RowMapper<UserProfile>(){

					@Override
					public UserProfile mapRow(ResultSet rs, int rowNum)
							throws SQLException  {

						UserProfile userprof = null;

						try
						{
							userprof = new UserProfile();
							userprof.setCompid(rs.getString("COMPID"));
							userprof.setCompname(rs.getString("COMPNAME"));
							userprof.setAddress(rs.getString("ADDRESS"));
							userprof.setRetention(rs.getInt("RETENTION"));

						}
						catch(SQLException e)
						{
							e.printStackTrace();
							throw new SQLException("Error while creating userprofile : "+e.getSQLState());
						}
						return userprof;
					}});

				usercredsget = jdbcTemplate.queryForObject(DBConnectorConstants.USERCREDGETQUERYBYUSERID, 
						new Object[]{userprofile.getCompid()},
						new RowMapper<UserCredentials>(){

					@Override
					public UserCredentials mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						UserCredentials usercreds = null;
						try 
						{
							usercreds = new UserCredentials();
							usercreds.setUsername(rs.getString("USERNAME"));
							usercreds.setPassword(rs.getString("PASSWORD"));
							usercreds.setCreated(rs.getDate("CREATED").toString());
							usercreds.setModified(rs.getDate("MODIFIED").toString());
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}
						return usercreds;
					}

				});

				userprofileget.setUsercreds(usercredsget);
				transactionManager.commit(status);
			}
			else
			{
				transactionManager.rollback(status);
				throw new UserProfileInsertException("Error in creating userprofile. ");
			}

		}


		catch(UserProfileInsertException e)
		{
			transactionManager.rollback(status);
			System.out.println("User profile not created for the following values : " +userprofile.toString());
			e.printStackTrace();
			throw new UserProfileInsertException("Error while creating user profile."+e);

		}
		catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			throw new Exception(e);
		}
		return userprofileget;



	}

	private boolean checkIfCreateProfileSuccess(int rows) throws UserProfileInsertException
	{
		if(rows > 0)
			return true;
		else
			throw new UserProfileInsertException("User profile not created.");
	}

		public static void main(String[] args) {
			try{
			UserProfile userprofile = new UserProfile();
			userprofile.setAddress("test");
			userprofile.setCompid("DEVTESTBLR51");
			userprofile.setCompname("dev");
			
			DBConnectionDAO4.getInstance().insertUserProfile(userprofile);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			} catch (UserProfileInsertException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
