package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


@Named(value="solrGeneFilter")
@SessionScoped
public class SolrGeneFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> filters;
	
	private String dialogHeader;
	private boolean anchorGeneCB = false;
	private boolean markerGeneCB = false;
	private boolean sourceCB = false;
	private ArrayList<String> sourcevalues;

	
	@Inject
   	private ParamBean paramBean;
	
	@Inject
   	private SolrTreeBean solrTreeBean;

	
	public SolrGeneFilter() {
		filters = new ArrayList<String>();
	}
	
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}

	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
    public void setDialogHeader(String dialogHeader) { 
        this.dialogHeader = "Gene Filter (" + getGeneCount() + ")";
    }

    public String getDialogHeader() { 
        return dialogHeader;
    }


	public int getGeneCount(){
		return solrTreeBean.getGeneCount(filters);		
	}
	
	public boolean getAnchorGeneCB(){
		return anchorGeneCB;		
	}
	public void setAnchorGeneCB(boolean val){
		anchorGeneCB = val;	
		if (anchorGeneCB) markerGeneCB = false;
	}
	public int getAnchorGeneCount(){
		return solrTreeBean.getAnchorGeneCount();	
	}

	public boolean getMarkerGeneCB(){
		return markerGeneCB;		
	}
	public void setMarkerGeneCB(boolean val){
		markerGeneCB = val;		
		if (markerGeneCB) anchorGeneCB = false;
	}
	public int getMarkerGeneCount(){
		return solrTreeBean.getMarkerGeneCount();	
	}

	public Map<String,String> getSourcelist(){
		
		Map<String,String> sourcelist = new LinkedHashMap<String,String>();

		Map<String, String> sources =  paramBean.getSourcelist(); 
	    Iterator it = sources.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String source = (String)pair.getKey();
	        int count = solrTreeBean.getGeneFilteredCount("SOURCE:" + source);
	        if (count > 0){
				String title = source + "(" + count + ")";
				sourcelist.put(title, source);
	        }
		}

		return sourcelist;
	}
	public ArrayList<String> getSourcevalues(){
		return sourcevalues;
	}	

	public boolean getSourceCB(){
		return sourceCB;		
	}
	public void setSourceCB(boolean val){
		sourceCB = val;		
	}
	
	public void refresh(){
		filters = new ArrayList<String>();
		
		if (anchorGeneCB){ 
			filters.add("ANCHOR:1");
			filters.remove("ANCHOR:1");
		}
	
		if (markerGeneCB){
			filters.add("MARKER:1");
			filters.remove("MARKER:1");
		};
		
		setDialogHeader("SomeHeader");
	}
	
	public void resetAll(){
		anchorGeneCB = false;
		markerGeneCB = false;
	}
	
}
