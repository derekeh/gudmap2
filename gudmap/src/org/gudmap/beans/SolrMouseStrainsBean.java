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
//import org.gudmap.assemblers.SolrMouseStrainsAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.MouseStrainsTableBeanModel;

@Named (value="solrMouseStrainsBean")
@SessionScoped
public class SolrMouseStrainsBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
//	private SolrMouseStrainsAssembler assembler;
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

    public SolrMouseStrainsBean() {
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
//     	assembler=new SolrMouseStrainsAssembler();
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
        totalRows = solrTreeBean.getSolrUtil().getMouseStrainsCount(solrInput, filters);
   	
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
//   	paramBean.resetValues();
    	return "solrMouseStrains";
    }

    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((MouseStrainsTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((MouseStrainsTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((MouseStrainsTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
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
    
	public List<MouseStrainsTableBeanModel> getData(String solrInput, HashMap<String, String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<MouseStrainsTableBeanModel> list = new ArrayList<MouseStrainsTableBeanModel>();

		SolrDocumentList sdl = solrTreeBean.getSolrUtil().getMouseStrainsData(solrInput,filterlist,sortColumn,ascending,offset,num);
		list = formatTableData(sdl);

		return list;
	}

	private List<MouseStrainsTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<MouseStrainsTableBeanModel> list = new ArrayList<MouseStrainsTableBeanModel>();
		MouseStrainsTableBeanModel model = null;
		
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
