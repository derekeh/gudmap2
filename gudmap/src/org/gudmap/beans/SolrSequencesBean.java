package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
//import org.gudmap.assemblers.SolrSequencesAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.ArraySeqTableBeanModel;

/**
 * <h1>SolrSequencesBean</h1>
 * The SolrSequencesBean class contains the methods to provide data and deal with events on the
 * solrSequences.xhtml web page
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
@Named (value="solrSequencesBean")
@SessionScoped
public class SolrSequencesBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
    private String whereclause = " WHERE ";
    private List<String> selectedItems;
    private String selectedSamples;
    private boolean areAllChecked;
    private boolean toggleCheck=false;
    
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

    public SolrSequencesBean() {
    	super(20,10,"RELEVANCE",true);
    	setup();
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
	public void setSolrFilter(SolrFilter solrFilter){
		this.solrFilter = solrFilter;
	}
 
	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
	public void setSolrInput(String solrInput){
//		solrInput = solrTreeBean.getSolrInput();
//		refresh();

		if (solrInput != solrTreeBean.getSolrInput()){
			solrInput = solrTreeBean.getSolrInput();
			refresh();
		}
	
	}
	
	public String getSolrInput(){
		if (solrInput != solrTreeBean.getSolrInput()){
			solrInput = solrTreeBean.getSolrInput();
			refresh();
		}
		return solrInput;
	}

	public void setup() {
        selectedItems = new ArrayList<String>(); 
    }
	
	public String getSelectedSamples(){
		return selectedSamples;
	}
	
	public void setSelectedSamples(String selectedSamples){
		this.selectedSamples = selectedSamples;
	}
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
        totalRows = solrTreeBean.getSolrUtil().getSequencesCount(solrInput, filters);
    	
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
     	loadDataList();
    	return "solrSequences";
    }
//    public void refresh(){
//     	loadDataList();
//    }

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
    	//return "browseCollectionEntriesTablePage";
    	//return "result";
    	return "";
    	//return collectionSaveOption();
    }
 
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((ArraySeqTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }

    public void toggleAll(ValueChangeEvent e) { 
    	boolean temp = (Boolean) e.getNewValue();
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((ArraySeqTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }

//    public void selectCheck(ValueChangeEvent e) {
//    	boolean temp = (Boolean) e.getNewValue();
//    }
    
    
    public void setToggleCheck(boolean toggleCheck){
    	this.toggleCheck = toggleCheck;
    }
    
    public boolean getToggleCheck() {
    	return toggleCheck;
    }
    
    public List<String> getSelectedItems() {
    	return selectedItems;
    }
    
    public void addToCollection(ActionEvent e) {
    	checkboxSelections();
    }

    public String removeFromCollection() {
    	return "";
    }

    public String clearCollection() {
    	for (int i=0;i<dataList.size();i++)
    		((ArraySeqTableBeanModel)dataList.get(i)).setSelected(false);
    	selectedItems.clear();
    	return "";
    }

    public String viewCollection() {
    	selectedSamples = "";
    	if(selectedItems != null) {
	    	for(String s : selectedItems){
	    		selectedSamples += s + ", ";
	    	}
    	}
    	return selectedSamples;
    }
   
//    public String getTitle(){
//    	String str="Sequences Search Results ";
//    	filters = solrFilter.getFilters();
//    	if (filters == null){
//	    	if (solrInput != null && solrInput != "")
//	    		str += "(" + solrTreeBean.getSequencesCount() + ") > " + solrInput;
//	    	else
//	    		str += "(" + solrTreeBean.getSequencesCount() + ") > ALL";
//    	}
//    	else{
//        	if (solrInput != null && solrInput != "")
//        		str += "(" + solrTreeBean.getSequencesCount(filters) + ") > " + solrInput;
//        	else
//        		str += "(" + solrTreeBean.getSequencesCount(filters) + ") > ALL";
//    		
//    	}
//    	return str;
//    }
    
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

		SolrDocumentList sdl;
		try {
			
			sdl = solrTreeBean.getSolrUtil().getSequencesData(solrInput,filterlist,sortColumn,ascending,offset,num);
			list = formatTableData(sdl);
			
			totals = (HashMap<String, String>) solrTreeBean.getSolrUtil().getSequencesDataCount(solrInput, filterlist);			
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * This method creates a list of ArraySeqTableBeanModels from the documents in the SolrDocumentList sdl.
	 * 
	 * @param sdl A SolrDocumentList	  
	 * @return A List of ArraySeqTableBeanModels
	 * @see ArraySeqTableBeanModel
	 */
	private List< ArraySeqTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		ArraySeqTableBeanModel model = null;
		
		int rowNum = sdl.size();
		Set<String> gudmapset = new HashSet<String>();
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			model = new ArraySeqTableBeanModel();
			if (doc.containsKey("GUDMAP")){
				model.setOid(doc.getFieldValue("GUDMAP").toString());
				model.setGudmap_accession("GUDMAP:" + doc.getFieldValue("GUDMAP").toString());
			}
			if (doc.containsKey("SAMPLE_GEO_ID")){
//				String gid = doc.getFieldValue("SAMPLE_GEO_ID").toString();
				model.setGeoSampleID(doc.getFieldValue("SAMPLE_GEO_ID").toString());
			}
			if (doc.containsKey("STAGE")){
				String stage = doc.getFieldValue("STAGE").toString();
				model.setStage(stage);
				model.setStage_order(stage.substring(2));
			}
//arraySeqmodel.setSpecies(result.getString("species"));
			if (doc.containsKey("DEV_STAGE"))
				model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			if (doc.containsKey("PI_NAME"))
				model.setSource(doc.getFieldValue("PI_NAME").toString());
			if (doc.containsKey("LIBRARY_STRATEGY"))
				model.setLibraryStrategy(doc.getFieldValue("LIBRARY_STRATEGY").toString());
			if (doc.containsKey("DATE"))
				model.setSubmission_date(doc.getFieldValue("DATE").toString());
			if (doc.containsKey("SEX"))
				model.setSex(doc.getFieldValue("SEX").toString());
			if (doc.containsKey("SAMPLE_DESCRIPTION"))
				model.setSampleDescription(doc.getFieldValue("SAMPLE_DESCRIPTION").toString());
			if (doc.containsKey("SAMPLE_NAME"))
				model.setTitle(doc.getFieldValue("SAMPLE_NAME").toString());
			if (doc.containsKey("SERIES_GEO_ID"))
				model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			if (doc.containsKey("COMPONENT"))
				model.setSampleComponents(doc.getFieldValue("COMPONENT").toString());
			if (doc.containsKey("GENOTYPE"))
				model.setGenotype(doc.getFieldValue("GENOTYPE").toString());				
//arraySeqmodel.setAssay_type(result.getString("assay_type"));
			if (doc.containsKey("SERIES_GEO_ID"))
				model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			if (doc.containsKey("SPECIES"))
				model.setSpecies(doc.getFieldValue("SPECIES").toString());

			
			list.add(model);	
			
			gudmapset.add(doc.getFieldValue("GUDMAP").toString());

		}
		
		return list;
	}	

	public HashMap<String,String> getTotals(){		
		return totals;
	}	
}
