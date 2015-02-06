package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.gudmap.assemblers.InsituTableBeanAssembler;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.utils.RepeatPaginator;

/*
 * This Bean invokes a paginator which takes in and processes the whole model. EG. All the subsequent work is done  client side on the model with no return to the DB server.
 * This is not the preferred option at present.
 * We XXXTablePageBean.java which extends PagerImpl.java so that a definite number of records are retrieved per request.
 */

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
