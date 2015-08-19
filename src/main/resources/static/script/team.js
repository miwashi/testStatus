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
		  
		  document.getElementById("team-numverified").innerHTML = data.team.numberOfSucceededRequirements;
    	  document.getElementById("team-ratioverified").innerHTML = data.team.numberOfSucceededRequirementsRatio;
			
    	  document.getElementById("team-numfails").innerHTML = data.team.numberOfFailedRequirements;
    	  document.getElementById("team-ratiofails").innerHTML = data.team.numberOfFailedRequirementsRatio;
    	  
    	  document.getElementById("team-numunstable").innerHTML = data.team.numberOfUnstableRequirements;
    	  document.getElementById("team-ratiounstable").innerHTML = data.team.numberOfUnstableRequirementsRatio;
    	  
	      $.each(data.requirements, function( index, requirement ) {
	    	  var prefix = "requirement-" + requirement.id;
    		      		  
    		  var row = document.getElementById(prefix);
    		  
    		  var failRate = document.getElementById(prefix + "-failrate");
    		  var lastTested = document.getElementById(prefix + "-lasttested");
    		  var duration = document.getElementById(prefix + "-duration");
    		  var notTestedSince = document.getElementById(prefix + "-nottestedsince");
    		  var numberOfRuns = document.getElementById(prefix + "-numberofruns");
    		  
          	  if(failRate!=null){
          		failRate.innerHTML = requirement.failRate  + '%';
          	  }
          	  
          	  if(duration!=null){
          		duration.innerHTML = requirement.duration;
          	  }
	          	
          	  if(notTestedSince!=null){
          		notTestedSince.innerHTML = requirement.notTestedSince;
          	  }
          	  
          	if(numberOfRuns!=null){
          		numberOfRuns.innerHTML = requirement.numberOfRuns;
          	  }
          	
	    	  if(requirement.failed){
	    		  if(row != null){
	    			  row.className = "failed";
	    		  }
	    	  }
	    	  
	    	  if(requirement.unstable){
	    		  if(row != null){
	    			  row.className = "unstable";
	    		  }
	    	  }
	    	  
	    	  if(requirement.success){
	    		  if(row != null){
	    			  row.className = "verified";
	    		  }
	    	  }
	    	  
	    	  if(lastTested==null){
	    		  console.log("New entry: " + prefix)
	    		  showErrorMessage("New data available, refresh browser!");
	    	  }else{
		    	  if(requirement.latestTestedDate != lastTested.value){
		    		  console.log ("Should update: " + prefix);
		    		  var testLightDiv = document.getElementById(prefix + "-testlight");
		    		  if(testLightDiv!=null){
		    			  testLightDiv.innerHTML = "";
		    			  $.each(requirement.results, function( index, result ) {
		    				  var a =  document.createElement("a");
		    				  a.setAttribute("href", "/result/" + result.id)
		    				  a.setAttribute("class", "resultindicator")
		    				  
			    			  var img = document.createElement("img");
		    				  if(result.success){
		    					  img.setAttribute("src", "/img/green_light.png")
		    				  }else if(result.fail){
		    					  img.setAttribute("src", "/img/red_light.png")
		    				  }else {
		    					  img.setAttribute("src", "/img/yellow_light.png")
		    				  }
			    			  img.setAttribute("class", "resultindicator")
			    			  testLightDiv.appendChild(a);
			    			  a.appendChild(img);
			    			  lastTested.value = requirement.latestTestedDate;
		    			  });
		    		  }
		    	  }
	    	  }
	      });
		  
		  setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}