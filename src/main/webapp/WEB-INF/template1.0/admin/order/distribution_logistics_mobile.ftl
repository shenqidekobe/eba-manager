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
    <title>物流信息</title>
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        .noInfoList{display:block;}
    </style>

</head>
<body>

<div class="send-in">
    [#list shippings as shipping]
    <div class="info-send">
        <div class="take-info">
            物流信息
            <button type="button" class="ta-st [@shiro.lacksPermission name = 'admin:order:cancelShipped'] hhhh [/@shiro.lacksPermission]"  onclick="javascript:cancelShipping(${order.id} , ${shipping.id});"[#if shipping.status == "senderChecked"] style="display: none" disabled="disabled"[/#if]>作废</button>

        </div>
        <div class="take-na">
            <div class="take-name">发货时间</div>
            <div class="take-content name">${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}</div>
        </div>
        <div class="take-comp">
            <div class="take-name">物流公司</div>
            <div class="take-content comp">${shipping.deliveryCorp!"-"}</div>
        </div>
        <div class="take-code">
            <div class="take-name">物流单号</div>
            <div class="take-content code">${shipping.trackingNo!"无"}</div>
        </div>
        <div class="take-status">
            <div class="take-name">收货状态</div>
            <div class="take-content status">
                [#if shipping.status == 'senderChecked']
                已收货
                [#else]
                未收货
                [/#if]
            </div>
        </div>
        <div class="goods-ifo">
            <div class="goods-title">商品信息</div>
            <div class="info-con">
                [#list shipping.shippingItems as shippingItem]
                <div class="goodInfo_li">
                    <div class="info-det">
                        <div class="good-name">
                            <div class="name">${shippingItem.name}</div>
                            [#if shippingItem.specifications?has_content]
                            <div class="count">${shippingItem.specifications?join(", ")}</div>
                            [/#if]
                        </div>
                    </div>
                    <div class="goods-cnt">
                        <div class="sent-le">
                            <div class="sent-na">发货</div>
                            <span class="get-num">${shippingItem.quantity}</span>
                        </div>
                        <div class="get-rg">
                            <div class="get-na">收货</div>
                            <span class="get-num">${shippingItem.trueRealQuantity}</span>
                        </div>
                    </div>
                </div>
                [/#list]
            </div>

        </div>
    </div>
    [/#list]
</div>

[#if !shippings]
<p class="noInfoList">还没有物流信息！</p>
[/#if]



<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        $('.goods-ifo .goods-title').toggle(function () {
            $(this).addClass('goodToggle');
            $(this).parents('.goods-ifo').find('.info-con').slideDown('slow');
        },function () {
            $(this).removeClass('goodToggle');
            $(this).parents('.goods-ifo').find('.info-con').slideUp('slow');
        });

        function cancelShipping(){
            alert(1234);
        }

        $('.hhhh').on('click',function(){
            alert(1231231233123);
        })





    });

    $(function(){
        pushHistory();
        var searchName = location.search.slice(1);
        window.addEventListener("popstate", function(e) {
            window.location.href = 'view.jhtml?'+searchName;
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
