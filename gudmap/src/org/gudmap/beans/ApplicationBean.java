package org.gudmap.beans;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped 
public class ApplicationBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final static String theilerUrl1 = "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts";
	private final static String theilerUrl2 = "definition.html";

	public ApplicationBean(){}
	
	public String getTheilerUrl1 (){
		return theilerUrl1;
	}
	
	public String getTheilerUrl2 (){
		return theilerUrl2;
	}
	
	public String getViewerFrameSourceName() {
		
		//just for testing the port to glenelgin
		return "http://glenelgin.hgu.mrc.ac.uk/mrciip/mrciip_gudmap.html";
	
		//return Utility.domainUrl+"mrciip/mrciip_gudmap.html";
	}

}
