package org.gudmap.queries.array;

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

}
