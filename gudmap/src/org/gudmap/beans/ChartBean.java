package org.gudmap.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.ChartModel;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.ChartQueries;

@Named
@RequestScoped
public class ChartBean {
	
	private ArrayList<ChartModel> chartModelList = null;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	DecimalFormat df = new DecimalFormat(".##");
	
	public ChartBean() {
		init();
	}
	
	public ArrayList<ChartModel> getChartModelList() {
		
		return chartModelList;
	}
	
	public void init() {
		String queryString = ChartQueries.ENTRIES_AND_GENES;
		ChartModel chartModel = null;
		int total_entries = 0;
		int total_genes = 0;
        try
		{
			//con = ds.getConnection();
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
                int ihc = result.getInt("ihc");
                chartModel.setIhc_percent(calculatePercentage(result.getInt("ihc"),total_entries));
                chartModel.setIhc_gene_percent(calculatePercentage(result.getInt("total_ihc_genes"),total_genes));
                chartModel.setTg_percent(calculatePercentage(result.getInt("tg"),total_entries));
                chartModel.setTg_gene_percent(calculatePercentage(result.getInt("total_tg_genes"),total_genes));
                chartModel.setWish_percent(calculatePercentage(result.getInt("wish"),total_entries));
                chartModel.setWish_gene_percent(calculatePercentage(result.getInt("total_wish_genes"),total_genes));
                chartModel.setSish_percent(calculatePercentage(result.getInt("sish"),total_entries));
                chartModel.setSish_gene_percent(calculatePercentage(result.getInt("total_sish_genes"),total_genes));
                chartModel.setOpt_percent(calculatePercentage(result.getInt("opt"),total_entries));
                chartModel.setOpt_gene_percent(calculatePercentage(result.getInt("total_opt_genes"),total_genes));
                chartModel.setMicroarray_percent(calculatePercentage(result.getInt("microarray"),total_entries));
                chartModel.setSequence_percent(calculatePercentage(result.getInt("sequence"),total_entries));
                
                chartModelList.add(chartModel);
               
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
	}
	
	private double calculatePercentage(int fraction, int total) {
		double y= Math.round(100 * ((fraction * 100f)/total)) / 100d;
		return y;
	}

}
