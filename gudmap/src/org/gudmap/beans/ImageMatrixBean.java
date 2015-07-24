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
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.ImageMatrixAssembler;
import org.gudmap.globals.Globals;
import org.gudmap.models.submission.ImageInfoModel;

@Named
@SessionScoped
public class ImageMatrixBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ImageMatrixAssembler imageMatrixAssembler;
	private ImageInfoModel[][]imageInfoModelArray=null;
	private String[] imageMatrixHeaders;
	private List<String> selectedItems;
    private boolean areAllChecked;
    private String geneId;
    
   
	
	public ImageMatrixBean() {
	}
		
	public void setup() {
		geneId=Globals.getParameterValue("geneId");
		imageMatrixAssembler = new ImageMatrixAssembler(getGeneId());
		
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
	    			//selectedItems.add(imageInfoModelArray[i][j].getOid()); 
	    			selectedItems.add(imageInfoModelArray[i][j].getFilePath());
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
    			if(imageInfoModelArray[i][j]!=null)
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
    
    public void setGeneId(String geneId) {
    	this.geneId = geneId;
    }
    
    public String getGeneId() {
    	return geneId;
    }
}
