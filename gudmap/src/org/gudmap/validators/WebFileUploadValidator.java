package org.gudmap.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;
 
@FacesValidator("WebFileUploadValidator")
public class WebFileUploadValidator implements Validator {
 
	@Override
	public void validate(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
	 
		Part part = (Part) value;
		 
		// 1. validate file name length
		String fileName = getFileName(part);
		//System.out.println("----- validator fileName: " + fileName);
		if(fileName.length() == 0 ) {
			FacesMessage message = new FacesMessage("Error: File name is invalid !!");
			throw new ValidatorException(message);
		} 
		else if (fileName.length() > 50) {
			FacesMessage message = new FacesMessage("Error: File name is too long !!");
			throw new ValidatorException(message);
		}
		 
		// 2. validate file type (only text files allowed)
		if (!"text/plain".equals(part.getContentType()) && !"application/pdf".equals(part.getContentType()) && !"application/msword".equals(part.getContentType()) 
				&& !"application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(part.getContentType()) 
				&& !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(part.getContentType()) 
				&& !"application/vnd.ms-excel".equals(part.getContentType())) {
			FacesMessage message = new FacesMessage("Error: File type is invalid !!");
			throw new ValidatorException(message);
		  }
		
		if(!fileName.endsWith(".pdf") && !fileName.endsWith(".txt") && !fileName.endsWith(".doc")  && !fileName.endsWith(".docx") 
			  && !fileName.endsWith(".xls")   && !fileName.endsWith(".xlsx")){
			FacesMessage message = new FacesMessage("Error: File types restricted to txt, pdf, xls, xlsx, doc and docx.");
			throw new ValidatorException(message);
		}
		 
		// 3. validate file size (should not be greater than 4MB)
		if (part.getSize() > 4194304) {
			FacesMessage message = new FacesMessage("Error: File size is too big !!");
			throw new ValidatorException(message);
		}
	}
	 
	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		//System.out.println("----- validator partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "";
	}
}
