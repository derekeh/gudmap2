package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.ArraySeqTableBeanModel;

/**
 * <h1>SolrMicroarrayBean</h1>
 * The SolrMicroarrayBean class contains the methods to provide data and deal with events on the
 * solrMicroarray.xhtml web page
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
@Named (value="solrMicroarrayBean")
@SessionScoped
public class SolrMicroarrayBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
    private String whereclause = " WHERE ";
    private List<String> selectedItems;
    private boolean areAllChecked;
    
    @Inject
   	private ParamBean paramBean;
   
    @Inject
   	private SolrTreeBean solrTreeBean;
    
    @Inject
   	private SolrFilter solrFilter;
    
	private String solrInput;
	private HashMap<String,String> filters;
	private HashMap<String,String> totals;
	private boolean showPageDetails = true;

	
    
    // Constructors -------------------------------------------------------------------------------

    public SolrMicroarrayBean() {
    	super(20,10,"RELEVANCE",true);
    	setup();
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

	public void setup() {
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
        totalRows = solrTreeBean.getSolrUtil().getSamplesFilteredCount(solrInput, filters);
    	
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
    	return "solrMicroarray";
    }

    public void resetAll() {
		paramBean.resetAll();
		loadDataList();
    }
   
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((ArraySeqTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((ArraySeqTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((ArraySeqTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }

    public String getTitle(){
    	String str="Microarray Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getMicroarrayCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getMicroarrayCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getMicroarrayCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getMicroarrayCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }

    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a list of ArraySeqTableBeanModels containing the retrieved documents.
     * 
	 * @param solrInput The main query string for retrieving relevant documents'
     * @param filterlist A list of filters to be applied to the solr search
     * @param sortColumn The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param num The number of documents to be retrieved in the result set.
     * @return A list of ArraySeqTableBeanModels
	 * @see ArraySeqTableBeanModel
     */
	public List<ArraySeqTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();

		List<String> ids = solrTreeBean.getSolrUtil().getSamplesData(solrInput,filterlist,sortColumn,ascending,offset,num);
		SolrDocumentList sdl = solrTreeBean.getSolrUtil().getSamplesViewData(ids,sortColumn,ascending,offset,num);
		list = formatTableData(sdl);
		
		
		totals = (HashMap<String, String>) solrTreeBean.getSolrUtil().getSamplesDataCount(solrInput, filterlist);
		

		return list;
	}

	/**
	 * This method creates a list of ArraySeqTableBeanModels from the documents in the SolrDocumentList sdl.
	 * 
	 * @param sdl A SolrDocumentList	  
	 * @return A List of ArraySeqTableBeanModels
	 * @see ArraySeqTableBeanModel
	 */
	private List<ArraySeqTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		ArraySeqTableBeanModel model = null;
		
		int rowNum = sdl.size();
		Set<String> gudmapset = new HashSet<String>();
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			// remove duplicate gudmapid entries
			if (gudmapset.contains(doc.getFieldValue("GUDMAP").toString())){
				
			}
			else{
				model = new ArraySeqTableBeanModel();
				if (doc.containsKey("GUDMAP"))
					model.setOid(doc.getFieldValue("GUDMAP").toString());
				if (doc.containsKey("GUDMAP_ID"))
					model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
				if (doc.containsKey("SAMPLE_GEO_ID"))
					model.setGeoSampleID(doc.getFieldValue("SAMPLE_GEO_ID").toString());
				if (doc.containsKey("STAGE")){
					String stage = doc.getFieldValue("STAGE").toString();
					model.setStage(stage);
					model.setStage_order(stage.replace("TS", ""));
				}
				if (doc.containsKey("DEV_STAGE"))
					model.setAge(doc.getFieldValue("DEV_STAGE").toString());
				if (doc.containsKey("SOURCE"))
					model.setSource(doc.getFieldValue("SOURCE").toString());
				if (doc.containsKey("DATE"))
					model.setSubmission_date(doc.getFieldValue("DATE").toString());
				if (doc.containsKey("SEX"))
					model.setSex(doc.getFieldValue("SEX").toString());
				if (doc.containsKey("DESCRIPTION"))
					model.setSampleDescription(doc.getFieldValue("DESCRIPTION").toString());
				if (doc.containsKey("TITLE"))
					model.setTitle(doc.getFieldValue("TITLE").toString());
				if (doc.containsKey("SERIES_GEO_ID"))
					model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
				if (doc.containsKey("COMPONENT"))
					model.setSampleComponents(doc.getFieldValue("COMPONENT").toString());
				if (doc.containsKey("SPECIES"))
					model.setSpecies(doc.getFieldValue("SPECIES").toString());
				if (doc.containsKey("GENOTYPE"))
					model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
				if (doc.containsKey("PLATFORM_GEO_ID"))
					model.setPlatformID(doc.getFieldValue("PLATFORM_GEO_ID").toString());
				
				list.add(model);	
				
				gudmapset.add(doc.getFieldValue("GUDMAP").toString());
			}
		}
		
		return list;
	}	

	public HashMap<String,String> getTotals(){		
		return totals;
	}
     
}
