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

</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 正式供应列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<div class="bar">
			<a href="add.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a>
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
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">企业名称</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="startDate">开始时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="endDate">结束时间</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="department">供应状态</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.content as supplierSupplier]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${supplierSupplier.id}" />
					</td>
					<td>
						${supplierSupplier.bySupplier.name}
					</td>
					<td>
						<span title="${supplierSupplier.startDate?string("yyyy-MM-dd")}">${supplierSupplier.startDate}</span>
					</td>
					<td>
						<span title="${supplierSupplier.endDate?string("yyyy-MM-dd")}">${supplierSupplier.endDate}</span>
					</td>
                    <td>
					${message("SupplierSupplier.Status." + supplierSupplier.status)}
                    </td>
					<td>
						[#if supplierSupplier.status == "inTheSupply"]
                            <a href="javascript:void(0);" onclick="javascript:updateStatus('suspendSupply', ${supplierSupplier.id});">[暂停供应]</a>
                           &nbsp;&nbsp; <a href="view.jhtml?id=${supplierSupplier.id}" >[查看]</a>
                            &nbsp;&nbsp; <a href="edit.jhtml?id=${supplierSupplier.id}" >[编辑]</a>
						[#elseif supplierSupplier.status == "toBeConfirmed"]
                                <a href="view.jhtml?id=${supplierSupplier.id}" >[查看]</a>
                        [#elseif supplierSupplier.status == "rejected"]
                                <a href="view.jhtml?id=${supplierSupplier.id}">[查看]</a>
                        [#elseif supplierSupplier.status == "suspendSupply"]
                        		 <a href="javascript:void(0);" onclick="javascript:updateStatus('inTheSupply', ${supplierSupplier.id});">[开启供应]</a>
                               &nbsp;&nbsp; <a href="view.jhtml?id=${supplierSupplier.id}">[查看]</a>
							   &nbsp;&nbsp; <a href="edit.jhtml?id=${supplierSupplier.id}">[编辑]</a>
						[#elseif supplierSupplier.status == "expired"]
                            <a href="view.jhtml?id=${supplierSupplier.id}">[查看]</a>
                            &nbsp;&nbsp; <a href="edit.jhtml?id=${supplierSupplier.id}">[编辑]</a>
						[#elseif supplierSupplier.status == "willSupply"]
                            <a href="view.jhtml?id=${supplierSupplier.id}">[查看]</a>
                            &nbsp;&nbsp; <a href="edit.jhtml?id=${supplierSupplier.id}">[编辑]</a>
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