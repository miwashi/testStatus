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
	      $.each(data.groups, function( index, group ) {
	    	  var id = "team-" + group.id;
	    	  var testedId = document.getElementById(id + "-tested");
              var failsId = document.getElementById(id + "-fails");
              var unstableId = document.getElementById(id + "-unstable");
              var lastRunId = document.getElementById(id + "-lastrun");
              
              if(testedId!=null){
            	  testedId.innerHTML = group.tested;
        	  }
              
              if(failsId!=null){
            	  failsId.innerHTML = group.fails;
        	  }
              
              if(unstableId!=null){
            	  unstableId.innerHTML = group.failsRelative;
        	  }
              
              if(lastRunId!=null){
            	  lastRunId.innerHTML = group.touched;
        	  }
              if(group.touchedDays>0){
            	  lastRunId.className="warn";
              }else{
            	  lastRunId.className="ok";
              }
	      });
	      setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}