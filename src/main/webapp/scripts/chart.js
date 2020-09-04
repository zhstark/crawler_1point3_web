
var chart1tab1
var chart1tab2
var chart2tab1
var chart2tab2
var chart3tab1
var chart3tab2
function parse(arr, chartId){
    let array = JSON.parse(arr);
    var companies = new Map();
    var xAxis = new Array();

    getCompaniesAndXAxis(array, companies, xAxis);
    var data = getData(companies, array);
    data["dates"] = xAxis;
    return createEchart(data.legendData, data.selected, data.dates, data.seriesData, chartId);
}

/**
 * Create a map contains all companies and thier heat rate
 * @param {*} array 
 * @param {Map} companies - empty map to collection data
 * @param {array} xAxis - empty array
 * @return [company name, number of showing up ]
 */
function getCompaniesAndXAxis(array, companies, xAxis) {
    for (let i = array.length-1; i >= 0; i--) {
        let obj = array[i];
        let keys = Object.keys(obj);
        for (let com of keys) {
            if (com == 'Date') {
                xAxis.push(parseInt(obj[com] / 100).toString()+"-"+(obj[com] % 100).toString());
            } else {
                if (!companies.has(com)) {
                    companies.set(com, 0);
                }
                companies.set(com, companies.get(com)+1);
            }
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
        let data = new Array();
        for(let j = array.length-1; j >= 0; j--) {
            let item = array[j];
            let n = item[company];
            if (typeof(n) == "undefined") {
                n = 0;
            }
            data.push(n);
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
    var title = chartId;
    if (chartId == 'jobs') {
        title = '求职帖统计';
    } else if (chartId == 'interviews') {
        title = '面经帖统计';
    } else if (chartId == 'lc') {
        title = 'LeetCode 面经'
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

    return option;
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

var $chart1tab1 = document.getElementById('chart1tab1');
var $chart1tab2 = document.getElementById('chart1tab2');
var $chart2tab1 = document.getElementById('chart2tab1');
var $chart2tab2 = document.getElementById('chart2tab2');
var $chart3tab1 = document.getElementById('chart3tab1');
var $chart3tab2 = document.getElementById('chart3tab2');

// chart option
var c1t1Option;
var c1t2Option;
var c2t1Option;
var c2t2Option;
var c3t1Option;
var c3t2Option;

var req = JSON.stringify({});
let jobsUrl = '/jobs';
var url = window.location.href;

// 1
ajax('GET', url + jobsUrl, req,
    function(res){
    c1t1Option = parse(res, "jobs");
    var chart1Tab1Table = echarts.init($chart1tab1);
    chart1Tab1Table.setOption(c1t1Option);
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);
ajax('GET', url + jobsUrl+"?byWeek=true", req,
    function(res){
    c1t2Option = parse(res, "jobs");
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);

//2
let interviewsUrl = '/interviews';
ajax('GET', url+interviewsUrl, req,
    function(res){
    c2t1Option = parse(res, "interviews");
    var chart2Tab1Table = echarts.init($chart2tab1);
    chart2Tab1Table.setOption(c2t1Option);
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);
ajax('GET', url+interviewsUrl+"?byWeek=true", req,
    function(res){
    c2t2Option = parse(res, "interviews");
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);

// 3
let lcUrl = '/leetcode-interview-questions'
ajax('GET', url + lcUrl, req,
    function(res){
    c3t1Option = parse(res, "lc");
    var chart3Tab1Table = echarts.init($chart3tab1);
    chart3Tab1Table.setOption(c3t1Option);
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);
ajax('GET', url + lcUrl+"?byWeek=true", req,
    function(res){
    c3t2Option = parse(res, "lc");
    },
    function() {
    showErrorMessage("ajax get fail");
    }
);

/* shown.bs.tab为tab选项卡高亮 */
$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
    /* 获取已激活的标签页的名称 */
    /* hash 属性是一个可读可写的字符串，该字符串是 URL 从 # 号开始的部分 */
    var activeTab = $(e.target)[0].hash;
    /* 当相应的标签被点击时，进行对应的图表渲染 */
    if (activeTab == "#chart1tab2") {
        /* 释放图表实例，使实例不可用,不加上这个，会报错： */
        /* there is a chart instance     already initialized on the dom */
        echarts.dispose($chart1tab2);
        var chart1Tab2Table = echarts.init($chart1tab2);
        chart1Tab2Table.setOption(c1t2Option);
    } else if (activeTab == "#chart2tab2") {
        echarts.dispose($chart2tab2);
        var chart2Tab2Table = echarts.init($chart2tab2);
        chart2Tab2Table.setOption(c2t2Option);
    } else if (activeTab == "#chart3tab2") {
        echarts.dispose($chart3tab2);
        var chart3Tab2Table = echarts.init($chart3tab2);
        chart3Tab2Table.setOption(c3t2Option);
    }
});