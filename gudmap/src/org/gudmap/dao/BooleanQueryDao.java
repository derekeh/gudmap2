package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import org.gudmap.globals.Globals;
import org.gudmap.utils.Utils;

public class BooleanQueryDao {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	private Connection conn; 
	private int ColumnNumbers = 18;//17;
	
	String outputString;

    private String[] ErrorArray={"ERROR 1:", 
                                "ERROR 2: "};
    private String[][] expression_array={{"present","p"},
                                              {"not detected","nd"},
                                              {"uncertain","u"}};

    private String SELECT_STR="SELECT ";
    private String DISTINCT_STR="DISTINCT ";
    private String FROM_STR="FROM QSC_ISH_CACHE LEFT JOIN QSC_ISH_LOCATION ON QIL_SUB_ACCESSION_ID = QIC_SUB_ACCESSION_ID  ";
    private String WHERE_STR="WHERE ";
    private String AND_STR="AND ";
    private String OR_STR="OR ";
    private String ORDER_STR="ORDER BY ";
    private String UNION_STR=") UNION ( ";
    private String SUB_UNION_STR="UNION ( ";
    private String INTERSECT_STR_ENTRY="AND QIC_SUB_ACCESSION_ID IN ( ";
    private String ACCESSION_STR="QIC_SUB_ACCESSION_ID ";
   /* private String COLUMN_STR="QIC_RPR_SYMBOL col1,"+
            "(SELECT GROUP_CONCAT(DISTINCT a.ANO_COMPONENT_NAME SEPARATOR '; ')  FROM ISH_SP_TISSUE, ANA_TIMED_NODE t, ANA_NODE a WHERE IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) AND t.ATN_PUBLIC_ID = IST_COMPONENT AND t.ATN_NODE_FK = a.ANO_OID) as col2, "+
            "'' col3,"+
            "QIC_SUB_SOURCE col4,"+
            "QIC_SUB_SUB_DATE col5,"+
            "QIC_STG_STAGE_DISPLAY col6,"+
            "QIC_SPN_ASSAY_TYPE col7,"+
            "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE, ' ', QIC_SPN_STAGE_FORMAT) WHEN 'P' THEN CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) ELSE CONCAT(QIC_SPN_STAGE_FORMAT, QIC_SPN_STAGE) END) col8,"+
            "QIC_SUB_THUMBNAIL col9,"+
            "QIC_SUB_ACCESSION_ID col10,"+
            "'' col11,"+
            "(SELECT GROUP_CONCAT(DISTINCT B.QIC_EXP_STRENGTH) FROM QSC_ISH_CACHE B WHERE B.QIC_SUB_ACCESSION_ID=QIC_SUB_ACCESSION_ID) col12,"+
            "REPLACE(QIC_SUB_ACCESSION_ID, ':', 'no') col13,"+
            "QIC_SUB_ASSAY_TYPE col14, QIC_SPN_SEX col15, QIC_PRB_PROBE_NAME col16, QIC_SPN_WILDTYPE col17, QIC_RPR_LOCUS_TAG col18 ";*/
    
    private String COLUMN_STR="QIC_RPR_SYMBOL gene,"+
            "(SELECT GROUP_CONCAT(DISTINCT a.ANO_COMPONENT_NAME SEPARATOR '; ')  FROM ISH_SP_TISSUE, ANA_TIMED_NODE t, ANA_NODE a WHERE IST_SUBMISSION_FK = CAST(SUBSTR(QIC_SUB_ACCESSION_ID FROM 8) AS UNSIGNED) AND t.ATN_PUBLIC_ID = IST_COMPONENT AND t.ATN_NODE_FK = a.ANO_OID) as tissue, "+
            "QIC_STG_SPECIES species,"+
            "QIC_SUB_SOURCE source,"+
            "DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%M %%Y') submission_date,"+
            "QIC_STG_STAGE_DISPLAY stage,"+
            "QIC_SPN_ASSAY_TYPE specimen,"+
            "QIC_STG_ALT_STAGE age,"+
            "QIC_SUB_THUMBNAIL image,"+
            "QIC_SUB_ACCESSION_ID gudmap_accession,"+
            "'' col11,"+
            "(SELECT GROUP_CONCAT(DISTINCT B.QIC_EXP_STRENGTH) FROM QSC_ISH_CACHE B WHERE B.QIC_SUB_ACCESSION_ID=QIC_SUB_ACCESSION_ID) expression,"+
            "CAST(SUBSTRING(QIC_SUB_ACCESSION_ID,8) AS UNSIGNED) oid,"+
            "QIC_SUB_ASSAY_TYPE assay_type, QIC_SPN_SEX sex, QIC_PRB_PROBE_NAME probe_name, QIC_SPN_WILDTYPE genotype, QIC_RPR_LOCUS_TAG gene_id ";

    // clause to find all parent of specified anatomy term(s)
    private String INHERITANCE_CRITERIA_STR_ANSCESTOR = " AND QIC_ATN_PUBLIC_ID IN ( " +
    		"SELECT DISTINCT ANCES_ATN.ATN_PUBLIC_ID " +
    		"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
    		"ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
    		"WHERE ANO_COMPONENT_NAME " +
    		"AND ANO_OID = DESCEND_ATN.ATN_NODE_FK " +
    		"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
    		"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
    		"AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
    		"AND APO_NODE_FK = ANO_OID " +
    		"AND APO_IS_PRIMARY = 1) ";

    // clause to find children node of given anatomy term
    private String INHERITANCE_CRITERIA_STR_DESCENDANT = " AND QIC_ATN_PUBLIC_ID IN ( " +
	"SELECT DESCEND_ATN.ATN_PUBLIC_ID " +
	"FROM ANA_TIMED_NODE ANCES_ATN, ANAD_RELATIONSHIP_TRANSITIVE, " +
	"ANA_TIMED_NODE DESCEND_ATN, ANA_NODE, ANAD_PART_OF " +
	"WHERE ANO_COMPONENT_NAME " +
	"AND ANO_OID = ANCES_ATN.ATN_NODE_FK " +
	"AND ANCES_ATN.ATN_NODE_FK = RTR_ANCESTOR_FK " +
	"AND RTR_DESCENDENT_FK = DESCEND_ATN.ATN_NODE_FK " +
	"AND ANCES_ATN.ATN_STAGE_FK = DESCEND_ATN.ATN_STAGE_FK " +
	"AND APO_NODE_FK = ANO_OID " +
	"AND APO_IS_PRIMARY = 1) ";

    private String GENE_STR = "QIC_RPR_SYMBOL ";
    private String INTERSECT_STR_GENE="AND QIC_RPR_SYMBOL IN ( ";
    
    private String prevOp="";
    
    private StringBuffer where_buf;
    private StringBuffer component_buf;
    private StringBuffer stage_buf;
    private StringBuffer expression_buf;
    private StringBuffer pattern_buf;
    private StringBuffer location_buf;

    private StringBuffer output_bf=new StringBuffer();

    private Vector v_queries=new Vector(0,1);
    private Vector v_expression=new Vector(0,1);
    private Vector v_stage=new Vector(0,1);
    private Vector v_component=new Vector(0,1);
    private Vector v_pattern=new Vector(0,1);
    private Vector v_location=new Vector(0,1);

    private int query_num=-1;
    private int patternIndex=-1;
    private int locationIndex=-1;
    private boolean includePattern=false;
    private boolean includeLocation=false;
    private boolean firstOperator=false;
    private boolean secondOperator=false;
    private boolean doOrAnd=false;	
	
	
	///////////////////////////
    
    public String getAllSubmissionsQuery(String input) {
    	
    	/*String sql = getProcessBooleanQuery(input)	+ " ORDER BY %s %s LIMIT ?, ?"; 
    			
    			if (sql.indexOf("DISTINCT")<0 || sql.indexOf("DISTINCT")>15)
    				sql = sql.replaceFirst("SELECT", "SELECT DISTINCT");*/
    	String sql = getProcessBooleanQuery(input);
    			
    	return sql;
    }
    
    /////////////////////////////
    
	public String getProcessBooleanQuery(String str) {
		v_queries=GetSeparatedMultiples(str,"|");
		StringBuffer[] buffers_array;
		Vector<StringBuffer[]> v_buffers = new Vector<StringBuffer[]>(0,1);
		query_num=v_queries.size();
		
		/*TO DETERMINE IF WE HAVE AN (A OR B) AND C AS THIS IS TREATED DIFFERENTLY TO THE REST */
        for(int i=0;i<query_num;i++) {
        	String tmp_str = v_queries.elementAt(i).toString();
        	String operator_str = tmp_str.substring(tmp_str.indexOf("}") + 1).trim();
            switch(i) {
            case 0:
                if(operator_str.equalsIgnoreCase("OR"))
                firstOperator = true;
                break;
            case 1:
                if(operator_str.equalsIgnoreCase("AND"))
                secondOperator = true;
                break;
            }
        }
        if(firstOperator&&secondOperator) {
        	doOrAnd = true;
            //System.out.println("THIS IS AN AND/OR QUERY!!!!!");
        }

        /*BUILD UP THE WHERE CLAUSE*/
        for(int i=0;i<query_num;i++) {
        	// initialise all sections of where clause
        	buffers_array=new StringBuffer[3];
//        	where_buf=new StringBuffer("WHERE (QIC_ANO_COMPONENT_NAME=");
        	where_buf=new StringBuffer("WHERE ");
        	StringBuffer subWhereClause = new StringBuffer("");
        	component_buf=new StringBuffer();
        	stage_buf=new StringBuffer("QIC_STG_STAGE_DISPLAY IN (");
//        	expression_buf=new StringBuffer("AND QIC_EXP_STRENGTH IN (");
        	expression_buf=new StringBuffer("QIC_EXP_STRENGTH= ");
        	pattern_buf=new StringBuffer("AND QIC_PTN_PATTERN=");
        	location_buf=new StringBuffer("AND QIL_LCN_LOCATION=");
        	
        	// reset data structures to re-build the query
        	ResetValues();
        	
        	// get input query text
        	String tmp_str = v_queries.elementAt(i).toString(); 
            // get component name
//            String componentName = tmp_str.substring(tmp_str.indexOf("{in") + 3, tmp_str.indexOf("TS")).trim();
            String componentName = tmp_str.substring(tmp_str.indexOf("\"")+1, tmp_str.lastIndexOf("\"")).trim();	
//            component_buf.append("'" + componentName + "'");
//            where_buf.append(component_buf.toString()+" ");
//            subWhereClause.append(component_buf.toString()+" ");
            
            // get stage
            String stage_str = 
            	tmp_str.substring(tmp_str.indexOf("TS"), tmp_str.indexOf("..") + 6);
            stage_str = SplitString(stage_str, "TS");
            SetStage(stage_str);
            where_buf.append(stage_buf.toString());
//            subWhereClause.append(stage_buf.toString());
            
            // check if pattern and/or location info included in the query string
            if (tmp_str.indexOf("pt=") > -1) {
//            	System.out.println("IS PATTERN??: " + tmp_str.indexOf("pt=")+"  ITERATION: " + i);
                includePattern = true;
                patternIndex = tmp_str.indexOf("pt=") + 3;
                //System.out.println("PATTERN_INDEX: " + patternIndex);
            }
            if (tmp_str.indexOf("lc=") > -1) {
            	includeLocation = true;
            	locationIndex = tmp_str.indexOf("lc=") + 3;
            }
            
            // get pattern if have one
            if (includePattern) {
                SetPattern(tmp_str);
                where_buf.append(pattern_buf.toString());
//                subWhereClause.append(pattern_buf.toString());
            }

            // get location if have one
            if (includeLocation) {
                SetLocation(tmp_str);
                where_buf.append(location_buf.toString());
//                subWhereClause.append(location_buf.toString());
            }
            
            // get expression (i.e. p, nd, u) and inheritance criteria
            String exp_str = tmp_str.substring(0, tmp_str.indexOf("{"));
            v_expression = GetSeparatedMultiples(exp_str, ",");
            int eLen = v_expression.size();
            if (eLen == 1) {
            	String expValue = 
            		this.getExpressionValueFromInput(v_expression.get(0).toString());
                subWhereClause.append(" AND ").append(expression_buf.toString())
                .append("'").append(expValue).append("' ") ;
                String inheritanceCiteriaString = ""; 
                if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));

                } else if (expValue.equalsIgnoreCase("not detected")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                }
                subWhereClause.append(inheritanceCiteriaString);
            } else { // more than one expression value
            	subWhereClause.append(" AND (");
            	String tempSwClause = "";
            	for (int j=0;j<eLen;j++) {
            		String expValue = 
            			this.getExpressionValueFromInput(this.v_expression.get(j).toString());
            		String expClause = expression_buf.toString() + "'" + expValue + "' ";
                    String inheritanceCiteriaString = ""; 
                    if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    	
                    } else if (expValue.equalsIgnoreCase("not detected")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    }
            		tempSwClause += "(" + expClause + inheritanceCiteriaString + ") OR ";
            	}
            	tempSwClause = tempSwClause.substring(0, tempSwClause.length()-4);
                subWhereClause.append(tempSwClause).append(")");
            }
            
//            System.out.println("subwhere: " + subWhereClause);
            where_buf.append(subWhereClause.toString());

            // get the operator after this sub-clause
            StringBuffer operator_buf = 
            	new StringBuffer(tmp_str.substring(tmp_str.indexOf("}") + 1).trim());
            
            buffers_array[0]=where_buf;
            buffers_array[1]=operator_buf;
            buffers_array[2]=new StringBuffer(prevOp);
            v_buffers.addElement(buffers_array);
            
            // SEND EACH ITERATION TO THE FULL QUERY BUILDER IF ITS NOT -OR AND-
            if(!doOrAnd)
                ConstructQuery(operator_buf.toString(),i,query_num, 0);

            prevOp=operator_buf.toString();
        }
        // GATHER ALL THE WHERE CLAUSES INTO A VECTOR FOR PROCESSING BY THE -OR AND- QUERY BUILDER
        if(doOrAnd)
            ConstructOrAnd(v_buffers, 0);

        SetOutput(output_bf);
        output_bf = null;
        output_bf=new StringBuffer();
        return GetOutput();
    }
    
    /////////////////////////////
	
	public ArrayList<String> getGeneSymbols(String input) {
		ArrayList<String> resultList = null;
		String sql = this.getGeneBooleanQueryString(input);
		if (sql == null || sql.equals("")) { // user might provide empty search query string
			return null;
		}
		String str = null;
		try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(sql); 
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				resultList = new ArrayList<String>();
				while (result.next()) {
					 str = Utils.netTrim(result.getString(1));
					    if (str!= null) {
						resultList.add(str);
					    }
				}
			}	
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		return resultList;
		
		
	}
	
	public String getGeneBooleanQueryString(String str) {
		v_queries=GetSeparatedMultiples(str,"|");
		StringBuffer[] buffers_array;
		Vector<StringBuffer[]> v_buffers = new Vector<StringBuffer[]>(0,1);
		query_num=v_queries.size();
		
		/*TO DETERMINE IF WE HAVE AN (A OR B) AND C AS THIS IS TREATED DIFFERENTLY TO THE REST */
        for(int i=0;i<query_num;i++) {
        	String tmp_str = v_queries.elementAt(i).toString();
        	String operator_str = tmp_str.substring(tmp_str.indexOf("}") + 1).trim();
            switch(i) {
            case 0:
                if(operator_str.equalsIgnoreCase("OR"))
                firstOperator = true;
                break;
            case 1:
                if(operator_str.equalsIgnoreCase("AND"))
                secondOperator = true;
                break;
            }
        }
        if(firstOperator&&secondOperator) {
        	doOrAnd = true;
            //System.out.println("THIS IS AN AND/OR QUERY!!!!!");
        }

        /*BUILD UP THE WHERE CLAUSE*/
        for(int i=0;i<query_num;i++) {
        	// initialise all sections of where clause
        	buffers_array=new StringBuffer[3];
//        	where_buf=new StringBuffer("WHERE (QIC_ANO_COMPONENT_NAME=");
        	where_buf=new StringBuffer("WHERE ");
        	StringBuffer subWhereClause = new StringBuffer("");
        	component_buf=new StringBuffer();
        	stage_buf=new StringBuffer("QIC_STG_STAGE_DISPLAY IN (");
//        	expression_buf=new StringBuffer("AND QIC_EXP_STRENGTH IN (");
        	expression_buf=new StringBuffer("QIC_EXP_STRENGTH= ");
        	pattern_buf=new StringBuffer("AND QIC_PTN_PATTERN=");
        	location_buf=new StringBuffer("AND QIL_LCN_LOCATION=");
        	
        	// reset data structures to re-build the query
        	ResetValues();
        	
        	// get input query text
        	String tmp_str = v_queries.elementAt(i).toString(); 
        	
            // get component name
            String componentName = tmp_str.substring(tmp_str.indexOf("\"")+1, tmp_str.lastIndexOf("\"")).trim();
            
            // get stage
            String stage_str = 
            	tmp_str.substring(tmp_str.indexOf("TS"), tmp_str.indexOf("..") + 6);
            stage_str = SplitString(stage_str, "TS");
            SetStage(stage_str);
            where_buf.append(stage_buf.toString());
//            subWhereClause.append(stage_buf.toString());
            
            // check if pattern and/or location info included in the query string
            // System.out.println("IS PATTERN??: " + tmp_str.indexOf("pt=")+"  ITERATION: " + i);
            if (tmp_str.indexOf("pt=") > -1) {
                includePattern = true;
                patternIndex = tmp_str.indexOf("pt=") + 3;
                //System.out.println("PATTERN_INDEX: " + patternIndex);
            }
            if (tmp_str.indexOf("lc=") > -1) {
                includeLocation = true;
                locationIndex = tmp_str.indexOf("lc=") + 3;
            }

            // get pattern if have one
            if (includePattern) {
                SetPattern(tmp_str);
                where_buf.append(pattern_buf.toString());
//                subWhereClause.append(pattern_buf.toString());
            }

            // get location if have one
            if (includeLocation) {
                SetLocation(tmp_str);
                where_buf.append(location_buf.toString());
//                subWhereClause.append(location_buf.toString());
            }
            
            // get expression (i.e. p, nd, u) and inheritance criteria
            String exp_str = tmp_str.substring(0, tmp_str.indexOf("{"));
            v_expression = GetSeparatedMultiples(exp_str, ",");
            int eLen = v_expression.size();
            if (eLen == 1) {
            	String expValue = 
            		this.getExpressionValueFromInput(v_expression.get(0).toString());
                subWhereClause.append(" AND ").append(expression_buf.toString())
                .append("'").append(expValue).append("' ") ;
                String inheritanceCiteriaString = ""; 
                if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));

                } else if (expValue.equalsIgnoreCase("not detected")) {
                    inheritanceCiteriaString += 
                    	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                    			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                }
                subWhereClause.append(inheritanceCiteriaString);
            } else { // more than one expression value
            	subWhereClause.append(" AND (");
            	String tempSwClause = "";
            	for (int j=0;j<eLen;j++) {
            		String expValue = 
            			this.getExpressionValueFromInput(this.v_expression.get(j).toString());
            		String expClause = expression_buf.toString() + "'" + expValue + "' ";
                    String inheritanceCiteriaString = ""; 
                    if (expValue.equalsIgnoreCase("present") || expValue.equalsIgnoreCase("uncertain")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_DESCENDANT.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    	
                    } else if (expValue.equalsIgnoreCase("not detected")) {
                        inheritanceCiteriaString += 
                        	this.INHERITANCE_CRITERIA_STR_ANSCESTOR.replaceAll("ANO_COMPONENT_NAME", 
                        			("ANO_COMPONENT_NAME = '" + componentName + "' "));
                    }
            		tempSwClause += "(" + expClause + inheritanceCiteriaString + ") OR ";
            	}
            	tempSwClause = tempSwClause.substring(0, tempSwClause.length()-4);
                subWhereClause.append(tempSwClause).append(")");
            }
            
//            System.out.println("subwhere: " + subWhereClause);
            where_buf.append(subWhereClause.toString());

            // get the operator after this sub-clause
            StringBuffer operator_buf = 
            	new StringBuffer(tmp_str.substring(tmp_str.indexOf("}") + 1).trim());
            
            buffers_array[0]=where_buf;
            buffers_array[1]=operator_buf;
            buffers_array[2]=new StringBuffer(prevOp);
            v_buffers.addElement(buffers_array);
            
            // SEND EACH ITERATION TO THE FULL QUERY BUILDER IF ITS NOT -OR AND-
            if(!doOrAnd)
                ConstructQuery(operator_buf.toString(),i,query_num, 1);

            prevOp=operator_buf.toString();
        } // end of for loop
        
        //  GATHER ALL THE WHERE CLAUSES INTO A VECTOR FOR PROCESSING BY THE -OR AND- QUERY BUILDER
        if(doOrAnd)
            ConstructOrAnd(v_buffers, 1);

        SetOutput(output_bf);
        output_bf = null;
        output_bf=new StringBuffer();
        return GetOutput();
    }
	
	/////////////////////////
	
	 private void SetOutput(StringBuffer bf)
	    {
	        outputString=bf.toString();
	    }

	    public String GetOutput()
	    {
	        return outputString;
	    }

	    private Vector GetSeparatedMultiples(String str, String separator)
	    {
	        Vector<String> v = new Vector<String>(0,1);
	        StringTokenizer st=new StringTokenizer(str,separator);
	        while(st.hasMoreTokens())
	        {
	           v.addElement(st.nextToken().trim());
	        }
	        return v;

	    }

	    private void ResetValues()
	    {
	        v_expression.clear();
	        v_stage.clear();
	        v_component.clear();
	        v_pattern.clear();
	        v_location.clear();
	        includePattern=false;
	        includeLocation=false;
	    }
	    
	    private String SplitString(String str,String rem)
	    {
	        StringBuffer sb=new StringBuffer();
	        String st[]=str.split(rem);
	        for(int i=0;i<st.length;i++)
	        {
	            sb.append(st[i]);
	        }
	        return sb.toString();
	   }
	    
	    private void SetStage(String str)
	    {
	        int from=0;
	        int to=0;
	        from=Integer.parseInt(str.substring(0,2));
	        to=Integer.parseInt(str.substring(4));
	        if (from > to) {
	        	int temp = from;
	        	from = to;
	        	to = temp;
	        }
	        for(int i=from;i<=to;i++)
	        {
	            stage_buf.append("'TS" + i + "'");
	            if(i<to)
	                stage_buf.append(",");
	            if(i==to)
	                stage_buf.append(") ");
	        }

	    }
	    
	    private void SetPattern(String str) 
	    {
	      int endIndex=-1;
	      if(includeLocation) {
	          endIndex=locationIndex-4;
	      }
	      else
	          endIndex=str.indexOf("}");
	      pattern_buf.append("'"+str.substring(patternIndex,endIndex).trim()+"' ");
	    }

	    private void SetLocation(String str)
	    {
	        location_buf.append("'"+str.substring(locationIndex,str.indexOf("}")).trim()+"' ");
	    }
	    
	    private String getExpressionValueFromInput(String input) {
			String expressionValue = "";
			for (int i=0;i<this.expression_array.length;i++) {
				if (input.equalsIgnoreCase(this.expression_array[i][1])) {
					expressionValue = this.expression_array[i][0];
					break;
				}
			}
			return expressionValue;
		}
	    
	    /**
	     * @param op
	     * @param num
	     * @param size
	     * @param queryType - 0: entry search; 1: gene search
	     */
		private void ConstructQuery(String op,int num,int size, int queryType) {
			
			if (op.equalsIgnoreCase("OR")&&num==0) {
				output_bf.append("( ");
			}
			
			output_bf.append(SELECT_STR);
			if(op.equalsIgnoreCase("AND") || op.equals("") || queryType == 1) { // add queryType condition
				output_bf.append(DISTINCT_STR);
			}
			if(prevOp.equalsIgnoreCase("AND")) {
				if (queryType == 0) {
					output_bf.append(ACCESSION_STR);
				} else if (queryType == 1) {
					output_bf.append(this.GENE_STR);
				}
			} else {
				if (queryType == 0) {
					output_bf.append(COLUMN_STR);
				} else if (queryType == 1) {
					output_bf.append(this.GENE_STR); 
				}
			}
			output_bf.append(FROM_STR);
			output_bf.append(where_buf.toString());
			
			if(op.equalsIgnoreCase("OR"))
				output_bf.append(UNION_STR);
			else if(prevOp.equalsIgnoreCase("AND") && num<v_queries.size()-1)
				output_bf.append(") ");
			
			if(op.equalsIgnoreCase("AND")) {
				if (queryType == 0) { // entry
					output_bf.append(INTERSECT_STR_ENTRY);
				} else if (queryType == 1) { // gene
					output_bf.append(INTERSECT_STR_GENE);
				}
			}
			if(num==v_queries.size()-1 && size!=1)
				output_bf.append(")");
		}

	    /**
	     * <p>modified by xingjun - 10/03/2009 - make it able to construct gene query string as well</p>
	     * @param v
	     * @param queryType
	     */
		private void ConstructOrAnd(Vector v, int queryType) {
	    	StringBuffer []tmp_buf=new StringBuffer[8];
	        output_bf.append(SELECT_STR);
	        if (queryType == 0) {
	    		output_bf.append(COLUMN_STR);
	        } else if (queryType == 1) {
	        	output_bf.append(this.DISTINCT_STR + this.GENE_STR); 
	        }
	        output_bf.append(FROM_STR);
	        tmp_buf=(StringBuffer[])v.elementAt(2);
	        output_bf.append(tmp_buf[0].toString());
	        if (queryType == 0) { // entry
	            output_bf.append(INTERSECT_STR_ENTRY);
	        } else if (queryType == 1) { // gene
	            output_bf.append(INTERSECT_STR_GENE);
	        }
	        output_bf.append(SELECT_STR);
	        
	        if (queryType == 0) { // entry
	            output_bf.append(ACCESSION_STR);
	        } else if (queryType == 1) { // gene
	            output_bf.append(GENE_STR);
	        }

	        output_bf.append(FROM_STR);
	        tmp_buf=(StringBuffer[])v.elementAt(0);
	        output_bf.append(tmp_buf[0].toString());
	        output_bf.append(SUB_UNION_STR);
	        output_bf.append(SELECT_STR);
	        
	        if (queryType == 0) { // entry
	            output_bf.append(ACCESSION_STR);
	        } else if (queryType == 1) { // gene
	            output_bf.append(GENE_STR);
	        }
	        output_bf.append(FROM_STR);
	        tmp_buf=(StringBuffer[])v.elementAt(1);
	        output_bf.append(tmp_buf[0].toString());
	        output_bf.append(")) ");
	    }

}
