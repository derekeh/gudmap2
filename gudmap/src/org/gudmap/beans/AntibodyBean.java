package org.gudmap.beans;

import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.models.submission.AntibodyModel;
import org.gudmap.models.submission.ProbeModel;
import org.gudmap.assemblers.AntibodyAssembler;
import org.gudmap.assemblers.MaProbeAssembler;

@Named
@RequestScoped
public class AntibodyBean {
	private AntibodyModel antibodyModel;
	private AntibodyAssembler antibodyAssembler;
	
	public AntibodyBean() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String antibodyId = facesContext.getExternalContext().getRequestParameterMap().get("antibody");

		antibodyAssembler = new AntibodyAssembler();
		antibodyModel = antibodyAssembler.getData(antibodyId,false);
	}
	
	
	public AntibodyModel getAntibodyModel() {
		return antibodyModel;
	}
	
	public void setAntibodyModel(AntibodyModel antibodyModel){
		this.antibodyModel = antibodyModel;
	}

}
