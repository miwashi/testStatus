/**
 * 
 */
var statUrl = "/api/statistics/all";

$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});


function poll() {
	
	$.ajax({
	   url: '/api/statistics/all',
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
		  
		  document.getElementById("mainstat_requirements_hdr").innerHTML = "Total";
	      document.getElementById("mainstat_requirements").innerHTML = data.statistics.summary.total;
	      
	      document.getElementById("mainstat_tested_hdr").innerHTML = "Runs";
	      document.getElementById("mainstat_tested").innerHTML = data.statistics.summary.runs;
	      
	      document.getElementById("mainstat_verified_hdr").innerHTML = "Verified";
	      document.getElementById("mainstat_verified").innerHTML = data.statistics.summary.passes;
	      
	      document.getElementById("mainstat_failed_hdr").innerHTML = "Failed";
	      document.getElementById("mainstat_failed").innerHTML = data.statistics.summary.fails;
	      
	      document.getElementById("mainstat_unstable_hdr").innerHTML = "Unstable";
	      document.getElementById("mainstat_unstable").innerHTML = data.statistics.summary.unstable;
	      
	      document.getElementById("mainstat_jobs_hdr").innerHTML = "";
	      document.getElementById("mainstat_jobs").innerHTML = "0";
	      
	      $.each(data.requirements, function( index, requirement ) {
	    	  
	      });
		  setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}