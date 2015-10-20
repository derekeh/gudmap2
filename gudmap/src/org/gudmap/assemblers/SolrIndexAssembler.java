package org.gudmap.assemblers;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.extraction.ExtractingParams;
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

	public void updateNextGenSeriesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrNextGenSeriesIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateNextGenSamplesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrNextGenSamplesIndexData();
		
		try {
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateTutorialIndex(HttpSolrClient server){

		
		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
		String solrId = "DevMRS";
		
		ContentStreamUpdateRequest up  = new ContentStreamUpdateRequest("/update/extract");	
		try {
			server.deleteByQuery( "*:*" ); 
			
			up.addFile(new File(fileName),"html");
			up.setParam("literal.id", solrId);
			up.setParam("uprefix", "attr_");
			up.setParam("fmap.content", "attr_content");
			up.setParam(ExtractingParams.EXTRACT_ONLY, "true");
//			up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
			server.request(up);
			server.commit();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public void updateDemoIndex(){

		HttpSolrClient server = new HttpSolrClient("http://localhost:8983/solr/core_demo");
		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
		
		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
		String solrId = "DevMRS";
			
		try {
			
			req.addFile(new File(fileName),"html");
			req.setParam(ExtractingParams.EXTRACT_ONLY, "true");
			NamedList<Object> result = server.request(req);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
