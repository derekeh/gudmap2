package org.gudmap.assemblers;

import org.gudmap.models.submission.PersonModel;
import org.gudmap.dao.IshSubmissionDao;

public class PersonAssembler {
	
	private IshSubmissionDao ishSubmissionDao;
	
	public PersonAssembler() {
		ishSubmissionDao = new IshSubmissionDao();		
	}
	
	public PersonModel getPersonById(String personId) {
	
		if (personId == null) {
			return null;
		}
		
		PersonModel person = ishSubmissionDao.findPersonByPersonId(personId);
		return person;
		
	}	

	public PersonModel[] getPeopleBySubmissionId(String submissionId) {
		
		if (submissionId == null) {
			return null;
		}
		
		PersonModel[] pis = ishSubmissionDao.findPIsBySubmissionId(submissionId);
		return pis;
		
	}
	
/*	public StatusNoteModel[] getStatusNotes(String oid) {
		
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
	}*/

}
