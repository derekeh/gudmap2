package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.queries.genestrip.GeneStripQueries;
import org.gudmap.utils.Utils;

public class GeneFunctionDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public GeneFunctionDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public String[] getGeneSymbols(String[] input, String queryString, String wildcard) {
		String [] geneSymbols=null;
		String[] goIdList = null;
		String [] symbolsQParts;
		boolean found=false;
		int rowCount=0;
		
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			for(int i=0;i<input.length;i++){
				if(wildcard.equalsIgnoreCase("contains")) {
					ps.setString(i+1, "%"+input[i].trim()+"%");
				}
				else if(wildcard.equalsIgnoreCase("starts with")){
					ps.setString(i+1, input[i].trim()+"%");
				}
				else {
					ps.setString(i+1, input[i].trim());
				}
		    }
			result =  ps.executeQuery();
			if (result.first()) {
				found=true;
				result.last();
				rowCount = result.getRow();
				result.beforeFirst();
				goIdList = new String[rowCount];
				int i = 0;
				//ad the lsit of go ids to an array
				while (result.next()) {
				    goIdList[i] = result.getString(1);
				    i++;
				}			
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		if(found){
			//get components to build query to find gene symbols using GO ids found above
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefMgiGoGene_MrkSymbol");
			//create the query string from the GO id list and the components stored in symbolsQParts
			String symbolsFromGoIdListQ = Utils.getSymbolsFromGeneInputParamsQuery(goIdList,symbolsQParts[0], symbolsQParts[1], 1);
			
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(symbolsFromGoIdListQ); 
				for(int i=0;i< goIdList.length;i++){
					ps.setString(i+1, goIdList[i].trim());
				}
				result =  ps.executeQuery();
				if (result.first()) {
					result.last();
				    rowCount = result.getRow();
				    result.beforeFirst();
				    geneSymbols = new String [rowCount];
				    int i = 0;
				    //add list of symbols to an array
				    while (result.next()) {
						geneSymbols[i] = result.getString(1);
						i++;
				    }
				}				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}			
		}		
		return geneSymbols;			
	}

}
