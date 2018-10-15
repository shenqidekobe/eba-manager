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
    <title>个体供应详情</title>
</head>
<body >
<div class="choose-customer">
    <input type="hidden" id="supplierId" name="clientType" value="${supplyNeed.id}" />
    <input type="hidden" name="totalPages" id="totalPages" value="" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="1" />
    <input type="hidden" name="pageSize" id="pageSize" value="" />
    <div class="info-top">
        <div class="info-time li_bot">
            <div class="start-name">开始时间</div>
            <div class="time">${supplyNeed.startDate}</div>
        </div>
        <div class="info-time li_bot">
            <div class="end-name">结束时间</div>
            <div class="time">${supplyNeed.endDate}</div>
        </div>
        <div class="info-code li_bot">
            <div class="code-name">个体客户</div>
            <div class="code">${supplyNeed.need.name}</div>
        </div>
        <div class="info-code li_bot">
            <div class="code-name">供应模式</div>
            [#if supplyNeed.assignedModel == "STRAIGHT"]
                <div class="code">直销模式</div>
            [/#if]
            [#if supplyNeed.assignedModel == "BRANCH"]
                <div class="code">分销模式</div>
            [/#if]
        </div>
        <div class="info-code li_bot">
            <div class="code-name">消息提醒</div>
            [#if supplyNeed.openNotice]
            <div class="code">已开启</div>
            [#else]
            <div class="code">未开启</div>
            [/#if]
        </div>
    </div>
    <div class="goods">
        <div class="title">商品信息</div>
        <ul class="goods-list" id="customerList">

        </ul>
    </div>
    <p class="p_loading">正在加载...</p>
</div>

<script src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        //选择企业用户
        $('.chooseGood').live('click',function () {
            if(!$(this).hasClass('selected')){
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected')
                    .parents('.good-li').siblings().find('img.chooseGood')
                    .attr({'src':'${base}/resources/mobile/images/xuanze-a.png'});
            }else{
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
            }

        });

        //搜索
        $('#search').on('click',function () {
            var searchName = $('#searchName').val();
            console.log(searchName);
            $.get("asyncConsultationMobile.jhtml",{searchName:searchName},function(o){
                if (o.code=='0') {
                    var orders = o.data;
                    var textHtml='';
                    console.log(orders);
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<div class="good-item">';
                        if(n.image != ''){
                            textHtml+='<img src="'+n.image+'" alt="">';
                        }else{
                            textHtml+='<img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">';
                        }
                        textHtml+='<div class="detail">';
                        textHtml+='<p class="goods-name">'+n.name+'</p>';
                        textHtml+='<p class="goodsTip">';
                        $.each(n.specificationValues,function (j,m) {
                            textHtml+='<span>['+m.value+']</span>';
                        });
                        textHtml+='</p></div></div>';
                        textHtml+='<div class="order-info">';
                        textHtml+='<div class="order-count">';
                        textHtml+='<span class="name">起订量</span>';
                        textHtml+='<input type="number" class="count" value="'+n.minOrderQuantity+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='<div class="order-price">';
                        textHtml+='<span class="name">供货价(￥)</span>';
                        textHtml+='<input type="number" class="price" value="'+n.supplyPrice+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='</div></li>';
                    });
                    $("#customerList").append(textHtml);
                }else {
                    errorInfoFun(o.msg);
                }
            });
        });


        var boolAjax = false;
        //加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            var supplierId = $("#supplierId").val();
            console.log(pageNumber+'----'+totalPages);
            if(parseInt(pageNumber) > parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var param={
                "pageNumber":pageNumber,
                "supplyNeedId":supplierId,
                "pageSize":20
            };
            var next = ++pageNumber;
            $.get("asyncViewGoods.jhtml",param,function(o){
                if (o.code=='0') {
                    console.log(o);
                    var orders = o.data.list;
                    if(orders.length<4){
                        $(".p_loading").html("");
                    }
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<div class="good-item">';
                        if(n.image != ''){
                            textHtml+='<img src="'+n.image+'" alt="">';
                        }else{
                            textHtml+='<img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">';
                        }
                        textHtml+='<div class="detail">';
                        textHtml+='<p class="goods-name">'+n.name+'</p>';
                        textHtml+='<p class="goodsTip">';
                        $.each(n.specificationValues,function (j,m) {
                            textHtml+='<span>'+m.value+'</span>';
                        });
                        textHtml+='</p></div></div>';
                        textHtml+='<div class="order-info">';
                        textHtml+='<div class="order-count">';
                        textHtml+='<span class="name">起订量</span>';
                        textHtml+='<input type="number" class="count" value="'+n.minOrderQuantity+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='<div class="order-price">';
                        textHtml+='<span class="name">供货价(￥)</span>';
                        textHtml+='<input type="number" class="price" value="'+n.supplyPrice+'" unselectable="on" onfocus="this.blur()" readonly />';
                        textHtml+='</div>';
                        textHtml+='</div></li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#pageSize").val(o.data.pageSize);
                    $("#totalPages").val(o.data.totalPages);
                    $("#pageNumber").val(next);
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
        loadData();
    });


</script>
</body>
</html>
[/#escape]
