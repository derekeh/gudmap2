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

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
//import org.gudmap.assemblers.SolrSequencesAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.ArraySeqTableBeanModel;

@Named (value="solrSequencesBean")
@SessionScoped
public class SolrSequencesBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
//	private SolrSequencesAssembler assembler;
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
		solrInput = solrTreeBean.getSolrInput();
		refresh();
	}
	
	public String getSolrInput(){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
		return solrInput;
	}

	public void setup() {
//     	assembler=new SolrSequencesAssembler();
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
//        totalRows = assembler.getCount(solrInput, filters);
        totalRows = solrTreeBean.getSolrUtil().getSequencesCount(solrInput, filters);
    	
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
    	return "solrMicroarrayTablePage";
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
    	String str="Sequences Search Results ";
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

	public List<ArraySeqTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();

		SolrDocumentList sdl;
		try {
			
			sdl = solrTreeBean.getSolrUtil().getSequencesData(solrInput,filterlist,sortColumn,ascending,offset,num);
			list = formatTableData(sdl);
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	private List<ArraySeqTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		ArraySeqTableBeanModel model = null;
		
		int rowNum = sdl.size();
		Set<String> gudmapset = new HashSet<String>();
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			model = new ArraySeqTableBeanModel();
			model.setOid(doc.getFieldValue("GUDMAP").toString());
			model.setGudmap_accession("GUDMAP:" + doc.getFieldValue("GUDMAP").toString());
			String gid = doc.getFieldValue("SAMPLE_GEO_ID").toString();
			model.setGeoSampleID(doc.getFieldValue("SAMPLE_GEO_ID").toString());
			String stage = doc.getFieldValue("STAGE").toString();
			model.setStage(stage);
			model.setStage_order(stage.substring(2));
//arraySeqmodel.setSpecies(result.getString("species"));
			model.setAge(doc.getFieldValue("AGE").toString());
			model.setSource(doc.getFieldValue("PI_NAME").toString());
			model.setLibraryStrategy(doc.getFieldValue("LIBRARY_STRATEGY").toString());
			model.setSubmission_date(doc.getFieldValue("DATE").toString());
			model.setSex(doc.getFieldValue("SEX").toString());
			model.setSampleDescription(doc.getFieldValue("SAMPLE_DESCRIPTION").toString());
			model.setTitle(doc.getFieldValue("SAMPLE_NAME").toString());
			model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			model.setSampleComponents(doc.getFieldValue("COMPONENT").toString());
			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());				
//arraySeqmodel.setAssay_type(result.getString("assay_type"));
			model.setGeoSeriesID(doc.getFieldValue("SERIES_GEO_ID").toString());
			
			list.add(model);	
			
			gudmapset.add(doc.getFieldValue("GUDMAP").toString());

		}
		
		return list;
	}	
    
}
