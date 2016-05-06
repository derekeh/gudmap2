package org.gudmap.models;

import java.util.ArrayList;
import java.util.List;

public class EditPageModel {
	
	public EditPageModel() {
		
	}
	private int oid;
	private String title;
	private String content_1;
	private String content_2;
	private String content_3;
	private String content_4;
	private String hash;
	private String alias;
	private String category;
	private int level;
	private String isVisible;
	private String modifiedBy;
	private String modifiedDate;
	private String username;
	private String url;
	private ArrayList<ArrayList<String>> highlights;
	
	
	public void setOid(int oid) {
		this.oid = oid;
	}
	
	public int getOid() {
		return oid;
	}
	
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
	
	public void setCategory(String category){
		this.category=category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setLevel(int level) {
		this.level=level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setIsVisible(String isVisible) {
		this.isVisible=isVisible;
	}
	
	public String getIsVisible() {
		return isVisible;
	}
	
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getModifiedDate() {
		return modifiedDate;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setHighlights(ArrayList<ArrayList<String>> highlights) {
		this.highlights = highlights;
	}
	
	public ArrayList<ArrayList<String>>  getHighlights() {
		return highlights;
	}
	
}
