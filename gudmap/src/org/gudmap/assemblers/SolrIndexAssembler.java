package org.gudmap.assemblers;




import org.apache.solr.common.SolrDocumentList;

import org.gudmap.dao.SolrDao;


public class SolrIndexAssembler {

	private SolrDao solrDao;
	
	public SolrIndexAssembler() {
		solrDao = new SolrDao();

	}
	

	public void getInsituData(){

					
		SolrDocumentList sdl  = solrDao.getSolrInsituData();


	}



}
