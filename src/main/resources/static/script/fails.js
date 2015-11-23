$(document ).ready(function() {
	Fails.init();
});

var Fails = function() {
	var pollIntervall = 20000;
	var sectionPrefix = "fails";
	var fails = {};
	
	var section;
	
	var url = "/api/requirements/fails";
	var sectionId = sectionPrefix + "-summary-section";
	
	var tableId = sectionPrefix + "-summary-table";
	var tableClassName = sectionPrefix + "-summary-table";
	
	fails.init = function () {
		section = document.getElementById(sectionId);
	    if(section == undefined){
	    	return;
	    }
	    Requirement.createTableInSection(section, tableId, tableClassName);
	    
	    console.log( "ready!" );
	    Fails.poll();
	};
	
	
	fails.poll = function() {
		
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
			  setTimeout(Fails.poll, pollIntervall);
		   },
		   type: 'GET'
		});
	};
	
	return fails;
}();