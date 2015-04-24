package org.gudmap.beans;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.models.EditPageModel;

@Named
@RequestScoped
public class EditPageBean {
	
	private String value="";
	private String title="";
	private String docID;
	private String pageID="";
	private String status="Page last updated: ";
	private EditPageAssembler editPageAssembler;
	private ArrayList<EditPageModel> editPageList;
	
	@Inject
   	protected ParamBean paramBean;
	
	public EditPageBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
    	if(facesContext.getExternalContext().getRequestParameterMap().get("pageid")!=null)
    		this.pageID=facesContext.getExternalContext().getRequestParameterMap().get("pageid");
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
    	if(!pageID.equals(""))
    		paramBean.setPageId(pageID);
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
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getTitle() {
    	title = editPageAssembler.retrievePage(docID).get(0).getTitle();
    	return title;
    }
    
    public void setEditPageList(ArrayList<EditPageModel> editPageList){
    	this.editPageList = editPageList;
    }
    
    public ArrayList<EditPageModel> getEditPageList() {
    	return editPageList;
    }
    
    public String updatePage() {
    	status += editPageAssembler.updatePage(docID, title, value);
    	return "ck_editor";
    }
    
    public String getStatus() {
    	return status;
    }

}
