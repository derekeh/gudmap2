package org.gudmap.assemblers;



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

/**
 * Assembler for accession input
 * @author dhoughto
 * @see AccessionTablePageBean
 */
public class AccessionTablePageBeanAssembler {
	
	
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
	private String queryTotals;
	private String input;
	private String focusGroupSpWhereclause;
	
	/**
	 * @param paramSQL The parameterized sql query passed from the view (browseAccessionTablePage)
	 */
	public  AccessionTablePageBeanAssembler(String paramSQL) {
		
		this.paramSQL=paramSQL;
		
	}
	
	
	/**
	 * Returns a list of submission models that are displayed in a table. A model entry represents a submission
	 * @param firstRow the db row at which the paginator starts
	 * @param rowCount the number of rows to retrieve
	 * @param sortField the column on which the query is sorted 
	 * @param sortAscending the direction of the sort
	 * @param whereclause the where clause to be inserted into the paramaterized query
	 * @param focusGroupWhereclause an extension to the whereclause where focus (tissue) groups are invoked
	 * @param expressionJoin an extension to the parameterized query if the focusGroupWhereclause is not null
	 * @param specimenWhereclause an extension to the parameterized query if the assay type is Sequence or Microarray
	 * @param input the user input
	 * @param focusGroupSpWhereclause an extension to the whereclause where focus (tissue) groups are invoked and the assay type is Sequence or Microarray
	 * @return a list of submission models
	 * 
	 * @see org.gudmap.models.InsituTableBeanModel
	 * @see org.gudmap.beans.GenericTablePageBean#getUserInput()
	 */
	public List<InsituTableBeanModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause, 
											String focusGroupWhereclause, String expressionJoin,String specimenWhereclause,String input,
											String focusGroupSpWhereclause){
		this.whereclause=whereclause;
		this.focusGroupWhereclause=focusGroupWhereclause;
		this.expressionJoin=expressionJoin;
		this.specimenWhereclause=specimenWhereclause;
		this.input=input;
		this.focusGroupSpWhereclause=focusGroupSpWhereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		String sql = String.format(paramSQL, expressionJoin,whereclause,input,input,input,input,input,
									focusGroupWhereclause,whereclause,input,focusGroupSpWhereclause,whereclause,input,
									focusGroupSpWhereclause,sortField, sortDirection);
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		try
		{
			con = Globals.getDatasource().getConnection();
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
	
	/**
	 * Returns the totals of all assay types and constructs a string detailing sub counts for insitu, microarray and sequence data
	 * @return the total count of submissions found
	 */
	public int count() {
		queryTotals="Totals returned: Insitu (";
		int count=0;
		int insitucount=0; int microarraycount=0; int sequencecount=0;
		String queryString=GenericQueries.ISH_ACCESSION_TOTAL;
		String sql = String.format(queryString, expressionJoin,whereclause,input,input,input,input,input,focusGroupWhereclause);
		try
		{
				con = Globals.getDatasource().getConnection();
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
		queryTotals+=(insitucount+")  Microarray(");
		queryString=GenericQueries.MICROARRAY_ACCESSION_TOTAL;
		sql = String.format(queryString, whereclause,input,focusGroupSpWhereclause);
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
		queryString=GenericQueries.SEQUENCE_ACCESSION_TOTAL;
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
		queryTotals+=(sequencecount+")");
		return count;
	}
	
	/**
	 * retrieves the distinct totals for display in table column headers in resultant view of 
	 * @return a hashmap with column identifiers and count key/value pairs
	 */
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
	
	/** Sets the assay type
	 * @param assayType ISH | IHC | TG | NextGen | Microarray 
	 */
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	
	/**
	 * @return the string displaying the totals for each assay type
	 * @see #getTotals()
	 */
	public String getQueryTotals() {
		return queryTotals;
	}
}
