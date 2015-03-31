package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.DiseaseResourceDao;
import org.gudmap.models.DiseaseResourceModel;

public class DiseaseResourceAssembler {
	
	private DiseaseResourceDao diseaseResourceDao;
	
	public DiseaseResourceAssembler () {
		diseaseResourceDao = new DiseaseResourceDao();
	}
	
	
	public ArrayList<DiseaseResourceModel> getData(String inputTerm,String type, boolean isDirect) {
		ArrayList<DiseaseResourceModel> datalist = diseaseResourceDao.getGeneAssocDisease(inputTerm,type,isDirect);
		return datalist;
	}
}
