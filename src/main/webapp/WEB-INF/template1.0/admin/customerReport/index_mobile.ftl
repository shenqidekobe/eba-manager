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
    <title>客户报表</title>
    <style>
        body{background:#f5f5f5;}
        .footer_nav .nav_index {
            background: url(${base}/resources/mobile/images/shouye-2.svg) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
        .orderForm{margin-bottom:1.5rem;}
        .tab-top{background:#fff;padding:0.2rem 0;}
    </style>
</head>
<body>

    <div class="tab-top">
        <ul class="tab-ul reportClass">
            <li class="tab-item" data-report="orderReport">订单报表</li>
            <li class="tab-item" data-report="commodityReport">商品报表</li>
            <li class="tab-item tab-active" data-report="customerReport">客户报表</li>
        </ul>
    </div>
    <div class="orderForm">
        <div class="query_switch">
            <input type="button" class="thisWeek select_color" value="本周" data-type="thisWeek" name="partitionSearch" />
            <input type="button" class="lastWeek" value="上周" data-type="lastWeek" name="partitionSearch"/>
            <input type="button" class="lastMonth " value="上月" data-type="lastMonth" name="partitionSearch"/>
            <input type="button" class="thisMonth " value="本月" data-type="thisMonth" name="partitionSearch"/>
            <i class="select_B"></i>
        </div>
        <div class="orderNum">
            <div class="formNum">
                <p class="order_p">订货客户数</p>
                <span class="order_num hasColor" id="orderCusNum"></span>
            </div>
            <div class="price">
                <p class="order_p">订货单数</p>
                <span class="order_num hasColor" id="orderNum"></span>
                <span class="question_o"></span>
            </div>
            <span class="question_o"></span>
            <div class="center_q">
                <i class="sanjiao"></i>
                此处统计：等待审核、等待发货、发货中、已发货、已完成、申请取消状态下的订货客户数
            </div>
        </div>
        <div style="padding-bottom:0.5rem;">
            <p class="noData2" style="display:none;position:absolute;width:100%;line-height: 5rem;text-align: center">暂无数据</p>
            <div id="main" style="width:calc(100% - 1.6rem);height:6rem; padding:0 0.8rem;"></div>
        </div>
    </div>

    <div class="modelDiv"></div>

    [#include "/admin/include/footerNav.ftl"]


    <script src="${base}/resources/mobile/js/echarts.js"></script>
    <script src="${base}/resources/mobile/js/common.js"></script>

    <script>

        $(function(){

            var statuses=["pendingReview","pendingShipment","inShipment","shipped","completed","applyCancel"];

            var params=[],myChart;

            orderFormTest("thisWeek");
            function orderFormTest(type){
                params=[
                    {name:"type",value:type},
                    {name:"statuses",value: statuses}
                ];
                console.log(params);
                $.ajax({
                    url: "list.jhtml",
                    type: "GET",
                    data: params,
                    dataType: "json",
                    success: function (data) {
                        console.log(data);

                        $("#main").css("height",(data.list.length*0.46+6)+"rem");

                        var list=data.list,legendData=[],seriesData=[],orderSum=0,amountSum=0;

                        for(var i = 0;i < list.length ;i++) {
                            var o = list[i];
                            legendData.push(o.name);
                            seriesData.push({
                                name: o.name,
                                value: o.orderNumber
                            });
                            orderSum+=o.orderNumber;
                            amountSum+=o.amount;
                        }

                        $("#orderCusNum").html(list.length);
                        $("#orderNum").html(orderSum);

                        console.log(orderSum);

                        if(seriesData.length){
                            $(".noData2").css("display","none");
                        }else{
                            $(".noData2").css("display","block");
                        }
                        myChart = echarts.init(document.getElementById("main"));
                        var color = color1;
                        if(seriesData.length<10){color = color2;}
                        var option = chartsForm(legendData,seriesData,orderSum,color);
                        myChart.setOption(option);
                    }
                })
            };

            $(".query_switch input").on("click",function(){
                $(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
                $(this).addClass("select_color").siblings().removeClass("select_color");
                var type = $(this).attr("data-type");
                myChart.dispose();
                orderFormTest(type);
            });

            function chartsForm(legendDat,seriesData,orderSum,color) {
                option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'center',
                        y: "bottom",
                        data: legendDat,
                        formatter:function(name){
                            var value=0;
                            $.each(seriesData,function (i,item) {
                                if(name == item.name){
                                    value=parseFloat((parseFloat((item.value/orderSum).toFixed(4))*100).toFixed(2));
                                }
                            });
                            return name+"  "+value+"%";
                        }
                    },
                    series: [
                        {
                            name: '所占百分比',
                            type: 'pie',
                            radius: ['40%', '80%'],
                            center: ['50%', '140px'],
                            avoidLabelOverlap: false,
                            label: {
                                normal: {
                                    show: false,
                                    position: 'center'
                                },
                                emphasis: {
                                    show: true,
                                    textStyle: {
                                        fontSize: '14',
                                        fontWeight: 'bold'
                                    }
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data: seriesData
                        }
                    ],
                    color:color
                };
                return option;
            };




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


            var color1 = ['#FE5F55','#E63946','#CA4236','#D96459','#ED7F2A','#F2A700','#FEC43F','#F2E394','#CBEAA6','#C0D684','#55BA7D','#94C9A9','#A8DADC','#8ABDDE','#457B9D','#006494','#314766','#814092','#A55BA4','#9897C7','#A5A7B7'];
            var color2 = ['#FE5F55','#CA4236','#ED7F2A','#FEC43F','#C0D684','#94C9A9','#8ABDDE','#006494','#814092','#9897C7'];
        });

    </script>
</body>
</html>
[/#escape]