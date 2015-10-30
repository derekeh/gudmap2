package org.gudmap.assemblers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.models.ArraySeqTableBeanModel;

public class SolrSequencesAssembler {

	private ArraySeqTableBeanModel model;
	private SolrUtil solrUtil;

	public SolrSequencesAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, String solrFilter) {

		int n = solrUtil.getSamplesCount(solrInput);

		return n;
	}

	public List<ArraySeqTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();

		SolrDocumentList sdl;
		try {
			
			sdl = solrUtil.getSequencesData(solrInput,filterlist,sortColumn,ascending,offset,num);
			list = formatTableData(sdl);
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	private List<ArraySeqTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		
		int rowNum = sdl.size();
		Set<String> gudmapset = new HashSet<String>();
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			model = new ArraySeqTableBeanModel();
			model.setOid(doc.getFieldValue("GUDMAP").toString());
			model.setGudmap_accession("GUDMAP:" + doc.getFieldValue("GUDMAP").toString());
			String gid = doc.getFieldValue("SAMPLE_GEO_ID").toString();
			model.setGeoSampleID(doc.getFieldValue("SAMPLE_GEO_ID").toString());
			String stage = doc.getFieldValue("STAGE").toString();
			model.setStage(stage);
			model.setStage_order(stage.substring(2));
//arraySeqmodel.setSpecies(result.getString("species"));
			model.setAge(doc.getFieldValue("AGE").toString());
			model.setSource(doc.getFieldValue("PI_NAME").toString());
			model.setLibraryStrategy(doc.getFieldValue("LIBRARY_STRATEGY").toString());
			model.setSubmission_date(doc.getFieldValue("DATE").toString());
			model.setSex(doc.getFieldValue("SEX").toString());
			model.setSampleDescription(doc.getFieldValue("SAMPLE_DESCRIPTION").toString());
			model.setTitle(doc.getFieldValue("SAMPLE_NAME").toString());
			model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			model.setSampleComponents(doc.getFieldValue("COMPONENT").toString());
			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());				
//arraySeqmodel.setAssay_type(result.getString("assay_type"));
			model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			
			list.add(model);	
			
			gudmapset.add(doc.getFieldValue("GUDMAP").toString());

		}
		
		return list;
	}	

	
}
