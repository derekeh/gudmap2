// JavaScript Document


function searchInit() {

        // Initialize our object data from our XHTML divs.
		 var solr = document.getElementById('gd_solrmenu');	
		 solr.style.width = '30px';
		 var solrtext = document.getElementById('gd_solr_text');	
		 solrtext.style.display = 'none';
		 var solrinput = document.getElementById('gd_solr_textfield');	
		 solrinput.style.display = 'none';
		 var solrbutton = document.getElementById('gd_solr_button');	
		 solrbutton.style.display = 'none';
/*		 var solrinput = document.getElementById('il_solar_form:gd_solr_textfield');	
		 solrinput.style.display = 'none';
		 var solrbutton = document.getElementById('il_solar_form:gd_solr_button');	
		 solrbutton.style.display = 'none';*/
		 var solrresult = document.getElementById('gd_solr_image');	
		 solrresult.style.display = 'none';
		 var solrclose = document.getElementById('gd_close_search');	
		 solrclose.style.display = 'none';
		 
    }

    function toggle() {

        // Initialize our object data from our XHTML divs.
		 var solr = document.getElementById('gd_solrmenu');	
		 var solrtext = document.getElementById('gd_solr_text');
		 var solrinput = document.getElementById('gd_solr_textfield');	
		 var solrbutton = document.getElementById('gd_solr_button');
//		 var solrinput = document.getElementById('il_solar_form:gd_solr_textfield');	
//		 var solrbutton = document.getElementById('il_solar_form:gd_solr_button');	
		 var solrresult = document.getElementById('gd_solr_image');
		 var solrclose = document.getElementById('gd_close_search');
		 var solrexpand = document.getElementById('gd_expand_search');
		 
        // If the sidebar is expanded...
        if (solr.style.minWidth == '350px') {

            // Collapse it by setting its width to 15px
            solr.style.width = '30px';
			 solr.style.minWidth = '30px';
			 solrtext.style.display = 'none';
            solrinput.style.display = 'none';
			 solrbutton.style.display = 'none';
			 solrresult.style.display = 'none';
			 solrclose.style.display = 'none';
			 solrexpand.style.display = 'block';
        // Otherwise, if the sidebar is already collapsed...
        } else {

			 solr.style.minWidth = '350px';
			 solrtext.style.display = 'block';
            solrinput.style.display = 'block';
			 solrbutton.style.display = 'block';
			 solrresult.style.display = 'block';
			 solrclose.style.display = 'block';
			 solrexpand.style.display = 'none';
        }
    }
function MM_callJS(jsStr) { //v2.0
  return eval(jsStr);
}

/**
 * function to open a popup window for a Lab details
 */
function showLaboratoryDetails(id) {
	  var w = window.open('viewLabDetail.jsf?personId='+ id,'labdetailPopup','resizable=1,toolbar=0,scrollbars=1,width=500,height=350');
	  w.focus();
}

/**
 * function to open a popup window for a Sequence details
 */
/*function showSequenceDetails(id) {
	  var w = window.open('viewSequenceDetail.jsf?id='+ id,'seqdetailPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');
	  w.focus();
}*/

/*function showExpressionInfo(oid, emapID, row) {
	  var w = window.open('viewExpressionDetail.jsf?id='+oid+'&componentId='+emapID,'expressionPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=400');
	  w.focus();
}*/

function showExpressionInformation(oid, componentId, row) {
	  var w = window.open('viewExpressionDetailPU.jsf?oid='+oid+'&componentId='+componentId,'expressionPopup','resizable=1,toolbar=0,scrollbars=1,width=860,height=560');
	  w.focus();
}

function monitor(data){
	//alert('source id: ' + data.source.id);
    //var loading = document.getElementById("dataform:il_datatable:spinner");
    var loading = document.getElementById("focusform:spinner");
    //alert('status: '+data.status);
    //alert('loading: ' + loading.style.display);
    if(data.status == "begin"){
        loading.style.display = "block";
    }
    else if(data.status == "success"){
        loading.style.display = "none";
    }
}

function slow_monitor(data){
	//alert('source id: ' + data.source.id);
    //var loading = document.getElementById("dataform:il_datatable:spinner");
    var loading = document.getElementById("focusform:slow_spinner");
    //alert('status: '+data.status);
    //alert('loading: ' + loading.style.display);
    if(data.status == "begin"){
        loading.style.display = "block";
    }
    else if(data.status == "success"){
        loading.style.display = "none";
    }
}

/*function showExpressionInformation() {
	  var w = window.open('viewExpressionDetail.jsf','expressionPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=400');
	  w.focus();
}*/

/*function applyDMXTooltip(trigger) {//v1.5
//	alert('found tooltip function');
	if (arguments.length < 3 || !arguments[2]) return true; // ignore empty tooltips
  window.stylesFolderName = '/styles';
//  alert(window.stylesFolderName);
	var arg = {};
	var options = ['contentType', 'dataProvider','showEffect','easing','showAt',
  'showDirection', 'showDuration','showDelay','closeEvent','styleTheme',
	'showCloseBtn','onshow','onhide','mouseOffsetX','mouseOffsetY','x','y','w','h'];
  for (var i = 0; i < options.length && i < arguments.length-1; i++) {
		arg[options[i]] = arguments[i + 1];
	}
	showTooltip(trigger, arg);
}*/
