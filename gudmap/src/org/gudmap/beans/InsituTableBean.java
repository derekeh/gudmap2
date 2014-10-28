package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.gudmap.beans.assemblers.InsituTableBeanAssembler;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.beans.utils.RepeatPaginator;

@Named
@SessionScoped
public class InsituTableBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private InsituTableBeanAssembler assembler;
	private List<InsituTableBeanModel> datalist;
	private Map<String,String> totalslist;
	private RepeatPaginator paginator;
	
	public InsituTableBean(){
		assembler = new InsituTableBeanAssembler();
		datalist=assembler.getData();
		totalslist=assembler.getTotals();
		paginator = new RepeatPaginator(datalist);
	}
	
	
	public List<InsituTableBeanModel> getDatalist() {
		return datalist;
	}
	
	public Map<String,String> getTotalslist() {
		return totalslist;
	}
	
	 public RepeatPaginator getPaginator() {
	        return paginator;
	 }
	 
	 
	
}
