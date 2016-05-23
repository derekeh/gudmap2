package org.gudmap.beans;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.gudmap.impl.PagerImpl;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;



@Named (value="rnaSeqHeatmapBean")
@SessionScoped
//@RequestScoped
public class RnaSeqHeatmapBean extends PagerImpl  implements Serializable{

	private static final long serialVersionUID = 1L;	
 
	private int topGeneCount = 50;
	private int selectedSampleCol = 2;
	private String cellSize = "15";
	private String selectedSample = "AdultProximal_Tubules-1";
	
	private String tableTitle;
	

    
    @Inject
   	protected SessionBean sessionBean;    
    
    public RnaSeqHeatmapBean() {
    	
       	super(1000,10,null,true);  
    }

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
    
    public int getTopGeneCount() {
    	return topGeneCount;
    }
    
    public void setTopGeneCount(int count) {
    	topGeneCount = count;
    }
    
	public void topGeneCountChanged(ValueChangeEvent e){
		topGeneCount = (int)e.getNewValue();
		loadDataList();
    	
	}

    public String getCellSize() {
    	return cellSize;
    }
    
    public void setCellSize(String cellSize) {
    	this.cellSize = cellSize;
    }
	
    public String getSelectedSample() {
    	return selectedSample;
    }
    
    public void setSelectedSample(String sample) {
    	selectedSample = sample;
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


		geneList = geneList.subList(0, topGeneCount);
		
		LinkedList<LinkedList<String>> data = new LinkedList<LinkedList<String>>();
		LinkedList<String> ids = new LinkedList<String>();
		LinkedList<String> genes = new LinkedList<String>();
		for (String[] line:geneList){
			int len = line.length;
			ids.add(line[0]);
			genes.add(line[1]);
			LinkedList<String> d = new LinkedList<String>();
			for (int i = 2; i < len-1; i++)
				d.add(line[i]);
			data.add(d);
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
	

}