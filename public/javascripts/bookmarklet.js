(function(){

    var v = "1.9.0";

    if (window.jQuery === undefined || window.jQuery.fn.jquery < v) {
        var done = false;
        var script = document.createElement("script");
        script.src = "http://ajax.googleapis.com/ajax/libs/jquery/" + v + "/jquery.min.js";
        script.onload = script.onreadystatechange = function(){
            if (!done && (!this.readyState || this.readyState == "loaded" || this.readyState == "complete")) {
                done = true;
                initMyBookmarklet();
            }
        };
        document.getElementsByTagName("head")[0].appendChild(script);
    } else {
        initMyBookmarklet();
    }

    function initMyBookmarklet() {
        (window.myBookmarklet = function() {

                if(typeof(dzPlayer) != "undefined"){
                window.open("http://pc-sg-013:9000?source=DEEZER&id=" + dzPlayer.getCurrentSongInfo().SNG_ID,"Share Music! " );
                }
                else {
                   var $iframe = $("#app-player").contents();
                   var $uri = $("#track-name a",$iframe).attr("href");
                   var $id = $uri.substring($uri.lastIndexOf("/")  + 1);
                    window.open("http://pc-sg-013:9000?source=SPOTIFY&id=spotify:track:" + $id,"Share Music! " );
                }


        })();
    }

})();