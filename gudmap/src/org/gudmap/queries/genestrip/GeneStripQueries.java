package org.gudmap.queries.genestrip;

import java.util.Hashtable;

public class GeneStripQueries {
	
//	final static public Hashtable<String, String[]>  getRefTableAndColTofindGeneSymbols() {
//		  //each string array contains the main query at [0] and the column to be parameterised at [1]
//		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
//		  lookup.put("RefProbe_Symbol", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE ","RPR_SYMBOL"});
//		  lookup.put("RefProbe_Name", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE ","RPR_NAME"});
//		  lookup.put("RefGeneInfo_Symbol", new String [] {"SELECT DISTINCT GNF_SYMBOL FROM REF_GENE_INFO WHERE ","GNF_SYMBOL"});
//		  lookup.put("RefGeneInfo_Name", new String [] {"SELECT DISTINCT GNF_SYMBOL FROM REF_GENE_INFO WHERE ","GNF_NAME"});
//		  lookup.put("RefMgiMrk_MGIAcc", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK WHERE ","RMM_MGIACC"});
//		  lookup.put("RefEnsGene_EnsemblId", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK, REF_ENS_GENE WHERE RMM_MGIACC = REG_PRIMARY_ACC AND ","REG_STABLE"});
//		  lookup.put("RefSyn_Synonym", new String [] {"SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE ","RSY_SYNONYM"});
//		  lookup.put("RefGeneInfo_synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID JOIN REF_GENE_INFO ON RMM_SYMBOL = GNF_SYMBOL WHERE ","RSY_SYNONYM"});
//		  lookup.put("RefMgiMrkRefSyn_Synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL FROM REF_MGI_MRK,REF_SYNONYM WHERE RSY_REF = RMM_ID AND RMM_SYMBOL IN (SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE) AND ","RSY_SYNONYM"});
//		  lookup.put("RefProbe_MTFJax", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_MTF_JAX LIKE 'MTF%' AND ","RPR_MTF_JAX"});
//		  lookup.put("RefGoTerm_GoId", new String [] {"SELECT DISTINCT GOT_ID FROM REF_GO_TERM WHERE ","GOT_TERM"});
//		  lookup.put("RefMgiGoGene_MrkSymbol", new String [] {"SELECT DISTINCT GOG_MRK_SYMBOL FROM REF_MGI_GOGENE WHERE ","GOG_TERM"});
//		  return lookup;
//	  }

	  final static public Hashtable getRefTableAndColTofindGeneSymbols() {
		  //each string array contains the main query at [0] and the column to be parameterised at [1]
		  Hashtable<String, String[]> lookup = new Hashtable<String,String[]>();
		  lookup.put("RefProbe_Symbol", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RPR_LOCUS_TAG=RMM_MGIACC WHERE ","RPR_SYMBOL","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefProbe_Name", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RPR_LOCUS_TAG=RMM_MGIACC WHERE ","RPR_NAME","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefGeneInfo_Symbol", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON GNF_ID=RMM_MGIACC WHERE ","GNF_SYMBOL",""});
		  lookup.put("RefGeneInfo_Name", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON GNF_ID=RMM_MGIACC WHERE ","GNF_NAME",""});
		  lookup.put("RefMgiMrk_MGIAcc", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG WHERE ","RMM_MGIACC","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefEnsGene_EnsemblId", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG JOIN REF_ENS_GENE ON RMM_MGIACC = REG_PRIMARY_ACC WHERE ","REG_STABLE","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefSyn_Synonym", new String [] {"SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE ","RSY_SYNONYM",""});
		  lookup.put("RefGeneInfo_synonym", new String [] {"SELECT DISTINCT GNF_SYMBOL, GNF_ID, RMM_SPECIES FROM REF_GENE_INFO JOIN REF_MGI_MRK ON RMM_MGIACC = GNF_ID JOIN REF_SYNONYM ON RSY_REF=RMM_ID WHERE ","RSY_SYNONYM",""});
		  lookup.put("RefProbe_synonym", new String [] {"SELECT RPR_SYMBOL, RPR_LOCUS_TAG, RMM_SPECIES FROM REF_PROBE JOIN REF_MGI_MRK ON RMM_MGIACC=RPR_LOCUS_TAG JOIN REF_SYNONYM ON RSY_REF=RMM_ID WHERE ","RSY_SYNONYM","GROUP BY RPR_LOCUS_TAG"});
		  lookup.put("RefMgiMrkRefSyn_Synonym", new String [] {"SELECT DISTINCT RMM_SYMBOL,RMM_MGIACC,RMM_SPECIES FROM REF_MGI_MRK,REF_SYNONYM WHERE RSY_REF = RMM_ID AND RMM_MGIACC IN (SELECT DISTINCT RPR_LOCUS_TAG FROM REF_PROBE) AND ","RSY_SYNONYM",""});
		  lookup.put("RefProbe_MTFJax", new String [] {"SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_MTF_JAX LIKE 'MTF%' AND ","RPR_MTF_JAX",""});
		  lookup.put("RefGoTerm_GoId", new String [] {"SELECT DISTINCT GOT_ID FROM REF_GO_TERM WHERE ","GOT_TERM",""});
		  lookup.put("RefMgiGoGene_MrkSymbol", new String [] {"SELECT DISTINCT GOG_MRK_SYMBOL FROM REF_MGI_GOGENE WHERE ","GOG_TERM",""});
		  return lookup;
	  }
	
	  public static String GENE_RELATED_SUBMISSIONS_ISH = "SELECT DISTINCT SUB_ACCESSION_ID, 'viewSubmissionDetails.jsf', CONCAT(STG_PREFIX, SUB_EMBRYO_STG), SPN_ASSAY_TYPE,  " + 
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
	                           		"ELSE 'viewMaprobeDetails.jsf' END, " +
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
	                                "WHERE RPR_LOCUS_TAG = ? " + 
	                                "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " + 
	                                "AND (SUB_ASSAY_TYPE = 'ISH' ||SUB_ASSAY_TYPE = 'IHC'||SUB_ASSAY_TYPE = 'TG') " +
	                                "GROUP BY SUB_OID " +
	                                "ORDER BY CONCAT(STG_PREFIX, SUB_EMBRYO_STG), natural_sort(SUB_ACCESSION_ID)";
	  
	  //THIS CAN BRING BACK A DIFFERENT COUNT FROM GENE_RELATED_SUBMISSIONS_ISH. ALTHOUGH ALL THE VALUES ARE VALID. ALSO IN THE GENE PAGE FOR FULL RELATED ISH DATA
	  //THIS IS THE COUNT THAT IS GIVEN.
	  //TODO CHECK GENE_RELATED_SUBMISSIONS_ISH OR RELATED QUERY ON GENE DETAIL PAGE IS IT THE SAME AS ABOVE?
	  public static String GENE_RELATED_ISH_SUBMISSIONS_QUICK = "SELECT DISTINCT SUB_ACCESSION_ID FROM ISH_SUBMISSION JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
			  		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID WHERE RPR_SYMBOL = ? " +
			  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND " +
			  		"(SUB_ASSAY_TYPE = 'ISH' || SUB_ASSAY_TYPE = 'IHC' || SUB_ASSAY_TYPE = 'TG') ORDER BY natural_sort(SUB_ACCESSION_ID)";
	  
	  //ALL PARAMETERS ARE THE SAME GENE SYMBOL
	  /*public static String GENESTRIP_ROW_MINUS_PROFILES = "SELECT ? gene,  " +
			  	    "(SELECT GROUP_CONCAT(RSY_SYNONYM) FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID WHERE RMM_SYMBOL = gene) synonyms, "+
			  	    "(SELECT DISTINCT RPR_LOCUS_TAG FROM REF_PROBE WHERE RPR_SYMBOL=gene) mgi, " +
			  		"(SELECT CONCAT(MIN(MBC_STG_NAME),'-', MAX(MBC_STG_NAME)) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL=gene) arrayRange, " +
			        "(SELECT CONCAT(MIN(SUB_EMBRYO_STG),'-', MAX(SUB_EMBRYO_STG)) " +
			  		"FROM ISH_SUBMISSION,ISH_PROBE,REF_PROBE WHERE PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_SYMBOL=gene) ishRange, " +
			        "(SELECT COUNT(DISTINCT DGA_OMIMID) FROM LNK_GENE_OMIMDIS WHERE DGA_SYMBOL=gene) omim";*/
	  
	  /*public static String GENESTRIP_ROW_MINUS_PROFILES = "SELECT ? gene,  " +
		  	    "(SELECT GROUP_CONCAT(RSY_SYNONYM) FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID WHERE  RMM_SYMBOL = gene) synonyms, "+
		  	    "(SELECT DISTINCT RPR_LOCUS_TAG FROM REF_PROBE WHERE  RPR_SYMBOL=gene) mgi, " +
		  		"(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) FROM MIC_BROWSE_CACHE JOIN REF_STAGE ON STG_OID = MBC_SUB_STAGE_FK WHERE  MBC_GNF_SYMBOL=gene) arrayRange, " +
		        "(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_SYMBOL=gene) ishRange, " +
		  		"(SELECT DISTINCT STG_SPECIES " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_SYMBOL=gene) species, " +
		        "(SELECT COUNT(DISTINCT DGA_OMIMID) FROM LNK_GENE_OMIMDIS WHERE DGA_SYMBOL=gene) omim";*/
	  
	  public static String GENESTRIP_ROW_MINUS_PROFILES = "SELECT ? mgi, " +			    
		  	    "(SELECT GROUP_CONCAT(RSY_SYNONYM) FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID WHERE  RMM_MGIACC = mgi) synonyms, "+
		  	    "(SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_LOCUS_TAG = mgi) gene,  " +
		  		"(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) FROM MIC_BROWSE_CACHE JOIN REF_STAGE ON STG_OID = MBC_SUB_STAGE_FK WHERE  MBC_MAN_MGI_ID = mgi) arrayRange, " +
		        "(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_LOCUS_TAG = mgi) ishRange, " +
		  		"(SELECT DISTINCT STG_SPECIES " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_LOCUS_TAG = mgi) species, " +
		        "(SELECT COUNT(DISTINCT DGA_OMIMID) FROM LNK_GENE_OMIMDIS WHERE DGA_MGIACC = mgi) omim";
	  
	  public static String GENESTRIP_ROW_MINUS_PROFILES_2 = "SELECT DISTINCT RPR_LOCUS_TAG mgi, " +			    
		  	    "(SELECT GROUP_CONCAT(RSY_SYNONYM) FROM REF_SYNONYM JOIN REF_MGI_MRK ON RSY_REF = RMM_ID WHERE  RMM_MGIACC = mgi) synonyms, "+
		  	    "(SELECT DISTINCT RPR_SYMBOL FROM REF_PROBE WHERE RPR_LOCUS_TAG = mgi) gene,  " +
		  		"(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) FROM MIC_BROWSE_CACHE JOIN REF_STAGE ON STG_OID = MBC_SUB_STAGE_FK WHERE  MBC_MAN_MGI_ID = mgi) arrayRange, " +
		        "(SELECT CONCAT(MIN(STG_ORDER),'-', MAX(STG_ORDER)) " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_LOCUS_TAG = mgi) ishRange, " +
		  		"(SELECT DISTINCT STG_SPECIES " +
		  		"FROM ISH_SUBMISSION, REF_STAGE, ISH_PROBE,REF_PROBE WHERE STG_OID = SUB_STAGE_FK AND PRB_SUBMISSION_FK=SUB_OID AND PRB_MAPROBE=RPR_OID AND RPR_LOCUS_TAG = mgi) species, " +
		        "(SELECT COUNT(DISTINCT DGA_OMIMID) FROM LNK_GENE_OMIMDIS WHERE DGA_MGIACC = mgi) omim FROM REF_PROBE WHERE RPR_LOCUS_TAG IN (%s) " +
		        "ORDER BY %s %s LIMIT ?, ?";
	  
	  public static String GENESTRIP_TOTALS = "SELECT COUNT(DISTINCT RPR_LOCUS_TAG) mgi FROM REF_PROBE WHERE RPR_LOCUS_TAG IN (%s)";


	 /* public static String GENE_EXPRESSION_FOR_GIVEN_STRUCTURE = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
	  		"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
	  		"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
	  		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
	  		"WHERE RPR_LOCUS_TAG = ? " +
	  		"AND EXP_COMPONENT_ID IN " +
	  		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";

	  public static String GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
		"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
		"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
		"WHERE RPR_LOCUS_TAG = ? " +
		"AND EXP_COMPONENT_ID NOT IN " +
		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";*/
	  
	  //new anatomy
	  public static String GENE_EXPRESSION_FOR_GIVEN_STRUCTURE = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
		  		"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
		  		"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
		  		"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
		  		"WHERE RPR_LOCUS_TAG = ? " +
		  		"AND EXP_COMPONENT_ID IN " +
		  		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";

		  public static String GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE = "SELECT DISTINCT EXP_COMPONENT_ID, EXP_STRENGTH FROM ISH_EXPRESSION " +
			"JOIN ISH_SUBMISSION ON EXP_SUBMISSION_FK = SUB_OID AND SUB_ASSAY_TYPE IN ('ISH', 'IHC', 'TG') " +
			"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
			"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
			"WHERE RPR_LOCUS_TAG = ? " +
			"AND EXP_COMPONENT_ID NOT IN " +
			"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";
	  
	  public static String FIND_CHILD_NODE = "SELECT DISTINCT DESCEND_ATN.ATN_PUBLIC_ID " +
	  		"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
	  		"WHERE ANCES_ATN.ATN_PUBLIC_ID IN " + // add parent node when assembling the sql
	  		"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	  		"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	  		"AND ANCES_ATN.ATN_STAGE_FK  = DESCEND_ATN.ATN_STAGE_FK " +
	  		"AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
	  		"AND APO_NODE_FK = ANO_OID AND APO_IS_PRIMARY = true";
	  
	  public static String FIND_CHILD_NODE_EMAPA = "SELECT DISTINCT DESC_TN.ATN_PUBLIC_ID " +
	  		"FROM ANA_NODE " +
			"JOIN ANAD_RELATIONSHIP_TRANSITIVE ON RTR_ANCESTOR_FK = ANO_OID " +
	  		"JOIN ANA_TIMED_NODE ANCES_TN ON ANCES_TN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
			"JOIN ANA_TIMED_NODE DESC_TN ON DESC_TN.ATN_NODE_FK = RTR_DESCENDENT_FK " +
	  		"AND ANCES_TN.ATN_STAGE_FK = DESC_TN.ATN_STAGE_FK " +
			"WHERE ANO_PUBLIC_ID IN ";
//	  		"ORDER BY EXP_STRENGTH DESC, NATURAL_SORT(EXP_COMPONENT_ID)";
	  
	  public static String GET_GENE_SYNONYM_BY_SYMBOL = "SELECT RSY_SYNONYM FROM REF_SYNONYM " +
	  		"JOIN REF_MGI_MRK ON RSY_REF = RMM_ID " +
	  		"WHERE RMM_SYMBOL = ?";
	  
	  public static String MASTER_SECTION_LIST = "SELECT AMT_OID, AMC_OID, AMC_TITLE, AMC_DESCRIPTION, AMT_PLATFORM_ID " +
				"FROM MIC_ANALYSIS_MASTER " +
				"JOIN MIC_ANAL_MASTER_SECTION ON AMC_A_MASTER_FK = AMT_OID " +
				"WHERE AMT_STATUS = 1 " +
				"ORDER BY AMC_OID DESC ";
	  
	  /*public static String INSITU_SUBMISSION_IMAGE_ID_BY_GENE_SYMBOL = "SELECT CONCAT(SUB_OID, '_', IMG_FILENAME) FROM ISH_ORIGINAL_IMAGE " +
				"JOIN ISH_SUBMISSION ON IMG_SUBMISSION_FK = SUB_OID " +
				"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
				"WHERE RPR_SYMBOL = ? AND IMG_TYPE NOT LIKE '%wlz%' " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
				"ORDER BY SUB_EMBRYO_STG, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";*/
	  
	 /* public static String INSITU_SUBMISSION_IMAGE_ID_BY_GENE_ID = "SELECT CONCAT(SUB_OID, '_', IMG_FILENAME) FROM ISH_ORIGINAL_IMAGE " +
				"JOIN ISH_SUBMISSION ON IMG_SUBMISSION_FK = SUB_OID " +
				"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
				"JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK " +
				"WHERE RPR_LOCUS_TAG = ? AND IMG_TYPE NOT LIKE '%wlz%' " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
				"ORDER BY STG_ORDER, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";*/
	  
	  //only get images if they are flagged PUBLIC
	  public static String INSITU_SUBMISSION_IMAGE_ID_BY_GENE_ID = "SELECT CONCAT(SUB_OID, '_', IMG_FILENAME) FROM ISH_ORIGINAL_IMAGE " +
				"JOIN ISH_SUBMISSION ON IMG_SUBMISSION_FK = SUB_OID " +
				"JOIN ISH_PROBE ON PRB_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
				"JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK " +
				"WHERE RPR_LOCUS_TAG = ? AND IMG_TYPE NOT LIKE '%wlz%' " +
				"AND IMG_IS_PUBLIC = 1 " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
				"ORDER BY STG_ORDER, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";
	  
	  /*public static String INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID = "SELECT SUB_ACCESSION_ID, SUB_EMBRYO_STG, SPN_ASSAY_TYPE, " +
			  	"CONCAT(I.URL_URL, IMG_FILEPATH, 'medium/',IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME), " +
			  	"CONCAT(SUB_OID,'_',IMG_FILENAME), SUB_OID " +
				"FROM ISH_SUBMISSION " +
				"JOIN ISH_ORIGINAL_IMAGE ON IMG_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " +
				"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " +
				"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
				"WHERE IMG_TYPE NOT LIKE '%wlz%' AND CONCAT(SUB_OID, '_', IMG_FILENAME) IN " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
				"ORDER BY SUB_EMBRYO_STG, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";*/
	  
	  public static String INSITU_SUBMISSION_IMAGES_BY_IMAGE_ID = "SELECT SUB_ACCESSION_ID, STG_STAGE_DISPLAY, SPN_ASSAY_TYPE, " +
			  	"CONCAT(I.URL_URL, IMG_FILEPATH, 'medium/',IMG_FILENAME), CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, IMG_CLICK_FILENAME), " +
			  	"CONCAT(SUB_OID,'_',IMG_FILENAME), SUB_OID " +
				"FROM ISH_SUBMISSION " +
				"JOIN ISH_ORIGINAL_IMAGE ON IMG_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_URL I ON I.URL_OID = IMG_URL_FK " +
				"JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK " +
				"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
				"JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK " +
				"WHERE IMG_TYPE NOT LIKE '%wlz%' AND CONCAT(SUB_OID, '_', IMG_FILENAME) IN " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
				"ORDER BY STG_ORDER, natural_sort(SUB_ACCESSION_ID), IMG_ORDER";

}
