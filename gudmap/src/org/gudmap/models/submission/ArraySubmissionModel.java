package org.gudmap.models.submission;

public class ArraySubmissionModel extends ArraySeqSubmissionModel {
	
	private String celFile;
	private String chpFile;
	private String rptFile;
	private String expFile;
	private String txtFile;
	private String tissue;
	
	
		public void setCelFile(String celFile) {
	        this.celFile = celFile;
	    }

	    public String getCelFile() {
	        return celFile;
	    }

	    public void setChpFile(String chpFile) {
	        this.chpFile = chpFile;
	    }

	    public String getChpFile() {
	        return chpFile;
	    }

	    public void setRptFile(String rptFile) {
	        this.rptFile = rptFile;
	    }

	    public String getRptFile() {
	        return rptFile;
	    }

	    public void setExpFile(String expFile) {
	        this.expFile = expFile;
	    }

	    public String getExpFile() {
	        return expFile;
	    }

	    public void setTxtFile(String txtFile) {
	        this.txtFile = txtFile;
	    }

	    public String getTxtFile() {
	        return txtFile;
	    }

	    public String getTissue() {
	    	return tissue;
	    }

	    public void setTissue(String tissue) {
	    	this.tissue = tissue;
	    }

}
