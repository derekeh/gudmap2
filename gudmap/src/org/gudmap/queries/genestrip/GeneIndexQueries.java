package org.gudmap.queries.genestrip;

public class GeneIndexQueries {
	
	 final static public String getISHGeneIndex(String prefix, String organ) {
		  String sql =  "";
		  if(!prefix.equals("0-9")) {
			  sql = "select distinct QGI_RPR_SYMBOL, QGI_ISH_PRESENT, QGI_ISH_NOT_DETECTED, QGI_ISH_UNKNOWN, QGI_MIC_PRESENT, QGI_MIC_NOT_DETECTED, QGI_MIC_UNKNOWN "+
			  		" from QSC_GENE_INDEX where QGI_RPR_SYMBOL like '"+prefix+"%' ";
		  } else {
			  sql = "select distinct QGI_RPR_SYMBOL, QGI_ISH_PRESENT, QGI_ISH_NOT_DETECTED, QGI_ISH_UNKNOWN, QGI_MIC_PRESENT, QGI_MIC_NOT_DETECTED, QGI_MIC_UNKNOWN "+
		  		" from QSC_GENE_INDEX where (QGI_RPR_SYMBOL like '0%' or QGI_RPR_SYMBOL like '1%' or "+
			  "QGI_RPR_SYMBOL like '2%' or QGI_RPR_SYMBOL like '3%' or "+
			  "QGI_RPR_SYMBOL like '4%' or QGI_RPR_SYMBOL like '5%' or "+
			  "QGI_RPR_SYMBOL like '6%' or QGI_RPR_SYMBOL like '7%' or "+
			  "QGI_RPR_SYMBOL like '8%' or QGI_RPR_SYMBOL like '9%') ";
		  }
		  if(null != organ && !organ.equals("")) {
			  sql += " AND QGI_ORGAN_KEY='" + organ +"' ";
		  } else {
			  sql += " AND QGI_ORGAN_KEY='0' ";
		  }
		  
			  sql += " order by QGI_RPR_SYMBOL";
		  
		  //System.out.println("INDEX:"+sql);
		  return sql;
	  }
	 
	 public final static String GENES_BY_EXPRESSION = "SELECT DISTINCT QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
			 "DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%M %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_RPR_JAX_ACC probe_name, QIC_SUB_EMBRYO_STG stage, " +
			 "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE,' ',QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT,QIC_SPN_STAGE) END) age, " +
			 "QIC_SPN_SEX sex, QIC_SPN_WILDTYPE genotype, GROUP_CONCAT(DISTINCT QIC_ANO_COMPONENT_NAME) tissue,  QIC_EXP_STRENGTH expression, " +
			 "QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image FROM QSC_ISH_CACHE " +
			 "WHERE QIC_RPR_SYMBOL= ? %s GROUP BY gudmap_accession ORDER BY %s %s, stage LIMIT ?, ?";
	 
	 public final static String TOTAL_GENES_BY_EXPRESSION = "SELECT COUNT(*) FROM (SELECT DISTINCT QIC_RPR_SYMBOL gene, QIC_SUB_ACCESSION_ID gudmap_accession, QIC_SUB_SOURCE source, " +
			 "DATE_FORMAT(QIC_SUB_SUB_DATE,'%%e %%M %%Y') submission_date, QIC_SUB_ASSAY_TYPE assay_type, QIC_RPR_JAX_ACC probe_name, QIC_SUB_EMBRYO_STG stage, " +
			 "TRIM(CASE QIC_SPN_STAGE_FORMAT WHEN 'dpc' THEN CONCAT(QIC_SPN_STAGE,' ',QIC_SPN_STAGE_FORMAT) ELSE CONCAT(QIC_SPN_STAGE_FORMAT,QIC_SPN_STAGE) END) age, " +
			 "QIC_SPN_SEX sex, QIC_SPN_WILDTYPE genotype, GROUP_CONCAT(DISTINCT QIC_ANO_COMPONENT_NAME) tissue,  QIC_EXP_STRENGTH expression, " +
			 "QIC_SPN_ASSAY_TYPE specimen, QIC_SUB_THUMBNAIL image FROM QSC_ISH_CACHE " +
			 "WHERE QIC_RPR_SYMBOL=? %s GROUP BY gudmap_accession) AS TABLE_A;";

}
