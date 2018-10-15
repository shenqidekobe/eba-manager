[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<style type="text/css">
.roles label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">
function updateStatus(type , id){
	var content = "您确认此条供应信息？" ;
	if("rejected" == type){
        content = "您决绝这条供应信息？" ;
	}
    $.dialog({
        type: "warn",
        content: content,
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
                            window.location.href="list.jhtml";
                        }, 2000);
                    }else{

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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 供应详情
	</div>
	<form id="inputForm" action="save.jhtml" method="post">

		<table class="input tabContent">
			<tr>
				<td>
                    ${supplierSupplier.supplier.name}
				</td>
			</tr>
			<tr>
				<td>
					供应时间:
					${supplierSupplier.startDate}
					至
					${supplierSupplier.endDate}
				</td>
			</tr>

            <tr id="selectPro">
                <td>
                    <div style="width:1024px;height:auto;">
                        <iframe marginheight="80px" width="100%" frameborder="no" border="0" height="500px" src="viewGoods.jhtml?supplierSupplierId=${supplierSupplier.id}"></iframe>
                    </div>
                </td>
            </tr>

		</table>
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					[#if type == 'confirm']
						<input type="button" class="button" id="submitButton" onclick="javascript:updateStatus('inTheSupply', ${supplierSupplier.id});" value="${message("admin.common.submit")}" />
						<input type="button" class="button" id="submitButton" onclick="javascript:updateStatus('rejected', ${supplierSupplier.id});" value="拒绝" />
					[/#if]
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
		<div id="selectProducts" [#--style="display: none"--]>

		</div>
	</form>
</body>
</html>
[/#escape]