package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Named(value="solrFilter")
@SessionScoped
public class SolrFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String,String> filters;	
	private ArrayList<ArrayList<String>> filters2;
//	private ArrayList<String> filterlist;	
	
	private String geneValue;
	private Date fromDateValue;
	private Date toDateValue;
	private ArrayList<String> sexValues;	
	private ArrayList<String> assayTypeValues;	
	private ArrayList<String> sourceValues;	
	private ArrayList<String> specimenTypeValues;
	private ArrayList<String> theilerStageValues;
	private ArrayList<String> carnegieStageValues;
	private String expressionValue = "";
	private String anatomy;
	private String page;
	private boolean showFilter = false;
	private int filterWidth = 300;
	
	
	@Inject
   	private ParamBean paramBean;
	
	@Inject
   	private SolrTreeBean solrTreeBean;


	
	public SolrFilter() {
		filters = new HashMap<String,String>();
		showFilter = false;
		
		filters2 = new ArrayList<ArrayList<String>>();
	}
	
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}

	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}		

	public HashMap<String,String> getFilters(){
		return filters;		
	}
	public void setFilters(HashMap<String,String> val){
		filters = val;	
	}	
	public ArrayList<ArrayList<String>> getFilters2(){
		return filters2;		
	}
	public void setFilters2(ArrayList<ArrayList<String>> val){
		filters2 = val;	
	}	
	
//	public ArrayList<String> getFilterlist(){
//		
//		return filterlist;		
//	}
//	
//	public void setFilterlist(ArrayList<String>  val){
//		filterlist = val;	
//	}

	
	public boolean getShowFilter(){		
		return showFilter;		
	}
	
	public void setShowFilter(boolean  val){
		
		showFilter = val;	
		if (showFilter)
			filterWidth = 500;
		else
			filterWidth = 300;
	}

	
	public int getFilterWidth(){		
		return filterWidth;		
	}
	
	public void setFilterWidth(int  val){
		filterWidth = val;	
	}
	
/************************ set controls *************************************/
	
	public int getInsituCount(){
		
		if (filters != null || !filters.isEmpty())
			return solrTreeBean.getInsituCount(filters);
		else
			return solrTreeBean.getInsituCount();
	}

	
	public String getGeneValue(){
		return geneValue;
	}	
	public void setGeneValue(String val){
		geneValue = val;
	}	
//	public void geneChangeListener(ValueChangeEvent event) {
//	    geneValue = (String)event.getNewValue(); // Look, (new) value is already set.
//	    refresh();
//	}	
//	public void geneListener(AjaxBehaviorEvent event) {
//	    refresh();
//	}	
	
	public Date getFromDateValue(){
		return fromDateValue;
	}	
	public void setFromDateValue(Date val){
		fromDateValue = val;
	}	

	public Date getToDateValue(){
		return toDateValue;
	}	
	public void setToDateValue(Date val){
		toDateValue = val;
	}	
	
	public Map<String,String> getSourceList(){
		
		Map<String,String> sourcemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getSourcelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			sourcemap.put(key, key);
		}
		return sourcemap;
	}
	public ArrayList<String> getSourceValues(){
		return sourceValues;
	}	
	public void setSourceValues(ArrayList<String> val){
		this.sourceValues = val;

	}

	public void sourceChanged(AjaxBehavior event){
//		String sel = (String)event.getNewValue();
		refresh();
	}
	
	public String update(){
		refresh();
		return "";
	}

	public Map<String,String> getSexList(){
		
		Map<String,String> sexmap = new LinkedHashMap<String,String>();

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("female", "female");
		map.put("male", "male");
		map.put("unknown", "unknown");
			
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			sexmap.put(key, key);
		}

		return sexmap;
	}	
		
	public ArrayList<String> getSexValues(){
		return sexValues;
	}	
	public void setSexValues(ArrayList<String> val){
		sexValues = val;
	}	
	
	public Map<String,String> getAssayTypeList(){
		
		Map<String,String> assaytypemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getAllassaytypelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			assaytypemap.put(key, key);

		}

		return assaytypemap;
	}	
	public ArrayList<String> getAssayTypeValues(){
		return assayTypeValues;
	}	
	public  void setAssayTypeValues(ArrayList<String> val){
		 assayTypeValues = val;
	}	

	public Map<String,String> getSpecimenTypeList(){
		
		Map<String,String> specimentypemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getSpecimentypelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
	        specimentypemap.put(key, key);
		}

		return specimentypemap;
	}	
	public ArrayList<String> getSpecimenTypeValues(){
		return specimenTypeValues;
	}	
	public void setSpecimenTypeValues(ArrayList<String> val){
		specimenTypeValues = val;
	}	

	public Map<String,String> getTheilerStageList(){
		
		Map<String,String> theilerstagemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getTheilerstagelist(true); 

		Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			theilerstagemap.put(key, key);
		}

		return theilerstagemap;
	}	
	public ArrayList<String> getTheilerStageValues(){
		return theilerStageValues;
	}	
	public void setTheilerStageValues(ArrayList<String> val){
		theilerStageValues = val;
	}	

	public Map<String,String> getCarnegieStageList(){
		
		Map<String,String> carnegiestagemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		for (int i = 10; i < 24; i++){
			map.put("CS"+i, "CS"+i);
		}
		for (int j = 10; j < 24; j++){
			String s = "Fetal Stage - " + j + "th week post-fertilization";
			map.put(s, s);
		}
//		Map<String, String> map =  paramBean.getCarnegiestagelist(true);

		Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			carnegiestagemap.put(key, key);
		}

		return carnegiestagemap;
	}	
	public ArrayList<String> getCarnegieStageValues(){
		return carnegieStageValues;
	}	
	public void setCarnegieStageValues(ArrayList<String> val){
		carnegieStageValues = val;
	}	

	
	public Map<String,String> getExpressionList(){
		
		Map<String,String> expressionmap = new LinkedHashMap<String,String>();
		expressionmap.put("All", "");
		expressionmap.put("Present", "detected");
		expressionmap.put("Not detected", "undetected");
		expressionmap.put("Uncertain", "uncertain");


		return expressionmap;
	}	

	public String getExpressionValue(){
		return expressionValue;
	}	
	public void setExpressionValue(String val){
		expressionValue = val;
	}	

	public String getAnatomy(){
		return anatomy;
	}	
	public void setAnatomy(String val){
		anatomy = val;
	}	
	
	/************************ refresh and set/rest filters *************************************/
	
	public void refresh(){
		showFilter = !showFilter;
		
		filters2 = new ArrayList<ArrayList<String>>();
		filters = new HashMap<String,String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if (geneValue != null && geneValue != "") {
			String filter = "(" + geneValue + ")";
			filters.put("GENE",filter);			
		}

		if (toDateValue != null) {
			if (fromDateValue != null) {
				String filter = "[" + df.format(fromDateValue) +  " TO " +  df.format(toDateValue) + "]";
				filters.put("DATE",filter);				
			}
			else {
				String filter = "[ * TO " + df.format(toDateValue) + "]";
				filters.put("DATE",filter);				
			}				
		}
		else if (fromDateValue != null) {
			String filter = "[" + df.format(fromDateValue) + " TO * ]";
			filters.put("DATE",filter);				
		}
//		sourceValues = getSourceValues();
		if (sourceValues != null && !sourceValues.isEmpty()) {
			if (sourceValues.size() == 1){
				filters.put("SOURCE",sourceValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : sourceValues) 
					filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("SOURCE",filter);
			}
		}
//		if (sourceValues != null && !sourceValues.isEmpty()) {
//			ArrayList<String> al = new ArrayList<String>();
//			for (String item : sourceValues) 
//				al.add("SOURCE:"+item);
//			filters2.add(al);
//		}

		if (sexValues != null && !sexValues.isEmpty()) {
			if (sexValues.size() == 1){
				filters.put("SEX",sexValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : sexValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("SEX",filter);
			}
		}
		
		if (assayTypeValues != null && !assayTypeValues.isEmpty()) {
			if (assayTypeValues.size() == 1){
				filters.put("ASSAY_TYPE",assayTypeValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : assayTypeValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("ASSAY_TYPE",filter);
			}
		}
		
		if (specimenTypeValues != null && !specimenTypeValues.isEmpty()) {
			if (specimenTypeValues.size() == 1){
				filters.put("SPECIMEN_ASSAY_TYPE",specimenTypeValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : specimenTypeValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("SPECIMEN_ASSAY_TYPE",filter);
			}
		}
		
		if (theilerStageValues != null && !theilerStageValues.isEmpty()) {
			if (theilerStageValues.size() == 1){
				filters.put("STAGE",theilerStageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : theilerStageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("STAGE",filter);
			}
		}

		if (carnegieStageValues != null && !carnegieStageValues.isEmpty()) {
			if (carnegieStageValues.size() == 1){
				filters.put("STAGE",carnegieStageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : carnegieStageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("STAGE",filter);
			}
		}

		if (expressionValue != null && !expressionValue.isEmpty() && anatomy != null && !anatomy.isEmpty()) {
				if (expressionValue.contains("detected"))
					filters.put("PRESENT", anatomy);
				if (expressionValue.contains("undetected"))
					filters.put("NOT_DETECTED", anatomy);
				if (expressionValue.contains("uncertain"))
					filters.put("UNCERTAIN", anatomy);
		}
	}
	
	public void reset(){
		filters2 = new ArrayList<ArrayList<String>>();
		filters = new HashMap<String,String>();
		
		geneValue = "";
		sourceValues = new ArrayList<String>();			
		assayTypeValues = new ArrayList<String>();
		sexValues = new ArrayList<String>();
		specimenTypeValues = new ArrayList<String>();
		theilerStageValues = new ArrayList<String>();
		carnegieStageValues = new ArrayList<String>();
		fromDateValue = null;
		toDateValue = null;		
	}

   
    public String getPage(String component){
    
    	UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
    	List<UIComponent> components = viewRoot.getChildren();
//    	if (components.contains(component))
//    		return ":" + component;
//    	else
//    		return "";
    	
		page = FacesContext.getCurrentInstance().getViewRoot().getViewId();  
		return page;
    }
    
    public void removeFilter(AjaxBehaviorEvent event) {
    	UIComponent source = (UIComponent)event.getSource();
    	String prefix = source.getId();    	
 //   	filters.remove(prefix);
		
    	switch(prefix){
    	case "GENE":
    		geneValue = "";
    		break;    	
    	case "DATE":
    		fromDateValue = null;
    		toDateValue = null;	
    		break;
    	case "SOURCE":
    		sourceValues.clear();
    		break;
    	case "SEX":
    		sexValues.clear();
    		break;
    	case "ASSAY_TYPE":
    		assayTypeValues.clear();
    		break;
    	case "SPECIMEN_ASSAY_TYPE":
    		specimenTypeValues.clear();
    		break;
    	case "THEILER_STAGE":
    		theilerStageValues.clear();
    		break;
    	case "CARNEGIE_STAGE":
    		carnegieStageValues.clear();
    		break;
    	default:
    		break;
    	}
    	
    	filters.clear();
    	refresh();
	}	
    
}
