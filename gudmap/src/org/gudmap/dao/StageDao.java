package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.totals.QueryTotals;

public class StageDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public StageDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String[][] getStageList(String[] stage, String organ, String symbol) {
		
		String[][] stageLists = null;			
		/** get data from database */
		// get insitu stage list
		String[] insituStageList = getStageList("insitu", stage, organ, symbol);
			
		// get microarray stage list
		String[] arrayStageList = getStageList("Microarray", stage, organ, symbol);
			
		// get age (dpc) stage list
		int len = stage.length;
		String[] dpcStageList = new String[len];
		for (int i=0;i<len;i++) {
			dpcStageList[i] = getDpcStageValue(stage[i]);
		}
			
		// put them together
		stageLists = new String[len][3];
		for (int i=0;i<len;i++) {
			stageLists[i][0] = dpcStageList[i];
			stageLists[i][1] = insituStageList[i];
			stageLists[i][2] = arrayStageList[i];
		}
			/** return the value object */
		return stageLists;
		
	}
	
	/////////
	
	public String[] getStageList(String assayType, String[] stage, String organ, String symbol) {
		String[] stageList = new String[stage.length];
		String stageString = null;
		String geneString = "";
		String componentString = null;
		String querySQL="";
		////////////
		///////////////
		if(null != stage) {
			if (assayType.equals("insitu")) {
				querySQL=QueryTotals.ReturnQuery("INSITU_TOTAL");
				stageString = " AND SUB_EMBRYO_STG = '";
				if (symbol != null && !symbol.equals("")) {
					geneString += " AND RPR_SYMBOL = '" + symbol + "'";
				}
				componentString = " AND EXP_COMPONENT_ID IN ";
			} 
			else if (assayType.equals("Microarray")) {
				// if gene criteria is not provided, use alternative query and much faster
				if (symbol != null && !symbol.equals("")) {
					querySQL = ArrayQueries.TOTAL_CACHE_ARRAY_SUBMISSIONS;
					stageString = " AND MBC_SUB_EMBRYO_STG = '";
					geneString += " AND MBC_GNF_SYMBOL = '" + symbol + "'";
					componentString = " AND MBC_COMPONENT_ID IN ";
				} 
				else {
					querySQL = ArrayQueries.TOTAL_ARRAY_SUBMISSIONS;
					stageString = " AND SUB_EMBRYO_STG = '";
					componentString = " AND EXP_COMPONENT_ID IN ";
				}
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
			con = ds.getConnection();
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
			con = ds.getConnection();
			ps = con.prepareStatement(queryString);
			ps.setString(1, theilerStage);
			result =  ps.executeQuery();
			if (result.first()) {
				dpcStageValue = result.getString(1) + " " + result.getString(2);
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


}
