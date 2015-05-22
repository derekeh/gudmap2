package org.gudmap.assemblers;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.gudmap.dao.EditPageDao;
import org.gudmap.models.EditPageModel;

public class EditPageAssembler {
	
	private ArrayList<EditPageModel> editPageList;
	private EditPageDao editPageDao;
	
	public EditPageAssembler() {
		editPageDao = new EditPageDao();
	}
	
	public ArrayList<EditPageModel> retrievePage(String docID){
		editPageList = editPageDao.retrievePage(docID);
		
		return editPageList;
	}
	
	public String updatePage(String oid, String title, String content) {
		String RET="";
		RET = editPageDao.updatePage( oid, title, content);
		return RET;
	}
	
	public String createPage(String alias, String title, String category, int level, String value, int userID) throws NoSuchAlgorithmException {
		
		String RET="";
		RET = editPageDao.createPage( alias, title, category, level, value, userID);
		return RET;
	}
	
	public ArrayList<String> getAliasList() {
		return editPageDao.getAliasList();
	}
	
	public int getLastInsert() {
		return editPageDao.getLastInsert();
	}
	
	public ArrayList<EditPageModel> getPageList() {
		return editPageDao.getPageList();
	}

}
