package org.gudmap.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.StageAssembler;
import org.gudmap.globals.Globals;

@Named
//@RequestScoped
//@ViewScoped
@SessionScoped
public class StageBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//private String[] stage = new String[]{"17","18","19","20","21","22","23","24","25","26","27","28"};
	private String[][] tableData=null;
	private String organ="";
	private String geneSymbol="";
	private StageAssembler stageAssembler;
	
	private String[] stage;
	private String[][] stageList;
	//private String gene;
	private String geneId;
	private String species;
	
	@Inject
   	protected SessionBean sessionBean;
	
	public StageBean() {
		stageAssembler = new StageAssembler();
		/*geneSymbol = Globals.getParameterValue("gene");
        geneId = Globals.getParameterValue("geneId");
        species = Globals.getParameterValue("species");
        if (species == null || species.equals(""))
			species = "Mus musculus";
        
        setup();*/		
	}
	
	 public void setSessionBean(SessionBean sessionBean){
			this.sessionBean=sessionBean;
		}
	    
	 @PostConstruct
	 public void setInputParams(){
	    sessionBean.setInputParam(geneSymbol);
	 }
	
	public void setup() {
		geneSymbol = Globals.getParameterValue("gene");
        geneId = Globals.getParameterValue("geneId");
        species = Globals.getParameterValue("species");
        if (species == null || species.equals(""))
			species = "Mus musculus";
		
		stage = stageAssembler.getStages(species);
		stageList = stageAssembler.getStageList(stage, organ, geneId);
		tableData=getSubmissions();
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
	
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}
	
	public String getGeneId() {
		return geneId;
	}
	
	public void setSpecies(String species) {
		this.species = species;
	}
	
	public String getSpecies() {
		return species;
	}
	
	public String[][] getSubmissions() {
		tableData = new String[stage.length][6];
		for(int i=0; i<stage.length; i++) {
			tableData[i][0] = new String(stage[i]);
			if(null != stageList[i][0]) {
				tableData[i][1] = new String(stageList[i][0]);
			} else {
				tableData[i][1] = new String("0");
			}
			if(null != stageList[i][1]) {
				tableData[i][2] = new String(stageList[i][1]);
			} else {
				tableData[i][2] = new String("0");
			}
			if(null != stageList[i][2]) {
				tableData[i][3] = new String(stageList[i][2]);
			} else {
				tableData[i][3] = new String("0");
			}
			tableData[i][4] = new String(stage[i].toLowerCase());
			
			if(null != stageList[i][3]) {
				tableData[i][5] = new String(stageList[i][3]);
			} else {
				tableData[i][5] = new String("0");
			}
		}
		return tableData;
	}

}
