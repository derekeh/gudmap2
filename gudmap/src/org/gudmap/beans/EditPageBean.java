package org.gudmap.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;

@Named
@RequestScoped
public class EditPageBean {
	
	private String value="hello derek houghton";
	private int docID=0;
	private EditPageAssembler editPageAssembler;
	
	@Inject
   	protected SessionBean sessionBean;
	
	public EditPageBean() {
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.docID = Integer.parseInt(facesContext.getExternalContext().getRequestParameterMap().get("docid"));*/
		editPageAssembler = new EditPageAssembler();
	}
	
	public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }
    
    @PostConstruct
	public void init(){
    	docID = sessionBean.getDocID();
	}
	  
    public String getValue() {  
    	value = editPageAssembler.retrievePage(docID);
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }
    
    

}
