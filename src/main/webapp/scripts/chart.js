var mychart = echarts.init(document.getElementById('main'));

var companies = new Map();
var company_list = [];
var series = [];

// How many days will show up
const date_range = 30;

function parse(arr){
    let array = JSON.parse(arr);
    // Create a map of companies
    for(let i = 0; i < array.length; i++) {
        let obj = array[i];
        let keys = Object.keys(obj);
        for (let j = 0; j < keys.length; j++) {
            com = keys[j];
            if (!companies.has(com)) {
                companies.set(com, 0);
            }
            companies.set(com, companies.get(com)+1);
        }
    }
    // Create a list of companies, ignore all companise those only appeared onece
    // for(let [company, value] of companies) {
    //     if (value > 1) {
    //         company_list.push(company);
    //     }
    // }
    for(let [company, value] of companies) {
        company_list.push(company);
    }

    // construct series for echarts
    for(let company of company_list) {
        // example:
        // {
        //     name: '联盟广告',
        //     type: 'line',
        //     stack: '总量',
        //     data: [220, 182, 191, 234, 290, 330, 310]
        // }
        let obj = {};
        obj["name"] = company;
        obj["type"] = "line";
        let data = new Array(date_range);
        let p = date_range-1;
        for(let i = array.length-1; i >= 0; i--) {
            let item = array[i];
            let n = item[company];
            if (typeof(n) == "undefined") {
                n = 0;
            }
            data[p--] = n;
        }
        obj["data"] = data;
        series.push(obj);
    }
    var dates = []
    for(let i = date_range-1; i >= 0; i--) {
        var s = i.toString();
        dates.push(s+"天前");
    }
    var option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            itemGap: 5,
            itemWidth: 22,
            bottom: "0%",
            data: company_list
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: dates
        },
        yAxis: {
            type: 'value'
        },
        series: series
    };

    mychart.setOption(option);

}
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

var req = JSON.stringify({});
var url = '/echarts';
ajax('GET', "http://localhost:8080/web2_war_exploded" + url, req,
    function(res){
    parse(res);
    },
    function() {
    showErrorMessage("ajax get fail");
    });


