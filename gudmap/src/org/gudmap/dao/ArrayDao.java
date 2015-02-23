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

public class ArrayDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private GeneModel geneModel;
	
	public ArrayDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public GeneModel findGeneInfoBySymbol(ArrayList<String> genes) {
		
		if (genes == null || genes.size() == 0) {
		    return null;
		}
        String queryString = ArrayQueries.GET_GENE_BY_SYMBOL;
        int len = genes.size();
        String symbolCriteria = "";
        String appendString = " OR GNF_SYMBOL = ";
        for (int i=0;i<len;i++) {
		    symbolCriteria += "'" + genes.get(i) + "'" +  appendString;
        }
        symbolCriteria = symbolCriteria.substring(0, (symbolCriteria.length()-appendString.length()));
        queryString += symbolCriteria;
        
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel = new GeneModel();
                geneModel.setSymbol(result.getString(1));  
                geneModel.setName(result.getString(2));
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return geneModel;
    }

}
