package org.gudmap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.gudmap.globals.Globals;
import org.gudmap.models.CollectionInfoModel;
import org.gudmap.queries.collections.CollectionQueries;
import org.gudmap.utils.Utils;

public class CollectionDao {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private CollectionInfoModel collectionInfoModel;
	
	public CollectionDao() {
		/*try
		{
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			result =  ps.executeQuery();
			if (result.first()) {
				collectionInfoModel = new CollectionInfoModel();
			}
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}*/
	}
	
	public ArrayList getCollections(int userId,int collectionType, int status,
			int column, boolean ascending, int offset, int num) {
		// TODO Auto-generated method stub
		
		if (userId < 0 || collectionType < 0 || status < 0) { // if the userId is invalid
			return null;
		}
		
		String queryString="";
        ArrayList resultList = null;
        String defaultOrder = CollectionQueries.COLLECTION_DEFAULT_ORDER_BY_COL;
        
        if (status == 0) { // own collection required
        	queryString = CollectionQueries.COLLECTION_BROWSE_EXCLUSIVE;
        } else if (status == 1) { // others's shared collection required
        	queryString = CollectionQueries.COLLECTION_BROWSE_OTHERS;
        } else if (status == 2) { // both own and others' shared collection required
        	queryString = CollectionQueries.COLLECTION_BROWSE_ALL;
        }
        
        queryString = assembleCollectionQueryString(queryString, defaultOrder, column, ascending, offset, num);
        
        try
		{
			con=Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString); 
			if (status == 0 || status == 1) { // own or others' shared collections
				ps.setInt(1, collectionType);
				ps.setInt(2, userId);
            } else if (status == 2) { // both own and others' shared collections
            	ps.setInt(1, collectionType);
            	ps.setInt(2, userId);
            	ps.setInt(3, collectionType);
            	ps.setInt(4, userId);
            }
			result =  ps.executeQuery();
			resultList = Utils.formatResultSetToArrayList(result);
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
        
        return resultList;
		
       //////////////////////////////
        
       
	}
	
	private String assembleCollectionQueryString(String query,
			String defaultOrder, int columnIndex, boolean ascending, int offset, int num) {
		
		String queryString = query + " ORDER BY ";
		if (columnIndex != -1) {
			
			// translate parameters into database column names
			String column =
				getCollectionBrowseOrderByColumn(columnIndex, ascending, defaultOrder);
			
			queryString += column;
			
		} else { // if don't specify order by column, order by gene symbol ascend by default
			queryString += defaultOrder;
		}
		
		// offset and retrieval number
		queryString += (offset==0 && num==0)?"":" LIMIT " + offset + ", " + num;
		
		// return assembled query string
		return queryString;
	}
	
	private String getCollectionBrowseOrderByColumn(int columnIndex,
			boolean ascending, String defaultOrder) {
		
		String column = new String("");
		String order = (ascending == true ? "ASC" : "DESC");
		
		// start to translate
		if (columnIndex == 0) { // name DEREK WAS 1
			column += defaultOrder + order;
		} else if (columnIndex == 2) { // description
			column = "CLN_DESCRIPTION " + order + ", " + defaultOrder;
		} else if (columnIndex == 4) { // owner
			column += "USR_UNAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 5) { // focus group
//			column += "CLN_FOCUS_GROUP " + order + ", " + defaultOrder;
			column += "CLN_FOCUS_GROUP_NAME " + order + ", " + defaultOrder;
		} else if (columnIndex == 6) { // entries
			column += "CLN_NUMBER " + order + ", " + defaultOrder;
		} else if (columnIndex == 7) { // status
			column += "CLN_STATUS " + order + ", " + defaultOrder;
		} else if (columnIndex == 8) { // last modified
			column += "CLN_LAST_UPDATED " + order + ", " + defaultOrder;
		} else if (columnIndex == 9) { // type
			column += "CLN_TYPE " + order + ", " + defaultOrder;
		} else { // order by default - name
			column = defaultOrder;
		}
		return column;
	}

}
