
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信小程序新用户注册</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <style>
        .form-horizontal .form-label{margin-top:10px;}
        .loginDiv a{
            display:block;
            width:90px;
            height:34px;
            border:1px solid #fff;
            line-height: 34px;
            text-align: center;
            font-size:14px;
            border-radius: 4px;
            margin:12px 40px;
        }
        label.error{
        	color:red
        }
    </style>
</head>
<body>
    <input id="redirectUrl" type="hidden"  value="${redirectUrl }"/>
    <div class="page_header reg_header">
        <img class="logo" src="${base}/resources/admin1.0/images/logo_icon.svg" alt="" />
        <div class="loginDiv">
            [#if type == 'shop']
            <a href="javascript:void(0);" onclick="login()">登录</a>
            [#else]
            <a href="../login.jhtml" class="toLogin">登录</a>
            [/#if]
        </div>
    </div>
    <form action="" id="listForm" class="form form-horizontal">
        <div class="forgetStep">
            <div class="forStep1 cssI">
                <i></i><i></i><i></i><i></i><i></i>
                <i></i><i></i><i></i><i></i><i></i>
                <span></span>
                <i></i><i></i><i></i><i></i><i></i>
            </div>
            <div class="forStep2">
                <i></i><i></i><i></i><i></i><i></i>
                <span></span>
                <i></i><i></i><i></i><i></i><i></i>
            </div>
            <div class="forStep3">
                <i></i><i></i><i></i><i></i><i></i>
            </div>
        </div>
        <div class="forgetCon">
            <div class="forCon1" style="display:block">
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">输入手机号</label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" placeholder="" name="tel" id="tel" />
                    </div>
                </div>
                <div class="row cl code">
                    <label class="form-label col-xs-4 col-sm-3">验证码</label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" placeholder="" name="code" id="code" />
                        <button type="button" id="sendCode">获取验证码</button>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3"></label>
                    <div class="formControls col-xs-8 col-sm-7">
                       <button type="button" class="get_B" id="submitTel">提交</button>
                    </div>
                </div>
            </div>
            <div class="forCon2" style="display:none">
	                <div class="row cl">
	                    <label class="form-label col-xs-4 col-sm-3">输入密码</label>
	                    <div class="formControls col-xs-8 col-sm-7">
	                        <input type="password" class="input-text radius" placeholder="" name="password" id="password" />
	                    </div>
	                </div>
	                <div class="row cl">
	                    <label class="form-label col-xs-4 col-sm-3">验证密码</label>
	                    <div class="formControls col-xs-8 col-sm-7">
	                        <input type="password" class="input-text radius" placeholder="" name="repassword" />
	                    </div>
	                </div>
	                <div class="row cl">
	                    <label class="form-label col-xs-4 col-sm-3"></label>
	                    <div class="formControls col-xs-8 col-sm-7">
	                        <button type="button" class="get_B" id="modify">提交</button>
	                    </div>
	                </div>
            </div>
            <div class="forCon3" style="display:none">
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3"></label>
                    <div class="formControls col-xs-8 col-sm-7 success">
                        <img src="${base}/resources/admin1.0/images/chenggong.svg" alt="">
                        <p>恭喜您成功设置新密码</p>
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3"></label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <button type="button" class="get_B" onclick="loginUrl();">立即登录</button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script src="${base}/resources/admin1.0/js/validate/messages_zh.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
	<script type="text/javascript">
	function login(){
		var redirectUrl = $("#redirectUrl").val();
  		window.location = '/shop?redirectUrl='+redirectUrl;
  	}
	
	function loginUrl() {
		var redirectUrl = $("#redirectUrl").val();
		if(redirectUrl){
			window.location.href="/shop/login/index.jhtml?redirectUrl=${redirectUrl}";
			return;
		}
		window.location.href="/admin/login.jhtml";
	}
	
	$(function(){
		[@flash_message /]
		var pattern = /^1[3|4|5|7|8]\d{9}$/;
		
		$("#listForm").validate({
			rules:{
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
				}
			},
			messages:{
				password:{
					required:"密码不能为空",
					pattern:"密码格式不正确"
				},
				repassword:{
					pattern:"格式不正确",
					equalTo:"两次输入的密码不一致"
				}
			}
			
		});
		
		$("#sendCode").on("click",function(){
			var tel = $("#tel").val();
			if(tel == "") {
				$.message("warn","手机号不能为空！");
				return;
			}
			if(!pattern.test(tel)) {
				$.message("warn","手机号格式不正确！");
				return;
			}
			//验证账号是否存在
			var exist = true;
			$.ajax({
				url: "checkUserwhetherexist.jhtml",
				type: "GET",
				async: false,
				data: {"tel":tel},
				dataType: "json",
				success: function(data) {
					if(data == false) {
						exist = false;
					}
				}
			});
			if(!exist) {
				$.message("warn","该手机号对应的账号不存在！");
				return;
			}
			//发送验证码
			var _this = $(this);
			$.ajax({
				url: "sendSms.jhtml",
				type: "GET",
				async: false,
				data: {"tel":tel},
				dataType: "json",
				success: function(data) {
					if(data.code == '0') {
						$.message("warn","验证码发送成功！");
						var bindCode_B = _this;
	            		clickButton(bindCode_B);
					}else {
						$.message("warn",data.msg);
					}
					
				}
			});
		});
		
		$("#submitTel").on("click",function(){
			var tel = $("#tel").val();
			var code = $("#code").val();
			if(tel == "") {
				$.message("warn","手机号不能为空！");
				return false;
			}
			if(!pattern.test(tel)) {
				$.message("warn","手机号格式不正确！");
				return false;
			}
	    	if(code == "") {
	    		$.message("error","验证码不能为空！");
	    		return false;
	    	}
	    	$(".forCon1").css("display","none");
			$(".forCon2").css("display","block");
	    	/* $.ajax({
				url: "checkSms.jhtml",
				type: "GET",
				async: false,
				data: {"tel":tel,"code":code},
				dataType: "json",
				success: function(data) {
					if(data.code == "0"){
						$(".forCon1").css("display","none");
						$(".forCon2").css("display","block");
	                }else{
	                    $.message("error", data.msg);
	                    return false;
	                }
				}
			}); */
		});
		
		$("#modify").on("click",function(){
			var tel = $("#tel").val();
			var code = $("#code").val();
			var password = $("#password").val();
			if(tel == "") {
				$.message("warn","手机号不能为空！");
				return false;
			}
			if(code == "") {
	    		$.message("error","验证码不能为空！");
	    		return false;
	    	}
			if(!$("#listForm").valid()){
	            return false ;
	     	}
			$.ajax({
				url: "update.jhtml",
				type: "POST",
				async: false,
				data: {"tel":tel,"password":password,"code":code},
				dataType: "json",
				success: function(data) {
					if(data.code == "0"){
						$(".forCon1").css("display","none");
						$(".forCon2").css("display","none");
						$(".forCon3").css("display","block");
	                }else{
	                    $.message("error", data.msg);
	                    return false;
	                }
				}
			});
		});

		
		
		
	});
	
	</script>
</body>
</html>