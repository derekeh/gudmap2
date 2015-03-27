package org.gudmap.models;

public class DiseaseResourceModel {
	
	public DiseaseResourceModel() {
		
	}
	
	private String omimID;
	private String diseaseName;
	private String humanGeneSymbol;
	private String mouseGeneSymbol;
	private String mouseGeneMgiID;
	private String hasInsituData;
	private String mpID;
	private String mpPhenotype;
	private String annotationType;
	
	public void setOmimID(String omimID) {
		this.omimID = omimID;
	}
	
	public String getOmimID() {
		return  omimID;
	}
	
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	
	public String getDiseaseName() {
		return  diseaseName;
	}
	
	public void setHumanGeneSymbol(String humanGeneSymbol) {
		this.humanGeneSymbol = humanGeneSymbol;
	}
	
	public String getHumanGeneSymbol() {
		return  humanGeneSymbol;
	}
	
	public void setMouseGeneSymbol(String mouseGeneSymbol) {
		this.mouseGeneSymbol = mouseGeneSymbol;
	}
	
	public String getMouseGeneSymbol() {
		return  mouseGeneSymbol;
	}
	
	public void setMouseGeneMgiID(String mouseGeneMgiID) {
		this.mouseGeneMgiID = mouseGeneMgiID;
	}
	
	public String getMouseGeneMgiID() {
		return  mouseGeneMgiID;
	}
	
	public void setHasInsituData(String hasInsituData) {
		this.hasInsituData = hasInsituData;
	}
	
	public String getHasInsituData() {
		return  hasInsituData;
	}
	
	public void setMpID(String mpID) {
		this.mpID = mpID;
	}
	
	public String getMpID() {
		return  mpID;
	}
	
	public void setMpPhenotype(String mpPhenotype) {
		this.mpPhenotype = mpPhenotype;
	}
	
	public String getMpPhenotype() {
		return  mpPhenotype;
	}
	
	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}
	
	public String getAnnotationType() {
		return  annotationType;
	}

}
