package org.gudmap.models.submission;

import java.util.ArrayList;

public class ImageDetailModel extends ImageInfoModel{
	
	 protected String geneSymbol;
	  protected String geneName;
	  protected String mgiGeneId;
	  protected String age;
	  protected ArrayList<String[]> allImageNotesInSameSubmission;
	  protected ArrayList<String> allPublicImagesInSameSubmission;
	  protected String sibling;
	  protected String group;	
	  protected String groupTitle;
	  protected String imageTitle;
	  protected String source;	  
	  protected String image;	  
	  
	  
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

     public String getMgiGeneId() {
		return mgiGeneId;
     }

     public void setMgiGeneId(String mgiGeneId) {
		this.mgiGeneId = mgiGeneId;
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
   
   public void setSibling(String sibling){
	   this.sibling = sibling;
   }
   public String getSibling(){
	   return sibling;
   }

   public void setGroup(String group){
	   this.group = group;
   }
   public String getGroup(){
	   return group;
   }

   public void setGroupTitle(String groupTitle){
	   this.groupTitle = groupTitle;
   }
   public String getGroupTitle(){
	   return groupTitle;
   }

   public void setImageTitle(String imageTitle){
	   this.imageTitle = imageTitle;
   }
   public String getImageTitle(){
	   return imageTitle;
   }

   public void setSource(String source){
	   this.source = source;
   }
   public String getSource(){
	   return source;
   }

   public void setImage(String image){
	   this.image = image;
   }
   public String getImage(){
	   return image;
   }
   
}
