[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微商平台</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.form-group{width:650px;}
			.form-control{display:inline-block;margin-right:10px;}
			.xxDialog {top: 120px;}
	        .xxDialog .dialogBottom {
	            width: 100%;
	            position: absolute;
	            bottom: 0;
	        }
	        .dialogContent {
	            height: calc(100% - 80px);
	            overflow: auto;
	            overflow-x: hidden;
	        }
	        .xxDialog .dialogBottom{height:42px;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="content_box">
						<div class="con_title">
							修改密码
						</div>
						<div class="con_form">
							<form id="inputForm" action="update.jhtml" method="POST" class="form-horizontal">
								<div class="form-group">
								    <label class="col-sm-4 control-label">旧密码</label>
								    <div class="col-sm-7">
								     	<input type="password" name="currentPassword" maxlength="20" autocomplete="off" class="form-control" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">新密码</label>
								    <div class="col-sm-7">
								    	<input type="password" id="password" name="password" class="form-control" maxlength="20" autocomplete="new-password" />
								    </div>
								</div>
								
								<div class="form-group">
								    <label class="col-sm-4 control-label">确认密码</label>
								    <div class="col-sm-7">
								    	<input type="password" name="rePassword" class="form-control" maxlength="20" autocomplete="new-password" />
								    </div>
								</div>
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label"></label>
								    <div class="col-sm-7" style="width:700px">
								    	<input type="button" class="input_B submit_In" id="change" value="保存" />
								    	<button type="button" class="cancel_B" onclick="javascript:history.back(-1)">取消</button>
								    </div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		
		<script type="text/javascript">
			$(function(){
				
				var heightObj = $(".page_con .nav").height()-9;
				$(".content_box").css("height",heightObj);
				
				var $inputForm = $("#inputForm");
				
				[@flash_message /]
				
				$.validator.addMethod("requiredTo", 
						function(value, element, param) {
							var parameterValue = $(param).val();
							if ($.trim(parameterValue) == "" || ($.trim(parameterValue) != "" && $.trim(value) != "")) {
								return true;
							} else {
								return false;
							}
						},
						"${message("admin.profile.requiredTo")}"
					);
				
				// 表单验证
				$inputForm.validate({
					rules: {
						currentPassword: {
							required:true,
							requiredTo: "#password",
							remote: {
								url: "check_current_password.jhtml",
								cache: false
							}
						},
						password: {
							required:true,
							minlength: 4
						},
						rePassword: {
							required:true,
							equalTo: "#password"
						}
					},
					messages:{
						currentPassword:{
							remote:"密码不正确"
						},
						password: {
							minlength:"密码不能少于四个字符"
						},
						rePassword: {
							equalTo: "两次密码不一致"
						}
						
					}
				});
				
				$("#change").on("click",function(){
					 if(!$inputForm.valid()){
				            return false ;
				     }
					$.ajax({
						url: "update.jhtml",
						type: "POST",
						data: $("#inputForm").serializeArray(),
						dataType: "json",
						success: function(message) {
							$.message(message);
							if (message.type == "success") {
								setTimeout(function () {
									var url = window.location.pathname;
							  		var param =  getUrlParam(window.location.href);
							  		window.location = '/shop/login/logout.jhtml?redirectUrl='+encodeURIComponent(url+param);
		    					}, 2000);
							}
						}
					});
					
					
				
				});
				
			});
		</script>

	</body>
</html>
[/#escape]