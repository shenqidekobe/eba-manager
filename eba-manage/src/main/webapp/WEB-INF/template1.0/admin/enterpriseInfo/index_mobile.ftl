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
    <link rel="stylesheet" href="${base}/resources/mobile/css/setting.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/js/mobiscroll/mobiscroll.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/mobileSelect.css" />
    <title>企业信息</title>
    <style>
        body{background:#fbfbfb;}
        .radius{
            border:none;
            display: inline-block;
            width:1rem;
            color: #fff;
            background-color: #4da1ff;
            font-size: 0.25rem;
            line-height: 0.4rem;
        }
        .companyIntroduce{
            border-bottom: none!important;
        }
    </style>
</head>
<body>
<form id="enterpriseinfo" class="form form-horizontal">
    <input type="hidden" id="id" name="id" value="${supplier.id}" />
    <input type="hidden" id="status" name="status" value="${supplier.status}"/>
    <input type="hidden" id="imagelogo" name="imagelogo" value="${supplier.imagelogo}"/>
    <div class="enterprisePage">
        <div class="enterIfo">
            <div class="enterImg">
                <div class="divTop"></div>
                [#if supplier.imagelogo??]
                	<img src="${supplier.imagelogo}" />
                [/#if]
            </div>
            <input type="text" readonly unselectable="on" onfocus="this.blur()" class="enterName" name="name" id="name" value="${supplier.name}" />
            <div class="inviteCode">邀请码：<input type="text" class="input-text radius" name="inviteCode" id="inviteCode" value="${supplier.inviteCode}" readonly="readonly"  unselectable="on" onfocus="this.blur()" /></div>

        </div>
        <div class="jiangeDiv"></div>

        <ul class="info_ul">

            <li class="li_info li_jiantou">
                <span class="label">所属行业</span>
                <div class="input">
                    <input type="hidden" name="industry" id="industry"/>
                    <input type="hidden"  class="industryCode" value="${supplier.industry}"/>
                    [#if supplier.industry == '']
                    <input type="hidden"  class="industryText" value='' />
                    [#else]
                    <input type="hidden"  class="industryText" value='${message("admin.supplier.industry." + supplier.industry)}' />
                    [/#if]
                    <ul id="goodsType" style="display:none">
                        [#list industrys as industry]
                        <li data-val='${message("admin.supplier.industry." + industry)},${industry}'>${message("admin.supplier.industry." + industry)}</li>
                        [/#list]
                    </ul>

                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">法人姓名</span>
                <div class="input">
                    <input type="text" name="legalPersonName" id="legalPersonName" value="${supplier.legalPersonName}" placeholder="请输入法人姓名" class="info">
                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">联系人</span>
                <div class="input">
                    <input type="text" name="userName" id="userName" value="${supplier.userName}" placeholder="请输入联系人" maxlength="20" class="info">
                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">联系方式</span>
                <div class="input">
                    <input type="text" name="tel" id="tel" value="${supplier.tel}" maxlength="20" placeholder="请输入联系方式" class="info">
                </div>
            </li>
            <li class="li_info li_jiantou">
                <span class="label">公司地址</span>
                <div class="input">
                    <input type="text" class="goodsType_in" value="${supplier.area.fullName}" id="mobileSelect" class="info" placeholder="请选择地址" unselectable="on" onfocus="this.blur()" readonly="readonly">
                    <input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}"  treePath="${(supplier.area.treePath)!}">
                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">详细地址</span>
                <div class="input">
                    <input type="text" id="address" name="address" maxlength="20" autocomplete="off" value="${supplier.address}" placeholder="请输入详细地址" class="info">
                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">电子邮箱</span>
                <div class="input">
                    <input type="text" name="email" id="email" value="${supplier.email}" placeholder="请输入电子邮箱" class="info">
                </div>
            </li>
            <li class="li_info li_bot">
                <span class="label">客服电话</span>
                <div class="input">
                    <input type="text" name="customerServiceTel" id="customerServiceTel" value="${supplier.customerServiceTel}" placeholder="请输入客服电话" class="info">
                </div>
            </li>
            <!--<li class="li_info li_logo">-->
                <!--<span class="label">企业logo</span>-->
                <!--<div class="logoImg">-->
                    <!--<div class="img_box">-->
                        <!--[#if supplier.imagelogo??]-->
                        <!--<img src="${supplier.imagelogo}" />-->
                        <!--[/#if]-->
                    <!--</div>-->
                    <!--<input type="text" style="display:none" id="imagelogo" name="imagelogo" value="${supplier.imagelogo}"/>-->
                    <!--<a id="filePicker" class="file" style="display:block;width:60px;height:60px;margin:30px;opacity: 0" >kjlkjlkjjklkjjj;lkjlkjlkjlkjlkjlkj</a>-->
                <!--</div>-->
                <!--[#if supplier.imagelogo??]-->
                <!--<span class="delImg">删除</span>-->
                <!--[/#if]-->
                <!--<span class='tishi'>(建议上传的logo的尺寸为80*80)</span>-->
            <!--</li>-->
            <li class="li_info li_jiantou companyIntroduce">
                <span class="label">公司介绍</span>
                <div class="input">
                    <textarea placeholder="请输入公司详情描述..." id="companyProfile" name="companyProfile" >${supplier.companyProfile}</textarea>
                </div>
            </li>
            <li class="li_info companyIntroduce Certificates">
                <input type="hidden" id="businessCard" value="${supplier.businessCard}">
                <div class="input">
                    <span class="setCard">工商证件</span>
                </div>
            </li>
        </ul>
    </div>
    <input id="yes" type="button" class="input_s input_B" value="保存">
</form>


    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/common.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <script src="${base}/resources/mobile/js/city.js"></script>
    <script src="${base}/resources/mobile/js/validate/jquery.validate.min.js"></script>
    <script src="${base}/resources/mobile/js/mobiscroll/mobiscroll.js"></script>
    <script src="${base}/resources/mobile/js/mobileSelect.js"></script>
    <script src="${base}/resources/mobile/js/webuploader.js"></script>
    <script>

        $(function() {
            var $enterpriseinfo=$("#enterpriseinfo");
            var businessCards=new Array();

            var industryCode = $(".industryCode").val();
            var industryText = $(".industryText").val();
            $('#goodsType').mobiscroll().treelist({
                theme: 'ios',
                lang: 'zh',
                display: 'bottom',
                placeholder: '请选择所属行业',
                defaultValue:[industryText+','+industryCode],
                onSet: function (event, inst) {
                    var text = event.valueText.split(",");
                    console.log(text);
                    $("#industry").val(text[1]);
                    $("#goodsType_dummy").val(text[0]);
                }

            });

            /*上传图片*/
            var $filePicker = $("#filePicker");
            $filePicker.uploader({
                before:function(file){
                    var fr = new FileReader();
                    var file = file.source.source ;
                    fr.onload = function () {
                        $(".logoImg .img_box").html("<img src='' />");
                        $(".logoImg img").attr("src", fr.result);
                        $(".delImg").show();
                    };
                    fr.readAsDataURL(file);

                },
                complete:function(){
                }
            });

            $(".delImg").on("click", function () {
                $(".logoImg .img_box").html("");
                $(this).css("display", "none");
            });


            $("#industry").val(industryCode);
            $("#goodsType_dummy").val(industryText);


            //显示工商证件
            if($("#businessCard").val() != ''){
                var businessCardImg = eval('(' + $("#businessCard").val() + ')');
                for(var o in businessCardImg){
                    $(".Certificates .input").append("<a href='javaScript:void(0);' target='view_window'><img class='todelImg' src='"+businessCardImg[o].imgUrl+"' height='100px' width='110px' /></a>");
                }
            }else{
                $(".Certificates .input").append("<p style='line-height:36px;color:#586897;'>无</p>");
            }

//            "+businessCardImg[o].imgUrl+"
//            $(".Certificates .input").delegate('a .todelImg','click',function(){
//                $(this).closest("a").remove();
//                var index = $(this).closest("a").index();
//                businessCards.splice(index,1)
//            });


            /*表单验证*/
            $enterpriseinfo.validate({
                rules:{
                    name:{
                        required:true
                    },
                    industry:{
                        required:true
                    },
                    userName:{
                        required:true
                    },
                    tel:{
                        required:true
                    },
                    areaId:{
                        required:true
                    },
                    address:{
                        required:true
                    },
                    email:{
                        required:true,
                        email:true
                    },
                    legalPersonName:{
                        required:true
                    }
                },
                messages:{
                    name:{
                        required:"企业名称不能为空"
                    },
                    industry:{
                        required:"必填"
                    },
                    userName:{
                        required:"用户名不能为空"
                    },
                    tel:{
                        required: "必填"
                        //pattern: "手机号格式不正确"
                    },
                    areaId:{
                        required:"必选"
                    },
                    address:{
                        required: "必填"
                    },
                    email:{
                        required:"邮箱不能为空",
                        email:"请输入正确格式的电子邮件"
                    },
                    legalPersonName:{
                        required:"法人姓名不能为空"
                    }
                }

            });
            $('#yes').on('click',function () {
                if(!$enterpriseinfo.valid()){
                    return false ;
                }
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
                    $.message("warn", "企业名称已注册,请修改企业名称！");
                    return false;
                }
                var businessCardsJson=new Array();
                for(var i=0;i<businessCards.length;i++){
                    businessCardsJson.push({
                        imgUrl:businessCards[i]
                    });
                }
                var tempParams=[];
                tempParams.push(
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
                    {name:"businessCard",value: JSON.stringify(businessCardsJson)},
                    {name:"customerServiceTel",value:$("#customerServiceTel").val()},
                    {name:"legalPersonName",value:$("#legalPersonName").val()}
                );
                $("#yes").attr('disabled',"true");
                $.ajax({
                    url: "updateEnterpriseInfo.jhtml",
                    type: "POST",
                    async: false,
                    data: tempParams,
                    dataType: "json",
                    success: function(data) {
                        if(data.isSuccess){
                            $.message("warn","提交成功！");
                            setTimeout(function () {
                                window.location = '../wxSetting/index.jhtml';
                            }, 2000);
                        }else{
                            $.message("warn","提交失败！");
                            $('#yes').removeAttr("disabled");
                        }
                    }
                });

            });




        })

        //省市县区域选择

        //地区下拉框初始化
        var defaultData=[{"id":"",value:""}];
        //判断 第几个轮子滑动
        var selectIndexArr=[0,0,0];
        var mobileSelect = new MobileSelect({
            trigger: '#mobileSelect',
            wheels: [
                {data: [{}]},
                {data: [{}]},
                {data: defaultData}
            ],
            transitionEnd:function(indexArr, data){
                //判断是哪个滑动的哪个轮子
                for (var i = 0; i < selectIndexArr.length-1; i++) {
                    if (selectIndexArr[i] != indexArr[i]) {
                        selectIndexArr=indexArr;
                        loadArea(i+1,data[i].id);
                        mobileSelect.locatePostion(i+1,0);
                        break;
                    }
                }
            },
            callback:function(indexArr, data){
                for (var i = data.length - 1; i >= 0; i--) {
                    if (data[i].id != "") {
                        $("#areaId").val(data[i].id);
                        break;
                    }
                }
            }
        });
        //加载第一层
        loadArea(0);
        function loadArea(index,parentId) {
            $.ajax({
                type: "GET",
                url: "../common/area.jhtml",
                async: false,
                data: {"parentId": parentId},
                success: function (o) {
                    var data = [];
                    for (var i = 0; i < o.length; i++) {
                        data.push({
                            "id": o[i].value,
                            "value": o[i].name
                        });
                    }
                    if (data.length > 0) {
                        mobileSelect.updateWheel(index, data);
                        loadArea(index + 1, data[0].id);
                    } else {
                        //如果下一层没有数据，则把后面所有层级置空
                        for (var j = index; j < mobileSelect.wheelsData.length; j++) {
                            mobileSelect.updateWheel(j, defaultData);
                        }
                    }
                }
            });
        }
    </script>
</body>
</html>
[/#escape]


















