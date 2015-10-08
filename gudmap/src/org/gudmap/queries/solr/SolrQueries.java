package org.gudmap.queries.solr;



public class SolrQueries {
	
	public final static String GET_INSITU_DATA = "SELECT COUNT(DISTINCT MBC_SUB_ACCESSION_ID) " +
			"FROM MIC_BROWSE_CACHE, ANA_TIMED_NODE, ANA_NODE " +
			"WHERE ATN_PUBLIC_ID = MBC_COMPONENT_ID " +
			"AND ATN_NODE_FK = ANO_OID ";
	
	
}

