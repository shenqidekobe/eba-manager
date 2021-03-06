[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.supplyType{display:block;width:50px;height:22px;text-align: center;line-height: 22px;border-radius: 5px;}
			.inTheSupply{background:#d2f0e6;color:#00b45a;}/*供应中*/
			.expired{background:#dce6eb;color:#7882e0}/*已过期*/
			.suspendSupply{background:#ffe5cd;color:#db5e03}/*暂停中*/
			/* .yfp{background:#d2f0e6;color:#00b45a}已分配 */
			.rejected{background:#ffd6d6; color:#ff5454;}/*已拒绝*/
			.toBeConfirmed{background:#c6f5ff; color:#41bcd6;}/*待确认*/
			.willSupply{background:#faf0b4;color:#be965a}/*未开始*/
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>企业供应列表 <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<input type="hidden" id="status" name="status" value="${status}">
			<div class="ch_condition">

                <div class="require_search" id="filterMenu">
					<span class="search">${message("admin.order.filter")}</span>
					<ul class="check">
						<li name="status" val="">所有状态</li>
						<li name="status"[#if "toBeConfirmed" == status] class="checked"[/#if] val="toBeConfirmed">待确认</li>
                        <li name="status"[#if "inTheSupply" == status] class="checked"[/#if] val="inTheSupply">已确认</li>
                        <li name="status"[#if "rejected" == status] class="checked"[/#if] val="rejected">已拒绝</li>
                        <li name="status"[#if "suspendSupply" == status] class="checked"[/#if] val="suspendSupply">已暂停</li>
                        <li name="status"[#if "expired" == status] class="checked"[/#if] val="expired">已失效</li>
					</ul>
				</div>
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入客户名称" />

					</div>
				</div>
				<button type="submit" class="search_button">查询</button>


                <div class="ch_operate">
                	[@shiro.hasPermission name = "admin:formalSupply:add"]
						<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="10%">客户名称</th>
							<th width="10%">开始时间</th>
							<th width="10%">结束时间</th>
							<th width="10%">供应状态</th>
							<th width="10%">供应批次</th>
							<th width="8%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="all_checked" type="checkbox" value="${supplierSupplier.id}" name="ids">
									</div>
								</th>
								<th width="10%"><div class="th_div">客户名称</div></th>
								<th width="10%"><div class="th_div">开始时间</div></th>
								<th width="10%"><div class="th_div">结束时间</div></th>
								<th width="10%"><div class="th_div">供应状态</div></th>
								<th width="10%"><div class="th_div">供应批次</div></th>
								<th width="8%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
							[#list page.content as supplierSupplier]
							<tr class="text-l">
								<td><input type="checkbox" value="${supplierSupplier.id}" name="ids"></td>
								<td>${supplierSupplier.bySupplier.name}</td>
								<td>
									<span title="${supplierSupplier.startDate?string("yyyy-MM-dd")}">${supplierSupplier.startDate}</span>
								</td>
								<td>
									<span title="${supplierSupplier.endDate?string("yyyy-MM-dd")}">${supplierSupplier.endDate}</span>
								</td>
								<td>
									<span class="supplyType ${supplierSupplier.status}">${message("SupplierSupplier.Status." + supplierSupplier.status)}</span>
								</td>
								<td>
									[#if supplierSupplier.supplyBatch > 0]
										第${supplierSupplier.supplyBatch}批
									[/#if]
								</td>
								<td class="td-manage">
									[#if supplierSupplier.status == "inTheSupply"]
										[@shiro.hasPermission name = "admin:formalSupply:updateStatus"]
											<a title="暂停供应" href="javascript:;" onclick="javascript:updateStatus('suspendSupply', ${supplierSupplier.id});" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_stop"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:view"]
											<a title="查看" href="view.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:edit"]
											<a title="编辑" href="edit.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a> 
										[/@shiro.hasPermission]
									[#elseif supplierSupplier.status == "toBeConfirmed"]
										[@shiro.hasPermission name = "admin:formalSupply:view"]
											<a title="查看" href="view.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:edit"]
											<a title="编辑" href="editUnconfirmed.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
										[/@shiro.hasPermission]
									[#elseif supplierSupplier.status == "rejected"]
										[@shiro.hasPermission name = "admin:formalSupply:view"]
											<a title="查看" href="view.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:delete"]
											<a title="删除" href="javascript:void(0);" onclick="deleteFormal(${supplierSupplier.id});"  class="ml-5" style="text-decoration:none"><i class="operation_icon icon_del"></i></a>
										[/@shiro.hasPermission]
									[#elseif supplierSupplier.status == "suspendSupply"]
										[@shiro.hasPermission name = "admin:formalSupply:updateStatus"]
											<a title="开启供应" href="javascript:;" onclick="javascript:updateStatus('inTheSupply', ${supplierSupplier.id});" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_start"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:view"]
											<a title="查看" href="view.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:edit"]
											<a title="编辑" href="edit.jhtml?id=${supplierSupplier.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
										[/@shiro.hasPermission]
									[#elseif supplierSupplier.status == "expired"]
										[@shiro.hasPermission name = "admin:formalSupply:view"]
											<a title="查看" href="view.jhtml?id=${supplierSupplier.id}"  class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:formalSupply:delete"]
											<a title="删除" href="javascript:void(0);" onclick="deleteFormal(${supplierSupplier.id});"  class="ml-5" style="text-decoration:none"><i class="operation_icon icon_del"></i></a>
										[/@shiro.hasPermission]
									[/#if]
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
			
			function add() {
				window.location.href="add.jhtml";
			}
			
			$().ready(function() {
				var $listForm = $("#listForm");
	        	var $filterMenu = $("#filterMenu");
	        	var $filterMenuItem = $("#filterMenu li");
				[@flash_message /]
				
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
	        	
	        	/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				$(".table_width").css("width", $(".list_t_tbody").css("width")); 
	        	
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

			});
			function updateStatus(type , id){
			    $.dialog({
			        type: "warn",
			        content: "是否要修改供应状态",
			        onOk: function() {
			            $.ajax({
			                url: "updateStatus.jhtml",
			                type: "GET",
			                data: {status:type , id:id},
			                dataType: "json",
			                cache: false,
			                success: function(message) {
			                    $.message(message);
			                    if (message.type == "success") {
			                        setTimeout(function () {
			                            location.reload(true);
			                        }, 2000);
			                    }
			                }
			            });
			        }
			    });
			}
			
			function deleteFormal(id){
				$.dialog({
					type: "warn",
					content: message("admin.dialog.deleteConfirm"),
					ok: message("admin.dialog.ok"),
					cancel: message("admin.dialog.cancel"),
					onOk: function() {
						$.ajax({
							url: "delete.jhtml",
							type: "POST",
							data: {"id":id},
							dataType: "json",
							cache: false,
							success: function(message) {
								$.message(message);
								if (message.type == "success") {
										location.reload();
								}
							}
						});
					}
				});
			}
			
			
        </script>
		
	</body>
</html>
[/#escape]