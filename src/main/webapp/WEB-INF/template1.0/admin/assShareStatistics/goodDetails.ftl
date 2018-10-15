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
        .tabCon,#tab-system{overflow: auto;padding -bottom:20px;}
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
        <form class="form form-horizontal" action="goodDetails.jhtml" method="get" id="listForm">
            <input type="hidden" name="id" value="${id}">
            <div id="tab-system" class="HuiTab">
                <div class="tabBar cl">
                    <span><a href="">基本信息</a></span>
                    <span class="tabTwo"><a href="userRecord.jhtml?id=${id}">访问记录</a></span>

                </div>

                <div class="tabCon">
                    <div class="pag_div">
                        <div class="row cl" style="float:left;">
                            <label class="form-label col-xs-4 col-sm-3">供应商</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span" style="color:#333">${assCustomerRelation.clientName}</span>
                            </div>
                        </div>
                        <div class="row cl" style="float:left;">
                            <label class="form-label col-xs-4 col-sm-3">
                                主题
                            </label>
                            <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span" style="color:#333">
                                        ${assCustomerRelation.theme}
                                    </span>
                            </div>
                        </div>
                        <div class="clear"></div>
                    </div>
                        <div class="table_box">
                            <table class="table table-border table-hover table_width boo">
                                <thead>
                                <tr class="text-l">
                                    <th width=""></th>
                                    <th width="">图片</th>
                                    <th width="">商品名称</th>
                                    <th width="">规格</th>
                                    <th width="">单位</th>
                                </tr>
                                </thead>
                                <tbody>
                                [#list page.content as goods]
                                <tr class="text-l">
                                    <td>${goods_index+1+(page.pageNumber-1)*page.pageSize}</td>
                                    <td class="tdGoodImg">
                                        [#list goods.image as images]
                                            [#if images_index == 0]
                                                <img src="${images}" alt="">
                                            [/#if]
                                        [/#list]
                                        [#if goods.image.size() == 0]
                                            <img style="border:1px solid #f0f0f0;" src="${base}/resources/admin1.0/images/mr_icon.png" alt="" />
                                        [/#if]
                                    </td>
                                    <td>${goods.name}</td>
                                    <td>
                                        [#list goods.assProducts as product]
                                        [#if product_index < 3]
                                        <p>${product.specification}</p>
                                        [/#if]
                                        [/#list]
                                        [#if goods.assProducts.size() gt 3]
                                        <p>………</p>
                                        [/#if]
                                    </td>
                                    <td>
                                        [#if goods.unit??]
                                        [#assign unit=goods.unit]
                                        ${message("AssGoods.Unit."+unit)}
                                        [/#if]
                                    </td>
                                </tr>
                                [/#list]
                                </tbody>
                            </table>
                        </div>
                        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                            [#include "/admin/include/pagination.ftl"]
                        [/@pagination]
                </div>

                <div class="tabCon"></div>

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
        $.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");



    });

    $("body").click(function(){
        window.top.$(".show_news").removeClass("show");
    })
</script>

</body>
</html>
[/#escape]