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
                <h3 class="form_title">广告信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
						${message("Ad.title")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="title" maxlength="200">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
						${message("Ad.type")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">

                        <input type="text" id="typeValue" class="input-text radius down_list" readonly placeholder="请选择">
                        <input type="text" id="type" name="type" class="downList_val" />
                        <ul class="downList_con"  style="z-index: 9999;">
							[#list types as type]
                                <li val="${type}">${message("Ad.Type." + type)}</li>
							[/#list]
                        </ul>

                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Ad.adPosition")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">

                        <input type="text" id="adPositionValue" class="input-text radius down_list" readonly placeholder="请选择">
                        <input type="text" id="adPositionId" name="adPositionId" class="downList_val" />
                        <ul class="downList_con" style="z-index: 9999;">
							[#list adPositions as adPosition]
                                <li val="${adPosition.id}">${adPosition.name} [${adPosition.width} × ${adPosition.height}]</li>
							[/#list]
                        </ul>

                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Article.content")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <textarea id="content" name="content" class="editor"></textarea>
                    </div>
                </div>

                <!--<div class="row cl" style="display: none">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Ad.path")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">

                        <span class="fieldSet">
						<input type="text" id="path" name="path" class="input-text radius" maxlength="200" disabled="disabled" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
					</span>
                    </div>
                </div>-->
                
				<div class="row cl" style="display: none">
                    <label class="form-label col-xs-4 col-sm-3">${message("Ad.path")}</label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <div class="updateImg">
                            <div class="img_box">
                            	[#if ad.path??]
                                <img src="${supplier.imagelogo}" style="width:500px;"/>
                            	[/#if]
                            </div>
                            <input type="text" id="path" name="path" value="${ad.path}" style="display:none;"/>
                            <a id="filePicker" class="file" style="display:block;width:60px;height:60px;margin:30px;opacity: 0" >gggggggggggggggggggggg</a>
                            <div class="img_model">
                                <span class="delImg"></span>
                            </div>
                        </div>
                        <span class="imgError"></span>
                    </div>
                </div>
				
				 <div class="row cl" tyle="display: none">
                    <label class="form-label col-xs-4 col-sm-3">
						${message("Ad.backgroundColor")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" name="backgroundColor" maxlength="200">
                    </div>
                </div>
				
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Ad.beginDate")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" id="beginDate" name="beginDate" class="input-text radius text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Ad.endDate")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" id="endDate" name="endDate" class="input-text radius  text  Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("Ad.url")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" name="url" class="input-text radius" maxlength="200" />
                    </div>
                </div>

                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
					${message("admin.common.order")}
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" name="order" class="input-text radius" maxlength="9" />
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
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
<script type="text/javascript">

    $(function(){

        var $inputForm = $("#inputForm");
        var $type = $("#type");
        var $content = $("#content");
        var $path = $("#path");
        var $filePicker = $("#filePicker");

		[@flash_message /]

       /*上传图片*/
        $filePicker.uploader({
            before: function (file) {

            },
            complete: function (file) {
                var fr = new FileReader();
                var file = file.file.source.source;
                fr.onload = function () {
                    $(".updateImg .img_box").html("<img src='' style='width:500px;'/>");
                    $(".updateImg img").attr("src", fr.result);
                };
                fr.readAsDataURL(file);
            }
        });
        
        
        $(".img_box").mouseover(function () {
            $(".img_model").css("display", "block");
        });
        $(".img_model").mouseleave(function () {
            $(".img_model").css("display", "none");
        });

        $(".updateImg .delImg").on("click", function () {
            $(".updateImg .img_box").html("");
            $(this).parent().css("display", "none");
        });
        
        
        
        
        
        
        

        $content.editor();

        $type.change(function() {
            if ($(this).val() == "text") {
                $content.prop("disabled", false).parent().parent().show();
                $path.prop("disabled", true).parent().parent().parent().hide();
            } else {
                $content.prop("disabled", true).parent().parent().hide();
                $path.prop("disabled", false).parent().parent().parent().show();
            }
        });

        // 表单验证
        $inputForm.validate({
            rules: {
                title: "required",
                adPositionId: "required",
                path: {
                    required: true,
                    pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                },
                url: {
                    pattern: /^(http:\/\/|https:\/\/|ftp:\/\/|mailto:|\/|#).*$/i
                },
                order: "digits"
            }
        });

        var formHeight=$(document.body).height()-100;
        $(".form_box").css("height",formHeight);

        /*下拉框的样式*/
        $(".down_list").click(function(){
        	//$(".downList_con").hide();
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });

		$(".downList_con").each(function(){
           $(this).find("li:eq(0)").addClass("li_bag");
           var firstText = $(this).find("li:eq(0)").text();
           var firstVal = $(this).find("li:eq(0)").attr("val");
           $(this).siblings(".down_list").val(firstText);
           $(this).siblings(".downList_val").val(firstVal);
       });

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
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