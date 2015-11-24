$(document ).ready(function() {
	Jobs.init();
});

var Jobs = function() {
	var jobs = {};
	
	var pollIntervall = 20000;
	var sectionPrefix = "jobs";
	
	var section;
	
	var url = "/api/jobs/all";
	var sectionId = sectionPrefix + "-summary-section";
	
	var tableId = sectionPrefix + "-summary-table";
	var tableClassName = sectionPrefix + "-summary-table";
	
	jobs.init = function () {
		section = document.getElementById(sectionId);
	    if(section == undefined){
	    	return;
	    }
	    Job.createTableInSection(section, tableId, tableClassName);
	    
	    console.log( "ready!" );
	    Jobs.poll();
	};
	
	
	jobs.poll = function() {
		
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
		      $.each(data.summary.jobs, function( index, job ) {
		    	  var prefix = sectionPrefix + "-" + job.key;
		    	  var row = document.getElementById(prefix);
		    	  if(row != undefined){
		    		  table.removeChild(row);
		    	  }
		    	  row = Job.createResultRow(job, sectionPrefix)
		    	  row.id = prefix;
		    	  table.appendChild(row);
		    	});
			  setTimeout(Jobs.poll, pollIntervall);
		   },
		   type: 'GET'
		});
	};
	
	return jobs;
}();