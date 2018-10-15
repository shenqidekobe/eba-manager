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
			body{background:#f9f9f9;}
			.child_page {
				width: calc(100% - 20px);
				height: calc(100% - 20px);
			}
			.update_B{float:right;margin-right:40px;}
			.confir_S{margin-top:8px;border:0;}
			.confir_S:hover{color:#fff;}
			.qiding_p{width:50px;}
			.qiding_p input{width:100%;}
			
		</style>
	</head>
	<body >
	
		<div class="child_page">
			<div class="cus_nav">
				<ul>
					<li><a href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="">流水供应 </a></li>
				</ul>
			</div>
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
				<div class="ch_condition">
					<div id="filterMenu" class="require_search">
						<span class="search">商品筛选</span>						
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
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
				
				<div class="table_con">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="10%">${message("Goods.sn")}</th>
								<th width="15%">${message("Goods.name")}</th>
								<th width="10%">${message("Goods.productCategory")}</th>
								<th width="15%">规格</th>
								<th width="10%">${message("Goods.price")}</th>
								<th width="13%">供应价</th>
                                <th width="17%">起订量</th>
							</tr>
						</thead>
					</table>
					<div class="list_t_tbody" id="listTable">
						<table class="table table-border table-hover table_width">
							<thead>
								<tr class="text-l">
									<th width="10%"><div class="th_div">${message("Goods.sn")}</div></th>
									<th width="15%"><div class="th_div">${message("Goods.name")}</div></th>
									<th width="10%"><div class="th_div">${message("Goods.productCategory")}</div></th>
									<th width="15%"><div class="th_div">规格</div></th>
									<th width="10%"><div class="th_div">${message("Goods.price")}</div></th>
									<th width="13%"><div class="th_div">供应价</div></th>
                                    <th width="17%"><div class="th_div">起订量</div></th>
								</tr>
							</thead>
							<tbody>
								[#list page.content as product]
								<tr class="text-l 
								[#if goods.supplierSupplier != null][#if goods.supplierSupplier.status != 'inTheSupply']trColor [/#if][/#if]">
									<td>
										${product.sn}
										
									</td>
									<td>
										<span title="${product.goods.name}">
											${abbreviate(product.goods.name, 80, "...")}
										</span>
										[#list product.goods.validPromotions as promotion]
											<span class="promotion" title="${promotion.title}">${promotion.name}</span>
										[/#list]
										[#if product.goods.source??]
										<span class="distribut_label">分销</span>
									[/#if]
									</td>
									<td>${product.goods.productCategory.name}</td>
									<td>
										[#list product.specificationValues as specificationValue]
											<p class="inPrP">${specificationValue.value}&nbsp;
											[/#list]
									</td>
									<td>
										${currency(product.price, true)}</p>
									</td>
									<td>
										<p class="price_form_p">
											<input type="hidden" name="productId" value="${product.id}"/>
											<span>￥</span>
											<input type="text" id="supplyPrice_${product.id}" class="in_no" name="productPrice" value="${product.turnoverSupplyPrice}" [#if product.goods.supplierSupplier != null][#if product.goods.supplierSupplier.status != 'inTheSupply']readonly="readonly" [/#if][/#if]/>
											<label id="price_${product.id}" class="error"></label>
										</p>
									</td>
                                    <td>
                                        <p class="price_form_p qiding_p">
                                        	[#if product.turnoverMinOrderQuantity == "" || product.turnoverMinOrderQuantity == null]
                                        		<input type="number" name="minOrderQuantity" id="minOrderQuantity_${product.id}" [#if product.minOrderQuantity == ""]  min="1" value="1" minOrderQuantity="1"[#else] min="${product.minOrderQuantity }" value="${product.minOrderQuantity }" minOrderQuantity="${product.minOrderQuantity }"[/#if] class="in_no quantityClass" value="${product.minOrderQuantity }" [#if product.goods.supplierSupplier != null][#if product.goods.supplierSupplier.status != 'inTheSupply']readonly="readonly"  [/#if][/#if]/>
                                        	[#else]
                                        		<input type="number" name="minOrderQuantity" id="minOrderQuantity_${product.id}" [#if product.turnoverMinOrderQuantity == ""]  min="1" value="1" minOrderQuantity="1"[#else][#if product.minOrderQuantity == ""] min="1" minOrderQuantity="1" value="${product.turnoverMinOrderQuantity }"[#else]min="${product.minOrderQuantity }" minOrderQuantity="${product.minOrderQuantity }" value="${product.turnoverMinOrderQuantity }"[/#if] [/#if] class="in_no quantityClass" value="${product.minOrderQuantity }" [#if product.goods.supplierSupplier != null][#if product.goods.supplierSupplier.status != 'inTheSupply']readonly="readonly"  [/#if][/#if]/>
                                        	[/#if]
                                        	<label id="min_${product.id}" class="error"></label>
                                        </p>
									</td>
								</tr>
								[/#list]
								
							</tbody>
						</table>
					</div>
				<input id="submitButton" class="btn radius confir_S" type="button" value="保存" />
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
		<script type="text/javascript">

            var heightObj = $(document.body).height() - 170;
            $(".list_t_tbody").css("height",heightObj);
            $(".table_width").css("width", $(".list_t_tbody").css("width"));


			function isAmount(target){
		      return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(target);
		    }
		
			function isNumber(target){
			  return (/^\d+(\.\d+)?$/).test(target);
			}
			
			$("#listForm").submit(function() {
				var $pageNumber = $("#pageNumber");
				var $searchValue = $("#searchValue");
				if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
					$pageNumber.val("1");
				}
				if ($searchValue.size() > 0 && $searchValue.val() != "" && $searchProperty.val() == "") {
					$searchProperty.val($searchPropertyMenuItem.first().attr("val"));
				}
				$('input.in_no').each(function(i,val){
		            val.removeAttribute('name');
		        })
			});
			
			$(function(){
				[@flash_message /]
				
				$inputForm = $("#listForm");
				
				 $.validator.addClassRules({
			        quantityClass:{
			            required : true ,
			            digits : true
			        }
			     });
				
				 $inputForm.validate({});
				 
				$("#submitButton").click(function(){
					var tempSaveJson = {};
					var i = 0;

					var flag = true;
					var inputs = $("input[name='productId']");
					 $.each(inputs, function (i, input) {

						 var id = $(input).val();
						 
						 $("#min_"+id).html("");
			             var supplyPrice = $("#supplyPrice_"+$(input).val()).val();
			             var minOrderQuantity = $("#minOrderQuantity_"+$(input).val()).val();
			             var min = $("#minOrderQuantity_"+$(input).val()).attr("minOrderQuantity");
			             
			             if(supplyPrice != ""){
			            	 if(!isAmount(supplyPrice)){
				            	$("#price_"+id).html("输入有效价格");
				            	 flag = false;
				             }
			             }
			             if(minOrderQuantity != ""){
			            	 if(minOrderQuantity < min){
			            		 $("#min_"+id).html("不允许小于"+min);
			            		 flag = false;
			            	 }
			             }

			             tempSaveJson['productList['+i+'].id'] = id;
			             tempSaveJson['productList['+i+'].turnoverMinOrderQuantity'] = minOrderQuantity;
	                     tempSaveJson['productList['+i+'].turnoverSupplyPrice'] = supplyPrice;
	                     i++;
			         });

					 if(!flag){
						 return false;
					 }
					 
					 $(this).addClass("in_no_click");
				     $(this).attr("disabled", true);
					 $.ajax({
				       type: "POST",
				       url: "update.jhtml",
				       data: tempSaveJson,
				       dataType: "json",
				       beforeSend: function () {

				       },
				       success: function (message) {
				           $.message(message);
					       	   if (message.type == "success") {
					             setTimeout(function () {
					              $("#listForm").submit();
					             }, 3000);
					           }
					       },
				       error: function (data) {
				           $.message(data);
				           $(this).removeClass("in_no_click");
					       $(this).attr("disabled", false);
				       }
				     });
				});
			});
			
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
			});
		</script>
	</body>
</html>
[/#escape]