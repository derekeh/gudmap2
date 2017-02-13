package org.gudmap.beans;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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




/**
 * <h1>SolrGeneStripBean</h1>
 * The SolrGeneStripBean class contains the methods to provide data and deal with events on the
 * solrGeneStrip.xhtml web page
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
@Named (value="solrGeneStripBean")
@SessionScoped
public class SolrGeneStripBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
//	private Connection con;
//	private PreparedStatement ps;
//	private ResultSet result;
	 
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
	private HashMap<String,String> totals;    
    
    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
	private ArrayList<MasterTableInfo> tableinfo;		
    private int maxColNumber = 0;
	private boolean showPageDetails = true;
   
    // Constructors -------------------------------------------------------------------------------

    public SolrGeneStripBean() {
    	super(10,10,"RELEVANCE",true);
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
    	loadDataList();
    	return "solrGeneStrip";
    }

    public void resetAll() {
		paramBean.resetAll();
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
	
	@SuppressWarnings("unchecked")
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
		
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = microarrayHeatmapBeanAssembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id);
    			int dlsize = dataList.size();
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String rma = item[2];
    				items.add(rma);	
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

		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		
    	for(MasterTableInfo info : tableinfo){
    		String id = info.getId();
    		String platformId = microarrayHeatmapBeanAssembler.getPlatformIdFromMasterTableId(id);
    		ArrayList<String> probeIds = microarrayHeatmapBeanAssembler.getProbeSetIdsBySymbolAndPlatformId(1, 20, null, true, geneId, platformId);
    		for (String probeId : probeIds){
    			ArrayList<String[]> dataList = microarrayHeatmapBeanAssembler.getHeatmapDataFromProbeIdAndMasterTableId(1, 20, null, true, probeId, id);
    			int dlsize = dataList.size();
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String scaledRma = item[5];
    				items.add(scaledRma);	
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

    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a list of GeneStripModels containing the retrieved documents.
     * 
	 * @param solrInput The main query string for retrieving relevant documents'
     * @param filterlist A list of filters to be applied to the solr search
     * @param sortColumn The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param num The number of documents to be retrieved in the result set.
     * @return A list of GeneStripModels
	 * @see GeneStripModel
     */
	public List<GeneStripModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
					
    	SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getGudmapGenes(solrInput, filterlist, sortColumn,ascending,offset,num);
		if (sdl==null){
			return null;
		}
		
		
		list = this.formatTableData(sdl);
		
		totals = (HashMap<String, String>) solrTreeBean.getSolrUtil().getGeneDataCount(solrInput, filterlist);


		return list;
	}
	
	public ArrayList<String> getGeneIds(){

		return geneIds;
	}
	
	/**
	 * This method creates a list of GeneStripModels from the documents in the SolrDocumentList sdl.
	 * 
	 * @param sdl A SolrDocumentList	  
	 * @return A List of GeneStripModels
	 * @see GeneStripModel
	 */
	public List<GeneStripModel> formatTableData(SolrDocumentList sdl){
		
		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
		GeneStripModel model = null;
		geneIds = new ArrayList<String>();
		
		for(SolrDocument doc : sdl) { 

//			String insituExpression = "";			
//			if (doc.containsKey("PRESENT") && doc.getFieldValue("PRESENT").toString() != "")
//				insituExpression = "present";
//			else if (doc.containsKey("UNCERTAIN") && doc.getFieldValue("UNCERTAIN").toString() != "")
//				insituExpression = "uncertain";
//			else if (doc.containsKey("NOT_DETECTED") && doc.getFieldValue("NOT_DETECTED").toString() != "")
//				insituExpression = "not detected";
			
			model = new GeneStripModel();

			

			if (doc.containsKey("MGI_GENE_ID")){
				String geneId = doc.getFieldValue("MGI_GENE_ID").toString();		
				model.setGene_id(geneId);
				model.setMgiId(geneId);
				
//				model.setMicroarrayProfile(assembler.buildMicroarrayProfile(geneId));
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
			String species = "";
			if (doc.containsKey("SPECIES")){
				species = doc.getFieldValue("SPECIES").toString();	
				model.setSpecies(species);
			}
			if (doc.containsKey("OMIM")){
				int omimCount = Integer.parseInt(doc.getFieldValue("OMIM").toString());	
				model.setOmimCount(omimCount);
			}
			
			String arrayRange = "";
			String ishRange = "";
			if (doc.containsKey("ARRAY_RANGE")){
				arrayRange = doc.getFieldValue("ARRAY_RANGE").toString();	
			}
			if (doc.containsKey("ISH_RANGE")){
				ishRange = doc.getFieldValue("ISH_RANGE").toString();	
			}
			model.setStageRange(calculateStageRange(arrayRange,ishRange,species));
			
			if (doc.containsKey("GENE") && doc.containsKey("MGI_GENE_ID")){
				String gene = doc.getFieldValue("GENE").toString();		
				String geneId = doc.getFieldValue("MGI_GENE_ID").toString();		
				model.setExpressionProfile(assembler.buildExpressionProfile(gene,geneId));
			}

			list.add(model);
			
		
		}
			
		return list;
	 }	
    
	  private String calculateStageRange(String arrayRange, String ishRange, String species){
		  String RET="";
		  Connection stgConn = null;
		  PreparedStatement stgps = null;;
		  ResultSet stgresult = null;
		  ArrayList<Integer> rangeList=new ArrayList<Integer>();
		  if(arrayRange!=null && arrayRange!="") {
			  String [] arrayValues = arrayRange.split("-");
			  
			  for(int i=0;i<arrayValues.length;i++) {
				  rangeList.add(Integer.valueOf(arrayValues[i]));
			  }
		  }
		  if(ishRange!=null && ishRange!="") {
			  String [] ishValues = ishRange.split("-"); 		  
			  for(int i=0;i<ishValues.length;i++) {
				  rangeList.add(Integer.valueOf(ishValues[i]));
			  }
		  }
		  if(rangeList.size()>0) {
			  java.util.Collections.sort(rangeList);	

			  if(species == null || species.startsWith("Mus")) {
				  RET="TS"+rangeList.get(0).toString()+"-TS"+rangeList.get((rangeList.size()-1)).toString();
			  }
			  else if(species.startsWith("Hom")) {
				  RET = "";
				  
				  //////////////
				try
				{
					stgConn = Globals.getDatasource().getConnection();
					stgps = stgConn.prepareStatement("SELECT STG_STAGE_DISPLAY FROM REF_STAGE WHERE STG_ORDER = "+rangeList.get(0).toString()+" AND STG_SPECIES = 'Homo sapiens'"); 
					stgresult =  stgps.executeQuery();
					while (stgresult.next()) {
						RET=stgresult.getString(1)+"-";			
					}			
				}
				catch(SQLException sqle){sqle.printStackTrace();}
				finally {
						Globals.closeQuietly(stgConn, stgps, stgresult);
				}
				
				try
				{
					stgConn = Globals.getDatasource().getConnection();
					stgps = stgConn.prepareStatement("SELECT STG_STAGE_DISPLAY FROM REF_STAGE WHERE STG_ORDER = "+rangeList.get((rangeList.size()-1)).toString()+" AND STG_SPECIES = 'Homo sapiens'"); 
					stgresult =  stgps.executeQuery();
					while (stgresult.next()) {
						RET+=stgresult.getString(1);			
					}			
				}
				catch(SQLException sqle){sqle.printStackTrace();}
				finally {
						Globals.closeQuietly(stgConn, stgps, stgresult);
				}
				  
				  /////////////
			  }
		  }

		  
		  return RET;
	  }

		public HashMap<String,String> getTotals(){		
			return totals;
		}
	  
}
