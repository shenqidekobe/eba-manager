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
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <title>商品信息</title>
    <style>
        html,body{background:#f5f5f5;}
        .orderAllInfo{
            margin-bottom:0.2rem;
        }
    </style>
</head>
<body>
    <div class='orderAllInfo'>
        <p class="titleName">商品信息</p>
        <ul class="order_info">
            [#list order.orderItems as orderItem]
            <li>
                [#if orderItem.product.goods.image??]
                    <img src="${orderItem.product.goods.image}" alt="">
                [#else]
                    <img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">
                [/#if]

                <div class="detail">
                    <p class="goodsName">${orderItem.name}</p>
                    <p class="goodsTip">
                        [#if orderItem.specifications?has_content]
                            <span>${orderItem.specifications?join(", ")}</span>
                        [/#if]
                    </p>
                    <p class="goodNum">商品数量 ${orderItem.quantity}件</p>
                </div>
                <div class="clearfix"></div>
            </li>
            [/#list]
        </ul>
        <p class="total">共<span>${order.quantity}</span>件商品</p>
    </div>
    <div class="orderAllInfo">
        <p class="titleName">商品记录</p>
        [#list order.orderItemLogs?reverse as orderitemLog]
        <div class="goodsRecode_li">
            <ul class="order_info">
                [#list orderitemLog.orderItemInfos as orderItemInfo]
                <li>
                    [#if orderItemInfo.product.goods.image??]
                        <img src="${orderItemInfo.product.goods.image}" alt="">
                    [#else]
                        <img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">
                    [/#if]
                    <div class="detail">
                        <p class="goodsName">${orderItemInfo.product.goods.name}</p>
                        <p class="goodsTip">
                            [#list orderItemInfo.product.specificationValues as specificationValue]
                                <span>${specificationValue.value}</span>
                            [/#list]
                        </p>
                        <p class="goodNum">商品数量 ${orderItemInfo.afterQuantity}件</p>
                    </div>
                    <div class="clearfix"></div>
                </li>
                [/#list]
            </ul>
            <div class="rocode_footer">
                <span classs="time">${orderitemLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                <span class="name">${orderitemLog.operatorName!"-"} ${message("admin.orderItemLog.operatorType."+orderitemLog.operatorType)}</span>
            </div>
        </div>
        [/#list]
    </div>

    <script>
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
