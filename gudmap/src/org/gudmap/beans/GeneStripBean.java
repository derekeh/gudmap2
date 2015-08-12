package org.gudmap.beans;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.gudmap.globals.Globals;
import org.gudmap.models.GeneStripModel;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.assemblers.GeneStripBeanAssembler;
import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.json.simple.JSONObject;


@Named
//@RequestScoped
@SessionScoped
public class GeneStripBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String oid;
    private String geneSymbol="";
    private String gene_id="";
    private String inputString="";
    private String wildcard = "equals";
    private GeneStripBeanAssembler geneStripBeanAssembler;
    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
    private ArrayList<GeneStripModel> dataList;
	private ArrayList<MasterTableInfo> tableinfo;	
	private ArrayList<String> geneIds;
	private boolean areAllChecked;
	private List<String> selectedItems;
   // private String tempParam="";
    String str;
    
    private int maxColNumber = 0;
    private String rowLabels;
    private String microarrayLinks;
    private String genelistAdjustedData;
    private String genelistValueData;    
    @Inject
   	protected SessionBean sessionBean;
    @Inject
   	protected ParamBean paramBean;
    
    public GeneStripBean() {
        
        /*if(Globals.getParameterValue("gene")!=null)
        	this.geneSymbol = Globals.getParameterValue("gene");
        if(Globals.getParameterValue("input")!=null)
        	this.inputString = Globals.getParameterValue("input");
        if(Globals.getParameterValue("geneId")!=null)
        	this.gene_id = Globals.getParameterValue("geneId");*/
        
        geneStripBeanAssembler = new GeneStripBeanAssembler();       
        microarrayHeatmapBeanAssembler  = new MicroarrayHeatmapBeanAssembler();
        tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
        
    }
    
    public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }
    
    public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
    
    public ParamBean getParamBean (){
		return paramBean;
	}
    //if the parameters are not passed in via the url then they are passed in from sessionBean.getTempParam
    //in a view (viewGeneStrip) which has this as Request scoped backing bean are unable to set the <f:param value to sessionBean.tempParam
   /* @PostConstruct
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
	 }*/
    
    
    
    public void setup() {
    	
        // check input string to decide wildcard value
    	inputString=sessionBean.getGeneParam();
       	
       	if (inputString != null && !inputString.equals("")) {
       		inputString = inputString.trim();
       		int stringLen = inputString.length();
       		String lastChar = inputString.trim().substring(stringLen-1, stringLen);
       		if (lastChar.equals("*")) {
       			inputString = inputString.substring(0, stringLen-1);
       			wildcard = "starts with";
       		}
       			       	 
       		dataList = geneStripBeanAssembler.getData(geneSymbol,inputString,wildcard);
       		geneIds = geneStripBeanAssembler.getGeneIds();         		
       	}
       	if(geneIds!=null) {
	       	for (String geneId : geneIds){
	       		createJSONFile(geneId);
	       	}
       	}
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
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((GeneStripModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((GeneStripModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((GeneStripModel) dataList.get(i)).getGene_id());
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	//return "browseCollectionEntriesTablePage";
    	return "result";
    }
	
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

	public void setGene_id(String gene_id){
		this.gene_id = gene_id;
	}
	
	public String getGene_id(){
		return gene_id;
	}
	
	private void createJSONFile(String geneId){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = ctx.getRealPath("/");
				
			path += "/resources/scripts/genestrip_" + geneId + ".json";
			File f = new File(path);
			if (!f.exists()){
				FileWriter writer = new FileWriter(f);
				
				JSONObject obj = createHeatmapJSONObject(geneId);
				
				writer.write(obj.toJSONString());
				writer.flush();
				writer.close();
			}

		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	private JSONObject createHeatmapJSONObject(String geneId){
		
		JSONObject obj = new JSONObject();
		
		obj.put("labels", getLabels(geneId));
		obj.put("links", getLinks(geneId));
//		obj.put("data", getDataValues(geneId));
		obj.put("adjdata", getDataAdjValues(geneId));
						
		return obj;
	}
	

	
	private LinkedList<String> getLabels(String geneId){
		
		//ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<String> labels = new LinkedList<String>();
		int colsize = 0;
		
		for(MasterTableInfo info : tableinfo){
			if (info.getSelected()){
				String id = info.getId();    			
				String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
				ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
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

	private LinkedList<String> getLinks(String geneId){
		
		//ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<String> links = new LinkedList<String>();
		
		for(MasterTableInfo info : tableinfo){
			if (info.getSelected()){
				String id = info.getId();    			
				String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
				ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
				for(String probeId : probeIds){
	       			links.add(info.getId());
				}
				links.add("");
			}
		}	
		return links;
	}
	
	private LinkedList<LinkedList<String>> getDataValues(String geneId){
		
		//ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
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
	
	private LinkedList<LinkedList<String>> getDataAdjValues(String geneId){

		//ArrayList<MasterTableInfo> tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
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
