package org.gudmap.dao;

import org.gudmap.globals.Globals;
import org.gudmap.models.submission.AntibodyModel;
import org.gudmap.models.submission.AlleleModel;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.models.submission.ImageDetailModel;
import org.gudmap.models.submission.PersonModel;
import org.gudmap.models.submission.ProbeModel;
import org.gudmap.models.submission.SpecimenModel;
import org.gudmap.models.submission.SubmissionModel;
import org.gudmap.queries.submission.*;
import org.gudmap.utils.Utils;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

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
	            submissionModel.setStageAnatomy(result.getString(29));
		    
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
    

    
    public ProbeModel findProbeBySubmissionId(String oid, String id2, boolean isSubOid) {
    	
    	boolean getExtra=false;
        if (oid == null) {
        	return null;
        }
        
		if (!isSubOid) {
			if(id2 == null || id2 == ""){;}
		    else {getExtra=true;}
		}
        ProbeModel probeModel = null;
        String queryString = (isSubOid)?IshSubmissionQueries.PROBE_BY_OID:IshSubmissionQueries.MAPROBE_BY_PROBE_ID;
        if(getExtra)
        	queryString=queryString.replace("WHERE RPR_JAX_ACC = ?", "WHERE RPR_JAX_ACC = ? AND  PRB_MAPROBE = ?");
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			if(getExtra)
				ps.setString(2, id2);
			result =  ps.executeQuery();
			if (result!= null && result.first()) {
	            probeModel = new ProbeModel();
	            probeModel.setGeneSymbol(result.getString(1));
	            probeModel.setGeneName(result.getString(2));
	            String probeName = result.getString(3);
	            probeModel.setProbeName(probeName);
	            probeModel.setGeneId(result.getString(4));
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
			    probeModel.setAssayType(result.getString(25));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = (isSubOid)?IshSubmissionQueries.PROBE_NOTE_BY_OID:IshSubmissionQueries.PROBE_NOTE_BY_MAPROBE_ID;;
        try
		{
        	String str="";
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			if(isSubOid)
				ps.setString(1, oid);
			else
				ps.setString(1, id2.replace("maprobe:", ""));
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
        
        queryString = (isSubOid)?IshSubmissionQueries.MAPROBE_NOTE_BY_OID:IshSubmissionQueries.MAPROBE_NOTE_BY_MAPROBE_ID;;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString);
			if(isSubOid)
				ps.setString(1, oid);
			else
				ps.setString(1, id2.replace("maprobe:", ""));
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
        if(isSubOid)
        {
	        queryString = IshSubmissionQueries.FULL_SEQUENCE_BY_OID;
	        try
			{
	        	//ArrayList <String>seqs = null;
	        	String[] seqs = null;
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
					   // seqs = new ArrayList<String>();
					    origin = fullSeq.trim();
					    count = origin.length() / 60;
					    seqs = new String[count+1];
					    for(int i = 0; i < count; i++) {
					    	//seqs.add(origin.substring(i*60, i*60+60));
					    	seqs[i]=origin.substring(i*60, i*60+60);
					    }
		                int remainder = origin.length() / 60;
			    
		                if (remainder > 0) {
		                	//seqs.add(origin.substring(count*60));
		                	seqs[count]=origin.substring(count*60);
		                } 
		                else
		                	seqs[count]="";
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
        }
        
        return probeModel;
	    
    }// findProbeBySubmissionId
    
 public AntibodyModel findAntibodyBySubmissionId(String oid, boolean isSubOid) {
    	
        AntibodyModel antibodyModel = null;
        
        
        
        String queryString = (isSubOid)?IshSubmissionQueries.ANTIBODY_BY_OID:IshSubmissionQueries.ANTIBODY_BY_MAPROBE_ID;
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
			    antibodyModel.setGeneId(result.getString(6));
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
			    antibodyModel.setAssayType(result.getString(26));
			    
			    antibodyModel.setCuratorNotes(result.getString(27));
	        }
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        if(isSubOid){
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
	        
	        queryString = IshSubmissionQueries.ANTIBODY_SPECIES_SPECIFICITY_BY_OID;
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
					
					antibodyModel.setSpeciesSpecificity(speciesSpecificities);
		        }
				
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
					
					antibodyModel.setDetectedVariantValue(antibodyVariants);
		        }
				 
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
        }
        
        return antibodyModel;
	    
    }// end findAntibodyBySubmissionId
    
    
 
    public SpecimenModel findSpecimenBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
        }
        SpecimenModel specimenModel = null;
        String queryString = IshSubmissionQueries.SPECIMEN_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
					specimenModel = new SpecimenModel();
					specimenModel.setGenotype(result.getString(1));
					specimenModel.setAssayType(result.getString(2));
					specimenModel.setFixMethod(result.getString(3));
					specimenModel.setEmbedding(result.getString(4));
					specimenModel.setStrain(result.getString(5));
					specimenModel.setStageFormat(result.getString(6));
					specimenModel.setOtherStageValue(result.getString(7));
					specimenModel.setPhase(result.getString(11));
					specimenModel.setSex(result.getString(9));
					specimenModel.setSpecies(result.getString(12));
					specimenModel.setStagingNotes(result.getString(13));
				
			}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.SPECIMEN_NOTE_BY_OID;
        List<String> notes = new ArrayList<String>();
        String str = null;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first() && specimenModel!=null) {
				result.beforeFirst();
                while (result.next()) {
                    str = Utils.netTrim(result.getString(1));
				    if (null != str)
					notes.add(str);
                }
		
				if (0 == notes.size())
				    specimenModel.setNotes(null);
				else
				    specimenModel.setNotes((String[])notes.toArray(new String[0]));
            }
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return specimenModel;
        
	    
    }// end findSpecimenBySubmissionId
    
    public AlleleModel[] findAlleleBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
        }
        String queryString = IshSubmissionQueries.ALLELE_BY_OID;
        ArrayList<AlleleModel> alleleList = new ArrayList<AlleleModel>();
	 	String str = null;
	 	AlleleModel alleleModel=null;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			
		 	if (result.first()) {
		 	    result.beforeFirst();
		 	    while (result.next()) {
			 		alleleModel = new AlleleModel();
			 		str = Utils.netTrim(result.getString(1));
					alleleModel.setGeneSymbol(str);
			
			 		str = Utils.netTrim(result.getString(2));
					alleleModel.setGeneId(str);
			
			 		str = Utils.netTrim(result.getString(3));
					alleleModel.setAlleleId(str);
					
					// name first, then lab name
			 		str = Utils.netTrim(result.getString(5));
					if (null == str)
					    str = Utils.netTrim(result.getString(4));
					alleleModel.setAlleleName(str);
			
			 		str = Utils.netTrim(result.getString(7));
					alleleModel.setType(str);
			
			 		str = Utils.netTrim(result.getString(8));
					alleleModel.setAlleleFirstChrom(str);
			
			 		str = Utils.netTrim(result.getString(9));
					alleleModel.setAlleleSecondChrom(str);
			
			 		str = Utils.netTrim(result.getString(10));
					alleleModel.setReporter(str);
			
			 		str = Utils.netTrim(result.getString(11));
					alleleModel.setVisMethod(str);
			
					str = Utils.netTrim(result.getString(12));
					alleleModel.setNotes(str);
			
			 		alleleList.add(alleleModel);
		 	    }
			}
			if (debug)
			    System.out.println("number of allele = "+alleleList.size());
		
			if (0 == alleleList.size())
			    return null;

	     	
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return (AlleleModel[])alleleList.toArray(new AlleleModel[0]);
        
    }// end findAlleleBySubmissionId
    
    public ArrayList<ImageInfoModel> findImageBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
        }
        ArrayList<ImageInfoModel> imageInfoList = null;
        String queryString = IshSubmissionQueries.IMAGE_INFO_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
	            result.beforeFirst();
	            int serialNo = 1;
	            imageInfoList = new ArrayList<ImageInfoModel>();
			    //int dotPosition = 0;
			   // String fileExtension = null;
			    String str = null;
			    ImageInfoModel imageInfoModel = null;

	            while (result.next()) {
	            	imageInfoModel = new ImageInfoModel();
					str = Utils.netTrim(result.getString(1));
					if (null != str && !str.equals("")) 
						imageInfoModel.setAccessionId(str);
					str = Utils.netTrim(result.getString(2));
					if (null != str && !str.equals("")) 
						imageInfoModel.setFilePath(str);
					str = Utils.netTrim(result.getString(3));
					if (null != str && !str.equals("")) 
						imageInfoModel.setNote(str);
					str = Utils.netTrim(result.getString(4));
					if (null != str && !str.equals("")) 
						imageInfoModel.setSpecimenType(str);
					str = Utils.netTrim(result.getString(5));
					if (null != str && !str.equals("")) 
						imageInfoModel.setClickFilePath(str);
				
					imageInfoModel.setSerialNo(""+serialNo);
	                serialNo++;
	                imageInfoList.add(imageInfoModel);
	            }	            
	        }			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return imageInfoList;
    }// end findImageBySubmissionId
    
    public ImageDetailModel findWlzImageDetailBySubmissionId(String oid) {
        ImageDetailModel imageDetailModel = null;
        String queryString = IshSubmissionQueries.IMAGE_DETAIL_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
		    	imageDetailModel = new ImageDetailModel();
		    	imageDetailModel.setAccessionId(result.getString(1));
		    	imageDetailModel.setGeneSymbol(result.getString(2));
		    	imageDetailModel.setGeneName(result.getString(3));
		    	imageDetailModel.setStage(result.getString(4));
		    	imageDetailModel.setSpecimenType(result.getString(7));
		    	imageDetailModel.setFilePath(result.getString(8));
		    	imageDetailModel.setClickFilePath(result.getString(9));
            }
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return imageDetailModel;
    	
    }//end findWlzImageDetailBySubmissionId
    
    public String findAuthorBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
	    }
        String authorInfo = null;
        String queryString = IshSubmissionQueries.AUTHOR_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
	            while (result.next()) {
	            	authorInfo += result.getString(1) + " ";
	            }	            
	        }			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
      //GROUP_CONCAT RETURNS NULL STRING IF NO NON NULL VALUES FOUND.
        //IF USE GROUP BY IN SQL THEN WILL LOSE THE SEPARATOR OF THE CONCAT SO...
        if(authorInfo.equalsIgnoreCase("null "))
        	return null;
        else
        	return authorInfo.trim().replace("null", "");        
        
      
    }// end findAuthorBySubmissionId
    
    public PersonModel[] findPIsBySubmissionId(String oid) {
    	if (oid == null) {
    		return null;
    	}
        PersonModel[] personModelArray = null;
        ArrayList<PersonModel> personList = null;
        String queryString = IshSubmissionQueries.PIS_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			
	        if (result.first()) {
	        	result.beforeFirst();
	        	personList = new ArrayList<PersonModel>();
			    while (result.next()) {
		                PersonModel personModel = new PersonModel();
		                personModel.setName(result.getString(1));
		                personModel.setLab(result.getString(2));
		                personModel.setAddress(result.getString(3));
		                personModel.setAddress2(result.getString(4));
		                personModel.setEmail(result.getString(5));
		                personModel.setCity(result.getString(6));
		                personModel.setPostcode(result.getString(7));
		                personModel.setCountry(result.getString(8));
		                personModel.setPhone(result.getString(9));
		                personModel.setFax(result.getString(10));
		                personModel.setId(result.getString(11));
		                personList.add(personModel);
			    }
	        }		
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        if (personList != null) {
        	personModelArray = personList.toArray(new PersonModel[0]);
 
        }
        if(null == personModelArray || 0 == personModelArray.length) {
        	queryString = IshSubmissionQueries.PI_BY_OID;
        	personModelArray = new PersonModel[1];
        	PersonModel personModel = new PersonModel();
        	try
     		{
     			con = ds.getConnection();
     			ps = con.prepareStatement(queryString); 
     			ps.setString(1, oid);
     			result =  ps.executeQuery();
     			
     			if (result.first()) {
     	            personModel.setName(result.getString(1));
     	            personModel.setLab(result.getString(2));
     	            personModel.setAddress(result.getString(3));
     	            personModel.setAddress2(result.getString(4));
     	            personModel.setEmail(result.getString(5));
     	            personModel.setCity(result.getString(6));
     	            personModel.setPostcode(result.getString(7));
     	            personModel.setCountry(result.getString(8));
     	            personModel.setPhone(result.getString(9));
     	            personModel.setFax(result.getString(10));
     	            personModel.setId(result.getString(11));
     	        } else {
     	            personModel.setName("n/a");
     	            personModel.setLab("");
     	            personModel.setAddress("");
     	            personModel.setAddress2("");
     	            personModel.setEmail("");
     	            personModel.setCity("");
     	            personModel.setPostcode("");
     	            personModel.setCountry("");
     	            personModel.setPhone("");
     	            personModel.setFax("");
     	            personModel.setId("");
     	        }
     			personModelArray[0] = personModel;
     		}
     		catch(SQLException sqle){sqle.printStackTrace();}
     		finally {
     		    Globals.closeQuietly(con, ps, result);
     		}
        	if(null == personModelArray[0])
        		personModelArray = null;
        }
        
       
	    return personModelArray;
	    
    }//end findPIsBySubmissionId
    
    public PersonModel findSubmitterBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
        }
        PersonModel personModel = new PersonModel();
        String queryString = IshSubmissionQueries.SUBMITTER_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
 	            personModel.setName(result.getString(1));
 	            personModel.setLab(result.getString(2));
 	            personModel.setAddress(result.getString(3));
 	            personModel.setAddress2(result.getString(4));
 	            personModel.setEmail(result.getString(5));
 	            personModel.setCity(result.getString(6));
 	            personModel.setPostcode(result.getString(7));
 	            personModel.setCountry(result.getString(8));
 	            personModel.setPhone(result.getString(9));
 	            personModel.setFax(result.getString(10));
 	            personModel.setId(result.getString(11));
 	        } else {
 	            personModel.setName("n/a");
 	            personModel.setLab("");
 	            personModel.setAddress("");
 	            personModel.setAddress2("");
 	            personModel.setEmail("");
 	            personModel.setCity("");
 	            personModel.setPostcode("");
 	            personModel.setCountry("");
 	            personModel.setPhone("");
 	            personModel.setFax("");
 	            personModel.setId("");
 	        }			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
        
        return personModel;      
    } // end findSubmitterBySubmissionId
    
    
    public PersonModel findPersonByPersonId(String oid) {
        if (oid == null) {
		    return null;
        }
        PersonModel personModel = new PersonModel();
        String queryString = IshSubmissionQueries.PERSON_BY_PER_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
 	            personModel.setName(result.getString(1));
 	            personModel.setLab(result.getString(2));
 	            personModel.setAddress(result.getString(3));
 	            personModel.setAddress2(result.getString(4));
 	            personModel.setEmail(result.getString(5));
 	            personModel.setCity(result.getString(6));
 	            personModel.setPostcode(result.getString(7));
 	            personModel.setCountry(result.getString(8));
 	            personModel.setPhone(result.getString(9));
 	            personModel.setFax(result.getString(10));
 	            personModel.setId(result.getString(11));
 	        } else {
 	            personModel.setName("n/a");
 	            personModel.setLab("");
 	            personModel.setAddress("");
 	            personModel.setAddress2("");
 	            personModel.setEmail("");
 	            personModel.setCity("");
 	            personModel.setPostcode("");
 	            personModel.setCountry("");
 	            personModel.setPhone("");
 	            personModel.setFax("");
 	            personModel.setId("");
 	        }			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		} 
        
        return personModel;      
    } // end findSubmitterBySubmissionId
    
    public ArrayList<String[]> findPublicationBySubmissionId(String oid) {
        if (oid == null) {
		    return null;
        }
        ArrayList<String[]> publicationList = null;
        String queryString = IshSubmissionQueries.LINKED_PUBLICATIONS_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			ResultSetMetaData resultSetData = result.getMetaData();
			int columnCount = resultSetData.getColumnCount();
			
			if (result.first()) {
				
				//need to reset cursor as 'if' move it on a place
				result.beforeFirst();
				
				//create ArrayList to store each row of results in
				publicationList = new ArrayList<String[]>();
				int i = 0;
				String[] columns = null;
				String str = null;
				while (result.next()) {
					columns = new String[columnCount];
					for (i = 0; i < columnCount; i++) {
						str = result.getString(i + 1);
						if (null == str)
						    str = "";
						else {
						    str = str.trim();
						    if (str.equalsIgnoreCase("null"))
							str = "";
						}

						columns[i] = str;
					}
					publicationList.add(columns);
				}	
			}
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return publicationList;

    }// end findPublicationBySubmissionId
    
    public String[] findAcknowledgementBySubmissionId(String oid) {
        String[] AcknowledgementArray = null;
        String queryString = IshSubmissionQueries.ACKNOWLEDGEMENTS_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			AcknowledgementArray = Utils.formatResultSetToStringArray(result);			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return AcknowledgementArray;
	    
    } // end findAcknowledgementBySubmissionId
    
    public ArrayList<String[]> findLinkedSubmissionBySubmissionId(String oid) {
        ArrayList<String[]> linkedSubmissionList = null;
        String queryString = IshSubmissionQueries.ACCESSION_BY_OID;
        String accessionID = "";
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if(result.first())
				accessionID = result.getString(1);
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        queryString = IshSubmissionQueries.LINKED_SUBMISSIONS_BY_OID;
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, accessionID);
			ps.setString(2, oid);
			result =  ps.executeQuery();
			linkedSubmissionList = Utils.formatResultSetToArrayList(result);
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return linkedSubmissionList;

    } //end findLinkedSubmissionBySubmissionId
    
    public String findTissueBySubmissionId(String oid)
    {
		String tissue = null;
		String queryString = IshSubmissionQueries.TISSUE_BY_OID;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
                tissue = result.getString(1);
				while (result.next()) {
					tissue += "," + result.getString(1);
				}
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		return tissue;
    } //end findTissueBySubmissionId
    
    public ArrayList<String[]> findRelatedSubmissionBySymbol(String geneId, String assayType) {
  		if (geneId == null || geneId.equals("")) {
  		    return null;
  		}
  		ArrayList<String[]> relatedSubmissionISH=null;
  		String queryString = IshSubmissionQueries.GENE_RELATED_SUBMISSIONS_ISH;
  		
//		if(assayType.equals("TG"))
//			queryString = queryString.replace("RPR_LOCUS_TAG", "ALE_MUTATED_GENE_ID");

  			
  		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, geneId);
			ps.setString(2, assayType);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				relatedSubmissionISH = Utils.formatResultSetToArrayList(result);
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return relatedSubmissionISH; 
                	    
       
      }
    public ArrayList<String[]> findRelatedSubmissionByProbeId(String probeId, String assayType) {
  		if (probeId == null || probeId.equals("")) {
  		    return null;
  		}
  		ArrayList<String[]> relatedSubmissionISH=null;
  		String queryString = IshSubmissionQueries.GENE_RELATED_SUBMISSIONS_ISH2;
  		
//		if(assayType.equals("TG"))
//			queryString = queryString.replace("RPR_LOCUS_TAG", "ALE_MUTATED_GENE_ID");

  			
  		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, probeId);
			ps.setString(2, assayType);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				relatedSubmissionISH = Utils.formatResultSetToArrayList(result);
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        return relatedSubmissionISH; 
                	    
       
      }
    
    public String findStageByOid(String oid) {
        String stage = null;
        String queryString=IshSubmissionQueries.STAGE_BY_OID;
        
        try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				stage = "TS" + result.getString(2);
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
       
        return stage;
                  
    }
    
    //////////////////////////////////////////
    
    public ImageDetailModel findImageDetailBySubmissionId(String submissionAccessionId, int serialNum) {
		ImageDetailModel imageDetailModel = null;
		String queryString = IshSubmissionQueries.SUBMISSION_IMAGE_DETAIL;// in situ
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setString(1, submissionAccessionId);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
				imageDetailModel = populateImageDetailModel(result,serialNum);
            }
						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		if(imageDetailModel==null) {
			
			queryString = IshSubmissionQueries.SUBMISSION_IMAGE_DETAIL_TG;// tg
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, submissionAccessionId);
				ps.setInt(2, serialNum);
				result =  ps.executeQuery();
				if (result.first()) {
					result.beforeFirst();
					imageDetailModel = populateImageDetailModel(result,serialNum);
	            }
							
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}	
		if(imageDetailModel!=null){
			queryString=IshSubmissionQueries.ISH_SUBMISSION_ALL_IMAGE_NOTES;
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, submissionAccessionId);
				result =  ps.executeQuery();
				if (result.first()) {
					ArrayList<String[]> allImageNotes = new ArrayList<String[]>();
					result.beforeFirst();
		          	String[] filenamenNote = null;
		          	while (result.next()) {
						    filenamenNote = new String[2];
						    filenamenNote[0] = Utils.netTrim(result.getString(1));
						    filenamenNote[1] = Utils.netTrim(result.getString(2));
						    if (null != filenamenNote[0] || null !=  filenamenNote[1])
						    allImageNotes.add(filenamenNote);
		          	}
		          	imageDetailModel.setAllImageNotesInSameSubmission(allImageNotes);
	            }
							
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
			
			queryString=IshSubmissionQueries.ISH_SUBMISSION_PUBLIC_IMGS;
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, submissionAccessionId);
				result =  ps.executeQuery();
				if (result.first()) {
					ArrayList<String> publicImgs = new ArrayList<String>();
					result.beforeFirst();
		          	while(result.next()){
		          		publicImgs.add(result.getString(1));
		          	}
		          	imageDetailModel.setAllPublicImagesInSameSubmission(publicImgs);
	            }
							
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			}
		}
		return imageDetailModel;
		
    }
    
    public ImageDetailModel populateImageDetailModel(ResultSet result,int serialNum) throws SQLException {
      if (result.first()) {
    	  ImageDetailModel imageDetailModel = new ImageDetailModel();
    	  imageDetailModel.setAccessionId(result.getString(1));
    	  imageDetailModel.setGeneSymbol(result.getString(2));
    	  imageDetailModel.setGeneName(result.getString(3));
    	  imageDetailModel.setStage(result.getString(4));
    	  imageDetailModel.setAge(result.getString(5));
    	  imageDetailModel.setAssayType(result.getString(6));
    	  imageDetailModel.setSpecimenType(result.getString(7));
    	  imageDetailModel.setFilePath(result.getString(8));
    	  imageDetailModel.setSerialNo(Integer.toString(serialNum + 1));
	    
          return imageDetailModel;
      }
      return null;
  }
    
    //////////////////////////////////////////
    
}
