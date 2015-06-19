package org.gudmap.assemblers;


import org.gudmap.dao.ArraySeqDao;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.DataProcessing;
import org.gudmap.models.SupplementaryFiles;
import org.gudmap.models.submission.ArraySubmissionModel;
import org.gudmap.models.submission.SeqSubmissionModel;
import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.utils.Utils;

public class ArraySeqSubmissionAssembler {
	
	private IshSubmissionDao ishSubmissionDao=null;
	private ArraySeqDao arraySeqDao = null;
	private ArraySubmissionModel arraySubmissionModel = null;
	private SeqSubmissionModel seqSubmissionModel = null;
	private SubmissionModel submissionModel=null;
	private int oid_int=0;
    
	public ArraySeqSubmissionAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		arraySeqDao = new ArraySeqDao();
	}
	
	public ArraySubmissionModel getArrayData(String oid) {
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
			
			arraySubmissionModel.setOriginalImages(arraySeqDao.getArraySeqImages(oid_int,"Microarray"));
			arraySubmissionModel.setTissue(arraySeqDao.getTissue(oid_int));
			
			//arraySubmissionModel.getSupplementaryFiles().
		}
		
		return arraySubmissionModel;
	}
	
	public SeqSubmissionModel getSeqData(String oid) {
		oid_int=Integer.parseInt(oid);
		//this cast results in a runtime error
		//arraySubmissionModel = (ArraySubmissionModel) ishSubmissionDao.findSubmissionById(oid);
		submissionModel = ishSubmissionDao.findSubmissionById(oid);
		if(submissionModel!=null) {
			seqSubmissionModel=new SeqSubmissionModel();
			seqSubmissionModel.setAccID(submissionModel.getAccID());
			seqSubmissionModel.setStage(submissionModel.getStage());
			seqSubmissionModel.setArchiveId(submissionModel.getArchiveId());
			seqSubmissionModel.setBatchId(submissionModel.getBatchId());
			seqSubmissionModel.setResultNotes(submissionModel.getResultNotes());
			seqSubmissionModel.setPublicFlag(submissionModel.getPublicFlag());
			seqSubmissionModel.setDeletedFlag(submissionModel.getDeletedFlag());
		
			//supplementary files
			SupplementaryFiles supplementaryFiles=arraySeqDao.findSeqSupplementaryFiles(oid_int);
			seqSubmissionModel.setRawFile(supplementaryFiles.getRawFile());
			seqSubmissionModel.setProcessedFile(supplementaryFiles.getProcessedFile());
			
			seqSubmissionModel.setSeqSampleModel(arraySeqDao.getSeqSampleData(oid_int));
			seqSubmissionModel.setSeqSeriesModel(arraySeqDao.getSeqSeriesData(oid_int));
			seqSubmissionModel.setDataProcessing(arraySeqDao.getDataProcessing(oid_int));
			
			seqSubmissionModel.setPrincipalInvestigators(ishSubmissionDao.findPIsBySubmissionId(oid));
			seqSubmissionModel.setSubmitter(ishSubmissionDao.findSubmitterBySubmissionId(oid));
			seqSubmissionModel.setAuthors(ishSubmissionDao.findAuthorBySubmissionId(oid));
			seqSubmissionModel.setAlleleModel(ishSubmissionDao.findAlleleBySubmissionId(oid));
			
			seqSubmissionModel.setLinkedSubmissions(Utils.formatLinkedSubmissionData(ishSubmissionDao.findLinkedSubmissionBySubmissionId(oid)));
			seqSubmissionModel.setLinkedPublications(ishSubmissionDao.findPublicationBySubmissionId(oid));
			seqSubmissionModel.setAcknowledgements(ishSubmissionDao.findAcknowledgementBySubmissionId(oid));
			
			seqSubmissionModel.setOriginalImages(arraySeqDao.getArraySeqImages(oid_int,"NextGen"));
			
		}
		
		return seqSubmissionModel;
	}

}
