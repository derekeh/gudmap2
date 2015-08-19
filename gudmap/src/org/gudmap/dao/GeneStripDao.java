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
import org.gudmap.models.GeneStripModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.queries.genestrip.GeneStripQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.submission.IshSubmissionQueries;
import org.gudmap.utils.Utils;

public class GeneStripDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	private Connection repeatCon=null;
	private PreparedStatement repeatPs=null;
	private ResultSet repeatResult=null;
	
	public GeneStripDao(){
		/*try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}*/
	}
	
	  public GeneStripModel getGeneStripDataFromSymbol(String geneId){
		  GeneStripModel geneStripModel=null;
		  String arrayRange="";
		  String ishRange="";
		  String species="";
		  
		  try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(GeneStripQueries.GENESTRIP_ROW_MINUS_PROFILES); 
				ps.setString(1, geneId);
				result =  ps.executeQuery();
				
				if (result.first()) {
					result.beforeFirst();
					geneStripModel = new GeneStripModel();
					while (result.next()) {
						geneStripModel.setGeneSymbol(result.getString("gene"));
						geneStripModel.setSynonyms(result.getString("synonyms"));
						geneStripModel.setMgiId(result.getString("mgi"));
						arrayRange = (result.getString("arrayRange"));
						ishRange = (result.getString("ishRange"));
						species = (result.getString("species"));
						geneStripModel.setExpressionProfile(buildExpressionProfile(result.getString("gene"),geneId));
						geneStripModel.setMicroarrayProfile(buildMicroarrayProfile(geneId));
						geneStripModel.setStageRange(calculateStageRange(arrayRange,ishRange,species));
						geneStripModel.setOmimCount(Integer.parseInt(result.getString("omim")));
						geneStripModel.setImageUrl(getRepresentativeImage(geneId));
						geneStripModel.setSelected(false);
						geneStripModel.setGene_id(geneId);
						geneStripModel.setSpecies(species);
					}
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		  
		  return geneStripModel;
	  }
	  
	private String buildExpressionProfile(String geneSymbol, String geneId) {
		String RET="";
		double[] insituExprofile = getInsituExprofile(geneId);
		String[] interestedAnatomyStructures = Globals.getInterestedAnatomyStructureIds();
		RET=getExpressionHtmlCode(insituExprofile,interestedAnatomyStructures,geneSymbol,geneId);
		return RET;
	}
	
	private MasterTableInfo[] buildMicroarrayProfile(String geneSymbol) {
		ArrayList<MasterTableInfo> masterTableInfoList=null;
		String queryString=GeneStripQueries.MASTER_SECTION_LIST;
		boolean found=false;
		////////////
		try
		{
			repeatCon = Globals.getDatasource().getConnection();
			repeatPs = repeatCon.prepareStatement(queryString); 
			repeatResult =  repeatPs.executeQuery();
			if (repeatResult.first()) {
				masterTableInfoList=new ArrayList<MasterTableInfo>();
				found=true;
				repeatResult.beforeFirst();
				 while (repeatResult.next()) {
						MasterTableInfo masterTableInfo = new MasterTableInfo();
						masterTableInfo.setId(repeatResult.getString(1) + "_" + repeatResult.getString(2));
						masterTableInfo.setMasterId(repeatResult.getString(1));
						masterTableInfo.setSectionId(repeatResult.getString(2));
						masterTableInfo.setTitle(repeatResult.getString(3));
						masterTableInfo.setDescription(repeatResult.getString(4));
						masterTableInfo.setPlatform(repeatResult.getString(5));
						masterTableInfoList.add(masterTableInfo);
			    }
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		}
		
		return masterTableInfoList.toArray(new MasterTableInfo[masterTableInfoList.size()]);
		
		//THIS IS CODE TAKEN FROM JSF1. BERNIE TO IMPLEMENT USING D3 
		/*for (MasterTableInfo item : masterTableInfo) {
		    if (DbUtility.retrieveGeneProbeIds(geneSymbol, item.getPlatform()) != null) {//check to see if there is possible data for this symbol (it is to avoid refering to null images which display as a crsss icon in IE) 
				element = new DataItem("../dynamicimages/heatmap_" + geneSymbol + ".jpg?tile=5&masterTableId="+item.getId(), 
						       "Click to see " + item.getTitle() + " microarray expression profile for "+ symbol, 
						       "mastertable_browse.html?gene="+symbol+"&masterTableId="+item.getId()+"&cleartabs=true", 15);
				
				complexValue.add(element);
		    }		
		}*/
		////////////
		/*if(found)
			return masterTableInfoList.toArray(new MasterTableInfo[masterTableInfoList.size()]);
		else
			return null;*/
	}
	
	public static String getExpressionHtmlCode(double[] values, String[] focusGroups, String symbol, String geneId) {
		// added by xingjun - 08/05/2009 - its possible values is null
		if (values == null || values.length == 0) {
			return "";
		}
		
		String code = "<div id='exprLevelsGraph_" + symbol + "' style='text-align:center'></div>";
		// get focus group string list
		String focusGroupString = "";
		for (int i=0;i<focusGroups.length;i++) {
			focusGroupString += focusGroups[i] + "+','+";
		}
		focusGroupString = focusGroupString.substring(0, (focusGroupString.length()-1));
		
		// url
		String browseLink = "'browseGeneListTablePage.jsf?expressionGene=" + symbol + "&amp;focusGroup='";
		
		// concatenate script string
    	//code += "<script type='text/javascript'> //<![CDATA[ var val=";
    	code += "<script type='text/javascript'> var val=";
    	for (int i=0; i<values.length; i++)
    		code += ((i==0)? "" : "+','+") + String.valueOf(values[i]);
    	// focus group string
    	code += ";focusGroups=" + focusGroupString + ";";
    	// browse link string
    	code += "browseLink=" + browseLink + ";";
    	code += "geneSymbol='" + symbol + "';";
    	// javascript function
    	code += "prepareGraph(geneSymbol,val,browseLink,focusGroups); </script>";
    	return code;
	}
	
	private double[] getInsituExprofile(String geneId) {
		if (geneId == null || geneId.equals("")) {
			return null;
		}
		String[] interestedAnatomyStructures = Globals.getInterestedAnatomyStructureIds();
		
		// get relevant component id list
		int analen = interestedAnatomyStructures.length;
		int barHeight = Globals.getDefaultExpressionProfileBarHeight();
		// array to store expression profiles
		// 1: present; -1: not detected; 0: not examined/uncertain
		// need to include expression profile not related to given structures - others
//		double[] expressionProfiles = new double[analen+1];
		double[] insituExprofile = new double[analen];
		ArrayList<String> componentsOfAllGivenStructures = new ArrayList<String>();
					
			/** calculate expression profile for all given structures */
			for (int i=0;i<analen;i++) {
	//			System.out.println("structure: " + i);
				// get component ids new anatomy
				//String[] componentIds = (String[])Globals.getEMAPID().get(interestedAnatomyStructures[i]);
				String[] componentIds = (String[])Globals.getEMAPAID().get(interestedAnatomyStructures[i]);
				
				// put component ids into componentIdsInAll arrayList
				int eLen = componentIds.length;
	//			System.out.println("component id number: " + eLen);
				for (int j=0;j<eLen;j++) {
					componentsOfAllGivenStructures.add(componentIds[j]);
				}
	
				// get expression info
				ArrayList<String[]> expressionOfGivenComponents = getGeneExpressionForStructure(geneId, componentIds, true);
				
				// start to calculate - only relevant expression exists
				double indicator = 0;
				if (expressionOfGivenComponents != null 
						&& expressionOfGivenComponents.size() != 0) {
					int compLen = expressionOfGivenComponents.size();
					// look for 'present'
					for (int j=0;j<compLen;j++) {
						String expression = ((String[])expressionOfGivenComponents.get(j))[1];
						if (expression.equalsIgnoreCase("present")) {
							indicator = 1.00;
							break;
						}
					}
					
					if (indicator == 0) { // there's no component with expression value of 'present'
						// look for 'not detected'
						for (int j=0;j<compLen;j++) {
							String expression = ((String[])expressionOfGivenComponents.get(j))[1];
							if (expression.equalsIgnoreCase("not detected")) {
								indicator = -1.00;
								break;
							}
						}
					}
				}
				
				// put calculation result into expression profile array
				insituExprofile[i] = indicator*barHeight;
			}
			/** return result */
			return insituExprofile;
		
		/** calculate expression profile for other structures */
		
	} // end of getInsituExprofile
	
	public ArrayList<String[]> getGeneExpressionForStructure(String geneId, String[] componentIds, boolean expressionForGivenComponents) {
		if (geneId == null || geneId.equals("")) {
		return null;
		}
		ArrayList<String[]> expressions = null;
		String componentClause = null;
		String componentString = " (";
		String expressionQuery="";
		if (expressionForGivenComponents) {
			expressionQuery = GeneStripQueries.GENE_EXPRESSION_FOR_GIVEN_STRUCTURE;
			componentClause = "AND EXP_COMPONENT_ID IN (";
		} 
		else {
			expressionQuery = GeneStripQueries.GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE;
			componentClause = "AND EXP_COMPONENT_ID NOT IN (";
		}
		// assemble parent component ids string
		int len = componentIds.length;
		for (int i=0;i<len;i++) {
			componentString += "'" + componentIds[i] + "', ";
		}
		componentString = componentString.substring(0, (componentString.length()-2)) + ")";
		
		// assemble full component ids string including child nodes as well as parent component ids
		/*String childQuery = GeneStripQueries.FIND_CHILD_NODE;
		componentClause += childQuery.replaceAll("WHERE ANCES_ATN.ATN_PUBLIC_ID IN", 
		("WHERE ANCES_ATN.ATN_PUBLIC_ID IN "+ componentString)) + ")";*/
		
		//NEW ANATOMY
		String childQuery = GeneStripQueries.FIND_CHILD_NODE_EMAPA;
		componentClause += childQuery.replaceAll("WHERE ANO_PUBLIC_ID IN ", 
    			("WHERE ANO_PUBLIC_ID IN  "+ componentString)) + ")";
		
		
		// use different query string according to what expressions we are looking for
		String queryString = null;
		if (expressionForGivenComponents) {
			queryString = expressionQuery.replace("AND EXP_COMPONENT_ID IN", componentClause);
		} 
		else {
			queryString = expressionQuery.replace("AND EXP_COMPONENT_ID NOT IN", componentClause);
		}
		
		
		try
		{
			repeatCon = Globals.getDatasource().getConnection();
			repeatPs = repeatCon.prepareStatement(queryString); 
			repeatPs.setString(1, geneId);
			repeatResult =  repeatPs.executeQuery();
			expressions = Utils.formatResultSetToArrayList(repeatResult);
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		}
		
		return expressions;
	}
	
/*	private String[] getInterestedAnatomyStructureIds() {
		String[] interestedAnatomyStructures=null;
		
		return interestedAnatomyStructures;
	}*/
	
/*	private String getExpressionHtmlCode(double[] insituExprofile, String[] interestedAnatomyStructures, String geneSymbol ) {
		String RET="";
		
		return RET;
	}*/
	
	public ArrayList<String> getSymbolsFromGeneInput(String input, String wildcard) {
		String[] geneInput = input.split(":|;|,"); 
		ArrayList<String> symbols = new ArrayList<String>();
		for(int i=0; i<geneInput.length; i++)
			if (!"".equals(geneInput[i].trim()))
				symbols.add(geneInput[i]);
		String[] symbolsArray = new String[symbols.size()];
		symbolsArray = symbols.toArray(symbolsArray);
		return this.getSymbolsFromGeneInput(symbolsArray, wildcard);
	}
	
	
	private ArrayList<String> getSymbolsFromGeneInput(String[] input, String wildcard) {
    	ArrayList<String> geneSymbols = null;
    	ArrayList<String> geneIds = null;
    	//array to contain components to build a specific query
		String[] symbolsQParts;
		//string to contain sql to find gene symbols from REF_PROBE using gene symbol
		String symbolsFromRefProbeSymbolQ;
		//string to contain sql to find gene symbol from REF_PROBE using gene name
		String symbolsFromRefProbeNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene symbol
		String symbolsFromrefGeneInfoSymbolQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene name
		String symbolsFromrefGeneInfoNameQ;
		//string to contain sql to find gene symbol from REF_GENE_INFO using gene synonym
		String symbolsFromrefGeneInfoSynonymQ;
		
		// this query will return a list of synonyms to be used as input in another genefinding query - symbolsFromSynListQ
		String synonymListQ;
		//string to contain sql to find gene symbol from REF_PROBE using gene synonym
		String symbolsFromRefProbeSynonymQ;
		

					
			//if user wants to do a wild card search
		if (wildcard.equalsIgnoreCase("contains") || wildcard.equalsIgnoreCase("starts with")) {
			//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
			//create sql from components and user input
			symbolsFromRefProbeSymbolQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
			//create sql from components and user input
			symbolsFromRefProbeNameQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
			//create sql from components and user input
			symbolsFromrefGeneInfoSymbolQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
			//create sql from components and user input
			symbolsFromrefGeneInfoNameQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			// 09/10/2009 - START
			//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
			//create sql from components and user input
			symbolsFromrefGeneInfoSynonymQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			// 09/10/2009 - END
			//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
			//create sql from components and user input
			synonymListQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
			//get components to build query to find synonymns from REF_PROBE using synonym as a param to narrow search
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_synonym");
			//create sql from components and user input
			symbolsFromRefProbeSynonymQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], symbolsQParts[2], 0);
			}
			//search for an exact string
			else {
				//get components to build query to find symbols from REF_PROBE using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Symbol");
				//create sql from components and user input
				symbolsFromRefProbeSymbolQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_PROBE using gene name as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_Name");
				//create sql from components and user input
				symbolsFromRefProbeNameQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene symbol as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Symbol");
				//create sql from components and user input
				symbolsFromrefGeneInfoSymbolQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				//get components to build query to find symbols from REF_GENE_INFO using gene name as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_Name");
				//create sql from components and user input
				symbolsFromrefGeneInfoNameQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 0);
				// 09/10/2009 - START
				//get components to build query to find symbol from REF_GENE_INFO using gene synonym as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefGeneInfo_synonym");
				//create sql from components and user input
				symbolsFromrefGeneInfoSynonymQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1],0);
				// 09/10/2009 - END
				//get components to build query to find synonymns from REF_SYNONYM using synonym as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefSyn_Synonym");
				//create sql from components and user input
				synonymListQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], 1);
				//get components to build query to find synonymns from REF_PROBE using synonym as a param to narrow search
				symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefProbe_synonym");
				//create sql from components and user input
				symbolsFromRefProbeSynonymQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], symbolsQParts[2], 0);
			}

			// need to execute query to get syn list here
			String[] synList = null;
			
			try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(synonymListQ);
				for(int i=0;i<input.length;i++){
					if(wildcard.equalsIgnoreCase("contains")) {
						ps.setString(i+1, "%"+input[i].trim()+"%");
					}
					else if(wildcard.equalsIgnoreCase("starts with")){
						ps.setString(i+1, input[i].trim()+"%");
					}
					else {
						ps.setString(i+1, input[i].trim());
					}
				}
				result = ps.executeQuery();
				if (result.first()) {
					result.last();
					int rowCount = result.getRow();
					result.beforeFirst();
					synList = new String[rowCount];
					int i = 0;
					while (result.next()) {
						synList[i] = result.getString(1);
						i++;
					}
				}
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
			

			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrk_MGIAcc");
			String symbolsFromMGiAccQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], symbolsQParts[2], 1);
			
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefEnsGene_EnsemblId");
			String symbolsFromEnsemblIdQ = getSymbolsFromGeneInputParamsQuery(input,symbolsQParts[0], symbolsQParts[1], symbolsQParts[2], 1);
			
			// sligtly different query - had to get list of relevant synonyms
			// from db to use as input for this query
			symbolsQParts = (String[]) (GeneStripQueries.getRefTableAndColTofindGeneSymbols()).get("RefMgiMrkRefSyn_Synonym");
			String symbolsFromSynListQ = getSymbolsFromGeneInputParamsQuery(synList,symbolsQParts[0], symbolsQParts[1], 1);

			String union = GenericQueries.UNION_CLAUSE;
			//use 'union' to incorporate all queryies into a single query
			String allQueriesQ = symbolsFromRefProbeSymbolQ + union
					+ symbolsFromRefProbeNameQ + union
					+ symbolsFromrefGeneInfoSymbolQ + union
					+ symbolsFromrefGeneInfoNameQ + union
					+ symbolsFromrefGeneInfoSynonymQ + union // 09/10/2009
					+ symbolsFromRefProbeSynonymQ + union
					+ symbolsFromMGiAccQ + union 
					+ symbolsFromEnsemblIdQ;
			if(!symbolsFromSynListQ.equals("")){
				allQueriesQ += union + symbolsFromSynListQ;
			}
			
			try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(allQueriesQ); 
				//for the first 4 in 'union' query, set the parameters
				for(int i=0;i<5;i++){// xingjun - 09/10/2009 - change from 4 to 5
					for(int j=0;j<input.length;j++){
						if(wildcard.equalsIgnoreCase("contains")) {
							ps.setString((i*input.length)+j+1, "%"+input[j].trim()+"%");
						}
						else if(wildcard.equalsIgnoreCase("starts with")) {
							ps.setString((i*input.length)+j+1, input[j].trim()+"%");
						}
						else {
							ps.setString((i*input.length)+j+1, input[j].trim());
						}
					}
				}
				//start the loop at 4 since we have already set params for the first four queries.
				//set the params for the remaining 'union' queries
				for(int i=5;i<8;i++){// xingjun - 09/10/2009 - change from 4 to 5 and 6 to 7 respectively
					for(int j=0;j<input.length;j++){
						ps.setString((i*input.length)+j+1, input[j].trim());
					}
				}
				//need to set additional params based on result of synonym search
				if(synList != null) {
				    for(int i = 0;i< synList.length;i++){
				    	//as there are 6 previous queries, need to start setting params from 6 onwards.
				    	ps.setString((8*input.length+1+i), synList[i].trim());// xingjun - 09/10/2009 - change from 6 to 7
				    }
				}
					
				result =  ps.executeQuery();
				String str = null;
				if(result.first()){
					result.last();
					geneSymbols = new ArrayList<String>();
					geneIds = new ArrayList<String>();
					result.beforeFirst();
					while (result.next()) {
					    str = Utils.netTrim(result.getString(1));
					    if(null != str){
					    	geneSymbols.add(str);
					    }
					    geneIds.add(result.getString(2));
					}
				}
	
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
			
			//return geneSymbols;
			return geneIds;

	} // end of getSymbolsFromGeneI
	
	//query to get a list of gene symbols from a specified reference table. Can only add search params to one column in table 
	 private String getSymbolsFromGeneInputParamsQuery(String [] input, 
			  String startQuery, String searchColumn, int type){
		  if(input == null)
			  return "";
		  StringBuffer symbolsQ = new StringBuffer(startQuery);
		  //0 == 'like' query ('contains' or 'starts with')
		  if(type == 0) {
			  symbolsQ.append("(");
			  for(int i=0; i<input.length;i++){
	    			if(i==0){
	    				symbolsQ.append(searchColumn+" LIKE ? ");
	    			}
	    			else {
	    				symbolsQ.append("OR "+searchColumn+" LIKE ? ");
	    			}
	    		}
	    		symbolsQ.append(")");
		  }
		  //else type will be 1: equivalent to 'equals'
		  else {
			  symbolsQ.append(searchColumn + " IN (");
			  for(int i=0;i<input.length;i++){
	            	if(i == input.length-1){
	            		symbolsQ.append("?)");
	            	}
	            	else {
	            		symbolsQ.append("?, ");
	            	}
	            }
		  }
		  return symbolsQ.toString();
	  }
	  
	  static public String getSymbolsFromGeneInputParamsQuery(String [] input, 
			  String startQuery, String searchColumn, String order, int type){
		  if(input == null)
			  return "";
		  StringBuffer symbolsQ = new StringBuffer(startQuery);
		  //0 == 'like' query ('contains' or 'starts with')
		  if(type == 0) {
			  symbolsQ.append("(");
			  for(int i=0; i<input.length;i++){
	    			if(i==0){
	    				symbolsQ.append(searchColumn+" RLIKE ? ");
	    			}
	    			else {
	    				symbolsQ.append("OR "+searchColumn+" RLIKE ? ");
	    			}
	    		}
	    		symbolsQ.append(")");
		  }
		  //else type will be 1: equivalent to 'equals'
		  else {
			  symbolsQ.append(searchColumn + " IN (");
			  for(int i=0;i<input.length;i++){
	            	if(i == input.length-1){
	            		symbolsQ.append("?)");
	            	}
	            	else {
	            		symbolsQ.append("?, ");
	            	}
	            }
		  }
		  return symbolsQ.toString() + order;
	  }

	  
	  private String calculateStageRange(String arrayRange, String ishRange, String species){
		  String RET="";
		  Connection stgConn = null;
		  PreparedStatement stgps = null;;
		  ResultSet stgresult = null;
		  ArrayList<Integer> rangeList=new ArrayList<Integer>();
		  if(arrayRange!=null) {
			  String [] arrayValues = arrayRange.split("-");
			  
			  for(int i=0;i<arrayValues.length;i++) {
				  rangeList.add(Integer.valueOf(arrayValues[i]));
			  }
		  }
		  if(ishRange!=null) {
			  String [] ishValues = ishRange.split("-"); 		  
			  for(int i=0;i<ishValues.length;i++) {
				  rangeList.add(Integer.valueOf(ishValues[i]));
			  }
		  }
		  if(rangeList.size()>0) {
			  java.util.Collections.sort(rangeList);			  
			  if(species.startsWith("Mus")) {
				  RET="TS"+rangeList.get(0).toString()+"-TS"+rangeList.get((rangeList.size()-1)).toString();
			  }
			  else if(species.startsWith("Hom")) {
				  RET = "";
				  
				  //////////////
				try
				{
					stgConn = Globals.getDatasource().getConnection();
					stgps = stgConn.prepareStatement("SELECT STG_STAGE_DISPLAY FROM REF_STAGE WHERE STG_ORDER = "+rangeList.get(0).toString()+" AND STG_SPECIES = 'Homo sapiens'"); 
					stgresult =  stgps.executeQuery();
					while (stgresult.next()) {
						RET=stgresult.getString(1)+"-";			
					}			
				}
				catch(SQLException sqle){sqle.printStackTrace();}
				finally {
						Globals.closeQuietly(stgConn, stgps, stgresult);
				}
				
				try
				{
					stgConn = Globals.getDatasource().getConnection();
					stgps = stgConn.prepareStatement("SELECT STG_STAGE_DISPLAY FROM REF_STAGE WHERE STG_ORDER = "+rangeList.get((rangeList.size()-1)).toString()+" AND STG_SPECIES = 'Homo sapiens'"); 
					stgresult =  stgps.executeQuery();
					while (stgresult.next()) {
						RET+=stgresult.getString(1);			
					}			
				}
				catch(SQLException sqle){sqle.printStackTrace();}
				finally {
						Globals.closeQuietly(stgConn, stgps, stgresult);
				}
				  
				  /////////////
			  }
		  }
		  
		  return RET;
	  }
	  
	  private String getRepresentativeImage(String geneSymbol){
		  if (geneSymbol == null || geneSymbol.equals("")) {
			    return null;
		  }
		  
		  ArrayList<String[]> relatedSubmissionsList=null;
		  String queryString = GeneStripQueries.GENE_RELATED_SUBMISSIONS_ISH;
		  try
			{
			  repeatCon = Globals.getDatasource().getConnection();
			  repeatPs = repeatCon.prepareStatement(queryString); 
			  repeatPs.setString(1, geneSymbol);
			  repeatResult =  repeatPs.executeQuery();
			  relatedSubmissionsList = Utils.formatResultSetToArrayList(repeatResult);				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
			}
		  /*TODO 
		   * NOW GET THE URL BASED ON THE CANDIDATE ENTRIES
		   * 
		   */
		  String candidateSubmission = chooseRepresentativeInsituSubmission(relatedSubmissionsList);
		  String thumbnailURL = "";
		  ArrayList <ImageInfoModel> imageInfoModel=null;
				// get the image and put the url into the string
		 if (candidateSubmission != null) {
					//thumbnail = this.getThumbnailURL(conn, candidateSubmission);
					//data[i][5] = new DataItem(thumbnail, "Click to see image matrix for "+symbol, "image_matrix_browse.html?gene="+symbol, 13);
			 queryString = IshSubmissionQueries.IMAGE_INFO_BY_ACCESSION_ID;
			 try
				{
				 repeatCon = Globals.getDatasource().getConnection();
				 repeatPs = repeatCon.prepareStatement(queryString); 
				 repeatPs.setString(1, candidateSubmission);
				 repeatResult =  repeatPs.executeQuery();
				 imageInfoModel = Utils.formatImageResultSet(repeatResult);
					
					
				}
				catch(SQLException sqle){sqle.printStackTrace();}
				finally {
				    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
				} 
			    //return imageInfoModel;
			
			if (imageInfoModel == null || imageInfoModel.size() == 0) {
					thumbnailURL = "N/A";
			} 
			else {
					thumbnailURL = ((ImageInfoModel)imageInfoModel.get(0)).getFilePath();
			}
		 } 
		 return thumbnailURL;
				
		}
	
	  
	  private String chooseRepresentativeInsituSubmission(ArrayList<String[]> insituSubmissions) {
			if (insituSubmissions == null || insituSubmissions.size() == 0) {
				return null;
			}
			int len = insituSubmissions.size();
			String submissionId = null;
			/** TS23 **/
			// try to find submission at ts23 & with assay type 'section'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS23") && assayType.trim().equalsIgnoreCase("section")) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}
			
			// failed, try to find submission at ts23 & with assay type 'wholemount'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS23") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}
			
			/** TS21 **/
			// failed, try to find submission at ts21 & with assay type 'section'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS21") && assayType.trim().equalsIgnoreCase("section")) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}
			// failed, try to find submission at ts21 & with assay type 'wholemount'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS21") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}

			/** TS20 **/
			// failed, try to find submission at ts20 & with assay type 'section'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS20") && assayType.trim().equalsIgnoreCase("section")) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}
			// failed, try to find submission at ts20 & with assay type 'wholemount'
			for (int i=0;i<len;i++) {
				String stage = ((String[])insituSubmissions.get(i))[2];
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (stage.equals("TS20") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}

			// failed, start from the submissions at stage ts17, head up to the end
			// try to find the first submission with assay type 'section'
			for (int i=0;i<len;i++) {
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (assayType.trim().equalsIgnoreCase("section")) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					//String stage = ((String[])insituSubmissions.get(i))[2];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}
			
			// failed, start from the submissions at stage ts17, head up to the end
			// try to find the first submission with assay type 'wholemount'
			for (int i=0;i<len;i++) {
				String assayType = ((String[])insituSubmissions.get(i))[3];
				if (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount")) {
					submissionId = ((String[])insituSubmissions.get(i))[0];
					//String stage = ((String[])insituSubmissions.get(i))[2];
					break;
				}
			}
			if (submissionId != null) { // found it!
				return submissionId;
			}

			// nothing found - impossible!!!
//			System.out.println("Nothing found!!!");
			return null;
		}
	    
	    //IMAGE MATRIX
	    public ArrayList<String> retrieveImageIdsByGeneId(String geneId) {
	    	if (geneId == null || geneId.equals("")) {
				return null;
			}
	    	String queryString = GeneStripQueries.INSITU_SUBMISSION_IMAGE_ID_BY_GENE_ID;
	    	ArrayList<String> imageList=null;
	    	try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(queryString);
				ps.setString(1, geneId);
				result =  ps.executeQuery();
				if (result.first()) {
					imageList = new ArrayList<String>();
		        	result.beforeFirst();
		        	while (result.next()) {
		        		String imageId = result.getString(1);
		        		imageList.add(imageId);
		        	}					
				}				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
	    	return imageList;
	    	
	    }
	    
	    public ArrayList <ImageInfoModel>getInsituSubmissionImagesByImageId(ArrayList<String> imageIds) {
	    	
			if (imageIds == null || imageIds.size() == 0) {
				return null;				
			}
			ArrayList<ImageInfoModel> imageInfoList = null;
			String queryString = GeneStripQueries.INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID;
	        int len = imageIds.size();
	    	String whereClause = "FILENAME) IN";
			String imageIdWhereClause = "FILENAME) IN ('" + imageIds.get(0).toString() + "'";
			
			for (int i=1;i<len;i++) {
			    imageIdWhereClause += ", '" + imageIds.get(i).toString()+"'";
			}
			imageIdWhereClause += ") ";
		
	        queryString = queryString.replace(whereClause, imageIdWhereClause);
	        try
			{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(queryString);
				result =  ps.executeQuery();
				if (result.first()) {
					result.beforeFirst();
				    imageInfoList = new ArrayList<ImageInfoModel>();
				    // get the submission id for the first record
				    String tempSubmissionId = null;
				    int serialNo = 0;
				    String submissionId = null;
				    ImageInfoModel imageInfoModel = null;
				    while (result.next()) {
						submissionId = null;
						imageInfoModel = new ImageInfoModel();
			
					    submissionId = result.getString(1);
					    imageInfoModel.setAccessionId(submissionId);
					    //imageInfoModel.setStage("TS"+result.getString(2));
					    imageInfoModel.setStage(result.getString(2));
					    imageInfoModel.setSpecimenType(result.getString(3));
					    imageInfoModel.setFilePath(result.getString(4));
					    imageInfoModel.setClickFilePath(result.getString(5));
					    imageInfoModel.setUniqueImage(result.getString(6));//DEREK
					    imageInfoModel.setOid(result.getString(7));
			
						if (tempSubmissionId == null || !submissionId.equals(tempSubmissionId)) { // its first record or a new submission
						    tempSubmissionId = submissionId;
						    serialNo = 1;
						    imageInfoModel.setSerialNo(""+serialNo);
						} else {
						    serialNo++;
						    imageInfoModel.setSerialNo(Integer.toString(serialNo));
						} 
						
						imageInfoModel.setSelected(false);
						// put the image detail object into the result
						imageInfoList.add(imageInfoModel);
				    }					
				}				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
	        
	        return imageInfoList;
	        
		}

}
