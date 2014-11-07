package org.gudmap.models;

import java.util.List;

public class InsituTableBeanModel {
private String oid,gene,gudmap_accession,source,submission_date,assay_type,probe_name,stage,age,sex,genotype,tissue,expression,specimen,image=null;
private List<String> totals=null;
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
	public void setProbe_name(String probe_name){
		this.probe_name=probe_name;
	}
	public String getProbe_name(){
		return probe_name;
	}
	public void setStage(String stage){
		this.stage=stage;
	}
	public String getStage(){
		return stage;
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
	public void setTissue(String tissue){
		this.tissue=tissue;
	}
	public String getTissue(){
		return tissue;
	}
	public void setExpression(String expression){
		this.expression=expression;
	}
	public String getExpression(){
		return expression;
	}
	public void setSpecimen(String specimen){
		this.specimen=specimen;
	}
	public String getSpecimen(){
		return specimen;
	}
	public void setImage(String image){
		this.image=image;
	}
	public String getImage(){
		return image;
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
