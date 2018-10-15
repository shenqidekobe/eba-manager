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
    <link rel="stylesheet" href="${base}/resources/mobile/css/customer.css" />
    <title>客户</title>
    <style>
        .footer_nav .nav_Customer {
            background: url(${base}/resources/mobile/images/kehu-a.png) no-repeat center 0.1rem;
            background-size: 0.44rem;
            color:#4DA1FF;
        }
    </style>
</head>
<body>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="clientType" name="clientType" value="${clientType}" />
    <input type="hidden" name="totalPages" id="totalPages" value="${page.getTotalPages()}" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageable.pageNumber}" />
    <input type="hidden" name="pageSize" id="pageSize" value="${page.pageable.pageSize}" />

    <div class="tap-title">
        <div class="tab-top">
            <ul class="tab-ul customerClass">
                <li class="tab-item tab-active" data-customer="customerRelation">企业客户</li>
                <li class="tab-item" data-customer="need">个体客户</li>
            </ul>
        </div>
    </div>
    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入客户名称/业务员/收货人" />
            </div>
        </div>
    </div>
    <ul class="customer-list" id="customerList">
        [#if page.content.size() <= 0 ]
        <div class="no-data" >无数据</div>
        [/#if]
        [#list page.content as customerRelation]
        <li class="customer-li">
            <p class="name">${customerRelation.clientName}</p>
            <img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">
            <ul class="operate-list">
                [@shiro.hasPermission name = "admin:customerRelation:edit"]
                <li class="edit"><a title="${message("admin.common.edit")}" href="edit.jhtml?id=${customerRelation.id}" class="cz_a edit">编辑</a></li>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:customerRelation:delete"]
                <li class="delete"><a href="javascript:;" customerId="${customerRelation.id}" class="cz_a del">删除</a></li>
                [/@shiro.hasPermission]
            </ul>
        </li>
        [/#list]
    </ul>
    [#if page.content.size() > 8]
    <p class="p_loading">正在加载...</p>
    [/#if]
    <div class="addSpace"></div>
    [@shiro.hasPermission name = "admin:customerRelation:edit"]
    <a class="addButton" href="add.jhtml"></a>
    [/@shiro.hasPermission]
</form>

[#include "/admin/include/footerNav.ftl"]


<!--<input type="button" class="input_B" value="提交">-->
<!--弹出确认框-->
<div class="mutail">
    <div class="mutailContent">
        <div class="content_M">

        </div>
        <span class="cancel">取消</span>
        <span class="delSure">确定</span>
    </div>
</div>


<script src="${base}/resources/mobile/js/common.js"></script>

<script>
    $(function(){

        //跳转搜索页面
        $('#searchName').on('focus',function () {
            location.href = "searchMobile.jhtml"
        });

        var customerId = 0;
        $("#customerList").delegate(".del","click",function(){
            goodsId = $(this).attr("customerId");
            $(".mutail").css("display","block");
            $(".mutail .content_M").text('您确定要删除吗？')
        });
//        //编辑显示隐藏
//        $("#customerList").delegate('.customer-operate','click',function (e) {
//            var $item = $(this).next('.operate-list');
//            if($item.is(':visible')){
//                $item.hide();
//            }else{
//                $item.show().parents('.customer-li').siblings().find('.operate-list').hide();
//            }
//            e.stopPropagation();
//        });
//        //点击全局环境隐藏
//        $(document).on('click',function () {
//            $('.operate-list').hide();
//        });

        //取消按钮
        $(".mutail .cancel").on("click",function(){
            $(".mutail").css("display","none");
        });

        //确认按钮，，取消订单
        $(".mutail .delSure").on("click",function(){
            $(".mutail").css("display","none");

            var obj = {"ids":goodsId};
            delGoods(obj);
        });

        //删除企业客户
        function delGoods(obj){
            $.ajax({
                url: "delete.jhtml",
                type: "POST",
                data: obj,
                dataType: "json",
                cache: false,
                success: function(message) {
                    if (message.type == "success") {
                        setTimeout(function() {
                            location.reload(true);
                        }, 3000);

                    }
                }
            });
        }


        var boolAjax = false;
//加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            console.log(pageNumber+'----'+totalPages);
            if(parseInt(pageNumber) >= parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var next = ++pageNumber;
            var param={
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            $.get("asynclist.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data.list;
                    var textHtml='';
                    $.each(orders,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<p class="name">'+n.clientName+'</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';
                        [@shiro.hasPermission name = "admin:customerRelation:edit"]
                        textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+ n.id + '" class="cz_a edit">编辑</a></li>';
                        [/@shiro.hasPermission]
                        [@shiro.hasPermission name = "admin:customerRelation:delete"]
                        textHtml+='<li class="delete"><a href="javascript:;" customerId="'+ n.id +'" class="cz_a del">删除</a></li>';
                        [/@shiro.hasPermission]
                        textHtml+='</ul></li>';
                    });
                    $("#customerList").append(textHtml);

                    $("#pageNumber").val(next);
                    boolAjax = false;
                }else {
                    errorInfoFun(o.msg);
                }
            });
        }

        $(window).scroll(function(){
            var top = $(".p_loading").offset().top;
            var divScroll = $(window).scrollTop();
            var divHeight = $(window).height();
            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                boolAjax=true;
                loadData();
            }
        });



    });


</script>
</body>
</html>
[/#escape]