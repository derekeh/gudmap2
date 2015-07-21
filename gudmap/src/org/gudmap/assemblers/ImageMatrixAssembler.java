package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.GeneStripDao;
import org.gudmap.models.submission.ImageInfoModel;

public class ImageMatrixAssembler {
	
	private ArrayList<String> imageIdList;
	private GeneStripDao geneStripDao;
	private ArrayList<Object> imageInfoList = null;
	private ArrayList<String> stages = null;
	
	public ImageMatrixAssembler() {
		
	}
	
	public ImageMatrixAssembler(String geneId){
		geneStripDao = new GeneStripDao();
		imageIdList = geneStripDao.retrieveImageIdsByGeneId(geneId);
		stages = new ArrayList<String>();
	}
	
	 public void getData() {
			ArrayList<ImageInfoModel>rawImageInfo = null;
			// if imageId is not null, get data by image id; or get data by gene
			if (imageIdList != null && imageIdList.size() > 0) {
			    rawImageInfo = geneStripDao.getInsituSubmissionImagesByImageId(imageIdList);
			}
			
			// re-construct the result for display
			if (rawImageInfo != null && rawImageInfo.size() != 0) {
			    imageInfoList = reconstructInsituSubmissionImageInfo(rawImageInfo);
			}
	}
	 
	 private ArrayList<Object> reconstructInsituSubmissionImageInfo(ArrayList<ImageInfoModel> rawImageInfo) {
			if (rawImageInfo != null) {
			    int numberOfImages = rawImageInfo.size();
			    ArrayList<Object> imageResultList = new ArrayList<Object>();
			    int step = 0;
			    String tempStage = null;
			    ImageInfoModel imageInfoModel = null;
			    String stage = null;
			    for (int i=0;i<numberOfImages;i+=step) {
					step = 0;
					ArrayList<ImageInfoModel> imagesAtTheSameStage = new ArrayList<ImageInfoModel>();
					// go through the list and put images at the same stage into
					// different ArrayList objects and then put them together into
					// the final result ArrayList
					for (int j=i;j<numberOfImages;j++) {
						imageInfoModel = rawImageInfo.get(j);
					    stage = imageInfoModel.getStage();
					    step++;
					    if (tempStage == null) { // first image of the stage
							tempStage = stage;
							imagesAtTheSameStage.add(imageInfoModel);
					    } 
					    else { // follow-up images
							/** image at different stage, move on to the next stage */
							if (!stage.equals(tempStage)) { 
								imageResultList.add(imagesAtTheSameStage);
							    tempStage = null;
							    step--;
							    break;
							} 
							else { /** image at the same stage, add it */
							    imagesAtTheSameStage.add(imageInfoModel);
							}
					    }
					    if (j==(numberOfImages-1)) { /** last image */
					    	imageResultList.add(imagesAtTheSameStage);
					    }
					}
			    }
			    return imageResultList;
			}
			return null;
	 }
	 
	 
	 public ImageInfoModel[][] retrieveData() {
			
		 	getData();
			
			if (imageInfoList == null || imageInfoList.size() == 0) {
			    return null;
			}
			int dataLength = getDataLength();
			int numRows = dataLength;
			int numCols = retrieveStages().size();
			
			ImageInfoModel[][] dataItems = new ImageInfoModel[numRows][numCols];
			ArrayList<ImageInfoModel> imagesOfGivenStage =null;
			int iNumber = -1;
			ImageInfoModel imageInfoItem = null;
			for (int i=0; i<numRows; i++)  
			    for (int j=0, col=0; j<imageInfoList.size(); j++) {
				
					imagesOfGivenStage = (ArrayList<ImageInfoModel>)imageInfoList.get(j);
					iNumber = imagesOfGivenStage.size();
					imageInfoItem = null;
					if (i >= iNumber)
					    dataItems[i][col] = null; //using null can use EL in rendered statement for checkbox
						//dataItems[i][col] = new ImageInfoModel();
					else {
					    imageInfoItem = imagesOfGivenStage.get(i);
					    dataItems[i][col] = imageInfoItem;
					}
					col++;
			    }
			
			return dataItems; 
	    }
	 
	  private int getDataLength() {
			int dataLength = 0;
			int imageNumbers = 0;
			for (Object list: imageInfoList) {
			    imageNumbers = ((ArrayList<?>)list).size();
			    if (imageNumbers > dataLength) {
			    	dataLength = imageNumbers;
			    }
			}
			return dataLength;
	 }
	  
	  public ArrayList<String> retrieveStages() {
			//stages is global so can be referenced from bean
			if (imageInfoList==null)
			    return stages;
			String stage = null;
			for(Object imagesForGivenStage : imageInfoList) {
			    stage = ((ImageInfoModel)((ArrayList<?>)imagesForGivenStage).get(0)).getStage();
			    stages.add(stage);
			}
			return stages;
	    }
	  
	  public ArrayList<String> getStages() {
		  return stages;
	  }

}
