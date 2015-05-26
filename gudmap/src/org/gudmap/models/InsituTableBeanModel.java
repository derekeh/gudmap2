package org.gudmap.models;



public class InsituTableBeanModel extends GenericTableBeanModel {
private String gene,probe_name,tissue,expression,specimen=null;

	
	public void setGene(String gene){
		this.gene=gene;
	}
	public String getGene(){
		return gene;
	}
	public void setProbe_name(String probe_name){
		this.probe_name=probe_name;
	}
	public String getProbe_name(){
		return probe_name;
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
	

}
