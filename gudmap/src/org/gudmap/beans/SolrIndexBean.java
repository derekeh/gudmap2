package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.extraction.ExtractingParams;
import org.gudmap.assemblers.SolrIndexAssembler;
import org.gudmap.utils.SolrUtil;

import java.io.File;
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

	// methods to update the solr indexes used for searching
	public String indexAll(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);

		server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		
		server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);

//		server = solrUtil.getMicroarrayServer();
//		assembler.updateMicroarrayIndex(server);

		server = solrUtil.getSamplesServer();
		assembler.updateSamplesIndex(server);

		server = solrUtil.getSeriesServer();
		assembler.updateSeriesIndex(server);
		
		server = solrUtil.getTissuesServer();
		assembler.updateTissueIndex(server);

		server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		
		server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);
		
		return null;
		
	}

	public String indexGenes(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);
		return null;		
	}

	public String indexInsitu(){
		
		HttpSolrClient server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);
		return null;		
	}

	public String indexMicroarray(){
		
//		HttpSolrClient server = solrUtil.getMicroarrayServer();
//		assembler.updateMicroarrayIndex(server);
		
//		server = solrUtil.getSamplesServer();
//		assembler.updateSamplesIndex(server);
//
//		server = solrUtil.getSeriesServer();
//		assembler.updateSeriesIndex(server);
		
		return null;		
	}

	public String indexGenelists(){
		
		HttpSolrClient server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		return null;		
	}

	public String indexTissues(){
		
		HttpSolrClient server = solrUtil.getTissuesServer();
		assembler.updateGenelistsIndex(server);
		return null;		
	}

	public String indexMouseStrains(){
		
		HttpSolrClient server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		return null;		
	}
	
	public String indexImages(){
		
		HttpSolrClient server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);
		return null;		
	}

	public String indexNextGenSeries(){
		
		HttpSolrClient server = solrUtil.getNextGenSeriesServer();
		assembler.updateNextGenSeriesIndex(server);
		return null;		
	}
	
	public String indexNextGenSamples(){
		
		HttpSolrClient server = solrUtil.getNextGenSamplesServer();
		assembler.updateNextGenSamplesIndex(server);
		return null;		
	}
		
	public String indexTutorials(){
		
		HttpSolrClient server = solrUtil.getTutorialServer();
		assembler.updateTutorialIndex(server);
		return null;		
	}
		
}
