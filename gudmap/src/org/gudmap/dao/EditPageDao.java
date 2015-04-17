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
import org.gudmap.models.EditPageModel;
import org.gudmap.queries.generic.WebPageQueries;

public class EditPageDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArrayList<EditPageModel> editPageList = null;
	
	public EditPageDao() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/Gudmap_jdbcResource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<EditPageModel> retrievePage(String oid) {
		if(oid==null || oid.equals(""))
			return null;
		
		String queryString=WebPageQueries.GET_WHOLE_PAGE;
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				ps.setString(1, oid);
				result =  ps.executeQuery();
				if (result.first()){
					editPageList = new ArrayList<EditPageModel>();
					EditPageModel editPageModel = new EditPageModel();
					editPageModel.setOid(result.getString("oid"));
					editPageModel.setTitle(result.getString("title"));
					editPageModel.setHash(result.getString("hash"));
					editPageModel.setAlias(result.getString("alias"));
					editPageModel.setContent_1(result.getString("content_1"));
					editPageModel.setContent_2(result.getString("content_2"));
					editPageModel.setContent_3(result.getString("content_3"));
					editPageModel.setContent_4(result.getString("content_4"));
					
					editPageList.add(editPageModel);
				}
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			} 
			      
        return editPageList;
	}


}
