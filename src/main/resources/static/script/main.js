$( document ).ready(function() {
	var d = document.getElementById('errorpopup');
    positionErrorDialog();
});

$( window ).resize(function() {
	positionErrorDialog();
});

function positionErrorDialog(){
	var w = $(window).width();
	var h = $(window).height();
	var d = document.getElementById('errorpopup');
	var divW = $(d).width();
	var divH = $(d).height();

	d.style.position="absolute";
	d.style.top = (h/2)-(divH/2)+"px";
	d.style.left = (w/2)-(divW/2)+"px";
}

function showErrorMessage(message){
	var d = document.getElementById('errorpopup');
	d.innerHTML = message;
	d.style.display="block";
}

function hideErrorMessage(){
	var d = document.getElementById('errorpopup');
	d.style.display="none";
}

function setGlobalTotal(header, value){
	document.getElementById("mainstat_requirements_hdr").innerHTML = header;
	document.getElementById("mainstat_requirements").innerHTML = value;
}

function setGlobalTested(header, value){
	document.getElementById("mainstat_tested_hdr").innerHTML = header;
	document.getElementById("mainstat_tested").innerHTML = value;
}

function setGlobalPassed(header, value){
	document.getElementById("mainstat_verified_hdr").innerHTML = header;
	document.getElementById("mainstat_verified").innerHTML = value;
}

function setGlobalFailed(header, value){
	document.getElementById("mainstat_failed_hdr").innerHTML = header;
	document.getElementById("mainstat_failed").innerHTML = value;
}

function setGlobalUnstable(header, value){
	document.getElementById("mainstat_unstable_hdr").innerHTML = header;
	document.getElementById("mainstat_unstable").innerHTML = value;
}

function setGlobalJobs(header, value){
	document.getElementById("mainstat_jobs_hdr").innerHTML = header;
	document.getElementById("mainstat_jobs").innerHTML = value;
}

function addClass(element, className){
	element.className += (element.className ? " " : "") + className;
}

function createHeader(headerText, className){
	var headerItem = document.createElement("th");
	headerItem.appendChild(document.createTextNode(headerText));
	headerItem.className = className;
	return headerItem;
}

var Job = function(){
	job = {};
	
	var columns = [
	              {
	              	key : 'job', 
	              	name: 'Job',
	              	className: 'job'
	              }, 
	              {
	              	key : 'touched', 
	              	name: 'Touched',
	              	className: 'touched'
	              }, 
	              {
	              	key : 'status', 
	              	name: 'Status',
	              	className: 'status'
	              } 
	           ];
	
	job.createTableInSection = function(section, tableId, tableClassName){
		var table = document.createElement("table");
	    table.id = tableId;
		table.className = tableClassName;
		table.appendChild(Job.createHeaderRow());
		section.appendChild(table);
	};
	
	job.createHeaderRow = function(){
		var headerRow = document.createElement("tr");
		for(var column of columns){
			var headerItem = document.createElement("th");
			headerItem.appendChild(document.createTextNode(column.name));
			headerItem.className = column.className;
			headerRow.appendChild(headerItem);
		}
		return headerRow;
	}
	
	job.createResultRow = function(job, sectionPrefix){
		var resultRow = document.createElement("tr");
		for(var column of columns){
			cell = Job.createCell(job, sectionPrefix, column.key);
			resultRow.appendChild(cell);
		}
		return resultRow;
	}
	
	job.createCell = function (job, sectionPrefix, header){
		var cell = document.createElement("td");
		var cellClassName = header;
		var cellId = sectionPrefix + "-" + job.key + "-" + header;
		var value;		
		
		for(var column of columns){
			if((header!=null) && ((new String(header)).localeCompare(column.key)==0)){
				cellClassName = column.className;
			}
		}
		
		switch(header){
		case "job":
			var linkText = job.name;
			var href = "/job/" + job.key;
			var title = "";
			cell = Requirement.createAnchorCell(linkText, href, title);
			break;
		case "touched":
			value = job.statistics.runs.lastRun.since;
			break;
		case "duration":
			value = job.statistics.duration.avg;
			break;	
		case "status":
			value = job.statistics.runs.status;
			break;
		default:
		}
		if(value != undefined || value!=null){
			var textNode = document.createTextNode(value);
			cell.appendChild(textNode);
		}
		cell.className=cellClassName;
		cell.id = cellId;
		return cell;
	}
	
	return job;
}();

var Requirement = function(){
	var requirement = {};
	
	var columns = [

		{
	   	key : 'team_to_requirement', 
	   	name: 'Team',
	   	className: 'team_to_requirement'
	   },
		{
       	key : 'requirement', 
       	name: 'Requirement',
       	className: 'requirement'
       }, 
       {
       	key : 'touched', 
       	name: 'Touched',
       	className: 'touched'
       }, 
       {
       	key : 'duration', 
       	name: 'Duration',
       	className: 'duration'
       }, 
       {
       	key : 'status', 
       	name: 'Status',
       	className: 'status'
       }, 
       {
          	key : 'fails', 
          	name: 'fails',
          	className: 'fails'
      }, 
      {
     	key : 'passes', 
     	name: 'passes',
     	className: 'passes'
     }, 
     {
     	key : 'failRatio', 
     	name: 'failRatio',
     	className: 'failRatio'
     },
     
     {
      	key : 'statusChange', 
      	name: 'statusChange',
      	className: 'statusChange'
      }
       
    ];
	
	requirement.createResultRow = function(requirement, sectionPrefix){
		var resultRow = document.createElement("tr");
		for(var column of columns){
			cell = Requirement.createCell(requirement, sectionPrefix, column.key);
			resultRow.appendChild(cell);
		}
		return resultRow;
	}
	
	requirement.createHeaderRow = function(){
			var headerRow = document.createElement("tr");
			for(var column of columns){
				var headerItem = document.createElement("th");
				headerItem.appendChild(document.createTextNode(column.name));
				headerItem.className = column.className;
				headerRow.appendChild(headerItem);
			}
			return headerRow;
	}
	
	requirement.createTableInSection = function(section, tableId, tableClassName){
		var table = document.createElement("table");
	    table.id = tableId;
		table.className = tableClassName;
		table.appendChild(Requirement.createHeaderRow());
		section.appendChild(table);
	};
	
	requirement.createCell = function (requirement, sectionPrefix, header){
		var cell = document.createElement("td");
		var cellClassName = header;
		var cellId = sectionPrefix + "-" + requirement.id + "-" + header;
		var value;		
		
		for(var column of columns){
			if((header!=null) && ((new String(header)).localeCompare(column.key)==0)){
				cellClassName = column.className;
			}
		}
		
		switch(header){
		case "team_to_requirement":
			var linkText = requirement.team;
			var href = "/requirement/" + requirement.id;
			var title = "";
			cell = Requirement.createAnchorCell(linkText, href, title);
			break;
		case "requirement":
			var linkText = requirement.readableName + "" + requirement.parameter;
			var href = "/requirement/" + requirement.id;
			var title = "";
			cell = Requirement.createAnchorCell(linkText, href, title);
			break;
		case "touched":
			value = requirement.statistics.lastresult.when.since;
			break;
		case "duration":
			value = requirement.statistics.duration.intervall;
			break;	
		case "status":
			cell = Requirement.createStatusCell("", sectionPrefix + "results", requirement.results);
			break;
		case "passes":
			value = requirement.statistics.summary.passes;
			break;
		case "fails":
			value = requirement.statistics.summary.fails;
			break;
		case "failRatio":
			value = requirement.statistics.summary.failRatio;
			break;
		case "statusChange":
			value = requirement.whenStatusChanged.since;
			break;
		default:
		}
		if(value != undefined || value!=null){
			var textNode = document.createTextNode(value);
			cell.appendChild(textNode);
		}
		cell.className=cellClassName;
		cell.id = cellId;
		return cell;
	}
	
	requirement.createAnchorCell = function (linkText, href, title){
		var cell = document.createElement("td");
		var a = document.createElement("a");
		var linkTextNode = document.createTextNode(linkText);
		a.appendChild(linkTextNode);
		a.title = title;
		a.href = href;
		cell.appendChild(a);
		return cell;
	};
	
	requirement.createStatusCell = function(className, id, results){
		var cell = document.createElement("td");
		var testlightsTable = document.createElement("table");
		var testLightsRow = document.createElement("tr");
		testlightsTable.appendChild(testLightsRow);
		testlightsTable.className = "testlights";
		cell.appendChild(testlightsTable);
		cell.className="testlights";
		cell.id = id;
		
		for(var i = 0 ; i < 10; i++){
			var img = document.createElement("img");
			var testLightCell = testLightsRow.insertCell();
			img.className="resultindicator";
			
			var result = results[i];
			if(result == undefined){
				img.src="/img/white_light.png";
				img.title= "not run yet";
				testLightCell.appendChild(img);
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
				
				var a = document.createElement("a");
				a.appendChild(img);
				a.href =result.buildUrl;
				testLightCell.appendChild(a);
			}
			testLightCell.className="testlights";
		}
		return cell;
	};
	
	return requirement;
}();