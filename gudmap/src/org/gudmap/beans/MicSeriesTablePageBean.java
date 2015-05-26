package org.gudmap.beans;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named
@SessionScoped
public class MicSeriesTablePageBean extends InsituTablePageBean implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
    // Constructors -------------------------------------------------------------------------------

    public MicSeriesTablePageBean() {
    	super(20,10,"source",true);   	
        setup("Microarray","");
    }
    
    // Overrides ----------------------------------------------------------------------------------
    
    @Override
    public String refresh(){
    	loadDataList();
    	return "browseMicSeriesTablePage";
    }
    
    @Override
    public String resetAll() {
		paramBean.resetAll();
    	
		//must return to homepage to reset focus group. Can't refresh div on other page
		loadDataList();
		return "browseMicSeriesTablePage";
	}
    
   
}
