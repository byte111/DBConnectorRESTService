package com.webproj.reports.service;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.webproj.reports.dao.DBConnectionDAO;

@Path("/user")
public class UserService {


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
		
		return response;
		
	}
}
