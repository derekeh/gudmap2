package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;

import org.gudmap.assemblers.SummaryBeanAssembler;
import org.gudmap.models.SummaryBeanModel;

@Named(value="volatileSummaryBean")
@ViewScoped
public class VolatileSummaryBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private SummaryBeanAssembler dbsummary;
	private List<SummaryBeanModel> datalist;
	private String focusGroup="reset";
	
	
	public VolatileSummaryBean() {
		dbsummary  = new SummaryBeanAssembler();
		datalist = dbsummary.getData();		
	}
	
	/*public void setFocusField(String focusField){
		this.focusField=focusField;
	}*/
	
	/*public String focusGroupAction(String focusGroup){
		dbsummary.setFocusGroup(focusGroup);
		datalist = dbsummary.getData();
		return null;
	}*/
	
	public void setFocusGroup(String focusGroup){
		this.focusGroup=focusGroup;
		dbsummary.setFocusGroup(focusGroup);
		datalist = dbsummary.getData();
	}
	
	public String getFocusGroup(){
		return focusGroup;
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
