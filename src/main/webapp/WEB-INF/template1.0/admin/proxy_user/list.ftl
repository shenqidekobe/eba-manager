[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>代理列表 - Powered By microBusiness</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}

		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>代理列表<span>(${message("admin.page.total", totalCount)}， 一级代理：${no1Count}人,&nbsp;&nbsp;二级代理：${no2Count}人,&nbsp;&nbsp;三级代理：${no3Count}人)</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">
			
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					
					<div class="search_input">
						<input type="text" id="searchName" name="searchName" value="${searchName}" maxlength="200" placeholder="请输入代理名称" />
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				
				<div class="ch_operate">
					[@shiro.hasPermission name = "admin:proxyUser:add"]
						<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					<button type="button" id="refreshButtons" class="op_button update_B" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="20%">代理名称</th>
							<th width="20%"><div class="th_div">身份证号</div></th>
							<th width="15%"><div class="th_div">联系方式</div></th>
							<th width="15%"><div class="th_div">直接下级人数</div></th>
							<th width="15%"><div class="th_div">下属团队总人数</div></th>
							<!--<th width="10%">${message("admin.common.order")}</th>-->
							<th width="30%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody">
					<table class="table table-border table-hover table_width" id="listTable">
						<thead>
							<tr class="text-l">
								<th width="20%"><div class="th_div">${message("proxyUser.name")}</div></th>
								<th width="20%"><div class="th_div">身份证号</div></th>
								<th width="15%"><div class="th_div">联系方式</div></th>
								<th width="15%"><div class="th_div">直接下级人数</div></th>
								<th width="15%"><div class="th_div">下属团队总人数</div></th>
								<!--<th width="10%"><div class="th_div">${message("admin.common.order")}</div></th>-->
								<th width="30%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list proxyUserTree as proxyUser]
							<tr class="text-l [#if proxyUser.supplierSupplier != null][#if proxyUser.supplierSupplier.status == 'expired']trColor [/#if][/#if]">
								<td>
									<span style="margin-left: ${proxyUser.grade * 20}px;">
										${proxyUser.name}
									</span>
								</td>
								<td>
									<span style="margin-left: ${proxyUser.grade * 20}px;">
										${proxyUser.idenNo}
									</span>
								</td>
								<td>
									<span style="margin-left: ${proxyUser.grade * 20}px;">
										${proxyUser.tel}
									</span>
								</td>
								<td>
									<span style="margin-left: ${proxyUser.grade * 20}px;">
										${proxyUser.children.size()}人
									</span>
								</td>
								<td>
									<span style="margin-left: ${proxyUser.grade * 20}px;">
										${proxyUser.groupCount}人
									</span>
								</td>
								<!--<td>
									<span>${proxyUser.order}</span>
								</td>-->
								<td class="td-manage">
									<!-- <a title="${message("admin.common.view")}" href="${base}${proxyUser.path}" target="_blank" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a> -->
										[@shiro.hasPermission name = "admin:proxyUser:edit"]
											<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${proxyUser.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a> 
										[/@shiro.hasPermission]
										[@shiro.hasPermission name = "admin:proxyUser:delete"]
											<a title="${message("admin.common.delete")}" href="javascript:;" class="delete" val="${proxyUser.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_del"></i></a>
										[/@shiro.hasPermission]
								</td>
							</tr>
						[/#list]	
						</tbody>
					</table>
				</div>
			</div>
			 [#--[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
			[/@pagination] --]
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
			$().ready(function() {
				
				var $delete = $("#listTable a.delete");
				var $refreshButton = $("#refreshButton");
				
				[@flash_message /]
				
				// 删除
				$delete.click(function() {
					var $this = $(this);
					$.dialog({
						type: "warn",
						content: "${message("admin.dialog.deleteConfirm")}",
						onOk: function() {
							$.ajax({
								url: "delete.jhtml",
								type: "POST",
								data: {id: $this.attr("val")},
								dataType: "json",
								cache: false,
								success: function(message) {
									$.message(message);
									if (message.type == "success") {
										$this.closest("tr").remove();
									}
								}
							});
						}
					});
					return false;
				});
				
			
	
			});
		
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			
			function add() {
				window.location.href="add.jhtml";
			}
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
		
        </script>
	</body>
</html>
[/#escape]