package org.gudmap.beans;

import java.io.Serializable;


import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class GeneStripBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String oid;
    private String geneSymbol;
    
    public GeneStripBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
    }
    
    public void setOid(String oid) {
    	this.oid=oid;
    }
    
    public String getOid() {
    	return oid;
    }
    
    public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
    
   
}
