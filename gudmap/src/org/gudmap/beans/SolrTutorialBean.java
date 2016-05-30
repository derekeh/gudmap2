package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;




import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.TutorialBeanModel;

@Named (value="solrTutorial")
@SessionScoped
public class SolrTutorialBean extends PagerImpl implements Serializable  {
	
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
	private boolean showPageDetails = true;
    
    // Constructors -------------------------------------------------------------------------------

    public SolrTutorialBean() {
    	super(10,10,"RELEVANCE",true);
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

     }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
//        totalRows = solrTreeBean.getSolrUtil().getTutorialCount(solrInput, filters);
   	
     	dataList = getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
       // Set currentPage, totalPages and pages.
//        currentPage = (totalRows / rowsPerPage) - ((totalRows - firstRow) / rowsPerPage) + 1;
//        totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
//        int pagesLength = Math.min(pageRange, totalPages);
//        pages = new Integer[pagesLength];
//
//        // firstPage must be greater than 0 and lesser than totalPages-pageLength.
//        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);
//
//        // Create pages (page numbers for page links).
//        for (int i = 0; i < pagesLength; i++) {
//            pages[i] = ++firstPage;
//        }
        
        if (dataList.size() > rowsPerPage)
        	showPageDetails = true;
        else
        	showPageDetails = false;
    }

    public String refresh(){
 //   	sortField = "RELEVANCE";
    	loadDataList();
//   	paramBean.resetValues();
    	return "solrTutorial";
    }

    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((TutorialBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((TutorialBeanModel) dataList.get(i)).getId()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((TutorialBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }

    
    public String getTitle(){
    	String str="Mousestrains Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getMouseStrainsCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getMouseStrainsCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getMouseStrainsCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getMouseStrainsCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }  
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }
    
    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a list of TutorialBeanModels containing the retrieved documents.
     * 
	 * @param solrInput The main query string for retrieving relevant documents'
     * @param filterlist A list of filters to be applied to the solr search
     * @param sortColumn The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param num The number of documents to be retrieved in the result set.
     * @return A list of TutorialBeanModels
     * @see TutorialBeanModel
     */
	public List<TutorialBeanModel> getData(String solrInput, HashMap<String, String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<TutorialBeanModel> list = new ArrayList<TutorialBeanModel>();

//		SolrDocumentList sdl = solrTreeBean.getSolrUtil().getTutorialData(solrInput,filterlist,sortColumn,ascending,offset,num);
//		list = formatTableData(sdl);

		return list;
	}

	/**
	 * This method creates a list of EditPageModels from the documents in the QueryResponse qr.
	 * 
	 * @param sdl A solr SolrDocumentList	  
	 * @return A List of TutorialBeanModels
     * @see TutorialBeanModel
	 */
	private List<TutorialBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<TutorialBeanModel> list = new ArrayList<TutorialBeanModel>();
		TutorialBeanModel model = null;
		
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			model = new TutorialBeanModel();
			if (doc.containsKey("id")){
				
				String file = doc.getFieldValue("id").toString();
				String old = "/export/data0/bernardh/solr-5.2.1/solr-5.2.1/gudmap_data/tutorials";
//				String repl = "glenelgin:/opt/MAWWW/Public/html/About/Tutorial";
				String repl = "http://www.gudmap.org/About/Tutorial";
				file = file.replace(old, repl);
				model.setId(file);
			}
			if (doc.containsKey("title"))
				model.setTitle(doc.getFieldValue("title").toString());
			if (doc.containsKey("resourcename"))
				model.setResourcename(doc.getFieldValue("resourcename").toString());
			if (doc.containsKey("dc_title"))
				model.setDctitle(doc.getFieldValue("dc_title").toString());
			
			list.add(model);			
		}
		
		return list;
	}	
   
}
