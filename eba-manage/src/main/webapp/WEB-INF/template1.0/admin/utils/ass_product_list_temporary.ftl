[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.goods.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.trColor{
				background-color:#f9f9f9;
			}
			.table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
			.table-hover tbody tr.trColor td{background-color:#fff;color:#999;}

		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			
		<form id="listForm" action="getAssProducts.jhtml" method="get">
			<input type="hidden" name="supplierId" value="${supplierId}"/>
        	<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
			<div class="ch_condition">
				<div id="filterMenu" class="require_search">
						<span class="search">所有分类</span>						
						<ul class="check moreList" name="productCategoryId">
							<li name="productCategoryId" val="">所有分类</li>
							[#list productCategoryTree as productCategory]
								<li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}"[#if productCategoryId == productCategory.id]class="checked"[/#if]>[#if productCategory.grade != 0]
									[#list 1..productCategory.grade as i]
	                                    &nbsp;&nbsp;
									[/#list]
								[/#if]${productCategory.name}</li>
								
							[/#list]
						</ul>
					</div>
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<!--<div class="ch_sear_type">
						<div class="setUp">	
							<p class="bianhao">编号</p>
							<span></span>
							<p class="canzuo">操作员</p>
						</div>
					</div>-->
					<div class="search_input">
						<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="请输入名称" />
						<input type="hidden" name="searchProperty" value="name">
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAlls"></th>
							<th width="5%">id</th>
							<th width="15%">${message("Goods.productCategory")}</th>
							<th width="20%">${message("Goods.name")}</th>
							<th width="20%">规格</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox">
									</div>
								</th>
								<th width="5%"><div class="th_div">id</div></th>
								<th width="15%"><div class="th_div">${message("Goods.productCategory")}</div></th>
								<th width="20%"><div class="th_div">${message("Goods.name")}</div></th>
								<th width="20%"><div class="th_div">规格</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as product]
							<tr [#if product.source??] [#if product.turnoverSupplyPrice?? && product.goods.supplierSupplier.status == "inTheSupply"] class="text-l"
							[#else] class="text-l trColor" [/#if] [#else] [#if product.turnoverSupplyPrice??] class="text-l" [#else] class="text-l trColor" [/#if] [/#if]>
								<td>
									<input type="checkbox" tempValue="{'productId':'${product.id}' , 'goodsName':'[#noescape]${product.goods.name?html?js_string}[/#noescape]' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]','productCategory':'[#noescape]${product.goods.productCategory.name?html?js_string}[/#noescape]', 'minOrderQuantity' : '${product.turnoverMinOrderQuantity}'}" name="ids" id="${product.id}" class="selectGoods" value="${product.id}" [#if product.source??] [#if product.turnoverSupplyPrice?? && product.goods.supplierSupplier.status == "inTheSupply"] onclick="selectProduct(this , {'productId':'${product.id}' , 'goodsName':'[#noescape]${product.goods.name?html?js_string}[/#noescape]' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]' , 'productCategory':'[#noescape]${product.goods.productCategory.name?html?js_string}[/#noescape]', 'minOrderQuantity' : '${product.turnoverMinOrderQuantity}'})" [#else] onclick="return false;" [#if product.goods.supplierSupplier.status != "inTheSupply"] title="分销商品的企业供应已过期" [#else] title="未设置流水客户价格" [/#if]  [/#if] [#else] [#if product.turnoverSupplyPrice??] onclick="selectProduct(this , {'productId':'${product.id}' , 'goodsName':'[#noescape]${product.goods.name?html?js_string}[/#noescape]' , 'specificationValues':'[#list product.specificationValues as specificationValue]${specificationValue.value?html} [/#list]' , 'productCategory':'[#noescape]${product.goods.productCategory.name?html?js_string}[/#noescape]', 'minOrderQuantity' : '${product.turnoverMinOrderQuantity}'})" [#else] onclick="return false;" title="未设置流水客户价格" [/#if] [/#if]

									 />
								</td>
								<td>${product.id}</td>
								<td>${product.goods.productCategory.name}</td>
								<td>${product.goods.name}</td>
								<td>
									[#list product.specificationValues as specificationValue]
										${specificationValue.value}&nbsp;
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
		<script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
		<script type="text/javascript" src="${base}/resources/admin1.0/js/kkpager/kkpager.js"></script><!--分页-->
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/laypage/1.2/laypage.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        <script type="text/javascript">
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 150;
			$(".list_t_tbody").css("height",heightObj);
			
			function selectProduct(now , obj){
		        var $id = $(now);

		        if($id.attr("checked")){
		            window.parent.addProduct(obj);
		        }
		        if(!$id.attr("checked")){
		            window.parent.delProduct(obj);
		        }
		    }
			
			/*搜索条件*/
        	$(".require_search li").on("click",function(){
        		$(this).parent().siblings(".search").html($(this).html());
        		$(this).addClass("li_bag").siblings().removeClass("li_bag");
        		$(".check").css("display","none");
        	});
        	$(".require_search").mouseover(function(){
				$(this).find("ul").css("display","block");
			});
			$(".require_search").mouseout(function(){
				$(this).find("ul").css("display","none");
			});

			$().ready(function() {
			    var cacheProducts = window.parent.getCacheFromParent();
	
			    $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
			        var id = $(this).val();
			        if(id in cacheProducts){
			            $(this).attr("checked" , true);
			            // $(this).attr("disabled" , true);
			        }
			    });
	
				[@flash_message /]
				
				var $listForm = $("#listForm");
				var $filterMenu = $("#filterMenu");
				var $filterMenuItem = $("#filterMenu li");
				
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
				$("#selectAlls").click(function(){
			        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
			        	if ($(this).parent().parent().hasClass("trColor")) {
			        		return true;
			        	}
			        	if ($("#selectAlls").is(":checked")) {
			        		$(this).attr("checked",true);
			        	}else{
			        		$(this).attr("checked",false);
			        	}
			            var obj = $(this).attr("tempValue");
			            selectProduct(this , eval("(" + obj + ")"));
			        });
				});
	
			});
			
		</script>
	</body>
</html>
[/#escape]