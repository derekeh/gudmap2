package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.genestrip.GeneDetailsQueries;
import org.gudmap.utils.Utils;

public class GeneDetailsDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public GeneDetailsDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
    public GeneModel findGeneInfoBySymbol(String symbol) {
        if (symbol == null) {
		    return null;
        }
        GeneModel geneModel = null;
       
        String queryString = GeneDetailsQueries.GENE_INFO;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, symbol);
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel = new GeneModel();
				geneModel.setSymbol(result.getString(1));
				geneModel.setName(result.getString(2));
				geneModel.setMgiAccID(result.getString(3));
		    
	            // synonyms string is '|' delimited, need change it to ','
	            String synonymString = result.getString(4);
	            if (synonymString != null  && !synonymString.trim().equals("")) {
	            	String[] snm = synonymString.split("\\|");
	            	String synonyms = new String("");
	            	int len = snm.length;
	            	for (int i = 0; i < len; i++) {
					    //                	System.out.println(snm[i]);
					    if(i+1 == len) {
						synonyms += snm[i];
					    } else {
						synonyms += snm[i] + ", ";
					    }
					    //                System.out.println(synonyms);
	                }
	            	geneModel.setSynonyms(synonyms);
	            } else {
	            	geneModel.setSynonyms("");
	            }
	            geneModel.setMgiURL(result.getString(5));
	            geneModel.setEnsemblID(result.getString(6));
	            geneModel.setEnsemblURL(result.getString(7));
	            geneModel.setGoURL(result.getString(8));
	            geneModel.setOmimURL(result.getString(9));
	            geneModel.setEntrezURL(result.getString(10));
	            geneModel.setXsomeStart(result.getString(11));
	            geneModel.setXsomeEnd(result.getString(12));
	            geneModel.setXsomeName(result.getString(13));
	            geneModel.setGenomeBuild(result.getString(14));
	            geneModel.setGeneCardURL(result.getString(15));
	            geneModel.setHgncSearchSymbolURL(result.getString(16));
	            geneModel.setUcscURL(result.getString(17));
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = GeneDetailsQueries.TOTAL_GENE_RELATED_ARRAYS;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, symbol);
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel.setNumMicArrays(result.getString(1));
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return geneModel;
      
    } // end findGeneInfoBySymbol
    
    public String findSymbolBySynonym(String synonym){
    	String RET="";
    	String queryString = GeneDetailsQueries.GET_GENE_SYMBOL_BY_SYNONYM;
    	try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, synonym);
			result =  ps.executeQuery();
			if (result.first()) {
				RET=result.getString(1);
			}
					
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return RET;
    }
    
    /**
     * method to get additional info on the gene when it only exists in microarray
     * @param geneInfo - a gene object containing partial data on a gene 
     * @return gene - object containing more complete info on a gene
     */
    public GeneModel findFurtherGeneInfoForMicroarray(GeneModel geneInfo) {
        GeneModel geneModel = geneInfo;
        String syn="";
        if (geneModel == null) {
            return null;
        }
        
        String queryString=ArrayQueries.GENE_INFO_FOR_ARRAY;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, geneModel.getSymbol());
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel.setMgiAccID(result.getString(3)); 
				geneModel.setMgiURL(result.getString(4));
				geneModel.setEnsemblID(result.getString(5));
				geneModel.setEnsemblURL(result.getString(6));
				geneModel.setGoURL(result.getString(7));
				geneModel.setOmimURL(result.getString(8));
				geneModel.setEntrezURL(result.getString(9));
				geneModel.setXsomeStart(result.getString(10));
				geneModel.setXsomeEnd(result.getString(11));
				geneModel.setXsomeName(result.getString(12));
				geneModel.setGenomeBuild(result.getString(13));
				geneModel.setGeneCardURL(result.getString(14));
				geneModel.setHgncSearchSymbolURL(result.getString(15));
				
				syn = result.getString(2);
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        /////////////////
        queryString=GeneDetailsQueries.GENE_SYNONYM_INFO_ARRAY;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, syn);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
			    syn = "";
			    while(result.next()){
					if(result.isLast()){
					    syn = syn + result.getString(1);
					}
					else {
					    syn = syn + result.getString(1) + ", ";
					}
			    }
			    geneModel.setSynonyms(syn);
			}			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        //////////////////
        queryString=GeneDetailsQueries.TOTAL_GENE_RELATED_ARRAYS;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, geneModel.getSymbol());
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel.setNumMicArrays(result.getString(1));
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return geneModel;

    }
    
    public ArrayList<String[]> findRelatedMAProbeBySymbol(String symbol) {
    	String queryString=GeneDetailsQueries.GENE_RELATED_MAPROBE;
    	ArrayList<String[]> relatedMAProbe=null;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, symbol);
			result =  ps.executeQuery();
			relatedMAProbe = Utils.formatResultSetToArrayList(result);						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return relatedMAProbe;
        
    }
    
    /**
     * method to get additional translational info on the gene
     * @param geneInfo - a gene object containing partial data on a gene 
     * @return gene - object containing more complete info on a gene
     */
    public GeneModel addGeneInfoIuphar(GeneModel geneInfo) {
		if (null == geneInfo)
		    return null;
		String str = geneInfo.getMgiAccID();
		if (null == str || str.trim().equals(""))
		    str = geneInfo.getSymbol();
		if (null == str || str.trim().equals(""))
		    return geneInfo;
        
		int type = -1;
		GeneModel geneModel = geneInfo;
		geneModel.setIuphar_db_URL(null); 
		geneModel.setIuphar_guide_URL(null); 
		
		String queryString = GeneDetailsQueries.GENE_INFO_IUPHAR;
		
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, str);
			ps.setString(2, str);
			result =  ps.executeQuery();
			if(result.first()){
				type = result.getInt(3);
				switch (type) {
				case 2:
				    geneModel.setIuphar_db_URL(result.getString(1)); 
				    geneModel.setIuphar_guide_URL(result.getString(2)); 
				    break;
				case 1:
					geneModel.setIuphar_guide_URL(result.getString(2)); 
				break;
				case 0:
				default:
					geneModel.setIuphar_db_URL(result.getString(1)); 
				    break;
				}
            }
									
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			geneModel.setIuphar_db_URL(null); 
			geneModel.setIuphar_guide_URL(null);
		}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return geneModel;		
       
    }
    

}
