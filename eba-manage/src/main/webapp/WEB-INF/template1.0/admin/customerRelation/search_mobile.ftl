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
    <link rel="stylesheet" href="${base}/resources/mobile/css/customer.css" />
    <title>微商平台</title>
    <style>
        .search-box .search-con{
            width:83%;
            float:left;
        }
        .search-box .search-text{width:calc(100% - 0.8rem)}
        .p_loading{display:none;}
    </style>
</head>
<body>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="clientType" name="clientType" value="" />
    <input type="hidden" name="totalPages" id="totalPages" value="" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="1" />
    <input type="hidden" name="pageSize" id="pageSize" value="8" />
    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="" class="search-detail" type="text" placeholder="请输入客户名称/业务员/收货人" />
            </div>
        </div>
        <button type="button" class="orderSearchB">搜索</button>
    </div>
    <ul class="customer-list" id="customerList">

    </ul>
    <p class="p_loading">正在加载...</p>
</form>

<!--弹出确认框-->
<div class="mutail">
    <div class="mutailContent">
        <div class="content_M">

        </div>
        <span class="cancel">取消</span>
        <span class="delSure">确定</span>
    </div>
</div>


<script src="${base}/resources/mobile/js/common.js"></script>

<script>
    $(function(){

        var customerId = 0;
        $("#customerList").delegate(".del","click",function(){
            goodsId = $(this).attr("customerId");
            $(".mutail").css("display","block");
            $(".mutail .content_M").text('您确定要删除吗？')
        });
//        //编辑显示隐藏
//        $("#customerList").delegate('.customer-operate','click',function (e) {
//            var $item = $(this).next('.operate-list');
//            if($item.is(':visible')){
//                $item.hide();
//            }else{
//                $item.show().parents('.customer-li').siblings().find('.operate-list').hide();
//            }
//            e.stopPropagation();
//        });
//        //点击全局环境隐藏
//        $(document).on('click',function () {
//            $('.operate-list').hide();
//        });

        //取消按钮
        $(".mutail .cancel").on("click",function(){
            $(".mutail").css("display","none");
        });

        //确认按钮，，取消订单
        $(".mutail .delSure").on("click",function(){
//            var orderId = $(this).attr("orderId");
            $(".mutail").css("display","none");

            var obj = {"ids":goodsId};
            delGoods(obj);
        });



        function delGoods(obj){
            $.ajax({
                url: "delete.jhtml",
                type: "POST",
                data: obj,
                dataType: "json",
                cache: false,
                success: function(message) {
                    if (message.type == "success") {
                        setTimeout(function() {
                            location.reload(true);
                        }, 3000);

                    }
                }
            });
        }

        //点击查询
        $(".orderSearchB").on("click",function(){
            var clientType=$("#clientType").val();
            var pageSize = $("#pageSize").val();
            var searchName=$("#searchName").val();
            var pageNumber = $("#pageNumber").val();
            console.log(searchName);
            param={
                "clientType":clientType,
                "searchName":searchName,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            loadData(param);
        });


//加载数据
        function loadData(param){
            console.log(pageNumber+'----'+totalPages);

            $.get("asynclist.jhtml",param,function(o){
                console.log(o);
                if (o.code=='0') {
                    var orders = o.data.list;
                    if(orders.length == 0){
                        $("#customerList").html('<li class="noInfoList">没有搜索到您要的订单</li>');
                    }else{
                        $("#customerList").html('');
                    }
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<p class="name">'+n.clientName+'</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';
                        [@shiro.hasPermission name = "admin:customerRelation:edit"]
                        textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+ n.id + '" class="cz_a edit">编辑</a></li>';
                        [/@shiro.hasPermission]
                        [@shiro.hasPermission name = "admin:customerRelation:delete"]
                        textHtml+='<li class="delete"><a href="javascript:;" customerId="'+ n.id +'" class="cz_a del">删除</a></li>';
                        [/@shiro.hasPermission]
                        textHtml+='</ul></li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#totalPages").val(o.data.totalPages);

                    /*当前页+1*/
                    var pageNumber = $("#pageNumber").val();
                    pageNumber++;
                    $("#pageNumber").val(pageNumber);
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

            $.get("asynclist.jhtml",param,function(o){
                console.log(o);
                if (o.code=='0') {
                    var orders = o.data.list;
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<p class="name">'+n.clientName+'</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';
                        [@shiro.hasPermission name = "admin:customerRelation:edit"]
                        textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+ n.id + '" class="cz_a edit">编辑</a></li>';
                        [/@shiro.hasPermission]
                        [@shiro.hasPermission name = "admin:customerRelation:delete"]
                        textHtml+='<li class="delete"><a href="javascript:;" customerId="'+ n.id +'" class="cz_a del">删除</a></li>';
                        [/@shiro.hasPermission]
                        textHtml+='</ul></li>';
                    });
                    $("#customerList").append(textHtml);

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



    });


</script>
</body>
</html>
[/#escape]