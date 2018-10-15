[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.profile.edit")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {
	if($("#businessCard").val() != ''){
		var businessCards=eval('(' + $("#businessCard").val() + ')');
		for(var o in businessCards){  
	         $("#imgTd").append("<a href='"+businessCards[o].imgUrl+"' target='view_window'><img src='"+businessCards[o].imgUrl+"' height='100px' width='110px' /></a>");
	      } 
	}else{
		$("#imgTd").append("无");
	}
	
	if($("#imagelogo").val() != '') {
		var imagelogo=$("#imagelogo").val();
		$("#logoId").append("<a href='"+imagelogo+"' target='view_window'><img src='"+imagelogo+"' height='100px' width='110px' /></a>");
	}else{
		$("#logoId").append("无");
	}
	
      $("#review").click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
			[@compress single_line = true]
                content: '<form id="form1" action="update.jhtml" method="get"><table id="moreTable" class="moreTable" style="border-spacing:10px;border-spacing:10px;">
					<input type="hidden" id="id" name="id" value="${supplier.id}" />
					<input type="hidden" name="adminId" value="${admin.id}" />
				<tr>
                    <th style="text-align: center;">
						审核:
                	<\/th>
            		<td>
            			<input type="radio" name="status" value="verified" checked> 通过
            			<input type="radio" name="status" value="authenticationFailed"> 不通过
           		 	<\/td>
           		 <tr\>
           		 <tr style="">
                    <th style="text-align: center;">
						原因:
                	<\/th>
            		<td>
            			<textarea name="reasons" cols="40" rows="5" value=""><\/textarea>
           		 	<\/td>
           		 <tr\>
            <\/table><\/form>'
			[/@compress]
			,
            width: 470,
				onOk: function() {
					$("#form1").submit();
				}
			});
		});
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 企业认证审核 
	</div>
		<table class="input">
			<tr>
				<th>
					用户名:
				</th>
				<td>
					${admin.username}
				</td>
			</tr>
			<tr>
				<th>
					企业名称:
				</th>
				<td>
					${supplier.name}
				</td>
			</tr>
			<tr>
				<th>
					所属行业:
				</th>
				<td>
					${message("admin.supplier.industry." + supplier.industry)}
				</td>
			</tr>
			<tr>
				<th>
					联系人:
				</th>
				<td>
					${supplier.userName}
				</td>
			</tr>
			<tr>
				<th>
					手机号:
				</th>
				<td>
					${supplier.tel}
				</td>
			</tr>
			<tr>
				<th>
					地址:
				</th>
				<td>
					${supplier.area.fullName} ${supplier.address}
				</td>
			</tr>
			<tr id="fileTr">
				<th>
					企业logo:
				</th>
				<td id="logoId">
					<input type="hidden" id="imagelogo" value="${supplier.imagelogo}">
				</td>
			</tr>
			<tr id="fileTr">
				<th>
					企业证件:
				</th>
				<td id="imgTd">
					<input type="hidden" id="businessCard" value="${supplier.businessCard}">
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="review" class="button" value="审核" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
</body>
</html>
[/#escape]