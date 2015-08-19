package org.gudmap.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.gudmap.assemblers.AccessionTablePageBeanAssembler;
import org.gudmap.assemblers.AnatomyTablePageBeanAssembler;
import org.gudmap.assemblers.CollectionEntriesAssembler;
import org.gudmap.assemblers.GeneExpressionTablePageBeanAssembler;
import org.gudmap.assemblers.GeneFunctionTablePageBeanAssembler;
import org.gudmap.assemblers.GeneListTablePageBeanAssembler;
import org.gudmap.assemblers.InsituTablePageBeanAssembler;
import org.gudmap.assemblers.MicPlatformTablePageBeanAssembler;
import org.gudmap.assemblers.MicSampleTablePageBeanAssembler;
import org.gudmap.assemblers.MicSeriesTablePageBeanAssembler;
import org.gudmap.assemblers.SeqSampleTablePageBeanAssembler;
import org.gudmap.assemblers.SeqSeriesTablePageBeanAssembler;
import org.gudmap.globals.Globals;
import org.gudmap.impl.PagerImpl;
import org.gudmap.models.InsituTableBeanModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.array.SequenceQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.genestrip.GeneIndexQueries;
import org.gudmap.queries.genestrip.GeneListQueries;
import org.gudmap.utils.Utils;

@Named
@SessionScoped
public class GenericTablePageBean extends PagerImpl implements Serializable  {
	
	private static final long serialVersionUID = 1L;

    // Data.
	private InsituTablePageBeanAssembler assembler;
	private MicSeriesTablePageBeanAssembler micSeriesAssembler;
	private MicSampleTablePageBeanAssembler micSampleAssembler;
	private MicPlatformTablePageBeanAssembler micPlatformAssembler;
	private SeqSeriesTablePageBeanAssembler seqSeriesAssembler=null;
	private SeqSampleTablePageBeanAssembler seqSampleAssembler=null;
	private GeneListTablePageBeanAssembler geneListAssembler;
	private AccessionTablePageBeanAssembler accessionAssembler;
	private AnatomyTablePageBeanAssembler anatomyAssembler;
	private GeneFunctionTablePageBeanAssembler genefunctionAssembler;
	private GeneExpressionTablePageBeanAssembler expressionAssembler;
	private CollectionEntriesAssembler collectionEntriesAssembler;
	private String whereclause = GenericQueries.WHERE_CLAUSE;
    private String specimenWhereclause="";
    private List<String> selectedItems;
    private boolean areAllChecked;
    
    private String queryTotals;
    private String userInput="";
    private String userInputQuery;
    private String wildcard="";
    
    private String assayType="";
    private String specimenAssay="";
    
    @Inject
   	protected ParamBean paramBean;
    
    @Inject
   	protected VolatileSummaryBean volatileSummaryBean;
   	
    // Constructors -------------------------------------------------------------------------------

    public GenericTablePageBean() {
    	//super(20,10,"SUB_OID",true);   	
        //setup("ISH","");
    }
    
	/*public GenericTablePageBean(int rowsperpage, int pagenumbers, String defaultOrder, boolean sortDirection) {
		super(rowsperpage,pagenumbers,defaultOrder,sortDirection);
	}*/

	public void setParamBean(ParamBean paramBean){
		this.paramBean=paramBean;
	}
	
	public void setVolatileSummaryBean(VolatileSummaryBean volatileSummaryBean){
		this.volatileSummaryBean=volatileSummaryBean;
	}
	
	public void init(String assayType, String specimenAssay, int rowsperpage, int pagenumbers, String defaultOrderCol, boolean sortDirection) {
		this.assayType=assayType;
		this.specimenAssay=specimenAssay;
		initPaging(rowsperpage,pagenumbers,defaultOrderCol,sortDirection);
		setup(assayType,specimenAssay);
	}
	
    
    public void setup(String assayType,String specimenAssay) {
    	//TODO find the generic query to use (and/or specimen assay types) based on assay type
    	//this.assayType=assayType;
    	
    	if(specimenAssay.equals("WISH"))
    		specimenWhereclause=GenericQueries.WHERE_WISH;
    	else if(specimenAssay.equals("SISH"))
    		specimenWhereclause = GenericQueries.WHERE_SISH;
    	else if(specimenAssay.equals("OPT"))
    		specimenWhereclause = GenericQueries.WHERE_OPT;
    	else
    		specimenWhereclause="";
    	
    	if(assayType.equals("collections"))
    		collectionEntriesAssembler=new CollectionEntriesAssembler(GenericQueries.BROWSE_LOCAL_STORAGE_PARAM);
    	else if(assayType.equals("genelist"))
    		geneListAssembler=new GeneListTablePageBeanAssembler(GeneListQueries.BROWSE_GENELIST_PARAM);
    	else if(assayType.equals("anatomy"))
    		anatomyAssembler=new AnatomyTablePageBeanAssembler();
    	else if(assayType.equals("accession"))
    		accessionAssembler=new AccessionTablePageBeanAssembler(GenericQueries.BROWSE_ACCESSION_PARAM);
    	else if(assayType.equals("genefunction"))
    		genefunctionAssembler=new GeneFunctionTablePageBeanAssembler(GeneListQueries.BROWSE_GENE_FUNCTION_PARAM);
    	else if(assayType.equals("Microarray")) {
    		if(specimenAssay.equals("micseries"))
    			micSeriesAssembler= new MicSeriesTablePageBeanAssembler(ArrayQueries.MIC_SERIES_BROWSE_PARAM,assayType);
	    	if(specimenAssay.equals("micsample"))
				micSampleAssembler= new MicSampleTablePageBeanAssembler(ArrayQueries.MIC_SAMPLE_BROWSE_PARAM,assayType);
	    	if(specimenAssay.equals("micplatform"))
    			micPlatformAssembler= new MicPlatformTablePageBeanAssembler(ArrayQueries.MIC_PLATFORM_BROWSE_PARAM,assayType);
    	}
    	else if(assayType.equals("NextGen")) {
    		if(specimenAssay.equals("seqseries")) {
	    		//if(seqSeriesAssembler==null)
	    			seqSeriesAssembler= new SeqSeriesTablePageBeanAssembler(SequenceQueries.SEQUENCE_SERIES_BROWSE_PARAM,assayType);
    		}
	    	if(specimenAssay.equals("seqsample")) {
	    		//if(seqSampleAssembler==null)
	    			seqSampleAssembler= new SeqSampleTablePageBeanAssembler(SequenceQueries.SEQUENCE_SAMPLE_BROWSE_PARAM,assayType);
	    	}
    	}
    	else if(assayType.equals("TG")){
    		assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_TG_PARAM,assayType);
    	}
    	else if(assayType.equals("IHC")) {
    		assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_ISH_PARAM,assayType);
    	}
    	else if(assayType.equals("ISH")) {
    		if(specimenAssay.equals("expression")) {
    			//if(expressionAssembler==null)
    				expressionAssembler = new GeneExpressionTablePageBeanAssembler(GeneIndexQueries.GENES_BY_EXPRESSION, assayType) ;
    		}
    		//insitu data
    		else {
    			if(Globals.getParameterValue("geneId")!=null)
    				paramBean.setGeneIdvalues(Globals.getParameterValue("geneId"));
    			if(Globals.getParameterValue("focusGroup")!=null)  {
    				paramBean.setFocusGroup(Globals.focusGroups[Integer.parseInt(Globals.getParameterValue("focusGroup"))]);
    				volatileSummaryBean.setFocusGroup(Globals.focusGroups[Integer.parseInt(Globals.getParameterValue("focusGroup"))]);
    			}
    			
    			assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_ISH_PARAM,assayType);
    		}
    	}
    	
    	/*else
    			assembler=new InsituTablePageBeanAssembler(GenericQueries.BROWSE_ISH_PARAM,assayType);*/
    	
    	
        selectedItems = new ArrayList<String>(); 
    }
    
    @PostConstruct
    public void doSomething() {
    	
    }
    
    /*@PostConstruct
    public void setRemoteWhereclause(){
    	paramBean.setWhereclause(whereclause);
    }*/
    
    @Override
    public void loadDataList() {
    	if(assayType.equals("collections")) {
    		//reset whereclause for this search
    		paramBean.setWhereclause(whereclause);
    		dataList = collectionEntriesAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
					paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
					paramBean.getFocusGroupSpWhereclause());
			totalRows = collectionEntriesAssembler.count();
			
			queryTotals=collectionEntriesAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("genelist")) {
    		//parameters passed from genestrip ishexpression column
    		if(Globals.getParameterValue("expressionGene")!=null)
    			setUserInput(Globals.getParameterValue("expressionGene"),false);
    		if(Globals.getParameterValue("focusGroup")!=null)  {
				paramBean.setFocusGroup(Globals.focusGroups[Integer.parseInt(Globals.getParameterValue("focusGroup"))]);
				//volatileSummaryBean.setFocusGroup(Globals.focusGroups[Integer.parseInt(Globals.getParameterValue("focusGroup"))]);
			}
    		
    		dataList = geneListAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
    				paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
    				paramBean.getFocusGroupSpWhereclause());
        	
        	totalRows = geneListAssembler.count();
        	queryTotals=geneListAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("anatomy")) {
    		//reset whereclause for this search
    		paramBean.setWhereclause(whereclause);
    		anatomyAssembler.init(userInputQuery.replace("'", ""));
        	dataList = anatomyAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
        									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
        									paramBean.getFocusGroupSpWhereclause(),paramBean.getCachewhereclause(),paramBean.getArraycachewhereclause());
            // Set currentPage, totalPages, columntotals and pages.
        	//setTotalslist(assembler.getTotals());
        	totalRows = anatomyAssembler.count();
        	queryTotals=anatomyAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("accession")) {
    		//reset whereclause for this search
    		paramBean.setWhereclause(whereclause);
    		dataList = accessionAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
					paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
					paramBean.getFocusGroupSpWhereclause());
			totalRows = accessionAssembler.count();
			
			queryTotals=accessionAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("genefunction")) {
    		//reset whereclause for this search
    		paramBean.setWhereclause(whereclause);
    		genefunctionAssembler.init(userInputQuery.replace("'", ""),"equals");
        	dataList = genefunctionAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
        									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause,userInputQuery,
        									paramBean.getFocusGroupSpWhereclause(),paramBean.getCachewhereclause());
            
        	totalRows = genefunctionAssembler.count();
        	queryTotals=genefunctionAssembler.getQueryTotals();
    	}
    	else if(assayType.equals("Microarray")) {
    		if(specimenAssay.equals("micseries")) {
    			micSeriesAssembler.setAssayType("Microarray");
	    		dataList = micSeriesAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getMicWhereclause(),
						paramBean.getFocusGroupWhereclause());
	    		//TODO Write column total queries.
				//setTotalslist(micSeriesAssembler.getTotals());
				totalRows = micSeriesAssembler.count();
    		}
    		if(specimenAssay.equals("micsample")) {
    			micSampleAssembler.setAssayType("Microarray");
	    		dataList = micSampleAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getMicWhereclause(),
						paramBean.getFocusGroupWhereclause());
	    		//TODO Write column total queries.
				//setTotalslist(micSeriesAssembler.getTotals());
				totalRows = micSampleAssembler.count();
    		}
    		if(specimenAssay.equals("micplatform")) {
    			micPlatformAssembler.setAssayType("Microarray");
	    		dataList = micPlatformAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending);
	    		//TODO Write column total queries.
				//setTotalslist(micSeriesAssembler.getTotals());
				totalRows = micPlatformAssembler.count();
    		}
    		
    	}
    	else if(assayType.equals("NextGen")) {
    		if(specimenAssay.equals("seqseries")) {
    			seqSeriesAssembler.setAssayType("NextGen");
	    		dataList = seqSeriesAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending,paramBean.getMicWhereclause());
	    		//TODO Write column total queries.
				//setTotalslist(micSeriesAssembler.getTotals());
				totalRows = seqSeriesAssembler.count();
    		}
    		if(specimenAssay.equals("seqsample")) {
    			seqSampleAssembler.setAssayType("NextGen");
	    		dataList = seqSampleAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending,paramBean.getMicWhereclause());
	    		//TODO Write column total queries.
				//setTotalslist(micSeriesAssembler.getTotals());
				totalRows = seqSampleAssembler.count();
    		}
    	}
    	/*else {
    		dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
					paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause);
			// Set currentPage, totalPages and pages.
			setTotalslist(assembler.getTotals());
			totalRows = assembler.count();
    	}*/
    	else if(assayType.equals("TG"))
    	{
    		dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
					paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause);
			// Set currentPage, totalPages and pages.
			setTotalslist(assembler.getTotals());
			totalRows = assembler.count();
    	}
    	else if(assayType.equals("IHC"))
    	{
    		dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
					paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause);
					// Set currentPage, totalPages and pages.
					setTotalslist(assembler.getTotals());
					totalRows = assembler.count();
    	}
    	else if(assayType.equals("ISH"))
    	{
    		if(specimenAssay.equals("expression")){
    			dataList = expressionAssembler.getData(firstRow, rowsPerPage, sortField, sortAscending);
				// Set currentPage, totalPages and pages.
				//setTotalslist(assembler.getTotals());
				totalRows = expressionAssembler.count();
    		}
    		else {
		    	dataList = assembler.getData(firstRow, rowsPerPage, sortField, sortAscending, paramBean.getWhereclause(),
		    									paramBean.getFocusGroupWhereclause(),paramBean.getExpressionJoin(),specimenWhereclause);
		        // Set currentPage, totalPages and pages.
		    	setTotalslist(assembler.getTotals());
		    	totalRows = assembler.count();
    		}
    	}
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
    
    public void refresh(){
    	loadDataList();
    }
    
    public void resetAll() {
		paramBean.resetAll();
		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public void resetAllArraySeq() {
		paramBean.resetAll();
		//must return to homepage to reset focus group. Can't refresh div on other page
		//paramBean.setFocusGroup("reset");
		loadDataList();
	}
    
    public String checkboxSelections() { 
    	//List<InsituTableBeanModel> items = (List<InsituTableBeanModel>)dataList;
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((InsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			if(paramBean.getCollectionType().equalsIgnoreCase("Entries"))
    				selectedItems.add(((InsituTableBeanModel) dataList.get(i)).getOid());
    			else if(paramBean.getCollectionType().equalsIgnoreCase("Genes"))
    				selectedItems.add(((InsituTableBeanModel) dataList.get(i)).getGene_id());
    		} 
    	} // do what you need to do with selected items } - See more at: http://www.stevideter.com/2008/10/09/finding-selected-checkbox-items-in-a-jsf-datatable/#sthash.FR6VuSyV.dpuf
    	//return "browseCollectionEntriesTablePage";
    	return "result";
    }
    
    public String clearCheckboxSelections() {
    	selectedItems.clear();
    	return "result";
    }
    
    //Test for running javascript from view
    public String processCheckboxSelections() { 
    	selectedItems.clear();
    	for (int i=0;i<dataList.size();i++) { 
    		if (((InsituTableBeanModel) dataList.get(i)).getSelected()) { 
    			selectedItems.add(((InsituTableBeanModel) dataList.get(i)).getOid()); 
    		} 
    	}    	
    	//return getSelectedItemstoStringBuffer();
    	return null;
    }
    
    public void checkAll() { 
    	areAllChecked=(areAllChecked)?false:true;
    	for (int i=0;i<dataList.size();i++) { 
    		((InsituTableBeanModel)dataList.get(i)).setSelected(areAllChecked);
    	} 
    }
    
    //create comma separated string of selected checkbox items created in checkboxSelections()
    public String getSelectedItemstoString(){
    	String str="";
    	if(selectedItems!=null) {
	    	for(String s : selectedItems){
	    		str+=s + ", ";
	    	}
    	}
    	return str;
    }
    
  
    //create string of ; separated values for passing to parameterized query. Surround String in single quotes to allow javascript to read it
    public String getSelectedItemstoStringBuffer(){
    	StringBuffer str=new StringBuffer();
    	
    	if(selectedItems!=null) {
    		str.append("'");
	    	for(String s : selectedItems){
	    		str.append(s+";");
	    	}
	    	str.append("'");
    	}
    	
    	return str.toString();
    }
    
    
    public String getQueryTotals() {
		return queryTotals;
	}
    
   /* public String prepareTable() {
    	loadDataList();
    	return "browseGeneListTablePage";
    }*/
    
    public void setUserInput(String input, boolean type){
    	if(input==null || input.equals(""))
    		this.userInput="";
    	else {
	    	String processedValues[] = Utils.processInputString(input,type);
	    	this.userInputQuery = processedValues[0];
	    	this.userInput = processedValues[1];
    	}
    }
       
    
    public String getUserInput(){
    	return userInput;
    }
    
    public String getUserInputQuery() {
    	return userInputQuery;
    }
    
    public void setWildcard(String wildcard){
    	this.wildcard = wildcard;
    }
    
    public String getWildcard() {
    	return wildcard;
    }
   
}
