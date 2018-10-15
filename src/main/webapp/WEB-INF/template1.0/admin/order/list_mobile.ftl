[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订货单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
     <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        .tab-top{padding:0.2rem 0;}
        .tab-top .tab-ul{background:#fbfbfb}
        .footer_nav .nav_order {
            background: url(${base}/resources/mobile/images/dingdan-a.png) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
        .p_loading{margin-bottom:0;}
    </style>
</head>
<body>

    <div class="order-box">
        <div class="tab-top [#if isDistributionModel != 'true'] twoBu_top [/#if]">
            <ul class="tab-ul orderClass">
                <li class="tab-item tab-active"><a data-order="order" href="javascript:;">订货单</a></li>
                <li class="tab-item"><a data-order="ownOrder" href="javascript:;">采购单</a></li>
                [#if isDistributionModel == "true"]
                    <li class="tab-item"><a data-order="distributionOrder" href="javascript:;">分销单审核</a></li>
                [/#if]
            </ul>
        </div>
        <input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageable.pageNumber}" />
        <input type="hidden" name="totalPages" id="totalPages" value="${page.getTotalPages()}" />
        
        <form id="listForm" action="list.jhtml" method="get">
        <input type="hidden" name="pageSize" id="pageSize" value="${page.pageable.pageSize}" />
            <input type="hidden" id="supplierId" value="${supplierId}">

            <div class="search-box">
                <div class="search-con">
                    <div class="search-img"></div>
                    <div class="search-text">
                        <input id="searchName" readonly="readonly" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入编号/客户名称" onclick="goSearch()" />
                    </div>
                </div>
            </div>
            <div class="select-box">
                <div id="wrapper">
                    <div id="">
                            <ul id="thelist" class="select-ul">
                                <input type="hidden" id="status" name="status" value="${status}">
                                <li [#if status??] class="select-item" [#else] class="select-item select-active" [/#if] data-status="">全部</li>
                                <li [#if status == "pendingReview"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="pendingReview">待审核</li>
                                <li [#if status == "pendingShipment"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="pendingShipment">待发货</li>
                                <li [#if status == "inShipment"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="inShipment">发货中</li>
                                <li [#if status == "applyCancel"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="applyCancel">申请取消</li>
                                <!--
                                <li [#if status == "shipped"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="shipped">已发货</li>
                                <li [#if status == "completed"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="completed">已完成</li>
                                <li [#if status == "canceled"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="canceled">已取消</li>
                                <li [#if status == "denied"] class="select-item select-active" [#else] class="select-item" [/#if] data-status="denied">已拒绝</li>
                                -->
                            </ul>
                        </div>
                    </div>
                </div>
        </form>
        <div class="list-box" id="dataList">
            [#if page.content.size() <= 0 ]
                <div class="no-data" >无数据</div>
            [/#if]
            [#list page.content as order]
                <div class="order-con" onclick="view(${order.id});">
                    <div class="order-top">
                        <div class="order-name">${order.sn}</div>
                        <div class="order-status ${order.status}">${message("Order.Status." + order.status)}</div>
                        [#if order.type != "general" && order.type != "formal"]
                            [#if order.type == "billDistribution"]
                                [#if order.toSupplier.id == supplierId]
                                   <span class="spanFx">分销</span>
                                [/#if]
                            [#else]
                                <span class="spanFx">分销</span>
                            [/#if]
                        [/#if]
                    </div>
                    <div class="order-code">
                        <div class="code-name">
                            [#if order.type == "billDistribution"]
                                [#if order.supplier.id == supplierId]
                                    ${order.need.supplier.name}
                                [#else]
                                    ${order.need.name}
                                [/#if]
                            [#elseif order.type == "formal"]
                                ${order.need.supplier.name}
                            [#else]
                                ${order.need.name}
                            [/#if]
                        </div>
                    </div>
                    <div class="order-count">共<span class="count">${order.quantity}</span>件商品</div>

                </div>
            [/#list]
            <!--
            <div class="order-con order-cancel">
                <div class="order-top">
                    <div class="order-name">妙计旅行</div>
                    <div class="order-status">已取消</div>
                </div>
                <div class="order-code">
                    <div class="code-name">订单编号</div>
                    <div class="code">4852459865742365832</div>
                </div>
                <div class="order-count">共<span class="count">43</span>件商品</div>
            </div>
            -->
        </div>
    [#if page.getTotalPages() > 1 ]
        <p class="p_loading">正在加载...</p>
    [/#if]
    <div class="footerDivE"></div>
    </div>
    [#include "/admin/include/footerNav.ftl"]


<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/iscroll.js"></script>
</body>
    <script type="text/javascript">
//    $(function(){
        //状态栏滑动效果
        /*var myScroll;
        function loaded() {
            myScroll = new iScroll('wrapper');
        }
        document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
        document.addEventListener('DOMContentLoaded', loaded, false);*/

        $("#thelist li").on("click",function(){
            var status=$(this).data("status");
            $("#status").val(status);
            $("#thelist li").each(function(){
                if($(this).hasClass("select-active")){
                    $(this).removeClass("select-active");
                }
            });
            $(this).addClass("select-active");
            $("#listForm").submit();
        });

        var boolAjax = false;
        //加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            var supplierId = $("#supplierId").val();
            console.log(pageNumber+'----'+totalPages);
            if(parseInt(pageNumber) >= parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var next = ++pageNumber;
            var searchName=$("#searchName").val();
            var status=$("#status").val();
            var param={
                "status":status,
                "searchName":searchName,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            $.get("asyncList.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data.orderList;
                    console.log(orders);
                    var textHtml='';
                    $.each(orders,function(i,n){

                        var imgHtml = '';
                        if(n.type != "general" && n.type != "formal"){
                            if(n.type == "billDistribution"){
                                if(n.toSupplierId == supplierId){
                                    imgHtml = '<span class="spanFx">分销</span>';
                                }
                            }else{
                                imgHtml = '<span class="spanFx">分销</span>'
                            }
                        }

                        textHtml+='<div class="order-con" onclick="view('+n.orderId+')">';
                        textHtml+='<div class="order-top">';
                        textHtml+='<div class="order-name">'+n.sn+'</div>';
                        textHtml+=imgHtml;
                        textHtml+='<div class="order-status '+ n.statusString +'">'+getStatus(n.status)+'</div>';
                        textHtml+='</div>';
                        textHtml+='<div class="order-code">';
                        textHtml+='<div class="code-name">'+n.needName+'</div>';
                        textHtml+='</div>';
                        textHtml+='<div class="order-count">共<span class="count">'+n.quantity+'</span>件商品</div>';
                        textHtml+='</div>';
                    });
                    $("#dataList").append(textHtml);

                    $("#pageNumber").val(next);
                    boolAjax = false;
                }else {
                    errorInfoFun(o.msg);
                }
            });
        }

        function view(id){
            window.location.href="view.jhtml?id="+id;
        }
        function goSearch(){
            var status=$("#status").val();
            window.location.href="search.jhtml?status="+status+"&supplierId="+$("#supplierId").val();
        }


        $(window).scroll(function(){
            /*加载标签的Y轴坐标*/
            var top = $(".p_loading").offset().top;
            //滚动跳的高度
            var divScroll = $(window).scrollTop();
            //手机屏幕的高度
            var divHeight = $(window).height();

            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                    boolAjax=true;
                    loadData();
            }
        });

        function getStatus(statusCode){
            var status='';
            switch(statusCode){
                case 0:
                    status='等待付款';
                    break;
                case 1:
                    status='等待审核';
                    break;
                case 2:
                    status='等待发货';
                    break;
                case 3:
                    status='已发货';
                    break;
                case 4:
                    status='已收货';
                    break;
                case 5:
                    status='已完成';
                    break;
                case 6:
                    status='已失败';
                    break;
                case 7:
                    status='已取消';
                    break;
                case 8:
                    status='已拒绝';
                    break;
                case 9:
                    status='申请取消';
                    break;
                case 10:
                    status='通过取消';
                    break;
                case 11:
                    status='拒绝取消';
                    break;
                case 12:
                    status='发货中';
                    break;
            }
            return status;
        }
//    })
    </script>
</html>
[/#escape]