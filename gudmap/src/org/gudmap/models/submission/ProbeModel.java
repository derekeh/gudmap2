package org.gudmap.models.submission;

import java.util.ArrayList;

public class ProbeModel extends CommonAntibodyProbeModel {
	
    private String probeName;
    private String geneIdUrl;
    private String source;
    private String strain;
    private String tissue;
    private String geneType;
    private String labelProduct;
    private String visMethod;
    private String cloneName;
    private String additionalCloneName;
    private String genbankID;
    private String probeNameURL;
    private String genbankURL;
    private String maProbeURL;
    private String seq5Loc;
    private String seq3Loc;
    private String seq5Primer;
    private String seq3Primer;
    private ArrayList<String> maprobeNotes;
    private String probeNameSource; // specify where the probe name come from
    //private ArrayList fullSequence;
    private String[] fullSequence;
    private String labProbeId; 


    private String maprobeNoteString;

    public String[] getFullSequence() {
    	return fullSequence;
	}

    public void setFullSequence(String[] fullSequence) {
    	this.fullSequence = fullSequence;
    }
    

    public void setProbeName(String probeName) {
        this.probeName = probeName;
    }

    public String getProbeName() {
        return probeName;
    }

  
    
    public void setGeneIdUrl(String geneIdUrl) {
        this.geneIdUrl = geneIdUrl;
    }

    public String getGeneIdUrl() {
        return geneIdUrl;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getStrain() {
        return strain;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public String getTissue() {
        return tissue;
    }


    public String getGeneType() {
        return geneType;
    }

    public void setGeneType(String geneType) {
        this.geneType = geneType;
    }

    public String getLabelProduct() {
        return labelProduct;
    }

    public void setLabelProduct(String labelProduct) {
        this.labelProduct = labelProduct;
    }

    public String getVisMethod() {
        return visMethod;
    }

    public void setVisMethod(String visMethod) {
        this.visMethod = visMethod;
    }

    public String getGenbankID() {
        return genbankID;
    }

    public void setGenbankID(String genbankID) {
        this.genbankID = genbankID;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }

    public String getAdditionalCloneName() {
        return additionalCloneName;
    }

    public void setAdditionalCloneName(String additionalCloneName) {
        this.additionalCloneName = additionalCloneName;
    }


    public String getProbeNameURL() {
        return probeNameURL;
    }

    public void setProbeNameURL(String value) {
        if (null != probeName) {
            probeNameURL = value;
        } else {
            probeNameURL = null;
        }
       if (null != probeNameURL) {
		   probeNameURL = probeNameURL.trim();
		   if (probeNameURL.equals(""))
		       probeNameURL = null;
       }
    }

    public String getGenbankURL() {
        return genbankURL;
    }

    public void setGenbankURL(String genbankURL) {
        this.genbankURL = genbankURL;
    }

    public String getMaProbeURL() {
        return maProbeURL;
    }

    public void setMaProbeURL(String maProbeURL) {
        this.maProbeURL = maProbeURL;
    }



    public String getSeq5Loc() {
        return seq5Loc;
    }

    public void setSeq5Loc(String value) {
        if (null == value || value.equals("0") || value.equals("n/a")) 
            seq5Loc = "";
        else 
            seq5Loc = value;
    }

    public String getSeq3Loc() {
        return seq3Loc;
    }

    public void setSeq3Loc(String value) {
        if (null == value || value.equals("0") || value.equals("n/a"))
            seq3Loc = "";
        else 
            seq3Loc = value;
    }
    
    // display info of probe database in the submission detail page
    public String getSeq5Primer() {
    	return seq5Primer;
    }
    
    public void setSeq5Primer(String value) {
        if (null == value || value.equals("n/a")) 
        	seq5Primer = "";
        else 
        	seq5Primer = value;
    }
    
    public String getSeq3Primer() {
    	return seq3Primer;
    }
    
    public void setSeq3Primer(String value) {
        if (null == value || value.equals("n/a")) 
        	seq3Primer = "";
        else 
        	seq3Primer = value;
    }
    
    public ArrayList<String> getMaprobeNotes() {
    	return maprobeNotes;
    }
    
    public void setMaprobeNotes(ArrayList<String> maprobeNotes) {
    	this.maprobeNotes = maprobeNotes;
    }
    
    public String getProbeNameSource() {
    	return probeNameSource;
    }
    
    public void setProbeNameSource(String probeNameSource) {
    	this.probeNameSource = probeNameSource;
    }
    
    public String getMaprobeNoteString() {
    	return maprobeNoteString;
    }
    
    public void setMaprobeNoteString(String maprobeNoteString) {
    	this.maprobeNoteString = maprobeNoteString;
    }
    
    public void setLabProbeId(String labProbeId) {
        this.labProbeId = labProbeId;
    }

    public String getLabProbeId() {
        return labProbeId;
    }
 


}
