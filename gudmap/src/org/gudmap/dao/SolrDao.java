package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.gudmap.globals.Globals;
import org.gudmap.queries.solr.SolrQueries;
import org.jsoup.Jsoup;

public class SolrDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private SolrInputDocument doc;
	private ArrayList<SolrInputDocument> docs;
	
	public SolrDao() {
	}
	
	public ArrayList<SolrInputDocument> getSolrGeneIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
		
        String queryString = SolrQueries.GET_GENE_INDEX_DATA;
//        queryString += " LIMIT 40000,1000 "; 
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				String id = result.getString(1);
				if (id != null && id != ""){
					doc.addField("id", id); 
					doc.addField("GENE", result.getString(1), (float)2.0); // added boost
					doc.addField("GENE_NAME", result.getString(2)); 
					String MGI_GENE_ID = result.getString(3);
					doc.addField("MGI_GENE_ID", MGI_GENE_ID); 
					doc.addField("MGI", result.getString(4)); 
					doc.addField("ENSEMBL_ID", result.getString(5)); 
					String synonyms = result.getString(6);
					doc.addField("SYNONYMS", synonyms, (float)1.5); // added boost
//					doc.addField("GENE_ID", result.getString(7)); 
					doc.addField("OMIM", result.getString(7));	
					doc.addField("ARRAY_RANGE", result.getString(8));	
					doc.addField("ISH_RANGE", result.getString(9));	
					
					
					doc.addField("PROBESETS", result.getString(10)); 
					doc.addField("ENTREZ_ID", result.getString(11)); 
					doc.addField("GENBANK_ID", result.getString(12)); 
					doc.addField("INSITU_ASSAY", result.getString(13)); 
					doc.addField("MA_ASSAY", result.getString(14)); 
					doc.addField("GUDMAP", result.getString(15)); 
					doc.addField("GUDMAP_IDS", result.getString(16)); 
					doc.addField("PRESENT", result.getString(17)); 
					doc.addField("DIR_PRESENT", result.getString(18)); 
					doc.addField("NOT_DETECTED", result.getString(19)); 
					doc.addField("UNCERTAIN", result.getString(20)); 
					doc.addField("EMAPS", result.getString(21)); 
					doc.addField("SOURCE", result.getString(22)); 
					doc.addField("PI_NAME", result.getString(23)); 
					doc.addField("LAB", result.getString(24));
					int anchor = result.getInt(25);
					if (anchor == 1)
						doc.addField("ANCHOR", "anchor"); 
					else
						doc.addField("ANCHOR", ""); 
					int marker = result.getInt(26);
					if (marker == 1)
						doc.addField("MARKER", "marker"); 
					else
						doc.addField("MARKER", ""); 
					doc.addField("GENE_TYPE", result.getString(27));
					doc.addField("SPECIES", result.getString(28));	
					
					// to filter out rubbish
					if (MGI_GENE_ID != null && MGI_GENE_ID != "")
						docs.add(doc);
				}
			}
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrGenelistsIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_GENELISTS_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
//				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("NAME", result.getString(2)); 
				doc.addField("DESCRIPTION", result.getString(3)); 	
				doc.addField("PLATFORM", result.getString(4)); 
				doc.addField("KEY_SAMPLE", result.getString(5)); 
				doc.addField("MA_DATASET", result.getString(6)); 	
				doc.addField("MA_DATASET_ID", result.getString(7)); 
				doc.addField("TOT_ENTITIES", result.getString(8)); 
				doc.addField("TOT_GENES", result.getString(9)); 	
				doc.addField("AUTHOR", result.getString(10)); 
				doc.addField("DATE", result.getString(11)); 
				doc.addField("STAGE", result.getString(12)); 
				doc.addField("SEX", result.getString(13)); 
				doc.addField("GENELIST_TYPE", result.getString(14)); 
				doc.addField("ENTITIES", result.getString(15)); 
				doc.addField("GENE", result.getString(16)); 
				doc.addField("GUDMAP_ID", result.getString(17)); 
				doc.addField("GUDMAP", result.getString(18)); 
				doc.addField("REF", result.getString(19)); 
				doc.addField("EMAP_IDS", result.getString(20)); 
				doc.addField("EMAP", result.getString(1)); 
				doc.addField("EMAP_TERM", result.getString(1)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
	public ArrayList<SolrInputDocument> getSolrInsituIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_INSITU_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(2)); 
				doc.addField("GUDMAP", result.getString(1)); 
				doc.addField("GUDMAP_ID", result.getString(2)); 
				doc.addField("GENE", result.getString(3)); 
				doc.addField("GENE_NAME", result.getString(4)); 
				doc.addField("MGI", result.getString(5)); 
				doc.addField("MGI_GENE_ID", result.getString(6)); 
				doc.addField("GENBANK_ID", result.getString(7)); 
				doc.addField("ENSEMBL_ID", result.getString(8)); 
				doc.addField("SYNONYMS", result.getString(9)); 
				doc.addField("PI_NAME", result.getString(10)); 
				doc.addField("LAB", result.getString(11)); 
				doc.addField("AUTHORS", result.getString(12)); 
				doc.addField("DATE", result.getString(13)); 
				doc.addField("STAGE", result.getString(14)); 
				doc.addField("PROBE_NAME", result.getString(15)); 
				doc.addField("CLONE_NAME", result.getString(16)); 
				doc.addField("PROBE_TISSUE", result.getString(17)); 
				doc.addField("PROBE_ID", result.getString(18)); 
				doc.addField("maprobe", result.getString(19)); 
				doc.addField("MAPROBE_ID", result.getString(20)); 
				doc.addField("PROBE_STRAIN", result.getString(21)); 
				doc.addField("PROBE_GENE_TYPE", result.getString(22)); 
				doc.addField("PROBE_TYPE", result.getString(23)); 
				doc.addField("PROBE_VISUAL_METHOD", result.getString(24)); 
				doc.addField("PROBE_NOTE", result.getString(25)); 
				doc.addField("CURATOR_NOTE", result.getString(26)); 
				doc.addField("RESULT_NOTE", result.getString(27)); 
				doc.addField("EXPERIMENT_NOTE", result.getString(28)); 
				doc.addField("IMAGE_WITH_NOTE", result.getString(29)); 
				doc.addField("IMAGE_NOTE", result.getString(30)); 
				doc.addField("IMAGE", result.getString(31)); 
				doc.addField("IMAGE_PATH", result.getString(32)); 
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(33)); 
				doc.addField("FIXATION_METHOD", result.getString(34)); 
				doc.addField("STRAIN", result.getString(35)); 
				doc.addField("SEX", result.getString(36)); 
				
				String age = result.getString(37);
				age = age.replace(" ", "");
				if (age.contains("P")){
					int len = age.length();
					age = age.substring(0, len-1);
					age = "P" + age;
				}
				doc.addField("DEV_STAGE", age); 
			
				doc.addField("GENOTYPE", result.getString(38)); 
				doc.addField("ASSAY_TYPE", result.getString(39)); 
				doc.addField("PROJECT", result.getString(40)); 
				doc.addField("ALT_ID", result.getString(41)); 
				doc.addField("SOURCE", result.getString(42)); 
				doc.addField("ANCHOR_GENE", result.getString(43)); 
				doc.addField("MARKER_GENE", result.getString(44)); 
				doc.addField("FOCUS_GROUPS", result.getString(45)); 
				doc.addField("ALLELE_MGI_ID", result.getString(46)); 
				doc.addField("ALLELE_NAME", result.getString(47)); 	
				doc.addField("ALLELE_TYPE", result.getString(48)); 					
				doc.addField("TISSUE", result.getString(49)); 					
				doc.addField("TISSUE_EMAPS", result.getString(50)); 					
				doc.addField("TISSUE_EMAPS_ID", result.getString(51)); 					
				doc.addField("SPECIES", result.getString(52)); 
				doc.addField("GENE_TYPE", result.getString(53)); 
				
				doc.addField("PRESENT", result.getString(54)); 
				doc.addField("DIR_PRESENT", result.getString(55)); 
				doc.addField("INF_PRESENT", result.getString(56)); 
				doc.addField("NOT_DETECTED", result.getString(57)); 
				doc.addField("INF_NOT_DETECTED", result.getString(58)); 
				doc.addField("UNCERTAIN", result.getString(59)); 
				doc.addField("EMAPS", result.getString(60)); 
				doc.addField("EXP_NOTES", result.getString(61)); 	
				doc.addField("EXPRESSION_NOTES", result.getString(62)); 	
				doc.addField("ANNOTATION", result.getString(63)); 				
				doc.addField("TISSUE_TYPE", result.getString(64)); 
 	
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

//	public ArrayList<SolrInputDocument> getSolrMicroarrayIndexData() {
//		
//		docs = new ArrayList<SolrInputDocument>();
//		
//        String queryString = SolrQueries.GET_MICROARRAY_INDEX_DATA;
//        
//        try
//		{
//			//con = ds.getConnection();
//			con=Globals.getDatasource().getConnection();
//			ps = con.prepareStatement(queryString); 
//			result =  ps.executeQuery();
//			
//			// add field maps the query result to the solr index schema
//			while (result.next()) {
//				doc = new SolrInputDocument();
////				doc.addField("id", result.getString(1)); 
//				doc.addField("GUDMAP", result.getString(1)); 
//				doc.addField("GUDMAP_ID", result.getString(2)); 
//				doc.addField("PLATFORM_GEO_ID", result.getString(3)); 
//				doc.addField("PLATFORM_TITLE", result.getString(4)); 
//				doc.addField("PLATFORM_NAME", result.getString(5)); 
//				doc.addField("SAMPLE_GEO_ID", result.getString(6)); 
//				doc.addField("SAMPLE_STRAIN", result.getString(7)); 
//				doc.addField("SAMPLE_SEX", result.getString(8)); 
//				doc.addField("DEVELOPMENT_STAGE", result.getString(9)); 
//				doc.addField("SAMPLE_THEILER_STAGE", result.getString(10)); 
//				doc.addField("SAMPLE_MOLECULE", result.getString(11)); 
//				doc.addField("SAMPLE_RNA_EXTRACT_PROTOCOL", result.getString(12));
//				doc.addField("SAMPLE_DISSECTION_METHOD", result.getString(13));
//				doc.addField("SAMPLE_EXPERIMENTAL_DESIGN", result.getString(14));	
//				doc.addField("SAMPLE_ARRAY_HYB_PROTOCOL", result.getString(15));
//				doc.addField("SAMPLE_DATA_ANALYSIS_METHOD", result.getString(16));	
//				doc.addField("SAMPLE_REFERENCE_USED", result.getString(17));
//				doc.addField("SAMPLE_TARGET_AMPLIFICATION_MANUFACTURER", result.getString(18));	
//				doc.addField("SAMPLE_SCAN_PROTOCOL", result.getString(19));
//				doc.addField("SAMPLE_LABEL_PROTOCOL", result.getString(20));	
//				doc.addField("SERIES_GEO_ID", result.getString(21));
//				doc.addField("SERIES_TITLE", result.getString(22));	
//				doc.addField("COMPONENT", result.getString(23));
//				doc.addField("EMAP", result.getString(24));	
//				doc.addField("PI_NAME", result.getString(25));	
//				doc.addField("LAB", result.getString(26));	
//				doc.addField("SOURCE", result.getString(27));					
//				doc.addField("DATE", result.getString(28));	
//				doc.addField("STAGE", result.getString(29));					
//				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(30));	
//				doc.addField("FIXATION_METHOD", result.getString(31));					
//				doc.addField("STRAIN", result.getString(32));					
//				doc.addField("SEX", result.getString(33));					
//				doc.addField("DEV_STAGE", result.getString(34));					
//				doc.addField("STAGE_FORMAT", result.getString(35));					
//				doc.addField("GENOTYPE", result.getString(36));				
//				doc.addField("FIRST_CHROMATID", result.getString(37));
//				doc.addField("SECOND_CHROMATID", result.getString(38));
//				doc.addField("ALLELE_MGI_ID", result.getString(39));
//				doc.addField("ALLELE_LAB_NAME", result.getString(40));
//				doc.addField("ALLELE_NAME", result.getString(41));
//				doc.addField("ALLELE_TYPE", result.getString(42));
//				doc.addField("GENE", result.getString(43));					
//				doc.addField("MGI_IDS", result.getString(44));					
//				doc.addField("MGI", result.getString(45));					
//				
//				docs.add(doc);
//			}
//		}
//		catch(SQLException sqle){sqle.printStackTrace();}
//		finally {
//		    Globals.closeQuietly(con, ps, result);
//		}
//        return docs;
//    }
	
	public ArrayList<SolrInputDocument> getSolrSamplesIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_SAMPLES_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
//				doc.addField("id", result.getString(1)); 
				doc.addField("GUDMAP_ID", result.getString(1)); 
				doc.addField("GUDMAP", result.getString(2)); 
				doc.addField("SAMPLE_GEO_ID", result.getString(3)); 
				doc.addField("STAGE", result.getString(4));
				String devstage = result.getString(5);
				devstage = devstage.replace(" ", "");
				doc.addField("DEV_STAGE", devstage); 
				doc.addField("SOURCE", result.getString(6)); 
				doc.addField("PI_NAME", result.getString(7)); 
				doc.addField("DATE", result.getString(8)); 
				doc.addField("SEX", result.getString(9)); 
				doc.addField("DESCRIPTION", result.getString(10)); 
				doc.addField("TITLE", result.getString(11)); 
				doc.addField("SERIES_GEO_ID", result.getString(12)); 
				doc.addField("COMPONENT", result.getString(13)); 
				doc.addField("QMC_ALE_GENE", result.getString(14)); 
				doc.addField("ASSAY_TYPE", result.getString(15)); 
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(16)); 
				doc.addField("PER_OID", result.getString(17)); 
				doc.addField("PLATFORM_GEO_ID", result.getString(18)); 
				doc.addField("SPECIES", result.getString(19)); 				
				doc.addField("GENOTYPE", result.getString(20)); 				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

//	public ArrayList<SolrInputDocument> getSolrSeriesIndexData() {
//		
//		docs = new ArrayList<SolrInputDocument>();
//		
//        String queryString = SolrQueries.GET_SERIES_INDEX_DATA;
//        
//        try
//		{
//			//con = ds.getConnection();
//			con=Globals.getDatasource().getConnection();
//			ps = con.prepareStatement(queryString); 
//			result =  ps.executeQuery();
//			
//			// add field maps the query result to the solr index schema
//			while (result.next()) {
//				doc = new SolrInputDocument();
////				doc.addField("id", result.getString(1)); 
//				doc.addField("SERIES_GEO_ID", result.getString(1)); 				
//				doc.addField("TITLE", result.getString(2)); 	
//				doc.addField("SAMPLE_NUMBER", result.getString(3)); 				
//				doc.addField("SOURCE", result.getString(4)); 	
//				doc.addField("PI_NAME", result.getString(5)); 				
//				doc.addField("PLATFORM_GEO_ID", result.getString(6)); 	
//				doc.addField("SERIES_OID", result.getString(7)); 				
//				doc.addField("COMPONENT", result.getString(8)); 	
////				doc.addField("GENE", result.getString(9)); 				
////				doc.addField("EMAP", result.getString(10)); 	
////				doc.addField("MGI", result.getString(11)); 				
////				doc.addField("PRESENT", result.getString(12)); 	
////				doc.addField("NOT_DETECTED", result.getString(13)); 				
////				doc.addField("UNCERTAIN", result.getString(14)); 	
////				doc.addField("FOCUS_GROUPS", result.getString(15)); 				
////				doc.addField("SEX", result.getString(16)); 	
////				doc.addField("LAB", result.getString(17)); 				
////				doc.addField("maprobe", result.getString(18)); 	
////				doc.addField("GUDMAP", result.getString(19)); 				
////				doc.addField("THEILER_STAGE", result.getString(20)); 	
////				doc.addField("AUTHORS", result.getString(21)); 				
//				
//				docs.add(doc);
//			}
//		}
//		catch(SQLException sqle){sqle.printStackTrace();}
//		finally {
//		    Globals.closeQuietly(con, ps, result);
//		}
//        return docs;
//    }
	
	public ArrayList<SolrInputDocument> getSolrTissueIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_TISSUE_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
//				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("NAME", result.getString(2), (float)2.0); // added boost
				doc.addField("SYNONYM", result.getString(3), (float)1.5); // added boost
				doc.addField("STAGES", result.getString(4)); 
				doc.addField("FOCUS_GROUPS", result.getString(5)); 
				doc.addField("GENELIST_IDS", result.getString(6)); 				
				doc.addField("GENELIST_NAMES", result.getString(7)); 				
				doc.addField("IMAGE_URL", result.getString(8)); 				
				doc.addField("IMAGE_NAME", result.getString(9)); 				
				doc.addField("SEC_NAMES", result.getString(10)); 				
				doc.addField("LINK_TYPES", result.getString(11)); 				
				doc.addField("GUDMAP", result.getString(12)); 				
				doc.addField("EMAP", result.getString(13)); 				
				doc.addField("EMAPIDS", result.getString(14)); 				
				doc.addField("COMPONENT", result.getString(15)); 				
				doc.addField("MGI", result.getString(16)); 				
				doc.addField("GENE_MGI_ID", result.getString(17)); 				
				doc.addField("GENE", result.getString(18)); 				
				doc.addField("PROBE_IDS", result.getString(19)); 				
				doc.addField("MA_PROBES_ID", result.getString(20)); 				
				doc.addField("maprobe", result.getString(21)); 				
				doc.addField("STAGE", result.getString(22)); 				
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrMouseStrainsIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_MOUSE_STRAINS_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
//				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("GENE", result.getString(2), (float)2.0); // added boost
				doc.addField("REPORTER_ALLELE", result.getString(3)); 
				doc.addField("ALLELE_TYPE", result.getString(4)); 
				doc.addField("ALLELE_VER", result.getString(5)); 
				doc.addField("ALLELE_VER_URL", result.getString(6)); 
				doc.addField("ALLELE_CHAR", result.getString(7)); 
				doc.addField("ALLELE_CHAR_URL", result.getString(8)); 
				doc.addField("STRAIN_AVA", result.getString(9)); 
				doc.addField("STRAIN_AVA_URL", result.getString(10)); 
				doc.addField("ORGAN", result.getString(11)); 
				doc.addField("CELL_TYPE", result.getString(12)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
	public ArrayList<SolrInputDocument> getSolrImageIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_IMAGE_INDEX_DATA;
        int count = 0;
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
//				doc.addField("id", result.getString(1)); 
				doc.addField("IMAGE_ID", result.getString(1)); 
				doc.addField("IMAGE", result.getString(2)); 
				doc.addField("IMAGE_PATH", result.getString(3)); 
				doc.addField("THUMBNAIL_PATH", result.getString(4)); 
				doc.addField("IMAGE_CLICK_PATH", result.getString(5)); 
				doc.addField("CLICK_FILENAME", result.getString(6)); 
				doc.addField("IMAGE_NOTE", result.getString(7)); 
				doc.addField("IMAGE_TYPE", result.getString(8)); 
				doc.addField("GUDMAP", result.getString(9)); 
				doc.addField("GUDMAP_ID", result.getString(10));
				doc.addField("GENE", result.getString(11)); 				
				doc.addField("GENE_NAME", result.getString(12)); 
				doc.addField("MGI", result.getString(13)); 				
				doc.addField("MGI_GENE_ID", result.getString(14)); 
				doc.addField("GENBANK_ID", result.getString(15)); 				
				doc.addField("ENSEMBL_ID", result.getString(16)); 
				doc.addField("SYNONYMS", result.getString(17)); 				
				doc.addField("PI_NAME", result.getString(18)); 
				doc.addField("DATE", result.getString(19)); 				
				doc.addField("STAGE", result.getString(20)); 
				doc.addField("PROBE_NAME", result.getString(21)); 
				doc.addField("CLONE_NAME", result.getString(22)); 				
				doc.addField("PROBE_TISSUE", result.getString(23)); 
				doc.addField("PROBE_ID", result.getString(24)); 				
				doc.addField("MAPROBE_ID", result.getString(25)); 
				doc.addField("maprobe", result.getString(26)); 				
				doc.addField("PROBE_STRAIN", result.getString(27)); 
				doc.addField("PROBE_GENE_TYPE", result.getString(28)); 				
				doc.addField("PROBE_TYPE", result.getString(29)); 
				doc.addField("PROBE_VISUAL_METHOD", result.getString(30)); 				
				doc.addField("PROBE_NOTE", result.getString(31)); 
				doc.addField("CURATOR_NOTE", result.getString(32)); 				
				doc.addField("RESULT_NOTE", result.getString(33)); 
				doc.addField("EXPERIMENT_NOTE", result.getString(34)); 				
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(35)); 
				doc.addField("FIXATION_METHOD", result.getString(36)); 				
				doc.addField("STRAIN", result.getString(37)); 
				doc.addField("SEX", result.getString(38)); 
				String devstage = result.getString(39);
				if (devstage != null)
					devstage = devstage.replace(" ", "");
				doc.addField("DEV_STAGE", devstage); 
				doc.addField("GENOTYPE", result.getString(40)); 				
				doc.addField("ASSAY_TYPE", result.getString(41)); 
				doc.addField("PROJECT", result.getString(42)); 				
				doc.addField("ALT_ID", result.getString(43)); 
				doc.addField("SOURCE", result.getString(44)); 				
				doc.addField("PRESENT", result.getString(45)); 
				doc.addField("INF_PRESENT", result.getString(46)); 				
				doc.addField("EMAPS", result.getString(47)); 
				doc.addField("EXPRESSION_NOTES", result.getString(48)); 
				doc.addField("EXP_NOTES", result.getString(49)); 
				doc.addField("SPECIES", result.getString(50)); 
				doc.addField("GENE_TYPE", result.getString(51)); 
				doc.addField("UIG_TITLE", result.getString(52)); 
				doc.addField("UIG_DESCE", result.getString(53)); 
				doc.addField("UGP_TITLE", result.getString(54)); 
				doc.addField("UGP_DESCRIPTION", result.getString(55)); 
				doc.addField("GROUP_ID", result.getString(56)); 
				
				docs.add(doc);
				count++;
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
       
       queryString = SolrQueries.GET_EUREXPRESS_IMAGE_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			count += 1;
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
//				doc.addField("id", count); 
				doc.addField("IMAGE_ID", String.valueOf(count)); 
				doc.addField("GUDMAP_ID", result.getString(1)); 
				doc.addField("GROUP_ID", result.getString(2)); 
				doc.addField("IMAGE", result.getString(4));
				doc.addField("EMAPS", result.getString(5)); 
				doc.addField("GENE", result.getString(7)); 
				doc.addField("STAGE", result.getString(8)); 
				doc.addField("DEV_STAGE", result.getString(9) + "dpc"); 				
				doc.addField("MGI_GENE_ID", result.getString(10)); 

				doc.addField("SPECIES", "Mus musculus"); 
				doc.addField("ASSAY_TYPE", "ISH"); 
				doc.addField("SEX", "unknown"); 
				doc.addField("IMAGE_TYPE", "image");
				
				
				String path = result.getString(1);
				if (path != null){
					path = path.replace("euxassay_","");
					int index = Integer.parseInt(path);
					if (index < 100)
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(0);
					else{
						index = index/100;
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(index);
					}
					path += "/" + result.getString(1) + "/";
					
					doc.addField("IMAGE_PATH", path + result.getString(4)); 
					doc.addField("THUMBNAIL_PATH", path + result.getString(4)); 
				}
				doc.addField("SOURCE", "eurexpress"); 				
				
				docs.add(doc);
				count++;

			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
 
/*
       queryString = SolrQueries.GET_EUREXPRESS_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			count += 1;
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
//				doc.addField("id", count); 
				doc.addField("IMAGE_ID", String.valueOf(count)); 
				doc.addField("GUDMAP_ID", result.getString(1)); 
				doc.addField("GENE", result.getString(2)); 
				doc.addField("MGI_GENE_ID", result.getString(3)); 
				doc.addField("DEV_STAGE", result.getString(5) + "dpc"); 
				doc.addField("STAGE", result.getString(6)); 
//				doc.addField("SYNONYM", result.getString(7)); 
//				doc.addField("COMPONENT", result.getString(8)); 

				doc.addField("SPECIES", "Mus musculus"); 
				doc.addField("ASSAY_TYPE", "ISH"); 
				doc.addField("SEX", "unknown"); 
				doc.addField("IMAGE_TYPE", "image");
				
				doc.addField("IMAGE", result.getString(1) + "_01.jpg");
				
				String path = result.getString(1);
				if (path != null){
					path = path.replace("euxassay_","");
					int index = Integer.parseInt(path);
					if (index < 100)
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(0);
					else{
						index = index/100;
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(index);
					}
					path += "/" + result.getString(1) + "/";
					
					doc.addField("IMAGE_PATH", path + result.getString(1) + "_01.jpg"); 
					doc.addField("THUMBNAIL_PATH", path + result.getString(1) + "_01.jpg"); 
				}
				doc.addField("SOURCE", "eurexpress"); 				
				
				docs.add(doc);
				count++;

			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
       
        
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        */       
        
        
        return docs;
    }

//	public ArrayList<SolrInputDocument> getSolrNextGenSeriesIndexData() {
//		
//		docs = new ArrayList<SolrInputDocument>();
//		
//        String queryString = SolrQueries.GET_NEXTGEN_SERIES_INDEX_DATA;
//        
//        try
//		{
//			//con = ds.getConnection();
//			con=Globals.getDatasource().getConnection();
//			ps = con.prepareStatement(queryString); 
//			result =  ps.executeQuery();
//			
//			// add field maps the query result to the solr index schema
//			while (result.next()) {
//				doc = new SolrInputDocument();
//				String tmp = result.getString(2);
////				doc.addField("id", result.getString(1)); 
//				doc.addField("SERIES_GEO_ID", result.getString(1)); 
//				doc.addField("TITLE", result.getString(2)); 
//				doc.addField("SAMPLE_NUMBER", result.getString(3)); 
//				doc.addField("SOURCE", result.getString(4)); 
//				doc.addField("PI_NAME", result.getString(5)); 
//				doc.addField("PLATFORM_GEO_ID", result.getString(6)); 
//				doc.addField("SERIES_OID", result.getString(7)); 
//				doc.addField("COMPONENT", result.getString(8)); 
//				doc.addField("GENE", result.getString(9)); 
//				doc.addField("EMAP", result.getString(10)); 
//				doc.addField("MGI", result.getString(11)); 
//				doc.addField("PRESENT", result.getString(12)); 
//				doc.addField("NOT_DETECTED", result.getString(13)); 
//				doc.addField("UNCERTAIN", result.getString(14)); 
//				doc.addField("FOCUS_GROUPS", result.getString(15)); 
//				doc.addField("SEX", result.getString(16)); 
//				doc.addField("LAB", result.getString(17)); 
//				doc.addField("maprobe", result.getString(18)); 
//				doc.addField("GUDMAP", result.getString(19)); 
//				doc.addField("THEILER_STAGE", result.getString(20)); 
//				doc.addField("AUTHORS", result.getString(21)); 
//				
//				docs.add(doc);
//			}
//		}
//		catch(SQLException sqle){sqle.printStackTrace();}
//		finally {
//		    Globals.closeQuietly(con, ps, result);
//		}
//        return docs;
//    }

	public ArrayList<SolrInputDocument> getSolrNextGenSamplesIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_NEXTGEN_SAMPLES_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
//				doc.addField("id", result.getString(2)); 
				doc.addField("GUDMAP", result.getString(1)); 
				doc.addField("SAMPLE_GEO_ID", result.getString(2)); 
				doc.addField("SERIES_GEO_ID", result.getString(3)); 
				doc.addField("SOURCE", result.getString(4)); 
				doc.addField("LIBRARY_STRATEGY", result.getString(5)); 
				doc.addField("STAGE", result.getString(6)); 
				doc.addField("PI_NAME", result.getString(7)); 
				String devstage = result.getString(8);
				devstage = devstage.replace(" ", "");
				doc.addField("DEV_STAGE", devstage); 
				doc.addField("DATE", result.getString(9)); 
				doc.addField("SEX", result.getString(10)); 
				doc.addField("SAMPLE_DESCRIPTION", result.getString(11)); 
				doc.addField("SAMPLE_NAME", result.getString(12)); 
				doc.addField("GENOTYPE", result.getString(13)); 
				doc.addField("COMPONENT", result.getString(14)); 
				doc.addField("SPECIES", result.getString(15)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<String> getGeneList() {
		
		ArrayList<String> genelist = new ArrayList<String>();
        String queryString = SolrQueries.GENE_LIST;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				genelist.add(result.getString(1).toLowerCase());
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return genelist;
    }
	
	public ArrayList<SolrInputDocument> getSolrWebIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_WEB_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("ID", result.getString(1)); 
				doc.addField("ALIAS", result.getString(4)); 
				doc.addField("TITLE", result.getString(5)); 
				doc.addField("DATE", result.getString(14)); 
				doc.addField("URL", "/pages/viewPage.jsf?docID=" + result.getString(1)); 
//				doc.addField("CONTENT", result.getString(6)); 
				// strip out the html markup
				String html = result.getString(6);
				String text = "";
				if (html != null)
					text = Jsoup.parse(html).text();
				else
					text = html;
				
				doc.addField("CONTENT", text);
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrEurExpressIndexData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_EUREXPRESS_INDEX_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			
			int count = 1;
			// add field maps the query result to the solr index schema
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", count); 
				doc.addField("ID", result.getString(9)); 
				doc.addField("EUREXP_ID", result.getString(1)); 
				doc.addField("GENE", result.getString(2)); 
				doc.addField("MGI_GENE_ID", result.getString(3)); 
				doc.addField("ENTREZ_ID", result.getString(4)); 
				doc.addField("DEV_STAGE", result.getString(5) + "dpc"); 
				doc.addField("STAGE", result.getString(6)); 
				doc.addField("SYNONYM", result.getString(7)); 
				doc.addField("COMPONENT", result.getString(8)); 

				doc.addField("SPECIES", "Mus musculus"); 
				doc.addField("ASSAY_TYPE", "ISH"); 
				doc.addField("SEX", "unknown"); 
				doc.addField("IMAGE_TYPE", "image");
				
				doc.addField("IMAGE", result.getString(1) + "_01.jpg");
				
				String path = result.getString(1);
				if (path != null){
					path = path.replace("euxassay_","");
					int index = Integer.parseInt(path);
					if (index < 100)
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(0);
					else{
						index = index/100;
						path = "http://www.eurexpress.org/euximages/images/" + String.valueOf(index);
					}
					path += "/" + result.getString(1) + "/";
					
					doc.addField("IMAGE_PATH", path + result.getString(1) + "_01.jpg"); 
					doc.addField("THUMBNAIL_PATH", path + result.getString(1) + "_01.jpg"); 
				}
				
				
				
				
				
				docs.add(doc);
				count++;

			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
	
}
