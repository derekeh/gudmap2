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

public class MicPlatformTablePageBeanAssembler {
	
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArraySeqTableBeanModel arraySeqmodel;
	private String paramSQL;
	private String assayType;
	//private String whereclause;
	//private String focusGroupWhereclause;
	
	public MicPlatformTablePageBeanAssembler(String paramSQL,String assayType) {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		this.paramSQL=paramSQL;
		//this.assayType=assayType;
		
	}
	
	public List<ArraySeqTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending){
		//this.whereclause=whereclause;
		//this.focusGroupWhereclause=focusGroupWhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, sortField, sortDirection);
		List<ArraySeqTableBeanModel> list = new ArrayList<ArraySeqTableBeanModel>();
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, firstRow);
			ps.setInt(2, rowCount);
			result =  ps.executeQuery();
			
			while(result.next()){
				arraySeqmodel=new ArraySeqTableBeanModel();
				arraySeqmodel.setPlatformID(result.getString("geo_platform_id"));
				arraySeqmodel.setPltName(result.getString("platform_name"));
				arraySeqmodel.setPltTechnology(result.getString("platform_technology"));
				arraySeqmodel.setPltManufacturer(result.getString("platform_manufacturer"));
				arraySeqmodel.setNumSeries(result.getInt("num_series"));
				
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
		String sql = ArrayQueries.COUNT_TOTAL_MIC_PLATFORM;
		try
		{
				con = ds.getConnection();
				ps = con.prepareStatement(sql);
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
}
