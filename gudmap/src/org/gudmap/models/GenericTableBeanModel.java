package org.gudmap.models;

import java.util.List;

public class GenericTableBeanModel {
protected String oid,gudmap_accession,source,submission_date,assay_type,specimen_assay_type,stage,stage_order,age,sex,genotype,image=null,gene_id,species,synonyms;
protected int personOid=0;
protected List<String> totals=null;
protected boolean selected;

	public void setOid(String oid){
		this.oid=oid;
	}
	public String getOid(){
		return oid;
	}
	public void setGudmap_accession(String gudmap_accession){
		this.gudmap_accession=gudmap_accession;
	}
	public String getGudmap_accession(){
		return gudmap_accession;
	}
	public void setSource(String source){
		this.source=source;
	}
	public String getSource(){
		return source;
	}
	public void setSubmission_date(String submission_date){
		this.submission_date=submission_date;
	}
	public String getSubmission_date(){
		return submission_date;
	}
	public void setAssay_type(String assay_type){
		this.assay_type=assay_type;
	}
	public String getAssay_type(){
		return assay_type;
	}
	public void setSpecimen_assay_type(String specimen_assay_type){
		this.specimen_assay_type=specimen_assay_type;
	}
	public String getSpecimen_assay_type(){
		return specimen_assay_type;
	}
	public void setStage(String stage){
		this.stage=stage;
	}
	public String getStage(){
		return stage;
	}
	public void setStage_order(String stage_order){
		this.stage_order=stage_order;
	}
	public String getStage_order(){
		return stage_order;
	}
	public void setAge(String age){
		this.age=age;
	}
	public String getAge(){
		return age;
	}
	public void setSex(String sex){
		this.sex=sex;
	}
	public String getSex(){
		return sex;
	}
	public void setGenotype(String genotype){
		this.genotype=genotype;
	}
	public String getGenotype(){
		return genotype;
	}
	public void setImage(String image){
		this.image=image;
	}
	public String getImage(){
		return image;
	}
	public void setPersonOid(int personOid){
		this.personOid=personOid;
	}
	public int getPersonOid(){
		return personOid;
	}
	public void setGene_id(String gene_id){
		this.gene_id=gene_id;
	}
	public String getGene_id(){
		return gene_id;
	}
	public void setSpecies(String species){
		this.species=species;
	}
	public String getSpecies(){
		return species;
	}
	public void setSynonyms(String synonyms){
		this.synonyms=synonyms;
	}
	public String getSynonyms(){
		return synonyms;
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
