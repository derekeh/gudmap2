package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gudmap.globals.Globals;
import org.gudmap.models.CollectionInfoModel;
import org.gudmap.queries.collections.CollectionQueries;

public class CollectionListAssembler {
	
	private String paramSQL;
	private String whereclause;
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	private CollectionInfoModel collectionInfoModel=null;
	private int userId;
	private int collectionType; // 0 - submission collections; 1 - gene collections; 2 - query collections
	private int status; // 0 - own collections; 1 - others' collections; 2 - both
	private String queryString="";
	
	public CollectionListAssembler() {
		if(Globals.getParameterValue("userId")!=null)
			userId = Integer.parseInt(Globals.getParameterValue("userId"));
		if(Globals.getParameterValue("collectionType")!=null)
			collectionType = Integer.parseInt(Globals.getParameterValue("collectionType"));
		if(Globals.getParameterValue("status")!=null)
			status = Integer.parseInt(Globals.getParameterValue("status"));
	}
	
	public CollectionListAssembler(String paramSQL) {
		this.paramSQL = paramSQL;
	}
	
	public List<CollectionInfoModel> getData(int firstRow, int rowCount, String sortField, boolean sortAscending, String whereclause){
		this.whereclause=whereclause;
		String sortDirection = sortAscending ? "ASC" : "DESC";
		
		 if (status == 0) { // own collection required
			 paramSQL = CollectionQueries.COLLECTION_BROWSE_EXCLUSIVE;
	     } 
		 else if (status == 1) { // others's shared collection required
	        	paramSQL = CollectionQueries.COLLECTION_BROWSE_OTHERS;
	     } 
		 else if (status == 2) { // both own and others' shared collection required
	        	paramSQL = CollectionQueries.COLLECTION_BROWSE_ALL;
	     }
		String sql=(status==2)?String.format(paramSQL,whereclause,sortField, sortDirection):String.format(paramSQL,whereclause,whereclause,sortField, sortDirection);
		
		
		List<CollectionInfoModel> list = new ArrayList<CollectionInfoModel>();
		try
		{
		con = Globals.getDatasource().getConnection();
		ps = con.prepareStatement(sql);
		if (status == 0 || status == 1) { // own or others' shared collections
			ps.setInt(1, collectionType);
			ps.setInt(2, userId);
			ps.setInt(3, firstRow);
			ps.setInt(4, rowCount);
			
        } 
		else if (status == 2) { // both own and others' shared collections
        	ps.setInt(1, collectionType);
        	ps.setInt(2, userId);
        	ps.setInt(3, collectionType);
        	ps.setInt(4, userId);
        	ps.setInt(5, firstRow);
			ps.setInt(6, rowCount);
        }
		result =  ps.executeQuery();
		
		//group_concat returning no value will return a null row so don't get those!
		while(result.next()){
			//while(result.next() && result.getString(1)!=null){
			if(result.getString(1)!=null)
			{
			collectionInfoModel=new CollectionInfoModel();
			collectionInfoModel.setId(result.getInt("id"));
			collectionInfoModel.setName(result.getString("name"));
			collectionInfoModel.setDescription(result.getString("description"));
			collectionInfoModel.setOwner(result.getInt("userid"));
			collectionInfoModel.setOwnerName(result.getString("username"));
			collectionInfoModel.setFocusGroup(result.getInt("focusgroupid"));
			collectionInfoModel.setEntries(result.getInt("entitycount"));
			collectionInfoModel.setStatus(result.getInt("status"));
			collectionInfoModel.setLastUpdate(result.getString("modified"));
			collectionInfoModel.setType(result.getInt("type"));
			collectionInfoModel.setFocusGroupName(result.getString("focusgroupname"));

			//collectionInfoModel.setSelected(false);
			list.add(collectionInfoModel);
			}
		}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		Globals.closeQuietly(con, ps, result);
		}
		return list;
		}
		
		public int count() {
			int count=0;
			String countsql = paramSQL.replace("ORDER BY %s %s LIMIT ?,?", "");
			countsql=(status==2)?String.format(countsql,whereclause):String.format(countsql,whereclause,whereclause);
						
			try
			{
					con = Globals.getDatasource().getConnection();
					ps = con.prepareStatement(countsql);
					if (status == 0 || status == 1) { // own or others' shared collections
						ps.setInt(1, collectionType);
						ps.setInt(2, userId);
						
			        } 
					else if (status == 2) { // both own and others' shared collections
			        	ps.setInt(1, collectionType);
			        	ps.setInt(2, userId);
			        	ps.setInt(3, collectionType);
			        	ps.setInt(4, userId);
			        }
					result =  ps.executeQuery();
					
					if (result.first()) {
						result.last();
						count = result.getRow();
		            }

			}
			catch(SQLException sqle){sqle.printStackTrace();}
			finally {
				    Globals.closeQuietly(con, ps, result);
			}		
			return count;
		}
		

}
