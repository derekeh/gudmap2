package org.gudmap.beans;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.ImageMatrixAssembler;
import org.gudmap.models.submission.ImageInfoModel;

@Named
@SessionScoped
//@RequestScoped
public class ImageMatrixBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ImageMatrixAssembler imageMatrixAssembler;
	private String geneSymbol="";
	private ImageInfoModel[][]imageInfoModelArray=null;
	private String[] imageMatrixHeaders;
	//private String[] imageMatrixHeaders = {"TS17","TS20","TS21","TS23","TS25","TS27","TS28"};
	private List<String> selectedItems;
    private boolean areAllChecked;
    
   /* @Inject
   	protected SessionBean sessionBean;*/
	
	public ImageMatrixBean() {
		//use this if request bean. 
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext.getExternalContext().getRequestParameterMap().get("gene")!=null)
    		this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
		imageMatrixAssembler = new ImageMatrixAssembler(geneSymbol);
		setup();*/
	}
	
	/*public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }*/
    
   /* @PostConstruct
	 public void setInputParams(){
    	if(geneSymbol==null || geneSymbol.equals(""))
    		geneSymbol=getSessionBean().getGeneParam();
    	//imageMatrixAssembler = new ImageMatrixAssembler(geneSymbol);
		//setup();
	 }*/
		
	public void setup() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
		imageMatrixAssembler = new ImageMatrixAssembler(geneSymbol);
		
		/*if(geneSymbol==null || geneSymbol.equals(""))
    		geneSymbol=getSessionBean().getGeneParam();*/
		
		imageInfoModelArray=imageMatrixAssembler.retrieveData();
	}
		
	public void setImageInfoModelArray(ImageInfoModel[][] imageInfoModelArray){
		this.imageInfoModelArray = imageInfoModelArray;
	}
	
	public ImageInfoModel[][] getImageInfoModelArray() {
		return imageInfoModelArray;
	}
	
	public String[] getImageMatrixHeaders() {
		imageMatrixHeaders = imageMatrixAssembler.getStages().toArray(new String[0]);
		Arrays.sort(imageMatrixHeaders);
		return imageMatrixHeaders;
	}
	
	public String checkboxSelections() { 
    	selectedItems.clear();
    	for (int i=0;i<imageInfoModelArray.length;i++) { 
    		for (int j=0; j<imageInfoModelArray[i].length; j++) {
	    		if (imageInfoModelArray[i][j].getSelected()) {
	    			selectedItems.add(imageInfoModelArray[i][j].getOid()); 
	    		} 
    		}
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	//since this is a request scoped bean, the values in selected items will not be available to result.jsf. Modify.
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<imageInfoModelArray.length;i++) { 
    		for (int j=0; j<imageInfoModelArray[i].length; j++) {
    			imageInfoModelArray[i][j].setSelected(areAllChecked); 
    		}
    	}
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
}
