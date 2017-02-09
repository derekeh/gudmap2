package org.gudmap.beans;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.SupplementaryFiles;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.utils.Utils;


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
	private String geneIdParam="";
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
	private String geneId;	
	private String softwareUpdate="06 Aug 2015";
	private String softwareVersion="JSF2 v2.05";
	private String editorialUpdate="28 Jul 2015";
	private Date softwareUpdateDB;
	private Date editorialUpdateDB;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public SessionBean() {
		      
		 setUpdateValues();
	}
	
	public void setUpdateValues() {
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(GenericQueries.GET_UPDATE_INFO); 
			result =  ps.executeQuery();
			if (result.first()) {
				setSoftwareUpdate(result.getString("software_update"));
				setEditorialUpdate(result.getString("editorial_update"));
				setSoftwareVersion(result.getString("software_version"));
			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
		
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(GenericQueries.GET_UPDATE_INFO_DB); 
			result =  ps.executeQuery();
			if (result.first()) {
				softwareUpdateDB=(result.getDate("software_update"));
				editorialUpdateDB=(result.getDate("editorial_update"));
			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
	}
	
	public String applicationUpdate() {
		int status=-1;
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(GenericQueries.UPDATE_INFO); 
			ps.setString(1, Utils.getMysqlDateFromInput(softwareUpdateDB));
			ps.setString(2, Utils.getMysqlDateFromInput(editorialUpdateDB));
			ps.setString(3, softwareVersion);
			status =  ps.executeUpdate();
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
		if(status > 0)	
			setUpdateValues();
		
		return "/db/database_homepage";
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
	 
	 public void setGeneIdParam(String geneIdParam) {
		 this.geneIdParam = geneIdParam;
	 }
	 
	 public String getGeneIdParam() {
		 return geneIdParam;
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

	 public void setGeneId(String id){
		    this.geneId=id;
		 }
				    
	public String getGeneId(){
		    return geneId;
	}
	
	public void setSoftwareUpdate(String softwareUpdate) {
		this.softwareUpdate = softwareUpdate;
	}
	public String getSoftwareUpdate() {
		return softwareUpdate;
	}
	
	public void setEditorialUpdate(String editorialUpdate) {
		this.editorialUpdate = editorialUpdate;
	}
	public String getEditorialUpdate() {
		return editorialUpdate;
	}
	
	public void setSoftwareUpdateDB(Date softwareUpdateDB) {
		this.softwareUpdateDB = softwareUpdateDB;
	}
	public Date getSoftwareUpdateDB() {
		return softwareUpdateDB;
	}
	
	public void setEditorialUpdateDB(Date editorialUpdateDB) {
		this.editorialUpdateDB = editorialUpdateDB;
	}
	public Date getEditorialUpdateDB() {
		return editorialUpdateDB;
	}
	
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	 
	 public void init() {
	 //dummy to initialise bean from view
	 }

	 

}
