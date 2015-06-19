package org.gudmap.models.submission;

public class SeqSeriesModel extends SeriesModel{
	private int created_by=0;
	private String shortLabel="";
	
	public void setCreated_by(int created_by) {
		this.created_by=created_by;
	}
	
	public int getCreated_by() {
		return created_by;
	}
	
	public void setShortLabel(String shortLabel) {
		this.shortLabel=shortLabel;
	}
	
	public String getShortLabel() {
		return shortLabel;
	}

}
