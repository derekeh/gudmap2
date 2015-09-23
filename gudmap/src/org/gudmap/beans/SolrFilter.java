package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
//import javax.faces.event.ValueChangeEvent;
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
import java.util.Map;
import java.util.Map.Entry;

import org.primefaces.context.RequestContext;

@Named(value="solrFilter")
@SessionScoped
public class SolrFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String,String> filters;	
	
	private String geneValue;
	private Date fromDateValue;
	private Date toDateValue;
	private ArrayList<String> sexValues;	
	private ArrayList<String> assayTypeValues;	
	private ArrayList<String> sourceValues;	
	private ArrayList<String> specimenTypeValues;
	private ArrayList<String> theilerStageValues;
	private ArrayList<String> carnegieStageValues;
	
	private String page;
	
	@Inject
   	private ParamBean paramBean;
	
	@Inject
   	private SolrTreeBean solrTreeBean;

	
	public SolrFilter() {
		filters = new HashMap<String,String>();
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

/************************ set controls *************************************/
	
	public int getInsituCount(){
		
		if (filters != null || !filters.isEmpty())
			return solrTreeBean.getInsituFilteredCount(filters);
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

//	public void sourceChangeValues(ValueChangeEvent event){
//		String sel = (String)event.getNewValue();
//		sourceCB = false;
//		refresh();
//	}
	
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

		Map<String, String> map =  paramBean.getAssaytypeinsitulist(); 
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
		
		Map<String,String> assaytypemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getSpecimentypelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			assaytypemap.put(key, key);
		}

		return assaytypemap;
	}	
	public ArrayList<String> getSpecimenTypeValues(){
		return specimenTypeValues;
	}	
	public void setSpecimenTypeValues(ArrayList<String> val){
		specimenTypeValues = val;
	}	

	public Map<String,String> getTheilerStageList(){
		
		Map<String,String> theilerstagemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		for (int i = 17; i < 29; i++){
			map.put("TS"+i, "TS"+i);
		}

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
	
	/************************ refresh and set/rest filters *************************************/
	
	public void refresh(){
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
				filters.put("THEILER_STAGE",theilerStageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : theilerStageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("THEILER_STAGE",filter);
			}
		}

		if (carnegieStageValues != null && !carnegieStageValues.isEmpty()) {
			if (carnegieStageValues.size() == 1){
				filters.put("CARNEGIE_STAGE",carnegieStageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : carnegieStageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("CARNEGIE_STAGE",filter);
			}
		}
	
	}
	
	public void reset(){
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

	public void refresh2(){
		
		RequestContext context = RequestContext.getCurrentInstance();
		page = FacesContext.getCurrentInstance().getViewRoot().getViewId(); 
		
		if (page.equalsIgnoreCase("/solr/solrInsitu.xhtml")){
			context.execute("PF('dialogSolrInsituFilter').show();");
		}
			
		if (page.equalsIgnoreCase("/db/database_homepage.xhtml")){
			context.execute("PF('dialogSolrSearch').show();");
			context.execute("PF('dialogSolrInsituFilter').show();");
		}
			
//		context.update("solrsearchform");
//		context.update(":solrinsitu_dataform");
	};
   
    public String getPage(String component){
    
//    	UIViewRoot viewRoot =  FacesContext.getCurrentInstance().getViewRoot();
//    	List<UIComponent> components = viewRoot.getChildren();
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
    	filters.remove(prefix);
	}	
}

