package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.GeneDetailsBeanAssembler;
import org.gudmap.globals.Globals;
import org.gudmap.models.submission.GeneModel;

@Named
@RequestScoped
public class GeneDetailsBean {
    private boolean debug = false;
	private GeneModel geneModel=null;
	private String geneId;
	private String probeset;
	private boolean entrezIdExists;
	private boolean xenbaseEntryExists;
	private boolean linkedArraysExist;
	private GeneDetailsBeanAssembler geneDetailsBeanAssembler;
	private String arrayDataViewName;
	private String geneStripViewName;
	private String geneSymbol;
	
	public GeneDetailsBean() {
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext.getExternalContext().getRequestParameterMap().get("gene")!=null) {
			this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
			geneDetailsBeanAssembler = new GeneDetailsBeanAssembler(geneSymbol);
			setup();
		}		
		if(facesContext.getExternalContext().getRequestParameterMap().get("geneId")!=null) {
			this.geneId = facesContext.getExternalContext().getRequestParameterMap().get("geneId");
			geneDetailsBeanAssembler = new GeneDetailsBeanAssembler(geneId);
			setup();
		}*/
		if(Globals.getParameterValue("geneId")!=null) {
			this.geneId = Globals.getParameterValue("geneId");
			geneDetailsBeanAssembler = new GeneDetailsBeanAssembler(geneId);
			setup();
		}
	}
	
	public void setup(){
		geneModel = geneDetailsBeanAssembler.getGeneModel();
		entrezIdExists = false;
		linkedArraysExist = false;
		xenbaseEntryExists = false;
		
		if(geneModel!=null){
			if(geneModel.getEntrezID() != null)
				entrezIdExists = true;
			if(geneModel.getMgiAccID() == null || geneModel.getMgiAccID().indexOf("HGNC") >=0 || geneModel.getMgiAccID().indexOf("MGI") >=0)
				xenbaseEntryExists = false;
		}
	}
	
	public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
    
    public void setGeneModel(GeneModel geneModel){
    	this.geneModel = geneModel;
    }
    
    public GeneModel getGeneModel() {
    	return geneModel;
    }
    
    public void setEntrezIdExists(boolean entrezIdExists) {
    	this.entrezIdExists = entrezIdExists;
    }
    
    public boolean getEntrezIdExists() {
    	return entrezIdExists;
    }
    public void setLinkedArraysExist(boolean linkedArraysExist) {
    	this.linkedArraysExist = linkedArraysExist;
    }
    
    public boolean getLinkedArraysExist() {
    	return linkedArraysExist;
    }
    public void setXenbaseEntryExists(boolean xenbaseEntryExists) {
    	this.xenbaseEntryExists = xenbaseEntryExists;
    }
    
    public boolean getXenbaseEntryExists() {
    	return xenbaseEntryExists;
    }

}
