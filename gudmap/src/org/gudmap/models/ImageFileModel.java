package org.gudmap.models;
import org.apache.commons.io.FileUtils;

public class ImageFileModel {
	
	public ImageFileModel(){
		
	}
	
	private String path;
	private String name;
	private String width;
	private String height;
	private String length;
	private String size;
	private String type;
	private String absolutePath;
	private String baseUrl="http://glenelgin.hgu.mrc.ac.uk/Appfiles/images/";
	private String docUrl="http://glenelgin.hgu.mrc.ac.uk/Appfiles/docs/";
	//private String baseUrl="http://glenelgin.hgu.mrc.ac.uk/Appfiles/";
	private String imageDir="images/";
	private String webfileDir="docs/";
	private int mb=1048576;
	
	public void setPath(String path) {
		this.path=path;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setWidth(String width) {
		this.width=width;
	}
	
	public String getWidth() {
		return width;
	}
	
	public void setHeight(String height) {
		this.height=height;
	}
	
	public String getHeight() {
		return height;
	}
	
	public void setLength(String length) {
		this.length=length;
	}
	
	public String getLength() {
		Long output = Long.valueOf(length);
		length = FileUtils.byteCountToDisplaySize(output);
		return length;
	}
	
	/*public String getLength() {
		int mblength = Integer.parseInt(length)/1048576;
		length = String.valueOf(mblength);
		if(mblength<1)
			return length+"K";
		else
			return length+"MB";
	}*/
	
	public void setSize(String size){
		this.size = size;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setType(String type) {
		this.type=type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setAbsolutePath(String absolutePath){
		this.absolutePath = absolutePath;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public String getDocUrl() {
		return docUrl;
	}

}
