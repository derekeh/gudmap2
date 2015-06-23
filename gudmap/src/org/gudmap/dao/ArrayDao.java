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
import org.gudmap.models.GenelistTreeInfo;
import org.gudmap.models.GenelistRnaSeqTreeInfo;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.array.ArrayQueries;

public class ArrayDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private GeneModel geneModel;
	
	public ArrayDao() {
		/*try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}*/
	}
	
	public GeneModel findGeneInfoBySymbol(ArrayList<String> genes) {
		
		if (genes == null || genes.size() == 0) {
		    return null;
		}
        String queryString = ArrayQueries.GET_GENE_BY_SYMBOL;
        int len = genes.size();
        String symbolCriteria = "";
        String appendString = " OR GNF_SYMBOL = ";
        for (int i=0;i<len;i++) {
		    symbolCriteria += "'" + genes.get(i) + "'" +  appendString;
        }
        symbolCriteria = symbolCriteria.substring(0, (symbolCriteria.length()-appendString.length()));
        queryString += symbolCriteria;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				geneModel = new GeneModel();
                geneModel.setSymbol(result.getString(1));  
                geneModel.setName(result.getString(2));
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return geneModel;
    }

	public ArrayList<GenelistTreeInfo> getRefGenelists(){
		
		ArrayList<GenelistTreeInfo> genelists = null;
		String queryString = ArrayQueries.GET_ALL_REF_GENELISTS;
        try
		{

			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();

        	if (result.first()) {
        		result.beforeFirst();
        		
        		genelists = new ArrayList<GenelistTreeInfo>();
        		while (result.next()) {
        			GenelistTreeInfo genelistTreeInfo = new GenelistTreeInfo();
        			
        			genelistTreeInfo.setGenelistOID(result.getString(1));
        			genelistTreeInfo.setGenelistUID(result.getString(2));
        			genelistTreeInfo.setName(result.getString(3));
        			genelistTreeInfo.setDescription(result.getString(4));
        			genelistTreeInfo.setSeriesPlatform(result.getString(5));
        			genelistTreeInfo.setPlatformGeoId(result.getString(6));
        			genelistTreeInfo.setSample(result.getString(7));
        			genelistTreeInfo.setEmapId(result.getString(8));
        			genelistTreeInfo.setDataset(result.getString(9));
        			genelistTreeInfo.setDatasetId(result.getString(10));
        			genelistTreeInfo.setMethod(result.getString(11));
        			genelistTreeInfo.setEntityType(result.getString(12));
        			genelistTreeInfo.setEntityCount(result.getString(13));
        			genelistTreeInfo.setGeneCount(result.getString(14));
        			genelistTreeInfo.setAuthor(result.getString(15));
        			genelistTreeInfo.setDate(result.getString(16));
        			genelistTreeInfo.setVersion(result.getString(17));
        			genelistTreeInfo.setReference(result.getString(18));
        			genelistTreeInfo.setPublished(Integer.toBinaryString(result.getInt(19)));
        			genelistTreeInfo.setOtherRefs(result.getString(20));
        			genelistTreeInfo.setStage(result.getString(21));
        			genelistTreeInfo.setGenelistType(result.getString(22));
        			genelistTreeInfo.setSex(result.getString(23));
        			genelistTreeInfo.setSubset1(result.getString(24));
        			genelistTreeInfo.setSubset2(result.getString(25));
        			genelistTreeInfo.setSubset3(result.getString(26));
        			genelistTreeInfo.setAmgId(result.getString(27));
        			genelistTreeInfo.setLpuRef(result.getString(28));
        			
        			
        			genelists.add(genelistTreeInfo);
        		}
        	}
		
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return genelists;
	}	
	
	public ArrayList<GenelistRnaSeqTreeInfo> getRefGenelistsRnaSeq(){
		
		ArrayList<GenelistRnaSeqTreeInfo> genelist = null;
		String queryString = ArrayQueries.GET_ALL_REF_GENELISTS_RNASEQ;
		
        try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();

        	if (result.first()) {
        		result.beforeFirst();
        		
        		genelist = new ArrayList<GenelistRnaSeqTreeInfo>();
        		while (result.next()) {
        			GenelistRnaSeqTreeInfo genelistTreeInfo = new GenelistRnaSeqTreeInfo();
        			
        			genelistTreeInfo.setGenelistOID(result.getString(1));
        			genelistTreeInfo.setShortName(result.getString(2));
        			genelistTreeInfo.setLongName(result.getString(3));
        			genelistTreeInfo.setDescription(result.getString(4));
        			genelistTreeInfo.setGlsClass(result.getString(5));
        			genelistTreeInfo.setSubClass(result.getString(6));
        			genelistTreeInfo.setCluster(result.getString(7));
        			genelistTreeInfo.setGeneCount(result.getString(8));
        			genelistTreeInfo.setHeatmap(result.getString(9));
        			genelistTreeInfo.setEmapId(result.getString(10));
        			genelistTreeInfo.setGoBp(result.getString(11));
        			genelistTreeInfo.setGoBpGenes(result.getString(12));
        			genelistTreeInfo.setGoCc(result.getString(13));
        			genelistTreeInfo.setGoCcGenes(result.getString(14));
        			genelistTreeInfo.setMousePheno(result.getString(15));
        			genelistTreeInfo.setMousePhenoGenes(result.getString(16));
        			genelistTreeInfo.setAuthor(result.getString(17));
        			genelistTreeInfo.setDate(result.getString(18));
        			genelistTreeInfo.setReference(result.getString(19));
        			genelistTreeInfo.setPublished(Integer.toBinaryString(result.getInt(20)));
        			genelistTreeInfo.setStage(result.getString(21));
        			
        			
        			genelist.add(genelistTreeInfo);
        		}
        	}
		
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return genelist;
	}	
	
}
