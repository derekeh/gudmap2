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


