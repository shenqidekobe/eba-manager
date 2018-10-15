<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <title>订单附件</title>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        html,body{background:#fff;}
        .noInfoList{display:block;}
        .fileImg img{
            display: inline-block;
            width:1.4rem;
            height:1.4rem;
        }
        .mutailImg .img{
            width: 100%;
            height:5rem;
            background-color: #fff;
            margin-top: 3rem;
        }
        .mutailImg .img img{
            display: inline-block;
            width: 100%;
            height:5rem;
        }
    </style>
</head>
<body>
    [#if !orderFiles]
        <p class="noInfoList">还没有附件信息！</p>
    [#else]
        <div class="attachment">
            <p class="name">附件</p>
            <ul>
                [#list orderFiles as orderFiles]

                    <li>
                        <div class="fileImg">
                            [#if orderFiles.source.indexOf(".pdf") != -1]
                            <img src="${base}/resources/mobile/images/PDF_icon.png" alt="" />
                            [#else]
                            <img src="${orderFiles.source}" alt="" />
                            [/#if]
                        </div>
                        <div class="right">
                            <p class="fileName">${orderFiles.title}</p>
                            <span>${(orderFiles.size/1024/1024)?string("0.##")}M</span>
                        </div>
                    </li>

                [/#list]
            </ul>
        </div>
    [/#if]
    <div class="mutailImg">
        <div class="title">
            <p></p>
            <span></span>
        </div>

        <div class="img">
            <img src="" alt="" />
        </div>
    </div>


    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script>
        $('.fileImg img').on('click',function () {
            $('.mutailImg').show()
                .find('.img img').attr({src:$(this).attr('src')});
        });
        $('.mutailImg .title span').on('click',function () {
            $('.mutailImg').hide();
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


