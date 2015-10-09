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
	
	public String indexAll(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
//		assembler.getGenesData(server);

		server = solrUtil.getGenelistsServer();
//		assembler.getGenelistsData(server);
		
		server = solrUtil.getInsituServer();
//		assembler.getInsituData(server);

		server = solrUtil.getMicroarrayServer();
//		assembler.getMicroarrayData(server);

		server = solrUtil.getSamplesServer();
//		assembler.getSamplesData(server);

		server = solrUtil.getSeriesServer();
//		assembler.getSeriesData(server);
		
		server = solrUtil.getTissuesServer();
//		assembler.getTissueData(server);

		server = solrUtil.getMouseStrainServer();
		assembler.getMouseStrainsData(server);
		
		server = solrUtil.getImageServer();
//		assembler.getImageData(server);
		
		return null;
		
	}

	public String indexGenes(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.getGenesData(server);
		return null;		
	}

	public String indexInsitu(){
		
		HttpSolrClient server = solrUtil.getInsituServer();
		assembler.getInsituData(server);
		return null;		
	}

	public String indexMicroarray(){
		
		HttpSolrClient server = solrUtil.getMicroarrayServer();
		assembler.getMicroarrayData(server);
		return null;		
	}

	public String indexGenelists(){
		
		HttpSolrClient server = solrUtil.getGenelistsServer();
//		assembler.getGenelistsData(server);
		return null;		
	}

	public String indexTissues(){
		
		HttpSolrClient server = solrUtil.getTissuesServer();
		assembler.getTissueData(server);
		return null;		
	}

	public String indexMouseStrains(){
		
		HttpSolrClient server = solrUtil.getMouseStrainServer();
		assembler.getMouseStrainsData(server);
		return null;		
	}
	
	public String indexImages(){
		
		HttpSolrClient server = solrUtil.getImageServer();
		assembler.getImageData(server);
		return null;		
	}
	
	
}
