[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>采购单搜索</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        .search-box .search-con{
            width:83%;
            float:left;
        }
        .search-box .search-text{width:calc(100% - 0.8rem)}
        .noInfoList{display:block;}
        .p_loading{display:none;}
    </style>
</head>
<body>

    <div class="order-box">
        <form id="listForm" action="list.jhtml" method="get">
            <input type="hidden" name="totalPages" id="totalPages" value="" />
            <input type="hidden" name="pageNumber" id="pageNumber" value="1" />
            <input type="hidden" name="pageSize" id="pageSize" value="8" />
            <input type="hidden" name="status" id="status" value="" />
            <div class="search-box">
                <div class="search-con">
                    <div class="search-img"></div>
                    <div class="search-text">
                        <input id="searchName" name="searchName" value="" class="search-detail" type="text" placeholder="请输入编号/客户名称" />
                    </div>
                </div>
                <button type="button" class="orderSearchB">搜索</button>
            </div>

        </form>
        <div class="list-box" id="dataList">

        </div>
        <p class="p_loading">正在加载...</p>
    </div>
    <div class="addModel" >
        <img src="${base}/resources/mobile/images/loading.gif" />
    </div>

<script src="${base}/resources/mobile/js/common.js"></script>
<script src="${base}/resources/mobile/js/iscroll.js"></script>
</body>
<script type="text/javascript">
    $(function(){
        var status = GetQueryString("status");
        $("#status").val(status);

    });
        var param={};
        $(".orderSearchB").on("click",function(){
            var status=$("#status").val();
            var pageSize = $("#pageSize").val();
            var searchName=$("#searchName").val();
            var pageNumber = $("#pageNumber").val();
            console.log(searchName);
            param={
                "status":status,
                "searchName":searchName,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            }
            $(".addModel").addClass('modelDisplay');
            listAjax(param);
        });


        function listAjax(param){
            $.get("asyncList.jhtml",param,function(o){
                console.log(o);
                $(".addModel").removeClass('modelDisplay');
                if (o.code=='0') {
                    var orders = o.data.orderList;
                    console.log(orders);
                    if(orders.length == 0){
                        $("#dataList").html('<p class="noInfoList">没有搜索到您要的订单</p>');
                    }else{
                        $("#dataList").html('');
                    }
                    var textHtml='';

                    $.each(orders,function(i,n){
                        var imgHtml = '';
                        if(n.type != "general" && n.type != "formal"){
                            imgHtml = '<span class="spanFx">分销</span>'
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
                    $("#totalPages").val(o.data.totalPages);

                    /*当前页+1*/
                    var pageNumber = $("#pageNumber").val();
                    pageNumber++;
                    $("#pageNumber").val(pageNumber);
                    /*请求成功后把变量置为false*/
                    boolAjax = false;
                    var pageSize = $("#pageSize").val();
                    if(orders.length == parseInt(pageSize)){
                        $(".p_loading").css("display","block");
                    }else{
                        $(".p_loading").css("display","none");
                    }

                }else {
                    errorInfoFun(o.msg);
                }
            });
        }

        var boolAjax = false;
        function appendAjax(param){
            $.get("asyncList.jhtml",param,function(o){

                if (o.code=='0') {
                    var orders = o.data.orderList;
                    var textHtml='';
                    $.each(orders,function(i,n){
                        textHtml+='<div class="order-con" onclick="view('+n.orderId+')">';
                        textHtml+='<div class="order-top">';
                        textHtml+='<div class="order-name">'+n.sn+'</div>';
                        textHtml+='<div class="order-status '+ n.statusString +'">'+getStatus(n.status)+'</div>';
                        textHtml+='</div>';
                        textHtml+='<div class="order-code">';
                        textHtml+='<div class="code-name">'+n.needName+'</div>';
                        textHtml+='</div>';
                        textHtml+='<div class="order-count">共<span class="count">'+n.quantity+'</span>件商品</div>';
                        textHtml+='</div>';
                    });
                    $("#dataList").append(textHtml);

                    /*当前页+1*/
                    var pageNumber = $("#pageNumber").val();
                    pageNumber++;
                    $("#pageNumber").val(pageNumber);

                    boolAjax = false;
                }else {
                    errorInfoFun(o.msg);
                }
            });
        }








        function GetQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null)
                return unescape(r[2]);
            return null;
        }



        $(window).scroll(function(){
            var top = $(".p_loading").offset().top;
            var divScroll = $(window).scrollTop();
            var divHeight = $(window).height();
            var pageNumber = $("#pageNumber").val();
            var totalPages = $("#totalPages").val();

            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                if(parseInt(pageNumber) <= parseInt(totalPages)){
                    param.pageNumber = pageNumber;
                    boolAjax=true;
                    appendAjax(param);
                }else{
                    $(".p_loading").html("");
                }

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

    function view(id){
        window.location.href="view.jhtml?id="+id;
    }
</script>
</html>
[/#escape]