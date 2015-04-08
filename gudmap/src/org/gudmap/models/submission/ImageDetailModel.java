package org.gudmap.models.submission;

import java.util.ArrayList;

public class ImageDetailModel extends ImageInfoModel{
	
	 protected String geneSymbol;
	  protected String geneName;
	  protected String age;
	  protected ArrayList<String[]> allImageNotesInSameSubmission;
	  protected ArrayList<String> allPublicImagesInSameSubmission;
	  
	  public String getGeneSymbol() {
		return geneSymbol;
     }

     public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	  }

     public String getGeneName() {
		return geneName;
     }

     public void setGeneName(String geneName) {
		this.geneName = geneName;
	  }

	  public String getAge() {
	    return age;
	  }

	  public void setAge(String age) {
	    this.age = age;
	  }

	  public ArrayList<String[]> getAllImageNotesInSameSubmission() {
		  return allImageNotesInSameSubmission;
	  }
	  
	  public void setAllImageNotesInSameSubmission(ArrayList<String[]> allImageNotesInSameSubmission) {
		  this.allImageNotesInSameSubmission = allImageNotesInSameSubmission;
	  }
	  
	  public ArrayList<String> getAllPublicImagesInSameSubmission() {
		  return allPublicImagesInSameSubmission;
	  }
	  
	  public void setAllPublicImagesInSameSubmission(ArrayList<String> allPublicImagesInSameSubmission){
		  this.allPublicImagesInSameSubmission = allPublicImagesInSameSubmission;
	  }

   public String getOptViewerUrl() {
	String ret = clickFilePath;
	String worker= "";
	if (null != geneSymbol && !geneSymbol.trim().equals(""))
	    worker += "%20Gene:"+geneSymbol;
	if (null != stage && !stage.trim().equals(""))
	    worker += "%20Stage:"+stage;
	if (null != accessionId && !accessionId.trim().equals(""))
	    worker += "%20Accession:"+accessionId;

	if (!worker.equals(""))
	    ret +="&viewerTitle="+worker;
	
	return ret;
   }

}
