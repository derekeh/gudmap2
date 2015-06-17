package org.gudmap.assemblers;

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
//import org.gudmap.models.submission.StatusNoteModel;
import org.gudmap.models.submission.IshSubmissionModel;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.utils.Utils;
//import org.gudmap.queries.submission.*;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.dao.AnatomyDao;



public class IshSubmissionAssembler {
    
	//private boolean debug = false;
	IshSubmissionDao ishSubmissionDao=null;
	AnatomyDao anatomyDao = null;
    
	public IshSubmissionAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		anatomyDao = new AnatomyDao();
	}
	
	public IshSubmissionModel getData(String oid, /*boolean isEditor, */
			boolean displayAnnotationAsTree /*, UserBean userBean, boolean onlyRetrieveTree*/) {
		if (oid == null) {
			return null;			
		}
				
		SubmissionModel submissionModel = ishSubmissionDao.findSubmissionById(oid);
			
		if(submissionModel == null){
				return null;
		}
		//This is for the old tree. Don't need anymore	
		//ArrayList<String> annotationTree = null;
		String annotatedTreeExpressions = null;
		String annotatedTreePatterns = null;
		String annotatedTreeExpressionNotes = null;
		ExpressionDetailModel [] expressionDetailModel = null;
			
		if(displayAnnotationAsTree) {
				//This is for the old tree. Don't need anymore
				//annotationTree = anatomyDao.findAnnotationTreeBySubmissionId(oid);
				annotatedTreeExpressions = anatomyDao.findAnnotationTreeExpressions(oid);
				annotatedTreePatterns = anatomyDao.findAnnotationTreePatterns(oid);
				annotatedTreeExpressionNotes = anatomyDao.findAnnotationTreeExpressionNotes(oid);
		}
		else {
			expressionDetailModel = anatomyDao.findAnnotatedListBySubmissionIds(oid);
		}	
					
		String assayType = submissionModel.getAssayType();
		
		IshSubmissionModel ishSubmissionModel = new IshSubmissionModel();
		ishSubmissionModel.setOid(submissionModel.getOid());
		ishSubmissionModel.setAccID(submissionModel.getAccID());
		ishSubmissionModel.setPublicFlag(submissionModel.getPublicFlag());
		ishSubmissionModel.setDeletedFlag(submissionModel.getDeletedFlag());
		ishSubmissionModel.setStage(submissionModel.getStage());
		ishSubmissionModel.setAssayType(assayType);
		ishSubmissionModel.setArchiveId(submissionModel.getArchiveId());
		//This is for the old tree. Don't need anymore
		//ishSubmissionModel.setAnnotationTree(annotationTree);
		ishSubmissionModel.setExpressionDetailModel(expressionDetailModel);
		ishSubmissionModel.setLabId(submissionModel.getLabId());
		ishSubmissionModel.setProject(submissionModel.getProject());
		ishSubmissionModel.setEuregeneId(submissionModel.getEuregeneId());
		ishSubmissionModel.setResultNotes(submissionModel.getResultNotes());
		ishSubmissionModel.setAnnotationTreeExpressions(annotatedTreeExpressions);
		ishSubmissionModel.setAnnotationTreePatterns(annotatedTreePatterns);
		ishSubmissionModel.setAnnotationTreeExpressionNotes(annotatedTreeExpressionNotes);
			
		/* DONT USE THIS ANYMORE - FOR PREVIOUS EDITING 
		 * if(onlyRetrieveTree) {
			return ishSubmissionModel;
		}*/
				
		ProbeModel probeModel = null;
		AntibodyModel antibodyModel = null;
		if (assayType.indexOf("ISH") >= 0 || assayType.indexOf("TG") >= 0) {
			probeModel = ishSubmissionDao.findProbeBySubmissionId(oid,null,true);
		}
		else if (assayType.indexOf("IHC") >= 0) { // assay type is IHC
			antibodyModel = ishSubmissionDao.findAntibodyBySubmissionId(oid,true);
		}
		
		// get specimen info
		SpecimenModel specimenModel = ishSubmissionDao.findSpecimenBySubmissionId(oid);
		
		// get original image info
		ArrayList<ImageInfoModel> images = ishSubmissionDao.findImageBySubmissionId(oid);
		
		//get wlz image
		ImageDetailModel imageDetailModel = ishSubmissionDao.findWlzImageDetailBySubmissionId(oid);
		
		// get author info
		String author = ishSubmissionDao.findAuthorBySubmissionId(oid);
					
				// get pi info
		PersonModel[] pi = ishSubmissionDao.findPIsBySubmissionId(oid);
					
				// get submitter info
		PersonModel submitter = ishSubmissionDao.findSubmitterBySubmissionId(oid);
		
			
		// get allele info
		AlleleModel[] alleleModel = ishSubmissionDao.findAlleleBySubmissionId(oid);
		
		// get publication info
		ArrayList<String[]> publications = ishSubmissionDao.findPublicationBySubmissionId(oid);
		
		ArrayList<String[]> linkedSubmissionsRaw = ishSubmissionDao.findLinkedSubmissionBySubmissionId(oid);
		
		// format the linked submission raw data into appropriate data structure
		ArrayList<Object> linkedSubmission = Utils.formatLinkedSubmissionData(linkedSubmissionsRaw);
			
		// get acknowledgement 
		String[] acknowledgements = ishSubmissionDao.findAcknowledgementBySubmissionId(oid);
		
	    if (assayType.indexOf("ISH") >=0) {
	    		ishSubmissionModel.setProbeModel(probeModel);
	    } else if (assayType.indexOf("IHC") >=0) {
	    	ishSubmissionModel.setAntibodyModel(antibodyModel);
	    } else if (assayType.indexOf("TG") >=0) {
	    	ishSubmissionModel.setProbeModel(probeModel);
	    }
		
	    ishSubmissionModel.setSpecimenModel(specimenModel);
	    ishSubmissionModel.setOriginalImages(images);
	    ishSubmissionModel.setImageDetailModel(imageDetailModel);
	    ishSubmissionModel.setAuthors(author);
	    ishSubmissionModel.setPrincipalInvestigators(pi);
	    ishSubmissionModel.setSubmitter(submitter);	
	    ishSubmissionModel.setAlleleModel(alleleModel);
	    ishSubmissionModel.setLinkedPublications(publications);
	    ishSubmissionModel.setLinkedSubmissions(linkedSubmission);
	   	ishSubmissionModel.setAcknowledgements(acknowledgements);
	   
	    String tissue = ishSubmissionDao.findTissueBySubmissionId(oid);
	    ishSubmissionModel.setTissue(tissue);

	    return ishSubmissionModel;
	}
		
	//Moved to Utils
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
	/*public ArrayList<Object> formatLinkedSubmissionData(ArrayList<String[]> linkedSubmissionsRaw) {
    	
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
    }*/
	
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

