package org.gudmap.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.models.EditPageModel;

@Named
@RequestScoped
public class ViewPageBean {
	private String docID="";
	private EditPageAssembler editPageAssembler;
	private ArrayList<EditPageModel> editPageList;
	private String query="";
	
	public ViewPageBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
    	this.docID = facesContext.getExternalContext().getRequestParameterMap().get("docID");	
    	Iterator<String> itr = facesContext.getExternalContext().getRequestParameterNames();
    	while(itr.hasNext()){
    		String element = itr.next().toString();
    		if (element.contains("query")){
    			this.query = facesContext.getExternalContext().getRequestParameterMap().get("query");
    		}
    	}
    	
    	editPageAssembler = new EditPageAssembler();
    	editPageList = editPageAssembler.retrievePage(docID);
    	
	}
	
	public ArrayList<EditPageModel> getEditPageList() {
		
//		if (query.length() > 0){
//			EditPageModel model = editPageList.get(0);
//			String content = model.getContent_1();
//			ArrayList<ArrayList<String>> highlights = model.getHighlights();
//			for (ArrayList<String> highlight : highlights){
//				content = content.replace(highlight.get(0), highlight.get(1));	
//			}
//			model.setContent_1(content);
//			editPageList.set(0, model);
//		}

		if (query.length() > 0){
			EditPageModel model = editPageList.get(0);
			String content = model.getContent_1();
			String[] arr = query.split(" ");
			int size = arr.length;
			for (int i=0; i<size; i++){
				String qtext = arr[i].toLowerCase();
				content = HighlightText(content,qtext);
			}
			model.setContent_1(content);
			editPageList.set(0, model);
		}
		
		
		return editPageList;
	}
	
	public String getDocID () {
		return docID;
	}
	
	public void setDocID (String docID) {
		this.docID = docID;
	}

	public String HighlightText( String input, String queryPattern){
		  
		StringBuffer result = new StringBuffer();
		//startIdx and idxOld delimit various chunks of input; these
		//chunks always end where queryPattern begins
		int startIdx = 0;
		int idxOld = 0;
		
		while ((idxOld = input.toLowerCase().indexOf(queryPattern, startIdx)) >= 0) {
	        //grab a part of aInput which does not include queryPattern
	        result.append( input.substring(startIdx, idxOld) );
	       
	        //grab text to be highlighted
			int len = queryPattern.length();				
			String ftext = input.substring(idxOld,idxOld+len);
			// add yellow highlight 
			String newPattern = "<span style='background-color: #FFFF00'>" + ftext + "</span>"; 
	       
	        //add newPattern to take place of queryPattern
	        result.append( newPattern );
	
	        //reset the startIdx to just after the current match, to see
	        //if there are any further matches
	        startIdx = idxOld + queryPattern.length();
        }
        //the final chunk will go to the end of input
        result.append( input.substring(startIdx) );
        return result.toString();
	}

}
