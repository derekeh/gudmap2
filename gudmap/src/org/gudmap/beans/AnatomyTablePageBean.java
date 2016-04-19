package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
//import javax.faces.event.ActionEvent;
//import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.AnatomyTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.utils.Utils;

@Named
@SessionScoped
public class AnatomyTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	protected AnatomyTablePageBeanAssembler assembler;
    private String whereclause = GenericQueries.WHERE_CLAUSE;
    private String specimenWhereclause="";
    protected List<String> selectedItems;
    private boolean areAllChecked;
    private String queryTotals;
    private String userInput="";
    private String userInputQuery;
    
    @Inject
   	protected ParamBean paramBean;
   	
    // Constructors -------------------------------------------------------------------------------

    public AnatomyTablePageBean() {
    	super(20,10,"x.assay_type",true);   	
        //setup("ISH","");
        setup();
    }
    
	public AnatomyTablePageBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}

	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
    
    public void setup() {
    	//TODO find the generic query to use (and/or specimen assay types) based on assay type
    	
    	//assembler=new AnatomyTablePageBeanAssembler(AnatomyQueries.BROWSE_ANATOMY_PARAM);
    	assembler=new AnatomyTablePageBeanAssembler();
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    }
    
    @Override
    public void loadDataList() {
    	
    	assembler.init(userInputQuery.replace("'", ""));
    	dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
    									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
    									paramBean.getFocusGroupSpWhereclause(),paramBean.getCachewhereclause(),paramBean.getArraycachewhereclause());
        // Set currentPage, totalPages, columntotals and pages.
    	//setTotalslist(assembler.getTotals());
    	totalRows = assembler.count();
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
        this.queryTotals=assembler.getQueryTotals();
       
    }
    
    public String refresh(){
    	loadDataList();
    	//paramBean.resetValues();
    	return "browseAnatomyTablePage";
    	
    }
    
    public String resetAll() {
		paramBean.resetAll();
		loadDataList();
		return "browseAnatomyTablePage";
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
    	return "browseAnatomyTablePage";
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
