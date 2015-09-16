package org.gudmap.assemblers;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gudmap.globals.Globals;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.ArraySeqTableBeanModel;

public class MicSeriesTablePageBeanAssembler {
	
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArraySeqTableBeanModel arraySeqmodel;
	private String paramSQL;
	private String assayType;
	private String whereclause;
	private String focusGroupWhereclause;
	private String platformID;
	
	public MicSeriesTablePageBeanAssembler(String paramSQL,String assayType) {
		
		this.paramSQL=paramSQL;
		if(Globals.getParameterValue("platform")!=null)
			platformID=Globals.getParameterValue("platform");
	}
	
	public List<ArraySeqTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause, 
											String focusGroupWhereclause){
		if(platformID!=null)
			whereclause+="PLT_GEO_ID = '"+platformID+"' AND ";
		this.whereclause=whereclause;
		this.focusGroupWhereclause=focusGroupWhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, whereclause, focusGroupWhereclause, sortField, sortDirection);
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, assayType);
			ps.setInt(2, firstRow);
			ps.setInt(3, rowCount);
			result =  ps.executeQuery();
			
			while(result.next()){
				arraySeqmodel=new ArraySeqTableBeanModel();
				arraySeqmodel.setTitle(result.getString("series_title"));
				arraySeqmodel.setGeoSeriesID(result.getString("geo_series_id"));
				arraySeqmodel.setNumSamples(result.getInt("num_samples"));
				arraySeqmodel.setSource(result.getString("source"));
				arraySeqmodel.setPlatformID(result.getString("platform"));
				arraySeqmodel.setSeriesOid(result.getInt("series_oid"));
				arraySeqmodel.setSeriesComponents(result.getString("components"));
				
				arraySeqmodel.setSelected(false);
				list.add(arraySeqmodel);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return list;
	}
	
	public int count() {
		int count=0;
		/*String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");*/
		String totalwhere=whereclause;
		String sql = String.format(ArrayQueries.COUNT_TOTAL_MIC_SERIES,totalwhere,focusGroupWhereclause);
		try
		{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, assayType);
				result =  ps.executeQuery();
				
				while(result.next()){
					count=result.getInt(1);
				}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
			    Globals.closeQuietly(con, ps, result);
		}		
		return count;
	}
	
	public Map<String,String>  getTotals() {
		Map<String,String> totals = new HashMap<String,String>();
		
		String [] queries=(assayType.equals("Microarray")?Globals.MicSeriesColTotals:Globals.SeqSeriesColTotals);
		
		String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");
		
		String sql="";
		for(int i=0;i<queries.length;i++) {
			try
			{
				con = Globals.getDatasource().getConnection();
				sql=String.format(QueryTotals.ReturnQuery(queries[i]),totalwhere,focusGroupWhereclause);

				ps = con.prepareStatement(sql);
				ps.setString(1, assayType);
				result =  ps.executeQuery();
				
				while(result.next()){
					totals.put(queries[i],result.getString("TOTAL"));
				}
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		return totals;
	}
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
}
