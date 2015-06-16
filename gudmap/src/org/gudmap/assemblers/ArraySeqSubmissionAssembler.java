package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.AnatomyDao;
import org.gudmap.dao.ArraySeqDao;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.SupplementaryFiles;
import org.gudmap.models.submission.AlleleModel;
import org.gudmap.models.submission.ArraySubmissionModel;
import org.gudmap.models.submission.PersonModel;
import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.utils.Utils;

public class ArraySeqSubmissionAssembler {
	
	private IshSubmissionDao ishSubmissionDao=null;
	private AnatomyDao anatomyDao = null;
	private ArraySeqDao arraySeqDao = null;
	private ArraySubmissionModel arraySubmissionModel = null;
	private SubmissionModel submissionModel=null;
	private int oid_int=0;
    
	public ArraySeqSubmissionAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		anatomyDao = new AnatomyDao();
		arraySeqDao = new ArraySeqDao();
	}
	
	public ArraySubmissionModel getData(String oid) {
		oid_int=Integer.parseInt(oid);
		//this cast results in a runtime error
		//arraySubmissionModel = (ArraySubmissionModel) ishSubmissionDao.findSubmissionById(oid);
		submissionModel = ishSubmissionDao.findSubmissionById(oid);
		if(submissionModel!=null) {
			arraySubmissionModel=new ArraySubmissionModel();
			arraySubmissionModel.setAccID(submissionModel.getAccID());
			arraySubmissionModel.setStage(submissionModel.getStage());
			arraySubmissionModel.setArchiveId(submissionModel.getArchiveId());
			arraySubmissionModel.setBatchId(submissionModel.getBatchId());
			arraySubmissionModel.setResultNotes(submissionModel.getResultNotes());
			arraySubmissionModel.setPublicFlag(submissionModel.getPublicFlag());
			arraySubmissionModel.setDeletedFlag(submissionModel.getDeletedFlag());
		
			//supplementary files
			arraySubmissionModel.setSupplementaryFiles(arraySeqDao.findSupplementaryFileInfoBySubmissionId(oid_int));
			
			arraySubmissionModel.setSampleModel(arraySeqDao.getSampleData(oid_int));
			arraySubmissionModel.setSeriesModel(arraySeqDao.getSeriesData(oid_int));
			arraySubmissionModel.setPlatformModel(arraySeqDao.getPlatformData(oid_int));
			
			arraySubmissionModel.setPrincipalInvestigators(ishSubmissionDao.findPIsBySubmissionId(oid));
			arraySubmissionModel.setSubmitter(ishSubmissionDao.findSubmitterBySubmissionId(oid));
			arraySubmissionModel.setAuthors(ishSubmissionDao.findAuthorBySubmissionId(oid));
			arraySubmissionModel.setAlleleModel(ishSubmissionDao.findAlleleBySubmissionId(oid));
			
			arraySubmissionModel.setLinkedSubmissions(Utils.formatLinkedSubmissionData(ishSubmissionDao.findLinkedSubmissionBySubmissionId(oid)));
			arraySubmissionModel.setLinkedPublications(ishSubmissionDao.findPublicationBySubmissionId(oid));
			arraySubmissionModel.setAcknowledgements(ishSubmissionDao.findAcknowledgementBySubmissionId(oid));
			
			arraySubmissionModel.setOriginalImages(arraySeqDao.getMicroarrayImages(oid_int));
			arraySubmissionModel.setTissue(arraySeqDao.getTissue(oid_int));
			
			//arraySubmissionModel.getSupplementaryFiles().
		}
		
		return arraySubmissionModel;
	}

}
