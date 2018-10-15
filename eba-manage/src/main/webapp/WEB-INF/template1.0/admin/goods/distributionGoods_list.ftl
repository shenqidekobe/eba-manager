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
		</style>
	</head>
	<body >
		<div class="" style="width:100%;">
			<form id="listForm" action="distributionGoodList.jhtml" method="get">
				<input type="hidden" id="needId" name="needId" value="${needId}" />
				<input type="hidden" id="supplierSupplierId" name="supplierSupplierId" value="${supplierSupplier.id}" />
                <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}" />
				<div class="hang_list">
					<div id="filterMenu" class="require_search">
						<span class="search">所有分类</span>						
						<ul class="check" name="productCategoryId">
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
						<div class="search_input">
							<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="请输入商品编号" />
							<input type="hidden" name="searchProperty" value="sn">
						</div>
					</div>
					<button type="submit" class="search_button">查询</button>
					<button type="button" class="op_button update_B" id="refreshButton">${message("admin.common.refresh")}</button>
				</div>
				
				<div class="table_box">
				<table class="table table-border table-hover table_width boo">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="10%">${message("Goods.sn")}</th>
							<th width="20%">${message("Goods.name")}</th>
							<th width="10%">${message("Goods.productCategory")}</th>
							<th width="10%">${message("Goods.price")}</th>
							<th width="10%">操作</th>
							
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
								<th width="10%"><div class="th_div">${message("Goods.sn")}</div></th>
								<th width="20%"><div class="th_div">${message("Goods.name")}</div></th>
								<th width="10%"><div class="th_div">${message("Goods.productCategory")}</div></th>
								<th width="10%"><div class="th_div">${message("Goods.price")}</div></th>
								<th width="10%"><div class="th_div">操作</div></th>
								
							</tr>
						</thead>
						<tbody>
							[#list page.content as goods]
							<tr class="text-l">
								<td><input type="checkbox" name="ids" id="${goods.id}" value="${goods.id}" onclick="selectGoods(${goods.id})"/></td>
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
								<td>${goods.productCategory.name}</td>
								<td>${currency(goods.price, true)}</td>
								<td>
									<a title="${message("admin.common.view")}" href="javascript:;" class="ml-5 toView" style="text-decoration:none" goodsId="${goods.id}"><i class="operation_icon icon_see"></i></a>
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
		<script type="text/javascript">
		
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 140;
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
			
			
			
			function selectGoods(id){
		        var $id = $("#"+id);
		        var parent = $id.parent().parent();
		        //tr下面所有规格价格
		        var $inputs = parent.find("input[name=ids]");
		        if($id.attr("checked")){
		        	window.parent.addGoods($inputs.val());
		        }
		        if(!$id.attr("checked")){
		        	var $tr=$id.parent().parent();
		        	var $good=$tr.find("input[name=ids]");
		        	window.parent.delGoods($good.val());
		        }
		    }

            $(".toView").on("click",function(){

                var id= $(this).attr("goodsId");
                window.parent.goodsView(id);

            });

			$().ready(function() {
				var $listForm = $("#listForm");
				var $filterMenuItem = $("#filterMenu li");
				
				[@flash_message /]
				
				var cacheProducts = window.parent.getCacheFromChild();
				$("input[name='ids']").each(function () {//遍历根据name属性取到的所有值
					var $id=$(this);
					var $tr=$id.parent().parent();
		        	var $goods=$tr.find("input[name=ids]");
		        	$goods.each(function(){
		        		//var productid= $(this).attr("productid");
		        		var goodId = $goods.val();
		            	$.each(cacheProducts, function (i, cacheProduct) {
							if(cacheProduct.goodId == goodId){
								$id.attr("checked" , true);
								$tr.addClass("trColor");
								$goods.prop("disabled","disabled");
							}
						});
		            });
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
			            var id = $(this).val();
			            selectGoods(id);
			        });
				});

			});



		</script>
	</body>
</html>
[/#escape]