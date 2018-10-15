[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>

    <style>
        body {background: #f9f9f9;}
        .pag_div {width: 95%;float: left;padding-left:0;}
        .tabBar {background: #f9f9f9;border: 0;}
        .tabBar span {padding:0;background: #f9f9f9;height:auto;}
        .tabBar span.current a{background: #fff;color: #333;}
        .tabBar span>a{
            display: block;
            padding: 5px 24px;
            color: #999;
        }
        .table_box{margin-top:20px;}
        table th {border-top: 1px solid #f0f0f0;}
        .tabCon,#tab-system{overflow: auto;}
        .dialogBottom{display:none;}

    </style>
    <title>查看订单</title>
</head>
<body>

<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li><a href="list.jhtml">分享统计</a></li>
            <li>分享详情</li>
        </ul>
    </div>
    <div class="form_box">
        <form class="form form-horizontal" action="userRecord.jhtml" method="get" id="listForm">
            <input type="hidden" name="id" value="${id}" id="id">
            <input type="hidden" name="status" id="status" value="${order.status}"/>
            <div id="tab-system" class="HuiTab">
                <div class="tabBar cl">
                    <span><a href="goodDetails.jhtml?id=${id}">基本信息</a></span>
                    <span class="tabTwo"><a href="">访问记录</a></span>

                </div>

                <div class="tabCon"></div>
                <div class="tabCon">
                        <div class="table_box">
                            <table class="table table-border table-hover table_width boo">
                                <thead>
                                <tr class="text-l">
                                    <th width="">访问人</th>
                                    <th width="">页面访问次数</th>
                                    <th width="">商品浏览次数</th>
                                    <th width="">是否同步</th>
                                    <th width="">最近访问日期</th>
                                </tr>
                                </thead>
                                <tbody>
                                [#list page.content as shareUserPage]
                                <tr class="text-l">
                                    <td class="tdHeadImg">
                                        <img src="${shareUserPage.headImgUrl}" alt="">
                                        ${shareUserPage.name}
                                    </td>
                                    <td>${shareUserPage.pageVisit}</td>
                                    <td>
                                        <span class="tdGoodNum" data-memberid=${shareUserPage.id}>${shareUserPage.goodsVisit}</span>
                                    </td>
                                    <td>
                                        [#if shareUserPage.isSynchronize()]
                                            是
                                        [#else]
                                            否
                                        [/#if]
                                    </td>
                                    <td>${(shareUserPage.date?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                                </tr>
                                [/#list]
                                </tbody>
                            </table>
                        </div>
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                        [#include "/admin/include/pagination.ftl"]
                        [/@pagination]
                </div>

            </div>


            <div class="footer_submit">
                <input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;">
            </div>
        </form>
    </div>
</div>
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
<script type="text/javascript">
    $(function () {

        /*通过js获取页面高度，来定义表单的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);

        $(".tabCon").css("height",formHeight - 40);

        //tab按钮切换
        $.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "1");

        //商品浏览次数
        $('.tdGoodNum').click(function() {
            var memberId=$(this).data("memberid");
            var id=$("#id").val();
            $.get("getShareUserGoods.jhtml",{"memberId":memberId,"id":id},function(json){
                var data=json.data;
                var html='<div class="goodsAisCount"><div class="li"><div class="name">商品名称</div><div class="num">浏览次数</div></div>';
                for (var i = 0; i < data.length; i++) {
                    html+=' <div class="li"><div class="name">'+data[i].goodsName+'</div><div class="num">'+data[i].goodsVisit+'</div></div>'
                }
                html+='</div>';

                $.dialog({
                    title: "商品访问统计",
                    height:250,
                    content: html,
                    onOk: function() {

                    },
                    onShow:function(){
                        $(".xxDialog").css("top","150px");
                    }
                });
            });

        });


    });

    $("body").click(function(){
        window.top.$(".show_news").removeClass("show");
    })
</script>

</body>
</html>
[/#escape]