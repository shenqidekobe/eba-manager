[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微信小程序</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			html,body{width:100%;height:100%;}
			label.error{
				float:right;
				font-size:12px;
				margin-right:4px;
				line-height: 34px;
			}
		</style>
	</head>
	<body class="reg10">
		
		<div class="login_header"></div>
		<div class="login_con">
			<div class="login_imgs">
				<img class="login_img1" src="${base}/resources/shop/common/images/denglu-wenzi.png" alt="" />
				<img class="login_img2" src="${base}/resources/shop/common/images/denglu-futu.png" alt="" />
			</div>
			
			<form id="loginForm" action="/admin/login.jhtml" method="post">
				<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
                        <input type="hidden" id="enPassword" name="enPassword" />
					[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
                        <input type="hidden" name="captchaId" value="${captchaId}" />
					[/#if]
				<div class="login_right">
					<div class="login_title">
						<h3>登录</h3>
						<a href="/admin/registered/index.jhtml?type=shop&redirectUrl=/">立即注册</a>
					</div>
					<ul class="login_input">
						<li class="userName_li">
							<span></span><input type="text" class="username"  id="username" name="username" placeholder="请输入用户名" />
						</li>
						<li class="passWord_li">
							<span></span>
							<input type="password" class="password" id="password" 
									name="password" placeholder="请输入密码" />
						</li>
						[#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
						<li class="code_li">
							<input class="code"id='captcha' name="captcha" type="text" placeholder="请输入验证码" />
							<img id="captchaImage" src="/admin/common/captcha.jhtml?captchaId=${captchaId}" alt="" />
						</li>
						[/#if]
						<li class="remenber_li">
							<input type="checkbox" id="isRememberUsername"/><span>记住我</span>
							<a href="/admin/retrievePassword/index.jhtml?type=shop&redirectUrl=/" style="float:right;margin-right:36px;">忘记密码？</a>
						</li>
						<li class="button_li">
							<input type="submit" value="登录" />
						</li>
					</ul>
				</div>
			</form>
		</div>
		<div class="login_footer">
			<p class="l_f_p1">所有方：上海驿游科技技术有限公司 &nbsp;&nbsp;        沪ICP备17037813号-1</p>
            <p class="l_f_p2"></p>
            <div class="l_f_-img">
                <!--<img src="${base}/resources/shop/common/images/05.jpg" alt="">-->
            </div>
		</div>
		
		
		<script src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script src="${base}/resources/shop/common/js/public.js"></script>
		
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jsbn.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/prng4.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/rng.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/rsa.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/base64.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        
		<script type="text/javascript">
		
		$(function(){
			
			var height1 = $(".login_con").height();
			var height2 = $(".login_imgs").height();
			var height3 = $(".login_right").height();
			if(height1>height2){
				$(".login_imgs").css("padding-top",(height1-height2)/2);
			}
			if(height1>height3){
				$(".login_right").css("top",(height1-height3)/2);
			}
			
			var width = $(".login_con").width()/10;
			$(".login_imgs").css("margin-left",width-40);
			$(".login_right").css("right",width);
			
			
			
			$("input").on("focus", function () {
				if($(this).hasClass("username")){
					$(this).parent().addClass("username_focus");
				}else if($(this).hasClass("password")){
					$(this).parent().addClass("password_focus");
				}else if($(this).hasClass("code")){
					$(this).parent().addClass('code_focus');
				}
			});
			
			$("input").on("blur", function () {
				if($(this).hasClass("username")){
					$(this).parent().removeClass("username_focus");
				}else if($(this).hasClass("password")){
					$(this).parent().removeClass("password_focus");
				}else if($(this).hasClass("code")){
					$(this).parent().removeClass("code_focus");
				}
			});
			
			
			
			 
			 
			 if (navigator.userAgent.toLowerCase().indexOf("chrome") >= 0) {
			      	$(window).load(function(){
			         	$('ul input:not(input[type=submit])').each(function(){
			              	var outHtml = this.outerHTML;
			              	$(this).append(outHtml);
			          	});
			      	});
			  	}
			  	
			  
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
                    $captchaImage.attr("src", "/admin/common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
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
			
		})
		
		</script>

	</body>
</html>
[/#escape]
