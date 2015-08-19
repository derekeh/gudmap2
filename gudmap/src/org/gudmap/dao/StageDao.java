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
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.array.SequenceQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.totals.QueryTotals;

public class StageDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public StageDao() {
		
	}
	
	public String[] getStageList(String assayType, String[] stage, String organ, String geneId) {
		String[] stageList = new String[stage.length];
		String stageString = null;
		String geneString = "";
		String componentString = null;
		String querySQL="";
		////////////
		if(null != stage) {
			if (assayType.equals("insitu")) {
				querySQL=QueryTotals.ReturnQuery("INSITU_TOTAL");
				stageString = " AND STG_STAGE_DISPLAY = '";
				if (geneId != null && !geneId.equals("")) {
					//geneString += " AND RPR_SYMBOL = '" + symbol + "'";
					geneString += " AND RPR_LOCUS_TAG = '" + geneId + "'";
				}
				componentString = " AND EXP_COMPONENT_ID IN ";
			} 
			else if (assayType.equals("Microarray")) {
				// if gene criteria is not provided, use alternative query and much faster
				if (geneId != null && !geneId.equals("")) {
					querySQL = ArrayQueries.TOTAL_CACHE_ARRAY_SUBMISSIONS;
					stageString = " AND MBC_STG_STAGE_DISPLAY = '";
					//geneString += " AND MBC_GNF_SYMBOL = '" + symbol + "'";
					geneString += " AND MBC_MAN_MGI_ID = '" + geneId + "'";
					componentString = " AND MBC_COMPONENT_ID IN ";
				} 
				else {
					querySQL = ArrayQueries.TOTAL_ARRAY_SUBMISSIONS;
					stageString = " AND STG_STAGE_DISPLAY = '";
					componentString = " AND EXP_COMPONENT_ID IN ";
				}
			}
			else if (assayType.equals("sequence")) {
				querySQL = SequenceQueries.TOTAL_NUMBER_OF_NGSEQUENCES;
				stageString = " AND STG_STAGE_DISPLAY = '";
				componentString = "";
				geneString = "";
			}
			
			
			String queryString = "";
			
			for(int i = 0; i < stage.length; i++) {
                // append stage criteria
				queryString = querySQL + stageString + stage[i] + "' ";
				
				// append gene criteria
				queryString += geneString;
				
				// append component criteria
				if(organ != null && !organ.equals("")) {
					String[] emapids = 
						(String[])Globals.getEMAPID().get(organ);
					String ids = "";
					  for(int j = 0; j < emapids.length; j++) {
						  ids += "'"+emapids[j] + "',";
					  }
					  if(emapids.length >= 1) {
						  ids = ids.substring(0, ids.length()-1);
					  }
					  queryString += componentString + 
						  " (SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
						  " FROM ANA_TIMED_NODE ANCES_ATN, " +
						  " ANAD_RELATIONSHIP_TRANSITIVE, " +
						  " ANA_TIMED_NODE DESCEND_ATN, " +
						  " ANA_NODE, " +
						  " ANAD_PART_OF " +
						  " WHERE ANCES_ATN.ATN_PUBLIC_ID IN (" + ids + ") " +
						  " AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
						  " AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
						  " AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
						  " AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
						  " AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = TRUE) ";
					  
				}
				stageList[i] = stageDetailsFromDB(queryString);
//				
			}
		}
		
		return stageList;
		

	} // end of getStageList(assayType, stage, organ, symbol)
	
	private String stageDetailsFromDB(String queryString){
		String RET="";
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				RET = result.getString(1);
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return RET;
	}
	
	////////
	
	public String getDpcStageValue(String theilerStage) {
		String dpcStageValue = null;
		String queryString = GenericQueries.EQUIVALENT_DPC_STAGE_FOR_THEILER_STAGE;
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString);
			ps.setString(1, theilerStage);
			result =  ps.executeQuery();
			if (result.first()) {
				dpcStageValue = result.getString(1);
			} else {
				dpcStageValue = "";
			}			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return dpcStageValue;
		
	}
	

	
public ArrayList<String> getStages(String species) {
		
		ArrayList<String> stages = new ArrayList<String>();
		
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(GenericQueries.STAGES_FROM_REF_STAGE);
			ps.setString(1, species);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				while(result.next()) {
					String stage = result.getString(1);
			        stages.add(stage);
				}
			}			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return stages;
	}

	


}
