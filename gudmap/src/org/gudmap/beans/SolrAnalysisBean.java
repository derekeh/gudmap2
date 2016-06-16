package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;



import javax.servlet.ServletContext;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.dao.ArrayDao;
import org.gudmap.models.GenelistTreeInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * <h1>SolrAnalysisBean</h1>
 * The SolrAnalysisBean class contains the methods to provide data and deal with events on the
 * solrGenelist.xhtml web page
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
@Named (value="solrAnalysisBean")
@SessionScoped
public class SolrAnalysisBean implements Serializable  {
	
	private ArrayDao arrayDao;
	private String genelistId;
	private String masterTableId;	 
	
	private static final long serialVersionUID = 1L;	 
    
    @Inject
   	private SolrTreeBean solrTreeBean;

    @Inject
   	private SolrFilter solrFilter;
    
	private String solrInput;
	private HashMap<String,String> filters;
	private boolean showPageDetails = true;
   
	
    
    // Constructors -------------------------------------------------------------------------------

    public SolrAnalysisBean() {
    	arrayDao = new ArrayDao();   	
    	createJSONFile();    
    }
    
    public void init(String x) {
    	
    	getSolrInput();
    	filters = solrFilter.getFilters();
    	createGeneList(solrInput,filters);
    }
    
	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
	public void setSolrFilter(SolrFilter solrFilter){
		this.solrFilter = solrFilter;
	}
	
	public void setSolrInput(String solrInput){
		solrInput = solrTreeBean.getSolrInput();

	}
	
	public String getSolrInput(){
		solrInput = solrTreeBean.getSolrInput();

		return solrInput;
	}

	public String getSelectedItem() {
		return genelistId;
	}

	public void setSelectedItem(String item) {
		genelistId = item;
	}

	public String getMasterTableId() {
		return masterTableId;
	}

	public void setMasterTableId(String masterTableId) {
		this.masterTableId = masterTableId;
	}

    public void processAction(ActionEvent event) throws AbortProcessingException,Exception {    
		if (!genelistId.equalsIgnoreCase("0")){
			String uri = getResultURL();
			FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
		}
	
    }

    public String findNode() throws IOException
    {   
		if (genelistId.equalsIgnoreCase("0"))
			return null;

		return "leaf";
    }


    public String getResultURL () {
    	String result = "../db/browseHeatmap.jsf?genelistId="+ genelistId + "&masterTableId="+ masterTableId;    	
        return result;
    }
	
	
    public String getTitle(){
    	String str = "Genelists from Microarray Analysis";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getInsituCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getInsituCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getInsituCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getInsituCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }

	private void createGeneList(String solrInput, HashMap<String,String> filterlist){
//		ArrayList<String> genelistids = new ArrayList<String> ();

		SolrDocumentList sdl  = solrTreeBean.getGenelistData(solrInput, filterlist);
		
		String whereclause = "WHERE GNL_OID IN (";
	    for(SolrDocument doc : sdl){
	    	whereclause += doc.getFieldValue("ID").toString() + ",";
		}
	    int pos = whereclause.lastIndexOf(",");
	    whereclause = whereclause.substring(0, pos);
	    whereclause += ")";
		ArrayList<GenelistTreeInfo> genelist = arrayDao.getRefGenelists(whereclause);
		createJSONObject(genelist);
	    
	}
    
	private void createJSONFile(){
		ArrayList<GenelistTreeInfo> genelist = arrayDao.getRefGenelists();
		createJSONObject(genelist);
	}

	@SuppressWarnings("unchecked")
	private void createJSONObject(ArrayList<GenelistTreeInfo> genelist){
		
		JSONObject obj = new JSONObject();	
		obj.put("children", createIsPublished(genelist));
		
		JSONArray  outerlist = new JSONArray();
		outerlist.add(createPublished(genelist));
		outerlist.add(createUnpublished(genelist));
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String realPath = ctx.getRealPath("/");
			String path = realPath + "/resources/scripts/solrgenelist.json";
			
			FileWriter file = new FileWriter(path);
			file.write(outerlist.toJSONString());
			file.flush();
			file.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
				
	
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray createIsPublished(ArrayList<GenelistTreeInfo> genelist){

		JSONArray  plist = new JSONArray();

		// published datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> publishedGenelist = new ArrayList<GenelistTreeInfo>();
		ArrayList<GenelistTreeInfo> unpublishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
			else
				unpublishedGenelist.add(inf);				
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getDataset())){ 
					publishedDatasets.add(inf.getDataset());
				}
			}
		}
		
		JSONArray  list = new JSONArray();
		int id = -10000;
		for(String dataset : publishedDatasets){
			id = id - 1;
			list.add(createPublishedDatasets(publishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		plist.add(obj);
		
		// unpublished datasets
		obj = new JSONObject();
		obj.put("data", "Unpublished");
		obj.put("state", "open");

		attr = new JSONObject();
		attr.put("id", -2000);
		obj.put("attr", attr);
		

		ArrayList<String> unpublishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				if(!unpublishedDatasets.contains(inf.getDataset())){ 
					unpublishedDatasets.add(inf.getDataset());
				}
			}
		}
		Collections.sort(unpublishedDatasets);

		list = new JSONArray();
		id = -20000;
		for(String dataset : unpublishedDatasets){
			id = id - 1;
			list.add(createUnpublishedDatasets(unpublishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		plist.add(obj);
		
		return plist;		
	
	}
	@SuppressWarnings("unchecked")
	private JSONObject createPublished(ArrayList<GenelistTreeInfo> genelist){

		// published datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> publishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getDataset())){ 
					publishedDatasets.add(inf.getDataset());
				}
			}
		}
		
		JSONArray  list = new JSONArray();
		int id = -10000;
		for(String dataset : publishedDatasets){
			id = id - 1;
			list.add(createPublishedDatasets(publishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		return obj;
		
	
	}
	@SuppressWarnings("unchecked")
	private JSONObject createUnpublished(ArrayList<GenelistTreeInfo> genelist){

		ArrayList<GenelistTreeInfo> unpublishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("0"))
				unpublishedGenelist.add(inf);				
		}

		
		// unpublished datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Unpublished");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -2000);
		obj.put("attr", attr);
		

		ArrayList<String> unpublishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				if(!unpublishedDatasets.contains(inf.getDataset())){ 
					unpublishedDatasets.add(inf.getDataset());
				}
			}
		}
		Collections.sort(unpublishedDatasets);

		JSONArray list = new JSONArray();
		int id = -20000;
		for(String dataset : unpublishedDatasets){
			id = id - 1;
			list.add(createUnpublishedDatasets(unpublishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		return obj;		
	
	}

	@SuppressWarnings("unchecked")
	private JSONObject createUnpublishedDatasets(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> samples = new ArrayList<String>();
		ArrayList<GenelistTreeInfo> unnamedsamples = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset())){
				String sample = inf.getSample();
				if(sample.isEmpty()){
					unnamedsamples.add(inf);
				}
				else if(!samples.contains(sample)){
						samples.add(sample);
				}
			}
		}
		Collections.sort(samples);

		
		
		JSONArray  list = new JSONArray();
		
		list.addAll(createLevelAll(unnamedsamples));
		
		int id2 = -10;
		for(String sample : samples){
			id2 = id2-1;
			list.add(createSamples(genelist, dataset, "", sample, id2));	
		}				
		obj.put("children", list);
				
		return obj;
	}	
//	@SuppressWarnings("unchecked")
//	private JSONObject createUnpublishedDatasets2(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
//		
//		JSONObject obj = new JSONObject();		
//		obj.put("data", dataset);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id", id);
//		attr.put("title", "Dataset = "+dataset);
//		obj.put("attr", attr);
//
//		ArrayList<String> stages = new ArrayList<String>();
//		ArrayList<String> multistages = new ArrayList<String>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset())){
//				if(!stages.contains(inf.getStage()) && !inf.getStage().trim().contentEquals("0")){
//					if (inf.getStage().length() == 4)
//						stages.add(inf.getStage());
//					else if(!multistages.contains(inf.getStage()))
//						multistages.add(inf.getStage());
//				}
//			}
//		}
//		Collections.sort(stages);
//		Collections.sort(multistages);
//		
//		JSONArray  list = new JSONArray();
//		
//		int id2 = -10;
//		for(String multistage : multistages){
//			id2 = id2-1;
//			list.add(createMultiStages(genelist, dataset, multistage, id2));					
//		}				
//		
//		for(String stage : stages){
//			id2 = id2-1;
//			list.add(createStages(genelist, dataset, stage, id2));					
//		}				
//		obj.put("children", list);
//				
//		return obj;
//	}

//	@SuppressWarnings("unchecked")
//	private JSONObject createStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, int id){
//
//		JSONObject obj = new JSONObject();		
//		obj.put("data", stage);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id",id);				
//		attr.put("title", "Dataset = "+ dataset + ", " + stage);
//		obj.put("attr", attr);
//
//		ArrayList<String> samples = new ArrayList<String>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
//				if(!samples.contains(inf.getSample()) && !inf.getSample().trim().equalsIgnoreCase(""))
//					samples.add(inf.getSample());
//			}
//		}
//		Collections.sort(samples);
//		
//		
//		JSONArray  list = new JSONArray();
//		int id2 = -100;
//		for(String sample : samples){
//			id2 = id2 - 1;
//			list.add(createSamples(genelist, dataset, stage, sample, id2));					
//		}				
//		obj.put("children", list);
//				
//		return obj;
//	}

//	@SuppressWarnings("unchecked")
//	private JSONObject createMultiStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, int id){
//
//		System.out.println("dataset = "+ dataset + " stage = "+ stage);
//
//		ArrayList<String> samples = new ArrayList<String>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
//				if(!samples.contains(inf.getSample()) && !inf.getSample().trim().equalsIgnoreCase(""))
//					samples.add(inf.getSample());
//			}
//		}
//		Collections.sort(samples);
//
//		JSONObject obj = new JSONObject();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
//				if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
//					System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
//					obj = new JSONObject();
//					obj.put("data", inf.getName());
//					JSONObject attr1 = new JSONObject();
//					attr1.put("id", inf.getGenelistOID());
//					String table = "";
//					if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
//						table = "4_" + inf.getDatasetId();
//					else
//						table = "3_" + inf.getDatasetId();
//					attr1.put("table", table);
//					attr1.put("rel", "Role");							
//					attr1.put("title", "Dataset = "+ dataset +  ", " + stage);
//					obj.put("attr", attr1);
//					return obj;
//				}
//			}
//		}
//
//		obj = new JSONObject();		
//		int id2 = -100;
//		for(String sample : samples){
//			id2 = id2 - 1;
//			obj = createSamples(genelist, dataset, stage, sample, id2);					
//		}				
//				
//		return obj;
//	}
	
//	@SuppressWarnings("unchecked")
//	private JSONObject createUnnamedSamples(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, String sample, int id){
//
//		JSONObject obj = new JSONObject();
//		if(sample.contains("GSM") || sample.isEmpty()){
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
//				if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
//					System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
//					obj = new JSONObject();
//					obj.put("data", inf.getName());
//					JSONObject attr1 = new JSONObject();
//					attr1.put("id", inf.getGenelistOID());
//					String table = "";
//					if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
//						table = "4_" + inf.getDatasetId();
//					else
//						table = "3_" + inf.getDatasetId();
//					attr1.put("table", table);
//					attr1.put("rel", "Role");							
//					attr1.put("title", "Dataset = "+ dataset +  ", " + stage);
//					obj.put("attr", attr1);
//					return obj;
//				}
//			}
//		}
//		}
//		
//		
//		
//
//
//
//		obj.put("data", sample);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id",id);
//		attr.put("title", "Dataset = "+ dataset + ", Sample = " +sample);
//		obj.put("attr", attr);
//
//
//		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
//				if(!allList.contains(inf.getSubset2()))
//					allList.add(inf);
//			}
//		}
//
//		JSONArray  list = new JSONArray();
//		list = createLevelAll(allList);
//		obj.put("children", list);
//				
//		return obj;
//	}

	@SuppressWarnings("unchecked")
	private JSONObject createSamples(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, String sample, int id){

		JSONObject obj = new JSONObject();
		if(sample.contains("GSM") || sample.isEmpty()){
			for(GenelistTreeInfo inf : genelist){
				if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
					if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
						System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
						obj = new JSONObject();
						obj.put("data", inf.getName());
						JSONObject attr1 = new JSONObject();
						attr1.put("id", inf.getGenelistOID());
						String table = "";
						if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
							table = "4_" + inf.getDatasetId();
						else
							table = "3_" + inf.getDatasetId();
						attr1.put("table", table);
						attr1.put("rel", "Role");							
						attr1.put("title", "Dataset = "+ dataset +  ", " + stage);
						obj.put("attr", attr1);
						return obj;
					}
				}
			}
		}
		
		obj.put("data", sample);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);
		attr.put("title", "Dataset = "+ dataset + ", Sample = " +sample);
		obj.put("attr", attr);


		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
				if(!allList.contains(inf.getSubset2()))
					allList.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		list = createLevelAll(allList);
		obj.put("children", list);
				
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray createLevelAll(ArrayList<GenelistTreeInfo> genelist){

		JSONArray  list = new JSONArray();
		
		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if (inf.getSubset2() != null){
				if(!allList.contains(inf.getSubset2()) && inf.getSubset2().equalsIgnoreCase("All"))
					allList.add(inf);
			}
			else{
				JSONObject obj = new JSONObject();		
				obj.put("data", inf.getName() + "(" + inf.getEntityCount() + " probes, " + inf.getGeneCount() + " genes)");
				String table = "";
				if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
					table = "4_" + inf.getDatasetId();
				else
					table = "3_" + inf.getDatasetId();
				JSONObject attr = new JSONObject();
				
				attr.put("id", inf.getGenelistOID());
				attr.put("table", table);
				attr.put("rel", "Role");
				attr.put("title", "Dataset = "+ inf.getDataset() + ", " + inf.getGenelistType() + ", " + inf.getAuthor());
				obj.put("attr", attr);
				System.out.println("object = "+ obj);
				list.add(obj);

			}
		}
		
		for (GenelistTreeInfo all : allList){
			JSONObject obj = new JSONObject();		
			obj.put("data", all.getName() + "(" + all.getEntityCount() + " probes, " + all.getGeneCount() + " genes)");
			obj.put("state", "closed");
			String table = "";
			if (all.getDatasetId().contentEquals("5") || all.getDatasetId().contentEquals("6") || all.getDatasetId().contentEquals("7"))
				table = "4_" + all.getDatasetId();
			else
				table = "3_" + all.getDatasetId();
			
			JSONObject attr = new JSONObject();
			attr.put("id", all.getGenelistOID());
			attr.put("table", table);
			attr.put("rel", "Role");
			attr.put("title", "Dataset = "+ all.getDataset() + ", Sample = " + all.getSample() + ", " + all.getStage() + ", " + all.getGenelistType() + ", " + all.getAuthor());
			obj.put("attr", attr);

			JSONArray  clusterlist = new JSONArray();
			clusterlist = createLevelCluster(genelist, all.getEntityCount());
			obj.put("children", clusterlist);
					
			list.add(obj);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private JSONArray createLevelCluster(ArrayList<GenelistTreeInfo> genelist, String count){

		ArrayList<GenelistTreeInfo> kList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if (inf.getSubset1() != null){
				if(!kList.contains(inf.getSubset1()) && inf.getSubset1().equalsIgnoreCase(count) && !inf.getSubset2().equalsIgnoreCase("All"))
					kList.add(inf);
			}
		}
		JSONArray  list = new JSONArray();
		
		for (GenelistTreeInfo k : kList){
			JSONObject obj = new JSONObject();		
			obj.put("data", k.getName() + "(" + k.getEntityCount() + " probes, " + k.getGeneCount() + " genes)");
			String table = "";
			if (k.getDatasetId().contentEquals("5") || k.getDatasetId().contentEquals("6") || k.getDatasetId().contentEquals("7"))
				table = "4_" + k.getDatasetId();
			else
				table = "3_" + k.getDatasetId();
			
			JSONObject attr = new JSONObject();
			attr.put("id", k.getGenelistOID());
			attr.put("table", table);
			attr.put("title", "Dataset = "+ k.getDataset() + ", Sample = " + k.getSample() + ", " + k.getStage() + ", " + k.getGenelistType() + ", " + k.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}

	
	
	@SuppressWarnings("unchecked")
	private JSONObject createPublishedDatasets(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "open");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> publishers = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset())){
				if(!publishers.contains(inf.getLpuRef()) && inf.getLpuRef() != null)
					publishers.add(inf.getLpuRef());
			}
		}
		Collections.sort(publishers);
						
		JSONArray  list = new JSONArray();

		for(String publisher : publishers){
			id = id-100;
			list.add(createPublisher(genelist, dataset, publisher, id));					
		}				
		obj.put("children", list);
				
		return obj;
	}

	@SuppressWarnings("unchecked")
	private JSONObject createPublisher(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", publisher);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> names = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
				if(!names.contains(inf.getName()))
					names.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		
		list = createPublishedNames(names);
		obj.put("children", list);
				
		return obj;
	}

//	@SuppressWarnings("unchecked")
//	private JSONObject createPublisher2(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, int id){
//		
//		JSONObject obj = new JSONObject();		
//		obj.put("data", publisher);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id", id);
//		obj.put("attr", attr);
//
//		ArrayList<String> stages = new ArrayList<String>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
//				if(!stages.contains(inf.getStage()) && !inf.getStage().trim().contentEquals("0"))
//					stages.add(inf.getStage());
//			}
//		}
//		Collections.sort(stages);
//
//		JSONArray  list = new JSONArray();
//		
//		// put multiple stages here outside the stage node
//		String redundantStage = "";
//		for(String stage1 : stages){
//			if (stage1.length() > 4){
//				for(GenelistTreeInfo inf : genelist){
//					if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage1.equalsIgnoreCase(inf.getStage())){						
//						JSONObject obj1 = new JSONObject();
//						obj1.put("data", inf.getName());
//						JSONObject attr1 = new JSONObject();
//						attr1.put("id", inf.getGenelistOID());
//						String table = "";
//						if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
//							table = "4_" + inf.getDatasetId();
//						else
//							table = "3_" + inf.getDatasetId();
//						attr1.put("table", table);
//						attr1.put("rel", "Role");
//						attr1.put("title", "Dataset = "+ inf.getDataset() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
//						obj1.put("attr", attr1);
//						list.add(obj1);
//						redundantStage = stage1;
//					}
//					
//				}
//			}
//		}
//		if (redundantStage.length() > 0)
//			stages.remove(redundantStage);
//			
//		for(String stage : stages){
//			id = id-1;
//			list.add(createPublishedStages(genelist, dataset, publisher, stage, id));					
//		}				
//		obj.put("children", list);
//				
//		return obj;
//	}
	
//	@SuppressWarnings("unchecked")
//	private JSONObject createPublishedStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, String stage, int id){
//
//		JSONObject obj = new JSONObject();
//		
//		obj.put("data", stage);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id",id);				
//		attr.put("title", "Dataset = "+ dataset + ", " + stage);
//		obj.put("attr", attr);
//
//		ArrayList<GenelistTreeInfo> names = new ArrayList<GenelistTreeInfo>();
//		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage.equalsIgnoreCase(inf.getStage())){
//				if(!names.contains(inf.getName()))
//					names.add(inf);
//			}
//		}
//		
//		JSONArray  list = new JSONArray();
//		list = createPublishedNames(names);
//		obj.put("children", list);
//				
//		return obj;
//	}

	@SuppressWarnings("unchecked")
	private JSONArray createPublishedNames(ArrayList<GenelistTreeInfo> genelist){
	
		JSONArray  list = new JSONArray();
		
		for (GenelistTreeInfo inf : genelist){
			JSONObject obj = new JSONObject();		
			obj.put("data", inf.getName() + "(" + inf.getEntityCount() + " probes, " + inf.getGeneCount() + " genes)");
			
			JSONObject attr = new JSONObject();
			attr.put("id", inf.getGenelistOID());
			String table = "";
			if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
				table = "4_" + inf.getDatasetId();
			else
				table = "3_" + inf.getDatasetId();
			attr.put("table", table);
			attr.put("rel", "Role");
			attr.put("title", "Dataset = "+ inf.getDataset() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}
   
}
