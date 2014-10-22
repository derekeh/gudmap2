package org.gudmap.beans.utils;

import java.util.List;

import org.gudmap.models.InsituTableBeanModel;

public class CopyOfRepeatPaginator {

    private static final int DEFAULT_RECORDS_NUMBER = 20;
    private static final int DEFAULT_PAGE_INDEX = 1;

    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
    private int gotopage;
    private List<InsituTableBeanModel> origModel;
    private List<InsituTableBeanModel> model;

    public CopyOfRepeatPaginator(List<InsituTableBeanModel> model) {
        this.origModel = model;
        this.records = DEFAULT_RECORDS_NUMBER;
        this.pageIndex = DEFAULT_PAGE_INDEX;        
        this.recordsTotal = model.size();

        if (records > 0) {
            pages = records <= 0 ? 1 : recordsTotal / records;

            if (recordsTotal % records > 0) {
                pages++;
            }

            if (pages == 0) {
                pages = 1;
            }
        } else {
            records = 1;
            pages = 1;
        }

        updateModel();
    }

    public void updateModel() {
        int fromIndex = getFirst();
        int toIndex = getFirst() + records;

        if(toIndex > this.recordsTotal) {
            toIndex = this.recordsTotal;
        }

        this.model = origModel.subList(fromIndex, toIndex);
    }

    public void next() {
        if(this.pageIndex < pages) {
            this.pageIndex++;
        }

        updateModel();
    }

    public void prev() {
        if(this.pageIndex > 1) {
            this.pageIndex--;
        }

        updateModel();
    }  
    
    public void firstpage() {
        this.pageIndex = 1;
        updateModel();
    } 
    
    public void lastpage() {
        this.pageIndex = pages;
        updateModel();
    } 
    
    public void SetGotopage(String gotopage) {
    	this.gotopage=Integer.parseInt(gotopage);
        this.pageIndex = this.gotopage;
        updateModel();
    }

    public int getRecords() {
        return records;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPages() {
        return pages;
    }

    public int getFirst() {
        return (pageIndex * records) - records;
    }

    public List<InsituTableBeanModel> getModel() {
        return model;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}

