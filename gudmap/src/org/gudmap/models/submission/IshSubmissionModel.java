package org.gudmap.models.submission;

import java.util.ArrayList;

public class IshSubmissionModel extends SubmissionModel{
	
		protected ProbeModel probeModel;
		
	    /*protected ArrayList<String[]> linkedPublications;
	    
	    protected String[] acknowledgements;
	    
	    protected ArrayList<Object> linkedSubmissions;*/
	    
	    protected ArrayList<?> annotationTree;
	    
	    protected ExpressionDetailModel [] expressionDetailModel;
	    protected AntibodyModel antibodyModel;
	    protected String tissue; 
	    /** imageDetailModel: filePath is the wlz download url
	     *	          clickFilePath is the iip viewer url
	    */
	    protected ImageDetailModel imageDetailModel = null;
	    
	    protected String annotationTreeExpressions;
	    protected String annotationTreePatterns;
	    protected String annotationTreeExpressionNotes;


	    public IshSubmissionModel() {
	    
	    }

	    public String getGeneSymbol() {
			String str = assayType.toLowerCase();
			String ret = null;
	
			if (-1 != str.indexOf("ish")) {
			    if (null != probeModel)
				ret = probeModel.getGeneSymbol();
			} else if (-1 == str.indexOf("tg")) {
			    if (null != antibodyModel)
				ret = antibodyModel.getGeneSymbol();
			} else
			    ret = super.getGeneSymbol();
		    
			if (null != ret && ret.trim().equals(""))
			    ret = null;
			
			return ret;
		    }
		    public String getGeneName() {
			String str = assayType.toLowerCase();
			String ret = null;
	
			if (-1 != str.indexOf("ish")) {
			    if (null != probeModel)
				ret = probeModel.getGeneName();
			} else if (str.contains("tg")) {
			    if (null != antibodyModel)
				ret = antibodyModel.getGeneName();
			    if (null != probeModel)
				ret = probeModel.getGeneName();
			}
		    
			if (null != ret && ret.trim().equals(""))
			    ret = null;
			
			return ret;
	    }
	    
	    public void setProbeModel(ProbeModel probeModel) {
	        this.probeModel = probeModel;
	    }

	    public ProbeModel getProbeModel() {
	        return probeModel;
	    }

	    /*public ArrayList<String[]> getLinkedPublications() {
	        return linkedPublications;
	    }

	    public void setLinkedPublications(ArrayList<String[]> linkedPublications) {
	        this.linkedPublications = linkedPublications;
	    }
	    
	    public ArrayList<Object> getLinkedSubmissions() {
	        return linkedSubmissions;
	    }

	    public void setLinkedSubmissions(ArrayList<Object> linkedSubmissions) {
	        this.linkedSubmissions = linkedSubmissions;
	    }
	    
	    public String[] getAcknowledgements() {
	        return acknowledgements;
	    }

	    public void setAcknowledgements(String[] acknowledgements) {
	        this.acknowledgements = acknowledgements;
	    }*/

	    
	    public ArrayList<?> getAnnotationTree(){
	        return annotationTree;
	    }
	    
	    public void setAnnotationTree(ArrayList<?> annotationTree){
	        this.annotationTree = annotationTree;
	    }
	    
	    public ExpressionDetailModel [] getExpressionDetailModel (){
	        return expressionDetailModel;
	    }
	    
	    public void setExpressionDetailModel(ExpressionDetailModel [] expressionDetailModel) {
	        this.expressionDetailModel = expressionDetailModel;
	    }
	    
	    public AntibodyModel getAntibodyModel() {
	    	return antibodyModel;
	    }
	    
	    public void setAntibodyModel(AntibodyModel antibodyModel) {
	    	this.antibodyModel = antibodyModel;
	    }
	    
	    public String getTissue() {
	    	return tissue;
	    }
	    
	    public void setTissue(String tissue) {
	    	this.tissue = tissue;
	    }  
	    
	    public ImageDetailModel getImageDetailModel() {
	    	return imageDetailModel;
	    }
	    
	    public void setImageDetailModel(ImageDetailModel imageDetailModel) {
	    	this.imageDetailModel = imageDetailModel;
	    } 
	    
	    
	    public String getAnnotationTreeExpressions() {
	    	return annotationTreeExpressions;
	    }
	    public void setAnnotationTreeExpressions (String annotationTreeExpressions) {
	    	this.annotationTreeExpressions = annotationTreeExpressions;
	    }
	    
	    public String getAnnotationTreePatterns() {
	    	return annotationTreePatterns;
	    }
	    public void setAnnotationTreePatterns(String annotationTreePatterns) {
	    	this.annotationTreePatterns = annotationTreePatterns;
	    }
	    
	    public String getAnnotationTreeExpressionNotes() {
	    	return annotationTreeExpressionNotes;
	    }
	    public void setAnnotationTreeExpressionNotes (String annotationTreeExpressionNotes) {
	    	this.annotationTreeExpressionNotes = annotationTreeExpressionNotes;
	    }
	

}
