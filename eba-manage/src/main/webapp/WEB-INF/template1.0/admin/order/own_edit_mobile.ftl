[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信小程序</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE">

    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>

    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/LCalendar.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        body{background:#fbfbfb;}
    </style>
</head>
<body>
<!--这是订单收货确认修改 -->
<div class="order-chg">
    <form id="updateItemForm" class="form form-horizontal" action="updateItems.jhtml" method="post">
        <input type="hidden" name="token" value="${token}" />
        <input type="hidden" name="id" value="${order.id}" />
        <div class="ta-time">
            <div class="take-goods">
                <span class="title">收货时间</span>
                <input type="text" unselectable="on" onfocus="this.blur()" readonly="readonly" id="reDate" name="reDate" class="reDate" value="${order.reDate}" placeholder="请选择时间" />
            </div>
        </div>
        <div class="or-info">
            <div class="title">商品信息</div>
            [#list order.orderItems as orderItem]
            <div class="good-it">
                <input type="hidden" id="productId" value="${orderItem.product.id}" />
                <input type="hidden" name="orderItems[${orderItem_index}].id" value="${orderItem.id}" />
                <input type="hidden" id="status" name="orderItems[${orderItem_index}].status" value="edit" />
                <div class="good-name">
                    <div class="name">
                        ${abbreviate(orderItem.name, 50, "...")}
                        [#if orderItem.specifications?has_content]
                        [/#if]
                    </div>
                    [#if orderItem.specifications?has_content]
                    <div class="count">${orderItem.specifications?join(", ")}</div>
                    [/#if]

                </div>
                <div class="order-count">
                    <div class="count-name">订货数</div>
                    <div class="count-ipm">
                        <input class="count-txt" type="number" min="0" name="orderItems[${orderItem_index}].checkQuantity" placeholder="请输入订货数量" value="${orderItem.quantity}" />
                    </div>
                </div>
            </div>
            [/#list]
        </div>
        <div class="confirm" type="button" id="submits" >确认</div>
    </form>
</div>


<script src="${base}/resources/mobile/js/LCalendar.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    //时间插件
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#reDate', //标签id
        'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
        'minDate': new Date().getFullYear() + '-' + (new Date().getMonth() + 1) + '-' + new Date().getDate(), //最小日期
        'maxDate': '2100-01-01' //最大日期
    });

    //表单提交
    $('#submits').on('click',function () {
        $("#updateItemForm").submit();
    });


</script>
</body>
</html>
[/#escape]