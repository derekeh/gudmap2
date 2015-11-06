package org.gudmap.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("insituCheckboxColumnsValidator")
public class InsituColumns implements Validator {
	

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        //UIInput selectManyCheckbox = ((UIInput) component.getAttributes().get("checkboxType"));
        //String checkboxType = (String) checkboxTypeComponent.getValue();
    	String[] insitucols = (String[]) value;
        
        if (insitucols.length > 9) {
            throw new ValidatorException(new FacesMessage("Max 9 columns allowed"));
        }
        /*if ("insitu".equals(checkboxType) && insitucols.length > 9) {
            throw new ValidatorException(new FacesMessage("Max 9 columns allowed"));
        }*/
        
    }
    
    

}
