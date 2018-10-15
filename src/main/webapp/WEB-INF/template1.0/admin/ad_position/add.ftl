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
        .pag_div{width:calc(100% - 30px);}
        label.error{left:300px;}
        #address+label.error{left:300px;top:50px;}
    </style>
</head>
<body >
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome" href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li><a href="list.jhtml">${message("admin.adPosition.list")}</a></li>
            <li> ${message("admin.adPosition.add")}</li>
        </ul>
    </div>
    <div class="form_box" style="overflow: auto;">
        <form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
            <div class="pag_div">
                <h3 class="form_title">广告位信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
					${message("AdPosition.name")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="name" maxlength="200">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
					${message("AdPosition.width")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="width" maxlength="9">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
					${message("AdPosition.height")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="height" maxlength="9" />
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("AdPosition.description")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" placeholder="" name="description" maxlength="200"/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("AdPosition.template")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <!-- <textarea id="description" name="description" class="editor" style="width: 100%;"></textarea> -->
                        <textarea rows="5" class="text_area" cols="50" rows="5" name="template" placeholder="" ></textarea>
                    </div>
                </div>


            </div>
            <div class="footer_submit">
                <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}" />
                <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
            </div>
        </form>
    </div>
</div>
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
<script type="text/javascript">

    $(function(){

        var $inputForm = $("#inputForm");

		[@flash_message /]

        // 表单验证
        $inputForm.validate({
            rules: {
                name: "required",
                width: {
                    required: true,
                    integer: true,
                    min: 1
                },
                height: {
                    required: true,
                    integer: true,
                    min: 1
                },
                template: "required"
            }
        });

        var formHeight=$(document.body).height()-100;
        $(".form_box").css("height",formHeight);
		
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