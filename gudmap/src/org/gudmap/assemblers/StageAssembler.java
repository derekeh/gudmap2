package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.StageDao;

public class StageAssembler {
	
	StageDao stageDao;
	
	
	public StageAssembler(){
		stageDao = new StageDao();
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
			//GET STAGES THAT EXIST IN DB SUBMISSIONS FROM QUERY
			/*for (int i=1 ; i<17; i++){
				if (i<10)
					isharray.remove("TS0"+i);
				else
					isharray.remove("TS" + i);
			}*/
			
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
