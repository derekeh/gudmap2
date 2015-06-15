package org.gudmap.models.submission;

public class ArraySubmissionModel extends ArraySeqSubmissionModel {
	
	private PlatformModel platformModel;
	private String tissue;
	
	
	public void setPlatformModel(PlatformModel platformModel) {
        this.platformModel = platformModel;
    }

    public PlatformModel getPlatformModel() {
        return platformModel;
    }

	public String getTissue() {
	    	return tissue;
	}

	public void setTissue(String tissue) {
	    	this.tissue = tissue;
	}

}
