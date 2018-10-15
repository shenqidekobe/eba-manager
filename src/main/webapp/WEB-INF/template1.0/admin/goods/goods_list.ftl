[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>
    <style>
        .require_search, .ch_search, .update_B {
            border: 1px solid #f0f0f0;
        }

        .hang_list {
            padding: 10px 0 10px 70px;
        }
		table th{border-top:1px solid #f0f0f0;}
        body {
            overflow: hidden;
        }
        .update_B {
            float: right;
            margin-right: 40px;
        }
    </style>
</head>
<body>
<div class="" style="width:100%;">
    <form action="selectList.jhtml" method="get" id="listForm">
        <input type="hidden" id="type" name="type" value="${type}"/>
        <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}"/>
        <input type="hidden" id="brandId" name="brandId" value="${brandId}"/>
        <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
        <input type="hidden" id="promotionId" name="promotionId" value="${promotionId}"/>
        <input type="hidden" id="isMarketable" name="isMarketable" value="${(isMarketable?string("true", "false"))!}"/>
        <input type="hidden" id="isList" name="isList" value="${(isList?string("true", "false"))!}"/>
        <input type="hidden" id="isTop" name="isTop" value="${(isTop?string("true", "false"))!}"/>
        <input type="hidden" id="isOutOfStock" name="isOutOfStock" value="${(isOutOfStock?string("true", "false"))!}"/>
        <input type="hidden" id="isStockAlert" name="isStockAlert" value="${(isStockAlert?string("true", "false"))!}"/>

        <input type="hidden" name="searchProperty" value="name"/>

        <div class="hang_list">
            <div class="require_search">
                <span class="search">所有分类</span>
                <ul class="check moreList">
                    <li name="productCategoryId" val="">所有分类</li>
                    [#list productCategoryTree as productCategory]
                        <li name="productCategoryId" val="${productCategory.id}"
                            value="${productCategory.id}" [#if productCategoryId == productCategory.id]class="li_bag"[/#if]>[#if productCategory.grade != 0]
                            [#list 1..productCategory.grade as i]
                                &nbsp;&nbsp;
                            [/#list]
                        [/#if]${productCategory.name}</li>
                    [/#list]
                </ul>
            </div>
            <div class="ch_search">
                <img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt=""/>
                <!--<div class="ch_sear_type">
                    <div class="setUp">
                        <p class="bianhao">编号</p>
                        <span></span>
                        <p class="canzuo">操作员</p>
                    </div>
                </div>-->
                <div class="search_input">
                    <input type="text" name="searchValue" placeholder="请输入编号搜索" value="${page.searchValue}"/>
                </div>
            </div>
            <button type="submit" class="search_button">查询</button>
            <button type="button" class="op_button update_B" id="refreshButton">刷新</button>
        </div>

		<div class="table_box">
        <table class="table table-border table-hover table_width boo">
            <thead>
            <tr class="text-l">
                <th width="3%"><input class="all_checked" type="checkbox" value="" id="selectAll"></th>
                <th width="10%">${message("Goods.sn")}</th>
                <th width="20%">${message("Goods.name")}</th>
                <th width="10%">${message("Goods.productCategory")}</th>
                <th width="10%">${message("Goods.price")}</th>
                <th width="10%">${message("Goods.isMarketable")}</th>
                <th width="16%">规格价</th>
                <th width="10%">供货价</th>
                <th width="10%">起订量</th>
            </tr>
            </thead>
        </table>
        <div class="list_t_tbody">
            <table class="table table-border table-hover table_width" id="listTable">
                <thead>
                <tr class="text-l">
                    <th width="3%" style="">
                        <div class="th_div" style="">
                            <input class="all_checked" type="checkbox" value="">
                        </div>
                    </th>
                    <th width="10%">
                        <div class="th_div">编号</div>
                    </th>
                    <th width="20%">
                        <div class="th_div">名称</div>
                    </th>
                    <th width="10%">
                        <div class="th_div">商品分类</div>
                    </th>
                    <th width="10%">
                        <div class="th_div">销售价</div>
                    </th>
                    <th width="10%">
                        <div class="th_div">是否上架</div>
                    </th>
                    <th width="16%">
                        <div class="th_div">规格价</div>
                    </th>
                    <th width="10%">
                        <div class="th_div">供货价</div>
                    </th>
                    <th width="10%">
                        <div class="th_div">起订量</div>
                    </th>
                </tr>
                </thead>
                <tbody>
                    [#list page.content as goods]
                    <tr class="text-l">
                        <td><input type="checkbox" name="ids" id="${goods.id}" class="selectGoods" value="${goods.id}"
                                   onclick="selectGoods(${goods.id})"/></td>
                        <td>${goods.sn}</td>
                        <td>${abbreviate(goods.name, 80, "...")}</td>
                        <td>${goods.productCategory.name}</td>
                        <td>${currency(goods.price, true)}</td>
                        <td><img src="${base}/resources/admin1.0/images/shangjias_icon.svg" alt=""/></td>
                        [#assign products = goods.products]
                        <td>
                            [#list products as product]
                                [#list product.specificationValues as specificationValue]
                                    <p class="inPrP">${specificationValue.value}&nbsp;${currency(product.price, true)}</p>

                                [/#list]
                            [/#list]
                        </td>
                        <td>
							
							[#list products as product]
								<p class="price_form_p">
									<span>￥</span>
									<input type="text" class="in_no" id="product_${product.id}" productId="${product.id}" goodsId="${goods.id}" name="product_${goods.id}" value="${product.price}" oninput="javascript:updatePrice(this);" onpropertychange="javascript:updatePrice(this);"/>
								</p>
							[/#list]

                        </td>

                        <td>
                            [#list products as product]
                                <p class="price_form_p">
                                    <input type="text" id="product_orderQuantity_${product.id}" min="1" class="in_no" productId="${product.id}" goodsId="${goods.id}" name="product_orderQuantity_${goods.id}" value="1" oninput="javascript:updateOrderQuantity(this);" onpropertychange="javascript:updateOrderQuantity(this);"/>
                                </p>
                            [/#list]
                        </td>

                    </tr>
                    [/#list]
                </tbody>
            </table>
            </div>
        </div>
        [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
            [#include "/admin/include/pagination.ftl"]
        [/@pagination]
    </form>
</div>


<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>

<script>

    /*表格的高度，，随着电脑分倍率的变化而变化*/
    var heightObj = $(document.body).height() - 140;

    function selectGoods(id) {
        //alert($("#"+id).val());
        var $id = $("#" + id);
        var parent = $id.parent().parent();
        //tr下面所有规格价格
        //var inputs = parent.find("input.products");
        var inputs = parent.find("input[name='product_"+id+"']");
        if ($id.attr("checked")) {
            var tempJson = {};
            $.each(inputs, function (i, input) {
                var prodId = $(input).attr("productId") ;
                var minOrderQuantity = $("#product_orderQuantity_"+prodId).val() ;
                tempJson[$(input).attr("productId")] = {
                    "products": prodId,
                    "supplyPrice": $(input).val(),
                    "minOrderQuantity" : minOrderQuantity
                };
            });
            window.parent.addNeedProduct(id, tempJson);
        }
        if (!$id.attr("checked")) {
            window.parent.delNeedProduct(id);
        }
    }

    function updatePrice(thisObj) {
        var goodsId = $(thisObj).attr("goodsId");
        var productId = $(thisObj).attr("productId");
        var updPrice = $(thisObj).val();
        if ($("#" + goodsId).attr("checked")) {
            window.parent.updatePrice(goodsId, productId, updPrice);
        }
    }


    //起订量修改
    function updateOrderQuantity(thisObj) {
        var goodsId = $(thisObj).attr("goodsId");
        var productId = $(thisObj).attr("productId");
        var orderQuantity = $(thisObj).val();
        if ($("#" + goodsId).attr("checked")) {
            window.parent.updateOrderQuantity(goodsId, productId, orderQuantity);
        }
    }

    $().ready(function () {
        var $listForm = $("#listForm");

//      $(".list_t_tbody").css("height", heightObj);
        $(".table_width").css("width", $(".list_t_tbody").css("width"));


        /*当input获得焦点时，外面的边框显示蓝色*/
        $(".focus_border").focus(function () {
            $(this).parent().addClass("add_border");
        })
        $(".focus_border").blur(function () {
            $(this).parent().removeClass("add_border");
        })

        /*搜索条件*/
        $(".require_search li").on("click", function () {
            var $this = $(this);
            var $dest = $("#" + $this.attr("name"));
            $dest.val($this.attr("val"));

            $(this).parent().siblings(".search").html($(this).html());
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
            $(".check").css("display", "none");

            $listForm.submit();


        });

        $(".search").html($(".require_search ul li.li_bag").html());


        $(".require_search").mouseover(function () {
            $(this).find("ul").css("display", "block");
        });
        $(".require_search").mouseout(function () {
            $(this).find("ul").css("display", "none");
        });

        var cacheProducts = window.parent.getCacheFromChild();

        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
            var id = $(this).val();
            if (id in cacheProducts) {
                $(this).attr("checked", true);
                var products = cacheProducts[id];
                $.each(products, function (key, item) {
                    $("#product_" + key).val(item['supplyPrice']);
                    $("#product_orderQuantity_"+key).val(item['minOrderQuantity']) ;
                });
            }
        });



        [@flash_message /]


        //全选事件
        $("#selectAll").click(function () {
            $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
                var id = $(this).val();
                selectGoods(id);
            });
        });

    });


</script>

</body>
</html>
[/#escape]
