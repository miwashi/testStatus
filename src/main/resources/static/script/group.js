/**
 * 
 */
var groupSummaryUrl = "/api/group/";
var groupsummaryTableId = "group-summary-section";
var summaryTableId = "teams-summary-table";
var summaryTableClassName = "teams-summary-table";

var headerCellClassName = "row-header";
var numericCellClassName = "numeric";

$( document ).ready(function() {
	var summaryTable = document.getElementById(groupsummaryTableId);
    if(summaryTable == undefined){
    	return;
    }
	var groupSummaryTable = document.createElement("table");
	groupSummaryTable.id = summaryTableId;
	
	groupSummaryTable.className = summaryTableId;
    summaryTable.appendChild(groupSummaryTable);
    console.log( "ready!" );
    poll();
});


function poll() {
	var id = document.getElementById("teamid").value;
	
	$.ajax({
	   url: groupSummaryUrl + id,
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
		  setGlobalStats(data);
		  var summaryTable = document.getElementById(summaryTableId);
	      $.each(data.requirements, function( index, requirement ) {
	    	  var prefix = "requirement-" + requirement.id;
	    	  var row = document.getElementById(prefix);
	    	  if(row == undefined){
	    		  row = document.createElement("tr")
		    	  row.id = prefix;
	    		  
	    		  createHeaderCell(row, headerCellClassName, prefix + "header", requirement);
	    		  appendCell(row, "", prefix + "-duration", requirement.statistics.duration.intervall);
	    		  appendResultCell(row, "", prefix + "results", requirement.results);
	    		  summaryTable.appendChild(row);
	    	  }else{
	    		  summaryTable.removeChild(row);
	    		  row = document.createElement("tr")
		    	  row.id = prefix;
	    		  createHeaderCell(row, headerCellClassName, prefix + "header", requirement);
	    		  appendCell(row, "", prefix + "-duration", requirement.statistics.duration.intervall);
	    		  appendResultCell(row, "", prefix + "results", requirement.results);
	    		  summaryTable.appendChild(row);
	    	  }
	      });
		  
		  setTimeout(poll, 2000);
	   },
	   type: 'GET'
	});
}

function setGlobalStats(data){
	setGlobalTotal("Total", data.statistics.summary.total);
	  setGlobalTested("Tested", data.statistics.summary.runs);
	  setGlobalPassed("Passed", data.statistics.summary.passRatio + "%");
	  setGlobalFailed("Fails", data.statistics.summary.failRatio + "%");
	  setGlobalUnstable("Unstable", data.statistics.summary.unstableRatio + "%");
	  setGlobalJobs("Jobs", 0);
}

function createHeaderCell(row, className, id, requirement){
	var cell = row.insertCell();
	var a = document.createElement("a");
	var linkText = document.createTextNode(requirement.readableName + "" + requirement.parameter);
	a.appendChild(linkText);
	a.title = requirement.parameter;
	a.href = "/requirement/" + requirement.id;
	 
	cell.appendChild(a);
	cell.className=headerCellClassName;
	cell.id = id;
	return cell;
}

function appendCell(row, className, id, nodeText){
	var cell = row.insertCell();
	var textNode = document.createTextNode(nodeText);
	cell.appendChild(textNode);
	cell.className=headerCellClassName;
	cell.id = id;
	return cell;
}

function appendResultCell(row, className, id, results){
	var cell = row.insertCell();
	
	var testlightsTable = document.createElement("table");
	var testLightsRow = document.createElement("tr");
	testlightsTable.appendChild(testLightsRow);
	testlightsTable.className = "testlights";
	cell.appendChild(testlightsTable);
	cell.className="testlights";
	cell.id = id;
	
	for(var i = 0 ; i < 10; i++){
		var img = document.createElement("img");
		img.className="resultindicator";
		var result = results[i];
		if(result == undefined){
			img.src="/img/white_light.png";
			img.title= "not run yet";
		}else{
			if(result.status == 'PASS'){
				img.src="/img/green_light.png";
			}
			else if(result.status == 'FAIL'){
				img.src="/img/red_light.png";
			}else{
				img.src="/img/yellow_light.png";
			}
			img.title=result.status + " " + result.when.since;
		}
		var testLightCell = testLightsRow.insertCell();
		testLightCell.appendChild(img);
		testLightCell.className="testlights";
	}
	return cell;
}