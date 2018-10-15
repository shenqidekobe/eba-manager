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
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
	.deleteImg{
		position: relative;
	}
</style>
<script type="text/javascript">
$().ready(function() {
	var $image=$("#image");
	var $businessCard=$("#businessCard");
	var $fileTr=$("#fileTr");
	var $filePicker = $("#filePicker");
	var $fileimagelogo = $("#fileimagelogo");
	var businessCards=new Array();
	var $name=$("#name");
	var $id=$("#id");
	// 地区选择
    $("#areaId").lSelect({
        url: "${base}/admin/common/area.jhtml"
    });
    $fileimagelogo.uploader();
    
    if($("#image").val() != ''){
		var businessCardsTemp=eval('(' + $("#image").val() + ')');
		$("#imgTr").remove();
		var imgTr="<tr id='imgTr'><th></th><td>";
		for(var o in businessCardsTemp){
			businessCards.push(businessCardsTemp[o].imgUrl);
			imgTr+="<span id='imgTd'><a href='"+businessCardsTemp[o].imgUrl+"' target='view_window'><img src='"+businessCardsTemp[o].imgUrl+"' height='100px' width='110px' /></a>";
			imgTr+="<a class='deleteImg' style='left:-10px; top:-45px;' href='javascript:void(0);'><img src='${base}/resources/admin/images/delete.png' width='10px' height='11px' alt='删除' /></a></span>"
		}
		imgTr+="</td></tr>"
		$fileTr.after(imgTr);
		$(".deleteImg").on("click",function(){
			var $this=$(this);
			var $imgTd=$(this).parent();
			businessCards.splice($imgTd.index(),1);
			if(businessCards.length == 0){
				$("#imgTr").remove();
			}else{
				$imgTd.remove();
			}
		});
	}
    
	$filePicker.uploader({complete:function(){
		businessCards.push($image.val());
		$("#imgTr").remove();
		var imgTr="<tr id='imgTr'><th></th><td>";
		for(var i=0;i<businessCards.length;i++){
			imgTr+="<span id='imgTd'><a href='"+businessCards[i]+"' target='view_window'><img src='"+businessCards[i]+"' height='100px' width='110px' /></a>";
			imgTr+="<a class='deleteImg' style='left:-10px; top:-45px;' href='javascript:void(0);'><img src='${base}/resources/admin/images/delete.png' width='10px' height='11px' alt='删除' /></a></span>"
		}
		imgTr+="</td></tr>"
		$fileTr.after(imgTr);
		$(".deleteImg").on("click",function(){
			var $this=$(this);
			var $imgTd=$(this).parent();
			businessCards.splice($imgTd.index(),1);
			if(businessCards.length == 0){
				$("#imgTr").remove();
			}else{
				$imgTd.remove();
			}
		});
	},before:function(){
		if(businessCards.length >= 3){
			$.message("warn", "最多只能上传3张图片！");
			return false;
		}
	}});
	$("#yes").on("click",function(){
		//验证
		if ($("#name").val() == "") {
			$.message("warn", "请输入企业名称");
			return false;
		}else{
			var exists=true;
			var params=[];
			params.push(
				{name:"id",value:$("#id").val()},
				{name:"name",value:$("#name").val()}
			);
			$.ajax({
				url: "nameExists.jhtml",
				async : false,
				type: "GET",
				data: params,
				dataType: "json",
				success: function(data) {
					exists=data.exists;
				}
			});
			if(exists){
				$.message("warn", "企业名称已注册");
				return false;
			}
		}
		if ($("#industry").val() == "") {
			$.message("warn", "请选择所属行业");
			return false;
		}
		if ($("#userName").val() == "") {
			$.message("warn", "请输入联系人");
			return false;
		}
		if ($("#tel").val() == "") {
			$.message("warn", "请输入手机号");
			return false;
		}else{
			var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
			if(!myreg.test($("#tel").val())){
				$.message("warn", "手机号格式不正确");
				return false;
			}
		}
		if ($("#areaId").val() == "") {
			$.message("warn", "请选择地区");
			return false;
		}
		if(businessCards.length == 0){
			$.message("warn", "请上传证件图片");
			return false;
		}
		var businessCardsJson=new Array();
		for(var i=0;i<businessCards.length;i++){
			businessCardsJson.push({
				imgUrl:businessCards[i]
			});
		}
		var params=[];
		params.push(
			{name:"id",value:$("#id").val()},
			{name:"name",value:$("#name").val()},
			{name:"industry",value:$("#industry").val()},
			{name:"imagelogo",value:$("#imagelogo").val()},
			{name:"userName",value:$("#userName").val()},
			{name:"tel",value:$("#tel").val()},
			{name:"areaId",value:$("#areaId").val()},
			{name:"address",value:$("#address").val()},
			{name:"businessCard",value: JSON.stringify(businessCardsJson)}
		);
		$("#yes").attr('disabled',"true");
		$.ajax({
				url: "update.jhtml",
				type: "POST",
				async: false,
				data: params,
				dataType: "json",
				success: function(data) {
					if(data.isSuccess){
						$.message("warn","提交成功！");
						setTimeout(function () {
        					parent.location.reload();
    					}, 2000);
					}else{
						$.message("warn","提交失败！");
						$('#yes').removeAttr("disabled");
					}
				}
			});
	});
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 企业认证
	</div>
		<input type="hidden" id="id" name="id" value="${supplier.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>企业名称:
				</th>
				<td>
					<input type="text" id="name" name="name" class="text" maxlength="20" value="${supplier.name}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>所属行业:
				</th>
				<td>
					<select id="industry" name="industry">
						[#list industrys as industry]
							<option value="${industry}"[#if industry == supplier.industry] selected="selected"[/#if]>
								${message("admin.supplier.industry." + industry)}
							</option>
						[/#list]
					</select>
				</td>
				
			</tr>
			<tr>
				<th>
					企业logo:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" id="imagelogo" name="imagelogo" class="text" maxlength="200" value="${supplier.imagelogo}" />
						<a href="javascript:;" id="fileimagelogo" class="button">上传logo</a>
						<span class="silver">(上传的logo尺寸为80*80)</span>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>联系人:
				</th>
				<td>
					<input type="text" id="userName" name="userName" class="text" maxlength="20" value="${supplier.userName}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>手机号:
				</th>
				<td>
					<input type="text" id="tel" name="tel" class="text" maxlength="20" value="${supplier.tel}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>地址:
				</th>
				<td>
					<span class="fieldSet">
						<input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}" treePath="${(supplier.area.treePath)!}" />
					</span>

					<br />
                    <input type="text" id="address" name="address" placeholder="详细地址" class="text" maxlength="20" autocomplete="off" value="${supplier.address}"/>
				</td>
			</tr>
			<tr id="fileTr">
				<th>
					<span class="requiredField">*</span>企业证件:
				</th>
				<td id="imgTd">
					<span class="fieldSet">
						<input type="text" id="image" name="image" class="text" maxlength="200" style="display:none" value="${supplier.businessCard}"/>
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
						<span class="silver">请上传企业三证，若三证合一只上传一张即可</span>
					</span>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="button" id="yes" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
</body>
</html>
[/#escape]