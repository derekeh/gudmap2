package org.gudmap.models.submission;

public class ImageInfoModel {
	
	protected String accessionId;
	protected String stage; // theiler stage
	protected String specimenType;
	protected String filePath;
	protected String clickFilePath;
	protected String serialNo;
	protected String note;
	protected String assayType;
	protected String unique_image;

	public void print() {
		System.out.println(" accessionId = "+ accessionId);
		System.out.println(" stage = "+stage);
		System.out.println(" specimenType = "+ specimenType);
		System.out.println(" filePath = "+filePath);
		System.out.println(" clickFilePath = "+ clickFilePath);
		System.out.println(" serialNo = "+serialNo);
		System.out.println(" note = "+note);
		System.out.println(" assayType = "+assayType);
		System.out.println(" unique_image = "+unique_image);
	}

    public String getAccessionId() {
    	return accessionId;
    }
    
    public void setAccessionId(String accessionId) {
    	this.accessionId = accessionId;
    }
    
    public String getStage() {
    	return stage;
    }

	  public void setStage(String stage) {
	    this.stage = stage;
	  }

	  public String getSpecimenType() {
	    return specimenType;
	  }
		  
	  public void setSpecimenType(String specimenType) {
	    this.specimenType = specimenType;
	  }

	  public String getFilePath() {
	    return filePath;
	  }
		  
	  public void setFilePath(String filePath) {
	    this.filePath = filePath;
	  }

	  public String getClickFilePath() {
	      // do not know why zoom-viewer dose not work for microarray tif 
	      // so put 'microarray' into specimenType so that
	      // microarray click image can be treated
	      if (null != specimenType && specimenType.equals("microarray")){	    	  
	    	  return filePath;
	      }

	      if (null == clickFilePath || clickFilePath.endsWith("tif")) {
	    	  String ret = org.gudmap.globals.Globals.applicationRoot +"db/zoom_viewer.html?id="+accessionId;
			  if (null == serialNo)
			      ret += "&serialNo=1";
			  else
			      ret =ret + "&serialNo="+serialNo;
			  
			  return ret;
	      }
	      
	      return clickFilePath;
	  }
		  
	  public void setClickFilePath(String clickFilePath) {
	    this.clickFilePath = clickFilePath;
	  }

	  public String getSerialNo() {
	    return serialNo;
	  }
		  
	  public void setSerialNo(String input) {
	    serialNo = input;
	    if (null != serialNo) {
			serialNo = serialNo.trim();
			if (serialNo.equals(""))
			    serialNo = null;
		    }
	  }

	  public String getNote() {
	    return note;
	  }
		  
	  public void setNote(String input) {
	    note = input;
	    if (null == note || note.trim().toLowerCase().equals("null"))
	    	note = "";
	  }
	  
	  public String getAssayType() {
		return assayType;
      }

      public void setAssayType(String assayType) {
    	  this.assayType = assayType;
	  }
      
      public String getUniqeImage() {
  		return unique_image;
      }

        public void setUniqueImage(String unique_image) {
        	this.unique_image = unique_image;
  	  }

	  

}
