package org.gudmap.queries.anatomy;

import java.util.ResourceBundle;

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
		
}
