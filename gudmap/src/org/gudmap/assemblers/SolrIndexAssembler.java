package org.gudmap.assemblers;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.SolrInputDocument;
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
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
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
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
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
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

//	public void updateMicroarrayIndex(HttpSolrClient server){
//
//		ArrayList<SolrInputDocument> docs  = solrDao.getSolrMicroarrayIndexData();
//		
//		try {
//			// clear index
//			server.deleteByQuery("*:*");
//			server.commit();			
//			
//			server.add(docs);			
//			server.commit();
//		} catch (SolrServerException | IOException e) {
//			e.printStackTrace();
//		}
//        docs.clear();
//	}
	
	public void updateSamplesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrSamplesIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

//	public void updateSeriesIndex(HttpSolrClient server){
//
//		ArrayList<SolrInputDocument> docs  = solrDao.getSolrSeriesIndexData();
//		
//		try {
//			// clear index
//			server.deleteByQuery("*:*");
//			server.commit();			
//			
//			server.add(docs);			
//			server.commit();
//		} catch (SolrServerException | IOException e) {
//			e.printStackTrace();
//		}
//        docs.clear();
//	}
	
	public void updateTissueIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrTissueIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
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
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
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
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

//	public void updateNextGenSeriesIndex(HttpSolrClient server){
//
//		ArrayList<SolrInputDocument> docs  = solrDao.getSolrNextGenSeriesIndexData();
//		
//		try {
//			// clear index
//			server.deleteByQuery("*:*");
//			server.commit();			
//			
//			server.add(docs);			
//			server.commit();
//		} catch (SolrServerException | IOException e) {
//			e.printStackTrace();
//		}
//        docs.clear();
//	}
	
	public void updateNextGenSamplesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrNextGenSamplesIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateTutorialIndex(HttpSolrClient server){
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			updateTutorialOverview(server);
			updateTutorialDevMRS(server);
			updateTutorialDevMUS(server);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTutorialOverview(HttpSolrClient server){

		
		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
		String solrId = "Overview";
		
		ContentStreamUpdateRequest up  = new ContentStreamUpdateRequest("/update/extract");	
		try {
			up.addFile(new File(fileName),"html");
			up.setParam("literal.id", solrId);
			up.setParam("uprefix", "attr_");
			up.setParam("fmap.content", "attr_content");
			up.setParam(ExtractingParams.EXTRACT_ONLY, "true");
			server.request(up);
			server.commit();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}
	public void updateTutorialDevMRS(HttpSolrClient server){

		
		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
		String solrId = "DevMRS";
		
		ContentStreamUpdateRequest up  = new ContentStreamUpdateRequest("/update/extract");	
		try {
			up.addFile(new File(fileName),"html");
			up.setParam("literal.id", solrId);
			up.setParam("uprefix", "attr_");
			up.setParam("fmap.content", "attr_content");
			up.setParam(ExtractingParams.EXTRACT_ONLY, "true");
			server.request(up);
			server.commit();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}
	public void updateTutorialDevMUS(HttpSolrClient server){

		
		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
		String solrId = "DevMUS";
		
		ContentStreamUpdateRequest up  = new ContentStreamUpdateRequest("/update/extract");	
		try {
			up.addFile(new File(fileName),"html");
			up.setParam("literal.id", solrId);
			up.setParam("uprefix", "attr_");
			up.setParam("fmap.content", "attr_content");
			up.setParam(ExtractingParams.EXTRACT_ONLY, "true");
			server.request(up);
			server.commit();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

	}

	public void updateWebIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrWebIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
	public void updateEurExpressIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getSolrEurExpressIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

//	public void updateDemoIndex(){
//
//		HttpSolrClient server = new HttpSolrClient("http://localhost:8983/solr/core_demo");
//		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
//		
//		String fileName= "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/server/solr/cores/core_tutorials/docs/DevMRS.html"; 
//		String solrId = "DevMRS";
//			
//		try {
//			
//			req.addFile(new File(fileName),"html");
//			req.setParam(ExtractingParams.EXTRACT_ONLY, "true");
//			NamedList<Object> result = server.request(req);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		}
//	}
	
	public ArrayList<String> getGeneList() {		
		return solrDao.getGeneList();		
	}

	public void updateBiomedAtlasImageIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getBiomedAtlasImageIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateBiomedAtlasInsituIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getBiomedAtlasInsituIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}

	public void updateBiomedAtlasGenesIndex(HttpSolrClient server){

		ArrayList<SolrInputDocument> docs  = solrDao.getBiomedAtlasGenesIndexData();
		
		try {
			// clear index
			server.deleteByQuery("*:*");
			server.commit();			
			
			server.add(docs);			
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
        docs.clear();
	}
	
}
