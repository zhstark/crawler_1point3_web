/**
 * @param method - GET|POST|PUT|DELETE
 * @param url - API end point
 * @param callback - This the successful callback
 * @param errorHandler - This is the failed callback
 */
function ajax(method, url, data, callback, errorHandler) {
    var xhr = new XMLHttpRequest();

    xhr.open(method, url, true);

    xhr.onload = function() {
        if (xhr.status === 200) {
            callback(xhr.responseText);
        } else {
            errorHandler();
        }
    };

    xhr.onerror = function() {
        console.error("The request couldn't be completed.");
        errorHandler();
    };

    if (data === null) {
        xhr.send();
    } else {
        xhr.setRequestHeader("Content-Type",
            "application/json;charset=utf-8");
        xhr.send(data);
    }
}

function showErrorMessage(msg) {
    console.log(msg);
}

// function getQueryVariable(variable)
// {
//     var query = window.location.search.substring(1);
//     var vars = query.split("&");
//     for (var i=0;i<vars.length;i++) {
//             var pair = vars[i].split("=");
//             if(pair[0] == variable){return pair[1];}
//     }
//     return(false);
// }
function get_table_html(data) {
    var titles=data["titles"];
    var levels=data["levels"];
    var notes=data["notes"];
    var salaries=data["salaries"];

    var tr="<tr><th>Title</th><th>Level</th><th>Notes</th><th>Salary</th></tr>";
    for (let i = 0; i < titles.length; i++) {
        let temp="<tr><td>"+titles[i]+"</td>" +
                "<td>"+levels[i]+"</td>" +
                "<td>"+notes[i]+"</td>" +
                "<td>"+salaries[i]+"</td></tr>";
        tr = tr+temp;
    }
    return tr
}

function insert_table(str) {
    document.getElementById("level-table").innerHTML=str;
}

function main(data) {
    let obj = JSON.parse(data);
    insert_table(get_table_html(obj[0]));
}

var company=getQueryVariable("company");
document.getElementById("company-name").innerHTML=company
var url="http://localhost:8080/web2_war_exploded/get_company_info?source=levels&company="+company;
var req = JSON.stringify({});
ajax('GET', url, req, function(res) {
    main(res);
}, function() {
    showErrorMessage("ajax get failed");
})

