package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.submission.ProbeModel;

public class MaProbeAssembler {
	
	private IshSubmissionDao ishSubmissionDao;
	public MaProbeAssembler(){
		ishSubmissionDao = new IshSubmissionDao();
	}
	
	public ProbeModel getData(String probeId, String maprobeId) {
		if ((probeId == null || probeId.equals("")) && (maprobeId == null || maprobeId.equals(""))) {
			return null;
		}

		ProbeModel probeModel = null;

		probeModel = ishSubmissionDao.findProbeBySubmissionId(probeId, maprobeId, false);
	
			if (probeModel == null)
			    System.out.println("!!!!possible error: probeId="+probeId+" maprobeId="+maprobeId+" does not have a probe in DB");
			else 
			{
			    if (probeModel.getSeqStatus() != null && !probeModel.getSeqStatus().equalsIgnoreCase("Unsequenced.")) {
					if (probeModel.getSeq5Loc().equals("n/a") || probeModel.getSeq3Loc().equals("n/a")) {
					    probeModel.setSeqInfo("Accession number for part sequence: ");
					} 
					else {
					    probeModel.setSeqInfo("Probe sequence spans from "
							     + probeModel.getSeq5Loc() + " to " + probeModel.getSeq3Loc()
							     + " of");
					}
			    }
			    
			    if (probeModel.getGeneSymbol() != null){
			    	//TODO
					ArrayList<String[]> relatedSubmissionISH = ishSubmissionDao.findRelatedSubmissionBySymbol(probeModel.getGeneSymbol(),probeModel.getAssayType());	
					if (null != relatedSubmissionISH) {
					    probeModel.setIshSubmissions(relatedSubmissionISH);
					}
			    }
			}
		return probeModel;
	}
}
