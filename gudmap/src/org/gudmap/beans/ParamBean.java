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

import org.gudmap.beans.assemblers.ParamBeanAssembler;
import org.gudmap.beans.utils.Utils;

@Named(value="paramBean")
@SessionScoped
public class ParamBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String focusGroup="reset";
	private boolean isLoggedIn=false;
	private String assayType="ISH";
	/*columns*/
	private boolean oidcol=true;
	private boolean genecol=true;
	private boolean gudmapaccessioncol=true;
	private boolean sourcecol=true;
	private boolean submissiondatecol=true;
	private boolean assaytypecol=true;
	private boolean probenamecol=true;
	private boolean embryostagecol=true;
	private boolean agecol=false;
	private boolean sexcol=false;
	private boolean genotypecol=false;
	private boolean tissuecol=false;
	private boolean expressioncol=false;
	private boolean specimentypecol=false;
	private boolean imagescol=true;
	private String[] insitucols;
	private Map<String,Boolean> resultmap;
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
	
	public ParamBean() {
		//DEFAULT COLUMNS TO SHOW
		insitucols= new String[]{"oid","gene","gudmapaccession","source","submissiondate","assaytype","probename","embryostage","images"};
		resultmap=new HashMap<String,Boolean>();
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
	
	public void setInsitucols(String[]insitucols){
		this.insitucols=insitucols;
		resultmap.clear();
		for(int i=0;i<insitucols.length;i++){
			resultmap.put(insitucols[i], true);	
		}
		oidcol=resultmap.containsKey("oid");
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
		insitucolmap.put("Oid", "oid"); //label, value
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
		setWhereclause(" WHERE ");
	}

	/*******************focus groups****************/
	public void focusGroup(ActionEvent event){
		String focusFieldAttribute = (String) event.getComponent().getAttributes().get("focusField");
			this.setFocusGroup(focusFieldAttribute);
	}
	
	/********************checkboxes******************/
	
	public void setCheckboxes(boolean[]checkboxes){
		this.checkboxes=checkboxes;
	}
	
	public boolean[] getCheckboxes(){
		return checkboxes;
	}
	
	
}
