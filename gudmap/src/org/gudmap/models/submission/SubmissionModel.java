package org.gudmap.models.submission;

import java.util.ArrayList;

public class SubmissionModel {
	
	protected String oid;
	protected String accID;
    protected SpecimenModel specimenModel;
    protected PersonModel submitter;
    protected String stage;
    protected String stageOrder;
    protected String stageAnatomy;
    protected String authors;
    protected ArrayList<ImageInfoModel> originalImages;
    protected int publicFlag;
    protected String assayType;
    protected String archiveId;

    protected int deletedFlag; 			// SUB_IS_DELETED
    protected int submitterId; 			//  SUB_SUBMITTER_FK: 3
    protected int piId; 				// SUB_PI_FK: 3
    protected int entryBy; 				// SUB_ENTRY_BY_FK: 1
    protected int modifierId; 			// SUB_MODIFIER_FK: 1
    protected int localStatusId; 		// SUB_LOCAL_STATUS_FK: 3
    protected int dbStatusId; 			// SUB_DB_STATUS_FK: 2
    protected String project; 			// SUB_PROJECT_FK: GUDMAP
    protected int batchId=0; 				// SUB_BATCH: 111
    protected String nameSpace; 		// SUB_NAMESPACE: http://www.gudmap.org
    protected String osAccession; 		// SUB_OS_ACCESSION: UNASSIGNED
    protected String loaclId; 			// SUB_LOCAL_ID:
    protected String source; 			// SUB_SOURCE:
    protected String validation; 		// SUB_VALIDATION:
    protected int control; 				// SUB_CONTROL: 0
    protected String assessment; 		// SUB_ASSESSMENT:
    protected int confidencLevel; 		// SUB_CONFIDENCE: 0
    protected String localDbName; 		// SUB_LOCALDB_NAME:
    protected String labId; 			// SUB_LAB_ID: 
    protected String localId; 			//SUB_LOCAL_ID
    protected String euregeneId; 		//SUB_ACCESSION_ID_2

    protected String[] resultNotes = null;
    
    protected PersonModel[] principalInvestigators;
    protected AlleleModel[] alleleModel;
    
    protected ArrayList<String[]> linkedPublications;
    protected String[] acknowledgements;
    protected ArrayList<Object> linkedSubmissions;
    protected String sourceLab; 			

    public SubmissionModel() {
    
    }
    
    public SubmissionModel(String accID, SpecimenModel specimenModel, PersonModel[] pis, PersonModel submitter, 
    		String stage, String authors, ArrayList<ImageInfoModel> originalImages) {
    	this.accID = accID;
    	this.specimenModel = specimenModel;
    	this.principalInvestigators = pis;
    	this.submitter = submitter;
    	this.stage = stage;
    	this.authors = authors;
        this.originalImages = originalImages;
    }
    
    

    public AlleleModel[] getAlleleModel() {
        return alleleModel;
    }

    public void setAlleleModel(AlleleModel[] alleleModel) {
        this.alleleModel = alleleModel;
        int iSize = 0;
		if (null != alleleModel)
		    iSize = alleleModel.length;
	
		if (0 == iSize) {
		    alleleModel = null;
		    return;
		}
	
		if (1 == iSize)
		    alleleModel[0].setTitle("Non-wild type AlleleModel:");
		else {
		    int i = 0;
		    iSize++;
		    for (i = 1; i < iSize; i++) {
			alleleModel[i-1].setTitle("Non-wild type AlleleModel "+i+":");
		    }
		}
    }
    
    public String getGeneId() {
		String ret = null;
		if (null != alleleModel && 0 < alleleModel.length)
	    	ret = alleleModel[0].getGeneId();
	    
		if (null != ret && ret.trim().equals(""))
		    ret = null;
		
		return ret;
    }

    public String getGeneSymbol() {
	String ret = null;
	if (null != alleleModel && 0 < alleleModel.length)
	    ret = alleleModel[0].getGeneSymbol();
    
	if (null != ret && ret.trim().equals(""))
	    ret = null;
	
	return ret;
    }
    public String getGeneName() {
	return null;
    }

    public boolean isTransgenic() {
	if (null != assayType && assayType.equalsIgnoreCase("tg"))
	    return true;

	return false;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }
    
    public void setAccID(String accID) {
        this.accID = accID;
    }

    public String getAccID() {
        return accID;
    }

    public void setSpecimenModel(SpecimenModel specimenModel) {
        this.specimenModel = specimenModel;
    }

    public SpecimenModel getSpecimenModel() {
        return specimenModel;
    }

    public void setSubmitter(PersonModel submitter) {
        this.submitter = submitter;
    }

    public PersonModel getSubmitter() {
        return submitter;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStage() {
        return stage;
    }
    
    public void setStageOrder(String stageOrder) {
        this.stageOrder = stageOrder;
    }

    public String getStageOrder() {
        return stageOrder;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuthors() {
        return authors;
    }
    
    public int getNumImages() {
		int ret = 0;
		if (null != originalImages)
		    ret = originalImages.size();
		
		return ret;
    }

    public ArrayList<ImageInfoModel> getOriginalImages() {
        return originalImages;
        
    	/*
    	// 4 image a row
    	int iSize = 0;
    	if (null != originalImages)
    	    iSize = originalImages.size();
    	if (0 == iSize)
    	    return null;

    	ArrayList ret = new ArrayList();
    	ImageInfo[] row = null;
    	int index = 0;
    	int  i = 0;
    	while (index < iSize) {
    	    row = new ImageInfo[4];
    	    for (i = 0; i < 4; i++)
    		row[i] = null;
    	    for (i = 0; i < 4; i++) 
    		if (index < iSize) {
    		    row[i] = (ImageInfo)originalImages.get(index);
    		    index++;
    		}
    	    ret.add(row);
    	}

    	return ret;
    	*/

    }

    public void setOriginalImages(ArrayList<ImageInfoModel> originalImages) {
        this.originalImages = originalImages;
    }
    
    public int getPublicFlag() {
    	return publicFlag;
    }
    
    public void setPublicFlag(int publicFlag) {
    	this.publicFlag = publicFlag;
    }
    
    public String getAssayType() {
    	return assayType;
    }
    
    public void setAssayType(String input) {
    	assayType = input;
	if (null != assayType) 
	    assayType = assayType.trim();
    }
    
    public String getArchiveId() {
        return archiveId;
    }
    
    public void setArchiveId(String archiveId){
        this.archiveId = archiveId;
    }
    
    public int getDeletedFlag() {
    	return deletedFlag;
    }
    
    public void setDeletedFlag(int deletedFlag) {
    	this.deletedFlag = deletedFlag;
    }
    
    public int getSubmitterId() {
    	return submitterId;
    }
    
    public void setSubmitterId(int submitterId) {
    	this.submitterId = submitterId;
    }
    
    public int getPiId() {
    	return piId;
    }
    
    public void setPiId(int piId) {
    	this.piId = piId;
    }

    public int getEntryBy() {
    	return entryBy;
    }
    
    public void setEntryBy(int entryBy) {
    	this.entryBy = entryBy;
    }

    public int getModifierId() {
    	return modifierId;
    }
    
    public void setModifierId(int modifierId) {
    	this.modifierId = modifierId;
    }

    public int getLocalStatusId() {
    	return localStatusId;
    }
    
    public void setLocalStatusId(int localStatusId) {
    	this.localStatusId = localStatusId;
    }

    public int getDbStatusId() {
    	return dbStatusId;
    }
    
    public void setDbStatusId(int dbStatusId) {
    	this.dbStatusId = dbStatusId;
    }

    public String getProject() {
    	return project;
    }
    
    public void setProject(String input) {
    	project = input;
		if (null == project)
		    project = "";
    }
    
    public int getBatchId() {
    	return batchId;
    }
    
    public void setBatchId(int batchId) {
    	this.batchId =batchId;
    }

    public String getNameSpace() {
    	return nameSpace;
    }
    
    public void setNameSpace(String input) {
    	nameSpace = input;
    	if (null == nameSpace)
    		nameSpace = "";
    }

    public String getOsAccession() {
    	return osAccession;
    }
    
    public void setOsAccession(String input) {
    	osAccession = input;
    	if (null == osAccession)
    		osAccession = "";
    }

    public String getLoaclId() {
    	return loaclId;
    }
    
    public void setLoaclId(String input) {
    	loaclId = input;
    	if (null == loaclId)
    		loaclId = "";
    }

    public String getSource() {
    	return source;
    }
    
    public void setSource(String source) {
    	this.source = source;
    	
    	String[] arr = source.split("-");
    	setSourceLab(arr[1] + " Lab");
    }

    public String getValidation() {
    	return validation;
    }
    
    public void setValidation(String input) {
    	validation = input;
    	if (null == validation)
    		validation = "";
    }

    public int getControl() {
    	return control;
    }
    
    public void setControl(int control) {
    	this.control = control;
    }

    public String getAssessment() {
    	return assessment;
    }
    
    public void setAssessment(String input) {
    	assessment = input;
    	if (null == assessment)
    		assessment = "";	
    }

    public int getConfidencLevel() {
    	return confidencLevel;
    }
    
    public void setConfidencLevel(int confidencLevel) {
    	this.confidencLevel = confidencLevel;
    }

    public String getLocalDbName() {
    	return localDbName;
    }
    
    public void setLocalDbName(String input) {
    	localDbName = input;
    	if (null == localDbName)
    		localDbName = "";
    }

    public String getLabId() {
    	return labId;
    }
    
    public void setLabId(String input) {
    	labId = input;
    	if (null == labId)
    		labId = "";
    }

    public void setPrincipalInvestigators(PersonModel[] principalInvestigators) {
        this.principalInvestigators = principalInvestigators;
    }
    
    public PersonModel[] getPrincipalInvestigators() {
        return principalInvestigators;
    }

    public String getEuregeneId() {
    	return euregeneId;
    }
    
   /* public void setEuregeneId(String input) {
    	euregeneId = input;
    	if (null == euregeneId)
    		euregeneId = "";
    }*/
    
    public void setEuregeneId(String euregeneId) {
    	this.euregeneId = euregeneId;
    }

    public String[] getResultNotes() {
    	return resultNotes;
    }
    
    public void setResultNotes(String[] input) {
    	resultNotes = input;
		if (null != resultNotes && 0 == resultNotes.length)
		    resultNotes = null;
    }    

    public boolean isReleased() {
		if (publicFlag < 1)
		    return false;
		return true;
    }
    
    public boolean isDeleted() {
		if (deletedFlag < 1)
		    return false;
		return true;
    }
    
    public ArrayList<String[]> getLinkedPublications() {
        return linkedPublications;
    }

    public void setLinkedPublications(ArrayList<String[]> linkedPublications) {
        this.linkedPublications = linkedPublications;
    }
    
    public ArrayList<Object> getLinkedSubmissions() {
        return linkedSubmissions;
    }

    public void setLinkedSubmissions(ArrayList<Object> linkedSubmissions) {
        this.linkedSubmissions = linkedSubmissions;
    }
    
    public String[] getAcknowledgements() {
        return acknowledgements;
    }

    public void setAcknowledgements(String[] acknowledgements) {
        this.acknowledgements = acknowledgements;
    }

    public void setStageAnatomy(String stageAnatomy) {
        this.stageAnatomy = stageAnatomy;
    }

    public String getStageAnatomy() {
        return stageAnatomy;
    }

    public void setSourceLab(String sourceLab) {
        this.sourceLab = sourceLab;
    }

    public String getSourceLab() {
        return sourceLab;
    }
    
}
