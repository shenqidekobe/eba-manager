[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>微信小程序新用户注册</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<style>
			.reg_input input.error{border:1px solid red;}
			.reg_input input.input_border{border:1px solid #148CF1;}
			.page_header ul{width:110px;}
		</style>
	</head>
	<body>
		<input id="redirectUrl" type="hidden"  value="${redirectUrl }"/>
		<div class="page_header reg_header">
			<img class="logo" src="${base}/resources/admin1.0/images/logo_icon.svg" alt="" />
			<h3>注册账号</h3>
			<ul>
				<!--<li><a href="javascript:;"></a></li>
				<li><a href="javascript:;"></a></li>-->
				<li class="icon_weixin">
					<a href="javascript:;"></a>
					<div class="twoCode">
						<span></span>
						<img src="${base}/resources/admin1.0/images/twoCode.jpg" alt="" />
						<p>微信小程序微信公众号</p>
					</div>
				</li>
				<!--<li><a href="javascript:;"></a></li>-->
			</ul>
			<div class="loginDiv">
				[#if type == 'shop']
			        <a href="javascript:void(0);" onclick="login()">登录</a>
			    [#else] 
			    	<a href="../login.jhtml" class="toLogin">登录</a>
			    [/#if]
			</div>
		</div>
		<div class="reg_content">
			<!--<div class="reg_title">
				<h3>注册账号</h3>
				[#if type == 'shop']
			        <a href="javascript:void(0);" onclick="login()">登录</a>
			    [#else] 
			    	<a href="../login.jhtml" class="toLogin">登录</a>
			    [/#if]
			</div>-->
			<form action="" id="regForm">
				<input type="hidden" name="bootPage" id="bootPage" value="supply"/>
				<ul class='reg_input'>
					<li>
						
						<div class="type_switch">
							<span class="supply_span checked" types="supply">供应商注册</span>	
							<span class="pruchse_span" types="purchase">采购商注册</span>	
							<i class="select_B"></i>
						</div>
					</li>
					<li>
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />企业名称</label>
						<input type="text" name='name' id='companyName' placeholder="请输入企业名称" />
					</li>
					<li>
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />用户名</label>
						<input type="text" name='username' id='username' placeholder="6-30位字符，只能由数字和字母组成" />
					</li>
					<li>
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />登录密码</label>
						<input type="password" name='password' id='password' placeholder="6-20位，由字母、数字或特殊字符组成" />
					</li>
					<li>
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />确认密码</label>
						<input type="password" name='repassword' id='repassword' placeholder="6-20位，由字母，数字或特殊字符组成" />
					</li>
					<li>
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />手机号</label>
						<input type="text" name='bindPhoneNum' id='bindPhoneNum' placeholder="11位数字" />
					</li>
					<li class="phoneCode">
						<label class="liLabel"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />验证码</label>
						<input type="text" name="code" id="code" /><button type="button" class="sendCode" style="cursor: pointer;">发送验证码</button>
					</li>
					<li class="submit">
						<button type="button" id="register">注册</button>
					</li>
				</ul>
			</form>
		</div>
		<!--<div class="reg_footer"></div>-->
		
		
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script src="${base}/resources/admin1.0/js/validate/messages_zh.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script>
		function login(){
			var redirectUrl = $("#redirectUrl").val();
	  		window.location = '/shop?redirectUrl='+redirectUrl;
	  	}
		
		$().ready( function() {
			var $username = $("#username");
			var $password = $("#password");
			var $repassword = $("#repassword");
			var $companyName = $("#companyName");
			[#if failureMessage??]
			$.message("${failureMessage.type}", "${failureMessage.content}");
			[/#if]
			/*当表单获得焦点时，，边框变蓝色*/
			$("input").on("focus",function(){
				$(this).addClass("input_border");
			});
			$("input").on("blur",function(){
				$(this).removeClass("input_border");
			});
			
			
			$(".type_switch span").on("click",function(){
				$(this).siblings(".select_B").animate({left:$(this).css("left")},"slow");
				$(this).addClass("checked").siblings().removeClass("checked");
				$("#bootPage").val($(this).attr("types"));
			});
			
			
			
			/*表单验证*/
			$("#regForm").validate({
				rules:{
					name:{
						required:true,
						remote:{
							url:"getSupplierByName.jhtml",
							type:"GET",
							dataType:"json",
							dataFilter:function(data) {
								var message = JSON.parse(data);
								if(!message.exists) {
									return "true";
								}else{
									return "false";
								}
							}
						}
					},
					username:{
						required:true,
                        //pattern: /^(?![0-9]+$)[0-9a-zA-Z_\u4e00-\u9fa5]+$/,
                        pattern: /^(?![0-9]+$)[0-9a-zA-Z_]+$/,
                        minlength: 6,
                        maxlength: 30,
						remote:{
							url:"usernameExists.jhtml",
							type:"GET",
							dataType:"JSON",
							data:{
								username:function() {
									return $("#username").val();
								}
							},
							dataFilter:function(data){
								var message = JSON.parse(data);
								if(!message.exists) {
									return "true";
								}else{
									return "false";
								}
							}
						}
					},
					password:{
						required:true,
						minlength: 6,
						maxlength: 20,
						pattern: /^(?![0-9]+$)/
					},
					repassword:{
						equalTo:"#password",
						minlength: 6,
						maxlength: 20
					},
					bindPhoneNum:{
						required: true,
		                pattern: /^[1|2][3|4|5|7|8]\d{9}$/,
		                remote: {
		                    url: "checkTel.jhtml",
		                    cache: false
		                }
					},
					code:{
						required: true
					},
					bootPage:{
						required:true
					}
				},
				messages:{
					name:{
						required:"企业名称不能为空",
						remote:"企业名称已认证！"
					},
					username:{
						required:"用户名不能为空",
						remote: "用户名已注册！"
					},
					password:{
						required:"密码不能为空",
						pattern:"密码格式不正确"
					},
					repassword:{
						pattern:"格式不正确",
						equalTo:"两次输入的密码不一致"
					},
					bindPhoneNum:{
						remote: "手机号已存在"
					}	
				}
				
			});
			
			//发送验证码
			$(".sendCode").on("click",function(){
				var bindPhoneNum = $("#bindPhoneNum").val();
        		var pattern = /^1[3|4|5|7|8]\d{9}$/;
        		if(bindPhoneNum == "") {
        			$.message("warn","手机号不能为空！");
        			return;
        		}
        		if(!pattern.test(bindPhoneNum)) {
        			$.message("warn","手机号格式不正确！");
        			return;
        		}
        		//验证手机号是否绑定
        		var exist = true;
        		$.ajax({
        			url: "checkTel.jhtml",
    				type: "GET",
    				async: false,
    				data: {"bindPhoneNum":$("#bindPhoneNum").val()},
    				dataType: "json",
    				success: function(data) {
    					if(data == false) {
    						exist = false;
    					}
    				}
        		});
        		if(!exist) {
        			$.message("warn","该手机号已经绑定过了！");
        			return;
        		}
        		//发送验证码
        		var _this = $(this);
        		$.ajax({
        			url: "sendSms.jhtml",
    				type: "GET",
    				async: false,
    				data: {"tel":$("#bindPhoneNum").val()},
    				dataType: "json",
    				success: function(data) {
    					if(data.code == '0') {
    						$.message("warn","验证码发送成功！");
	                		clickButton(_this);
    					}else {
    						$.message("warn",data.msg);
    					}
    					
    				}
        		});
			});
		
			$("#register").on("click",function(){
				 if(!$("#regForm").valid()){
			            return false ;
			     }
				$.ajax({
					url: "register.jhtml",
					type: "POST",
					data: $("#regForm").serializeArray(),
					dataType: "json",
					success: function(data) {
						if(data.code == '0'){
							$.message("warn","注册成功！跳转至登录页面...");
							setTimeout(function () {
								if('${redirectUrl}'){
									window.location.href="/shop/login/index.jhtml?redirectUrl=${redirectUrl}";
									return;
								}
	        					window.location.href="/admin/login.jhtml";
	    					}, 3000);
						}else {
							$.message("warn",data.msg);
						}
					}
				});
				
				
			
			});
		
		});
			
		
			
		</script>
		
	</body>
</html>
[/#escape]