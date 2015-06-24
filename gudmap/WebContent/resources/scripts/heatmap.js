// methods to interact with the d3 heatmap code
function gudmapColor(){
	//colors = ['#005824','#1A693B','#347B53','#4F8D6B','#699F83','#83B09B','#9EC2B3','#B8D4CB','#D2E6E3','#EDF8FB','#FFFFFF','#F1EEF6','#E6D3E1','#DBB9CD','#D19EB9','#C684A4','#BB6990','#B14F7C','#A63467','#9B1A53','#91003F'];
	colors = ['#0033CC','#1A47D1','#335CD6','#4D70DB','#6685E0','#8099E6','#99ADEB','#B3C2F0','#CCD6F5','#E6EBFA','#FFFFFF','#FFFAE6','#FFF5CC','#FFF0B3','#FFEB99','#FFE680','#FFE066','#FFDB4D','#FFD633','#FFD11A','#FFCC00'];	
	return colors;
}

function columnLabelArray(labels){
	var array = labels.split(",");
	return array;
}

function rowLabelArray(labels){
	var array = labels.split(",");
	return array;

}

function hccolArray(hccol){
	var array = hccol.split(",");
	var hccol = [];
	array.forEach(function(d){hccol.push(+d);});
	return hccol;

}
function hcrowArray(hcrow){
	var array = hcrow.split(",");	
    var hcrow = [];
    array.forEach(function(d){hcrow.push(+d);});
	return hcrow;
}

function dataArray(darray){
	var array = darray.split(",");
	var data = [];
	   array.forEach(function(d){data.push(d);});
	   var dataarray = [];
//	   var item = [];
	   var index;
	   for(index = 0; index < data.length; index++){
		   var item = {row:(1*data[index]),col:(1*data[index+1]),value:(1*data[index+2]),adjvalue:(1*data[index+3]),backgroundcolor:(data[index+4])};
		   dataarray[dataarray.length] = item;
		   index += 4;		   
	   }
	return dataarray;
}

function annotatedDataArray(annotations){
	var darray = annotations.split(",");
	var index;
	var dataset = [];
	for(index = 0, tmpDataset = []; index < darray.length; index++){
		var tmpDataset = [];
        tmpDataset.push(darray[index]);
        tmpDataset.push(darray[index+1]);
        tmpDataset.push(darray[index+2]);
        tmpDataset.push(darray[index+3]);
        tmpDataset.push(darray[index+4]);
        tmpDataset.push(darray[index+5]);
        tmpDataset.push(darray[index+6]);
        tmpDataset.push("GUDMAP");
        tmpDataset.push("UCSC");
        tmpDataset.push("KEGG");
        tmpDataset.push("ENS");
        
        index +=6;
        dataset.push(tmpDataset);
    }
    return dataset;
}
function annotatedDataArray2(annotations){
	var darray = annotations.split(",");
	var index,item;
	var dataset = [];
	for(index = 0, tmpDataset = []; index < darray.length; index++){
		var tmpDataset = [];
		item = {value:darray[index],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index+5],link:""};
        tmpDataset.push(item);
        
		item = {value:darray[++index],link:""};
        tmpDataset.push(item);
        
		item = {value:"GUDMAP",link:""};
        tmpDataset.push(item);
        
		item = {value:"UCSC",link:""};
        tmpDataset.push(item);
        
		item = {value:"KEGG",link:""};
        tmpDataset.push(item);
        
		item = {value:"ENS",link:""};
        tmpDataset.push(item);
       

        dataset.push(tmpDataset);
    }
    return dataset;
}


function colorDataArray(colors,length){
	var darray = colors.split(",");
	var i,j;
	var dataset = [];
	for(i = 0, tmpDataset = [];i < darray.length; i++){
		tmpDataset = [];
		for(j = 0; j < length; j++){
			var item = {color:darray[i+j], value:i+j};
			tmpDataset.push(item);
//			tmpDataset.push(darray[i+j]);
		}
        dataset.push(tmpDataset);
        i += length -1;
    }
    return dataset;
}

function valueDataArray(values,length){
	var darray = values.split(",");
	var i,j;
	var dataset = [];
	for(i = 0, tmpDataset = [];i < darray.length; i++){
		tmpDataset = [];
		for(j = 0; j < length; j++){
			var item = {value:darray[i+j]};
			tmpDataset.push(item);
//			tmpDataset.push(darray[i+j]);
		}
        dataset.push(tmpDataset);
        i += length -1;
    }
    return dataset;
}

function valueDataArray2(values1,values2,length){
	var array1 = values1.split(",");
	var array2 = values2.split(",");
	var i,j;
	var dataset = [];
	for(i = 0, tmpDataset = [];i < array1.length; i++){
		tmpDataset = [];
		for(j = 0; j < length; j++){
			var item = {value:array1[i+j],adjvalue:array2[i+j]};
			tmpDataset.push(item);
//			tmpDataset.push(darray[i+j]);
		}
        dataset.push(tmpDataset);
        i += length -1;
    }
    return dataset;
}


function getHeatmapColor(value){
	
	var colorValue = Math.round(value*255);
	if (colorValue < 0)
		return htmlColor(0, 0, -colorValue);
	else
		return htmlColor(colorValue, 0, 0);	
}

function htmlColor(r, g, b) {
	var res =  twoDigitHex(r) + twoDigitHex(g) + twoDigitHex(b);
	return "#" + res;
}

function twoDigitHex(value) {
	value = Math.min(255, value);
	value = Math.max(0, value);
	var hex = value.toString(16);
	if (hex.length < 2 )
		hex = "0" + hex;
	return hex;
}

function annotatedDataArray2(annotations){
	var annotationHeaders = ["Platform","Gene","Probe Seq ID","MGI Gene ID","Entrez Gene ID","Human Ortholog Symbol","Human Ortholog Entrez","GUDMAP-ISH","UCSC","KEGG","ENS"];
	var darray = annotations;
	var index;
	var dataset = [];
	var tmpDataset = [];
	var item;
	for(index = 0, tmpDataset = []; index < darray.length; index++){		
		item = {title:annotationHeaders[index],value:annotations[index]};
	 	tmpDataset.push(item); 
	}
    
    dataset.push(tmpDataset);

 return tmpDataset;
}

function annotatedDataArray3(annotations){
	var dataset = [];
	var item = {platform:annotations[0],gene:annotations[1],probe:annotations[2],mgi:annotations[3],entrez:annotations[4],hos:annotations[5],hoe:annotations[6],ish:annotations[7],ucsc:annotations[8],kegg:annotations[9],ens:annotations[10]};
	dataset.push(item); 

	return dataset;
}

function changePalette(paletteName, heatmapId) {
	var svg = d3.select(heatmapId);
	var t = svg.transition().duration(500);
	t.selectAll(".cell")
	     .style("fill", function(d) {
	    	 if (paletteName == "Default")
	             return getHeatmapColor(d.adjvalue);
	    	 else{
	    		 var classesNumber = 10;
	    		 var colors = colorbrewer[paletteName][classesNumber];
	    		 var colorScale = d3.scale.quantize()
	    		      .domain([-2.0, 2.0])
	    		      .range(colors);
	   		 
	    		 return colorScale(d.adjvalue);
	    	 }
	      });
}

function gudmap_heatmap(heatmapid, data, dataset2, headers, geneLabel, colLabel, rowLabel, cellSize, tooltip) {


	   var row_number = rowLabel.length;
       var hcrow = [];
       for (var i=1; i<row_number+1; i++)
    	   hcrow.push(1*i);

	   var col_number = colLabel.length;
	   
       var hccol = [];
       for (i=1; i<col_number+1; i++)
			hccol.push(1*i);
      
	   var margin = { top: 190, right: 10, bottom: 50, left: 100 },
	   width = cellSize*col_number*1.2, // - margin.left - margin.right,
	   height = cellSize*row_number, // - margin.top - margin.bottom,	
	   genes = geneLabel,
	   samples = colLabel;	
 
 var svg = d3.select(heatmapid).append("svg")
// .attr("width", width + margin.left + margin.right)
 .attr("width", width)
 .attr("height", height + margin.bottom + margin.top)
 .append("g")
 .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
 ;
 
 var rowSortOrder=false;
 var colSortOrder=false;
 
 var geneLabelLength = 0;

 	// some precalculation to get text length
	var geneLabels0 = svg.append("g")
	.selectAll(".geneLabelg")
	.data(geneLabel)
	.enter()
	.append("text")
	.text(function (d) { return d; })
	.attr("x", 0)
	.attr("y", function (d, i) { return (i * cellSize); })
	.style("text-anchor", "end")
	.style("visibility", "hidden")
	.attr("transform", "translate(50," + cellSize / 1.5 + ")")
//	.attr("class", function (d,i) { return "geneLabel mono r"+i;} ) 
	//     .on("click", function(d,i) {rowSortOrder=!rowSortOrder; sortbylabel("r",i,rowSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;})
	 .attr("width", function(d){ 
		 var len = this.parentNode.getBBox().width;
		 if (len > geneLabelLength) geneLabelLength = len;
		 })
	 ;
	geneLabelLength = 	geneLabelLength+10;
	
	 
	var geneLabels = svg.append("g")
	.selectAll(".geneLabelg")
	.data(geneLabel)
	.enter()
	.append("text")
	.text(function (d) { return d; })
	.attr("x", 0)
	.attr("y", function (d, i) { return (i * cellSize); })
	.style("text-anchor", "end")
	.attr("transform", "translate(" + geneLabelLength + "," + cellSize / 1.5 + ")")
	.attr("class", function (d,i) { return "geneLabel mono r"+i;} ) 
	.on("mouseover", function(d) {d3.select(this).classed("text-hover",true);})
	.on("mouseout" , function(d) {d3.select(this).classed("text-hover",false);})
	//     .on("click", function(d,i) {rowSortOrder=!rowSortOrder; sortbylabel("r",i,rowSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;})
	 .attr("width", function(d){ 
		 var len = this.parentNode.getBBox().width;
		 if (len > geneLabelLength) geneLabelLength = len;
		 })
	 ;
 
 
 
 
 var rowLabels = svg.append("g")
    .selectAll(".rowLabelg")
    .data(rowLabel)
    .enter().append("text")
    .text(function(d) { return d.count > 1 ? d.join("/") : d; })
    .attr("x", 0)
    .attr("y", function(d, i) {return (i * cellSize); })
    .style("text-anchor", "end")
    .attr("transform", function(d, i) { return "translate(0," + cellSize / 1.5 + ")"; })
 .attr("class", function (d,i) { return "rowLabel mono r"+i;} ) 
 .on("mouseover", function(d) {d3.select(this).classed("text-hover",true);})
 .on("mouseout" , function(d) {d3.select(this).classed("text-hover",false);})
 .on("click", function(d,i) {rowSortOrder=!rowSortOrder; sortbylabel("r",i,rowSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;})
 ;
	
 	var colLabelspacer = geneLabelLength + 10;
	var colLabels = svg.append("g")
		.selectAll(".colLabelg")
		.data(colLabel)
		.enter()
		.append("text")
		.text(function(d) { return d; })
		.attr("x", 0)
		.attr("y", function (d, i) { return (i * cellSize); })
		.style("text-anchor", "left")
//		.attr("transform", "translate(60,0) translate("+cellSize/2 + ",-6) rotate (-90)")
		.attr("transform", "translate("+ colLabelspacer +",0) translate("+ cellSize/2 + ",-6) rotate (-90)")
		//.attr("transform", "translate("+cellSize/2 + ",-6) rotate (-90)")
		.attr("class",  function (d,i) { return "colLabel mono c"+i;} )
		.on("mouseover", function(d) {d3.select(this).classed("text-hover",true);})
		.on("mouseout" , function(d) {d3.select(this).classed("text-hover",false);})
		.on("click", function(d,i) {colSortOrder=!colSortOrder;  sortbylabel("c",i,colSortOrder);d3.select("#order").property("selectedIndex", 4).node().focus();;})
		;		     
 
		var row = svg.selectAll(".row")
		.data(data)
		.enter().append("g")
		.attr("class", "row")
		.attr("id", function(d) {
		    return d.idx;
		});

		
//		var fixed = false;
	 	var rectLabelspacer = geneLabelLength + 6;
		var j = 0;
		var heatMap = row.selectAll(".cell")
		.data(function(d) {
		    j++;
		    return d;
		})
		.enter().append("svg:rect")
		.attr("x", function(d, i) {
//		    return (i * cellSize + 56);
		    return (i * cellSize + rectLabelspacer);
		})
		.attr("y", function(d, i, j) {
		    return j * cellSize;
		})
//		.attr("rx", 4)
//		.attr("ry", 4)
		.attr("class", function(d, i, j) {
		    return "cell bordered cr" + j + " cc" + i;
		})
		.attr("row", function(d, i, j) {
		    return j;
		})
		.attr("col", function(d, i, j) {
		    return i;
		})
		.attr("width", cellSize)
		.attr("height", cellSize)
		.style("fill", function(d) { return getHeatmapColor(d.adjvalue); })
		.on('mouseover', function(d, i, j) {
        d3.select(this).classed("cell-hover",true);
        d3.selectAll(".geneLabel").classed("text-highlight",function(r,ri){ return ri==j;});
        d3.selectAll(".rowLabel").classed("text-highlight",function(r,ri){ return ri==j;});
        d3.selectAll(".colLabel").classed("text-highlight",function(c,ci){ return ci==i;});
			tooltip.html('<div class="mytooltip">gene:' + genes[j] + '<br\/> sample:' + samples[i] + '<br\/> value:' + d.value + '</div>');
        tooltip.style("left", (d3.event.pageX-160) + "px")
        tooltip.style("top", (d3.event.pageY-100) + "px")
			tooltip.style("visibility", "visible");

		})
		.on('mouseout', function(d, i, j) {
        d3.select(this).classed("cell-hover",false);
        d3.selectAll(".geneLabel").classed("text-highlight",false);
        d3.selectAll(".rowLabel").classed("text-highlight",false);
        d3.selectAll(".colLabel").classed("text-highlight",false);
			tooltip.style("visibility", "hidden");
		})
		.on('click', function(d,i,j) {
			d3.select("#tabulate2").remove;
			var item = dataset2[j];
			var ds1 = [];
			ds1.push(item);
//			alert(ds1);
//			var ad = annotatedDataArray3(item);
//			alert(ad);
			
			tabulate(ds1, headers);

        d3.selectAll(".geneLabel").classed("text-selected",function(r,ri){ return ri==j;});
    	d3.selectAll(".rowLabel").classed("text-selected",function(r,ri){ return ri==j;});
        d3.selectAll(".colLabel").classed("text-selected",function(c,ci){ return ci==i;});

		}); 
		
		function tabulate(dataset2, columns) {
			jQuery("#annotations").empty();

			var mytable = d3.select("#annotations")
		    .append("table")
		    .style("border-collapse", "collapse")
		    .style("border", "2px black solid")
		    ;
			
			mytable.selectAll('thead').data([0]).enter().append('thead');
			var thead = mytable.select('thead');					
			
	        var th = thead.selectAll("th")
		        .data(columns)
		        .enter()
		        .append("th")
			    .style("border", "1px black solid")
			    .style("padding", "5px")
			    .on("mouseover", function(){d3.select(this).style("background-color", "aliceblue")}) 
			    .on("mouseout", function(){d3.select(this).style("background-color", "white")}) 
		        .text(function(column) { return column; })
			    .style("font-size", "12px")
		        ;
			
			var tbody = mytable.append("tbody");
			var rows = tbody.selectAll("tr")
			    .data(dataset2)
			    .enter()
			    .append("tr");
			

			var cells = rows.selectAll("td")
			    .data(function(d){return d;})
			    .enter().append("td")
			    .style("border", "1px black solid")
			    .style("padding", "5px")
			    .on("mouseover", function(){d3.select(this).style("background-color", "aliceblue")}) 
			    .on("mouseout", function(){d3.select(this).style("background-color", "white")}) 
			    .on("click", function(d){ return openLink(d,dataset2);}) 
//			    .html(function(d) { return d; });
			    .text(function(d){return d;})
			    .style("font-size", "10px")
			    ;
			
		};

		function openLink(item, dataset2) {
//			alert("click " + item + " , " + dataset2);
		};

	    //==================================================
	    d3.select("#palette")
	        .on("keyup", function() {
		var newPalette = d3.select("#palette").property("value");
		if (newPalette != null)						// when interfaced with jQwidget, the ComboBox handles keyup event but value is then not available ?
	            	changePalette(newPalette, heatmapid);
	        })
	        .on("change", function() {
		var newPalette = d3.select("#palette").property("value");
	            changePalette(newPalette, heatmapid);
	        });			    


	    //==================================================
	    // Change ordering of cells
	   function sortbylabel(rORc,i,sortOrder){
	          var t = svg.transition().duration(3000);
	          var log2r=[];
	          var sorted; // sorted is zero-based index
	          d3.selectAll(".c"+rORc+i) 
	            .filter(function(ce){
//	               log2r.push(ce.value);
	               log2r.push(ce.adjvalue);
	             })
	          ;
	          if(rORc=="r"){ // sort log2ratio of a gene
	            sorted=d3.range(col_number).sort(function(a,b){ if(sortOrder){ return log2r[b]-log2r[a];}else{ return log2r[a]-log2r[b];}});
	            t.selectAll(".cell")
	              .attr("x", function(d) { 
	                  var col = parseInt(d3.select(this).attr("col"));
	            	  return sorted.indexOf(col) * cellSize + rectLabelspacer; 
	              })
	            ;
	            t.selectAll(".colLabel")
	             .attr("y", function (d, i) { return sorted.indexOf(i) * cellSize; })
	            ;
	          }else{ // sort log2ratio of a contrast
	            sorted=d3.range(row_number).sort(function(a,b){
	            	if(sortOrder){ return log2r[b]-log2r[a];}
	            	else{ return log2r[a]-log2r[b];}});
	            
	            t.selectAll(".cell")
	              .attr("y", function(d) { 
	                  var col = parseInt(d3.select(this).attr("row"));
	            	  return sorted.indexOf(col) * cellSize; 
	              })
	            ;
	            t.selectAll(".rowLabel")
	             .attr("y", function (d, i) { return sorted.indexOf(i) * cellSize; })
	            ;
	            t.selectAll(".geneLabel")
	             .attr("y", function (d, i) { return sorted.indexOf(i) * cellSize; })
	            ;
	          }
	     }

	     d3.select("#order").on("change",function(){
	       order(this.value);
	     });
	     
	     function order(value){
	      if(value=="hclust"){
	       var t = svg.transition().duration(3000);
	       t.selectAll(".cell")
	         .attr("x", function(d) { return hccol.indexOf(d.col) * cellSize + rectLabelspacer; })
	         .attr("y", function(d) { return hcrow.indexOf(d.row) * cellSize; })
	         ;

	       t.selectAll(".rowLabel")
	         .attr("y", function (d, i) { return hcrow.indexOf(i+1) * cellSize; })
	         ;

	       t.selectAll(".colLabel")
	         .attr("y", function (d, i) { return hccol.indexOf(i+1) * cellSize; })
	         ;

	      }else if (value=="probecontrast"){
	       var t = svg.transition().duration(3000);
	       t.selectAll(".cell")
	         .attr("x", function(d) { return (d.col - 1) * cellSize + rectLabelspacer; })
	         .attr("y", function(d) { return (d.row - 1) * cellSize; })
	         ;

	       t.selectAll(".rowLabel")
	         .attr("y", function (d, i) { return i * cellSize; })
	         ;

	       t.selectAll(".colLabel")
	         .attr("y", function (d, i) { return i * cellSize; })
	         ;

	      }else if (value=="probe"){
	       var t = svg.transition().duration(3000);
	       t.selectAll(".cell")
	         .attr("y", function(d) { return (d.row - 1) * cellSize; })
	         ;

	       t.selectAll(".rowLabel")
	         .attr("y", function (d, i) { return i * cellSize; })
	         ;
	      }else if (value=="contrast"){
	       var t = svg.transition().duration(3000);
	       t.selectAll(".cell")
	         .attr("x", function(d) { return (d.col - 1) * cellSize + rectLabelspacer; })
	         ;
	       t.selectAll(".colLabel")
	         .attr("y", function (d, i) { return i * cellSize; })
	         ;
	      }
	     }
	     // 
	     var sa=d3.select(".g3")
	         .on("mousedown", function() {
	             if( !d3.event.altKey) {
	                d3.selectAll(".cell-selected").classed("cell-selected",false);
	                d3.selectAll(".rowLabel").classed("text-selected",false);
	                d3.selectAll(".colLabel").classed("text-selected",false);
	             }
	            var p = d3.mouse(this);
	            sa.append("rect")
	            .attr({
	                rx      : 0,
	                ry      : 0,
	                class   : "selection",
	                x       : p[0],
	                y       : p[1],
	                width   : 1,
	                height  : 1
	            })
	         })
	         .on("mousemove", function() {
	            var s = sa.select("rect.selection");
	         
	            if(!s.empty()) {
	                var p = d3.mouse(this),
	                    d = {
	                        x       : parseInt(s.attr("x"), 10),
	                        y       : parseInt(s.attr("y"), 10),
	                        width   : parseInt(s.attr("width"), 10),
	                        height  : parseInt(s.attr("height"), 10)
	                    },
	                    move = {
	                        x : p[0] - d.x,
	                        y : p[1] - d.y
	                    }
	                ;
	         
	                if(move.x < 1 || (move.x*2<d.width)) {
	                    d.x = p[0];
	                    d.width -= move.x;
	                } else {
	                    d.width = move.x;       
	                }
	         
	                if(move.y < 1 || (move.y*2<d.height)) {
	                    d.y = p[1];
	                    d.height -= move.y;
	                } else {
	                    d.height = move.y;       
	                }
	                s.attr(d);
	         
	                    // deselect all temporary selected state objects
	                d3.selectAll('.cell-selection.cell-selected').classed("cell-selected", false);
	                d3.selectAll(".text-selection.text-selected").classed("text-selected",false);

	                d3.selectAll('.cell').filter(function(cell_d, i) {
	                    if(
	                        !d3.select(this).classed("cell-selected") && 
	                            // inner circle inside selection frame
	                        (this.x.baseVal.value)+cellSize >= d.x && (this.x.baseVal.value)<=d.x+d.width && 
	                        (this.y.baseVal.value)+cellSize >= d.y && (this.y.baseVal.value)<=d.y+d.height
	                    ) {
	         
	                        d3.select(this)
	                        .classed("cell-selection", true)
	                        .classed("cell-selected", true);

	                        d3.select(".r"+(cell_d.row-1))
	                        .classed("text-selection",true)
	                        .classed("text-selected",true);

	                        d3.select(".c"+(cell_d.col-1))
	                        .classed("text-selection",true)
	                        .classed("text-selected",true);
	                    }
	                });
	            }
	         })
	         .on("mouseup", function() {
	               // remove selection frame
	            sa.selectAll("rect.selection").remove();
	         
	                // remove temporary selection marker class
	            d3.selectAll('.cell-selection').classed("cell-selection", false);
	            d3.selectAll(".text-selection").classed("text-selection",false);
	         })
	         .on("mouseout", function() {
	            if(d3.event.relatedTarget.tagName=='html') {
	                    // remove selection frame
	                sa.selectAll("rect.selection").remove();
	                    // remove temporary selection marker class
	                d3.selectAll('.cell-selection').classed("cell-selection", false);
	                d3.selectAll(".rowLabel").classed("text-selected",false);
	                d3.selectAll(".colLabel").classed("text-selected",false);
	            }
	         })
	         ;
	     
}

function gudmap_genelist_heatmap(heatmapid, data, maxColNumber, rowLabel, cellSize, tooltip, symbol, microarrayLinks) {

	   var row_number = rowLabel.length;
	   var hcrow = [];
	   for (var i=1; i<row_number+1; i++)
		   hcrow.push(1*i);

	   var col_number = maxColNumber;
	   
	   var hccol = [];
	   for (i=1; i<col_number+1; i++)
			hccol.push(1*i);
	   	   
	   var margin = { top: 1, right: 1, bottom: 1, left: 1 },
	   width = cellSize*col_number, // - margin.left - margin.right,
	   height = cellSize*row_number ; // - margin.top - margin.bottom,
	

	   var svg = d3.select(heatmapid).append("svg")
	   .attr("width", width)
	   //.attr("height", height + margin.bottom + margin.top)
	   .attr("height", height).append("g")
	   .attr("transform", "translate(" + 1 + "," + 1 + ")")
	   ;


		var row = svg.selectAll(".row")
		.data(data)
		.enter().append("g")
		.attr("class", "row")
		.attr("id", function(d) {
		    return d.idx;
		});


		var j = 0;
		var heatMap = row.selectAll(".cell")
		.data(function(d) {
		    j++;
		    return d;
		})
		.enter().append("svg:rect")
		.attr("x", function(d, i) {
		    return (i * cellSize);
		})
		.attr("y", function(d, i, j) {
		    return j * cellSize;
		})
		.attr("class", function(d, i, j) {
		    return "cell bordered cr" + j + " cc" + i;
		})
		.attr("row", function(d, i, j) {
		    return j;
		})
		.attr("col", function(d, i, j) {
		    return i;
		})
		.attr("width", cellSize)
		.attr("height", cellSize)
		.style("fill", function(d) { 
			if (d.adjvalue == 100) 
				return '#FFFFFF'; 
			else 
				return getHeatmapColor(d.adjvalue); 
		})
		.on('mouseover', function(d, i, j) {
			d3.select(this).classed("cell-hover",true);
		   tooltip.html('<div class="mytooltip">'+rowLabel[j]+'</div>');
		   tooltip.style("left", (d3.event.pageX-50) + "px");
		   tooltip.style("top", (d3.event.pageY-50) + "px");
		   if (rowLabel[j] == "")
			   tooltip.style("visibility", "hidden")
		   else
			   tooltip.style("visibility", "visible");
		})
		.on('mouseout', function(d, i, j) {
			d3.select(this).classed("cell-hover",false);
			tooltip.style("visibility", "hidden");
		})
		.on('click', function(d,i,j) {
			var masterTableId = microarrayLinks[j];
			var url = "browseHeatmap.jsf?gene="+ symbol + "&masterTableId="+ masterTableId;  
			window.location = url;
		}); 


	     var sa=d3.select(".g3")
	         .on("mousedown", function() {
	             if( !d3.event.altKey) {
	                d3.selectAll(".cell-selected").classed("cell-selected",false);
	                d3.selectAll(".rowLabel").classed("text-selected",false);
	                d3.selectAll(".colLabel").classed("text-selected",false);
	             }
	            var p = d3.mouse(this);
	            sa.append("rect")
	            .attr({
	                rx      : 0,
	                ry      : 0,
	                class   : "selection",
	                x       : p[0],
	                y       : p[1],
	                width   : 1,
	                height  : 1
	            })
	         })
	         .on("mousemove", function() {
	            var s = sa.select("rect.selection");
	         
	            if(!s.empty()) {
	                var p = d3.mouse(this),
	                    d = {
	                        x       : parseInt(s.attr("x"), 10),
	                        y       : parseInt(s.attr("y"), 10),
	                        width   : parseInt(s.attr("width"), 10),
	                        height  : parseInt(s.attr("height"), 10)
	                    },
	                    move = {
	                        x : p[0] - d.x,
	                        y : p[1] - d.y
	                    }
	                ;
	         
	                if(move.x < 1 || (move.x*2<d.width)) {
	                    d.x = p[0];
	                    d.width -= move.x;
	                } else {
	                    d.width = move.x;       
	                }
	         
	                if(move.y < 1 || (move.y*2<d.height)) {
	                    d.y = p[1];
	                    d.height -= move.y;
	                } else {
	                    d.height = move.y;       
	                }
	                s.attr(d);
	         
	                    // deselect all temporary selected state objects
	                d3.selectAll('.cell-selection.cell-selected').classed("cell-selected", false);
	                d3.selectAll(".text-selection.text-selected").classed("text-selected",false);

	                d3.selectAll('.cell').filter(function(cell_d, i) {
	                    if(
	                        !d3.select(this).classed("cell-selected") && 
	                            // inner circle inside selection frame
	                        (this.x.baseVal.value)+cellSize >= d.x && (this.x.baseVal.value)<=d.x+d.width && 
	                        (this.y.baseVal.value)+cellSize >= d.y && (this.y.baseVal.value)<=d.y+d.height
	                    ) {
	         
	                        d3.select(this)
	                        .classed("cell-selection", true)
	                        .classed("cell-selected", true);

	                        d3.select(".r"+(cell_d.row-1))
	                        .classed("text-selection",true)
	                        .classed("text-selected",true);

	                        d3.select(".c"+(cell_d.col-1))
	                        .classed("text-selection",true)
	                        .classed("text-selected",true);
	                    }
	                });
	            }
	         })
	         .on("mouseup", function() {
	               // remove selection frame
	            sa.selectAll("rect.selection").remove();
	         
	                // remove temporary selection marker class
	            d3.selectAll('.cell-selection').classed("cell-selection", false);
	            d3.selectAll(".text-selection").classed("text-selection",false);
	         })
	         .on("mouseout", function() {
	            if(d3.event.relatedTarget.tagName=='html') {
	                    // remove selection frame
	                sa.selectAll("rect.selection").remove();
	                    // remove temporary selection marker class
	                d3.selectAll('.cell-selection').classed("cell-selection", false);
	                d3.selectAll(".rowLabel").classed("text-selected",false);
	                d3.selectAll(".colLabel").classed("text-selected",false);
	            }
	         })
	         ;
	     
}