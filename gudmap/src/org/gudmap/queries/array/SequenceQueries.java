package org.gudmap.queries.array;

import org.gudmap.queries.generic.GenericQueries;

public class SequenceQueries {
	
	public final static String TOTAL_SEQUENCE_SERIES = "SELECT COUNT(DISTINCT NGR_GEO_ID) TOTAL FROM NGD_SERIES, NGD_SAMPLE_SERIES, NGD_SAMPLE, " +
			"ISH_SUBMISSION WHERE NGL_SERIES_FK = NGR_OID AND NGL_SAMPLE_FK = NGS_OID AND NGS_SUBMISSION_FK = SUB_OID AND " +
			GenericQueries.PUBLIC_ENTRIES;
	
	public final static String SEQUENCE_SERIES_BROWSE_PARAM = "SELECT DISTINCT NGR_TITLE series_title, NGR_GEO_ID geo_series_id, SUB_SOURCE source, " +
			"(SELECT COUNT(distinct NGL_SAMPLE_FK) FROM NGD_SAMPLE_SERIES WHERE NGL_SERIES_FK = NGR_OID) num_samples, NGP_LIBRARY_STRATEGY library_strategy, " +
			"GROUP_CONCAT(DISTINCT ANO_COMPONENT_NAME SEPARATOR ', ') components, NGR_OID series_oid FROM " +
			"NGD_SERIES, NGD_SAMPLE_SERIES, NGD_PROTOCOL, NGD_SAMPLE, ISH_SUBMISSION, ISH_SP_TISSUE, ISH_PERSON, ANA_NODE, ANA_TIMED_NODE " +
			"%s " +
			"NGL_SERIES_FK = NGR_OID AND NGL_SAMPLE_FK = NGS_OID AND NGS_SUBMISSION_FK = SUB_OID AND NGS_PROTOCOL_FK=NGP_OID AND " +
			"IST_SUBMISSION_FK=SUB_OID AND SUB_PI_FK = PER_OID AND ATN_PUBLIC_ID = IST_COMPONENT AND ATN_NODE_FK = ANO_OID AND " +
			GenericQueries.PUBLIC_ENTRIES  +
			"GROUP BY NGR_OID  ORDER BY %s %s, NATURAL_SORT(TRIM(NGR_GEO_ID)) LIMIT ?, ?";
	//"GROUP BY NGR_OID  ORDER BY PER_SURNAME, NATURAL_SORT(TRIM(NGR_GEO_ID)) LIMIT 0, 20";
	
	public final static String SEQUENCE_SAMPLE_BROWSE_PARAM="SELECT DISTINCT SUB_OID oid, SUB_ACCESSION_ID gudmap_accession, NGS_GEO_ID geo_sample_id, " +
			"NGS_OID series_oid, NGR_GEO_ID geo_series_id, SUB_SOURCE source, NGP_LIBRARY_STRATEGY library_strategy, " +
			"TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END) age, " +
			"SUB_SUB_DATE submission_date, NGS_SEX sex, NGS_DESCRIPTION sample_description, NGS_SAMPLE_NAME sample_name, CASE NGS_GENOTYPE WHEN 'true' THEN 'wild type' " +
			"ELSE CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
			"IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) " +
			"ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype, " +
			"GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME, ' (' , ATN_PUBLIC_ID, ')') SEPARATOR ', ') components, SUB_ASSAY_TYPE assay_type, " +
			"PER_OID person_oid, SUB_EMBRYO_STG stage " +
			"FROM ISH_SUBMISSION JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID JOIN NGD_SAMPLE_SERIES ON NGL_SAMPLE_FK = NGS_OID JOIN NGD_SERIES ON NGL_SERIES_FK = NGR_OID " +
			"JOIN NGD_PROTOCOL ON NGS_PROTOCOL_FK=NGP_OID JOIN ISH_PERSON ON PER_OID = SUB_PI_FK JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK=SUB_OID " +
			"JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID=IST_COMPONENT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID " +
			"LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
			"%s " +
			"SUB_ASSAY_TYPE = ? AND " + GenericQueries.PUBLIC_ENTRIES +
			"GROUP BY SUB_ACCESSION_ID  " +
			"ORDER BY %s %s, NATURAL_SORT(SUB_ACCESSION_ID) LIMIT ?, ?";
	//"ORDER BY natural_sort(NGS_GEO_ID)  ASC , NATURAL_SORT(SUB_ACCESSION_ID)";
	//"SUB_ASSAY_TYPE = 'NextGen'
	
	public final static String TOTAL_SEQUENCE_SAMPLE = "SELECT COUNT(DISTINCT SUB_ACCESSION_ID) TOTAL FROM ISH_SUBMISSION  " +
			"WHERE SUB_ASSAY_TYPE = ? AND " +GenericQueries.PUBLIC_ENTRIES;
			

	
}	