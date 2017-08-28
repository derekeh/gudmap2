package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.EurExpTableBeanModel;
import org.gudmap.models.SolrInsituTableBeanModel;




/**
 * solrEurExp.xhtml web page
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
@Named (value="solrEurExpBean")
@SessionScoped
public class SolrEurExpBean extends PagerImpl implements Serializable  {
	
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

    public SolrEurExpBean() {
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
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrInput = solrTreeBean.getSolrInput();
     }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
        totalRows = solrTreeBean.getSolrUtil().getEurExpFilteredCount(solrInput,filters);
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
    	return "solrEurExpress";
    }

    public void resetAll() {
		paramBean.resetAll();
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((SolrInsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((SolrInsituTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((SolrInsituTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
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
    	String str="EurExpress Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getEurExpCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getEurExpCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getEurExpCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getEurExpCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }
    
    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a list of InsituTableBeanModels containing the retrieved documents.
     * 
	 * @param solrInput The main query string for retrieving relevant documents'
     * @param filterlist A list of filters to be applied to the solr search
     * @param sortColumn The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param num The number of documents to be retrieved in the result set.
     * @return A list of InsituTableBeanModels
	 * @see InsituTableBeanModel
     */
	public List<EurExpTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<EurExpTableBeanModel> list = new ArrayList<EurExpTableBeanModel>();
					
    	try {
    		
			SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getEurExpData(solrInput, filterlist, sortColumn,ascending,offset,num);
			if (sdl==null){
				return null;
			}
			list = formatTableData(sdl);
			

			totals = (HashMap<String, String>) solrTreeBean.getSolrUtil().getEurExpDataCount(solrInput, filterlist);
			
    	} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * This method creates a list of InsituTableBeanModels from the documents in the SolrDocumentList sdl.
	 * 
	 * @param sdl A SolrDocumentList	  
	 * @return A List of InsituTableBeanModels
	 * @see InsituTableBeanModel
	 */
	private List<EurExpTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<EurExpTableBeanModel> list = new ArrayList<EurExpTableBeanModel>();
		EurExpTableBeanModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			
			model = new EurExpTableBeanModel();
			if (doc.containsKey("ID"))
				model.setEeid(doc.getFieldValue("ID").toString());
			if (doc.containsKey("EUREXP_ID"))
				model.setOid(doc.getFieldValue("EUREXP_ID").toString());
			if (doc.containsKey("GENE"))
				model.setGene(doc.getFieldValue("GENE").toString());
			if (doc.containsKey("MGI_GENE_ID"))
				model.setMgi_id(doc.getFieldValue("MGI_GENE_ID").toString());
			if (doc.containsKey("ENTREZ_ID"))
				model.setEntrez_id(doc.getFieldValue("ENTREZ_ID").toString());
			if (doc.containsKey("DEV_STAGE"))
				model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			if (doc.containsKey("STAGE")){
				String stage = doc.getFieldValue("STAGE").toString();
				model.setStage(stage);
				model.setStage_order(stage.replace("TS", ""));
			}
			if (doc.containsKey("COMPONENT"))
				model.setTissue(doc.getFieldValue("COMPONENT").toString());
			if (doc.containsKey("SYNONYM"))
				model.setSynonyms(doc.getFieldValue("SYNONYM").toString());

			if (doc.containsKey("IMAGE_TYPE"))
				model.setImageType(doc.getFieldValue("IMAGE_TYPE").toString());
			
			if (doc.containsKey("IMAGE"))
				model.setImage(doc.getFieldValue("IMAGE").toString());
			if (doc.containsKey("IMAGE_PATH"))
				model.setImage_path(doc.getFieldValue("IMAGE_PATH").toString());
			if (doc.containsKey("THUMBNAIL_PATH"))
				model.setThumbnail_path(doc.getFieldValue("THUMBNAIL_PATH").toString());
			if (doc.containsKey("SEX"))
				model.setSex(doc.getFieldValue("SEX").toString());
			

			list.add(model);			
		}
			
		return list;
	 }
	
	/**
	 * This method takes a string and filters out the EMAP ID's.
	 * 
	 * @param tissues A string
	 * @return A String containing EMAP ID's
	 */
	private String TissueFilter(String tissues){
		// filter out the EMAP strings for the display
		
		String result = "";
		String[] items = tissues.split(";");
		if (items.length > 0){
			for(String item : items){
				if (!item.contains("EMAP")){
					result += item + "; ";
				}
			}
			int len = result.length();
			result = result.substring(0, len-2);
		}
		return result;
	}

	public HashMap<String,String> getTotals(){		
		return totals;
	}
}
