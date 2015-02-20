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
	
	 public void init() {
	 //dummy to initialise bean from view
	 }

	 

}
