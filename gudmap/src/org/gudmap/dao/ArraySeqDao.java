package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gudmap.globals.Globals;
import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.DataProcessing;
import org.gudmap.models.SeqProtocol;
import org.gudmap.models.SupplementaryFiles;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.models.submission.PlatformModel;
import org.gudmap.models.submission.SampleModel;
import org.gudmap.models.submission.SeqSampleModel;
import org.gudmap.models.submission.SeqSeriesModel;
import org.gudmap.models.submission.SeriesModel;
import org.gudmap.queries.array.ArrayQueries;
import org.gudmap.queries.array.SequenceQueries;
import org.gudmap.utils.Utils;

public class ArraySeqDao {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public ArraySeqDao() {
		
	}
	
    public SupplementaryFiles findSupplementaryFileInfoBySubmissionId(int oid) {
		
		if (oid == 0) {
		    return null;
		}		
		SupplementaryFiles supplementaryFiles = null;		
        try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SUPPLEMENTARY_FILES); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				supplementaryFiles = new SupplementaryFiles();
				supplementaryFiles.setFileLocation(result.getString("location"));
				supplementaryFiles.setCelFile(result.getString("cel_file"));
				supplementaryFiles.setChpFile(result.getString("chp_file"));
				supplementaryFiles.setExpFile(result.getString("exp_file"));
				supplementaryFiles.setRptFile(result.getString("rpt_file"));
				supplementaryFiles.setTxtFile(result.getString("txt_file"));
			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}        
        return supplementaryFiles;		
    }
    
    public SupplementaryFiles findSeqSupplementaryFiles(int oid) {
    	SupplementaryFiles supplementaryFiles = null;		
        try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(SequenceQueries.SEQUENCE_SUPPLEMENTARY_FILES); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
			    supplementaryFiles = new SupplementaryFiles();
			    List<SupplementaryFiles> rFiles=new ArrayList<SupplementaryFiles>();
			    List<SupplementaryFiles> pFiles=new ArrayList<SupplementaryFiles>();
			    List<SupplementaryFiles> qFiles=new ArrayList<SupplementaryFiles>();			    
			    SupplementaryFiles ngdsupfiles=null;
			    StringBuffer filesize=new StringBuffer();
			    String qcpath = "";
			    
			    do
			    {
			    	filesize.setLength(0);
			    	if(result.getString(4)!=null)
			    		filesize.append("("+result.getString(4)+")");
			    		
			    	if(result.getString(3).equalsIgnoreCase("raw"))
			    	{
			    		ngdsupfiles=new SupplementaryFiles();
			    		ngdsupfiles.setFilename(result.getString(2).trim());
		    	    	ngdsupfiles.setFilesize(filesize.toString());
		    	    	ngdsupfiles.setFiletype(result.getString(3).trim());
			    		rFiles.add(ngdsupfiles);//1.NGF_FILEPATH; 2.NGF_FILENAME; 3.NGF_RAW; 4.NGF_FILESIZE
			    	}
			    	else if (result.getString(3).equalsIgnoreCase("processed"))
			    	{
			    		ngdsupfiles=new SupplementaryFiles();
			    		ngdsupfiles.setFilename(result.getString(2).trim());
		    	    	ngdsupfiles.setFilesize(filesize.toString());
		    	    	ngdsupfiles.setFiletype(result.getString(3).trim());
			    		pFiles.add(ngdsupfiles);//1.NGF_FILEPATH; 2.NGF_FILENAME; 3.NGF_RAW; 4.NGF_FILESIZE
			    		
			    		qcpath = result.getString(1);
			    	}
				    
			    }
			    while(result.next());

			    ngdsupfiles=new SupplementaryFiles();
			    ngdsupfiles.setFilename("index.html");
			    qFiles.add(ngdsupfiles);
			    
			    
			    if (0 == pFiles.size())
			    	supplementaryFiles.setProcessedFile(null);
			    else 
			    	supplementaryFiles.setProcessedFile(pFiles);
			    
			    if (0 == rFiles.size())
			    	supplementaryFiles.setRawFile(null);
			    else 
			    	supplementaryFiles.setRawFile(rFiles);

			    if (0 == qFiles.size())
			    	supplementaryFiles.setQCFile(null);
			    else 
			    	supplementaryFiles.setQCFile(qFiles);
			    
			    return supplementaryFiles;
			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}        
        return supplementaryFiles;
    }
    
    //sample, series, platform/library data for array/seq
    public ArraySeqTableBeanModel getArraySeqData(int oid){
    	ArraySeqTableBeanModel arraySeqTableBeanModel = null;
    	
    	//arraySeqTableBeanModel = new ArraySeqTableBeanModel();
    	
    	return arraySeqTableBeanModel;
    }
    
    public SampleModel getSampleData(int oid) {
    	SampleModel sampleModel=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SINGLE_SAMPLE); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				sampleModel=new SampleModel();
				sampleModel.setGeoID(result.getString("geo_sample_id"));
			    sampleModel.setTitle(result.getString("title"));
			    sampleModel.setSource(result.getString("source"));
			    sampleModel.setOrganism(result.getString("organism"));
			    sampleModel.setStrain(result.getString("strain"));
			    sampleModel.setMutation(result.getString("mutation"));
			    sampleModel.setSex(result.getString("sex"));
			    sampleModel.setDevAge(result.getString("dev_stage"));
			    sampleModel.setTheilerStage(result.getString("stage"));
			    sampleModel.setdissectionMethod(result.getString("dissect_method"));
			    sampleModel.setMolecule(result.getString("molecule"));
			    sampleModel.setA_260_280(result.getString("a260"));
			    sampleModel.setExtractionProtocol(result.getString("extract_protocol"));
			    sampleModel.setAmplificationKit(result.getString("manufacturer"));
			    sampleModel.setAmplificationProtocol(result.getString("amp_protocol"));
			    sampleModel.setAmplificationRounds(result.getString("rounds"));
			    sampleModel.setVolTargHybrid(result.getString("tgt_hyb"));
			    sampleModel.setLabel(result.getString("label"));
			    sampleModel.setWashScanHybProtocol(result.getString("hyb_protocol"));
			    sampleModel.setGcosTgtVal(result.getString("gcos"));
			    sampleModel.setDataAnalProtocol(result.getString("data_ana_method"));
			    sampleModel.setReference(result.getString("reference"));
			    sampleModel.setDescription(result.getString("description"));
			    sampleModel.setExperimentalDesign(result.getString("design"));
			    sampleModel.setLabelProtocol(result.getString("label_protocol"));
			    sampleModel.setScanProtocol(result.getString("scan_protocol"));
			    sampleModel.setPoolSize(result.getString("pool_size"));
			    sampleModel.setPooledSample(result.getString("pooled_sample"));
			    sampleModel.setDevelopmentalLandmarks(result.getString("landmark"));

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	if (sampleModel != null) { 
    		try
    		{
    			con = Globals.getDatasource().getConnection();
    			ps = con.prepareStatement(ArrayQueries.SAMPLE_ANATOMY); 
    			ps.setInt(1, oid);
    			result =  ps.executeQuery();
    			if (result.first()) {
    				result.beforeFirst();
    				String anatomySource = "";
    				ArrayList<String> anatomySourceList = new ArrayList<String>();
    				while (result.next()) { // it's possible it's expressed in more than one component 
    				    anatomySource = result.getString(1);
    				    if (!anatomySourceList.contains(anatomySource))
    				    		anatomySourceList.add(anatomySource);
    				}
    				anatomySource = "";
    				for(String s: anatomySourceList)
    					anatomySource += s + ";";
    				if (anatomySource.trim().length() != 0) { // xingjun - 18/11/2009
    				    sampleModel.setSource(anatomySource);
    				}    				
    			}						
    		}
    		catch(SQLException sqle){sqle.printStackTrace();}
    		finally {
    		    Globals.closeQuietly(con, ps, result);
    		}
	    }
    	
    	return sampleModel;
    }
    
    public SeqSampleModel getSeqSampleData(int oid) {
    	SeqSampleModel seqSampleModel=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(SequenceQueries.SEQUENCE_SINGLE_SAMPLE); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				seqSampleModel=new SeqSampleModel();
				seqSampleModel.setGeoID(result.getString(1));
				seqSampleModel.setDescription(result.getString(2));
				seqSampleModel.setTitle(result.getString(3));
				seqSampleModel.setOrganism(result.getString(4));
				seqSampleModel.setStrain(result.getString(5));
				seqSampleModel.setGenotype(result.getString(6));
				seqSampleModel.setSex(result.getString(7));
				seqSampleModel.setDevAge(result.getString(8));
				seqSampleModel.setTheilerStage(result.getString(9));
				seqSampleModel.setPooledSample(result.getString(10));
				seqSampleModel.setPoolSize(result.getString(11));
				seqSampleModel.setExperimentalDesign(result.getString(12));
				seqSampleModel.setSampleNotes(result.getString(13));
				seqSampleModel.setLibraryPoolSize(result.getString(14));
				seqSampleModel.setLibraryReads(result.getString(15));
				seqSampleModel.setReadLength(result.getString(16));
				seqSampleModel.setMeanInsertSize(result.getString(17));

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	
    	return seqSampleModel;
    }
    
    public SeriesModel getSeriesData(int oid) {
    	
    	SeriesModel seriesModel=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SINGLE_SERIES); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				seriesModel=new SeriesModel();
				seriesModel.setGeoID(result.getString("geo_series_id"));
				seriesModel.setTitle(result.getString("title"));
				seriesModel.setSummary(result.getString("summary"));
				seriesModel.setType(result.getString("type"));
				seriesModel.SetDesign(result.getString("overall_design"));
				seriesModel.setOid(result.getInt("series_oid"));
				

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	if(seriesModel!=null) {
    		
    		try
    		{
    			con = Globals.getDatasource().getConnection();
    			ps = con.prepareStatement(ArrayQueries.ARRAY_NUM_SAMPLES); 
    			ps.setInt(1, oid);
    			result =  ps.executeQuery();
    			if (result.first()) {
    				seriesModel.setNumSamples(result.getInt("total"));
    			}						
    		}
    		catch(SQLException sqle){sqle.printStackTrace();}
    		finally {
    		    Globals.closeQuietly(con, ps, result);
    		}
    	}
    	
    	return seriesModel;
    }
    
public SeqSeriesModel getSeqSeriesData(int oid) {
    	
	SeqSeriesModel seqSeriesModel=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(SequenceQueries.SEQUENCE_SINGLE_SERIES); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				seqSeriesModel=new SeqSeriesModel();
				seqSeriesModel.setGeoID(result.getString("geo_series_id"));
				seqSeriesModel.setTitle(result.getString("title"));
				seqSeriesModel.setSummary(result.getString("summary"));
				seqSeriesModel.setCreated_by(result.getInt("created_by"));
				seqSeriesModel.SetDesign(result.getString("overall_design"));
				seqSeriesModel.setOid(result.getInt("series_oid"));
				

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	if(seqSeriesModel!=null) {
    		
    		try
    		{
    			con = Globals.getDatasource().getConnection();
    			ps = con.prepareStatement(SequenceQueries.SEQUENCE_NUM_SAMPLES);
    			ps.setInt(1, oid);
    			result =  ps.executeQuery();
    			if (result.first()) {
    				seqSeriesModel.setNumSamples(result.getInt("total"));
    			}						
    		}
    		catch(SQLException sqle){sqle.printStackTrace();}
    		finally {
    		    Globals.closeQuietly(con, ps, result);
    		}
    	}
    	
    	return seqSeriesModel;
    }
    
    public PlatformModel getPlatformData(int oid) {
    	PlatformModel platformModel=null;
    	
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SINGLE_PLATFORM); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				platformModel=new PlatformModel();
				platformModel.setGeoID(result.getString("geo_platform_id"));
				platformModel.setTitle(result.getString("title"));
				platformModel.setName(result.getString("name"));
				platformModel.setDistribution(result.getString("distribution"));
				platformModel.setTechnology(result.getString("technology"));
				platformModel.setOrganism(result.getString("organism"));
				platformModel.setManufacturer(result.getString("manufacturer"));
				platformModel.setManufactureProtocol(result.getString("protocol"));
				platformModel.setCatNo(result.getString("catno"));
				

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return platformModel;
    }
    
    public ArrayList<ImageInfoModel> getArraySeqImages(int oid, String assayType) {
    	ArrayList<ImageInfoModel> image_list = null;
    	
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SEQ_IMAGES); 
			ps.setInt(1, oid);
			ps.setString(2, assayType);
			result =  ps.executeQuery();
			if (result.first()) {
				image_list = new ArrayList<ImageInfoModel>();
				result.beforeFirst();
				int serialNo = 1;
				//int dotPosition = 0;
				//String fileExtension = null;
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
				    	imageInfoModel.setClickFilePath(str);
				    
				    // do not know the reason zoom_viewer does not work for microarray
				    // even though microarray images have tif. So
				    // use specimen type to mark microarray image
				    imageInfoModel.setSpecimenType(assayType);
				    imageInfoModel.setSerialNo(""+serialNo);
		
				    serialNo++;
				    image_list.add(imageInfoModel);
				}

			}						
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return image_list;
    }
    
    
    
    public String getTissue(int oid) {
    	String tissue=null;
    	
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_TISSUE); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				tissue = result.getString(1);
	        }					
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	
    	return tissue;
    }
    
    public DataProcessing[] getDataProcessing(int oid) {
    	ArrayList<DataProcessing> dataProcessingList = new ArrayList<DataProcessing>();
    	DataProcessing dataProcessing=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(SequenceQueries.SEQUENCE_DATA_PROCESSING); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
		 	    while (result.next()) {
					dataProcessing = new DataProcessing();
					dataProcessing.setProStep(result.getString(1));
					dataProcessing.setBuild(result.getString(2));
					dataProcessing.setAlignedGenome(result.getString(3));
					dataProcessing.setUnalignedGenome(result.getString(4));
					dataProcessing.setRnaReads(result.getString(5));
					dataProcessing.setFiveThreeRatio(result.getString(6));
					dataProcessing.setFormatContent(result.getString(7));				
					dataProcessing.setNumberOfReads(result.getString(8));
					dataProcessing.setBeforeCleanUpReads(result.getString(9));
					dataProcessing.setPairedEnd(result.getString(10));
					dataProcessing.setFilename(result.getString(11));
					dataProcessing.setFiletype(result.getString(12));
					dataProcessing.setRawOrProcessed(result.getString(13));
					
					dataProcessingList.add(dataProcessing);
		 	    }
	        }					
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	if (0 == dataProcessingList.size())
		    return null;
    	
    	return (DataProcessing[])dataProcessingList.toArray(new DataProcessing[0]);
    }
    
   public SeqProtocol getSequenceProtocol(int oid) {
	   SeqProtocol seqProtocol=null;
	   try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(SequenceQueries.SEQUENCE_PROTOCOL); 
			ps.setInt(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				 seqProtocol = new SeqProtocol();
				 seqProtocol.setName(result.getString(1));
				 seqProtocol.setLibraryConProt(result.getString(2));
				 seqProtocol.setLibraryStrategy(result.getString(3));
				 seqProtocol.setExtractedMolecule(result.getString(4));
				 seqProtocol.setRnaIsolationMethod(result.getString(5));
				 seqProtocol.setSequencingMethod(result.getString(6));
				 seqProtocol.setLabelMethod(result.getString(7));
				 seqProtocol.setInstrumentModel(result.getString(8));
				 seqProtocol.setPairedEnd(result.getString(9));
				 seqProtocol.setProtAdditionalNotes(result.getString(10));
				 seqProtocol.setDnaExtractMethod(result.getString(11));
				 seqProtocol.setAntibody(result.getString(12));
				 seqProtocol.setCreatedBy(result.getString(13));
			}		
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
	   return seqProtocol;
   }

}
