package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.models.GeneStripModel;
import org.gudmap.dao.GeneStripDao;

public class GeneStripBeanAssembler {
	
	private GeneStripDao geneStripDao;	
	private ArrayList<String> geneIds;
	
	public GeneStripBeanAssembler() {
		geneStripDao = new GeneStripDao();
	}
	
	public ArrayList<GeneStripModel> getData(String gene, String input, String wildcard) {
		ArrayList<GeneStripModel> geneStripList = null;  
		
   		//ArrayList<String> geneIds = geneStripDao.getSymbolsFromGeneInput(input, wildcard);
   		geneIds = geneStripDao.getSymbolsFromGeneInput(input, wildcard);
   		if(geneIds==null  || geneIds.size() <1)
   			return null;
   		else
   			geneStripList = new ArrayList<GeneStripModel>();
   		
   		for(String currentGeneId : geneIds) {
   			
   			geneStripList.add(geneStripDao.getGeneStripDataFromSymbol(currentGeneId));
   		}
   		
		
		return geneStripList;
	}
	
	public ArrayList<String> getGeneIds() {
		return geneIds;
	}

}
