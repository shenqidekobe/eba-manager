[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <meta content="telephone=no,email=no" name="format-detection">
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/goods.css" />
    <title>账号设置</title>
    <style>
        body{background:#fbfbfb;}
        .li_info{
            position: relative;
        }
        .img-1,.img-2,.img-3{
            width: 0.4rem;
            height: 0.4rem;
            font-size: 0.22rem;
            border-radius: 0.06rem;
            position: absolute;
            right: 0.7rem;
            top: 0.3rem;
            text-align: center;
            display: none;
        }
        .li_bot::after{
            left:10%;
        }
    </style>
</head>
<body>
    <form action="update.jhtml" method="post" id="inputForm">
        <div class="addGoods">

            <ul class="info_ul">
                <li class="li_info li_bot">
                    <span class="label">用户名</span>
                    <div class="input">
                        <input type="text" unselectable="on" onfocus="this.blur()" readonly="readonly" class="info" value="${admin.username}">
                    </div>
                </li>
                <li class="li_info li_bot">
                    <span class="label">姓名</span>
                    <div class="input">
                        <input type="text" unselectable="on" onfocus="this.blur()" readonly="readonly" class="info" value="${admin.name}">
                    </div>
                </li>
                <li class="li_info">
                    <span class="label">部门</span>
                    <div class="input">
                        <input type="text" unselectable="on" onfocus="this.blur()" readonly="readonly" class="info" value="${admin.department.name}">
                    </div>
                </li>
                <!--<li class="li_info li_bot">-->
                    <!--<span class="label">当前密码</span>-->
                    <!--<img class="img-1" src="${base}/resources/mobile/images/xuanze-b.png" alt="图片">-->
                    <!--<div class="input current_div">-->
                        <!--<input type="password" placeholder="请输入当前密码" class="info" id="currentPassword" name="currentPassword" maxlength="20" autocomplete="off" ／>-->
                    <!--</div>-->
                <!--</li>-->
                <!--<li class="li_info li_bot">-->
                    <!--<span class="label">新密码</span>-->
                    <!--<img class="img-2" src="${base}/resources/mobile/images/xuanze-b.png" alt="图片">-->
                    <!--<div class="input password_div">-->
                        <!--<input type="password" placeholder="请输入新的密码" class="info" id="password" name="password" maxlength="20" autocomplete="off" ／>-->
                    <!--</div>-->
                <!--</li>-->
                <!--<li class="li_info rePassword_li">-->
                    <!--<span class="label">确认密码</span>-->
                    <!--<img class="img-3" src="${base}/resources/mobile/images/xuanze-b.png" alt="图片">-->
                    <!--<div class="input">-->
                        <!--<input type="password" placeholder="请再次输入新的密码" class="info" id="rePassword" name="rePassword" maxlength="20" autocomplete="off" ／>-->
                    <!--</div>-->
                <!--</li>-->
            </ul>
        </div>

        <!--<input type="button" class="input_s input_B" id="yes" value="保存">-->
    </form>


    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/common.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/jquery.form.js"></script>
    <script>

        $(function() {

            var $inputForm = $("#inputForm");
            //清楚输入框的内容
            $('#currentPassword').val('');
            $('#password').val('');
            $('#rePassword').val('');

            $("#currentPassword").on("blur",function(){
                var _this = this;
                rePass(_this,{"currentPassword":$(this).val()});

            });


            $("#password").on("blur",function(){
                var _this = this,
                    value = $(_this).val(),
                    rgRex = /^(?![0-9]+$)/,
                    parent = $(_this).parent();
                if(value == ""){
                    $('.img-2').hide();
                    return false;
                }else if(value.length<6||value.length>20){
                    $('.img-2').hide();
                    errorInfoFun("密码长度6～20位！");
                    if(!parent.hasClass("errorInfo")){
                        parent.addClass("errorInfo");
                    }
                    return false;
                }else if(!rgRex.test(value)){
                    $('.img-2').hide();
                    errorInfoFun("密码格式不能纯数字");
                    if(!parent.hasClass("errorInfo")){
                        parent.addClass("errorInfo");
                    }
                    return false;
                }else{
                    $('.img-2').show();
                    parent.removeClass("errorInfo");
                }

            });
            $("#rePassword").on("blur",function(){
                var _this = this,
                    password = $("#password").val(),
                    repassword = $(_this).val(),
                    parent = $(_this).closest("li");
                if(password != repassword){
                    $('.img-3').hide();
                    errorInfoFun("两次密码不一致");
//                    parent.addClass("errorBorder");
                }else if(password ==''){
                    $('.img-3').hide();
                }else{
                    $('.img-3').show();
//                    parent.removeClass("errorBorder");
                }
            });


            $("#yes").on("click",function(){
                var currentPassword = $("#currentPassword").val(),
                    password = $("#password").val(),
                    rePassword = $("#rePassword").val();
                var tempBool =true;
                if(currentPassword == ""){
                    errorInfoFun("请输入当前密码");
                    tempBool =false;
                    return false;
                }
//                tempBool = rePass($("#currentPassword"),{"currentPassword":currentPassword});
                if(password == ""){
                    errorInfoFun("新密码不能为空");
                    tempBool =false;
                    return false;
                }
                if(rePassword == ""){
                    errorInfoFun("确认密码不能为空");
                    tempBool =false;
                    return false;
                }
                if(password != rePassword){
                    errorInfoFun("确认密码与新密码不相同");
                    tempBool =false;
                    return false;
                }
                if($(".current_div").hasClass("errorInfo") || $(".password_div").hasClass("errorInfo") || $(".rePassword_li").hasClass("errorBorder")){
                    errorInfoFun("密码输入格式不正确");
                    tempBool =false;
                    return false;
                }
                if(tempBool){
                    var params=$inputForm.serializeArray();
                    $.ajax({
                        type: "POST",
                        url: "update.jhtml",
                        data: params,
                        dataType: "json",
                        //contentType:'application/json;charset=utf-8', //设置请求头信息
                        beforeSend: function () {

                        },
                        success: function (message) {
                            // $.message(message);
                            errorInfoFun(message.content);
                            if (message.type == "success") {
                                setTimeout(function () {
                                    window.location = '../wxSetting/index.jhtml';
                                }, 1000);
                            }
                        },
                        error: function (data) {
                            $.message("修改失败");
                        }
                    });
                }

            });

            //判断当前密码是否正确
            function rePass(_this,param) {
                $.ajax({
                    url: "check_current_password.jhtml",
                    type: "get",
                    data: param,
                    async:true,
                    dataType: "json",
                    success: function(data) {
                        console.log(data);
                        if(data){
                            $(_this).parent().removeClass("errorInfo");
                            $('.img-1').show();
                            return true;
                        }else{
                            errorInfoFun("密码不正确");
                            $('.img-1').hide();
                            $(_this).parent().addClass("errorInfo");
                            return false;
                        }
                    }
                });
            }

        })
    </script>
</body>
</html>
[/#escape]


















