package org.gudmap.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.StageAssembler;

@Named
//@RequestScoped
@ViewScoped
public class StageBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String[] stage = new String[]{"17","18","19","20","21","22","23","24","25","26","27","28"};
	private String[][] tableData=null;
	private String organ="";
	private String geneSymbol="";
	private StageAssembler stageAssembler;
	
	@Inject
   	protected SessionBean sessionBean;
	
	public StageBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
        this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
        stageAssembler = new StageAssembler();
        setup();		
	}
	
	 public void setSessionBean(SessionBean sessionBean){
			this.sessionBean=sessionBean;
		}
	    
	 @PostConstruct
	 public void setInputParams(){
	    sessionBean.setInputParam(geneSymbol);
	 }
	
	public void setup() {
		tableData=stageAssembler.retrieveData(stage, organ, geneSymbol);
	}
	
	public void setTableData(String[][]tableData) {
		this.tableData = tableData;
	}
	
	public String[][] getTableData() {
		return tableData;
	}
	
	public void setGeneSymbol(String geneSymbol){
		this.geneSymbol = geneSymbol;
	}
	
	public String getGeneSymbol() {
		return geneSymbol;
	}

}
