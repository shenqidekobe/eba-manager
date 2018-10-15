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
    <title>供应确认详情</title>
</head>
<body >
<div class="choose-customer">
    <input type="hidden" id="supplierId" name="clientType" value="${supplierSupplier.id}" />
    <input type="hidden" name="totalPages" id="totalPages" value="" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="1" />
    <input type="hidden" name="pageSize" id="pageSize" value="" />
    <div class="info-top">
        <!--<div class="info-status">供应确认</div>-->
        <div class="info-time li_bot">
            <div class="start-name">开始时间</div>
            <div class="time">${supplierSupplier.startDate}</div>
        </div>
        <div class="info-time li_bot">
            <div class="end-name">结束时间</div>
            <div class="time">${supplierSupplier.endDate}</div>
        </div>
        <div class="info-code li_bot">
            <div class="code-name">供应商</div>
            <div class="code">${supplierSupplier.supplier.name}</div>
        </div>
    </div>
    <div class="goods">
        <div class="title">商品信息</div>
        <ul class="goods-list" id="customerList">

        </ul>
    </div>
    [#if type == 'confirm']
    <div class="confirm-box">
        <button class="refuse" data-type="rejected" supplierSupplierId="${supplierSupplier.id}">拒绝</button>
        <button class="confirm" data-type="inTheSupply" supplierSupplierId="${supplierSupplier.id}">确定</button>
    </div>
    [/#if]
    <p class="p_loading">正在加载...</p>
</div>
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
<script type="text/javascript">
    $(function () {
        var supplierSupplierId ='';
        var supplierType ='';
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
                "supplierSupplierId":supplierId,
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

        $(".refuse,.confirm").on("click",function(){
            supplierSupplierId = $(this).attr("supplierSupplierId");
            supplierType = $(this).attr('data-type');
            $(".mutail").css("display","block");
            if("rejected" == supplierType){
                $(".mutail .content_M").text('您决定拒绝这条供应信息？');
            }else{
                $(".mutail .content_M").text('您确认此条供应信息？');
            }


        });

        //取消按钮
        $(".mutail .cancel").on("click",function(){
            $(".mutail").css("display","none");
        });

        //确认按钮，，取消订单
        $(".mutail .delSure").on("click",function(){
            $(".mutail").css("display","none");
            updateStatus(supplierType,supplierSupplierId);
        });

        //确认或者拒绝
        function updateStatus(type , id){
            $.ajax({
                url: "updateStatus.jhtml",
                type: "GET",
                data: {status:type , id:id},
                dataType: "json",
                cache: false,
                success: function(message) {
                    $.message(message);
                    if (message.type == "success") {
                        setTimeout(function () {
                            window.location.href="list.jhtml";
                        }, 2000);
                    }
                }
            });
        }
    });


</script>
</body>
</html>
[/#escape]
