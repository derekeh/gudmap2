package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.faces.context.FacesContext;







import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.dao.GeneDetailsDao;
import org.gudmap.globals.Globals;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.SolrInsituTableBeanModel;
import org.gudmap.models.submission.ImageDetailModel;






@Named (value="solrImagesBean")
@SessionScoped
public class SolrImagesBean extends PagerImpl implements Serializable  {
	
	 private static final long serialVersionUID = 1L;
	 
    // Data.
//	private SolrInsituAssembler assembler;
	private GeneDetailsDao geneDetailsDao;
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
	private HashMap<String,String> filters;
	private boolean showPageDetails = true;
   
	private String geneId;
    
    // Constructors -------------------------------------------------------------------------------

    public SolrImagesBean() {
    	super(1000,10,"RELEVANCE",true);
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
    
    public void setup() {
//    	assembler=new SolrInsituAssembler();
    	geneDetailsDao = new GeneDetailsDao();
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    	solrInput = solrTreeBean.getSolrInput();
     }

    
    @Override
    public void loadDataList() {
    	filters = solrFilter.getFilters();
//        totalRows = assembler.getCount(solrInput, filters);
        totalRows = solrTreeBean.getSolrUtil().getImagesCount(solrInput,filters);
    	
//     	dataList = assembler.getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
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
//    	paramBean.resetValues();
    	return "solrInsitu";
    }

    public void resetAll() {
		paramBean.resetAll();
//		solrFilterBean.resetAll();		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((SolrInsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((SolrInsituTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((SolrInsituTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }
    
    public String getTitle(){
    	String str="Images Search Results ";
    	filters = solrFilter.getFilters();
    	return str;
    }
    
    public boolean getShowPageDetails(){
    	return showPageDetails;
    }
    
	public List<ImageDetailModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<ImageDetailModel> list = new ArrayList<ImageDetailModel>();

    		
		SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getImagesData(solrInput, filterlist, sortColumn,ascending,offset,num);
		if (sdl==null){
			return null;
		}
		list = formatTableData(sdl);
			


		return list;
	}

	private List<ImageDetailModel> formatTableData(SolrDocumentList sdl){
		
		List<ImageDetailModel> list = new ArrayList<ImageDetailModel>();
		ImageDetailModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			
			model = new ImageDetailModel();
			model.setAccessionId(doc.getFieldValue("GUDMAP_ID").toString());
			model.setGeneSymbol(doc.getFieldValue("GENE").toString());
			model.setSpecimenType(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
			model.setStage(doc.getFieldValue("STAGE").toString());
			String image_path = doc.getFieldValue("THUMBNAIL_PATH").toString();
			model.setFilePath(image_path.replace("thumbnails", "medium"));
			model.setClickFilePath(doc.getFieldValue("IMAGE_CLICK_PATH").toString());
			model.setAssayType(doc.getFieldValue("ASSAY_TYPE").toString());
			model.setMgiGeneId(doc.getFieldValue("MGI_GENE_ID").toString());
			
			list.add(model);			
		}

			
		return list;
	}	

}
