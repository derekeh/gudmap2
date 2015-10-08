package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.assemblers.SolrIndexAssembler;
import org.gudmap.utils.SolrUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;



@Named (value="solrIndexBean")
@RequestScoped
public class SolrIndexBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SolrIndexAssembler assembler;
	private SolrUtil solrUtil;

	
	public SolrIndexBean(){
		solrUtil = new SolrUtil();
		assembler = new SolrIndexAssembler();
	}
	
	public String update(){
		
		HttpSolrClient server = solrUtil.getTissuesServer();
		assembler.getTissuesData(server);
		
		
//		HttpSolrClient server = solrUtil.getInsituServer();
//		assembler.getInsituData(server);
		
		return null;
		
	}
	
}
