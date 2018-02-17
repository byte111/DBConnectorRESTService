package com.webproj.reports.service;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webproj.reports.beans.HelloBean;
import com.webproj.reports.dao.DBConnectionDAO;
import com.webproj.reports.dao.DBConnectionDAO1;
import com.webproj.reports.dao.DBConnectionDAO2;
import com.webproj.reports.dao.DBConnectionDAO3;
import com.webproj.reports.dao.DBConnectionDAO4;
import com.webproj.reports.dao.MappingQueryTEst;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.exception.UserProfileInsertException;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.UserProfileDetails;
import com.webproj.reports.jaxb.Userprof;
import com.webproj.reports.vo.UserCredentials;
import com.webproj.reports.vo.UserProfile;


@Path("/daotest")
public class TestService {
	
	HelloBean helloBO;
	/*
	 * 
	 * To test if the component id up and running 
	 * e.g. http://localhost:8041/DBConnector/rest/daotest/testcomp
	 * 
	 */
	@GET
	@Path("/testcomp")
	@Produces("application/json")
	public Response testResponse()
	{
		System.out.println("helloBO="+helloBO);
		return Response.status(200).entity(" DB Connector is UP !!").build();
	}
	
	
	/*========================================     jdbc template      ==============================================================*/
	
	
	/*
	 *  Below method uses jdbc template and uses queryForObject method to fetch a object
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/profile?compId=dev
	 */
	
	
	@GET
	@Path("/profile")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public UserProfile getUser(@QueryParam("compId") String compId)
	{
	
		UserProfile userprofile = null;
		
		try 
		{
			DBConnectionDAO conn = DBConnectionDAO.getInstance();
			userprofile = conn.getUserProfileDets(compId);			
		
		} 
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			DBConnectionDAO.removeInstance();
		}
		
		return  userprofile;
	}
	
	/*
	 *  Below method uses jdbc template and uses queryForList method to fetch a list of objects 
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/userslist
	 */
		
	@GET
	@Path("/userslist")
	//@Produces({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	@ResponseBody
	public Response getAllUsers()
	{
		try
		{
			System.out.println("started");
			Response response = null;
			Userprof  userresp = new Userprof();
			List<UserProfile> userlist= DBConnectionDAO.getInstance().getAllUsers(0	, 100);
			
			for(UserProfile u : userlist)
			{
				UserProfileDetails temp = new UserProfileDetails();
				temp.setCompname(u.getCompname());
				temp.setAddress(u.getAddress());
				temp.setCompid(u.getCompid());
				userresp.getUserprofile().add(temp);
			}
			System.out.println("getting ");
			return Response.status(HttpStatus.OK.value()).entity(userresp).build();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}	
		
	}
	
	/*
	 *  Below method uses jdbc template and uses queryForList method to fetch a list of objects 
	 *  This method accepts request body but currently hardcoded in this method for testing purpose.
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/addtp
	 *  
	 *  Request Body:
	 *  
	 * 
			{
			"partnershipid":100,
			"owneruser":
						{
							"compname":"dev test company",
							"compid":"dev",
							"address":"BLR",
							"retention":100,
							"userCreds":null
							
						},
			"partneruser":{
							"compname":"Devashish",
							"compid":"dev1",
							"address":"Bangalore",
							"retention":1,
							"userCreds":null
				
						},
			"charges":"Sender",
			"created":null
			
		}
	 */
	
	@POST
	@Path("/addtp")
	//@RequestMapping(method = RequestMethod.POST,value="/addtp")
	@ResponseBody
	public Response insertTradingPartnership(@RequestBody Partners partners)
	{
		
		try{
		DBConnectionDAO conn = DBConnectionDAO.getInstance();
		/*Partners partners = new Partners();	
		partners.setOwneruser(conn.getUserProfileDets1("dev"));
		partners.setPartneruser(conn.getUserProfileDets1("dev1"));
		partners.setPartnershipid(100);
		partners.setCharges("Sender");*/
		conn.insertTradingPartnership(partners);
		
		return Response.status(201).entity("Data inserted successfully !! ").build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		} catch (TradingPrtnerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		}
		
	}
	
	
	/*========================================    Named parameter jdbc template      ==============================================================*/

	/*
	 *  Below method uses named jdbc template and uses queryForObject method to fetch a object
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/namedprofile?compId=dev
	 */
	
	
	@GET
	@Path("/namedprofile")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Response getUserNamed(@QueryParam("compId") String compId)
	{
	
		UserProfileDetails userprofile = null;
		
		try 
		{
			DBConnectionDAO1 conn = DBConnectionDAO1.getInstance();
			userprofile = conn.getUserProf(compId);			
		
		} 
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		finally
		{
			DBConnectionDAO.removeInstance();
		}
		
		return  Response.status(200).entity(userprofile).build();
	}
	
	/*========================================    Simple Jdbc Insert      ==============================================================*/
	
	/*
	 *  Below method uses simple jdbc insert.
	 *  This method accepts request body.
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/addtp1
	 *  
	 *  Request Body:
	 *  
	 * 
			{
			"partnershipid":100,
			"owneruser":
						{
							"compname":"dev test company",
							"compid":"dev",
							"address":"BLR",
							"retention":100,
							"userCreds":null
							
						},
			"partneruser":{
							"compname":"Devashish",
							"compid":"dev1",
							"address":"Bangalore",
							"retention":1,
							"userCreds":null
				
						},
			"charges":"Sender",
			"created":null
			
		}
	 */
	
	@POST
	@Path("/addtp1")
	//@RequestMapping(method = RequestMethod.POST,value="/addtp1")
	@ResponseBody
	public Response insertTradingPartnership1(@RequestBody Partners partners)
	{
		
		try{
		DBConnectionDAO2 conn = DBConnectionDAO2.getInstance();
		/*Partners partners = new Partners();	
		partners.setOwneruser(conn.getUserProfileDets1("dev"));
		partners.setPartneruser(conn.getUserProfileDets1("dev1"));
		partners.setPartnershipid(100);
		partners.setCharges("Sender");*/
		conn.insertTradingPartnership(partners);
		
		return Response.status(201).entity("Data inserted successfully !! ").build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		} catch (TradingPrtnerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		}
		
	}
	
	
	/*========================================    Simple Jdbc Call      ==============================================================*/
	/*
	 *  Below method uses named jdbc template and uses queryForObject method to fetch a object
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/userdets1?compId=dev
	 */
	
	@GET
	@Path("/userdets1")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	
	public Response getUserDets1(@QueryParam("compId") String compId)
	{
		try{
		UserProfileDetails result =  DBConnectionDAO3.getInstance().callTestProcedure(compId);
		return Response.status(200).entity(result).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		}
		
	}
	
	
	/*========================================   Mapping SQL Query   ==============================================================*/
	/*
	 *  Below method uses mapping sql query  and uses findObject method to fetch a object
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/userdets2?compId=dev
	 */
	
	
	@GET
	@Path("/userdets2")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	
	public Response getUserDets2(@QueryParam("compId") String compId)
	{
		try{
		UserProfileDetails result =  new MappingQueryTEst().getUserDetails1(compId);
		return Response.status(200).entity(result).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		}
	}
	
	
	/*
	 *  Below method uses mapping sql query  and uses findObject method to fetch a object
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/userdets2?compId=dev&address=BLR
	 */
	
	/*@GET
	@Path("/userdets3")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	
	public Response getUserDets2(@QueryParam("compId") String compId,@QueryParam("address") String address)
	{
		try{
			UserProfileDetails result =  new MappingQueryTEst().getUserDetails2(compId,address);
		return Response.status(200).entity(result).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("Error !! ").build();
		}
	}*/
	
	/*========================================   Transaction Management   ==============================================================*/
	/*
	 *  Below method uses transaction management
	 *  
	 *  E.g. :  http://localhost:8041/DBConnector/rest/daotest/createuser
	 */
	
	@GET
	@Path("/createuser")
	public Response createUser()
	{
		try {
		UserProfile userprofile  = new UserProfile();
		UserCredentials usercreds  = new UserCredentials();
		userprofile.setAddress("Bangalore");
		userprofile.setCompid("DEVTESTBLR51");
		userprofile.setCompname("Devashish Test");
		userprofile.setRetention(100);
		usercreds.setUsername("DEVTESTBLR51");
		usercreds.setPassword("password");
		userprofile.setUsercreds(usercreds);
		DBConnectionDAO4 conn = DBConnectionDAO4.getInstance();
		
		UserProfile resukt = 	conn.insertUserProfile(userprofile);
			System.out.println("resukt = " + resukt.getAddress());
			return Response.status(200).build();
		} catch (UserProfileInsertException | Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@ExceptionHandler(Exception.class)
	  public ResponseEntity<String> handleException(Exception ex) {
	
	    return  new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	
	public static void main(String[] args) {
		try{
		DBConnectionDAO conn = DBConnectionDAO.getInstance();
		Partners partners = new Partners();	
		partners.setOwneruser(conn.getUserProfileDets1("dev"));
		partners.setPartneruser(conn.getUserProfileDets1("dev1"));
		partners.setPartnershipid(100);
		partners.setCharges("Sender");
		partners.setCreated(new Date()+"");
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, partners);
		System.out.println(writer.toString());
		conn.insertTradingPartnership(partners);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} catch (TradingPrtnerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
		
}
