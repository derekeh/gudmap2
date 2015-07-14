package org.gudmap.beans;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.gudmap.models.GeneStripModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.assemblers.GeneStripBeanAssembler;
import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.json.simple.JSONObject;

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
    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
    private ArrayList<GeneStripModel> dataList;
	private ArrayList<MasterTableInfo> tableinfo;	
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
        microarrayHeatmapBeanAssembler  = new MicroarrayHeatmapBeanAssembler();
        tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
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
       	
       	createJSONFile();
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

//	public String getGene_id(){
//		return gene_id;
//	}
//	
//	public String getGene_id(){
//		return gene_id;
//	}
	
	private void createJSONFile(){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = ctx.getRealPath("/");
				
			path += "/resources/scripts/genestrip_heatmap.json";
			
			FileWriter writer = new FileWriter(path);
			
			JSONObject obj = createHeatmapJSONObject();
			
			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();


		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	private JSONObject createHeatmapJSONObject(){
		
		JSONObject obj = new JSONObject();
		
		obj.put("labels", getLabels());
		obj.put("links", getLinks());
		obj.put("data", getDataValues());
		obj.put("adjdata", getDataAdjValues());
						
		return obj;
	}
	

	
	private LinkedList<String> getLabels(){
		
		ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<String> labels = new LinkedList<String>();
		int colsize = 0;
		
		for(MasterTableInfo info : tableinfo){
			if (info.getSelected()){
				String id = info.getId();    			
				String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
				ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
				for(String probeId : probeIds){
					labels.add(info.getTitle());

        			colsize = microarrayHeatmapBeanAssembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id).size();
        			if (colsize > maxColNumber) 
        				maxColNumber = colsize;
				}
				labels.add("");
			}

		}	
		return labels;
	}

	private LinkedList<String> getLinks(){
		
		ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<String> links = new LinkedList<String>();
		
		for(MasterTableInfo info : tableinfo){
			if (info.getSelected()){
				String id = info.getId();    			
				String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
				ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
				for(String probeId : probeIds){
	       			links.add(info.getId());
				}
				links.add("");
			}
		}	
		return links;
	}
	
	private LinkedList<LinkedList<String>> getDataValues(){
		
		ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = microarrayHeatmapBeanAssembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id);
    			int dlsize = dataList.size();
    			int colCounter = 1;
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String rma = item[2];
    				items.add(rma);	
					colCounter ++;
    			}
    			if (dlsize < maxColNumber){
    				int diff = maxColNumber - dlsize;
    				for (int i = 0; i < diff; i ++){
    					items.add("100");
    				}
    			}
    			data.add(items);
     		}
    		items = new LinkedList<String>();
    		for (int i = 0; i < maxColNumber; i++){
    			items.add("100");
    		}
    	
    		data.add(items);
    	}
				
		return data;
	}
	
	private LinkedList<LinkedList<String>> getDataAdjValues(){

		ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, inputString, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = microarrayHeatmapBeanAssembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id);
    			int dlsize = dataList.size();
    			int colCounter = 1;
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String scaledRma = item[5];
    				items.add(scaledRma);	
					colCounter ++;
    			}
    			if (dlsize < maxColNumber){
    				int diff = maxColNumber - dlsize;
    				for (int i = 0; i < diff; i ++){
    					items.add("100");
    				}
    			}
    			data.add(items);
     		}
    		items = new LinkedList<String>();
    		for (int i = 0; i < maxColNumber; i++){
    			items.add("100");
    		}
    	
    		data.add(items);
    	}
				
		return data;
	}
	
	
}
