package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.assemblers.SolrIndexAssembler;
import org.gudmap.utils.SolrUtil;

import java.io.Serializable;



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
	
	public void update(){
		assembler.getInsituData();
	}
	
}
