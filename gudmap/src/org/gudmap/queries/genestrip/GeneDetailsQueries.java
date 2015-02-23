package org.gudmap.queries.genestrip;

public class GeneDetailsQueries {
	
	  public static String GENE_INFO = "SELECT DISTINCT RPR_SYMBOL, RPR_NAME, RPR_LOCUS_TAG, RPR_SYNONYMS, " +
	  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " + 
	  		                        "WHEN 'MGI:' THEN CONCAT(GENE_URL.URL_URL, RPR_LOCUS_TAG) " + 
	  		                        "ELSE /* NON MGI */ '' " + 
	  		                        "END, " +
	  		                        "RPR_ENSEMBL, CONCAT(ENS_URL.URL_URL,RPR_ENSEMBL), " +
	  		                        "CASE substring(RPR_LOCUS_TAG from 1 for position(':' in RPR_LOCUS_TAG)) " +
			                        "WHEN 'MGI:' THEN CONCAT(GO_URL.URL_URL, RPR_LOCUS_TAG) " + 
			                        "ELSE /* non-MGI */ '' " + 
			                        "END, " +
	  		                        "CONCAT(OMIM_URL.URL_URL,RPR_SYMBOL), " +
	  		                        "CONCAT(ENTREZ_URL.URL_URL,RPR_SYMBOL), " +
	  		                        "REG_CHROM_START, REG_CHROM_END, REG_CHROME_NAME, M.MIS_ENS_GENEBUILD, " +
	                                "CONCAT(GENECARDS_URL.URL_URL,RPR_SYMBOL,GENECARDS_URL.URL_SUFFIX), " +
	                                "CONCAT(HGNC_SYMBOL_SEARCH_URL.URL_URL,RPR_SYMBOL), " +
	                                "CONCAT(UCSC_URL.URL_URL, REG_CHROME_NAME, ':', REG_CHROM_START, '-', REG_CHROM_END, UCSC_URL.URL_SUFFIX) " + 
	  		                        "FROM REF_MISC M, ISH_PROBE " +
	  		                        "JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
	  		                        "LEFT JOIN REF_PROBE ON PRB_MAPROBE = RPR_OID " +
	  		                        "LEFT JOIN REF_ENS_GENE ON REG_STABLE = RPR_ENSEMBL " +
	  		                        "LEFT JOIN REF_MGI_MRK ON RMM_MGIACC = RPR_LOCUS_TAG " +
	  		                        "JOIN REF_URL GENE_URL " +
	  		                        "JOIN REF_URL GO_URL " +
	  		                        "JOIN REF_URL ENS_URL " +
	  		                        "JOIN REF_URL OMIM_URL " +
	  		                        "JOIN REF_URL ENTREZ_URL " +
	                                "JOIN REF_URL GENECARDS_URL "+
	                                "JOIN REF_URL HGNC_SYMBOL_SEARCH_URL "+
	                                "JOIN REF_URL UCSC_URL " + 
	  		                        "WHERE GENE_URL.URL_TYPE = 'jax_gene' " + 
	  		                        "AND GO_URL.URL_TYPE = 'go_gene' " +
	  		                        "AND ENS_URL.URL_TYPE = 'ens_gene' " +
	  		                        "AND OMIM_URL.URL_TYPE = 'omim_gene' " +
	  		                        "AND ENTREZ_URL.URL_TYPE = 'entrez_all' " +
	                                "AND GENECARDS_URL.URL_TYPE = 'genecards_gene' " +
	                                "AND HGNC_SYMBOL_SEARCH_URL.URL_TYPE = 'hgnc_symbol_search' " +
	                                "AND UCSC_URL.URL_TYPE = 'ucsc_genome' " + 
	  		                        "AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
	  		                        "AND RPR_SYMBOL = ?";
	  
	  public static String GENE_INFO_IUPHAR = "SELECT DISTINCT CONCAT(IUPHAR_URL.URL_URL, IUP_IUPHAR_ID), "+
	  		                        "CONCAT(IUPHAR_2_URL.URL_URL, IUP_IUPHAR_ID), IUP_URL_TYPE " +
	  		                        "FROM REF_IUPHAR "+
	  		                        "JOIN REF_URL IUPHAR_URL " +
	  		                        "JOIN REF_URL IUPHAR_2_URL " +
	  		                        "WHERE IUPHAR_URL.URL_TYPE = 'iuphar_gene' " + 
	  		                        "AND IUPHAR_2_URL.URL_TYPE = 'iuphar_gene2' " +
	  		                        "AND (IUP_SYMBOL = ? OR IUP_MGIACC = ?) ";
	  
	  public static String TOTAL_GENE_RELATED_ARRAYS = "SELECT COUNT(*) FROM MIC_BROWSE_CACHE WHERE MBC_GNF_SYMBOL = ?";
	  
	  public static String GET_GENE_SYMBOL_BY_SYNONYM = "SELECT RMM_SYMBOL FROM REF_MGI_MRK " +
				"JOIN REF_SYNONYM ON RMM_ID = RSY_REF " +
				"WHERE RSY_SYNONYM = ? ";
	  
	  public static String GENE_SYNONYM_INFO_ARRAY = "SELECT RSY_SYNONYM FROM REF_SYNONYM WHERE RSY_REF = ?";
	  
	  //query to find maprobs associated with a particular gene entry
	  public static String GENE_RELATED_MAPROBE = "SELECT DISTINCT '', RPR_JAX_ACC, CONCAT(RPR_PREFIX,RPR_OID), " + 
	  		"CASE substring(RPR_JAX_ACC from 1 for 4)  WHEN 'MGI:' THEN " +
	   		"CONCAT('http://www.informatics.jax.org/accession/', RPR_JAX_ACC) " +
	   		"ELSE 'viewMaProbeDetails.jsf' END " +
	  		"FROM REF_PROBE " +
	  		"JOIN ISH_PROBE ON PRB_MAPROBE = RPR_OID " +
	  		"JOIN ISH_SUBMISSION ON SUB_OID = PRB_SUBMISSION_FK " +
	  		"LEFT JOIN REF_MGI_PRB ON RMP_MGIACC = RPR_JAX_ACC " +  		
	  		"WHERE SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
	  		"AND RPR_SYMBOL = ? ";

}
