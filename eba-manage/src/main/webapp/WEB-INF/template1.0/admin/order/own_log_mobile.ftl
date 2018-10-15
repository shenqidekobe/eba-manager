[#escape x as x?html]
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <title>订单日志</title>
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        html,body{background:#ffffff;}
    </style>
</head>
<body>

<ul class="orderDate">
    [#list orderLogs as orderLog]
    <li>
        <p class="content">${message("OrderLog.Type." + orderLog.type)}</p>
        <p class="supplyName">${orderLog.operator!"-"}</p>
        <p class="time">${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
    </li>
    [/#list]
</ul>

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