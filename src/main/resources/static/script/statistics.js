var pollUrl = "/api/statistics/all";

$(document ).ready(function() {
	
	
	$('ul.tabs').each(function(){
    // For each set of tabs, we want to keep track of
    // which tab is active and its associated content
    var $active, $content, $links = $(this).find('a');

    // If the location.hash matches one of the links, use that as the active tab.
    // If no match is found, use the first link as the initial active tab.
    $active = $($links.filter('[href="'+location.hash+'"]')[0] || $links[0]);
    $active.addClass('active');
    
    $content = $($active[0].hash);
    // Hide the remaining content
    $links.not($active).each(function () {
      $(this.hash).hide();
    });

    // Bind the click event handler
    $(this).on('click', 'a', function(e){
      // Make the old tab inactive.
      $active.removeClass('active');
      $content.hide();

      // Update the variables with the new link and content
      $active = $(this);
      $content = $(this.hash);

      // Make the tab active.
      $active.addClass('active');
      $content.show();

      // Prevent the anchor's default click action
      e.preventDefault();
    });
    
  });
	
	$('div.team_summary').click(function(event){
		var text = $(event.target).text();
		var div = $(event.target).attr('id');
		
		alert($(event.target).attr('name'));
		return false;
	});
	
	var table = $('#requirementsTable');
	table.dataTable({
		paging:   false
        ,ordering: true
        ,info:     true
        ,scrollX: true
        ,order: [[1, "desc"]]
		,ajax: {
			    "url": "/api/statistics/requirements",
			    "dataSrc": ""
		}
		,columns: [
		    {"data": "id"}
			,{"data": "parameter"}
			,{"data": "results"}
			,{"data": "host"}
			,{"data": "team"}
			,{"data": "statusPass"}
			
		]
		,columnDefs: [
		    { 
		    	width: "100px"
		    	,targets: 3 
		    }
	        ,{
	            // The `data` parameter refers to the data for the cell (defined by the
	            // `data` option, which defaults to the column being worked with, in
	            // this case `data: 0`.
	            render: function ( data, type, row ) {
	                return '<a href="' + data + '">'  + data + '</a><br/>' + row.readableName;
	            },
	            targets: 1
	        }
	        ,{
	            render: function ( data, type, row ) {
	            	var html = "<table><tr>";
	            	var limit = 0;
	            	var img = "/img/yellow_light.png";
	            	$.each(data, function( index, result ) {
	            		if(result.status === "FAIL"){
	            			img = "/img/red_light.png";
	            		}else if(result.status === "PASS"){
	            			img = "/img/green_light.png";
	            		}else{
	            			img = "/img/yellow_light.png";
	            		}
	            		html+='<td width="0"><a class="resultindicator" href="/statistics/result/' + result.id + '" ><img class="resultindicator" src="' + img + '"></a></td>';
	            		limit++;
	            	});
	            	
	            	for(indx = 0; indx < (10 - limit); indx++){
	            		html+='<td width="0"><a class="resultindicator"><img class="resultindicator" src="/img/white_light.png"></a></td>';
	            	}
	            	html+= "</tr></table>"
	                return html;
	            },
	            targets: 2
	        }
	        ,{
	            render: function ( data, type, row ) {
	                return '<a href="/statistics/requirement/' + row.id + '">'  + data + '</a>';
	            },
	            targets: 5
	        }
	        ,{ 
	        	visible: false
	        	,targets: [ 0 ] 
	        }
	    ]
		,buttons: [
		           'colvis',
		           'excel',
		           'print'
		       ]
		,createdRow: function( row, data, dataIndex ) {
			//data är en URLRequirement post
			//row är en tr
			//dataIndex är posten i hela json responsen
		    if ( data.statusPass) {
		    	$(row).addClass( 'pass' );
		    }else{
		    	$(row).addClass( 'fail' );
		    }
		}
	});
	
	setInterval( function () {
	    table.api().ajax.reload(function(json){
	    	
	    }, false);
	}, 5000 );
	
	//setTimeout(poll, 2000);
});

function poll() {
	$.ajax({
	   url: pollUrl,
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
		   hideErrorMessage();
		   var dataTable = $('#requirementsTable');
		   var tBody = $('#requirementsTable > tbody');
		   var overview = data.overview;
		   var modified = false;
		   /*
		   $.each(overview.requirements, function( index, requirement ) {
			   var row = $('#requirement-' + requirement.id);
			   if(row.length){
				   
			   }else{
				   modified = true;
				   var data = '<td data-search="' + requirement.host + '">' + requirement.parameter + '</td>';
				   data = data + '<td>' + requirement.team + '</td></td>';
				   data = data + '<td>' + requirement.host + '</td>';
				   data = data + '<td>30</td>';
				   data = data + '<td data-order="1279062000">Wed 14th Jul 10</td>';
				   data = data + '<td data-order="86500">$86,500/y</td>';
				   tBody.append('<tr id="requirement-' + requirement.id + '">' + data + '</tr>');
			   }
		   });
		   
		   if(modified){
		   		dataTable.DataTable();
	   		}
	   		*/
		   setTimeout(poll, 5000);
	   },
	   type: 'GET'
	});
}