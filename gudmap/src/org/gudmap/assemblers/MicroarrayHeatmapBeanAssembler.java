package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.gudmap.globals.Globals;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.queries.array.MicroarrayHeatmapQueries;
import org.gudmap.utils.Utils;


public class MicroarrayHeatmapBeanAssembler {
		
	private Connection con;
	private PreparedStatement ps;
	private ResultSet resSet;
	
	public  MicroarrayHeatmapBeanAssembler() {
//		
	}
	
    public ArrayList<MasterTableInfo> getMasterTableList() {
    	
    	String sql = String.format(MicroarrayHeatmapQueries.MASTER_SECTION_LIST);
	    ArrayList<MasterTableInfo> masterTableList = new ArrayList<MasterTableInfo>();

		try
		{
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					MasterTableInfo masterTableInfo = new MasterTableInfo();
					masterTableInfo.setId(resSet.getString(1) + "_" + resSet.getString(2));
					masterTableInfo.setMasterId(resSet.getString(1));
					masterTableInfo.setSectionId(resSet.getString(2));
					masterTableInfo.setTitle(resSet.getString(3));
					masterTableInfo.setDescription(resSet.getString(4));
					masterTableInfo.setPlatform(resSet.getString(5));
					
					// set the default selection for the tabbed view
//					if (masterTableInfo.getTitle().contains("Developing Kidney (ST1)"))
						masterTableInfo.setSelected(true);
					
					
					masterTableList.add(masterTableInfo);
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}

		return masterTableList;
	}

    
    // ***************************** deal with heatmap
    
    

    // heatmap scaling
    private double upContrast = 2.4;
    private double downContrast = 2.4;
    private double limit = 2.4;
    private double zeroOffset = 0.0;
    private double scale = 1.0;
 	
	

    public String getPlatformIdFromMasterTableId(String masterTableId){

	    String platformId = null;    	
    	String sql = String.format(MicroarrayHeatmapQueries.PLATFORM_ID_FROM_MASTERTABLE_ID);

		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);			
		    String masterId = Utils.parseMasterTableId(masterTableId)[0];		    
		    ps.setInt(1, Integer.parseInt(masterId));
		    
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	platformId = resSet.getString(1);
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    
	    return platformId;
   	
    }

    public ArrayList<String> getProbeSetIdsBySymbolAndPlatformId(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, String symbol, String platformId){

	    ArrayList<String> probeSetIds = new ArrayList<String>();    	
    	//String sql = String.format(MicroarrayHeatmapQueries.PROBESET_FROM_SYMBOL_AND_PLATFORM_ID);

		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(MicroarrayHeatmapQueries.PROBESET_FROM_SYMBOL_AND_PLATFORM_ID);
			ps.setString(1, symbol);
			ps.setString(2, platformId);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					probeSetIds.add(resSet.getString(1)); 
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    
	    return probeSetIds;
   	
    }
    
    public ArrayList<String> getProbeSetIdsByGenelistIdAndPlatformId(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, String genelistId, String platformId){
    	
	    ArrayList<String> probeSetIds = new ArrayList<String>();    	
    	//String sql = String.format(MicroarrayHeatmapQueries.PROBESET_FROM_GENELIST_ID_AND_PLATFORM_ID);

		// check if given genelist is linked to the given platform
		// if it is, return relevant probe ids;
		// if not, get probe ids on the given platform
		String geneListPlatformId = getAnalysisGenelistPlatformId(genelistId);
		
		// given genelist is not linked to any platform in GUDMAP database
		if (geneListPlatformId == null) {
		    return null;
		}
		
		if (geneListPlatformId.equalsIgnoreCase(platformId)) {
//		    probeSetIds = getProbeSetIdsByAnalysisGenelistId(genelistId, true, 0, 20);
		    probeSetIds = getProbeSetIdsByAnalysisGenelistId(genelistId, sortAscending, firstRow, rowsPerPage);
		}
		else{
		    // get relevant gene symbols
			ArrayList<String> geneSymbols = getGeneSymbolByAnalysisGenelistIdAndPlatformId(genelistId, geneListPlatformId, sortAscending, firstRow, rowsPerPage);
		    
		    // get probe ids on the given platform
		    if (geneSymbols != null && geneSymbols.size() > 0) {				
		    	probeSetIds =  getProbeSetIdsBySymbolsAndPlatformId(geneSymbols, platformId, sortAscending, firstRow, rowsPerPage);
		    }
			
		}
	    
	    return probeSetIds;
    }

	public String getAnalysisGenelistPlatformId(String genelistId) {
		
    	String platformId = null;   
    	String sql = String.format(MicroarrayHeatmapQueries.ANALYSIS_GENELIST_PLATFORM);
    	
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, genelistId);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	platformId = resSet.getString(1);
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    return platformId;
		
		
	}
    
    public ArrayList<String> getProbeSetIdsByAnalysisGenelistId(String genelistId, boolean ascending, int firstRow, int rowsPerPage){

    	ArrayList<String> probeSetIds = new ArrayList<String>();   
    	String sql = String.format(MicroarrayHeatmapQueries.PROBE_SET_ID_BY_ANALYSIS_GENELIST_ID);
    	
    	// append order by clause
    	if (!ascending) {
    	    sql += " DESC";
    	}    	
    	// append row number restriction clause
    	if (firstRow >= 0) {
    	    sql += " LIMIT " + Integer.toString(firstRow) + " ," + Integer.toString(rowsPerPage);
    	}
    
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(genelistId));
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
				    probeSetIds.add(resSet.getString(1));
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    return probeSetIds;
  	
    }
   
    
    
    public ArrayList<String> getGeneSymbolByAnalysisGenelistIdAndPlatformId(String genelistId, String platformId, boolean ascending, int firstRow, int rowsPerPage){

	    ArrayList<String> genes = new ArrayList<String>();    	
    	String sql = String.format(MicroarrayHeatmapQueries.GENE_SYMBOL_BY_ANALYSIS_GENELIST_ID);
    	// append order by clause
    	if (!ascending) {
    	    sql += " DESC";
    	}    	
    	// append row number restriction clause
    	if (firstRow >= 0) {
    	    sql += " LIMIT " + Integer.toString(firstRow) + " ," + Integer.toString(rowsPerPage);
    	}
    	
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(genelistId));
			ps.setString(2, platformId);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
		    	String gene;
				while (resSet.next()) {
					gene = resSet.getString(1); 
	    			if (gene != null && !gene.equals("") && !gene.equalsIgnoreCase("SymbolNA")) {
	        			genes.add(gene);
	    			}
				}

		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    return genes;
   	
    }
    
    public ArrayList<String> getProbeSetIdsBySymbolsAndPlatformId(ArrayList<String> symbols, String platformId, boolean ascending, int firstRow, int rowsPerPage){

	    ArrayList<String> probeSetIds = new ArrayList<String>();    	
    	String sql = String.format(MicroarrayHeatmapQueries.ARRAY_PROBESET_FROM_SYMBOL_AND_PLATFORM_ID);
        // put symbol criteria into the query string
        String symbolString = "('";
        for (String symbol : symbols) {
        	symbolString += symbol + "', '";
        }
        symbolString = symbolString.substring(0, symbolString.length()-3) + ")";
        String queryString = sql.replaceAll("WHERE GNF_SYMBOL = \\?", "WHERE GNF_SYMBOL IN"+symbolString );

    	// append order by clause
    	if (!ascending) {
    		queryString += " DESC";
    	}    	
    	// append row number restriction clause
    	if (firstRow >= 0) {
    		queryString += " LIMIT " + Integer.toString(firstRow) + " ," + Integer.toString(rowsPerPage);
    	}
        
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString);
			ps.setString(1, platformId);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					probeSetIds.add(resSet.getString(1)); 
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
	    
	    return probeSetIds;
   	
    }
    
    public ArrayList<String> getHeatmapExpressionTitlesFromMasterTableId(String masterTableId){
    	
    	ArrayList<String> expressionTitles = new ArrayList<String>(); 
    	String sql = String.format(MicroarrayHeatmapQueries.EXPRESSION_TITLES_FROM_MASTERTABLE_ID);
   	
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			
        	String[] masterTableAndSectionId = Utils.parseMasterTableId(masterTableId);
			ps.setInt(1, Integer.parseInt(masterTableAndSectionId[0]));
			ps.setInt(2, Integer.parseInt(masterTableAndSectionId[1]));
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					expressionTitles.add(resSet.getString(3)); 
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
		
		return expressionTitles;
    }

    public ArrayList<String[]> getHeatmapDataFromProbeIdAndMasterTableId(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, String probeId, String masterTableId){
    	
    	ArrayList<String[]> data = new ArrayList<String[]>(); 
    	String sql = String.format(MicroarrayHeatmapQueries.HEATMAP_DATA_FROM_PROBE_ID_AND_MASTERTABLE_ID);
   	
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			
        	String[] masterTableAndSectionId = Utils.parseMasterTableId(masterTableId);
        	ps.setString(1, probeId);
			ps.setInt(2, Integer.parseInt(masterTableAndSectionId[0]));
			ps.setInt(3, Integer.parseInt(masterTableAndSectionId[1]));
			ps.setString(4, probeId);

			System.out.println("getHeatmapDataFromProbeIdAndMasterTableId ps = " + ps);
			
            double scaledLimit = Math.abs(limit * scale);
			
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					String[] item = new String[7];
	                item[ 0] = resSet.getString(1); // AME_M_HEADER_FK
	                item[ 1] = resSet.getString(2); // AME_PROBE_SET_FK
	                double value = resSet.getDouble(3); // AME_VALUE_RMA
	                double median = resSet.getDouble(4); // AMS_MEDIAN
	                double stdDev = resSet.getDouble(5); // AMS_STD
	                
	                item[ 2] = resSet.getString(3); // AME_VALUE_RMA
	                item[ 3] = resSet.getString(4); // AMS_MEDIAN
	                item[ 4] = resSet.getString(5); // AMS_STD
	                
	                // adjusted value
					double adjustedValue = value - median;
					adjustedValue /= stdDev;
					adjustedValue -= zeroOffset;
					adjustedValue *= scale;
					adjustedValue /= (adjustedValue<0)? downContrast : upContrast;
					if (adjustedValue < -scaledLimit)
						adjustedValue = -scaledLimit;
					if (adjustedValue > scaledLimit)
						adjustedValue = scaledLimit;
//	                item[ 5] = String.valueOf(adjustedValue); // AMS_STD
					DecimalFormat df = new DecimalFormat("#0.0000");
	                item[ 5] = df.format(adjustedValue); // AMS_STD
	                
	                item[ 6] = getHeatmapColor(adjustedValue);
					data.add(item);
				}
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
		
		return data;
    }
    
    private String getHeatmapColor(double value){
    	
		int colorValue = (int)Math.round(value*255);
		if (colorValue < 0)
			return htmlColor(0, 0, -colorValue);
		else
			return htmlColor(colorValue, 0, 0);
  	
    }
	private String htmlColor(int r, int g, int b) {
		return twoDigitHex(r) + twoDigitHex(g) + twoDigitHex(b);
	}

	private String twoDigitHex(int value) {
		value = Math.min(255, value);
		value = Math.max(0, value);
		String hex = Integer.toHexString(value);
		if (hex.length() < 2 )
			hex = "0" + hex;
		return hex;
	}
    
    
	public String getGenelistTitle(String genelistId) {
		
    	String genelistTitle = null; 
    	String sql = String.format(MicroarrayHeatmapQueries.ANALYSIS_GENELIST_TITLE);
   	
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, Integer.parseInt(genelistId));
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	genelistTitle = resSet.getString(1); 
		    }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
		
		return genelistTitle.replace("_", " ");
	}
   
	public ArrayList<String[]> getAnnotationByProbeSetIds(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, ArrayList<String> probeSetIds) {
		
		ArrayList<String[]> annotations = new ArrayList<String[]>(); 
    	String sql = String.format(MicroarrayHeatmapQueries.MIC_ANALYSIS_ANNOTATION);
    	String queryString = null;
    	
        if (probeSetIds != null && probeSetIds.size() != 0) {
        	String probeSetIdString = "WHERE MAN_PROBE_SET_ID IN (";
        	String probeSetIdList = "";
        	int len = probeSetIds.size();
        	for (int i=0;i<len;i++) {
        		probeSetIdString += "'" + probeSetIds.get(i).toString() + "', ";
        		probeSetIdList += "'" + probeSetIds.get(i).toString() + "', ";
        	}
        	probeSetIdString = probeSetIdString.substring(0, probeSetIdString.length()-2) + ") ";
        	probeSetIdList = probeSetIdList.substring(0, probeSetIdList.length()-2);
        	queryString = sql.replace("WHERE MAN_PROBE_SET_ID", probeSetIdString);
         	queryString = queryString.replace("PROBE_SET_ID_ARG", probeSetIdList);
        } else {
        	queryString = sql.replace("WHERE MAN_PROBE_SET_ID", "");
        	queryString = queryString.replaceAll("ORDER BY FIELD(MAN_PROBE_SET_ID, PROBE_SET_ID_ARG)", "");
        }

//    	// append order by clause
//    	if (!sortAscending) {
//    		queryString += " DESC";
//    	}    	
//    	// append row number restriction clause
//    	if (firstRow >= 0) {
//    		queryString += " LIMIT " + Integer.toString(firstRow) + " ," + Integer.toString(rowsPerPage);
//    	}
       
        
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString);
			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					String[] item = new String[8];
	                item[0] = resSet.getString(1); 
	                item[1] = resSet.getString(2); 
	                item[2] = resSet.getString(3); 
	                item[3] = resSet.getString(4); 
	                item[4] = resSet.getString(5); 
	                item[5] = resSet.getString(6); 
	                item[6] = resSet.getString(7); 
	                item[7] = resSet.getString(8); 
	                
	                annotations.add(item);
				}
		    }
        	
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
		
		return annotations;
	}
    
	public ArrayList<String[]> getMicroarrayGeneAverage(String gene) {
		
		ArrayList<String[]> averages = new ArrayList<String[]>(); 
    	String sql = String.format(MicroarrayHeatmapQueries.MIC_GENE_AVERAGE);
    	              
		try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql);
        	ps.setString(1, gene);

			resSet =  ps.executeQuery();
		    if (resSet.first()) {
		    	resSet.beforeFirst();
				while (resSet.next()) {
					String[] item = new String[2];
	                item[0] = resSet.getString(1); // MGA_DATASET_ID
	                item[1] = resSet.getString(2); // MGA_AVERAGE_EXP
	                
	                averages.add(item);
				}
		    }
        	
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, resSet);
		}
		
		return averages;
	}

   
    
    
    
    
    
    
    
    
    
}
