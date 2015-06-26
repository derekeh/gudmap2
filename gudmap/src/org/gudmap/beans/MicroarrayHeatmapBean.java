package org.gudmap.beans;



import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.impl.PagerImpl;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;


@Named (value="microarrayHeatmapBean")
@SessionScoped
//@RequestScoped
public class MicroarrayHeatmapBean extends PagerImpl  implements Serializable{

	private static final long serialVersionUID = 1L;	
    private boolean debug = true;
 

	private MicroarrayHeatmapBeanAssembler assembler;
	
	private String tableTitle;
	private ArrayList<MasterTableInfo> tableinfo;
	
	private ArrayList<String> selectedTabs;
	private String selectedSample;
	private boolean dataAvailable = true;
    private String columnLabels;
    private String rowLabels;
    private String genelistLabels;
    private String data;

    
    private String gene;// = "Sox8";
    private String masterTableId;// = "3_2";
    private String genelistId;// = "1493";

    private ArrayList<String> probeIds; 
    
    private String selectedTabIndex;
    private String tabStyle;
    private int maxStringLength = 0;

    private String geneLabels;
    private String annotationData;
    private String valueData;
    private String genelistAdjustedData;
    private String genelistValueData;
    private String adjustedData;
    private int maxColNumber = 0;    

    @Inject
   	protected SessionBean sessionBean;    
    
    public MicroarrayHeatmapBean() {
    	
       	super(1000,10,null,true);   	
    }
    
	public MicroarrayHeatmapBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}
    
    public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }

	public void init(){
		
    	FacesContext facesContext = FacesContext.getCurrentInstance();
		gene = facesContext.getExternalContext().getRequestParameterMap().get("gene");
		masterTableId = facesContext.getExternalContext().getRequestParameterMap().get("masterTableId");
		genelistId = facesContext.getExternalContext().getRequestParameterMap().get("genelistId");
		
		
		if (gene != null){
			sessionBean.setGeneParam(gene);
			sessionBean.setGenelistId(null);
		}
		else
			gene = sessionBean.getGeneParam();
		
		
		if (masterTableId != null)
			sessionBean.setMasterTableId(masterTableId);
		else
			masterTableId = sessionBean.getMasterTableId();
		
				
		if (genelistId != null) {
			sessionBean.setGeneParam(null);
			sessionBean.setGenelistId(genelistId);
//			tableTitle = assembler.getGenelistTitle(genelistId) + " (gene list)";
		}
		else
			tableTitle = gene;
					
		assembler = new MicroarrayHeatmapBeanAssembler();
		tableinfo = assembler.getMasterTableList();	
		
		selectedTabs = new ArrayList<String>();
    	for(MasterTableInfo info : tableinfo){
    		if (info.getSelected()){
    			String id = info.getId();
    			selectedTabs.add(id);
    		}
    	}

    	this.setSelectedSample(masterTableId);
	}
	
    public String updateHeatmap(){
    	    	   	
	   	this.loadDataList();
	   	return null;	
    }
    
   
	public String getGeneList(){
		String genelist = gene;
		return genelist;
	}
	
    public void setGene(String gene) {
    	this.gene = gene;
    }
    
    public String getGene() {
    	return gene;
    }
    
    public void setGenelistId(String genelistId) {
    	this.genelistId = genelistId;
    }
    
    public String getGenelistId() {
    	return genelistId;
    }

	public String getMasterTableId(){
		return masterTableId;
	}
    
	public void setMasterTableId(String masterTableId){
		this.masterTableId = masterTableId;
	}
	
	public ArrayList<MasterTableInfo> getAllMasterTableInfo() {
		return tableinfo;
	}
    	 
    public Map<String,String> getSampleOptions() {
    	LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
    	for(MasterTableInfo info : tableinfo){
    		options.put(info.getTitle(), info.getId());
    	}
    	return options;
    }
    
    public String getSelectedSample() {
    	return selectedSample;
    }
    
    public void setSelectedSample(String selection) {
    	selectedSample = selection;
    }
    
	public void selectedSampleChanged(ValueChangeEvent e){
		selectedSample = (String)e.getNewValue();
		
    	masterTableId = selectedSample;
    	sessionBean.setMasterTableId(masterTableId);
    	
    	this.loadDataList();
	}
	    
	public boolean getDataAvailable(){
		return dataAvailable;
	}

// ********************   deal with heatmap  
	
	private String CreateRowLabels(){
		
		
		String platformId = assembler.getPlatformIdFromMasterTableId(masterTableId);
		
		if (genelistId != null){
			probeIds = assembler.getProbeSetIdsByGenelistIdAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, genelistId, platformId);
			totalRows = assembler.getProbeSetIdsByGenelistIdAndPlatformId(-1, rowsPerPage, sortField, sortAscending, genelistId, platformId).size();		
		}
		else{
			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, gene, platformId);
			totalRows = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, gene, platformId).size();
		}

		if (totalRows == 0)
			return null;
		
		String labels = "";
		for(String probeId : probeIds){
			labels += probeId + ",";
		}
		labels = labels.substring(0, labels.length()-1);
		return labels;

	}
	public String getRowLabels(){
		return rowLabels;
	}
    
	public String getGeneLabels(){
		return geneLabels;
	}

	private void CreateColumnLabels(){
		
		int length = 0;
		ArrayList<String> expressionTitles = assembler.getHeatmapExpressionTitlesFromMasterTableId(masterTableId);
		columnLabels = "";
		for(String expressionTitle : expressionTitles){
			columnLabels += expressionTitle + ",";
			
			length = expressionTitle.toCharArray().length;
			if (length > maxStringLength)
				maxStringLength = length;
			
		}
		columnLabels = columnLabels.substring(0, columnLabels.length()-1);

	}
	
	public String getColumnLabels(){
		return columnLabels;
	}

	public String getGenelistLabels(){
		return genelistLabels;
	}
	
	public int getMaxColNumber(){
		return maxColNumber;
	}
	
	public String getData(){
		return data;
	}
	
	public String getAnnotationData(){
		return annotationData;
	}
	
	public String getValueData(){
		return valueData;
	}
	
	public String getAdjustedData(){
		return adjustedData;
	}

	public String getGenelistValueData(){
		return genelistValueData;
	}
	
	public String getGenelistAdjustedData(){
		return genelistAdjustedData;
	}

	private void CreateAnnotations(){
		geneLabels = "";
		
		ArrayList<String[]> annotations = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for (String[] annotation : annotations){
			
			geneLabels += annotation[1] + "," ;
		}
		geneLabels = geneLabels.substring(0, geneLabels.length()-1);
	}

	private void CreateAnnotationData(){
		annotationData = "";
		
		ArrayList<String[]> annotations = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for (String[] annotation : annotations){
			annotationData +=  annotation[0] + "," + annotation[1] + "," + annotation[2] + "," + annotation[3] + "," + annotation[4] + "," + annotation[5] + "," + annotation[6] + ",";
		}
		annotationData = annotationData.substring(0, annotationData.length()-1);
	}
	
	
    @Override
    public void loadDataList() {
    	
    	createJSONFile();    	
    	
//    	rowLabels = CreateRowLabels();
//    	if (rowLabels == null){
//    		dataAvailable = false;
//    		return;
//    	}
//    	else
//    		dataAvailable = true;
//    	
//    	CreateColumnLabels();
//    	CreateAnnotations();
//    	CreateAnnotationData();
//
//    	
//		String heatmapData = "";
//		String values = "";
//		String adjvalues = "";
//		
//		int rowCounter = 1;
//		for (String probeId : probeIds){
//			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, masterTableId);
//			int colCounter = 1;
////			String subcolor = "";
//			for(String[] data : dataList){
//				String rma = data[2];
//				String scaledRma = data[5];
//				String backgroundColor = "#" + data[6];
//				String hdata = Integer.toString(rowCounter) + "," + Integer.toString(colCounter) + "," + rma + "," + scaledRma + "," + backgroundColor + ",";
//				heatmapData += Integer.toString(rowCounter) + "," + Integer.toString(colCounter) + "," + rma + "," + scaledRma + "," + backgroundColor + ",";
//				values += rma + ",";
//				adjvalues += scaledRma + ",";
//				
//				colCounter ++;
//				
//				if (debug)
//					System.out.println("heatmapData: " + hdata);
//			}
//			rowCounter++;
//		}
//		data = heatmapData.substring(0, heatmapData.length()-1);
//		valueData = values.substring(0, values.length()-1);
//		adjustedData = adjvalues.substring(0, adjvalues.length()-1);
		
        // Set currentPage, totalPages and pages.
//    	setTotalslist(assembler.getTotals());

        currentPage = (totalRows / rowsPerPage) - ((totalRows - firstRow) / rowsPerPage) + 1;
        totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRange, totalPages);
        pages = new Integer[pagesLength];

        // firstPage must be greater than 0 and lesser than totalPages-pageLength.
        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);

        // Create pages (page numbers for page links).
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
    }
	
	public String getTitle() {
		
		if (genelistId != null) 
			tableTitle = assembler.getGenelistTitle(genelistId) + " (gene list)";
		else
			tableTitle = gene;
		
		return tableTitle;
	}

	public int getMaxStringLength() {
		return maxStringLength;
	}
	
	private void CreateGenelistLabels(){
		
		String labels = "";
		int colsize = 0;
    	for(MasterTableInfo info : tableinfo){
    		if (info.getSelected()){
    			String id = info.getId();
    			
    			String platformId = assembler.getPlatformIdFromMasterTableId(id);
    			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, gene, platformId);
    			for(String probeId : probeIds){
    				labels += info.getTitle() + ",";
    				
        			colsize = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, id).size();
        			if (colsize > maxColNumber) 
        				maxColNumber = colsize;
    			}
    			labels += ",";
    			
    		}
    	}
    	genelistLabels = labels.substring(0, labels.length()-1);
    	
		String values = "";
		String adjvalues = "";
	
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = assembler.getPlatformIdFromMasterTableId(id);
			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, gene, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, id);
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
	
	
	private void createJSONFile(){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = ctx.getRealPath("/");
			
			if (genelistId != null){
				// cache the analysis json files
				
				path += "/resources/scripts/" + genelistId + "heatmap.json";
				
				File f = new File(path);
				if (!f.exists()){
				
					FileWriter writer = new FileWriter(f);
					
					JSONObject obj = createHeatmapJSONObject();
					
					writer.write(obj.toJSONString());
					writer.flush();
					writer.close();
				}				
				
			}
			else {
				// write over any browsed json files
				
				path += "/resources/scripts/heatmap.json";
				
				FileWriter writer = new FileWriter(path);
				
				JSONObject obj = createHeatmapJSONObject();
				
				writer.write(obj.toJSONString());
				writer.flush();
				writer.close();
			}

		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	private JSONObject createHeatmapJSONObject(){
		
		JSONObject obj = new JSONObject();
		
		obj.put("samples", getSamples());
		obj.put("probes", getProbes());
		obj.put("genes", getGenes());
		obj.put("data", getDataValues());
		obj.put("adjdata", getDataAdjValues());
		obj.put("annotations", getProbeAnnotations());
		
				
		return obj;
	}
	
	private LinkedList<String> getSamples(){
		
		LinkedList<String> samples = new LinkedList<String>();

		ArrayList<String> expressionTitles = assembler.getHeatmapExpressionTitlesFromMasterTableId(masterTableId);
		for(String expressionTitle : expressionTitles){
			samples.add(expressionTitle);
		}
			
		return samples;
	}
	
	private LinkedList<String> getProbes(){
		
		LinkedList<String> probes = new LinkedList<String>();

		String platformId = assembler.getPlatformIdFromMasterTableId(masterTableId);
		
		if (genelistId != null){
			probeIds = assembler.getProbeSetIdsByGenelistIdAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, genelistId, platformId);
		}
		else{
			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, gene, platformId);
		}

		for(String probe : probeIds){
			probes.add(probe);
		}
			
		return probes;
	}

	private LinkedList<String> getGenes(){
		
		LinkedList<String> genes = new LinkedList<String>();

		ArrayList<String[]> annotations = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for (String[] annotation : annotations){
			
			 genes.add(annotation[1]);
		}
			
		return genes;
	}

	private LinkedList<LinkedList<String>> getDataValues(){
		
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		for (String probeId : probeIds){
			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, masterTableId);
			items = new LinkedList<String>();
			for(String[] item : dataList){
				String rma = item[2];
				String scaledRma = item[5];
				
				items.add(rma);
			}
			data.add(items);
		}
				
		return data;
	}
	
	private LinkedList<LinkedList<String>> getDataAdjValues(){
		
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		for (String probeId : probeIds){
			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, masterTableId);
			items = new LinkedList<String>();
			for(String[] item : dataList){
				String rma = item[2];
				String scaledRma = item[5];
				
				items.add(scaledRma);
			}
			data.add(items);
		}
				
		return data;
	}

	private LinkedList<LinkedList<String>> getProbeAnnotations(){
		
		LinkedList<LinkedList<String>> annotations = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		ArrayList<String[]> dataList = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for(String[] item : dataList){
			items = new LinkedList<String>();

			items.add(item[0]);
			items.add(item[1]);
			items.add(item[2]);
			items.add(item[3]);
			items.add(item[4]);
			items.add(item[5]);
			items.add(item[6]);
			items.add("GUDMAP");
			items.add("UCSC");
			items.add("KEGG");
			items.add("ENS");
			
			annotations.add(items);
		}
				
		return annotations;
	}
	
 }
