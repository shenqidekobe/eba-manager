[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.specification.list")} - Powered By DreamForYou</title>
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
					<li>${message("admin.specification.list")} <span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">
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
					[@shiro.hasPermission name = "admin:specification:add"]
						<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					[@shiro.hasPermission name = "admin:specification:delete"]
						<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="all_checked" type="checkbox" id="selectAll"></th>
							<th width="20%">${message("Specification.name")}</th>
							<th width="15%">${message("Specification.productCategory")}</th>
							<th width="35%">${message("Specification.options")}</th>
							<th width="10%">${message("admin.common.order")}</th>
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
										<input class="all_checked" type="checkbox">
									</div>
								</th>
								<th width="20%"><div class="th_div">${message("Specification.name")}</div></th>
								<th width="15%"><div class="th_div">${message("Specification.productCategory")}</div></th>
								<th width="35%"><div class="th_div">${message("Specification.options")}</div></th>
								<th width="10%"><div class="th_div">${message("admin.common.order")}</div></th>
								<th width="8%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as specification]
							<tr class="text-l [#if specification.productCategory.supplierSupplier != null][#if specification.productCategory.supplierSupplier.status == 'expired']trColor [/#if][/#if]">
								<td>
									[#if specification.source == null]
										<input type="checkbox" value="${specification.id}" name="ids">
									[/#if]
								</td>
								<td>${specification.name}
									[#if specification.source??]
										<span class="distribut_label">分销</span>
									[/#if]
								</td>
								<td>${specification.productCategory.name}</td>
								<td>
									[#list specification.options as option]
										${option}
										[#if option_index > 5]
											...[#break /]
										[/#if]
									[/#list]
								</td>
								<td>${specification.order}</td>
								<td class="td-manage">
								[#if specification.source == null]
									[@shiro.hasPermission name = "admin:specification:edit"]
										<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${specification.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a> 
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

            	[@flash_message /]
            	
            	
            	$("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})

            });
			
		</script>
	</body>
</html>
[/#escape]