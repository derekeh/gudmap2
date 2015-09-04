package org.gudmap.queries.array;

import org.gudmap.queries.generic.GenericQueries;

public class ArrayQueries {
	
	public final static String TOTAL_CACHE_ARRAY_SUBMISSIONS = "SELECT COUNT(DISTINCT MBC_SUB_ACCESSION_ID) " +
			"FROM MIC_BROWSE_CACHE, ANA_TIMED_NODE, ANA_NODE " +
			"WHERE ATN_PUBLIC_ID = MBC_COMPONENT_ID " +
			"AND ATN_NODE_FK = ANO_OID ";
	
	
	
	public final static String TOTAL_ARRAY_SUBMISSIONS = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) FROM ISH_SUBMISSION " +
		  		"JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID " +
		  		"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK " +
		  		"JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
		  		"JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID " +
		  		"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK " +
		  		"JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK " +
		  		"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID " +
		  		"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
		  		"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
		  		"WHERE SUB_ASSAY_TYPE = 'Microarray' " +
		  		"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 ";
	
	public static String GET_GENE_BY_SYMBOL = "SELECT GNF_SYMBOL, GNF_NAME from REF_GENE_INFO WHERE GNF_SYMBOL = ";
	
	 //query to find mgi info linked to a specific probe set
	public static String GENE_INFO_FOR_ARRAY = "SELECT RMM_SYMBOL, RMM_ID, RMM_MGIACC, CONCAT(GENE_URL.URL_URL, RMM_MGIACC), REG_STABLE, " + 
	                                "CONCAT(ENS_URL.URL_URL, REG_STABLE), " + 
	                                "CONCAT(GO_URL.URL_URL, RMM_MGIACC), " + 
	                                "CONCAT(OMIM_URL.URL_URL, RMM_SYMBOL), " + 
	                                "CONCAT(ENTREZ_URL.URL_URL, RMM_SYMBOL), " + 
	                                "REG_CHROM_START, REG_CHROM_END, REG_CHROME_NAME, M.MIS_ENS_GENEBUILD, " + 
	                                "CONCAT(GENECARDS_URL.URL_URL,RMM_SYMBOL,GENECARDS_URL.URL_SUFFIX), " + 
	                                "CONCAT(HGNC_SYMBOL_SEARCH_URL.URL_URL,RMM_SYMBOL)  FROM REF_MISC M, REF_MGI_MRK " + 
	                                "LEFT JOIN REF_ENS_GENE " + 
	                                " ON RMM_MGIACC = REG_PRIMARY_ACC " + 
	                                "JOIN REF_URL GENE_URL  JOIN REF_URL ENS_URL  JOIN REF_URL GO_URL  JOIN REF_URL OMIM_URL  JOIN REF_URL ENTREZ_URL  JOIN REF_URL GENECARDS_URL " + 
	                                "JOIN REF_URL HGNC_SYMBOL_SEARCH_URL " + 
	                                "WHERE GENE_URL.URL_TYPE = 'jax_gene' " + 
	                                "AND ENS_URL.URL_TYPE = 'ens_gene'  " +
	                                "AND GO_URL.URL_TYPE = 'go_gene'  " +
	                                "AND OMIM_URL.URL_TYPE = 'omim_gene'  " +
	                                "AND ENTREZ_URL.URL_TYPE = 'entrez_all'  " +
	                                "AND GENECARDS_URL.URL_TYPE = 'genecards_gene'  " +
	                                "AND HGNC_SYMBOL_SEARCH_URL.URL_TYPE = 'hgnc_symbol_search' " + 
	                                "AND RMM_SYMBOL = ?";
	

	////////////////////////microarray browse//////////////////
	
	/*public static String MIC_SERIES_BROWSE = "SELECT DISTINCT SER_TITLE series_title, SER_GEO_ID geo_series_id, " +
			"(SELECT COUNT(distinct SRM_SAMPLE_FK) FROM MIC_SERIES_SAMPLE WHERE SRM_SERIES_FK = SER_OID) num_samples, " +
			"SUB_SOURCE source, PLT_GEO_ID platform, SER_OID series_oid, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') components " +
			"FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, MIC_PLATFORM, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +
			"WHERE SRM_SERIES_FK = SER_OID AND SRM_SAMPLE_FK = SMP_OID AND SMP_SUBMISSION_FK = SUB_OID AND EXP_SUBMISSION_FK=SUB_OID AND SUB_PI_FK = PER_OID " +
			"AND PLT_OID = SER_PLATFORM_FK AND ATN_PUBLIC_ID = EXP_COMPONENT_ID AND ATN_NODE_FK = ANO_OID AND " +
			GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY SER_GEO_ID  ORDER BY %s %s PER_SURNAME, NATURAL_SORT(TRIM(SER_GEO_ID)) LIMIT ?,?";*/
	
	public static String MIC_SERIES_BROWSE_PARAM = "SELECT DISTINCT SER_TITLE series_title, SER_GEO_ID geo_series_id, " +
			"(SELECT COUNT(distinct SRM_SAMPLE_FK) FROM MIC_SERIES_SAMPLE WHERE SRM_SERIES_FK = SER_OID) num_samples, " +
			"SUB_SOURCE source, PLT_GEO_ID platform, SER_OID series_oid, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') components " +
			"FROM MIC_SERIES JOIN MIC_SERIES_SAMPLE ON SRM_SERIES_FK = SER_OID JOIN MIC_SAMPLE ON SRM_SAMPLE_FK = SMP_OID JOIN ISH_SUBMISSION ON SMP_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID JOIN MIC_PLATFORM ON PLT_OID = SER_PLATFORM_FK JOIN ISH_PERSON ON SUB_PI_FK = PER_OID " +			
			"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = EXP_COMPONENT_ID JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s AND " +GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY SER_GEO_ID  " +
			"ORDER BY %s %s, NATURAL_SORT(TRIM(SER_GEO_ID)) LIMIT ?, ?";
	
	public static String COUNT_TOTAL_MIC_SERIES = "SELECT COUNT(*) FROM " +
			"(SELECT DISTINCT SER_TITLE, SER_GEO_ID, SUB_SOURCE, SER_OID FROM MIC_SERIES JOIN MIC_SERIES_SAMPLE ON SRM_SERIES_FK = SER_OID " +
			"JOIN MIC_SAMPLE ON SRM_SAMPLE_FK = SMP_OID JOIN  ISH_SUBMISSION ON SMP_SUBMISSION_FK = SUB_OID " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s AND " + GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY SER_GEO_ID ) AS S_TABLE";
	
	
	/*public static String MIC_SAMPLE_BROWSE_PARAM = "SELECT DISTINCT SUB_OID oid, SUB_ACCESSION_ID gudmap_accession, SMP_GEO_ID geo_sample_id, " +
			"SUB_EMBRYO_STG stage,TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) age, " +
			"SUB_SOURCE source, DATE_FORMAT(SUB_SUB_DATE,'%%e %%b %%Y') submission_date, SMP_SEX sex, SRM_SAMPLE_DESCRIPTION sample_description, SMP_TITLE sample_title, " +
			"SER_GEO_ID geo_series_id, GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') components, " +
			"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  " +
			"WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
			"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, " +
			"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype, SUB_ASSAY_TYPE assay_type, SPN_ASSAY_TYPE specimen_assay_type,  " +
			"PER_OID person_oid, SER_OID series_oid " +
			"FROM " +
			"ISH_SUBMISSION JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
			"JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID JOIN ISH_PERSON ON PER_OID = SUB_PI_FK JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID " +
			"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s %s AND " +GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY oid " +
			"ORDER BY %s %s,  NATURAL_SORT(SUB_ACCESSION_ID) LIMIT ?, ?";*/
	
	public static String MIC_SAMPLE_BROWSE_PARAM = "SELECT DISTINCT SUB_OID oid, SUB_ACCESSION_ID gudmap_accession, SMP_GEO_ID geo_sample_id, " +
			"STG_STAGE_DISPLAY stage, STG_SPECIES species, STG_ALT_STAGE age, " +
			"SUB_SOURCE source, DATE_FORMAT(SUB_SUB_DATE,'%%e %%b %%Y') submission_date, SMP_SEX sex, SRM_SAMPLE_DESCRIPTION sample_description, SMP_TITLE sample_title, " +
			"SER_GEO_ID geo_series_id, GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') components, " +
			"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  " +
			"WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, " +
			"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, " +
			"LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype, SUB_ASSAY_TYPE assay_type, SPN_ASSAY_TYPE specimen_assay_type,  " +
			"PER_OID person_oid, SER_OID series_oid " +
			"FROM " +
			"ISH_SUBMISSION JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
			"JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID JOIN ISH_PERSON ON PER_OID = SUB_PI_FK JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID " +
			"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s %s AND " +GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY oid " +
			"ORDER BY %s %s,  NATURAL_SORT(SUB_ACCESSION_ID) LIMIT ?, ?";
	
	public static String COUNT_TOTAL_MIC_SAMPLE = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM ISH_SUBMISSION JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID " +
			"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK  JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
			"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s %s AND " + GenericQueries.PUBLIC_ENTRIES;
	
	/*public static String COUNT_TOTAL_MIC_SAMPLE = "SELECT COUNT(DISTINCT SUB_OID) TOTAL FROM ISH_SUBMISSION JOIN MIC_SAMPLE ON SMP_SUBMISSION_FK = SUB_OID " +
			"JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID JOIN MIC_SERIES ON SRM_SERIES_FK = SER_OID " +
			"JOIN ISH_PERSON ON PER_OID = SUB_PI_FK JOIN ISH_EXPRESSION ON EXP_SUBMISSION_FK=SUB_OID JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=EXP_COMPONENT_ID " +
			"JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
			"%s " +
			"SUB_ASSAY_TYPE = ? %s %s AND " + GenericQueries.PUBLIC_ENTRIES;*/
	
	public static String MIC_PLATFORM_BROWSE_PARAM="SELECT DISTINCT PLT_GEO_ID geo_platform_id, PLT_NAME platform_name, PLT_TECHNOLOGY platform_technology, " +
			"PLT_MANUFACTURER platform_manufacturer, (SELECT COUNT(*) FROM MIC_SERIES S WHERE S.SER_PLATFORM_FK = PLT_OID) num_series " +
			"FROM MIC_PLATFORM, MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION " +
			"WHERE SRM_SERIES_FK = SER_OID and SRM_SAMPLE_FK= SMP_OID and SER_PLATFORM_FK=PLT_OID and SMP_SUBMISSION_FK=SUB_OID and EXP_SUBMISSION_FK=SUB_OID  " +
			"ORDER BY %s %s  LIMIT ?, ?";
			//"ORDER BY natural_sort(TRIM(PLT_GEO_ID))";
	
	public static String COUNT_TOTAL_MIC_PLATFORM = "SELECT COUNT(DISTINCT PLT_GEO_ID) TOTAL  FROM MIC_PLATFORM, MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
			"WHERE SRM_SERIES_FK = SER_OID and SRM_SAMPLE_FK= SMP_OID and SER_PLATFORM_FK=PLT_OID and SMP_SUBMISSION_FK=SUB_OID";
	
	public static String ARRAY_SERIES = "SELECT SER_GEO_ID geo_series_id, COUNT(distinct SRM_SAMPLE_FK) num_samples, SER_TITLE title, SER_SUMMARY summary, " +
			"SER_TYPE type, SER_OVERALL_DESIGN overall_design, SER_OID series_oid, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') components, " +
			"SUB_ARCHIVE_ID archive_id, SUB_BATCH batch_id FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, " +
			"ANA_TIMED_NODE WHERE SER_OID = ? AND SRM_SERIES_FK = SER_OID AND SRM_SAMPLE_FK = SMP_OID AND SMP_SUBMISSION_FK = SUB_OID AND EXP_SUBMISSION_FK=SUB_OID " +
			"AND ATN_PUBLIC_ID = EXP_COMPONENT_ID AND ATN_NODE_FK = ANO_OID GROUP BY SER_GEO_ID, SER_TITLE, SER_SUMMARY, SER_TYPE, SER_OVERALL_DESIGN";

	public final static String GET_ALL_REF_GENELISTS = "SELECT GNL_OID,GNL_UID,GNL_SHORT_NAME,GNL_DESCRIPTION,GNL_SERIES_PLATFORM," +
			"GNL_PLATFORM_GEO_ID,GNL_KEY_SAMPLE,GNL_EMAP_ID,GNL_MA_DATASET,GNL_MA_DATASET_ID,GNL_GEN_METHOD,GNL_ENTITY_TYPE," +
			"GNL_TOTAL_ENTITIES,GNL_TOTAL_GENES,GNL_AUTHOR,GNL_DATE,GNL_VERSION,GNL_REFERENCE,GNL_IS_PUBLISHED,GNL_OTHER_REFS," +
			"GNL_STAGE,GNL_GENELIST_TYPE,GNL_SEX,GNL_SUBSET_1,GNL_SUBSET_2,GNL_SUBSET_3,GNL_AMG_OID_FK,LPU_REF " +
			"FROM REF_GENELISTS LEFT JOIN ISH_LINKED_PUBLICATION ON LPU_OID = GNL_REFERENCE";
	
	
	public final static String GET_ALL_REF_GENELISTS_RNASEQ = "SELECT GLS_OID,GLS_SHORT_NAME,GLS_LONG_NAME,GLS_DESCRIPTION,GLS_CLASS,GLS_SUBCLASS," +
			"GLS_CLUSTER,GLS_TOTAL_GENES,GLS_HEATMAP,GLS_EMAPA_ID,GLS_GO_BP,GLS_GO_BP_GENES,GLS_GO_CC,GLS_GO_CC_GENES," +
			"GLS_MOUSE_PHENO,GLS_MOUSE_PHENO_GENES,GLS_AUTHOR,GLS_DATE,GLS_REFERENCE,GLS_IS_PUBLISHED,GLS_STAGE " +
			"FROM REF_GENELISTS_RNASEQ LEFT JOIN ISH_LINKED_PUBLICATION ON LPU_OID = GLS_REFERENCE";

	public static String ARRAY_SUPPLEMENTARY_FILES = "SELECT DISTINCT SMP_FILE_LOCATION location,SMP_CEL_FILENAME cel_file, SMP_CHP_FILENAME chp_file, " +
			"SMP_RPT_FILENAME rpt_file, SMP_EXP_FILENAME exp_file, SMP_TXT_FILENAME txt_file " +
            "FROM MIC_SAMPLE JOIN ISH_SUBMISSION ON SUB_OID = ? AND SMP_SUBMISSION_FK = SUB_OID";
	
	public static String ARRAY_SINGLE_SAMPLE = "SELECT SMP_GEO_ID geo_sample_id, SMP_TITLE title, SMP_SOURCE source, SMP_ORGANISM organism, SMP_STRAIN strain, SMP_MUTATION mutation, " +
	  		"SMP_SEX sex, SMP_DEVELOPMENT_STAGE dev_stage, SMP_THEILER_STAGE stage, SMP_DISSECT_METHOD dissect_method, SMP_MOLECULE  molecule, SMP_A260_280 a260, " +
	  		"SMP_RNA_EXTRACT_PROTOCOL extract_protocol, SMP_TGT_AMP_MANUFACTURER manufacturer, SMP_TGT_AMP_PROTOCOL amp_protocol, SMP_ROUNDS_OF_AMP rounds, SMP_AMT_LAB_TGT_HYB_TO_ARY tgt_hyb, " +
	  		"SMP_LABEL label, SMP_ARY_HYB_PROTOCOL hyb_protocol, SMP_GCOS_TGT_VAL gcos, SMP_DATA_ANA_METHOD data_ana_method, SMP_REFERENCE_USED reference, " +
	  		"SRM_SAMPLE_DESCRIPTION description, SMP_EXPERIMENTAL_DESIGN design, " + 
	  		"SMP_LABEL_PROTOCOL label_protocol, SMP_SCAN_PROTOCOL scan_protocol, SMP_POOL_SIZE pool_size, SMP_POOLED_SAMPLE pooled_sample, TRIM(SMP_DEVELOPMENT_LANDMARK) landmark " +
	  		"FROM MIC_SAMPLE " +
	  		"JOIN ISH_SUBMISSION ON SMP_SUBMISSION_FK = SUB_OID " +
	  		"JOIN MIC_SERIES_SAMPLE ON SRM_SAMPLE_FK = SMP_OID " +
	  		"WHERE SUB_OID = ? ";
	
	public static String SAMPLE_ANATOMY = "SELECT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ') ') from ISH_SUBMISSION "+
			  "JOIN ISH_EXPRESSION on EXP_SUBMISSION_FK=SUB_OID "+
			  "JOIN ANA_TIMED_NODE on ATN_PUBLIC_ID=EXP_COMPONENT_ID "+
			  "JOIN ANA_NODE on ATN_NODE_FK = ANO_OID where SUB_OID=?"; 
	
	 public static String ARRAY_SINGLE_SERIES = "SELECT SER_GEO_ID geo_series_id, SER_TITLE title, SER_SUMMARY summary, SER_TYPE type, " +
			 "SER_OVERALL_DESIGN overall_design, SER_OID series_oid " +
             "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
             "WHERE SUB_OID = ? " +
             "AND SUB_OID=SMP_SUBMISSION_FK " +
             "AND SRM_SAMPLE_FK=SMP_OID " +
             "AND SRM_SERIES_FK = SER_OID";
	 
	 public static String ARRAY_NUM_SAMPLES = "SELECT COUNT(SMP_GEO_ID) total " +
             "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE " +
             "WHERE SRM_SAMPLE_FK=SMP_OID " +
             "AND SRM_SERIES_FK = SER_OID " +
             "AND SER_OID IN " +
             "(SELECT SER_OID " +
             "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION " +
             "WHERE SUB_OID= ? " +
             "AND SUB_OID=SMP_SUBMISSION_FK " +
             "AND SRM_SAMPLE_FK=SMP_OID " +
             "AND SRM_SERIES_FK = SER_OID)";
	 
	 public static String ARRAY_SINGLE_PLATFORM= "SELECT PLT_GEO_ID  geo_platform_id, PLT_TITLE title, PLT_NAME name, PLT_DISTRIBUTION distribution, " +
			 "PLT_TECHNOLOGY technology, PLT_ORGANISM organism, PLT_MANUFACTURER manufacturer, PLT_MANUFACTURER_PROTOCOL protocol, PLT_CATALOGUE_NO catno " +
             "FROM MIC_SERIES, MIC_SERIES_SAMPLE, MIC_SAMPLE, ISH_SUBMISSION, MIC_PLATFORM " +
             "WHERE SUB_OID = ? " +
             "AND SUB_OID=SMP_SUBMISSION_FK " +
             "AND SRM_SAMPLE_FK=SMP_OID " +
             "AND SRM_SERIES_FK = SER_OID " +
             "AND SER_PLATFORM_FK=PLT_OID";
	 
	 public static String ARRAY_SEQ_IMAGES = "SELECT SUB_ACCESSION_ID gudmap_accession, CONCAT(I.URL_URL, IMG_FILEPATH, IMG_FILENAME) filepath, CONCAT(C.URL_URL, IMG_CLICK_FILEPATH, " +
			 "IMG_CLICK_FILENAME) url FROM ISH_ORIGINAL_IMAGE JOIN ISH_SUBMISSION ON SUB_OID = ? AND  IMG_SUBMISSION_FK = SUB_OID  " +
			 "JOIN REF_URL I ON I.URL_OID = IMG_URL_FK JOIN REF_URL C ON C.URL_OID = IMG_CLICK_URL_FK WHERE SUB_IS_DELETED = 0 AND " +
			 "IMG_IS_PUBLIC = 1 AND IMG_TYPE NOT LIKE '%wlz%' AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE= ? ORDER BY IMG_ORDER ";
	 

	
	 public static String ARRAY_TISSUE="SELECT DISTINCT ANO_COMPONENT_NAME FROM ANA_NODE " +
		"JOIN ANA_TIMED_NODE ON ATN_NODE_FK = ANO_OID " +
		"JOIN ISH_SP_TISSUE ON IST_COMPONENT  = ATN_PUBLIC_ID " +
		"WHERE IST_SUBMISSION_FK = ?";

}

