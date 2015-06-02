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
import org.gudmap.queries.array.SequenceQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.ArraySeqTableBeanModel;

public class SeqSeriesTablePageBeanAssembler {
	
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArraySeqTableBeanModel arraySeqmodel;
	private String paramSQL;
	private String assayType;
	private String whereclause;
	
	public SeqSeriesTablePageBeanAssembler(String paramSQL,String assayType) {
		/*try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}*/
		this.paramSQL=paramSQL;
		//this.assayType=assayType;
		
	}
	
	public List<ArraySeqTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause){
		this.whereclause=whereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, whereclause, sortField, sortDirection);
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, firstRow);
			ps.setInt(2, rowCount);
			result =  ps.executeQuery();
			
			while(result.next()){
				arraySeqmodel=new ArraySeqTableBeanModel();
				arraySeqmodel.setTitle(result.getString("series_title"));
				arraySeqmodel.setGeoSeriesID(result.getString("geo_series_id"));
				arraySeqmodel.setNumSamples(result.getInt("num_samples"));
				arraySeqmodel.setSource(result.getString("source"));
				arraySeqmodel.setLibraryStrategy(result.getString("library_strategy"));
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
		//String totalwhere=whereclause;
		//String sql = String.format(SequenceQueries.COUNT_TOTAL_MIC_SERIES);
		try
		{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(SequenceQueries.TOTAL_SEQUENCE_SERIES);
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
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	
/*	public Map<String,String>  getTotals() {
		Map<String,String> totals = new HashMap<String,String>();
		
		String [] queries=(assayType.equals("Microarray")?Globals.MicSeriesColTotals:Globals.SeqSeriesColTotals);
		
		String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");
		
		String sql="";
		for(int i=0;i<queries.length;i++) {
			try
			{
				Globals.getDatasource().getConnection();
				sql=String.format(QueryTotals.ReturnQuery(queries[i]),totalwhere);

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
	}*/
	
	
}
