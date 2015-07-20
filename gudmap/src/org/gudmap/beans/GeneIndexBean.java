package org.gudmap.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.inject.Named;

import org.gudmap.assemblers.GeneIndexAssembler;
import org.gudmap.globals.Globals;

@Named
@RequestScoped
public class GeneIndexBean {
	
		private String[] index;
		private HtmlDataTable myDataTable = new HtmlDataTable();
		private GeneIndexAssembler geneIndexAssembler;
		private String focusGroup;
		private String index_val;
		
		public GeneIndexBean() {

			index =new String[]{"A","B","C","D","E",
								"F","G","H","I","J",
								"K","L","M","N","O",
								"P","Q","R","S","T",
								"U","V","W","X","Y",
								"Z","0-9"};
			
		}
	    
	    
	    private void getData() {
			geneIndexAssembler = new GeneIndexAssembler();
			index_val = Globals.getParameterValue("index_val");
			if(null == index_val || index_val.equals(""))
				index_val = "A";
			Object[][] data = geneIndexAssembler.getGeneIndex(index_val,focusGroup);
			myDataTable.setValue(data);
	    }
	    
		public String[] getIndex() {
			return index;
		}

		public void setIndex(String[] index) {
			this.index = index;
		}
		
		public String getIndex_val() {
			return index_val;
		}

		public void setIndex_val(String index_val) {
			this.index_val = index_val;
		}

		public HtmlDataTable getMyDataTable() {
			getData();
			return myDataTable;
		}

		public void setMyDataTable(HtmlDataTable myDataTable) {
			this.myDataTable = myDataTable;
		}
		

}
