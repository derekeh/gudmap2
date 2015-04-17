package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.EditPageDao;
import org.gudmap.models.EditPageModel;

public class EditPageAssembler {
	
	//private EditPageModel editPageModel=null;
	private ArrayList<EditPageModel> editPageList;
	private EditPageDao editPageDao;
	
	public EditPageAssembler() {
		editPageDao = new EditPageDao();
	}
	
	public ArrayList<EditPageModel> retrievePage(String docID){
		editPageList = editPageDao.retrievePage(docID);
		
		return editPageList;
	}

}
