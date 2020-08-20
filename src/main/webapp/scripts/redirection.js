// (function(){
function getQueryVariable(variable)
    {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
                var pair = vars[i].split("=");
                if(pair[0] == variable){return pair[1];}
        }
        return(false);
    }
    if (getQueryVariable("company") == false) {
        var url = window.location.href.split('?')[0];
        document.location = url+"?company=Amazon"
    }
// })()