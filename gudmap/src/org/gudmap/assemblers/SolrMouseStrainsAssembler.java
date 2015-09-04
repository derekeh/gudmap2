package org.gudmap.assemblers;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.models.MouseStrainsTableBeanModel;


public class SolrMouseStrainsAssembler {

	private MouseStrainsTableBeanModel model;
	private SolrUtil solrUtil;

	public SolrMouseStrainsAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, String solrFilter) {

		int n = solrUtil.getMouseStrainsCount(solrInput);

		return n;
	}

	public List<MouseStrainsTableBeanModel> getData(String solrInput, String filter, List<String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<MouseStrainsTableBeanModel> list = new ArrayList<MouseStrainsTableBeanModel>();

		SolrDocumentList sdl = solrUtil.getMouseStrainsData(solrInput,sortColumn,ascending,offset,num);
		list = formatTableData(sdl);

		return list;
	}

	private List<MouseStrainsTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<MouseStrainsTableBeanModel> list = new ArrayList<MouseStrainsTableBeanModel>();
		
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			model = new MouseStrainsTableBeanModel();
			model.setOid(doc.getFieldValue("ID").toString());
			model.setGene(doc.getFieldValue("GENE").toString());
			model.setReporterAllele(doc.getFieldValue("REPORTER_ALLELE").toString());
			model.setAlleleType(doc.getFieldValue("ALLELE_TYPE").toString());
			model.setAlleleVerification(doc.getFieldValue("ALLELE_VER").toString());
			model.setAlleleCharacterisation(doc.getFieldValue("ALLELE_CHAR").toString());
			model.setStrainAvailability(doc.getFieldValue("STRAIN_AVA").toString());
			model.setOrgan(doc.getFieldValue("ORGAN").toString());
			model.setCellType(doc.getFieldValue("CELL_TYPE").toString());
			
			list.add(model);			
		}
		
		return list;
	}	
	
}
