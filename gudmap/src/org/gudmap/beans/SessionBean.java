package org.gudmap.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;


@Named(value="sessionBean")
@SessionScoped
public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean displayAsTree=true;
	private String annotationDisplayText="View annotated components as a list";
	private String annotationGroupsText="Show annotation under groups";
	private boolean displayAnnotationGroups=false;
	private String viewSubmissionId="";
	private String inputParam="";
	private String tempParam="";
	private String geneParam="";
	private String anatomyParam="";
	private String accessionParam="";
	private String geneFunctionParam="";
	private String diseaseNameParam="";
	private String diseaseGeneParam="";
	private String diseasePhenotypeParam="";
	//disease headers: omimID,diseaseName,humanGeneSymbol,mouseGeneSymbol,mouseGeneMgiID,hasInsituData,mpID,mpPhenotype,annotationType
	private boolean[] diseaseHeaders = {false,false,false,false,false,false,false,false,false};
	private int docID=0;
	private String masterTableId;
	private String genelistId;
	
	public SessionBean() {
		 
	}
	
	
	public void setDisplayAsTree(boolean displayAsTree) {
		this.displayAsTree=displayAsTree;
		if(displayAsTree)
			annotationDisplayText="View annotated components as a list";
		else
			annotationDisplayText="View annotated components as a tree";
	}
	
	public boolean getDisplayAsTree() {
		return displayAsTree;
	}
	
	public String getAnnotationDisplayText() {
		return annotationDisplayText;
    }
	
	public String getAnnotationGroupsText() {
		return annotationGroupsText;
    }
	
	public void setDisplayAnnotationGroups(boolean displayAnnotationGroups) {
		 this.displayAnnotationGroups=displayAnnotationGroups;
	    	if (displayAnnotationGroups)
	    		annotationGroupsText =  "Hide annotation under groups";
	        else
	        	annotationGroupsText =  "Show annotation under groups";
	 }
	 
	 public boolean getDisplayAnnotationGroups () {
		 return displayAnnotationGroups;
	 }
	 
	 public String getSubmissionView() {
		 return "viewSubmissionDetails";
	 }
	 
	 public void setViewSubmissionId(String viewSubmissionId ) {
		 this.viewSubmissionId=viewSubmissionId;
	 }
	 
	 public void setInputParam(String inputParam){
		 this.inputParam=inputParam;
	 }
	 
	 public String getInputParam() {
		 return inputParam;
	 }
	 
	 public void setTempParam(String tempParam){
		 this.tempParam=tempParam;
	 }
	 
	 public String getTempParam(){
		 return tempParam;
	 }
	 
	 public void setAnatomyParam(String anatomyParam){
		 this.anatomyParam = anatomyParam;
	 }
	 
	 public String getAnatomyParam() {
		 return anatomyParam;
	 }
	 
	 public void setAccessionParam(String accessionParam){
		 this.accessionParam = accessionParam;
	 }
	 
	 public String getAccessionParam() {
		 return accessionParam;
	 }
	 
	 public void setGeneFunctionParam(String geneFunctionParam){
		 this.geneFunctionParam = geneFunctionParam;
	 }
	 
	 public String getGeneFunctionParam() {
		 return geneFunctionParam;
	 }
	 
	 public void setGeneParam(String geneParam) {
		 this.geneParam = geneParam;
	 }
	 
	 public String getGeneParam() {
		 return geneParam;
	 }
	 
	 public void setDiseaseNameParam(String diseaseNameParam){
		 this.diseaseNameParam=diseaseNameParam;
	 }
	 
	 public String getDiseaseNameParam() {
		 return diseaseNameParam;
	 }
	 
	 public void setDiseaseGeneParam(String diseaseGeneParam){
		 this.diseaseGeneParam=diseaseGeneParam;
	 }
	 
	 public String getDiseaseGeneParam() {
		 return diseaseGeneParam;
	 }
	 
	 public void setDiseasePhenotypeParam(String diseasePhenotypeParam){
		 this.diseasePhenotypeParam=diseasePhenotypeParam;
	 }
	 
	 public String getDiseasePhenotypeParam() {
		 return diseasePhenotypeParam;
	 }
	 
	 public void setDiseaseHeaders(boolean[]diseaseHeaders){
		 this.diseaseHeaders = diseaseHeaders;
	 }
	 
	 public boolean[] getDiseaseHeaders() {
		 return diseaseHeaders;
	 }
	 
	 public void setDocID(int docID){
	    this.docID=docID;
	 }
	    
	 public int getDocID(){
	    return docID;
	 }
	 
	 public void setMasterTableId(String id){
	    this.masterTableId=id;
	 }
		    
	 public String getMasterTableId(){
	    return masterTableId;
	 }

	 public void setGenelistId(String id){
	    this.genelistId=id;
	 }
			    
	 public String getGenelistId(){
	    return genelistId;
	 }
	 
	 public void init() {
	 //dummy to initialise bean from view
	 }

	 

}
