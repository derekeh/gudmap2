package org.gudmap.assemblers;

import javax.faces.context.FacesContext;
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
import org.gudmap.queries.genestrip.GeneIndexQueries;
import org.gudmap.queries.genestrip.GeneListQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.InsituTableBeanModel;

public class GeneExpressionTablePageBeanAssembler {
	
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private InsituTableBeanModel ishmodel;
	private String paramSQL;
	private String assayType;
	private String expressionStrength;
	private String geneSymbol;
	private String geneId;
	private String type;
	private String queryTotals;
	
	public  GeneExpressionTablePageBeanAssembler(String paramSQL, String assayType) {
		
		this.paramSQL=paramSQL;
		this.assayType=assayType;
	}
	
	public List<InsituTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending){
		
		if(Globals.getParameterValue("geneId")!=null)
			geneId = Globals.getParameterValue("geneId");
		if(Globals.getParameterValue("strength")!=null)
			expressionStrength = Globals.getParameterValue("strength");
		
		if(geneId==null || geneId.equals("") || expressionStrength==null || expressionStrength.equals(""))
			return null;
		
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, processExpression(expressionStrength, assayType),sortField, sortDirection);
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, geneId);
			ps.setInt(2, firstRow);
			ps.setInt(3, rowCount);
			result =  ps.executeQuery();
			
			//group_concat returning no value will return a null row so don't get those!
			while(result.next()){
				//while(result.next() && result.getString(1)!=null){
				if(result.getString(1)!=null)
				{
					ishmodel=new InsituTableBeanModel();
					//ishmodel.setOid(result.getString("gudmap_accession").substring(7));
					ishmodel.setOid(result.getString("oid"));
					ishmodel.setGene(result.getString("gene"));
					ishmodel.setGudmap_accession(result.getString("gudmap_accession"));
					ishmodel.setSource(result.getString("source"));
					ishmodel.setSubmission_date(result.getString("submission_date"));
					ishmodel.setAssay_type(result.getString("assay_type"));
					ishmodel.setProbe_name(result.getString("probe_name"));
					ishmodel.setStage(result.getString("stage"));
					ishmodel.setStage_order(result.getString("stage").substring(2));
					ishmodel.setSpecies(result.getString("species"));
					ishmodel.setAge(result.getString("age"));
					ishmodel.setSex(result.getString("sex"));
					ishmodel.setGenotype(result.getString("genotype"));
					ishmodel.setTissue(result.getString("tissue"));
					ishmodel.setExpression(result.getString("expression"));
					ishmodel.setSpecimen(result.getString("specimen"));
					ishmodel.setImage(result.getString("image"));
					ishmodel.setGene_id(result.getString("gene_id"));
					
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
		queryTotals="Totals returned: Insitu (";
		int count=0;
		int insitucount=0; int microarraycount=0; int sequencecount=0;
		String queryString=GeneIndexQueries.TOTAL_GENES_BY_EXPRESSION;
		String sql = String.format(queryString, expressionStrength);
		try
		{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(sql);
				ps.setString(1, geneId);
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
		queryTotals+=insitucount+")";
		//queryTotals+=(insitucount+")  Microarray(");
		/*queryString=GeneListQueries.MICROARRAY_GENELIST_TOTAL;
		sql = String.format(queryString, arrayWhereclause,input,focusGroupSpWhereclause);
		try
		{
				con = Globals.getDatasource().getConnection();
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
		queryTotals+=(microarraycount+")  Sequence(");
		queryString=GeneListQueries.SEQUENCE_GENELIST_TOTAL;
		sql = String.format(queryString, whereclause,input,focusGroupSpWhereclause);
		try
		{
				con = Globals.getDatasource().getConnection();
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
		queryTotals+=(sequencecount+")");*/
		return count;
	}
	
	/*public Map<String,String>  getTotals() {
		Map<String,String> totals = new HashMap<String,String>();
		
		String [] queries=(assayType.equals("TG")?Globals.TgColTotals:Globals.IshColTotals);
		
		String totalwhere=(whereclause.equals(" WHERE "))?"":Utils.removeWhere(whereclause, " WHERE ");
		if(assayType.equals("TG"))
			totalwhere = totalwhere.replace("RPR_SYMBOL", "ALE_GENE");
		String sql="";
		for(int i=0;i<queries.length;i++) {
			try
			{
				con = Globals.getDatasource().getConnection();
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
	}*/
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	
	public String getQueryTotals() {
		return queryTotals;
	}
	
	public String processExpression(String expressionStrength, String assayType) {
		String ishString="";
		String micString="";
		if(null != expressionStrength) {
			if(assayType.equals("ISH"))
			{
			    if(expressionStrength.equals("present")) {
			    	ishString = " AND QIC_EXP_STRENGTH='present' ";
			    } else if (expressionStrength.equals("absent")) {
			    	ishString = " AND QIC_EXP_STRENGTH='not detected' ";
			    } else if (expressionStrength.equals("unknown")) {
			    	ishString = " AND QIC_EXP_STRENGTH not in ('not detected', 'present') ";
			    } else { 
			    	ishString = " AND QIC_EXP_STRENGTH='uncertain' ";
			    }
			}
			else if(assayType.equals("Microarray"))
			{
			    if(expressionStrength.equals("P")) {
			    	micString = " AND MBC_GLI_DETECTION='P' ";
			    } else if (expressionStrength.equals("A")) {
			    	micString = " AND MBC_GLI_DETECTION='A' ";
			    } else if (expressionStrength.equals("M")){
			    	micString = " AND MBC_GLI_DETECTION='M' ";
			    }
			}
		}
		if(assayType.equals("microarray"))
			return micString;
		
		return ishString;
	}
}
