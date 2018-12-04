[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>微商平台订单系统</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<!-- <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE"> -->
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script src="${base}/resources/admin1.0/js/validate/messages_zh.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/jsbn.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/prng4.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/rng.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/rsa.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/base64.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/Particleground.js"></script>

		<style>
		
		 body{height:100%;background:#f0ffff;overflow:hidden;}
		
		.login_area input.input_border{border-bottom:1px solid #148CF1;}
		/*input:-webkit-autofill, textarea:-webkit-autofill, select:-webkit-autofill{
			background:red !important;
		}*/
			
		 canvas{z-index:0;position:absolute;}
		.black_overlay{  display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color: black;  z-index:1001;  -moz-opacity: 0.8;  opacity:.80;  filter: alpha(opacity=80);  }  
		.white_content {  display: none;  position: absolute;  top: 25%;  left: 35%;  width: 25%;  height: 15%;  padding: 16px;  border: 2 solid black;  background-color: white;  z-index:1002;  overflow: auto;  } 
		</style>
	</head>
	
	<body>
		<div class="loginX">
			<div class="page_header login_top">
				<ul>
					<!--<li class=""><a href="javascript:;"></a></li>
					<li class=""><a href="javascript:;"></a></li>-->
					<li class="icon_weixin">
						<a href="javascript:;"></a>
						<div class="twoCode">
							<span></span>
							<img src="${base}/resources/admin1.0/images/twoCode.jpg" alt="" />
							<p>微商平台微信公众号</p>
						</div>
					</li>
					<!--<li class=""><a href="javascript:;"></a></li>-->
				</ul>
			</div>
			<div class="login_content">
				<div class="con_left">
				<!--	<img src="${base}/resources/admin1.0/images/logo_icon.svg" alt="" />-->
				</div>
				<div class="con_right" style="">
				<!--<a href="registered/index.jhtml" class="toReg">注册账号</a>-->
					<form id="loginForm" action="login.jhtml" method="post">
						<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
                        <input type="hidden" id="enPassword" name="enPassword" />
					[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
                        <input type="hidden" name="captchaId" value="${captchaId}" />
					[/#if]
						<ul class="login_area">
							<li class="username">
								<input type="text" id="username" name="username" placeholder="请输入用户名/手机号" />
							</li>
							<li class="possword">
								<input type="password" id="password" name="password" placeholder="请输入密码" autocomplete="off" />
							</li>
							[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
							<li class="code">
								<input class="input_code" id='captcha' name="captcha" type="text" placeholder="请输入验证码" />
								<img id="captchaImage" src="common/captcha.jhtml?captchaId=${captchaId}" alt="" />
							</li>
							[/#if]
							<li class="remember">
								<input id="isRememberUsername" class='check_1' type="checkbox" value="true" /><span>记住用户名</span>
								<a href="retrievePassword/index.jhtml" style="float:right;">忘记密码？</a>
							</li>
							<li class="button"><button type="submit">登录</button></li>
						</ul>	
					</form>
				</div>
			</div>
		</div>
		
		<script>
            $().ready( function() {
            
              //粒子背景特效
			  $('body').particleground({
			    dotColor: '#4DA1FF',
			    lineColor: '#5cbdaa'
			  });

                //跳出iframe
                if (top.location != self.location){
                    top.location=self.location;
                    return false;
				}

            	/*if (navigator.userAgent.toLowerCase().indexOf("chrome") >= 0) {
			      	$(window).load(function(){
			         	$('ul input:not(input[type=submit])').each(function(){
			              	var outHtml = this.outerHTML;
			              	$(this).append(outHtml);
			          	});
			      	});
			  	}*/
            	
            	
            	
            	
                /*当表单获得焦点时，，边框变蓝色*/
                $("input").on("focus", function () {
                    $(this).addClass("input_border");
                	/*$(window).load(function(){
			         	$('ul input:not(input[type=submit])').each(function(){
			              	var outHtml = this.outerHTML;
			              	$(this).append(outHtml);
			          	});
			      	});*/
                });
                $("input").on("blur", function () {
                    $(this).removeClass("input_border");
                    /*$(window).load(function(){
			         	$('ul input:not(input[type=submit])').each(function(){
			              	var outHtml = this.outerHTML;
			              	$(this).append(outHtml);
			          	});
			      	});*/
                    
                });

                var $loginForm = $("#loginForm");
                var $enPassword = $("#enPassword");
                var $username = $("#username");
                var $password = $("#password");
                var $captcha = $("#captcha");
                var $captchaImage = $("#captchaImage");
                var $isRememberUsername = $("#isRememberUsername");


				[#if failureMessage??]
                    $.message("${failureMessage.type}", "${failureMessage.content}");
				[/#if]

                // 记住用户名
                if(getCookie("adminUsername") != null) {
                    $isRememberUsername.prop("checked", true);
                    $username.val(getCookie("adminUsername"));
                    $password.focus();
                } else {
                    $isRememberUsername.prop("checked", false);
                    $username.focus();
                }

                // 更换验证码
                $captchaImage.click( function() {
                    $captchaImage.attr("src", "common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
                });

                // 表单验证、记住用户名
                $loginForm.submit( function() {
                    if ($username.val() == "") {
                        $.message("warn", "${message("admin.login.usernameRequired")}");
                        return false;
                    }
                    if ($password.val() == "") {
                        $.message("warn", "${message("admin.login.passwordRequired")}");
                        return false;
                    }
                    if ($captcha.val() == "") {
                        $.message("warn", "${message("admin.login.captchaRequired")}");
                        return false;
                    }

                    if ($isRememberUsername.prop("checked")) {
                        addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
                    } else {
                        removeCookie("adminUsername");
                    }

                    var rsaKey = new RSAKey();
                    rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
                    var enPassword = hex2b64(rsaKey.encrypt($password.val()));
                    $enPassword.val(enPassword);

                });

            });
		</script>
	</body>
</html>
[/#escape]