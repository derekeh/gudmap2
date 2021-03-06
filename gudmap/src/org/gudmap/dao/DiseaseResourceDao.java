package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.models.DiseaseResourceModel;
import org.gudmap.queries.disease.DiseaseQueries;

public class DiseaseResourceDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	//private String annotationType;
	private boolean getPhenotypeID=false;
	
	public DiseaseResourceDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<DiseaseResourceModel> getGeneAssocDisease(String inputTerm,String type, boolean isDirect) {
		if(inputTerm==null || inputTerm.equals(""))
			return null;
		
		String queryString = "";
		if(type.equals("name"))
			queryString=DiseaseQueries.GENES_ASSOC_DISEASE;
		else if(type.equals("gene")){
			if(inputTerm.startsWith("MGI:"))
				queryString=DiseaseQueries.GENE_ASSOC_DISEASE_MGI;
			else
				queryString=DiseaseQueries.GENE_ASSOC_DISEASE_TERM;
		}
		else if(type.equals("phenotype")) {
			queryString=DiseaseQueries.PHENOTYPE_ID_FROM_TERM;
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, inputTerm);
				result =  ps.executeQuery();
				if (result.first()){
					inputTerm=result.getString(1);					
				}
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			} 
			queryString=DiseaseQueries.GENES_ASSOC_PHENOTYPE;
		}
		else if(type.equals("phenotypegene")){
			if(isDirect)//TODO AMEND CONDITION
				queryString=DiseaseQueries.PHENOTYPE_BY_GENE_DIRECT;
			else
				queryString=DiseaseQueries.PHENOTYPE_BY_GENE_DERIVED;
		}
		ArrayList<DiseaseResourceModel> datalist = new ArrayList<DiseaseResourceModel>();
		DiseaseResourceModel diseaseResourceModel=null;
		
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, inputTerm);
			result =  ps.executeQuery();
			if (result.first()){
				result.beforeFirst();
				while(result.next()) {
					diseaseResourceModel = new DiseaseResourceModel();
					diseaseResourceModel.setOmimID(result.getString("omim_id"));
					diseaseResourceModel.setDiseaseName(result.getString("disease_name"));
					diseaseResourceModel.setHumanGeneSymbol(result.getString("human_gene_symbol"));
					diseaseResourceModel.setMouseGeneSymbol(result.getString("mouse_gene_symbol"));
					diseaseResourceModel.setMouseGeneMgiID(result.getString("mouse_gene_mgi_id"));
					diseaseResourceModel.setHasInsituData(result.getString("has_insitu_data"));
					diseaseResourceModel.setMpID(result.getString("mp_id"));
					diseaseResourceModel.setMpPhenotype(result.getString("mp_phenotype"));
					diseaseResourceModel.setAnnotationType(result.getString("annotation_type"));
					
					datalist.add(diseaseResourceModel);
				}				
			}			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}        
        return datalist;
	}


}
