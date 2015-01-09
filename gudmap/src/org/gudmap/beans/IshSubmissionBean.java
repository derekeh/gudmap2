package org.gudmap.beans;

import java.io.Serializable;


//import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
//import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.gudmap.models.submission.IshSubmissionModel;
import org.gudmap.assemblers.IshSubmissionAssembler;

@Named
@RequestScoped
public class IshSubmissionBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IshSubmissionModel ishSubmissionModel;
    private IshSubmissionAssembler ishSubmissionAssembler;
    // protected String submissionID;
    //protected String id;
    //private boolean renderPage;
   // private String displayOfAnnoGps;
    private boolean displayOfAnnotationGroups;
   // private String annotationDisplayType;
    private boolean renderPrbSeqInfo;
    private boolean renderPrbNameURL;
    private boolean expressionMapped;
    private String oid;
    private boolean displayAsTree=true;
    private boolean isTransgenic=false;
    
    public IshSubmissionBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
		String accId = facesContext.getExternalContext().getRequestParameterMap().get("accId");
		if(accId!=null && accId!="")
			oid=accId.substring(accId.indexOf(":")+1);
    	ishSubmissionAssembler = new IshSubmissionAssembler();
    }
    
    public void returnResults() {
    	ishSubmissionModel = ishSubmissionAssembler.getData(oid, displayAsTree);
    	 //if submission is not null, perform post-processing
        if (ishSubmissionModel != null){
            // initialise probe/antibodyModel sequence info
            String assayType = ishSubmissionModel.getAssayType();
            //this.setOid(oid);
           
	        if (assayType.indexOf("ISH") >=0 && null != ishSubmissionModel.getProbeModel()) { // ISH data
	            	if (ishSubmissionModel.getProbeModel().getSeqStatus() != null && !ishSubmissionModel.getProbeModel().getSeqStatus().equals("Unsequenced.")) {
	            		renderPrbSeqInfo = true;
	                    if (ishSubmissionModel.getProbeModel().getSeq5Loc().equals("n/a") ||
	                        ishSubmissionModel.getProbeModel().getSeq3Loc().equals("n/a")) {
	                        ishSubmissionModel.getProbeModel().setSeqInfo("Accession number for part sequence: ");
	                    } else {
	                        ishSubmissionModel.getProbeModel().setSeqInfo("Probe sequence spans from " +
	                                                         ishSubmissionModel.getProbeModel().getSeq5Loc() +
	                                                         " to " +
	                                                         ishSubmissionModel.getProbeModel().getSeq3Loc() +
	                                                         " of");
	                    }
	                    if (ishSubmissionModel.getProbeModel().getProbeNameURL()!= null && !(ishSubmissionModel.getProbeModel().getProbeName().indexOf("maprobe") >=0)){
	                        renderPrbNameURL = true;
	                    }   
	                }
	         } 
	         else if (assayType.indexOf("IHC") >= 0 && null != ishSubmissionModel.getAntibodyModel()) { // IHC data
            	if (ishSubmissionModel.getAntibodyModel().getSeqStatus() != null && !ishSubmissionModel.getAntibodyModel().getSeqStatus().equals("Unsequenced.")) {
            		renderPrbSeqInfo = true;
            		if (ishSubmissionModel.getAntibodyModel().getSeqStartLocation() == 0 || ishSubmissionModel.getAntibodyModel().getSeqEndLocation() == 0) {
            			ishSubmissionModel.getAntibodyModel().setSeqInfo("Accession number for part sequence: ");
            		}else {
            			ishSubmissionModel.getAntibodyModel().setSeqInfo("Specific sequence recognized: aa " + ishSubmissionModel.getAntibodyModel().getSeqStartLocation() +
            					" - " + ishSubmissionModel.getAntibodyModel().getSeqEndLocation() + " of ");
            		}
            		if (ishSubmissionModel.getAntibodyModel().getUrl() != null && ishSubmissionModel.getAntibodyModel().getUrl() != "") {
            			renderPrbNameURL = true;
            		}
            	}
            }
	        else if (assayType.indexOf("TG") >= 0) { // transgenic data
	            	isTransgenic=true;
	        }
	        else { // other assay type
	            	
	        }
	        
	        //TODO UNCOMMENT
            /*if (ishSubmissionModel.getAnnotationTree() != null || ishSubmissionModel.getExpressionDetailModel() != null){
                expressionMapped = true;
            }*/
        }
    } // end returnResults
    
    public void setOid(String oid) {
    	this.oid=oid;
    }
    
    public String getOid() {
    	return oid;
    }
    
    public void setDisplayAsTree(boolean displayAsTree) {
    	this.displayAsTree = displayAsTree;
    }
    
    public boolean getDisplayAsTree() {
    	return displayAsTree;
    }
    
    public void setRenderPrbSeqInfo(boolean renderPrbSeqInfo) {
    	this.renderPrbSeqInfo = renderPrbSeqInfo;
    }
    
    public boolean getRenderPrbSeqInfo() {
    	return renderPrbSeqInfo;
    }
    
    public void setRenderPrbNameURL(boolean renderPrbNameURL) {
    	this.renderPrbNameURL = renderPrbNameURL;
    }
    
    public boolean getRenderPrbNameURL() {
    	return renderPrbNameURL;
    }
    
    public void setExpressionMapped(boolean expressionMapped) {
    	this.expressionMapped = expressionMapped;
    }
    
    public boolean getExpressionMapped() {
    	return expressionMapped;
    }
    
    public void setDisplayOfAnnotationGroups(boolean displayOfAnnotationGroups) {
    	this.displayOfAnnotationGroups = displayOfAnnotationGroups;
    }
    
    public boolean getDisplayOfAnnotationGroups() {
    	return displayOfAnnotationGroups;
    }
    
    public String getAnnotationDisplayLinkTxt() {
        if (!displayAsTree)
            return "View annotated components as a list";
        else
            return "View annotated components as a tree";
    }
    
    public String getDisplayOfAnnotatedGpsTxt() {
        if (!displayOfAnnotationGroups )
        	return "Show annotation under groups";
        else
        	return "Hide annotation under groups";
    }
    
    public IshSubmissionModel getIshSubmissionModel () {
    	return ishSubmissionModel;
    }
    
    public void setIsTransgenic(boolean isTransgenic){
    	this.isTransgenic=isTransgenic;
    }
    
    public boolean getIsTransgenic(){
    	return isTransgenic;
    }

}
