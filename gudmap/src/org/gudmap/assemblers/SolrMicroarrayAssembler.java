package org.gudmap.assemblers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.models.ArraySeqTableBeanModel;

public class SolrMicroarrayAssembler {

	private ArraySeqTableBeanModel model;
	private SolrUtil solrUtil;

	public SolrMicroarrayAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, HashMap<String,String> filterlist) {

//		int n = solrUtil.getSamplesCount(solrInput);
		int n = solrUtil.getMicroarrayFilteredCount(solrInput,filterlist);

		return n;
	}

	public List<ArraySeqTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();

		List<String> ids = solrUtil.getMicroarrayData(solrInput,filterlist,sortColumn,ascending,offset,num);
		SolrDocumentList sdl = solrUtil.getMicroarrayViewData(ids,sortColumn,ascending,offset,num);
		list = formatTableData(sdl);

		return list;
	}

	private List<ArraySeqTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		
		int rowNum = sdl.size();
		Set<String> gudmapset = new HashSet<String>();
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			// remove duplicate gudmapid entries
			if (gudmapset.contains(doc.getFieldValue("GUDMAP").toString())){
				
			}
			else{
//				arraySeqmodel=new ArraySeqTableBeanModel();
//				arraySeqmodel.setOid(result.getString("oid"));
//				arraySeqmodel.setGudmap_accession(result.getString("gudmap_accession"));
//				arraySeqmodel.setGeoSampleID(result.getString("geo_sample_id"));
//				arraySeqmodel.setStage(result.getString("stage"));
//				arraySeqmodel.setStage_order(result.getString("stage").substring(2));
//				arraySeqmodel.setSpecies(result.getString("species"));
//				arraySeqmodel.setAge(result.getString("age"));
//				arraySeqmodel.setSource(result.getString("source"));
//				arraySeqmodel.setSubmission_date(result.getString("submission_date"));
//				arraySeqmodel.setSex(result.getString("sex"));
//				arraySeqmodel.setSampleDescription(result.getString("sample_description"));
//				arraySeqmodel.setTitle(result.getString("sample_title"));
//				arraySeqmodel.setGeoSeriesID(result.getString("geo_series_id"));
//				arraySeqmodel.setSampleComponents(result.getString("components"));
//				arraySeqmodel.setGenotype(result.getString("genotype"));
//				arraySeqmodel.setAssay_type(result.getString("assay_type"));
//				arraySeqmodel.setSpecimen_assay_type(result.getString("specimen_assay_type"));
//				arraySeqmodel.setPersonOid(result.getInt("person_oid"));
//				arraySeqmodel.setSeriesOid(result.getInt("series_oid"));	

				model = new ArraySeqTableBeanModel();
				model.setOid(doc.getFieldValue("GUDMAP").toString());
				model.setGudmap_accession("GUDMAP:" + doc.getFieldValue("GUDMAP").toString());
				model.setGeoSampleID(doc.getFieldValue("SAMPLE_GEO_ID").toString());
				model.setStage(doc.getFieldValue("STAGE").toString());
				model.setAge(doc.getFieldValue("DEV_STAGE").toString());
				model.setSource(doc.getFieldValue("PI_NAME").toString());
				model.setSubmission_date(doc.getFieldValue("DATE").toString());
				model.setSex(doc.getFieldValue("SEX").toString());
				model.setSampleDescription(doc.getFieldValue("DESCRIPTION").toString());
				model.setTitle(doc.getFieldValue("TITLE").toString());
//				model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
				model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
				model.setSampleComponents(doc.getFieldValue("COMPONENT").toString());
				
				list.add(model);	
				
				gudmapset.add(doc.getFieldValue("GUDMAP").toString());
			}
		}
		
		return list;
	}	

	
}
