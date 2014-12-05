package org.gudmap.beans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.models.submission.IshSubmissionModel;
import org.gudmap.models.submission.AntibodyModel;
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
    
    public IshSubmissionBean() {
    	ishSubmissionAssembler = new IshSubmissionAssembler();
    }
    
    public void returnResults() {
    	ishSubmissionModel = ishSubmissionAssembler.getData(oid, displayAsTree);
    	 //if submission is not null, perform post-processing
        if (ishSubmissionModel != null){
            // initialise probe/antibodyModel sequence info
            String assayType = ishSubmissionModel.getAssayType();
            this.setOid(oid);
           
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
	         else if (assayType.indexOf("IHC") >= 0) { // IHC data
	            	AntibodyModel antibodyModel = ishSubmissionModel.getAntibodyModel();
	            	if (antibodyModel.getSeqStatus() != null && !antibodyModel.getSeqStatus().equals("Unsequenced.")) {
	            		renderPrbSeqInfo = true;
	            		if (antibodyModel.getSeqStartLocation() == 0 || antibodyModel.getSeqEndLocation() == 0) {
	            			antibodyModel.setSeqInfo("Accession number for part sequence: ");
	            		}else {
	            			antibodyModel.setSeqInfo("Specific sequence recognized: aa " + antibodyModel.getSeqStartLocation() +
	            					" - " + antibodyModel.getSeqEndLocation() + " of ");
	            		}
	            		if (antibodyModel.getUrl() != null && antibodyModel.getUrl() != "") {
	            			renderPrbNameURL = true;
	            		}
	            	}
	          } 
	          
          
            
            if (ishSubmissionModel.getAnnotationTree() != null || ishSubmissionModel.getExpressionDetailModel() != null){
                expressionMapped = true;
            }
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
    
    

}
