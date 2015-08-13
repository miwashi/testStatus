/**
 * 
 */
var statUrl = "/api/team/all";

$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});



function poll() {
	$.ajax({
	   url: statUrl,
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
	      
	      setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}