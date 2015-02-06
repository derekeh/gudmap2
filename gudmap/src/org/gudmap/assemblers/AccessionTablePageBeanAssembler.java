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
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.InsituTableBeanModel;

public class AccessionTablePageBeanAssembler {
	
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private InsituTableBeanModel ishmodel;
	private String paramSQL;
	private String assayType;
	private String whereclause;
	private String focusGroupWhereclause;
	private String expressionJoin;
	private String specimenWhereclause;
	private String accessionTotals;
	private String input;
	
	public  AccessionTablePageBeanAssembler(String paramSQL) {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		this.paramSQL=paramSQL;
		
	}
	
	public List<InsituTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause, 
											String focusGroupWhereclause, String expressionJoin,String specimenWhereclause,String input){
		this.whereclause=whereclause;
		this.focusGroupWhereclause=focusGroupWhereclause;
		this.expressionJoin=expressionJoin;
		this.specimenWhereclause=specimenWhereclause;
		this.input=input;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		String ishSearch="'GUDMAP:21339', 'maprobe:21339', 'MGI:21339', 'ENSMUSG21339', 'MTF#21339'";
		
		String sql = String.format(paramSQL, input,input,input,input,input,input,input);
		
		/*if(assayType.equals("TG"))
			whereclause = whereclause.replace("RPR_SYMBOL", "ALE_GENE");*/
		
		/*String sql = String.format(paramSQL, expressionJoin, whereclause, focusGroupWhereclause, sortField, sortDirection);
		if(!expressionJoin.equals(""))
			sql=sql.replace("FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID", "");
		sql=sql.replace("SUB_DB_STATUS_FK = 4  AND", "SUB_DB_STATUS_FK = 4  AND"+specimenWhereclause);*/
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, firstRow);
			ps.setInt(2, rowCount);
			result =  ps.executeQuery();
			
			//group_concat returning no value will return a null row so don't get those!
			while(result.next()){
				//while(result.next() && result.getString(1)!=null){
				if(result.getString(1)!=null)
				{
					ishmodel=new InsituTableBeanModel();
					ishmodel.setOid(result.getString("oid"));
					ishmodel.setGene(result.getString("gene"));
					ishmodel.setGudmap_accession(result.getString("gudmap_accession"));
					ishmodel.setSource(result.getString("source"));
					ishmodel.setSubmission_date(result.getString("submission_date"));
					ishmodel.setAssay_type(result.getString("assay_type"));
					ishmodel.setProbe_name(result.getString("probe_name"));
					ishmodel.setStage(result.getString("stage"));
					ishmodel.setAge(result.getString("age"));
					ishmodel.setSex(result.getString("sex"));
					ishmodel.setGenotype(result.getString("genotype"));
					ishmodel.setTissue(result.getString("tissue"));
					ishmodel.setExpression(result.getString("expression"));
					ishmodel.setSpecimen(result.getString("specimen"));
					ishmodel.setImage(result.getString("image"));
					
					ishmodel.setSelected(false);
					list.add(ishmodel);
				}
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return list;
	}
	
	public int count() {
		accessionTotals="Totals returned: Insitu (";
		int count=0;
		int insitucount=0; int microarraycount=0; int sequencecount=0;
		/*String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");
		if(assayType.equals("TG"))
			totalwhere = totalwhere.replace("RPR_SYMBOL", "ALE_GENE");
		String sql = String.format(GenericQueries.ASSAY_TYPE_TOTAL_GUDMAP_ACCESSION,expressionJoin,totalwhere,focusGroupWhereclause);
		sql=sql.replace(" WHERE ", " WHERE "+specimenWhereclause);*/
		String queryString=GenericQueries.ISH_ACCESSION_TOTAL;
		String sql = String.format(queryString, input,input,input,input,input);
		try
		{
				con = ds.getConnection();
				ps = con.prepareStatement(sql);
				//ps.setString(1, assayType);
				result =  ps.executeQuery();
				
				while(result.next()){
					insitucount=result.getInt(1);
					count=result.getInt(1);
				}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
			    Globals.closeQuietly(con, ps, result);
		}
		accessionTotals+=(insitucount+")  Microarray(");
		queryString=GenericQueries.MICROARRAY_ACCESSION_TOTAL;
		sql = String.format(queryString, input);
		try
		{
				con = ds.getConnection();
				ps = con.prepareStatement(sql);
				//ps.setString(1, assayType);
				result =  ps.executeQuery();
				
				while(result.next()){
					microarraycount=result.getInt(1);
					count=count+result.getInt(1);
				}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
			    Globals.closeQuietly(con, ps, result);
		}
		accessionTotals+=(microarraycount+")  Sequence(");
		queryString=GenericQueries.SEQUENCE_ACCESSION_TOTAL;
		sql = String.format(queryString, input);
		try
		{
				con = ds.getConnection();
				ps = con.prepareStatement(sql);
				//ps.setString(1, assayType);
				result =  ps.executeQuery();
				
				while(result.next()){
					sequencecount=result.getInt(1);
					count=count+result.getInt(1);
				}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
			    Globals.closeQuietly(con, ps, result);
		}
		accessionTotals+=(sequencecount+")");
		return count;
	}
	
	public Map<String,String>  getTotals() {
		Map<String,String> totals = new HashMap<String,String>();
		
		String [] queries=(assayType.equals("TG")?Globals.TgColTotals:Globals.IshColTotals);
		
		String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");
		if(assayType.equals("TG"))
			totalwhere = totalwhere.replace("RPR_SYMBOL", "ALE_GENE");
		String sql="";
		for(int i=0;i<queries.length;i++) {
			try
			{
				con = ds.getConnection();
				if(queries[i].equals("ASSAY_TYPE_TOTAL_TISSUE") || queries[i].equals("ASSAY_TYPE_TOTAL_EXPRESSION") || queries[i].equals("TG_TYPE_TOTAL_EXPRESSION")){
					sql= String.format(QueryTotals.ReturnQuery(queries[i]),totalwhere,focusGroupWhereclause);
					sql=sql.replace(" WHERE ", " WHERE "+specimenWhereclause);
				}
				else if(queries[i].equals("TG_TYPE_TOTAL_PROBE_NAME"))
					sql = QueryTotals.ReturnQuery(queries[i]);
				else {
					sql=String.format(QueryTotals.ReturnQuery(queries[i]),expressionJoin,totalwhere,focusGroupWhereclause);
					sql=sql.replace(" WHERE ", " WHERE "+specimenWhereclause);
					//ps = con.prepareStatement(sql);
				}
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
	
	public String getAccessionTotals() {
		return accessionTotals;
	}
}
