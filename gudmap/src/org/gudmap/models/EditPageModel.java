package org.gudmap.models;

public class EditPageModel {
	
	public EditPageModel() {
		
	}
	
	private String title;
	private String content_1;
	private String content_2;
	private String content_3;
	private String content_4;
	private String hash;
	private String alias;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setContent_1(String content_1){
		this.content_1=content_1;
	}
	
	public String getContent_1(){
		return content_1;
	}
	
	public void setContent_2(String content_2){
		this.content_2=content_2;
	}
	
	public String getContent_2(){
		return content_2;
	}
	
	public void setContent_3(String content_3){
		this.content_3=content_3;
	}
	
	public String getContent_3(){
		return content_3;
	}
	
	public void setContent_4(String content_4){
		this.content_4=content_4;
	}
	
	public String getContent_4(){
		return content_4;
	}
	
	public void setHash(String hash){
		this.hash=hash;
	}
	
	public String getHash(){
		return hash;
	}
	
	public void setAlias(String alias){
		this.alias=alias;
	}
	
	public String getAlias(){
		return alias;
	}

}
