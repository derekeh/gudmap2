package org.gudmap.assemblers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.gudmap.utils.SolrUtil;
import org.gudmap.globals.Globals;
import org.gudmap.models.GeneStripModel;
import org.gudmap.models.MasterTableInfo;
import org.gudmap.models.submission.ImageInfoModel;
import org.gudmap.queries.genestrip.GeneStripQueries;
import org.gudmap.queries.generic.GenericQueries;
import org.gudmap.queries.submission.IshSubmissionQueries;
import org.gudmap.utils.Utils;


public class SolrGeneStripAssembler {

	private Connection con;
	private PreparedStatement ps;
	private ResultSet result;
	
	private Connection repeatCon=null;
	private PreparedStatement repeatPs=null;
	private ResultSet repeatResult=null;
	
	
	private GeneStripModel model;
	private SolrUtil solrUtil;
	private ArrayList<String> geneIds;

	public SolrGeneStripAssembler() {
		solrUtil = new SolrUtil();
	}
	
	public int getCount(String solrInput, HashMap<String,String> filterlist) {

		int n = 0;
		n = solrUtil.getGeneCount(solrInput,filterlist);
		
		return n;
	}

	public List<GeneStripModel> getData(String solrInput, HashMap<String,String> filterlist, String sortColumn, boolean ascending, int offset, int num){

		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
					
    	SolrDocumentList sdl  = solrUtil.getGudmapGenes(solrInput, filterlist, sortColumn,ascending,offset,num);
		if (sdl==null){
			return null;
		}
		list = formatTableData(sdl);

		return list;
	}

	private List<GeneStripModel> formatTableData(SolrDocumentList sdl){
		
		List<GeneStripModel> list = new ArrayList<GeneStripModel>();
		geneIds = new ArrayList<String>();
		
		int rowNum = sdl.size();
		
		for(int i=0; i<rowNum; i++) { 
			SolrDocument doc = sdl.get(i);

			String insituExpression = "";			
			if (doc.getFieldValue("PRESENT").toString() != "")
				insituExpression = "present";
			else if (doc.getFieldValue("UNCERTAIN").toString() != "")
				insituExpression = "uncertain";
			else if (doc.getFieldValue("NOT_DETECTED").toString() != "")
				insituExpression = "not detected";
			
			model = new GeneStripModel();
			
//			model.setOid(doc.getFieldValue("GUDMAP").toString());
//			model.setGene(doc.getFieldValue("GENE").toString());
//			model.setGudmap_accession(doc.getFieldValue("GUDMAP_ID").toString());
//			model.setSource(doc.getFieldValue("SOURCE").toString());
//			model.setSubmission_date(doc.getFieldValue("DATE").toString());
//			model.setAssay_type(doc.getFieldValue("ASSAY_TYPE").toString());
//			model.setProbe_name(doc.getFieldValue("PROBE_NAME").toString());
//			model.setStage(doc.getFieldValue("THEILER_STAGE").toString());
//			model.setAge(doc.getFieldValue("STAGE").toString());
//			model.setSex(doc.getFieldValue("SEX").toString());
//			model.setGenotype(doc.getFieldValue("GENOTYPE").toString());
//			model.setTissue(doc.getFieldValue("TISSUE_TYPE").toString());
//			model.setExpression(insituExpression);
//			model.setSpecimen(doc.getFieldValue("SPECIMEN_ASSAY_TYPE").toString());
//			model.setImage(doc.getFieldValue("IMAGE_PATH").toString());
//			model.setGene_id(doc.getFieldValue("MGI_GENE_ID").toString());
			

			String geneId = doc.getFieldValue("MGI_GENE_ID").toString();		
			String gene = doc.getFieldValue("GENE").toString();		

			model.setGeneSymbol(gene);
			model.setSynonyms(doc.getFieldValue("SYNONYMS").toString());
			model.setMgiId(geneId);
			
//				arrayRange = (result.getString("arrayRange"));
//				ishRange = (result.getString("ishRange"));
//				species = (result.getString("species"));
			model.setExpressionProfile(buildExpressionProfile(gene,geneId));
			model.setMicroarrayProfile(buildMicroarrayProfile(geneId));
//				geneStripModel.setStageRange(calculateStageRange(arrayRange,ishRange,species));
// 			    geneStripModel.setOmimCount(Integer.parseInt(result.getString("omim")));
			model.setImageUrl(getRepresentativeImage(geneId));
//				geneStripModel.setSelected(false);
			model.setGene_id(geneId);
//				geneStripModel.setSpecies(species);
			
			
			geneIds.add(geneId);

			list.add(model);			
		
		}
			
		return list;
	 }	

	public ArrayList<String> getGeneIds(){
		return geneIds;
	}
	
	private String buildExpressionProfile(String geneSymbol, String geneId) {
		String RET="";
		double[] insituExprofile = getInsituExprofile(geneId);
		String[] interestedAnatomyStructures = Globals.getInterestedAnatomyStructureIds();
		RET=getExpressionHtmlCode(insituExprofile,interestedAnatomyStructures,geneSymbol,geneId);
		return RET;
	}

	public static String getExpressionHtmlCode(double[] values, String[] focusGroups, String symbol, String geneId) {
		// added by xingjun - 08/05/2009 - its possible values is null
		if (values == null || values.length == 0) {
			return "";
		}
		
		String code = "<div id='exprLevelsGraph_" + symbol + "' style='text-align:center'></div>";
		// get focus group string list
		String focusGroupString = "";
		for (int i=0;i<focusGroups.length;i++) {
			focusGroupString += focusGroups[i] + "+','+";
		}
		focusGroupString = focusGroupString.substring(0, (focusGroupString.length()-1));
		
		// url
		String browseLink = "'browseGeneListTablePage.jsf?expressionGene=" + symbol + "&amp;focusGroup='";
		
		// concatenate script string
    	//code += "<script type='text/javascript'> //<![CDATA[ var val=";
    	code += "<script type='text/javascript'> var val=";
    	for (int i=0; i<values.length; i++)
    		code += ((i==0)? "" : "+','+") + String.valueOf(values[i]);
    	// focus group string
    	code += ";focusGroups=" + focusGroupString + ";";
    	// browse link string
    	code += "browseLink=" + browseLink + ";";
    	code += "geneSymbol='" + symbol + "';";
    	// javascript function
    	code += "prepareGraph(geneSymbol,val,browseLink,focusGroups); </script>";
    	return code;
	}
	
	private double[] getInsituExprofile(String geneId) {
		if (geneId == null || geneId.equals("")) {
			return null;
		}
		String[] interestedAnatomyStructures = Globals.getInterestedAnatomyStructureIds();
		
		// get relevant component id list
		int analen = interestedAnatomyStructures.length;
		int barHeight = Globals.getDefaultExpressionProfileBarHeight();
		// array to store expression profiles
		// 1: present; -1: not detected; 0: not examined/uncertain
		// need to include expression profile not related to given structures - others
//		double[] expressionProfiles = new double[analen+1];
		double[] insituExprofile = new double[analen];
		ArrayList<String> componentsOfAllGivenStructures = new ArrayList<String>();
					
			/** calculate expression profile for all given structures */
			for (int i=0;i<analen;i++) {
	//			System.out.println("structure: " + i);
				// get component ids
				String[] componentIds = (String[])Globals.getEMAPID().get(interestedAnatomyStructures[i]);
				
				// put component ids into componentIdsInAll arrayList
				int eLen = componentIds.length;
	//			System.out.println("component id number: " + eLen);
				for (int j=0;j<eLen;j++) {
					componentsOfAllGivenStructures.add(componentIds[j]);
				}
	
				// get expression info
				ArrayList<String[]> expressionOfGivenComponents = getGeneExpressionForStructure(geneId, componentIds, true);
				
				// start to calculate - only relevant expression exists
				double indicator = 0;
				if (expressionOfGivenComponents != null 
						&& expressionOfGivenComponents.size() != 0) {
					int compLen = expressionOfGivenComponents.size();
					// look for 'present'
					for (int j=0;j<compLen;j++) {
						String expression = ((String[])expressionOfGivenComponents.get(j))[1];
						if (expression.equalsIgnoreCase("present")) {
							indicator = 1.00;
							break;
						}
					}
					
					if (indicator == 0) { // there's no component with expression value of 'present'
						// look for 'not detected'
						for (int j=0;j<compLen;j++) {
							String expression = ((String[])expressionOfGivenComponents.get(j))[1];
							if (expression.equalsIgnoreCase("not detected")) {
								indicator = -1.00;
								break;
							}
						}
					}
				}
				
				// put calculation result into expression profile array
				insituExprofile[i] = indicator*barHeight;
			}
			/** return result */
			return insituExprofile;
		
		/** calculate expression profile for other structures */
		
	} // end of getInsituExprofile
	
	public ArrayList<String[]> getGeneExpressionForStructure(String geneId, String[] componentIds, boolean expressionForGivenComponents) {
		if (geneId == null || geneId.equals("")) {
		return null;
		}
		ArrayList<String[]> expressions = null;
		String componentClause = null;
		String componentString = " (";
		String expressionQuery="";
		if (expressionForGivenComponents) {
			expressionQuery = GeneStripQueries.GENE_EXPRESSION_FOR_GIVEN_STRUCTURE;
			componentClause = "AND EXP_COMPONENT_ID IN (";
		} 
		else {
			expressionQuery = GeneStripQueries.GENE_EXPRESSION_FOR_NONGIVEN_STRUCTURE;
			componentClause = "AND EXP_COMPONENT_ID NOT IN (";
		}
		// assemble parent component ids string
		int len = componentIds.length;
		for (int i=0;i<len;i++) {
			componentString += "'" + componentIds[i] + "', ";
		}
		componentString = componentString.substring(0, (componentString.length()-2)) + ")";
		
		// assemble full component ids string including child nodes as well as parent component ids
		String childQuery = GeneStripQueries.FIND_CHILD_NODE;
		componentClause += childQuery.replaceAll("WHERE ANCES_ATN.ATN_PUBLIC_ID IN", 
		("WHERE ANCES_ATN.ATN_PUBLIC_ID IN "+ componentString)) + ")";
		
		
		// use different query string according to what expressions we are looking for
		String queryString = null;
		if (expressionForGivenComponents) {
			queryString = expressionQuery.replace("AND EXP_COMPONENT_ID IN", componentClause);
		} 
		else {
			queryString = expressionQuery.replace("AND EXP_COMPONENT_ID NOT IN", componentClause);
		}
		try
		{
			repeatCon = Globals.getDatasource().getConnection();
			repeatPs = repeatCon.prepareStatement(queryString); 
			repeatPs.setString(1, geneId);
			repeatResult =  repeatPs.executeQuery();
			expressions = Utils.formatResultSetToArrayList(repeatResult);
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		}
		
		return expressions;
	}
	
	
	private MasterTableInfo[] buildMicroarrayProfile(String geneSymbol) {
		ArrayList<MasterTableInfo> masterTableInfoList=null;
		String queryString=GeneStripQueries.MASTER_SECTION_LIST;
		boolean found=false;
		////////////
		try
		{
			repeatCon = Globals.getDatasource().getConnection();
			repeatPs = repeatCon.prepareStatement(queryString); 
			repeatResult =  repeatPs.executeQuery();
			if (repeatResult.first()) {
				masterTableInfoList=new ArrayList<MasterTableInfo>();
				found=true;
				repeatResult.beforeFirst();
				 while (repeatResult.next()) {
						MasterTableInfo masterTableInfo = new MasterTableInfo();
						masterTableInfo.setId(repeatResult.getString(1) + "_" + repeatResult.getString(2));
						masterTableInfo.setMasterId(repeatResult.getString(1));
						masterTableInfo.setSectionId(repeatResult.getString(2));
						masterTableInfo.setTitle(repeatResult.getString(3));
						masterTableInfo.setDescription(repeatResult.getString(4));
						masterTableInfo.setPlatform(repeatResult.getString(5));
						masterTableInfoList.add(masterTableInfo);
			    }
			}
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		}
		
		return masterTableInfoList.toArray(new MasterTableInfo[masterTableInfoList.size()]);
		
		//THIS IS CODE TAKEN FROM JSF1. BERNIE TO IMPLEMENT USING D3 
		/*for (MasterTableInfo item : masterTableInfo) {
		    if (DbUtility.retrieveGeneProbeIds(geneSymbol, item.getPlatform()) != null) {//check to see if there is possible data for this symbol (it is to avoid refering to null images which display as a crsss icon in IE) 
				element = new DataItem("../dynamicimages/heatmap_" + geneSymbol + ".jpg?tile=5&masterTableId="+item.getId(), 
						       "Click to see " + item.getTitle() + " microarray expression profile for "+ symbol, 
						       "mastertable_browse.html?gene="+symbol+"&masterTableId="+item.getId()+"&cleartabs=true", 15);
				
				complexValue.add(element);
		    }		
		}*/
		////////////
		/*if(found)
			return masterTableInfoList.toArray(new MasterTableInfo[masterTableInfoList.size()]);
		else
			return null;*/
	}
	
  private String getRepresentativeImage(String geneSymbol){
	  if (geneSymbol == null || geneSymbol.equals("")) {
	    return null;
	  }
  
	  ArrayList<String[]> relatedSubmissionsList=null;
	  String queryString = GeneStripQueries.GENE_RELATED_SUBMISSIONS_ISH;
	  try
		{
		  repeatCon = Globals.getDatasource().getConnection();
		  repeatPs = repeatCon.prepareStatement(queryString); 
		  repeatPs.setString(1, geneSymbol);
		  repeatResult =  repeatPs.executeQuery();
		  relatedSubmissionsList = Utils.formatResultSetToArrayList(repeatResult);				
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		}
	  /*TODO 
	   * NOW GET THE URL BASED ON THE CANDIDATE ENTRIES
	   * 
	   */
	  String candidateSubmission = chooseRepresentativeInsituSubmission(relatedSubmissionsList);
	  String thumbnailURL = "";
	  ArrayList <ImageInfoModel> imageInfoModel=null;
			// get the image and put the url into the string
	 if (candidateSubmission != null) {
				//thumbnail = this.getThumbnailURL(conn, candidateSubmission);
			//data[i][5] = new DataItem(thumbnail, "Click to see image matrix for "+symbol, "image_matrix_browse.html?gene="+symbol, 13);
	 queryString = IshSubmissionQueries.IMAGE_INFO_BY_ACCESSION_ID;
	 try
		{
		 repeatCon = Globals.getDatasource().getConnection();
		 repeatPs = repeatCon.prepareStatement(queryString); 
		 repeatPs.setString(1, candidateSubmission);
		 repeatResult =  repeatPs.executeQuery();
		 imageInfoModel = Utils.formatImageResultSet(repeatResult);
			
			
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(repeatCon, repeatPs, repeatResult);
		} 
	    //return imageInfoModel;
	
	if (imageInfoModel == null || imageInfoModel.size() == 0) {
			thumbnailURL = "N/A";
		} 
		else {
				thumbnailURL = ((ImageInfoModel)imageInfoModel.get(0)).getFilePath();
		}
	 } 
	 return thumbnailURL;
			
	}

  private String chooseRepresentativeInsituSubmission(ArrayList<String[]> insituSubmissions) {
		if (insituSubmissions == null || insituSubmissions.size() == 0) {
			return null;
		}
		int len = insituSubmissions.size();
		String submissionId = null;
		/** TS23 **/
		// try to find submission at ts23 & with assay type 'section'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS23") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		// failed, try to find submission at ts23 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS23") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		/** TS21 **/
		// failed, try to find submission at ts21 & with assay type 'section'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS21") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		// failed, try to find submission at ts21 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS21") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		/** TS20 **/
		// failed, try to find submission at ts20 & with assay type 'section'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS20") && assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		// failed, try to find submission at ts20 & with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String stage = ((String[])insituSubmissions.get(i))[2];
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (stage.equals("TS20") && (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount"))) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		// failed, start from the submissions at stage ts17, head up to the end
		// try to find the first submission with assay type 'section'
		for (int i=0;i<len;i++) {
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (assayType.trim().equalsIgnoreCase("section")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				//String stage = ((String[])insituSubmissions.get(i))[2];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}
		
		// failed, start from the submissions at stage ts17, head up to the end
		// try to find the first submission with assay type 'wholemount'
		for (int i=0;i<len;i++) {
			String assayType = ((String[])insituSubmissions.get(i))[3];
			if (assayType.trim().equalsIgnoreCase("wholemount") || assayType.trim().equalsIgnoreCase("opt-wholemount")) {
				submissionId = ((String[])insituSubmissions.get(i))[0];
				//String stage = ((String[])insituSubmissions.get(i))[2];
				break;
			}
		}
		if (submissionId != null) { // found it!
			return submissionId;
		}

		// nothing found - impossible!!!
//		System.out.println("Nothing found!!!");
		return null;
	}
    
    //IMAGE MATRIX
    public ArrayList<String> retrieveImageIdsByGeneId(String geneId) {
    	if (geneId == null || geneId.equals("")) {
			return null;
		}
    	String queryString = GeneStripQueries.INSITU_SUBMISSION_IMAGE_ID_BY_GENE_ID;
    	ArrayList<String> imageList=null;
    	try
		{
			con = Globals.getDatasource().getConnection();
			ps = con.prepareStatement(queryString);
			ps.setString(1, geneId);
			result =  ps.executeQuery();
			if (result.first()) {
				imageList = new ArrayList<String>();
	        	result.beforeFirst();
	        	while (result.next()) {
	        		String imageId = result.getString(1);
	        		imageList.add(imageId);
	        	}					
			}				
		}
		catch(SQLException sqle){sqle.printStackTrace();}
		finally {
		    Globals.closeQuietly(con, ps, result);
		}
    	return imageList;
    	
    }
  
}
