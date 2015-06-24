package org.gudmap.beans;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.gudmap.globals.Globals;
import org.gudmap.models.ArraySeqTableBeanModel;
import org.gudmap.models.submission.ProbeModel;
import org.gudmap.assemblers.ArraySeqSeriesBeanAssembler;
import org.gudmap.assemblers.MaProbeAssembler;

@Named
@RequestScoped
public class ArraySeqSeriesBean {
	private String seriesID="";
	private ArrayList<ArraySeqTableBeanModel> datalist=null;
	private ArraySeqSeriesBeanAssembler arraySeqAssembler;
	boolean isArray=false;
	private String platformID="";
	
	public ArraySeqSeriesBean() {
		
		/*FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facesContext.getExternalContext().getRequestParameterMap().get("arraySeriesID")!=null){
			seriesID = facesContext.getExternalContext().getRequestParameterMap().get("arraySeriesID");
			isArray=true;
		}
		if(facesContext.getExternalContext().getRequestParameterMap().get("seqSeriesID")!=null) {
			seriesID= facesContext.getExternalContext().getRequestParameterMap().get("seqSeriesID");
			isArray=false;
		}*/
		//seriesID="";
		if(Globals.getParameterValue("arraySeriesID")!=null){
			seriesID = Globals.getParameterValue("arraySeriesID");
			isArray=true;
		}
		if(Globals.getParameterValue("seqSeriesID")!=null) {
			seriesID= Globals.getParameterValue("seqSeriesID");
			isArray=false;
		}

	  
		
		arraySeqAssembler = new ArraySeqSeriesBeanAssembler(seriesID,isArray);
		datalist = arraySeqAssembler.getData();
	}
	
	public ArrayList<ArraySeqTableBeanModel> getDatalist() {
		return datalist;
	}
	
	public void setDatalist(ArrayList<ArraySeqTableBeanModel> datalist){
		this.datalist = datalist;
	}

}
