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

    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>

    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/setting.css" />
    <title>设置</title>
    <style>
        body{background:#f5f5f5;}
        .footer_nav .nav_setting {
            background: url(${base}/resources/mobile/images/shezhi-a.png) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
    </style>
</head>
<body>

    <div class="settingPage">
        <div class="user">
            <div class="userImg">
                <img src="${base}/resources/mobile/images/zhanghao-b.png" />
            </div>
            <dvi class="userInfo">
                <p class="name">${admin.username}</p>
                <p class="phone">${admin.bindPhoneNum}</p>
            </dvi>
        </div>
        <ul class="info_ul">
            <li class="li_info userSet">
                <span class="label">账号信息</span>
            </li>
            <li class="li_info qyInfo">
                <span class="label">企业信息</span>
            </li>
        </ul>

    </div>

    <div class="signOut">
        <a href="../logout.jhtml" target="_top">退出当前账号</a>
    </div>
    [#include "/admin/include/footerNav.ftl"]


    <script src="${base}/resources/mobile/js/common.js"></script>

    <script>

        $(function() {
            $(".userSet").on("click",function(){
                window.location = '../profile/edit.jhtml';
            });


        })
    </script>
</body>
</html>
[/#escape]