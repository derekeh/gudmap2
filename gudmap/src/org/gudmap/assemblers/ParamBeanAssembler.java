package org.gudmap.assemblers;

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
	private Map<String,String>allassaytypelist;
	private Map<String,String>theilerstagelist;
	private Map<String,String>sexlist;
	private Map<String,String>specimentypelist;
	private Map<String,String>geneoptionlist;
	private Map<String,String>annotationtypelist;
	private Map<String,String>imagedirlist;
	
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
	
	public Map<String,String> getAllassaytypelist() {
		allassaytypelist = new LinkedHashMap<String,String>();
		String queryString=GenericQueries.ALL_ASSAY_TYPES;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next())
				allassaytypelist.put(result.getString(1), result.getString(1));
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return allassaytypelist;
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
			sexlist.put("ALL","ALL");
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
	
	public Map<String,String> getGeneoptionlist() {
		geneoptionlist = new LinkedHashMap<String,String>();
		geneoptionlist.put("Expression Summaries", "Expression Summaries");
		geneoptionlist.put("GUDMAP Entries", "GUDMAP Entries");
		
		return geneoptionlist;
	}
	
	public Map<String,String> getAnnotationtypelist() {
		annotationtypelist = new LinkedHashMap<String,String>();
		annotationtypelist.put("Direct Annotations", "Direct Annotations");
		annotationtypelist.put("Direct and Derived Annotations", "Direct and Derived Annotations");
		
		return annotationtypelist;
	}
	
	public Map<String,String> getImagedirlist() {
		imagedirlist = new LinkedHashMap<String,String>();
		imagedirlist.put("anatomy","anatomy");
		imagedirlist.put("general","general");
		imagedirlist.put("help","help");
		imagedirlist.put("icons","icons");
		imagedirlist.put("tissue","tissue");
		
		return imagedirlist;
	}

	
}
