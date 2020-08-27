var days = 180;

var url=rootUrl+"get_company_info?source=analysis&company="+company+"&days="+days.toString();

function draw(legends, dates, series, chartId) {
    let mychart = echarts.init(document.getElementById(chartId));
    let title = chartId;
    let option = {
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
            data: legends,

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

Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

function fill_company_chart(res) {
    let obj = JSON.parse(res);
    let series = [];
    let data = obj[0];
    let legends = Object.keys(data);
    for (let key of legends ){
        let temp = {};
        temp["name"] = key;
        temp["type"] = "line";
        temp["data"] = data[key];
        temp["smooth"] = true;
        temp["lineStyle"] = {
            width:1,
        }
        series.push(temp);
    }
    let dates=[];
    for (let i = days-1; i >= 0; i--)
    {
        let date = new Date();
        date.setDate(date.getDate() - i);
        date.format("yyyy-MM-dd");
        dates.push(date.format("yyyy-MM-dd").substring(5));
    }
    draw(legends, dates, series, "analysis");
}


ajax('GET', url, req, function(res) {
    fill_company_chart(res);
}, function() {
    showErrorMessage("ajax get failed");
})