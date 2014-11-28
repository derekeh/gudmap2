package org.gudmap.globals;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

//@ManagedBean (eager=true)
//@ApplicationScoped
public class Globals {
	public Globals(){
		
	}
	
	public static void closeQuietly(Connection connection, PreparedStatement statement, ResultSet resultSet) {
	    if (resultSet != null) try { resultSet.close(); } catch (SQLException logOrIgnore) {}
	    if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
	    if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}
	}
	
	public static final String[]IshColTotals={"ASSAY_TYPE_TOTAL_GENE","ASSAY_TYPE_TOTAL_GUDMAP_ACCESSION","ASSAY_TYPE_TOTAL_SOURCE","ASSAY_TYPE_TOTAL_SUBMISSION_DATE","ASSAY_TYPE_TOTAL_ASSAY_TYPE",
			"ASSAY_TYPE_TOTAL_PROBE_NAME","ASSAY_TYPE_TOTAL_EMBRYO_STAGE","ASSAY_TYPE_TOTAL_AGE","ASSAY_TYPE_TOTAL_SEX","ASSAY_TYPE_TOTAL_GENOTYPE",
			"ASSAY_TYPE_TOTAL_TISSUE","ASSAY_TYPE_TOTAL_EXPRESSION","ASSAY_TYPE_TOTAL_SPECIMEN_TYPE","ASSAY_TYPE_TOTAL_IMAGES"};
	
	public static final String[]TgColTotals={"TG_TYPE_TOTAL_GENE","TG_TYPE_TOTAL_GUDMAP_ACCESSION","TG_TYPE_TOTAL_SOURCE","TG_TYPE_TOTAL_SUBMISSION_DATE","TG_TYPE_TOTAL_ASSAY_TYPE",
			"TG_TYPE_TOTAL_PROBE_NAME","TG_TYPE_TOTAL_EMBRYO_STAGE","TG_TYPE_TOTAL_AGE","TG_TYPE_TOTAL_SEX","TG_TYPE_TOTAL_GENOTYPE",
			"TG_TYPE_TOTAL_TISSUE","TG_TYPE_TOTAL_EXPRESSION","TG_TYPE_TOTAL_SPECIMEN_TYPE","TG_TYPE_TOTAL_IMAGES"};

}
