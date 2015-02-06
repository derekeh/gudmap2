package org.gudmap.impl;

import java.util.List;

import org.gudmap.abstracts.RepeatPaginatorAbs;
/*
 * This paginator takes in and processes the whole model. EG. All the subsequent work is done  client side on the model with no return to the DB server.
 * This is not the preferred option at present.
 * We use PagerImpl.java so that a definite number of records are retrieved per request.
 */
public class RepeatPaginatorImpl implements RepeatPaginatorAbs{

    protected static final int DEFAULT_RECORDS_NUMBER = 20;
    protected static final int DEFAULT_PAGE_INDEX = 1;

    protected int records;
    protected int recordsTotal;
    protected int pageIndex;
    protected int pages;
    protected List<?> origModel;
    protected List<?> model;
    protected int gotopage;
    
    public RepeatPaginatorImpl(List<?> model) {
        this.origModel = model;
        this.records = DEFAULT_RECORDS_NUMBER;
        this.pageIndex = DEFAULT_PAGE_INDEX; 
        this.gotopage = DEFAULT_PAGE_INDEX;
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
    
    public void setGotopage(int gotopage) {
    	this.gotopage=gotopage;
    	this.pageIndex = gotopage;
        updateModel();
    }
    
    public int getGotopage() {
    	return gotopage;
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

    public List<?> getModel() {
        return model;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

}

