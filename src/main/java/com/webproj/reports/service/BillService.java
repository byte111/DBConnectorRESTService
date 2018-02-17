package com.webproj.reports.service;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationTemp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.webproj.reports.beans.HelloBean;
import com.webproj.reports.beans.IHelloBO;
import com.webproj.reports.dao.DBConnectionDAO;
import com.webproj.reports.dao.DBConnectionDAO1;
import com.webproj.reports.dao.DBConnectionDAO2;
import com.webproj.reports.dao.DBConnectionDAO3;
import com.webproj.reports.exception.TradingPrtnerException;
import com.webproj.reports.helper.XMLReaderHelper;
import com.webproj.reports.jaxb.Partners;
import com.webproj.reports.jaxb.UserProfileDetails;
import com.webproj.reports.vo.BillingMO;


@Path("/bill")
public class BillService {

	/*@Autowired
	IHelloBO helloBO;*/


	@GET
	@Path("/testcomp")
	@Produces("application/json")
	public Response testResponse()
	{	
		/*ApplicationContext context =
	    		new ClassPathXmlApplicationContext("../applicationContext.xml");
		HelloBean helloBean = (HelloBean)context.getBean("helloBO");
		System.out.println(helloBean.getVal());*/
		UserProfileDetails userprofdets;
		try {
			//userprofdets = DBConnectionDAO2.getInstance().getUserProf("dev");
			//System.out.println(userprofdets.getAddress());
			
			/*Partners partners = new Partners();
			partners.setPartnershipid(111);
			
			DBConnectionDAO2.getInstance().insertTradingPartnership(partners);*/
			
			
			
			
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return Response.status(200).entity(" BILL Component is UP!!").build();
	}
	
	@PUT
	@Path("/testcomp1/{id}")
	@Produces("application/json")
	public Response testResponse1(@PathParam("id") String id)
	{	
		return Response.status(200).entity(" BILL Component is UP!!" + id).build();
	}
	
	@POST
	@Path("/testcomp1/{id}")
	@Produces("application/json")
	public Response testResponse2(@PathParam("id") String id)
	{	
		return Response.status(200).entity(" BILL Component is UP!!" + id).build();
	}


	/*@POST
	@Consumes("application/xml")
	@Path("/postBillEvnt")
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public Response createBillingRecord(InputStream in,String agent) throws ParserConfigurationException, SAXException, IOException
	{
		int res = 0;
		BillingMO billingMO= null;
		try {
			
			InputStreamReader reader  = new InputStreamReader(in);
			System.out.println("Reading file in BillService... in = " + in );
			
			int ch = reader.read();
		System.out.println("ch  = "+ch);
			while(ch != -1 )				
			{
				
				System.out.println(ch);
				ch = reader.read();
			}
			billingMO = new XMLReaderHelper().readBillingFile(in);
		} catch (Exception e1) {
			System.out.println("Exception in reading input from client.");
			e1.printStackTrace();
		}
		try {
			res = DBConnectionDAO.getInstance().insertBillEvent(billingMO, agent);
		} catch (Exception e) {

			System.out.println("Exception in inserting billing event.");		
			e.printStackTrace();
		}
		if(res > 0 )
		{
		return Response.status(200).entity("true").build();
		}
		else
		{
			return  Response.status(200).entity("false").build();
		}
	}*/
	
	
	@POST
	@Consumes("application/xml")
	@Path("/postBillEvnt")
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public Response createBillingRecord(String billevent) throws ParserConfigurationException, SAXException, IOException
	{
		int res = 0;
		BillingMO billingMO= null;

		System.out.println("Incoming request  = "+ billevent);
		
		try {
		
			billingMO = new XMLReaderHelper().readBillingFile(billevent);
			
			System.out.println("billiing event obj after reading xml = "+billingMO.toString());
		} catch (Exception e1) {
			System.out.println("Exception in reading input from client.");
			e1.printStackTrace();
		}
		try {
			
			res = DBConnectionDAO.getInstance().insertBillEvent(billingMO, "BILLSERVICEREST");
			
		} catch (Exception e) {

			System.out.println("Exception in inserting billing event.");		
			e.printStackTrace();
		}
		
		finally
		{
			DBConnectionDAO.removeInstance();
		}
		if(res > 0 )
		{
		return Response.status(200).entity("true").build();
		}
		else
		{
			return  Response.status(200).entity("false").build();
		}
	
		
		
		
	}
	
	
	
	@GET
	@Path("/testproc/{compid}")
	@Produces("application/json")
	public Response testResponse3(@PathParam("compid") String compid)
	{	

		UserProfileDetails userprofdets;
		try {

			return Response.status(200).entity(DBConnectionDAO3.getInstance().callTestProcedure(compid)).build();
			
		} catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	

}
