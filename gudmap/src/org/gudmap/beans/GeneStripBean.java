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

import org.gudmap.models.GeneStripModel;
import org.gudmap.assemblers.GeneStripBeanAssembler;

@Named
@RequestScoped
//@ViewScoped
//@SessionScoped
public class GeneStripBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String oid;
    private String geneSymbol;
    private String inputString="";
    private String wildcard = "equals";
    private GeneStripBeanAssembler geneStripBeanAssembler;
    private ArrayList<GeneStripModel> dataList;
    private String tempParam="";
    String str;
    
    @Inject
   	protected SessionBean sessionBean;
    
    public GeneStripBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
        this.inputString = facesContext.getExternalContext().getRequestParameterMap().get("input");
        
        geneStripBeanAssembler = new GeneStripBeanAssembler();       
        //setup();
        
    }
    
    public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    //if the parameters are not passed in via the url then they are passed in from sessionBean.getTempParam
    //in a view (viewGeneStrip) which has this as Request scoped backing bean are unable to set the <f:param value to sessionBean.tempParam
    @PostConstruct
	 public void setInputParams(){
    	if(inputString==null || inputString.equals(""))
        	inputString=sessionBean.getTempParam();
		 sessionBean.setInputParam(inputString);
		 setup();
	 }
    
	 /*@PostConstruct
	 public void setInputParams(){
		 sessionBean.setInputParam(inputString);
	 }*/
    
    
    public void setup() {
        // check input string to decide wildcard value
       	
       	if (inputString != null) {
       		inputString = inputString.trim();
       		int stringLen = inputString.length();
       		String lastChar = inputString.trim().substring(stringLen-1, stringLen);
       		if (lastChar.equals("*")) {
       			inputString = inputString.substring(0, stringLen-1);
       			wildcard = "starts with";
       		}
       			       	 
       		dataList = geneStripBeanAssembler.getData(geneSymbol,inputString,wildcard);
       	}
    	
    }
    
    public void setOid(String oid) {
    	this.oid=oid;
    }
    
    public String getOid() {
    	return oid;
    }
    
    public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
    
    public void setDataList(ArrayList<GeneStripModel> dataList){
    	this.dataList=dataList;
    }
    
    public ArrayList<GeneStripModel> getDataList() {
    	return dataList;
    }
    
    public void setInputString(String inputString) {
    	this.inputString = inputString;
    }
    
    public String getInputString() {
    	return inputString;
    }
    
    public void checkAll(){}
	public void checkboxSelections(){}
	
	public void setTempParam(String tempParam) {
		this.tempParam=tempParam;
	}
    
	public String getTempParam(){
		return tempParam;
	}
}
