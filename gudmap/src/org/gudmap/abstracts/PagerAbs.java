package org.gudmap.abstracts;

import java.util.List;
import javax.faces.event.ActionEvent;
/**
 * Interface methods required for paging. 
 * @author dhoughto
 * @see impl.PagerImpl
 */
public abstract interface PagerAbs {
	
	public abstract void pageNext();
	public abstract void pagePrevious();
	public abstract void pageFirst();
	public abstract void pageLast();
	public abstract void page(int pagenum);
	public abstract void page(ActionEvent e);
	public abstract void loadDataList();
	public abstract int getTotalRows();
	public abstract int getFirstRow();
    public abstract int getRowsPerPage();
    public abstract Integer[] getPages();;
    public abstract int getCurrentPage(); ;
    public abstract int getTotalPages();;
	public abstract List<?> getDataList();

}
