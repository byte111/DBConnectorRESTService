package com.webproj.reports.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.webproj.reports.vo.BillingMO;

public class XMLReaderHelper {

	public BillingMO readBillingFile(String billevnt) throws SAXException, IOException, ParserConfigurationException,Exception
	{
		String billingId = null,senderId = null,receiverId=null;
		float fileize;
		Long timestamp;
		DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder  builder = docfactory.newDocumentBuilder();
		String filename = "billevent"+System.currentTimeMillis()+".tmp";
		File tmpfile = new File("C:\\Temp\\webprojdatastore\\"+filename);
		FileWriter fileWriter = new FileWriter(tmpfile);
		fileWriter.write(billevnt);
		fileWriter.flush();
		
		InputStream in = new FileInputStream(new File("C:\\Temp\\webprojdatastore\\"+filename));
		
		Document billDoc = builder.parse(in);
		NodeList nodeList  = billDoc.getElementsByTagName("BillEvent");
		if(nodeList.getLength() > 1)
		{
			fileWriter.close();
			throw new Exception("Invalid BillEvent file format ");
		}



		Node node = nodeList.item(0);
		Element element = (Element)node;
		BillingMO billingEventObj = new BillingMO();
		billingId = element.getElementsByTagName("billingId").item(0).getTextContent();
		senderId = element.getElementsByTagName("senderId").item(0).getTextContent();
		receiverId = element.getElementsByTagName("receiverId").item(0).getTextContent();
		fileize = Float.parseFloat(element.getElementsByTagName("fileize").item(0).getTextContent());
		timestamp = Long.parseLong(element.getElementsByTagName("timestamp").item(0).getTextContent());
		
		

		try
		{
			billingEventObj.setBillingId(billingId);
			billingEventObj.setFileSize(fileize);
			billingEventObj.setReceiverId(receiverId);
			billingEventObj.setSenderId(senderId);
			billingEventObj.setTimestamp(timestamp);
		}
		catch(Exception e)
		{
			throw new Exception("Unable to set values to BillingMO.Exiting.");
		}
		
		finally
		{
			if(fileWriter != null)
			{
				fileWriter.close();
			}
		}
		
		return billingEventObj;
	}


	public HashMap<String,String> getSchemaDef(String nodeName) throws Exception
	{
		HashMap<String,String> schemaMap = new LinkedHashMap<String,String>();
		File file= new File("C:/devwrkspc/pwd/sts_wrspc/testwrspc/DBConnector/src/main/webapp/WEB-INF/DBSchemaDef.xml");
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file);
		NodeList nodeList = doc.getElementsByTagName("schema");

		for(int i=0;i<nodeList.getLength();i++)
		{
			Node node = nodeList.item(i);
			Element element  = (Element)node;
			
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				if(nodeName.trim().toUpperCase().equals(element.getAttribute("name")))
				{				
					int attrlength = element.getElementsByTagName("column").getLength();					
					for (int j = 0; j < attrlength; j++) {
						Element colElement =  (Element) element.getElementsByTagName("column").item(j);						
						schemaMap.put(colElement.getAttribute("name"),colElement.getAttribute("type"));				
						
						}
				}
				break;
			}
		}

		if(schemaMap.size() == 0)
		{
			throw new Exception("Schema definition not found.");
		}
		return schemaMap;
	}


	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, Exception {

		System.out.println(new XMLReaderHelper().getSchemaDef("BILLEVENT"));


	}
}
