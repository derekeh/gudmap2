package org.gudmap.beans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.gudmap.models.ImageFileModel;

@Named
@RequestScoped
public class FileManagementBean {
	private ArrayList<ImageFileModel> imageList =null;
	//private String imagedir="";
	private static File dir;
	private  String[] EXTENSIONS;
	private  FilenameFilter IMAGE_FILTER;
	
	public FileManagementBean() {
		
	}
	
	public void init(String imagedir) {
		dir = new File("/export/system0/MAWWW/Public/html/Appfiles/images/"+imagedir);
		EXTENSIONS = new String[]{
		        "jpg", "png", "jpeg" // and other formats you need
		};
		
		IMAGE_FILTER = new FilenameFilter() {

	        @Override
	        public boolean accept(final File dir, final String name) {
	            for (final String ext : EXTENSIONS) {
	                if (name.endsWith("." + ext)) {
	                    return (true);
	                }
	            }
	            return (false);
	        }
	    };
	}
	
	
	  // File representing the folder that you select using a FileChooser
    //static final File dir = new File("/export/data0/documents/image_uploads");

    // array of supported extensions (use a List if you prefer)
   /* static final String[] EXTENSIONS = new String[]{
        "jpg", "png", "jpeg" // and other formats you need
    };*/
    // filter to identify images based on their extensions
/*    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };*/
	
	
	
	public ArrayList<ImageFileModel> getImageList(String directory) {
		init(directory);
		if (dir.isDirectory()) { // make sure it's a directory
			imageList = new ArrayList<ImageFileModel>();
			ImageFileModel imageFileModel;
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img = null;
                imageFileModel = new ImageFileModel();
                try {
                    img = ImageIO.read(f);
                    imageFileModel.setName(f.getName());
                    imageFileModel.setHeight(String.valueOf(img.getHeight()));
                    imageFileModel.setWidth(String.valueOf(img.getWidth()));
                    imageFileModel.setLength(String.valueOf(f.length()));
                    imageFileModel.setAbsolutePath(f.getAbsolutePath());
                    imageList.add(imageFileModel);
                    
                    // you probably want something more involved here
                    // to display in your UI
                    /*System.out.println("image: " + f.getName());
                    System.out.println(" width : " + img.getWidth());
                    System.out.println(" height: " + img.getHeight());
                    System.out.println(" size  : " + f.length());*/
                } catch (final IOException e) {
                    // handle errors here
                }
            }
            
        }
		return imageList;
	}
	
	/*public void ListDir(){
		File folder = new File("/export/data0/documents/image_uploads");
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
	}*/

}
