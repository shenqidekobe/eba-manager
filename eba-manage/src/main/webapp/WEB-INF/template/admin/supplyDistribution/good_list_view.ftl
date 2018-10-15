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


$().ready(function() {

	var $listForm = $("#listForm");
	var $filterMenu = $("#filterMenu");
	var $filterMenuItem = $("#filterMenu li");
	
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

});
</script>
</head>
<body>
	<form id="listForm" action="viewGoods.jhtml" method="get">
		<input type="hidden" name="supplierSupplierId" value="${supplierSupplier.id}">
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
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

                <th>
                    <a href="javascript:;">起订量</a>
                </th>
			</tr>
			[#list page.content as goods]
				<tr>
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
					[#assign supplierProducts = goods.supplierProducts]
					<td>
						[#list supplierProducts as supplierProduct]
							[#list supplierProduct.products.specificationValues as specificationValue]
								${specificationValue.value}&nbsp;
							[/#list]
						${currency(supplierProduct.products.price, true)}<br />
						[/#list]
					</td>

                    <td>
						[#list supplierProducts as supplierProduct]
							${supplierProduct.supplyPrice}
                            <br />
						[/#list]
                    </td>

                    <td>
						[#list supplierProducts as supplierProduct]
						${supplierProduct.minOrderQuantity}
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