package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.globals.Globals;
import org.gudmap.queries.solr.SolrQueries;

public class SolrDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private SolrDocumentList docs;
	private SolrInputDocument doc;
	
	public SolrDao() {
	}
	
	public SolrDocumentList getSolrInsituData() {
		
		docs = new SolrDocumentList();
		
        String queryString = SolrQueries.GET_INSITU_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				
//				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
}
