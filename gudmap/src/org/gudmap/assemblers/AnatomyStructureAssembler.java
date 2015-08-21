package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.gudmap.dao.AnatomyDao;
import org.gudmap.globals.Globals;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.utils.Utils;

public class AnatomyStructureAssembler {
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	AnatomyDao anatomyDao = null;


    public AnatomyStructureAssembler() {
    	anatomyDao = new AnatomyDao();
    }
    
	public ArrayList getStageRanges() {
			ArrayList stageRanges = null;
		 try
			{
				//con = ds.getConnection();
				con=Globals.getDatasource().getConnection();
				ps = con.prepareStatement(AnatomyQueries.ANNOTATED_STAGE_RANGE); 
				result =  ps.executeQuery();
				if(result.first()){
					result.beforeFirst();
					stageRanges = Utils.formatResultSetToArrayList(result);
				}
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		
			return stageRanges;

		
	}
	
	/*public String getComponentNameFromId(String id){
		if (debug)
		    System.out.println("AnatomyStructureAssembler.getComponentNameFromId");
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			String componentName = anatomyDAO.getAnatomyTermFromPublicId(id);
			return componentName;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getComponentNameFromId failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	public String getOntologyTerms() {
		if (debug)
		    System.out.println("AnatomyStructureAssembler.getOntologyTerms");
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			String ontologyTerms = anatomyDAO.getOnlogyTerms();
			return ontologyTerms;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getOntologyTerms failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	*/
	
	public boolean stageRangesAreValid(String startStage, String endStage) {
		if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return false;
        }
		boolean isValid = true;
		int startStageSequence = anatomyDao.findSequencebyStageName(startStage);
		int endStageSequence = anatomyDao.findSequencebyStageName(endStage);

			if (startStageSequence == -1 || endStageSequence == -1 || startStageSequence > endStageSequence) {
				isValid = false;
			}

			return isValid;
		
	}
	
	/**
	 * 
	 * @param startStage
	 * @param endStage
	 * @return
	 */
	public ArrayList buildTree(String startStage, String endStage, boolean isForBooleanQ) {
		if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
        	return null;
        }
        
		ArrayList anatomyTree = anatomyDao.getAnatomyTreeByStages(startStage, endStage, isForBooleanQ);
		return anatomyTree;
		
	}
	
	/**
	 * 
	 * @param components
	 * @param startStage
	 * @param endStage
	 * @param expressionState
	 * @param order
	 * @param offset
	 * @param num
	 * @return
	 */
/*	public ISHBrowseSubmission[] getISHBrowseSubmission(String[] components,
			String startStage, String endStage, String expressionState, 
			String[] order, String offset, String num) {
		if (debug)
		    System.out.println("AnatomyStructureAssembler.getISHBrowseSubmission");

        if (components == null || components.length == 0) {
//        	System.out.println("bad components!!!!!!!");
        	return null;
        }
        
		if (startStage == null || startStage.equals("") || endStage == null || endStage.equals("")) {
//			System.out.println("bad stage!!!!!!!");
//        	return null;
        }
		
		if (expressionState == null ||
				(!expressionState.equalsIgnoreCase("present") && !expressionState.equalsIgnoreCase("not detected"))) {
			return null;
		}

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHBrowseSubmission[] annotatedSubmissions;
			AnatomyDAO anatomyDAO = MySQLDAOFactory.getAnatomyDAO(conn);
			// get data
			ArrayList publicIds = anatomyDAO.findPublicIdByComponentIdAndStage(components, startStage, endStage);
			if (publicIds == null || publicIds.size() == 0) {
				return null;
			} else {
				annotatedSubmissions =
					anatomyDAO.getAnnotatedSubmissionByPublicIdAndStage(publicIds, startStage, endStage, expressionState,
							order, offset, num);
			}
			return annotatedSubmissions;
		}
		catch(Exception e){
			System.out.println("AnatomyStructureAssembler::getISHBrowseSubmission failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}*/

}
