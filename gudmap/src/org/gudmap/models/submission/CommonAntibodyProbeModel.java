package org.gudmap.models.submission;

import java.util.ArrayList;

public class CommonAntibodyProbeModel {
	
	protected String geneSymbol;
	protected String geneName;
	protected String geneID;
	protected String seqStatus;
	protected String seqInfo;
	protected String type;
	protected String notes;
	protected String maprobeID;
	protected ArrayList <String[]>ishSubmissions; 
	protected ArrayList<String[]> ishFilteredSubmissions; 
    
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneName(String value) {
        geneName = value;
	if (null != geneName && geneName.equals("0"))
	    geneName = null;
    }

    public String getGeneName() {
        return geneName;
    }
    
    public void setGeneID(String geneID) {
        this.geneID = geneID;
    }

    public String getGeneID() {
        return geneID;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
    public String getMaprobeID() {
        return maprobeID;
    }

    public void setMaprobeID(String maprobeID) {
        this.maprobeID = maprobeID;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
    
    public String getSeqStatus() {
        return seqStatus;
    }

    public void setSeqStatus(String value) {
	if (null == value)
            seqStatus = "";

        else if (value.equals("FULLY_SEQUENCED".trim())) {
            seqStatus = "Fully Sequenced.";
        } else if (value.equals("PARTIALLY_SEQUENCED".trim())) {
            seqStatus = "Partially Sequenced.";
        } else {
            seqStatus = "Unsequenced.";
        }
    }

    /**
     * @param sequenceInfo
     */
    public void setSeqInfo(String seqInfo) {
        this.seqInfo = seqInfo;
    }

    public String getSeqInfo() {
        return seqInfo;
    }
    
    public ArrayList<String[]> getIshSubmissions() { 	  	
        return ishSubmissions;
    }
	
    public void setIshSubmissions(ArrayList<String[]> values) {
        ishSubmissions = values;

	if (null == values) {
	    if (null != ishFilteredSubmissions)
		ishFilteredSubmissions.clear();
	    return;
	}

        ishFilteredSubmissions = new ArrayList<String[]>();        
        String[] arr = null;
        for (int i=0; i<ishSubmissions.size(); i++){
        	arr = (String[]) ishSubmissions.get(i);
        	if (arr[9].equalsIgnoreCase(maprobeID) || arr[6].equalsIgnoreCase(maprobeID))
        		ishFilteredSubmissions.add(arr);
        }
    }
	
    public ArrayList<String[]> getIshFilteredSubmissions() { 	  	
        return ishFilteredSubmissions;
    }

}
