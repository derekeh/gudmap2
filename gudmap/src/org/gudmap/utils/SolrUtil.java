package org.gudmap.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
//import java.util.ResourceBundle;
import java.util.Set;

import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;



/**
 * <h1>SolrUtil</h1>
 * The SolrUtil class contains the methods to connect and run the Solr Search engine
 * 
 * @author Bernard Haggarty
 * @version 1.0
 * @since 13/03/2013 
 */
public class SolrUtil {
		
	public CoreContainer container;
	
	public HttpSolrClient insitu_server;
	public HttpSolrClient genes_server;
	public HttpSolrClient genelists_server;
	public HttpSolrClient samples_server;
	public HttpSolrClient ng_samples_server;
	public HttpSolrClient tissues_server;
	public HttpSolrClient mouse_strain_server;
	public HttpSolrClient image_server;
//	public HttpSolrClient tutorial_server;
	public HttpSolrClient web_server;
	
	public Set<String> insitu_schema;
	public Set<String> genes_schema;
	public Set<String> genelists_schema;
	public Set<String> samples_schema;
	public Set<String> ng_samples_schema;
	public Set<String> tissues_schema;
	public Set<String> mouse_strain_schema;
	public Set<String> image_schema;
		
	public int ishExpressionCount = 0;	
	public String genelistids;
	public String genes;
	public String searchString = null;
	
	/**
	 * SolrUtil class constructor.
	 */
	public SolrUtil(){
		
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		String solr_home = facesContext.getExternalContext().getInitParameter( "SOLR_HOME" );
//		String solr_home = "/export/data0/bernardh/qq/solr";
//    	File home = new File(solr_home);
//    	File f = new File( home, "solr.xml" );
//    	try {
//			container = new CoreContainer(solr_home.getPath());
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		}
		
		insitu_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_insitu" );		
		genes_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_genes" );		
		genelists_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_genelists" );
		samples_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_samples" );		
		ng_samples_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_nextgen_samples" );		
		tissues_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_tissues" );		
		mouse_strain_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_mousestrains" );		
		image_server = new HttpSolrClient( "http://localhost:8983/solr/gudmap_images" );		
//		tutorial_server = new HttpSolrClient("http://localhost:8983/solr/gudmap_tutorial");
		web_server = new HttpSolrClient("http://localhost:8983/solr/gudmap_web");
		
		
    	insitu_schema = getInsituSchema();
		genes_schema = getGeneSchema();
		genelists_schema = getGeneListsSchema();
		samples_schema = getSamplesSchema();
		ng_samples_schema = getSequenceSamplesSchema();
		tissues_schema = getTissuesSchema();
		mouse_strain_schema = getMouseStrainsSchema();
		image_schema = getImagesSchema();
			

		
	}

	
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_insitu index
	 */
	public HttpSolrClient getInsituServer(){
		return insitu_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_genes index
	 */
	public HttpSolrClient getGenesServer(){
		return genes_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_genelistss index
	 */
	public HttpSolrClient getGenelistsServer(){
		return genelists_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_samples index
	 */
	public HttpSolrClient getSamplesServer(){
		return samples_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_tissues index
	 */
	public HttpSolrClient getTissuesServer(){
		return tissues_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_mousestrains index
	 */
	public HttpSolrClient getMouseStrainServer(){
		return mouse_strain_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_images index
	 */
	public HttpSolrClient getImageServer(){
		return image_server;
	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_nextgen_samples index
	 */
	public HttpSolrClient getNextGenSamplesServer(){
		return ng_samples_server;
	}
//	/**
//	 * @return Returns the HttpSolrClient instance connected to the gudmap_tutorial index
//	 */
//	public HttpSolrClient getTutorialServer(){
//		return tutorial_server;
//	}
	/**
	 * @return Returns the HttpSolrClient instance connected to the gudmap_web index
	 */
	public HttpSolrClient getWebServer(){
		return web_server;
	}
	
	public String getExpressionFilter(String filter) {
		
		String fs = "";
		
    	if (filter.contains("present"))
    		fs = "DIR_PRESENT:*";
    	
    	if (filter.contains("uncertain")){
    		if (fs != "")
    			fs += " OR UNCERTAIN:*";
    		else   		
    			fs = "UNCERTAIN:*";
    	}
    	
    	if (filter.contains("not detected")){
    		if (fs != "")
    			fs += " OR NOT_DETECTED:*";
    		else   		
    			fs = "NOT_DETECTED:*";
    	}
    	
    	return fs;
	}

	public ArrayList<String> checkSchema(HttpSolrClient server, ArrayList<String> filters){
		
		ArrayList<String> updatedfilters = new ArrayList<String>();
		
        LukeRequest lukeRequest = new LukeRequest();
        LukeResponse lukeResponse;
        lukeRequest.setNumTerms(0);
        try {
			lukeResponse = lukeRequest.process(server);
			Set<String> keys = lukeResponse.getFieldInfo().keySet();
			System.out.println("schema fields = "+keys);
			for (String filter: filters){
				int index = filter.indexOf(":");
				String prefix = filter.substring(0, index);
				if (keys.contains(prefix))
					updatedfilters.add(filter);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		return updatedfilters;		
	}
	
	/**
	 * This method returns a modified query string. The query is modified by adding the wildcard symbol
	 * to each unreserved word in the query string
	 * ie sox AND bladder -> sox* AND bladder*
	 * @param queryString This is the search query string
	 * @return String This returns modified query string
	 */
	public String setWidcard(String q){
		String query = "";
		String ops = "AND OR NOT";
		
		String[] items = q.split(" ");
		for( int i = 0; i < items.length; i++){
			String update = items[i];
			if (ops.contains(update))
				query += update + " ";
			else
				query += update + "* ";
		}
		
		return query.trim();
	}
    //***************************** INSITU METHODS *****************************************************

	/**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
 	 * @return The number of documents retrieved in the result set
	 */
    public int getInsituCount(String queryString){
    	return getInsituCount(queryString, null);
    }

	/**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getInsituCount(String queryString, String filter){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
    	
        int insituCount = 0;
      	insitu_server.setParser(new XMLResponseParser());
        
        try{
	        SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.set("wt", "xml");
	      	if (filter != null)
	      		parameters.addFilterQuery(filter);

	      	
	        QueryResponse qr = insitu_server.query(parameters);
	        SolrDocumentList sdl = qr.getResults();
	        insituCount = (int)sdl.getNumFound();
        }
        catch (SolrServerException e){
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return  insituCount;
    }

	/**
     * This method runs the queryString against the gudmap_nextgen_samples solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filter A filter to be applied to the solr search
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
     * @throws SolrServerException
	 */    
    public int getInsituCount(String queryString, String filter, List<String> filters) throws SolrServerException{   	

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

    	int count = 0;

        try
        {
   		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);

        	if (filter == null || filter.contentEquals("undefined")){
        	}
        	else
        	{
	            String[] fields = filter.split(":");
	            if (fields.length > 1){
	            	filter = fields[0] + ":" + '"' + fields[1] + '"';
	            	parameters.addFilterQuery(filter);
	            }
	        }
	        if (filters != null){    
	            for (String fs : filters){
	            	if (fs.contains("STAGE"))
	            		fs = fs.replace(":", ":TS");
	            		            	
	            	if (fs.contains("EXP_STRENGTH"))
	            		fs = getExpressionFilter(fs);	            		            	
	            	parameters.addFilterQuery(fs);
	            }
	        }
	        
            QueryResponse qr = insitu_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
  
            count = (int)sdl.getNumFound();
        }
        catch (SolrServerException e){
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

       return count;    
    }
    
	/**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getInsituFilteredCount(String queryString, HashMap<String,String> filters)
    {    			
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
		int count = 0;
		try{
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);	
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (insitu_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	      	
	        QueryResponse qr = insitu_server.query(parameters);                      
	        
	        SolrDocumentList sdl = qr.getResults();
	        count = (int)sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return count;
    }
    
//    public int getAssayCount(String queryString){
//    	return getInsituCount(queryString, null);
//    }
    
//    public int getIshStagesCount(String queryString){
//    	return getInsituCount(queryString, null);
//    }

//    public int getIshAnatomyCount(String queryString){
//    	return getInsituCount(queryString, null);
//    }

//    public int getIshAssayCount(String queryString){
//    	String filter = "ASSAY_TYPE:ISH";
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIhcAssayCount(String queryString){
//    	String filter = "ASSAY_TYPE:IHC";
//    	return getInsituCount(queryString, filter);
//    }

//    public int getTgAssayCount(String queryString){
//    	String filter = "ASSAY_TYPE:TG";
//    	return getInsituCount(queryString, filter);
//    }
    
//    public int getIshMetanephrosCount(String queryString){
//    	String filter = "FOCUS_GROUPS:metanephros";
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIshMRSCount(String queryString){
//    	String filter = "FOCUS_GROUPS:male reproductive system";
//        String[]ffields = filter.split(":");
//    	filter = ffields[0] + ":" + '"' + ffields[1] + '"';
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIshFRSCount(String queryString){
//    	String filter = "FOCUS_GROUPS:female reproductive system";
//        String[]ffields = filter.split(":");
//    	filter = ffields[0] + ":" + '"' + ffields[1] + '"';
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIshERSCount(String queryString){
//        String filter = "FOCUS_GROUPS:early genitourinary system";
//        String[] ffields = filter.split(":");
//    	filter = ffields[0] + ":" + '"' + ffields[1] + '"';
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIshLUTCount(String queryString){
//        String filter = "FOCUS_GROUPS:lower urinary tract";
//        String[] ffields = filter.split(":");
//    	filter = ffields[0] + ":" + '"' + ffields[1] + '"';
//    	return getInsituCount(queryString, filter);
//    }

//    public int getIshMesonephrosCount(String queryString){
//    	String filter = "FOCUS_GROUPS:mesonephros";
//    	return getInsituCount(queryString, filter);
//    }
    
//    public int getInsituPresentCount(String queryString){    	
//    	String filter = "PRESENT:['' TO *]";
//    	return getInsituCount(queryString, filter);
//    }

//    public int getInsituCountByUncertain(String queryString){
//    	String filter = "UNCERTAIN:['' TO *]";
//    	return getInsituCount(queryString, filter);
//    }

//    public int getInsituCountByNotDetected(String queryString){
//    	String filter = "NOT DETECTED:['' TO *]";
//    	return getInsituCount(queryString, filter);
//    }
    
//	// method to retrieve the list of Insitu stages count for the results page
//    public List<Integer> getInsituStages(String queryString){
//
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//		else
//			queryString = setWidcard(queryString);
//
//		List<Integer> ishStages = new ArrayList<Integer>();
//		for (int i= 0; i < 12; i++){
//			ishStages.add(0);
//		}
//
//        try
//        {
//
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setFacet(true);
//	        parameters.setRows(0);	      
//	        parameters.setFacetMinCount(0);
//	        parameters.setFacetLimit(1000);
//	        	      	        
//            QueryResponse qr = insitu_server.query(parameters);
//            SolrDocumentList sdl = qr.getResults();
//           
//           for (int i = 17; i < 29; i ++){            
//        	   parameters.addFilterQuery("THEILER_STAGE_FILTER:"+i);
//	            qr = insitu_server.query(parameters);
//	            sdl = qr.getResults();
//	            ishStages.set(i-17, (int)sdl.getNumFound());
//	            parameters.removeFilterQuery("THEILER_STAGE_FILTER:"+i);
//            }
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//			e.printStackTrace();
//		}        
//        return ishStages;    
//    }
    

    /**
     * This method runs the queryString against the gudmap_insitu solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     * @throws SolrServerException.
     */
    public SolrDocumentList getInsituData(String queryString, HashMap<String,String> filters, String column, boolean ascending, int offset, int rows) throws SolrServerException{

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
		

    	if (column.contentEquals("GUDMAP_ID"))
    		column = "GUDMAP";
    	
        SolrDocumentList sdl = new SolrDocumentList();
        
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
        try
        {

			   		    		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setFacet(true);
//	        parameters.setFacetLimit(12000);
	        parameters.setFacetMinCount(0);
	        parameters.setIncludeScore(true);
	        parameters.setStart(offset);
	        parameters.setRows(rows); //(1000);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
	        
	        parameters.addFacetField("GUDMAP");
	        parameters.addField("GUDMAP");
	        parameters.addField("GUDMAP_ID");
	        parameters.addField("GENE");
	        parameters.addField("STAGE");
	        parameters.addField("DEV_STAGE");
	        parameters.addField("SOURCE");
	        parameters.addField("DATE");
	        parameters.addField("ASSAY_TYPE");
	        parameters.addField("SPECIMEN_ASSAY_TYPE");
	        parameters.addField("SEX");
	        parameters.addField("PROBE_NAME");
	        parameters.addField("GENOTYPE");
	        parameters.addField("PROBE_TYPE");
	        parameters.addField("IMAGE");
	        parameters.addField("MGI_GENE_ID");
	        parameters.addField("SPECIES");
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (insitu_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        

            QueryResponse qr = insitu_server.query(parameters);
            sdl = qr.getResults();
            
            
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

        return sdl;
    }

    // method to calculate the total in each field from an insitu query used to display column totals
    public List<Integer> getInsituDataTotals(String queryString, String filter, List<String> filters) throws SolrServerException{
		
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		List<Integer> fieldCounts = new ArrayList<Integer>();	

        try
        {
			   		    		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0); 
	        parameters.set("group", true);
	        parameters.set("group.ngroups", true);
	        
	        String[] groupFields = {"GENE","GUDMAP","SOURCE","DATE","ASSAY_TYPE","PROBE_NAME","THEILER","DEV_STAGE","SEX","GENOTYPE","TISSUE_TYPE","PRESENT","SPECIMEN_ASSAY_TYPE","IMAGE_PATH"};

        	if (filter == null || filter.contentEquals("undefined")){
        	}
        	else{
	            String[] fields = filter.split(":");
	            if (fields.length > 1){
	            	filter = fields[0] + ":" + '"' + fields[1] + '"';
	            	parameters.addFilterQuery(filter);
	            }
        	}
            
            for (String fs : filters){
            	if (fs.contains("STAGE"))
            		fs = fs.replace(":", ":TS");
            	
            	if (fs.contains("EXP_STRENGTH"))
            		fs = getExpressionFilter(fs);
            	
            	parameters.addFilterQuery(fs);
            }
	        
        	for (String str : groupFields){
        		if (str != "PROBE_TYPE"){
        			parameters.set("group.field", str);
	        		QueryResponse qr = insitu_server.query(parameters);
	        		List<GroupCommand> gc = qr.getGroupResponse().getValues();
	        		GroupCommand groups = gc.get(0);
	        		int groupCount = groups.getNGroups();
	        		fieldCounts.add(groupCount);
        		}
        		else
        			fieldCounts.add(1);
        	}
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
       
        return fieldCounts;
    }
    
    
    
    //***************************** GENE METHODS *****************************************************

	/**
     * This method runs the queryString against the gudmap_genes solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
 	 * @return The number of documents retrieved in the result set
	 */
   public int getGeneCount(String query){ 
    	return getGeneCount(query, null);
    }
    
    
	/**
     * This method runs the queryString against the gudmap_genes solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getGeneCount(String queryString, HashMap<String,String> filters)
    {    			
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
		int count = 0;
		try{
	    	
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);	
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (genes_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	
	        QueryResponse qr = genes_server.query(parameters);                      
	        
	        SolrDocumentList sdl = qr.getResults();
	        count = (int)sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return count;
    }

	/**
     * This method runs the queryString against the gudmap_genes solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filter A filter to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getGeneFilteredCount(String queryString, String filter)
    {    			
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
		int count = 0;
		try{
	    	
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);	 
	      	if (filter != null || filter != ""){
	      		parameters.addFilterQuery(filter);
	      	}
	
	        QueryResponse qr = genes_server.query(parameters);                      
	        
	        SolrDocumentList sdl = qr.getResults();
	        count = (int)sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return count;
    }
    
	/**
     * This method runs the queryString against the gudmap_genes solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filter A filter to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getGeneCount2(String queryString, String filter)
    {    			
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
		int count = 0;
		ArrayList<String> genelist = new ArrayList<String>();  
		
		try{

			int fetchSize = 1000;
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.addField("GENE");
	        parameters.addField("SYNONYMS");
	        parameters.addField("MGI_GENE_ID");
	        parameters.setRows(fetchSize);	 
	        
	      	if (filter != null)
	      		parameters.addFilterQuery(filter);
	      	

	      	long offset = 0;
	        QueryResponse qr = genes_server.query(parameters);
	        long totalResults = qr.getResults().getNumFound();
	        
	        while (offset < totalResults)
	        {
	        	parameters.setStart((int) offset);  // requires an int? wtf?
	        	parameters.setRows(fetchSize);

	            for (SolrDocument doc : genes_server.query(parameters).getResults())
	            {
		    		String id = doc.getFieldValue("GENE").toString();
		    		if(!genelist.contains(id) && (id.length() != 0))
		    			genelist.add(id);
	            }

	            offset += fetchSize;
	        }	        
//	        SolrDocumentList sdl = qr.getResults();
//    	    for(SolrDocument doc : sdl){
//	    		String id = doc.getFieldValue("GENE").toString();
//	    		if(!genelist.contains(id) && (id.length() != 0))
//	    			genelist.add(id);
//    		}
	        
	        count = genelist.size();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return count;
    }       
    
  
    /**
     * This method runs the queryString against the gudmap_genes solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     */
    public SolrDocumentList getGudmapGenes(String queryString, HashMap<String,String> filters, String column, boolean ascending, int offset, int rows){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		List<String> genes = new ArrayList<String>();
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
    	SolrDocumentList sdl = null;
    	
        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setFacet(true);        	      	        
	        parameters.addFacetField("GENE");        	        
//	        parameters.addFacetField("MGI_GENE_ID");        	        
	        parameters.addFacetField("SYNONYMS");        	        
	        parameters.setFacetMinCount(0);
	        parameters.setIncludeScore(true);
//	        parameters.setFacetLimit(100000);
	        parameters.setStart(offset);
	        parameters.setRows(rows); //(25000);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);

	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (genes_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = genes_server.query(parameters);
                       
            sdl = qr.getResults();
//    		int rowNum = sdl.size();
//    		for(int i=0; i<rowNum; i++) { 
//    			SolrDocument doc = sdl.get(i);
//    			String id = doc.getFieldValue("GENE").toString();
//    			genes.add(id);
//    		}
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}  

    	return sdl; //genes;
    }



    /**
     * This method runs the queryString against the gudmap_genelists solr index.
     * It returns a string containing the geness from the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @return A string of genes
     */
    public String getGenesFromGenelistId(String queryString){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		String genes = "";

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(24000);	      
	        
	        
            QueryResponse qr = genelists_server.query(parameters);
                    
            SolrDocumentList sdl = qr.getResults();
    		int rowNum = sdl.size();
    		for(int i=0; i<rowNum; i++) { 
    			SolrDocument doc = sdl.get(i);
    			String id = doc.getFieldValue("GENE").toString();
    			genes += id + ",";
    		}
    		genes = genes.replaceAll(";", ",");
    		genes = genes.substring(0, genes.lastIndexOf(","));
//    		List<String> l = Arrays.asList(genes.split("\\s*,\\s*"));
//    		int c = l.size();
    		
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		} 

        return genes;
    }
    
    public HashSet<String> getSourceList(String queryString, ArrayList<String> filters){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		HashSet<String> sources = new HashSet<String>();                       
        try
        {
			   		    		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setFacet(true);
	        parameters.setRows(50);
            
	        parameters.addField("SOURCE");
	        
            QueryResponse qr = genes_server.query(parameters);                                 
            SolrDocumentList sdl = qr.getResults();

            String source = null;
    	    for(SolrDocument doc : sdl){
    	    	source = doc.getFieldValue("SOURCE").toString();
	    			sources.add(source);
    		}
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}  
       
    	return sources;    
    }

    //***************************** SEQUENCES METHODS *****************************************************
    
	/**
     * This method runs the queryString against the gudmap_nextgen_samples solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
 	 * @return The number of documents retrieved in the result set
	 */
    public int getSequencesCount(String query){ 
    	return getSequencesCount(query, null);
    }
    
    
	/**
     * This method runs the queryString against the gudmap_nextgen_samples solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getSequencesCount(String queryString, HashMap<String,String> filters)
    {    			
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
		int count = 0;
		try{
	    	
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);	
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            String key = pair.getKey();
		            if (ng_samples_schema.contains(key))
		            	parameters.addFilterQuery(key + ":" + pair.getValue());
		        }
	        }
	
	        QueryResponse qr = ng_samples_server.query(parameters);                      
	        
	        SolrDocumentList sdl = qr.getResults();
	        count = (int)sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        return count;
    }

    /**
     * This method runs the queryString against the gudmap_nextgen_samples solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     * @throws SolrServerException
     */
    public SolrDocumentList getSequencesData(String queryString, HashMap<String,String> filters, String column, boolean ascending, int offset, int rows) throws SolrServerException{

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

    	if (column.contentEquals("GUDMAP_ID"))
    		column = "GUDMAP";
    	
        SolrDocumentList sdl = new SolrDocumentList();
        
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
        try
        {
			   		    		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setFacet(true);
	        parameters.setFacetLimit(12000);
	        parameters.setFacetMinCount(0);
	        parameters.setIncludeScore(true);
	        parameters.setStart(offset);
	        parameters.setRows(rows); //(1000);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
	        
//	        parameters.addFacetField("GUDMAP");
//	        parameters.addField("DEV_STAGE");
//	        parameters.addField("COMPONENT");
//	        parameters.addField("DATE");
//	        parameters.addField("GENOTYPE");
//	        parameters.addField("GUDMAP");
//	        parameters.addField("LIBRARY_STRATEGY");
//	        parameters.addField("PI_NAME");
//	        parameters.addField("SAMPLE_DESCRIPTION");
//	        parameters.addField("SAMPLE_GEO_ID");
//	        parameters.addField("SAMPLE_NAME");
//	        parameters.addField("SERIES_GEO_ID");
//	        parameters.addField("SOURCE");
//	        parameters.addField("STAGE");
//	        parameters.addField("SEX");


	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            String key = pair.getKey();
		            if (ng_samples_schema.contains(key))
		            	parameters.addFilterQuery(key + ":" + pair.getValue());
		        }
	        }


            QueryResponse qr = ng_samples_server.query(parameters);
            sdl = qr.getResults();
            
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

        return sdl;
    }
    
    //***************************** GENELISTS METHODS *****************************************************

	/**
     * This method runs the queryString against the gudmap_genelists solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
 	 * @return The number of documents retrieved in the result set
	 */
    public void queryGenelists(String queryString)
    {
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		try
        {
            
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
        
        	parameters.addField("ID");
	        parameters.setRows(Integer.MAX_VALUE);

            QueryResponse qr = genelists_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();

            // if query is everything then pass * to genelistids to be processed in SearchTree 
            // to avoid sending large string of genelist ids
    		if (queryString.equalsIgnoreCase("*")){
    			genelistids = "*";
    			return;
    		}
    		
            // build a new string of genelist ids on the sly
            List<String> ids = new ArrayList<String>();
            genelistids = "";
    		for(int i=0; i<sdl.size(); i++) { 
    			SolrDocument doc = sdl.get(i);
    			String id = doc.getFieldValue("ID").toString();
				ids.add(id);
				genelistids += id + ",";
    		}

        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}

    }
    
	/**
     * This method runs the queryString against the gudmap_genelists solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
	 */    
    public int getGenelistCount(String queryString, HashMap<String,String> filters){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		long count = 0;

        try
        {
    		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (genelists_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = genelists_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
            count = sdl.getNumFound();   
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}  

        return (int)count;    
    }

    /**
     * This method runs the queryString against the gudmap_genelists solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @return A SolrDocumentList of retrieved documents
     */
     public SolrDocumentList getGenelistData(String queryString, HashMap<String,String> filters){

 		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

 		SolrDocumentList sdl = null;
    	   	    	
        try
        {   		
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        
	      	parameters.addField("ID");
//        	parameters.addField("NAME");
//        	parameters.addField("DESCRIPTION");
//	      	parameters.addField("PLATFORM");
//        	parameters.addField("SAMPLE");
//        	parameters.addField("EMAP");
//	      	parameters.addField("MA_DATASET");
//        	parameters.addField("MA_DATASET_ID");
//        	parameters.addField("TOT_ENTITIES");
//	      	parameters.addField("TOT_GENES");
//        	parameters.addField("AUTHOR");
//        	parameters.addField("DATE");
//	      	parameters.addField("STAGE");
//        	parameters.addField("SEX");
//        	parameters.addField("GENELIST_TYPE");
//        	parameters.addField("ENTITIES");
//        	parameters.addField("GENE");
//	      	parameters.addField("GUDMAP_IDS");
//        	parameters.addField("REF");
//        	parameters.addField("EMAP_TERM");

	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (genelists_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        parameters.setRows(Integer.MAX_VALUE); 
        	
            QueryResponse qr = genelists_server.query(parameters);
            sdl = qr.getResults();    
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}    

        return sdl;
    }
    
    //***************************** TISSUE METHODS *****************************************************

 	/**
     * This method runs the queryString against the gudmap_tissues solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
 	 */    
    public int getTissueCount(String queryString, HashMap<String,String> filters){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
    	long count = 0;

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (tissues_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = tissues_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
            count = sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        

        return (int)count;    
    }
 

    /**
     * This method runs the queryString against the gudmap_tissues solr index.
     * It returns a QueryResponse object.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A Solr QueryResponse
     */
    public QueryResponse getTissueData(String queryString, HashMap<String, String> filters, String column, boolean ascending, int offset, int rows){
    	
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		QueryResponse qr = null;
    	
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        parameters.setIncludeScore(true);
	        
	        parameters.setStart(offset);
	        parameters.setRows(rows);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
      
	      	parameters.addField("ID");
        	parameters.addField("NAME");
        	parameters.addField("STAGES");

	        parameters.setParam("debugparameters", "on");
	        parameters.setHighlight(true);  
	        parameters.setParam("hl.fl", "*");	 
	        
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (tissues_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            qr = tissues_server.query(parameters);
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}

        return qr; 
    }
     
      //***************************** SERIES METHODS *****************************************************

      // method to retrieve the Series count for the results page
//    public int getSeriesCount(String queryString){
//
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//   	
//    	int count = 0;
//
//        try
//        {      	
//
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setFacet(true);
//	      	parameters.setRows(0);	      
//	        parameters.setFacetMinCount(0);
//	        parameters.setIncludeScore(true);
//	        parameters.setFacetLimit(1000);
//
//	        parameters.addFacetPivotField("SERIES_GEO_ID,GUDMAP"); 	        
//	        parameters.addFacetPivotField("GUDMAP,SERIES_GEO_ID"); 
//
//            QueryResponse qr = series_server.query(parameters);
//
//            NamedList<List<PivotField>> pivots = qr.getFacetPivot();
//
//            for(Map.Entry<String, List<PivotField>> entry : pivots ) {
//	  	          if (entry.getKey().equalsIgnoreCase("SERIES_GEO_ID,GUDMAP")){
//	  	        	  	count = entry.getValue().size();
//	  	          }
//            }  
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        }        
//        catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        return count;    
//    }

	// method to retrieve the series geo ids for the results page
//   public List<String> getSeriesData(String queryString, String column, boolean ascending, int offset, int rows){
//
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//	   
//    	List<String> ids = new ArrayList<String>();
//		    	
//    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);    	    	
//
//        try
//        {
//
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        
//	        parameters.setIncludeScore(true);
//	        
//	        parameters.setStart(offset);
////	        parameters.setRows(10000);
//	        parameters.setRows(1000);
//	        if (!column.equalsIgnoreCase("RELEVANCE"))
//	        	parameters.setSort(column, order);  
//	        
//            QueryResponse qr = series_server.query(parameters);
//        	SolrDocumentList sdl = qr.getResults();
//                        
//    	    for(SolrDocument doc : sdl)
//    	    {
//    	    	// remove duplicate GEO entries
//    	    	String id = doc.getFieldValue("SERIES_GEO_ID").toString();
//    	    	if (!ids.contains(id)){
//    	    		ids.add(id);
//    	    	}    	    	
//    		}
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        }        
//        catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        return ids;
//    }
//
//	// method to retrieve the Series data for the results page
//    public SolrDocumentList getSeriesViewData(List<String> ids, String column, boolean ascending, int offset, int rows){
//    	
//    	SolrDocumentList sdl = new SolrDocumentList();
//    	String queryString = "";
//    	for (String s : ids)
//    		queryString += s + ",";
//		   	
//    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);    	    	
//        try
//        {
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setIncludeScore(true);	        
//	        parameters.setStart(offset);
//	        parameters.setRows(rows);
//	        if (!column.equalsIgnoreCase("RELEVANCE"))
//	        	parameters.setSort(column, order);
//      
//	        parameters.addField("TITLE");
//	      	parameters.addField("SERIES_GEO_ID");
//        	parameters.addField("SAMPLE_NUMBER");
//        	parameters.addField("SOURCE");
////        	parameters.addField("PI_NAME"); 
//        	parameters.addField("PLATFORM_GEO_ID");
//        	parameters.addField("SERIES_OID");
//        	parameters.addField("COMPONENT");
//
//            QueryResponse qr = series_server.query(parameters);
//        	sdl = qr.getResults();
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        }        
//        catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        return sdl;
//    }
    
    //***************************** SAMPLE METHODS *****************************************************

 	/**
     * This method runs the queryString against the gudmap_samples solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
 	 */    
    public int getSamplesFilteredCount(String queryString, HashMap<String,String> filters){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
   	
    	int count = 0;

		try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setFacet(true);
	      	parameters.setRows(0);	      
	        parameters.setFacetMinCount(0);
	        parameters.setIncludeScore(true);
	        parameters.setFacetLimit(1000);
	        	      	        
	        parameters.addFacetPivotField("SAMPLE_GEO_ID,GUDMAP"); 	    
	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (samples_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	       	        
            QueryResponse qr = samples_server.query(parameters);
            
            NamedList<List<PivotField>> pivots = qr.getFacetPivot();

            for(Map.Entry<String, List<PivotField>> entry : pivots ) {
	  	          if (entry.getKey().equalsIgnoreCase("SAMPLE_GEO_ID,GUDMAP")){
	  	        	  	count = entry.getValue().size();
	  	          }
            }    
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        


        return count;    
    }

    /**
     * This method runs the queryString against the gudmap_samples solr index.
     * It returns a a list containing the GEO ID's from the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A list of GEO ID's from the retrieved documents
     */
    public List<String> getSamplesData(String queryString, HashMap<String,String> filters, String column, boolean ascending, int offset, int rows){
 
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
    	
    	List<String> ids = new ArrayList<String>();
		    	
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);    	    	
        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        parameters.setIncludeScore(true);
	        
	        parameters.setStart(offset);
//	        parameters.setRows(10000);
	        parameters.setRows(1000);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);

	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (samples_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = samples_server.query(parameters);
        	SolrDocumentList sdl = qr.getResults();
                        
    	    for(SolrDocument doc : sdl)
    	    {
    	    	// remove duplicate GEO entries
    	    	String id = doc.getFieldValue("SAMPLE_GEO_ID").toString();
    	    	if (!ids.contains(id)){
    	    		ids.add(id);
    	    	}    	    	
    		}
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        


        return ids;
    }
   
    /**
     * This method runs the queryString against the gudmap_samples solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     */
    public SolrDocumentList getSamplesViewData(List<String> ids, String column, boolean ascending, int offset, int rows){
    	
    	SolrDocumentList sdl = new SolrDocumentList();
    	String queryString = "";
    	for (String s : ids)
    		queryString += s + ",";
    	
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);

        try
        {
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setIncludeScore(true);	        
	        parameters.setStart(offset);
	        parameters.setRows(rows);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
      
//	      	parameters.addField("GUDMAP_ID");
//	      	parameters.addField("GUDMAP");
//        	parameters.addField("SAMPLE_GEO_ID");
//        	parameters.addField("STAGE");
//        	parameters.addField("DEV_STAGE");
//        	parameters.addField("SOURCE");
////        	parameters.addField("PI_NAME");
//        	parameters.addField("DATE");
//        	parameters.addField("SEX");
//        	parameters.addField("DESCRIPTION");
//        	parameters.addField("TITLE");
//        	parameters.addField("SERIES_GEO_ID");
//        	parameters.addField("COMPONENT");
//        	parameters.addField("QMC_ALE_GENE");
//        	parameters.addField("ASSAY_TYPE");
//        	parameters.addField("SPECIMEN_ASSAY_TYPE");
//        	parameters.addField("PER_OID");
//        	parameters.addField("PLATFORM_GEO_ID");
//        	parameters.addField("SPECIES");
//        	parameters.addField("GENOTYPE");
        	
            QueryResponse qr = samples_server.query(parameters);
        	sdl = qr.getResults();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        


        return sdl;
    }

    //***************************** MICROARRAY METHODS *****************************************************

//    public int getMicroarrayFilteredCount(String queryString, HashMap<String,String> filters){
//
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//
//    	int count = 0;
//
//        try
//        {
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setFacet(true);
//	      	parameters.setRows(0);	      
//	        parameters.setFacetMinCount(0);
//	        parameters.setFacetLimit(1000);
//	        	      	        
//	        parameters.addFacetPivotField("SAMPLE_GEO_ID,GUDMAP"); 	        
//	        
//	
//	        if (filters != null){
//		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
//		        while (it.hasNext()) {
//		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
//		            if (microarray_schema.contains(pair.getKey()))
//		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
//		        }
//	        }
//	        
//            QueryResponse qr = microarray_server.query(parameters);
//            
//            NamedList<List<PivotField>> pivots = qr.getFacetPivot();
//
//            for(Map.Entry<String, List<PivotField>> entry : pivots ) {
//	  	          if (entry.getKey().equalsIgnoreCase("SAMPLE_GEO_ID,GUDMAP")){
//	  	        	  	count = entry.getValue().size();
//	  	          }
//            }  
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//			e.printStackTrace();
//		}        
//
//        return count;    
//    }
    

    // method to retrieve the Microarray data for the results page
//    public List<String> getMicroarrayData(String queryString, HashMap<String,String> filters, String column, boolean ascending, int offset, int rows){
// 
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//   	
//    	
//    	List<String> ids = new ArrayList<String>();
//		    	
//    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);    	    	
//        try
//        {
//
//
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setIncludeScore(true);	        
//	        parameters.setStart(offset);
////	        parameters.setRows(10000);
//	        parameters.setRows(1000);
//	        if (!column.equalsIgnoreCase("RELEVANCE"))
//	        	parameters.setSort(column, order);
//      
//	        if (filters != null){
//		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
//		        while (it.hasNext()) {
//		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
//		            if (microarray_schema.contains(pair.getKey()))
//		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
//		        }
//	        }
//
//            QueryResponse qr = microarray_server.query(parameters);
//         	SolrDocumentList sdl = qr.getResults();
//
//         	
//         	
//         	
//    	    for(SolrDocument doc : sdl)
//    	    {
//    	    	// remove duplicate GEO entries
//    	    	String id = doc.getFieldValue("SAMPLE_GEO_ID").toString();
//    	    	if (!ids.contains(id)){
//    	    		ids.add(id);
//    	    	}    	    	
//    		}
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } 
//        catch (IOException e) {
//			e.printStackTrace();
//		}        
//
//        return ids;
//    }

//    public SolrDocumentList getMicroarrayViewData(List<String> ids, String column, boolean ascending, int offset, int rows){
//    	
//    	
//    	SolrDocumentList sdl = new SolrDocumentList();
//    	String queryString = "";
//    	for (String s : ids)
//    		queryString += s + ",";
//    	
//    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
//
//        try
//        {
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setIncludeScore(true);	        
//	        parameters.setStart(offset);
//	        parameters.setRows(rows);
//	        if (!column.equalsIgnoreCase("RELEVANCE"))
//	        	parameters.setSort(column, order);
//      
//	      	parameters.addField("GUDMAP");
//        	parameters.addField("SAMPLE_GEO_ID");
//        	parameters.addField("STAGE");
//        	parameters.addField("DEV_STAGE");
//        	parameters.addField("PI_NAME");
//        	parameters.addField("DATE");
//        	parameters.addField("SEX");
//        	parameters.addField("DESCRIPTION");
//        	parameters.addField("TITLE");
//        	parameters.addField("SERIES_GEO_ID");
//        	parameters.addField("COMPONENT");
//        	parameters.addField("GENOTYPE");
//        	parameters.addField("ASSAY_TYPE");
//        	parameters.addField("SPECIMAN_ASSAY_TYPE");
//        	parameters.addField("PER_OID");
//        	parameters.addField("PLATFORM_GEO_ID");
//        	
//            QueryResponse qr = samples_server.query(parameters);
//        	sdl = qr.getResults();
//
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//			e.printStackTrace();
//		}        
//
//
//        return sdl;
//    }

//    //***************************** TUTORIAL METHODS *****************************************************
//
//
// 	/**
// 	 * This method return the count of tutorials for a search query
// 	 * @param queryString This is the search query
// 	 * @return int This returns the count of the query
// 	 */
//    public int getTutorialCount(String queryString){
//    	return getTutorialCount(queryString, null);
//    }
//    
// 	/**
// 	 * This method return the count of tutorials for a search query with
// 	 * modifying filter and modifying filter list
// 	 * @param queryString This is the search query
// 	 * @param filters The list of filters used to modify the query
// 	 * @return int This returns the count of the query
// 	 */    
//
//    public int getTutorialCount(String queryString, HashMap<String,String> filters){
//
//    	long count = 0;
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//		else
//			queryString = setWidcard(queryString);
//
//        try
//        {
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        parameters.setRows(0);
//
////	        if (filters != null){
////		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
////		        while (it.hasNext()) {
////		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
//////		            if (tutorial_schema.contains(pair.getKey()))
////		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
////		        }
////	        }
//	        
//            QueryResponse qr = tutorial_server.query(parameters);
//            SolrDocumentList sdl = qr.getResults();
//            count = sdl.getNumFound();
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//			e.printStackTrace();
//		}        
//
//
//        return (int)count;    
//    }
//
//    /**
//     * This method retuns a SolrDocumentList for a query on the tutorials index
//	 * @param queryString The main query string for retrieving relevant documents'
//     * @param filters The filters to be applied to the solr search
//     * @param column The field on which the result should be sorted.
//     * @param ascending The sort direction
//     * @param offset The offset from which the documents will be returned.
//     * @param rows The number of documents to be retrieved in the result set.
//     * @return sdl The SolrDocumentList
//     */
//    public SolrDocumentList getTutorialData(String queryString, HashMap<String, String> filters, String column, boolean ascending, int offset, int rows){
//    	
//		if (queryString == "" || queryString == null || queryString == "*")
//			queryString = "*:*";
//		else
//			queryString = setWidcard(queryString);
//
//		
//    	SolrDocumentList sdl = null;
//    	
//    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
//
//        try
//        {
//
//            SolrQuery parameters = new SolrQuery();
//	        parameters.set("q",queryString);
//	        
//	        parameters.setIncludeScore(true);
//	        
//	        parameters.setStart(offset);
//	        parameters.setRows(rows);
//	        if (!column.equalsIgnoreCase("RELEVANCE"))
//	        	parameters.setSort(column, order);
//      
//	        
////	        if (filters != null){
////		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
////		        while (it.hasNext()) {
////		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
////		            if (tutorial_schema.contains(pair.getKey()))
////		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
////		        }
////	        }
//        	
//	        QueryResponse qr = tutorial_server.query(parameters);
//            sdl = qr.getResults();
//
//        }
//        catch (SolrServerException e)
//        {
//            e.printStackTrace();
//        } catch (IOException e) {
//			e.printStackTrace();
//		}        
//
//        return sdl;
//    }
//   
//    
//    public int getTutorialOverviewCount(String queryString){
//        HashMap<String,String> filter = new HashMap<String,String>();
//        filter.put("title","Tutorial overview"); 	
//    	return getTutorialCount(queryString, filter);
//    }
//
//    public int getTutorialDevMRSCount(String queryString){
//        HashMap<String,String> filter = new HashMap<String,String>();
//        filter.put("title","Dev MRS Tutorial");
//    	return getTutorialCount(queryString, filter);
//    }
//
//    public int getTutorialDevMUSCount(String queryString){
//        HashMap<String,String> filter = new HashMap<String,String>();
//        filter.put("title","Dev MUS Tutorial");
//    	return getTutorialCount(queryString, filter);
//    }
    
    //***************************** MOUSE STRAINS METHODS *****************************************************
    
 	/**
     * This method runs the queryString against the gudmap_images solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
 	 */    
    public int getMouseStrainsCount(String queryString, HashMap<String,String> filters){

		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);
    	
    	long count = 0;

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);

	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (mouse_strain_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = mouse_strain_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
            count = sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        }        
        catch (IOException e) {

			e.printStackTrace();
		}

        return (int)count;    
    }


    /**
     * This method runs the queryString against the gudmap_mousestrains solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     */
    public SolrDocumentList getMouseStrainsData(String queryString, HashMap<String, String> filters, String column, boolean ascending, int offset, int rows){
    	
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		
    	SolrDocumentList sdl = null;
    	
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        parameters.setIncludeScore(true);
	        
	        parameters.setStart(offset);
	        parameters.setRows(rows);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
      
	      	parameters.addField("ID");
        	parameters.addField("GENE");
        	parameters.addField("REPORTER_ALLELE");
        	parameters.addField("ALLELE_TYPE");
        	parameters.addField("ALLELE_VER");
        	parameters.addField("ALLELE_VER_URL");
        	parameters.addField("ALLELE_CHAR");
        	parameters.addField("ALLELE_CHAR_URL");
        	parameters.addField("STRAIN_AVA");
        	parameters.addField("STRAIN_AVA_URL");
        	parameters.addField("ORGAN");
        	parameters.addField("CELL_TYPE");

	        
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (mouse_strain_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
        	
	        QueryResponse qr = mouse_strain_server.query(parameters);
            sdl = qr.getResults();

        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        

        return sdl;
    }

    //***************************** IMAGES METHODS *****************************************************

 	/**
     * This method runs the queryString against the gudmap_images solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
 	 */    
    public int getImagesCount(String queryString, HashMap<String,String> filters){

    	long count = 0;
    	
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);

	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (image_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
            QueryResponse qr = image_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
            count = sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        


        return (int)count;    
    }


    /**
     * This method runs the queryString against the gudmap_images solr index.
     * It returns a SolrDocumentList object containing the retrieved documents.
     * 
	 * @param queryString The main query string for retrieving relevant documents'
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted.
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned.
     * @param rows The number of documents to be retrieved in the result set.
     * @return A SolrDocumentList of retrieved documents
     */
    public SolrDocumentList getImagesData(String queryString, HashMap<String, String> filters, String column, boolean ascending, int offset, int rows){
    	
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		SolrDocumentList sdl = null;

    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);

        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        parameters.setIncludeScore(true);
	        
	        parameters.setStart(offset);
	        parameters.setRows(rows);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
      
//        	parameters.addField("GENE");
 //       	parameters.addField("GUDMAP_ID");
//        	parameters.addField("SPECIMEN_ASSAY_TYPE");
//        	parameters.addField("SOURCE");
//        	parameters.addField("SEX");
 //       	parameters.addField("STAGE");
//        	parameters.addField("IMAGE_NOTE");
//        	parameters.addField("IMAGE_PATH");
//        	parameters.addField("THUMBNAIL_PATH");
//           	parameters.addField("IMAGE_CLICK_PATH");
//           	parameters.addField("IMAGE_TYPE");
//           	parameters.addField("SPECIES");
           	
	        if (filters != null){
		        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
		        while (it.hasNext()) {
		            Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		            if (image_schema.contains(pair.getKey()))
		            	parameters.addFilterQuery(pair.getKey() + ":" + pair.getValue());
		        }
	        }
	        
	        QueryResponse qr = image_server.query(parameters);
            sdl = qr.getResults();
            
//            SolrDocumentList docs = new SolrDocumentList();
//            ArrayList<String> genes = new ArrayList<String>();
//            for(SolrDocument doc:sdl){
//            	if (!genes.contains(doc.getFieldValue("GUDMAP_ID").toString())){
//            		genes.add(doc.getFieldValue("GUDMAP_ID").toString());
//            		docs.add(doc);
//            		
//            	}
//            }
//            sdl = docs;
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        

        return sdl;
    }
    //***************************** WEB METHODS *****************************************************


 	/**
     * This method runs the queryString against the gudmap_web solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
 	 * @return The number of documents retrieved in the result set
 	 */    
   public int getWebCount(String queryString){
    	return getWebCount(queryString, null);
    }
    
 	/**
     * This method runs the queryString against the gudmap_web solr index.
     * It returns the number of documents found.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
 	 * @return The number of documents retrieved in the result set
 	 */    
    public int getWebCount(String queryString, HashMap<String,String> filters){

    	long count = 0;
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

        try
        {
            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        parameters.setRows(0);
	        
            QueryResponse qr = web_server.query(parameters);
            SolrDocumentList sdl = qr.getResults();
            count = sdl.getNumFound();
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        


        return (int)count;    
    }

    /**
     * This method runs the queryString against the gudmap_web solr index.
     * It returns a QueryResponse object.
     * 
	 * @param queryString The main query string for retrieving relevant documents
     * @param filters A list of filters to be applied to the solr search
     * @param column The field on which the result should be sorted
     * @param ascending The sort direction
     * @param offset The offset from which the documents will be returned
     * @param rows The number of documents to be retrieved in the result set
     * @return A Solr QueryResponse
     */
    public QueryResponse getWebData(String queryString, HashMap<String, String> filters, String column, boolean ascending, int offset, int rows){
    	
		if (queryString == "" || queryString == null || queryString == "*")
			queryString = "*:*";
		else
			queryString = setWidcard(queryString);

		
    	SolrDocumentList sdl = null;
    	
    	ORDER order = (ascending == true ? ORDER.asc: ORDER.desc);
    	QueryResponse qr = null;
        try
        {

            SolrQuery parameters = new SolrQuery();
	        parameters.set("q",queryString);
	        
	        parameters.setIncludeScore(true);
	        
	        parameters.setStart(offset);
	        parameters.setRows(rows);
	        if (!column.equalsIgnoreCase("RELEVANCE"))
	        	parameters.setSort(column, order);
      
	        parameters.setHighlight(true);  
	        parameters.addHighlightField("CONTENT");
	        parameters.setHighlightSimplePre("<strong>");	 
	        parameters.setHighlightSimplePost("</strong>");	 
	        parameters.setHighlightFragsize(350);
	        parameters.setHighlightSnippets(10);

	        qr = web_server.query(parameters);

        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}        

        return qr;
    }
   
    

    
       
    public String getGenelistIds(){
    	return genelistids;
    }
    
    public String getGenes(){
    	return genes;
    }
    
//    public int getIshExpressionCount(){
//    	return ishExpressionCount;
//    }
//    
//    public void setIshExpressionCount(PivotField p){
//    	ishExpressionCount += p.getPivot().size();
//    }

    //***************************** SCHEMA METHODS *****************************************************
     
	/**
	 * @return A set containing the valid field names of the gudmap_genes index schema
	 */
	public  Set<String> getGeneSchema() {
    	
		Set<String> schema = new HashSet<String>();
    
    	schema.add("GENE");
    	schema.add("GENE_NAME");
    	schema.add("MGI_GENE_ID");
    	schema.add("MGI");
    	schema.add("ENSEMBL_ID");
    	schema.add("SYNONYMS");
    	schema.add("PROBESETS");
    	schema.add("ENTREZ_ID");
    	schema.add("GENBANK_ID");
    	schema.add("INSITU_ASSAY");
    	schema.add("MA_ASSAY");
    	schema.add("GUDMAP");
	    schema.add("GUDMAP_IDS");
	    schema.add("PRESENT");
	    schema.add("DIR_PRESENT");
	    schema.add("NOT_DETECTED");
	    schema.add("UNCERTAIN");
	    schema.add("EMAPS");
	    schema.add("SOURCE");
	    schema.add("PI_NAME");
	    schema.add("LAB");
	    schema.add("ANCHOR");
	    schema.add("MARKER");   
		schema.add("GENE_TYPE"); 	    
		schema.add("SPECIES"); 
		
		return schema;
	}
    
	/**
	 * @return A set containing the valid field names of the gudmap_mousestrains index schema
	 */
	public Set<String> getMouseStrainsSchema() {
    	
		Set<String> schema = new HashSet<String>();
    
		schema.add("ID");
		schema.add("GENE");
		schema.add("REPORTER_ALLELE"); 
		schema.add("ALLELE_TYPE");
		schema.add("ALLELE_VER");
		schema.add("ALLELE_VER_URL");
		schema.add("ALLELE_CHAR");
		schema.add("ALLELE_CHAR_URL");   
		schema.add("STRAIN_AVA");
		schema.add("STRAIN_AVA_URL");   
		schema.add("ORGAN");   
		schema.add("CELL_TYPE");
	    
	    return schema;
	}

	/**
	 * @return A set containing the valid field names of the gudmap_insitu index schema
	 */
	public Set<String> getInsituSchema() {
   	
	   Set<String> schema = new HashSet<String>();

	   schema.add("GUDMAP"); 
	   schema.add("GUDMAP_ID"); 
	   schema.add("GENE"); 
	   schema.add("GENE_NAME");
	   schema.add("MGI");
	   schema.add("MGI_GENE_ID");
	   schema.add("GENBANK_ID");
	   schema.add("ENSEMBL_ID");
	   schema.add("SYNONYMS");
	   schema.add("PI_NAME");   
	   schema.add("LAB");   
	   schema.add("AUTHORS");
	   schema.add("DATE");
	   schema.add("STAGE");
	   schema.add("PROBE_NAME");
	   schema.add("CLONE_NAME");
	   schema.add("PROBE_TISSUE");
	   schema.add("PROBE_ID");
	   schema.add("maprobe");
	   schema.add("MAPROBE_ID");
	   schema.add("PROBE_STRAIN");
	   schema.add("PROBE_GENE_TYPE");
	   schema.add("PROBE_TYPE");
	   schema.add("PROBE_VISUAL_METHOD");
	   schema.add("PROBE_NOTE");
	   schema.add("CURATOR_NOTE");
	   schema.add("RESULT_NOTE");
	   schema.add("EXPERIMENT_NOTE");   
	   schema.add("IMAGE_WITH_NOTE");
	   schema.add("IMAGE_NOTE");
	   schema.add("IMAGE");
	   schema.add("IMAGE_PATH");
	   schema.add("SPECIMEN_ASSAY_TYPE");
	   schema.add("FIXATION_METHOD");
	   schema.add("STRAIN");
	   schema.add("SEX");
	   schema.add("DEV_STAGE");
	   schema.add("GENOTYPE");
	   schema.add("ASSAY_TYPE");
	   schema.add("PROJECT");
	   schema.add("ALT_ID");
	   schema.add("SOURCE");
	   schema.add("ANCHOR_GENE");
	   schema.add("MARKER_GENE");
	   schema.add("FOCUS_GROUPS");
	   schema.add("ALLELE_MGI_ID");
	   schema.add("ALLELE_NAME");
	   schema.add("ALLELE_TYPE");
	   schema.add("PRESENT");
	   schema.add("DIR_PRESENT");
	   schema.add("INF_PRESENT");
	   schema.add("NOT_DETECTED");
	   schema.add("INF_NOT_DETECTED");
	   schema.add("UNCERTAIN");
	   schema.add("EMAPS");
	   schema.add("EXP_NOTES");
	   schema.add("EXPRESSION_NOTES");
	   schema.add("ANNOTATION");
	   schema.add("TISSUE_TYPE");
	   schema.add("SPECIES"); 
	   schema.add("GENE_TYPE"); 
		   
	   return schema;
    }

	/**
	 * @return A set containing the valid field names of the gudmap_nextgen_samples index schema
	 */
	public Set<String> getSequenceSamplesSchema() {
   	
	   	Set<String> schema = new HashSet<String>();
	   
	    schema.add("GUDMAP");
	    schema.add("SAMPLE_GEO_ID");
	    schema.add("SERIES_GEO_ID");
	    schema.add("SOURCE");
	    schema.add("LIBRARY_STRATEGY");
	    schema.add("STAGE");
	    schema.add("PI_NAME");
	    schema.add("DEV_STAGE");
	    schema.add("DATE");
	    schema.add("SEX");
	    schema.add("SAMPLE_DESCRIPTION");
	    schema.add("SAMPLE_NAME");
	    schema.add("GENOTYPE");
	    schema.add("COMPONENT");
	    schema.add("SPECIES"); 
    
		return schema;
    }

	/**
	 * @return A set containing the valid field names of the gudmap_samples index schema
	 */
	public Set<String> getSamplesSchema() {
		   	
		Set<String> schema = new HashSet<String>();
		   
		schema.add("GUDMAP");
		schema.add("SAMPLE_GEO_ID");
		schema.add("STAGE");
		schema.add("DEV_STAGE");
		schema.add("SOURCE");
		schema.add("PI_NAME");
		schema.add("DATE");
		schema.add("SEX");
		schema.add("DESCRIPTION");
		schema.add("TITLE");
		schema.add("SERIES_GEO_ID");
		schema.add("COMPONENT");
		schema.add("QMC_ALE_GENE");
		schema.add("ASSAY_TYPE");
		schema.add("SPECIMEN_ASSAY_TYPE");
		schema.add("PER_OID");
		schema.add("PLATFORM_GEO_ID"); 
		schema.add("SPECIES"); 
		schema.add("GENOTYPE"); 
		
		return schema;
	}
   
	/**
	 * @return A set containing the valid field names of the gudmap_tissues index schema
	 */
	public Set<String> getTissuesSchema() {
		   	
		Set<String> schema = new HashSet<String>();
		   
		schema.add("ID");
		schema.add("NAME");
		schema.add("SYNONYM");
		schema.add("STAGES");
		schema.add("FOCUS_GROUPS");
		schema.add("GENELIST_IDS");
		schema.add("GENELIST_NAMES");
		schema.add("IMAGE_URL");
		schema.add("IMAGE_NAME");
		schema.add("SEC_NAMES");
		schema.add("LINK_TYPES");
		schema.add("GUDMAP");
		schema.add("EMAP");
		schema.add("EMAPIDS");
		schema.add("COMPONENT");
		schema.add("MGI");
		schema.add("GENE_MGI_ID");
		schema.add("GENE");
		schema.add("PROBE_IDS");
		schema.add("MA_PROBES_ID");
		schema.add("maprobe");
		schema.add("STAGE");  
	    
		return schema;
	}   


	/**
	 * @return A set containing the valid field names of the gudmap_images index schema
	 */
	public Set<String> getGeneListsSchema() {
		   	
	   	Set<String> schema = new HashSet<String>();
	   
	    schema.add("ID");
	    schema.add("NAME");
	    schema.add("DESCRIPTION"); 
	    schema.add("PLATFORM");
	    schema.add("KEY_SAMPLE");
	    schema.add("MA_DATASET");
	    schema.add("MA_DATASET_ID");
	    schema.add("TOT_ENTITIES");   
	    schema.add("TOT_GENES");
	    schema.add("AUTHOR");
	    schema.add("DATE");   
	    schema.add("STAGE");   
	    schema.add("SEX");
	    schema.add("GENELIST_TYPE");
	    schema.add("ENTITIES");   
	    schema.add("GENE");
	    schema.add("GUDMAP_ID");
	    schema.add("GUDMAP");
	    schema.add("REF");   
	    schema.add("EMAP_IDS");   
	    schema.add("EMAP");   
	    schema.add("EMAP_TERM");   
		    
		return schema;
	}   
	   
	/**
	 * @return A set containing the valid field names of the gudmap_genelists index schema
	 */
	public Set<String> getImagesSchema() {
	   	
	   	Set<String> schema = new HashSet<String>();
	   
	    schema.add("IMAGE_ID");
	    schema.add("IMAGE");
	    schema.add("IMAGE_PATH");
	    schema.add("THUMBNAIL_PATH");
	    schema.add("IMAGE_CLICK_PATH");
	    schema.add("CLICK_FILENAME");
	    schema.add("IMAGE_NOTE");
	    schema.add("IMAGE_TYPE");
	    schema.add("GUDMAP");
	    schema.add("GUDMAP_ID");
	    schema.add("GENE");
	    schema.add("GENE_NAME");
	    schema.add("MGI");
	    schema.add("MGI_GENE_ID");
	    schema.add("GENBANK_ID");
	    schema.add("ENSEMBL_ID");
	    schema.add("SYNONYMS");
	    schema.add("PI_NAME");
	    schema.add("DATE");
	    schema.add("STAGE");
	    schema.add("PROBE_NAME");
	    schema.add("CLONE_NAME");
	    schema.add("PROBE_TISSUE");
	    schema.add("PROBE_ID");
	    schema.add("MAPROBE_ID");
	    schema.add("maprobe");
	    schema.add("PROBE_STRAIN");
	    schema.add("PROBE_GENE_TYPE");
	    schema.add("PROBE_TYPE");
	    schema.add("PROBE_VISUAL_METHOD");
	    schema.add("PROBE_NOTE");
	    schema.add("CURATOR_NOTE");
	    schema.add("RESULT_NOTE");
	    schema.add("EXPERIMENT_NOTE"); 
	    schema.add("SPECIMEN_ASSAY_TYPE");
	    schema.add("FIXATION_METHOD");
	    schema.add("STRAIN");
	    schema.add("SEX");
	    schema.add("DEV_STAGE");
	    schema.add("GENOTYPE");
	    schema.add("ASSAY_TYPE");
	    schema.add("PROJECT");
	    schema.add("ALT_ID");
	    schema.add("SOURCE");
	    schema.add("PRESENT");
	    schema.add("INF_PRESENT");
	    schema.add("EMAPS");
	    schema.add("EXPRESSION_NOTES");
	    schema.add("EXP_NOTES");
		schema.add("SPECIES"); 
		schema.add("GENE_TYPE"); 
		    
		return schema;
	}   
	   
}
