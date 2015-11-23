$(document ).ready(function() {
	Unstable.init();
});

var Unstable = function() {
	var pollIntervall = 20000;
	var sectionPrefix = "unstable";
	var unstable = {};
	
	var section;
	
	var url = "/api/requirements/unstable";
	var sectionId = sectionPrefix + "-summary-section";
	
	var tableId = sectionPrefix + "-summary-table";
	var tableClassName = sectionPrefix + "-summary-table";
	
	unstable.init = function () {
		section = document.getElementById(sectionId);
	    if(section == undefined){
	    	return;
	    }
	    Requirement.createTableInSection(section, tableId, tableClassName);
	    
	    console.log( "ready!" );
	    Unstable.poll();
	};
	
	
	unstable.poll = function() {
		
		$.ajax({
		   url: url,
		   data: {
		      format: 'json'
		   },
		   error: function(request, error) {
		      console.log("An error: " + error);
		      console.log(request);
		      showErrorMessage("Unable to get data: " + error);
		      setTimeout(poll, 20000);
		   },
		   dataType: 'json',
		   success: function(data) {
			  console.log("loading");
			  hideErrorMessage()
			  var table = document.getElementById(tableId);
		      $.each(data.summary.requirements, function( index, requirement ) {
		    	  var prefix = sectionPrefix + "-" + requirement.id;
		    	  var row = document.getElementById(prefix);
		    	  if(row != undefined){
		    		  table.removeChild(row);
		    	  }
		    	  row = Requirement.createResultRow(requirement, sectionPrefix);
		    	  row.id = prefix;
		    	  table.appendChild(row);
		    	});
			  setTimeout(Unstable.poll, pollIntervall);
		   },
		   type: 'GET'
		});
	};
	
	return unstable;
}();