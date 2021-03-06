package org.gudmap.models;

public class DataProcessing {
	
	private String filetype;
	private String filename;
	private String rawOrProcessed;
	private String proStep;
	private String build;
	private String alignedGenome;
    private String unalignedGenome;
    private String rnaReads;
    private String fiveThreeRatio;
    private String formatContent;
    private String numberOfReads;
    private String beforeCleanUpReads;
    private String pairedEnd;

    public void setFiletype(String filetype) {
    	this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }
    
    public void setFilename(String filename) {
    	this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
    
    public void setRawOrProcessed(String rawOrProcessed) {
    	this.rawOrProcessed = rawOrProcessed;
    }

    public String getRawOrProcessed() {
        return rawOrProcessed;
    }
    
    
    public void setProStep(String proStep) {
    	this.proStep = proStep;
    }

    public String getProStep() {
        return proStep;
    }
    
    public void setBuild(String build) {
        this.build = build;
    }

    public String getBuild() {
        return build;
    }  

    public void setAlignedGenome(String alignedGenome) {
    	this.alignedGenome = alignedGenome;
    }

    public String getAlignedGenome() {
        return alignedGenome;
    }   

    public void setUnalignedGenome(String unalignedGenome) {
    	this.unalignedGenome = unalignedGenome;
    }

    public String getUnalignedGenome() {
        return unalignedGenome;
    }

    public void setRnaReads(String rnaReads) {
    	this.rnaReads = rnaReads;
    }

    public String getRnaReads() {
        return rnaReads;
    }

    public void setFiveThreeRatio(String fiveThreeRatio) {
    	this.fiveThreeRatio = fiveThreeRatio;
    }

    public String getFiveThreeRatio() {
        return fiveThreeRatio;
    }

    public void setFormatContent(String formatContent) {
    	this.formatContent = formatContent;
    }

    public String getFormatContent() {
        return formatContent;
    }

    public void setNumberOfReads(String numberOfReads) {
    	this.numberOfReads = numberOfReads;
    }

    public String getNumberOfReads() {
        return numberOfReads;
    }
 
    public void setBeforeCleanUpReads(String beforeCleanUpReads) {
    	this.beforeCleanUpReads = beforeCleanUpReads;
    }

    public String getBeforeCleanUpReads() {
        return beforeCleanUpReads;
    }
    
    public void setPairedEnd(String pairedEnd) {
    	this.pairedEnd = pairedEnd;
    }

    public String getPairedEnd() {
        return pairedEnd;
    }

}
