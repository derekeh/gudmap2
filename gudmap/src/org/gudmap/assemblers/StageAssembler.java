package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.StageDao;

public class StageAssembler {
	
	StageDao stageDao;
	
	
	public StageAssembler(){
		stageDao = new StageDao();
	}
	
	public String[][] retrieveData(String[] stage, String organ, String geneSymbol) {
		
		String[][] stageList = stageDao.getStageList( stage, organ, geneSymbol);
		
		String[][] tableData = new String[stage.length][5];
		
		for(int i=0; i<stage.length; i++) {
			tableData[i][0] = new String("TS"+stage[i]);
			if(null != stageList[i][0]) {
				tableData[i][1] = new String(stageList[i][0]);
			} else {
				tableData[i][1] = new String("0");
			}
			if(null != stageList[i][1]) {
				tableData[i][2] = new String(stageList[i][1]);
			} else {
				tableData[i][2] = new String("0");
			}
			if(null != stageList[i][2]) {
				tableData[i][3] = new String(stageList[i][2]);
			} else {
				tableData[i][3] = new String("0");
			}
			tableData[i][4] = new String(stage[i]);
		}
		
		return tableData;
	}
	
	
	
	public String[] getStages(String species) {
		
		ArrayList<String> isharray =  stageDao.getStages(species);
		ArrayList<String> micarray =  stageDao.getStages(species);
			if (micarray != null){
				for (String item : micarray){
					if (!isharray.contains(item))
						isharray.add(item);
				}
			}
			
			for (int i=1 ; i<17; i++){
				if (i<10)
					isharray.remove("TS0"+i);
				else
					isharray.remove("TS" + i);
			}
			
			String[] stages = new String[isharray.size()];			
			for(int i = 0; i< isharray.size(); i++)
				stages[i] = isharray.get(i);

			return stages;	
	}
	
	public String[][] getStageList(String[] stage, String organ, String geneId) {
		
		String[][] stageLists = null;
			
			/** get data from database */
			// get insitu stage list
			String[] insituStageList = stageDao.getStageList("insitu", stage, organ, geneId);
			
			// get microarray stage list
			String[] arrayStageList = stageDao.getStageList("Microarray", stage, organ, geneId);
			
			// get sequence stage list
			String[] sequenceStageList = stageDao.getStageList("sequence", stage, organ, geneId);			
			
			// get age (dpc) stage list
			int len = stage.length;
			String[] dpcStageList = new String[len];
			for (int i=0;i<len;i++) {
				dpcStageList[i] = stageDao.getDpcStageValue(stage[i]);
			}
			
			// put them together
			stageLists = new String[len][4];
			for (int i=0;i<len;i++) {
				stageLists[i][0] = dpcStageList[i];
				stageLists[i][1] = insituStageList[i];
				stageLists[i][2] = arrayStageList[i];
				stageLists[i][3] = sequenceStageList[i];
			}
			/** return the value object */
			return stageLists;
		
	}

}
