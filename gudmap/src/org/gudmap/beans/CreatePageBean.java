package org.gudmap.beans;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
//import org.gudmap.models.EditPageModel;

@Named
@RequestScoped
public class CreatePageBean {
	
	private String alias="";
	private String title="";
	private String category="";
	private int level=0;
	private String value="";
	private String status="Status: ";
	private EditPageAssembler editPageAssembler;
	private ArrayList<String> aliasList;
	private int lastInsert=0;
	private int userID;
	
	@Inject
   	protected ParamBean paramBean;
	
	@Inject
	protected LoginBean loginBean;
	
	public CreatePageBean() {
		editPageAssembler = new EditPageAssembler();
		//lastInsert=0;
	}
	
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
    
    public ParamBean getParamBean() {
    	return paramBean;
    }
    
    public void setLoginBean(LoginBean loginBean){
		this.loginBean=loginBean;
	}
    
    public LoginBean getLoginBean() {
    	return loginBean;
    }
    
    @PostConstruct
	public void init(){
    	userID=loginBean.getUserID();
	}
	  
    public void setAlias(String alias) {
    	this.alias = alias;
    }
    
    public String getAlias() {
    	return alias;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public void setCategory(String category) {
    	this.category = category;
    }
    
    public String getCategory() {
    	return category;
    }
    
    public void setLevel(int level) {
    	/*try {
    		this.level=Integer.parseInt(level);
    	}
    	catch(NumberFormatException nfe){}*/
    	this.level=level;
    }
    
    public int getLevel() {
    	return level;
    }
    
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value; 

    }
    
    public String createPage() throws NoSuchAlgorithmException {
    	category=paramBean.getPageCategory();
    	status += editPageAssembler.createPage(alias, title, category, level, value, userID);
    	
    	lastInsert=editPageAssembler.getLastInsert();
    	return "create_page.jsf";
    }
    
    public String getStatus() {
    	return status;
    }
    
    public int getLastInsert() {
    	return lastInsert;
    }
    
    public ArrayList<String> getAliasList() {
    	this.aliasList= editPageAssembler.getAliasList();
    	return aliasList;
    }
    
    public void setAliasList(ArrayList<String> aliasList) {
    	this.aliasList = aliasList;
    }
    
    public void validateAlias(FacesContext context, UIComponent componentToValidate, Object value) throws ValidatorException {
    	String userAlias=(String)value;
    	if(getAliasList().contains((String)value)) {
    		FacesMessage message = new FacesMessage("That alias is already in use. Please choose another.");
    		throw new ValidatorException(message);
    	}
    	if(userAlias.contains(" ")) {
    		FacesMessage message = new FacesMessage("Alias cannot contain spaces.");
    		throw new ValidatorException(message);
    	}
    }


}
