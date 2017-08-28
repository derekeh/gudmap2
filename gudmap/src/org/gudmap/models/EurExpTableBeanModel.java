package org.gudmap.models;



public class EurExpTableBeanModel extends GenericTableBeanModel {
private String eeid,gene,mgi_id,entrez_id,tissue=null,image_path,thumbnail_path,imageType;

	
	public void setEeid(String eeid){
		this.eeid=eeid;
	}
	public String getEeid(){
		return eeid;
	}
	public void setGene(String gene){
		this.gene=gene;
	}
	public String getGene(){
		return gene;
	}
	public void setMgi_id(String mgi_id){
		this.mgi_id=mgi_id;
	}
	public String getMgi_id(){
		return mgi_id;
	}
	public void setEntrez_id(String entrez_id){
		this.entrez_id=entrez_id;
	}
	public String getEntrez_id(){
		return entrez_id;
	}
	public void setTissue(String tissue){
		this.tissue=tissue;
	}
	public String getTissue(){
		return tissue;
	}

	public void setImageType(String imageType){
		this.imageType=imageType;
	}
	public String getImageType(){
		return imageType;
	}
	
	public void setImage_path(String image_path){
		this.image_path=image_path;
	}
	public String getImage_path(){
		return image_path;
	}
	public void setThumbnail_path(String thumbnail_path){
		this.thumbnail_path=thumbnail_path;
	}
	public String getThumbnail_path(){
		return thumbnail_path;
	}

}
