package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.submission.AntibodyModel;

public class AntibodyAssembler {
	
	private IshSubmissionDao ishSubmissionDao;
	public AntibodyAssembler(){
		ishSubmissionDao = new IshSubmissionDao();
	}
	
	public AntibodyModel getData(String antibodyId, boolean isSubOid) {
		if (antibodyId == null){
			return null;
		}

		AntibodyModel antibodyModel = ishSubmissionDao.findAntibodyBySubmissionId(antibodyId,isSubOid);
		if (antibodyModel.getGeneSymbol() != null){
			ArrayList<String[]> relatedSubmissionISH = ishSubmissionDao.findRelatedSubmissionBySymbol(antibodyModel.getGeneSymbol(),antibodyModel.getAssayType());		
			if (null != relatedSubmissionISH) {
				antibodyModel.setIshSubmissions(relatedSubmissionISH);
			}
		}
		return antibodyModel;
	}
}
