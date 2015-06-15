package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.ArraySeqSubmissionAssembler;
import org.gudmap.models.submission.ArraySubmissionModel;

@Named
@RequestScoped
public class ArraySeqSubmissionBean {
	
	private ArraySeqSubmissionAssembler arraySeqSubmissionAssembler;
	private String accId="";
	private String oid="";
	private ArraySubmissionModel arraySubmissionModel=null;
	
	public ArraySeqSubmissionBean () {
	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		accId = facesContext.getExternalContext().getRequestParameterMap().get("accId");
		if(accId!=null && accId!="")
			oid=accId.substring(accId.indexOf(":")+1);
		
		arraySeqSubmissionAssembler = new ArraySeqSubmissionAssembler();
		arraySubmissionModel = arraySeqSubmissionAssembler.getData(oid);
	
	}
	
	public void setArraySubmissionModel(ArraySubmissionModel arraySubmissionModel) {
		this.arraySubmissionModel = arraySubmissionModel;
	}
	
	public ArraySubmissionModel getArraySubmissionModel () {
		return arraySubmissionModel;
	}

}
