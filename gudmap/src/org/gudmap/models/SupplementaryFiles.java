package org.gudmap.models;

import java.util.List;

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
	private List<SupplementaryFiles> rawFiles;
	private List<SupplementaryFiles> processedFiles;
	private List<SupplementaryFiles> rnaSeqQCFiles;
	
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
	
	public void setRawFile(List<SupplementaryFiles> file) {
        rawFiles = file;
    }

    public List<SupplementaryFiles> getRawFile() {
        return rawFiles;
    }

    public void setProcessedFile(List<SupplementaryFiles> file) {
       processedFiles = file;
    }

    public List<SupplementaryFiles> getProcessedFile() {
        return processedFiles;
    }

    public void setQCFile(List<SupplementaryFiles> file) {
        rnaSeqQCFiles = file;
    }

    public List<SupplementaryFiles> getQCFile() {
         return rnaSeqQCFiles;
    }    

}
