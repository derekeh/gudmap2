package org.gudmap.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
	private String query="";
	
	public ViewPageBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.docID = facesContext.getExternalContext().getRequestParameterMap().get("docID");	
    	Iterator<String> itr = facesContext.getExternalContext().getRequestParameterNames();
    	while(itr.hasNext()){
    		String element = itr.next().toString();
    		if (element.contains("query")){
    			this.query = facesContext.getExternalContext().getRequestParameterMap().get("query");
    		}
    	}
    	
    	editPageAssembler = new EditPageAssembler();
    	editPageList = editPageAssembler.retrievePage(docID);
    	
	}
	
	public ArrayList<EditPageModel> getEditPageList() {
		
		if (query.length() > 0){
			EditPageModel model = editPageList.get(0);
			String content = model.getContent_1();
			String[] arr = query.split(" ");
			int size = arr.length;
			for (int i=0; i<size; i++){
				String old = arr[i];
				// add yellow highlight 
				String update = "<span style='background-color: #FFFF00'>" + old + "</span>"; 
				content = content.replace(old, update);					
			}
			model.setContent_1(content);
			editPageList.set(0, model);
		}
		
		return editPageList;
	}
	
	public String getDocID () {
		return docID;
	}
	
	public void setDocID (String docID) {
		this.docID = docID;
	}


}
