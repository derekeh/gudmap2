package org.gudmap.dao;

import java.security.NoSuchAlgorithmException;
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
import org.gudmap.utils.Utils;

public class EditPageDao {
	
	private DataSource ds;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private ArrayList<EditPageModel> editPageList = null;
	private int insertID=-1;
	
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
					editPageModel.setOid(result.getInt("oid"));
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
	
	public String updatePage(String oid, String title, String content) {
		String RET="Update failed.";
		int status;
		String contentCol = "ZWE_CONTENT_1";
		String queryString=WebPageQueries.UPDATE_CONTENT_AND_TITLE;
		String sql=String.format(queryString,contentCol);
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(sql); 
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setString(3, oid);
			status =  ps.executeUpdate();
			if(status>0)
				RET=Utils.getDateToday();
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		return RET;
	}
	
	public String createPage(String alias, String title, String category, int level, String value, int userID)  throws NoSuchAlgorithmException {
		String RET="Create new page failed.";
		int status;
		int last_increment=0;
		String queryString=WebPageQueries.GET_MAX_INCREMENT;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result=ps.executeQuery();
			if(result.next())
				last_increment=result.getInt(1)+10;
			else
				return RET;
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		
		queryString=WebPageQueries.INSERT_NEW_PAGE;
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			ps.setInt(1, last_increment);
			ps.setString(2, Utils.getSha1(String.valueOf(last_increment)));
			ps.setString(3, alias);
			ps.setString(4, title);
			ps.setString(5, category);
			ps.setInt(6, level);
			ps.setString(7, value);
			ps.setString(8, null);
			ps.setString(9, null);
			ps.setString(10, null);
			ps.setInt(11, userID);
			status =  ps.executeUpdate();
			if(status>0) {
				RET="Page created successfully " +Utils.getDateToday();
				queryString=WebPageQueries.GET_LAST_PAGE_INSERT;
				ps=con.prepareStatement(queryString);
				result=ps.executeQuery();
				result.next();
				insertID=result.getInt(1);
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		return RET;
	}
	
	public ArrayList<String> getAliasList() {
		String queryString=WebPageQueries.GET_ALIASES;
		ArrayList<String> aliasList = new ArrayList<String>();
		try
		{
			con = ds.getConnection();
			ps = con.prepareStatement(queryString); 
			result=ps.executeQuery();
			while(result.next()){
				aliasList.add(result.getString(1));
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
		
		return aliasList;
		
	}
	
	public int getLastInsert() {		
		return insertID;
	}
	
	public ArrayList<EditPageModel> getPageList() {
			String queryString=WebPageQueries.GET_PAGE_LIST;
			try
			{
				con = ds.getConnection();
				ps = con.prepareStatement(queryString); 
				result =  ps.executeQuery();
				if (result.first()){
					editPageList = new ArrayList<EditPageModel>();
					result.beforeFirst();
					while(result.next()) {
						EditPageModel editPageModel = new EditPageModel();
						editPageModel.setOid(result.getInt("oid"));
						editPageModel.setTitle(result.getString("title"));
						editPageModel.setHash(result.getString("hash"));
						editPageModel.setAlias(result.getString("alias"));
						editPageModel.setCategory(result.getString("category"));
						editPageModel.setLevel(result.getInt("level"));
						editPageModel.setIsVisible(result.getString("isvisible"));
						editPageModel.setModifiedDate(result.getString("modifiedDate"));
						editPageModel.setUsername(result.getString("username"));
						
						editPageList.add(editPageModel);
					}
				}
				
			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
			    Globals.closeQuietly(con, ps, result);
			} 
			      
        return editPageList;
	}

}
