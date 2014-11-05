package org.gudmap.beans.utils;

public class Utils {
	//remove last character from string
	public static String removeLastChar(String str, char lastchar) {
	    if (str.length() > 0 && str.charAt(str.length()-1)==lastchar) {
	      str = str.substring(0, str.length()-1);
	    }
	    return str;
	}
	//remove where from whereclause 7 chars
	public static String removeWhere(String whereclause, String wherestring) {
		String str=whereclause;
	    if (str.length() > 0 && str.startsWith(wherestring)) {
	      str = " AND " + whereclause.substring(7);
	      str=str.substring(0,str.lastIndexOf("AND"));
	      
	    }
	    return str;
	}

}
