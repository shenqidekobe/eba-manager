[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>企业信息</title>
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
	var $email=$("#email");
	var $companyProfile=$("#companyProfile");
	var $status=$("#status");
	//未认证
	if($status.val() == 'notCertified') {
		$('#div1').css('background','#E0E0E0');
		$('#div1').html("V未认证");
	}
	//已认证
	if($status.val() == 'verified') {
		$('#div1').css('background','#FFFF00');
		$('#div1').html("V已认证");
		$('#name').attr("disabled",true);
		
	}
	//认证中
	if($status.val() == 'certification') {
		$('#div1').css('background','#E0E0E0');
		$('#div1').html("V认证中");
		$('#name').attr("disabled",true);
	}
	//认证未通过
	if($status.val() == 'authenticationFailed') {
		$('#div1').css('background','#E0E0E0');
		$('#div1').html("V认证未通过");
	}
	
	if($status.val() == '' || $status.val() == null) {
		$('#div1').hide();
	}
	// 地区选择
    $("#areaId").lSelect({
        url: "${base}/admin/common/area.jhtml"
    });
    
    if($("#businessCard").val() != ''){
		var businessCards=eval('(' + $("#businessCard").val() + ')');
		for(var o in businessCards){  
	         $("#imgTd").append("<a href='"+businessCards[o].imgUrl+"' target='view_window'><img src='"+businessCards[o].imgUrl+"' height='100px' width='110px' /></a>");
	      } 
	}else{
		$("#imgTd").append("无");
	}
    
    
    $fileimagelogo.uploader();
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
		if ($email.val() != "") {
			var emailReg=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if(!emailReg.test($email.val())){
				$.message("warn", "邮箱格式不正确");
				return false;
			}
		}else{
            $.message("warn", "邮箱不能为空");
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
			{name:"email",value:$("#email").val()},
			{name:"companyProfile",value:$("#companyProfile").val()},
			{name:"businessCard",value: JSON.stringify(businessCardsJson)}
		);
		$("#yes").attr('disabled',"true");
		$.ajax({
				url: "updateEnterpriseInfo.jhtml",
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 企业信息
	</div>
		<input type="hidden" id="id" name="id" value="${supplier.id}" />
		<input type="hidden" id="status" name="status" value="${supplier.status}"/>
		<table class="input">
			<tr>
				<th>
					<span style="font-size:18px"><b>基本信息</b></span>
				</th>
				<td>
					
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>企业名称:
				</th>
				<td>
					<input type="text" id="name" name="name" class="text" maxlength="20" value="${supplier.name}" style="float:left"/>
					<div id="div1" style="background-color:#E0E0E0;width:70px;height:30px;margin-left:20px;float:left;text-align:center"></div>
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
						<input type="text" id="imagelogo" name="imagelogo" class="text" maxlength="200" value="${supplier.imagelogo}"/>
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
						<input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}" treePath="${(supplier.area.treePath)!}"/>
					</span>

					<br />
                    <input type="text" id="address" name="address" placeholder="详细地址" class="text" maxlength="20" autocomplete="off" value="${supplier.address}" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>电子邮箱:
				</th>
				<td>
					<input type="text" id="email" name="email" class="text" value="${supplier.email}"/>
				</td>
			</tr>
			<tr>
				<th>
					公司介绍:
				</th>
				<td>
					<input type="text" id="companyProfile" name="companyProfile" style="width:300px;heigth:400px" class="text" value="${supplier.companyProfile}"/>
				</td>
			</tr>
			<tr>
				<th>
					<span style="font-size:16px"><b>证件信息</b></span>
				</th>
				<td>
					
				</td>
			</tr>
			<tr id="fileTr">
				<th>
					<span class="requiredField">*</span>工商证件:
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
					<input type="button" id="yes" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
</body>
</html>
[/#escape]