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
			.require_search,.ch_search,.update_B{border:1px solid #f0f0f0;}
			.hang_list{padding:10px 0 10px 70px;}
			table th{border-top:1px solid #f0f0f0;}
			.update_B{float:right;margin-right:40px;}
			.qiding_p{width:50px;}
			.qiding_p input{width:100%;}
			.require_search .check{max-height: 240px;}
		</style>
	</head>
	<body >
		<div class="" style="width:100%;">
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
				<div class="hang_list">
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
							<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入名称" />
						</div>
					</div>
					<button type="submit" class="search_button">查询</button>
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
				
				<div class="table_box">
					<table class="table table-border table-hover table_width boo">
						<thead>
							<tr class="text-l">
								<th width="3%"><input class="all_checked" type="checkbox" id="selectAll"></th>
								<th width="15%">${message("Goods.sn")}</th>
								<th width="15%">${message("Goods.name")}</th>
								<th width="10%">${message("Goods.productCategory")}</th>
								<th width="15%">规格</th>
								<th width="10%">${message("Goods.price")}</th>
								<th width="12%">供货价</th>
                                <th width="15%">起订量</th>
							</tr>
						</thead>
					</table>
					<div class="list_t_tbody" id="listTable">
						<table class="table table-border table-hover table_width">
							<thead>
								<tr class="text-l">
									<th width="3%" style="">
										<div class="th_div" style="">
											<input class="all_checked" type="checkbox" value="">
										</div>
									</th>
									<th width="15%"><div class="th_div">${message("Goods.sn")}</div></th>
									<th width="15%"><div class="th_div">${message("Goods.name")}</div></th>
									<th width="10%"><div class="th_div">${message("Goods.productCategory")}</div></th>
									<th width="15%"><div class="th_div">规格</div></th>
									<th width="10%"><div class="th_div">${message("Goods.price")}</div></th>
									<th width="12%"><div class="th_div">供货价</div></th>
                                    <th width="15%"><div class="th_div">起订量</div></th>
								</tr>
							</thead>
							<tbody>
								[#list page.content as product]
								<tr class="text-l 
								[#if product.goods.supplierSupplier != null][#if product.goods.supplierSupplier.status != 'inTheSupply']trColor [/#if][/#if]">
									 <td>
									 	<input type="checkbox" name="ids" id="${product.id}" value="${product.id}" name="sub_checked" [#if product.goods.supplierSupplier != null && product.goods.supplierSupplier.status != 'inTheSupply'] onclick="return false"  [#else] onclick="selectGoods(${product.id})" [/#if]/></td>
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
											<span>￥</span>
											<input type="text" class="in_no" id="product_${product.id}" productId="${product.id}" goodsId="${product.goods.id}" name="product_${product.id}" value="${product.price}" oninput="javascript:updatePrice(this);" onpropertychange="javascript:updatePrice(this);"/>
										</p>
									</td>
                                    <td>
										<p class="price_form_p qiding_p" style="width:56px;">
                                        <input type="number" [#if product.minOrderQuantity == ""]  min="1" value="1" minQuantity="1"[#else] min="${product.minOrderQuantity }" value="${product.minOrderQuantity }" minQuantity="${product.minOrderQuantity }" [/#if] id="product_orderQuantity_${product.id}" class="in_no addClassRules" productId="${product.id}" goodsId="${product.goods.id}" name="product_orderQuantity_${product.goods.id}" oninput="javascript:updateOrderQuantity(this);" onpropertychange="javascript:updateOrderQuantity(this);"/>
                                    	<label id="min_${product.id}" class="error"></label>
										</p>
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
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
			
			
			/* function selectGoods(id){
		        var $id = $("#"+id);
		        var parent = $id.parent().parent();
		        //tr下面所有规格价格
		        var inputs = parent.find("input[name='product_"+id+"']");
		        if($id.attr("checked")){
		            var tempJson = {} ;
		            $.each(inputs, function (i, input) {
		                var prodId = $(input).attr("productId") ;
		                var min = $("#product_orderQuantity_"+prodId).attr("minQuantity") ;
		                var minOrderQuantity = $("#product_orderQuantity_"+prodId).val() ;
		                tempJson[$(input).attr("productId")] = {"products":prodId , "supplyPrice":$(input).val() , "minOrderQuantity":minOrderQuantity , "min":min};
		            });
		            window.parent.addNeedProduct(id , tempJson);
		        }
		        if(!$id.attr("checked")){
		            window.parent.delNeedProduct(id);
		        }
		    } */
			
			function selectGoods(id){
		        var $id = $("#"+id);
		        var parent = $id.parent().parent();
		        //tr下面所有规格价格
		        var inputs = parent.find("input[name='product_"+id+"']");
		        if($id.attr("checked")){
		            var tempJson = {} ;
		            $.each(inputs, function (i, input) {
		                var prodId = $(input).attr("productId") ;
		                var min = $("#product_orderQuantity_"+prodId).attr("minQuantity") ;
		                var minOrderQuantity = $("#product_orderQuantity_"+prodId).val() ;
		                tempJson[id] = {"products":id , "supplyPrice":$(input).val() , "minOrderQuantity":minOrderQuantity , "min":min};
		            });
		            window.parent.addNeedProduct(id , tempJson);
		        }
		        if(!$id.attr("checked")){
		            window.parent.delNeedProduct(id);
		        }
		    }
			
			 function updatePrice(thisObj){
			    	//var goodsId = $(thisObj).attr("goodsId") ;
			        var productId = $(thisObj).attr("productId") ;
					var updPrice = $(thisObj).val() ;
			        if($("#"+productId).attr("checked")){
			            window.parent.updatePrice(productId , updPrice);
			        }
			    }
			//起订量修改
            function updateOrderQuantity(thisObj) {
                //var goodsId = $(thisObj).attr("goodsId");
                var productId = $(thisObj).attr("productId");
                var orderQuantity = $(thisObj).val();
                if ($("#" + productId).attr("checked")) {
                    window.parent.updateOrderQuantity(productId, orderQuantity);
                }
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
			
			$().ready(function() {
				var $listForm = $("#listForm");
				var $filterMenuItem = $("#filterMenu li");
				var cacheProducts = window.parent.getCacheFromChild();

				$listForm.validate({
		        });
				
				 $.validator.addClassRules({
					  quantityClass:{
			                required : true ,
			                digits : true
			            }
			     });
				
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
			 	
				
				//全选事件
				$("#selectAll").click(function(){
			        $("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
			        	if($(this).closest("tr").hasClass("trColor")){
			        		$(this).prop("checked",false);
			        	}else{
			        		var id = $(this).val();
					        selectGoods(id);
			        	}
			        });
				});
				
				
				
				
			});
		
		</script>
	</body>
</html>
[/#escape]