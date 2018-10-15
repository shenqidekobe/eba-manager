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
            $.each(inputs, function (i, input) {
            	window.parent.addNeedProduct($(input).attr("productId"),$(input).val());
            });
        }
        if(!$id.attr("checked")){
        	var $tr=$id.parent().parent();
        	var $products=$tr.find("input[name=product_"+$id.val()+"]");
            $products.each(function(){
            	window.parent.removeByValue($(this).attr("productid"));
            });
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
			var $id=$(this);
			var $tr=$id.parent().parent();
        	var $products=$tr.find("input[name=product_"+$id.val()+"]");
        	$products.each(function(){
        		var productid= $(this).attr("productid");
            	$.each(cacheProducts, function (i, cacheProduct) {
					if(cacheProduct.products == productid){
						$id.attr("checked" , true);
					}
				});
            });
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
	<form id="listForm" action="distributionGoods.jhtml" method="get">
		<input type="hidden" id="needId" name="needId" value="${needId}" />
		<input type="hidden" id="supplierSupplierId" name="supplierSupplierId" value="${supplierSupplier.id}" />
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
                <th style="display:none">
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

                    <td style="display:none">
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