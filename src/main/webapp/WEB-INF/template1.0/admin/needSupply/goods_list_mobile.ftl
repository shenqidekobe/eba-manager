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
    <title>商品信息</title>
    <style>
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
    <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
    <form action="selectList.jhtml" method="get">
        <input type="hidden" name="goodListStatus" value="add">
        <div class="search-box">
            <div class="search-con">
                <div class="search-img"></div>
                <div class="search-text">
                    <input id="searchName" name="searchName" value="${searchName}" class="search-detail" type="text" placeholder="请输入商品名称" />
                </div>
            </div>
            <button id="search" type="submit" class="orderSearchB">搜索</button>
        </div>
    </form>
    <div class="goods">
        <div class="title">
            全部分类
            <img src="${base}/resources/mobile/images/down.png" alt="下拉">
        </div>
        <ul class="goods-type" id="filterMenu">
            [#list productCategoryTree as productCategory]
            <li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}" [#if productCategoryId == productCategory.id] class="checked" [/#if]>
            [#if productCategory.grade != 0]
            [#list 1..productCategory.grade as i]
            &nbsp;&nbsp;
            [/#list]
            [/#if]
            ${productCategory.name}
            </li>
            [/#list]
        </ul>
        <ul class="goods-list" id="productList">
            [#list page.content as product]
            <li class="good-li">
                <input type="hidden" name="id" value="${product.id}">
                <img class="chooseGood" data-id="${product.id}" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
                <div class="good-item">
                    [#if product.goods.image??]
                    <img src="${product.goods.image}" alt="">
                    [#else]
                    <img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">
                    [/#if]
                    <div class="detail">
                        <p class="goods-name">${product.goods.name}</p>
                        <p class="goodsTip">
                            [#list product.specificationValues as specificationValue]
                            <span>${specificationValue.value}</span>
                            [/#list]
                        </p>
                    </div>
                </div>
                <div class="order-info">
                    <div class="order-count">
                        <span class="name">起订量</span>
                        <input type="number" name="minOrderQuantity" class="count" min="${product.minOrderQuantity}" value="${product.minOrderQuantity}" placeholder="请输入" />
                    </div>
                    <div class="order-price">
                        <span class="name">供货价(￥)</span>
                        <input type="number" name="supplyPrice" class="price" min="0" value="${product.supplyPrice}" placeholder="请输入"/>
                    </div>
                </div>
            </li>
            [/#list]
        </ul>
    </div>
    <div class="confirm-box">
        <img class="chooseAll" id="all" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">
        <span class="all" >全选</span>
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
        var needProducts=eval('(' + storage.getItem("needProducts")+ ')');
        if (needProducts == null) {
            needProducts=[];
        }

        var $filterMenuItem = $("#filterMenu li");
        //商品种类下拉
        $('.goods .title').on('click',function (){
            if($('.goods-type').is(':hidden')){
                $('.goods .title img').attr({src:'${base}/resources/mobile/images/up.png'});
                $('.goods-type').slideDown();
            }else{
                $('.goods .title img').attr({src:'${base}/resources/mobile/images/down.png'});
                $('.goods-type').slideUp();
            }
        });

        // 筛选
        $filterMenuItem.click(function() {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            if ($this.hasClass("checked")) {
                $dest.val("");
            } else {
                $dest.val($this.attr("val"));
            }
            $('.goods .title img').attr({src:'${base}/resources/mobile/images/down.png'});
            $('.goods-type').slideUp();
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            var param={
                "pageNumber":pageNumber,
                "pageSize":pageSize,
                "searchName":$("#searchName").val(),
                "productCategoryId":$dest.val()
            };
            $.get("asyncSelectList.jhtml",param,function(o){
                if (o.code=='0') {
                    console.log(o);
                    var products = o.data.list;
                    $("#pageNumber").val(o.data.pageNumber);
                    $("#pageSize").val(o.data.pageSize);
                    $("#totalPages").val(o.data.totalPages);
                    $("#productList").html('');
                    var textHtml='';
                    $.each(products,function(i,n){
                        textHtml+='<li class="good-li">';
                        textHtml+='<input type="hidden" name="id" value="'+n.id+'">';
                        textHtml+='<img class="chooseGood" data-id="'+n.id+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<div class="good-item">';
                        if (n.image != '' && n.image != null) {
                            textHtml+='    <img src="'+n.image+'" alt="">';
                        }else{
                            textHtml+='   <img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">';
                        }
                        textHtml+='    <div class="detail">';
                        textHtml+='        <p class="goods-name">'+n.name+'</p>';
                        textHtml+='        <p class="goodsTip">';
                        textHtml+='            [#list product.specificationValues as specificationValue]';
                        textHtml+='                <span>${specificationValue.value}</span>';
                        textHtml+='            [/#list]';
                        textHtml+='        </p>';
                        textHtml+='    </div></div><div class="order-info">';
                        textHtml+='    <div class="order-count">';
                        textHtml+='        <span class="name">起订量</span>';
                        textHtml+='        <input type="number" name="minOrderQuantity"  class="count" min="1" value="1" placeholder="请输入" />';
                        textHtml+='    </div>';
                        textHtml+='    <div class="order-price">';
                        textHtml+='        <span class="name">供货价(￥)</span>';
                        textHtml+='        <input type="number"  name="supplyPrice" class="price" value="'+n.price+'" readonly />';
                        textHtml+='    </div></div></li>';
                    });

                    $("#productList").append(textHtml);

                    initSelect();
                }else {
                    errorInfoFun(o.msg);
                }
            });



        });

        //初始化选择本地已保存的数据
        function initSelect(){
            console.log(1);
            $.each(needProducts,function(index,value){
                $(".good-li").each(function(){
                    if($(this).find("img").first().data("id") == value.id){
                        $(this).find("img").first().attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                    }
                });
            });
        }
        initSelect();

        function addProducts(product){
            delProducts(product.id);
            needProducts.push(product);
        }

        function delProducts(id){
            for (var i = 0; i < needProducts.length; i++) {
                if(id == needProducts[i].id){
                    needProducts.splice(i,1);
                    break;
                }
            }
        }
        //选择商品
        function selectProduct(li){
            var id=parseInt($(li).find("input[name=id]").first().val());
            var $minOrderQuantity=$(li).find("input[name=minOrderQuantity]").first();
            var $supplyPrice=$(li).find("input[name=supplyPrice]").first();
            var minOrderQuantity=parseInt($minOrderQuantity.val());
            var supplyPrice=parseFloat($supplyPrice.val());
            if (isNaN(minOrderQuantity)) {
                var min=parseInt($minOrderQuantity.attr("min"));
                minOrderQuantity=min;
            }else{
                var min=parseInt($minOrderQuantity.attr("min"));
                if (minOrderQuantity < min) {
                    minOrderQuantity=min;
                }
            }
            if (isNaN(supplyPrice)) {
                supplyPrice=$supplyPrice.attr("min");
            }
            $supplyPrice.val(supplyPrice);
            $minOrderQuantity.val(minOrderQuantity);
            console.log(supplyPrice);
            console.log(minOrderQuantity);
            var product={};
            product.id=id;
            product.minOrderQuantity=minOrderQuantity;
            product.supplyPrice=supplyPrice;
            addProducts(product);
        }

        //选择商品
        $('#productList').delegate('.chooseGood','click',function () {
            if(!$(this).hasClass('selected')){
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                selectProduct($(this).parent());
            }else{
                $(this).attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                delProducts(parseInt($(this).data("id")));
            }

        });

        $('#productList').delegate("input[name=minOrderQuantity]","blur",function(){
            selectProduct($(this).parent().parent().parent());
        });

        $('#productList').delegate("input[name=supplyPrice]","blur",function(){
            selectProduct($(this).parent().parent().parent());
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
            $(".good-li").each(function(){
                if(isSelected){
                    $(this).find("img").first().attr({'src':'${base}/resources/mobile/images/xuanze-b.png'}).addClass('selected');
                    selectProduct($(this));
                }else{
                    $(this).find("img").first().attr({'src':'${base}/resources/mobile/images/xuanze-a.png'}).removeClass('selected');
                    delProducts(parseInt($(this).find("img").data("id")));
                }
            });
        });

        $("#yes").on("click",function(){
            var storage=window.localStorage;
            storage.setItem("needProducts",JSON.stringify(needProducts));
            window.location.href="add.jhtml";
        });

        var boolAjax = false;

        //加载数据
        function loadData(){
            var pageNumber = $("#pageNumber").val();
            var pageSize = $("#pageSize").val();
            var totalPages = $("#totalPages").val();
            if(parseInt(pageNumber) >= parseInt(totalPages)) {
                $(".p_loading").html("");
                return;
            }
            var next = ++pageNumber;
            var param={
                "pageNumber":pageNumber,
                "pageSize":pageSize
            };
            $.get("asyncSelectList.jhtml",param,function(o){
                if (o.code=='0') {
                    console.log(o);
                    var products = o.data.list;
                    if(products.length < 5){
                        $(".p_loading").html("");
                    }
                    var textHtml='';
                    $.each(products,function(i,n){
                        textHtml+='<li class="good-li">';
                        textHtml+='<input type="hidden" name="id" value="'+n.id+'">';
                        textHtml+='<img class="chooseGood" data-id="'+n.id+'" src="${base}/resources/mobile/images/xuanze-a.png" alt="图片">';
                        textHtml+='<div class="good-item">';
                        if (n.image != '' && n.image != null) {
                            textHtml+='    <img src="'+n.image+'" alt="">';
                        }else{
                            textHtml+='   <img src="${base}/resources/admin1.0/images/mr_icon.png" alt="">';
                        }
                        textHtml+='    <div class="detail">';
                        textHtml+='        <p class="goods-name">'+n.name+'</p>';
                        textHtml+='        <p class="goodsTip">';
                        textHtml+='            [#list product.specificationValues as specificationValue]';
                        textHtml+='                <span>${specificationValue.value}</span>';
                        textHtml+='            [/#list]';
                        textHtml+='        </p>';
                        textHtml+='    </div></div><div class="order-info">';
                        textHtml+='    <div class="order-count">';
                        textHtml+='        <span class="name">起订量</span>';
                        textHtml+='        <input type="number" name="minOrderQuantity"  class="count" min="'+n.minOrderQuantity+'" value="'+n.minOrderQuantity+'" placeholder="请输入" />';
                        textHtml+='    </div>';
                        textHtml+='    <div class="order-price">';
                        textHtml+='        <span class="name">供货价(￥)</span>';
                        textHtml+='        <input type="number"  min="0" name="supplyPrice" class="price" value="'+n.price+'" readonly />';
                        textHtml+='    </div></div></li>';
                    });

                    $("#productList").append(textHtml);

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
