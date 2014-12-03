package org.gudmap.utils;

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

}
