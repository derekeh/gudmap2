package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;

import org.gudmap.assemblers.SummaryBeanAssembler;
import org.gudmap.models.SummaryBeanModel;

@Named
@SessionScoped
public class SummaryBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private SummaryBeanAssembler dbsummary;
	private List<SummaryBeanModel> datalist;
	
	
	public SummaryBean() {
		dbsummary  = new SummaryBeanAssembler();
		datalist = dbsummary.getData();		
	}
	
	public int getIshTotal() {
		return datalist.get(0).GetIshTotal();
		
	}
	public int getWishTotal() {
		return datalist.get(0).GetWishTotal();
		
	}
	public int getSishTotal() {
		return datalist.get(0).GetSishTotal();
		
	}
	public int getOptTotal() {
		return datalist.get(0).GetOptTotal();
		
	}	
	public int getIhcTotal() {
		return datalist.get(0).GetIhcTotal();
		
	}
	public int getTgTotal() {
		return datalist.get(0).GetTgTotal();
		
	}
	public int getMicroarrayTotal() {
		return datalist.get(0).GetMicroarrayTotal();
		
	}
	
	public int getSequenceTotal() {
		return datalist.get(0).GetSequenceTotal();
		
	}
	
	public int getIshGeneTotal() {
		return datalist.get(0).GetIshGeneTotal();
		
	}
	
	public int getWishGeneTotal() {
		return datalist.get(0).GetWishGeneTotal();
		
	}
	
	public int getSishGeneTotal() {
		return datalist.get(0).GetSishGeneTotal();
		
	}
	
	public int getOptGeneTotal() {
		return datalist.get(0).GetOptGeneTotal();
		
	}
	
	public int getIhcGeneTotal() {
		return datalist.get(0).GetIhcGeneTotal();
		
	}
	
	public int getTgGeneTotal() {
		return datalist.get(0).GetTgGeneTotal();
		
	}


}
