package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.utils.Utils;

/**
 * 
 * @author Mehran Sharghi
 *
 *
*/
@Named
@RequestScoped
public class MessageBean {
    private boolean debug = false;

	String message;
	String targetPage;
	boolean escape;
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public MessageBean() {
	if (debug)
	    System.out.println("MessageBean.constructor");

		message = Globals.getParameterValue("message");
		targetPage = Globals.getParameterValue("targetPage");
		escape = Utils.getValue(Globals.getParameterValue("escape"), "false").equalsIgnoreCase("true");
	}	

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String navigateTarget() {
		if (targetPage==null || targetPage.equals("")) {
			targetPage = Globals.getRefererPage();
			return "navigateTargetPage"; 
		}
		int extensionIndex = targetPage.indexOf(".jsp");
		if (extensionIndex<0) 
			extensionIndex = targetPage.indexOf(".html");
		if (extensionIndex<0)
			return targetPage;
		
		targetPage = targetPage.replaceFirst(".html", ".jsp");
		return "navigateTargetPage";
	}
	
	// ********************************************************************************
	// Static Methodas
	// ********************************************************************************
	public static String showMessage(String message) {
		return showMessage(message, null, false);
	}
	
	public static String showMessage(String message, boolean escape) {
		return showMessage(message, null, escape);
	}
	
	public static String showMessage(String message, String target) {
		return showMessage(message, target, false);
	}
	
	public static String showMessage(String message, String target, boolean escape) {
		Globals.setParameterValue("message", message);
		Globals.setParameterValue("targetPage", target);
		Globals.setParameterValue("escape", String.valueOf(escape));
		return "messagePage";
	}

	// ********************************************************************************
	// Getters & Setters
	// ********************************************************************************

	public DataModel getMessages() {
		if (message==null || message.equals(""))
			return null;
		String[] messageLines = message.split("\n");
		return new ArrayDataModel(messageLines);
	}

	public String getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}
	
	public boolean getEscape() {
		return escape; 
	}

}
