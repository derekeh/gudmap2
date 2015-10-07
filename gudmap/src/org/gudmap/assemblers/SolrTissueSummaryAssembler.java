package org.gudmap.assemblers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.models.TissueSummaryTableBeanModel;

public class SolrTissueSummaryAssembler {

	private TissueSummaryTableBeanModel model;
	private SolrUtil solrUtil;

	public SolrTissueSummaryAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, String solrFilter) {

		int n = solrUtil.getTissueCount(solrInput, null);

		return n;
	}

	public List<TissueSummaryTableBeanModel> getData(String solrInput, HashMap<String, String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<TissueSummaryTableBeanModel> list = new ArrayList<TissueSummaryTableBeanModel>();

    	QueryResponse qr  = solrUtil.getTissueData(solrInput, filterlist, sortColumn,ascending,offset,num);
		list = formatTableData(qr);

		return list;
	}

	private List<TissueSummaryTableBeanModel> formatTableData(QueryResponse qr){
		
		List<TissueSummaryTableBeanModel> list = new ArrayList<TissueSummaryTableBeanModel>();
		
		SolrDocumentList sdl = qr.getResults();
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			model = new TissueSummaryTableBeanModel();
			String id = doc.getFieldValue("ID").toString();
	    	String name = doc.getFieldValue("NAME").toString();
	    	String stages = doc.getFieldValue("STAGES").toString();

	        Map<String,Map<String,List<String>>> highlightMap = qr.getHighlighting();			
	    	String reason = "";	    	
	    	if (highlightMap != null){
		        Set<String> keys = highlightMap.keySet();
		    	if (keys.contains(id)){            
		            Set<String> fields = highlightMap.get(id).keySet();
		            for(String field :fields){
		                List<String> result = highlightMap.get(id).get(field);
		            	for(String item : result){
		            		if (item.contains("<em>") && item.contains("</em>")){
		            			int start = item.indexOf(">");
		            			item = item.substring(start);
		            			int end = item.indexOf("<");
		            			item = item.substring(1,end);
		                    	System.out.println("found = "+ item);
		                    	if (reason.isEmpty())
		                    		reason += item + " in document " + field + " field";
		                    	else
		                    		reason += " & " + item + " in document " + field + " field";
		            		}
		            	}            	
		             }	    	
		    	}
	    	}
	    	model.setOid(id);
			model.setName(name);
			model.setStages(stages);
			model.setReason(reason);
			
			list.add(model);			
		}
		
		return list;
	}	
	
}
