package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.globals.Globals;
import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.SupplementaryFiles;
import org.gudmap.models.submission.GeneModel;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.models.submission.PlatformModel;
import org.gudmap.models.submission.SampleModel;
import org.gudmap.models.submission.SeriesModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.array.ArrayQueries;
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
    				while (result.next()) { // it's possible it's expressed in more than one component 
    				    anatomySource += result.getString(1) + "; ";
    				}
    				anatomySource = anatomySource.substring(0, anatomySource.length()-2);
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
    
    public ArrayList<ImageInfoModel> getMicroarrayImages(int oid) {
    	ArrayList<ImageInfoModel> image_list = null;
    	
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_IMAGES); 
			ps.setInt(1, oid);
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
				    imageInfoModel.setSpecimenType("microarray");
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
    
  

}
