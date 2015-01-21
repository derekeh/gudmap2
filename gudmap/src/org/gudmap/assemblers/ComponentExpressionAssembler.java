package org.gudmap.assemblers;


import java.util.ArrayList;

import org.gudmap.dao.AnatomyDao;
import org.gudmap.dao.IshSubmissionDao;
import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.models.submission.ExpressionPatternModel;

public class ComponentExpressionAssembler {
	private  AnatomyDao anatomyDao;
	private  IshSubmissionDao ishSubmissionDao;
	
	public ComponentExpressionAssembler () {
		anatomyDao = new  AnatomyDao();
		ishSubmissionDao = new IshSubmissionDao();
		
	}
	
	public ExpressionDetailModel getData(String oid, String componentId) {
		if (oid == null || oid.equals("")) {
        	return null;
        }
        
        if (componentId == null || componentId.equals("")) {
        	return null;
        }
		ExpressionDetailModel expressionDetailModel =
				anatomyDao.findExpressionDetailBySubmissionIdAndComponentId(oid, componentId);
		
		if (expressionDetailModel != null) {
				
				ExpressionPatternModel [] expressionPatternModel = 
						anatomyDao.findComponentPatternsAndLocations(String.valueOf(expressionDetailModel.getExpressionId()));
				expressionDetailModel.setPattern(expressionPatternModel);
	                        
				expressionDetailModel.setSubmissionId(oid);
		}
		else 
		{
            
			String stage = ishSubmissionDao.findStageByOid(oid);
			ArrayList<Object> componentDetail = anatomyDao.findComponentDetailByComponentId(componentId);
			expressionDetailModel = new ExpressionDetailModel();
			expressionDetailModel.setSubmissionId(oid);
			expressionDetailModel.setStage(stage);
			expressionDetailModel.setComponentId(((String)componentDetail.get(0)));
			expressionDetailModel.setComponentName(((String)componentDetail.get(1)));
			expressionDetailModel.setComponentDescription(((ArrayList)componentDetail.get(2)));
			
//			expressionDetail.setPattern("not applicable");
			ExpressionPatternModel[] expressionPatternModelArray = new ExpressionPatternModel[1];
			ExpressionPatternModel expressionPatternModel = new ExpressionPatternModel();
			expressionPatternModel.setPattern("not applicable");
			expressionPatternModelArray[0] = expressionPatternModel;
			expressionDetailModel.setPattern(expressionPatternModelArray);
			
			boolean hasParent = anatomyDao.hasParentNode(componentId, stage, oid);
			if (hasParent) {
				// inferred not detected
				expressionDetailModel.setPrimaryStrength("inferred not detected");
				
			} else {
				boolean hasChild = anatomyDao.hasChildenNode(componentId, stage, oid);
				if (hasChild) {
					// inferred present
					expressionDetailModel.setPrimaryStrength("inferred present");
					
				} else {
					// not examined
					expressionDetailModel.setPrimaryStrength("not examined");
				}
			}
		}
		
		return expressionDetailModel;
	}

}
