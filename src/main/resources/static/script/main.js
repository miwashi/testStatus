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
}

function hideErrorMessage(){
	var d = document.getElementById('errorpopup');
	d.style.display="none";
}