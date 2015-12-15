package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.handler.extraction.ExtractingParams;
import org.gudmap.assemblers.MicroarrayHeatmapBeanAssembler;
import org.gudmap.assemblers.SolrIndexAssembler;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.utils.SolrUtil;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;



@Named (value="solrIndexBean")
@RequestScoped
public class SolrIndexBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SolrIndexAssembler assembler;
	private SolrUtil solrUtil;

    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
	private ArrayList<MasterTableInfo> tableinfo;		
    private int maxColNumber = 0;
	
	public SolrIndexBean(){
		solrUtil = new SolrUtil();
		assembler = new SolrIndexAssembler();
		microarrayHeatmapBeanAssembler = new MicroarrayHeatmapBeanAssembler();
        tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
    }

	// methods to update the solr indexes used for searching
	public String indexAll(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);

		server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		
		server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);

//		server = solrUtil.getMicroarrayServer();
//		assembler.updateMicroarrayIndex(server);

		server = solrUtil.getSamplesServer();
		assembler.updateSamplesIndex(server);

		server = solrUtil.getSeriesServer();
		assembler.updateSeriesIndex(server);
		
		server = solrUtil.getTissuesServer();
		assembler.updateTissueIndex(server);

		server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		
		server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);
		
		return null;
		
	}

	public String indexGenes(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);
		return null;		
	}

	public String indexInsitu(){
		
		HttpSolrClient server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);
		return null;		
	}

	public String indexMicroarray(){
		
//		HttpSolrClient server = solrUtil.getMicroarrayServer();
//		assembler.updateMicroarrayIndex(server);
		
//		server = solrUtil.getSamplesServer();
//		assembler.updateSamplesIndex(server);
//
//		server = solrUtil.getSeriesServer();
//		assembler.updateSeriesIndex(server);
		
		return null;		
	}

	public String indexGenelists(){
		
		HttpSolrClient server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		return null;		
	}

	public String indexTissues(){
		
		HttpSolrClient server = solrUtil.getTissuesServer();
		assembler.updateGenelistsIndex(server);
		return null;		
	}

	public String indexMouseStrains(){
		
		HttpSolrClient server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		return null;		
	}
	
	public String indexImages(){
		
		HttpSolrClient server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);
		return null;		
	}

	public String indexNextGenSeries(){
		
		HttpSolrClient server = solrUtil.getNextGenSeriesServer();
		assembler.updateNextGenSeriesIndex(server);
		return null;		
	}
	
	public String indexNextGenSamples(){
		
		HttpSolrClient server = solrUtil.getNextGenSamplesServer();
		assembler.updateNextGenSamplesIndex(server);
		return null;		
	}
		
	public String indexTutorials(){
		
		HttpSolrClient server = solrUtil.getTutorialServer();
		assembler.updateTutorialIndex(server);
		return null;		
	}

	public void createGenestripFiles(){
		ArrayList<String> geneIds = new ArrayList<String>();
		for (int i = 0; i < 25000; i++){

			geneIds = microarrayHeatmapBeanAssembler.getGeneIds(i,1); 
			if (!geneIds.isEmpty()){	
			   	for (String geneId : geneIds){
			   		createGenestripJSONFile(geneId);
			   	} 
			}
		}
	}
	
	private void createGenestripJSONFile(String geneId){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//			String path = ctx.getRealPath("/");			
//			path += "/resources/genestrips/genestrip_" + geneId + ".json";
			
//			String geneIdn = geneId.replaceAll(":", "_");
			String path = "/export/data0/bernardh/MAWWW/Public/html/AppFiles/genestrips/genestrip_" + geneId + ".json";
			File f = new File(path);
			if (!f.exists()){
				FileWriter writer = new FileWriter(f);
				
				JSONObject obj = createGenestripJSONObject(geneId);
				
				writer.write(obj.toJSONString());
				writer.flush();
				writer.close();
			}

		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	private JSONObject createGenestripJSONObject(String geneId){
		
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

	
//	private void createHeatmapJSONFile(){
//		
//		try{
//			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
////			String path = ctx.getRealPath("/");
//			
//			if (genelistId != null){
//				// cache the analysis json files
//				
////				path += "/resources/scripts/" + genelistId + "heatmap.json";
//				String path = "/export/data0/bernardh/MAWWW/Public/html/AppFiles/genestrips/genestrip_" + genelistId + ".json";
//				
//				File f = new File(path);
//				if (!f.exists()){
//				
//					FileWriter writer = new FileWriter(f);
//					
//					JSONObject obj = createHeatmapJSONObject();
//					
//					writer.write(obj.toJSONString());
//					writer.flush();
//					writer.close();
//				}				
//				
//			}
//
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
//		
//	}
//	
//	private JSONObject createHeatmapJSONObject(){
//		
//		JSONObject obj = new JSONObject();
//		
//		obj.put("samples", getSamples());
//		obj.put("probes", getProbes());
//		obj.put("genes", getGenes());
//		obj.put("data", getDataValues());
//		obj.put("adjdata", getDataAdjValues());
//		obj.put("annotations", getProbeAnnotations());
//		
//				
//		return obj;
//	}
//	
//	private LinkedList<String> getSamples(){
//		
//		LinkedList<String> samples = new LinkedList<String>();
//
//		ArrayList<String> expressionTitles = assembler.getHeatmapExpressionTitlesFromMasterTableId(masterTableId);
//		for(String expressionTitle : expressionTitles){
//			samples.add(expressionTitle);
//		}
//			
//		return samples;
//	}
//	
//	private LinkedList<String> getProbes(){
//		
//		LinkedList<String> probes = new LinkedList<String>();
//
//		String platformId = assembler.getPlatformIdFromMasterTableId(masterTableId);
//		
//		if (genelistId != null){
//			probeIds = assembler.getProbeSetIdsByGenelistIdAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, genelistId, platformId);
//		}
//		else{
//			probeIds = assembler.getProbeSetIdsBySymbolAndPlatformId(firstRow, rowsPerPage, sortField, sortAscending, geneId, platformId);
//		}
//
//		for(String probe : probeIds){
//			probes.add(probe);
//		}
//			
//		return probes;
//	}
//
//	private LinkedList<String> getGenes(){
//		
//		LinkedList<String> genes = new LinkedList<String>();
//
//		ArrayList<String[]> annotations = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
//		for (String[] annotation : annotations){
//			
//			 genes.add(annotation[1]);
//		}
//			
//		return genes;
//	}
//
//	private LinkedList<LinkedList<String>> getDataValues(){
//		
//		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
//		LinkedList<String> items;
//
//		for (String probeId : probeIds){
//			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, masterTableId);
//			items = new LinkedList<String>();
//			for(String[] item : dataList){
//				String rma = item[2];
//				String scaledRma = item[5];
//				
//				items.add(rma);
//			}
//			data.add(items);
//		}
//				
//		return data;
//	}
//	
//	private LinkedList<LinkedList<String>> getDataAdjValues(){
//		
//		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
//		LinkedList<String> items;
//
//		for (String probeId : probeIds){
//			ArrayList<String[]> dataList = assembler.getHeatmapDataFromProbeIdAndMasterTableId(firstRow, rowsPerPage, sortField, sortAscending, probeId, masterTableId);
//			items = new LinkedList<String>();
//			for(String[] item : dataList){
//				String rma = item[2];
//				String scaledRma = item[5];
//				
//				items.add(scaledRma);
//			}
//			data.add(items);
//		}
//				
//		return data;
//	}
//
//	private LinkedList<LinkedList<String>> getProbeAnnotations(){
//		
//		LinkedList<LinkedList<String>> annotations = new LinkedList<LinkedList<String>>();
//		LinkedList<String> items;
//
//		ArrayList<String[]> dataList = assembler.getAnnotationByProbeSetIds(firstRow, rowsPerPage, sortField, sortAscending, probeIds);
//		for(String[] item : dataList){
//			items = new LinkedList<String>();
//
//			items.add(item[0]);
//			items.add(item[1]);
//			items.add(item[2]);
//			items.add(item[3]);
//			items.add(item[4]);
//			items.add(item[5]);
//			items.add(item[6]);
//			items.add("GUDMAP");
//			items.add("UCSC");
//			items.add("KEGG");
//			items.add("ENS");
//			
//			annotations.add(items);
//		}
//				
//		return annotations;
//	}
	

	public ArrayList<String> getGeneList() {
		
		return assembler.getGeneList();
		
	}
	
}
