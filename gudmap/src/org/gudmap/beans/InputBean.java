package org.gudmap.beans;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
 
@Named
@RequestScoped
public class InputBean implements Serializable {
 
	private static final long serialVersionUID = 9040359120893077422L;
	 
	private Part part;
	private String statusMessage;
	
	@Inject
   	protected SessionBean sessionBean;
	public void setSessionBean(SessionBean sessionBean){
			this.sessionBean=sessionBean;
	}
	
	@Inject
	protected ParamBean paramBean;
	public void setParamBean(ParamBean paramBean){
		this.paramBean = paramBean;
	}
	    //if the parameters are not passed in via the url then they are passed in from sessionBean.getTempParam
	    //in a view (viewGeneStrip) which has this as Request scoped backing bean are unable to set the <f:param value to sessionBean.tempParam
	@PostConstruct
	public void setInputParams(){
	   sessionBean.init();
	}
	 
	public String uploadFile() throws IOException {
	 
		// Extract file name from content-disposition header of file part
		String fileName = getFileName(part);
		sessionBean.setTempParam(processMyFile(fileName,part));
		
		//don't need to write the file just read it
		/*String basePath = "/export/data0/documents/geneuploads/";
		File outputFilePath = new File(basePath + fileName);
		 
		// Copy uploaded file to destination path
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = part.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);
			 
			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			 
			statusMessage = "File upload successfull !!";
			} catch (IOException e) {
			e.printStackTrace();
			statusMessage = "File upload failed !!";
		} 
		finally {
				if (outputStream != null) {
				outputStream.close();
				}
				if (inputStream != null) {
				inputStream.close();
				}
		}*/
		return paramBean.geneSearchRedirect();    // return to same page
	}
	
	 
	public Part getPart() {
		return part;
	}
	 
	public void setPart(Part part) {
		this.part = part;
	}
	 
	public String getStatusMessage() {
		return statusMessage;
	}
	 
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	 
	// Extract file name from content-disposition header of file part
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		//System.out.println("***** partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
	
	/**
	 * Navigation method. Processes a text or csv file containing gene lists and redirects the user to an appropriate
	 * page where the input of the file will be used to query the database
	 * @return a string determining where the user should be redirected to 
	 */
	public String processMyFile(String filename,Part myFile) {
		int geneNum=0;
		int uploadLimit=100;
	    String uploadedGenes = "";
	    String tempname = myFile.getName();
		try {   
			if (filename.indexOf("csv") == -1 && filename.indexOf("CSV") == -1 &&
					filename.indexOf("txt") == -1 && filename.indexOf("TXT") == -1 )  
				return null;
			InputStream in = new BufferedInputStream(myFile.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bReader = new BufferedReader(inReader, in.available());
			String oneLine = "";
			String[] splitedLine;
			while (bReader.ready() && geneNum<=uploadLimit) {
				oneLine = bReader.readLine();
				splitedLine = oneLine.split("\t");
				if (splitedLine!=null)
					for (int i=0; i<splitedLine.length; i++) {
						splitedLine[i] = splitedLine[i].trim();
						if (geneNum==0) 		
							if (splitedLine[i].charAt(0) == 0xFEFF) // This is to remove a special character(BOM) added by some editors (e.g oofiice) at the beging of a file 
								splitedLine[i] = splitedLine[i].substring(1).trim();
						if(splitedLine[i]==null || splitedLine[i].equals(""))
							continue;
						geneNum++;
						if (geneNum>uploadLimit) 
							break;
						uploadedGenes += ((geneNum==1)?"":"; ") + splitedLine[i];
					}
			}
			in.close();
			inReader.close();
			bReader.close();
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		} 
		return uploadedGenes;
            
		/*query = "Gene";
		input = uploadedGenes;
		if (uploadResultOption.equalsIgnoreCase("genes")) 
			if (geneNum>uploadLimit)
				return MessageBean.showMessage("There are too many genes in your file! only the first 100 are uploaded.", "gene_query_result.jsp"+ getSearchParams());
			else
				return "GeneQuery";
   		else 
			if (geneNum>uploadLimit)
				return MessageBean.showMessage("There are too many genes in your file! only the first 100 are uploaded.", "focus_gene_browse.jsp"+ getSearchParams());
			else
				return "AdvancedQuery";*/
	} 
	
}
