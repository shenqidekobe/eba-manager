[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/LCalendar.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/supply.css" />
    <title>客户信息</title>
    <style>
        .search-box .search-con{
            width:83%;
            float:left;
        }
        .search-box .search-text{width:calc(100% - 0.8rem)}
    </style>
</head>
<body >
<div class="choose-customer">
    <input type="hidden" name="totalPages" id="totalPages" value="${page.getTotalPages()}" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageable.pageNumber}" />
    <input type="hidden" name="pageSize" id="pageSize" value="${page.pageable.pageSize}" />
    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入客户名称" />
            </div>
        </div>
        <button id="search" type="button" class="orderSearchB">搜索</button>
    </div>
    <ul class="customer-list" id="customerList">
        [#list page.content as consultation]
            <li class="customer-li">
                <img class="chooseImg" data-id="${consultation.bySupplier.id}" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
                <p class="customer-name">${consultation.clientName}</p>
            </li>
        [/#list]
    </ul>
    <div class="confirm-box">
        <button class="confirm" id="yes">确定</button>
    </div>
    [#if page.content.size() > 8]
    <p class="p_loading">正在加载...</p>
    [/#if]
</div>

<script src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        var storage=window.localStorage;
        var bysupplierId=storage.getItem("bysupplierId");
        function initSelect(){
            $(".chooseImg").each(function(){
                if ($(this).data("id") == bysupplierId) {
                    $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                }
            });
        }
        initSelect();
        //选择企业用户
        $('#customerList').delegate('.chooseImg','click',function () {
            if(!$(this).hasClass('selected')){
                $(this).parent().parent().find("img").attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                storage.setItem("bysupplierId",$(this).data("id"));
            }else{
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                storage.removeItem("bysupplierId");
            }

        });

        //搜索
        var boolAjax = false;

        $('#search').on('click',function () {
            var searchName = $('#searchName').val();
            $("#customerList").html('');
            $(".p_loading").html("正在加载...");
            $("#pageNumber").val('1');
            boolAjax = false;

            $.get("asyncConsultationMobile.jhtml",{searchName:searchName},function(o){
                console.log(o.data.length);
                if (o.code=='0') {
                    var orders = o.data;
                    if(orders.length == 0){
                        $("#customerList").append('<div class="no-data" >无数据</div>');
                        $(".p_loading").hide();
                    }else if(orders.length <8){
                        $(".p_loading").show().html("");
                    }else{
                        $(".p_loading").show();
                    }
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<img class="chooseImg" data-id="'+n.bySupplierId+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<p class="customer-name" >'+n.clientName+'</p>';
                        textHtml+='</li>';
                    });
                    $("#customerList").append(textHtml);
                }else {
                    errorInfoFun(o.msg);
                }
            });
        });
        $("#yes").on("click",function(){
            window.location.href="add.jhtml";
        });


        //加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            console.log(pageNumber+'----'+totalPages);
            if(parseInt(pageNumber) >= parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var next = ++pageNumber;
            var param={
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            $.get("asyncConsultationMobile.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data;
                    if(orders.length < 10){
                        $(".p_loading").html("");
                    }
                    var textHtml='';
                    console.log(orders);
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<img class="chooseImg" data-id="'+n.id+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<p class="customer-name">'+n.clientName+'</p>';
                        textHtml+='</li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#pageNumber").val(next);
                    initSelect();
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
            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                boolAjax=true;
                loadData();
            }
        });
    });

    $(function(){
        pushHistory();
        window.addEventListener("popstate", function(e) {
            window.location.href = 'add.jhtml';
        }, false);
        function pushHistory() {
            var state = {
                title: "title",
                url: "#"
            };
            window.history.pushState(state, "title", "#");
        }
    });

</script>
</body>
</html>
[/#escape]
