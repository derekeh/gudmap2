package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.models.GeneStripModel;
import org.gudmap.dao.GeneStripDao;

public class GeneStripBeanAssembler {
	
	private GeneStripDao geneStripDao;	
	
	public GeneStripBeanAssembler() {
		geneStripDao = new GeneStripDao();
	}
	
	public ArrayList<GeneStripModel> getData(String gene, String input, String wildcard) {
		ArrayList<GeneStripModel> geneStripList=new ArrayList<GeneStripModel>();
		
   		ArrayList<String> geneSymbols = geneStripDao.getSymbolsFromGeneInput(input, wildcard);
   		for(String currentGene : geneSymbols) {
   			
   			geneStripList.add(geneStripDao.getGeneStripDataFromSymbol(currentGene));
   		}
   		
		
		return geneStripList;
	}

}
