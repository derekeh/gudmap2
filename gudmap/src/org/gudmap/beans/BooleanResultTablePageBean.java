package org.gudmap.beans;

import org.gudmap.globals.Globals;

public class BooleanResultTablePageBean {
	
	private String searchResultOption;
	
	public BooleanResultTablePageBean() {
		searchResultOption ="entry";
		//String input = (String)FacesUtil.getSessionValue("input");
		String input = (String)Globals.getSessionValue("input");
		if(null != input && !input.equals("") && input.indexOf(":")>=0 ) {
			//System.out.println("input not null and searchResultOption: " + searchResultOption);
			String prefix = input.substring(0, input.indexOf(":"));
			if (prefix != null) {
				input = input.substring(input.indexOf(":")+1);
				if (prefix.equalsIgnoreCase("gene") || prefix.equalsIgnoreCase("genes"))
					searchResultOption = "gene";
				if (prefix.equalsIgnoreCase("TF") || prefix.equalsIgnoreCase("transcription factor"))
					searchResultOption = "TF";
			}
		}
	}

}
