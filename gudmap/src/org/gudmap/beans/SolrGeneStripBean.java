package org.gudmap.beans;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.faces.context.FacesContext;





import javax.servlet.ServletContext;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.assemblers.SolrGeneStripAssembler;
import org.gudmap.globals.Globals;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.GeneStripModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.models.SolrInsituTableBeanModel;
import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.json.simple.JSONObject;




@Named (value="solrGeneStripBean")
@SessionScoped
public class SolrGeneStripBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
	private SolrGeneStripAssembler assembler;
    private String whereclause = " WHERE ";
    private List<String> selectedItems;
    private boolean areAllChecked;
    private ArrayList<String> geneIds;
    
    @Inject
   	private ParamBean paramBean;
   
    @Inject
   	private SolrTreeBean solrTreeBean;
    
    @Inject
   	private SolrFilter solrFilter;
    
	private String solrInput;
	private HashMap<String,String> filters;
    
    
    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
	private ArrayList<MasterTableInfo> tableinfo;		
    private int maxColNumber = 0;
	private boolean showPageDetails = true;
   
    // Constructors -------------------------------------------------------------------------------

    public SolrGeneStripBean() {
    	super(5,10,"RELEVANCE",true);
    	setup();
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
 
	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
	public void setSolrFilter(SolrFilter solrFilter){
		this.solrFilter = solrFilter;
	}
	

	public void setSolrInput(String solrInput){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
	}
	
	public String getSolrInput(){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
		return solrInput;
	}
    
    public void setup() {
    	assembler=new SolrGeneStripAssembler();
        microarrayHeatmapBeanAssembler  = new MicroarrayHeatmapBeanAssembler();
        tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrInput = solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
        totalRows = solrTreeBean.getSolrUtil().getGeneCount(solrInput, filters);
    	
     	dataList = getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);

   		ArrayList<String> geneIds = getGeneIds();         		

	   	for (String geneId : geneIds){
	   		createJSONFile(geneId);
	   	}     	
     	
        // Set currentPage, totalPages and pages.
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
        
        if (dataList.size() > rowsPerPage)
        	showPageDetails = true;
        else
        	showPageDetails = false;
   }

    public String refresh(){
 //   	sortField = "RELEVANCE";
    	loadDataList();
//    	paramBean.resetValues();
    	return "solrGeneStrip";
    }

    public void resetAll() {
		paramBean.resetAll();
//		solrFilterBean.resetAll();		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((SolrInsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((SolrInsituTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((SolrInsituTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }

	private void createJSONFile(String geneId){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = ctx.getRealPath("/");			
			path += "/resources/genestrips/genestrips_" + geneId + ".json";
			
//			String geneIdn = geneId.replaceAll(":", "_");
//			String path = "/export/data0/bernardh/MAWWW/Public/html/AppFiles/heatmaps/genestrip_" + geneIdn + ".json";
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
	
    public String getTitle(){
    	String str="Genestrip Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getGeneCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getGeneCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getGeneCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getGeneCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }

	public List<GeneStripModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
					
    	SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getGudmapGenes(solrInput, filterlist, sortColumn,ascending,offset,num);
		if (sdl==null){
			return null;
		}
		
		
		list = this.formatTableData(sdl);

		return list;
	}
	
	public ArrayList<String> getGeneIds(){

		return geneIds;
	}
	
	public List<GeneStripModel> formatTableData(SolrDocumentList sdl){
		
		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
		GeneStripModel model = null;
		geneIds = new ArrayList<String>();
		
		for(SolrDocument doc : sdl) { 

			String insituExpression = "";			
			if (doc.containsKey("PRESENT") && doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.containsKey("UNCERTAIN") && doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.containsKey("NOT_DETECTED") && doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new GeneStripModel();

			

			if (doc.containsKey("MGI_GENE_ID")){
				String geneId = doc.getFieldValue("MGI_GENE_ID").toString();		
				model.setGene_id(geneId);
				model.setMgiId(geneId);
				
				model.setMicroarrayProfile(assembler.buildMicroarrayProfile(geneId));
				model.setImageUrl(assembler.getRepresentativeImage(geneId));
				
				geneIds.add(geneId);
			}
			if (doc.containsKey("GENE")){
				String gene = doc.getFieldValue("GENE").toString();				
				model.setGeneSymbol(gene);
			}
			if (doc.containsKey("SYNONYMS")){
				String synonyms = doc.getFieldValue("SYNONYMS").toString();	
				model.setSynonyms(synonyms);
			}
			
			if (doc.containsKey("GENE") && doc.containsKey("MGI_GENE_ID")){
				String gene = doc.getFieldValue("GENE").toString();		
				String geneId = doc.getFieldValue("MGI_GENE_ID").toString();		
				model.setExpressionProfile(assembler.buildExpressionProfile(gene,geneId));
			}

			list.add(model);
			
		
		}
			
		return list;
	 }	
    
    
}
