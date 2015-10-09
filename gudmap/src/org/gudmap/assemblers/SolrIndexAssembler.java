package org.gudmap.assemblers;




import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.dao.SolrDao;


public class SolrIndexAssembler {

	private SolrDao solrDao;
	
	public SolrIndexAssembler() {
		solrDao = new SolrDao();

	}
	
	public void updateGenesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrGeneIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateGenelistsIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrGenelistsIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateInsituIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrInsituIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateMicroarrayIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrMicroarrayIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateSamplesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrSamplesIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateSeriesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrSeriesIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateTissueIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrTissueIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateMouseStrainsIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrMouseStrainsIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateImageIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrImageIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
}
