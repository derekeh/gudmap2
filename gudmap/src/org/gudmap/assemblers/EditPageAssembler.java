package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.models.EditPageModel;

public class EditPageAssembler {
	
	EditPageModel editPageModel=null;
	private ArrayList<EditPageModel> editPageList;
	
	public EditPageAssembler() {
		
	}
	
	public ArrayList<EditPageModel> retrievePage(int docID){
		//editPageModel = editPageDao.getPage(docID);
		
		return editPageList;
	}

}
