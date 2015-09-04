package org.gudmap.beans;


import java.io.Serializable;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.SolrTissueSummaryAssembler;
import org.gudmap.impl.PagerImpl;


@Named (value="solrTissueSummaryBean")
@SessionScoped
public class SolrTissueSummaryBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
	private SolrTissueSummaryAssembler assembler;
    private String whereclause = " WHERE ";
    
    @Inject
   	private ParamBean paramBean;
    
    @Inject
   	private SolrTreeBean solrTreeBean;
    
	private String solrInput;
    
	    
    // Constructors -------------------------------------------------------------------------------

    public SolrTissueSummaryBean() {
    	super(100,10,"RELEVANCE",true);
    	setup();
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
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
     	assembler=new SolrTissueSummaryAssembler();
     }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
        totalRows = assembler.getCount(solrInput, "");
    	
     	dataList = assembler.getData(solrInput, "", null, sortField, sortAscending, firstRow, rowsPerPage);
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
    }

    public String refresh(){
 //   	sortField = "RELEVANCE";
    	loadDataList();
    	paramBean.resetValues();
    	return "solrTissueSummaryTablePage";
    }
    
}
