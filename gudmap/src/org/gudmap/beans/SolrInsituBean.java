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
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.models.SolrInsituTableBeanModel;




@Named (value="solrInsituBean")
@SessionScoped
public class SolrInsituBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
//	private SolrInsituAssembler assembler;
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

    public SolrInsituBean() {
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
        totalRows = solrTreeBean.getSolrUtil().getInsituFilteredCount(solrInput,filters);
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
//    	paramBean.resetValues();
    	return "solrInsitu";
    }

    public void resetAll() {
		paramBean.resetAll();
//		solrFilterBean.resetAll();		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
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
    	String str="Insitu Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getInsituCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getInsituCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getInsituCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getInsituCount(filters) + ") > ALL";
    		
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
	public List<InsituTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
					
    	try {
    		
			SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getInsituData(solrInput, filterlist, sortColumn,ascending,offset,num);
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

	/**
	 * This method creates a list of InsituTableBeanModels from the documents in the SolrDocumentList sdl.
	 * 
	 * @param sdl A SolrDocumentList	  
	 * @return A List of InsituTableBeanModels
	 * @see InsituTableBeanModel
	 */
	private List<InsituTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		InsituTableBeanModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			String insituExpression = "";			
			if (doc.containsKey("PRESENT")  && doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.containsKey("UNCERTAIN")  && doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.containsKey("NOT_DETECTED")  && doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new InsituTableBeanModel();
			if (doc.containsKey("GUDMAP"))
				model.setOid(doc.getFieldValue("GUDMAP").toString());
			if (doc.containsKey("GENE"))
				model.setGene(doc.getFieldValue("GENE").toString());
			if (doc.containsKey("SOURCE"))
				model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
			if (doc.containsKey("GUDMAP_ID"))
				model.setSource(doc.getFieldValue("SOURCE").toString());
			if (doc.containsKey("DATE"))
				model.setSubmission_date(doc.getFieldValue("DATE").toString());
			if (doc.containsKey("ASSAY_TYPE"))
				model.setAssay_type(doc.getFieldValue("ASSAY_TYPE").toString());
			if (doc.containsKey("PROBE_NAME"))
				model.setProbe_name(doc.getFieldValue("PROBE_NAME").toString());
			if (doc.containsKey("STAGE")){
				String stage = doc.getFieldValue("STAGE").toString();
				model.setStage(stage);
				model.setStage_order(stage.replace("TS", ""));
			}
			if (doc.containsKey("DEV_STAGE"))
				model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			if (doc.containsKey("SEX"))
				model.setSex(doc.getFieldValue("SEX").toString());
			if (doc.containsKey("GENOTYPE"))
				model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
			if (doc.containsKey("SPECIMEN_ASSAY_TYPE"))
				model.setSpecimen(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
			if (doc.containsKey("IMAGE_PATH"))
				model.setImage(doc.getFieldValue("IMAGE_PATH").toString());
			if (doc.containsKey("MGI_GENE_ID"))
				model.setGene_id(doc.getFieldValue("MGI_GENE_ID").toString());
			if (doc.containsKey("SYNONYMS"))
				model.setSynonyms(doc.getFieldValue("SYNONYMS").toString());
			if (doc.containsKey("TISSUE_TYPE")){
				String tissue = doc.getFieldValue("TISSUE_TYPE").toString();
				model.setTissue(TissueFilter(tissue));
			}			
			if (doc.containsKey("SPECIES"))
				model.setSpecies(doc.getFieldValue("SPECIES").toString());
			
			model.setExpression(insituExpression);

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
    
}
