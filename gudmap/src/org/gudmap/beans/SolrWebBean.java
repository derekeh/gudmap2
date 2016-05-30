package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;







import javax.servlet.http.HttpServletRequest;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.assemblers.EditPageAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.EditPageModel;

@Named (value="solrWeb")
@SessionScoped
public class SolrWebBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.

    private String whereclause = " WHERE ";
    private List<String> selectedItems;
    private boolean areAllChecked;
    
    @Inject
   	private ParamBean paramBean;
    
    @Inject
   	private SolrTreeBean solrTreeBean;

    @Inject
   	private SolrFilter solrFilter;
    
	private String solrInput;
	private String host;
	private HashMap<String,String> filters;
	private boolean showPageDetails = true;
    
    // Constructors -------------------------------------------------------------------------------

    public SolrWebBean() {
    	super(10,10,"RELEVANCE",true);
    	setup();
    }
    
	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
 
	public void setSolrTreeBean(SolrTreeBean solrTreeBean){
		this.solrTreeBean=solrTreeBean;
	}
	
	public void setSolrFilter(SolrFilter solrFilter){
		this.solrFilter = solrFilter;
	}
	
	public void setSolrInput(String solrInput){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
	}
	
	public String getSolrInput(){
		solrInput = solrTreeBean.getSolrInput();
		refresh();
		return solrInput;
	}

	
	public String getHost(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		
		return request.getLocalName();
	}
	
   public void setup() {

     }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrTreeBean.getSolrInput();
    }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
        totalRows = solrTreeBean.getSolrUtil().getWebCount(solrInput, filters);
   	
     	dataList = getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
       // Set currentPage, totalPages and pages.
        currentPage = (totalRows / rowsPerPage) - ((totalRows - firstRow) / rowsPerPage) + 1;
        totalPages = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        int pagesLength = Math.min(pageRange, totalPages);
        pages = new Integer[pagesLength];

        // firstPage must be greater than 0 and lesser than totalPages-pageLength.
        int firstPage = Math.min(Math.max(0, currentPage - (pageRange / 2)), totalPages - pagesLength);

        // Create pages (page numbers for page links).
        for (int i = 0; i < pagesLength; i++) {
            pages[i] = ++firstPage;
        }
        
        if (dataList.size() > rowsPerPage)
        	showPageDetails = true;
        else
        	showPageDetails = false;
    }

    public String refresh(){
 //   	sortField = "RELEVANCE";
    	loadDataList();
//   	paramBean.resetValues();
    	return "solrTutorial";
    }

//    public String checkboxSelections() { 
//    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
//    	selectedItems.clear();
//    	for (int i=0;i<dataList.size();i++) { 
//    		if (((EditPageModel) dataList.get(i)).getSelected()) { 
//    			selectedItems.add(((EditPageModel) dataList.get(i)).getId()); 
//    		} 
//    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
//    	return "result";
//    }
    
//    public void checkAll() { 
//    	areAllChecked=(areAllChecked)?false:true;
//    	for (int i=0;i<dataList.size();i++) { 
//    		((EditPageModel)dataList.get(i)).setSelected(areAllChecked);
//    	} 
//    }

    
    public String getTitle(){
    	String str="Mousestrains Search Results ";
    	filters = solrFilter.getFilters();
    	if (filters == null){
	    	if (solrInput != null && solrInput != "")
	    		str += "(" + solrTreeBean.getMouseStrainsCount() + ") > " + solrInput;
	    	else
	    		str += "(" + solrTreeBean.getMouseStrainsCount() + ") > ALL";
    	}
    	else{
        	if (solrInput != null && solrInput != "")
        		str += "(" + solrTreeBean.getMouseStrainsCount(filters) + ") > " + solrInput;
        	else
        		str += "(" + solrTreeBean.getMouseStrainsCount(filters) + ") > ALL";
    		
    	}
    	return str;
    }  
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }
    
    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a list of EditPageModels containing the retrieved documents.
     * 
	 * @param solrInput The main query string for retrieving relevant documents'
     * @param filterlist A list of filters to be applied to the solr search
     * @param sortColumn The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param num The number of documents to be retrieved in the result set.
     * @return A list of EditPageModels
     * @see EditPageModel
     */
	public List<EditPageModel> getData(String solrInput, HashMap<String, String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<EditPageModel> list = new ArrayList<EditPageModel>();

		QueryResponse qr = solrTreeBean.getSolrUtil().getWebData(solrInput,filterlist,sortColumn,ascending,offset,num);
		list = formatTableData(qr);

		return list;
	}

	/**
	 * This method creates a list of EditPageModels from the documents in the QueryResponse qr.
	 * 
	 * @param qr A solr QueryResponse	  
	 * @return A List of EditPageModels
     * @see EditPageModel
	 */
	private List<EditPageModel> formatTableData(QueryResponse qr){
		
		List<EditPageModel> list = new ArrayList<EditPageModel>();
		EditPageModel model = null;
		
		Map<String,Map<String,List<String>>> highlightMap = qr.getHighlighting();
		Set<String> keys = highlightMap.keySet();
		
		

		SolrDocumentList sdl = qr.getResults();
		
		Map<String,Map<String,List<String>>> hl = qr.getHighlighting();
//		for (Map.Entry<String,Map<String,List<String>>> entry1 :hl.entrySet()){
//			String hkey = entry1.getKey();
//			Map<String,List<String>> hval = entry1.getValue();
//			for(Map.Entry<String,List<String>> entry2 : hval.entrySet() ){
//				String hkey2 = entry2.getKey();
//				List<String> hval2 = entry2.getValue();
//				for(String v2: hval2){
//					int l = v2.length();
//				}
//					
//			}
//			
//		}
		
		
		
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			model = new EditPageModel();
			if (doc.containsKey("ID"))
				model.setOid(Integer.parseInt(doc.getFieldValue("ID").toString()));
			
			String id = doc.getFieldValue("ID").toString();
	    	if (keys.contains(id)){            
	            Set<String> fields = highlightMap.get(id).keySet();
	            for(String field :fields){
	                List<String> result = highlightMap.get(id).get(field);
	            	for(String item : result){
	            		model.setContent_1(item);
	            	}            	
	             }	    	
	    	}
	    	
	    	ArrayList<ArrayList<String>> modellist = new ArrayList<ArrayList<String>>();
			for (Map.Entry<String,Map<String,List<String>>> entry1 :hl.entrySet()){
				if( id.contains(entry1.getKey())){	
					Map<String,List<String>> hval = entry1.getValue();
					for(Map.Entry<String,List<String>> entry2 : hval.entrySet() ){
						String hkey2 = entry2.getKey();
						List<String> hval2 = entry2.getValue();
						for(String v2: hval2){
							String orig = v2.replace("<strong>","");
							orig = orig.replace("</strong>","");
							String update = v2.replace("<strong>", "<span style='background-color: #FFFF00'>");
							update = update.replace("</strong>", "</span>");
							ArrayList<String> al = new ArrayList<String>();
							al.add(orig);
							al.add(update);
							modellist.add(al);							
						}
							
					}
				}
				
			}
			
			model.setHighlights(modellist);
			
			if (doc.containsKey("TITLE"))
				model.setTitle(doc.getFieldValue("TITLE").toString());
			if (doc.containsKey("ALIAS"))
				model.setAlias(doc.getFieldValue("ALIAS").toString());
			if (doc.containsKey("DATE")){
				String date = doc.getFieldValue("DATE").toString();
				date = date.substring(0, 10);
				model.setModifiedDate(date);
			}
			if (doc.containsKey("URL"))
				model.setUrl(doc.getFieldValue("URL").toString());

				
			list.add(model);			
		}
		
		return list;
	}	
   
}
