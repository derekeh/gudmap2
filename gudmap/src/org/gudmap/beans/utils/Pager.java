package org.gudmap.beans.utils;


import org.gudmap.impl.PagerImpl;

public class Pager extends PagerImpl {
	
	public Pager(int rowsPerPage, int pageRange, String sortField, boolean sortAscending) {
        super(rowsPerPage, pageRange, sortField, sortAscending);
    }

}
