package org.gudmap.beans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.gudmap.assemblers.ArraySeqSubmissionAssembler;
import org.gudmap.models.submission.ArraySubmissionModel;
import org.gudmap.models.submission.SeqSubmissionModel;

@Named
//@RequestScoped
@ViewScoped
public class ArraySeqSubmissionBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArraySeqSubmissionAssembler arraySeqSubmissionAssembler;
	private String accId="";
	private String oid="";
	private boolean isArray;
	
	private ArraySubmissionModel arraySubmissionModel=null;
	private SeqSubmissionModel seqSubmissionModel=null;
	
	public ArraySeqSubmissionBean () {
	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		accId = facesContext.getExternalContext().getRequestParameterMap().get("accId");
		if(accId!=null && accId!="") {
			oid=accId.substring(accId.indexOf(":")+1);
			
		}
		isArray = (facesContext.getExternalContext().getRequestParameterMap().get("isArray").equals("true")?true:false);
	
		
		arraySeqSubmissionAssembler = new ArraySeqSubmissionAssembler();
		
		if(isArray)
			arraySubmissionModel = arraySeqSubmissionAssembler.getArrayData(oid);
		else
			seqSubmissionModel = arraySeqSubmissionAssembler.getSeqData(oid);
		//arraySubmissionModel.getAccID();
	}
	
	public void setArraySubmissionModel(ArraySubmissionModel arraySubmissionModel) {
		this.arraySubmissionModel = arraySubmissionModel;
	}
	
	public ArraySubmissionModel getArraySubmissionModel () {
		return arraySubmissionModel;
	}
	
	public void setSeqSubmissionModel(SeqSubmissionModel seqSubmissionModel) {
		this.seqSubmissionModel = seqSubmissionModel;
	}
	
	public SeqSubmissionModel getSeqSubmissionModel () {
		return seqSubmissionModel;
	}
	
	

}
