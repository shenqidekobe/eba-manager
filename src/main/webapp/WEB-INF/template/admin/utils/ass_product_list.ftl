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

    function selectProduct(now , obj){
        var $id = $(now);

        if($id.attr("checked")){
            window.parent.addProduct(obj);
        }
        if(!$id.attr("checked")){
            window.parent.delProduct(obj);
        }
    }


$().ready(function() {


    var cacheProducts = window.parent.getCacheFromParent();

    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
        var id = $(this).val();
        if(id in cacheProducts){
            $(this).attr("checked" , true);
            $(this).attr("disabled" , true);
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


	//全选事件
	$("#selectAll").click(function(){
        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
            var obj = $(this).attr("tempValue");
            selectProduct(this , JSON.parse(obj));
        });
	});

});
</script>
</head>
<body>
	<form id="listForm" action="getAssProducts.jhtml" method="get">
        <input type="hidden" name="supplierId" value="${supplierId}"/>
        <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
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
							<li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}" [#if productCategory.id == productCategoryId] class="checked"[/#if]>[#if productCategory.grade != 0]
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
                    <li[#if page.searchProperty == "sn"] class="current"[/#if] val="sn">${message("Goods.sn")}</li>
                    <li[#if page.searchProperty == "name"] class="current"[/#if] val="name">${message("Goods.name")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;">id</a>
				</th>
                <th>
                    <a href="javascript:;">${message("Goods.productCategory")}</a>
                </th>
				<th>
					<a href="javascript:;">${message("Goods.name")}</a>
				</th>

				<th>
					<a href="javascript:;">规格</a>
				</th>
			</tr>
			[#list page.content as product]
				<tr>
					<td>
						<input type="checkbox" tempValue='{"productId":"${product.id}" , "goodsName":"${product.goods.name}" , "specificationValues":"[#list product.specificationValues as specificationValue]${specificationValue.value} [/#list]","productCategory":"${product.goods.productCategory.name}"}' name="ids" id="${product.id}" class="selectGoods" value="${product.id}" onclick="selectProduct(this , {'productId':'${product.id}' , 'goodsName':'${product.goods.name}' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value} [/#list]' , 'productCategory':'${product.goods.productCategory.name}'})" />
					</td>
					<td>
						${product.id}
					</td>
					<td>
						${product.goods.productCategory.name}
					</td>
					<td>
						${product.goods.name}
					</td>
					<td>
						[#list product.specificationValues as specificationValue]
							${specificationValue.value}&nbsp;
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