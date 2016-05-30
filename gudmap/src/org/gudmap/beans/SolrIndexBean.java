package org.gudmap.beans;


import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
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



/**
 * @author bernardh
 *
 */
@Named (value="solrIndexBean")
@RequestScoped
public class SolrIndexBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SolrIndexAssembler assembler;
	private SolrUtil solrUtil;

    private MicroarrayHeatmapBeanAssembler microarrayHeatmapBeanAssembler;
	private ArrayList<MasterTableInfo> tableinfo;		
    private int maxColNumber = 0;
	
	/**
	 * Constructor for SolrIndexBean
	 */
	public SolrIndexBean(){
		solrUtil = new SolrUtil();
		assembler = new SolrIndexAssembler();
		microarrayHeatmapBeanAssembler = new MicroarrayHeatmapBeanAssembler();
        tableinfo = microarrayHeatmapBeanAssembler.getMasterTableList();
    }

	/**
	 * This method creates all the indexes required by the Solr Search Engine.
	 * 
	 * @return
	 */
	public String indexAll(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);

		server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		
		server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);

		server = solrUtil.getSamplesServer();
		assembler.updateSamplesIndex(server);

		server = solrUtil.getTissuesServer();
		assembler.updateTissueIndex(server);

		server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		
		server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);

		server = solrUtil.getNextGenSamplesServer();
		assembler.updateNextGenSamplesIndex(server);

		server = solrUtil.getWebServer();
		assembler.updateWebIndex(server);
		
		return null;
		
	}

	/**
	 * This method rebuilds the gudmap_genes index.
	 * 
	 * @return
	 */
	public String indexGenes(){
		
		HttpSolrClient server = solrUtil.getGenesServer();
		assembler.updateGenesIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_insitu index.
	 * 
	 * @return
	 */
	public String indexInsitu(){
		
		HttpSolrClient server = solrUtil.getInsituServer();
		assembler.updateInsituIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_samples index.
	 * 
	 * @return
	 */
	public String indexSamples(){
		
		HttpSolrClient server = solrUtil.getSamplesServer();
		assembler.updateSamplesIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_genelists index.
	 * 
	 * @return
	 */
	public String indexGenelists(){
		
		HttpSolrClient server = solrUtil.getGenelistsServer();
		assembler.updateGenelistsIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_tissues index.
	 * 
	 * @return
	 */
	public String indexTissues(){
		
		HttpSolrClient server = solrUtil.getTissuesServer();
		assembler.updateTissueIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_mousestrains index.
	 * 
	 * @return
	 */
	public String indexMouseStrains(){
		
		HttpSolrClient server = solrUtil.getMouseStrainServer();
		assembler.updateMouseStrainsIndex(server);
		return null;		
	}
	
	/**
	 * This method rebuilds the gudmap_images index.
	 * 
	 * @return
	 */
	public String indexImages(){
		
		HttpSolrClient server = solrUtil.getImageServer();
		assembler.updateImageIndex(server);
		return null;		
	}
	
	/**
	 * This method rebuilds the gudmap_nextgen_samples index.
	 * 
	 * @return
	 */
	public String indexNextGenSamples(){
		
		HttpSolrClient server = solrUtil.getNextGenSamplesServer();
		assembler.updateNextGenSamplesIndex(server);
		return null;		
	}

	/**
	 * This method rebuilds the gudmap_web index.
	 * 
	 * @return
	 */
	public String indexWeb(){
		
		HttpSolrClient server = solrUtil.getWebServer();
		assembler.updateWebIndex(server);
		return null;		
	}
	
	/**
	 * This method creates a series of genestrip json files.
	 * Each file contains the data to create the D3 image.
	 */
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
	
	/**
	 * This method generates a json file based on the geneId.
	 * 
	 * @param geneId
	 */
	private void createGenestripJSONFile(String geneId){
		
		try{
//			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
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
	
	@SuppressWarnings("unchecked")
	private JSONObject createGenestripJSONObject(String geneId){
		
		JSONObject obj = new JSONObject();
		
		obj.put("labels", getLabels(geneId));
		obj.put("links", getLinks(geneId));
//		obj.put("data", getDataValues(geneId));
		obj.put("adjdata", getDataAdjValues(geneId));
						
		return obj;
	}
	

	
	private LinkedList<String> getLabels(String geneId){
		
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
//    			int colCounter = 1;
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String rma = item[2];
    				items.add(rma);	
//					colCounter ++;
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
//    			int colCounter = 1;
    			items = new LinkedList<String>();
    			for(String[] item : dataList){
    				String scaledRma = item[5];
    				items.add(scaledRma);	
//					colCounter ++;
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


	public ArrayList<String> getGeneList() {
		
		return assembler.getGeneList();
		
	}
	
}
