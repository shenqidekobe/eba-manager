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

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.need.list")} <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="distributionList.jhtml" method="get">
		<input type="hidden" name="id" value="${id}">
		<div class="bar">
			<div class="buttonGroup">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
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
					<li[#if page.searchProperty == "userName"] class="current"[/#if] val="userName">店长姓名</li>
					<li[#if page.searchProperty == "name"] class="current"[/#if] val="name">收货点名称</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">${message("admin.need.name")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="area">${message("admin.need.address")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="userName">${message("admin.need.userName")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="tel">${message("admin.need.tel")}</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="createDate">分配状态</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.content as need]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${admin.id}" />
					</td>
					<td>
						${need.name}
					</td>
					<td>
						${need.area.fullName} ${need.address}
					</td>
					<td>
						${need.userName}
					</td>
					<td>
						${need.tel}
					</td>
					<td>
						[#if need.status == 'on']
							已分配
						[#elseif need.status == 'off']
							未分配
						[/#if]
					</td>
					<td>
						<a href="distributionView.jhtml?id=${id}&needId=${need.id}">[商品分配]</a>
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