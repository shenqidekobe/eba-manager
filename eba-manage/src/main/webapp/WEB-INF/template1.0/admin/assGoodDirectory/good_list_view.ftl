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
			<form id="listForm" action="getViewGoodList.jhtml" method="get">
				<input type="hidden" name="assGoodDirectoryId" value="${productCategoryId}">
				<div class="hang_list">
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
				
				<div class="table_box">
					<table class="table table-border table-hover table_width boo">
						<thead>
							<tr class="text-l">
								<th width="10%">${message("Goods.sn")}</th>
								<th width="15%">${message("Goods.name")}</th>
								<th width="10%">${message("Goods.productCategory")}</th>
                                <th width="15%">单位</th>
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
                                    <th width="15%"><div class="th_div">单位</div></th>
								</tr>
							</thead>
							<tbody>
								[#list page.content as goods]
								<tr class="text-l">
									<td>
										<span[#if goods.isOutOfStock] class="red"[#elseif goods.isStockAlert] class="blue"[/#if]>
											${goods.sn}
										</span>
									</td>
									<td>
										<span title="${goods.name}">
											${abbreviate(goods.name, 80, "...")}
										</span>
										[#list goods.validPromotions as promotion]
										<span class="promotion" title="${promotion.title}">${promotion.name}</span>
										[/#list]
										[#if goods.source??]
											<span class="distribut_label">分销</span>
										[/#if]
									</td>
									<td>${goods.productCategory.name}</td>
									<td>
										[#if goods.unit??]
											[#assign unit=goods.unit]
											${message("Goods.unit."+unit)}
										[/#if]
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
		
		</script>
	</body>
</html>
[/#escape]