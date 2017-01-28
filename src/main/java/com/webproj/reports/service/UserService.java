package com.webproj.reports.service;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.hadoop.hdfs.web.resources.UserProvider;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.research.ws.wadl.Application;
import com.webproj.reports.dao.DBConnectionDAO;
import com.webproj.reports.exception.UserProfileInsertException;
import com.webproj.reports.jaxb.UserProfileElement_old;
import com.webproj.reports.jaxb.Userprofileelement;
import com.webproj.reports.vo.UserProfile;





@Path("/user")
public class UserService {


	
	@GET
	@Path("/userslist")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public Response getAllUsers(int startindex,int size)
	{
		try
		{
			Response response = null;
			List<UserProfile> userproflist = DBConnectionDAO.getInstance().getAllUsers(startindex, size);
			return  Response.status(200).entity(userproflist).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return  Response.status(500).build();
		}
		
		
	}
	
	@GET
	@Path("/validatelogin")
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public Response validateLogin(@QueryParam("username") String username, @QueryParam("password") String password) throws SQLException,Exception
	{
		Response  response = null;
		try {
			if(DBConnectionDAO.getInstance().validateLogin(username, password))
			{
				response =  Response.status(200).entity("true").build();
			}
			else
			{
				response =  Response.status(200).entity("false").build();
			}
		} catch (SQLException e) {
			e.printStackTrace();			
			throw new SQLException(e);
		} catch (Exception e) {

			e.printStackTrace();
			throw new Exception(e);
		}
		finally
		{
			DBConnectionDAO.removeInstance();
		}
		return response;
		
	}
	
	
	@GET
	@Path("/profile")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public UserProfile getUserProfile(@QueryParam("compId") String compId)
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
	
	@PUT
	@Path("/updateprofile")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public UserProfile updateProfile(UserProfile userprofile) 
	{
		return null;
	}
	@POST
	@Path("/createprofile")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public UserProfile createProfile(UserProfile userprofile) 
	{		
		try 
		{				
			return DBConnectionDAO.getInstance().insertUserProfile(userprofile);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			
		} catch (UserProfileInsertException e) {
			
			e.printStackTrace();
		}
		
		finally
		{
			DBConnectionDAO.removeInstance();
		}
		return null;
	}
	
	public static void main(String[] args)
	
	{
		/*
		try {
			
			Client client = new Client();			
			WebResource service = client.resource(UriBuilder.fromUri("http://localhost:8041/DBConnector/rest/user/profile").build());		
			ClientResponse response = service.queryParam("userId", "dev").get(ClientResponse.class);
			System.out.println("response = "+response);
		} 
		catch (Exception e) 
		{
		  e.printStackTrace();
		}*/
			
		
		
		UserProfile userprofile = new UserProfile();
		//Userprofileelement userprofile = new Userprofileelement();
		userprofile.setCompname("devashish");
		userprofile.setCompid("devashish");
		userprofile.setAddress("bangalore");
		userprofile.setRetention(1);
		
	//	CommonHelper.validate(userprofile);
		
		Client client = new Client();
		try {
		WebResource service = client.resource(UriBuilder.fromUri("http://localhost:8041/DBConnector/rest/user/createprofile").build());
		ClientResponse response  = service.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, userprofile);
		ObjectMapper mapper = new ObjectMapper();
		
			Userprofileelement newprof = mapper.readValue(response.getEntityInputStream(), Userprofileelement.class);
			System.out.println(newprof.toString());
		} /*catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}

