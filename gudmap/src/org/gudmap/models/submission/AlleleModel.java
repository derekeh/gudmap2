package org.gudmap.models.submission;

public class AlleleModel {
	
	private boolean debug = false;
    private String title;
    private String geneSymbol;
    private String geneId;
	private String alleleId;
	private String alleleIdUrl;
    // allele symbol
	private String alleleName;
    private String reporter;
    private String visMethod;
    private String notes;
    private String alleleFirstChrom;
    private String alleleSecondChrom;
    private String type;
    
    public void print() {
    	System.out.println("title = "+title);
    	System.out.println("geneSymbol = "+geneSymbol);
    	System.out.println("alleleId = "+alleleId);
    	System.out.println("geneId = "+geneId);
    	System.out.println("alleleIdUrl = "+alleleIdUrl);
    	System.out.println("alleleName = "+alleleName);
    	System.out.println("reporter = "+reporter);
    	System.out.println("visMethod = "+visMethod);
    	System.out.println("notes = "+notes);
    	System.out.println("alleleFirstChrom = "+alleleFirstChrom);
    	System.out.println("alleleSecondChrom = "+alleleSecondChrom);
    	System.out.println("type = "+type);
     }
    
    public String getType() {
    	return type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }

    public String getTitle() {
    	return title;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getAlleleId() {
    	return alleleId;
    }
    
    public void setAlleleId(String input) {
    	alleleId = input;
	// if input is MGI accession, hardwire its url
	// otherwise, input cantains url
	if (null != input && -1 != input.indexOf("MGI:"))
    	    setAlleleIdUrl("http://www.informatics.jax.org/accession/" + input);

    }

    public String getAlleleIdUrl() {
    	return alleleIdUrl;
    }

    public void setAlleleIdUrl(String alleleIdUrl) {
        this.alleleIdUrl = alleleIdUrl;
    }

    public String getAlleleName() {
    	return alleleName;
    }
    
    public void setAlleleName(String input) {
    	alleleName = input;
	if (null != alleleName &&
	    -1 != alleleName.indexOf("<")) {
		alleleName = alleleName.replace("<", "##1");
		alleleName = alleleName.replace(">", "##2");
		alleleName = alleleName.replace("##1", "<sup>");
		alleleName = alleleName.replace("##2", "</sup>");
	}

    }
    
    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        reporter = reporter;
    }

    public String getVisMethod() {
        return visMethod;
    }

    public void setVisMethod(String visMethod) {
        this.visMethod = visMethod;
    }

    public String getNotes() {
    	return notes;
    }

    public void setNotes(String nts) {
        notes = nts;
        if (null != notes) {
           int index = notes.indexOf(";");
           if (-1 != index) {
	     String str1 = notes.substring(0, index);
	     String str2 = notes.substring(index + 1);
	     index = str1.indexOf(">");
	     if (-1 != index)
		 notes = str1.substring(0, index + 1)+geneSymbol+" "+str1.substring(index+1)+"&nbsp;&#38;";
	     else
		 notes = str1+"&nbsp;&#38;";
	     index = str2.indexOf(">");
	     if (-1 != index)
		 notes = notes + str2.substring(0, index + 1)+geneSymbol+" "+str2.substring(index+1);
	     else
		 notes = notes + str2;
	   }
	}
    }
    
    public String getAlleleFirstChrom() {
    	return alleleFirstChrom;
    }
    
    public void setAlleleFirstChrom(String input) {
    	alleleFirstChrom = input;
	if (null != alleleFirstChrom &&
	    -1 != alleleFirstChrom.indexOf("<")) {
		alleleFirstChrom = alleleFirstChrom.replace("<", "##1");
		alleleFirstChrom = alleleFirstChrom.replace(">", "##2");
		alleleFirstChrom = alleleFirstChrom.replace("##1", "<sup>");
		alleleFirstChrom = alleleFirstChrom.replace("##2", "</sup>");
	}
    }
    
    public String getAlleleSecondChrom() {
    	return alleleSecondChrom;
    }
    
    public void setAlleleSecondChrom(String input) {
    	alleleSecondChrom = input;
	if (null != alleleSecondChrom &&
	    -1 != alleleSecondChrom.indexOf("<")) {
		alleleSecondChrom = alleleSecondChrom.replace("<", "##1");
		alleleSecondChrom = alleleSecondChrom.replace(">", "##2");
		alleleSecondChrom = alleleSecondChrom.replace("##1", "<sup>");
		alleleSecondChrom = alleleSecondChrom.replace("##2", "</sup>");
	}
    }

}
