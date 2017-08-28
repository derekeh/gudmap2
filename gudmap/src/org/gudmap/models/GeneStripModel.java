package org.gudmap.models;

import java.util.ArrayList;

public class GeneStripModel {
	
	private String geneSymbol;
	private String synonyms;
	private String mgiId;
	private int omimCount;
	private String stageRange;
	private String expressionProfile;
	private String imageUrl;
	private MasterTableInfo[] microarrayProfile;
	private ArrayList<String> rnaSeq;
	private boolean selected=false;
	private String gene_id;
	private String species;
	private int rowCount=0;
	private String source;
	private String clickFilePath;
	
	public GeneStripModel() {
	}
	
	public void setGeneSymbol(String geneSymbol){
		this.geneSymbol=geneSymbol;
	}
	
	public String getGeneSymbol() {
		return geneSymbol;
	}
	
	public void setSynonyms(String synonyms){
		this.synonyms=synonyms;
	}
	
	public String getSynonyms() {
		return synonyms;
	}
	
	public void setMgiId(String mgiId){
		this.mgiId=mgiId;
	}
	
	public String getMgiId() {
		return mgiId;
	}
	
	public void setOmimCount(int omimCount){
		this.omimCount=omimCount;
	}
	
	public int getOmimCount() {
		return omimCount;
	}
	
	public void setStageRange(String stageRange){
		this.stageRange=stageRange;
	}
	
	public String getStageRange() {
		return stageRange;
	}
	
	public void setExpressionProfile(String expressionProfile){
		this.expressionProfile=expressionProfile;
	}
	
	public String getExpressionProfile() {
		return expressionProfile;
	}
	
	public void setImageUrl(String imageUrl){
		this.imageUrl=imageUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setMicroarrayProfile(MasterTableInfo[] microarrayProfile){
		this.microarrayProfile=microarrayProfile;
	}
	
	public MasterTableInfo[] getMicroarrayProfile() {
		return microarrayProfile;
	}
	
	public void setRnaSeq(ArrayList<String> rnaSeq){
		this.rnaSeq=rnaSeq;
	}
	
	public ArrayList<String> getRnaSeq() {
		return rnaSeq;
	}
	
	public void setSelected(boolean selected){
		this.selected=selected;
		
	}
	
	public boolean getSelected(){
		return selected;
	}

	public void setGene_id(String gene_id){
		this.gene_id=gene_id;
	}
	public String getGene_id(){
		return gene_id;
	}
	
	public void setSpecies(String species) {
		this.species = species;
	}
	
	public String getSpecies() {
		return species;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public void setRowCount(int rowCount) {
		this.rowCount=rowCount;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSource() {
		return source;
	}

	public void setClickFilePath(String clickFilePath) {
	    this.clickFilePath = clickFilePath;
	}
	public String getClickFilePath() {
	    return clickFilePath;
	}
	
}
