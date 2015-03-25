package org.gudmap.queries.genestrip;

public class GeneListQueries {
	
	/*public static String BROWSE_GENELIST_PARAM = "SELECT DISTINCT x.col1, GROUP_CONCAT(DISTINCT x.col2), x.col3, x.col4, x.col5, x.col6, x.col7, " +
					"x.col8, x.col9, x.col10, x.col11, x.col12, x.col13, x.col14, x.col15, col16, col17 " +
					"FROM " +
					"((select distinct QIC_RPR_SYMBOL col1, GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') col2, QIC_EXP_STRENGTH col3, " +
					"QIC_SUB_SOURCE col4, DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%M %%Y') col5, QIC_SUB_EMBRYO_STG col6, QIC_SPN_ASSAY_TYPE col7, " +
					"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE " +
					"CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8, QIC_SUB_THUMBNAIL col9, QIC_SUB_ACCESSION_ID col10, '' col11, '' col12, " +
					"REPLACE(QIC_SUB_ACCESSION_ID, ':', 'no') col13, QIC_SUB_ASSAY_TYPE col14,  QIC_SPN_SEX col15, QIC_PRB_PROBE_NAME col16, QIC_SPN_WILDTYPE col17 " +  
					"FROM " +
					"QSC_ISH_CACHE LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) " +
					"LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
					"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  WHERE QIC_RPR_SYMBOL in (%s) GROUP BY col10) " +
					"UNION " +
					"(select distinct MBC_GNF_SYMBOL col1,MBC_ANO_COMPONENT_NAME col2,'' col3,MBC_SUB_SOURCE col4,DATE_FORMAT(MBC_SUB_SUB_DATE,'%%e %%M %%Y') col5,MBC_SUB_EMBRYO_STG col6, " +
					"MBC_SPN_ASSAY_TYPE col7, concat(TRIM(CASE MBC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(MBC_SPN_STAGE,' ',MBC_SPN_STAGE_FORMAT) ELSE " +
					"CONCAT(MBC_SPN_STAGE_FORMAT,MBC_SPN_STAGE) END)) col8,'' col9,MBC_SUB_ACCESSION_ID col10, '' col11,'' col12, " +
					"'' col13, 'Microarray' col14, QMC_SPN_SEX col15, '' col16, QMC_SPN_WILDTYPE col17 " + 
					"FROM MIC_BROWSE_CACHE as Gene JOIN QSC_MIC_CACHE as Cache WHERE MBC_SUB_ACCESSION_ID=QMC_SUB_ACCESSION_ID  AND ( MBC_GNF_SYMBOL in (%s) )) " +
					"ORDER BY col1  ASC , col14, FIELD(col3, 'present', 'uncertain', 'not detected', ''), col6, col2  ) AS x " +
					"GROUP BY x.col10  order by x.col14, x.col1, FIELD(x.col3, 'present', 'uncertain', 'not detected', ''), x.col6, x.col2, x.col15 limit ?, ?";*/
	
	public static String BROWSE_GENELIST_PARAM = "SELECT DISTINCT x.oid, x.gene, x.gudmap_accession, x.source, x.submission_date, x.assay_type, x.probe_name, x.stage, x.age, x.sex, x.genotype, " +
			   "GROUP_CONCAT(DISTINCT x.tissue) tissue, x.expression, x.specimen, x.image " +
				"FROM ((" +
				"SELECT DISTINCT SUB_OID oid, RPR_SYMBOL gene, SUB_ACCESSION_ID gudmap_accession, SUB_SOURCE source, DATE_FORMAT(SUB_SUB_DATE,'%%e %%M %%Y') submission_date, " + 
				"IF(SUB_CONTROL=0,SUB_ASSAY_TYPE,CONCAT(SUB_ASSAY_TYPE,' control')) assay_type, RPR_JAX_ACC probe_name, SUB_EMBRYO_STG stage,  " +
				"TRIM(CASE SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(SPN_STAGE,' ',SPN_STAGE_FORMAT) ELSE CONCAT(SPN_STAGE_FORMAT,SPN_STAGE) END) age, SPN_SEX sex,  " +
				"CASE SPN_WILDTYPE WHEN 'Wild Type' THEN 'wild type' ELSE CASE WHEN  " +
				"(SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) IS NOT NULL THEN  " +
				"(SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) ELSE  " +
				"(SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype,  " +
				"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME) tissue, (SELECT GROUP_CONCAT(DISTINCT EXP_STRENGTH) FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK=SUB_OID) expression,  " +
				"SPN_ASSAY_TYPE specimen, CONCAT(IMG_URL.URL_URL, IMG_FILEPATH, IMG_URL.URL_SUFFIX, IMG_SML_FILENAME) image  " +
				"FROM ISH_SUBMISSION %s JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK JOIN ISH_PERSON ON SUB_PI_FK = PER_OID JOIN ISH_SPECIMEN ON SUB_OID = SPN_SUBMISSION_FK  " +
				"LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
				"LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE JOIN ISH_ORIGINAL_IMAGE ON SUB_OID = IMG_SUBMISSION_FK AND IMG_TYPE NOT LIKE '%%wlz%%'  " +
				"AND IMG_ORDER = (SELECT MIN(I.IMG_ORDER) FROM ISH_ORIGINAL_IMAGE I WHERE I.IMG_SUBMISSION_FK = SUB_OID) JOIN REF_URL IMG_URL ON IMG_URL.URL_OID = IMG_URL_FK  " + 
				"%s  RPR_SYMBOL IN (%s) " +
				"AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4  AND SUB_ASSAY_TYPE NOT IN ('Microarray','NextGen') %s  GROUP BY oid )  " +
				"UNION " +
				"(SELECT DISTINCT SUBSTRING(MBC_SUB_ACCESSION_ID,8) oid, MBC_GNF_SYMBOL gene, MBC_SUB_ACCESSION_ID gudmap_accession, MBC_SUB_SOURCE source , " +
				"DATE_FORMAT(MBC_SUB_SUB_DATE,'%%e %%M %%Y') submission_date, 'Microarray' assay_type, '' probe_name, MBC_SUB_EMBRYO_STG stage, " +
				"concat(TRIM(CASE MBC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(MBC_SPN_STAGE,' ',MBC_SPN_STAGE_FORMAT) ELSE " +
				"CONCAT(MBC_SPN_STAGE_FORMAT,MBC_SPN_STAGE) END)) age, QMC_SPN_SEX sex, QMC_SPN_WILDTYPE genotype, MBC_ANO_COMPONENT_NAME tissue, " +
				"'' expression,'' specimen,'' image " +
				"FROM MIC_BROWSE_CACHE as Gene JOIN QSC_MIC_CACHE as Cache %s MBC_SUB_ACCESSION_ID=QMC_SUB_ACCESSION_ID  AND ( MBC_GNF_SYMBOL in (%s) ) %s ) " +
				"ORDER BY gene  ASC , assay_type, FIELD(expression, 'present', 'uncertain', 'not detected', ''), stage, tissue  ) AS x  " +
				"GROUP BY x.gudmap_accession  ORDER BY %s %s, x.assay_type, x.gene, " +
				"FIELD(x.expression, 'present', 'uncertain', 'not detected', ''), x.stage, x.tissue, x.sex LIMIT ? , ?";
	
	 public final static String ISH_GENELIST_TOTAL = "SELECT COUNT(DISTINCT SUB_OID) TOTAL_INSITU FROM ISH_SUBMISSION %s JOIN ISH_PROBE ON SUB_OID = PRB_SUBMISSION_FK " +
			   "LEFT JOIN REF_PROBE ON RPR_OID = PRB_MAPROBE " +
			   "%s  RPR_SYMBOL IN (%s)  " +
			   "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 AND SUB_ASSAY_TYPE NOT IN ('Microarray','NextGen') %s";
	   
	 public final static String MICROARRAY_GENELIST_TOTAL = "SELECT COUNT(DISTINCT MBC_SUB_ACCESSION_ID) TOTAL_MICROARRAY FROM " +
			   "MIC_BROWSE_CACHE as Gene JOIN QSC_MIC_CACHE as Cache %s MBC_SUB_ACCESSION_ID=QMC_SUB_ACCESSION_ID  AND ( MBC_GNF_SYMBOL in (%s) ) %s";
			   
	 public final static String SEQUENCE_GENELIST_TOTAL = "SELECT 0 TOTAL_SEQUENCE"; 
	   
	 public static String BROWSE_GENE_FUNCTION_PARAM = "SELECT DISTINCT x.oid, x.gene, x.gudmap_accession, x.source, x.submission_date, x.assay_type, x.probe_name, x.stage, x.age, x.sex, x.genotype, " +
			 "GROUP_CONCAT(DISTINCT x.tissue) tissue, x.expression, x.specimen, x.image " +
			 "FROM ((" +
			 "SELECT DISTINCT SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%M %%Y') submission_date, " +
			 "QIC_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name, QIC_SUB_EMBRYO_STG stage, " +
			 "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) age, " +
			 "QIC_SPN_SEX sex, QIC_SPN_WILDTYPE genotype, " +
			 "GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR '; ') tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image " +
			 "FROM QSC_ISH_CACHE LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
			 "LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " + 
			 "%s  QIC_RPR_SYMBOL in (%s) GROUP BY gudmap_accession ) " +
			 "ORDER BY gene  ASC , assay_type, FIELD(expression, 'present', 'uncertain', 'not detected', ''), stage, tissue  ) AS x " +
			 "GROUP BY x.gudmap_accession  ORDER BY %s %s, x.assay_type, x.gene, FIELD(x.expression, 'present', 'uncertain', 'not detected', ''), x.stage, x.tissue, x.sex limit ?,?";
	
	public static String ISH_GENE_FUNCTION_TOTAL = "SELECT COUNT(DISTINCT gudmap_accession) FROM ((SELECT DISTINCT QIC_SUB_ACCESSION_ID gudmap_accession " +
				"FROM QSC_ISH_CACHE %s  QIC_RPR_SYMBOL in (%s) ) ) as TABLEA";

}
