package org.gudmap.utils;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ViewSolrSearch {
	
	private boolean showCloseButton=false;
	private boolean showOpenButton=true;
	private boolean showSolrTree=false;
	private boolean showSolrText = false;
	private boolean showSolrInput = false;
	
	public void init() {
		showCloseButton=false;
		showOpenButton=true;
		showSolrTree=false;
		showSolrText=false;
		showSolrInput=false;
	}
	
	public void toggle() {
		showCloseButton=(showCloseButton)?false:true;
		showOpenButton=(showOpenButton)?false:true;
		showSolrTree=(showSolrTree)?false:true;
		showSolrText=(showSolrText)?false:true;
		showSolrInput=(showSolrInput)?false:true;
	}
	
	public boolean getShowCloseButton(){
		return showCloseButton;
	}
	
	public boolean getShowOpenButton(){
		return showOpenButton;
	}
	
	public boolean getShowSolrTree(){
		return showSolrTree;
	}
	
	public boolean getShowSolrText(){
		return showSolrText;
	}
	
	public boolean getShowSolrInput(){
		return showSolrInput;
	}
	
}
