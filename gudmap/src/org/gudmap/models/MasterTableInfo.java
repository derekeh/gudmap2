package org.gudmap.models;

public class MasterTableInfo {

    protected String id;
    protected String masterId;
    protected String sectionId;
    protected String title;
    protected String description;
    protected String platform;
    protected boolean selected;
    
    public void print() {
	System.out.println(" id = "+id);
	System.out.println(" masterId = "+masterId);
	System.out.println(" sectionId = "+sectionId);
	System.out.println(" title = "+title);
	System.out.println(" description = "+description);
	System.out.println(" platform = "+platform);
    }

    public MasterTableInfo() {

    }
    
    public MasterTableInfo(String id, String title, String description, String platform) {
    	this.id = id;
    	this.title = title;
    	this.description = description;
    	this.platform = platform;
    	this.selected = false;
    }
    
    public MasterTableInfo(String masterId, String sectionId, String title, String description, String platform) {
    	this.masterId = masterId;
    	this.sectionId = sectionId;
    	this.id = masterId + "_" + sectionId;
    	this.title = title;
    	this.description = description;
    	this.platform = platform;
    	this.selected = false;
    }
    
    public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMasterId() {
		return masterId;
	}
	
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	public String getSectionId() {
		return sectionId;
	}
	
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
