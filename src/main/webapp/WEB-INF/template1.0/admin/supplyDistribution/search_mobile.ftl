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
    <title>搜索</title>
    <style>
        .search-box .search-con{
            width:83%;
            float:left;
        }
        .search-box .search-text{width:calc(100% - 0.8rem)}
        .p_loading{display:none;}
        .distribut_label{
            margin-left:0.06rem;
        }
    </style>
</head>
<body>
<form id="listForm" action="list.jhtml" method="get">
    <input type="hidden" id="status" name="status" value="">
    <input type="hidden" name="totalPages" id="totalPages" value="" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="1" />
    <input type="hidden" name="pageSize" id="pageSize" value="8" />
    <input type="hidden" id="supplierSupplierId" value="${supplierSupplier.id}" />

    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="" class="search-detail" type="text" placeholder="请输入客户名称" />
            </div>
        </div>
        <button type="button" class="orderSearchB">搜索</button>
    </div>
    <ul class="customer-list" id="customerList">

    </ul>
    <p class="p_loading">正在加载...</p>
</form>

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

        //点击查询
        $(".orderSearchB").on("click",function(){
            var clientType=$("#clientType").val();
            var pageSize = $("#pageSize").val();
            var searchName=$("#searchName").val();
            var pageNumber = $("#pageNumber").val();
            console.log(searchName);
            param={
                "clientType":clientType,
                "searchName":searchName,
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            loadData(param);
        });


        var boolAjax = false;
        //加载数据
        function loadData(param){
            console.log(pageNumber+'----'+totalPages);
            $.get("asyncList.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data.list;
                    var textHtml='';
                    console.log(orders);
                    $.each(orders,function(i,n){
                        var name = '';

                        textHtml+='<li class="customer-li">';
                        textHtml+='<span class="supplyType ' +n.status+ '">'+getStatus(n.status)+'</span>';
                        textHtml+='<p class="name">'+n.supplierName+'</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';
                        if(n.status == 'inTheSupply'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
//                            textHtml+='<li class="edit"><a title="商品分配" href="distributionList.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">商品分配</a></li>';
                        }else if(n.status == 'toBeConfirmed'){
                            textHtml+='<li class="edit"><a title="确认" href="view.jhtml?id='+n.id+'&type=confirm" class="ml-5" style="text-decoration:none">确认</a></li>';
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'suspendSupply' || n.status == 'expired'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'willSupply' || n.status == 'rejected'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }
                        textHtml+='</ul></li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#totalPages").val(o.data.totalPages);

                    /*当前页+1*/
                    var pageNumber = $("#pageNumber").val();
                    pageNumber++;
                    $("#pageNumber").val(pageNumber);
                    boolAjax = false;
                    var pageSize = $("#pageSize").val();
                    if(orders.length == parseInt(pageSize)){
                        $(".p_loading").css("display","block");
                    }else{
                        $(".p_loading").css("display","none");
                    }
                }else {
                    errorInfoFun(o.msg);
                }
            });
        }

        function appendAjax(param){
            console.log(pageNumber+'----'+totalPages);
            $.get("asyncList.jhtml",param,function(o){
                if (o.code=='0') {
                    var orders = o.data.list;
                    var textHtml='';
                    console.log(orders);
                    $.each(orders,function(i,n){
                        var name = '';

                        textHtml+='<li class="customer-li">';
                        textHtml+='<span class="supplyType ' +n.status+ '">'+getStatus(n.status)+'</span>';
                        textHtml+='<p class="name">'+n.supplierName+'</p>';
                        textHtml+='<img class="customer-operate" src="${base}/resources/mobile/images/diandiandian.png" alt="图片">';
                        textHtml+='<ul class="operate-list">';
                        if(n.status == 'inTheSupply'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
//                            textHtml+='<li class="edit"><a title="商品分配" href="distributionList.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">商品分配</a></li>';
                        }else if(n.status == 'toBeConfirmed'){
                            textHtml+='<li class="edit"><a title="确认" href="view.jhtml?id='+n.id+'&type=confirm" class="ml-5" style="text-decoration:none">确认</a></li>';
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'suspendSupply' || n.status == 'expired'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }else if(n.status == 'willSupply' || n.status == 'rejected'){
                            [@shiro.hasPermission name = "admin:supplyDistribution:view"]
                            textHtml+='<li class="look"><a title="查看" href="view.jhtml?id='+n.id+'" class="ml-5" style="text-decoration:none">查看</a></li>';
                            [/@shiro.hasPermission]
                        }
                        textHtml+='</ul></li>';
                    });
                    $("#customerList").append(textHtml);

                    /*当前页+1*/
                    var pageNumber = $("#pageNumber").val();
                    pageNumber++;
                    $("#pageNumber").val(pageNumber);

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
            var pageNumber = $("#pageNumber").val();
            var totalPages = $("#totalPages").val();

            if(divHeight-(top-divScroll)>70){
                if(boolAjax){return false;}
                if(parseInt(pageNumber) <= parseInt(totalPages)){
                    param.pageNumber = pageNumber;
                    boolAjax=true;
                    appendAjax(param);
                }else{
                    $(".p_loading").html("");
                }

            }
        });
        function getStatus(statusCode){
            var status='';
            switch(statusCode){
                case 'inTheSupply':
                    status='供应中';
                    break;
                case 'toBeConfirmed':
                    status='待确认';
                    break;
                case 'expired':
                    status='已过期';
                    break;
                case 'rejected':
                    status='已拒绝';
                    break;
                case 'suspendSupply':
                    status='暂停供应';
                    break;
                case 'willSupply':
                    status='未开始';
                    break;
            }
            return status;
        }


    });


</script>
</body>
</html>
[/#escape]