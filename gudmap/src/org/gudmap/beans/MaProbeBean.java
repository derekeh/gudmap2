package org.gudmap.beans;

import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.models.submission.ProbeModel;
import org.gudmap.assemblers.MaProbeAssembler;

@Named
@RequestScoped
public class MaProbeBean {
	private ProbeModel probeModel;
	private MaProbeAssembler maProbeAssembler;
	
	public MaProbeBean() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String probeId = facesContext.getExternalContext().getRequestParameterMap().get("probe");
		String maprobeId = facesContext.getExternalContext().getRequestParameterMap().get("maprobe");

	   if (maprobeId != null && 8 < maprobeId.length())
			maprobeId = maprobeId.substring(8);
		
		maProbeAssembler = new MaProbeAssembler();
		probeModel = maProbeAssembler.getData(probeId, maprobeId);
	}
	
	public ProbeModel getProbeModel() {
		return probeModel;
	}
	
	public void setProbeModel(ProbeModel probeModel){
		this.probeModel = probeModel;
	}

}
