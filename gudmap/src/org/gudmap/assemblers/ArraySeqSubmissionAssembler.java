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
import org.gudmap.utils.Utils;

public class ArraySeqSubmissionAssembler {
	
	private IshSubmissionDao ishSubmissionDao=null;
	private AnatomyDao anatomyDao = null;
	private ArraySeqDao arraySeqDao = null;
	private ArraySubmissionModel arraySubmissionModel = null;
    
	public ArraySeqSubmissionAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		anatomyDao = new AnatomyDao();
		arraySeqDao = new ArraySeqDao();
	}
	
	public ArraySubmissionModel getData(String oid) {
		
		arraySubmissionModel = (ArraySubmissionModel)ishSubmissionDao.findSubmissionById(oid);
		//supplementary files
		arraySubmissionModel.setSupplementaryFiles(arraySeqDao.findSupplementaryFileInfoBySubmissionId(oid));
		
		arraySubmissionModel.setSampleModel(arraySeqDao.getSampleData());
		arraySubmissionModel.setSeriesModel(arraySeqDao.getSeriesData());
		arraySubmissionModel.setPlatformModel(arraySeqDao.getPlatformData());
		
		arraySubmissionModel.setPrincipalInvestigators(ishSubmissionDao.findPIsBySubmissionId(oid));
		arraySubmissionModel.setSubmitter(ishSubmissionDao.findSubmitterBySubmissionId(oid));
		arraySubmissionModel.setAuthors(ishSubmissionDao.findAuthorBySubmissionId(oid));
		arraySubmissionModel.setAlleleModel(ishSubmissionDao.findAlleleBySubmissionId(oid));
		
		arraySubmissionModel.setLinkedSubmissions(Utils.formatLinkedSubmissionData(ishSubmissionDao.findLinkedSubmissionBySubmissionId(oid)));
		arraySubmissionModel.setLinkedPublications(ishSubmissionDao.findPublicationBySubmissionId(oid));
		arraySubmissionModel.setAcknowledgements(ishSubmissionDao.findAcknowledgementBySubmissionId(oid));
		
		return arraySubmissionModel;
	}

}
