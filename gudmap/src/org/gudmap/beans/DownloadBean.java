package org.gudmap.beans;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.gudmap.globals.Globals;


@Named
@SessionScoped
public class DownloadBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	    * Download file.
	    * Use this if the file is on the current filesystem
	    */
	   public void downloadFile() throws IOException
	   {
		  String downloadFilename = Globals.getParameterValue("filetodownload");
		  String fileUrl = java.net.URLEncoder.encode(downloadFilename,"UTF-8");
	      File file = new File(downloadFilename);
	      InputStream fis = new FileInputStream(file);
	      byte[] buf = new byte[1024];
	      int offset = 0;
	      int numRead = 0;
	      while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length -offset)) >= 0)) 
	      {
	        offset += numRead;
	      }
	      fis.close();
	      HttpServletResponse response =
	         (HttpServletResponse) FacesContext.getCurrentInstance()
	        .getExternalContext().getResponse();

	     response.setContentType("application/octet-stream");
	     response.setHeader("Content-Disposition", "attachment;filename="+downloadFilename);
	     response.getOutputStream().write(buf);
	     response.getOutputStream().flush();
	     response.getOutputStream().close();
	     FacesContext.getCurrentInstance().responseComplete();
	   }
	   
	   /**
	    * Download file.
	    * Use this if the file is streamed from a URL or remote server
	    */
	   public void downloadFileFromUrl() throws IOException
	   {
		  String downloadFilename = Globals.getParameterValue("filetodownload");
		  String filename = Globals.getParameterValue("filename");
		  FacesContext fc = FacesContext.getCurrentInstance();
		  
		  /* JSF1 SYNTAX
		   * HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
		    response.reset(); 
		    response.setContentType(contentType);
		    response.setContentLength(contentLength);
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");		
		    OutputStream output = response.getOutputStream();
		   * 
		   */
		  
		  ExternalContext ec = fc.getExternalContext();

		    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
		    ec.setResponseContentType("application/octet-stream"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
		   // ec.setResponseContentLength(length); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

		    OutputStream output = ec.getResponseOutputStream();
		    // Now you can write the InputStream of the file to the above OutputStream the usual way.
		    BufferedInputStream in = null;
		    try {
		        in = new BufferedInputStream(new URL(downloadFilename).openStream());
//1024
		        final byte data[] = new byte[16384];
		        int count;
		        while ((count = in.read(data, 0, 16384)) != -1) {
		        	output.write(data, 0, count);
		        	
		        	if (count > 16384 * 16384) { //flush after 1MB
		                count = 0;
		                output.flush();
		        	}
		        }
		    } finally {
		        if (in != null) {
		            in.close();
		        }
		        if (output != null) {
		        	output.flush();
		        	output.close();
		        }
		    }

		    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
		}
	 
	   /*
	    * Alternative download using NIO. Needs some work!
	    */
	   public void downloadUrlFile() throws IOException
	   {
		  String downloadFilename = Globals.getParameterValue("filetodownload");
		  URL urlFile = new URL(downloadFilename);
		  
		  HttpServletResponse response =
			         (HttpServletResponse) FacesContext.getCurrentInstance()
			        .getExternalContext().getResponse();

			     response.setContentType("application/octet-stream");
			     response.setHeader("Content-Disposition", "attachment;filename=myfile.sra");
		  
		  ReadableByteChannel rbc = Channels.newChannel(urlFile.openStream());
		  FileOutputStream fos = new FileOutputStream("myfile.sra");
		  fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	      
	     //response.getOutputStream().write(buf);
	     //response.getOutputStream().flush();
	     //response.getOutputStream().close();
	     FacesContext.getCurrentInstance().responseComplete();
	   }
	   
	   public void downloadFileFromUrl_orig() throws IOException
	   {
		  String downloadFilename = Globals.getParameterValue("filetodownload");
		  String filename = Globals.getParameterValue("filename");
		  FacesContext fc = FacesContext.getCurrentInstance();
		  
		  /* JSF1 SYNTAX
		   * HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
		    response.reset(); 
		    response.setContentType(contentType);
		    response.setContentLength(contentLength);
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");		
		    OutputStream output = response.getOutputStream();
		   * 
		   */
		  
		  ExternalContext ec = fc.getExternalContext();

		    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
		    ec.setResponseContentType("application/octet-stream"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
		   // ec.setResponseContentLength(length); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

		    OutputStream output = ec.getResponseOutputStream();
		    // Now you can write the InputStream of the file to the above OutputStream the usual way.
		    BufferedInputStream in = null;
		    try {
		        in = new BufferedInputStream(new URL(downloadFilename).openStream());
		       // output = new FileOutputStream(filename);

		        final byte data[] = new byte[1024];
		        int count;
		        while ((count = in.read(data, 0, 1024)) != -1) {
		        	output.write(data, 0, count);
		        }
		    } finally {
		        if (in != null) {
		            in.close();
		        }
		        if (output != null) {
		        	output.close();
		        }
		    }

		    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
		}

}
