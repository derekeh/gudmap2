package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class EditPageBean {
	
	private String value="hello derek houghton";
	
	public EditPageBean() {
		
	}
	  
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }

}
