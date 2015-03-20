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
import org.gudmap.globals.Globals;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.totals.QueryTotals;
import org.gudmap.utils.Utils;
import org.gudmap.models.InsituTableBeanModel;

public class AnatomyTablePageBeanAssembler {
	
	
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
	private String timedComponentsQueryString="";
	private String descendentComponentsQueryString="";
	private String ancestorComponentsQueryString="";
	private AnatomyDao anatomyDao;
	
	private boolean ish_present=false;
	private boolean array_present=false;
	
	public  AnatomyTablePageBeanAssembler(/*String paramSQL*/) {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		//this.paramSQL=paramSQL;
		anatomyDao = new AnatomyDao();
		
	}
	//TODO this is the order of events. Still to Work through their implemenation
	public void init(String input) {
		String [] descendentComponents = null;
		String [] ancestorComponents = null;
		String transitiveRelations=null;
		//input is the input from the anatomy text input autocomplete on databaseHomepage. (only takes one string at present) - can use
		String [] timedComponentsArray = anatomyDao.getTimedComponentIdsFromInput(Utils.normaliseApostrophe(input));
		//get exact expression
		if(transitiveRelations != null && transitiveRelations.equals("0")) {
			descendentComponents = timedComponentsArray;
			ancestorComponents = timedComponentsArray;
		} 
		//get inferred expression
		else {
			descendentComponents = anatomyDao.getTransitiveRelations(timedComponentsArray, "descendent");
			ancestorComponents = anatomyDao.getTransitiveRelations(timedComponentsArray, "ancestor");
		}
		timedComponentsQueryString=Utils.createSqlInputFromResult(timedComponentsArray);
		descendentComponentsQueryString=Utils.createSqlInputFromResult(descendentComponents);
		ancestorComponentsQueryString=Utils.createSqlInputFromResult(ancestorComponents);
		
	}
	
	public List<InsituTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause, 
											String focusGroupWhereclause, String expressionJoin,String specimenWhereclause,String input,
											String focusGroupSpWhereclause, String cachewhereclause, String arraycachewhereclause){
		this.whereclause=whereclause;
		this.focusGroupWhereclause=focusGroupWhereclause;
		this.expressionJoin=expressionJoin;
		this.specimenWhereclause=specimenWhereclause;
		this.input=input;
		this.focusGroupSpWhereclause=focusGroupSpWhereclause;
		this.cachewhereclause=cachewhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		this.arraycachewhereclause = arraycachewhereclause;
		
		//assemble unions based on assaytype in filter
		assembleParamSQL(cachewhereclause);
		
		/*String sql = String.format(paramSQL, expressionJoin,whereclause,input,input,input,input,input,
									focusGroupWhereclause,whereclause,input,focusGroupSpWhereclause,whereclause,input,
									focusGroupSpWhereclause,sortField, sortDirection);*/
		String sql="";
		//different substitutions to query string based on assaytype in filter
		if(ish_present && array_present) {
			sql = String.format(paramSQL,cachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,
									cachewhereclause,timedComponentsQueryString,ancestorComponentsQueryString,
									cachewhereclause,timedComponentsQueryString,
									arraycachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,ancestorComponentsQueryString,
									sortField, sortDirection);
		}
		
		if(ish_present && !array_present){
			sql = String.format(paramSQL,cachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,
					cachewhereclause,timedComponentsQueryString,ancestorComponentsQueryString,
					cachewhereclause,timedComponentsQueryString,
					sortField, sortDirection);
		}
		
		if(!ish_present && array_present){
			sql = String.format(paramSQL,arraycachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,ancestorComponentsQueryString,
					sortField, sortDirection);
		}
		
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
		String queryString=AnatomyQueries.TOTAL_ISH_ANATOMY;
		if(ish_present){
			//String sql = String.format(queryString, expressionJoin,whereclause,input,input,input,input,input,focusGroupWhereclause);
			sql = String.format(queryString, cachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,
					cachewhereclause,timedComponentsQueryString,ancestorComponentsQueryString,
					cachewhereclause,timedComponentsQueryString);
			
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
		}
		queryTotals+=(insitucount+")  Microarray(");
		queryString=AnatomyQueries.TOTAL_MICROARRAY_ANATOMY;
		if(array_present){
			sql = String.format(queryString, arraycachewhereclause,timedComponentsQueryString,descendentComponentsQueryString,ancestorComponentsQueryString);
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
		queryTotals+=(sequencecount+")");
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
	
	public void assembleParamSQL(String cachewhereclause) {
		//String partParamSQL= AnatomyQueries.BROWSE_ANATOMY_HEADER_PARAM;
		String partParamSQL= AnatomyQueries.BROWSE_ANATOMY_HEADER_PARAM;
		ish_present=false;
		array_present=false;
		if(cachewhereclause.contains("SUB_ASSAY_TYPE")){
			
			if(cachewhereclause.contains("'ISH'") || cachewhereclause.contains("'IHC'") || cachewhereclause.contains("'TG'") ) {
				ish_present=true;
				partParamSQL+=AnatomyQueries.BROWSE_ANATOMY_ISH_PARAM;
			}
			
			if(cachewhereclause.contains("Microarray") || cachewhereclause.contains("NextGen")) {
				array_present=true;
				partParamSQL+=(ish_present)?(" UNION " +AnatomyQueries.BROWSE_ANATOMY_MIC_PARAM):AnatomyQueries.BROWSE_ANATOMY_MIC_PARAM;
			}
			
		}
		else {
			ish_present=true;
			array_present=true;
			partParamSQL+=(AnatomyQueries.BROWSE_ANATOMY_ISH_PARAM + " UNION " + AnatomyQueries.BROWSE_ANATOMY_MIC_PARAM);
		}
		
		
		partParamSQL+=AnatomyQueries.BROWSE_ANATOMY_FOOTER_PARAM;
		paramSQL=partParamSQL;	
	}
}
