package org.gudmap.models;

import java.util.List;

public class MouseStrainsTableBeanModel {
	
	private String oid,gene,reporterAllele,alleleType,alleleVerification,alleleCharacterisation,strainAvailability,organ,cellType=null;
	private List<String> totals=null;;
	private boolean selected;

	public void setOid(String oid){
		this.oid=oid;
	}
	public String getOid(){
		return oid;
	}
	public void setGene(String gene){
		this.gene=gene;
	}
	public String getGene(){
		return gene;
	}
	public void setReporterAllele(String reporterAllele){
		this.reporterAllele=reporterAllele;
	}
	public String getReporterAllele(){
		return reporterAllele;
	}
	public void setAlleleType(String alleleType){
		this.alleleType=alleleType;
	}
	public String getAlleleType(){
		return alleleType;
	}
	public void setAlleleVerification(String alleleVerification){
		this.alleleVerification=alleleVerification;
	}
	public String getAlleleVerification(){
		return alleleVerification;
	}
	public void setAlleleCharacterisation(String alleleCharacterisation){
		this.alleleCharacterisation=alleleCharacterisation;
	}
	public String getAlleleCharacterisation(){
		return alleleCharacterisation;
	}
	public void setStrainAvailability(String strainAvailability){
		this.strainAvailability=strainAvailability;
	}
	public String getStrainAvailability(){
		return strainAvailability;
	}
	public void setOrgan(String organ){
		this.organ=organ;
	}
	public String getOrgan(){
		return organ;
	}
	public void setCellType(String cellType){
		this.cellType=cellType;
	}
	public String getCellType(){
		return cellType;
	}
	
	/////////////////////////////////////////////////////////
	public void setTotals(List<String> totals){
		this.totals=totals;
	}
	public List<String> getTotals(){
		return totals;
	}
	
	public void setSelected(boolean selected){
		this.selected=selected;
	}
	
	public boolean getSelected() {
		return selected;
	}
	

}
