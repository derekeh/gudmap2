package org.gudmap.models.submission;

public class ExpressionPatternModel {
	
	private String pattern;
    private String locations;
    private String patternImage;
    private boolean selected;
    private int expressionId;
    private int patternId;
    private int locationId;
    
    private String locationAPart; // alphabetical part of location when its value in 'adjacent to xxxxx' format
    private String locationNPart; // numerical part of location when its value in 'adjacent to xxxxx' format
    
    public ExpressionPatternModel() {
    }
    
    public void setPattern(String pattern){
        this.pattern = pattern;
    }
    
    public String getPattern(){
        return pattern;
    }
    
    public void setLocations(String locations){
        this.locations = locations;
    }
    
    public String getLocations(){
        return locations;
    }
    
    public void setPatternImage(String patternImage){
        this.patternImage = patternImage;
    }
    
    public String getPatternImage() {
        return patternImage;
    }
    
    public boolean isSelected() {
    	return selected;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public int getExpressionId() {
    	return expressionId;
    }
    
    public void setExpressionId(int expressionId) {
    	this.expressionId = expressionId;
    }
    
    public int getPatternId() {
    	return patternId;
    }
    
    public void setPatternId(int patternId) {
    	this.patternId = patternId;
    }
    
    public int getLocationId() {
    	return locationId;
    }
    
    public void setLocationId(int locationId) {
    	this.locationId = locationId;
    }
    
    public String getLocationAPart() {
    	return locationAPart;
    }
    
    public void setLocationAPart(String locationAPart) {
    	this.locationAPart = locationAPart;
    }
    
    public String getLocationNPart() {
    	return locationNPart;
    }
    
    public void setLocationNPart(String locationNPart) {
    	this.locationNPart = locationNPart;
    }

}
