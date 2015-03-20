package org.gudmap.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.generic.AutocompleteQueries;
import org.gudmap.utils.Utils;

@Named
@RequestScoped
public class AutocompleteBean {
	
	ArrayList<String> geneList=null;
	ArrayList<String> anatomyList=null;
	ArrayList<String> geneFunctionList=null;
	private String geneInput="";
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public AutocompleteBean() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		populateGeneList();
		populateAnatomyList();
		populateGeneFunctionList();
	}
	
	public void setGeneList(ArrayList<String> geneList) {
		this.geneList=geneList;
	}
	
	public ArrayList<String> getGeneList() {
		return geneList;
	}
	
	public void populateGeneList() {
		String queryString=AutocompleteQueries.GENE_SYMBOLS_AND_SYNONYMS2;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				geneList = new ArrayList<String>();
				while (result.next()) {
					geneList.add(result.getString(1));
				}
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
	}
	
	public void populateAnatomyList() {
		String queryString=AutocompleteQueries.ANNATOMY_TERMS;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				anatomyList = new ArrayList<String>();
				while (result.next()) {
					anatomyList.add(result.getString(1));
				}
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
	}
	
	public void populateGeneFunctionList() {
		String queryString=AutocompleteQueries.GO_TERMS;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				geneFunctionList = new ArrayList<String>();
				while (result.next()) {
					String goterm=Utils.filterNoiseCharacters(result.getString(1));
					geneFunctionList.add(goterm);
				}
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
	
    }
	
	public void setGeneInput(String geneInput) {
		this.geneInput = geneInput;
	}
	
	public String getGeneInput() {
		return geneInput;
	}
	
	public ArrayList<String> completeGene(String input) {
		ArrayList<String> matches = new ArrayList<String>();
		Iterator<String> iterator = geneList.iterator();
		String str="";
		while(iterator.hasNext()){
			str = iterator.next().toString();
			if(str.toUpperCase().startsWith(input.toUpperCase()))
				matches.add(str);
		}
		
		return matches;
	}
	
	public ArrayList<String> completeAnatomy(String input) {
		ArrayList<String> matches = new ArrayList<String>();
		Iterator<String> iterator = anatomyList.iterator();
		String str="";
		while(iterator.hasNext()){
			str = iterator.next().toString();
			if(str.toUpperCase().startsWith(input.toUpperCase()))
				matches.add(str);
		}
		
		return matches;
	}
	
	public ArrayList<String> completeGeneFunction(String input) {
		ArrayList<String> matches = new ArrayList<String>();
		Iterator<String> iterator = geneFunctionList.iterator();
		String str="";
		while(iterator.hasNext()){
			str = iterator.next().toString();
			if(str.toUpperCase().startsWith(input.toUpperCase()))
				matches.add(str);
		}
		
		return matches;
	}

}
