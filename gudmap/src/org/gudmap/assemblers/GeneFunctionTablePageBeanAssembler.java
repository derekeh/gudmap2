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

import org.gudmap.dao.AnatomyDao;
import org.gudmap.dao.GeneFunctionDao;
import org.gudmap.globals.Globals;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.genestrip.GeneListQueries;
import org.gudmap.queries.genestrip.GeneStripQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.InsituTableBeanModel;

public class GeneFunctionTablePageBeanAssembler {
	
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private InsituTableBeanModel ishmodel;
	private String paramSQL;
	private String assayType;
	private String whereclause;
	private String focusGroupWhereclause;
	private String specimenWhereclause;
	private String focusGroupSpWhereclause;
	private String cachewhereclause;
	private String arraycachewhereclause;
	private String expressionJoin;	
	private String queryTotals;
	private String input;
	private String inputArray[];
	private String geneQueryString="";
	private GeneFunctionDao geneFunctionDao;
	private String [] geneSymbols=null;;
	
	private boolean ish_present=false;
	private boolean array_present=false;
	
	public  GeneFunctionTablePageBeanAssembler(String paramSQL) {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		this.paramSQL=paramSQL;
		geneFunctionDao = new GeneFunctionDao();
	}
	
	public void init (String userInput, String wildcard){
		
		inputArray=Utils.splitInputString(userInput);
    	//array to contain the a specific sql query and the condition to narrow down the result set
    	String [] symbolsQParts;
    	String goIdListQ;
    	geneSymbols=null;
    		    
		    //if user wants to do a wild card search
		if (wildcard.equalsIgnoreCase("contains") || wildcard.equalsIgnoreCase("starts with")) {
				//get components to build query to find GO: ids for a specific function
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGoTerm_GoId");
				//create the query string from the users input and the components stored in symbolsQParts
				goIdListQ = Utils.getSymbolsFromGeneInputParamsQuery(inputArray,symbolsQParts[0], symbolsQParts[1], 0);
		 }
		    //search for an exact string
		 else {
				//get components to build query to find GO: ids for a specific function
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGoTerm_GoId");
				//create the query string from the users input and the components stored in symbolsQParts
				goIdListQ = Utils.getSymbolsFromGeneInputParamsQuery(inputArray,symbolsQParts[0], symbolsQParts[1], 1);
		 }
		 geneSymbols=geneFunctionDao.getGeneSymbols(inputArray, goIdListQ, wildcard); 
    	 geneQueryString = Utils.createSqlInputFromResult(geneSymbols);
   }
	
	
	public List<InsituTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause, 
											String focusGroupWhereclause, String expressionJoin,String specimenWhereclause,String input,
											String focusGroupSpWhereclause, String cachewhereclause){
		this.whereclause=whereclause;
		this.focusGroupWhereclause=focusGroupWhereclause;
		this.expressionJoin=expressionJoin;
		this.specimenWhereclause=specimenWhereclause;
		this.input=input;
		this.focusGroupSpWhereclause=focusGroupSpWhereclause;
		this.cachewhereclause=cachewhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
				
		/*String sql = String.format(paramSQL, expressionJoin,whereclause,input,input,input,input,input,
									focusGroupWhereclause,whereclause,input,focusGroupSpWhereclause,whereclause,input,
									focusGroupSpWhereclause,sortField, sortDirection);*/
		String sql = String.format(paramSQL, cachewhereclause,geneQueryString,sortField, sortDirection);
		
		
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
		String sql="";
		String queryString=GeneListQueries.ISH_GENE_FUNCTION_TOTAL;
		
			//String sql = String.format(queryString, expressionJoin,whereclause,input,input,input,input,input,focusGroupWhereclause);
			sql = String.format(queryString, cachewhereclause,geneQueryString);
			
			try
			{
					con = ds.getConnection();
					ps = con.prepareStatement(sql);
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
		
			queryTotals+=(insitucount+")");
			
		//NOT BRINGING BACK MICROARRAY OR SEQUENCE DATA FOR NOW	
		/*queryTotals+=(insitucount+")  Microarray(");
		queryString=AnatomyQueries.TOTAL_MICROARRAY_ANATOMY;
		if(array_present){
			sql = String.format(queryString, arraycachewhereclause,geneQueryString);
			//sql = String.format(queryString, whereclause,input,focusGroupSpWhereclause);
			
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
		}
		queryTotals+=(microarraycount+")  Sequence(");
		queryString=AnatomyQueries.TOTAL_SEQUENCE_ANATOMY;
		//sql = String.format(queryString, whereclause,input,focusGroupSpWhereclause);
		//sql = String.format(queryString, whereclause,input,focusGroupSpWhereclause);
		sql = queryString;
		try
		{
				con = ds.getConnection();
				ps = con.prepareStatement(sql);
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
	
	public String getQueryTotals() {
		return queryTotals;
	}
	
	
}
