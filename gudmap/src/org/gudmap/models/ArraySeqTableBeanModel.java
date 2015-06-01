package org.gudmap.models;

public class ArraySeqTableBeanModel extends GenericTableBeanModel {
//series
private String title,geoSeriesID,seriesComponents,platformID,libraryStrategy=null;
//sample
private String geoSampleID,sampleDescription,sampleComponents=null;
//platform
private String pltName,pltTechnology,pltManufacturer=null;
//integers
private int numSamples,numSeries,seriesOid,sampleOid=0;

	public void setTitle(String title){
		this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setGeoSeriesID(String geoSeriesID){
		this.geoSeriesID=geoSeriesID;
	}
	public String getGeoSeriesID(){
		return geoSeriesID;
	}
	public void setPlatformID(String platformID){
		this.platformID=platformID;
	}
	public String getPlatformID(){
		return platformID;
	}
	public void setLibraryStrategy(String libraryStrategy){
		this.libraryStrategy=libraryStrategy;
	}
	public String getLibraryStrategy(){
		return libraryStrategy;
	}
	public void setSeriesComponents(String seriesComponents){
		this.seriesComponents=seriesComponents;
	}
	public String getSeriesComponents(){
		return seriesComponents;
	}
	public void setGeoSampleID(String geoSampleID){
		this.geoSampleID=geoSampleID;
	}
	public String getGeoSampleID(){
		return geoSampleID;
	}
	public void setSampleDescription(String sampleDescription){
		this.sampleDescription=sampleDescription;
	}
	public String getSampleDescription(){
		return sampleDescription;
	}
	public void setSampleComponents(String sampleComponents){
		this.sampleComponents=sampleComponents;
	}
	public String getSampleComponents(){
		return sampleComponents;
	}
	public void setPltName(String pltName){
		this.pltName=pltName;
	}
	public String getPltName(){
		return pltName;
	}
	public void setPltTechnology(String pltTechnology){
		this.pltTechnology=pltTechnology;
	}
	public String getPltTechnology(){
		return pltTechnology;
	}
	public void setPltManufacturer(String pltManufacturer){
		this.pltManufacturer=pltManufacturer;
	}
	public String getPltManufacturer(){
		return pltManufacturer;
	}
	public void setNumSamples(int numSamples){
		this.numSamples=numSamples;
	}
	public int getNumSamples(){
		return numSamples;
	}
	public void setNumSeries(int numSeries){
		this.numSeries=numSeries;
	}
	public int getNumSeries(){
		return numSeries;
	}
	public void setSeriesOid(int seriesOid){
		this.seriesOid=seriesOid;
	}
	public int getSeriesOid(){
		return seriesOid;
	}
	public void setSampleOid(int sampleOid){
		this.sampleOid=sampleOid;
	}
	public int getSampleOid(){
		return sampleOid;
	}
	
	
	
}
