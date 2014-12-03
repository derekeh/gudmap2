package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.util.ArrayList;


/*import gmerg.beans.UserBean;
import gmerg.db.AnatomyDAO;
import gmerg.db.ArrayDAO;
import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;*/
import org.gudmap.models.submission.AntibodyModel;
import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.models.submission.PersonModel;
import org.gudmap.models.submission.ProbeModel;
import org.gudmap.models.submission.SpecimenModel;
import org.gudmap.models.submission.ImageDetailModel;
import org.gudmap.models.submission.AlleleModel;
import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.models.submission.StatusNoteModel;
import org.gudmap.models.submission.IshSubmissionModel;
import org.gudmap.queries.submission.*;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.dao.AnatomyDao;



public class IshSubmissionAssembler {
    
	private boolean debug = false;
	IshSubmissionDao ishSubmissionDao=null;
	AnatomyDao anatomyDao = null;
    
	public IshSubmissionAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		anatomyDao = new AnatomyDao();
	}
	
	public IshSubmissionModel getData(String oid, boolean isEditor, 
			boolean displayAnnotationAsTree, /*UserBean userBean,*/ boolean onlyRetrieveTree) {
		if (oid == null) {
			return null;			
		}
				
		SubmissionModel submissionModel = ishSubmissionDao.findSubmissionById(oid);
			
		if(submissionModel == null){
				return null;
		}
			
		ArrayList<String> annotationTree = null;
		ExpressionDetailModel [] expressionDetailModel = null;
			
		if(displayAnnotationAsTree) {
				annotationTree = anatomyDao.findAnnotationTreeBySubmissionId(oid);
		}
		else {
			expressionDetailModel = anatomyDao.findAnnotatedListBySubmissionIds(oid);
		}		
			
		String assayType = submissionModel.getAssayType();
		
		IshSubmissionModel ishSubmissionModel = new IshSubmissionModel();
		ishSubmissionModel.setAccID(submissionModel.getAccID());
		ishSubmissionModel.setPublicFlag(submissionModel.getPublicFlag());
		ishSubmissionModel.setDeletedFlag(submissionModel.getDeletedFlag());
		ishSubmissionModel.setStage(submissionModel.getStage());
		ishSubmissionModel.setAssayType(assayType);
		ishSubmissionModel.setArchiveId(submissionModel.getArchiveId());
		ishSubmissionModel.setAnnotationTree(annotationTree);
		ishSubmissionModel.setExpressionDetailModel(expressionDetailModel);
		ishSubmissionModel.setLabId(submissionModel.getLabId());
		ishSubmissionModel.setProject(submissionModel.getProject());
		ishSubmissionModel.setEuregeneId(submissionModel.getEuregeneId());
		ishSubmissionModel.setResultNotes(submissionModel.getResultNotes());
			
		if(onlyRetrieveTree) {
			return ishSubmissionModel;
		}
		
		ProbeModel probeModel = null;
		AntibodyModel antibodyModel = null;
		if (assayType.indexOf("ISH") >= 0 || assayType.indexOf("TG") >= 0) {
			probeModel = ishSubmissionDao.findProbeBySubmissionId(oid);
		}
		else if (assayType.indexOf("IHC") >= 0) { // assay type is IHC
			antibodyModel = ishSubmissionDao.findAntibodyBySubmissionId(oid);
		}
		/*	
			// get specimen info
		SpecimenModel specimenModel = ishDAO.findSpecimenBySubmissionId(oid);
	
			// get allel info
		AlleleModel[] alleleModel = ishDAO.findAlleleBySubmissionId(oid);
			
		// get original image info
		ArrayList images = ishDAO.findImageBySubmissionId(oid);
		//get wlz image
		ImageDetailModel imageDetailModel = ishDAO.findWlzImageDetailBySubmissionId(oid);
			
			// get author info
		String author = ishDAO.findAuthorBySubmissionId(oid);
			
			// get pi info
		PersonModel[] pi = ishDAO.findPIsBySubmissionId(oid);
			
			// get submitter info
		PersonModel submitter = ishDAO.findSubmitterBySubmissionId(oid);
			
			// get publication info
		ArrayList publication = ishDAO.findPublicationBySubmissionId(oid);
			
			// get acknowledgement 
		String[] acknowledgement = ishDAO.findAcknowledgementBySubmissionId(oid);
	
		ArrayList linkedSubmissionsRaw = ishDAO.findLinkedSubmissionBySubmissionId(oid);
			
			// format the linked submission raw data into appropriate data structure
		ArrayList linkedSubmission = formatLinkedSubmissionData(linkedSubmissionsRaw);
			
	    if (assayType.indexOf("ISH") >=0) {
	    		ishSubmissionModel.setProbeModel(probeModel);
	    } else if (assayType.indexOf("IHC") >=0) {
	    	ishSubmissionModel.setAntibodyModel(antibodyModel);
	    } else if (assayType.indexOf("TG") >=0) {
	    	ishSubmissionModel.setProbeModel(probeModel);
	    }
			
	    ishSubmissionModel.setSpecimenModel(specimenModel);
	    ishSubmissionModel.setAlleleModel(alleleModel);
	    ishSubmissionModel.setOriginalImages(images);
	    ishSubmissionModel.setImageDetailModel(imageDetailModel);
	    ishSubmissionModel.setAuthors(author);
	    ishSubmissionModel.setPrincipalInvestigators(pi);
	    ishSubmissionModel.setSubmitter(submitter);	
	    ishSubmissionModel.setLinkedPublications(publication);
	    ishSubmissionModel.setAcknowledgements(acknowledgement);
	    ishSubmissionModel.setLinkedSubmissions(linkedSubmission);
	    
	    String tissue = ishDAO.findTissueBySubmissionId(oid);
	    ishSubmissionModel.setTissue(tissue);*/

	    return ishSubmissionModel;
	}
		

	
	/**
	 * Linked Submissions(ArrayList)-- resource (e.g. GUDMAP)
	 *                              -- submissions(ArrayList)-- serial no
	 *                                                       -- link type (e.g. control) 
	 *                                                       -- oid(ArrayList)-- accId1, ..., accIdN
	 *                              -- URL
	 * 
	 * @param linkedSubmissionsRaw
	 * @return an array list of LS, within every element of this array list, it's an 3-unit object array,
	 *         the first element is resource string, the second element is an array list of LS type and
	 *         accession ids, the third one is URL string;
	 *         The first element of the array list of LS type and accesion ids is the serial no,
	 *         the second element is the type string,
	 *         the third element is an array list of accession ids.
	 *         
	 */
	private ArrayList formatLinkedSubmissionDataBackup(ArrayList linkedSubmissionsRaw) {
    	
		if (linkedSubmissionsRaw == null || linkedSubmissionsRaw.isEmpty()) {
			return null;
		}
		
		int len = linkedSubmissionsRaw.size();

		// go through the linked submission list raw data and assemble them into desired data structure
		ArrayList<Object> result = new ArrayList<Object>();
		
        // linked submissions from one resource
		ArrayList<Object> linkedSubmission = null;
		
        // list of linked submission type and accession ids
		ArrayList<Object> typeAndAccessionIDsList = null;
		
		// linked submission type and accession ids
		ArrayList<Object> typeAndAccessionIDs = null;
		
		ArrayList<String> accessionIDs = null;
		String tempResource = null;
		String tempSubmissiontype = null;
//		int typeAndAccessionIDSerialNo = 1; // used for display purpose
		
		for (int i=0;i<len;i++) {
    		
			// get the data
			String resource = ((String[])linkedSubmissionsRaw.get(i))[0];
    		String submissionType = ((String[])linkedSubmissionsRaw.get(i))[1];
    		String oid = ((String[])linkedSubmissionsRaw.get(i))[2];
    		String url = ((String[])linkedSubmissionsRaw.get(i))[3];
    		
    		// assemble
    		if (i == 0) { // first row
    			linkedSubmission = new ArrayList<Object>();
        		linkedSubmission.add(0, resource);
        		linkedSubmission.add(1, url);
        		
        		tempResource = resource;
        		tempSubmissiontype = submissionType;
        		
        		typeAndAccessionIDsList = new ArrayList<Object>();
        		
        		typeAndAccessionIDs = new ArrayList<Object>();
        		typeAndAccessionIDs.add(0, Integer.toString(1));
        		typeAndAccessionIDs.add(1, submissionType);
        		
        		accessionIDs = new ArrayList<String>();
        		accessionIDs.add(oid);
        		
    		} else { // if it's not the first row, compare the resource, type, and assemble the LS accordingly
    			
    			if (resource.equals(tempResource)) {
    				if (submissionType.equals(tempSubmissiontype)) {
    					accessionIDs.add(oid);
    				} else {
    					typeAndAccessionIDs.add(2, accessionIDs);
    					typeAndAccessionIDsList.add(typeAndAccessionIDs);
    					
    					typeAndAccessionIDs = new ArrayList<Object>();
    					int serialNo = typeAndAccessionIDsList.size() + 1;
    	        		typeAndAccessionIDs.add(0, Integer.toString(serialNo));
    	        		typeAndAccessionIDs.add(1, submissionType);
    					tempSubmissiontype = submissionType;

    					accessionIDs = new ArrayList<String>();
    					accessionIDs.add(oid);
    				}
    				
    			} else { // not the same resource
    				
    				// add the accession id, type, into LS data structure (ArrayList)
					typeAndAccessionIDs.add(2, accessionIDs);
					typeAndAccessionIDsList.add(typeAndAccessionIDs);
					linkedSubmission.add(1, typeAndAccessionIDsList);
    				
					// convert the data structure for display
					Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    				
        			linkedSubmission = new ArrayList<Object>();
            		linkedSubmission.add(0, resource);
            		linkedSubmission.add(1, url);
            		
            		tempResource = resource;
            		tempSubmissiontype = submissionType;
            		
            		typeAndAccessionIDsList = new ArrayList<Object>();
            		
            		typeAndAccessionIDs = new ArrayList<Object>();
	        		typeAndAccessionIDs.add(0, Integer.toString(1));
	        		typeAndAccessionIDs.add(1, submissionType);
            		
            		accessionIDs = new ArrayList<String>();
            		accessionIDs.add(oid);
    			}
    			
    			// put the last row of data into the result data structure
    			if (i == len-1) {
    				typeAndAccessionIDs.add(2, accessionIDs);
    				typeAndAccessionIDsList.add(typeAndAccessionIDs);
    				linkedSubmission.add(1, typeAndAccessionIDsList);
    				Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    			}
    		}
    		
		} // end of going through the linked submission list raw data
		return result;
    }
	
	/**
	 * Linked Submissions(ArrayList)-- resource (e.g. GUDMAP)
	 *                              -- submissions(ArrayList)-- oid
	 *                                                                  -- link type(ArrayList) 
	 *                              -- URL
	 * 
	 * @param linkedSubmissionsRaw
	 * @return an array list of LS, within every element of this array list, it's an 3-unit object array,
	 *         the first element is resource string, the second element is an array list of accession id and
	 *         link types, the third one is URL string;
	 *         The first element of the array list of accesion id and link types is the accession id,
	 *         the second element is an array list of link types.
	 *         
	 */
	public ArrayList formatLinkedSubmissionData(ArrayList linkedSubmissionsRaw) {
    	
		if (linkedSubmissionsRaw == null || linkedSubmissionsRaw.isEmpty()) {
			return null;
		}
		
		int len = linkedSubmissionsRaw.size();
//		System.out.println("len: " + len);

		// go through the linked submission list raw data and assemble them into desired data structure
		ArrayList<Object> result = new ArrayList<Object>();
		
        // linked submissions from one resource
		ArrayList<Object> linkedSubmission = null;
		
        // list of linked submission and accession id and their link types
		ArrayList<Object> accessionIDAndTypesList = null;
		
		// linked submission accession id and types
		ArrayList<Object> accessionIDAndTypes = null;
		
		ArrayList<String> linkTypes = null;
		String tempResource = null;
		String tempAccessionId = null;
		
		for (int i=0;i<len;i++) {
    		
			// get the data
			String resource = ((String[])linkedSubmissionsRaw.get(i))[0].trim();
//			System.out.println("resource len: " + resource.length());
    		String oid = ((String[])linkedSubmissionsRaw.get(i))[1].trim();
//    		System.out.println("accessionid: " + oid);
    		String linkType = ((String[])linkedSubmissionsRaw.get(i))[2].trim();
    		String url = ((String[])linkedSubmissionsRaw.get(i))[3].trim();
//    		System.out.println("this is no " + i);
    		
    		// assemble
    		if (i == 0) { // first row
    			linkedSubmission = new ArrayList<Object>();
        		linkedSubmission.add(0, resource);
        		linkedSubmission.add(1, url);
        		
        		tempResource = resource;
        		tempAccessionId = oid;
        		
        		accessionIDAndTypesList = new ArrayList<Object>();
        		
        		accessionIDAndTypes = new ArrayList<Object>();
        		accessionIDAndTypes.add(0, oid);
        		
        		linkTypes = new ArrayList<String>();
        		linkTypes.add(linkType);
        		
    		} else { // if it's not the first row, compare the resource, accession id, and assemble the link type accordingly
    			
    			if (resource.equals(tempResource)) {
    				if (oid.equals(tempAccessionId)) {
    					linkTypes.add(linkType);
    				} else {
    					accessionIDAndTypes.add(1, linkTypes);
    					accessionIDAndTypesList.add(accessionIDAndTypes);
    					
    					accessionIDAndTypes = new ArrayList<Object>();
    	        		accessionIDAndTypes.add(0, oid);
    	        		tempAccessionId = oid;

    					linkTypes = new ArrayList<String>();
    					linkTypes.add(linkType);
    				}
    				
    			} else { // not the same resource
    				
    				// add the type, accession id, into LS data structure (ArrayList)
					accessionIDAndTypes.add(1, linkTypes);
					accessionIDAndTypesList.add(accessionIDAndTypes);
					linkedSubmission.add(1, accessionIDAndTypesList);
    				
					// convert the data structure for display
					Object[] linkedSubmissionObj = 
    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
    				result.add(linkedSubmissionObj);
    				
        			linkedSubmission = new ArrayList<Object>();
            		linkedSubmission.add(0, resource);
            		linkedSubmission.add(1, url);
            		
            		tempResource = resource;
            		tempAccessionId = oid;
            		
            		accessionIDAndTypesList = new ArrayList<Object>();
            		
            		accessionIDAndTypes = new ArrayList<Object>();
	        		accessionIDAndTypes.add(0, oid);
            		
            		linkTypes = new ArrayList<String>();
            		linkTypes.add(linkType);
    			}
    		}
			// put the last row of data into the result data structure
//			System.out.println("len: " + len);
			if (i == len-1) {
				accessionIDAndTypes.add(1, linkTypes);
				accessionIDAndTypesList.add(accessionIDAndTypes);
				linkedSubmission.add(1, accessionIDAndTypesList);
				Object[] linkedSubmissionObj = 
					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
				result.add(linkedSubmissionObj);
			}
    		
		} // end of going through the linked submission list raw data
		return result;
    }
	
/*	public PersonModel getPersonById(String personId) {
		
		if (personId == null) {
			return null;
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			PersonModel pi = ishDAO.findPersonById(personId);
			return pi;
		}
		catch(Exception e){
			System.out.println("ISHSubmissionAssembler::getPersonById !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}	

	
	public StatusNoteModel[] getStatusNotes(String oid) {
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			StatusNoteModel[] statusNotes = ishDAO.getStatusNotesBySubmissionId(oid);
			return statusNotes;
		}
		catch(Exception e){
			System.out.println("ISHSubmissionAssembler::getStatusNotes !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
	
	public PersonModel[] getPeopleBySubmissionId(String submissionId) {
		
		if (submissionId == null) {
			return null;
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			PersonModel[] pis = ishDAO.findPIsBySubmissionId(submissionId);
			return pis;
		}
		catch(Exception e){
			System.out.println("ISHSubmissionAssembler::getStatusNotes !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}*/
}

