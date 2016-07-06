package com.webproj.reports.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.webproj.reports.exception.DataTypeMismatchException;
import com.webproj.reports.vo.BillingMO;
import com.webproj.reports.vo.IPreparedStatementSetterObj;



public class PreparedStatementSetter implements PreparedStatementCallback {

	IPreparedStatementSetterObj obj = null;
	HashMap<String,String> schemaDef= null;
	public PreparedStatementSetter(BillingMO obj,HashMap<String,String> schemaDef)
	{
		this.obj = obj;
		this.schemaDef = schemaDef;
		
	}
	@Override
	public Object doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
		
		Iterator<String> it= (Iterator<String>) schemaDef.keySet();
		while(it.hasNext())
		{
			String type = schemaDef.get(it.next());
			switch(type)
			{
				case "String":
				//	pstmt.setString(1,obj.);
					break;
					default:
						throw new SQLException(type +" is not a valid Data tyep.");
			}
		}
		return null;
	}
	

}
