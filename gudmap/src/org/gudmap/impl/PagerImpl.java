package org.gudmap.impl;

import java.util.List;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;

import org.gudmap.abstracts.PagerAbs;


public class PagerImpl implements PagerAbs {
	
	protected List<?> dataList;
    protected Map<String,String> totalslist;
	protected int totalRows;

    // Paging.
	protected int firstRow;
	protected int rowsPerPage;
	protected int totalPages;
	protected int pageRange;
	protected Integer[] pages;
	protected int currentPage;
	protected int gotopage;

    // Sorting.
	protected String sortField;
	protected boolean sortAscending;
	
	public PagerImpl() {
		
	}
	
	public PagerImpl(int rowsPerPage, int pageRange, String sortField, boolean sortAscending){
		// Set default values somehow (properties files?).
        this.rowsPerPage = rowsPerPage; // Default rows per page (max amount of rows to be displayed at once).
        this.pageRange = pageRange; // Default page range (max amount of page links to be displayed at once).
        this.sortField = sortField; // Default sort field.
        this.sortAscending = sortAscending; // Default sort direction.
	}
	
	// Paging actions -----------------------------------------------------------------------------

    public void pageFirst() {
        page(0);
    }

    public void pageNext() {
        page(firstRow + rowsPerPage);
    }

    public void pagePrevious() {
        page(firstRow - rowsPerPage);
    }

    public void pageLast() {
        page(totalRows - ((totalRows % rowsPerPage != 0) ? totalRows % rowsPerPage : rowsPerPage));
    }

    public void page(ActionEvent event) {
        page(((Integer) ((UICommand) event.getComponent()).getValue() - 1) * rowsPerPage);
    }

    public void page(int firstRow) {
        this.firstRow = firstRow;
        loadDataList(); // Load requested page.
    }
	
	
    public void loadDataList() {
    	//dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending);
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

    // Getters ------------------------------------------------------------------------------------

    public List<?> getDataList() {
        if (dataList == null) {
            loadDataList(); // Preload page for the 1st view.
        }
        return dataList;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public Integer[] getPages() {
        return pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
    
    // non abstract functions--------------------
    
 // Sorting actions ----------------------------------------------------------------------------

    public void sort(ActionEvent event) {
        String sortFieldAttribute = (String) event.getComponent().getAttributes().get("sortField");

        // If the same field is sorted, then reverse order, else sort the new field ascending.
        if (sortField.equals(sortFieldAttribute)) {
            sortAscending = !sortAscending;
        } else {
            sortField = sortFieldAttribute;
            sortAscending = true;
        }

        pageFirst(); // Go to first page and load requested page.
    }
    
    public Map<String,String> getTotalslist() {
		return totalslist;
	}
    
    public int getGotopage(){
    	return gotopage;
    }
    
 // Setters ------------------------------------------------------------------------------------

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }
    
    public void setGotopage(int gotopage){
    	this.gotopage=gotopage;
    	page((gotopage - 1) * rowsPerPage);
    }
    
    public void setDataList(List<?> dataList){
    	this.dataList=dataList;
    }
    
    public void setTotalRows(int totalRows){
    	this.totalRows=totalRows;
    }
    
    public void setTotalslist(Map<String,String> totalslist) {
    	this.totalslist=totalslist;
    }
    
   
    
    

}
