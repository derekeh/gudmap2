package org.gudmap.models.submission;

//import java.util.ArrayList;

public class AntibodyModel extends CommonAntibodyProbeModel{
	
	private String name;
	private String accessionId;
	private int seqStartLocation;
	private int seqEndLocation;
	private String url;
	private String supplier;
	private String catalogueNumber;
	private String lotNumber;
	private String host;
	private String hybridomaValue;
	private String phageDisplayValue;
	private String speciesImmunizedValue;
	private String purificationMethod;
	private String immunoglobulinIsotype;
	private String chainType;
	private String detectedVariantValue;
	private String speciesSpecificity;
	private String labelProduct;
	private String directLabel;
	private String signalDetectionMethod;
	private String uniprotId;
	private String detectionNotes;
	private String secondaryAntibody;
	private String dilution;
	private String experimentalNotes;
	private String labProbeId;
	private String locusTag;
	private String geneId;	
    private String curatorNotes;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccessionId() {
		return accessionId;
	}
	
	public void setAccessionId(String input) {
		accessionId = input;
		// need to be MGI accession
		if (null != accessionId) {
		    accessionId = accessionId.trim();
		    if (accessionId.equals(""))
			accessionId = null;
		}

		if (null != accessionId && 0 != accessionId.toLowerCase().indexOf("mgi:"))
		    accessionId = null;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	
    
    public int getSeqStartLocation() {
    	return seqStartLocation;
    }
    
    public void setseqStartLocation(int seqStartLocation) {
    	this.seqStartLocation = seqStartLocation;
    }
    
    public int getSeqEndLocation() {
    	return seqEndLocation;
    }
    
    public void setSeqEndLocation(int seqEndLocation) {
    	this.seqEndLocation = seqEndLocation;
    }
    
    
	public String getSupplier() {
		return supplier;
	}
	
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
	public String getCatalogueNumber() {
		return catalogueNumber;
	}
	
	public void setCatalogueNumber(String catalogueNumber) {
		this.catalogueNumber = catalogueNumber;
	}
	
	public String getLotNumber() {
		return lotNumber;
	}
	
	public void setLotNumber(String lotNumber) {
	    if (null == lotNumber)
		this.lotNumber = "";
	    else {
		if (lotNumber.equalsIgnoreCase("na"))
		    this.lotNumber = "";
		else
		    this.lotNumber = lotNumber;
	    }
	}
	
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getHybridomaValue() {
		return hybridomaValue;
	}
	
	public void setHybridomaValue(String hybridomaValue) {
		this.hybridomaValue = hybridomaValue;
	}
	
	public String getPhageDisplayValue() {
		return phageDisplayValue;
	}
	
	public void setPhageDisplayValue(String phageDisplayValue) {
		this.phageDisplayValue = phageDisplayValue;
	}
	
	public String getSpeciesImmunizedValue() {
		return speciesImmunizedValue;
	}
	
	public void setSpeciesImmunizedValue(String speciesImmunizedValue) {
		this.speciesImmunizedValue = speciesImmunizedValue;
	}
	
	public String getPurificationMethod() {
		return purificationMethod;
	}
	
	public void setPurificationMethod(String purificationMethod) {
		this.purificationMethod = purificationMethod;
	}
	
	public String getImmunoglobulinIsotype() {
		return immunoglobulinIsotype;
	}
	
	public void setImmunoglobulinIsotype(String immunoglobulinIsotype) {
		this.immunoglobulinIsotype = immunoglobulinIsotype;
	}
	
	public String getChainType() {
		return chainType;
	}
	
	public void setChainType(String chainType) {
		this.chainType = chainType;
	}
	
	public String getDetectedVariantValue() {
		return detectedVariantValue;
	}
	
	public void setDetectedVariantValue(String detectedVariantValue) {
		this.detectedVariantValue = detectedVariantValue;
	}
	
	public String getSpeciesSpecificity() {
		return speciesSpecificity;
	}
	
	public void setSpeciesSpecificity(String speciesSpecificity) {
		this.speciesSpecificity = speciesSpecificity;
	}
	
	public String getLabelProduct() {
		return labelProduct;
	}
	
	public void setLabelProduct(String labelProduct) {
		this.labelProduct = labelProduct;
	}
	
	public String getDirectLabel() {
		return directLabel;
	}
	
	public void setDirectLabel(String directLabel) {
		this.directLabel = directLabel;
	}
	
	public String getSignalDetectionMethod() {
		return signalDetectionMethod;
	}
	
	public void setSignalDetectionMethod(String signalDetectionMethod) {
		this.signalDetectionMethod = signalDetectionMethod;
	}
	

	public String getUniprotId(){
		return uniprotId;
	}
	
	public void setUniprotId(String uniprotId) {
		this.uniprotId = uniprotId;
	}

	public String getDetectionNotes() {
		return detectionNotes;
	}
	
	public void setDetectionNotes(String detectionNotes) {
		this.detectionNotes = detectionNotes;
	}

	public String getSecondaryAntibody() {
		return secondaryAntibody;
	}
	
	public void setSecondaryAntibody(String secondaryAntibody) {
		this.secondaryAntibody = secondaryAntibody;
	}

	public String getDilution(){
		return dilution;
	}
	
	public void setDilution(String dilution) {
		this.dilution = dilution;
	}

	public String getExperimentalNotes() {
		return experimentalNotes;
	}
	
	public void setExperimentalNotes(String experimentalNotes) {
		this.experimentalNotes = experimentalNotes;
	}

	public String getLabProbeId() {
		return labProbeId;
	}
	
	public void setLabProbeId(String labProbeId) {
		this.labProbeId = labProbeId;
	}

	public String getLocusTag() {
		return locusTag;
	}
	
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	public String getGeneId() {
		return geneId;
	}
	
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}

	public String getCuratorNotes() {
		return curatorNotes;
	}
	
	public void setCuratorNotes(String curatorNotes) {
		this.curatorNotes = curatorNotes;
	}
	
}
