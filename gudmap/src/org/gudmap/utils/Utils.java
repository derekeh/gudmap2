package org.gudmap.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Utils {
	//remove last character from string
	public static String removeLastChar(String str, char lastchar) {
	    if (str.length() > 0 && str.charAt(str.length()-1)==lastchar) {
	      str = str.substring(0, str.length()-1);
	    }
	    return str;
	}
	//remove WHERE from whereclause (7 chars) and replace with AND and remove last AND
	public static String removeWhere(String whereclause, String wherestring) {
		String str=whereclause;
	    if (str.length() > 0 && str.startsWith(wherestring)) {
	      str = " AND " + whereclause.substring(7);
	      str=str.substring(0,str.lastIndexOf("AND"));
	      
	    }
	    return str;
	}
	
	public static String netTrim(String input) {
		String ret = input;
		if (null != ret) {
			  ret = ret.trim();
			  if (ret.equals(""))
				ret = null;
		}
			
		return ret;
	}
	
	public static ResourceBundle getConfigBundle() {
		return ResourceBundle.getBundle("org.gudmap.bundles.config");
	}
	
	public static String[] formatResultSetToStringArray(ResultSet resSet) throws SQLException {
		String str = null;
	    	if (resSet.first()) {
	    		resSet.beforeFirst();
				ArrayList<String> results = new ArrayList<String>();
	    		while (resSet.next()) {
			    str = netTrim(resSet.getString(1));
				if (null != str)
				    results.add(str);
	    		}
	    		if (results != null && results.size() > 0) {
	    			return results.toArray(new String[0]);
	    		}
	    	}
			return null;
	}
	
public static ArrayList<String[]> formatResultSetToArrayList(ResultSet resSet) throws SQLException {
		
		ResultSetMetaData resSetData = resSet.getMetaData();
		int columnCount = resSetData.getColumnCount();
		
		if (resSet.first()) {
			
			//need to reset cursor as 'if' move it on a place
			resSet.beforeFirst();
			
			//create ArrayList to store each row of results in
			ArrayList<String[]> results = new ArrayList<String[]>();
			int i = 0;
			String[] columns = null;
			String str = null;
			while (resSet.next()) {
				columns = new String[columnCount];
				for (i = 0; i < columnCount; i++) {
					str = resSet.getString(i + 1);
					if (null == str)
					    str = "";
					else {
					    str = str.trim();
					    if (str.equalsIgnoreCase("null"))
						str = "";
					}

					columns[i] = str;
				}
				results.add(columns);
			}
			return results;
			
		}
		return null;
	}

}
