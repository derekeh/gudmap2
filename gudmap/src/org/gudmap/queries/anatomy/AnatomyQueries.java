package org.gudmap.queries.anatomy;

import java.util.ResourceBundle;

import org.gudmap.queries.generic.GenericQueries;

public class AnatomyQueries {
	
	public static ResourceBundle bundle = ResourceBundle.getBundle("org.gudmap.bundles.config");
	
	 //@FROM EXPRESSION DETAIL
	  public static String PARENT_NODES ="SELECT PARENTS.ATN_PUBLIC_ID " +
	  		                       "FROM ANA_TIMED_NODE KID, ANA_TIMED_NODE PARENTS, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE " +
	  		                       "WHERE KID.ATN_PUBLIC_ID = ? " + // 1
	  		                       "AND PARENTS.ATN_STAGE_FK = STG_OID " +
	  		                       "AND KID.ATN_STAGE_FK = STG_OID " +
	  		                       "AND STG_NAME = ? " +            // 2
	  		                       "AND KID.ATN_NODE_FK = RTR_DESCENDENT_FK " +
	  		                       "AND PARENTS.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	  		                       "AND KID.ATN_NODE_FK != RTR_ANCESTOR_FK " +
	  		                       "AND PARENTS.ATN_PUBLIC_ID IN " +
	  		                       "(SELECT EXP_COMPONENT_ID " +
	  		                       " FROM ISH_EXPRESSION, ISH_SUBMISSION " +
	  		                       " WHERE SUB_OID = ? " + // 3
	  		                       " AND EXP_SUBMISSION_FK = SUB_OID " +
	  		                       " AND EXP_STRENGTH = 'not detected')";
	  
	  // (for a specified submission, find kid nodes for a specified node in the specified stage whose strength are 'present')
	  public static String CHILD_NODES = "SELECT KIDS.ATN_PUBLIC_ID " +
	  		                        "FROM ANA_TIMED_NODE KIDS, ANA_TIMED_NODE PARENT, ANAD_RELATIONSHIP_TRANSITIVE, ANA_STAGE " +
	  		                        "WHERE PARENT.ATN_PUBLIC_ID = ? " + // 1
	  		                        "AND PARENT.ATN_STAGE_FK = STG_OID " +
	  		                        "AND KIDS.ATN_STAGE_FK = STG_OID " +
	  		                        "AND STG_NAME = ? " +               // 2
	  		                        "AND KIDS.ATN_NODE_FK = RTR_DESCENDENT_FK " +
	  		                        "AND PARENT.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	  		                        "AND PARENT.ATN_NODE_FK != RTR_DESCENDENT_FK " +
	  		                        "AND KIDS.ATN_PUBLIC_ID IN " +
	  		                        "(SELECT EXP_COMPONENT_ID " +
	   		                       " FROM ISH_EXPRESSION, ISH_SUBMISSION " +
	  		                       " WHERE SUB_OID= ? " +     // 3
	  		                       " AND EXP_SUBMISSION_FK = SUB_OID " +
	  		                        " AND EXP_STRENGTH = 'present')";
	  
	  public static String COMPONENT_EXPRESSION_NOTE = "SELECT ENT_VALUE " +
                "FROM ISH_EXPRESSION, ISH_EXPRESSION_NOTE, ISH_SUBMISSION " +
                "WHERE ENT_EXPRESSION_FK = EXP_OID " +
                "AND EXP_SUBMISSION_FK = SUB_OID " +
                "AND SUB_OID = ? " +
              "AND EXP_COMPONENT_ID = ? ";

	  public static String COMPONENT_DENSITY_NOTE = "SELECT DNN_VALUE " +
                "FROM ISH_EXPRESSION, ISH_SUBMISSION, ISH_DENSITY_NOTE, ISH_DENSITY " +
                "WHERE DNN_DENSITY_FK = DEN_OID " +
                "AND DEN_EXPRESSION_FK = EXP_OID " +
                "AND EXP_SUBMISSION_FK = SUB_OID " +
                "AND SUB_OID = ? " +
              "AND EXP_COMPONENT_ID = ? ";

	  public static String COMPONENT_DENSITY_DETAIL = "SELECT DEN_RELATIVE_TO_TOTAL, DEN_DIRECTION_CHANGE, DEN_MAGNITUDE_CHANGE " +
                "FROM ISH_EXPRESSION, ISH_SUBMISSION, ISH_DENSITY " +
                "WHERE DEN_EXPRESSION_FK = EXP_OID " +
                "AND EXP_SUBMISSION_FK = SUB_OID " +
                "AND SUB_OID = ? " +
              "AND EXP_COMPONENT_ID = ? ";
	  
////////////////////////////////////////////////////////NEW TREEE///////////////////////////////////////////////
	    
		public static String ANNOT_TREE_EXPRESSIONS = "SELECT EXP_COMPONENT_ID, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH FROM ISH_EXPRESSION WHERE EXP_SUBMISSION_FK = ?";
		
		public static String ANNOT_TREE_PATTERNS = "SELECT EXP_COMPONENT_ID,PTN_PATTERN FROM ISH_EXPRESSION, ISH_PATTERN WHERE EXP_OID = PTN_EXPRESSION_FK AND EXP_SUBMISSION_FK = ?";
		
		public static String ANNOT_TREE_EXPRESSION_NOTES = "SELECT EXP_COMPONENT_ID,ENT_VALUE FROM ISH_EXPRESSION,ISH_EXPRESSION_NOTE WHERE EXP_OID = ENT_EXPRESSION_FK AND EXP_SUBMISSION_FK = ?";

		//IS ANNOTATED?
		
		public static String TREE_ANNOTATIONS = "SELECT COUNT(*) FROM ISH_EXPRESSION, ISH_SUBMISSION WHERE EXP_SUBMISSION_FK = SUB_OID AND SUB_OID = ?";
		
		//TREE CONTENT (TODO CREATE BUNDLE (PERSPECTIVE)
		
/*		public static String TREE_CONTENT = "SELECT APO_DEPTH,APO_SEQUENCE, PATN.ATN_PUBLIC_ID, PARENT.ANO_COMPONENT_NAME, CONCAT('(',strt.STG_NAME,'-',end.sTG_NAME,')') AS 'RANGE', " +
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
		   "ORDER BY APO_SEQUENCE";*/
		
		public static String EXPRESSION_PATTERNS = "SELECT DISTINCT PTN_OID, PTN_PATTERN FROM ISH_PATTERN WHERE PTN_EXPRESSION_FK = ?";
	    
	    public static String PATTERN_LOCATIONS = "SELECT DISTINCT LCN_LOCATION, LCN_PATTERN_FK, LCN_OID FROM ISH_LOCATION WHERE LCN_PATTERN_FK = ?";
	    
	    public static String COMPONENT_NAME_FROM_ATN_PUBLIC_ID = "SELECT ANO_COMPONENT_NAME FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ATN_PUBLIC_ID = ?";
	    
	    public static String COMPONENT_DETAIL_FROM_ATN_PUBLIC_ID = "SELECT ATN_PUBLIC_ID, ANO_COMPONENT_NAME, APO_FULL_PATH " +
	    		                        "FROM ANA_NODE, ANAD_PART_OF, ANA_TIMED_NODE " +
	    		                        "WHERE ATN_PUBLIC_ID = ? " + // 1
	    		                        "AND ANO_OID = ATN_NODE_FK " +
	    		                        "AND APO_NODE_FK = ANO_OID " +
	    		                        "AND APO_IS_PRIMARY_PATH = 1";
	    //////////////////////////////////////////////////////EXPRESSION FOR SINGLE COMPONENT ID/////////////////////////////////////////////////////////
		
		public static  String COMPONENT_EXPRESSION_DETAIL = "SELECT EXP_COMPONENT_ID, ANO_COMPONENT_NAME, APO_FULL_PATH, EXP_STRENGTH, EXP_ADDITIONAL_STRENGTH, EXP_OID, SUB_EMBRYO_STG, SUB_OID, SUB_DB_STATUS_FK " +
		    "FROM ISH_SUBMISSION, ISH_EXPRESSION, ANA_NODE, ANAD_PART_OF, ANA_TIMED_NODE " +
		    "WHERE EXP_SUBMISSION_FK = SUB_OID " +
		    "AND SUB_OID = ? " + // 1
		    "AND EXP_COMPONENT_ID = ? " + // 2
		    "AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
		    "AND ANO_OID = ATN_NODE_FK " +
		    "AND APO_NODE_FK = ANO_OID " +
		    "AND APO_IS_PRIMARY_PATH = 1";
		
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
		
		public static String COMPONENT_IDS_BY_NAME = "SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ANO_OID = ATN_NODE_FK AND ANO_COMPONENT_NAME = ?" +
    			" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE, ANA_SYNONYM WHERE SYN_OBJECT_FK = ANO_OID AND ANO_OID = ATN_NODE_FK AND SYN_SYNONYM = ?" +
    			"  UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ATN_PUBLIC_ID = ?" +
    			" UNION SELECT DISTINCT ATN_PUBLIC_ID FROM ANA_NODE, ANA_TIMED_NODE WHERE ATN_NODE_FK = ANO_OID AND ANO_PUBLIC_ID = ?";
		
		public static String ISHSelectForAnatomy =  "(select distinct QIC_RPR_SYMBOL col1, "+
				"QIC_ANO_COMPONENT_NAME col2, "+
		        "QIC_EXP_STRENGTH col3,"+
				"QIC_SUB_SOURCE col4,"+
				"QIC_SUB_SUB_DATE col5,"+
				"QIC_SUB_EMBRYO_STG col6,"+
				"QIC_SPN_ASSAY_TYPE col7,"+
				"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
				"QIC_SUB_THUMBNAIL col9,"+
				"QIC_SUB_ACCESSION_ID col10,"+
				"'' col11,'' col12, REPLACE(QIC_SUB_ACCESSION_ID, ':" + "', '" + "no" + "') col13, QIC_SUB_ASSAY_TYPE col14, QIC_SPN_SEX col15," +
				"QIC_PRB_PROBE_NAME col16,QIC_SPN_WILDTYPE col17 " +
				"FROM QSC_ISH_CACHE ";
		
		public static String getComponentRelations(int numberOfComponents, String col1, String col2) {
		    	
		    	StringBuffer componentRealtionsQ = new StringBuffer("SELECT DISTINCT "+col1 + 
		    			" FROM ANA_TIMED_NODE DESCEND_ATN " +
		    			"JOIN ANAD_RELATIONSHIP_TRANSITIVE " +
		    			"ON DESCEND_ATN.ATN_NODE_FK = RTR_DESCENDENT_FK " +
		    			"JOIN ANA_TIMED_NODE ANCES_ATN " +
		    			"ON ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
		    			"WHERE ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
		    			"AND "+col2+" IN (");
		    	
		    	for(int i=0;i<numberOfComponents;i++){
		        	if(i == numberOfComponents-1){
		        		componentRealtionsQ.append("?)");
		        	}
		        	else {
		        		componentRealtionsQ.append("?, ");
		        	}
		        }
		    	
		    	return componentRealtionsQ.toString();
		 }
		
		public static String BROWSE_ANATOMY_PARAM = "SELECT DISTINCT x.oid, x.gene, x.gudmap_accession, x.source, x.submission_date, x.assay_type, x.probe_name, x.stage, x.age, x.sex, " +
				"x.genotype, x.tissue, x.expression, x.specimen, x.image " +
			"FROM " + 
			"((select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
			"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_SUB_EMBRYO_STG stage, " +
			"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) age, QIC_SPN_SEX sex, " +
			"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image " +
			"FROM QSC_ISH_CACHE LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
			"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
			"%s " +
			"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='present' ) " + 
			"UNION " +
			"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
			"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_SUB_EMBRYO_STG stage, " +
			"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) age, QIC_SPN_SEX sex, " +
			"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image " + 
			"FROM QSC_ISH_CACHE LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
			"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " + 
			"%s " +
			"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='not detected') " +
			"UNION " + 
			"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
			"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_SUB_EMBRYO_STG stage, " +
			"TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) age, QIC_SPN_SEX sex, " +
			"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image " + 
			"FROM QSC_ISH_CACHE LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
			"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +  
			"%s  " +
			"(QIC_ATN_PUBLIC_ID in (%s))  AND QIC_EXP_STRENGTH not in('present', 'not detected') ) " + 
			"UNION " +
			"(select distinct SUBSTRING(QMC_SUB_ACCESSION_ID,8) oid, '' gene, QMC_SUB_ACCESSION_ID gudmap_accession, QMC_SUB_SOURCE source, " +
			"DATE_FORMAT(QMC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Microarray' assay_type, '' probe_name, QMC_SUB_EMBRYO_STG stage, " +
			"TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE, ' ', QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT, QMC_SPN_STAGE) END) age, QMC_SPN_SEX sex, " +
			"QMC_SPN_WILDTYPE genotype, QMC_ANO_COMPONENT_NAME tissue, '' expression, QMC_SPN_ASSAY_TYPE specimen, '' image " +
			"FROM " +
			"QSC_MIC_CACHE " +
			"%s  " +
			"(QMC_ATN_PUBLIC_ID in (%s,%s,%s)))  " +
			"ORDER BY gene  ASC , assay_type, FIELD(expression, 'present', 'uncertain', 'not detected', ''), stage, tissue  ) AS x " +
			"GROUP BY x.gudmap_accession  ORDER BY %s %s, x.assay_type, x.gene, FIELD(x.expression, 'present', 'uncertain', 'not detected', ''), x.stage, x.tissue, x.sex limit ?,?";
		
		public static String BROWSE_ANATOMY_HEADER_PARAM="SELECT DISTINCT x.oid, x.gene, x.gudmap_accession, x.source, x.submission_date, x.assay_type, x.probe_name, x.stage, x.species, x.age, x.sex, " +
				"x.genotype, x.tissue, x.expression, x.specimen, x.image, x.gene_id FROM (";
		
		
		/*public static String BROWSE_ANATOMY_ISH_PARAM = "(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, IF(INSTR(QIC_STG_ALT_STAGE,'(')>0, SUBSTRING(QIC_STG_ALT_STAGE,1,INSTR(QIC_STG_ALT_STAGE,'(')-1), QIC_STG_ALT_STAGE ) age," +
				"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id " +
				"FROM QSC_ISH_CACHE JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
				"%s " +
				"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='present' ) " + 
				"UNION " +
				"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, IF(INSTR(QIC_STG_ALT_STAGE,'(')>0, SUBSTRING(QIC_STG_ALT_STAGE,1,INSTR(QIC_STG_ALT_STAGE,'(')-1), QIC_STG_ALT_STAGE ) age,"
				+"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id  " + 
				"FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " + 
				"%s " +
				"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='not detected') " +
				"UNION " + 
				"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, IF(INSTR(QIC_STG_ALT_STAGE,'(')>0, SUBSTRING(QIC_STG_ALT_STAGE,1,INSTR(QIC_STG_ALT_STAGE,'(')-1), QIC_STG_ALT_STAGE ) age," +
				"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id  " + 
				"FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +  
				"%s  " +
				"(QIC_ATN_PUBLIC_ID in (%s))  AND QIC_EXP_STRENGTH not in('present', 'not detected') ) ";*/
		
		public static String BROWSE_ANATOMY_ISH_PARAM = "(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE,' ',QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT,QIC_SPN_STAGE) END) age," +
				"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id " +
				"FROM QSC_ISH_CACHE JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " +
				"%s " +
				"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='present' ) " + 
				"UNION " +
				"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE,' ',QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT,QIC_SPN_STAGE) END) age,"
				+"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id  " + 
				"FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  " + 
				"%s " +
				"(QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='not detected') " +
				"UNION " + 
				"(select distinct SUBSTRING(QIC_SUB_ACCESSION_ID,8) oid, QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
				"DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_PRB_PROBE_NAME probe_name,QIC_STG_STAGE_DISPLAY stage, " +
				"QIC_STG_SPECIES species, TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE,' ',QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT,QIC_SPN_STAGE) END) age," +
				"QIC_SPN_SEX sex, " +
				"QIC_SPN_WILDTYPE genotype, ANO_COMPONENT_NAME tissue, QIC_EXP_STRENGTH expression, QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image, QIC_RPR_LOCUS_TAG gene_id  " + 
				"FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT " +
				"LEFT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID " +  
				"%s  " +
				"(QIC_ATN_PUBLIC_ID in (%s))  AND QIC_EXP_STRENGTH not in('present', 'not detected') ) ";
		
		
		/*public static String BROWSE_ANATOMY_MIC_PARAM = //"UNION " +
				"(select distinct SUBSTRING(QMC_SUB_ACCESSION_ID,8) oid, '' gene, QMC_SUB_ACCESSION_ID gudmap_accession, QMC_SUB_SOURCE source, " +
				"DATE_FORMAT(QMC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Microarray' assay_type, '' probe_name, QMC_STG_STAGE_DISPLAY stage, " +
				"QMC_STG_SPECIES species, IF(INSTR(QMC_STG_ALT_STAGE,'(')>0, SUBSTRING(QMC_STG_ALT_STAGE,1,INSTR(QMC_STG_ALT_STAGE,'(')-1), QMC_STG_ALT_STAGE ) age," +
				"QMC_SPN_SEX sex, " +
				"QMC_SPN_WILDTYPE genotype, QMC_ANO_COMPONENT_NAME tissue, '' expression, QMC_SPN_ASSAY_TYPE specimen, '' image, '' gene_id " +
				"FROM " +
				"QSC_MIC_CACHE  JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK  " +
				"%s  " +
				"(QMC_ATN_PUBLIC_ID in (%s,%s,%s))) ";*/
		
		public static String BROWSE_ANATOMY_SEQ_PARAM="(SELECT DISTINCT SUB_OID oid, '' gene, SUB_ACCESSION_ID gudmap_accession, SUB_SOURCE source,  " +
				"DATE_FORMAT(SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Sequence' assay_type, '' probe_name,  STG_STAGE_DISPLAY stage, " +
				"STG_SPECIES species, TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END) age, " +
				"NGS_SEX sex, " +
				"CASE NGS_GENOTYPE WHEN 'true' THEN (CASE NGS_SPECIES WHEN 'Homo sapiens' THEN '' ELSE 'wild type' END) ELSE  CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID)  IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID)  ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype,  " +
				"GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME) SEPARATOR ', ') tissue, '' expression, '' specimen, '' image, '' gene_id " +
				"FROM " +
				"ISH_SUBMISSION JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK=SUB_OID  JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK  JOIN " +
				"ANA_TIMED_NODE ON ATN_PUBLIC_ID=IST_COMPONENT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID  " +
				"LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
				"%s " + 
				"(IST_COMPONENT IN (%s,%s,%s)) AND " + GenericQueries.PUBLIC_ENTRIES + "  GROUP BY SUB_ACCESSION_ID) ";
		
		public static String BROWSE_ANATOMY_MIC_PARAM = 
				"(select distinct SUBSTRING(QMC_SUB_ACCESSION_ID,8) oid, '' gene, QMC_SUB_ACCESSION_ID gudmap_accession, QMC_SUB_SOURCE source, " +
				"DATE_FORMAT(QMC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Microarray' assay_type, '' probe_name, QMC_STG_STAGE_DISPLAY stage, " +
				"QMC_STG_SPECIES species, TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE,' ',QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT,QMC_SPN_STAGE) END) age," +
				"QMC_SPN_SEX sex, " +
				"QMC_SPN_WILDTYPE genotype, QMC_ANO_COMPONENT_NAME tissue, '' expression, QMC_SPN_ASSAY_TYPE specimen, '' image, '' gene_id " +
				"FROM " +
				"QSC_MIC_CACHE  JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK  " +
				"%s  " +
				"(QMC_ATN_PUBLIC_ID in (%s,%s,%s))) ";
		
		/*public static String BROWSE_ANATOMY_MIC_PARAM  = //"UNION " +
				"(select distinct SUBSTRING(QMC_SUB_ACCESSION_ID,8) oid, '' gene, QMC_SUB_ACCESSION_ID gudmap_accession, QMC_SUB_SOURCE source, " +
				"DATE_FORMAT(QMC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Microarray' assay_type, '' probe_name, QMC_STG_STAGE_DISPLAY stage, " +
				"QMC_STG_SPECIES species, TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE,' ',QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT,QMC_SPN_STAGE) END) age," +
				"QMC_SPN_SEX sex, " +
				"QMC_SPN_WILDTYPE genotype, QMC_ANO_COMPONENT_NAME tissue, '' expression, QMC_SPN_ASSAY_TYPE specimen, '' image, '' gene_id " +
				"FROM " +
				"QSC_MIC_CACHE  JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK  " +
				"%s  " +
				"(QMC_ATN_PUBLIC_ID in (%s,%s,%s))) UNION " + BROWSE_ANATOMY_SEQ_PARAM;*/
		
		public static String BROWSE_ANATOMY_MIC_PARAM_2  = //"UNION MICROARRAY AND NEXTGEN " +
				"(select distinct SUBSTRING(QMC_SUB_ACCESSION_ID,8) oid, '' gene, QMC_SUB_ACCESSION_ID gudmap_accession, QMC_SUB_SOURCE source, " +
				"DATE_FORMAT(QMC_SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Microarray' assay_type, '' probe_name, QMC_STG_STAGE_DISPLAY stage, " +
				"QMC_STG_SPECIES species, TRIM(CASE QMC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QMC_SPN_STAGE,' ',QMC_SPN_STAGE_FORMAT) ELSE CONCAT(QMC_SPN_STAGE_FORMAT,QMC_SPN_STAGE) END) age," +
				"QMC_SPN_SEX sex, " +
				"QMC_SPN_WILDTYPE genotype, QMC_ANO_COMPONENT_NAME tissue, '' expression, QMC_SPN_ASSAY_TYPE specimen, '' image, '' gene_id " +
				"FROM " +
				"QSC_MIC_CACHE  JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK  " +
				"%s  " +
				"(QMC_ATN_PUBLIC_ID in (%s,%s,%s))) " +
				"UNION " + 
				"(SELECT DISTINCT SUB_OID oid, '' gene, SUB_ACCESSION_ID gudmap_accession, SUB_SOURCE source,  " +
				"DATE_FORMAT(SUB_SUB_DATE,'%%e %%b %%Y') submission_date, 'Sequence' assay_type, '' probe_name,  STG_STAGE_DISPLAY stage, " +
				"STG_SPECIES species, TRIM(CASE NGS_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(NGS_DEV_STAGE,' ',NGS_STAGE_FORMAT) ELSE CONCAT(NGS_STAGE_FORMAT,NGS_DEV_STAGE) END) age, " +
				"NGS_SEX sex, " +
				"CASE NGS_GENOTYPE WHEN 'true' THEN (CASE NGS_SPECIES WHEN 'Homo sapiens' THEN '' ELSE 'wild type' END) ELSE  CASE WHEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID)  IS NOT NULL THEN (SELECT DISTINCT GROUP_CONCAT(ALE_ALLELE_NAME) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID)  ELSE (SELECT DISTINCT GROUP_CONCAT(ALE_LAB_NAME_ALLELE) FROM " +
				"ISH_ALLELE, LNK_SUB_ALLELE  WHERE SAL_ALE_OID_FK=ALE_OID AND SAL_SUBMISSION_FK=SUB_OID) END  END AS genotype,  " +
				"GROUP_CONCAT(DISTINCT CONCAT(ANO_COMPONENT_NAME) SEPARATOR ', ') tissue, '' expression, '' specimen, '' image, '' gene_id " +
				"FROM " +
				"ISH_SUBMISSION JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK=SUB_OID  JOIN REF_STAGE ON STG_OID = SUB_STAGE_FK  JOIN " +
				"ANA_TIMED_NODE ON ATN_PUBLIC_ID=IST_COMPONENT JOIN ANA_NODE ON ATN_NODE_FK = ANO_OID  LEFT JOIN LNK_SUB_ALLELE ON SAL_SUBMISSION_FK = SUB_OID  " +
				"LEFT JOIN ISH_ALLELE ON SAL_ALE_OID_FK = ALE_OID " +
				"%s " + 
				"(IST_COMPONENT IN (%s,%s,%s)) AND " +GenericQueries.PUBLIC_ENTRIES+"  GROUP BY SUB_ACCESSION_ID) ";
		
		
		
		public static String BROWSE_ANATOMY_FOOTER_PARAM = "ORDER BY gene  ASC , assay_type, FIELD(expression, 'present', 'uncertain', 'not detected', ''), stage, tissue  ) AS x " +
			"GROUP BY x.gudmap_accession  ORDER BY %s %s, x.assay_type, x.gene, FIELD(x.expression, 'present', 'uncertain', 'not detected', ''), x.stage, x.tissue, x.sex limit ?,?";
		
		/*public static String BROWSE_ANATOMY_FOOTER_PARAM = " ) AS x " +
				"GROUP BY x.gudmap_accession  ORDER BY %s %s, x.assay_type, x.gene, FIELD(x.expression, 'present', 'uncertain', 'not detected', ''), x.stage, x.tissue, x.sex limit ?,?";
		*/
		
		public static String TOTAL_ISH_ANATOMY="select count(distinct gudmap_accession) FROM " +
				"((SELECT DISTINCT QIC_SUB_ACCESSION_ID gudmap_accession FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON " +
				"IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT LEFT JOIN " +
				"ANA_NODE ON ATN_NODE_FK = ANO_OID  %s  (QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='present' ) " +
				"UNION " +
				"(SELECT DISTINCT QIC_SUB_ACCESSION_ID gudmap_accession FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON " +
				"IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT LEFT JOIN " +
				"ANA_NODE ON ATN_NODE_FK = ANO_OID  %s  (QIC_ATN_PUBLIC_ID in (%s,%s))  AND QIC_EXP_STRENGTH='not detected' ) " +
				"UNION " +
				"(SELECT DISTINCT QIC_SUB_ACCESSION_ID gudmap_accession FROM QSC_ISH_CACHE  JOIN REF_STAGE ON STG_OID = QIC_SUB_STAGE_FK LEFT JOIN ISH_SP_TISSUE ON " +
				"IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) LEFT JOIN ANA_TIMED_NODE ON ATN_PUBLIC_ID = IST_COMPONENT LEFT JOIN " +
				"ANA_NODE ON ATN_NODE_FK = ANO_OID %s  (QIC_ATN_PUBLIC_ID in (%s))  AND QIC_EXP_STRENGTH NOT IN ('present', 'not detected') )) AS TABLEA";
		
		public static String TOTAL_MICROARRAY_ANATOMY="select count(distinct QMC_SUB_ACCESSION_ID) total_microarray FROM QSC_MIC_CACHE JOIN REF_STAGE ON STG_OID = QMC_SUB_STAGE_FK   %s  " +
				"(QMC_ATN_PUBLIC_ID in (%s,%s,%s))";
		
		public static String TOTAL_SEQUENCE_ANATOMY="SELECT 0 TOTAL_SEQUENCE";
		public static String TOTAL_SEQUENCE_ANATOMY_2="SELECT COUNT(DISTINCT SUB_OID) TOTAL_SEQUENCE FROM ISH_SUBMISSION JOIN ISH_SP_TISSUE ON IST_SUBMISSION_FK = SUB_OID " +
				"JOIN REF_STAGE ON SUB_STAGE_FK = STG_OID JOIN NGD_SAMPLE ON NGS_SUBMISSION_FK = SUB_OID " +
				"%s  SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND " +
				"SUB_DB_STATUS_FK = 4 AND (IST_COMPONENT IN(%s,%s,%s)) ";
		
	    public static String ANNOTATED_STAGE_RANGE = "SELECT DISPLAY.STG_NAME " +
		                                "FROM ANA_STAGE DISPLAY " +
		                                "WHERE DISPLAY.STG_SEQUENCE " +
		                                "BETWEEN " +
		                                " (SELECT MIN(RANGE1.STG_SEQUENCE) " +
		                                "  FROM ANA_STAGE RANGE1, ANA_TIMED_NODE, ISH_EXPRESSION, ISH_SUBMISSION " +
		                                "  WHERE RANGE1.STG_OID = ATN_STAGE_FK " +
		                                "  AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
		                                "  AND EXP_SUBMISSION_FK = SUB_OID " +
		                                "  AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
		                                "  AND SUB_ASSAY_TYPE = 'ISH') " +
		                                "AND " +
		                                "  (SELECT MAX(RANGE2.STG_SEQUENCE) " +
		                                "   FROM ANA_STAGE RANGE2, ANA_TIMED_NODE, ISH_EXPRESSION, ISH_SUBMISSION " +
		                                "   WHERE RANGE2.STG_OID = ATN_STAGE_FK " +
		                                "   AND ATN_PUBLIC_ID = EXP_COMPONENT_ID " +
		                                "   AND EXP_SUBMISSION_FK = SUB_OID " +
		                                "   AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
		                                "   AND SUB_ASSAY_TYPE = 'ISH') " +
		                                "ORDER BY STG_SEQUENCE ";
	    
	    public static String ANATOMY_STAGES_IN_ENTRIES = "SELECT DISTINCT STG_NAME FROM ANA_STAGE,ANA_TIMED_NODE,ISH_EXPRESSION,ISH_SUBMISSION WHERE " +
	    		"STG_OID=ATN_STAGE_FK AND ATN_PUBLIC_ID = EXP_COMPONENT_ID AND EXP_SUBMISSION_FK = SUB_OID AND SUB_IS_PUBLIC = 1 AND SUB_IS_DELETED = 0 AND SUB_DB_STATUS_FK = 4 " +
	    		"AND SUB_ASSAY_TYPE = 'ISH' ORDER BY STG_SEQUENCE";
	    
	    public static String STAGE_SEQUENCE = "SELECT STG_SEQUENCE FROM ANA_STAGE WHERE STG_NAME = ?";
	    
	    public static String QUERY_TREE_CONTENT = "SELECT DISTINCT APO_DEPTH, APO_SEQUENCE, PARENT.ANO_OID,PARENT.ANO_COMPONENT_NAME, CONCAT(as1.STG_NAME,'-',as2.STG_NAME) as rnge, "+
	                                   "(select count(*) "+
	                                    "from ANA_RELATIONSHIP, ANA_NODE CHILD, ANA_TIMED_NODE CATN, ANA_STAGE CSTG "+
	                                    "where REL_PARENT_FK = PARENT.ANO_OID "+
	                                    "and REL_CHILD_FK  = CHILD.ANO_OID "+
	                                    "and CATN.ATN_NODE_FK = CHILD.ANO_OID "+
	                                    "and CSTG.STG_OID = CATN.ATN_STAGE_FK "+
	                                    "and CSTG.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE) as kids,"+
	                                    "case when !APO_IS_PRIMARY OR ANO_IS_GROUP THEN 1 ELSE 0 END AS IP, "+
	                                    "'','', '', 0 "+
	                                    "'', '', '', '', "+  
	                                    "'', '' " +
	                                "FROM ANA_NODE PARENT "+
	                                 "JOIN ANAD_PART_OF "+
	                                   "ON ANO_OID = APO_NODE_FK AND APO_FULL_PATH NOT LIKE '%mouse.embryo%' "+
	                                 "JOIN ANAD_PART_OF_PERSPECTIVE " +
	                                 "ON POP_PERSPECTIVE_FK = 'Combined Perspective (GUDMAP)' "+
	                                   " AND POP_APO_FK = APO_OID " +
	                                 "JOIN ANA_STAGE qs1 "+
	                                   "ON qs1.STG_NAME = ? "+
	                                 "JOIN ANA_STAGE qs2 "+
	                                   "ON qs2.STG_NAME = ? "+
	                                 "JOIN ANA_STAGE as1 "+
	                                   "ON as1.STG_OID = APO_PATH_START_STAGE_FK  "+
	                                  "JOIN ANA_STAGE as2 "+
	                                    "ON as2.STG_OID = APO_PATH_END_STAGE_FK "+
	                                  "WHERE as1.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE   AND qs2.STG_SEQUENCE "+
	                                  "OR as2.STG_SEQUENCE BETWEEN qs1.STG_SEQUENCE AND qs2.STG_SEQUENCE  "+
	                                  "OR (as1.STG_SEQUENCE < qs1.STG_SEQUENCE AND as2.STG_SEQUENCE > qs2.STG_SEQUENCE ) "+
	                                  "ORDER BY APO_SEQUENCE";
		
}
