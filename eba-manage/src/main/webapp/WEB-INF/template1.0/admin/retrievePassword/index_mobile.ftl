[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微商平台</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE"> -->
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/login.css" />
</head>
<body>
<input id="redirectUrl" type="hidden"  value="${redirectUrl }"/>
<form method="post" id="listForm" class="form form-horizontal">
    <div class="login-box form1" style="display: block;">
        <h3 class="title">验证手机号</h3>
        <div class="phone">手机号</div>
        <div class="phone-c">
            <input  name="tel" id="tel"  type="text" placeholder="请输入手机号" />
            <div class="phone-de"></div>
        </div>
        <div class="code">验证码</div>
        <div class="code-c">
            <div class="code-input">
                <input name="code" id="code"  type="text" placeholder="请输入验证码" />
            </div>
            <input class="code-sent" type="button" id="sendCode" value="发送验证码" />
        </div>
        <button class="next-b" type="button" id="submitTel">下一步</button>
    </div>
    <div class="login-box form2" style="display: none;">
        <h3 class="title">重置密码</h3>
        <div class="password">新密码</div>
        <div class="password-c1">
            <input name="password" id="password" type="password" placeholder="请设置6-20位登录密码" />
        </div>
        <div class="password">确认密码</div>
        <div class="password-c2">
            <input id="user-pass2" name="repassword" type="password" placeholder="确认新密码" />
        </div>
        <div class="finish-b" id="modify">确 定</div>
    </div>

    <div class="login-box form3" style="display: none;">
        <div class="finish-im"></div>
        <div class="finish-ti">已成功设置新密码</div>
        <button type="button" class="login-b" id="login" onclick="loginUrl();">立即登录</button>
    </div>
</form>

<script src="${base}/resources/mobile/js/jquery.min.js"></script>
<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script src="${base}/resources/admin1.0/js/validate/messages_zh.js"></script>
<script src="${base}/resources/mobile/js/adaptive.js"></script>
<script src="${base}/resources/mobile/js/common.js"></script>
</body>
<script>

    function loginUrl() {
        var redirectUrl = $("#redirectUrl").val();
        if(redirectUrl){
            window.location.href="/shop/login/index.jhtml?redirectUrl=${redirectUrl}";
            return;
        }
        window.location.href="/admin/login.jhtml";
    }

    $(function () {
        //输入验证
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

        //手机号输入
        $('#tel').focus(function () {
            $(this).parents('.phone-c').addClass('border-ch');
        }).blur(function () {
            $(this).parents('.phone-c').removeClass('border-ch');
        });
        //验证码输入
        $('#code').focus(function () {
            $(this).parents('.code-c').addClass('border-ch');
        }).blur(function () {
            $(this).parents('.code-c').removeClass('border-ch');
        });
        //登录按钮颜色变化
        $('#tel,#code').on('keyup',function () {
            //显示删除图标
//            if($('#user-phone').val() != ''){
//                $('.login-box .phone-de').show();
//            }else{
//                $('.login-box .phone-de').hide();
//            }
            if($('#tel').val() != '' || $('#code').val() != ''){
                $('#submitTel').addClass('back-color');
            }
            if($('#tel').val() == '' && $('#code').val() == ''){
                $('#submitTel').removeClass('back-color');
            }
        });
        //点击删除图标删除手机号
        $('.login-box .phone-de').on('click',function () {
            $('#tel').val('');
        });
        //点击发送验证码
        (function () {
            var countdown =60;
            function countDown(obj) {
                if(countdown == 0){
                    $(obj).attr({'disabled':false}).val('发送验证码');
                    countdown =60
                }else{
                    $(obj).attr({'disabled':true}).val('重新发送('+countdown+')');
                    countdown--;
                    setTimeout(function () {
                        countDown(obj);
                    },1000);
                }
            }
            $("#sendCode").on("click",function(){
                var tel = $("#tel").val();
                if(tel == "") {
                    errorInfoFun("手机号不能为空！");
                    return;
                }
                if(!pattern.test(tel)) {
                    errorInfoFun("手机号格式不正确！");
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
                    errorInfoFun("该手机号对应的账号不存在！");
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
                            errorInfoFun("验证码发送成功！");
                            countDown(this);
                            var bindCode_B = _this;
                            clickButton(bindCode_B);
                        }else {
                            errorInfoFun(data.msg);
                        }

                    }
                });

            });
        })();

        //验证码提交
        $("#submitTel").on("click",function(){
            var tel = $("#tel").val();
            var code = $("#code").val();
            var password = $("#password").val();
            if(tel == "") {
                errorInfoFun("手机号不能为空！");
                return false;
            }
            if(!pattern.test(tel)) {
                errorInfoFun("手机号格式不正确！");
                return false;
            }
            if(code == "") {
                errorInfoFun("验证码不能为空！");
                return false;
            }
            $(".form1").css("display","none");
            $(".form2").css("display","block");
//            $.ajax({
//                url: "update.jhtml",
//                type: "POST",
//                async: false,
//                data: {"tel":tel,"password":password,"code":code},
//                dataType: "json",
//                success: function(data) {
//                    if(data.code == "0"){
//                        $(".form1").css("display","none");
//                        $(".form2").css("display","block");
//                    }else{
//                        errorInfoFun(data.msg);
//                        return false;
//                    }
//                }
//            });

        });

        //验证确认
        $("#modify").on("click",function(){
            var tel = $("#tel").val();
            var code = $("#code").val();
            var password = $("#password").val();
            if(tel == "") {
                errorInfoFun("warn","手机号不能为空！");
                return false;
            }
            if(code == "") {
                errorInfoFun("error","验证码不能为空！");
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
                        $(".form1").css("display","none");
                        $(".form2").css("display","none");
                        $(".form3").css("display","block");
                    }else{
                        errorInfoFun(data.msg);
                        return false;
                    }
                }
            });
        });

        //密码输入
        $('#password').focus(function () {
            $(this).parents('.password-c1').addClass('border-ch');
        }).blur(function () {
            $(this).parents('.password-c1').removeClass('border-ch');
        });
        //密码确认输入
        $('#user-pass2').focus(function () {
            $(this).parents('.password-c2').addClass('border-ch');
        }).blur(function () {
            $(this).parents('.password-c2').removeClass('border-ch');
        });
        //确定按钮颜色变化
        $('#password,#user-pass2').on('keyup',function () {
            if($('#password').val() != '' || $('#user-pass2').val() != ''){
                $('#modify').addClass('back-color');
            }
            if($('#password').val() == '' && $('#user-pass2').val() == ''){
                $('#modify').removeClass('back-color');
            }
        });
    });
</script>
</html>
[/#escape]