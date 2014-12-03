package org.gudmap.utils;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ComponentSystemEvent;
import javax.annotation.PostConstruct;
import java.io.Serializable;

import org.gudmap.beans.LoginBean;

@Named
@RequestScoped
public class Authenticator implements Serializable{
	
	private static final long serialVersionUID = 1L;
	boolean loggedin=false;
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	
	public void setLoginBean(LoginBean loginBean){
		this.loginBean=loginBean;
	}
	public LoginBean getLoginBean() {
		return loginBean;
	}
	
//	@PostConstruct
//	public void init() {
//		if(loginBean.getUsername()!=null)
//			loggedin=true;
//	}
	
	public void check(ComponentSystemEvent event) throws IOException {
		/*if (loginBean.getUsername()!=null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("#{appRoot}/db/database_homepage.jsf");
        }*/
    }

}
