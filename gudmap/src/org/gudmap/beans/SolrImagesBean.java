package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.faces.context.FacesContext;



import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.assemblers.ImageMatrixAssembler;
import org.gudmap.globals.Globals;
//import org.gudmap.assemblers.SolrInsituAssembler;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.models.SolrInsituTableBeanModel;
import org.gudmap.models.submission.ImageInfoModel;




@Named (value="solrImagesBean")
@SessionScoped
public class SolrImagesBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	private ImageMatrixAssembler imageMatrixAssembler;
	private ImageInfoModel[][]imageInfoModelArray=null;
	private String[] imageMatrixHeaders;
	private List<String> selectedItems;
    private boolean areAllChecked;
    private String geneId;
    
    @Inject
   	private SolrTreeBean solrTreeBean;

    @Inject
   	private SolrFilter solrFilter;
   
	private String solrInput;
	private HashMap<String,String> filters;
	
	public SolrImagesBean() {
	}
		
	public void setup() {
//		geneId=Globals.getParameterValue("geneId");
		imageMatrixAssembler = new ImageMatrixAssembler(getGeneId());
		
		imageInfoModelArray=imageMatrixAssembler.retrieveData();
	}
		
	public void setImageInfoModelArray(ImageInfoModel[][] imageInfoModelArray){
		this.imageInfoModelArray = imageInfoModelArray;
	}
	
	public ImageInfoModel[][] getImageInfoModelArray() {
		return imageInfoModelArray;
	}
	
	public String[] getImageMatrixHeaders() {
		imageMatrixHeaders = imageMatrixAssembler.getStages().toArray(new String[0]);
		Arrays.sort(imageMatrixHeaders);
		return imageMatrixHeaders;
	}
	
	public String checkboxSelections() { 
    	selectedItems.clear();
    	for (int i=0;i<imageInfoModelArray.length;i++) { 
    		for (int j=0; j<imageInfoModelArray[i].length; j++) {
	    		if (imageInfoModelArray[i][j].getSelected()) {
	    			//selectedItems.add(imageInfoModelArray[i][j].getOid()); 
	    			selectedItems.add(imageInfoModelArray[i][j].getFilePath());
	    		} 
    		}
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	//since this is a request scoped bean, the values in selected items will not be available to result.jsf. Modify.
    	return "result";
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<imageInfoModelArray.length;i++) { 
    		for (int j=0; j<imageInfoModelArray[i].length; j++) {
    			if(imageInfoModelArray[i][j]!=null)
    				imageInfoModelArray[i][j].setSelected(areAllChecked); 
    		}
    	}
    }
    
    public String getSelectedItemstoString(){
    	String str="";
    	for(String s : selectedItems){
    		str+=s + ", ";
    	}
    	return str;
    }
    
    public void setGeneId(String geneId) {
    	this.geneId = geneId;
    }
    
    public String getGeneId() {
    	return geneId;
    }

    public void loadDataList() {
    	filters = solrFilter.getFilters();
//        totalRows = assembler.getCount(solrInput, filters);
//        totalRows = solrTreeBean.getSolrUtil().getInsituFilteredCount(solrInput,filters);
    	
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
   }

	public List<InsituTableBeanModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
					
		SolrDocumentList sdl  = solrTreeBean.getSolrUtil().getImagesData(solrInput, filterlist, sortColumn,ascending,offset,num);
		if (sdl==null){
			return null;
		}
		list = formatTableData(sdl);
			

		return list;
	}

	private List<InsituTableBeanModel> formatTableData(SolrDocumentList sdl){
		
		List<InsituTableBeanModel> list = new ArrayList<InsituTableBeanModel>();
		InsituTableBeanModel model = null;
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			String insituExpression = "";			
			if (doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new InsituTableBeanModel();
			model.setOid(doc.getFieldValue("GUDMAP").toString());
			model.setGene(doc.getFieldValue("GENE").toString());
			model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
			model.setSource(doc.getFieldValue("SOURCE").toString());
			model.setSubmission_date(doc.getFieldValue("DATE").toString());
			model.setAssay_type(doc.getFieldValue("ASSAY_TYPE").toString());
			model.setProbe_name(doc.getFieldValue("PROBE_NAME").toString());
			model.setStage(doc.getFieldValue("STAGE").toString());
			model.setAge(doc.getFieldValue("DEV_STAGE").toString());
			model.setSex(doc.getFieldValue("SEX").toString());
			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
			model.setTissue(doc.getFieldValue("TISSUE_TYPE").toString());
			model.setExpression(insituExpression);
			model.setSpecimen(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
			model.setImage(doc.getFieldValue("IMAGE_PATH").toString());
			model.setGene_id(doc.getFieldValue("MGI_GENE_ID").toString());
			
			list.add(model);			
		}
			
		return list;
	 }	
    
}
