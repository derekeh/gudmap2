package org.gudmap.beans;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.models.CollectionInfoModel;

@Named
@RequestScoped
public class NewCollectionBean {
	
	private String collection_name="";
	private String collection_description="";
	private String collection_status="public";
	private String collection_type="";
	private CollectionInfoModel collectionInfoModel;
	
	public NewCollectionBean() {
		
	}
	
	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}

	public String getCollection_name() {
		return collection_name;
	}
	
	public void setCollection_description(String collection_description) {
		this.collection_description = collection_description;
	}

	public String getCollection_description() {
		return collection_description;
	}
	
	public void setCollection_status(String collection_status) {
		this.collection_status = collection_status;
	}

	public String getCollection_status() {
		return collection_status;
	}
	
	public void setCollection_type(String collection_type) {
		this.collection_type = collection_type;
	}

	public String getCollection_type() {
		return collection_type;
	}
	
	public String submit(List<String> selectedItems) {
		collectionInfoModel=new CollectionInfoModel();
		
		return "browseCollectionListTablePage";
	
	}
	
	public String cancel() {
		
		return "database_homepage";
	
	}
	
	public void reset() {
		collection_name="";
		collection_description="";
		collection_status="public";
		collection_type="";
	}
	
	
}
