package org.gudmap.models;

import java.util.List;

public class TutorialBeanModel {
	
	private String id,title,resourcename,dctitle=null;
	private List<String> totals=null;;
	private boolean selected;

	public void setId(String id){
		this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setResourcename(String resourcename){
		this.resourcename=resourcename;
	}
	public String getResourcename(){
		return resourcename;
	}
	public void setDctitle(String dctitle){
		this.dctitle=dctitle;
	}
	public String getDctitle(){
		return dctitle;
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
