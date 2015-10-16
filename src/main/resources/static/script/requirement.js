/**
 * 
 */
var statUrl = "/api/team/all";

$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});


function poll() {
	var id = document.getElementById("requirementid").value;
	
	$.ajax({
	   url: '/api/requirement/' + id,
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
		  hideErrorMessage()
		  document.getElementById("mainstat_requirements_hdr").innerHTML = "Tested";
		  document.getElementById("mainstat_requirements").innerHTML = data.requirement.numberOfRuns;
		  
		  document.getElementById("mainstat_tested_hdr").innerHTML = "Fails";
		  document.getElementById("mainstat_tested").innerHTML = data.requirement.numberOfFails;
		  
		  document.getElementById("mainstat_verified_hdr").innerHTML = "Longest run";
    	  document.getElementById("mainstat_verified").innerHTML = data.requirement.longestConsecutiveSuccess;
			
    	  document.getElementById("mainstat_failed_hdr").innerHTML = "Latest test";
    	  document.getElementById("mainstat_failed").innerHTML = data.requirement.latestTestedDateAsStr;
    	  
    	  document.getElementById("mainstat_unstable_hdr").innerHTML = "Duration";
    	  document.getElementById("mainstat_unstable").innerHTML = data.requirement.duration;
    	  
    	  document.getElementById("mainstat_jobs_hdr").innerHTML = "";
    	  document.getElementById("mainstat_jobs").innerHTML = "";
    	  
	      $.each(data.requirements, function( index, requirement ) {
	    	  
	      });
		  setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}