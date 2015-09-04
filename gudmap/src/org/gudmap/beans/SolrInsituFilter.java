package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Named(value="solrInsituFilter")
@SessionScoped
public class SolrInsituFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> filters;
	
	
	private String geneValue;
	private Date fromDateValue;
	private String fromDateString;
	private Date toDateValue;
	private String toDateString;
	private ArrayList<String> sexValues;	
	private ArrayList<String> assayTypeValues;	
	private ArrayList<String> personValues;
	private ArrayList<String> sourceValues;	
	private ArrayList<String> specimenTypeValues;
	private ArrayList<String> theilerStageValues;
	private ArrayList<String> carnegieStageValues;
	
	private String filterstring;
	private String page;
	
	@Inject
   	private ParamBean paramBean;
	
	@Inject
   	private SolrTreeBean solrTreeBean;

	
	public SolrInsituFilter() {
		filters = new ArrayList<String>();
	}
	
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}

	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}		

	public ArrayList<String> getFilters(){
		return filters;		
	}
	public void setFilters(ArrayList<String> val){
		filters = val;	
	}
   

/************************ set controls *************************************/
	
	public String getFilterString(){
		filterstring = "";
		int index;
		String item;
		if (filters != null && !filters.isEmpty()){
			for (String filter :filters){
				index = filter.indexOf(":");
				item = filter.substring(index+2,filter.length()-1);
				filterstring += item + ",";
			}
			filterstring = filterstring.substring(0, filterstring.length() - 1);
		}
		return filterstring;
	}

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
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();
	        
	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("SOURCE:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);

	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
			String title = key + "(" + count + ")";
			sourcemap.put(title, key);
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
////		String sel = (String)event.getNewValue();
////		sourceCB = false;
//		refresh();
//	}
	
	public String update(){
		refresh();
		return "";
	}

/*
	public Map<String,String> getPersonList(){
		
		Map<String,String> pimap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getPilist(); 
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();
	        
	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("PI_NAME:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
			String title = key + "(" + count + ")";
			pimap.put(title, key);
		}

		return pimap;
	}	
	public ArrayList<String> getPersonValues(){
		return personValues;
	}	
	public void getPersonValues(ArrayList<String> val){
		personValues = val;
	}	
*/
	public Map<String,String> getSexList(){
		
		Map<String,String> sexmap = new LinkedHashMap<String,String>();

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("female", "female");
		map.put("male", "male");
		map.put("unknown", "unknown");
		
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();

	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("SEX:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
			String title = key + "(" + count + ")";
			sexmap.put(title, key);
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
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();

	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("ASSAY_TYPE:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
//	        if (count > 0){
				String title = key + "(" + count + ")";
				assaytypemap.put(title, key);
//	        }
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
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();

	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("SPECIMEN_ASSAY_TYPE:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
//	        if (count > 0){
				String title = key + "(" + count + ")";
				assaytypemap.put(title, key);
//	        }
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

		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();

	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("THEILER_STAGE:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
//	        if (count > 0){
				String title = key + "(" + count + ")";
				theilerstagemap.put(title, key);
//	        }
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

		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        String key = (String)pair.getKey();

	        ArrayList<String> countfilter = new ArrayList<String>();
	        countfilter.add("CARNEGIE_STAGE:" + key);
	        
	        if (filters != null && !filters.isEmpty())
	        	countfilter.addAll(filters);
	        
	        int count = solrTreeBean.getInsituFilteredCount(countfilter);
//	        if (count > 0){
				String title = key + "(" + count + ")";
				carnegiestagemap.put(title, key);
//	        }
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
		filters = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		if (geneValue != null && geneValue != "") {
			String filter = "GENE:(" + geneValue + ")";
			filters.add(filter);
		}

		if (toDateValue != null) {
			if (fromDateValue != null) {
				String filter = "DATE:[" + df.format(fromDateValue) +  " TO " +  df.format(toDateValue) + "]";
				filters.add(filter);
			}
			else {
				String filter = "DATE:[ * TO " + df.format(toDateValue) + "]";
				filters.add(filter);
			}				
		}
		else if (fromDateValue != null) {
			String filter = "DATE:[" + df.format(fromDateValue) + " TO * ]";
			filters.add(filter);
		}
		
		if (sourceValues != null && !sourceValues.isEmpty()) {
			String filter = "SOURCE:(";
			for (String item : sourceValues) filter += item + " OR ";
			filter = filter.substring(0, filter.length()-3) + ")";
			filters.add(filter);
		}

		if (sexValues != null && !sexValues.isEmpty()) {
			String filter = "SEX:(";
			for (String item : sexValues) filter += item + " OR ";
			filter = filter.substring(0, filter.length()-3) + ")";
			filters.add(filter);
		}
		
		if (assayTypeValues != null && !assayTypeValues.isEmpty()) {
			String filter = "ASSAY_TYPE:(";
			for (String item : assayTypeValues) filter += item + " OR ";
			filter = filter.substring(0, filter.length()-3) + ")";
			filters.add(filter);
		}
		
		if (specimenTypeValues != null && !specimenTypeValues.isEmpty()) {
			String filter = "SPECIMEN_ASSAY_TYPE:(";
			for (String item : specimenTypeValues) filter += item + " OR ";
			filter = filter.substring(0, filter.length()-3) + ")";
			filters.add(filter);
		}
		
		if (theilerStageValues != null && !theilerStageValues.isEmpty()) {
			String filter = "THEILER_STAGE:(";
			for (String item : theilerStageValues) filter += item + " OR ";
			filter = filter.substring(0, filter.length()-3) + ")";
			filters.add(filter);
		}
	}

	public void submit(){
		refresh();
	}
	
	public void reset(){
		filters = new ArrayList<String>();
		
		geneValue = "";
		sourceValues = new ArrayList<String>();			
		assayTypeValues = new ArrayList<String>();
		sexValues = new ArrayList<String>();
		specimenTypeValues = new ArrayList<String>();
		theilerStageValues = new ArrayList<String>();
		fromDateValue = null;
		toDateValue = null;		
	}

	public void resetAll(){
		filters = new ArrayList<String>();
		
		geneValue = "";
		sourceValues = new ArrayList<String>();			
		assayTypeValues = new ArrayList<String>();
		sexValues = new ArrayList<String>();
		specimenTypeValues = new ArrayList<String>();
		theilerStageValues = new ArrayList<String>();
		fromDateValue = null;
		toDateValue = null;		

	}

    
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
	
}

