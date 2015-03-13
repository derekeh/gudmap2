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
import javax.faces.context.FacesContext;

import org.gudmap.globals.Globals;
import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.models.submission.ExpressionPatternModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
//import org.gudmap.models.submission.IshBrowseSubmissionModel;
//import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.queries.submission.IshSubmissionQueries;
import org.gudmap.utils.Utils;

public class AnatomyDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public AnatomyDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}	
	}
	
	public String findAnnotationTreeExpressions(String oid) {
		if (oid == null) {
			return null;
		}
		
		//query to find out if the submission has any entries in the expression table of the db
		String treeExpressions = null;
		
		String queryString=AnatomyQueries.ANNOT_TREE_EXPRESSIONS;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			String RET = null;
			if (result.first()) {
				result.beforeFirst();
				RET = new String("");
				while (result.next()) {
					RET += result.getString(1) + "," + result.getString(2) + "," + result.getString(3) + "| ";
				}
				treeExpressions = RET;
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return treeExpressions;
		
	}
	
	 public String findAnnotationTreePatterns(String oid) {
			if (oid == null) {
				return null;
			}
			
			String treePatterns = null;
			
			String queryString=AnatomyQueries.ANNOT_TREE_PATTERNS;
	        try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, oid);
				result =  ps.executeQuery();
				String RET = null;
				if (result.first()) {
					result.beforeFirst();
					RET = new String("");
					while (result.next()) {
						RET += result.getString(1) + "," + result.getString(2) + "| ";
					}
					treePatterns = RET;
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
	        
	        return treePatterns;
			
		}
	 
	 public String findAnnotationTreeExpressionNotes(String oid) {
			if (oid == null) {
				return null;
			}
			
			String treeExpressionNotes = null;
			
			String queryString=AnatomyQueries.ANNOT_TREE_EXPRESSION_NOTES;
	        try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, oid);
				result =  ps.executeQuery();
				String RET = null;
				if (result.first()) {
					result.beforeFirst();
					RET = new String("");
					while (result.next()) {
						RET += result.getString(1) + "," + result.getString(2) + "| ";
					}
					treeExpressionNotes = RET;
				}
				
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
	        
	        return treeExpressionNotes;
			
		} 
	

    /////////////////// create local connection objects here, as we are keeping the global ones open in the wrapper method
    
    String getPatternsForAnnotatedComponent(String expressionOID){
    	
    	String queryString = AnatomyQueries.EXPRESSION_PATTERNS;
    	StringBuffer patterns = new StringBuffer("");
    	Connection connection=null;
    	PreparedStatement pstmt=null;
    	ResultSet rset=null;
        try
		{
			connection = ds.getConnection();
			pstmt = connection.prepareStatement(queryString); 
			pstmt.setString(1, expressionOID);
			rset=  pstmt.executeQuery();
			
			if(rset.first()){
				rset.beforeFirst();  
	            while(rset.next()){
					if(rset.isLast()) {
						  patterns.append(rset.getString(2));
					}
					else {
						   patterns.append(rset.getString(2)+",");
					}  
	             }
	         }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(connection, pstmt, rset);
		}
        return patterns.toString();
        
       
    }
    
    /**
     * query database to find a list of annotated components for a specific submission
     * @param submissionAccessionId - the id of the selected submission
     * @param isEditor - boolean to determine whether the user is logged in as an editor or not
     * @return result - an array of annotated components corresponding to the submissionAccessionId param
     */
    public ExpressionDetailModel [] findAnnotatedListBySubmissionIds(String oid) {
    	
    	String queryString=AnatomyQueries.TREE_ANNOTATIONS;
    	//array of annotated components for this submission
        ExpressionDetailModel [] expressionDetailModel = null;
        Connection locationConnection = null;
        PreparedStatement locationStatement = null;
        ResultSet locationResultSet = null;
        Connection componentConnection = null;
        PreparedStatement componentStatement = null;
        ResultSet componentResultSet = null;
        Connection patternConnection=null;
    	PreparedStatement patternStatement=null;
    	ResultSet patternResultSet=null;
    	
    	String locationQuery = AnatomyQueries.PATTERN_LOCATIONS;
    	String annotationQuery = AnatomyQueries.EXPRESSION_PATTERNS;
    	String componentQuery = AnatomyQueries.COMPONENT_NAME_FROM_ATN_PUBLIC_ID;
	   
        if (oid == null) {
			return null;
        }
                        
       try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (!result.first() || result.getInt(1) < 1) {
				return null;
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
       
       queryString=AnatomyQueries.ANNOTATED_LIST_BY_OID;
       try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			
			result.last();
            //need to count no of rows so size of Annotated component array can be determined
            int arraySize = result.getRow();
            expressionDetailModel = new ExpressionDetailModel [arraySize];
            
            //go back to start of result set so you can obtain relevant info for each row
            result.beforeFirst();
            int index = 0;
            String str = null;
            //String imgPath = "/images/tree/";
            
            while(result.next()){
                
                //String imgPath = "/images/tree/";
                
                expressionDetailModel[index] = new ExpressionDetailModel();
                
                //set values in the AnnotatedComponent object for specified element in array
                expressionDetailModel[index].setComponentId(result.getString(2));
                expressionDetailModel[index].setComponentName(result.getString(3));
                expressionDetailModel[index].setPrimaryStrength(result.getString(4));
                expressionDetailModel[index].setSecondaryStrength(result.getString(5));
                expressionDetailModel[index].setNoteExists(result.getBoolean(6));
                                   
                str = Utils.netTrim(result.getString(4));
                
			    if (null != str) {
					if(str.trim().equalsIgnoreCase("present")){
					    str = Utils.netTrim(result.getString(5));
					    if(str == null){
						expressionDetailModel[index].setExpressionImage("DetectedRoundPlus20x20.gif");
					    } else if(str.equals("strong")){
						expressionDetailModel[index].setExpressionImage("StrongRoundPlus20x20.gif");
					    } else if(str.equals("moderate")){
						expressionDetailModel[index].setExpressionImage("ModerateRoundPlus20x20.gif");
					    } else if(str.equals("weak")){
						expressionDetailModel[index].setExpressionImage("WeakRoundPlus20x20.gif");
					    }
					} else if(str.equalsIgnoreCase("not detected")){
					    expressionDetailModel[index].setExpressionImage("NotDetectedRoundMinus20x20.gif");
					} else if(str.equalsIgnoreCase("uncertain") || str.equalsIgnoreCase("possible")){
					    expressionDetailModel[index].setExpressionImage("PossibleRound20x20.gif");
					}
	            }
	                    
                //String annotationQuery = IshSubmissionQueries.EXPRESSION_PATTERNS;
                try
        		{
        			patternConnection = ds.getConnection();
        			patternStatement = patternConnection.prepareStatement(annotationQuery); 
        			patternStatement.setString(1, result.getString(1));
        			patternResultSet=  patternStatement.executeQuery();
        			
        			if(patternResultSet.first()){
                        
                        //need to count no of rows so size of ExpressionPattern array can be determined
        				patternResultSet.last();                        
                        arraySize = patternResultSet.getRow();
                        ExpressionPatternModel [] patterns = new ExpressionPatternModel [arraySize];
                        
                        //go back to start of expressionDetailModel set so you can obtain relevant info for each row
                        patternResultSet.beforeFirst();
                        int index2 = 0;

                        while(patternResultSet.next()){
                            
                            patterns[index2] = new ExpressionPatternModel();
                            
                            //set pattern value in ExpressionPattern object for specified element in array
                            str = Utils.netTrim(patternResultSet.getString(2));
                            patterns[index2].setPattern(str);
                            if (null != str) {
                                if(str.indexOf("homogeneous") >= 0){
                                    patterns[index2].setPatternImage("HomogeneousRound20x20.png");
                                }
                                else if(str.indexOf("spotted") >= 0){
                                    patterns[index2].setPatternImage("SpottedRound20x20.png");
                                }
                                else if(str.indexOf("regional") >= 0){
                                    patterns[index2].setPatternImage("RegionalRound20x20.png");
                                }
                                else if(str.indexOf("graded") >= 0){
                                    patterns[index2].setPatternImage("GradedRound20x20.png");
                                }
                                else if(str.indexOf("ubiquitous") >= 0) {
                                    patterns[index2].setPatternImage("UbiquitousRound20x20.png");
                                }
                                else if(str.indexOf("other") >= 0) {
                                    patterns[index2].setPatternImage("OtherRound20x20.png");
                                }
                                else if(str.indexOf("single cell") >= 0) {
                                    patterns[index2].setPatternImage("SingleCellRound20x20.png");
                                }
                                else if(str.indexOf("restricted") >= 0) {
                                    patterns[index2].setPatternImage("RestrictedRound20x20.png");
                                }
                            }
                            
                            //String locationQuery = IshSubmissionQueries.PATTERN_LOCATIONS;
                            StringBuffer locations = null;
                            try
                            {
                    			locationConnection = ds.getConnection();
                    			locationStatement = con.prepareStatement(locationQuery); 
                    			locationStatement.setString(1, patternResultSet.getString(1));
                    			locationResultSet =  locationStatement.executeQuery();
                    			
                    			if(locationResultSet.first()){
                    				locationResultSet.beforeFirst();
                                    
                                    //all locations will be stored in a String (comma separated)
                                    locations = new StringBuffer("");
        							String adjacentTxt = "adjacent to ";
        							String atnPubIdVal = null;
                                    String anatIdPrefix = Utils.getConfigBundle().getString("anatomy_id_prefix");
                                    
                                    while(locationResultSet.next()){
                                     
                                        if(locationResultSet.isFirst()){
                                            locations.append("- ");
                                        }
                                        
                                        
                                        //if the location string begins with 'adjacent to ', further query is required
                                        str = Utils.netTrim(locationResultSet.getString(1));
                                        if(null != str && str.indexOf(adjacentTxt) >= 0) {
                                        
                                            //the components public id is a substring of the location string 
                                            atnPubIdVal = str.substring(adjacentTxt.length());
                                            
                                            /*need to get the anatomy prefix and attach it to the id obtained previously
                                            then query the database to get the mane of the component*/
                                            //String componentQuery = IshSubmissionQueries.COMPONENT_NAME_FROM_ATN_PUBLIC_ID;
                                            try
                                    		{
                                    			componentConnection = ds.getConnection();
                                    			componentStatement = con.prepareStatement(componentQuery); 
                                    			componentStatement.setString(1, anatIdPrefix+atnPubIdVal);
                                    			componentResultSet =  componentStatement.executeQuery();
                                    			if(componentResultSet.first()){
                                                    locations.append(adjacentTxt+ componentResultSet.getString(1));
                                                }
                                    		}
                                    		catch(SQLException sqle){sqle.printStackTrace();}
                                    		finally {
                                    		    Globals.closeQuietly(componentConnection, componentStatement, componentResultSet);
                                    		}                            
                                        }
                                        else {
                                            locations.append(locationResultSet.getString(1));
                                        }
                                        
                                        if(!locationResultSet.isLast()){
                                            locations.append(", ");
                                        }
                                    }
                                }
                    		}
                    		catch(SQLException sqle){sqle.printStackTrace();}
                    		finally {
                    		    Globals.closeQuietly(locationConnection, locationStatement, locationResultSet);
                    		}
                             //if location values exist for this pattern
                            
                            if(locations != null){
                                patterns[index2].setLocations(locations.toString());
                            }                             
                            index2++;
                        }
                        //set the location value in the ExpressionPattern object for the specified element in the array
                        expressionDetailModel[index].setPattern(patterns);
                    }
        		}
        		catch(SQLException sqle){sqle.printStackTrace();}
        		finally {
        		    Globals.closeQuietly(patternConnection, patternStatement, patternResultSet);
        		}
                
                
                //set values in the AnnotatedComponent object for nerve density                    
                expressionDetailModel[index].setDensityRelativeToTotal(result.getString(8));
                expressionDetailModel[index].setDensityComponentId(result.getString(9));
                expressionDetailModel[index].setDensityDirectionalChange(result.getString(10));
                expressionDetailModel[index].setDensityMagnitudeChange(result.getString(11));
                
                str = Utils.netTrim(result.getString(8));                    
			    if (null != str) {
					if(str.trim().equalsIgnoreCase("high")){
						expressionDetailModel[index].setDensityImageRelativeToTotal("max_high.gif");
					} else if(str.equalsIgnoreCase("medium")){
					    expressionDetailModel[index].setDensityImageRelativeToTotal("mod_medium.gif");
					} else if(str.equalsIgnoreCase("low")){
					    expressionDetailModel[index].setDensityImageRelativeToTotal("min_low.gif");
					}
	            }
                
			    str = Utils.netTrim(result.getString(10));
			    if (null != str) {
					if(str.trim().equalsIgnoreCase("increased")){
					    str = Utils.netTrim(result.getString(11));
					    if(str.equalsIgnoreCase("small")){
					    	expressionDetailModel[index].setDensityImageRelativeToAge("inc_small.gif");
					    } else if(str.equalsIgnoreCase("large")){
					    	expressionDetailModel[index].setDensityImageRelativeToAge("inc_large.gif");
					    }
					} else if(str.equalsIgnoreCase("decreased")){
					    str = Utils.netTrim(result.getString(11));
					    if(str.equalsIgnoreCase("small")){
					    	expressionDetailModel[index].setDensityImageRelativeToAge("dec_small.gif");
					    } else if(str.equalsIgnoreCase("large")){
					    	expressionDetailModel[index].setDensityImageRelativeToAge("dec_large.gif");
					    }
					}
	            }
                
                index++;
            }            
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
       
       return expressionDetailModel;
    }//end  findAnnotatedListBySubmissionIds
    
    public ExpressionDetailModel findExpressionDetailBySubmissionIdAndComponentId(String oid,
            String componentId) {
		ExpressionDetailModel expressionDetailModel = null;
		String str = null;
		String [] components = null;
		ArrayList<String> componentList = null;
		String queryString=AnatomyQueries.COMPONENT_EXPRESSION_DETAIL;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			ps.setString(2, componentId);
			result =  ps.executeQuery();
			if (result.first()){
				expressionDetailModel=new ExpressionDetailModel();
				expressionDetailModel.setComponentId(result.getString(1));
				expressionDetailModel.setComponentName(result.getString(2));
			    str = Utils.netTrim(result.getString(3));
			    if (null == str) {
			    	expressionDetailModel.setComponentDescription(null);
			    } else {
				
					components = str.split("\\.");
					componentList = Utils.reformatComponentFullPath(components);
					expressionDetailModel.setComponentDescription(componentList);
			    }
			    expressionDetailModel.setPrimaryStrength(result.getString(4));
			    expressionDetailModel.setSecondaryStrength(result.getString(5));
			    expressionDetailModel.setExpressionId(result.getInt(6));
			    expressionDetailModel.setStage("TS" + result.getString(7));
			    expressionDetailModel.setSubmissionDbStatus(result.getInt(9));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		if (expressionDetailModel == null) {
			return null;
		}
		queryString = AnatomyQueries.COMPONENT_EXPRESSION_NOTE;
		String expressionNote = null;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			ps.setString(2, componentId);
			result =  ps.executeQuery();
			if (result.first()){
				expressionNote = Utils.formatResultSetToString(result);
				expressionDetailModel.setExpressionNote(expressionNote);
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		
		/////////////////////////
		
		queryString = AnatomyQueries.COMPONENT_DENSITY_NOTE;
		String densityNote=null;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			ps.setString(2, componentId);
			result =  ps.executeQuery();
			if (result.first()){
				densityNote = Utils.formatResultSetToString(result);
				expressionDetailModel.setDensityNote(densityNote);
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		queryString = AnatomyQueries.COMPONENT_DENSITY_DETAIL;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			ps.setString(2, componentId);
			result =  ps.executeQuery();
			if (result.first()){
				
				expressionDetailModel.setDensityRelativeToTotal(result.getString(1));
				expressionDetailModel.setDensityDirectionalChange(result.getString(2));
				expressionDetailModel.setDensityMagnitudeChange(result.getString(3));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		
		return expressionDetailModel;
		
		
		}
    
    public ExpressionPatternModel[] findComponentPatternsAndLocations(String expression_oid) {
    	ExpressionPatternModel[] expressionPatternModel=null;
    	Connection locationConnection = null;
        PreparedStatement locationStatement = null;
        ResultSet locationResultSet = null;
        Connection componentConnection = null;
        PreparedStatement componentStatement = null;
        ResultSet componentResultSet = null;
    	
    	if (expression_oid == null) {
		    //            throw new NullPointerException("id parameter or componentId parameter");
		    return null;
        }
    	
    	String queryString = AnatomyQueries.EXPRESSION_PATTERNS;
    	String locationQuery = AnatomyQueries.PATTERN_LOCATIONS;
    	String componentQuery = AnatomyQueries.COMPONENT_NAME_FROM_ATN_PUBLIC_ID;
    	
    	try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, expression_oid);
			result =  ps.executeQuery();
			if (result.first()){
				
				//need to get number of rows to determine size of array
				result.last();
                int numPatterns = result.getRow();
		
                //set size of patterns array based on num rows
                expressionPatternModel = new ExpressionPatternModel[numPatterns];
		
                //return the result set to the start
                result.beforeFirst();
		
                //integer to put data into the correct element in the pattern array object
                int index = 0;
		
                //need to fill each item in array with correct data
                while (result.next()) {
		    
                	expressionPatternModel[index] = new ExpressionPatternModel();
                	expressionPatternModel[index].setPattern(result.getString(2));
                	
                	try
            		{
            			locationConnection = ds.getConnection();
            			locationStatement = locationConnection.prepareStatement(locationQuery); 
            			locationStatement.setString(1, result.getString(1));
            			locationResultSet =  locationStatement.executeQuery();
            			
            			StringBuffer locations = null;
    				    String str = null;
    				    String atnPubIdVal = null;
    				    String anatIdPrefix = Utils.getConfigBundle().getString("anatomy_id_prefix");
            			
    				    if (locationResultSet.first()){
                            locations = new StringBuffer("");
                            locationResultSet.beforeFirst();
    						String adjacentTxt = "adjacent to ";
                            while (locationResultSet.next()) {
                                //if the location string begins with 'adjacent to ', further query is required
                                str = Utils.netTrim(locationResultSet.getString(1));
                                if(null != str && str.indexOf(adjacentTxt) >= 0) {
                                    //the components public id is a substring of the location string 
                                    atnPubIdVal = str.substring(adjacentTxt.length());
    				
                                    /*need to get the anatomy prefix and attach it to the id obtained previously
    				  					then query the database to get the mane of the component*/
                                    try
                            		{
                            			componentConnection = ds.getConnection();
                            			componentStatement = componentConnection.prepareStatement(componentQuery); 
                            			componentStatement.setString(1,anatIdPrefix+atnPubIdVal);
                            			componentResultSet =  componentStatement.executeQuery();
                            			if (componentResultSet.first()){
                            				
                            				locations.append(adjacentTxt+ componentResultSet.getString(1));
                            	        }
                            		}
                            		catch(SQLException sqle){sqle.printStackTrace();}
                            		finally {
                            		    Globals.closeQuietly(componentConnection, componentStatement, componentResultSet);
                            		}
                                   
                                }
                                else {
                                    locations.append(locationResultSet.getString(1));
                                }
    			    
                                if(!locationResultSet.isLast()){
                                    locations.append(", ");
                                }
                            }
                            if(locations != null){
                            	expressionPatternModel[index].setLocations(locations.toString());
                            }
                        }

            		}
            		catch(SQLException sqle){sqle.printStackTrace();}
            		finally {
            		    Globals.closeQuietly(locationConnection, locationStatement, locationResultSet);
            		}
                	
                    index++;
                }
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return expressionPatternModel;
    	
    }
    
    public ArrayList<Object> findComponentDetailByComponentId(String componentId) {
        ArrayList<Object> componentDetail = null;
        String queryString=AnatomyQueries.COMPONENT_DETAIL_FROM_ATN_PUBLIC_ID;
        String [] components = null;
		ArrayList<String> componentList = null;
		String str = null;
        
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, componentId);
			result =  ps.executeQuery();
			if (result.first()) {
				componentDetail = new ArrayList<Object>();
                componentDetail.add(result.getString(1));
                componentDetail.add(result.getString(2));
                str = Utils.netTrim(result.getString(3));
				if (null != str) {
		                components = str.split("\\.");
		                componentList = Utils.reformatComponentFullPath(components);
		                componentDetail.add(componentList);
				}
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
               
       return componentDetail;
       
    }
    
    public boolean hasParentNode(String componentId, String stageName,
            String oid) {
    	
    	String queryString=AnatomyQueries.PARENT_NODES;
        boolean hasParents=false;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, componentId);
			ps.setString(2, stageName);
			ps.setString(3, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				hasParents=true;
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return hasParents;
    }
    
    public boolean hasChildenNode(String componentId, String stageName,
            String oid) {
    	String queryString=AnatomyQueries.CHILD_NODES;
        boolean hasChildren=false;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, componentId);
			ps.setString(2, stageName);
			ps.setString(3, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				hasChildren=true;
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return hasChildren;
		
	}
    
    public String [] getTimedComponentIdsFromInput(String input) {
    	String queryString = AnatomyQueries.COMPONENT_IDS_BY_NAME;
    	String [] componentSet=null;
    	try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, input);
			ps.setString(2, input);
			ps.setString(3, input);
			ps.setString(4, input);
			result =  ps.executeQuery();
			if (result.first()) {
				result.last();
				componentSet = new String [result.getRow()];
				result.beforeFirst();
				int index = 0;
				while(result.next()){
				    componentSet[index] = result.getString(1);
				    index++;
				}
            }						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	return componentSet;
		
    }
    
    public String [] getTransitiveRelations(String [] input, String relationType){
    	
    	//string to contain sql
    	String queryString;
    	String[] componentSet=null;
    	//check whether you want to find ancestor or desendent - sql dependent on this clause
    	if(relationType.equalsIgnoreCase("ancestor")) { 
    		queryString = AnatomyQueries.getComponentRelations(input.length, "ANCES_ATN.ATN_PUBLIC_ID", "DESCEND_ATN.ATN_PUBLIC_ID");
    	}
    	//must be descendent
    	else {
    		queryString = AnatomyQueries.getComponentRelations(input.length, "DESCEND_ATN.ATN_PUBLIC_ID","ANCES_ATN.ATN_PUBLIC_ID");
    	}
    	
    	try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			for(int i=0;i<input.length;i++){
		    	ps.setString(i+1, input[i]);
		    }
			result =  ps.executeQuery();
			if(result.first()){
				result.last();
				int i = result.getRow();
				result.beforeFirst();
				componentSet = new String[i];
				i = 0;
				//add each result row to array
				while(result.next()) {
					componentSet[i] = result.getString(1);
				    i++;
				}
		    }						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return componentSet;
	
    }
   

}
