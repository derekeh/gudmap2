package org.gudmap.models.submission;

import java.util.ArrayList;

public class SeriesModel {
	
	 private String geoID;
	    private String numSamples;
	    private String title;
	    private String summary;
	    private String type;
	    private String design;
	    private ArrayList<String> summaryResults;
	    private int oid;
	    private String description;// list of components
	    private int archiveId;
	    private int batchId;
	    
	    public void setGeoID(String geoID) {
	        this.geoID = geoID;
	    }

	    public String getGeoID() {
	        return geoID;
	    }

	    public void setNumSamples(String numSamples) {
	        this.numSamples = numSamples;
	    }

	    public String getNumSamples() {
	        return numSamples;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setSummary(String summary) {
	        this.summary = summary;
	    }

	    public String getSummary() {
	        return summary;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public String getType() {
	        return type;
	    }

	    public void SetDesign(String design) {
	        this.design = design;
	    }

	    public String getDesign() {
	        return design;
	    }

	    public ArrayList<String>  getSummaryResults() {
	        return summaryResults;
	    }

	    public void setSummaryResults(ArrayList<String>  summaryResults) {
	        this.summaryResults = summaryResults;
	    }
	    
	    public void setOid(int oid) {
	        this.oid = oid;
	    }

	    public int getOid() {
	        return oid;
	    }

	    public void SetDescription(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }
	    
	    public int getArchiveId() {
	    	return archiveId;
	    }
	    
	    public void setArchiveId(int archiveId) {
	    	this.archiveId = archiveId;
	    }
	    
	    public int getBatchId() {
	    	return batchId;
	    }
	    
	    public void setBatchId(int batchId) {
	    	this.batchId = batchId;
	    }


}
