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

public class SeqSampleTablePageBeanAssembler {
	
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArraySeqTableBeanModel arraySeqmodel;
	private String paramSQL;
	private String assayType;
	private String paramValue;
	//private String whereclause;
	//private String focusGroupWhereclause;
	
	public SeqSampleTablePageBeanAssembler(String paramSQL,String assayType) {
	
		this.paramSQL=paramSQL;
		paramValue=(Globals.getParameterValue("seqSeriesID")!=null)?"AND NGL_SERIES_FK="+Globals.getParameterValue("seqSeriesID")+" ":"";
		paramValue+=(Globals.getParameterValue("stage")!=null)?" AND STG_STAGE_DISPLAY='"+Globals.getParameterValue("stage")+"' ":"";
		
	}
	
	public List<ArraySeqTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause){
		//this.whereclause=whereclause;
		//this.focusGroupWhereclause=focusGroupWhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, whereclause, paramValue, sortField, sortDirection);
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
				arraySeqmodel.setOid(result.getString("oid"));
				arraySeqmodel.setGudmap_accession(result.getString("gudmap_accession"));
				arraySeqmodel.setGeoSampleID(result.getString("geo_sample_id"));
				arraySeqmodel.setStage(result.getString("stage"));
				arraySeqmodel.setStage_order(result.getString("stage").substring(2));
				arraySeqmodel.setSpecies(result.getString("species"));
				arraySeqmodel.setAge(result.getString("age"));
				arraySeqmodel.setSource(result.getString("source"));
				arraySeqmodel.setLibraryStrategy(result.getString("library_strategy"));
				arraySeqmodel.setSubmission_date(result.getString("submission_date"));
				arraySeqmodel.setSex(result.getString("sex"));
				arraySeqmodel.setSampleDescription(result.getString("sample_description"));
				arraySeqmodel.setTitle(result.getString("sample_name"));
				arraySeqmodel.setGeoSeriesID(result.getString("geo_series_id"));
				arraySeqmodel.setSampleComponents(result.getString("components"));
				arraySeqmodel.setGenotype(result.getString("genotype"));
				arraySeqmodel.setAssay_type(result.getString("assay_type"));
				arraySeqmodel.setPersonOid(result.getInt("person_oid"));
				arraySeqmodel.setSeriesOid(result.getInt("series_oid"));
				
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
		String sql = String.format(SequenceQueries.TOTAL_SEQUENCE_SAMPLE,paramValue);
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
				con = Globals.getDatasource().getConnection();
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
