package org.gudmap.queries.generic;

public class WebPageQueries {
	
	public final static String GET_ALL_PAGES = "SELECT ZWE_OID, ZWE_ALIAS FROM Z_WEB WHERE ZWE_IS_DELETED=0";
	
	public static String GET_WHOLE_PAGE = "SELECT ZWE_OID oid, ZWE_INC increment, ZWE_HASH hash, ZWE_ALIAS alias, ZWE_TITLE title, ZWE_CONTENT_1 content_1, " +
			"ZWE_CONTENT_2 content_2, ZWE_CONTENT_3 content_3, ZWE_CONTENT_4 content_4 " +
			"FROM Z_WEB WHERE ZWE_OID = ?";
	
	public static String GET_CONTENT_ONLY = "SELECT ZWE_OID, %s " +
			"FROM Z_WEB WHERE ZWE_OID = ?";
	
	public static String INSERT_WHOLE_PAGE = "INSERT INTO Z_WEB (ZWE_INC, ZWE_HASH, ZWE_ALIAS, ZWE_TITLE, ZWE_CONTENT_1, ZWE_CONTENT_2, ZWE_CONTENT_3 ZWE_CONTENT_4) "+
			"VALUES(?,?,?,?,?,?,?,?)";
	
	public static String INSERT_NEW_PAGE = "INSERT INTO Z_WEB (ZWE_INC, ZWE_HASH, ZWE_ALIAS, ZWE_TITLE, ZWE_CATEGORY, ZWE_LEVEL, ZWE_CONTENT_1, ZWE_CONTENT_2, ZWE_CONTENT_3, ZWE_CONTENT_4, ZWE_MODIFIED_BY) "+
			"VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	public static String UPDATE_WHOLE_PAGE = "UPDATE Z_WEB SET ZWE_ALIAS = ?, ZWE_TITLE = ?, ZWE_CONTENT_1 = ?, ZWE_CONTENT_2 = ?, "+
			"ZWE_CONTENT_3 = ?, ZWE_CONTENT_4 = ? WHERE ZWE_OID = ?";
	
	public static String UPDATE_CONTENT_AND_TITLE = "UPDATE Z_WEB SET ZWE_TITLE = ?, %s = ? WHERE ZWE_OID = ?";
	
	public static String GET_MAX_INCREMENT = "SELECT MAX(ZWE_INC) FROM Z_WEB";
	
	public static String GET_ALIASES = "SELECT ZWE_ALIAS FROM Z_WEB ORDER BY ZWE_ALIAS ASC";
	
	public static String GET_LAST_PAGE_INSERT = "SELECT LAST_INSERT_ID()";
	
	public static String GET_PAGE_LIST = "SELECT ZWE_OID oid, ZWE_INC increment, ZWE_HASH hash, ZWE_ALIAS alias, ZWE_TITLE title, ZWE_CATEGORY category, " +
							"ZWE_LEVEL level, IF(ZWE_SHOW=1,'TRUE','FALSE') isvisible, DATE_FORMAT(ZWE_MODIFIED_DATE,'%D %M %Y at %H:%i%p') modifiedDate, "+
							"USR_UNAME username FROM Z_WEB,REF_USER WHERE USR_OID=ZwE_MODIFIED_BY AND ZWE_IS_DELETED=0 ORDER BY category,level";



}
