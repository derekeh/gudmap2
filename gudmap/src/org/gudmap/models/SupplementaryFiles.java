package org.gudmap.models;

public class SupplementaryFiles {
	
	private String filename;
	private String filesize;
    private String filetype;

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

}
