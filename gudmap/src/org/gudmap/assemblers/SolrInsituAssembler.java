package org.gudmap.assemblers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.models.InsituTableBeanModel;

public class SolrInsituAssembler {

	private InsituTableBeanModel model;
	private SolrUtil solrUtil;

	public SolrInsituAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, String solrFilter) {

		int n = 0;
		try {

	    	n = solrUtil.getInsituCount(solrInput,solrFilter,null);
		} 
		catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		return n;
	}

	public List<InsituTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
					
    	try {
    		
			SolrDocumentList sdl  = solrUtil.getInsituData(solrInput, filterlist, sortColumn,ascending,offset,num);
			if (sdl==null){
				return null;
			}
			list = formatTableData(sdl);
			
    	} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	private List<InsituTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			String insituExpression = "";			
			if (doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new InsituTableBeanModel();
			model.setOid(doc.getFieldValue("GUDMAP").toString());
			model.setGene(doc.getFieldValue("GENE").toString());
			model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
			model.setSource(doc.getFieldValue("SOURCE").toString());
			model.setSubmission_date(doc.getFieldValue("DATE").toString());
			model.setAssay_type(doc.getFieldValue("ASSAY_TYPE").toString());
			model.setProbe_name(doc.getFieldValue("PROBE_NAME").toString());
			model.setStage(doc.getFieldValue("STAGE").toString());
			model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			model.setSex(doc.getFieldValue("SEX").toString());
			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
			model.setTissue(doc.getFieldValue("TISSUE_TYPE").toString());
			model.setExpression(insituExpression);
			model.setSpecimen(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
			model.setImage(doc.getFieldValue("IMAGE_PATH").toString());
			model.setGene_id(doc.getFieldValue("MGI_GENE_ID").toString());
			
			list.add(model);			
		}
			
		return list;
	 }	

}
