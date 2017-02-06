package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.queries.solr.SolrQueries;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <h1>SolrFilter</h1>
 * The SolrFilter class contains the methods to initialise and update the search filter used
 * by the search facility
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2016 
 */
@Named(value="solrFilter")
@SessionScoped
public class SolrFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String,String> filters;	
		
	private String geneValue;
	private Date fromDateValue;
	private Date toDateValue;
	private ArrayList<String> geneValues;
	private ArrayList<String> speciesValues;
	private ArrayList<String> sexValues;	
	private ArrayList<String> assayTypeValues;	
	private ArrayList<String> sourceValues;	
	private ArrayList<String> specimenTypeValues;
	private ArrayList<String> theilerStageValues;
	private ArrayList<String> carnegieStageValues;
	private ArrayList<String> ageValues;
	private ArrayList<String> imageValues;
	private String expressionValue = "";
	private String anatomy;
	private String page;
	private boolean showFilter = false;
	private int filterWidth = 300;
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	@Inject
   	private ParamBean paramBean;
	
	@Inject
   	private SolrTreeBean solrTreeBean;


	
	public SolrFilter() {
		filters = new HashMap<String,String>();
		showFilter = false;

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
	
	/**
	 * This method returns the start date from the Submission Date Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return The fromDateValue
	 */
	public Date getFromDateValue(){
		return fromDateValue;
	}	
	/**
	 * This method sets the current start date to be displayed in the Submission Date Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setFromDateValue(Date val){
		fromDateValue = val;
	}	

	/**
	 * This method returns the end date from the Submission Date Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return The toDateValue
	 */
	public Date getToDateValue(){
		return toDateValue;
	}	
	/**
	 * This method sets the current end date to be displayed in the Submission Date Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setToDateValue(Date val){
		toDateValue = val;
	}	
	
	/**
	 * This method returns a map of sources to be displayed in the Sources Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of sources
	 */
	public Map<String,String> getSourceList(){
		
		Map<String,String> sourcemap = new LinkedHashMap<String,String>();
		sourcemap.put("---clear---", "clear");

		Map<String, String> map =  paramBean.getSourcelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
			sourcemap.put(key, key);
		}
		return sourcemap;
	}
	/**
	 * This method returns the current list of selected sources from the Sources Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The sourceValues
	 */
	public ArrayList<String> getSourceValues(){
		return sourceValues;
	}	
	/**
	 * This method sets the current list of sources to be displayed in the Sources Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setSourceValues(ArrayList<String> val){
		if (val.contains("clear"))
			this.sourceValues.clear();
		else
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

	/**
	 * This method returns a map of gene types to be displayed in the Genes Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of gene types
	 */
	public Map<String,String> getGeneList(){

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("Anchor", "anchor");
		map.put("Marker", "marker");
		return map;
	}			
	/**
	 * This method returns the current list of selected genes types from the Genes Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The geneValues
	 */
	public ArrayList<String> getGeneValues(){
		return geneValues;
	}	
	/**
	 * This method sets the current list of selected gene types to be displayed in
	 * the Genes Filter of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setGeneValues(ArrayList<String> val){
		geneValues = val;
	}	
	
	/**
	 * This method returns a map of species to be displayed in the Species Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of species
	 */
	public Map<String,String> getSpeciesList(){

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("Human", "Homo sapiens");
		map.put("Mouse", "Mus musculus");
		return map;
	}	
	/**
	 * This method returns the current list of selected species from the Species Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The speciesValues
	 */
	public ArrayList<String> getSpeciesValues(){
		return speciesValues;
	}	
	/**
	 * This method sets the current list of species to be displayed in the Species Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setSpeciesValues(ArrayList<String> val){
		speciesValues = val;
	}	
	
	/**
	 * This method returns a map of sexes to be displayed in the Sex Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of sexes
	 */
	public Map<String,String> getSexList(){

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("Female", "female");
		map.put("Male", "male");
		map.put("Unknown", "unknown");
		return map;
	}			
	/**
	 * This method returns the current list of selected sexes from the Sex Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The sexValues
	 */
	public ArrayList<String> getSexValues(){
		return sexValues;
	}	
	/**
	 * This method sets the current list of sexes to be displayed in the Sex Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setSexValues(ArrayList<String> val){
		sexValues = val;
	}	
	
	/**
	 * This method returns a map of assay types to be displayed in the Assay Types Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of assay types
	 */
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
	/**
	 * This method returns the current list of selected assay types from the Assay Types Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The assayTypeValues
	 */
	public ArrayList<String> getAssayTypeValues(){
		return assayTypeValues;
	}	
	/**
	 * This method sets the current list of assay types to be displayed in the Assay Types Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public  void setAssayTypeValues(ArrayList<String> val){
		 assayTypeValues = val;
	}	

	/**
	 * This method returns a map of specimen types to be displayed in the Specimen Types Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of specimen types
	 */
	public Map<String,String> getSpecimenTypeList(){
		
		Map<String,String> specimentypemap = new LinkedHashMap<String,String>();

		Map<String, String> map =  paramBean.getSpecimentypelist(); 
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        String key = (String)pair.getKey();
	        String val = (String)pair.getKey();	        
	        if (key.contains("mouse marker strain"))
	        	val = '"' + val + '"';
	        specimentypemap.put(key, val);
		}

		return specimentypemap;
	}	
	/**
	 * This method returns the current list of selected specimen types from the Specimen Types Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The specimenTypeValues
	 */
	public ArrayList<String> getSpecimenTypeValues(){
		return specimenTypeValues;
	}	
	/**
	 * This method sets the current list of specimen types to be displayed in the Specimen Types Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setSpecimenTypeValues(ArrayList<String> val){
		specimenTypeValues = val;
	}	

	/**
	 * This method returns a map of theiler stages to be displayed in the Theiler Stages Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of theiler stages
	 */
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
	/**
	 * This method returns the current list of selected theiler stages from the Theiler Stage Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The theilerStageValues
	 */
	public ArrayList<String> getTheilerStageValues(){
		return theilerStageValues;
	}	
	/**
	 * This method sets the current list of theiler stages to be displayed in the Theiler Stage Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setTheilerStageValues(ArrayList<String> val){
		theilerStageValues = val;
	}	

	/**
	 * This method returns a map of ages to be displayed in the Ages Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of ages
	 */
	public Map<String,String> getAgeList(){
		
		Map<String,String> agemap = new LinkedHashMap<String,String>();
		agemap.put("10.5 dpc", "10.5dpc");
		agemap.put("11.5 dpc", "11.5dp");
		agemap.put("12.5 dpc", "12.5dpc");
		agemap.put("13.5 dpc", "13.5dpc");
		agemap.put("14.5 dpc", "14.5dpc");
		agemap.put("15.5 dpc", "15.5dpc");
		agemap.put("16.5 dpc", "16.5dpc");
		agemap.put("17.5 dpc", "17.5dpc");
		agemap.put("18.5 dpc", "18.5dpc");
		agemap.put("Newborn (range P0-P3)", "[P0 TO P3]");
		agemap.put("Range P4-Adult", "[P4 TO P200] OR Adult");
		return agemap;
	}	
	
	/**
	 * This method returns the current list of selected ages from the Age Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The ageValues
	 */
	public ArrayList<String> getAgeValues(){
		return ageValues;
	}	
	/**
	 * This method sets the current list of ages to be displayed in the Age Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setAgeValues(ArrayList<String> val){
		ageValues = val;
	}	
	
	/**
	 * This method returns a map of carnegie stages to be displayed in the Human Stages Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of carnegie stages
	 */
	public Map<String,String> getCarnegieStageList(){
		
		Map<String,String> carnegiestagemap = new LinkedHashMap<String,String>();


		String queryString=SolrQueries.CARNEGIE_STAGES;
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while(result.next()){
				String key = result.getString(1);
				String val = result.getString(1);
				if (key.contains("Fetal")){
			        val = '"' + val + '"';					
				}
				carnegiestagemap.put(key, val);
			}

		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return carnegiestagemap;
	}	
	/**
	 * This method returns the current list of selected carnegie stages from the Carnegie Stage Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The carnegieStageValues
	 */
	public ArrayList<String> getCarnegieStageValues(){
		return carnegieStageValues;
	}	
	/**
	 * This method sets the current list of carnegie stages to be displayed in the Carnegie Stage Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setCarnegieStageValues(ArrayList<String> val){
		carnegieStageValues = val;
	}	

	/**
	 * This method returns a map of image types to be displayed in the Image Type Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of image types
	 */
	public Map<String,String> getImageList(){

		Map<String, String> map =  new LinkedHashMap<String, String>(); 
		map.put("Image", "image");
		map.put("Schematic", "schematic");

		return map;
	}			
	/**
	 * This method returns the current list of selected image types from the Image Type Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @return The imageValues
	 */
	public ArrayList<String> getImageValues(){
		return imageValues;
	}	
	/**
	 * This method sets the current list of image types to be displayed in the Image Type Filter 
	 * of the Advanced Search Pages.
	 * 
	 * @param val
	 */
	public void setImageValues(ArrayList<String> val){
		imageValues = val;
	}	
	
	/**
	 * This method returns a map of expression types to be displayed in the Expression Filter 
	 * of the Advanced Search Pages
	 * 
	 * @return A Map of expression types
	 */
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
	
	/**
	 * This method updates the Map of filters used by the Advanced Search
	 */
	public void refresh(){
		showFilter = !showFilter;
		
//		filters2 = new ArrayList<ArrayList<String>>();
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

		if (geneValues != null && !geneValues.isEmpty()) {
			if (geneValues.size() == 1){
				filters.put("GENE_TYPE",geneValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : geneValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("GENE_TYPE",filter);
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

		if (ageValues != null && !ageValues.isEmpty()) {
			if (ageValues.size() == 1){
				filters.put("DEV_STAGE",ageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : ageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("DEV_STAGE",filter);
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

		if (speciesValues != null && !speciesValues.isEmpty()) {
			if (speciesValues.size() == 1){
				filters.put("SPECIES",speciesValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : speciesValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("SPECIES",filter);
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
		
		if (imageValues != null && !imageValues.isEmpty()) {
			if (imageValues.size() == 1){
				filters.put("IMAGE_TYPE",imageValues.get(0));
			}
			else {
				String filter = "(";
				for (String item : imageValues) filter += item + " OR ";
				filter = filter.substring(0, filter.length()-3) + ")";
				filters.put("IMAGE_TYPE",filter);
			}
		}
		
	}
	
	/**
	 * This method resets the Map of filters, and all individual filters used by the Advanced Search
	 */
	public void reset(){
		filters = new HashMap<String,String>();
		
		geneValue = "";
		geneValues = new ArrayList<String>();			
		sourceValues = new ArrayList<String>();			
		assayTypeValues = new ArrayList<String>();
		speciesValues = new ArrayList<String>();
		sexValues = new ArrayList<String>();
		imageValues = new ArrayList<String>();
		specimenTypeValues = new ArrayList<String>();
		theilerStageValues = new ArrayList<String>();
		carnegieStageValues = new ArrayList<String>();
		ageValues = new ArrayList<String>();
		fromDateValue = null;
		toDateValue = null;		
		
    	refresh();
	}

   
    public String getPage(String component){
		page = FacesContext.getCurrentInstance().getViewRoot().getViewId();  
		return page;
    }
    
//    public void removeFilter(AjaxBehaviorEvent event) {
//    	UIComponent source = (UIComponent)event.getSource();
//    	String prefix = source.getId();    	
// 		
//    	switch(prefix){
//    	case "GENE":
//    		geneValue = "";
//    		break;    	
//    	case "DATE":
//    		fromDateValue = null;
//    		toDateValue = null;	
//    		break;
//    	case "SOURCE":
//    		sourceValues.clear();
//    		break;
//    	case "SPECIES":
//    		speciesValues.clear();
//    		break;
//    	case "SEX":
//    		sexValues.clear();
//    		break;
//    	case "ASSAY_TYPE":
//    		assayTypeValues.clear();
//    		break;
//    	case "SPECIMEN_ASSAY_TYPE":
//    		specimenTypeValues.clear();
//    		break;
//    	case "THEILER_STAGE":
//    		theilerStageValues.clear();
//    		break;
//    	case "CARNEGIE_STAGE":
//    		carnegieStageValues.clear();
//    		break;
//    	case "ANCHOR":
//    		geneValues.clear();
//    		break;
//    	case "MARKER":
//    		geneValues.clear();
//    		break;
//    	default:
//    		break;
//    	}
//    	
//    	filters.clear();
//    	refresh();
//	}	

}