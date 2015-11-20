package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
//import org.gudmap.assemblers.SolrTissueSummaryAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.TissueSummaryTableBeanModel;


@Named (value="solrTissueSummaryBean")
@SessionScoped
public class SolrTissueSummaryBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
//	private SolrTissueSummaryAssembler assembler;
    private String whereclause = " WHERE ";
    
    @Inject
   	private ParamBean paramBean;
    
    @Inject
   	private SolrTreeBean solrTreeBean;

    @Inject
   	private SolrFilter solrFilter;
    
	private String solrInput;
	private HashMap<String,String> filters;
	private boolean showPageDetails = true;
   
	    
    // Constructors -------------------------------------------------------------------------------

    public SolrTissueSummaryBean() {
    	super(20,10,"RELEVANCE",true);
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
 
	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
	public void setSolrFilter(SolrFilter solrFilter){
		this.solrFilter = solrFilter;
	}
	
	public void setSolrInput(String solrInput){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
	}
	
	public String getSolrInput(){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
		return solrInput;
	}

    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
//        totalRows = assembler.getCount(solrInput, filters);
        totalRows = solrTreeBean.getSolrUtil().getTissueCount(solrInput,filters);
   	
//     	dataList = assembler.getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
     	dataList = getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
        // Set currentPage, totalPages and pages.
        currentPage = (totalRows / rowsPerPage) - ((totalRows - firstRow) / rowsPerPage) + 1;
        totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRange, totalPages);
        pages = new Integer[pagesLength];

        // firstPage must be greater than 0 and lesser than totalPages-pageLength.
        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);

        // Create pages (page numbers for page links).
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
        
        if (dataList.size() > rowsPerPage)
        	showPageDetails = true;
        else
        	showPageDetails = false;
    }

    public String refresh(){
 //   	sortField = "RELEVANCE";
    	loadDataList();
    	paramBean.resetValues();
    	return "solrTissueSummary";
    }
    
    public String getTitle(){
    	String str="Tissue Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getTissueCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getTissueCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getTissueCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getTissueCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }

    public boolean getShowPageDetails(){
    	return showPageDetails;
    }

	public List<TissueSummaryTableBeanModel> getData(String solrInput, HashMap<String, String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<TissueSummaryTableBeanModel> list = new ArrayList<TissueSummaryTableBeanModel>();

    	QueryResponse qr  = solrTreeBean.getSolrUtil().getTissueData(solrInput, filterlist, sortColumn,ascending,offset,num);
		list = formatTableData(qr);

		return list;
	}

	private List<TissueSummaryTableBeanModel> formatTableData(QueryResponse qr){
		
		List<TissueSummaryTableBeanModel> list = new ArrayList<TissueSummaryTableBeanModel>();
		TissueSummaryTableBeanModel model = null;
		
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
