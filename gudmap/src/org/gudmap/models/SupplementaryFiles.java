package org.gudmap.models;

public class SupplementaryFiles {
	
	private String filename;
	private String filesize;
    private String filetype;
    private String celFile;
	private String chpFile;
	private String rptFile;
	private String expFile;
	private String txtFile;
	private String file_location;
	private String tissue;
	
	public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
    
    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFilesize() {
        return filesize;
    }
    
    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }
		
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
	
	public String getFile_location() {
    	return file_location;
    }

	public void setFileLocation(String file_location) {
    	this.file_location = file_location;
    }

    

}
