[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>${message("admin.goods.list")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team"/>
<meta name="copyright" content="UTLZ"/>
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<style type="text/css">
.moreTable th {
	width: 80px;
	line-height: 25px;
	padding: 5px 10px 5px 0px;
	text-align: right;
	font-weight: normal;
	color: #333333;
	background-color: #f8fbff;
}

.moreTable td {
	line-height: 25px;
	padding: 5px;
	color: #666666;
}

.promotion {
	color: #cccccc;
}

.stockAlert {
	color: orange;
}

.outOfStock {
	color: red;
	font-weight: bold;
}
</style>
<script type="text/javascript">
    function getParentData(){
        var scopeValue = window.parent.document.getElementById("scopeValue").value;
        //alert(scopeValue);
        var arr = new Array();
        arr = scopeValue.split(",");
        for (i=0;i<arr.length ;i++ ){
            //alert(arr[i]); //分割后的字符输出
            $("input[name='ids']").each(function(){//遍历根据name属性取到的所有值
                //alert($(this).val());
                var id = $(this).val();
                if(id == arr[i]){
                    $("#"+id).attr("checked",true)
                }
                /* if($("#"+id).attr("checked")){
                    $("#selectAll").attr("checked",true);
                } */
            });
        }
    }


    function selectGoods(id){
        //alert($("#"+id).val());
        var $id = $("#"+id);
        var parent = $id.parent().parent();
        //tr下面所有规格价格
        var inputs = parent.find("input[name='product_"+id+"']");
        if($id.attr("checked")){
            var tempJson = {} ;
            $.each(inputs, function (i, input) {
                tempJson[$(input).attr("productId")] = {"products":$(input).attr("productId") , "supplyPrice":$(input).val()};
            });
            window.parent.addNeedProduct(id , tempJson);
        }
        if(!$id.attr("checked")){
            window.parent.delNeedProduct(id);
        }
    }

    function updatePrice(thisObj){
    	var goodsId = $(thisObj).attr("goodsId") ;
        var productId = $(thisObj).attr("productId") ;
		var updPrice = $(thisObj).val() ;
        if($("#"+goodsId).attr("checked")){
            window.parent.updatePrice(goodsId ,productId , updPrice);
        }
    }


$().ready(function() {

    var cacheProducts = window.parent.getCacheFromChild();

    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
        var id = $(this).val();
        if (id in cacheProducts) {
            $(this).attr("checked", true);
            var products = cacheProducts[id];
            $.each(products, function (key, item) {
                $("#product_" + key).val(item['supplyPrice']);
            });
        }
    });

	var $listForm = $("#listForm");
	var $filterMenu = $("#filterMenu");
	var $filterMenuItem = $("#filterMenu li");
	var $moreButton = $("#moreButton");
	
	[@flash_message /]
	
	// 筛选菜单
	$filterMenu.hover(
		function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		}
	);
	
	// 筛选
	$filterMenuItem.click(function() {
		var $this = $(this);
		var $dest = $("#" + $this.attr("name"));
		if ($this.hasClass("checked")) {
			$dest.val("");
		} else {
			$dest.val($this.attr("val"));
		}
		$listForm.submit();
	});

	// 更多选项
	$moreButton.click(function() {
		$.dialog({
			title: "${message("admin.goods.moreOption")}",
			[@compress single_line = true]
				content: '
				<table id="moreTable" class="moreTable">
					<tr>
						<th>
							${message("Goods.productCategory")}:
						<\/th>
						<td>
							<select name="productCategoryId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list productCategoryTree as productCategory]
									<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>
										[#if productCategory.grade != 0]
											[#list 1..productCategory.grade as i]
												&nbsp;&nbsp;
											[/#list]
										[/#if]
										[#noescape]
											${productCategory.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.type")}:
						<\/th>
						<td>
							<select name="type">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list types as value]
									<option value="${value}"[#if value == type] selected="selected"[/#if]>${message("Goods.Type." + value)}<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.brand")}:
						<\/th>
						<td>
							<select name="brandId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list brands as brand]
									<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>
										[#noescape]
											${brand.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.tags")}:
						<\/th>
						<td>
							<select name="tagId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list tags as tag]
									<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>
										[#noescape]
											${tag.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
					<tr>
						<th>
							${message("Goods.promotions")}:
						<\/th>
						<td>
							<select name="promotionId">
								<option value="">${message("admin.common.choose")}<\/option>
								[#list promotions as promotion]
									<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>
										[#noescape]
											${promotion.name?html?js_string}
										[/#noescape]
									<\/option>
								[/#list]
							<\/select>
						<\/td>
					<\/tr>
				<\/table>',
			[/@compress]
			width: 470,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onOk: function() {
				$("#moreTable :input").each(function() {
					var $this = $(this);
					$("#" + $this.attr("name")).val($this.val());
				});
				$listForm.submit();
			}
		});
	});

	//全选事件
	$("#selectAll").click(function(){
        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
            var id = $(this).val();
            selectGoods(id);
        });
	});

});
</script>
</head>
<body>
	<form id="listForm" action="selectList.jhtml" method="get">
		<input type="hidden" id="type" name="type" value="${type}" />
		<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
		<input type="hidden" id="brandId" name="brandId" value="${brandId}" />
		<input type="hidden" id="tagId" name="tagId" value="${tagId}" />
		<input type="hidden" id="promotionId" name="promotionId" value="${promotionId}" />
		<input type="hidden" id="isMarketable" name="isMarketable" value="${(isMarketable?string("true", "false"))!}" />
		<input type="hidden" id="isList" name="isList" value="${(isList?string("true", "false"))!}" />
		<input type="hidden" id="isTop" name="isTop" value="${(isTop?string("true", "false"))!}" />
		<input type="hidden" id="isOutOfStock" name="isOutOfStock" value="${(isOutOfStock?string("true", "false"))!}" />
		<input type="hidden" id="isStockAlert" name="isStockAlert" value="${(isStockAlert?string("true", "false"))!}" />
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div id="filterMenu" class="dropdownMenu">
					<a href="javascript:;" class="button">
						${message("admin.goods.filter")}<span class="arrow">&nbsp;</span>
					</a>
					<ul class="check">
						<li name="productCategoryId" val="">所有分类</li>
						[#list productCategoryTree as productCategory]
							<li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}">[#if productCategory.grade != 0]
								[#list 1..productCategory.grade as i]
                                    &nbsp;&nbsp;
								[/#list]
							[/#if]${productCategory.name}</li>
						[/#list]
					</ul>
				</div>
			</div>
			<div id="searchPropertyMenu" class="dropdownMenu">
				<div class="search">
					<span class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li val="sn">${message("Goods.sn")}</li>
					<li val="name">${message("Goods.name")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="sn">${message("Goods.sn")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">${message("Goods.name")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="productCategory">${message("Goods.productCategory")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="price">${message("Goods.price")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="isMarketable">${message("Goods.isMarketable")}</a>
				</th>
				<th>
					<a href="javascript:;">规格价</a>
				</th>
                <th>
                    <a href="javascript:;">供货价</a>
                </th>
			</tr>
			[#list page.content as goods]
				<tr>
					<td>
						<input type="checkbox" name="ids" id="${goods.id}" class="selectGoods" value="${goods.id}" onclick="selectGoods(${goods.id})" />
					</td>
					<td>
						<span[#if goods.isOutOfStock] class="red"[#elseif goods.isStockAlert] class="blue"[/#if]>
							${goods.sn}
						</span>
					</td>
					<td>
						<span title="${goods.name}">
							${abbreviate(goods.name, 80, "...")}
						</span>
						[#if goods.type != "general"]
							<span class="red">*</span>
						[/#if]
						[#list goods.validPromotions as promotion]
							<span class="promotion" title="${promotion.title}">${promotion.name}</span>
						[/#list]
					</td>
					<td>
						${goods.productCategory.name}
					</td>
					<td>
						${currency(goods.price, true)}
					</td>
					<td>
						<span class="${goods.isMarketable?string("true", "false")}Icon">&nbsp;</span>
					</td>
					<td>
						[#list goods.products as product]
							[#list product.specificationValues as specificationValue]
								${specificationValue.value}&nbsp;
							[/#list]
						${currency(product.price, true)}<br />
						[/#list]
					</td>

                    <td>
						[#list goods.products as product]

						<input id="product_${product.id}" productId="${product.id}" goodsId="${goods.id}" name="product_${goods.id}" style="width: 50px;height: 10px" value="${product.price}" class="text" oninput="javascript:updatePrice(this);" onpropertychange="javascript:updatePrice(this);">
                            <br />
						[/#list]
                    </td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>
[/#escape]