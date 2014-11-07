package org.gudmap.beans.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.globals.Globals;

public class ParamBeanAssembler {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private Map<String,String>sourcelist;
	private Map<String,String>assaytypeinsitulist;
	private Map<String,String>theilerstagelist;
	private Map<String,String>sexlist;
	private Map<String,String>specimentypelist;
	
	public ParamBeanAssembler() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}		
	}
	
	
	public Map<String,String> getSourcelist() {
		sourcelist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_SOURCES;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next())
				sourcelist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return sourcelist;
	}
	
	public Map<String,String> getAssaytypeinsitulist() {
		assaytypeinsitulist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_ASSAY_TYPES_INSITU;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next())
				assaytypeinsitulist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return assaytypeinsitulist;
	}
	
	public Map<String,String> getTheilerstagelist() {
		theilerstagelist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_THEILER_STAGES;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			theilerstagelist.put("ALL","ALL");
			while(result.next())
				theilerstagelist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return theilerstagelist;
	}
	
	public Map<String,String> getSexlist() {
		sexlist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_SEXES;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next())
				sexlist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return sexlist;
	}
	
	public Map<String,String> getSpecimentypelist() {
		specimentypelist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_SPECIMEN_TYPES;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next())
				specimentypelist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return specimentypelist;
	}
	
	
}