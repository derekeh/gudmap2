package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.ImageMatrixAssembler;
import org.gudmap.models.submission.ImageInfoModel;

@Named
@RequestScoped
public class ImageMatrixBean {
	
	ImageMatrixAssembler imageMatrixAssembler;
	String geneSymbol="";
	ImageInfoModel[][]imageInfoModelArray=null;
	private String[] imageMatrixHeaders =  {"TS17","TS20","TS21","TS23","TS25","TS27","TS28"};
	
	public ImageMatrixBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
		imageMatrixAssembler = new ImageMatrixAssembler(geneSymbol);
		setup();
	}
		
	public void setup() {
		imageInfoModelArray=imageMatrixAssembler.retrieveData();
	}
		
	public void setImageInfoModelArray(ImageInfoModel[][] imageInfoModelArray){
		this.imageInfoModelArray = imageInfoModelArray;
	}
	
	public ImageInfoModel[][] getImageInfoModelArray() {
		return imageInfoModelArray;
	}
	
	public String[] getImageMatrixHeaders() {
		return imageMatrixHeaders;
	}
}
