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
        .footer_nav .nav_index {
            background: url(${base}/resources/mobile/images/shouye-2.svg) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
        .tab-top{padding:0.2rem 0;}
        .tab-top .tab-ul{
            background: #eee;
        }
        .noAuthority{
            text-align: center;
            line-height:8rem;
            font-size:0.28rem;
        }
    </style>
</head>
<body>
<div class="tab-top">
    <ul class="tab-ul reportClass">
        <li class="tab-item  tab-active" data-report="orderReport">订单报表</li>
        <li class="tab-item" data-report="commodityReport">商品报表</li>
        <li class="tab-item" data-report="customerReport">客户报表</li>
    </ul>
</div>

    <p class="noAuthority">无此页面操作权限</p>

    [#include "/admin/include/footerNav.ftl"]


    <script src="${base}/resources/mobile/js/common.js"></script>
    <script src="${base}/resources/mobile/js/iscroll.js"></script>
    <script type="text/javascript">

    </script>
</body>

</html>
[/#escape]