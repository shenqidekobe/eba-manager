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
<style type="text/css">
.roles label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]

    $("#description").editor();
    // 地区选择
    $("#areaId").lSelect({
        url: "${base}/admin/common/area.jhtml"
    });
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: {
				required: true
			},
			userName: {
				required: true
			},
			tel: {
				required: true,
                pattern: /^1[3|4|5|7|8]\d{9}$/
			},
			area: {
				required: true
			}
		},
		messages: {
			username: {
				pattern: "${message("admin.validate.illegal")}",
				remote: "${message("admin.validate.exist")}"
			}
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.need.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">

		<table class="input tabContent">
			<tr>
				<th>
					<span class="requiredField">*</span>供应商:
				</th>
				<td>
					<input type="text" name="name" class="text" maxlength="20" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>地址:
				</th>
				<td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" />
					</span>

					<br />
                    <input type="text" name="address" placeholder="详细地址" class="text" maxlength="20" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>联系人:
				</th>
				<td>
					<input type="text" name="userName" class="text" maxlength="20" autocomplete="off" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>手机号:
				</th>
				<td>
					<input type="text" name="tel" class="text" maxlength="200" />
				</td>
			</tr>

            [#--<tr>
                <th>
                    ${message("admin.need.description")}:
                </th>
                <td>
                    <textarea id="description" name="description" class="editor" style="width: 100%;"></textarea>
                </td>
            </tr>--]

		</table>
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]