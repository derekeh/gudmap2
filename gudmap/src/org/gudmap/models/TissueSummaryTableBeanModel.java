package org.gudmap.models;

import java.util.List;

public class TissueSummaryTableBeanModel {

	private String oid,name,stages,reason=null;
	private List<String> totals=null;;
	private boolean selected;

	public void setOid(String oid){
		this.oid=oid;
	}
	public String getOid(){
		return oid;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setStages(String stages){
		this.stages=stages;
	}
	public String getStages(){
		return stages;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public String getReason(){
		return reason;
	}
	
	/////////////////////////////////////////////////////////
	public void setTotals(List<String> totals){
		this.totals=totals;
	}
	public List<String> getTotals(){
		return totals;
	}
	
	public void setSelected(boolean selected){
		this.selected=selected;
	}
	
	public boolean getSelected() {
		return selected;
	}
	

}
