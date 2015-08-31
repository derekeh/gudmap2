package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gudmap.dao.BooleanQueryDao;
import org.gudmap.globals.Globals;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.genestrip.GeneListQueries;

public class BooleanResultAssembler {
	
	BooleanQueryDao booleanQueryDao=null;
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
	private String searchResultOption;
	private String booleanInput;
	private int numRows=-1;
	private String booleanSql;
	
	public BooleanResultAssembler() {
		booleanQueryDao = new BooleanQueryDao();
		
		searchResultOption ="entry";
		booleanInput = (String)Globals.getSessionValue("input");
		if(null != booleanInput && !booleanInput.equals("") && booleanInput.indexOf(":")>=0 ) {
			String prefix = booleanInput.substring(0, booleanInput.indexOf(":"));
			if (prefix != null) {
				booleanInput = booleanInput.substring(booleanInput.indexOf(":")+1);
				if (prefix.equalsIgnoreCase("gene") || prefix.equalsIgnoreCase("genes"))
					searchResultOption = "gene";
				if (prefix.equalsIgnoreCase("TF") || prefix.equalsIgnoreCase("transcription factor"))
					searchResultOption = "TF";
			}
		}
  }

	
	
	
	public  ArrayList<String> retrieveGenes(){
			
		ArrayList<String> genes = booleanQueryDao.getGeneSymbols(booleanInput);
		return genes;
	}
	
	///////////////////////////////////////
	
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
		
		/*String sql = String.format(paramSQL, expressionJoin,whereclause,input,input,input,input,input,
			focusGroupWhereclause,whereclause,input,focusGroupSpWhereclause,whereclause,input,
			focusGroupSpWhereclause,sortField, sortDirection);*/
		
		booleanSql = booleanQueryDao.getAllSubmissionsQuery(booleanInput);
				
		String paramSql = booleanSql	+ " ORDER BY %s %s LIMIT ?, ?";
		
		if (paramSql.indexOf("DISTINCT")<0 || paramSql.indexOf("DISTINCT")>15)
			paramSql = paramSql.replaceFirst("SELECT", "SELECT DISTINCT");
		
		String sql = String.format(paramSql, sortField, sortDirection);
		
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
		int count=0;
		if (booleanSql == null || booleanSql.equals("")) {
					return 0;
		}
		String queryString = booleanSql.replaceFirst("SELECT", "SELECT DISTINCT");
		String sql = "select count(*) from (" +	queryString + ") as tablea";
		try
		{
				con = Globals.getDatasource().getConnection();
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

}
