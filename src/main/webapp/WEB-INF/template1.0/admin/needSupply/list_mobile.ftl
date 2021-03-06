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
    <!--<link rel="stylesheet" href="${base}/resources/mobile/css/customer.css" />-->
    <link rel="stylesheet" href="${base}/resources/mobile/css/supply.css" />
    <title>供应</title>
    <style>
        .footer_nav .nav_supply {
            background: url(${base}/resources/mobile/images/gongying-a.png) no-repeat center 0.1rem;
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
            <ul class="tab-ul supplyClass">
                <li class="tab-item" data-supply="formalSupply">企业供应</li>
                <li class="tab-item tab-active" data-supply="needSupply">个体供应</li>
                <li class="tab-item" data-supply="supplyDistribution">供应确认</li>
            </ul>
        </div>
    </div>
    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入客户名称" />
            </div>
        </div>
    </div>
    <ul class="customer-list" id="customerList">
        [#if page.content.size() <= 0 ]
        <div class="no-data" >无数据</div>
        [/#if]
        [#list page.content as supplyNeed]
        <li class="customer-li">
            <span class="supplyType ${supplyNeed.status}">${message("SupplyNeed.Status."+supplyNeed.status)}</span>

            <p class="name">
                ${supplyNeed.need.name}
                [#if supplyNeed.assignedModel == 'BRANCH']
                <span class="distribut_label">分销</span>
                [/#if]
            </p>
            <img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">
            <ul class="operate-list">
                [#if supplyNeed.status == "SUPPLY"]
                [@shiro.hasPermission name = "admin:needSupply:view"]
                <li class="look"><a title="查看" href="view.jhtml?id=${supplyNeed.id}" class="ml-5" style="text-decoration:none">查看</a></li>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:needSupply:edit"]
                <li class="edit"><a title="编辑" href="edit.jhtml?id=${supplyNeed.id}" class="ml-5" style="text-decoration:none">编辑</a></li>
                [/@shiro.hasPermission]
                [#elseif supplyNeed.status == "EXPIRED"]
                [@shiro.hasPermission name = "admin:needSupply:view"]
                <li class="look"><a title="查看" href="view.jhtml?id=${supplyNeed.id}"  class="ml-5" style="text-decoration:none">查看</a></li>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:needSupply:edit"]
                <li class="edit"><a title="编辑" href="edit.jhtml?id=${supplyNeed.id}" class="ml-5" style="text-decoration:none">编辑</a></li>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:needSupply:delete"]
                <li class="delete"><a title="删除" href="javascript:void(0);" supplyNeedId="${supplyNeed.id}"  class="ml-5 del" style="text-decoration:none">删除</a></li>
                [/@shiro.hasPermission]
                [#elseif supplyNeed.status == "WILLSUPPLY"]
                [@shiro.hasPermission name = "admin:needSupply:view"]
                <li class="look"><a title="查看" href="view.jhtml?id=${supplyNeed.id}"  class="ml-5" style="text-decoration:none">查看</a></li>
                [/@shiro.hasPermission]
                [@shiro.hasPermission name = "admin:needSupply:edit"]
                <li class="edit"><a title="编辑" href="edit.jhtml?id=${supplyNeed.id}" class="ml-5" style="text-decoration:none">编辑</a></li>
                [/@shiro.hasPermission]
                [/#if]
            </ul>
        </li>
        [/#list]
    </ul>
    [#if page.content.size() > 8]
    <p class="p_loading">正在加载...</p>
    [/#if]
    <div class="addSpace"></div>

    [@shiro.hasPermission name = "admin:formalSupply:add"]
    <a class="addButton" href="javascript:void(0);" id="add"></a>
    [/@shiro.hasPermission]
</form>

[#include "/admin/include/footerNav.ftl"]
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
        var supplyNeedId ='';
        //跳转搜索页面
        $('#searchName').on('focus',function () {
            location.href = "searchMobile.jhtml"
        });

        $("#customerList").delegate(".del","click",function(){
            supplyNeedId = $(this).attr("supplyNeedId");
            $(".mutail").css("display","block");
            $(".mutail .content_M").text('确定要删除该供应？')
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

            var obj = {"id":supplyNeedId};
            deleteFormal(obj);
        });

        //删除
        function deleteFormal(obj){
            $.ajax({
                url: "delete.jhtml",
                type: "POST",
                data: obj,
                dataType: "json",
                cache: false,
                success: function(message) {
//                    $.message(message);
                    if (message.type == "success") {
                        location.reload();
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
            $.get("asyncList.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data.list;
                    var textHtml='';
                    console.log(orders);
                    $.each(orders,function(i,n){
                        var name = '';

                        textHtml+='<li class="customer-li">';
                        textHtml+='<span class="supplyType ' +n.status+ '">'+getStatus(n.status)+'</span>';
                        textHtml+='<p class="name">';
                        textHtml+= n.name;
                        if(n.assignedModel == 'BRANCH'){
                            textHtml+='<span class="distribut_label">分销</span>';
                        }
                        textHtml+='</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';

                        if(n.status == 'SUPPLY'){
                            [@shiro.hasPermission name = "admin:needSupply:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:needSupply:edit"]
                            textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">编辑</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'EXPIRED'){
                            [@shiro.hasPermission name = "admin:needSupply:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:needSupply:edit"]
                            textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">编辑</a></li>';
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:needSupply:delete"]
                            textHtml+='<li class="delete"><a title="删除" href="javascript:void(0);" supplyNeedId="'+n.id+'"  class="ml-5 del" style="text-decoration:none">删除</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'WILLSUPPLY'){
                            [@shiro.hasPermission name = "admin:needSupply:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:needSupply:edit"]
                            textHtml+='<li class="edit"><a title="编辑" href="edit.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">编辑</a></li>';
                            [/@shiro.hasPermission]
                        }
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
        function getStatus(statusCode){
            var status='';
            switch(statusCode){
                case 'SUPPLY':
                    status='供应中';
                    break;
                case 'WILLSUPPLY':
                    status='已供应';
                    break;
                case 'EXPIRED':
                    status='已过期';
                    break;
            }
            return status;
        }

        $("#add").on("click",function(){
            var storage=window.localStorage;
            storage.removeItem("needStartDate");
            storage.removeItem("needEndDate");
            storage.removeItem("needIds");
            storage.removeItem("needProducts");
            storage.removeItem("listTitle");
            storage.removeItem("news");
            window.location.href="add.jhtml";
        });

    });


</script>
</body>
</html>
[/#escape]