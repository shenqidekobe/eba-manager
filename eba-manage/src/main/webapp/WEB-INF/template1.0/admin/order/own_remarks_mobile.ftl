[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <title>备注信息</title>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <style>
        html,body{background:#fff;}
        .noInfoList{display:block;}
    </style>
</head>

<body>

    <ul class="remarkInfo">
        [#list orderRemarks as orderRemark]
        [#if type != 'formal']
            [#if orderRemark.msgType=="btob"]
            <li>
                <p class="content">${orderRemark.description}</p>
                <p class="supplyName">${orderRemark.name}</p>
                <p class="time">${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
                <div class="images">
                    [#list orderRemark.annex as annex]
                    <img src="${annex.url}" alt="">
                    [/#list]
                </div>
            </li>
            [/#if]
        [#else]
        <li>
            <p class="content">${orderRemark.description}</p>
            <p class="supplyName">${orderRemark.name}</p>
            <p class="time">${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
            <div class="images">
                [#list orderRemark.annex as annex]
                <img src="${annex.url}" alt="">
                [/#list]
            </div>
        </li>
        [/#if]
        [/#list]

    </ul>
    [#if !orderRemarks]
        <p class="noInfoList">还没有备注信息！</p>
    [/#if]
    <div class="footerDivE"></div>
    [@shiro.hasPermission name = "admin:order:addRemarks"]
    <button type="button" class="submit_to">添加备注</button>
    [/@shiro.hasPermission]

    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
    <script>


        $(".submit_to").on("click",function(){

            var id = GetQueryString("id");
            window.location = "addOwnRemarksMobile.jhtml?id="+id;

        })

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