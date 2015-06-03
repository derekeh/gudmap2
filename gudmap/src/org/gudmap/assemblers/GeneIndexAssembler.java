package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


import org.gudmap.globals.Globals;
import org.gudmap.queries.genestrip.GeneIndexQueries;

public class GeneIndexAssembler {
	
	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	public GeneIndexAssembler() {
		
	}
	
	public Object[][] getGeneIndex(String prefix, String focusGroup) {
		// TODO Auto-generated method stub
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		Object[][] resultObject = null;
		try
		{
				con = Globals.getDatasource().getConnection();
				ps = con.prepareStatement(GeneIndexQueries.getISHGeneIndex(prefix, focusGroup));
				result =  ps.executeQuery();
				
				list = formatBrowseSeriesResultSet(result);
				if(null != list) {
					resultObject = new Object[list.size()][];
					for(int i = 0; i < list.size(); i++) {
						resultObject[i] = (Object[])list.get(i);						
					}				
				}
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
			    Globals.closeQuietly(con, ps, result);
		}
		return resultObject;
		
	}
	
	 private ArrayList <String[]>formatBrowseSeriesResultSet(ResultSet resSet) throws SQLException {
		
   		ArrayList<String[]> results = null;
		ResultSetMetaData resSetMetaData = resSet.getMetaData();
   		int columnCount = resSetMetaData.getColumnCount();

		if (resSet.first()) {
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			results = new ArrayList<String[]>();
			
			while(resSet.next()) {
				String[] columns = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resSet.getString(i + 1);
		        }
		        results.add(columns);
			}
		}
		return results;
	}

}
