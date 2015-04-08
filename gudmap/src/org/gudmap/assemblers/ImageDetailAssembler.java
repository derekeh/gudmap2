package org.gudmap.assemblers;

import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.submission.ImageDetailModel;
import org.gudmap.utils.Utils;

public class ImageDetailAssembler {
	
	IshSubmissionDao ishSubmissionDao;
	
	public ImageDetailAssembler() {
		ishSubmissionDao = new IshSubmissionDao();
		
	}
	
	public ImageDetailModel getData(String submissionAccessionId, String serialNum) {
		
		if (submissionAccessionId == null || submissionAccessionId.equals("")) {
			return null;
		}
		if (serialNum == null || serialNum.equals("")) {
			serialNum = "1";
		} else if (!Utils.isValidInteger(serialNum)) {
			serialNum = "1";
		}
		
		ImageDetailModel imageDetailModel = ishSubmissionDao.findImageDetailBySubmissionId(submissionAccessionId, Integer.parseInt(serialNum)-1);
		
		return imageDetailModel;
		
	}

}
