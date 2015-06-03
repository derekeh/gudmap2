package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.inject.Named;

import org.gudmap.assemblers.GeneIndexAssembler;
import org.gudmap.utils.FacesUtil;

@Named
@RequestScoped
public class GeneIndexBean {
	
		private String[] index;
//		private ArrayDataModel rows;
		private HtmlDataTable myDataTable = new HtmlDataTable();
		private GeneIndexAssembler geneIndexAssembler;
		private String focusGroup;
		
		public GeneIndexBean() {

			index =new String[]{"A","B","C","D","E",
								"F","G","H","I","J",
								"K","L","M","N","O",
								"P","Q","R","S","T",
								"U","V","W","X","Y",
								"Z","0-9"};
			getData();
		}
	    
	    public String queryGenes() {

	    	//Visit.setStatusParam("query", FacesUtil.getRequestParamValue("query"));
	    	//Visit.setStatusParam("input", FacesUtil.getRequestParamValue("input"));
	    	return "AdvancedQuery";
	    }
	    
	    private void getData() {
			geneIndexAssembler = new GeneIndexAssembler();
			String index = FacesUtil.getRequestParamValue("index");
			if(null == index || index.equals(""))
				index = "A";
			Object[][] data = geneIndexAssembler.getGeneIndex(index,focusGroup);
			myDataTable.setValue(data);
	    }
	    
		public String[] getIndex() {
			return index;
		}

		public void setIndex(String[] index) {
			this.index = index;
		}

		public HtmlDataTable getMyDataTable() {
			return myDataTable;
		}

		public void setMyDataTable(HtmlDataTable myDataTable) {
			this.myDataTable = myDataTable;
		}
		
	/*
	    public ArrayDataModel getRows() {
			if (rows!=null)
				return rows;
		
			FocusGeneIndexAssembler assembler = new FocusGeneIndexAssembler();
			String focusedOrgan = Visit.getRequestParam("focusedOrgan");
			String index = FacesUtil.getRequestParamValue("index");
			if(null == index || index.equals(""))
				index = "A";
			Object[][] data = assembler.getGeneIndex(index, focusedOrgan);
			rows = new ArrayDataModel();
			rows.setWrappedData(data);
			myDataTable.setValue(data);
			return rows;
		}
		
		public void setRows(ArrayDataModel rows) {
			this.rows = rows;
		}
	*/

}
