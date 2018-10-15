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
<script>
    $().ready(function() {
        var $inputForm = $("#inputForm");
        // 表单验证
        $inputForm.validate({
            rules: {
                startDate:{
                    required: true
                },
                endDate:{
                    required: true
                }
            }
        });

		$("#submitButton").click(function(){
            if(!$inputForm.valid()){
                return false ;
            }
            var parsms = $inputForm.serializeArray() ;
            $.ajax({
                type: "GET",
                url: "validDate.jhtml",
                async: false,
                data: parsms,
                dataType: "json",
                success: function (data) {
                    if(!data.isTrue){
                        isTrue=false;
                        $.message({'type':'error' , 'content':'选择的企业该时间段已有供应关系！'});
                    }else{
                        $inputForm.submit();
					}
                }
            });
		});
    });
</script>

</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo;修改正式供应
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
		<input type="hidden" name="id" value="${supplierSupplier.id}"/>
		<table class="input tabContent">
			<tr>
				<th>
					企业:
				</th>
				<td>
                    ${supplierSupplier.bySupplier.name}
				</td>
			</tr>
			<tr>
				[#--/*<td>
					${supplierSupplier.startDate}
					至
					${supplierSupplier.endDate}
				</td>*/--]
                    <th>
                        <span class="requiredField">*</span>供应时间:
                    </th>
                    <td>
                        <input type="text" id="startDate"  name="startDate" class="text Wdate"
                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',readOnly:true, maxDate: '#F{$dp.$D(\'endDate\')}'});" value="${supplierSupplier.startDate}"/>
                        至
                        <input type="text" id="endDate"  name="endDate" class="text Wdate"
                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',readOnly:true, minDate: '#F{$dp.$D(\'startDate\')}'});" value="${supplierSupplier.endDate}"/>
                    </td>
			</tr>

            <tr id="selectPro">
				<th>
					供应商品：
				</th>
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
					<input type="button" class="button" id="submitButton" value="${message("admin.common.submit")}" />
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