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

@Named
@RequestScoped
public class AutocompleteBean {
	
	ArrayList<String> geneList=null;
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
		matches.add("Derek");
		matches.add("Houghton");
		
		return matches;
	}
	
	public ArrayList<String> completeGeneFunction(String input) {
		ArrayList<String> matches = new ArrayList<String>();
		matches.add("Gudmap");
		matches.add("Project");
		
		return matches;
	}

}
