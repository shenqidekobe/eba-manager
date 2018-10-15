[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.assistantGoods.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.child_page {
            width: calc(100% - 20px);
            height: calc(100% - 20px);
        }
        .trColor{
            background-color:#f9f9f9;
        }
        .table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
        .table-hover tbody tr.trColor td{background-color:#fff;color:#999;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a 
					id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.assistantGoods.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="isMarketable" name="isMarketable" value="${(isMarketable?string("true", "false"))!}" />
			<div class="ch_condition">
				<div class="require_search" id="filterMenu">
					<span class="search">${message("admin.goods.filter")}</span>
					<ul class="check">
						<li name="isMarketable" val="">${message("admin.goods.filter")}</li>
                        <li name="isMarketable"[#if isMarketable?? && isMarketable] class="checked"[/#if] val="true">${message("admin.goods.isMarketable")}</li>
						<li name="isMarketable"[#if isMarketable?? && !isMarketable] class="checked"[/#if] val="false">${message("admin.goods.notMarketable")}</li>
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
						<input type="text" id="searchName" name="searchName" value="${page.searchName}" maxlength="200" placeholder="请输入编号/名称" />
					</div>
				</div>
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					[@shiro.hasPermission name = "admin:assgoods:add"]
						<button type="button" class="op_button add_B" onclick="javascript:location.href='add.jhtml'">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					[@shiro.hasPermission name = "admin:assgoods:delete"]
						<button type="button" class="op_button del_B " id="deleteButtons">${message("admin.common.delete")}<button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>

			<div class="table_con">
				<table class="table table-border table-hover table_width" id="box">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="16%">编号</th>
							<th width="15%">供应商</th>
							<th width="15%">名称</th>
							<th width="10%">分类</th>
							<th width="10%">创建日期</th>
							<th width="10%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div id="listTable" class="list_t_tbody">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox" value="">
									</div>
								</th>
								<th width="16%"><div class="th_div">编号</div></th>
								<th width="15%"><div class="th_div">供应商</div></th>
								<th width="15%"><div class="th_div">名称</div></th>
								<th width="10%"><div class="th_div">分类</div></th>
								<th width="10%"><div class="th_div">创建日期</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as assGoods]
							<tr class="text-l">
								<td><input type="checkbox" value="${assGoods.id}" name="ids"></td>
								<td>
									<span[#if assGoods.isOutOfStock] class="red"[#elseif assGoods.isStockAlert] class="blue"[/#if]>
										${assGoods.sn}
									</span>
								</td>
								<td>${assGoods.bySupplier.name}</td>
								<td>
									<span title="${assGoods.name}">
										${abbreviate(assGoods.name, 80, "...")}
										
									</span>
									[#list assGoods.validPromotions as promotion]
										<span class="promotion" title="${promotion.title}">${promotion.name}</span>
									[/#list]
									
								</td>
								<td>${assGoods.productCategory.name}</td>
								<td>
									<span title="${assGoods.createDate?string("yyyy-MM-dd HH:mm:ss")}">${assGoods.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
								</td>
								<td class="td-manage">
									[@shiro.hasPermission name = "admin:assgoods:edit"]
										<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${assGoods.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
									[/@shiro.hasPermission]
									
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
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			$(".table_width").css("width", $(".list_t_tbody").css("width")); 
			
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
				
				[@flash_message /]
				
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


				/*搜索条件*/
	        	$(".require_search li").on("click",function(){
	        		$(this).parent().siblings(".search").html($(this).html());
	        		//$(this).addClass("li_bag").siblings().removeClass("li_bag");
	        		$(".check").css("display","none");
	        	});
	        	$(".require_search").mouseover(function(){
					$(this).find("ul").css("display","block");
				});
				$(".require_search").mouseout(function(){
					$(this).find("ul").css("display","none");
				});
				
				
				$("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})
				
			});
			$("#deleteButtons").on("click",function(){
				var ids = [];
				$('input[name="ids"]:checked').each(function(){ 
					ids.push($(this).val()); 
				});
				if(ids.length == 0) {
					$.message("warn", "请选择至少一条记录！");
					return false;
				}

				var checkDelete = true;
				$.ajax({
	                url: "checkBatchDelete.jhtml",
	                type: "POST",
	                async: false,
	                data: {ids: ids},
	                dataType: "json",
	                cache: false,
	                success: function(message) {
	                    if (message.type != "success") {
	                        $.message(message);
	                        checkDelete = false;
	                    }
	                }
	            });
	            if (checkDelete) {
    				$.ajax({
    	                url: "delete.jhtml",
    	                type: "POST",
    	                async: false,
    	                data: {ids: ids},
    	                dataType: "json",
    	                cache: false,
    	                success: function(message) {
    	                    if (message.type == "success") {
    	                        $("#listForm").submit(); 
    	                    }else{
    	                    	$.message("删除失败！");
    	                    }
    	                }
    	            });

	            }

			});
        </script>
        
	</body>
</html>
[/#escape]