[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>订单设置</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

    <style>
        body{background:#f9f9f9;color:#333;}
        .form_box{
            overflow: auto;
            overflow-x: hidden;
        }
        .switch{
            display:none;
        }
        label{
            position:relative;
            display: block;
            padding: 1px;
            border-radius: 24px;
            height: 22px;
            margin-bottom: 15px;
            background-color: #eee;
            cursor: pointer;
            vertical-align: top;
            -webkit-user-select: none;
        }
        label:before{
            content: '';
            display: block;
            border-radius: 24px;
            height: 22px;
            background-color: white;
            -webkit-transform: scale(1, 1);
            -webkit-transition: all 0.3s ease;
        }
        label:after{
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -11px;
            margin-left: -11px;
            width: 22px;
            height: 22px;
            border-radius: 22px;
            background-color: white;
            box-shadow: 1px 1px 1px 1px rgba(0,0,0,0.08);
            -webkit-transform: translateX(-9px);
            -webkit-transition: all 0.3s ease;
        }
        .switch:checked~label:after{
            -webkit-transform: translateX(9px);
        }

        .switch:checked~label:before{
            background-color:#4DA1FF;
        }
    </style>
</head>
<body >
<form action="" class="form form-horizontal" method="post">
    <input name="id" id="id" type="hidden" value="${systemSetting.id}">
    <div class="child_page"><!--内容外面的大框-->
        <div class="cus_nav">
            <ul>
                <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
                <li>分销设置</li>
            </ul>
        </div>
        <div class="form_box">

            <div style="padding:30px 10px;">
                <div style="overflow: hidden;padding:15px 0px;">
                    <span style="font-weight:900;float:left;width:100px;padding-left:20px;">分销模式开关</span>
                    <div class="bg_con" style="float:left;width:43px;">
                        <input id="checked_1" type="checkbox" class="switch" name="isDistributionModel" value="true" [#if systemSetting.isDistributionModel] checked [/#if] />
                        <label for="checked_1"></label>
                    </div>
                </div>
                <div style="">
                    <span style="font-weight:900;width:100px;padding-left:20px;">分销模式流程</span>
                    <ul class="step_ul">
                        <li>
                            <span class="step_span"></span>
                            <p>供应商发起供应</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>加盟品牌商确认供应</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>加盟品牌商分销供应商商品</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>加盟品牌商发起分销供应</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>加盟店下单</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <i></i>
                            <p>审核后拆单</p>
                            <div class="ques_div">
                                <span class="ques_span"></span>
                                <div class="ques_show">
                                    拆单后品牌商自有商品生成订货单列表，分销商品生成采购单列表
                                </div>
                            </div>
                            <div style="clear: both"></div>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>处理订单</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>加盟店收货</p>
                            <i></i>
                        </li>
                        <li>
                            <span class="step_span"></span>
                            <p>订单完成</p>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer_submit">
            <input class="btn radius confir_S" type="button" id="save" value="保存">
        </div>
    </div>

</form>
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var formHeight = $(document.body).height() - 100;
            $(".form_box").css("height", formHeight);



            var ulWidth = $(".step_ul").width();
            var liWidth = ulWidth/9-30;

            $(".step_ul li").width(liWidth);
            $(".step_ul li .step_span").css("margin-left",(liWidth-48)/2);
            $(".step_ul li i").css({'width':(liWidth-48+34),"left":(liWidth-48)/2+46});

            $(".ques_div").css("left",liWidth/2+36);

        });

        $("#save").on("click",function(){
            var isDistributionModel;
            if ($("#checked_1").is(':checked')) {
                isDistributionModel=true;
            }else{
                isDistributionModel=false;
            }
            $.post("save.jhtml",{"id":$("#id").val(),"isDistributionModel":isDistributionModel},function(message){
                if(message.type == 'success'){
                    $.message(message);
                    setTimeout(function() {
                        window.parent.location.reload();
                    }, 2000);
                }else{
                    $.message(message);
                }
            });
        });
    </script>
</body>
</html>
[/#escape]