package org.gudmap.beans;

import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.models.EditPageModel;

@Named
@RequestScoped
public class ViewPageBean {
	private String docID="";
	private EditPageAssembler editPageAssembler;
	private ArrayList<EditPageModel> editPageList;
	
	public ViewPageBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.docID = facesContext.getExternalContext().getRequestParameterMap().get("docID");	
    	editPageAssembler = new EditPageAssembler();
    	editPageList = editPageAssembler.retrievePage(docID);
    	
	}
	
	public ArrayList<EditPageModel> getEditPageList() {
		return editPageList;
	}

}
