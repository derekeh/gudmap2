package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.faces.context.FacesContext;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
//import org.gudmap.assemblers.SolrInsituAssembler;
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
//    	assembler=new SolrInsituAssembler();
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
//        totalRows = assembler.getCount(solrInput, filters);
        totalRows = solrTreeBean.getSolrUtil().getInsituFilteredCount(solrInput,filters);
    	
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

	private List<InsituTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		InsituTableBeanModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			String insituExpression = "";			
			if (doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new InsituTableBeanModel();
			model.setOid(doc.getFieldValue("GUDMAP").toString());
			model.setGene(doc.getFieldValue("GENE").toString());
			model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
			model.setSource(doc.getFieldValue("SOURCE").toString());
			model.setSubmission_date(doc.getFieldValue("DATE").toString());
			model.setAssay_type(doc.getFieldValue("ASSAY_TYPE").toString());
			model.setProbe_name(doc.getFieldValue("PROBE_NAME").toString());
			model.setStage(doc.getFieldValue("STAGE").toString());
			model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			model.setSex(doc.getFieldValue("SEX").toString());
			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
			model.setTissue(doc.getFieldValue("TISSUE_TYPE").toString());
			model.setExpression(insituExpression);
			model.setSpecimen(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
			model.setImage(doc.getFieldValue("IMAGE_PATH").toString());
			model.setGene_id(doc.getFieldValue("MGI_GENE_ID").toString());
			
			list.add(model);			
		}
			
		return list;
	 }	
    
}
