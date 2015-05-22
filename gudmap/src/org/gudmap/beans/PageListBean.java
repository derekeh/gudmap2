package org.gudmap.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.models.EditPageModel;

@Named
@SessionScoped
public class PageListBean implements Serializable{


	private static final long serialVersionUID = 1L;
	private EditPageAssembler editPageAssembler;
	private ArrayList<EditPageModel> pageList;
	
	public PageListBean() {
		//editPageAssembler = new EditPageAssembler();
	}
	
	public void setup() {
		editPageAssembler = new EditPageAssembler();
		this.pageList = editPageAssembler.getPageList();
	}
    
    public ArrayList<EditPageModel> getPageList() {
    	 //this.pageList = editPageAssembler.getPageList();
    	 return pageList;
    }
    
  


}
