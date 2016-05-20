package org.gudmap.beans;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.ImageFileModel;

@Named
@RequestScoped
public class FileManagementBean {
	private ArrayList<ImageFileModel> imageList =null;
	private ArrayList<ImageFileModel> webfileList =null;
	private static File dir;
	private  String[] EXTENSIONS;
	private  FilenameFilter IMAGE_FILTER;
	
	public FileManagementBean() {
		
	}
	
	public void init(String imagedir) {
		//dir = new File("/export/system0/MAWWW/Public/html/Appfiles/images/"+imagedir);
		/*dir = new File("/opt/MAWWW/Public/html/Appfiles/images/"+imagedir);*/
		dir = new File(Globals.imagePath+imagedir);
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
                    
                } catch (final IOException e) {
                    // handle errors here
                }
            }
            
        }
		return imageList;
	}
	
	public void init2(String imagedir) {
		dir = new File(Globals.webfilePath+imagedir);
		EXTENSIONS = new String[]{
		        "txt", "pdf", "doc", "docx", "xls", "xlsx" // and other formats you need
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
	
	
	public ArrayList<ImageFileModel> getWebfileList(String directory) {
		init2(directory);
		if (dir.isDirectory()) { // make sure it's a directory
			webfileList = new ArrayList<ImageFileModel>();
			ImageFileModel imageFileModel;
			File[] files = dir.listFiles(IMAGE_FILTER);
			//sort files alphabetically
			Arrays.sort(files);
			//for (final File f : dir.listFiles(IMAGE_FILTER)) {
            for (File f : files) {
                //BufferedImage img = null;
                imageFileModel = new ImageFileModel();
                //try {
                    //img = ImageIO.read(f);
                    imageFileModel.setName(f.getName());
                    imageFileModel.setLength(String.valueOf(f.length()));
                    imageFileModel.setAbsolutePath(f.getAbsolutePath());
                    webfileList.add(imageFileModel);
                //} catch (final IOException e) {
                    // handle errors here
               // }
            }
            
        }
		return webfileList;
	}
	


}
