package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.DiseaseResourceAssembler;
import org.gudmap.models.DiseaseResourceModel;

@Named
//@RequestScoped
@SessionScoped

public class DiseaseResourceBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	ArrayList<DiseaseResourceModel> datalist=null;
	String inputTerm="";
	String type="";
	boolean isDirect=false;
	DiseaseResourceAssembler diseaseResourceAssembler;
	@Inject
	protected SessionBean sessionBean;
	
	public DiseaseResourceBean() {
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("name")){
			this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseName");
			type="name";
		}
		else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("gene")){
			this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseGene");
			type="gene";
		}
		else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("phenotype")){
			this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseasePhenotype");
			type="phenotype";
		}
		else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("phenotypegene")){
			this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseGene");
			type="phenotypegene";
		}
		
		if(facesContext.getExternalContext().getRequestParameterMap().get("isDirect").equals("true")){
			this.isDirect=true;
		}*/
		//setup();
	}
	
	 public void setSessionBean(SessionBean sessionBean){
			this.sessionBean=sessionBean;
	}
	    
	public SessionBean getSessionBean() {
	    	return sessionBean;
	}
	
	 //@PostConstruct
	 public void init(){
		 FacesContext facesContext = FacesContext.getCurrentInstance();
			if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("name")){
				this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseName");
				type="name";
			}
			else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("gene")){
				this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseGene");
				type="gene";
			}
			else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("phenotype")){
				this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseasePhenotype");
				type="phenotype";
			}
			else if(facesContext.getExternalContext().getRequestParameterMap().get("type").equals("phenotypegene")){
				this.inputTerm = facesContext.getExternalContext().getRequestParameterMap().get("diseaseGene");
				type="phenotypegene";
			}
			
			if(facesContext.getExternalContext().getRequestParameterMap().get("isDirect").equals("true")){
				this.isDirect=true;
			} 
		 
		 
    	boolean [] tableHeaders=new boolean[9];
    	//disease headers: omimID,diseaseName,humanGeneSymbol,mouseGeneSymbol,mouseGeneMgiID,hasInsituData,mpID,mpPhenotype,annotationType
    	if(type.equals("name") || type.equals("gene")){
    		tableHeaders[0]=true;
    		tableHeaders[1]=true;
    		tableHeaders[2]=true;
    		tableHeaders[3]=true;
    		tableHeaders[4]=true;
    		tableHeaders[5]=true;
    		tableHeaders[6]=false;
    		tableHeaders[7]=false;
    		tableHeaders[8]=false;
    	}
    	if(type.equals("phenotype") || type.equals("phenotypegene")){
    		tableHeaders[0]=false;
    		tableHeaders[1]=false;
    		tableHeaders[2]=false;
    		tableHeaders[3]=true;
    		tableHeaders[4]=true;
    		tableHeaders[5]=false;
    		tableHeaders[6]=true;
    		tableHeaders[7]=true;
    		tableHeaders[8]=true;
    	}
    	getSessionBean().setDiseaseHeaders(tableHeaders);
		setup();
	 }
	
	public void setup() {
		diseaseResourceAssembler = new DiseaseResourceAssembler();
		datalist = diseaseResourceAssembler.getData(inputTerm,type,isDirect);
	}
	public ArrayList<DiseaseResourceModel> getDatalist() {
		return datalist;
	}
	
	
	

}
