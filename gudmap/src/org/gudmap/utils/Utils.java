package org.gudmap.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.gudmap.models.submission.ImageInfoModel;

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

	public static String formatResultSetToString(ResultSet resSet) throws SQLException {
		
		String result = null;
		if (resSet.first()) {
			resSet.beforeFirst();
			result = new String("");
			while (resSet.next()) {
				result += resSet.getString(1) + " ";
			}
			return result;
		}
		return null;
	}
	
	 public static ArrayList <ImageInfoModel> formatImageResultSet(ResultSet resSetImage) throws SQLException {
	        if (resSetImage.first()) {
	            resSetImage.beforeFirst();
	            int serialNo = 1;
	            ArrayList <ImageInfoModel> results = new  ArrayList<ImageInfoModel>();
			    //int dotPosition = 0;
			    //String fileExtension = null;
			    String str = null;
			    ImageInfoModel imageInfoModel = null;

	            while (resSetImage.next()) {
	            	imageInfoModel = new ImageInfoModel();
					str = Utils.netTrim(resSetImage.getString(1));
					if (null != str && !str.equals("")) 
						imageInfoModel.setAccessionId(str);
					str = Utils.netTrim(resSetImage.getString(2));
					if (null != str && !str.equals("")) 
						imageInfoModel.setFilePath(str);
					str = Utils.netTrim(resSetImage.getString(3));
					if (null != str && !str.equals("")) 
						imageInfoModel.setNote(str);
					str = Utils.netTrim(resSetImage.getString(4));
					if (null != str && !str.equals("")) 
						imageInfoModel.setSpecimenType(str);
					str = Utils.netTrim(resSetImage.getString(5));
					if (null != str && !str.equals("")) 
						imageInfoModel.setClickFilePath(str);
				
					imageInfoModel.setSerialNo(""+serialNo);
	                serialNo++;
	                results.add(imageInfoModel);
	            }
	            return results;
	        }
	        return null;
	    }

	public static int stringArraySearch(String[] a, String value, boolean ignoreCase) {
	if (a == null || value == null)
	    return -1;
	
	for(int i=0; i<a.length; i++)
	    if (ignoreCase) {
		if (value.equalsIgnoreCase(a[i]))
		    return i;
	    }
	    else
		if (value.equals(a[i]))
		    return i;
	return -1;
	}
	
	public static ArrayList<String> reformatComponentFullPath(String [] components) {
        ArrayList<String> componentList = new ArrayList<String>();
        for(int i=0;i< components.length;i++){
            StringBuffer component = new StringBuffer("");
            for(int j=0;j<i;j++){
                component.append(". . ");
            }
            component.append(components[i]);
	    //            System.out.println(components[i]);
            componentList.add(component.toString());
        }
        return componentList;
    }
	
	//////////////
	
	/*public static String checkAccessionInput(String accessionInput)
    {
	// check Numeric
        if (accessionInput.matches("(?i)[0-9]+$")) {
	    String gudmap = "GUDMAP:" + accessionInput;
	    String maprobe = "maprobe:" + accessionInput;
	    String mgi = "MGI:" + accessionInput;
	    String mtf = "MTF#" + accessionInput;
	    String ensmusg = "ENSMUSG" + accessionInput;
	    
	      String tub = "TUB" + accessionInput;
	      String msr = "MSR" + accessionInput;
	      String gpl = "GPL" + accessionInput;
	      String gse = "GSE" + accessionInput;
	      String gsm = "GSM" + accessionInput;
	      String ensmust = "ENSMUST" + accessionInput;
	      String ensmusp = "ENSMUSP" + accessionInput;
	    
	    return gudmap + "; " + maprobe + "; " + mgi  + "; " + ensmusg + "; " + mtf ;
	}
	
	// check GUDMAP
        if (accessionInput.matches("(?i)^gudmap:[0-9]+$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gudmap[\\W]*[\\w]*[0-9]+$"))
	    return "GUDMAP:" + getDigits(accessionInput, true);
	
        // check TUB
        if (accessionInput.matches("(?i)^tub[0-9]{3,3}[1-9]$"))
	    return accessionInput;
        if (accessionInput.matches("(?i)^tub[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "TUB" + getDigits(accessionInput, true);
	
        // check MSR
        if (accessionInput.matches("(?i)^msr[0-9]{3,3}[1-9]$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^msr[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "MSR" + getDigits(accessionInput, true);
	
        // check maprobe
        if (accessionInput.matches("(?i)^maprobe:[1-9]{4,4}$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^maprobe[\\W]*[\\w]*[1-9]{4,4}$"))
	    return "maprobe:" + getDigits(accessionInput, true);
	
        // check GEO - GPL
        if (accessionInput.matches("(?i)^gpl[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gpl[\\W]*[1-9][0-9]*$"))
	    return "GPL" + getDigits(accessionInput, false);
	
        // check GEO - GSE
        if (accessionInput.matches("(?i)^gse[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gse[\\W]*[1-9][0-9]*$"))
	    return "GSE" + getDigits(accessionInput, false);
	
        // check GEO - GSM
        if (accessionInput.matches("(?i)^gsm[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^gsm[\\W]*[1-9][0-9]*$"))
	    return "GSM" + getDigits(accessionInput, false);
	
        // check MGI
        if (accessionInput.matches("(?i)^mgi:[1-9][1-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^mgi[\\W]*[1-9][0-9]*$"))
	    return "MGI:" + getDigits(accessionInput, false);
	
        // check MTF
        if (accessionInput.matches("(?i)^mtf#[1-9][0-9]*$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^mtf[\\W]*[1-9][0-9]*$"))
	    return "MTF#" + getDigits(accessionInput, false);
	
        // check ENSEMBLE
        if (accessionInput.matches("(?i)^ensmus[gtp][0-9]{11,11}$"))
	    return accessionInput;
	
        if (accessionInput.matches("(?i)^ensmus[gtp][\\W]*[0-9]+$"))
	    return getEnsemble(accessionInput);
	
	return accessionInput;
    }*/
	
	public static String checkAccessionInput(String accessionInput)
    {
	// check Numeric
        if (accessionInput.matches("(?i)[0-9]+$")) {
		    String gudmap = "GUDMAP:" + accessionInput;
		    String maprobe = "maprobe:" + accessionInput;
		    String mgi = "MGI:" + accessionInput;
		    String mtf = "MTF#" + accessionInput;
		    String ensmusg = "ENSMUSG" + accessionInput;
		    /*
		      String tub = "TUB" + accessionInput;
		      String msr = "MSR" + accessionInput;
		      String gpl = "GPL" + accessionInput;
		      String gse = "GSE" + accessionInput;
		      String gsm = "GSM" + accessionInput;
		      String ensmust = "ENSMUST" + accessionInput;
		      String ensmusp = "ENSMUSP" + accessionInput;
		    */
		    return "'"+gudmap + "','"+maprobe + "','"+mgi + "','"+ensmusg + "','"+mtf+"'" ;
        }
	
	// check GUDMAP
        if (accessionInput.matches("(?i)^gudmap:[0-9]+$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^gudmap[\\W]*[\\w]*[0-9]+$"))
	    return "'GUDMAP:" + getDigits(accessionInput, true)+"'";
	
        // check TUB
        if (accessionInput.matches("(?i)^tub[0-9]{3,3}[1-9]$"))
	    return "'"+accessionInput+"'";
        if (accessionInput.matches("(?i)^tub[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "'TUB" + getDigits(accessionInput, true)+"'";
	
        // check MSR
        if (accessionInput.matches("(?i)^msr[0-9]{3,3}[1-9]$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^msr[\\W]*[\\w]*[0-9]{3,3}[1-9]$"))
	    return "'MSR" + getDigits(accessionInput, true)+"'";
	
        // check maprobe
        if (accessionInput.matches("(?i)^maprobe:[1-9]{4,4}$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^maprobe[\\W]*[\\w]*[1-9]{4,4}$"))
	    return "'maprobe:" + getDigits(accessionInput, true)+"'";
	
        // check GEO - GPL
        if (accessionInput.matches("(?i)^gpl[1-9][0-9]*$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^gpl[\\W]*[1-9][0-9]*$"))
	    return "'GPL" + getDigits(accessionInput, false)+"'";
	
        // check GEO - GSE
        if (accessionInput.matches("(?i)^gse[1-9][0-9]*$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^gse[\\W]*[1-9][0-9]*$"))
	    return "'GSE" + getDigits(accessionInput, false)+"'";
	
        // check GEO - GSM
        if (accessionInput.matches("(?i)^gsm[1-9][0-9]*$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^gsm[\\W]*[1-9][0-9]*$"))
	    return "'GSM" + getDigits(accessionInput, false)+"'";
	
        // check MGI
        if (accessionInput.matches("(?i)^mgi:[1-9][1-9]*$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^mgi[\\W]*[1-9][0-9]*$"))
	    return "'MGI:" + getDigits(accessionInput, false)+"'";
	
        // check MTF
        if (accessionInput.matches("(?i)^mtf#[1-9][0-9]*$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^mtf[\\W]*[1-9][0-9]*$"))
	    return "'MTF#" + getDigits(accessionInput, false)+"'";
	
        // check ENSEMBLE
        if (accessionInput.matches("(?i)^ensmus[gtp][0-9]{11,11}$"))
	    return "'"+accessionInput+"'";
	
        if (accessionInput.matches("(?i)^ensmus[gtp][\\W]*[0-9]+$"))
	    return "'"+getEnsemble(accessionInput)+"'";
	
	return "'"+accessionInput+"'";
    }
    
    public static String getDigits(String str, Boolean allowLeadingZeros)
    {
        String digits = "";
        Boolean digitFound = false;
	
        for (int i=0; i < str.length(); i++)
	    {
		if (Character.isDigit(str.charAt(i)))
		    {
			if (!allowLeadingZeros)
			    {
				if (str.charAt(i) != '0')
				    digitFound = true;
			    }
			
			if (allowLeadingZeros || digitFound)
			    {
				digits += str.charAt(i);
			    }
		    }
	    }
        return digits;
    }
    
    public static String getEnsemble(String ensembleString)
    {
        String digits = "";
	
        // extract the digit part of the string 'str'
        for (int i=0; i < ensembleString.length(); i++)
	    {
		if (Character.isDigit(ensembleString.charAt(i)))
		    {
			digits += ensembleString.charAt(i);
		    }
	    }
	
        if (digits.length() == 0)
            return ensembleString;
	
        // pad the string with leading zeros if required
        if (digits.length() < 11)
	    {
		digits = padZero(digits);
	    }
	
        // rebuild the ensemble string
        if (ensembleString.matches("(?i)^ensmusg[\\p{Print}]*"))
            return "ensmusg" + digits;
	
        else if (ensembleString.matches("(?i)^ensmust[\\p{Print}]*"))
            return "ensmust" + digits;
	
        else if (ensembleString.matches("(?i)^ensmusp[\\p{Print}]*"))
            return "ensmusp" + digits;
	
        else return ensembleString;
    }
    
   /* public static String getEnsemble(String ensembleString)
    {
        String digits = "";
	
        // extract the digit part of the string 'str'
        for (int i=0; i < ensembleString.length(); i++)
	    {
		if (Character.isDigit(ensembleString.charAt(i)))
		    {
			digits += ensembleString.charAt(i);
		    }
	    }
	
        if (digits.length() == 0)
            return ensembleString;
	
        // pad the string with leading zeros if required
        if (digits.length() < 11)
	    {
		digits = padZero(digits);
	    }
	
        // rebuild the ensemble string
        if (ensembleString.matches("(?i)^ensmusg[\\p{Print}]*"))
            return "ensmusg" + digits;
	
        else if (ensembleString.matches("(?i)^ensmust[\\p{Print}]*"))
            return "ensmust" + digits;
	
        else if (ensembleString.matches("(?i)^ensmusp[\\p{Print}]*"))
            return "ensmusp" + digits;
	
        else return ensembleString;
    }*/
    
    public static String padZero(String str)
    {
        if (str.length() < 11)
        {
            String newStr = "0" + str;
            return padZero(newStr);
        }
        return str;
    }
    
    public static String normaliseApostrophe(String inputString) {
    	return inputString.replaceAll("\'", "\\"+"\\\'");
    }
    
    public static String createSqlInputFromResult(String[] input) {
	    String output = "";
	    if(null != input) {
			for(int i = 0; i < input.length; i++) {
				output += "'"+input[i] + "',";
			}
			if(input.length >= 1) {
				output = output.substring(0, output.length()-1);
			}
	    }
	    return output;
    }
    
    public static String[] splitInputString(String input) {
    	String[] userInput = input.split(":|;|,"); 
    	return userInput;
    }
    
    public static String[] processInputString(String input, boolean isAccession){
    	String RET[] = null;
        	// check for empty string
        	if (input==null || input == "")
        	{
        		return RET;
        	}
        	else
        	{    
        		RET = new String[2];
        		String[] accessionList = input.split("\\;");
        		String parsedString = "";
        		String parsedQuery = "";
        		String tmpStr = "";
        		for (int i=0; i<accessionList.length; i++)
        		{
        			if(isAccession){
	        			parsedQuery += checkAccessionInput(accessionList[i].trim()) + ",";
	        			tmpStr = checkAccessionInput(accessionList[i].trim())+"\n"; 
	        			parsedString += tmpStr;
	        			tmpStr="";
        			}
        			else {
        				parsedQuery += "'"+accessionList[i].trim() + "',";
	        			tmpStr = accessionList[i].trim()+"\n"; 
	        			parsedString += tmpStr;
	        			tmpStr="";
        			}
        		}
        		//userInputQuery
        		RET[0]= parsedQuery.substring(0,parsedQuery.length()-1);
        		//userInput
        		RET[1]= parsedString.replace("'","");
        		
        		/*this.accessionInputQuery = parsedQuery.substring(0,parsedQuery.length()-1); 
        		this.accessionInput=parsedString.replace("'","");*/
        		
        	} 
        	return RET;

    }
    
    public static String filterNoiseCharacters(String targetString) {
    	final String[][] specialChars = {	{"<", "("}, 
    										{">", ")"}
    									};
    	if (targetString == null || targetString.equals("")) 
    		return targetString;
    	String result = targetString;
       	for (int i=0; i< specialChars.length; i++)
       		result = result.replaceAll(specialChars[i][0], specialChars[i][1]);
       	return result;
   	}
    
    public static boolean isValidInteger(String value) {
		
		boolean RET = true;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			RET = false;
		}
		return RET;
	}
    
    //query to get a list of gene symbols from a specified reference table. Can only add search params to one column in table 
	  public static  String getSymbolsFromGeneInputParamsQuery(String [] input, 
			  String startQuery, String searchColumn, int type){
		  if(input == null)
			  return "";
		  StringBuffer symbolsQ = new StringBuffer(startQuery);
		  //0 == 'like' query ('contains' or 'starts with') ie wildcard
		  if(type == 0) {
			  symbolsQ.append("(");
			  for(int i=0; i<input.length;i++){
	    			if(i==0){
	    				symbolsQ.append(searchColumn+" LIKE ? ");
	    			}
	    			else {
	    				symbolsQ.append("OR "+searchColumn+" LIKE ? ");
	    			}
	    		}
	    		symbolsQ.append(")");
		  }
		  //else type will be 1: equivalent to 'equals'
		  else {
			  symbolsQ.append(searchColumn + " IN (");
			  for(int i=0;i<input.length;i++){
	            	if(i == input.length-1){
	            		symbolsQ.append("?)");
	            	}
	            	else {
	            		symbolsQ.append("?, ");
	            	}
	            }
		  }
		  return symbolsQ.toString();
	  }
	  
	  public static String getDateToday() {
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (month/day/year)
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		return  df.format(today);
	  }
	  
	  public static String getSha1(String input) throws NoSuchAlgorithmException {
	        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
	        byte[] result = mDigest.digest(input.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < result.length; i++) {
	            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        return sb.toString();
	   }
	  
	 /* public static boolean verifyChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException
	    {
	        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
	        FileInputStream fis = new FileInputStream(file);
	  
	        byte[] data = new byte[1024];
	        int read = 0;
	        while ((read = fis.read(data)) != -1) {
	            sha1.update(data, 0, read);
	        };
	        byte[] hashBytes = sha1.digest();
	  
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < hashBytes.length; i++) {
	          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	         
	        String fileHash = sb.toString();
	         
	        return fileHash.equals(testChecksum);
	    }
*/


    public static String[] parseMasterTableId(String masterId) {
		String[] result = new String[2];
		if (masterId.indexOf("_") == -1) {
		    result[0] = masterId.substring(0, 1);
		    result[1] = "0";
		} else {
		    int delimiterPos = masterId.indexOf("_");
		    result[0] = masterId.substring(0, delimiterPos);
		    result[1] = masterId.substring(delimiterPos+1, masterId.length());;
		}
		return result;
    }
   


	  /**
		 * @param linkedSubmissionsRaw
		 * @return an array list of LS, within every element of this array list, it's an 3-unit object array,
		 *         the first element is resource string, the second element is an array list of accession id and
		 *         link types, the third one is URL string;
		 *         The first element of the array list of accesion id and link types is the accession id,
		 *         the second element is an array list of link types.
		 *         
		 */
		public static ArrayList<Object> formatLinkedSubmissionData(ArrayList<String[]> linkedSubmissionsRaw) {
	    	
			if (linkedSubmissionsRaw == null || linkedSubmissionsRaw.isEmpty()) {
				return null;
			}
			
			int len = linkedSubmissionsRaw.size();
//			System.out.println("len: " + len);

			// go through the linked submission list raw data and assemble them into desired data structure
			ArrayList<Object> result = new ArrayList<Object>();
			
	        // linked submissions from one resource
			ArrayList<Object> linkedSubmission = null;
			
	        // list of linked submission and accession id and their link types
			ArrayList<Object> accessionIDAndTypesList = null;
			
			// linked submission accession id and types
			ArrayList<Object> accessionIDAndTypes = null;
			
			ArrayList<String> linkTypes = null;
			String tempResource = null;
			String tempAccessionId = null;
			
			for (int i=0;i<len;i++) {
	    		
				// get the data
				String resource = ((String[])linkedSubmissionsRaw.get(i))[0].trim();
//				System.out.println("resource len: " + resource.length());
	    		String oid = ((String[])linkedSubmissionsRaw.get(i))[1].trim();
//	    		System.out.println("accessionid: " + oid);
	    		String linkType = ((String[])linkedSubmissionsRaw.get(i))[2].trim();
	    		String url = ((String[])linkedSubmissionsRaw.get(i))[3].trim();
//	    		System.out.println("this is no " + i);
	    		
	    		// assemble
	    		if (i == 0) { // first row
	    			linkedSubmission = new ArrayList<Object>();
	        		linkedSubmission.add(0, resource);
	        		linkedSubmission.add(1, url);
	        		
	        		tempResource = resource;
	        		tempAccessionId = oid;
	        		
	        		accessionIDAndTypesList = new ArrayList<Object>();
	        		
	        		accessionIDAndTypes = new ArrayList<Object>();
	        		accessionIDAndTypes.add(0, oid);
	        		
	        		linkTypes = new ArrayList<String>();
	        		linkTypes.add(linkType);
	        		
	    		} else { // if it's not the first row, compare the resource, accession id, and assemble the link type accordingly
	    			
	    			if (resource.equals(tempResource)) {
	    				if (oid.equals(tempAccessionId)) {
	    					linkTypes.add(linkType);
	    				} else {
	    					accessionIDAndTypes.add(1, linkTypes);
	    					accessionIDAndTypesList.add(accessionIDAndTypes);
	    					
	    					accessionIDAndTypes = new ArrayList<Object>();
	    	        		accessionIDAndTypes.add(0, oid);
	    	        		tempAccessionId = oid;

	    					linkTypes = new ArrayList<String>();
	    					linkTypes.add(linkType);
	    				}
	    				
	    			} else { // not the same resource
	    				
	    				// add the type, accession id, into LS data structure (ArrayList)
						accessionIDAndTypes.add(1, linkTypes);
						accessionIDAndTypesList.add(accessionIDAndTypes);
						linkedSubmission.add(1, accessionIDAndTypesList);
	    				
						// convert the data structure for display
						Object[] linkedSubmissionObj = 
	    					(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
	    				result.add(linkedSubmissionObj);
	    				
	        			linkedSubmission = new ArrayList<Object>();
	            		linkedSubmission.add(0, resource);
	            		linkedSubmission.add(1, url);
	            		
	            		tempResource = resource;
	            		tempAccessionId = oid;
	            		
	            		accessionIDAndTypesList = new ArrayList<Object>();
	            		
	            		accessionIDAndTypes = new ArrayList<Object>();
		        		accessionIDAndTypes.add(0, oid);
	            		
	            		linkTypes = new ArrayList<String>();
	            		linkTypes.add(linkType);
	    			}
	    		}
				// put the last row of data into the result data structure
//				System.out.println("len: " + len);
				if (i == len-1) {
					accessionIDAndTypes.add(1, linkTypes);
					accessionIDAndTypesList.add(accessionIDAndTypes);
					linkedSubmission.add(1, accessionIDAndTypesList);
					Object[] linkedSubmissionObj = 
						(Object[])linkedSubmission.toArray(new Object[linkedSubmission.size()]);
					result.add(linkedSubmissionObj);
				}
	    		
			} // end of going through the linked submission list raw data
			return result;
	    }

}
