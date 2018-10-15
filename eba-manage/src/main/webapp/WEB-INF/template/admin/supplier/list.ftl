[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.admin.list")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $filterMenu = $("#filterMenu");
	var $filterMenuItem = $("#filterMenu li");
	var $listForm = $("#listForm");
	// 筛选菜单
	$filterMenu.hover(
		function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		}
	);
	
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
	

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 企业认证审核<span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
	<input type="hidden" id="status" name="status" value="${status}" />
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div id="filterMenu" class="dropdownMenu">
					<a href="javascript:;" class="button">
						认证状态<span class="arrow">&nbsp;</span>
					</a>
					<ul>
						<li name="status" val="" value="">所有企业</li>
						<li name="status" val="notCertified" value="notCertified">未认证</li>
						<li name="status" val="certification" value="certification">认证中</li>
						<li name="status" val="authenticationFailed" value="authenticationFailed">认证失败</li>
						<li name="status" val="verified" value="verified">已认证</li>
					</ul>
			</div>
				<div id="pageSizeMenu" class="dropdownMenu">
					<a href="javascript:;" class="button">
						${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
					</a>
					<ul>
						<li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
						<li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
						<li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
						<li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
					</ul>
				</div>
			</div>
			
			<div id="searchPropertyMenu" class="dropdownMenu">
				<div class="search">
					<span class="arrow">&nbsp;</span>
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li[#if page.searchProperty == "username"] class="current"[/#if] val="username">${message("Admin.username")}</li>
					<li[#if page.searchProperty == "email"] class="current"[/#if] val="email">${message("Admin.email")}</li>
					<li[#if page.searchProperty == "name"] class="current"[/#if] val="name">${message("Admin.name")}</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th>
					<a href="javascript:;" class="sort" name="name">供应商</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">地址</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="userName">联系人</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="tel">手机号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">${message("admin.common.createDate")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="status">认证状态</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.content as supplier]
				<tr>
					<td>
						${supplier.name}
					</td>
					<td>
						${supplier.area.fullName} ${supplier.address}
					</td>
					<td>
						${supplier.userName}
					</td>
					<td>
						${supplier.tel}
					</td>
					<td>
						<span title="${supplier.createDate?string("yyyy-MM-dd HH:mm:ss")}">${supplier.createDate}</span>
					</td>
					<td>
						[#if supplier.status == 'notCertified' ]
							<span style="color:red">企业未认证</span>
						[/#if]
						[#if supplier.status == 'authenticationFailed' ]
						 	<span  style="color:red">认证失败</span>
						[/#if]
						[#if supplier.status == 'certification' ]
						 	<span  style="color:blue">认证中</span>
						[/#if]
						[#if supplier.status == 'verified' ]
						 	<span  style="color:green">企业已认证</span>
						[/#if]
					</td>
					<td>
						[#if supplier.status == 'certification' ]
						 	<a href="review.jhtml?id=${supplier.id}">【审核】</a>
						[/#if]
						[#if supplier.status != 'certification' ]
						 	<a href="look.jhtml?id=${supplier.id}">【查看】</a>
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>
[/#escape]