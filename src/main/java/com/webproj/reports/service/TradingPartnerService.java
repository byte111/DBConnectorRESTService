package com.webproj.reports.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.webproj.reports.dao.DBConnectionDAO;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.Tradpartners;

@Path("/tradpartners")
public class TradingPartnerService {

	
	@POST
	@Path("/createpartnership")
	@Consumes({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	@Produces({javax.ws.rs.core.MediaType.APPLICATION_XML,javax.ws.rs.core.MediaType.APPLICATION_JSON})
	public Response createProfile(Tradpartners tradpartners) throws TradingPrtnerException, Exception
	{		
		Partners responsedata = null;
		try 
		{				
			DBConnectionDAO connobj =  DBConnectionDAO.getInstance();
			
			responsedata = tradpartners.getPartners().get(0);
			connobj.insertTradingPartnership(responsedata);
			
			
		}
		catch( TradingPrtnerException e)
		{
			e.printStackTrace();
			throw new TradingPrtnerException(e.getMessage());
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
			
		} 	
		finally
		{
			
			DBConnectionDAO.removeInstance();
		}
		return  Response.status(HttpStatus.CREATED.value()).entity(tradpartners).build();
	}
	
	
	@ExceptionHandler(Exception.class)
	  public ResponseEntity<String> handleException(Exception ex) {
	
	    return  new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	
	@ExceptionHandler(TradingPrtnerException.class)
	  public ResponseEntity<String> handleTPException(TradingPrtnerException ex) {
		
	    return  new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	
	
	public static void main(String[] args) {
		
	}
	
}
