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

public class SolrDao {
	
	//private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private SolrInputDocument doc;
	 private ArrayList<SolrInputDocument> docs;
	public SolrDao() {
	}
	
	public ArrayList<SolrInputDocument> getSolrGeneData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_GENE_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				doc.addField("GENE", result.getString(1)); 
				doc.addField("GENE_NAME", result.getString(2)); 
				doc.addField("MGI_GENE_ID", result.getString(3)); 
				doc.addField("MGI", result.getString(4)); 
				doc.addField("ENSEMBL_ID", result.getString(15)); 
				doc.addField("SYNONYMS", result.getString(16)); 
				doc.addField("PROBESETS", result.getString(17)); 
				doc.addField("ENTREZ_ID", result.getString(18)); 
				doc.addField("GENBANK_ID", result.getString(19)); 
				doc.addField("INSITU_ASSAY", result.getString(10)); 
				doc.addField("MA_ASSAY", result.getString(11)); 
				doc.addField("GUDMAP", result.getString(12)); 
				doc.addField("GUDMAP_IDS", result.getString(13)); 
				doc.addField("PRESENT", result.getString(14)); 
				doc.addField("DIR_PRESENT", result.getString(15)); 
				doc.addField("NOT_DETECTED", result.getString(16)); 
				doc.addField("UNCERTAIN", result.getString(17)); 
				doc.addField("EMAP", result.getString(18)); 
				doc.addField("SOURCE", result.getString(19)); 
				doc.addField("PI_NAME", result.getString(20)); 
				doc.addField("LAB", result.getString(21)); 
				doc.addField("ANCHOR", result.getString(22)); 
				doc.addField("MARKER", result.getString(23)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrGenelistsData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_GENELISTS_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
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
				doc.addField("THEILER_STAGE", result.getString(12)); 
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
	
	public ArrayList<SolrInputDocument> getSolrInsituData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_INSITU_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
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
				doc.addField("THEILER_STAGE", result.getString(14)); 
				doc.addField("THEILER_STAGE_FILTER", result.getString(15)); 
				doc.addField("PROBE_NAME", result.getString(16)); 
				doc.addField("CLONE_NAME", result.getString(17)); 
				doc.addField("PROBE_TISSUE", result.getString(18)); 
				doc.addField("PROBE_ID", result.getString(19)); 
				doc.addField("maprobe", result.getString(20)); 
				doc.addField("MAPROBE_ID", result.getString(21)); 
				doc.addField("PROBE_STRAIN", result.getString(22)); 
				doc.addField("PROBE_GENE_TYPE", result.getString(23)); 
				doc.addField("PROBE_TYPE", result.getString(24)); 
				doc.addField("PROBE_VISUAL_METHOD", result.getString(25)); 
				doc.addField("PROBE_NOTE", result.getString(26)); 
				doc.addField("CURATOR_NOTE", result.getString(27)); 
				doc.addField("RESULT_NOTE", result.getString(28)); 
				doc.addField("EXPERIMENT_NOTE", result.getString(29)); 
				doc.addField("IMAGE_WITH_NOTE", result.getString(30)); 
				doc.addField("IMAGE_NOTE", result.getString(31)); 
				doc.addField("IMAGE", result.getString(32)); 
				doc.addField("IMAGE_PATH", result.getString(33)); 
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(34)); 
				doc.addField("FIXATION_METHOD", result.getString(35)); 
				doc.addField("STRAIN", result.getString(36)); 
				doc.addField("SEX", result.getString(37)); 
				doc.addField("STAGE", result.getString(38)); 
				doc.addField("GENOTYPE", result.getString(39)); 
				doc.addField("ASSAY_TYPE", result.getString(40)); 
				doc.addField("PROJECT", result.getString(41)); 
				doc.addField("ALT_ID", result.getString(42)); 
				doc.addField("SOURCE", result.getString(43)); 
				doc.addField("ANCHOR_GENE", result.getString(4)); 
				doc.addField("MARKER_GENE", result.getString(44)); 
				doc.addField("FOCUS_GROUPS", result.getString(45)); 
				doc.addField("PRESENT", result.getString(46)); 
				doc.addField("DIR_PRESENT", result.getString(47)); 
				doc.addField("INF_PRESENT", result.getString(48)); 
				doc.addField("NOT_DETECTED", result.getString(49)); 
				doc.addField("INF_NOT_DETECTED", result.getString(50)); 
				doc.addField("UNCERTAIN", result.getString(51)); 
				doc.addField("EMAP", result.getString(52)); 
				doc.addField("EXP_NOTES", result.getString(53)); 	
				doc.addField("EXPRESSION_NOTES", result.getString(54)); 	
				doc.addField("ANNOTATION", result.getString(55)); 				
				doc.addField("TISSUE_TYPE", result.getString(56)); 
				doc.addField("ALLELE_MGI_ID", result.getString(57)); 
				doc.addField("ALLELE_NAME", result.getString(58)); 	
				doc.addField("ALLELE_TYPE", result.getString(59)); 	
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrMicroarrayData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_MICROARRAY_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				doc.addField("GUDMAP", result.getString(1)); 
				doc.addField("GUDMAP_ID", result.getString(2)); 
				doc.addField("PLATFORM_GEO_ID", result.getString(3)); 
				doc.addField("PLATFORM_TITLE", result.getString(4)); 
				doc.addField("PLATFORM_NAME", result.getString(5)); 
				doc.addField("SAMPLE_GEO_ID", result.getString(6)); 
				doc.addField("SAMPLE_STRAIN", result.getString(7)); 
				doc.addField("SAMPLE_SEX", result.getString(8)); 
				doc.addField("DEVELOPMENT_STAGE", result.getString(9)); 
				doc.addField("SAMPLE_THEILER_STAGE", result.getString(10)); 
				doc.addField("SAMPLE_MOLECULE", result.getString(11)); 
				doc.addField("SAMPLE_RNA_EXTRACT_PROTOCOL", result.getString(12));
				doc.addField("SAMPLE_DISSECTION_METHOD", result.getString(13));
				doc.addField("SAMPLE_EXPERIMENTAL_DESIGN", result.getString(14));	
				doc.addField("SAMPLE_ARRAY_HYB_PROTOCOL", result.getString(15));
				doc.addField("SAMPLE_DATA_ANALYSIS_METHOD", result.getString(16));	
				doc.addField("SAMPLE_REFERENCE_USED", result.getString(17));
				doc.addField("SAMPLE_TARGET_AMPLIFICATION_MANUFACTURER", result.getString(18));	
				doc.addField("SAMPLE_SCAN_PROTOCOL", result.getString(19));
				doc.addField("SAMPLE_LABEL_PROTOCOL", result.getString(20));	
				doc.addField("SERIES_GEO_ID", result.getString(21));
				doc.addField("SERIES_TITLE", result.getString(22));	
				doc.addField("COMPONENT", result.getString(23));
				doc.addField("EMAP", result.getString(24));	
				doc.addField("PI_NAME", result.getString(25));	
				doc.addField("LAB", result.getString(26));	
				doc.addField("SOURCE", result.getString(27));					
				doc.addField("DATE", result.getString(28));	
				doc.addField("THEILER_STAGE", result.getString(29));					
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(30));	
				doc.addField("FIXATION_METHOD", result.getString(31));					
				doc.addField("STRAIN", result.getString(32));					
				doc.addField("SEX", result.getString(33));					
				doc.addField("STAGE", result.getString(34));					
				doc.addField("STAGE_FORMAT", result.getString(35));					
				doc.addField("GENOTYPE", result.getString(36));					
				doc.addField("GENE", result.getString(37));					
				doc.addField("MGI_IDS", result.getString(38));					
				doc.addField("MGI", result.getString(39));					
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
	public ArrayList<SolrInputDocument> getSolrSamplesData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_SAMPLES_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				doc.addField("GUDMAP", result.getString(1)); 
				doc.addField("SAMPLE_GEO_ID", result.getString(2)); 
				doc.addField("THEILER_STAGE", result.getString(3)); 
				doc.addField("STAGE", result.getString(4)); 
				doc.addField("SOURCE", result.getString(5)); 
				doc.addField("PI_NAME", result.getString(6)); 
				doc.addField("DATE", result.getString(7)); 
				doc.addField("SEX", result.getString(8)); 
				doc.addField("DESCRIPTION", result.getString(9)); 
				doc.addField("TITLE", result.getString(10)); 
				doc.addField("SERIES_GEO_ID", result.getString(11)); 
				doc.addField("COMPONENT", result.getString(12)); 
				doc.addField("QMC_ALE_GENE", result.getString(13)); 
				doc.addField("ASSAY_TYPE", result.getString(14)); 
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(15)); 
				doc.addField("PER_OID", result.getString(16)); 
				doc.addField("PLATFORM_GEO_ID", result.getString(17)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrSeriesData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_SERIES_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				doc.addField("SERIES_GEO_ID", result.getString(1)); 				
				doc.addField("TITLE", result.getString(2)); 	
				doc.addField("SAMPLE_NUMBER", result.getString(3)); 				
				doc.addField("SOURCE", result.getString(4)); 	
				doc.addField("PI_NAME", result.getString(5)); 				
				doc.addField("PLATFORM_GEO_ID", result.getString(6)); 	
				doc.addField("SERIES_OID", result.getString(7)); 				
				doc.addField("COMPONENT", result.getString(8)); 	
				doc.addField("GENE", result.getString(9)); 				
				doc.addField("EMAP", result.getString(10)); 	
				doc.addField("MGI", result.getString(11)); 				
				doc.addField("PRESENT", result.getString(12)); 	
				doc.addField("NOT_DETECTED", result.getString(13)); 				
				doc.addField("UNCERTAIN", result.getString(14)); 	
				doc.addField("FOCUS_GROUPS", result.getString(15)); 				
				doc.addField("SEX", result.getString(16)); 	
				doc.addField("LAB", result.getString(17)); 				
				doc.addField("maprobe", result.getString(18)); 	
				doc.addField("GUDMAP", result.getString(19)); 				
				doc.addField("THEILER_STAGE", result.getString(20)); 	
				doc.addField("AUTHORS", result.getString(21)); 				
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
	public ArrayList<SolrInputDocument> getSolrTissueData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_TISSUE_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("NAME", result.getString(2)); 
				doc.addField("SYNONYM", result.getString(3)); 
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
				doc.addField("THEILER_STAGE", result.getString(22)); 				
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }

	public ArrayList<SolrInputDocument> getSolrMouseStrainsData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_MOUSE_STRAINS_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
				doc.addField("id", result.getString(1)); 
				doc.addField("ID", result.getString(1)); 
				doc.addField("GENE", result.getString(2)); 
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
	
	public ArrayList<SolrInputDocument> getSolrImageData() {
		
		docs = new ArrayList<SolrInputDocument>();
		
        String queryString = SolrQueries.GET_IMAGE_DATA;
        
        try
		{
			//con = ds.getConnection();
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			while (result.next()) {
				doc = new SolrInputDocument();
				String tmp = result.getString(1);
				doc.addField("id", result.getString(1)); 
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
				doc.addField("THEILER_STAGE", result.getString(20)); 
				doc.addField("THEILER_STAGE_FILTER", result.getString(21)); 				
				doc.addField("PROBE_NAME", result.getString(22)); 
				doc.addField("CLONE_NAME", result.getString(23)); 				
				doc.addField("PROBE_TISSUE", result.getString(24)); 
				doc.addField("PROBE_ID", result.getString(25)); 				
				doc.addField("MAPROBE_ID", result.getString(26)); 
				doc.addField("maprobe", result.getString(27)); 				
				doc.addField("PROBE_STRAIN", result.getString(28)); 
				doc.addField("PROBE_GENE_TYPE", result.getString(29)); 				
				doc.addField("PROBE_TYPE", result.getString(30)); 
				doc.addField("PROBE_VISUAL_METHOD", result.getString(31)); 				
				doc.addField("PROBE_NOTE", result.getString(32)); 
				doc.addField("CURATOR_NOTE", result.getString(33)); 				
				doc.addField("RESULT_NOTE", result.getString(34)); 
				doc.addField("EXPERIMENT_NOTE", result.getString(35)); 				
				doc.addField("SPECIMEN_ASSAY_TYPE", result.getString(36)); 
				doc.addField("FIXATION_METHOD", result.getString(37)); 				
				doc.addField("STRAIN", result.getString(38)); 
				doc.addField("SEX", result.getString(39)); 				
				doc.addField("STAGE", result.getString(40)); 
				doc.addField("GENOTYPE", result.getString(41)); 				
				doc.addField("ASSAY_TYPE", result.getString(42)); 
				doc.addField("PROJECT", result.getString(43)); 				
				doc.addField("ALT_ID", result.getString(44)); 
				doc.addField("SOURCE", result.getString(45)); 				
				doc.addField("PRESENT", result.getString(46)); 
				doc.addField("INF_PRESENT", result.getString(47)); 				
				doc.addField("EMAP", result.getString(48)); 
				doc.addField("EXPRESSION_NOTES", result.getString(49)); 
				doc.addField("EXP_NOTES", result.getString(50)); 
				
				docs.add(doc);
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return docs;
    }
	
}
