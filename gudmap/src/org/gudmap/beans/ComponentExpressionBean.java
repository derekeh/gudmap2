package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.assemblers.ComponentExpressionAssembler;

@Named
@RequestScoped
public class ComponentExpressionBean {
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

}
