package org.gudmap.beans;

import java.io.Serializable;








//import javax.annotation.PostConstruct;
//import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
//import javax.inject.Inject;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.gudmap.models.submission.IshSubmissionModel;
import org.gudmap.assemblers.IshSubmissionAssembler;
//import org.gudmap.utils.CookieOperations;
//import org.gudmap.utils.FacesUtil;

@Named
//@SessionScoped
//@RequestScoped
@ViewScoped
public class IshSubmissionBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IshSubmissionModel ishSubmissionModel;
    private IshSubmissionAssembler ishSubmissionAssembler;
    private boolean renderPrbSeqInfo;
    private boolean renderPrbNameURL;
    private boolean expressionMapped;
    protected String id;
    private String oid;
    private String accId;
    //private boolean displayAsTree=false;
    private boolean isTransgenic=false;
    protected String annotatedTreeExpressions;
    protected String annotatedTreePatterns;
    protected String annotatedTreeExpressionNotes;
    private String newid="8";
    
    /*@Inject
   	protected SessionBean sessionBean;*/
    
    public IshSubmissionBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	//View submission Details link from viewMaProbeDetails
		accId = facesContext.getExternalContext().getRequestParameterMap().get("accId");
		if(accId!=null && accId!="")
			oid=accId.substring(accId.indexOf(":")+1);
		
    	ishSubmissionAssembler = new IshSubmissionAssembler();	
    }
    
   /* public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
     @PostConstruct
    public void getRemoteDisplayTree(){
    	this.displayAsTree=sessionBean.getDisplayAsTree();
    }*/
    
    public void returnResults(boolean displayAsTree) {
    	//this.displayAsTree=displayAsTree;
    	
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
    
    
    public IshSubmissionModel getIshSubmissionModel () {
    	return ishSubmissionModel;
    }
    
    public void setIsTransgenic(boolean isTransgenic){
    	this.isTransgenic=isTransgenic;
    }
    
    public boolean getIsTransgenic(){
    	return isTransgenic;
    }
    
    
    public String getAnnotationTreeExpressions(){
        return annotatedTreeExpressions;
    }
    
    public void setAnnotationTreeExpressions(String expressions){
    	annotatedTreeExpressions = expressions;
    }
    
    public String getAnnotationTreeExpressionNotes(){
        return annotatedTreeExpressionNotes;
    }
    
    public void setAnnotationTreeExpressionNotes(String notes){
    	annotatedTreeExpressionNotes = notes;
    }

    public String getAnnotationTreePatterns(){
        return annotatedTreePatterns;
    }
    
    public void setAnnotationTreePatterns(String patterns){
    	annotatedTreePatterns = patterns;
    }
    
    public void setNewid(String newid){
    	this.newid=newid;
    }
    
    public String getNewid() {
    	return newid;
    }

}
