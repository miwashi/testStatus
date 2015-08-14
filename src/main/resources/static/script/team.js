/**
 * 
 */
var statUrl = "/api/team/all";

$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});


function poll() {
	var id = document.getElementById("teamid").value;
	
	$.ajax({
	   url: '/api/team/' + id,
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
		  
		  document.getElementById("team-tested").innerHTML = data.team.tested;
    	  document.getElementById("team-numfails").innerHTML = data.team.numberOfFailedRequirements;
    	  document.getElementById("team-ratio").innerHTML = data.team.numberOfSucceededRequirementsRatio;
			
		  
	      $.each(data.requirements, function( index, requirement ) {
	    	  
	    	  
	    	  var prefix = "requirement-";
    		  var requirementRow = prefix + requirement.id;
    		  
    		  var failedRow = document.getElementById("failed-" + requirementRow);
    		  var unstableRow = document.getElementById("unstable-" + requirementRow);
    		  var verifiedRow = document.getElementById("verified-" + requirementRow);
    		  
    		  var failedFailRate = document.getElementById("failed-" + requirementRow + "-failrate");
    		  var failedDuration = document.getElementById("failed-" + requirementRow + "-duration");
    		  var failedNotTestedSince = document.getElementById("failed-" + requirementRow + "-nottestedsince");
    		  
    		  var unstabledFailRate = document.getElementById("unstable-" + requirementRow + "-failrate");
    		  var unstableDuration = document.getElementById("unstable-" + requirementRow + "-duration");
    		  var unstableNotTestedSince = document.getElementById("unstable-" + requirementRow + "-nottestedsince");
    		  
    		  var verifiedFailRate = document.getElementById("verified-" + requirementRow + "-failrate");
    		  var verifiedDuration = document.getElementById("verified-" + requirementRow + "-duration");
    		  var verifiedNotTestedSince = document.getElementById("verified-" + requirementRow + "-nottestedsince");
    		  
          	  if(failedFailRate!=null){
          		failedFailRate.innerHTML = requirement.failRate;
          	  }
          	  
          	  if(failedDuration!=null){
          		failedDuration.innerHTML = requirement.duration;
          	  }
	          	
          	  if(failedNotTestedSince!=null){
          		failedNotTestedSince.innerHTML = requirement.notTestedSince;
          	  }
          	
          	if(unstabledFailRate!=null){
          		failedFailRate.innerHTML = requirement.failRate;
          	  }
          	  
          	  if(unstableDuration!=null){
          		failedDuration.innerHTML = requirement.duration;
          	  }
	          	
          	  if(unstableNotTestedSince!=null){
          		failedNotTestedSince.innerHTML = requirement.notTestedSince;
          	  }
          	  
          	if(verifiedFailRate!=null){
          		failedFailRate.innerHTML = requirement.failRate;
          	  }
          	  
          	  if(verifiedDuration!=null){
          		failedDuration.innerHTML = requirement.duration;
          	  }
	          	
          	  if(verifiedNotTestedSince!=null){
          		failedNotTestedSince.innerHTML = requirement.notTestedSince;
          	  }
    		  
	    	  if(requirement.failed){
	    		  if(failedRow != null){
	    			  failedRow.className = "failed_in_failed";
	    		  }
	    		  if(unstableRow != null){
	    			  unstableRow = "notunstable_in_unstable";
	    		  }
	    		  if(verifiedRow!=null){
	    			  verifiedRow = "notverified_in_verified";
	    		  }
	    	  }
	    	  
	    	  if(requirement.unstable){
	    		  if(failedRow != null){
	    			  failedRow.className = "notfailed_in_failed";
	    		  }
	    		  if(unstableRow != null){
	    			  unstableRow = "unstable_in_unstable";
	    		  }
	    		  if(verifiedRow!=null){
	    			  verifiedRow = "notverified_in_verified";
	    		  }
	    	  }
	    	  
	    	  if(requirement.success){
	    		  if(failedRow != null){
	    			  failedRow.className = "notfailed_in_failed";
	    		  }
	    		  if(unstableRow != null){
	    			  unstableRow = "notunstable_in_unstable";
	    		  }
	    		  if(verifiedRow!=null){
	    			  verifiedRow = "verified_in_verified";
	    		  }
	    	  }
	      });
		  
		  setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}