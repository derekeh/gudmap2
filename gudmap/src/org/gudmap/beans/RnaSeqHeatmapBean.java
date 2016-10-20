package org.gudmap.beans;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.impl.PagerImpl;
import org.gudmap.queries.array.SequenceQueries;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h1>RnaSeqHeatmapBean</h1>
 * The RnaSeqHeatmapBean class contains the methods to display the browseSeqHeatmap.xhtml
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2016 
 */
@Named (value="rnaSeqHeatmapBean")
@SessionScoped
//@RequestScoped
public class RnaSeqHeatmapBean extends PagerImpl  implements Serializable{
	

	private static final long serialVersionUID = 1L;	
 
	private int topGeneCount = 50;
	private int selectedSampleCol = 2;
	private String cellSize = "15";
	private String selectedSample = "AdultProximal_Tubules-1";
	private String selectedSeries;
	private String selectedGene;
	private String seqFile;
	private String seqDir;
	private ArrayList<String> biotypes;
	private ArrayList<String> biotypeList;
	private int geneBioTypesCount = 0;	
	
	private String tableTitle;
    
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
   
    
    @Inject
   	protected SessionBean sessionBean;    
    
    public RnaSeqHeatmapBean() {
    	
       	super(1000,10,null,true); 
       	
       	biotypeList = new ArrayList<String>();
       	biotypeList.add("3prime_overlapping_ncRNA");        
       	biotypeList.add("antisense");        
 		biotypeList.add("bidirectional_promoter_lncRNA");        
 		biotypeList.add("IG_pseudogene");        
 		biotypeList.add("IG_C_gene");        
 		biotypeList.add("IG_C_pseudogene");        
 		biotypeList.add("IG_D_gene");        
 		biotypeList.add("IG_D_pseudogene");        
 		biotypeList.add("IG_J_gene");        
  		biotypeList.add("IG_LV_gene");        
 		biotypeList.add("IG_V_gene");        
 		biotypeList.add("IG_V_pseudogene");        
		biotypeList.add("lincRNA");        
 		biotypeList.add("macro_lncRNA");
 		biotypeList.add("miRNA");
 		biotypeList.add("misc_RNA");
 		biotypeList.add("Mt_tRNA");
		biotypeList.add("polymorphic_pseudogene");        
		biotypeList.add("processed_transcript");        
		biotypeList.add("protein_coding");
		biotypeList.add("processed_pseudogene");        
		biotypeList.add("pseudogene");        
 		biotypeList.add("ribozyme");        
		biotypeList.add("rRNA");        
		biotypeList.add("sRNA");        
		biotypeList.add("scaRNA");        
		biotypeList.add("snRNA");        
		biotypeList.add("snoRNA");        
		biotypeList.add("sense_intronic");        
		biotypeList.add("sense_overlapping");        
		biotypeList.add("TEC");        
 		biotypeList.add("TR_C_gene");        
 		biotypeList.add("TR_D_gene");        
 		biotypeList.add("TR_J_gene");        
 		biotypeList.add("TR_J_pseudogene");        
 		biotypeList.add("TR_V_gene");        
 		biotypeList.add("TR_V_pseudogene");        
		biotypeList.add("transcribed_processed_pseudogene");        
		biotypeList.add("transcribed_unprocessed_pseudogene");        
		biotypeList.add("transcribed_unitary_pseudogene");        
 		biotypeList.add("unitary_pseudogene");        
		biotypeList.add("unprocessed_pseudogene"); 

		geneBioTypesCount = biotypeList.size();       	
		biotypes = biotypeList;      	
       	
    }
   
	/**
	 * This method returns the current gene type from the GeneBioType control 
	 * of the browseSeqHeatmap.xhtml page.
	 * 
	 * @return The selected gene biotypes
	 */
    public ArrayList<String> getBiotypes() {
    	return biotypes;
    }
    
	/**
	 * This method sets the selected gene biotypes to be displayed in the GeneBioType control 
	 * of the browseSeqHeatmap.xhtml page.
	 * @param val 

	 */
    public void setBiotypes(ArrayList<String> val) {
    	int c = val.size();
    	this.biotypes = val;
    }
 
	/**
	 * This method returns a list of gene biotypes to be displayed in the GeneBioType control 
	 * of the browseSeqHeatmap.xhtml page.
	 * This filters the number of genes to be displayed in the heatmap.
	 * 
	 * @return A list of gene biotypes
	 */    
	public ArrayList<String> getBiotypeList(){
		return biotypeList;
	}	
    
	/**
	 * This method returns a map of counts to be displayed in the Table Size control 
	 * of the browseSeqHeatmap.xhtml page.
	 * This sets the number of rows to be displayed in the heatmap.
	 * 
	 * @return A Map of counts
	 */    
    public Map<String,Integer> getTopGeneCountOptions() {
    	LinkedHashMap<String,Integer> options = new LinkedHashMap<String,Integer>();
    	options.put("25",25);
    	options.put("50",50);
    	options.put("100",100);
    	options.put("250",250);
    	options.put("500",500);
    	options.put("1000",1000);
    	
    	return options;
    }
    
	/**
	 * This method returns the current row count from the Table Size control 
	 * of the browseSeqHeatmap.xhtml page.
	 * 
	 * @return The topGeneCount
	 */
    public int getTopGeneCount() {
    	return topGeneCount;
    }
    
	/**
	 * This method sets the row count to be displayed in the Table Size control 
	 * of the browseSeqHeatmap.xhtml page.
	 * 
	 * @param count
	 */
    public void setTopGeneCount(int count) {
    	topGeneCount = count;
    }
    
	/**
	 * This method redisplays the heatmap when the row count in the Table Size control changes.
	 * 
	 * @param e
	 */
	public void topGeneCountChanged(ValueChangeEvent e){
		topGeneCount = (int)e.getNewValue();
		loadDataList();    	
	}

    public String getCellSize() {
    	return cellSize;
    }
    
	/**
	 * This method sets the cell size of the heatmap displayed in the browseSeqHeatmap.xhtml page.
	 * The cell size is incremented/decremented by the Heatmap Scale button on the browseSeqHeatmap.xhtml page.
	 * 
	 * @param cellSize
	 */
    public void setCellSize(String cellSize) {
    	this.cellSize = cellSize;
    }
	
	/**
	 * This method gets the selected sample used as the sort column in the displayed heatmap of 
	 * the browseSeqHeatmap.xhtml page.
	 * 
	 * @return The selectedSample
	 */
    public String getSelectedSample() {
    	return selectedSample;
    }
    
	/**
	 * This method sets the selected sample. The selected sample is used as the sort column in
	 * the displayed heatmap of the browseSeqHeatmap.xhtml page.
	 * 
	 * @param cellSize
	 */
    public void setSelectedSample(String sample) {
    	selectedSample = sample;
    	createSeqFile(selectedSeries);
    	createSeqFile2(selectedSeries);
//    	init(selectedSample);
    }

	/**
	 * This method gets the selected gene used as the sort row in the displayed heatmap of 
	 * the browseSeqHeatmap.xhtml page.
	 * 
	 * @return The selectedGene
	 */
    public String getSelectedGene() {
    	return selectedGene;
    }
    
	/**
	 * This method sets the selected gene. The selected gene is used as the sort row in
	 * the displayed heatmap of the browseSeqHeatmap.xhtml page.
	 * The selected gene is used to resort the rows when the column sort changes
	 * 
	 * @param cellSize
	 */
    public void setSelectedGene(String gene) {
    	selectedGene = gene;
    }

    public String getSelectedSeries() {
    	return selectedSeries;
    }
    
    public void setSelectedSeries(String series) {
    	selectedSeries = series;
    }
    
    public String getSeqFile() {
    	return seqFile;
    }
    
    public void setSeqFile(String fileName) {
    	seqFile = fileName;
    }
    
    public String getSeqDir() {
    	return seqDir;
    }
    
    public void setSeqDir(String dir) {
    	seqDir = dir;
    }
    
	public RnaSeqHeatmapBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}
    
    public void setSessionBean(SessionBean sessionBean){
		this.sessionBean=sessionBean;
	}
    
    public SessionBean getSessionBean() {
    	return sessionBean;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException,Exception {  
    	loadDataList();
    }

   
// ********************   deal with heatmap  
	
	
    @Override
    public void loadDataList() {
    	if(Globals.getParameterValue("seriesID")!=null){
    		selectedSeries = Globals.getParameterValue("seriesID");
    		createSeqFile(selectedSeries);
    		createSeqFile2(selectedSeries);
//    		init(selectedSample);    		
    	}
    }
	
	public String getTitle() {
		
		return tableTitle;
	}
	
    public void refresh(){
     	getBiotypes();
     	createSeqFile(selectedSeries);
     	createSeqFile2(selectedSeries);
//    	init(selectedSample);
    }
    
    public void resetAll() {
    	setBiotypes(biotypeList);
    	createSeqFile(selectedSeries);  
    	createSeqFile2(selectedSeries);
//    	init(selectedSample);
//		loadDataList();
	}

    public void clear(){
    	biotypes.clear();;
    }
    
	@SuppressWarnings("unchecked")
	public void init(String sample){
		
		
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = ctx.getRealPath("/");
		path += "/resources/genestrips/rnaseq_test.csv";
		
		JSONObject obj = new JSONObject();
		BufferedReader br = null;
		List<String[]> geneList = null;
		String[] header = null;
		try {
			br = new BufferedReader(new FileReader(path));
			
			String line;
			geneList = new ArrayList<String[]>();
			header = br.readLine().split(",");
			LinkedList<String> newheader = new LinkedList<String>();
			for(int i = 2; i < header.length-1; i++){
				String s = header[i].replace("cufflink_", "");
				s = s.replace("_genes.fpkm_tracking.collapse", "");
				s = s.replace(".genes.fpkm_tracking.collapse", "");				
				newheader.add(s);
			}			
			obj.put("samples", newheader);
			selectedSampleCol = newheader.indexOf(sample) + 2;
			
			
			LinkedList<String> maxvalues = new LinkedList<String>();
			String[] max = br.readLine().split(",");
			for(int i = 2; i < max.length-1; i++){
				maxvalues.add(max[i]);
			}
			obj.put("maxvalues", maxvalues);
			
			while ((line = br.readLine()) != null){
				String[] data = line.split(",");
				geneList.add(data);			
			}
			
			// sort the genelist
			Collections.sort(geneList, new Comparator<String[]> () {
			    @Override
			    public int compare(String[] a, String[] b) {
			    	if(a[selectedSampleCol].isEmpty() || a[selectedSampleCol] == null) a[selectedSampleCol] = "0";
			    	if(b[selectedSampleCol].isEmpty() || b[selectedSampleCol] == null) b[selectedSampleCol] = "0";
			    	Float f1 = Float.parseFloat(a[selectedSampleCol]);
			    	Float f2 = Float.parseFloat(b[selectedSampleCol]);
			        return f2.compareTo(f1);
			    }
			});

			// filter genelist by selected biotypes
			if (geneBioTypesCount != biotypes.size()){
				int count = 0;
				List<String> genesFromBiotypes = getGenesFromBioTypes(biotypes);
				List<String[]> geneList0 = new ArrayList<String[]>();
				for(String[] item:geneList){
					if(genesFromBiotypes.contains(item[1])){
						geneList0.add(item);
						count++;
					}
					if(count ==  topGeneCount)
						break;
				}
				geneList = geneList0;
			}
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException n) {
			n.printStackTrace();
		}finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		


		List<String[]> subGeneList = geneList.subList(0, topGeneCount);
		
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> ids = new LinkedList<String>();
		LinkedList<String> genes = new LinkedList<String>();
		boolean geneFound = false;
		
//		if (geneTypes == null || geneTypes.isEmpty() || geneTypes.size() == geneTypesCount){
			
			for (String[] line:subGeneList){
				int len = line.length;
				ids.add(line[0]);
				genes.add(line[1]);
				if(selectedGene != null && selectedGene.contentEquals(line[1]))
					geneFound = true;
	
				LinkedList<String> d = new LinkedList<String>();
				for (int i = 2; i < len-1; i++)
					d.add(line[i]);
				data.add(d);
			}

		
		// if selected gene not in list of topGeneCount, find gene entries in file and add to list
		// this will allow sort by gene to be reinstated
		while (selectedGene != null && !geneFound){
			for (int i = topGeneCount; i < geneList.size(); i++){
				String[] line = geneList.get(i);
				if(selectedGene.contentEquals(line[1])){
					LinkedList<String> d = new LinkedList<String>();
					for (int j = 2; j < line.length-1; j++)
						d.add(line[j]);

					data.add(d);
					geneFound = true;
				}
				if (geneFound) 
					break;
			}
		}			
		
		
		obj.put("ids", ids);
		obj.put("genes", genes);
		obj.put("data", data);
		
		
		FileWriter writer;
		try {
			
			path = ctx.getRealPath("/");
			path += "/resources/genestrips/rnaseq_top.json";
		
			
			writer = new FileWriter(path);

			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> getGenesFromBioTypes(ArrayList<String> biotypes){
		
		
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = ctx.getRealPath("/");
		path += "/resources/genestrips/biotypes.txt";
		
		BufferedReader br = null;
		List<String> geneList = new ArrayList<String>();

		try {
			br = new BufferedReader(new FileReader(path));
			br.readLine();
			
			String line;
			while ((line = br.readLine()) != null){
				String[] data = line.split("\t");
				if (biotypes.contains(data[2]))
					geneList.add(data[1]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return geneList;
	}
	
    
    @SuppressWarnings("unchecked")
	public void createSeqFile(String selectedSeries){
    	
    	selectedSeries = "GSE59129";
    	
		JSONObject obj = new JSONObject();
		BufferedReader br = null;
		LinkedList<String> samples = new LinkedList<String>();
		LinkedList<String> max = new LinkedList<String>();
		Map<String,LinkedList<String>> geneList = new HashMap<String,LinkedList<String>>();
		LinkedList<String> llist;
		
    	String dirPath0 = "http://www.gudmap.org/Gudmap/ngsData/" + selectedSeries + "_processed";
    	String dirPath1 = seqDir + "_processed";
    	File dir0 = new File(dirPath0);
    	File[] dList0 = dir0.listFiles();
    	
    	String dirPath = "/export/data0/next_gen_archive/" + selectedSeries + "_processed"; 
    	File dir = new File(dirPath);
    	if (!dir.exists() || !dir.isDirectory()){
    		return;
    	}
    	
    	File[] dList = dir.listFiles();
//		int index = 0;
    	for(File d:dList){
    		String path = d.getPath() + "/processed/cufflink_results/genes.fpkm_tracking";
    		if (new File(path).isFile()){
	    		String name = "GUDMAP:" + d.getName();
	    		
	    		if (!name.contains("DS_Store")){
	    			String line;
	    		
		    		samples.add(name);
	      		
					try {
						br = new BufferedReader(new FileReader(path));										
						br.readLine();
						float maxval = 0;
						while ((line = br.readLine()) != null){
							String[] data = line.split("\t");
							String gene = data[4];
							if (geneList.containsKey(gene)){
								llist = geneList.get(gene);
							} 
							else {
								llist = new LinkedList<String>();
								llist.add(data[0]);
								llist.add(data[4]);
							}							
							llist.add(data[9]);
							geneList.put(gene, llist);
							
							// find the max vale for the sample
							String sval = data[9];
							float val = Float.parseFloat(sval);
							if (val > maxval) maxval = val;
																				
						}						
						max.add(Float.toString(maxval));
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	   		
	    	}
    	}

		//put the key values in a String[] for sorting
		List<String[]> gList = new ArrayList<String[]>();
		Set<String> keys = geneList.keySet();
		for(String key:keys){
			String[] arr = geneList.get(key).toArray(new String[geneList.get(key).size()]);
			gList.add(arr);				
		}		

		// sort the gList
		int index = samples.indexOf(selectedSample);
		if (index != -1)
			selectedSampleCol = index + 2;
		
		Collections.sort(gList, new Comparator<String[]> () {
		    @Override
		    public int compare(String[] a, String[] b) {
		    	if(a[selectedSampleCol].isEmpty() || a[selectedSampleCol] == null) a[selectedSampleCol] = "0";
		    	if(b[selectedSampleCol].isEmpty() || b[selectedSampleCol] == null) b[selectedSampleCol] = "0";
		    	Float f1 = Float.parseFloat(a[selectedSampleCol]);
		    	Float f2 = Float.parseFloat(b[selectedSampleCol]);
		        return f2.compareTo(f1);
		    }
		});

		
		// filter genelist by selected biotypes
		if (geneBioTypesCount != biotypes.size()){
			int count = 0;
			List<String> genesFromBiotypes = getGenesFromBioTypes(biotypes);
			List<String[]> gList0 = new ArrayList<String[]>();
			for(String[] item:gList){
				if(genesFromBiotypes.contains(item[1])){
					gList0.add(item);
					count++;
				}
				if(count ==  topGeneCount)
					break;
			}
			gList = gList0;
		}
		
		List<String[]> subGeneList = gList.subList(0, topGeneCount);
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> ids = new LinkedList<String>();
		LinkedList<String> genes = new LinkedList<String>();
		boolean geneFound = false;
		
//		if (geneTypes == null || geneTypes.isEmpty() || geneTypes.size() == geneTypesCount){
			
			for (String[] line:subGeneList){
				int len = line.length;
				ids.add(line[0]);
				genes.add(line[1]);
				if(selectedGene != null && selectedGene.contentEquals(line[1]))
					geneFound = true;
	
				LinkedList<String> d = new LinkedList<String>();
				for (int i = 2; i < len; i++)
					d.add(line[i]);
				data.add(d);
			}

			// if selected gene not in list of topGeneCount, find gene entries in file and add to list
			// this will allow sort by gene to be reinstated
			while (selectedGene != null && !geneFound){
				for (int i = topGeneCount; i < geneList.size(); i++){
					String[] line = gList.get(i);
					if(selectedGene.contentEquals(line[1])){
						LinkedList<String> d = new LinkedList<String>();
						for (int j = 2; j < line.length-1; j++)
							d.add(line[j]);

						data.add(d);
						geneFound = true;
					}
					if (geneFound) 
						break;
				}
			}			
			
    	obj.put("maxvalues", max);
		obj.put("samples", samples);
		obj.put("ids", ids);
		obj.put("genes", genes);
		obj.put("data", data);

		FileWriter writer;
		try {
			FacesContext fctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fctx.getExternalContext().getSession(false);
			String sessionId = session.getId();
			
			seqFile = selectedSeries + "_" + sessionId + ".json";
			
			ServletContext ctx = (ServletContext) fctx.getExternalContext().getContext();
			String path = ctx.getRealPath("/");
//			path += "/resources/genestrips/Gudmap:" + selectedSeries + "_" + sessionId + ".json";
			path += "/resources/genestrips/" + seqFile;
					
			writer = new FileWriter(path);
			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

	public void createSeqFile2(String selectedSeries){
    	
    	selectedSeries = "GSE59129";
    	
		JSONObject obj = new JSONObject();
		
		JSONObject x = new JSONObject();
		JSONObject y = new JSONObject();
		JSONObject z = new JSONObject();
		JSONObject m = new JSONObject();
		
		BufferedReader br = null;
		LinkedList<String> samples = new LinkedList<String>();
		LinkedList<String> max = new LinkedList<String>();
		Map<String,LinkedList<String>> geneList = new HashMap<String,LinkedList<String>>();
		LinkedList<String> llist;
		LinkedList<String> desc = new LinkedList<String>();
		
    	String dirPath0 = "http://www.gudmap.org/Gudmap/ngsData/" + selectedSeries + "_processed";
    	String dirPath1 = seqDir + "_processed";
    	File dir0 = new File(dirPath0);
    	File[] dList0 = dir0.listFiles();
    	
    	String dirPath = "/export/data0/next_gen_archive/" + selectedSeries + "_processed"; 
    	File dir = new File(dirPath);
    	if (!dir.exists() || !dir.isDirectory()){
    		return;
    	}
    	
    	File[] dList = dir.listFiles();
//		int index = 0;
    	for(File d:dList){
    		String path = d.getPath() + "/processed/cufflink_results/genes.fpkm_tracking";
    		if (new File(path).isFile()){
	    		String name = "GUDMAP:" + d.getName();
	    		
	    		if (!name.contains("DS_Store")){
	    			String line;
	    		
		    		samples.add(name);
	      		
					try {
						br = new BufferedReader(new FileReader(path));										
						br.readLine();
						float maxval = 0;
						while ((line = br.readLine()) != null){
							String[] data = line.split("\t");
							String gene = data[4];
							if (geneList.containsKey(gene)){
								llist = geneList.get(gene);
							} 
							else {
								llist = new LinkedList<String>();
								llist.add(data[0]);
								llist.add(data[4]);
							}							
							llist.add(data[9]);
							geneList.put(gene, llist);
							
							// find the max vale for the sample
							String sval = data[9];
							float val = Float.parseFloat(sval);
							if (val > maxval) maxval = val;
																				
						}						
						max.add(Float.toString(maxval));
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	   		
	    	}
    	}

		//put the key values in a String[] for sorting
		List<String[]> gList = new ArrayList<String[]>();
		Set<String> keys = geneList.keySet();
		for(String key:keys){
			String[] arr = geneList.get(key).toArray(new String[geneList.get(key).size()]);
			gList.add(arr);				
		}		

		// sort the gList
		int index = samples.indexOf(selectedSample);
		if (index != -1)
			selectedSampleCol = index + 2;
		
		Collections.sort(gList, new Comparator<String[]> () {
		    @Override
		    public int compare(String[] a, String[] b) {
		    	if(a[selectedSampleCol].isEmpty() || a[selectedSampleCol] == null) a[selectedSampleCol] = "0";
		    	if(b[selectedSampleCol].isEmpty() || b[selectedSampleCol] == null) b[selectedSampleCol] = "0";
		    	Float f1 = Float.parseFloat(a[selectedSampleCol]);
		    	Float f2 = Float.parseFloat(b[selectedSampleCol]);
		        return f2.compareTo(f1);
		    }
		});

		
		// filter genelist by selected biotypes
		if (geneBioTypesCount != biotypes.size()){
			int count = 0;
			List<String> genesFromBiotypes = getGenesFromBioTypes(biotypes);
			List<String[]> gList0 = new ArrayList<String[]>();
			for(String[] item:gList){
				if(genesFromBiotypes.contains(item[1])){
					gList0.add(item);
					count++;
				}
				if(count ==  topGeneCount)
					break;
			}
			gList = gList0;
		}
		
		List<String[]> subGeneList = gList.subList(0, topGeneCount);
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> ids = new LinkedList<String>();
		LinkedList<String> genes = new LinkedList<String>();
		boolean geneFound = false;
		
//		if (geneTypes == null || geneTypes.isEmpty() || geneTypes.size() == geneTypesCount){
			
			for (String[] line:subGeneList){
				int len = line.length;
				ids.add(line[0]);
				genes.add(line[1]);
				if(selectedGene != null && selectedGene.contentEquals(line[1]))
					geneFound = true;
	
				LinkedList<String> d = new LinkedList<String>();
				for (int i = 2; i < len; i++)
					d.add(line[i]);
				data.add(d);
			}

			// if selected gene not in list of topGeneCount, find gene entries in file and add to list
			// this will allow sort by gene to be reinstated
			while (selectedGene != null && !geneFound){
				for (int i = topGeneCount; i < geneList.size(); i++){
					String[] line = gList.get(i);
					if(selectedGene.contentEquals(line[1])){
						LinkedList<String> d = new LinkedList<String>();
						for (int j = 2; j < line.length-1; j++)
							d.add(line[j]);

						data.add(d);
						geneFound = true;
					}
					if (geneFound) 
						break;
				}
			}			


    	desc.add(selectedSeries);
    	
		y.put("vars", samples);			
    	y.put("desc", desc);
		y.put("smps", genes);
//		obj.put("ids", ids);
		obj.put("data", data);
		
		
    	obj.put("x", x);
		obj.put("y", y);
		obj.put("z", z);
		obj.put("m", m);
		

		FileWriter writer;
		try {
			FacesContext fctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) fctx.getExternalContext().getSession(false);
			String sessionId = session.getId();
			
			seqFile = selectedSeries + "_" + sessionId + "_2.json";
			
			ServletContext ctx = (ServletContext) fctx.getExternalContext().getContext();
			String path = ctx.getRealPath("/");
//			path += "/resources/genestrips/Gudmap:" + selectedSeries + "_" + sessionId + ".json";
			path += "/resources/genestrips/" + seqFile;
					
			writer = new FileWriter(path);
			writer.write(obj.toJSONString());
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
}