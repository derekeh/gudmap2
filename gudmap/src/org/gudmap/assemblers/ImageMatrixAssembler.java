package org.gudmap.assemblers;

import java.util.ArrayList;

import org.gudmap.dao.GeneStripDao;
import org.gudmap.models.submission.ImageInfoModel;

public class ImageMatrixAssembler {
	
	private ArrayList<String> imageIdList;
	private GeneStripDao geneStripDao;
	private ArrayList<ArrayList> imageInfoList = null;
	
	public ImageMatrixAssembler() {
		
	}
	
	public ImageMatrixAssembler(String geneSymbol){
		geneStripDao = new GeneStripDao();
		imageIdList = geneStripDao.retrieveImageIdsByGeneSymbol(geneSymbol);
	}
	
	 public void getData() {
			/*if (imageData != null && !isAnyParameterChenged()) {
			    return imageData;
			}*/
			ArrayList<ImageInfoModel>rawImageInfo = null;
			//ArrayList<ArrayList> imageInfoList = null;
			// if imageId is not null, get data by image id; or get data by gene
			if (imageIdList != null && imageIdList.size() > 0) {
			    rawImageInfo = geneStripDao.getInsituSubmissionImagesByImageId(imageIdList);
			}
			
			// re-construct the result for display
			if (rawImageInfo != null && rawImageInfo.size() != 0) {
			    imageInfoList = reconstructInsituSubmissionImageInfo(rawImageInfo);
			}
			
			//return imageInfoList;
	}
	 
	 private ArrayList<ArrayList> reconstructInsituSubmissionImageInfo(ArrayList<ImageInfoModel> rawImageInfo) {
			if (rawImageInfo != null) {
			    int numberOfImages = rawImageInfo.size();
			    ArrayList<ArrayList> imageResultList = new ArrayList<ArrayList>();
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
						imageInfoModel = (ImageInfoModel)rawImageInfo.get(j);
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
			//int numRows = Math.min(num, dataLength-offset);
			int numRows = dataLength;
			int numCols = getStages().size();
			int offset=0;
			
			ImageInfoModel[][] dataItems = new ImageInfoModel[numRows][numCols];
			ArrayList imagesOfGivenStage =null;
			int iNumber = -1;
			ImageInfoModel imageInfoItem = null;
			int idx = 0;
			String submissionId = null;
			String stage = null;
			String serialNumber = null;
		    String uniqueImage = null;//DEREK
			
			for (int i=0; i<numRows; i++)  
			    for (int j=0, col=0; j<imageInfoList.size(); j++) {
				
					imagesOfGivenStage = (ArrayList)imageInfoList.get(j);
					iNumber = imagesOfGivenStage.size();
					imageInfoItem = null;
					idx = i+offset;
					if (idx >= iNumber)
					    dataItems[i][col] = new ImageInfoModel();
					else {
					    imageInfoItem = (ImageInfoModel)imagesOfGivenStage.get(i+offset);
					   /* submissionId = imageInfoItem.getAccessionId();
					    stage = imageInfoItem.getStage();
					    serialNumber = imageInfoItem.getSerialNo();
					    uniqueImage = imageInfoItem.getUniqeImage();//derek
			
					    ArrayList<DataItem> complexValue = new ArrayList<DataItem>();
					    complexValue.add(new DataItem(submissionId, -1)); 	// Type=-1 means that the item will not display
					    complexValue.add(new DataItem(submissionId, submissionId, "ish_submission.html?id="+submissionId, 10)); // submission id
					    complexValue.add(new DataItem(stage,"", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/"+stage.toLowerCase()+"definition.html", 3));  // stage
					    DataItem zoomViewerItem = new DataItem(imageInfoItem.getFilePath(), "Click to open in the zoom viewer", imageInfoItem.getClickFilePath(), 14);
					    complexValue.add(zoomViewerItem);  // zoom viewer
					    complexValue.add(new DataItem(uniqueImage, -1));//DEREK
*/					    dataItems[i][col] = imageInfoItem;
					}
					col++;
			    }
			
			return dataItems; 
	    }
	 
	  private int getDataLength() {
			int dataLength = 0;
			int len = imageInfoList.size();
			int imageNumbers = 0;
			for (Object list: imageInfoList) {
			    imageNumbers = ((ArrayList)list).size();
			    if (imageNumbers > dataLength) {
			    	dataLength = imageNumbers;
			    }
			}
			return dataLength;
	 }
	  
	  public ArrayList<String> getStages() {
			ArrayList<String> stages = new ArrayList<String>();
			//imageData = getData();
			if (imageInfoList==null)
			    return stages;
			int len = imageInfoList.size();
			String stage = null;
			for(Object imagesForGivenStage : imageInfoList) {
			    stage = ((ImageInfoModel)((ArrayList)imagesForGivenStage).get(0)).getStage();
			    stages.add(stage);
			}
			return stages;
	    }

}
