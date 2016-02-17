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
	private List<ImageDetailModel> sublist;  
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
//        totalRows = solrTreeBean.getSolrUtil().getImagesCount(solrInput,filters);
    	
     	dataList = getData(solrInput, filters, sortField, sortAscending, firstRow, rowsPerPage);
     	totalRows = dataList.size();

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
    	return "solrImages";
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
    
    public List<ImageDetailModel> getSublist(){
    	return sublist;
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
		sublist = new ArrayList<ImageDetailModel>();
		List<String> ids = new ArrayList<String>();
		ImageDetailModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);
			
			if (doc.getFieldValue("GUDMAP_ID") != null){
				String accessionId = doc.getFieldValue("GUDMAP_ID").toString();
						
					model = new ImageDetailModel();
					if (doc.containsKey("GUDMAP_ID"))
						model.setAccessionId(accessionId);
					if (doc.containsKey("IMAGE_ID"))
						model.setOid(doc.getFieldValue("IMAGE_ID").toString());
					if (doc.containsKey("GENE"))
						model.setGeneSymbol(doc.getFieldValue("GENE").toString());
					if (doc.containsKey("SPECIMEN_ASSAY_TYPE"))
						model.setSpecimenType(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
					if (doc.containsKey("STAGE"))
						model.setStage(doc.getFieldValue("STAGE").toString());
					if (doc.containsKey("THUMBNAIL_PATH")){
						String image_path = doc.getFieldValue("THUMBNAIL_PATH").toString();
						model.setThumbnailPath(image_path);
						model.setFilePath(image_path.replace("thumbnails", "medium"));
					}
					if (doc.containsKey("IMAGE_CLICK_PATH"))
						model.setClickFilePath(doc.getFieldValue("IMAGE_CLICK_PATH").toString());
					if (doc.containsKey("ASSAY_TYPE"))
						model.setAssayType(doc.getFieldValue("ASSAY_TYPE").toString());
					if (doc.containsKey("MGI_GENE_ID"))
						model.setMgiGeneId(doc.getFieldValue("MGI_GENE_ID").toString());
					if (doc.containsKey("SPECIES"))
						model.setSpecies(doc.getFieldValue("SPECIES").toString());
					
					model.setGroup(accessionId.replace("GUDMAP:", ""));
					model.setSibling(accessionId.replace(":", "_"));
					
					if (!ids.contains(accessionId)){
						
						list.add(model);
						ids.add(accessionId);
					}
					else{
						sublist.add(model);
					}
					
			}
		}


		return list;
	}	

}
