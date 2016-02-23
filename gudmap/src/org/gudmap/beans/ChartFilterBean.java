package org.gudmap.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.ChartModel;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.ChartQueries;
import org.gudmap.queries.generic.GenericQueries;

@Named
@SessionScoped
public class ChartFilterBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<ChartModel> chartModelList = null;
	private ArrayList<ChartModel> drillByAssayTypeList = null;
	private ArrayList<ChartModel> chartModelBarList = null;
	private Map<String,Integer>drillByAssayTypeMap = null;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	DecimalFormat df = new DecimalFormat(".##");
	int total_entries = 0;
	int total_genes = 0;
	int total_wish = 0;
	int total_sish = 0;
	int total_opt = 0;
	int total_ihc = 0;
	int total_tg = 0;
	int total_microarray = 0;
	int total_sequence = 0;
	private String json_return="{\"name\":\"Lab\",\"data\":[[\"Little\",\"1197\"],[\"McMahon\",\"183\"],[\"Gaido\",\"187\"],[\"Lessard\",\"0\"]]}";
	
	public ChartFilterBean() {
		init();
		drillByAssayTypeMap = new LinkedHashMap<String,Integer>();
		//drillChart();
		//barChart();
	}
	
	public ArrayList<ChartModel> getChartModelList() {
		
		return chartModelList;
	}
	
	public ArrayList<ChartModel> getDrillByAssayTypeList() {
		
		return drillByAssayTypeList;
	}
	
	public ArrayList<ChartModel> getChartModelBarList() {
		
		return chartModelBarList;
	}
	
	public void init() {
		String queryString = ChartQueries.ENTRIES_AND_GENES;
		ChartModel chartModel = null;
		
        try
		{
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				chartModelList = new ArrayList<ChartModel>();
				chartModel = new ChartModel();
				total_entries = result.getInt("total_entries");
				chartModel.setTotal_entries(total_entries);
				total_genes = result.getInt("total_genes");
                chartModel.setTotal_genes(total_genes);
                total_ihc = result.getInt("ihc");
                chartModel.setIhc_percent(calculatePercentage(result.getInt("ihc"),total_entries));
                chartModel.setIhc_gene_percent(calculatePercentage(result.getInt("total_ihc_genes"),total_genes));
                total_tg = result.getInt("tg");
                chartModel.setTg_percent(calculatePercentage(result.getInt("tg"),total_entries));
                chartModel.setTg_gene_percent(calculatePercentage(result.getInt("total_tg_genes"),total_genes));
                total_wish = result.getInt("wish");
                chartModel.setWish_percent(calculatePercentage(result.getInt("wish"),total_entries));
                chartModel.setWish_gene_percent(calculatePercentage(result.getInt("total_wish_genes"),total_genes));
                total_sish = result.getInt("sish");
                chartModel.setSish_percent(calculatePercentage(result.getInt("sish"),total_entries));
                chartModel.setSish_gene_percent(calculatePercentage(result.getInt("total_sish_genes"),total_genes));
                total_opt = result.getInt("opt");
                chartModel.setOpt_percent(calculatePercentage(result.getInt("opt"),total_entries));
                chartModel.setOpt_gene_percent(calculatePercentage(result.getInt("total_opt_genes"),total_genes));
                total_microarray = result.getInt("microarray");
                chartModel.setMicroarray_percent(calculatePercentage(result.getInt("microarray"),total_entries));
                total_sequence = result.getInt("sequence");
                chartModel.setSequence_percent(calculatePercentage(result.getInt("sequence"),total_entries));
                
                chartModelList.add(chartModel);
               
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
	}
	
	public void drillLabByAssayTypeChart(int assayType) {
		//int assayType = Integer.parseInt(input);
		drillByAssayTypeMap.clear();
		String[] assayArray = {"wholemount","section","opt-wholemount","IHC","TG","NextGen","Microarray"};
		String lab="";
		String abbrevLab="";
		ArrayList<String>sourceList = new ArrayList<String>();
		ArrayList<String>abbrevList = new ArrayList<String>();
		ChartModel chartModel = null;
		 try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(GenericQueries.ALL_PUBLIC_SOURCES); 
				result =  ps.executeQuery();
				while (result.next()) {
					sourceList.add(result.getString("lab"));
					abbrevList.add(result.getString("abbrv"));
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		
		 //drillByAssayTypeMap = new LinkedHashMap<String,Integer>();
		 //drillByAssayTypeList = new ArrayList<ChartModel>();
		 
		String queryString = (assayType<3)?ChartQueries.LABS_BY_ISH:ChartQueries.LABS_BY_NON_ISH; 
		for(int i=0;i<sourceList.size();i++){
			lab = sourceList.get(i);
			abbrevLab = abbrevList.get(i);
			//chartModel = new ChartModel();
	        try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, lab);
				ps.setString(2, assayArray[assayType]);
				result =  ps.executeQuery();
				if (result.first()) {
					drillByAssayTypeMap.put(abbrevLab,result.getInt("TOTAL"));
					
				}				      
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		
	}
	
	
	

	
	private double calculatePercentage(int fraction, int total) {
		double y= Math.round(100 * ((fraction * 100f)/total)) / 100d;
		return y;
	}
	
	public Map<String,Integer> getDrillByAssayTypeMap() {
		
		return drillByAssayTypeMap;
	}
	
	public void setJson_return(String json_return) {
		this.json_return = json_return;
	}
	
	public String getJson_return () {
		return json_return;
	}

}
