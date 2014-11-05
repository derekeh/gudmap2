package org.gudmap.beans;


import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.beans.assemblers.InsituTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.queries.generic.GenericQueries;

@Named
@SessionScoped
public class InsituTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	private InsituTablePageBeanAssembler assembler;
    private String whereclause = " WHERE ";
    
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
   
}
