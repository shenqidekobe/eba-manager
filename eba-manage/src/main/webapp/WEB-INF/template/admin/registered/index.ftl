[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.login.title")} - Powered By DreamForYou</title>
<meta name="author" content="UTLZ Team" />
<meta name="copyright" content="UTLZ" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/admin/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jsbn.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/prng4.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rng.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/rsa.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/base64.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<style type="text/css">
	.heads{
		margin:10px 30px 15px 20px;
	}
	.context{
		width:25%;
		margin:30px auto;
	}
	table th{
		text-align:right;
		line-height:50px;
	}
</style>
<script type="text/javascript">
$().ready( function() {

	var $username = $("#username");
	var $password = $("#password");
	var $repassword = $("#repassword");
	var $email = $("#email");
	var $companyName = $("#companyName");
	[#if failureMessage??]
		$.message("${failureMessage.type}", "${failureMessage.content}");
	[/#if]

	$("#register").on("click",function(){
		//验证
		if ($username.val() == "") {
			$.message("warn", "请输入用户名");
			return false;
		}else{
			var exists=true;
			$.ajax({
				url: "usernameExists.jhtml",
				async : false,
				type: "GET",
				data: {username:$username.val()},
				dataType: "json",
				success: function(data) {
					exists=data.exists;
				}
			});
			if(exists){
				$.message("warn", "用户名已注册");
				return false;
			}
		}
		if ($password.val() == "") {
			$.message("warn", "请输入密码");
			return false;
		}
		if ($repassword.val() == "") {
			$.message("warn", "请再次输入密码");
			return false;
		}else{
			if($password.val() != $repassword.val()){
				$.message("warn", "两次输入的密码不一致，请重新输入");
				$repassword.val("")
				return false;
			}
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
		if ($companyName.val() == "") {
			$.message("warn", "企业名称不能为空");
			return false;
		}else{
			var exists=true;
			$.ajax({
				url: "getSupplierByName.jhtml",
				async : false,
				type: "GET",
				data: {name:$companyName.val()},
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
		$.ajax({
				url: "register.jhtml",
				type: "POST",
				data: $("#form1").serializeArray(),
				dataType: "json",
				success: function(data) {
					if(data.isRegister){
						$.message("warn","注册成功！跳转至登陆页面...");
						setTimeout(function () {
        					window.location.href="/admin/login.jhtml";
    					}, 3000);
					}
				}
			});
	
	});

});
</script>
</head>
<body>
	<div class="heads">
		<img src="${base}/resources/admin/images/index_logo.png" alt="UTLZ" />
	</div>
	<div class="heads"><span style="font-size:150%;margin-left:30px;">注册帐号</span><a href="../login.jhtml" style="float:right;font-size:130%;">${message("admin.login.login")}</a></div>
	<hr />
	<div class="context">
		<form id="form1">
			<table>
                <tr>
                    <th><span class="requiredField">*</span>企业名称:</th>
                    <td><input type="text" id="companyName" name="name" class="text" maxlength="50" value=""/></td>
                </tr>

				<tr>
					<th><span class="requiredField">*</span>${message("admin.login.username")}:</th>
					<td><input type="text" id="username" name="username" class="text" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<th><span class="requiredField">*</span>登录密码:</th>
					<td><input type="password" id="password" name="password" class="text" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<th><span class="requiredField">*</span>确认密码:</th>
					<td><input type="password" id="repassword" name="repassword" class="text" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<th><span class="requiredField">*</span>email:</th>
					<td><input type="text" id="email" name="email" class="text" maxlength="20" value=""/></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="button"  class="button" id="register" value="确认">
						<input type="button" class="button" value="取消" onclick="location.href='../login.jhtml'" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
[/#escape]