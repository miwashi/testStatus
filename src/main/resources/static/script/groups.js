/**
 * 
 */
var teamsSummaryUrl = "/api/groups/all";
var teamSummarySectionId = "teams-summary-section";
var teamSummarySectionClassName ="";

var summaryTableId = "teams-summary-table";
var summaryTableClassName = "teams-summary-table";

var teamSummaryIdPrefix = "team-summary-";

var groupSummaryIdPrefix = "group-summary-";
var groupSummaryClassName = "group-summary-table";
var groupSummaryRowClassName = "group-summary-row";

var headerCellClassName = "row-header";
var numericCellClassName = "numeric";

$( document ).ready(function() {
    var summarySection = document.getElementById(teamSummarySectionId);
    if(summarySection == undefined){
    	return;
    }
    var summaryTable = document.createElement("table");
    summaryTable.id = summaryTableId;
    summaryTable.className = summaryTableClassName;
    summarySection.appendChild(summaryTable);
    console.log( "ready!" );
    pollTeamsSummary();
});

function getGroupIds(){
	var groupIds = [];
	var groupRows = document.getElementsByClassName(groupSummaryRowClassName);
	[].forEach.call(groupRows, function (row) {
		//console.log(row.id);  
		groupIds.push(row.id);
	});
	return groupIds;
}

function addTeamTable(teamName){
	var teamsTable = document.getElementById(summaryTableId);
	var teamsTeamRow = teamsTable.insertRow(0);
	var teamsHeaderRow = teamsTable.insertRow(0);
	
	var teamsTeamCell = teamsTeamRow.insertCell(0);	
	teamSummaryTable = document.createElement("table");
	teamSummaryTable.className=groupSummaryClassName;
	teamSummaryTable.id = groupSummaryIdPrefix + teamName;
	
	teamSummaryTable.appendChild(createGroupsTableHeaderRow(teamName));
	teamsTeamCell.appendChild(teamSummaryTable);
}

function createGroupsTableHeaderRow(teamName){
	var groupHeader = document.createElement("th");
	groupHeader.appendChild(document.createTextNode(teamName));
	groupHeader.className = headerCellClassName;
	
	var numberOfRequiremenstHeader = document.createElement("th");
	numberOfRequiremenstHeader.appendChild(document.createTextNode("Requirements"));
	numberOfRequiremenstHeader.className = numericCellClassName;
	
	var numberOfPassesHeader = document.createElement("th");
	numberOfPassesHeader.appendChild(document.createTextNode("Pass"));
	numberOfPassesHeader.className = numericCellClassName;
	
	var numberOfFailHeader = document.createElement("th");
	numberOfFailHeader.appendChild(document.createTextNode("Fail"));
	numberOfFailHeader.className = numericCellClassName;
	
	var numberOfUnstableHeader = document.createElement("th");
	numberOfUnstableHeader.appendChild(document.createTextNode("Unstable"));
	numberOfUnstableHeader.className = numericCellClassName;
	
	
	var headerRow = document.createElement("tr");
	headerRow.appendChild(groupHeader);
	headerRow.appendChild(numberOfRequiremenstHeader);
	headerRow.appendChild(numberOfPassesHeader);
	headerRow.appendChild(numberOfFailHeader);
	headerRow.appendChild(numberOfUnstableHeader);
	
	return headerRow;
}

function addGroupRow(group){
	var groupSummaryTable = document.getElementById(groupSummaryIdPrefix + group.teamName);
	if(groupSummaryTable == undefined){
		return;//Try later
	}
	
	var a = document.createElement("a");
	var linkText = document.createTextNode(group.name);
	a.appendChild(linkText);
	a.title = group.name;
	a.href = "/group/" + group.id;
	
	var rowCount = groupSummaryTable.rows.length;
	var row = groupSummaryTable.insertRow(rowCount);
	
	row.id = groupSummaryIdPrefix + group.id;
	row.className = groupSummaryRowClassName;
	
	var teamNumberOfUnstableCell = row.insertCell(0);
	var teamNumberOfFailCell = row.insertCell(0);
	var teamNumberOfPassCell = row.insertCell(0);
	var teamNumberOfRequirementsCell = row.insertCell(0);
	var teamHeaderCell = row.insertCell(0);
	
	teamHeaderCell.className=headerCellClassName;
	teamHeaderCell.appendChild(a);
	
	teamNumberOfRequirementsCell.appendChild(document.createTextNode(group.statistics.summary.total));
	teamNumberOfRequirementsCell.className=numericCellClassName;
	
	teamNumberOfPassCell.appendChild(document.createTextNode(group.statistics.summary.passes));
	teamNumberOfPassCell.className=numericCellClassName;
	
	teamNumberOfFailCell.appendChild(document.createTextNode(group.statistics.summary.failRatio + "%"));
	teamNumberOfFailCell.className=numericCellClassName;
	if(group.statistics.summary.failRatio>5){
		addClass(teamNumberOfFailCell, "high-fail-ratio");
	}
	teamNumberOfUnstableCell.appendChild(document.createTextNode(group.statistics.summary.unstableRatio + "%"));
	teamNumberOfUnstableCell.className=numericCellClassName;
	if(group.statistics.summary.unstableRatio>5){
		addClass(teamNumberOfUnstableCell, "high-unstable-ratio");
	}
}

function pollTeamsSummary() {
	$.ajax({
	   url: teamsSummaryUrl,
	   data: {
	      format: 'json'
	   },
	   error: function(request, error) {
		  showErrorMessage("Unable to connect to " + statUrl + "!");
	      console.log("An error: " + error);
	      console.log(request);
	      setTimeout(pollTeamsSummary, 20000);
	   },
	   dataType: 'json',
	   success: function(data) {
		  hideErrorMessage();
		  getGroupIds();
		  
		  setGlobalTotal("Total", data.statistics.summary.total);
		  setGlobalTested("Tested", data.statistics.summary.runs);
		  setGlobalPassed("Passed", data.statistics.summary.passRatio + "%");
		  setGlobalFailed("Fails", data.statistics.summary.failRatio + "%");
		  setGlobalUnstable("Unstable", data.statistics.summary.unstableRatio + "%");
		  setGlobalJobs("Jobs", 0);
		  
	      $.each(data.teams, function( index, team ) {
	    	  var id = groupSummaryIdPrefix + team.name;
	    	  var teamTable = document.getElementById(id);
	    	  if(teamTable == undefined){
	    		  console.log("Adding team " + team.name + "!");
	    		  addTeamTable(team.name);
	    	  }
	      });
	      
	      $.each(data.groups, function( index, group ) {
	    	  var id = groupSummaryIdPrefix + group.id;
	    	  var groupRow = document.getElementById(id);
	    	  if(groupRow == undefined){
	    		  console.log("Adding team " + group.name + " for " + group.teamName + "!");
	    		  addGroupRow(group);
	    	  }
	      });
	      
	      setTimeout(pollTeamsSummary, 2000);
	   },
	   type: 'GET'
	});
}