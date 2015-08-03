package org.gudmap.models;

public class ImageFileModel {
	
	public ImageFileModel(){
		
	}
	
	private String path;
	private String name;
	private String width;
	private String height;
	private String length;
	private String type;
	private String absolutePath;
	private String baseUrl="http://glenelgin.hgu.mrc.ac.uk/Appfiles/images/";
	
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
		return length;
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

}
