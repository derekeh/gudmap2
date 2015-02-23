package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.ArrayDao;
import org.gudmap.dao.GeneDetailsDao;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.submission.GeneModel;

public class GeneDetailsBeanAssembler {
	
	private GeneModel geneModel=null;
	private String geneSymbol;
	private GeneDetailsDao geneDetailsDao;
	private ArrayDao arrayDao;
	private IshSubmissionDao ishSubmissionDao;
	
	public GeneDetailsBeanAssembler() {
		
	}
	
	public GeneDetailsBeanAssembler(String geneSymbol) {
		this.geneSymbol = geneSymbol;
		geneDetailsDao = new GeneDetailsDao();
		arrayDao = new ArrayDao();
		ishSubmissionDao = new IshSubmissionDao();
	}
	
	public GeneModel getGeneModel(){
		if (geneSymbol == null || geneSymbol.equals("")) {
			return null;
		}
		String alternateSymbol = null;
		
			 geneModel = geneDetailsDao.findGeneInfoBySymbol(geneSymbol);
			
			// might not find the gene
			if (geneModel == null) {
				ArrayList<String> synonymsAndSymbol = new ArrayList<String>();
				synonymsAndSymbol.add(geneSymbol);
				alternateSymbol = geneDetailsDao.findSymbolBySynonym(geneSymbol);
				if (alternateSymbol != null && !alternateSymbol.equals("")) {
					synonymsAndSymbol.add(alternateSymbol);
				}
								
				geneModel = arrayDao.findGeneInfoBySymbol(synonymsAndSymbol);
				if(geneModel == null){
			    	return null;
				} 
				else {
					geneModel = geneDetailsDao.findFurtherGeneInfoForMicroarray(geneModel);
			    	// should use alternate symbol to make query
			    	geneSymbol = alternateSymbol;
			    }
			}
	
			// get associated probe
			ArrayList<String[]> associatedProbe = geneDetailsDao.findRelatedMAProbeBySymbol(geneSymbol);
			
			//get related ish submissions
			ArrayList<String[]> relatedSubmissionISH = ishSubmissionDao.findRelatedSubmissionBySymbol(geneSymbol,"ISH");

			//get related ish submissions
			ArrayList<String[]> relatedSubmissionIHC = ishSubmissionDao.findRelatedSubmissionBySymbol(geneSymbol,"IHC");

			//get related ish submissions
			ArrayList<String[]> relatedSubmissionTG = ishSubmissionDao.findRelatedSubmissionBySymbol(geneSymbol,"TG");
			
			/** ---complete gene object---  */
			if (null != relatedSubmissionISH) {
				geneModel.setIshSubmissions(relatedSubmissionISH);
			}
			if (null != relatedSubmissionIHC) {
				geneModel.setIhcSubmissions(relatedSubmissionIHC);
			}
			if (null != relatedSubmissionTG) {
				geneModel.setTgSubmissions(relatedSubmissionTG);
			}
			if (null != associatedProbe) {
				geneModel.setAssocProbes(associatedProbe);
			}
			
			// get translational links info
			geneModel = geneDetailsDao.addGeneInfoIuphar(geneModel);
		
		
		return geneModel;
	}

}
