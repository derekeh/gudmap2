package org.gudmap.beans;

//import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.assemblers.ComponentExpressionAssembler;

@Named
@RequestScoped
//@SessionScoped
public class ComponentExpressionBean { //implements Serializable {
	//private static final long serialVersionUID = 1L;
	private String oid;
	private String componentId;
	private boolean hasSecondaryStrength;
    private boolean hasPatterns;
    private ExpressionDetailModel expressionDetailModel;
    private ComponentExpressionAssembler componentExpressionAssembler;
	
	public ComponentExpressionBean () {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		this.oid = facesContext.getExternalContext().getRequestParameterMap().get("oid");
		this.componentId = facesContext.getExternalContext().getRequestParameterMap().get("componentId");
		
		componentExpressionAssembler = new ComponentExpressionAssembler();
        
		hasSecondaryStrength = false;
        hasPatterns = false;
        
        expressionDetailModel = componentExpressionAssembler.getData(oid, componentId);
        if(expressionDetailModel != null){
            if(expressionDetailModel.getSecondaryStrength() != null && !expressionDetailModel.getSecondaryStrength().equals("")) {
                hasSecondaryStrength = true;
            }
            if(expressionDetailModel.getPattern() != null){
                hasPatterns = true;
            }
        }
	}
	
	
    public ExpressionDetailModel getExpressionDetailModel() {
    	
    	/*hasSecondaryStrength = false;
        hasPatterns = false;
        
        expressionDetailModel = componentExpressionAssembler.getData(oid, componentId);
        if(expressionDetailModel != null){
            if(expressionDetailModel.getSecondaryStrength() != null && !expressionDetailModel.getSecondaryStrength().equals("")) {
                hasSecondaryStrength = true;
            }
            if(expressionDetailModel.getPattern() != null){
                hasPatterns = true;
            }
        }*/
        
        return expressionDetailModel;
    }
    
    public void setExpressionDetailModel (ExpressionDetailModel expressionDetailModel){
        this.expressionDetailModel = expressionDetailModel;
    }
    
    public boolean getHasSecondaryStrength() {
        return hasSecondaryStrength;
    }
    
    public boolean getHasPatterns() {
        return hasPatterns;
    }
    
    public void setOid(String oid) {
    	this.oid=oid;
    }
    
    public String getOid() {
    	return oid;
    }
    
    public void setComponentId(String componentId) {
    	this.componentId=componentId;
    }
    
    public String getComponentId() {
    	return componentId;
    }
    
    public void newValues(String id, String compid) {
    	this.oid=id;
    	this.componentId=compid.replace("EMAP", "EMAP:");
    }

}
