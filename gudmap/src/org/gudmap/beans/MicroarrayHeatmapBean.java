package org.gudmap.beans;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.gudmap.dao.ArrayDao;
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
import javax.servlet.http.HttpServletRequest;


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

    private String gene;// = "Sox8";
    private String geneId;  
    private String masterTableId;// = "3_2";
    private String genelistId;// = "1493";

    private ArrayList<String> probeIds; 
    private ArrayDao arrayDAO;
    
    @Inject
   	protected SessionBean sessionBean;    
    
    public MicroarrayHeatmapBean() {
    	
       	super(1000,10,null,true);   
       	arrayDAO = new ArrayDao();
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
		geneId = facesContext.getExternalContext().getRequestParameterMap().get("geneId");
		masterTableId = facesContext.getExternalContext().getRequestParameterMap().get("masterTableId");
		genelistId = facesContext.getExternalContext().getRequestParameterMap().get("genelistId");
		
		
		if (geneId != null){
			sessionBean.setGeneId(geneId);
			sessionBean.setGenelistId(null);
		}
		else
			geneId = sessionBean.getGeneId();

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
			sessionBean.setGeneId(null);
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
    
   
//	public String getGeneList(){
//		String genelist = gene;
//		return genelist;
//	}
	public String getGeneList(){
		String genelist = "";
		if (genelistId != null) 
			genelist = arrayDAO.retrieveGenelist(genelistId);
//		else if (listOfGenes != null) 
//			genelist = DbUtility.retrieveGenelistFromGenes(listOfGenes);
		else
			genelist = gene;
		
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

    public void setGeneId(String geneId) {
    	this.geneId = geneId;
    }
    
    public String getGeneId() {
    	return geneId;
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
	
	
    @Override
    public void loadDataList() {
    	
    	createJSONFile();    	
    	

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
	
	private void createJSONFile(){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String path = ctx.getRealPath("/");
			
			if (genelistId != null){
				// cache the analysis json files
				
				path += "/resources/genestrips/" + genelistId + "heatmap.json";
				
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
				
				path += "/resources/genestrips/heatmap.json";
								
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
		obj.put("urls", getUrls());
		
				
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
			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, geneId, platformId);
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

	private LinkedList<LinkedList<String>> getProbeAnnotationsOrig(){
		
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
	private LinkedList<LinkedList<LinkedList<String>>> getProbeAnnotations(){
		
		String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		String contextpath = request.getContextPath();
		
		String locale = request.getRemoteHost();
		String addr = request.getRemoteAddr();
		String usr = request.getRemoteUser();
		String pathinfo = request.getPathInfo();
		
		String host = request.getLocalName() + path;

		
		
		LinkedList<LinkedList<LinkedList<String>>> annotations = new LinkedList<LinkedList<LinkedList<String>>>();
		LinkedList<LinkedList<String>> items;
		LinkedList<String> links;

		ArrayList<String[]> dataList = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for(String[] item : dataList){
			items = new LinkedList<LinkedList<String>>();
			String link = "";
			
			
			links = new LinkedList<String>();
			links.add(item[0]);
			link = "www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + item[0];	
			links.add(link);
			items.add(links);
			
			links = new LinkedList<String>();
			links.add(item[1]);
			link = path + "/pages/gene.html?geneid="+item[3];	
			links.add(null);
			items.add(links);

			links = new LinkedList<String>();
			links.add(item[2]);
			link = "www.informatics.jax.org/accession/"+item[3];
			links.add(link);
			items.add(links);
			
			links = new LinkedList<String>();
			links.add(item[3]);
			link = "www.informatics.jax.org/accession/"+item[3];
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add(item[4]);
			link = "www.ncbi.nlm.nih.gov/gquery/gquery.fcgi?term="+ item[1];
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add(item[5]);
			link = "www.ncbi.nlm.nih.gov/gquery/gquery.fcgi?term="+ item[5];
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add(item[6]);
			link = "www.ncbi.nlm.nih.gov/gquery/gquery.fcgi?term="+ item[6];
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add("GUDMAP");
			links.add(null);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add("UCSC");
			link = "genome.ucsc.edu/cgi-bin/hgNear?hgsid=80317038&org=Mouse&db=mm8&near_search="+item[1]+"&submit=Go!&near_order=expGnfAtlas2&near.count=50";
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add("KEGG");
			link = "www.genome.jp/dbget-bin/www_bfind_sub?dbkey=genes&keywords="+ item[1];
			links.add(link);			
			items.add(links);
			
			links = new LinkedList<String>();
			links.add("ENS");
			link = "www.ensembl.org/Mus_musculus/Gene/Summary?db=core;g="+item[1];
			links.add(link);			
			items.add(links);
			
			annotations.add(items);

		}
				
		return annotations;
	}

	private LinkedList<LinkedList<String>> getUrls(){
		
		LinkedList<LinkedList<String>> urls = new LinkedList<LinkedList<String>>();
		LinkedList<String> items;

		ArrayList<String[]> dataList = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
		for(String[] item : dataList){
			items = new LinkedList<String>();

			items.add("www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + item[0]);//platform
			items.add("/pages/gene.html?geneid="+item[3]);// gene
			items.add(item[2]);//probe seq id
			items.add("www.informatics.jax.org/accession/"+item[3]);//mgi gene id
			items.add("www.ncbi.nlm.nih.gov/gquery/gquery.fcgi?term="+ item[1]);//entrez geneid
			items.add(item[5]);//Human Ortholog Symbol
			items.add(item[6]);//Human Ortholog Entrez
			items.add("GUDMAP");//GUDMAP-ISH
			items.add("genome.ucsc.edu/cgi-bin/hgNear?hgsid=80317038&org=Mouse&db=mm8&near_search="+item[1]+"&submit=Go!&near_order=expGnfAtlas2&near.count=50");
			items.add("www.genome.jp/dbget-bin/www_bfind_sub?dbkey=genes&keywords="+ item[1]);
			items.add("www.ensembl.org/Mus_musculus/Gene/Summary?db=core;g="+item[1]);//ENS
			
			urls.add(items);
		}
				
		return urls;
	}
	
 }
