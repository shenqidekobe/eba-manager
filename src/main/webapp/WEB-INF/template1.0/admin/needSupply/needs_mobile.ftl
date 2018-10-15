[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/LCalendar.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/supply.css" />
    <title>客户信息</title>
    <style>
        .chooseNeed{
            display: inline-block;
            width: 0.4rem;
            height: 0.4rem;
            font-size: 0.22rem;
            border-radius: 0.06rem;
            position: absolute;
            left: 0.4rem;
            top: 0.25rem;
            text-align: center;
        }
        .needAll{
            font-size: 0.28rem;
            color: #333;
            position: absolute;
            top: 0.26rem;
            left: 1.1rem;
            font-family: 'PingFangSC';
        }
        .search-box .search-con{
            width:83%;
            float:left;
        }
        .search-box .search-text{width:calc(100% - 0.8rem)}
    </style>
</head>
<body >
<div class="choose-customer">
    <input type="hidden" name="totalPages" id="totalPages" value="${page.getTotalPages()}" />
    <input type="hidden" name="pageNumber" id="pageNumber" value="${page.pageable.pageNumber}" />
    <input type="hidden" name="pageSize" id="pageSize" value="${page.pageable.pageSize}" />
    <div class="search-box">
        <div class="search-con">
            <div class="search-img"></div>
            <div class="search-text">
                <input id="searchName" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入客户名称" />
            </div>
        </div>
        <button id="search" type="button" class="orderSearchB">搜索</button>
    </div>
    <ul class="customer-list" id="customerList">
        [#list page.content as need]
        <li class="customer-li">
            <img class="chooseImg" data-id="${need.id}" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
            <p class="customer-name">${need.name}</p>
        </li>
        [/#list]
    </ul>
    <div class="confirm-box">
        <img class="chooseNeed" id="all" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
        <span class="needAll" >全选</span>
        <button class="confirm" id="yes">确定</button>
    </div>
    [#if page.content.size() > 8]
    <p class="p_loading">正在加载...</p>
    [/#if]
</div>

<script src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
    $(function () {
        var storage=window.localStorage;
        var needIds=eval('(' + storage.getItem("needIds")+ ')');
        console.log(needIds);
        if (needIds == null) {
            needIds=[];
        }

        function addNeedId(id){
            delNeedId(id);
            needIds.push(id);
        }

        function delNeedId(id){
            for (var i = 0; i < needIds.length; i++) {
                if(id == needIds[i]){
                    needIds.splice(i,1);
                    break;
                }
            }
        }
        //初始化数据
        function initSelect(){
            $.each(needIds,function(index,value){
                $(".chooseImg").each(function(){
                    if($(this).data("id") == value){
                        $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                    }
                });
            });
        }
        initSelect();
        //选择个体用户
        $("#customerList").delegate('.chooseImg','click',function () {
            if(!$(this).hasClass('selected')){
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                addNeedId($(this).data("id"));
            }else{
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                delNeedId($(this).data("id"));
            }
        });

        $("#all").on("click",function(){
            var isSelected;
            if(!$(this).hasClass('selected')){
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                isSelected=true;
            }else{
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                isSelected=false;
            }
            $(".chooseImg").each(function(){
                if(isSelected){
                    $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                    addNeedId($(this).data("id"));
                }else{
                   $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                    delNeedId($(this).data("id"));
                }
            });
        });

        //搜索
        $('#search').on('click',function () {
            var searchName = $('#searchName').val();
            $("#customerList").html('');
            $(".p_loading").html("正在加载...");
            $("#pageNumber").val('1');
            boolAjax = false;

            $.get("asyncNeedsMobile.jhtml",{searchName:searchName},function(o){
                console.log(o);
                if (o.code=='0') {
                    var needs = o.data.list;
                    if(needs.length == 0){
                        $("#customerList").append('<div class="no-data" >无数据</div>');
                        $(".p_loading").hide();
                    }else if(needs.length <8){
                        $(".p_loading").show().html("");
                    }else{
                        $(".p_loading").show();
                    }
                    var textHtml='';
                    $.each(needs,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<img class="chooseImg" data-id="'+n.id+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<p class="customer-name" id="'+n.id+'">'+n.name+'</p>';
                        textHtml+='</li>';
                    });
                    $("#customerList").html(textHtml);
                    $("#pageNumber").val(o.data.pageNumber);
                    $("#pageSize").val(o.data.pageSize);
                    $("#totalPages").val(o.data.totalPages);
                    initSelect();
                }else {
                    errorInfoFun(o.msg);
                }
            });
        });
        $("#yes").on("click",function(){
            storage.setItem("needIds",JSON.stringify(needIds));
            window.location.href="add.jhtml";
        });


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
                "pageSize":pageSize,
                "searchName":$('#searchName').val()
            };
            $.get("asyncNeedsMobile.jhtml",param,function(o){
                if (o.code=='0') {
                    var needs = o.data.list;
                    if(needs.length < 10){
                        $(".p_loading").html("");
                    }
                    var textHtml='';
                    $.each(needs,function(i,n){
                        var name = '';
                        textHtml+='<li class="customer-li">';
                        textHtml+='<img class="chooseImg" data-id="'+n.id+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<p class="customer-name">'+n.name+'</p>';
                        textHtml+='</li>';
                    });
                    $("#customerList").append(textHtml);
                    $("#pageNumber").val(next);
                    initSelect();
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

    $(function(){
        pushHistory();
        window.addEventListener("popstate", function(e) {
            window.location.href = 'add.jhtml';
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
