package org.gudmap.assemblers;

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
	
	

}
