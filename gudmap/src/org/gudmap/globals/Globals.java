package org.gudmap.globals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.gudmap.queries.generic.GenericQueries;

public class Globals {
	public Globals(){
		
	}
	
	public static DataSource getDatasource() {
		DataSource ds=null;
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	public static String getParameterValue (String paramName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext.getExternalContext().getRequestParameterMap().get(paramName)!=null) 
			return facesContext.getExternalContext().getRequestParameterMap().get(paramName);
		
		return null;
	}
	
	public static void setParameterValue (String key, Object value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().getRequestMap().put(key,value);
		
	}
	
	public static Object getSessionValue(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}
	
	public static void setSessionValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
	}
	
	public static String getRefererPage() {
		String refererURI = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer"); 
		if (refererURI==null)
			return "database_homepage.jsp";		
		else
			return refererURI;
	}
	
	public static String getDevAge(String oid) {
		String RET="";
		 if(oid.equals("1"))
			 RET="0";
		 else if (oid.equals("15"))
			 RET="9.5 dpc";
		 else if (oid.equals("16"))
				 RET="10 dpc";
		 else if (oid.equals("20"))
			 RET="12.5 dpc";
		 else if (oid.equals("21"))
			 RET="13 dpc";
		 else if (oid.equals("22"))
			 RET="14.5 dpc";
		 else if (oid.equals("23"))
			 RET="15 dpc";
		 else if (oid.equals("27"))
			 RET="Newborn";
		 else if (oid.equals("28"))
			 RET="Post Natal Adult";
		return RET;
	}
	
	/*public static String getFacesRequestParamValue(String key) {
		Object value = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);	
		if (value==null)
			return null;
		return String.valueOf(value);	
	}
	
	
	public static void setFacesRequestParamValue(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);	
	}*/
	
	
	public static final String domainUrl="http://www.gudmap.org/";
    public static final String appUrl=domainUrl+"gudmap/";
    public static final String applicationRoot="/gudmap/";
    public static final String imagePath="/opt/MAWWW/Public/html/Appfiles/images/";
    public static final String webfilePath="/opt/MAWWW/Public/html/Appfiles/docs/";
    public static final String heatmapPath="/opt/MAWWW/Public/html/Appfiles/heatmaps/";
    public static final String seqDataPath="ngsData/";

    
	
	public static void closeQuietly(Connection connection, PreparedStatement statement, ResultSet resultSet) {
	    if (resultSet != null) try { resultSet.close(); } catch (SQLException logOrIgnore) {}
	    if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
	    if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}
	}
	
	public static final String[]IshColTotals={"ASSAY_TYPE_TOTAL_GENE","ASSAY_TYPE_TOTAL_GUDMAP_ACCESSION","ASSAY_TYPE_TOTAL_SOURCE","ASSAY_TYPE_TOTAL_SUBMISSION_DATE","ASSAY_TYPE_TOTAL_ASSAY_TYPE",
			"ASSAY_TYPE_TOTAL_PROBE_NAME","ASSAY_TYPE_TOTAL_EMBRYO_STAGE","ASSAY_TYPE_TOTAL_SPECIES","ASSAY_TYPE_TOTAL_AGE","ASSAY_TYPE_TOTAL_SEX","ASSAY_TYPE_TOTAL_GENOTYPE",
			"ASSAY_TYPE_TOTAL_TISSUE","ASSAY_TYPE_TOTAL_EXPRESSION","ASSAY_TYPE_TOTAL_SPECIMEN_TYPE","ASSAY_TYPE_TOTAL_IMAGES"};
	
	public static final String[]TgColTotals={"TG_TYPE_TOTAL_GENE","TG_TYPE_TOTAL_GUDMAP_ACCESSION","TG_TYPE_TOTAL_SOURCE","TG_TYPE_TOTAL_SUBMISSION_DATE","TG_TYPE_TOTAL_ASSAY_TYPE",
			"TG_TYPE_TOTAL_PROBE_NAME","TG_TYPE_TOTAL_EMBRYO_STAGE","TG_TYPE_TOTAL_SPECIES","TG_TYPE_TOTAL_AGE","TG_TYPE_TOTAL_SEX","TG_TYPE_TOTAL_GENOTYPE",
			"TG_TYPE_TOTAL_TISSUE","TG_TYPE_TOTAL_EXPRESSION","TG_TYPE_TOTAL_SPECIMEN_TYPE","TG_TYPE_TOTAL_IMAGES"};
	
	public static final String[]MicSeriesColTotals={"MIC_SERIES_TOTAL_GEO_ID","MIC_SERIES_TOTAL_SOURCE","MIC_SERIES_TOTAL_PLATFORM"};
	
	public static final String[]SeqSeriesColTotals={"MIC_SERIES_TOTAL_GEO_ID","MIC_SERIES_TOTAL_SOURCE","MIC_SERIES_TOTAL_PLATFORM"};
	
    //************************* gene strip *******************************
	private static final int defaultExpressionProfileBarHeight = 50;
    
    public static int getDefaultExpressionProfileBarHeight() {
    	return defaultExpressionProfileBarHeight;
    }
    
   /* final static public String[] interestedStructures = {
		  "Mesonephros (all parts, all stages)", 
		  "Metanephros",
		  "Lower urinary tract",
		  "Early reproductive system",
		  "Male reproductive system",
		  "Female reproductive system", 
		  "Others" // items down from this item will not be implemented for time being - 13/11/2008
	};*/
    
    static public String[] focusGroups={"NA","Metanephros","Early reproductive system","Male reproductive system","Female reproductive system","Lower urinary tract",
    									"Mesonephros"};
    	
    
	  
	final static public String[] getInterestedAnatomyStructureIds () {
		  return new String[] {"6", "1", "5", "2", "3", "4"};
	}
	
	
	
	 final static public Hashtable<String, String[]> getEMAPID(){
			Hashtable<String, String[]> lookupsection = new Hashtable<String,String[]>();
			lookupsection.put("1",new String[]{// metanephros (11)
					"EMAP:3233","EMAP:3841","EMAP:4587",
					"EMAP:5504","EMAP:6674","EMAP:8226","EMAP:9536","EMAP:10896","EMAP:12256",
					"EMAP:29491","EMAP:30247"}); 
			lookupsection.put("2",new String[]{// early reproductive system (7)
//					 removed by xingjun - 12/01/2009 -required by editors - dont have relevant data
//					"EMAP:1958", "EMAP:1436", "EMAP:1961", 
					"EMAP:3850", "EMAP:2572",
					"EMAP:3226","EMAP:3851",
					"EMAP:2576","EMAP:3229", "EMAP:27645"}); 
			lookupsection.put("3",new String[]{// Male Reproductive System (9)
					"EMAP:28873","EMAP:5532","EMAP:6705","EMAP:8257",
					"EMAP:8443","EMAP:9803","EMAP:11163", "EMAP:29348","EMAP:30157"}); 
			lookupsection.put("4",new String[]{// Female Reproductive System (9)
					"EMAP:28872","EMAP:5523","EMAP:6694","EMAP:8245",
					"EMAP:9557","EMAP:10917","EMAP:12277","EMAP:29396","EMAP:30203"}); 
			lookupsection.put("5",new String[]{// Lower Urinary Tract (39)
					"EMAP:6668", "EMAP:8219","EMAP:9528",
					"EMAP:10888","EMAP:12248","EMAP:29457",
					"EMAP:30374","EMAP:28749","EMAP:28750","EMAP:28751",
					"EMAP:28752","EMAP:29443","EMAP:30418","EMAP:28556",
					"EMAP:30898","EMAP:27572","EMAP:3237","EMAP:3846",
					"EMAP:4593","EMAP:5516",
					// added by xingjun as required by Editors - 04/12/2007
					"EMAP:3238","EMAP:3847","EMAP:4594","EMAP:5517","EMAP:6689","EMAP:2575",
					"EMAP:3239","EMAP:3848","EMAP:4595","EMAP:5521","EMAP:6692","EMAP:8243",
					"EMAP:9553","EMAP:10913","EMAP:12273","EMAP:30416","EMAP:29475",
					"EMAP:30902", "EMAP:31643"});

			// added by xingjun - used for in situ expression profile display - 17/11/2008
			lookupsection.put("6", new String[]{// Mesonephros (11 all parts, all stages)
					"EMAP:2576", "EMAP:3229", "EMAP:27645",
					// added by xingjun - 12/01/2009 - suggested by editors
					"EMAP:29139", "EMAP:28910", "EMAP:29140", "EMAP:28911", "EMAP:29141",
					"EMAP:28912", "EMAP:29142", "EMAP:28913"});

			return lookupsection;
	  }

	  final static public Hashtable getEMAPAID(){
			Hashtable<String, String[]> lookupsection = new Hashtable<String,String[]>();
			lookupsection.put("1",new String[]{// metanephros (11)
					"EMAPA:17373"}); 
			lookupsection.put("2",new String[]{// early reproductive system (7)
					"EMAPA:16744","EMAPA:16857","EMAPA:27644","EMAPA:17382","EMAPA:17383"}); 
			lookupsection.put("3",new String[]{// Male Reproductive System (9)
					"EMAPA:17968"}); 
			lookupsection.put("4",new String[]{// Female Reproductive System (9)
					"EMAPA:17959"}); 
			lookupsection.put("5",new String[]{// Lower Urinary Tract (39)
					"EMAPA:17210","EMAPA:17211","EMAPA:17212","EMAPA:17378","EMAPA:17379",
					"EMAPA:17380","EMAPA:18321","EMAPA:28555","EMAPA:28747","EMAPA:30897",
					"EMAPA:30901","EMAPA:31522"});
			lookupsection.put("6", new String[]{// Mesonephros (11 all parts, all stages)
					"EMAPA:16744","EMAPA:27644", "EMAPA:28909", "EMAPA:29138"});

			return lookupsection;
	  }
	  
	  
	 
}
