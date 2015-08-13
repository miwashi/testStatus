/**
 * 
 */
var statUrl = "/api/stats";

$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});



function poll() {
	$.ajax({
	   url: '/api/stats',
	   data: {
	      format: 'json'
	   },
	   error: function(request, error) {
	      console.log("An error: " + error);
	      console.log(request);
	      setTimeout(poll, 20000);
	   },
	   dataType: 'json',
	   success: function(data) {
	      console.log(data);
	      document.title="Tests run " + (data.stats.total.starts - data.stats.total.skipped);
	      
	      $("#stat-numberOfRequirements").html(data.stats.numberOfRequirements);
	      $("#stat-numberOfTestedRequirements").html(data.stats.numberOfTestedRequirements);
	      $("#stat-numberOfVerifiedRequirements").html(data.stats.numberOfVerifiedRequirements);
	      $("#stat-numberOfFailedRequirements").html(data.stats.numberOfFailedRequirements);
	      $("#stat-numberOfUnstableRequirements").html(data.stats.numberOfUnstableRequirements);
	      
	      $('#stats-today-starts').html(data.stats.today.starts - data.stats.today.skipped);
	      $('#stats-today-complete').html(data.stats.today.complete - data.stats.today.skipped);
	      $('#stats-today-fails').html(data.stats.today.failShare);
	      
	      $('#stats-total-starts').html(data.stats.total.starts - data.stats.total.skipped);
	      $('#stats-total-complete').html(data.stats.total.complete - data.stats.total.skipped);
	      $('#stats-total-fails').html(data.stats.total.failShare);
	      
	      $('#stats-last24Hours-starts').html(data.stats.last24Hours.starts - data.stats.last24Hours.skipped);
	      $('#stats-last24Hours-complete').html(data.stats.last24Hours.complete - data.stats.last24Hours.skipped);
	      $('#stats-last24Hours-fails').html(data.stats.last24Hours.failShare);
	      
	      $('#stats-lastHour-starts').html(data.stats.lastHour.starts - data.stats.lastHour.skipped);
	      $('#stats-lastHour-complete').html(data.stats.lastHour.complete - data.stats.lastHour.skipped);
	      $('#stats-lastHour-fails').html(data.stats.lastHour.failShare);
	      
	      $('#stats-lastWeek-starts').html(data.stats.lastWeek.starts - data.stats.lastWeek.skipped);
	      $('#stats-lastWeek-complete').html(data.stats.lastWeek.complete - data.stats.lastWeek.skipped);
	      $('#stats-lastWeek-fails').html(data.stats.lastWeek.failShare);
	      
	      $.each(data.stats.hourly, function( index, value ) {
	    	  console.log( index + ": " + value.caption );
	    	  var headerId = "#" + value.caption + "-header";
	    	  var startsId = "#" + value.caption + "-starts";
	    	  var completeId = "#" + value.caption + "-complete";
	    	  var failedId = "#" + value.caption + "-failed";
	    	  
	    	  $(startsId).html(value.startsWithoutSkipped);
		      $(completeId).html(value.completeWithoutSkipped);
		      $(failedId).html(value.failShare);
	      });
	      
	      
	      setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}