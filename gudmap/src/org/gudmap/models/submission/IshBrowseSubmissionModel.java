package org.gudmap.models.submission;

public class IshBrowseSubmissionModel extends BrowseSubmissionModel {

	private String geneSymbol;
    private String assay;
    private String specimen;
    private String thumbnail;
    private String imageName;
    private String imgSerialNo = "1";

    public IshBrowseSubmissionModel() {
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }

    public String getAssay() {
        return assay;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
    
    public void setImgSerialNo(String imgSerialNo) {
        this.imgSerialNo = imgSerialNo;
    }

    public String getImgSerialNo() {
        return imgSerialNo;
    }
    
}
