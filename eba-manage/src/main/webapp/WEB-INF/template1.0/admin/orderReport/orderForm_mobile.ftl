[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>

    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/report.css" />
    <title>订单报表</title>
    <style>
        body{background:#f5f5f5;}
        .footer_nav .nav_index {
            background: url(${base}/resources/mobile/images/shouye-a.png) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
        .tab-top{background:#fff;padding:0.2rem 0;}
        .loading{
            width: 0.6rem;
            height: 0.6rem;
            position: fixed;
            top:calc(50% - 0.6rem);
            left:calc(50% - 0.3rem);
        }
    </style>
</head>
<body>
    <div class="loading">
        <img src="${base}/resources/mobile/images/loading.gif" alt="正在加载中...">
    </div>
    <div class="tab-top">
        <ul class="tab-ul reportClass">
            <li class="tab-item tab-active" data-report="orderReport">订单报表</li>
            <li class="tab-item" data-report="commodityReport">商品报表</li>
            <li class="tab-item" data-report="customerReport">客户报表</li>
        </ul>
    </div>
    <div class="orderForm">
        <h3 class="h3_title">订货单走势</h3>
        <div class="query_switch orderForm_switch">
            <input type="button" class="thisWeek select_color" value="本周" data-type="thisWeek" name="partitionSearch" />
            <input type="button" class="lastWeek" value="上周" data-type="lastWeek" name="partitionSearch"/>
            <input type="button" class="lastMonth " value="上月" data-type="lastMonth" name="partitionSearch"/>
            <input type="button" class="thisMonth " value="本月" data-type="thisMonth" name="partitionSearch"/>
            <i class="select_B"></i>
        </div>
        <div class="orderNum">
            <div class="formNum">
                <p class="order_p">订货单数</p>
                <span class="order_num divSum hasColor"></span>
            </div>
            <div class="price">
                <p class="order_p">订货单金额</p>
                <span class="order_num hasColor" id="amountSum"></span>
            </div>
            <span class="question_o"></span>
            <div class="center_q">
                <i class="sanjiao"></i>
                此处统计：等待审核、等待发货、发货中、已发货、已完成、申请取消状态下的订货单数
            </div>
        </div>
        <div>
            <div id="main" style="width:100%;height:6rem;"></div>
        </div>
    </div>



    <div class="purchaseForm">
        <h3 class="h3_title">采购单走势</h3>
        <div class="query_switch purchase_switch">
            <input type="button" class="thisWeek select_color" value="本周" data-type="thisWeek" name="partitionSearch" />
            <input type="button" class="lastWeek" value="上周" data-type="lastWeek" name="partitionSearch"/>
            <input type="button" class="lastMonth" value="上月" data-type="lastMonth" name="partitionSearch"/>
            <input type="button" class="thisMonth" value="本月" data-type="thisMonth" name="partitionSearch"/>
            <i class="select_B"></i>
        </div>
        <div class="orderNum">
            <div class="formNum">
                <p class="order_p">订货单数</p>
                <span class="order_num divSum2 hasColor"></span>
            </div>
            <div class="price">
                <p class="order_p">订货单金额</p>
                <span class="order_num hasColor"  id="amountSum2"></span>
            </div>
            <span class="question_o"></span>
            <div class="center_q">
                <i class="sanjiao"></i>
                此处统计：等待审核、等待发货、发货中、已发货、已完成、申请取消状态下的采购单数
            </div>
        </div>
        <div>
            <div id="own_main" style="width:100%;height:6rem;"></div>
        </div>
    </div>
    <div class="modelDiv"></div>

    [#include "/admin/include/footerNav.ftl"]

    <script src="${base}/resources/mobile/js/common.js"></script>
    <script src="${base}/resources/mobile/js/echarts.js"></script>

    <script>

        $(function(){

            var statuses=["pendingReview","pendingShipment","inShipment","shipped","completed","applyCancel"];
            orderFrom("thisWeek");
            function orderFrom(type){
                $.get("orderList.jhtml",{
                    "type":type,
                    "statuses":statuses,
                    "queyType":"orderForm"
                },function(data){

                    console.log(data);
                    var xData=[],values=[],orderSum=0,amountSum=0;
                    for(var value of data.list){
                        xData.push(value.reportDate);
                        values.push(value.orderNumber);
                        orderSum+=value.orderNumber;
                        amountSum+=value.amount;
                    };

                    $(".divSum").html(orderSum);
                    $("#amountSum").html(amountSum);
                    //图表
                    var myChart = echarts.init(document.getElementById("main"));
                    var option=getOption(xData,values);
                    myChart.setOption(option);
                    $('.loading').hide()
                })
            };

            $(".orderForm_switch input").on("click",function(){
                $(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
                $(this).addClass("select_color").siblings().removeClass("select_color");
                var type = $(this).attr("data-type");
                orderFrom(type);
            });


            purchaseOrder("thisWeek");
            function purchaseOrder(type){
                $.get("purchaseList.jhtml",{
                    "type":type,
                    "statuses":statuses,
                    "queyType":"purchaseForm"
                },function(data){
                    console.log(data);
                    var xData=[],values=[],orderSum=0,amountSum=0;
                    for(var value of data.list){
                        xData.push(value.reportDate);
                        values.push(value.orderNumber);
                        orderSum+=value.orderNumber;
                        amountSum+=value.amount;
                    };

                    $(".divSum2").html(orderSum);
                    $("#amountSum2").html(amountSum);
                    //图表
                    var myChart = echarts.init(document.getElementById("own_main"));
                    var option=getOption(xData,values);
                    myChart.setOption(option);
                })
            };

            $(".purchase_switch input").on("click",function(){
                $(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
                $(this).addClass("select_color").siblings().removeClass("select_color");
                var type = $(this).attr("data-type");
                purchaseOrder(type);
            });



            var xData = ['周一','周二','周三','周四','周五','周六','周日'];
            var values = [11, 11, 15, 13, 12, 13, 10];


            var myChart2 = echarts.init(document.getElementById("own_main"));
            var option2=getOption(xData,values);
            myChart2.setOption(option2);
            function getOption(xData,values){
                var option = {
                    title: {
                        text: '',
                        subtext: '(单位：笔)'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    xAxis:  {
                        type: 'category',
                        boundaryGap: false,
                        data: xData
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        }
                    },
                    series: [
                        {
                            name:'',
                            type:'line',
                            data:values,
                            itemStyle : {
                                normal : {
                                    color:'#4DA1FF',
                                    lineStyle:{
                                        color:"#4DA1FF"
                                    }
                                }
                            }
                        }
                    ]
                };
                return option;
            }


            var event_f = function(e){e.preventDefault()};
            $(".question_o").on("click",function(){
                if($(this).hasClass("q_select")){
                    $(this).removeClass("q_select");
                    $(".modelDiv").css("display","none");
                    $(this).siblings(".center_q").css("display","none");
                    document.body.removeEventListener('touchmove', event_f, false);
                }else{
                    $(this).addClass("q_select");
                    $(".modelDiv").css("display","block");
                    $(this).siblings(".center_q").css("display","block");
                    document.body.addEventListener('touchmove', event_f, false);
                }
            });
            $(".modelDiv").on("click",function(){
                $(".question_o").removeClass("q_select");
                $(".modelDiv").css("display","none");
                $(".question_o").siblings(".center_q").css("display","none");
                document.body.removeEventListener('touchmove', event_f, false);
            });




        });

    </script>
</body>
</html>
[/#escape]