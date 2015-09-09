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
	
	public ArrayList<GeneStripModel> getData(String gene, String input, String wildcard,int firstRow, int rowsPerPage, String sortField, boolean sortAscending) {
		ArrayList<GeneStripModel> geneStripList = null; 
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
   		//geneIds = geneStripDao.getSymbolsFromGeneInput(input, wildcard);
   		//if(geneIds==null  || geneIds.size() <1)
   		//	return null;
   		//else
   		//	geneStripList = new ArrayList<GeneStripModel>();
   		
   		/*for(String currentGeneId : geneIds) {
   			
   			geneStripList.add(geneStripDao.getGeneStripDataFromSymbol(currentGeneId));
   		}*/
   		
   		geneStripList = geneStripDao.getData(geneIds,firstRow,rowsPerPage,sortField,sortDirection);
   		
		
		return geneStripList;
	}
	
	public ArrayList<String> getGeneIds(String input, String wildcard) {
		geneIds = geneStripDao.getSymbolsFromGeneInput(input, wildcard);
		return geneIds;
	}
	
	public int count() {
		return geneStripDao.count(geneIds);
	}

}
