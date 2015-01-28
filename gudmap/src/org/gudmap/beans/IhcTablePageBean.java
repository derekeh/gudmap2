package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.InsituTablePageBeanAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.generic.GenericQueries;

@Named
@SessionScoped
public class IhcTablePageBean extends InsituTablePageBean implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
    // Constructors -------------------------------------------------------------------------------

    public IhcTablePageBean() {
    	super(20,10,"SUB_OID",true);   	
        setup("IHC","");
    }
    
    // Overrides ----------------------------------------------------------------------------------
    
    @Override
    public String refresh(){
    	loadDataList();
    	paramBean.resetValues();
    	return "browseIhcTablePage";
    }
    
    @Override
    public String resetAll() {
		paramBean.resetValues();
		paramBean.setWhereclause(GenericQueries.WHERE_CLAUSE);
		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
		return "browseIhcTablePage";
	}
    
   
}
