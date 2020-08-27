// (function(){
function getQueryVariable(variable)
    {
        let query = window.location.search.substring(1);
        let vars = query.split("&");
        for (let i=0;i<vars.length;i++) {
            let pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    }
    if (getQueryVariable("company") == false) {
        let url = window.location.href.split('?')[0];
        document.location = url+"?company=Amazon"
    }
// })()