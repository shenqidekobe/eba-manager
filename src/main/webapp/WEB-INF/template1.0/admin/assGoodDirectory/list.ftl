[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.order.list")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.search_button{margin-left:36px;}
			.form-horizontal .form-label{width:160px;}
			.check-box i{color:#999;font-size:12px;padding-left:20px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>商品目录列表<span>(${message("admin.page.total", page.total)})</span></li>
				</ul>
			</div>
		<form id="listForm" action="list.jhtml" method="get">
			<div class="ch_condition">
				
				<div class="ch_search">
					<img class="search_img" src="${base}/resources/admin1.0/images/sousuo_icon.svg" alt="" />
					<div class="search_input">
						<input type="text" id="theme" name="theme" value="${theme}" maxlength="200" placeholder="请输入主题" />
					</div>
				</div>
				
				<button type="submit" class="search_button">查询</button>
				<div class="ch_operate">
					[@shiro.hasPermission name = "admin:goodDirectory:add"]
					<button type="button" class="op_button add_B" onclick="add();">${message("admin.common.add")}</button>
					[/@shiro.hasPermission]
					[@shiro.hasPermission name = "admin:goodDirectory:delete"]
					<button type="button" class="op_button del_B disabled" id="deleteButton">${message("admin.common.delete")}<button>
					[/@shiro.hasPermission]
					<button type="button" class="op_button update_B" id="refreshButtons" onclick="javascript:window.location.href='list.jhtml'">${message("admin.common.refresh")}</button>
				</div>
			</div>
			
			<div class="table_con">
				<table class="table table-border table-hover table_width">
					<thead>
						<tr class="text-l">
							<th width="4%"><input class="selectAll" type="checkbox" id="selectAll" id="selectAll"></th>
							<th width="30%">${message("AssGoodDirectory.theme")}</th>
							<th width="30%">${message("admin.common.createDate")}</th>
							<th width="36%">${message("admin.common.action")}</th>
						</tr>
					</thead>
				</table>
				<div class="list_t_tbody" id="listTable">
					<table class="table table-border table-hover table_width">
						<thead>
							<tr class="text-l">
								<th width="4%" style="">
									<div class="th_div" style="">
										<input class="selectAll" type="checkbox" id="selectAll">
									</div>
								</th>
								<th width="30%"><div class="th_div">${message("AssGoodDirectory.theme")}</div></th>
								<th width="30%"><div class="th_div">${message("admin.common.createDate")}</div></th>
								<th width="36%"><div class="th_div">${message("admin.common.action")}</div></th>
							</tr>
						</thead>
						<tbody>
						[#list page.content as assGoodDirectory]
							<tr class="text-l">
								<td><input type="checkbox" name="ids" value="${assGoodDirectory.id}"></td>
								<td>
									${assGoodDirectory.theme}
								</td>
								<td><span title="${assGoodDirectory.createDate?string("yyyy-MM-dd HH:mm:ss")}">${assGoodDirectory.createDate?string("yyyy-MM-dd HH:mm:ss")}</span></td>

								<td class="td-manage">
									<a title="${message("admin.common.view")}" href="view.jhtml?id=${assGoodDirectory.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_see"></i></a>
									[@shiro.hasPermission name = "admin:goodDirectory:edit"]
									<a title="${message("admin.common.edit")}" href="edit.jhtml?id=${assGoodDirectory.id}" class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"></i></a>
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        
        <script type="text/javascript">
	        $().ready(function() {
	        	
	        	var $listForm = $("#listForm");
	        	
	        	[@flash_message /]
	        	
	        	/*表格的高度，，随着电脑分倍率的变化而变化*/
				var heightObj = $(document.body).height() - 170;
				$(".list_t_tbody").css("height",heightObj);
				$(".table_width").css("width", $(".list_t_tbody").css("width")); 
	        	
	        	[@flash_message /]
			
			/*表格的高度，，随着电脑分倍率的变化而变化*/
			var heightObj = $(document.body).height() - 170;
			$(".list_t_tbody").css("height",heightObj);
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})
                $("body").click(function(){
                    window.top.$(".show_news").removeClass("show");
                })
	      });
	        
        function add() {
			window.location.href="add.jhtml";
		}


		</script>
	</body>
</html>
[/#escape]