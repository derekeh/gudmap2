package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.gudmap.globals.Globals;
import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.array.ArrayQueries;

public class ArraySeqSeriesBeanAssembler {
	
	private ArraySeqTableBeanModel arraySeqTableBeanModel=null;
	private ArrayList<ArraySeqTableBeanModel> datalist=null;
	private String seriesID="";
	private boolean isArray;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public ArraySeqSeriesBeanAssembler(String seriesID, boolean isArray) {
		this.seriesID=seriesID;
		this.isArray=isArray;
	}
	
	public ArrayList<ArraySeqTableBeanModel> getData() {
		
		//Microarray Series
		if(isArray){
			try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(ArrayQueries.ARRAY_SEQ_SERIES);
				ps.setString(1, seriesID);
				result =  ps.executeQuery();
				
				//group_concat returning no value will return a null row so don't get those!
				while(result.next()){
					arraySeqTableBeanModel=new ArraySeqTableBeanModel();
					datalist=new ArrayList<ArraySeqTableBeanModel>();
					arraySeqTableBeanModel.setGeoSeriesID(result.getString("geo_series_id"));
					arraySeqTableBeanModel.setNumSamples(result.getInt("num_samples"));
					arraySeqTableBeanModel.setTitle(result.getString("title"));
					arraySeqTableBeanModel.setSummary(result.getString("summary"));
					arraySeqTableBeanModel.setType(result.getString("type"));
					arraySeqTableBeanModel.setOverallDesign(result.getString("overall_design"));
					arraySeqTableBeanModel.setSeriesOid(result.getInt("series_oid"));
					arraySeqTableBeanModel.setSeriesComponents(result.getString("components"));
					arraySeqTableBeanModel.setBatchID(result.getInt("batch_id"));
					arraySeqTableBeanModel.setArchiveID(result.getInt("archive_id"));
						
					
					datalist.add(arraySeqTableBeanModel);
				}
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		//Sequence Series
		else {
			
		}
		
		return datalist;
		
	}

}
