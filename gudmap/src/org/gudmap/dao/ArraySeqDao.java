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
import org.gudmap.models.submission.PlatformModel;
import org.gudmap.models.submission.SampleModel;
import org.gudmap.models.submission.SeriesModel;
import org.gudmap.queries.anatomy.AnatomyQueries;
import org.gudmap.queries.array.ArrayQueries;

public class ArraySeqDao {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public ArraySeqDao() {
		
	}
	
    public SupplementaryFiles findSupplementaryFileInfoBySubmissionId(String oid) {
		
		if (oid == null) {
		    return null;
		}		
		SupplementaryFiles supplementaryFiles = null;		
        try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(ArrayQueries.ARRAY_SUPPLEMENTARY_FILES); 
			ps.setString(1, oid);
			result =  ps.executeQuery();
			if (result.first()) {
				result.beforeFirst();
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
    public ArraySeqTableBeanModel getArraySeqData(String oid){
    	if (oid == null) {
		    return null;
		}
    	ArraySeqTableBeanModel arraySeqTableBeanModel = new ArraySeqTableBeanModel();
    	
    	return arraySeqTableBeanModel;
    }
    
    public SampleModel getSampleData() {
    	SampleModel sampleModel=new SampleModel();
    	
    	return sampleModel;
    }
    
    public SeriesModel getSeriesData() {
    	SeriesModel seriesModel=new SeriesModel();
    	
    	return seriesModel;
    }
    
    public PlatformModel getPlatformData() {
    	PlatformModel platformModel=new PlatformModel();
    	
    	return platformModel;
    }
    
  

}
