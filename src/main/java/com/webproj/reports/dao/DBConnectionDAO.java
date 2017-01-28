package com.webproj.reports.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.KeyHolder;

import com.webproj.constants.DBConnectorConstants;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.exception.UserProfileInsertException;
import com.webproj.reports.helper.Utility;
import com.webproj.reports.helper.XMLReaderHelper;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.Tradpartners;
import com.webproj.reports.jaxb.UserProfileDetails;
import com.webproj.reports.service.TradingPartnerService;
import com.webproj.reports.vo.BillingMO;
import com.webproj.reports.vo.IPreparedStatementSetterObj;
import com.webproj.reports.vo.UserCredentials;
import com.webproj.reports.vo.UserProfile;


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
		String sqlSelect = "SELECT * FROM  USERCREDS WHERE USERNAME = ? AND PASSWORD = ?";
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

	public List<UserProfile> getAllUsers(final int startindex , final int size) throws Exception
	{
		final String query  = DBConnectorConstants.USERPROFGETALLQUERY;

		List<Map<String, Object>> userproflist = new  ArrayList<Map<String, Object>>();

		try
		{
			userproflist = jdbcTemplate.queryForList(query, new Object[]{startindex + size , startindex});
			//System.out.println(userproflist.size());


			Iterator it= userproflist.iterator();
			List<UserProfile> userprofilelistfinal = new ArrayList<UserProfile>();
			while(it.hasNext())
			{
				Map<String, Object> map = (Map<String, Object>) it.next();
				UserProfile temp = new  UserProfile();
				//temp.setAddress(map.get("compid").toString());
				temp.setCompid(map.get("compid").toString());
				temp.setCompname(map.get("compname").toString());			
				temp.setAddress(map.get("address").toString());
				temp.setRetention(Integer.parseInt(map.get("retention").toString()));
				userprofilelistfinal.add(temp);
			}
			return userprofilelistfinal;

		}

		catch(Exception e )
		{
			e.printStackTrace();
			throw new Exception(e);
		}

	}
	public UserProfile getUserProfileDets(String compid) throws Exception
	{

		UserProfile userprof = null;
		try {

			String query = DBConnectorConstants.USERPROFGETQUERYBYUSERID;
			userprof = jdbcTemplate.queryForObject(query, new Object[]{compid}, new RowMapper<UserProfile>(){

				@Override
				public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserProfile userprof = new UserProfile();
					userprof.setCompid(rs.getString("COMPID"));
					userprof.setCompname(rs.getString("COMPNAME"));
					userprof.setAddress(rs.getString("ADDRESS"));
					userprof.setRetention(rs.getInt("RETENTION"));
					return userprof;
				}});


		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception("Error in getting user profile : " + e.getMessage());
		}

		return userprof;
	}


	// need not pass tag parameter. this is just to overload method
	public UserProfileDetails getUserProfileDets(String compid,String tag) throws Exception
	{

		UserProfileDetails userprof = null;
		try {

			String query = DBConnectorConstants.USERPROFGETQUERYBYUSERID;
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


		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception("Error in getting user profile : " + e.getMessage());
		}

		return userprof;
	}


	public void insertTradingPartnership(Partners partners) throws TradingPrtnerException, Exception
	{
		String query = DBConnectorConstants.TARDPARTINSERTQUERY;
		try 
		{	
			validateTradingPartnerDetails(partners);		

			int rows = jdbcTemplate.update(query, new Object[]{
					partners.getPartnershipid(),
					partners.getOwneruser().getCompid(),
					partners.getPartneruser().getCompid(),
					partners.getCharges(),
					new java.sql.Date(System.currentTimeMillis()),
					new java.sql.Date(System.currentTimeMillis())
			});

			if(rows <= 0)
			{

				throw new TradingPrtnerException("Error in inserting trading partner data in DB.");
			}


		} 

		catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}



	}

	public void updateTradingPartnership(Partners partners)
	{

	}

	public void getAllTradingPartners(final String compid) throws Exception
	{
		final String query = DBConnectorConstants.TARDPARTSELECTQUERY;
		try
		{
			jdbcTemplate.query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, compid);					
					return ps;
				}
			}, new RowMapper<Partners>() {

				@Override
				public Partners mapRow(ResultSet rs, int rowNum) throws SQLException {

					Partners resultset = new Partners();
					resultset.setPartnershipid(rs.getInt("PARTNERSHIPID"));

					UserProfileDetails temp = null;

					// get Owner user details

					try
					{
						temp = getUserProfileDets(rs.getString("OWNERCOMPID"),"");
					} 
					catch (Exception e) 
					{						
						e.printStackTrace();
						throw new SQLException("Error in getting owner user details in trading partnership. ");
					}

					temp = null;

					resultset.setOwneruser(temp);
					// get Partner user details

					try
					{
						temp = getUserProfileDets(rs.getString("PARTNERCOMPID"),"");
					} 
					catch (Exception e) 
					{						
						e.printStackTrace();
						throw new SQLException("Error in getting partner user details in trading partnership. ");
					}

					resultset.setCharges(rs.getString("CHARGES"));
					resultset.setCreated(rs.getString("CREATED"));

					return resultset;
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}


	public UserProfile insertUserProfile(UserProfile userprofile) throws UserProfileInsertException, Exception
	{
		UserProfile userprofileget = null;
		UserCredentials usercredsget = null;

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

			rows += jdbcTemplate.update(DBConnectorConstants.USERCREDINSERTQUERY, new Object[]{
					userprofile.getCompid(),
					Utility.getFirstTimePassword(),
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
			}
			else
			{
				throw new UserProfileInsertException("Error in creating userprofile. ");
			}

		}


		catch(UserProfileInsertException e)
		{
			System.out.println("User profile not created for the following values : " +userprofile.toString());
			e.printStackTrace();
			throw new UserProfileInsertException("Error while creating user profile."+e);

		}
		catch (Exception e) {
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
		try {
			try {
				System.out.println(DBConnectionDAO.getInstance().getAllUsers(5,5));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void validateTradingPartnerDetails(Partners partners) throws TradingPrtnerException, Exception
	{
		if (partners == null)
		{
			throw new TradingPrtnerException("Partner details cannot be null or empty");
		}
		System.out.println("partners = " + partners.getOwneruser().getCompid() );


		try
		{
			UserProfile owneruser = getUserProfileDets(partners.getOwneruser().getCompid());
			if(owneruser == null)
			{
				throw new TradingPrtnerException("Owner user is not valid. ");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Error validating owner user is not valid. " + e.getMessage());
		}



		try
		{
			UserProfile partneruser = getUserProfileDets(partners.getPartneruser().getCompid());
			if(partneruser == null)
			{
				throw new TradingPrtnerException("Partner user is not valid. ");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Error validating Partner user is not valid. " + e.getMessage());
		}

		if(partners.getCharges() == null)
		{
			throw new TradingPrtnerException("Charge cannot be null. ");
		}


	}



}
