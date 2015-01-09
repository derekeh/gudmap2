package org.gudmap.queries.submission;

import java.util.ResourceBundle;

public class IshSubmissionQueries {
	
public static ResourceBundle bundle = ResourceBundle.getBundle("org.gudmap.bundles.config");

public static String ACCESSION_BY_OID = "SELECT DISTINCT SUB_ACCESSION_ID FROM ISH_SUBMISSION WHERE SUB_OID = ?";

// for every format except DPC, which is what GUDMAP uses, the format goes first.
public static String STAGE_FORMAT_CONCAT = bundle.getString("project").equals("GUDMAP") ? 
		  "TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) " +
		  "WHEN 'P' THEN CONCAT('P',SPN_STAGE) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END)" :
		  "CONCAT(SPN_STAGE_FORMAT, SPN_STAGE)";
	
	
	/////////////////////////////////SUBMISSION///////////////////////////////////////
	
	//SUBMISSION BY ID
	
	public static String SUBMISSION_BY_OID = "SELECT SUB_ACCESSION_ID, SUB_EMBRYO_STG, SUB_ASSAY_TYPE, " +
	"SUB_MODIFIED_DATE, SUB_MODIFIED_BY, SUB_VERSION_NO, SUB_IS_PUBLIC, SUB_ARCHIVE_ID, " +
	"SUB_IS_DELETED, SUB_SUBMITTER_FK, SUB_PI_FK, SUB_ENTRY_BY_FK, SUB_MODIFIER_FK, " +
	"SUB_DB_STATUS_FK, SUB_PROJECT_FK, SUB_BATCH, " +
	"SUB_NAMESPACE, SUB_OS_ACCESSION, SUB_LOCAL_ID, SUB_SOURCE, SUB_VALIDATION, " +
	"SUB_CONTROL, SUB_ASSESSMENT, SUB_CONFIDENCE, SUB_LOCALDB_NAME, " +
	"SUB_LAB_ID, SUB_ACCESSION_ID_2, SUB_OID FROM ISH_SUBMISSION WHERE SUB_OID = ?";
	
	
	//SUBMISSION NOTES
	
	public static String SUBMISSION_NOTES_BY_OID = "SELECT SNT_VALUE, SNT_TYPE FROM ISH_SUBMISSION_NOTE WHERE SNT_SUBMISSION_FK = ?";
	
	
	
	
	//////////////////////////////////PROBE//////////////////////////////////////////
	
	//PROBE  BY ID
	
	public static String PROBE_BY_OID = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, /* 1-3 */ " +
	  "RPR_LOCUS_TAG, PRB_SOURCE, PRB_STRAIN, /* 4-6 */ " +
	  "PRB_TISSUE, PRB_PROBE_TYPE, PRB_GENE_TYPE, /* 7-9 */ "+
	  "PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, /* 10-12 */ " +
	  "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), /* 13-14 */ " +
	  "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END) as prbName_url, /* 15 */ "+
	  "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), RPR_TYPE, /* 16-17 */ " +
	  "RPR_5_LOC, RPR_3_LOC, /* 18-19 */ " +
	  "RPR_5_PRIMER, RPR_3_PRIMER, /* 20-21 */ " +
	  "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " + 
	  "WHEN 'MGI:' THEN CONCAT(GENE_URL.URL_URL, RPR_LOCUS_TAG) " + 
	  "ELSE /* non MGI */ '' END, /* 22 */" +
	  "RPR_CLONE_NAME_2, PRB_LAB_ID, SUB_ASSAY_TYPE /* 23-25 */" +
	  "FROM ISH_PROBE " + 
	  "JOIN ISH_SUBMISSION ON PRB_SUBMISSION_FK = SUB_OID AND SUB_OID = ? " +
	  "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_OID = 4 " + 
	  "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " + 
	  "JOIN REF_URL GENE_URL ON GENE_URL.URL_TYPE = 'jax_gene' " + 
	  "LEFT JOIN REF_MGI_PRB ON RPR_JAX_ACC = RMP_MGIACC " + 
	  "LEFT JOIN REF_MGI_MRK ON RPR_LOCUS_TAG = RMM_MGIACC " +
	  "LEFT JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = " + 
	  "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
	  "WHEN 'MGI:'     THEN  'jax_gene' " + 
	  "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
	  "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
	  "END";
	
	
	//PROBE NOTE by oid
	
	public static String PROBE_NOTE_BY_OID = "SELECT PNT_VALUE FROM ISH_PROBE_NOTE, ISH_SUBMISSION WHERE PNT_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC=1 AND SUB_OID = ? ORDER BY PNT_SEQ";
	
	
	//MAPROBE NOTE  BY OID
	
	public static String MAPROBE_NOTE_BY_OID = "SELECT RPN_NOTES FROM REF_PRB_NOTES, ISH_SUBMISSION, ISH_PROBE, REF_PROBE " +
	"WHERE SUB_OID = ? " +
	"AND PRB_SUBMISSION_FK = SUB_OID " +
	"AND PRB_MAPROBE = RPR_OID " +
	"AND RPN_PROBE_FK = RPR_OID " +
	"AND RPN_ISDELETED = 0 " +
	"ORDER BY RPN_OID DESC";
	
	//final static String name237 = "PROBE_NOTE_BY_MAPROBE_ID";
	//PROBE NOTE BY MAPROBE ID
	public static String PROBE_NOTE_BY_MAPROBE_ID = "SELECT PNT_SUBMISSION_FK,PNT_VALUE FROM ISH_PROBE_NOTE, ISH_PROBE WHERE PNT_SUBMISSION_FK = PRB_SUBMISSION_FK AND PRB_MAPROBE = ? ORDER BY PNT_SEQ";
	
	//final static String name238 = "MAPROBE_NOTE";
	//MAPROBE NOTE BY MAPROBE ID
	public static String MAPROBE_NOTE_BY_MAPROBE_ID = "SELECT RPN_NOTES FROM REF_PRB_NOTES, REF_PROBE WHERE RPR_OID = ? AND RPN_PROBE_FK = RPR_OID AND RPN_ISDELETED = 0 ORDER BY RPN_OID DESC";
	
	//PROBE BY MAPROBE ID
	  public static String MAPROBE_BY_PROBE_ID = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, RPR_LOCUS_TAG, "+  
	                                 "PRB_SOURCE, PRB_STRAIN, PRB_TISSUE, PRB_PROBE_TYPE, "+
	                                 "PRB_GENE_TYPE, PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, "+  
	                                 "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), "+
	                                 "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END), "+
	                                 "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), "+  
	                                 "RPR_TYPE, RPR_5_LOC, RPR_3_LOC, RPR_5_PRIMER, RPR_3_PRIMER, '', " +
	                                 "RPR_CLONE_NAME_2, PRB_LAB_ID, SUB_ASSAY_TYPE  "+ 
	                                 "FROM REF_PROBE "+
	                                 "JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID "+
	                                 "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK "+
	                                 "JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = "+
	                                 "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
	                                 "WHEN 'MGI:'     THEN  'jax_gene' " + 
	                                 "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
	                                 "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
	                                 "END " +
	                                 "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_TYPE = 'genbank_sequence' "+
	                                 "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC "+
	                                 "WHERE RPR_JAX_ACC = ? "+
	                                 "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";

	  final static String name239 = "MAPROBE_DETAILS_EXTRA this is comibined into above";
	  final static String query239 = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_JAX_ACC, RPR_LOCUS_TAG, "+  
	                                 "PRB_SOURCE, PRB_STRAIN, PRB_TISSUE, PRB_PROBE_TYPE, "+
	                                 "PRB_GENE_TYPE, PRB_LABEL_PRODUCT, PRB_VISUAL_METHOD, RPR_MTF_JAX, "+  
	                                 "RPR_GENBANK, CONCAT(RPR_PREFIX,RPR_OID), "+
	                                 "CONCAT(PRB_NAME_URL.URL_URL,  CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN RPR_JAX_ACC ELSE substring(RPR_JAX_ACC from position(':' in RPR_JAX_ACC) + 1) END), "+
	                                 "CONCAT(GENBANK_URL.URL_URL,RPR_GENBANK), "+  
	                                 "RPR_TYPE, RPR_5_LOC, RPR_3_LOC, RPR_5_PRIMER, RPR_3_PRIMER, '', " +
	                                 "RPR_CLONE_NAME_2, PRB_LAB_ID "+ 
	                                 "FROM REF_PROBE "+
	                                 "JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID "+
	                                 "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK "+
	                                 "JOIN REF_URL PRB_NAME_URL ON PRB_NAME_URL.URL_TYPE = "+
	                                 "CASE substring(RPR_JAX_ACC from 1 for position(':' in RPR_JAX_ACC)) " + 
	                                 "WHEN 'MGI:'     THEN  'jax_gene' " + 
	                                 "WHEN 'maprobe:' THEN 'maprobe_probe' " + 
	                                 "ELSE '-1' /* unrecognised prefix, get NULL record */ " + 
	                                 "END " +
	                                 "JOIN REF_URL GENBANK_URL ON GENBANK_URL.URL_TYPE = 'genbank_sequence' "+
	                                 "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC "+
	                                 "WHERE RPR_JAX_ACC = ? "+
	                                 "AND  PRB_MAPROBE = ? "+
	                                 "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	
	
	//FULL  PROBE SEQUENCE
	
	public static String FULL_SEQUENCE_BY_OID = "SELECT DISTINCT RPS_SEQUENCE, RPS_RPR_FK FROM REF_PROBE_SEQUENCE, REF_PROBE, ISH_PROBE, ISH_SUBMISSION"+
		 "	where RPR_OID = PRB_MAPROBE "+ 
		 "	AND PRB_SUBMISSION_FK = SUB_OID "+
		 "	AND RPS_RPR_FK=RPR_OID "+
		 "	AND SUB_OID = ? "+
		 "	ORDER BY RPS_RPR_FK, RPS_SEQ ASC";
	
	
	
	
	
	
	///////////////////////////////ANTIBODY/////////////////////////////////////////
	
	//ANTIBODY by ID
	
	public static String ANTIBODY_BY_OID = "SELECT DISTINCT  CONCAT(ATB_PREFIX,ATB_OID), ATB_NAME, "+
	"RPR_JAX_ACC, RPR_SYMBOL, RPR_NAME, RPR_LOCUS_TAG, RPR_GENBANK, RPR_5_LOC, RPR_3_LOC, "+
	"ATB_TYPE, ATB_PROD_METHOD, ATB_CLONE_ID, ATB_SP_IMMUNIZED, ATB_PURIFICATION_MD, ATB_IMM_ISOTYPE, ATB_CHAIN, ATB_DIRECT_LABEL,"+
	"ATL_DETECTION_NOTES, ATL_DILUTION, ATL_LAB_ID, " +
	"SUP_COMPANY, SUP_CAT_NUM, SUP_LOT_NUM, " +
	"ATL_SEC_ANTIBODY, ATL_DETECTION_METHOD " +
	"FROM ISH_ANTIBODY " +
	    "LEFT JOIN REF_PROBE ON ATB_OID=RPR_OID " +
	"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " +  
	"JOIN ISH_SUBMISSION ON SUB_OID = ATL_SUBMISSION_FK " +
	"JOIN LNK_SUP_ANTIBODY ON LAS_ATB_OID_FK = ATB_OID " +
	"JOIN GEN_SUPPLIER ON LAS_SUP_FK = SUP_OID " +
	"WHERE ATL_SUBMISSION_FK = ?";
	
	
	//PROBE NOTE
	
	//public static String PROBE_NOTE_BY_OID = "SELECT PNT_VALUE FROM ISH_PROBE_NOTE, ISH_SUBMISSION WHERE PNT_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC=1 AND SUB_ACCESSION_ID = ? ORDER BY PNT_SEQ";
	
		
	//ANTIBODY SPECIES SPECIFICITY
	
	public static String ANTIBODY_SPECIES_SPECIFICITY_BY_OID = "SELECT ATB_OID,ABP_SPECIES " +
	"FROM ISH_ANTIBODY " +
	"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " + 
	"JOIN ISH_ATB_SPECIFICITY ON ABP_ATB_FK = ATB_OID " +
	"WHERE ATL_SUBMISSION_FK = ?";
	
	
	//ANTIBODY VARIANTS
	
	public static String ANTIBODY_VARIANTS_BY_OID = "SELECT ATB_OID,ABV_VARIANT_NAME " +
	"FROM ISH_ANTIBODY " +
	"JOIN LNK_SUB_ANTIBODY ON ATL_ATB_OID_FK = ATB_OID " + 
	"JOIN ISH_ATB_VARIANTS ON ABV_ATB_FK = ATB_OID " +
	"WHERE ATL_SUBMISSION_FK = ?";
	
	
	////////////////////////////////////////////////////////////////SPECIMEN//////////////////////////////////////
	
	//SPECIMEN BY ID
	
	public static String SPECIMEN_BY_OID = "SELECT SPN_WILDTYPE, SPN_ASSAY_TYPE, SPN_FIXATION_METHOD, SPN_EMBEDDING, SPN_STRAIN, SPN_STAGE_FORMAT, SPN_STAGE, " +
				"SUB_EMBRYO_STG, SPN_SEX, SPN_TISSUE_TYPE, SPN_STAGE_ADD_VALUE, SPN_SPECIES  FROM ISH_SPECIMEN, ISH_SUBMISSION WHERE " +
			    "SPN_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND SUB_OID = ?";
	
	
	//SPECIMEN NOTE
	
	public static String SPECIMEN_NOTE_BY_OID = "SELECT MNT_VALUE FROM ISH_SPECIMEN_NOTE, ISH_SUBMISSION WHERE SUB_OID = MNT_SUBMISSION_FK AND SUB_OID = ? ORDER BY MNT_SEQ";
	
	
	//////////////////////////////////////////////////////////////////ALLELE////////////////////////////////////
	//ALLELE BY ID
	
	public static String ALLELE_BY_OID = "SELECT ALE_GENE, ALE_MUTATED_GENE_ID, ALE_MGI_ALLELE_ID, "+
	"ALE_LAB_NAME_ALLELE, ALE_ALLELE_NAME, ALE_NON_PAIRED , ALE_TYPE, "+
	"SAL_FIRST_CHROM, SAL_SECOND_CHROM, SAL_TG_REPORTER, SAL_TG_VISUALISATION, SAL_NOTES "+  
	"FROM LNK_SUB_ALLELE LEFT JOIN  ISH_ALLELE ON SAL_ALE_OID_FK=ALE_OID WHERE  SAL_SUBMISSION_FK = ? ORDER BY SAL_ORDER ";
		
	
	////////////////////////////////////////////////////////////////IMAGES///////////////////////////////////
	
	//IMAGES BY ID (USES IMAGE INFO MODEL)
	
	public static String IMAGE_INFO_BY_OID = "SELECT SUB_ACCESSION_ID, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_SML_FILENAME), INT_VALUE, SPN_ASSAY_TYPE, " +
							"CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_OID = ? AND  " +
							"IMG_SUBMISSION_FK = SUB_OID  JOIN ISH_SPECIMEN ON SPN_SUBMISSION_FK= SUB_OID JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " +
							"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK LEFT JOIN ISH_IMAGE_NOTE ON INT_IMAGE_FK = IMG_OID WHERE SUB_IS_DELETED = 0 " +
							"AND IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' ORDER BY IMG_ORDER ";
	
	/////////////////////////////////////////////////////////////IMAGE DETAIL WLZ////////////////////////////
	
	//WLZ BY ID (USES IMAGE DETAIL MODEL)
	
	public static String IMAGE_DETAIL_BY_OID = "SELECT SUB_ACCESSION_ID, RPR_SYMBOL, RPR_NAME, " +
	"SUB_EMBRYO_STG, " + STAGE_FORMAT_CONCAT + ", SUB_ASSAY_TYPE, SPN_ASSAY_TYPE, " +
	"CONCAT(I.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME), " +
	"CONCAT(C.URL_URL, '?greyImg=', IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME) " +
	"FROM ISH_PROBE " +
	"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
	"JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
	"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
	"JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK " +
	"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " + 
	"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " + 
	" WHERE IMG_TYPE='opt_wlz_merged'  AND SUB_OID= ? " +
	"ORDER BY IMG_ORDER ";
	
	
	////////////////////////////////////////////////////////////AUTHOR///////////////////////////////////////
	
	//AUTHOR BY ID
	
	public static String AUTHOR_BY_OID = "SELECT GROUP_CONCAT(AUT_NAME ORDER BY LSA_ORDER SEPARATOR ', ') AS AUTHOR FROM ISH_AUTHORS, LNK_SUB_AUTHORS, "+
			"ISH_SUBMISSION WHERE AUT_OID=LSA_AUT_FK AND LSA_SUB_FK=SUB_OID AND SUB_OID = ?";
	
	///////////////////////////////////////////////////////////PI/////////////////////////////////////////
	//PI BY ID
	
	public static String PI_BY_OID = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, " +
								"PER_FAX, PER_OID FROM ISH_PERSON, ISH_SUBMISSION WHERE PER_OID = SUB_PI_FK AND SUB_OID = ?";
	
	
	//PIS BY ID (RETURN ARRAY)
	
	public static String PIS_BY_OID = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, PER_FAX, PER_OID " +
	"FROM ISH_PERSON " +
	"WHERE PER_OID IN ( " +
	"SELECT GDT_PERSON_FK FROM REF_GROUP_DETAIL " +
	"JOIN REF_GROUP ON GDT_GROUP_FK = GRP_OID " +
	"JOIN REF_SUBMISSION_PERSON_GRP ON SPG_GROUP_FK = GRP_OID " +
	"WHERE SPG_SUBMISSION_FK = ?)";
	
	////////////////////////////////////////////////////////////SUBMITTER///////////////////////////////////
	
	//SUBMITTER BY ID
	
	public static String SUBMITTER_BY_OID = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, " +
						"PER_FAX, PER_OID FROM ISH_PERSON, ISH_SUBMISSION WHERE PER_OID = SUB_SUBMITTER_FK AND SUB_IS_PUBLIC=1 AND SUB_OID = ?";
	
	//PERSON BY PERSON ID
	
	public static String PERSON_BY_PER_OID = "SELECT PER_NAME, PER_LAB, PER_ADDRESS, PER_ADDRESS_2, PER_EMAIL, PER_CITY, PER_POSTCODE, PER_COUNTRY, PER_PHONE, " +
							"PER_FAX, PER_OID FROM ISH_PERSON WHERE PER_OID = ?";
	//////////////////////////////////////////////////////////PUBLICATION////////////////////////////////////
	
	//LINKED PUBLICATIONS BY ID
	
	public static String LINKED_PUBLICATIONS_BY_OID = "SELECT LPU_AUTHORS,LPU_YEAR,LPU_TITLE,LPU_JOURNAL,LPU_VOLUME,LPU_ISSUE,LPU_PAGES,LPU_DB,LPU_ACCESSION " +
								"FROM ISH_LINKED_PUBLICATION, ISH_SUB_PUB, ISH_SUBMISSION WHERE SUB_OID = SLN_SUBMISSION_FK AND LPU_OID = SLN_LINKEDPUB_FK AND " +
								"SUB_OID = ? AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
	
	//////////////////////////////////////////////////////////ACKNOWLEDGEMENT////////////////////////////////
	
	//ACKNOWLEDGEMENTS BY ID
	
	public static String ACKNOWLEDGEMENTS_BY_OID = "SELECT AKN_REASON FROM ISH_ACKNOWLEDGEMENT, ISH_SUBMISSION WHERE SUB_OID = ?  AND " +
						"AKN_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4";
	
	//////////////////////////////////////////////////////////LINKED SUBMISSION//////////////////////////////
	
	//LINKED SUBMISSIONS BY ID (second one is RAW and passed into FORMAT_LINKED_SUBMISSIONS
	
	public static String OID_BY_ACCESSION = "SELECT DISTINCT SUB_OID FROM ISH_SUBMISSION WHERE SUB_ACCESSION_ID = ?";
	
	public static String LINKED_SUBMISSIONS_BY_OID = "SELECT LSU_RESOURCE, SUB_ACCESSION_ID AS ACCESSION, LST_TYPE, LSU_URL " + 
	    "FROM ISH_LINKED_SUBMISSION, XPR_LINKED_TYPE, ISH_SUBMISSION " + 
	    "WHERE LSU_ACCESSION = ? " + 
	    "AND LSU_SUBMISSION_FK = SUB_OID " + 
	    "AND LSU_OID = LST_LSU_FK " + 
	    "UNION " + 
	    "SELECT LSU_RESOURCE, LSU_ACCESSION AS ACCESSION, LST_TYPE, LSU_URL " + 
	    "FROM ISH_LINKED_SUBMISSION, ISH_SUBMISSION, XPR_LINKED_TYPE " + 
	    "WHERE LSU_SUBMISSION_FK = ? " + 
	    "AND SUB_OID = LSU_SUBMISSION_FK " + 
	    "AND LST_LSU_FK = LSU_OID " +
	    "ORDER BY natural_sort(ACCESSION), LST_TYPE ";
	
	//////////////////////////////////////////////////////////TISSUE/////////////////////////////////////////
	
	//TISSUE BY ID
	
	public static String TISSUE_BY_OID = "SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE " +
	"JOIN ANA_TIMED_NODE ON ATN_NODE_FK = ANO_OID " +
	"JOIN ISH_SP_TISSUE ON IST_COMPONENT  = ATN_PUBLIC_ID " +
	"WHERE IST_SUBMISSION_FK = ?";
	
	
	
	/////////////////////////////////////////////////////////ANNOTATION TREE////////////////////////////////
	
	//IF (DISPLAY AS TREE )
	
	//STAGE NAME
	
	public static String STAGE_NAME_BY_OID = "SELECT CONCAT(STG_PREFIX,SUB_EMBRYO_STG),SPN_SPECIES,SUB_EMBRYO_STG  FROM ISH_SUBMISSION, ISH_SPECIMEN, REF_STAGE " +
						"WHERE SUB_OID = SPN_SUBMISSION_FK AND SUB_OID = ?";
	
	
	//IF (HUMAN)
	
	public static String STAGE_BY_SPECIES = "SELECT STG_VALUE FROM REF_STAGE, REF_HUMAN_STAGE WHERE HSS_MOUSE_FK = STG_OID AND HSS_CARNEGIE = ?";
	
	
	//TREE CONTENT (TODO CREATE BUNDLE (PERSPECTIVE)
	
	public static String TREE_CONTENT = "SELECT APO_DEPTH,APO_SEQUENCE, PATN.ATN_PUBLIC_ID, PARENT.ANO_COMPONENT_NAME, CONCAT('(',strt.STG_NAME,'-',end.sTG_NAME,')') AS 'RANGE', " +
	     "(select count(*) " +
	     "from ANA_RELATIONSHIP, ANA_NODE CHILD, ANA_TIMED_NODE CATN, ANA_STAGE CSTG " +
	     "where REL_PARENT_FK = PARENT.ANO_OID " +
	     "and REL_CHILD_FK  = CHILD.ANO_OID " +
	     "and CATN.ATN_NODE_FK = CHILD.ANO_OID " +
	     "and CSTG.STG_OID = CATN.ATN_STAGE_FK " +
	     "and CSTG.STG_SEQUENCE = stg.STG_SEQUENCE) as kids," +
	     "case when !APO_IS_PRIMARY_PATH OR ANO_IS_GROUP THEN 1 ELSE 0 END AS IP, " +
	     "exp.EXP_STRENGTH AS EXP, exp.EXP_ADDITIONAL_STRENGTH, exp.EXP_OID, CASE WHEN ENT_VALUE IS NULL THEN 0 ELSE 1 END AS E_NOTE, " +
	     "DEN_RELATIVE_TO_AGE, DEN_RELATIVE_TO_TOTAL, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE, "+  
	     "DNN_VALUE , CASE WHEN DNN_VALUE IS NULL THEN 0 ELSE 1 END AS D_NOTE " +
	   "FROM ANA_NODE PARENT " +
	   "JOIN ANAD_PART_OF " +
	     "ON APO_NODE_FK = PARENT.ANO_OID AND APO_FULL_PATH NOT LIKE '%mouse.embryo%' " +
	   "JOIN ANAD_PART_OF_PERSPECTIVE " +
	    "ON POP_PERSPECTIVE_FK = '" + bundle.getString("perspective") + "' "+
	     " AND POP_APO_FK = APO_OID " +
	   "JOIN ANA_TIMED_NODE PATN  " +
	     "ON PARENT.ANO_OID = PATN.ATN_NODE_FK " +
	   "JOIN ANA_STAGE stg " +
	     "ON stg.STG_OID = PATN.ATN_STAGE_FK " +
	     "AND stg.STG_NAME = ? " +
	   "JOIN ANA_STAGE strt " +
	     "ON APO_PATH_START_STAGE_FK = strt.STG_OID " +
	   "JOIN ANA_STAGE end " +
	     "ON APO_PATH_END_STAGE_FK = end.STG_OID AND  stg.STG_SEQUENCE BETWEEN strt.STG_SEQUENCE AND end.STG_SEQUENCE " +
	   "LEFT JOIN ISH_EXPRESSION exp " +
	    //"ON exp.EXP_SUBMISSION_FK = (select SUB_OID from ISH_SUBMISSION where SUB_ACCESSION_ID = ? ) " +
	     "ON exp.EXP_SUBMISSION_FK = ? " +
	     "AND exp.EXP_COMPONENT_ID = ATN_PUBLIC_ID " +
	   "LEFT JOIN ISH_EXPRESSION_NOTE " +
	     "ON exp.EXP_OID = ENT_EXPRESSION_FK " +
	     "LEFT JOIN ISH_DENSITY ON DEN_EXPRESSION_FK = EXP_OID "+
	     "LEFT JOIN ISH_DENSITY_NOTE ON DNN_DENSITY_FK = DEN_OID "+
	   "GROUP BY PARENT.ANO_COMPONENT_NAME,'RANGE',PATN.ATN_PUBLIC_ID, APO_SEQUENCE, APO_DEPTH,strt.STG_NAME,end.STG_NAME " +
	   "ORDER BY APO_SEQUENCE";
	
	////////////////////////////////////////////////////////EXPRESSION DETAIL/////////////////////////////
	
	//IS ANNOTATED?
	
	public static String TREE_ANNOTATIONS = "SELECT COUNT(*) FROM ISH_EXPRESSION, ISH_SUBMISSION WHERE EXP_SUBMISSION_FK = SUB_OID AND SUB_OID = ?";
	
	
	//ANNOTATED LIST
	
	public static String ANNOTATED_LIST_BY_OID = "SELECT DISTINCT EXP_OID, ATN_PUBLIC_ID, ANO_COMPONENT_NAME, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, CASE WHEN ENT_VALUE IS NULL THEN 0 ELSE 1 END AS E_NOTE, "+
			 "DEN_RELATIVE_TO_AGE, DEN_RELATIVE_TO_TOTAL, DEN_COMPONENT_ID, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE "+
	     "FROM ANA_NODE "+
	     "JOIN ANA_TIMED_NODE "+
	     "ON ANO_OID = ATN_NODE_FK "+
	     "JOIN ISH_EXPRESSION "+
	     "ON EXP_COMPONENT_ID = ATN_PUBLIC_ID "+
	     "JOIN ISH_SUBMISSION "+
	     "ON EXP_SUBMISSION_FK = SUB_OID AND SUB_OID = ? " +
	     "LEFT JOIN ISH_EXPRESSION_NOTE " +
	     "ON ENT_EXPRESSION_FK = EXP_OID "+
	     "LEFT JOIN ISH_DENSITY ON DEN_EXPRESSION_FK = EXP_OID "+
	     "ORDER BY ANO_COMPONENT_NAME";


    public static String EXPRESSION_PATTERNS = "SELECT DISTINCT PTN_OID, PTN_PATTERN FROM ISH_PATTERN WHERE PTN_EXPRESSION_FK = ?";
    
    public static String PATTERN_LOCATIONS = "SELECT DISTINCT LCN_LOCATION, LCN_PATTERN_FK, LCN_OID FROM ISH_LOCATION WHERE LCN_PATTERN_FK = ?";
    
    public static String COMPONENT_NAME_FROM_ATN_PUBLIC_ID = "SELECT ANO_COMPONENT_NAME FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ATN_PUBLIC_ID = ?";
    
    //LIST OF SUBMISSIONS WITH GENE SYMBOL
    public static String GENE_RELATED_SUBMISSIONS_ISH = "SELECT DISTINCT SUB_ACCESSION_ID, 'ish_submission.html', CONCAT(STG_PREFIX, SUB_EMBRYO_STG), SPN_ASSAY_TYPE,  " + 
                                  "CASE WHEN (EXP_SUBMISSION_FK > 0) THEN 'with annotation' " + 
                                  "ELSE 'without annotation' " + 
                                  "END, " +
                                  "CASE WHEN (SPN_SEX = 'unknown') THEN 'unknown sex' " + 
                                  "ELSE SPN_SEX " + 
                                  "END, " +
                                  "RPR_JAX_ACC, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '), " + 
                                  "CASE WHEN (LOCATE(';',GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ')) > 0) THEN " + 
                                  "CONCAT(SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; '),'; ',1),'...') " +
                                  "ELSE GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') " +
                                  "END, " + 
                                  "CASE WHEN (CONCAT(RPR_PREFIX,RPR_OID) =  RPR_JAX_ACC) THEN '' ELSE CONCAT(RPR_PREFIX,RPR_OID) END, " +
                            		"CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN " +
                             		"CONCAT('http://www.informatics.jax.org/accession/', RPR_JAX_ACC) " +
                             		"ELSE 'probe.html' END, " +
                            		"GROUP_CONCAT(DISTINCT ALE_ALLELE_NAME ORDER BY SAL_ORDER)  " +
                                  "FROM ISH_SUBMISSION " + 
                                  "JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " + 
                                  "JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " + 
                                  "JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " + 
                                  "JOIN REF_STAGE " +
                                  "LEFT JOIN ISH_EXPRESSION ON SUB_OID = EXP_SUBMISSION_FK " + 
                                  "LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
                                  "LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
                                  "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
                                  "LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC " +
                                  "LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +                                
                                  "WHERE RPR_SYMBOL = ? " + 
                                  "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
                                  "AND SUB_ASSAY_TYPE = ? " +
                                  "GROUP BY SUB_OID " +
                                  "ORDER BY CONCAT(STG_PREFIX, SUB_EMBRYO_STG), natural_sort(SUB_ACCESSION_ID)";

    



}
