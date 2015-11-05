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