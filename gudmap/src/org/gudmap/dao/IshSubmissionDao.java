package org.gudmap.dao;

import org.gudmap.globals.Globals;
import org.gudmap.models.submission.ExpressionDetailModel;
import org.gudmap.models.submission.ExpressionPatternModel;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.models.submission.IshBrowseSubmissionModel;
import org.gudmap.models.submission.AntibodyModel;
import org.gudmap.models.submission.AlleleModel;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.models.submission.ImageDetailModel;
import org.gudmap.models.submission.PersonModel;
import org.gudmap.models.submission.ProbeModel;
import org.gudmap.models.submission.SpecimenModel;
import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.models.submission.StatusNoteModel;
import org.gudmap.queries.submission.*;
import org.gudmap.utils.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.sql.*;
import java.text.DateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class IshSubmissionDao {
	
	private boolean debug = false;
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public IshSubmissionDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
    public SubmissionModel findSubmissionById(String oid) {
        if (oid == null) {
	    //            throw new NullPointerException("id parameter");
        	return null;
        }
	
        SubmissionModel submissionModel = null;
        String queryString = IshSubmissionQueries.SUBMISSION_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				submissionModel = new SubmissionModel();
				submissionModel.setAccID(result.getString(1));
				submissionModel.setStage(result.getString(2));
				submissionModel.setAssayType(result.getString(3));
				submissionModel.setPublicFlag(result.getInt(7));
				submissionModel.setArchiveId(result.getString(8));		    
				submissionModel.setDeletedFlag(result.getInt(9));
				submissionModel.setSubmitterId(result.getInt(10));
				submissionModel.setPiId(result.getInt(11));
				submissionModel.setEntryBy(result.getInt(12));
				submissionModel.setModifierId(result.getInt(13));
				submissionModel.setDbStatusId(result.getInt(14));
				submissionModel.setProject(result.getString(15));
				submissionModel.setBatchId(result.getInt(16));
				submissionModel.setNameSpace(result.getString(17));
				submissionModel.setOsAccession(result.getString(18));
				submissionModel.setLoaclId(result.getString(19));
				submissionModel.setSource(result.getString(20));
				submissionModel.setValidation(result.getString(21));
				submissionModel.setControl(result.getInt(22));
				submissionModel.setAssessment(result.getString(23));
	            submissionModel.setConfidencLevel(result.getInt(24));
	            submissionModel.setLocalDbName(result.getString(25));
	            submissionModel.setLabId(result.getString(26));
	            submissionModel.setEuregeneId(result.getString(27));
	            submissionModel.setOid(result.getString(28));
		    
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        //NOW GET THE NOTES
        queryString = IshSubmissionQueries.SUBMISSION_NOTES_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			
			if (null != result && null != submissionModel) {
			    String str = null;
			    List<String> list = new ArrayList<String>();
			    while (result.next()) {
					str = result.getString(2);
					if (null != str) {
					    str = str.trim();
					    if (str.equals(""))
					    	str = null;
					}
					if (null != str && str.equalsIgnoreCase("result")) {
					    str = result.getString(1);
					    if (null != str) {
							str = str.trim();
							if (!str.equals("") && !list.contains(str))
							    list.add(str);
					    }
					}
			    }

			    if (0 == list.size())
			    	submissionModel.setResultNotes(null);
			    else 
			    	submissionModel.setResultNotes((String[])list.toArray(new String[0]));
			        //submissionInfo.setResultNotes((String[])list.toArray(new String[list.size()]));
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return submissionModel;       
    } //end findSubmissionById
    
    public ProbeModel findProbeBySubmissionId(String oid) {
    	
        if (oid == null) {
        	return null;
        }
	
        ProbeModel probeModel = null;
        String queryString = IshSubmissionQueries.PROBE_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result!= null && result.first()) {
	            probeModel = new ProbeModel();
	            probeModel.setGeneSymbol(result.getString(1));
	            probeModel.setGeneName(result.getString(2));
	            String probeName = result.getString(3);
	            probeModel.setProbeName(probeName);
	            probeModel.setGeneID(result.getString(4));
	            probeModel.setSource(result.getString(5));
	            probeModel.setStrain(result.getString(6));
	            probeModel.setTissue(result.getString(7));
	            probeModel.setType(result.getString(8));
	            probeModel.setGeneType(result.getString(9));
	            probeModel.setLabelProduct(result.getString(10));
	            probeModel.setVisMethod(result.getString(11));
	            probeModel.setCloneName(result.getString(12));
	            probeModel.setGenbankID(result.getString(13));
	            probeModel.setMaprobeID(result.getString(14));
	            probeModel.setProbeNameURL(result.getString(15));
	            probeModel.setGenbankURL(result.getString(16));
	            probeModel.setSeqStatus(result.getString(17));
	            probeModel.setSeq5Loc(result.getString(18));
	            probeModel.setSeq3Loc(result.getString(19));
		    
			    String str = null;
		            str = Utils.netTrim(result.getString(20));
			    if (null == str)
			    	probeModel.setSeq5Primer(null);
			    else
			    	probeModel.setSeq5Primer(str.toUpperCase());
			    
	            str = Utils.netTrim(result.getString(21));
			    if (null == str)
			    	probeModel.setSeq3Primer(null);
			    else
			    	probeModel.setSeq3Primer(str.toUpperCase());
			    
			    probeModel.setGeneIdUrl(result.getString(22));	    
			    probeModel.setAdditionalCloneName(result.getString(23));
			    probeModel.setLabProbeId(result.getString(24));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.PROBE_NOTE_BY_OID;
        try
		{
        	String str="";
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result!= null && result.first() && probeModel!=null) {
				result.beforeFirst();
                String notes = new String("");
                while (result.next()) {
                    str = Utils.netTrim(result.getString(1));
                    if (null != str)
                    notes += str + " ";
                }
                probeModel.setNotes(notes.trim());
            }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.MAPROBE_NOTE_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result!= null && result.first() && probeModel!=null) {
				ArrayList<String> maprobeNotes = new ArrayList<String>();
				result.beforeFirst();
                String notes = null;
                String[] separatedNote = null;
                while (result.next()) {
				    notes = Utils.netTrim(result.getString(1));
				    if (null != notes) {
						notes = notes.replaceAll("\\s", " ").trim();
						if (notes.equals(""))
						    notes = null;
				    }
				    if (null != notes) {
                        separatedNote = notes.split("\\|");
                    	if(null != separatedNote && separatedNote.length > 1) {
						    for(int i = 0; i < separatedNote.length; i++) {
						    	maprobeNotes.add(separatedNote[i]);
						    }
                    	} 
                    	else {
						    maprobeNotes.add(notes);
                    	}
                    }
                }
                if (maprobeNotes.size() == 0) {
            		probeModel.setMaprobeNotes(null);
                } else {
                    probeModel.setMaprobeNotes(maprobeNotes);
                }
            }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.FULL_SEQUENCE_BY_OID;
        try
		{
        	ArrayList <String>seqs = null;
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result!= null && result.first() && probeModel!=null) {
				result.beforeFirst();
            	String fullSeq = new String();
                while (result.next()) {
				    fullSeq += result.getString(1);
                }
                String origin = null;
                int count;                
                if(null != fullSeq) {               
				    seqs = new ArrayList<String>();
				    origin = fullSeq.trim();
				    count = origin.length() / 60;
				    for(int i = 0; i < count; i++) {
				    	seqs.add(origin.substring(i*60, i*60+60));
				    }
	                int remainder = origin.length() / 60;
		    
	                if (remainder > 0) {
	                	seqs.add(origin.substring(count*60));
	                }               	
                }     	
                probeModel.setFullSequence(seqs);
            }
			else
				probeModel.setFullSequence(null);
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return probeModel;
	    
    }// findProbeBySubmissionId
    
    public AntibodyModel findAntibodyBySubmissionId(String oid) {
    	
        AntibodyModel antibodyModel = null;
        
        
        
        String queryString = IshSubmissionQueries.ANTIBODY_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
			    antibodyModel = new AntibodyModel();
			    // set properties
			    antibodyModel.setMaprobeID(result.getString(1));
			    antibodyModel.setName(result.getString(2));
			    antibodyModel.setAccessionId(result.getString(3));
			    antibodyModel.setGeneSymbol(result.getString(4));
			    antibodyModel.setGeneName(result.getString(5));
			    antibodyModel.setLocusTag(result.getString(6));
			    antibodyModel.setUniprotId(result.getString(7));
			    antibodyModel.setseqStartLocation(result.getInt(8));
			    antibodyModel.setSeqEndLocation(result.getInt(9));
			    
			    String antibodyType = result.getString(10);
			    antibodyModel.setType(antibodyType);
			    // obtain host based on antibodyModel type
			    if (antibodyType.trim().equalsIgnoreCase("monoclonal")) {
					String productionMethod = result.getString(11);
					String cloneId = result.getString(12);
					if (cloneId == null)
					    antibodyModel.setHost(productionMethod);
					else
					    antibodyModel.setHost(productionMethod + " " +cloneId);
			    } else { // antibodyModel type = polyclonal
			    	antibodyModel.setHost(result.getString(13));
			    }
			    
			    antibodyModel.setPurificationMethod(result.getString(14));
			    antibodyModel.setImmunoglobulinIsotype(result.getString(15));
			    antibodyModel.setChainType(result.getString(16));
			    antibodyModel.setDirectLabel(result.getString(17));
			    antibodyModel.setDetectionNotes(result.getString(18));
			    antibodyModel.setDilution(result.getString(19));
			    antibodyModel.setLabProbeId(result.getString(20));
			    
			    // supplier
			    antibodyModel.setSupplier(result.getString(21));
			    antibodyModel.setCatalogueNumber(result.getString(22));
			    antibodyModel.setLotNumber(result.getString(23));
			    
			    antibodyModel.setSecondaryAntibody(result.getString(24));
			    antibodyModel.setSignalDetectionMethod(result.getString(25));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.PROBE_NOTE_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result != null && result.first() && antibodyModel != null) {
				antibodyModel.setNotes(result.getString(1));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.ANTIBODY_SPECIECS_SPECIFICITY_BY_OID;
        String speciesSpecificities = null;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result != null && result.first() && antibodyModel != null) {
				result.beforeFirst();
				speciesSpecificities = new String("");
				while (result.next()) {
				    speciesSpecificities += result.getString(2) + ", ";
				}
				// remove trailing ',' character
				speciesSpecificities = speciesSpecificities.substring(0, speciesSpecificities.length()-2);
	        }
			antibodyModel.setSpeciesSpecificity(speciesSpecificities);
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.ANTIBODY_VARIANTS_BY_OID;
        String antibodyVariants = null;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result != null && result.first() && antibodyModel != null) {
				result.beforeFirst();
				antibodyVariants = new String("");
				while (result.next()) {
				    antibodyVariants += result.getString(2) + ", ";
				}
				// remove trailing ',' character
				antibodyVariants = antibodyVariants.substring(0, antibodyVariants.length()-2);
	        }
			 antibodyModel.setDetectedVariantValue(antibodyVariants);
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return antibodyModel;
	    
    }// end findAntibodyBySubmissionId
    
}
