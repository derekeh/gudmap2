package org.gudmap.models.submission;

import java.util.ArrayList;

import org.gudmap.models.ArraySeqTableBeanModel;

public class ArraySeqSubmissionModel extends SubmissionModel {
	
	 protected ArraySeqTableBeanModel arraySeqTableBeanModel; //contains series, sample and platform data
	 protected String filesLocation;
	
	
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

}
