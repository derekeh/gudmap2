package org.gudmap.beans;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.models.EditPageModel;

@Named
@RequestScoped
public class EditPageBean {
	
	private String value="";
	private String docID;
	private EditPageAssembler editPageAssembler;
	private ArrayList<EditPageModel> editPageList;
	
	@Inject
   	protected ParamBean paramBean;
	
	public EditPageBean() {
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.docID = Integer.parseInt(facesContext.getExternalContext().getRequestParameterMap().get("docid"));*/
		editPageAssembler = new EditPageAssembler();
	}
	
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
    
    public ParamBean getSessionBean() {
    	return paramBean;
    }
    
    @PostConstruct
	public void init(){
    	docID = paramBean.getPageId();
	}
	  
    public String getValue() {  
    	value = editPageAssembler.retrievePage(docID).get(0).getContent_1();
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value; 
        /*FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage("Content Saved."));*/

    }
    
    public void setEditPageList(ArrayList<EditPageModel> editPageList){
    	this.editPageList = editPageList;
    }
    
    public ArrayList<EditPageModel> getEditPageList() {
    	return editPageList;
    }
    
    

}
