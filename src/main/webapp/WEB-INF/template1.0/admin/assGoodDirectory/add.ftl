[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${message("admin.admin.add")} - Powered By DreamForYou</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#f9f9f9;}
        .pag_div{width:45%;float:left;}
        .col-sm-7{width:72%;}
        th{border-top:1px solid #f0f0f0;}
        .form_box{
            overflow: auto;
            overflow-x: hidden;
        }
        .footer_submit{
            width:100%;
        }
    </style>
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script>
        $(function(){
            $("#iframeList").load(function () {
                var mainheight = $(this).contents().find("body>div").height() + 40;
                if(mainheight<300){
                    mainheight = 300;
                };
                $(this).height(mainheight);
            });
        })

    </script>
</head>
<body >
    <form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
        <div class="child_page"><!--内容外面的大框-->
            <div class="cus_nav">
                <ul>
                    <li><a id="goHome"  href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
                    <li><a href="list.jhtml">商品目录列表</a></li>
                    <li>添加商品目录</li>
                </ul>
            </div>
            <div class="form_box">

                <div class='form_baseInfo'>
                    <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                    <div class="pag_div">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">
                                <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>主题名</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <input type="text" class="input-text radius" name="theme" id="theme" placeholder="例如冰爽夏日饮品"
                                       value=""/>
                            </div>
                        </div>


                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">
                                简介</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="fieldSet">
                                    <textarea class="text_area1" id="profiles" placeholder="最多100字" name="profiles"></textarea>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="selectList"  style="width:100%;height:100%">
                    <h3 class="form_title" style="margin:20px 0 0 20px;">商品信息</h3>
                    <iframe src="getGoodList.jhtml" id="iframeList" name="iframeList"  frameborder="0" width="100%"
                            height=""  scrolling="no"></iframe>
                </div>
            </div>
            <div class="footer_submit">
                <input class="btn radius confir_S" type="button" id="yes" value="${message("admin.common.submit")}">
                <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back();return false;">
            </div>
        </div>
    </form>

    <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
    <script type="text/javascript">
         var goods=[];

        function addGoods(good){
            delGoods(good.id);
            goods.push(good);
        }

        function delGoods(id){
            for (var i = 0; i < goods.length; i++) {
                if(id == goods[i].id){
                    goods.splice(i,1);
                    break;
                }
            }
        }

        function getGoods(){
            return goods ;
        }

        $(function(){

            var $inputForm = $("#inputForm");

            [@flash_message /]

            // 地区选择
            $("#areaId").lSelect({
                url: "${base}/admin/common/area.jhtml"
            });

         // 表单验证
            $inputForm.validate({
                rules: {
                    theme: {
                        required: true,
                        remote: {
                            url: "checkTheme.jhtml",
                            cache: false
                        }
                    }
                },
                messages: {
                    theme:{
                        remote: "已存在"
                    }
                }
            });

            var formHeight=$(document.body).height()-100;
            $(".form_box").css("height",formHeight);


             $("#yes").on("click",function(){
                 if(!$("#inputForm").valid()){
                     return false ;
                 }
                 if (goods.length <= 0) {
                    $.message("warn","请选择商品");
                    return false;
                 }
                var theme=$("#theme").val();
                var profiles=$("#profiles").val();
                var param={};
                param["theme"]=theme;
                param["profiles"]=profiles;
                for (var i = 0; i < goods.length; i++) {
                     param["goodsList["+i+"].id"]=goods[i].id;
                }
                $(this).addClass("in_no_click");
                $(this).attr("disabled", true);
                $.post("save.jhtml",param,function(message){
                    $.message(message);
                    if (message.type == "success") {
                        setTimeout(function () {
                            location.href="list.jhtml";
                        }, 2000);
                    }else{
                        $(this).removeClass("in_no_click");
                        $(this).attr("disabled", false);
                    }
                });
            });

            $("#goHome").on("click",function(){
                var nav = window.top.$(".index_nav_one");
                    nav.find("li li").removeClass('clickTo');
                    nav.find("i").removeClass('click_border');
            })

        });

    </script>

</body>
</html>
[/#escape]