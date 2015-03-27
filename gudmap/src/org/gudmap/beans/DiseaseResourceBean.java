package org.gudmap.beans;


import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.DiseaseResourceAssembler;
import org.gudmap.models.DiseaseResourceModel;

@Named
@RequestScoped
public class DiseaseResourceBean {
	
	
	
	ArrayList<DiseaseResourceModel> datalist=null;
	String diseaseName="";
	DiseaseResourceAssembler diseaseResourceAssembler;
	
	public DiseaseResourceBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.diseaseName = facesContext.getExternalContext().getRequestParameterMap().get("diseaseName");
		setup();
	}
	
	public void setup() {
		diseaseResourceAssembler = new DiseaseResourceAssembler();
		datalist = diseaseResourceAssembler.getData(diseaseName);
	}
	public ArrayList<DiseaseResourceModel> getDatalist() {
		return datalist;
	}
	
	public void setDiseaseName(String diseaseName) {
		this.diseaseName=diseaseName;
	}
	
	public String getDiseaseName(){
		return diseaseName;
	}
	
	

}
