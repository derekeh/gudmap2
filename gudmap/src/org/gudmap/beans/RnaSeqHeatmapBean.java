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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

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
	private String selectedGene;
	private ArrayList<String> geneTypes;
	private int geneTypesCount;

	
	private String tableTitle;
	

 
    private List<String> list; 
    
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
   
    
    @Inject
   	protected SessionBean sessionBean;    
    
    public RnaSeqHeatmapBean() {
    	
       	super(1000,10,null,true); 
       	
       	
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
        System.out.println("Values set: " + list);
    }
   
	/**
	 * This method returns the current gene type from the GeneType control 
	 * of the browseSeqHeatmap.xhtml page.
	 * 
	 * @return The Gene Types
	 */
    public ArrayList<String> getGeneTypes() {
    	return geneTypes;
    }
    
	/**
	 * This method sets the selected gene types to be displayed in the Gene Type control 
	 * of the browseSeqHeatmap.xhtml page.
	 * 
	 * @param geneTypes
	 */
    public void setGeneTypes(ArrayList<String> geneTypes) {
    	this.geneTypes = geneTypes;
    }
 
    
	/**
	 * This method is called when the selection of gene types in the Gene Type control 
	 * of the browseSeqHeatmap.xhtml page changes.
	 * 
	 */
    public void updateGeneTypes() {
    	getGeneTypes();
    }
    
	/**
	 * This method returns a map of gene types to be displayed in the Gene Type control 
	 * of the browseSeqHeatmap.xhtml page.
	 * This filters the number of genes to be displayed in the heatmap.
	 * 
	 * @return A Map of counts
	 */    
    public Map<String,String> getGeneTypeOptions() {
    	HashMap<String,String> options = new LinkedHashMap<String,String>();
		options.put("3prime_overlapping_ncRNA" , "3prime_overlapping_ncRNA");        
		options.put("antisense" , "antisense");        
 		options.put("bidirectional_promoter_lncRNA" , "bidirectional_promoter_lncRNA");        
 		options.put("IG_pseudogene" , "IG_pseudogene");        
 		options.put("IG_C_gene" , "IG_C_gene");        
 		options.put("IG_C_pseudogene" , "IG_C_pseudogene");        
 		options.put("IG_D_gene" , "IG_D_gene");        
 		options.put("IG_D_pseudogene" , "IG_D_pseudogene");        
 		options.put("IG_J_gene" , "IG_J_gene");        
  		options.put("IG_LV_gene" , "IG_LV_gene");        
 		options.put("IG_V_gene" , "IG_V_gene");        
 		options.put("IG_V_pseudogene" , "IG_V_pseudogene");        
		options.put("lincRNA" , "lincRNA");        
 		options.put("macro_lncRNA" , "macro_lncRNA");
 		options.put("miRNA" , "miRNA");
 		options.put("misc_RNA" , "misc_RNA");
 		options.put("Mt_tRNA" , "Mt_tRNA");
		options.put("polymorphic_pseudogene" , "polymorphic_pseudogene");        
		options.put("processed_transcript" , "processed_transcript");        
		options.put("protein_coding" , "protein_coding");
		options.put("processed_pseudogene" , "processed_pseudogene");        
		options.put("pseudogene" , "pseudogene");        
 		options.put("ribozyme" , "ribozyme");        
		options.put("rRNA" , "rRNA");        
		options.put("sRNA" , "sRNA");        
		options.put("scaRNA" , "scaRNA");        
		options.put("snRNA" , "snRNA");        
		options.put("snoRNA" , "snoRNA");        
		options.put("sense_intronic" , "sense_intronic");        
		options.put("sense_overlapping" , "sense_overlapping");        
		options.put("TEC" , "TEC");        
 		options.put("TR_C_gene" , "TR_C_gene");        
 		options.put("TR_D_gene" , "TR_D_gene");        
 		options.put("TR_J_gene" , "TR_J_gene");        
 		options.put("TR_J_pseudogene" , "TR_J_pseudogene");        
 		options.put("TR_V_gene" , "TR_V_gene");        
 		options.put("TR_V_pseudogene" , "TR_V_pseudogene");        
		options.put("transcribed_processed_pseudogene" , "transcribed_processed_pseudogene");        
		options.put("transcribed_unprocessed_pseudogene" , "transcribed_unprocessed_pseudogene");        
		options.put("transcribed_unitary_pseudogene" , "transcribed_unitary_pseudogene");        
 		options.put("unitary_pseudogene" , "unitary_pseudogene");        
		options.put("unprocessed_pseudogene" , "unprocessed_pseudogene"); 
		
		geneTypesCount = options.size();
		return options;
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
    	
    	init(selectedSample);
    }
	
	public String getTitle() {
		
		return tableTitle;
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
		
		if (geneTypes == null || geneTypes.isEmpty() || geneTypes.size() == geneTypesCount){
			
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
			
		}
		else {
			
			for (String[] line:subGeneList){
				// using genebiotype filtering
				if (geneBiotypeMatch(selectedGene)){
					
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
			}
			
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
	
	/**
	 * This method checks if the gene has a matching bio type in the geneType list.
	 * This is used to filter the sequence heatmap according to the gene type
	 * 
	 * @param gene
	 * @param geneTypes	 
	 * @return boolean True if matching bio type found
	 */
	private boolean geneBiotypeMatch(String gene){
		
		boolean found = false;
        String queryString = SequenceQueries.SEQUENCE_GENE_BIOTYPE;

		try {
			con= Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, gene);
			ps.setString(2, gene);
			ps.setString(3, geneTypes.toString());
			result =  ps.executeQuery();
			if (result.isBeforeFirst()){
				found = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return found;
	}
}