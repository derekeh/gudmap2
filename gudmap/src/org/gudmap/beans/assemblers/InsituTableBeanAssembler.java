package org.gudmap.beans.assemblers;

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
import org.gudmap.models.InsituTableBeanModel;

public class InsituTableBeanAssembler {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private InsituTableBeanModel ishmodel;
	
	public  InsituTableBeanAssembler() {
		
	}
	
	public List<InsituTableBeanModel> getData(){
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String queryString=GenericQueries.BROWSE_ISH;
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			while(result.next()){
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
				
				list.add(ishmodel);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return list;
	}
	
	public Map<String,String>  getTotals() {
		Map<String,String> totals = new HashMap<String,String>();
		String[]queries={"ASSAY_TYPE_TOTAL_GENE","ASSAY_TYPE_TOTAL_GUDMAP_ACCESSION","ASSAY_TYPE_TOTAL_SOURCE","ASSAY_TYPE_TOTAL_SUBMISSION_DATE","ASSAY_TYPE_TOTAL_ASSAY_TYPE",
				"ASSAY_TYPE_TOTAL_PROBE_NAME","ASSAY_TYPE_TOTAL_EMBRYO_STAGE","ASSAY_TYPE_TOTAL_AGE","ASSAY_TYPE_TOTAL_SEX","ASSAY_TYPE_TOTAL_GENOTYPE",
				"ASSAY_TYPE_TOTAL_TISSUE","ASSAY_TYPE_TOTAL_EXPRESSION","ASSAY_TYPE_TOTAL_SPECIMEN_TYPE","ASSAY_TYPE_TOTAL_IMAGES"};
		
		for(int i=0;i<queries.length;i++) {
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(QueryTotals.ReturnQuery(queries[i])); 
				ps.setString(1, "ISH");
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
	

}
