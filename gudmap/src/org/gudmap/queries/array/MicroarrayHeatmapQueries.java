package org.gudmap.queries.array;

import java.util.ResourceBundle;

public class MicroarrayHeatmapQueries {
	
public static ResourceBundle bundle = ResourceBundle.getBundle("org.gudmap.bundles.config");

public static String MASTER_SECTION_LIST = "SELECT AMT_OID, AMC_OID, AMC_TITLE, AMC_DESCRIPTION, AMT_PLATFORM_ID FROM MIC_ANALYSIS_MASTER  JOIN MIC_ANAL_MASTER_SECTION ON AMC_A_MASTER_FK = AMT_OID  WHERE AMT_STATUS = 1 ORDER BY AMC_OID DESC";

public static String PLATFORM_ID_FROM_MASTERTABLE_ID = "SELECT AMT_PLATFORM_ID FROM MIC_ANALYSIS_MASTER WHERE AMT_OID = ? ";

public static String PROBESET_FROM_GENEID_AND_PLATFORM_ID = "SELECT DISTINCT PRS_PROBE_SET_ID FROM MIC_PROBE_SET JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID WHERE GNF_ID= ? AND PRS_PLATFORM_ID = ?";

public static String EXPRESSION_TITLES_FROM_MASTERTABLE_ID = "SELECT AMH_ORDER, 'Master Expression', AMG_DISPLAY_NAME, AMG_DESCRIPTION FROM MIC_ANAL_MSTR_GRP_HEADER JOIN MIC_ANAL_MASTER_GROUP ON AMH_AM_GROUP_FK = AMG_OID WHERE AMH_A_MASTER_FK = ? AND AMG_SECTION_FK = ? ";

public static String HEATMAP_DATA_FROM_PROBE_ID_AND_MASTERTABLE_ID = "SELECT AME_M_HEADER_FK, AME_PROBE_SET_FK, AME_VALUE_RMA, AMS_MEDIAN, AMS_STD FROM MIC_ANAL_MSTR_EXPRESSION JOIN MIC_ANAL_MSTR_GRP_HEADER ON AME_M_HEADER_FK = AMH_OID JOIN MIC_PROBE_SET ON AME_PROBE_SET_FK = PRS_OID JOIN MIC_ANAL_MASTER_GROUP ON AMG_OID = AMH_AM_GROUP_FK JOIN MIC_ANAL_MSTR_EXP_SUMMARY ON AMS_A_MASTER_FK = AMH_A_MASTER_FK AND AMS_PROBE_SET_FK = PRS_OID WHERE PRS_PROBE_SET_ID IN (?)  AND AMH_A_MASTER_FK = ? AND AMG_SECTION_FK = ? ORDER BY AME_M_HEADER_FK, FIELD(PRS_PROBE_SET_ID, ?)";

public static String PROBESET_FROM_GENELIST_ID_AND_PLATFORM_ID = "SELECT GNL_PLATFORM_GEO_ID FROM REF_GENELISTS WHERE GNL_OID = ? ";

public static String ARRAY_PROBESET_FROM_SYMBOL_AND_PLATFORM_ID = "SELECT DISTINCT PRS_PROBE_SET_ID FROM MIC_PROBE_SET JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID WHERE GNF_SYMBOL = ? AND PRS_PLATFORM_ID = ?";

public static String GENE_SYMBOL_BY_ANALYSIS_GENELIST_ID = "SELECT DISTINCT GNF_SYMBOL " +
		"FROM MIC_ANAL_GLST_COLUMN_ITEM " +
		"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
		"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
		"JOIN MIC_PROBE_SET ON GCT_PROBE_SET_ID = PRS_PROBE_SET_ID " +
		"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
		"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
		"WHERE ANG_OID = ? " + // genelist id
		"AND PRS_PLATFORM_ID = ? ";

public static String GENE_SYMBOL_BY_ANALYSIS_GENELIST_CLUSTER_ID = "SELECT DISTINCT GNF_SYMBOL " +
		"FROM MIC_ANAL_GLST_COLUMN_ITEM " +
		"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
		"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
		"JOIN MIC_PROBE_SET ON GCT_PROBE_SET_ID = PRS_PROBE_SET_ID " +
		"JOIN MIC_PROBE_SET_GENE ON PSG_PROBE_SET_FK = PRS_OID " +
		"JOIN REF_GENE_INFO ON PSG_GENE_INFO_FK = GNF_OID " +
		"WHERE ANG_OID = ? " +
		"AND PRS_PLATFORM_ID = ? " +
		"AND AGC_OID = ? ";

public static String ANALYSIS_GENELIST_PLATFORM = "SELECT GNL_PLATFORM_GEO_ID FROM REF_GENELISTS WHERE GNL_OID = ? ";

public static String ANALYSIS_GENELIST_PLATFORM_BY_CLUSTER_ID = "SELECT ANG_PLATFORM_ID " +
		"FROM MIC_ANALYSIS_GENELIST " +
		"JOIN MIC_ANAL_GENELIST_COLUMN ON AGC_ANAL_GENELIST_FK = ANG_OID " +
		"WHERE ANG_OID = ? " +
		"AND AGC_OID = ? ";

public static String PROBE_SET_ID_BY_ANALYSIS_GENELIST_ID = "SELECT GLE_ENTITY_ID FROM GNL_ENTITIES " +
		"JOIN REF_GENELISTS ON GLE_LIST_ID = GNL_OID " +
		"WHERE GNL_OID = ? " +
		"ORDER BY GLE_ORDER ";

public static String PROBE_SET_ID_BY_ANALYSIS_GENELIST_CLUSTER_ID = "SELECT GCT_PROBE_SET_ID FROM MIC_ANAL_GLST_COLUMN_ITEM  " +
		"JOIN MIC_ANAL_GENELIST_COLUMN ON GCT_GLST_COLUMN_FK = AGC_OID " +
		"JOIN MIC_ANALYSIS_GENELIST ON AGC_ANAL_GENELIST_FK = ANG_OID " +
		"WHERE ANG_OID = ? " +
		"AND AGC_OID = ? " +
		"ORDER BY GCT_OID ";

public static String ANALYSIS_GENELIST_TITLE = "SELECT GNL_SHORT_NAME FROM REF_GENELISTS WHERE GNL_OID = ? ";

public static String ANALYSIS_GENELIST_CLUSTER_TITLE = "SELECT AGC_TITLE FROM MIC_ANAL_GENELIST_COLUMN WHERE AGC_OID = ? ";

public static String MIC_ANALYSIS_ANNOTATION = "SELECT MAN_PLATFORM_ID, MAN_GENE_SYMBOL, "+
		" MAN_PROBE_SEQ_ID, MAN_MGI_ID,  MAN_ENTREZ_GENE_ID, " +
		" MAN_HUMAN_ORTH_SYMBOL, MAN_HUMAN_ORTH_ENTREZ_ID,MAN_PROBE_SET_ID "+
		"FROM MIC_ANAL_ANNOTATION " +
		"WHERE MAN_PROBE_SET_ID " +
		"ORDER BY FIELD(MAN_PROBE_SET_ID, PROBE_SET_ID_ARG) ";


public static String MIC_GENE_AVERAGE = "SELECT MGA_DATASET_ID,MGA_AVERAGE_EXP FROM MIC_GENE_AVERAGE WHERE MGA_GENE_SYMBOL = ?";

public static String GENE_IDS = "SELECT GNF_ID FROM REF_GENE_INFO";

public static String GET_ANALYSIS_GENELIST = "SELECT DISTINCT MAN_HUMAN_ORTH_SYMBOL FROM MIC_ANAL_ANNOTATION, GNL_ENTITIES " +
			"WHERE MAN_PROBE_SET_ID = GLE_ENTITY_ID AND GLE_LIST_ID = ? AND MAN_HUMAN_ORTH_SYMBOL != ''";

}
