package org.gudmap.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gudmap.assemblers.ParamBeanAssembler;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.utils.Utils;
import org.primefaces.event.SelectEvent;

@Named(value="paramBean")
@SessionScoped
public class ParamBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//false=production
	private boolean debug=false;
	private boolean multiwhere=false;
	
	private ParamBeanAssembler assembler;
	
	private String focusGroup="reset";
	private String params="";
	private boolean isLoggedIn=false;
	private String assayType="ISH";
	/*columns*/
	private boolean oidcol=false;
	private boolean genecol=true;
	private boolean gudmapaccessioncol=true;
	private boolean sourcecol=false;
	private boolean submissiondatecol=false;
	private boolean assaytypecol=true;
	private boolean probenamecol=false;
	private boolean embryostagecol=true;
	private boolean speciescol=false;
	private boolean agecol=true;
	private boolean sexcol=true;
	private boolean genotypecol=true;
	private boolean tissuecol=true;
	private boolean expressioncol=false;
	private boolean specimentypecol=false;
	private boolean imagescol=true;
	/*genestrip cols*/
	private boolean synonymcol=false;
	private boolean omimcol=true;
	private boolean rnaseqcol=true;
	private boolean ishexpressioncol=true;
	private boolean arrayexpressioncol=true;
	/*column structures*/
	private String[] insitucols;
	private String[] tgcols;
	private String[] genestripcols;
	private String[] micseriescols;
	private String[] micsamplecols;
	private String[] seqsamplecols;
	private String[] micplatformcols;
	private String[] seqseriescols;
	private String[] collectioncols;
	private Map<String,Boolean> resultmap;
	private Map<String,Boolean> tgresultmap;
	private Map<String,Boolean> genestripresultmap;
	private Map<String,Boolean> micseriesresultmap;
	private Map<String,Boolean> micsampleresultmap;
	private Map<String,Boolean> seqsampleresultmap;
	private Map<String,Boolean> micplatformresultmap;
	private Map<String,Boolean> seqseriesresultmap;
	private Map<String,Boolean> collectionresultmap;

	
	/*filter*/
	private String genevalues;
	private String geneIdvalues;
	private String[] sourcevalues;
	private Date fromdatevalues;
	private Date todatevalues;
	private String todatemysql;
	private String fromdatemysql;
	private String[] assaytypeinsituvalues;
	private String probenamevalues;
	private String theilerstagefromvalues;
	private String theilerstagetovalues;
	private String carnegiestagefromvalues;
	private String carnegiestagetovalues;
	private String sexvalues="ALL";
	private String specimentypevalues;
	
	private String[] assaysourcevalues;
	private String seqlibstrategyvalues="ALL";
	private String arrayplatformvalues="ALL";
	private String seqsexvalues="ALL";
	private String arraysexvalues="ALL";
	
	private int col_statusvalues=2;
	private int col_optionvalues=0;
	private int col_idvalues=-1;
	
	private String whereclause=" WHERE ";
	private String cachewhereclause=" WHERE ";
	private String cacheprefix="QIC_";
	private String arraycachewhereclause=" WHERE ";
	private String arraycacheprefix="QMC_";
	private String tempfromvalues;
	private String temptheilervalues;
	private String tempcarnegievalues;
    private String stagecondition=" AND ";
	
	//microarray
	private String micWhereclause="";
	private boolean mic_titlecol=true;
	private boolean mic_geoSeriesIDcol=false;
	private boolean mic_numsamplescol=true;
	private boolean mic_platformIDcol=false;
	private boolean mic_platformnamecol=true;
	private boolean mic_platformtechnologycol=true;
	private boolean mic_platformmanufacturercol=true;
	private boolean mic_numseriescol=true;
	private boolean mic_sourcecol=false;
	private boolean mic_stagecol=true;
	private boolean mic_agecol=true;
	private boolean mic_submissiondatecol=false;
	private boolean mic_sexcol=true;
	private boolean mic_genotypecol=true;
	private boolean mic_geoSampleIDcol=false;
	private boolean mic_sampledescriptioncol=true;
	private boolean mic_samplenamecol=false;
	private boolean mic_componentscol=true;
	private boolean mic_gudmapaccessioncol=true;
	private boolean mic_librarystrategycol=false;
	private boolean mic_sampleplatformIDcol=false;
	private boolean mic_samplelibrarystrategycol = false;
	

	
	
	//checkboxes
	private boolean[]checkboxes;
	
	//disease
	private String annotationtypevalues="Direct Annotations";
	private String isDirect="false";
	
	SimpleDateFormat sdf;
	
	/*gene search options*/
	private String geneoptionvalues="Expression Summaries";
	
	/*url display options*/
	private String urloptionvalues="Hide URL";
	private String urlShowHide="Show URL";
	
	//image upload directories
	private String imageDir="general";
	
	//web file upload directories
	private String webfileDir="general";
	
	//page editing
	private String pageId="1";
	
	//page categories
	private String pageCategory="About";
	
	//collections
	private String collectionType="Entries";
	private String localStorage="";
	private String totalLocalStorage="";
	private String species="ALL";
	private boolean col_namecol=true;
	private boolean col_descriptioncol=true;
	private boolean col_ownercol=true;
	private boolean col_focusgroupcol=true;
	private boolean col_countcol=true;
	private boolean col_statuscol=true;
	private boolean col_modifiedcol=true;
	private boolean col_downloadcol=true;
	private boolean col_sharecol=true;
	
	//charts
	private int assayTypeIndex=0;
	
	private boolean autoAction=false;
	
		
	public ParamBean() {
		//DEFAULT COLUMNS TO SHOW
		insitucols= new String[]{"gene","gudmapaccession","assaytype","age","embryostage","sex","genotype","tissue"};
		tgcols= new String[]{"gene","gudmapaccession","assaytype","embryostage","age","sex","genotype","tissue"};
		genestripcols= new String[]{"gene","expressionprofile","images","microarrayprofile","rnaseq","stagerange","omim"};
		micseriescols= new String[]{"title","numsamples","components"};
		micsamplecols= new String[]{"gudmapaccession","sampledescription","age","stage","sex","genotype","components"};
		seqsamplecols= new String[]{"gudmapaccession","sampledescription","age","stage","sex","genotype","components"};
		seqseriescols= new String[]{"title","numsamples","components"};		
		micplatformcols= new String[]{"geoplatformid","platformname","platformtechnology","platformmanufacturer","numseries"};		
		collectioncols= new String[]{"name","description","owner","count","focusgroup","status","modified","download","share"};

		resultmap=new HashMap<String,Boolean>();
		tgresultmap=new HashMap<String,Boolean>();
		genestripresultmap=new HashMap<String,Boolean>();
		micseriesresultmap=new HashMap<String,Boolean>();
		micsampleresultmap=new HashMap<String,Boolean>();
		seqsampleresultmap=new HashMap<String,Boolean>();
		micplatformresultmap=new HashMap<String,Boolean>();
		seqseriesresultmap=new HashMap<String,Boolean>();
		collectionresultmap=new HashMap<String,Boolean>();
		
		assembler = new ParamBeanAssembler();
		sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	}
	
	public void setMultiwhere(boolean multiwhere){
		this.multiwhere = multiwhere;
	}
	
	public boolean getMultiwhere() {
		return multiwhere;
	}
	
	public void setFocusGroup(String focusGroup){
		this.focusGroup=focusGroup;
	}
	public String getFocusGroup(){
		return focusGroup;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn){
		this.isLoggedIn=isLoggedIn;
	}
	public boolean getIsLoggedIn(){
		return isLoggedIn;
	}
	
	public void setAssayType(String assayType){
		this.assayType=assayType;
	}
	public String getAssayType(){
		return assayType;
	}
	
	public void setOidcol(boolean oidcol){
		this.oidcol=oidcol;
	}
	
	public boolean getOidcol(){
		return oidcol;
	}
	
	public void setGudmapaccessioncol(boolean gudmapaccessioncol){
		this.gudmapaccessioncol=gudmapaccessioncol;
	}
	
	public boolean getGudmapaccessioncol(){
		return gudmapaccessioncol;
	}
	
	public void setGenecol(boolean genecol){
		this.genecol=genecol;
	}
	
	public boolean getGenecol(){
		return genecol;
	}
	
	public void setSourcecol(boolean sourcecol){
		this.sourcecol=sourcecol;
	}
	
	public boolean getSourcecol(){
		return sourcecol;
	}
	
	public void setSubmissiondatecol(boolean submissiondatecol){
		this.submissiondatecol=submissiondatecol;
	}
	
	public boolean getSubmissiondatecol(){
		return submissiondatecol;
	}
	
	public void setAssaytypecol(boolean assaytypecol){
		this.assaytypecol=assaytypecol;
	}
	
	public boolean getAssaytypecol(){
		return assaytypecol;
	}
	
	public void setProbenamecol(boolean probenamecol){
		this.probenamecol=probenamecol;
	}
	
	public boolean getProbenamecol(){
		return probenamecol;
	}
	
	public void setEmbryostagecol(boolean embryostagecol){
		this.embryostagecol=embryostagecol;
	}
	
	public boolean getEmbryostagecol(){
		return embryostagecol;
	}
	
	public void setSpeciescol(boolean speciescol){
		this.speciescol=speciescol;
	}
	
	public boolean getSpeciescol(){
		return speciescol;
	}
	
	public void setAgecol(boolean agecol){
		this.agecol=agecol;
	}
	
	public boolean getAgecol(){
		return agecol;
	}
	
	public void setSexcol(boolean sexcol){
		this.sexcol=sexcol;
	}
	
	public boolean getSexcol(){
		return sexcol;
	}
	
	public void setGenotypecol(boolean genotypecol){
		this.genotypecol=genotypecol;
	}
	
	public boolean getGenotypecol(){
		return genotypecol;
	}
	
	public void setTissuecol(boolean tissuecol){
		this.tissuecol=tissuecol;
	}
	
	public boolean getTissuecol(){
		return tissuecol;
	}
	
	public void setExpressioncol(boolean expressioncol){
		this.expressioncol=expressioncol;
	}
	
	public boolean getExpressioncol(){
		return expressioncol;
	}
	
	public void setSecimentypecol(boolean specimentypecol){
		this.specimentypecol=specimentypecol;
	}
	
	public boolean getSpecimentypecol(){
		return specimentypecol;
	}
	
	public void setImagescol(boolean imagescol){
		this.imagescol=imagescol;
	}
	
	public boolean getImagescol(){
		return imagescol;
	}
	
	public void setSynonymcol(boolean synonymcol ) {
		this.synonymcol=synonymcol;
	}
	
	public boolean getSynonymcol() {
		return synonymcol;
	}
	
	public void setOmimcol(boolean omimcol ) {
		this.omimcol=omimcol;
	}
	
	public boolean getOmimcol() {
		return omimcol;
	}
	
	public void setRnaseqcol(boolean rnaseqcol ) {
		this.rnaseqcol=rnaseqcol;
	}
	
	public boolean getRnaseqcol() {
		return rnaseqcol;
	}
	public void setIshexpressioncol(boolean ishexpressioncol ) {
		this.ishexpressioncol=ishexpressioncol;
	}
	
	public boolean getIshexpressioncol() {
		return ishexpressioncol;
	}
	
	public void setArrayexpressioncol(boolean arrayexpressioncol ) {
		this.arrayexpressioncol=arrayexpressioncol;
	}
	
	public boolean getArrayexpressioncol() {
		return arrayexpressioncol;
	}
	
	public void setInsitucols(String[]insitucols){
		this.insitucols=insitucols;
		resultmap.clear();
		for(int i=0;i<insitucols.length;i++){
			resultmap.put(insitucols[i], true);	
		}
		oidcol=false;
		genecol=resultmap.containsKey("gene");
		gudmapaccessioncol=resultmap.containsKey("gudmapaccession");
		sourcecol=resultmap.containsKey("source");
		submissiondatecol=resultmap.containsKey("submissiondate");
		assaytypecol=resultmap.containsKey("assaytype");
		probenamecol=resultmap.containsKey("probename");
		embryostagecol=resultmap.containsKey("embryostage");
		speciescol=resultmap.containsKey("species");
		agecol=resultmap.containsKey("age");
		sexcol=resultmap.containsKey("sex");
		genotypecol=resultmap.containsKey("genotype");
		tissuecol=resultmap.containsKey("tissue");
		expressioncol=resultmap.containsKey("expression");
		specimentypecol=resultmap.containsKey("specimentype");
		//imagescol=resultmap.containsKey("images");
	}
	
	
	public String[] getInsitucols(){
		return insitucols;
	}
	
	
	
	private static Map<String,Object> insitucolmap;
	static{
		insitucolmap = new LinkedHashMap<String,Object>();
		//insitucolmap.put("Oid", "oid"); //label, value
		insitucolmap.put("Gene", "gene");
		insitucolmap.put("Assay Detail", "gudmapaccession");
		//insitucolmap.put("Images", "images");
		insitucolmap.put("Assay Type", "assaytype");
		insitucolmap.put("Specimen Type", "specimentype");
		insitucolmap.put("Species", "species");
		insitucolmap.put("Age", "age");
		insitucolmap.put("Stage", "embryostage");
		insitucolmap.put("Sex", "sex");
		insitucolmap.put("Genotype", "genotype");
		insitucolmap.put("Tissue", "tissue");
		insitucolmap.put("Expression", "expression");
		insitucolmap.put("Probe Name", "probename");
		insitucolmap.put("Source", "source");
		insitucolmap.put("Submission Date", "submissiondate");
	}
 
	public Map<String,Object> getInsitucolmap() {
		return insitucolmap;
	}
 
	public String getInsitucolsInString() {
		return Arrays.toString(insitucols);
	}
	
	public void setTgcols(String[]tgcols){
		this.tgcols=tgcols;
		tgresultmap.clear();
		for(int i=0;i<tgcols.length;i++){
			tgresultmap.put(tgcols[i], true);	
		}
		oidcol=false;
		genecol=tgresultmap.containsKey("gene");
		gudmapaccessioncol=tgresultmap.containsKey("gudmapaccession");
		sourcecol=tgresultmap.containsKey("source");
		submissiondatecol=tgresultmap.containsKey("submissiondate");
		assaytypecol=tgresultmap.containsKey("assaytype");
		//probenamecol=tgresultmap.containsKey("probename");
		embryostagecol=tgresultmap.containsKey("embryostage");
		speciescol=tgresultmap.containsKey("species");
		agecol=tgresultmap.containsKey("age");
		sexcol=tgresultmap.containsKey("sex");
		genotypecol=tgresultmap.containsKey("genotype");
		tissuecol=tgresultmap.containsKey("tissue");
		expressioncol=tgresultmap.containsKey("expression");
		specimentypecol=tgresultmap.containsKey("specimentype");
		//imagescol=tgresultmap.containsKey("images");
	}
	
	
	public String[] getTgcols(){
		return tgcols;
	}
	
	private static Map<String,Object> tgcolmap;
	static{
		tgcolmap = new LinkedHashMap<String,Object>();
		//tgcolmap.put("Oid", "oid"); //label, value
		tgcolmap.put("Gene", "gene");
		tgcolmap.put("Assay Detail", "gudmapaccession");		
		tgcolmap.put("Assay Type", "assaytype");
		tgcolmap.put("Specimen Type", "specimentype");
		//tgcolmap.put("Probe Name", "probename");
		tgcolmap.put("Species", "species");
		tgcolmap.put("Age", "age");
		tgcolmap.put("Stage", "embryostage");		
		tgcolmap.put("Sex", "sex");
		tgcolmap.put("Genotype", "genotype");
		tgcolmap.put("Tissue", "tissue");
		tgcolmap.put("Expression", "expression");
		tgcolmap.put("Source", "source");
		tgcolmap.put("Submission Date", "submissiondate");
		
		//tgcolmap.put("Images", "images");
	}
 
	public Map<String,Object> getTgcolmap() {
		return tgcolmap;
	}
	
	public void setGenestripcols(String[]genestripcols){
		this.genestripcols=genestripcols;
		genestripresultmap.clear();
		for(int i=0;i<genestripcols.length;i++){
			genestripresultmap.put(genestripcols[i], true);	
		}
		oidcol=false;
		genecol=genestripresultmap.containsKey("gene");
		synonymcol=genestripresultmap.containsKey("synonym");
		speciescol=genestripresultmap.containsKey("species");
		omimcol=genestripresultmap.containsKey("omim");
		embryostagecol=genestripresultmap.containsKey("stagerange");
		ishexpressioncol=genestripresultmap.containsKey("expressionprofile");
		imagescol=genestripresultmap.containsKey("images");
		arrayexpressioncol=genestripresultmap.containsKey("microarrayprofile");
		rnaseqcol=genestripresultmap.containsKey("rnaseq");
	}
	
	public String[] getGenestripcols(){
		return genestripcols;
	}
	
	
	private static Map<String,Object> genestripcolmap;
	static{
		genestripcolmap = new LinkedHashMap<String,Object>();
		//insitucolmap.put("Oid", "oid"); //label, value
		genestripcolmap.put("Gene", "gene");
		genestripcolmap.put("Synonyms", "synonym");
		genestripcolmap.put("Species", "species");
		genestripcolmap.put("In situ Expression", "expressionprofile");
		genestripcolmap.put("Images", "images");
		genestripcolmap.put("Microarray Profile", "microarrayprofile");
		genestripcolmap.put("Sequence", "rnaseq");
		genestripcolmap.put("Stages", "stagerange");
		genestripcolmap.put("Disease", "omim");
		
	}
 
	public Map<String,Object> getGenestripcolmap() {
		return genestripcolmap;
	}

	
	
	/*************************FILTER*******************************/
	
	/*************************populate filter*******************/
	
	public Map<String,String> getSourcelist(){
		return assembler.getSourcelist(); 
	}
	
	public Map<String,String> getAssaysourcelist(String assayType){
		return assembler.getAssaysourcelist(assayType); 
	}
	
	public Map<String,String> getAssaytypeinsitulist(){
		return assembler.getAssaytypeinsitulist();
	}
	
	public Map<String,String> getAllassaytypelist(){
		return assembler.getAllassaytypelist();
	}
	
	public Map<String,String> getSpecieslist(){
		return assembler.getSpecieslist();
	}
	
	public Map<String,String> getTheilerstagelist(boolean direction){
		return assembler.getTheilerstagelist(direction);
	}
	
	public Map<String,String> getCarnegiestagelist(boolean direction){
		return assembler.getCarnegiestagelist(direction);
	}
	
	public Map<String,String> getSexlist(){
		return assembler.getSexlist();
	}
	
	public Map<String,String> getSpecimentypelist(){
		return assembler.getSpecimentypelist();
	}
	
	public Map<String,String> getArrayplatformlist(){
		return assembler.getArrayplatformlist();
	}
	
	public Map<String,String> getSeqlibstrategylist(){
		return assembler.getSeqlibstrategylist();
	}
	
	public Map<String,Integer> getCollectionstatuslist(){
		return assembler.getCollectionstatuslist();
	}
	
	public Map<String,Integer> getCollectionoptionlist(){
		return assembler.getCollectionoptionlist();
	}
	
	/**gene search options***/
	public Map<String,String> getGeneoptionlist(){
		return assembler.getGeneoptionlist();
	}
	
	/**URL display options***/
	public Map<String,String> getUrloptionlist(){
		return assembler.getUrloptionlist();
	}

/*******************************
 * getters and setters for filter outcomes 
 * CHECK FOR NULL VALUE WHEN THE COMPONENT IN FILTER VIEW MIGHT BE COMMENTED OUT
 * ************************/
	private String sourcevalueclause="";
	private String cachesourcevalueclause="";
	public void setSourcevalues(String[] sourcevalues){
		this.sourcevalues=sourcevalues;
		if(sourcevalues!=null){
			if(sourcevalues.length>0){
				String str="";
				for(int i=0;i<sourcevalues.length;i++){
					str+="'"+sourcevalues[i]+"',";
				}
				//whereclause+="SUB_SOURCE IN ("+Utils.removeLastChar(str, ',')+") AND ";
				sourcevalueclause="SUB_SOURCE IN ("+Utils.removeLastChar(str, ',')+") AND ";
				cachesourcevalueclause=cacheprefix+sourcevalueclause;
			}
		}
	}
	public String[] getSourcevalues(){
		return sourcevalues;
	}
	
	public String getSourcevaluesInString() {
		return Arrays.toString(sourcevalues);
	}
	
	public void setFromdatevalues(Date fromdatevalues){
		if(fromdatevalues!=null && !fromdatevalues.equals(""))
		{
			this.fromdatevalues=fromdatevalues;
			fromdatemysql = sdf.format(fromdatevalues);
			tempfromvalues="SUB_SUB_DATE BETWEEN '"+fromdatemysql+"' AND '";
		}
	}
	
	public Date getFromdatevalues(){
		return fromdatevalues;
	}
	
	public String getFromdatemysql() {
		return fromdatemysql;
	}
	
	private String datevalueclause="";
	private String cachedatevalueclause="";
	public void setTodatevalues(Date todatevalues){
		if(todatevalues!=null && !todatevalues.equals(""))
		{
			this.todatevalues=todatevalues;
			todatemysql = sdf.format(todatevalues);
			if(getFromdatevalues()!=null && !getFromdatevalues().equals("")){
				tempfromvalues+=todatemysql+"'";
				//whereclause+=tempfromvalues+" AND ";
				datevalueclause=tempfromvalues+" AND ";
				cachedatevalueclause=cacheprefix+datevalueclause;
			}
		}
	}
	
	public Date getTodatevalues(){
		return todatevalues;
	}
	
	public String getTodatemysql() {
		return todatemysql;
	}
	
	private String assaytypevalueclause="";
	private String cacheassaytypevalueclause="";
	public void setAssaytypeinsituvalues(String[] assaytypeinsituvalues){
		this.assaytypeinsituvalues=assaytypeinsituvalues;
		if(assaytypeinsituvalues!=null){
			if(assaytypeinsituvalues.length>0){
				String str="";
				for(int i=0;i<assaytypeinsituvalues.length;i++){
					str+="'"+assaytypeinsituvalues[i]+"',";
				}
				//whereclause+="SUB_ASSAY_TYPE IN ("+Utils.removeLastChar(str, ',')+") AND ";
				assaytypevalueclause="SUB_ASSAY_TYPE IN ("+Utils.removeLastChar(str, ',')+") AND ";
				cacheassaytypevalueclause=cacheprefix+assaytypevalueclause;
			}
		}
		
	}
	public String[] getAssaytypeinsituvalues(){
		return assaytypeinsituvalues;
	}
	
	public String getAssaytypeinsituvaluesInString() {
		return Arrays.toString(assaytypeinsituvalues);
	}
	//used to remove this from the mic cache query
	public String getCacheassaytypevalueclause() {
		return cacheassaytypevalueclause;
	}
	
	public void setSpecies(String species) {
		this.species = species;
	}
	
	public String getSpecies() {
		return species;
	}
	/////////////THEILER STAGE///////////////////////
	private String theilerstagevalueclause="";
	private String cachetheilerstagevalueclause="";
	public void setTheilerstagefromvalues(String theilerstagefromvalues){
		this.theilerstagefromvalues=theilerstagefromvalues;
		if(theilerstagefromvalues!=null){
			if(!theilerstagefromvalues.equals("ALL")){
				temptheilervalues="STG_SPECIES='Mus musculus' AND STG_ORDER BETWEEN "+theilerstagefromvalues+" AND ";
			}
			if(theilerstagefromvalues.equals("ALL")){
				theilerstagevalueclause="";
				cachetheilerstagevalueclause="";
			}
		}
	}
	public String getTheilerstagefromvalues(){
		return theilerstagefromvalues;
	}
	
	
	public void setTheilerstagetovalues(String theilerstagetovalues){
		this.theilerstagetovalues=theilerstagetovalues;
		if(theilerstagetovalues!=null){
			if(getTheilerstagefromvalues()!=null && !getTheilerstagefromvalues().equals("") && !getTheilerstagefromvalues().equals("ALL") && !theilerstagetovalues.equals("ALL")){
				temptheilervalues+=theilerstagetovalues;
				//theilerstagevalueclause=temptheilervalues+" AND ";
				theilerstagevalueclause=temptheilervalues+" AND ";
				cachetheilerstagevalueclause=cacheprefix+theilerstagevalueclause;
			}
			if(theilerstagetovalues.equals("ALL")){
				theilerstagevalueclause="";
				cachetheilerstagevalueclause="";
			}
			
		}
	}
	
	public String getTheilerstagetovalues(){
		return theilerstagetovalues;
	}
	///////////////////CARNEGIE STAGE/////////////////////
	
	private String carnegiestagevalueclause="";
	private String cachecarnegiestagevalueclause="";
	public void setCarnegiestagefromvalues(String carnegiestagefromvalues){
		this.carnegiestagefromvalues=carnegiestagefromvalues;
		if(carnegiestagefromvalues!=null){
			if(!carnegiestagefromvalues.equals("ALL")){
				tempcarnegievalues="STG_SPECIES='Homo sapiens' AND STG_ORDER BETWEEN "+carnegiestagefromvalues+" AND ";
			}
			if(carnegiestagefromvalues.equals("ALL")){
				carnegiestagevalueclause="";
				cachecarnegiestagevalueclause="";
			}
		}
	}
	public String getCarnegiestagefromvalues(){
		return carnegiestagefromvalues;
	}
	
	
	public void setCarnegiestagetovalues(String carnegiestagetovalues){
		this.carnegiestagetovalues=carnegiestagetovalues;
		if(carnegiestagetovalues!=null){
			if(getCarnegiestagefromvalues()!=null && !getCarnegiestagefromvalues().equals("") && !getCarnegiestagefromvalues().equals("ALL") && !carnegiestagetovalues.equals("ALL")){
				tempcarnegievalues+=carnegiestagetovalues;
				carnegiestagevalueclause=tempcarnegievalues+" AND ";
				cachecarnegiestagevalueclause=cacheprefix+carnegiestagevalueclause;
			}
			if(carnegiestagetovalues.equals("ALL")){
				carnegiestagevalueclause="";
				cachecarnegiestagevalueclause="";
			}
			
		}
	}
	
	public String getCarnegiestagetovalues(){
		return carnegiestagetovalues;
	}
	
	//////////////////////////////////////////////////////
	private String sexvalueclause="";
	private String cachesexvalueclause="";
	public void setSexvalues(String sexvalues){
		this.sexvalues=sexvalues;
		if(!sexvalues.equals("")) {
				sexvalueclause="SPN_SEX = '"+sexvalues+"' AND ";
				cachesexvalueclause=cacheprefix+sexvalueclause;
		}
		if(sexvalues.equals("ALL")) {
			sexvalueclause="";	
			cachesexvalueclause="";
		}
	}
	
	public String getSexvalues(){
		return sexvalues;
	}
	
	private String specimentypevalueclause="";
	private String cachespecimentypevalueclause="";
	public void setSpecimentypevalues(String specimentypevalues){
		this.specimentypevalues=specimentypevalues;
		if(!specimentypevalues.equals("")) {
			//whereclause+="SPN_ASSAY_TYPE = '"+specimentypevalues+"' AND ";
			specimentypevalueclause="SPN_ASSAY_TYPE = '"+specimentypevalues+"' AND ";
			cachespecimentypevalueclause=cacheprefix+specimentypevalueclause;
		}
	}
	
	public String getSpecimentypevalues(){
		return specimentypevalues;
	}
	
	private String genevalueclause="";
	private String cachegenevalueclause="";
	
	public void setGenevalues(String genevalues){
		this.genevalues=genevalues;
		if(!genevalues.equals("")) {
			//whereclause+="RPR_SYMBOL = '"+genevalues+"' AND ";
			genevalueclause="RPR_SYMBOL = '"+genevalues+"' AND ";
			cachegenevalueclause=cacheprefix+genevalueclause;
		}
	}

	public String getGenevalues(){
		return genevalues;
	}
	
	private String geneIdvalueclause="";
	private String cachegeneIdvalueclause="";
	
	public void setGeneIdvalues(String geneIdvalues){
		this.geneIdvalues=geneIdvalues;
		if(!geneIdvalues.equals("")) {
			geneIdvalueclause="RPR_LOCUS_TAG = '"+geneIdvalues+"' AND ";
			cachegeneIdvalueclause=cacheprefix+geneIdvalueclause;
		}
	}

	public String getGeneIdvalues(){
		return geneIdvalues;
	}
	
	//used to remove this from the mic cache query
		public String getCachegenevalueclause() {
			return cachegenevalueclause;
		}
	
	private String probenamevalueclause="";
	private String cacheprobenamevalueclause="";
	public void setProbenamevalues(String probenamevalues){
		this.probenamevalues=probenamevalues;
		if(!probenamevalues.equals("")) {
			//whereclause+="RPR_JAX_ACC = '"+probenamevalues+"' AND ";
			probenamevalueclause="RPR_JAX_ACC = '"+probenamevalues+"' AND ";
			cacheprobenamevalueclause=cacheprefix+probenamevalueclause;
		}
	}
	
	public String getProbenamevalues(){
		return probenamevalues;
	}
	
	
	/**************array and seq ***************/
	private String platformvalueclause="";
	private String cacheplatformvalueclause="";
	public void setArrayplatformvalues(String arrayplatformvalues) {
		this.arrayplatformvalues = arrayplatformvalues;
		
		if(!arrayplatformvalues.equals("")) {
			platformvalueclause="PLT_GEO_ID = '"+arrayplatformvalues+"' AND ";
			cacheplatformvalueclause=cacheprefix+platformvalueclause;
		}
		if(arrayplatformvalues.equals("ALL")) {
			platformvalueclause="";	
			cacheplatformvalueclause="";
		}
	}
	
	public String getArrayplatformvalues () {
		return arrayplatformvalues;
	}
	
	private String libstrategyvalueclause="";
	private String cachelibstrategyvalueclause="";
	public void setSeqlibstrategyvalues(String seqlibstrategyvalues) {
		this.seqlibstrategyvalues = seqlibstrategyvalues;
		if(!seqlibstrategyvalues.equals("")) {
			libstrategyvalueclause="NGP_LIBRARY_STRATEGY = '"+seqlibstrategyvalues+"' AND ";
			cachelibstrategyvalueclause=cacheprefix+libstrategyvalueclause;
		}
		if(seqlibstrategyvalues.equals("ALL")) {
			libstrategyvalueclause="";	
			cachelibstrategyvalueclause="";
		}
		
	}
	
	public String getSeqlibstrategyvalues () {
		return seqlibstrategyvalues;
	}
	
	public void setAssaysourcevalues(String[] assaysourcevalues) {
		this.assaysourcevalues = assaysourcevalues;
		setSourcevalues(assaysourcevalues);
	}
	public String[] getAssaysourcevalues(){
		return assaysourcevalues;
	}
	
	private String seqsexvalueclause="";
	private String cacheseqsexvalueclause="";
	public void setSeqsexvalues(String seqsexvalues){
		this.seqsexvalues=seqsexvalues;
		if(!seqsexvalues.equals("")) {
			seqsexvalueclause="NGS_SEX = '"+seqsexvalues+"' AND ";
			cacheseqsexvalueclause=cacheprefix+seqsexvalueclause;
		}
		if(seqsexvalues.equals("ALL")) {
			seqsexvalueclause="";	
			cacheseqsexvalueclause="";
		}
	}
	
	public String getSeqsexvalues(){
		return seqsexvalues;
	}
	
	private String arraysexvalueclause="";
	private String cachearraysexvalueclause="";
	public void setArraysexvalues(String arraysexvalues){
		this.arraysexvalues=arraysexvalues;
		if(!arraysexvalues.equals("")) {
			arraysexvalueclause="SMP_SEX = '"+arraysexvalues+"' AND ";
			cachearraysexvalueclause=cacheprefix+arraysexvalueclause;
		}
		if(arraysexvalues.equals("ALL")) {
			arraysexvalueclause="";	
			cachearraysexvalueclause="";
		}
	}
	
	public String getArraysexvalues(){
		return arraysexvalues;
	}
	
	/*****gene search options**********/
	public void setGeneoptionvalues(String geneoptionvalues){
		this.geneoptionvalues = geneoptionvalues;
	}
	
	public String getGeneoptionvalues() {
		return geneoptionvalues;
	}
	
	/*****URL display options**********/
	public void setUrloptionvalues(String urloptionvalues){
		if(urloptionvalues.equals("change")) {
			if(this.urlShowHide.equals("Show URL")) {
					this.urloptionvalues="Show URL";
					this.urlShowHide="Hide URL";
			}
			else {
				this.urloptionvalues="Hide URL";
				this.urlShowHide="Show URL";
			}
		}
		else
			this.urloptionvalues=urloptionvalues;
		
	}
	
	public String getUrloptionvalues() {
		return urloptionvalues;
	}
	
	public void setUrlShowHide(String urlShowHide) {
		this.urlShowHide =  urlShowHide;
	}
	
	public String getUrlShowHide() {
		return urlShowHide;
	}
	
	/********************collection save options************/
	
	public void setCol_optionvalues(int col_optionvalues) {
		this.col_optionvalues = col_optionvalues;
	}
	
	public int getCol_optionvalues() {
		return col_optionvalues;
	}
	
	/**********************collection filter*************/
	
	private String col_statusvalueclause="";
	public void setCol_statusvalues(int col_statusvalues){
		this.col_statusvalues=col_statusvalues;
		if(col_statusvalues<2) {
			col_statusvalueclause="CLN_STATUS = "+col_statusvalues+" AND ";
		}
		if(col_statusvalues==2) {
			col_statusvalueclause="";	
		}
	}
	
	public int getCol_statusvalues(){
		return col_statusvalues;
	}
	
	
	/*****************today*******************/
	public Date getMaxDate() {
	    // wherever you want the date to come from
	    return new Date();
	 }
	
	/****************where clause**************/
	
	public void setWhereclause(String whereclause){
		this.whereclause=whereclause;
	}
	
	public String getWhereclause(){
		StringBuffer stagebuffer = new StringBuffer(theilerstagevalueclause+carnegiestagevalueclause);
		if(stagebuffer.indexOf("Mus")>0 && stagebuffer.indexOf("Homo")>0) {
			stagebuffer.insert(0, "((");
			stagebuffer.insert(stagebuffer.lastIndexOf("AND")-1, "))");
			stagebuffer.replace(stagebuffer.lastIndexOf("STG_SPECIES")-4, stagebuffer.lastIndexOf("STG_SPECIES")-1, ") OR (");			
		}
		whereclause=GenericQueries.WHERE_CLAUSE+sourcevalueclause+datevalueclause+assaytypevalueclause+stagebuffer.toString()+sexvalueclause+specimentypevalueclause+
				genevalueclause+geneIdvalueclause+probenamevalueclause+col_statusvalueclause;
		//+col_statusvalueclause
		
		/*if(!debug && !multiwhere)
			resetFilter();*/
		return whereclause;
	}
	
	public void setCachewhereclause(String cachewhereclause){
		this.cachewhereclause=cachewhereclause;
	}
	
	public String getCachewhereclause(){
		StringBuffer stagebuffer = new StringBuffer(cachetheilerstagevalueclause+cachecarnegiestagevalueclause);
		if(stagebuffer.indexOf("Mus")>0 && stagebuffer.indexOf("Homo")>0) {
			stagebuffer.insert(0, "((");
			stagebuffer.insert(stagebuffer.lastIndexOf("AND")-1, "))");
			stagebuffer.replace(stagebuffer.lastIndexOf("QIC_STG_SPECIES")-4, stagebuffer.lastIndexOf("QIC_STG_SPECIES")-1, ") OR (");			
		}
		cachewhereclause=GenericQueries.WHERE_CLAUSE+cachesourcevalueclause+cachedatevalueclause+cacheassaytypevalueclause+stagebuffer.toString()+
				cachesexvalueclause+cachespecimentypevalueclause+cachegenevalueclause+cachegeneIdvalueclause+cacheprobenamevalueclause;
		/*cachewhereclause=GenericQueries.WHERE_CLAUSE+cachesourcevalueclause+cachedatevalueclause+cacheassaytypevalueclause+cachetheilerstagevalueclause+cachecarnegiestagevalueclause+
				cachesexvalueclause+cachespecimentypevalueclause+cachegenevalueclause+cachegeneIdvalueclause+cacheprobenamevalueclause;*/
		
		/*if(!debug && !multiwhere)
			resetFilter();*/
		return cachewhereclause;
	}
	
	public void setarrayCachewhereclause(String arraycachewhereclause){
		this.arraycachewhereclause=arraycachewhereclause;
	}
	//array cache table does not have all the columns of the insitu cache. The column prefix is Replaced in the assembler
	public String getArraycachewhereclause(){
		StringBuffer stagebuffer = new StringBuffer(cachetheilerstagevalueclause+cachecarnegiestagevalueclause);
		if(stagebuffer.indexOf("Mus")>0 && stagebuffer.indexOf("Homo")>0) {
			stagebuffer.insert(0, "((");
			stagebuffer.insert(stagebuffer.lastIndexOf("AND")-1, "))");
			stagebuffer.replace(stagebuffer.lastIndexOf("QIC_STG_SPECIES")-4, stagebuffer.lastIndexOf("QIC_STG_SPECIES")-1, ") OR (");			
		}
		/*arraycachewhereclause=GenericQueries.WHERE_CLAUSE+cachesourcevalueclause+cachedatevalueclause+stagebuffer.toString()+
				cachesexvalueclause+cachespecimentypevalueclause;*/
		arraycachewhereclause=GenericQueries.WHERE_CLAUSE+cachesourcevalueclause+cachedatevalueclause+stagebuffer.toString()+
				cachesexvalueclause+cachespecimentypevalueclause;
		arraycachewhereclause=arraycachewhereclause.replace("QIC", "QMC");
		
		/*if(!debug && !multiwhere)
			resetFilter();*/
		return arraycachewhereclause;
	}
	
	public void setMicWhereclause(String micWhereclause) {
		this.micWhereclause = micWhereclause;
	}
	public String getMicWhereclause () {
		//micWhereClause = GenericQueries.WHERE_CLAUSE;
		micWhereclause = getWhereclause() + platformvalueclause + libstrategyvalueclause + seqsexvalueclause + arraysexvalueclause;
		
		if(!debug)
			resetArraySeqFilter();
		return micWhereclause;
	}
		 
	/******************RESETS*******************/
	
	public void resetValues(){
		//reset the parameterized values
		genevalues="";
		geneIdvalues="";
		fromdatevalues=null;
		todatevalues=null;
		setSourcevalues(new String[0]);
		todatemysql="";
		fromdatemysql="";
		setAssaytypeinsituvalues(new String[0]);
		setProbenamevalues("");
		setTheilerstagefromvalues("");
		setTheilerstagetovalues("");
		setCarnegiestagefromvalues("");
		setCarnegiestagetovalues("");
		setSexvalues("ALL");
		setSpecimentypevalues("");
		col_statusvalues=2;
		tempfromvalues="";
		
		//DON'T RESET THE WHERECLAUSE HERE OTHERWISE THE PAGING WONT WORK!!!
		//setWhereclause(GenericQueries.WHERE_CLAUSE);
		
	}
	
	public void resetClauses() {
		sourcevalueclause="";
		datevalueclause="";assaytypevalueclause="";theilerstagevalueclause="";carnegiestagevalueclause="";sexvalueclause="";specimentypevalueclause="";
		genevalueclause="";geneIdvalueclause="";probenamevalueclause="";
		cachesourcevalueclause="";
		cachedatevalueclause="";cacheassaytypevalueclause="";cachetheilerstagevalueclause="";cachecarnegiestagevalueclause="";cachesexvalueclause="";cachespecimentypevalueclause="";
		cachegenevalueclause="";cachegeneIdvalueclause="";cacheprobenamevalueclause="";col_statusvalueclause="";
	}
	
	public void resetWhereClauses() {
		setWhereclause(GenericQueries.WHERE_CLAUSE);
		setCachewhereclause(GenericQueries.WHERE_CLAUSE);
		setarrayCachewhereclause(GenericQueries.WHERE_CLAUSE);
		
		setMicWhereclause(GenericQueries.WHERE_CLAUSE);
	}
	//DONT RESET THE THEILER STAGES HERE BECAUSE THEY ARE SET IN THE OPTIONS AND PRESERVED FOR USE IN THE SUBSEQUENT FILTER
	public String resetGeneSearchValues() {
		
		//setTheilerstagefromvalues("");
		//setTheilerstagetovalues("");
		//setGeneoptionvalues("Expression Summaries");
		return "database_homepage";
	}
	
	public String resetAllGeneSearchValues(boolean isGene) {
		String RET=resetGeneSearchValues();
		if(isGene)
			setGeneoptionvalues("Expression Summaries");
		resetWhereClauses();
		resetClauses();
		setTheilerstagefromvalues("");
		setTheilerstagetovalues("");
		return RET;
	}
	
	public String geneSearchRedirect() {
		 if(getGeneoptionvalues().equals("Expression Summaries"))
			 return "viewGeneStrip";
		 else
		 {
			 return "browseGeneListTablePage";
		 }
	 }
	
	public void resetAll() {
		resetValues();
		resetClauses();
		resetWhereClauses();
		//focusGroup="reset";
		resetArraySeqValues();
		resetArraySeqClauses();
	}
	
	public void resetAllArraySeq() {
		
		//resetArraySeqValues();
		//resetClauses();
		//resetWhereClauses();
		//focusGroup="reset";
	}
	//TODO populate once array/seq filters are in place
	public void resetArraySeqValues(){
		//array and sequence
		setAssaysourcevalues(new String[0]);
		setArrayplatformvalues("");
		setSeqlibstrategyvalues("");
		setSeqsexvalues("ALL");
		setArraysexvalues("ALL");
	}
	
	public void resetArraySeqClauses() {
		cacheplatformvalueclause="";platformvalueclause="";libstrategyvalueclause="";cachelibstrategyvalueclause="";
		seqsexvalueclause="";cacheseqsexvalueclause="";arraysexvalueclause="";cachearraysexvalueclause="";
	}
	
	public void resetFilter() {
		resetValues();
		resetClauses();
	}
	
	public void resetArraySeqFilter() {
		resetArraySeqValues();
		resetArraySeqClauses();
	}
	
	public void resetInsitucols() {
		setInsitucols(new String[]{"gene","gudmapaccession","assaytype","age","embryostage","sex","genotype","tissue"});
	}
	
	public void resetTgcols() {
		setTgcols(new String[]{"gene","gudmapaccession","assaytype","embryostage","age","sex","genotype","tissue"});
	}
	
	public void resetGenestripcols() {
		setGenestripcols(new String[]{"gene","expressionprofile","images","microarrayprofile","rnaseq","stagerange","omim"});
	}
	
	public void resetMicseriescols() {
		setMicseriescols(new String[]{"title","numsamples","components"});
	}
	
	public void resetMicsamplecols() {
		setMicsamplecols(new String[]{"gudmapaccession","sampledescription","age","stage","sex","genotype","components"});
	}
	
	public void resetSeqsamplecols() {
		setSeqsamplecols(new String[]{"gudmapaccession","sampledescription","age","stage","sex","genotype","components"});
	}
	
	public void resetSeqseriescols() {
		setSeqseriescols(new String[]{"title","numsamples","components"});
	}
	
	public void resetMicplatformcols() {
		setMicplatformcols(new String[]{"geoplatformid","platformname","platformtechnology","platformmanufacturer","numseries"});
	}
	
	public void resetCollectioncols() {
		setCollectioncols(new String[]{"name","description","owner","count","focusgroup","status","modified","download","share"});
	}
	
	


	/*******************focus groups****************/
	public void focusGroupAction(ActionEvent event){
		String focusFieldAttribute = (String) event.getComponent().getAttributes().get("focusField");
			this.setFocusGroup(focusFieldAttribute);
	}
	
	//focus group for ish gets anatomy ID from ISH_EXPRESSION
	public String getFocusGroupWhereclause(){
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else if(focusGroup.equals("Metanephros"))
			RET =  GenericQueries.FOCUS_METANEPHROS;
		else if(focusGroup.equals("Mesonephros"))
			RET =  GenericQueries.FOCUS_MESONEPHROS;
		else if(focusGroup.equals("Lower urinary tract"))
			RET =  GenericQueries.FOCUS_URINARY;
		else if(focusGroup.equals("Early reproductive system"))
			RET =  GenericQueries.FOCUS_EARLY_REPRO;
		else if(focusGroup.equals("Male reproductive system"))
			RET =  GenericQueries.FOCUS_MALE_REPRO;
		else if(focusGroup.equals("Female reproductive system"))
			RET =  GenericQueries.FOCUS_FEMALE_REPRO;
		
		
		return RET;
	}
	
	//focus group for non-ish gets anatomy ID from ISH_SP_TISSUE
	public String getFocusGroupSpWhereclause(){
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else if(focusGroup.equals("Metanephros"))
			RET =  GenericQueries.FOCUS_METANEPHROS_SP;
		else if(focusGroup.equals("Mesonephros"))
			RET =  GenericQueries.FOCUS_MESONEPHROS_SP;
		else if(focusGroup.equals("Lower urinary tract"))
			RET =  GenericQueries.FOCUS_URINARY_SP;
		else if(focusGroup.equals("Early reproductive system"))
			RET =  GenericQueries.FOCUS_EARLY_REPRO_SP;
		else if(focusGroup.equals("Male reproductive system"))
			RET =  GenericQueries.FOCUS_MALE_REPRO_SP;
		else if(focusGroup.equals("Female reproductive system"))
			RET =  GenericQueries.FOCUS_FEMALE_REPRO_SP;
		
		return RET;
	}
	
	public String getExpressionJoin() {
		String RET="";
		if(focusGroup.equals("reset"))
			RET="";
		else
			RET =  GenericQueries.EXPRESSION_JOIN;
		
		return RET;
	}
	
	/////////////DISEASE///////////////
	
	public void setAnnotationtypevalues(String annotationtypevalues) {
		this.annotationtypevalues = annotationtypevalues;
		if(annotationtypevalues.equalsIgnoreCase("Direct Annotations"))
			isDirect="true";
		else
			isDirect="false";
	}
	
	public String getAnnotationtypevalues() {
		return annotationtypevalues;
	}
	
	public Map<String,String> getAnnotationtypelist(){
		return assembler.getAnnotationtypelist();
	}
	
	public String getIsDirect() {
		return isDirect;
	}
	
	public void setIsDirect(String isDirect) {
		this.isDirect = isDirect;
	}
	
	////////////////IMAGE UPLOAD DIRECTORIES/////////////////
	
	public Map<String,String> getImagedirlist(){
		return assembler.getImagedirlist();
	}
	
	public void setImageDir(String imageDir){
		this.imageDir = imageDir;
	}
	
	public String getImageDir() {
		return imageDir;
	}
	
	public void imageDirChanged(ValueChangeEvent e){
		this.imageDir = e.getNewValue().toString();
	}
	
////////////////WEB FILE UPLOAD DIRECTORIES/////////////////
	
	public Map<String,String> getWebfiledirlist(){
		return assembler.getWebfiledirlist();
	}
	
	public void setWebfileDir(String webfileDir){
		this.webfileDir = webfileDir;
	}
	
	public String getWebfileDir() {
		return webfileDir;
	}
	
	public void webfileDirChanged(ValueChangeEvent e){
		this.webfileDir = e.getNewValue().toString();
	}
	
	/////////////////PAGE EDITING////////////////////////////
	public Map<String,String> getPageIdlist(){
		return assembler.getPageIdlist();
	}
	
	public void setPageId(String pageId){
		this.pageId = pageId;
	}
	
	public String getPageId() {
		return pageId;
	}
	
	public void pageIdChanged(ValueChangeEvent e){
		this.pageId = e.getNewValue().toString();
	}
	
	/////////////////PAGE CATEGORIES////////////////////////////
	public Map<String,String> getPageCategorylist(){
	return assembler.getPageCategorylist();
	}
	
	public void setPageCategory(String pageCategory){
	this.pageCategory = pageCategory;
	}
	
	public String getPageCategory() {
	return pageCategory;
	}
	
	public void pageCategoryChanged(ValueChangeEvent e){
	this.pageCategory = e.getNewValue().toString();
	}
	
	////////////////////MICROARRAY/SEQUENCE///////////////
	
	public void setMic_titlecol(boolean mic_titlecol){
		this.mic_titlecol=mic_titlecol;
	}
	
	public boolean getMic_titlecol() {
		return mic_titlecol;
	}
	
	public void setMic_geoSeriesIDcol(boolean mic_geoSeriesIDcol){
		this.mic_geoSeriesIDcol=mic_geoSeriesIDcol;
	}
	
	public boolean getMic_geoSeriesIDcol() {
		return mic_geoSeriesIDcol;
	}
	
	public void setMic_sourcecol(boolean mic_sourcecol){
		this.mic_sourcecol=mic_sourcecol;
	}
	
	public boolean getMic_sourcecol() {
		return mic_sourcecol;
	}
	
	public void setMic_numsamplescol(boolean mic_numsamplescol){
		this.mic_numsamplescol=mic_numsamplescol;
	}
	
	public boolean getMic_numsamplescol() {
		return mic_numsamplescol;
	}
	
	
	public void setMic_sampleplatformIDcol(boolean mic_sampleplatformIDcol){
		this.mic_sampleplatformIDcol=mic_sampleplatformIDcol;
	}
	
	public boolean getMic_sampleplatformIDcol() {
		return mic_sampleplatformIDcol;
	}
	
	public void setMic_platformIDcol(boolean mic_platformIDcol){
		this.mic_platformIDcol=mic_platformIDcol;
	}
	
	public boolean getMic_platformIDcol() {
		return mic_platformIDcol;
	}
	
	public void setMic_platformnamecol(boolean mic_platformnamecol ) {
		this.mic_platformnamecol = mic_platformnamecol;
	}
	public boolean getMic_platformnamecol() {
		return mic_platformnamecol;
	}
	
	public void setMic_platformtechnologycol(boolean mic_platformtechnologycol ) {
		this.mic_platformtechnologycol = mic_platformtechnologycol;
	}
	public boolean getMic_platformtechnologycol() {
		return mic_platformtechnologycol;
	}
	
	public void setMic_platformmanufacturercol(boolean mic_platformmanufacturercol ) {
		this.mic_platformmanufacturercol = mic_platformmanufacturercol;
	}
	public boolean getMic_platformmanufacturercol() {
		return mic_platformmanufacturercol;
	}
	
	public void setMic_numseriescol(boolean mic_numseriescol ) {
		this.mic_numseriescol = mic_numseriescol;
	}
	public boolean getMic_numseriescol() {
		return mic_numseriescol;
	}
	
	public void setMic_componentscol(boolean mic_componentscol){
		this.mic_componentscol=mic_componentscol;
	}
	
	public boolean getMic_componentscol() {
		return mic_componentscol;
	}
	
	public void setMic_stagecol(boolean mic_stagecol) {
		this.mic_stagecol = mic_stagecol;
	}
	public boolean getMic_stagecol() {
		return mic_stagecol;
	}
	
	public void setMic_agecol(boolean mic_agecol) {
		this.mic_agecol = mic_agecol;
	}
	public boolean getMic_agecol() {
		return mic_agecol;
	}
	
	public void setMic_submissiondatecol(boolean mic_submissiondatecol) {
		this.mic_submissiondatecol = mic_submissiondatecol;
	}
	public boolean getMic_submissiondatecol() {
		return mic_submissiondatecol;
	}
	
	public void setMic_sexcol(boolean mic_sexcol) {
		this.mic_sexcol = mic_sexcol;
	}
	public boolean getMic_sexcol() {
		return mic_sexcol;
	}
	
	public void setMic_genotypecol(boolean mic_genotypecol) {
		this.mic_genotypecol = mic_genotypecol;
	}
	public boolean getMic_genotypecol() {
		return mic_genotypecol;
	}
	
	public void setMic_geoSampleIDcol(boolean mic_geoSampleIDcol) {
		this.mic_geoSampleIDcol = mic_geoSampleIDcol;
	}
	public boolean getMic_geoSampleIDcol() {
		return mic_geoSampleIDcol;
	}
	
	public void setMic_sampledescriptioncol(boolean mic_sampledescriptioncol) {
		this.mic_sampledescriptioncol = mic_sampledescriptioncol;
	}
	public boolean getMic_sampledescriptioncol() {
		return mic_sampledescriptioncol;
	}
	
	public void setMic_samplenamecol(boolean mic_samplenamecol) {
		this.mic_samplenamecol = mic_samplenamecol;
	}
	public boolean getMic_samplenamecol() {
		return mic_samplenamecol;
	}
	
	public void setMic_gudmapaccessioncol(boolean mic_gudmapaccessioncol) {
		this.mic_gudmapaccessioncol = mic_gudmapaccessioncol;
	}
	public boolean getMic_gudmapaccessioncol() {
		return mic_gudmapaccessioncol;
	}
	
	public void setMic_librarystrategycol(boolean mic_librarystrategycol) {
		this.mic_librarystrategycol = mic_librarystrategycol;
	}
	public boolean getMic_librarystrategycol() {
		return mic_librarystrategycol;
	}
		
	public void setMic_samplelibrarystrategycol(boolean mic_samplelibrarystrategycol) {
		this.mic_samplelibrarystrategycol = mic_samplelibrarystrategycol;
	}
	public boolean getMic_samplelibrarystrategycol() {
		return mic_samplelibrarystrategycol;
	}
	
	public void setCol_namecol(boolean col_namecol) {
		this.col_namecol = col_namecol;
	}
	
	public boolean getCol_namecol() {
		return col_namecol;
	}
	
	public void setCol_descriptioncol(boolean col_descriptioncol) {
		this.col_descriptioncol = col_descriptioncol;
	}
	
	public boolean getCol_descriptioncol() {
		return col_descriptioncol;
	}
	
	public void setCol_ownercol(boolean col_ownercol) {
		this.col_ownercol = col_ownercol;
	}
	
	public boolean getCol_ownercol() {
		return col_ownercol;
	}
	
	public void setCol_focusgroupcol(boolean col_focusgroupcol) {
		this.col_focusgroupcol = col_focusgroupcol;
	}
	
	public boolean getCol_focusgroupcol() {
		return col_focusgroupcol;
	}
	
	public void setCol_countcol(boolean col_countcol) {
		this.col_countcol = col_countcol;
	}
	
	public boolean getCol_countcol() {
		return col_countcol;
	}
	
	public void setCol_statuscol(boolean col_statuscol) {
		this.col_statuscol = col_statuscol;
	}
	
	public boolean getCol_statuscol() {
		return col_statuscol;
	}
	
	public void setCol_modifiedcol(boolean col_modifiedcol) {
		this.col_modifiedcol = col_modifiedcol;
	}
	
	public boolean getCol_modifiedcol() {
		return col_modifiedcol;
	}
	
	public void setCol_downloadcol(boolean col_downloadcol) {
		this.col_downloadcol = col_downloadcol;
	}
	
	public boolean getCol_downloadcol() {
		return col_downloadcol;
	}
	
	public void setCol_sharecol(boolean col_sharecol) {
		this.col_sharecol = col_sharecol;
	}
	
	public boolean getCol_sharecol() {
		return col_sharecol;
	}
	
	
	public void setMicseriescols(String[]micseriescols){
		this.micseriescols=micseriescols;
		micseriesresultmap.clear();
		for(int i=0;i<micseriescols.length;i++){
			micseriesresultmap.put(micseriescols[i], true);	
		}
		mic_titlecol=micseriesresultmap.containsKey("title");
		mic_geoSeriesIDcol=micseriesresultmap.containsKey("geoid");
		mic_sourcecol=micseriesresultmap.containsKey("source");
		mic_numsamplescol=micseriesresultmap.containsKey("numsamples");
		mic_platformIDcol=micseriesresultmap.containsKey("platform");
		mic_componentscol=micseriesresultmap.containsKey("components");
	}
	
	
	public String[] getMicseriescols(){
		return micseriescols;
	}
	
	
	
	
	private static Map<String,Object> micseriescolmap;
	static{
		micseriescolmap = new LinkedHashMap<String,Object>();
		micseriescolmap.put("Title", "title");
		micseriescolmap.put("Number of Samples", "numsamples");
		micseriescolmap.put("Component", "components");
		micseriescolmap.put("GEO Series ID", "geoid");
		micseriescolmap.put("Platform", "platform");
		micseriescolmap.put("Source", "source");
	}
 
	public Map<String,Object> getMicseriescolmap() {
		return micseriescolmap;
	}
 
	public String getMicseriescolsInString() {
		return Arrays.toString(micseriescols);
	}
	
	///////////mic sample///////////////
	public void setMicsamplecols(String[]micsamplecols){
		this.micsamplecols=micsamplecols;
		micsampleresultmap.clear();
		for(int i=0;i<micsamplecols.length;i++){
			micsampleresultmap.put(micsamplecols[i], true);	
		}
		mic_gudmapaccessioncol=micsampleresultmap.containsKey("gudmapaccession");
		mic_geoSampleIDcol=micsampleresultmap.containsKey("geosampleid");
		mic_geoSeriesIDcol=micsampleresultmap.containsKey("geoseriesid");
		mic_sampleplatformIDcol=micsampleresultmap.containsKey("platform");
		mic_sourcecol=micsampleresultmap.containsKey("source");
		mic_stagecol=micsampleresultmap.containsKey("stage");
		mic_agecol=micsampleresultmap.containsKey("age");
		mic_submissiondatecol=micsampleresultmap.containsKey("submissiondate");
		mic_sexcol=micsampleresultmap.containsKey("sex");
		mic_sampledescriptioncol=micsampleresultmap.containsKey("sampledescription");
		mic_samplenamecol=micsampleresultmap.containsKey("samplename");
		mic_genotypecol=micsampleresultmap.containsKey("genotype");
		mic_componentscol=micsampleresultmap.containsKey("components");
	}
	
	
	
	public String[] getMicsamplecols(){
		return micsamplecols;
	}
	
	
	private static Map<String,Object> micsamplecolmap;
	static{
		micsamplecolmap = new LinkedHashMap<String,Object>();	
		micsamplecolmap.put("Assay Detail", "gudmapaccession");
		micsamplecolmap.put("Sample Description", "sampledescription");
		micsamplecolmap.put("Sample Name", "samplename");
		micsamplecolmap.put("Component", "components");
		micsamplecolmap.put("Age", "age");
		micsamplecolmap.put("Stage", "stage");
		micsamplecolmap.put("Sex", "sex");
		micsamplecolmap.put("Genotype", "genotype");
		micsamplecolmap.put("GEO Sample ID", "geosampleid");
		micsamplecolmap.put("GEO Series ID", "geoseriesid");
		micsamplecolmap.put("Platform", "platform");
		micsamplecolmap.put("Source", "source");
		micsamplecolmap.put("Submission Date", "submissiondate");
		
		
	}
 
	public Map<String,Object> getMicsamplecolmap() {
		return micsamplecolmap;
	}
 
	public String getMicsamplecolsInString() {
		return Arrays.toString(micsamplecols);
	}
	
	////////////////seq sample////////////////////
	
	public void setSeqsamplecols(String[]seqsamplecols){
		this.seqsamplecols=seqsamplecols;
		seqsampleresultmap.clear();
		for(int i=0;i<seqsamplecols.length;i++){
			seqsampleresultmap.put(seqsamplecols[i], true);	
		}
		mic_gudmapaccessioncol=seqsampleresultmap.containsKey("gudmapaccession");
		mic_geoSampleIDcol=seqsampleresultmap.containsKey("geosampleid");
		mic_geoSeriesIDcol=seqsampleresultmap.containsKey("geoseriesid");
		mic_samplelibrarystrategycol=seqsampleresultmap.containsKey("librarystrategy");
		mic_sourcecol=seqsampleresultmap.containsKey("source");
		mic_stagecol=seqsampleresultmap.containsKey("stage");
		mic_agecol=seqsampleresultmap.containsKey("age");
		mic_submissiondatecol=seqsampleresultmap.containsKey("submissiondate");
		mic_sexcol=seqsampleresultmap.containsKey("sex");
		mic_sampledescriptioncol=seqsampleresultmap.containsKey("sampledescription");
		mic_samplenamecol=seqsampleresultmap.containsKey("samplename");
		mic_genotypecol=seqsampleresultmap.containsKey("genotype");
		mic_componentscol=seqsampleresultmap.containsKey("components");
	}
	
	
	
	public String[] getSeqsamplecols(){
		return seqsamplecols;
	}
	
	
	private static Map<String,Object> seqsamplecolmap;
	static{
		seqsamplecolmap = new LinkedHashMap<String,Object>();	
		seqsamplecolmap.put("Assay Detail", "gudmapaccession");
		seqsamplecolmap.put("Sample Description", "sampledescription");
		seqsamplecolmap.put("Sample Name", "samplename");
		seqsamplecolmap.put("Component", "components");
		seqsamplecolmap.put("Age", "age");
		seqsamplecolmap.put("Stage", "stage");
		seqsamplecolmap.put("Sex", "sex");
		seqsamplecolmap.put("Genotype", "genotype");
		seqsamplecolmap.put("GEO Sample ID", "geosampleid");
		seqsamplecolmap.put("GEO Series ID", "geoseriesid");
		seqsamplecolmap.put("Library Strategy", "librarystrategy");
		seqsamplecolmap.put("Source", "source");
		seqsamplecolmap.put("Submission Date", "submissiondate");
		
	}
	
	public Map<String,Object> getSeqsamplecolmap() {
		return seqsamplecolmap;
	}
	
	public String getSeqsamplecolsInString() {
		return Arrays.toString(seqsamplecols);
	}
	
	//////////////platform////////////////
	public void setMicplatformcols(String[]micplatformcols){
		this.micplatformcols=micplatformcols;
		micplatformresultmap.clear();
		for(int i=0;i<micplatformcols.length;i++){
			micplatformresultmap.put(micplatformcols[i], true);	
		}
		mic_platformIDcol=micplatformresultmap.containsKey("geoplatformid");
		mic_platformnamecol=micplatformresultmap.containsKey("platformname");
		mic_platformtechnologycol=micplatformresultmap.containsKey("platformtechnology");
		mic_platformmanufacturercol=micplatformresultmap.containsKey("platformmanufacturer");
		mic_numseriescol=micplatformresultmap.containsKey("numseries");
	}
	
	public String[] getMicplatformcols(){
		return micplatformcols;
	}
	
	
	private static Map<String,Object> micplatformcolmap;
	static{
		micplatformcolmap = new LinkedHashMap<String,Object>();
		micplatformcolmap.put("GEO ID", "geoplatformid");
		micplatformcolmap.put("Name", "platformname");
		micplatformcolmap.put("Technology", "platformtechnology");
		micplatformcolmap.put("Manufacturer", "platformmanufacturer");
		micplatformcolmap.put("Number of Series", "numseries");
	}
 
	public Map<String,Object> getMicplatformcolmap() {
		return micplatformcolmap;
	}
 
	public String getMicplatformcolsInString() {
		return Arrays.toString(micplatformcols);
	}
	
	//////////sequence series/////////////
	public void setSeqseriescols(String[]seqseriescols){
		this.seqseriescols=seqseriescols;
		seqseriesresultmap.clear();
		for(int i=0;i<seqseriescols.length;i++){
			seqseriesresultmap.put(seqseriescols[i], true);	
		}
		mic_titlecol=seqseriesresultmap.containsKey("title");
		mic_geoSeriesIDcol=seqseriesresultmap.containsKey("geoid");
		mic_sourcecol=seqseriesresultmap.containsKey("source");
		mic_numsamplescol=seqseriesresultmap.containsKey("numsamples");
		mic_librarystrategycol=seqseriesresultmap.containsKey("librarystrategy");
		mic_componentscol=seqseriesresultmap.containsKey("components");
	}
	
	
	public String[] getSeqseriescols(){
		return seqseriescols;
	}
	
	
	private static Map<String,Object> seqseriescolmap;
	static{
		seqseriescolmap = new LinkedHashMap<String,Object>();
		seqseriescolmap.put("Title", "title");
		seqseriescolmap.put("Number of Samples", "numsamples");
		seqseriescolmap.put("Component", "components");
		seqseriescolmap.put("GEO Series ID", "geoid");
		seqseriescolmap.put("Library Strategy", "librarystrategy");
		seqseriescolmap.put("Source", "source");
	}
 
	public Map<String,Object> getSeqseriescolmap() {
		return seqseriescolmap;
	}
 
	public String getSeqseriescolsInString() {
		return Arrays.toString(seqseriescols);
	}
	
	//////////collections/////////////
	public void setCollectioncols(String[]collectioncols){
		this.collectioncols=collectioncols;
		collectionresultmap.clear();
		for(int i=0;i<collectioncols.length;i++){
			collectionresultmap.put(collectioncols[i], true);	
		}
		col_namecol=collectionresultmap.containsKey("name");
		col_descriptioncol=collectionresultmap.containsKey("description");
		col_ownercol=collectionresultmap.containsKey("owner");
		col_focusgroupcol=collectionresultmap.containsKey("focusgroup");
		col_countcol=collectionresultmap.containsKey("count");
		col_statuscol=collectionresultmap.containsKey("status");
		col_modifiedcol=collectionresultmap.containsKey("modified");
		col_downloadcol=collectionresultmap.containsKey("download");
		col_sharecol=collectionresultmap.containsKey("share");
	}
	
	
	public String[] getCollectioncols(){
		return collectioncols;
	}
	
	
	private static Map<String,Object> collectioncolmap;
	static{
		collectioncolmap = new LinkedHashMap<String,Object>();
		collectioncolmap.put("Name", "name");
		collectioncolmap.put("Description", "description");
		collectioncolmap.put("Owner", "owner");
		collectioncolmap.put("Focus Group", "focusgroup");
		collectioncolmap.put("Entity Count", "count");
		collectioncolmap.put("Status", "status");
		collectioncolmap.put("Last Modified", "modified");
		collectioncolmap.put("Download", "download");
		collectioncolmap.put("Share", "share");
	}
	
	public Map<String,Object> getCollectioncolmap() {
		return collectioncolmap;
	}
	
	public String getCollectioncolsInString() {
		return Arrays.toString(collectioncols);
	}
	
	//////////////REDIRECTIONS////////////////
	
	public String returnDetailsPage(String assayType) {
		String RET="";
		if(assayType.equalsIgnoreCase("Microarray"))
			RET="viewArraySubmissionDetails";
		else if(assayType.equalsIgnoreCase("NextGen") || assayType.equalsIgnoreCase("Sequence"))
			RET="viewSeqSubmissionDetails";
		else
			RET="viewSubmissionDetails";
		return RET;
	}
	
	public String returnBatchPage(String assayType, String specimenAssay) {
		String RET="";
		/*if(specimenAssay.equalsIgnoreCase("section"))
			RET="browseSishTablePage";
		else if(specimenAssay.equalsIgnoreCase("wholemount"))
			RET="browseWishTablePage";
		else if(specimenAssay.equalsIgnoreCase("opt-wholemount"))
			RET="browseOptTablePage";
		else */if(assayType.equalsIgnoreCase("IHC"))
			RET="browseIhcTablePage";
		else if(assayType.equalsIgnoreCase("Tg"))
			RET="browseTgTablePage";
		else
			RET="browseInsituTablePage";
		return RET;
	}
	
	///////FROM browseStageSubmissions//////////////////
	public void updateStaging(String stage, String geneSymbol){
		if(stage==null)
			return;
		String species="";
		if(stage.startsWith("TS0")){
			stage=stage.substring(3);
			species="Mouse";
		}
		else if(stage.startsWith("TS")){
			stage=stage.substring(2);
			species="Mouse";
		}
			
	
		resetAll();
		setTheilerstagefromvalues(stage);
		setTheilerstagetovalues(stage);
		setSpecies(species);
		if(!geneSymbol.equals(""))
			setGenevalues(geneSymbol);
		
	}
	
	public void confirmStaging(String stage){
		if(stage==null)
			return;
		if(stage.startsWith("TS0")){
			stage=stage.substring(3);
			species="Mouse";
		}
		else if(stage.startsWith("TS")){
			stage=stage.substring(2);
			species="Mouse";
		}
		setTheilerstagefromvalues(stage);
		setTheilerstagetovalues(stage);
		
	}
	
/////////////////COLLECTIONS////////////////////////////
	
	public Map<String,Integer>getCollectionlist(int userId) {
		return assembler.getCollectionlist(userId);
	}
	public void setCol_idvalues(int col_idvalues) {
		this.col_idvalues = col_idvalues;
	}
	public int getCol_idvalues() {
		return col_idvalues;
	}
	
	public Map<String,String> getCollectionTypelist(){
	return assembler.getCollectionTypelist();
	}
	
	public void setCollectionType(String collectionType){
	this.collectionType = collectionType;
	}
	
	public String getCollectionType() {
	return collectionType;
	}
	
	 public String getCollectionTypeBuffer(){
	    	StringBuffer str=new StringBuffer();
	    	
	    	if(collectionType!=null) {
	    		str.append("'");
		    	str.append(collectionType);
		    	str.append("'");
	    	}
	    	
	    	return str.toString();
	    }
	
	public void collectionTypeChanged(ValueChangeEvent e){
	this.collectionType = e.getNewValue().toString();
	}
	
	Set<String> items = null;
	public void setLocalStorage(String localStorage) {
		//get rid of duplicates by splitting the string into a set
		items = new HashSet<String>(Arrays.asList(localStorage.split(";")));
		String tmpstr = items.toString();
		tmpstr=tmpstr.substring(1,tmpstr.lastIndexOf(']'));
		tmpstr=tmpstr.replace(", ",";");
		this.localStorage=tmpstr.replace("undefined","");
		if(this.localStorage.startsWith(";"))
			this.localStorage=this.localStorage.substring(1);
		int i=0;
	}
	
	public String getLocalStorage() {
		return localStorage;
	}
	
	public void resetItems() {
		if(items!=null)
			items.clear();
	}
	//TODO remove duplicates from newval see above
	public String returnTotalLocalStorage(String newval){
		StringBuffer outputstr = new StringBuffer();
		newval=newval.substring(1,newval.lastIndexOf("'"));
		
		List<String> list = new ArrayList<String>(Arrays.asList(newval.split(";")));
		for(String str : list) {
			/*if(!items.contains(str) && !str.equals(""))
				outputstr.append(str+";");*/
			if( (items==null || !items.contains(str) ) && !str.equals(""))
				outputstr.append(str+";");
		}
		if(getLocalStorage().equals(""))
			return outputstr.toString();
		else if (outputstr.toString().equals(""))
			return getLocalStorage();
		else
			return getLocalStorage()+";"+outputstr.toString();
		
		
	}
	
	public String collectionSaveOption() {
		 if(getCol_optionvalues()==0)
			 return "newCollection";
		 else
			 return "browseCollectionListTablePage";
	 }
	
	////////////charts/////////////////
	public void setAssayTypeIndex(int assayTypeIndex){
		this.assayTypeIndex = assayTypeIndex;
	}
	
	public int getAssayTypeIndex() {
		return assayTypeIndex;
	}
	
	/////////////debug//////////////////
	
	public void setDebug(boolean debug)
	{
		this.debug = debug;	
	}
	public boolean getDebug() {
		return debug;
	}
	
	/////////////PARAM STRING//////////////
	
	public void setParams(String params) {
		this.params=params;
	}
	public String getParams(){
		if(params.equals("Dev Age between: Post Natal Adult and Post Natal Adult"))
			return "DevAge = Post Natal Adult";
		else
			return params;
	}
	
	/////////////autocomplete////////////////
	
	public void updateAutoAction(AjaxBehaviorEvent event) {
		autoAction=true;
	}
	public void setAutoAction(boolean autoAction) {
		this.autoAction=autoAction;
	}
	public boolean getAutoAction() {
		return autoAction;
	}
	
	public void handleSelect(SelectEvent event) {
		//String item = event.getObject().toString();
		//FacesMessage msg = new FacesMessage("Selected", "Item:" + item);
		autoAction=true;
	}
	
	
	/**********TODO **********checkboxes keep this code ******************/
	
	/*public void setCheckboxes(boolean[]checkboxes){
		this.checkboxes=checkboxes;
	}
	
	public boolean[] getCheckboxes(){
		return checkboxes;
	}*/
	
	
	
}
