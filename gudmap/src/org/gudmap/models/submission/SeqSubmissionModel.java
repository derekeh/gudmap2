package org.gudmap.models.submission;

import java.util.List;

import org.gudmap.models.DataProcessing;
import org.gudmap.models.SupplementaryFiles;

public class SeqSubmissionModel extends ArraySeqSubmissionModel {
	
	 private DataProcessing[] dataProcessing;
	 private List<SupplementaryFiles>  rawFile;
	 private List<SupplementaryFiles> processedFile;
	
	
	  public void setDataProcessing(DataProcessing[] dataProcessing) {
	    	this.dataProcessing = dataProcessing;
	    }

	    public DataProcessing[] getDataProcessing() {
	        return dataProcessing;
	    }


	    public void setRawFile(List<SupplementaryFiles> rawFile) {
	        this.rawFile = rawFile;
	    }

	    public List<SupplementaryFiles> getRawFile() {
	        return rawFile;
	    }

	    public void setProcessedFile(List<SupplementaryFiles> processedFile) {
	       this.processedFile = processedFile;
	    }

	    public List<SupplementaryFiles> getProcessedFile() {
	        return processedFile;
	    }

}
