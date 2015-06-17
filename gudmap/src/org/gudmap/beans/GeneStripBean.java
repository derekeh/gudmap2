package org.gudmap.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.models.GeneStripModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.assemblers.GeneStripBeanAssembler;
import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;

@Named
@RequestScoped
//@SessionScoped
public class GeneStripBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String oid;
    private String geneSymbol="";
    private String inputString="";
    private String wildcard = "equals";
    private GeneStripBeanAssembler geneStripBeanAssembler;
    private ArrayList<GeneStripModel> dataList;
   // private String tempParam="";
    String str;
    
    private int maxColNumber = 0;
    private String rowLabels;
    private String microarrayLinks;
    private String genelistAdjustedData;
    private String genelistValueData;    
    @Inject
   	protected SessionBean sessionBean;
    
    public GeneStripBean() {
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	if(facesContext.getExternalContext().getRequestParameterMap().get("gene")!=null)
    		this.geneSymbol = facesContext.getExternalContext().getRequestParameterMap().get("gene");
        if(facesContext.getExternalContext().getRequestParameterMap().get("input")!=null)
        	this.inputString = facesContext.getExternalContext().getRequestParameterMap().get("input");
        
        geneStripBeanAssembler = new GeneStripBeanAssembler();       
        //setup();
        
    }
    
    public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }
    //if the parameters are not passed in via the url then they are passed in from sessionBean.getTempParam
    //in a view (viewGeneStrip) which has this as Request scoped backing bean are unable to set the <f:param value to sessionBean.tempParam
    @PostConstruct
	 public void setInputParams(){
    	if(inputString==null || inputString.equals(""))
    	{
    		if(geneSymbol==null || geneSymbol.equals(""))
    			inputString=getSessionBean().getTempParam();
    		else
    			inputString=geneSymbol;
    	}
    	getSessionBean().setInputParam(inputString);
		setup();
	 }
    
	 /*@PostConstruct
	 public void setInputParams(){
		 sessionBean.setInputParam(inputString);
	 }*/
    
    
    public void setup() {
        // check input string to decide wildcard value
       	
       	if (inputString != null && !inputString.equals("")) {
       		inputString = inputString.trim();
       		int stringLen = inputString.length();
       		String lastChar = inputString.trim().substring(stringLen-1, stringLen);
       		if (lastChar.equals("*")) {
       			inputString = inputString.substring(0, stringLen-1);
       			wildcard = "starts with";
       		}
       			       	 
       		dataList = geneStripBeanAssembler.getData(geneSymbol,inputString,wildcard);
       	}
       	
       	CreateMicroarrayExpressionProfile();
    }
    
    public void setOid(String oid) {
    	this.oid=oid;
    }
    
    public String getOid() {
    	return oid;
    }
    
    public void setGeneSymbol(String geneSymbol) {
    	this.geneSymbol = geneSymbol;
    }
    
    public String getGeneSymbol() {
    	return geneSymbol;
    }
    
    public void setDataList(ArrayList<GeneStripModel> dataList){
    	this.dataList=dataList;
    }
    
    public ArrayList<GeneStripModel> getDataList() {
    	return dataList;
    }
    
    public void setInputString(String inputString) {
    	this.inputString = inputString;
    }
    
    public String getInputString() {
    	return inputString;
    }
    
    public void checkAll(){}
	public void checkboxSelections(){}
	
	/*public void setTempParam(String tempParam) {
		this.tempParam=tempParam;
	}
    
	public String getTempParam(){
		return tempParam;
	}*/

	public String getRowLabels(){
		return rowLabels;
	}

	public String getMicroarrayLinks(){
		return microarrayLinks;
	}
	
	public int getMaxColNumber(){
		return maxColNumber;
	}

	public String getGenelistValueData(){
		return genelistValueData;
	}
	
	public String getGenelistAdjustedData(){
		return genelistAdjustedData;
	}
	
	private void CreateMicroarrayExpressionProfile(){

		MicroarrayHeatmapBeanAssembler assembler = new MicroarrayHeatmapBeanAssembler();
		ArrayList<MasterTableInfo> tableinfo = assembler.getMasterTableList();	
		
		String labels = "";
		String links = "";	
		int colsize = 0;
    	for(MasterTableInfo info : tableinfo){
    		if (info.getSelected()){
    			String id = info.getId();    			
    			String platformId = assembler.getPlatformIdFromMasterTableId(id);
    			ArrayList<String> probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
    			for(String probeId : probeIds){
    				labels += info.getTitle() + ",";
           			links += info.getId() + ",";

        			colsize = assembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id).size();
        			if (colsize > maxColNumber) 
        				maxColNumber = colsize;
    			}
    			labels += ",";
       			links += ",";
   			
    		}
    	}
    	rowLabels = labels.substring(0, labels.length()-2);
    	microarrayLinks = links.substring(0, links.length()-2);

		String values = "";
		String adjvalues = "";
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = assembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id);
    			int dlsize = dataList.size();
    			int colCounter = 1;
    			for(String[] data : dataList){
    				String rma = data[2];
					String scaledRma = data[5];
					String backgroundColor = "#" + data[6];
					values += rma + ",";
					adjvalues += scaledRma + ",";
    					
					colCounter ++;
    			}
    			if (dlsize < maxColNumber){
    				int diff = maxColNumber - dlsize;
    				for (int i = 0; i < diff; i ++){
    					values += "100,";
    					adjvalues += "100,";
    				}
    			}
     		}
    		for (int i = 0; i < maxColNumber; i++){
				values += "100,";
				adjvalues += "100,";   
    		}
    	}

		genelistValueData = values.substring(0, values.length()-1);
		genelistAdjustedData = adjvalues.substring(0, adjvalues.length()-1);

	}
	
}
