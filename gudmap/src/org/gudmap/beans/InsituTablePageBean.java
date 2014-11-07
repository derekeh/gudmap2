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

import org.gudmap.beans.assemblers.InsituTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.generic.GenericQueries;

@Named
@SessionScoped
public class InsituTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	private InsituTablePageBeanAssembler assembler;
    private String whereclause = " WHERE ";
    private List<String> selectedItems;
    private boolean areAllChecked;
    
    @Inject
   	private ParamBean paramBean;
   	
    // Constructors -------------------------------------------------------------------------------

    public InsituTablePageBean() {
    	super(20,10,"SUB_OID",true);   	
        setup();
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
    
    public void setup() {
    	assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_ISH_PARAM,"ISH",whereclause);
        setTotalslist(assembler.getTotals());
        totalRows = assembler.count();
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    }
    
    @Override
    public void loadDataList() {
    	dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause());
        // Set currentPage, totalPages and pages.
    	setTotalslist(assembler.getTotals());
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
    }
    
    public String refresh(){
    	loadDataList();
    	paramBean.resetValues();
    	return "browseInsituTablePage";
    	
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
   
}
