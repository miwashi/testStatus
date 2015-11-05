/**
 * 
 */
var statUrl = "/api/requirement/";
var pollIntervall = 5000;
var summarySectionId = "requirement-summary-section";
var summaryTableId = "requirement-summary-table";
var summaryTableClassName = "requirement-summary-table";

var headerCellClassName = "row-header";
var numericCellClassName = "numeric";


$( document ).ready(function() {
    console.log( "ready!" );
    poll();
});

$( document ).ready(function() {
	var summarySection = document.getElementById(summarySectionId);
    if(summarySection == undefined){
    	return;
    }
	var summaryTable = document.createElement("table");
	summaryTable.id = summaryTableId;
	summaryTable.className = summaryTableClassName;
	summarySection.appendChild(summaryTable);
	summaryTable.appendChild(createHeaderRow());
    console.log( "ready!" );
    poll();
});

function createHeaderRow(){
	var headerRow = document.createElement("tr");
	headerRow.appendChild(createHeader("Status",""));
	headerRow.appendChild(createHeader("Browser",""));
	headerRow.appendChild(createHeader("Platform",""));
	headerRow.appendChild(createHeader("Touched",""));
	headerRow.appendChild(createHeader("Job",""));
	headerRow.appendChild(createHeader("Duration","numeric"));
	return headerRow;
}


function poll() {
	var id = document.getElementById("requirementid").value;
	
	$.ajax({
	   url: statUrl + id,
	   data: {
	      format: 'json'
	   },
	   error: function(request, error) {
	      console.log("An error: " + error);
	      console.log(request);
	      showErrorMessage("Unable to get data: " + error);
	      setTimeout(poll, pollIntervall * 10);
	   },
	   dataType: 'json',
	   success: function(data) {
		  hideErrorMessage()
		  setGlobalStats(data.summary.requirement.statistics);
		  
		  var summaryTable = document.getElementById(summaryTableId);
	      $.each(data.summary.requirement.results, function( index, result ) {
	    	  var prefix = "result-" + result.id;
	    	  var row = document.getElementById(prefix);
	    	  if(row == undefined){
	    		  row = document.createElement("tr")
		    	  row.id = prefix;
	    		  row.className = result.status;
	    		  appendHeaderCell(row, "status", prefix + "-status", result);
	    		  //appendCell(row, "status", prefix + "-status", result.status);
	    		  appendCell(row, "", prefix + "-browser", result.browser);
	    		  appendCell(row, "", prefix + "-platform", result.platform);
	    		  appendCell(row, "", prefix + "-since", result.when.since);
	    		  appendJenkinsLinkCell(row, "", prefix + "jenkinslink", result);
	    		  appendCell(row, "numeric", prefix + "-duration", result.duration);
	    		  summaryTable.appendChild(row);
	    	  }else{
	    		  summaryTable.removeChild(row);
	    		  row = document.createElement("tr")
		    	  row.id = prefix;
	    		  row.className = result.status;
	    		  appendHeaderCell(row, "status", prefix + "-status", result);
	    		  //appendCell(row, "", prefix + "-status", result.status);
	    		  appendCell(row, "", prefix + "-browser", result.browser);
	    		  appendCell(row, "", prefix + "-platform", result.platform);
	    		  appendCell(row, "", prefix + "-since", result.when.since);
	    		  appendJenkinsLinkCell(row, "", prefix + "jenkinslink", result);
	    		  appendCell(row, "numeric", prefix + "-duration", result.duration);
	    		  //appendResultCell(row, "", prefix + "results", requirement.results);
	    		  summaryTable.appendChild(row);
	    	  }  
	      });
		  setTimeout(poll, pollIntervall);
	   },
	   type: 'GET'
	});
}

function appendHeaderCell(row, className, id, result){
	var cell = row.insertCell();
	var a = document.createElement("a");
	var linkText = document.createTextNode(result.status);
	a.appendChild(linkText);
	a.title = result.status
	a.href = "/result/" + result.id;
	 
	cell.appendChild(a);
	cell.className=headerCellClassName;
	cell.id = id;
	return cell;
}

function appendJenkinsLinkCell(row, className, id, result){
	var cell = row.insertCell();
	var a = document.createElement("a");
	var linkText = document.createTextNode(result.job);
	a.appendChild(linkText);
	a.title = result.job
	a.href = result.builUrl;
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

function setGlobalStats(statistics){
	setGlobalTotal("Total", statistics.summary.total);
	  setGlobalTested("Tested", statistics.summary.runs);
	  setGlobalPassed("Passed", statistics.summary.passRatio + "%");
	  setGlobalFailed("Fails", statistics.summary.failRatio + "%");
	  setGlobalUnstable("Unstable", statistics.summary.unstable);
	  setGlobalJobs("Jobs", 0);
}