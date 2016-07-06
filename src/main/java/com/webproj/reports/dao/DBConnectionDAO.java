package com.webproj.reports.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.webproj.reports.helper.XMLReaderHelper;
import com.webproj.reports.vo.BillingMO;
import com.webproj.reports.vo.IPreparedStatementSetterObj;


public class DBConnectionDAO {

	private static JdbcTemplate jdbcTemplate= null;
	private static DBConnectionDAO daoObj = null;
	private DBConnectionDAO()
	{
		try {
			jdbcTemplate = getJdbcTemplateInstance();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private  SimpleDriverDataSource getDataSource() throws Exception{

		SimpleDriverDataSource datasource = new SimpleDriverDataSource();
		try {			
			datasource.setDriver(new oracle.jdbc.driver.OracleDriver());
			datasource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
			datasource.setUsername("SYSTEM");
			datasource.setPassword("dev");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Exception in getting datasource");
		}

		return datasource;
	}

	public static DBConnectionDAO getInstance()
	{
		if(daoObj!=null)
		{
			return daoObj;
		}
		else
		{
			return new DBConnectionDAO();
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


	public int insertBillEvent(final BillingMO billingMO,final String AGENT) throws Exception
	{
		String sqlInsert = "INSERT INTO BILLEVENT(BILLINGID,SENDERID,RECEIVERID,CHARCOUNT,EVNTDATETIME,AGENT) VALUES (?,?,?,?,?,?)";

		System.out.println("jdbcTemplate = "+jdbcTemplate +"billingMO"+billingMO.toString());
		int	result = jdbcTemplate.execute(sqlInsert, new PreparedStatementCallback<Integer>() {


			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, billingMO.getBillingId());
				ps.setString(2, billingMO.getSenderid());
				ps.setString(3, billingMO.getReceiverId());
				ps.setFloat(4, billingMO.getFileSize());
				ps.setDate(5,new java.sql.Date(billingMO.getTimestamp()));
				ps.setString(6,AGENT);				
				return  ps.executeUpdate();
			}	
		});


		System.out.println("result = "+result);
		return result;
	}


	public boolean validateLogin(final String username, final String password) throws SQLException, Exception
	{
		ResultSet  result = null;
		String sqlSelect = "SELECT * from  usercreds where username = ? and password = ?";
		try
		{
			 result = jdbcTemplate.execute(sqlSelect, new PreparedStatementCallback<ResultSet>() {

				@Override
				public ResultSet doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					
					try {
						ps.setString(1, username.trim());
						ps.setString(2, password.trim());			
					} 
					
					catch(SQLException se)
					{
						se.printStackTrace();
						throw new SQLException(se);
					}
					
					/*finally
					{
						if(ps != null)
						{
							ps.close();
						}
					}*/
					
					ResultSet temprs =  ps.executeQuery();
					
					
					if(temprs.next())
					{
						return temprs;
					}
					else
					{
						return null;
					}
				}
			});
			
			System.out.println( " result = "+result);
			if(result != null )
			{	
				return true;
			}
			else
			{
				return false;
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
		
		
	}


}
