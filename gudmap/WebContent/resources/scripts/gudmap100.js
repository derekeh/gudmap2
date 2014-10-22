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
