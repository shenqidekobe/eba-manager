[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信小程序</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE"> -->
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/login.css" />

    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
    <script src="${base}/resources/mobile/js/validate/messages_zh.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/jsbn.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/prng4.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/rng.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/rsa.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/base64.js"></script>
    <script src="${base}/resources/mobile/js/common.js"></script>
</head>
<body>
    <div class="login-box">请使用PC-Chrome浏览器登录</div>
    <!--
    <div class="login-box">
        <h3 class="title">登录</h3>
        <form id="loginForm" method="post" action="login.jhtml">
            <input type="hidden" name="redirectUrl" value="${redirectUrl}" />
            <input type="hidden" id="enPassword" name="enPassword" />
            [#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("adminLogin")]
            <input type="hidden" name="captchaId" value="${captchaId}" />
            [/#if]
            <div class="user-b">
                <div class="img-c"></div>
                <div class="input-b">
                    <input name="username" id="username" type="text" placeholder="请输入用户名" />
                </div>
            </div>
            <div class="password-b">
                <div class="pass-i"></div>
                <div class="input-b">
                    <input name="password" id="password" type="password" placeholder="请输入密码" />
                </div>
            </div>
            <div class="code-b">
                <div class="code-i">
                    <input  class="input_code" id='captcha' name="captcha" type="text" placeholder="请输入验证码" />
                </div>
                <img id="captchaImage" src="common/captcha.jhtml?captchaId=${captchaId}" alt="" />
            </div>
            <button type="submit" class="login-b" id="login">登录</button>
            <a class="forget-pa" href="retrievePassword/index.jhtml" target="_self">忘记密码？</a>
        </form>
        <div class="logo-b"></div>
    </div>
    -->
</body>
<script>
    $(function () {
        //用户名输入
        $('#user-name').focus(function () {
            $(this).parents('.user-b').addClass('border-ch')
                .find('.img-c').addClass('border-r-ch');
        }).blur(function () {
            $(this).parents('.user-b').removeClass('border-ch')
                .find('.img-c').removeClass('border-r-ch');
        });
        //密码输入
        $('#user-pass').focus(function () {
            $(this).parents('.password-b').addClass('border-ch')
                .find('.pass-i').addClass('border-p-ch');
        }).blur(function () {
            $(this).parents('.password-b').removeClass('border-ch')
                .find('.pass-i').removeClass('border-p-ch');
        });
        //登录按钮颜色变化
        $('#user-name,#user-pass').on('keyup',function () {
            if($('#user-name').val() != '' || $('#user-pass').val() != ''){
                $('#login').addClass('back-color');
            }
            if($('#user-name').val() == '' && $('#user-pass').val() == ''){
                $('#login').removeClass('back-color');
            }
        });

        // 更换验证码
        var $captchaImage = $("#captchaImage");
        $captchaImage.click( function() {
            $captchaImage.attr("src", "common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + new Date().getTime());
        });
        
        //表单提交
        var $loginForm = $("#loginForm");
        var $enPassword = $("#enPassword");
        var $username = $("#username");
        var $password = $("#password");
        var $captcha = $("#captcha");
        var $captchaImage = $("#captchaImage");
        var $isRememberUsername = $("#isRememberUsername");


        [#if failureMessage??]
        errorInfoFun("${failureMessage.content}");
        [/#if]

        $loginForm.submit(function () {
            //提示
            if ($username.val() == "") {
                errorInfoFun('请输入您的用户名');
                return false;
            }
            if ($password.val() == "") {
                errorInfoFun('请输入您的密码');
                return false;
            }
            if ($captcha.val() == "") {
                errorInfoFun('请输入您的验证码');
                return false;
            }

            var rsaKey = new RSAKey();
            rsaKey.setPublic(b64tohex("${modulus}"), b64tohex("${exponent}"));
            var enPassword = hex2b64(rsaKey.encrypt($password.val()));
            $enPassword.val(enPassword);

        });
    });
</script>
</html>
[/#escape]