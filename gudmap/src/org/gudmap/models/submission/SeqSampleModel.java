package org.gudmap.models.submission;

public class SeqSampleModel extends SampleModel{
	
	private String stageFormat;
    private String genotype;
    private String sampleNotes;
    private String libraryPoolSize;
    private String libraryReads;
    private String readLength;
    private String meanInsertSize;
    

    
    public void setStageFormat(String stageFormat) {
    	this.stageFormat = stageFormat;
    }

    public String getStageFormat() {
        return stageFormat;
    }

    public void setGenotype(String genotype) {
    	this.genotype = genotype;
    }

    public String getGenotype() {
        return genotype;
    }
    
    public void setSampleNotes(String sampleNotes) {
        this.sampleNotes = sampleNotes;
    }

    public String getSampleNotes() {
        return sampleNotes;
    }
    
    public void setLibraryPoolSize(String libraryPoolSize) {
        this.libraryPoolSize = libraryPoolSize;
    }

    public String getLibraryPoolSize() {
        return libraryPoolSize;
    }
    
    public void setLibraryReads(String libraryReads) {
        this.libraryReads = libraryReads;
    }

    public String getLibraryReads() {
        return libraryReads;
    }
    
    public void setReadLength(String readLength) {
        this.readLength = readLength;
    }

    public String getReadLength() {
        return readLength;
    }
    
    public void setMeanInsertSize(String meanInsertSize) {
        this.meanInsertSize = meanInsertSize;
    }

    public String getMeanInsertSize() {
        return meanInsertSize;
    }


}
