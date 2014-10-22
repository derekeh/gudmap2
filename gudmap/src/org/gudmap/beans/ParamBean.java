package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="paramBean")
@SessionScoped
public class ParamBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String focusGroup="none";
	private boolean isLoggedIn=false;
	private String assayType="ISH";
	
	public void paramBean() {
		
	}
	
	public void setFocusGroup(String focusGroup){
		this.focusGroup=focusGroup;
	}
	public String getFocusGroup(){
		return focusGroup;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn){
		this.isLoggedIn=isLoggedIn;
	}
	public boolean getIsLoggedIn(){
		return isLoggedIn;
	}
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	public String getAssayType(){
		return assayType;
	}

}
