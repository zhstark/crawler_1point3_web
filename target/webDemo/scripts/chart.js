// How many days will show up
const date_range = 180;

function parse(arr, chartId){
    let array = JSON.parse(arr);
    var companies = new Map();
    var xAxis = new Array(date_range);

    getCompaniesAndXAxis(array, companies, xAxis);
    var data = getData(companies, array);
    data["dates"] = xAxis;
    createEchart(data.legendData, data.selected, data.dates, data.seriesData, chartId);
}

/**
 * Create a map contains all companies and thier heat rate
 * @param {*} array 
 * @return [company name, number of showing up ]
 */
function getCompaniesAndXAxis(array, companies, xAxis) {
    // Create a map of companies
    for(let i = 0; i < array.length; i++) {
        let obj = array[i];
        let keys = Object.keys(obj);
        for (let j = 0; j < keys.length; j++) {
            com = keys[j];
            if (com == "Date"){
                if (date_range-i > 0) {
                    xAxis[date_range-1-i] = parseInt(obj[com] / 100).toString()+"-"+(obj[com] % 100).toString();
                }
                continue;
            }
            if (!companies.has(com)) {
                companies.set(com, 0);
            }
            companies.set(com, companies.get(com)+1);
        }
    }
    return;
}
/**
 * 
 * @param {*} companies - A map contains all companies and thier heat rate
 * @param array - Input json array
 * @returns 
 */
function getData(companies, array) {
    var company_list = [];
    var selected = {};
    var seriesData = [];
    for(let [company, value] of companies) {
        company_list.push(company);
    }

    company_list.sort(function (a, b) {
        return companies.get(b) - companies.get(a);
    })

    for(let i = 0; i < company_list.length; i++) {
        let company = company_list[i];
        selected[company] = i < 6;

        // construct series for echarts
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
        for(let i = 0; i < array.length; i++) {
            let item = array[i];
            let n = item[company];
            if (typeof(n) == "undefined") {
                n = 0;
            }
            data[p--] = n;
        }
        obj["data"] = data;
        obj["smooth"] = true;
        obj["lineStyle"] = {
            width:1,
        }
        seriesData.push(obj);
    }

    return {
        legendData: company_list,
        seriesData: seriesData,
        selected: selected,
    };
}
/**
 * Create Echart
 * @param {list} company_list 
 * @param selected
 * @param {list} dates 
 * @param {list} series 
 */
function createEchart(company_list, selected, dates, series, chartId) {
    var mychart = echarts.init(document.getElementById(chartId));
    var title = chartId;
    if (chartId == 'jobs') {
        title = '求职帖统计';
    } else if (chartId == 'interviews') {
        title = '面经帖统计';
    }
    var option = {
        title:{
            text: title,
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            type: 'scroll',
            itemGap: 5,
            itemWidth: 22,
            left: "15%",
            right: "5%",
            data: company_list,

            selected: selected
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
let jobsUrl = '/jobs';
var url = window.location.href;
ajax('GET', url + jobsUrl, req,
    function(res){
    parse(res, "jobs");
    },
    function() {
    showErrorMessage("ajax get fail");
    });

let interviewsUrl = '/interviews';
ajax('GET', url+interviewsUrl, req,
    function(res){
    parse(res, "interviews");
    },
    function() {
    showErrorMessage("ajax get fail");
    });
let lcUrl = '/leetcode-interview-questions'
ajax('GET', url + lcUrl, req,
    function(res){
    parse(res, "lc");
    },
    function() {
    showErrorMessage("ajax get fail");
    });
