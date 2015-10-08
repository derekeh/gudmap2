package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.globals.Globals;
import org.gudmap.queries.solr.SolrQueries;

public class SolrDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private SolrInputDocument doc;
	 private ArrayList<SolrInputDocument> docs;
	public SolrDao() {
	}
	
	public ArrayList<SolrInputDocument> getSolrInsituData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
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
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	
	public ArrayList<SolrInputDocument> getSolrTissueData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_TISSUE_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("NAME", result.getString(2)); 
				tmp = result.getString(3);
				doc.addField("SYNONYM", result.getString(3)); 
				doc.addField("STAGES", result.getString(4)); 
				doc.addField("FOCUS_GROUPS", result.getString(5)); 
				doc.addField("GENELIST_IDS", result.getString(6)); 				
				doc.addField("GENELIST_NAMES", result.getString(7)); 				
				doc.addField("IMAGE_URL", result.getString(8)); 				
				doc.addField("IMAGE_NAME", result.getString(9)); 				
				doc.addField("SEC_NAMES", result.getString(10)); 				
				doc.addField("LINK_TYPES", result.getString(11)); 				
				doc.addField("GUDMAP", result.getString(12)); 				
				doc.addField("EMAP", result.getString(13)); 				
				doc.addField("EMAPIDS", result.getString(14)); 				
				doc.addField("COMPONENT", result.getString(15)); 				
				doc.addField("MGI", result.getString(16)); 				
				doc.addField("GENE_MGI_ID", result.getString(17)); 				
				doc.addField("GENE", result.getString(18)); 				
				doc.addField("PROBE_IDS", result.getString(19)); 				
				doc.addField("MA_PROBES_ID", result.getString(20)); 				
				doc.addField("maprobe", result.getString(21)); 				
				doc.addField("THEILER_STAGE", result.getString(22)); 				
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
}
