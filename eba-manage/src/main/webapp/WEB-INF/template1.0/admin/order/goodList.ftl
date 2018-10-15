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
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			.require_search,.ch_search,.update_B{border:1px solid #CADBF3;}
			.hang_list{padding:10px 0 10px 70px;}
			table th{border-top:1px solid #f0f0f0;}
			.update_B{float:right;margin-right:40px;}
			.trColor{
				background-color:#f9f9f9;
			}
			.table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
			.table-hover tbody tr.trColor td{background-color:#fff;color:#999;}
		</style>
	</head>
	<body >
		<div class="" style="width:100%;height:100%;overflow-y: auto">
			<form id="listForm" action="getGoodList.jhtml" method="get">
				<input type="hidden" name="orderId" value="${orderId}">
				<input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
				<div class="hang_list">
					<div id="filterMenu" class="require_search">
						<span class="search">商品筛选</span>						
						<ul class="check moreList" name="productCategoryId">
							<li name="productCategoryId" val="">所有分类</li>
							[#list productCategoryTree as productCategory]
								<li name="productCategoryId" val="${productCategory.id}" value="${productCategory.id}" [#if productCategoryId == productCategory.id]class="checked"[/#if]>
								
								${productCategory.name}</li>
							[/#list]
						</ul>
					</div>
					<div class="ch_search">
						<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
						<div class="search_input">
							<input type="text" id="goodName" name="goodName" value="${goodName}" maxlength="200" placeholder="请输入名称" />
						</div>
					</div>
					<button type="submit" class="search_button">查询</button>
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
				
				<div class="table_box">
					<table class="table table-border table-hover table_width" id="box">
						<thead>
							<tr class="text-l">
								<th width="5%"><input class="all_checked" type="checkbox" id="selectAlls" ></th>
								<th width="20%">${message("Goods.sn")}</th>
								<th width="20%">${message("Goods.name")}</th>
								<th width="20%">基本单位</th>
								<th width="20%">起订量</th>
								<th width="15%">订货量</th>
							</tr>
						</thead>
					</table>
					<div class="list_t_tbody" id="listTable">
						<table class="table table-border table-hover table_width">
							<thead>
								<tr class="text-l">
									<th width="5%" style="">
										<div class="th_div"></div>
									</th>
									<th width="20%"><div class="th_div"></div></th>
									<th width="20%"><div class="th_div"></div></th>
									<th width="20%"><div class="th_div"></div></th>
									<th width="20%"><div class="th_div"></div></th>
									<th width="15%"><div class="th_div"></div></th>
								</tr>
							</thead>
							<tbody>
								[#list page.content as product]
								<tr class="text-l" name="dataTr">
									<td><input type="checkbox" id="productId" name="ids" id="${product.id}" value="${product.id}" name="sub_checked" onclick="checkProduct(this);"/></td>
									<td>
										<span[#if product.goods.isOutOfStock] class="red"[#elseif product.goods.isStockAlert] class="blue"[/#if]>
											${product.sn}
										</span>
									</td>
									<td>
										<span title="${product.goods.name}">
											${abbreviate(product.goods.name, 80, "...")}
											[#if product.specifications?has_content]
											    [${product.specifications?join(", ")}]
											[/#if]
										</span>
									</td>
									<td>
									[#if product.goods.unit??]
										[#assign unit=product.goods.unit]
										${message("Goods.unit."+unit)}
									[/#if]
									</td>
                                    <td>
                                    	${product.minOrderQuantity}
									</td>
									<td>
										<input type="number" id="quantity" min="1" class="in_no input-text radius" value="1" oninput="checkUpdateQuantity(${product.id},this);" onpropertychange="checkUpdateQuantity(${product.id},this);"/>
										<input type="hidden" id="supplyPrice" value="${product.supplyPrice}" />
										<input type="hidden" id="productName" value="${product.goods.name} [#if product.specifications?has_content]
                                                            [${product.specifications?join(", ")}]
                                                        [/#if]" />
										<input type="hidden" id="sn" value="${product.sn}" />
									</td>
								</tr>
								[/#list]
								
							</tbody>
						</table>
					</div>
				
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
					[#include "/admin/include/pagination.ftl"]
				[/@pagination]
				</div>
			</form>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
		<script type="text/javascript">
		
			/*表格的高度，，随着电脑分倍率的变化而变化*/
//			var heightObj = $(document.body).height() - 140;
//			$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width")); 
		
			
			/*当input获得焦点时，外面的边框显示蓝色*/
			$(".focus_border").focus(function(){
				$(this).parent().addClass("add_border");
			})
			$(".focus_border").blur(function(){
				$(this).parent().removeClass("add_border");
			})
	
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
				var $listForm = $("#listForm");
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
				var checkedDom =  $("#filterMenu li.checked");
                var firstDom;
                var firstText;
				if(checkedDom.length == 0){
                    firstDom = $("#filterMenu").find("li:eq(0)");
                    firstText = firstDom.html();
                    firstDom.addClass("checked");
				}else{
                    firstText = checkedDom.html();
                }
                $(".search").html(firstText);

                $("tr[name=dataTr]").each(function(){
                	var productId=$(this).find("#productId").val();
                	var needProducts=window.parent.getCacheFromChild();
                	if(needProducts[productId] != undefined ){
                		$(this).find("#quantity").val(needProducts[productId].quantity);
                		$(this).addClass("trColor");
                		$(this).find("#productId").attr("onclick","return false;");
                		$(this).find("#productId").attr("name","");
                		$(this).find("#quantity").attr("readonly","readonly");
                	}
                	var needAddProducts=window.parent.getNeedAddProducts();
                	if (needAddProducts[productId] != undefined) {
                		$(this).find("#quantity").val(needAddProducts[productId].quantity);
                		$(this).addClass("trColor");
                		$(this).find("#productId").attr("onclick","return false;");
                		$(this).find("#productId").attr("name","");
                		$(this).find("#quantity").attr("readonly","readonly");
                	}
                });
			});

			function checkProduct(obj){
				var productId=$(obj).val();
				if ($(obj).is(':checked')) {
					var product={};
					product.productId=productId;
					product.quantity=$(obj).parent().parent().find("#quantity").val();
					product.supplyPrice=$(obj).parent().parent().find("#supplyPrice").val();
					product.productName=$(obj).parent().parent().find("#productName").val();
					product.sn=$(obj).parent().parent().find("#sn").val();

					window.parent.addNeedAddProduct(productId,product);
				}else{
					window.parent.delNeedAddProduct(productId);
				}
			}

			function checkAllProduct(){
				$("input[name=ids]").each(function(){
					var productId=$(this).val();
					if ($(this).is(':checked')) {
						var product={};
						product.productId=productId;
						product.quantity=$(this).parent().parent().find("#quantity").val();
						product.supplyPrice=$(this).parent().parent().find("#supplyPrice").val();
						product.productName=$(this).parent().parent().find("#productName").val();
						product.sn=$(this).parent().parent().find("#sn").val();

						window.parent.addNeedAddProduct(productId,product);
					}else{
						window.parent.delNeedAddProduct(productId);
					}
				});
			}

			// 全选
			$("#selectAlls").click( function() {
				var $contentRow = $("#listTable tr:gt(0)");
				var $this = $(this);
				var $enabledIds = $("#listTable input[name='ids']:enabled");
				if ($this.prop("checked")) {
					$enabledIds.prop("checked", true);
					if ($enabledIds.filter(":checked").size() > 0) {
						$contentRow.addClass("selected");
					} else {
						$deleteButton.addClass("disabled");
					}
				} else {
					$enabledIds.prop("checked", false);
					$contentRow.removeClass("selected");
				}
				checkAllProduct();
			});

			function checkUpdateQuantity(productId,obj){
				var needAddProducts=window.parent.getNeedAddProducts();
				if(needAddProducts[productId] != undefined){
			    	window.parent.updateOrderAddQuantity(productId,$(obj).val());
				}
			}
		</script>
	</body>
</html>
[/#escape]