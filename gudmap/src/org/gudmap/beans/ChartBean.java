package org.gudmap.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.ChartModel;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.ChartQueries;
import org.gudmap.queries.generic.GenericQueries;

@Named
//@RequestScoped
@ApplicationScoped
public class ChartBean {
	
	private ArrayList<ChartModel> chartModelList = null;
	private ArrayList<ChartModel> chartModelDrillList = null;
	private ArrayList<ChartModel> chartModelListAge = null;
	private ArrayList<ChartModel> chartModelDrillListAge = null;
	private ArrayList<ChartModel> chartModelBarList = null;
	private ArrayList<ChartModel> chartModelDrillListTissue = null;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	DecimalFormat df = new DecimalFormat(".##");
	int total_entries = 0;
	int total_focus_entries = 0;
	int total_genes = 0;
	int total_wish = 0;
	int total_sish = 0;
	int total_opt = 0;
	int total_ihc = 0;
	int total_tg = 0;
	int total_microarray = 0;
	int total_sequence = 0;
	
	public ChartBean() {
		init();
		initAge();
		initTissue();
		drillChart();
		drillChartAge();
		drillChartTissue();
		//barChart();
	}
	
	public ArrayList<ChartModel> getChartModelList() {
		
		return chartModelList;
	}
	
	public ArrayList<ChartModel> getChartModelDrillList() {
		
		return chartModelDrillList;
	}
	
	public ArrayList<ChartModel> getChartModelListAge() {
		
		return chartModelListAge;
	}
	
	
	
	public ArrayList<ChartModel> getChartModelDrillListAge() {
		
		return chartModelDrillListAge;
	}
	
	public ArrayList<ChartModel> getChartModelBarList() {
		
		return chartModelBarList;
	}
	
	public ArrayList<ChartModel> getChartModelDrillListTissue() {
		
		return chartModelDrillListTissue;
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
	
	public void drillChart() {
		ChartModel chartModel = null;
		String lab="";
		ArrayList<String>sourceList = new ArrayList<String>();
		 try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(GenericQueries.ALL_PUBLIC_SOURCES); 
				result =  ps.executeQuery();
				while (result.next()) {
					sourceList.add(result.getString("lab"));
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		
		chartModelDrillList = new ArrayList<ChartModel>();
		
		for(int i=0;i<sourceList.size();i++){
			lab = sourceList.get(i);
	        try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(ChartQueries.ENTRIES_BY_LAB); 
				ps.setString(1, lab);
				ps.setString(2, lab);
				ps.setString(3, lab);
				ps.setString(4, lab);
				ps.setString(5, lab);
				ps.setString(6, lab);
				ps.setString(7, lab);
				result =  ps.executeQuery();
				if (result.first()) {
					chartModel = new ChartModel();
					chartModel.setWish_lab(calculatePercentage(result.getInt("wish_lab"),total_wish));
					chartModel.setSish_lab(calculatePercentage(result.getInt("sish_lab"),total_sish));
					chartModel.setOpt_lab(calculatePercentage(result.getInt("opt_lab"),total_opt));
					chartModel.setIhc_lab(calculatePercentage(result.getInt("ihc_lab"),total_ihc));
					chartModel.setTg_lab(calculatePercentage(result.getInt("tg_lab"),total_tg));
					chartModel.setMicroarray_lab(calculatePercentage(result.getInt("microarray_lab"),total_microarray));
					chartModel.setSequence_lab(calculatePercentage(result.getInt("sequence_lab"),total_sequence));
	                
					chartModelDrillList.add(chartModel);       
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		
	}
	
	public void initAge() {
		String queryString = ChartQueries.ENTRIES_BY_AGE;
		ChartModel chartModel = null;
		
        try
		{
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				chartModelListAge = new ArrayList<ChartModel>();
				chartModel = new ChartModel();
                chartModel.setAge_1_percent(calculatePercentage(result.getInt("TOT_AGE_1"),total_entries));
                chartModel.setAge_2_percent(calculatePercentage(result.getInt("TOT_AGE_2"),total_entries));
                chartModel.setAge_3_percent(calculatePercentage(result.getInt("TOT_AGE_3"),total_entries));
                chartModel.setAge_4_percent(calculatePercentage(result.getInt("TOT_AGE_4"),total_entries));
                chartModel.setAge_5_percent(calculatePercentage(result.getInt("TOT_AGE_5"),total_entries));
                
                chartModelListAge.add(chartModel);
               
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
	}
	
	public void drillChartAge() {
		ChartModel chartModel = null;
		Integer ageArray[]={1,15,16,20,21,22,23,27,28,28};
		
		chartModelDrillListAge = new ArrayList<ChartModel>();
		
		for(int i=0;i<ageArray.length;i+=2){
	        try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(ChartQueries.ASSAY_TYPES_BY_AGE); 
				ps.setInt(1, ageArray[i]);
				ps.setInt(2, ageArray[i+1]);
				ps.setInt(3, ageArray[i]);
				ps.setInt(4, ageArray[i+1]);
				ps.setInt(5, ageArray[i]);
				ps.setInt(6, ageArray[i+1]);
				ps.setInt(7, ageArray[i]);
				ps.setInt(8, ageArray[i+1]);
				ps.setInt(9, ageArray[i]);
				ps.setInt(10, ageArray[i+1]);
				ps.setInt(11, ageArray[i]);
				ps.setInt(12, ageArray[i+1]);
				ps.setInt(13, ageArray[i]);
				ps.setInt(14, ageArray[i+1]);
				result =  ps.executeQuery();
				if (result.first()) {
					chartModel = new ChartModel();
					chartModel.setWish_age(calculatePercentage(result.getInt("wish_age"),total_entries));
					chartModel.setSish_age(calculatePercentage(result.getInt("sish_age"),total_entries));
					chartModel.setOpt_age(calculatePercentage(result.getInt("opt_age"),total_entries));
					chartModel.setIhc_age(calculatePercentage(result.getInt("ihc_age"),total_entries));
					chartModel.setTg_age(calculatePercentage(result.getInt("tg_age"),total_entries));
					chartModel.setMicroarray_age(calculatePercentage(result.getInt("microarray_age"),total_entries));
					chartModel.setSequence_age(calculatePercentage(result.getInt("sequence_age"),total_entries));
	                
					chartModelDrillListAge.add(chartModel);       
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		
	}
	
	public void initTissue() {
		ChartModel chartModel = null;
		//int total_focus_entries=0;
		try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(ChartQueries.ENTRIES_BY_FOCUS_GROUP); 
				result =  ps.executeQuery();
				if (result.first()) {
					chartModel = new ChartModel();
					chartModelBarList = new ArrayList<ChartModel>();
					total_focus_entries=result.getInt("TOT_MET")+result.getInt("TOT_LUT")+result.getInt("TOT_ERS")+result.getInt("TOT_FRS")+result.getInt("TOT_MRS");
					chartModel.setTot_met_entries(result.getInt("TOT_MET"));
					chartModel.setMet_percent(calculatePercentage(result.getInt("TOT_MET"),total_focus_entries));
					chartModel.setTot_lut_entries(result.getInt("TOT_LUT"));
					chartModel.setLut_percent(calculatePercentage(result.getInt("TOT_LUT"),total_focus_entries));
					chartModel.setTot_ers_entries(result.getInt("TOT_ERS"));
					chartModel.setErs_percent(calculatePercentage(result.getInt("TOT_ERS"),total_focus_entries));
					chartModel.setTot_frs_entries(result.getInt("TOT_FRS"));
					chartModel.setFrs_percent(calculatePercentage(result.getInt("TOT_FRS"),total_focus_entries));
					chartModel.setTot_mrs_entries(result.getInt("TOT_MRS"));
					chartModel.setMrs_percent(calculatePercentage(result.getInt("TOT_MRS"),total_focus_entries));
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		
		
		
			/*try
			{
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(ChartQueries.GENES_BY_FOCUS_GROUP); 
				result =  ps.executeQuery();
				if (result.first()) {
					chartModel.setTot_met_genes(result.getInt("TOT_MET_GENE"));
					chartModel.setTot_lut_genes(result.getInt("TOT_LUT_GENE"));
					chartModel.setTot_ers_genes(result.getInt("TOT_ERS_GENE"));
					chartModel.setTot_frs_genes(result.getInt("TOT_FRS_GENE"));
					chartModel.setTot_mrs_genes(result.getInt("TOT_MRS_GENE"));
	          	      
				}
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}*/
			
			chartModelBarList.add(chartModel); 
		
	}
	
	public void drillChartTissue() {
		ChartModel chartModel = null;
		String tissueArray[]={"LMN_EMAP","LKP_METANEPHROS","LUR_EMAP","LKP_URINARY","LER_EMAP","LKP_EARLY_REPRO","LFR_EMAP","LKP_FEMALE_REPRO","LMR_EMAP","LKP_MALE_REPRO"};
		String queryString = ChartQueries.ASSAY_TYPES_BY_TISSUE;
		String sql="";
		chartModelDrillListTissue= new ArrayList<ChartModel>();
		
		for(int i=0;i<tissueArray.length;i+=2){
	        try
			{	sql = String.format(queryString,tissueArray[i],tissueArray[i+1],tissueArray[i],tissueArray[i+1],tissueArray[i],tissueArray[i+1],tissueArray[i],tissueArray[i+1],
			 	      tissueArray[i],tissueArray[i+1],tissueArray[i],tissueArray[i+1],tissueArray[i],tissueArray[i+1]);
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(sql); 
				result =  ps.executeQuery();
				if (result.first()) {
					chartModel = new ChartModel();
					chartModel.setWish_tissue(calculatePercentage(result.getInt("wish_tissue"),total_focus_entries));
					chartModel.setTot_wish_tissue_entries(result.getInt("wish_tissue"));
					chartModel.setSish_tissue(calculatePercentage(result.getInt("sish_tissue"),total_focus_entries));
					chartModel.setTot_sish_tissue_entries(result.getInt("sish_tissue"));
					chartModel.setOpt_tissue(calculatePercentage(result.getInt("opt_tissue"),total_focus_entries));
					chartModel.setTot_opt_tissue_entries(result.getInt("opt_tissue"));
					chartModel.setIhc_tissue(calculatePercentage(result.getInt("ihc_tissue"),total_focus_entries));
					chartModel.setTot_ihc_tissue_entries(result.getInt("ihc_tissue"));
					chartModel.setTg_tissue(calculatePercentage(result.getInt("tg_tissue"),total_focus_entries));
					chartModel.setTot_tg_tissue_entries(result.getInt("tg_tissue"));
					chartModel.setMicroarray_tissue(calculatePercentage(result.getInt("microarray_tissue"),total_focus_entries));
					chartModel.setTot_microarray_tissue_entries(result.getInt("microarray_tissue"));
					chartModel.setSequence_tissue(calculatePercentage(result.getInt("sequence_tissue"),total_focus_entries));
					chartModel.setTot_sequence_tissue_entries(result.getInt("sequence_tissue"));
	                
					chartModelDrillListTissue.add(chartModel);       
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

}
