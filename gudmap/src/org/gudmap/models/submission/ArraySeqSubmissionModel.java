package org.gudmap.models.submission;

import java.util.ArrayList;

import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.SupplementaryFiles;

public class ArraySeqSubmissionModel extends SubmissionModel {
	
	 protected ArraySeqTableBeanModel arraySeqTableBeanModel; //contains series, sample and platform data
	 protected String filesLocation;
	 protected SampleModel sampleModel;
	 protected SeriesModel seriesModel;
	
	 protected SupplementaryFiles supplementaryFiles;
	
	
		public ArraySeqTableBeanModel getArraySeqTableBeanModel() {
			return arraySeqTableBeanModel;
		}
		
		public void setArraySeqTableBeanModel(ArraySeqTableBeanModel arraySeqTableBeanModel) {
			this.arraySeqTableBeanModel = arraySeqTableBeanModel;
		}
	
	    
	    public void setFilesLocation(String filesLocation) {
	        this.filesLocation = filesLocation;
	    }

	    public String getFilesLocation() {
	        return filesLocation;
	    }
	    
	    public void setSampleModel(SampleModel sampleModel) {
	        this.sampleModel = sampleModel;
	    }

	    public SampleModel getSampleModel() {
	        return sampleModel;
	    }
	    
	    public void setSeriesModel(SeriesModel seriesModel) {
	        this.seriesModel = seriesModel;
	    }

	    public SeriesModel getSeriesModel() {
	        return seriesModel;
	    }
	    
	    public void setSupplementaryFiles(SupplementaryFiles supplementaryFiles){
	    	this.supplementaryFiles = supplementaryFiles;
	    }
	    
	    public SupplementaryFiles getSupplementaryFiles() {
	    	return supplementaryFiles;
	    }

}
