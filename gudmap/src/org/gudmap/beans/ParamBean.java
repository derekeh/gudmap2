package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.gudmap.assemblers.ParamBeanAssembler;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.utils.Utils;

@Named(value="paramBean")
@SessionScoped
public class ParamBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String focusGroup="reset";
	private boolean isLoggedIn=false;
	private String assayType="ISH";
	/*columns*/
	private boolean oidcol=false;
	private boolean genecol=true;
	private boolean gudmapaccessioncol=true;
	private boolean sourcecol=true;
	private boolean submissiondatecol=true;
	private boolean assaytypecol=true;
	private boolean probenamecol=true;
	private boolean embryostagecol=true;
	private boolean agecol=true;
	private boolean sexcol=false;
	private boolean genotypecol=false;
	private boolean tissuecol=false;
	private boolean expressioncol=false;
	private boolean specimentypecol=false;
	private boolean imagescol=true;
	/*genestrip cols*/
	private boolean synonymcol=true;
	private boolean omimcol=true;
	private boolean rnaseqcol=true;
	private boolean ishexpressioncol=true;
	private boolean arrayexpressioncol=true;
	/*column structures*/
	private String[] insitucols;
	private String[] tgcols;
	private String[] genestripcols;
	private Map<String,Boolean> resultmap;
	private Map<String,Boolean> tgresultmap;
	private Map<String,Boolean> genestripresultmap;
	/*filter*/
	private String genevalues;
	private String[] sourcevalues;
	private Date fromdatevalues;
	private Date todatevalues;
	private String todatemysql;
	private String fromdatemysql;
	private String[] assaytypeinsituvalues;
	private String probenamevalues;
	private String theilerstagefromvalues;
	private String theilerstagetovalues;
	private String sexvalues;
	private String specimentypevalues;	
	private ParamBeanAssembler assembler;
	private String whereclause=" WHERE ";
	private String tempfromvalues;
	private String temptheilervalues;
	
	private boolean[]checkboxes;
	
	SimpleDateFormat sdf;
	
	/*gene search options*/
	private String geneoptionvalues="Expression Summaries";
		
	public ParamBean() {
		//DEFAULT COLUMNS TO SHOW
		insitucols= new String[]{"gene","gudmapaccession","source","submissiondate","assaytype","probename","embryostage","age","images"};
		tgcols= new String[]{"gene","gudmapaccession","source","submissiondate","assaytype","embryostage","age","genotype","images"};
		genestripcols= new String[]{"gene","synonym","omim","stagerange","expressionprofile","images","microarrayprofile","rnaseq"};
		resultmap=new HashMap<String,Boolean>();
		tgresultmap=new HashMap<String,Boolean>();
		genestripresultmap=new HashMap<String,Boolean>();
		assembler = new ParamBeanAssembler();
		sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	}
	
	public void setFocusGroup(String focusGroup){
		this.focusGroup=focusGroup;
	}
	public String getFocusGroup(){
		return focusGroup;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn){
		this.isLoggedIn=isLoggedIn;
	}
	public boolean getIsLoggedIn(){
		return isLoggedIn;
	}
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	public String getAssayType(){
		return assayType;
	}
	
	public void setOidcol(boolean oidcol){
		this.oidcol=oidcol;
	}
	
	public boolean getOidcol(){
		return oidcol;
	}
	
	public void setGudmapaccessioncol(boolean gudmapaccessioncol){
		this.gudmapaccessioncol=gudmapaccessioncol;
	}
	
	public boolean getGudmapaccessioncol(){
		return gudmapaccessioncol;
	}
	
	public void setGenecol(boolean genecol){
		this.genecol=genecol;
	}
	
	public boolean getGenecol(){
		return genecol;
	}
	
	public void setSourcecol(boolean sourcecol){
		this.sourcecol=sourcecol;
	}
	
	public boolean getSourcecol(){
		return sourcecol;
	}
	
	public void setSubmissiondatecol(boolean submissiondatecol){
		this.submissiondatecol=submissiondatecol;
	}
	
	public boolean getSubmissiondatecol(){
		return submissiondatecol;
	}
	
	public void setAssaytypecol(boolean assaytypecol){
		this.assaytypecol=assaytypecol;
	}
	
	public boolean getAssaytypecol(){
		return assaytypecol;
	}
	
	public void setProbenamecol(boolean probenamecol){
		this.probenamecol=probenamecol;
	}
	
	public boolean getProbenamecol(){
		return probenamecol;
	}
	
	public void setEmbryostagecol(boolean embryostagecol){
		this.embryostagecol=embryostagecol;
	}
	
	public boolean getEmbryostagecol(){
		return embryostagecol;
	}
	
	public void setAgecol(boolean agecol){
		this.agecol=agecol;
	}
	
	public boolean getAgecol(){
		return agecol;
	}
	
	public void setSexcol(boolean sexcol){
		this.sexcol=sexcol;
	}
	
	public boolean getSexcol(){
		return sexcol;
	}
	
	public void setGenotypecol(boolean genotypecol){
		this.genotypecol=genotypecol;
	}
	
	public boolean getGenotypecol(){
		return genotypecol;
	}
	
	public void setTissuecol(boolean tissuecol){
		this.tissuecol=tissuecol;
	}
	
	public boolean getTissuecol(){
		return tissuecol;
	}
	
	public void setExpressioncol(boolean expressioncol){
		this.expressioncol=expressioncol;
	}
	
	public boolean getExpressioncol(){
		return expressioncol;
	}
	
	public void setSecimentypecol(boolean specimentypecol){
		this.specimentypecol=specimentypecol;
	}
	
	public boolean getSpecimentypecol(){
		return specimentypecol;
	}
	
	public void setImagescol(boolean imagescol){
		this.imagescol=imagescol;
	}
	
	public boolean getImagescol(){
		return imagescol;
	}
	
	public void setSynonymcol(boolean synonymcol ) {
		this.synonymcol=synonymcol;
	}
	
	public boolean getSynonymcol() {
		return synonymcol;
	}
	
	public void setOmimcol(boolean omimcol ) {
		this.omimcol=omimcol;
	}
	
	public boolean getOmimcol() {
		return omimcol;
	}
	
	public void setRnaseqcol(boolean rnaseqcol ) {
		this.rnaseqcol=rnaseqcol;
	}
	
	public boolean getRnaseqcol() {
		return rnaseqcol;
	}
	public void setIshexpressioncol(boolean ishexpressioncol ) {
		this.ishexpressioncol=ishexpressioncol;
	}
	
	public boolean getIshexpressioncol() {
		return ishexpressioncol;
	}
	
	public void setArrayexpressioncol(boolean arrayexpressioncol ) {
		this.arrayexpressioncol=arrayexpressioncol;
	}
	
	public boolean getArrayexpressioncol() {
		return arrayexpressioncol;
	}
	
	public void setInsitucols(String[]insitucols){
		this.insitucols=insitucols;
		resultmap.clear();
		for(int i=0;i<insitucols.length;i++){
			resultmap.put(insitucols[i], true);	
		}
		oidcol=false;
		genecol=resultmap.containsKey("gene");
		gudmapaccessioncol=resultmap.containsKey("gudmapaccession");
		sourcecol=resultmap.containsKey("source");
		submissiondatecol=resultmap.containsKey("submissiondate");
		assaytypecol=resultmap.containsKey("assaytype");
		probenamecol=resultmap.containsKey("probename");
		embryostagecol=resultmap.containsKey("embryostage");
		agecol=resultmap.containsKey("age");
		sexcol=resultmap.containsKey("sex");
		genotypecol=resultmap.containsKey("genotype");
		tissuecol=resultmap.containsKey("tissue");
		expressioncol=resultmap.containsKey("expression");
		specimentypecol=resultmap.containsKey("specimentype");
		imagescol=resultmap.containsKey("images");
	}
	
	
	public String[] getInsitucols(){
		return insitucols;
	}
	
	
	private static Map<String,Object> insitucolmap;
	static{
		insitucolmap = new LinkedHashMap<String,Object>();
		//insitucolmap.put("Oid", "oid"); //label, value
		insitucolmap.put("Gene", "gene");
		insitucolmap.put("Gudmap Accession", "gudmapaccession");
		insitucolmap.put("Source", "source");
		insitucolmap.put("Submission Date", "submissiondate");
		insitucolmap.put("Assay Type", "assaytype");
		insitucolmap.put("Probe Name", "probename");
		insitucolmap.put("Theiler Stage", "embryostage");
		insitucolmap.put("Age", "age");
		insitucolmap.put("Sex", "sex");
		insitucolmap.put("Genotype", "genotype");
		insitucolmap.put("Tissue", "tissue");
		insitucolmap.put("Expression", "expression");
		insitucolmap.put("Specimen Type", "specimentype");
		insitucolmap.put("Images", "images");
	}
 
	public Map<String,Object> getInsitucolmap() {
		return insitucolmap;
	}
 
	public String getInsitucolsInString() {
		return Arrays.toString(insitucols);
	}
	
	public void setTgcols(String[]tgcols){
		this.tgcols=tgcols;
		tgresultmap.clear();
		for(int i=0;i<tgcols.length;i++){
			tgresultmap.put(tgcols[i], true);	
		}
		oidcol=false;
		genecol=tgresultmap.containsKey("gene");
		gudmapaccessioncol=tgresultmap.containsKey("gudmapaccession");
		sourcecol=tgresultmap.containsKey("source");
		submissiondatecol=tgresultmap.containsKey("submissiondate");
		assaytypecol=tgresultmap.containsKey("assaytype");
		//probenamecol=tgresultmap.containsKey("probename");
		embryostagecol=tgresultmap.containsKey("embryostage");
		agecol=tgresultmap.containsKey("age");
		sexcol=tgresultmap.containsKey("sex");
		genotypecol=tgresultmap.containsKey("genotype");
		tissuecol=tgresultmap.containsKey("tissue");
		expressioncol=tgresultmap.containsKey("expression");
		specimentypecol=tgresultmap.containsKey("specimentype");
		imagescol=tgresultmap.containsKey("images");
	}
	
	
	public String[] getTgcols(){
		return tgcols;
	}
	
	private static Map<String,Object> tgcolmap;
	static{
		tgcolmap = new LinkedHashMap<String,Object>();
		//tgcolmap.put("Oid", "oid"); //label, value
		tgcolmap.put("Gene", "gene");
		tgcolmap.put("Gudmap Accession", "gudmapaccession");
		tgcolmap.put("Source", "source");
		tgcolmap.put("Submission Date", "submissiondate");
		tgcolmap.put("Assay Type", "assaytype");
		//tgcolmap.put("Probe Name", "probename");
		tgcolmap.put("Theiler Stage", "embryostage");
		tgcolmap.put("Age", "age");
		tgcolmap.put("Sex", "sex");
		tgcolmap.put("Genotype", "genotype");
		tgcolmap.put("Tissue", "tissue");
		tgcolmap.put("Expression", "expression");
		tgcolmap.put("Specimen Type", "specimentype");
		tgcolmap.put("Images", "images");
	}
 
	public Map<String,Object> getTgcolmap() {
		return tgcolmap;
	}
	
	public void setGenestripcols(String[]genestripcols){
		this.genestripcols=genestripcols;
		genestripresultmap.clear();
		for(int i=0;i<genestripcols.length;i++){
			genestripresultmap.put(genestripcols[i], true);	
		}
		oidcol=false;
		genecol=genestripresultmap.containsKey("gene");
		synonymcol=genestripresultmap.containsKey("synonym");
		omimcol=genestripresultmap.containsKey("omim");
		embryostagecol=genestripresultmap.containsKey("stagerange");
		ishexpressioncol=genestripresultmap.containsKey("expressionprofile");
		imagescol=genestripresultmap.containsKey("images");
		arrayexpressioncol=genestripresultmap.containsKey("microarrayprofile");
		rnaseqcol=genestripresultmap.containsKey("rnaseq");
	}
	
	
	public String[] getGenestripcols(){
		return genestripcols;
	}
	
	
	private static Map<String,Object> genestripcolmap;
	static{
		genestripcolmap = new LinkedHashMap<String,Object>();
		//insitucolmap.put("Oid", "oid"); //label, value
		genestripcolmap.put("Gene", "gene");
		genestripcolmap.put("Synonyms", "synonym");
		genestripcolmap.put("Disease", "omim");
		genestripcolmap.put("Theiler Stage", "stagerange");
		genestripcolmap.put("Expression Profile", "expressionprofile");
		genestripcolmap.put("Expression Images", "images");
		genestripcolmap.put("Microarray Expression Profile", "microarrayprofile");
		genestripcolmap.put("RNA-SEQ", "rnaseq");
	}
 
	public Map<String,Object> getGenestripcolmap() {
		return genestripcolmap;
	}
	
	/*************************FILTER*******************************/
	
	/*************************populate filter*******************/
	
	public Map<String,String> getSourcelist(){
		return assembler.getSourcelist(); 
	}
	
	public Map<String,String> getAssaytypeinsitulist(){
		return assembler.getAssaytypeinsitulist();
	}
	
	public Map<String,String> getTheilerstagelist(){
		return assembler.getTheilerstagelist();
	}
	
	public Map<String,String> getSexlist(){
		return assembler.getSexlist();
	}
	
	public Map<String,String> getSpecimentypelist(){
		return assembler.getSpecimentypelist();
	}
	
	/**gene search options***/
	public Map<String,String> getGeneoptionlist(){
		return assembler.getGeneoptionlist();
	}

/*******************************
 * getters and setters for filter outcomes 
 * CHECK FOR NULL VALUE WHEN THE COMPONENT IN FILTER VIEW MIGHT BE COMMENTED OUT
 * ************************/
	
	public void setSourcevalues(String[] sourcevalues){
		this.sourcevalues=sourcevalues;
		if(sourcevalues!=null){
			if(sourcevalues.length>0){
				String str="";
				for(int i=0;i<sourcevalues.length;i++){
					str+="'"+sourcevalues[i]+"',";
				}
				whereclause+="SUB_SOURCE IN ("+Utils.removeLastChar(str, ',')+") AND ";
			}
		}
	}
	public String[] getSourcevalues(){
		return sourcevalues;
	}
	
	public String getSourcevaluesInString() {
		return Arrays.toString(sourcevalues);
	}
	
	public void setFromdatevalues(Date fromdatevalues){
		if(fromdatevalues!=null && !fromdatevalues.equals(""))
		{
			this.fromdatevalues=fromdatevalues;
			fromdatemysql = sdf.format(fromdatevalues);
			tempfromvalues="SUB_SUB_DATE BETWEEN '"+fromdatemysql+"' AND '";
		}
	}
	
	public Date getFromdatevalues(){
		return fromdatevalues;
	}
	
	public String getFromdatemysql() {
		return fromdatemysql;
	}
	
	public void setTodatevalues(Date todatevalues){
		if(todatevalues!=null && !todatevalues.equals(""))
		{
			this.todatevalues=todatevalues;
			todatemysql = sdf.format(todatevalues);
			if(getFromdatevalues()!=null && !getFromdatevalues().equals("")){
				tempfromvalues+=todatemysql+"'";
				whereclause+=tempfromvalues+" AND ";
			}
		}
	}
	
	public Date getTodatevalues(){
		return todatevalues;
	}
	
	public String getTodatemysql() {
		return todatemysql;
	}
	
	public void setAssaytypeinsituvalues(String[] assaytypeinsituvalues){
		this.assaytypeinsituvalues=assaytypeinsituvalues;
		if(assaytypeinsituvalues!=null){
			if(assaytypeinsituvalues.length>0){
				String str="";
				for(int i=0;i<assaytypeinsituvalues.length;i++){
					str+="'"+assaytypeinsituvalues[i]+"',";
				}
				whereclause+="SUB_ASSAY_TYPE IN ("+Utils.removeLastChar(str, ',')+") AND ";
			}
		}
		
	}
	public String[] getAssaytypeinsituvalues(){
		return assaytypeinsituvalues;
	}
	
	public String getAssaytypeinsituvaluesInString() {
		return Arrays.toString(assaytypeinsituvalues);
	}
	
	public void setTheilerstagefromvalues(String theilerstagefromvalues){
		this.theilerstagefromvalues=theilerstagefromvalues;
		if(theilerstagefromvalues!=null){
			if(!theilerstagefromvalues.equals("ALL")){
				temptheilervalues="SUB_EMBRYO_STG BETWEEN "+theilerstagefromvalues+" AND ";
			}
		}
	}
	public String getTheilerstagefromvalues(){
		return theilerstagefromvalues;
	}
	
	public void setTheilerstagetovalues(String theilerstagetovalues){
		this.theilerstagetovalues=theilerstagetovalues;
		if(theilerstagetovalues!=null){
			if(getTheilerstagefromvalues()!=null && !getTheilerstagefromvalues().equals("") && !getTheilerstagefromvalues().equals("ALL") && !theilerstagetovalues.equals("ALL")){
				temptheilervalues+=theilerstagetovalues;
				whereclause+=temptheilervalues+" AND ";
			}
		}
	}
	
	public String getTheilerstagetovalues(){
		return theilerstagetovalues;
	}
	
	public void setSexvalues(String sexvalues){
		this.sexvalues=sexvalues;
		if(!sexvalues.equals(""))
				whereclause+="SPN_SEX = '"+sexvalues+"' AND ";
	}
	
	public String getSexvalues(){
		return sexvalues;
	}
	
	public void setSpecimentypevalues(String specimentypevalues){
		this.specimentypevalues=specimentypevalues;
		if(!specimentypevalues.equals(""))
			whereclause+="SPN_ASSAY_TYPE = '"+specimentypevalues+"' AND ";
	}
	
	public String getSpecimentypevalues(){
		return specimentypevalues;
	}
	
	public void setGenevalues(String genevalues){
		this.genevalues=genevalues;
		if(!genevalues.equals(""))
			whereclause+="RPR_SYMBOL = '"+genevalues+"' AND ";
	}
	
	public String getGenevalues(){
		return genevalues;
	}
	
	public void setProbenamevalues(String probenamevalues){
		this.probenamevalues=probenamevalues;
		if(!probenamevalues.equals(""))
			whereclause+="RPR_JAX_ACC = '"+probenamevalues+"' AND ";
	}
	
	public String getProbenamevalues(){
		return probenamevalues;
	}
	
	/*****gene search options**********/
	public void setGeneoptionvalues(String geneoptionvalues){
		this.geneoptionvalues = geneoptionvalues;
	}
	
	public String getGeneoptionvalues() {
		return geneoptionvalues;
	}
	
	/*****************today*******************/
	public Date getMaxDate() {
	    // wherever you want the date to come from
	    return new Date();
	 }
	
	/****************where clause**************/
	
	public void setWhereclause(String whereclause){
		this.whereclause=whereclause;
	}
	
	public String getWhereclause(){
		return whereclause;
	}
	 
	/******************reset*******************/
	
	public void resetValues(){
		fromdatevalues=null;
		todatevalues=null;
		setSourcevalues(new String[0]);
		todatemysql="";
		fromdatemysql="";
		setAssaytypeinsituvalues(new String[0]);
		setProbenamevalues("");
		setTheilerstagefromvalues("");
		setTheilerstagetovalues("");
		setSexvalues("");
		setSpecimentypevalues("");
		tempfromvalues="";
		//setWhereclause(GenericQueries.WHERE_CLAUSE);
	}
	
	public String resetGeneSearchValues() {
		setTheilerstagefromvalues("");
		setTheilerstagetovalues("");
		//setGeneoptionvalues("Expression Summaries");
		return "database_homepage";
	}
	
	public String resetAllGeneSearchValues() {
		String RET=resetGeneSearchValues();
		setGeneoptionvalues("Expression Summaries");
		setWhereclause(GenericQueries.WHERE_CLAUSE);
		return RET;
	}
	
	public String geneSearchRedirect() {
		 if(getGeneoptionvalues().equals("Expression Summaries"))
			 return "viewGeneStrip";
		 else
			 return "browseGeneListTablePage";
	 }
	
	public void resetAll() {
		resetValues();
		setWhereclause(GenericQueries.WHERE_CLAUSE);
		//focusGroup="reset";
	}

	/*******************focus groups****************/
	public void focusGroupAction(ActionEvent event){
		String focusFieldAttribute = (String) event.getComponent().getAttributes().get("focusField");
			this.setFocusGroup(focusFieldAttribute);
	}
	
	//focus group for ish gets anatomy ID from ISH_EXPRESSION
	public String getFocusGroupWhereclause(){
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else if(focusGroup.equals("Metanephros"))
			RET =  GenericQueries.FOCUS_METANEPHROS;
		else if(focusGroup.equals("Lower urinary tract"))
			RET =  GenericQueries.FOCUS_URINARY;
		else if(focusGroup.equals("Early reproductive system"))
			RET =  GenericQueries.FOCUS_EARLY_REPRO;
		else if(focusGroup.equals("Male reproductive system"))
			RET =  GenericQueries.FOCUS_MALE_REPRO;
		else if(focusGroup.equals("Female reproductive system"))
			RET =  GenericQueries.FOCUS_FEMALE_REPRO;
		
		return RET;
	}
	
	//focus group for non-ish gets anatomy ID from ISH_SP_TISSUE
	public String getFocusGroupSpWhereclause(){
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else if(focusGroup.equals("Metanephros"))
			RET =  GenericQueries.FOCUS_METANEPHROS_SP;
		else if(focusGroup.equals("Lower urinary tract"))
			RET =  GenericQueries.FOCUS_URINARY_SP;
		else if(focusGroup.equals("Early reproductive system"))
			RET =  GenericQueries.FOCUS_EARLY_REPRO_SP;
		else if(focusGroup.equals("Male reproductive system"))
			RET =  GenericQueries.FOCUS_MALE_REPRO_SP;
		else if(focusGroup.equals("Female reproductive system"))
			RET =  GenericQueries.FOCUS_FEMALE_REPRO_SP;
		
		return RET;
	}
	
	public String getExpressionJoin() {
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else
			RET =  GenericQueries.EXPRESSION_JOIN;
		
		return RET;
	}
	
	/**********TODO **********checkboxes keep this code ******************/
	
	/*public void setCheckboxes(boolean[]checkboxes){
		this.checkboxes=checkboxes;
	}
	
	public boolean[] getCheckboxes(){
		return checkboxes;
	}*/
	
	
	
}
