package org.gudmap.models.submission;

import java.util.List;

import org.gudmap.models.DataProcessing;
import org.gudmap.models.SeqProtocol;
import org.gudmap.models.SupplementaryFiles;

public class SeqSubmissionModel extends ArraySeqSubmissionModel {
	
	 private DataProcessing[] dataProcessing;
	 private List<SupplementaryFiles>  rawFile;
	 private List<SupplementaryFiles> processedFile;
	 private SeqSampleModel seqSampleModel;
	 private SeqSeriesModel seqSeriesModel;
	 private SeqProtocol seqProtocol;
	
	
	
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
	    
	    public void setSeqSampleModel(SeqSampleModel seqSampleModel) {
	    	this.seqSampleModel=seqSampleModel;
	    }
	    
	    public SeqSampleModel getSeqSampleModel() {
	    	return seqSampleModel;
	    }
	    
	    public void setSeqSeriesModel(SeqSeriesModel seqSeriesModel) {
	    	this.seqSeriesModel=seqSeriesModel;
	    }
	    
	    public SeqSeriesModel getSeqSeriesModel() {
	    	return seqSeriesModel;
	    }
	    
	    public void setSeqProtocol(SeqProtocol seqProtocol) {
	    	this.seqProtocol=seqProtocol;
	    }
	    
	    public SeqProtocol getSeqProtocol() {
	    	return seqProtocol;
	    }

}
