var tab1Option = {
    title: {
        text: '折线图堆叠'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
            name: '视频广告',
            type: 'line',
            stack: '总量',
            data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
            name: '直接访问',
            type: 'line',
            stack: '总量',
            data: [320, 332, 301, 334, 390, 330, 320]
        },
        {
            name: '搜索引擎',
            type: 'line',
            stack: '总量',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]
};


let tab2Option = {
    title: {
        text: '折线图堆叠',
        bottom: 0,
        left: "50%"
    },
    tooltip: {
        trigger: 'axis',
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
        type: 'scroll',
        right: "15%",
        left: "15%",
        top: 0,
        data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
            name: '视频广告',
            type: 'line',
            stack: '总量',
            data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
            name: '直接访问',
            type: 'line',
            stack: '总量',
            data: [320, 332, 301, 334, 390, 330, 320]
        },
        {
            name: '搜索引擎',
            type: 'line',
            stack: '总量',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]
};
var $tab1 = document.getElementById('chart1');
var $tab2 = document.getElementById('chart2');
var tab1Table = echarts.init($tab1);
tab1Table.setOption(tab1Option);

/* shown.bs.tab为tab选项卡高亮 */
$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
    /* 获取已激活的标签页的名称 */
    /* hash 属性是一个可读可写的字符串，该字符串是 URL 从 # 号开始的部分 */
    var activeTab = $(e.target)[0].hash;
    /* 当相应的标签被点击时，进行对应的图表渲染 */
    if (activeTab == "#tab2") {
        /* 释放图表实例，使实例不可用,不加上这个，会报错： */
        /* there is a chart instance     already initialized on the dom */
        echarts.dispose($tab2);
        var tab2Table = echarts.init($tab2);
        tab2Table.setOption(tab2Option);
    } else if (activeTab == "#tab3") {
        echarts.dispose($tab3);
        var tab3Table = echarts.init($tab3);
        tab3Table.setOption(tab3Option);
    }
});

$('#')