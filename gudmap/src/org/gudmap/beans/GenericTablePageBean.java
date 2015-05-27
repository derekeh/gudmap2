package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.GeneListTablePageBeanAssembler;
import org.gudmap.assemblers.InsituTablePageBeanAssembler;
import org.gudmap.assemblers.MicSeriesTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.genestrip.GeneListQueries;
import org.gudmap.utils.Utils;

@Named
@SessionScoped
public class GenericTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	protected InsituTablePageBeanAssembler assembler;
	protected MicSeriesTablePageBeanAssembler micSeriesAssembler;
	private GeneListTablePageBeanAssembler geneListAssembler;
    private String specimenWhereclause="";
    protected List<String> selectedItems;
    private boolean areAllChecked;
    
    private String queryTotals;
    private String userInput="";
    private String userInputQuery;
    
    private String assayType="";
    
    @Inject
   	protected ParamBean paramBean;
    
  /*  @Inject
   	protected SessionBean sessionBean;*/
   	
    // Constructors -------------------------------------------------------------------------------

    public GenericTablePageBean() {
    	//super(20,10,"SUB_OID",true);   	
        //setup("ISH","");
    }
    
	/*public GenericTablePageBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}*/

	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
/*	public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}*/
	
	public void init(String assayType, String specimenAssay, int rowsperpage, int pagenumbers, String defaultOrderCol, boolean sortDirection) {
		this.assayType=assayType;
		initPaging(rowsperpage,pagenumbers,defaultOrderCol,sortDirection);
		setup(assayType,specimenAssay);
	}
	
    
    public void setup(String assayType,String specimenAssay) {
    	//TODO find the generic query to use (and/or specimen assay types) based on assay type
    	this.assayType=assayType;
    	
    	if(specimenAssay.equals("WISH"))
    		specimenWhereclause=GenericQueries.WHERE_WISH;
    	else if(specimenAssay.equals("SISH"))
    		specimenWhereclause = GenericQueries.WHERE_SISH;
    	else if(specimenAssay.equals("OPT"))
    		specimenWhereclause = GenericQueries.WHERE_OPT;
    	
    	if(assayType.equals("genelist"))
    		geneListAssembler=new GeneListTablePageBeanAssembler(GeneListQueries.BROWSE_GENELIST_PARAM);
    	else if(assayType.equals("Microarray"))
    		micSeriesAssembler= new MicSeriesTablePageBeanAssembler(ArrayQueries.MIC_SERIES_BROWSE_PARAM,assayType);
    	else if(assayType.equals("TG"))
    		assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_TG_PARAM,assayType);
    	else
    		assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_ISH_PARAM,assayType);
    	
    	
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void doSomething() {
    	
    }
    
    /*@PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    }*/
    
    @Override
    public void loadDataList() {
    	if(assayType.equals("genelist")) {
    		dataList = geneListAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
    				paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
    				paramBean.getFocusGroupSpWhereclause());
        	
            // Set currentPage, totalPages and pages.
        	//setTotalslist(assembler.getTotals());
        	totalRows = geneListAssembler.count();
        	queryTotals=geneListAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("Microarray")) {
    		dataList = micSeriesAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getMicWhereclause(),
					paramBean.getFocusGroupWhereclause());
			// Set currentPage, totalPages and pages.
    		//TODO Write column total queries.
			//setTotalslist(micSeriesAssembler.getTotals());
			totalRows = micSeriesAssembler.count();
    	}
    	else
    	{
	    	dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
	    									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause);
	        // Set currentPage, totalPages and pages.
	    	setTotalslist(assembler.getTotals());
	    	totalRows = assembler.count();
    	}
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
    }
    
   /* public String refresh(String returnPage){
    	loadDataList();
    	////paramBean.resetValues();
    	return returnPage;
    }
    
    public String resetAll(String returnPage) {
		paramBean.resetAll();
		//must return to homepage to reset focus group. Can't refresh div on other page
		////paramBean.setFocusGroup("reset");
		loadDataList();
		return returnPage;
	}*/
    
    public void refresh(){
    	loadDataList();
    }
    
    public void resetAll() {
		paramBean.resetAll();
		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((InsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((InsituTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((InsituTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }
    
    public String getQueryTotals() {
		return queryTotals;
	}
    
    public String prepareTable() {
    	loadDataList();
    	return "browseGeneListTablePage";
    }
    
    public void setUserInput(String input){
    	if(input==null || input.equals(""))
    		this.userInput="";
    	else {
	    	String processedValues[] = Utils.processInputString(input,false);
	    	this.userInputQuery = processedValues[0];
	    	this.userInput = processedValues[1];
    	}
    }
       
    
    public String getUserInput(){
    	return userInput;
    }
    
    public String getUserInputQuery() {
    	return userInputQuery;
    }
   
}
