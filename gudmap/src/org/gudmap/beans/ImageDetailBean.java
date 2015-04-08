package org.gudmap.beans;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.ImageDetailAssembler;
import org.gudmap.models.submission.ImageDetailModel;

@Named
@RequestScoped
public class ImageDetailBean {
	
    private ImageDetailModel imageDetailModel;  //object containing details of a submitted image
    private ImageDetailAssembler imageDetailAssembler;
	ArrayList<String[]> notesList;
	ArrayList<String> publicImagesList;
	String serialNo;
	String id;
	
    
    public ImageDetailBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.id = facesContext.getExternalContext().getRequestParameterMap().get("id");
        this.serialNo= facesContext.getExternalContext().getRequestParameterMap().get("serialNo");
        
        imageDetailAssembler = new ImageDetailAssembler();
        setup();

    	
    }
    
    public void setup() {
    	imageDetailModel = imageDetailAssembler.getData(id, serialNo);
        if (imageDetailModel != null) {
        	notesList = imageDetailModel.getAllImageNotesInSameSubmission();
            publicImagesList = imageDetailModel.getAllPublicImagesInSameSubmission();
        }
        else {
        	notesList = null;
        	publicImagesList = null;
    	}
    }
    
    public ImageDetailModel getImageDetailModel(){
        return imageDetailModel;
    }
    
    public void setImageDetailModel(ImageDetailModel imageDetailModel) {
    	this.imageDetailModel = imageDetailModel;
    }
    
    public String getSubmissionName () {
    	return "gudmap";

        /*if(Utils.getProject().equalsIgnoreCase("gudmap")){
            return "gudmap";
        }
        else if(Utility.getProject().equalsIgnoreCase("euregene")) {
            if(Utility.getSpecies().equalsIgnoreCase("mouse")) {
                return "euregene_mge";
            }
            else {
                return "euregene_xge";
            }
        }
        else {
            return "";
        }*/
    
    }
    
    public String getImageDir () {
	if (null == imageDetailModel)
	    return "";

    	String path = imageDetailModel.getFilePath();
    	/*if (path.startsWith("/", path.length()-1));
    	//path = path.substring(0, path.length()-2);
    	//String[] s = imageDetail.getFilePath().split("/");
        
        
    	return (s[s.length-2]+"/"+s[s.length-1]);*/
        
    	return path.substring(0,path.length()-1);
    }
	
	public String getThumbnail() {
		if (notesList == null)
			return "";
		try {
			int i = Integer.parseInt(serialNo);
			if (i <= notesList.size())
			  	return ((String[])notesList.get(i-1))[0];
			else
			  	return "";
		}
		catch (NumberFormatException e) {
			return "";
		}
	}	
	
	public String getAllNotes() {
		String allNotes = "";
		if (notesList == null)
			return allNotes;
			
		for(int i=0; i<notesList.size(); i++) {
			String[] note = (String[])notesList.get(i);
			if(i<notesList.size()-1)
				allNotes += note[0] + "!$!" + note[1] + "!%!";
			else
				allNotes += note[0] + "!$!" + note[1];
		}
		
		try {
			allNotes = URLEncoder.encode(allNotes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (null != allNotes) {
		    allNotes = allNotes.trim();
		    if (allNotes.equals("") || allNotes.equalsIgnoreCase("null"))
			allNotes = null;
		}
		
		return allNotes;
	}
	
	public String getPublicImages() {
		StringBuffer publicImages = new StringBuffer("");
		if(publicImagesList == null){
			return publicImages.toString();
		}
		for(int i=0;i<publicImagesList.size();i++){
			if(i < publicImagesList.size()-1){
				publicImages.append(publicImagesList.get(i)+"|");
			}
			else {
				publicImages.append(publicImagesList.get(i));
			}
		}
		
		return publicImages.toString();
	}

	public String getViewerFrameSourceName() {
		//just for testing the port to glenelgin
		return "http://glenelgin.hgu.mrc.ac.uk/mrciip/mrciip_gudmap.html";
	
		//return Utility.domainUrl+"mrciip/mrciip_gudmap.html";
	}

}
