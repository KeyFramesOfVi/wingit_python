var debounce = false;
$("#abttn").click(function() {
	if(debounce) return;
	debounce = true;
    $('html, body').stop().animate({
        scrollTop: $("#about").offset().top
    }, 1500, function() {debounce=false;});
});