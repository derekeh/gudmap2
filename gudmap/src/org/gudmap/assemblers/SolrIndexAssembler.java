package org.gudmap.assemblers;




import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.dao.SolrDao;


public class SolrIndexAssembler {

	private SolrDao solrDao;
	
	public SolrIndexAssembler() {
		solrDao = new SolrDao();

	}
	

	public void getInsituData(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrInsituData();
		
		try {
			UpdateResponse resp = server.add(docs);
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        docs.clear();

		
	}

	public void getTissuesData(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrTissueData();
		
		try {
			UpdateResponse resp = server.add(docs);
			
			server.commit();
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        docs.clear();

		
	}
	
}
