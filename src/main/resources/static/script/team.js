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
	      console.log(data);
		   setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}