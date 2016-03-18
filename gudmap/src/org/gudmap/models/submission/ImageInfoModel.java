package org.gudmap.models.submission;

public class ImageInfoModel {
	
	protected String oid;
	protected String accessionId;
	protected String stage; // theiler stage
	protected String specimenType;
	protected String filePath;
	protected String thumbnailPath;
	protected String clickFilePath;
	protected String serialNo;
	protected String note;
	protected String assayType;
	protected String unique_image;
	protected boolean selected=false;
	protected String species;
	protected String image_type;
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
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

	  public String getThumbnailPath() {
	    return thumbnailPath;
	  }
		  
	  public void setThumbnailPath(String thumbnailPath) {
	    this.thumbnailPath = thumbnailPath;
	  }

	  public String getClickFilePath() {
	      // do not know why zoom-viewer dose not work for microarray tif 
	      // so put 'microarray' into specimenType so that
	      // microarray click image can be treated
	      if (null != specimenType && specimenType.equals("microarray")){	    	  
	    	  return filePath;
	      }
	
	      if (null == clickFilePath || clickFilePath.endsWith("tif")) {
	    	  String ret = org.gudmap.globals.Globals.applicationRoot +"db/zoom_viewer.jsf?id="+accessionId;
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
        
      public void setSelected(boolean selected){
    	  this.selected = selected;
      }
      
      public boolean getSelected() {
    	  return selected;
      }
	  
      public String getSpecies() {
  		return species;
      }

      public void setSpecies(String species) {
        	this.species = species;
  	  }

      public String getImageType() {
  		return image_type;
      }

      public void setImageType(String image_type) {
        	this.image_type = image_type;
  	  }
      
}
