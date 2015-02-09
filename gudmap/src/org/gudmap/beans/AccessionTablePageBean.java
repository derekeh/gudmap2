package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.AccessionTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.utils.Utils;

@Named
@SessionScoped
public class AccessionTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	protected AccessionTablePageBeanAssembler assembler;
    private String whereclause = GenericQueries.WHERE_CLAUSE;
    private String specimenWhereclause="";
    protected List<String> selectedItems;
    private boolean areAllChecked;
    private String accessionTotals;
    private String accessionInput;
    private String accessionInputQuery;
    
    @Inject
   	protected ParamBean paramBean;
   	
    // Constructors -------------------------------------------------------------------------------

    public AccessionTablePageBean() {
    	super(20,10,"gene",true);   	
        //setup("ISH","");
        setup();
    }
    
	public AccessionTablePageBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}

	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
    
    public void setup() {
    	//TODO find the generic query to use (and/or specimen assay types) based on assay type
    	
    	assembler=new AccessionTablePageBeanAssembler(GenericQueries.BROWSE_ACCESSION_PARAM);
    	
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    }
    
    @Override
    public void loadDataList() {
    	dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
    									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,accessionInputQuery,
    									paramBean.getFocusGroupSpWhereclause());
        // Set currentPage, totalPages and pages.
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
        this.accessionTotals=assembler.getAccessionTotals();
    }
    
    public String refresh(){
    	loadDataList();
    	paramBean.resetValues();
    	return "browseAccessionTablePage";
    	
    }
    
    public String resetAll() {
		paramBean.resetAll();
		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
		return "browseAccessionTablePage";
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
    
    public String getAccessionTotals() {
		return accessionTotals;
	}
    
    public String processAccession() {
    	loadDataList();
    	return "browseAccessionTablePage";
    }
    
    public void setAccessionInput(String accessionInput){
    	// check for empty string
    	if (accessionInput == "")
    	{
    		this.accessionInput = accessionInput;
    	}
    	else
    	{    						
    		String[] accessionList = accessionInput.split("\\;");
    		String parsedString = "";
    		String parsedQuery = "";
    		String tmpStr = "";
    		for (int i=0; i<accessionList.length; i++)
    		{
    			parsedQuery += Utils.checkAccessionInput(accessionList[i].trim()) + ",";
    			tmpStr = Utils.checkAccessionInput(accessionList[i].trim())+"\n"; 
    			parsedString += tmpStr;
    			tmpStr="";
    		}
    		this.accessionInputQuery = parsedQuery.substring(0,parsedQuery.length()-1); 
    		this.accessionInput=parsedString.replace("'","");
    		
    	}    	
    }
    
    public String getAccessionInput(){
    	return accessionInput;
    }
    
    public String getAccessionInputQuery() {
    	return accessionInputQuery;
    }
   
}
