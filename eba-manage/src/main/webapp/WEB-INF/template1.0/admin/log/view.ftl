[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

	<style>
		body{background:#f9f9f9;}
		.pag_div{width:96%;float:left;}
		.tabBar{background:#f9f9f9;border:0;}
		.tabBar span{padding:5px 24px;background:#f9f9f9;color:#c3cfe5;}
		.tabBar span.current{background:#fff;color:#333;}
		.beizhu{height:80px;}
		.check-box{width:150px;height:28px;overflow: hidden;}
		#tab-system{position:relative;}
		.opera_butt{position:absolute;top:0;right:0; margin-top:5px;}
		.col-sm-7{width:80%;}
	</style>
<title>${message("admin.admin.edit")} - Powered By DreamForYou</title>
</head>
<body>
	<div class="child_page"><!--内容外面的大框-->	
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        		<li><a href="list.jhtml">日志列表</a></li>
				<li>日志详情</li>
			</ul>
		</div>
		<div class="form_box">
			<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="id" value="${member.id}" />
				<div id="tab-system" class="HuiTab">
					<div >
						<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									操作人
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${log.operator}</span>
								</div>
							
								<label class="form-label col-xs-2 col-sm-2">
									操作类型
								</label>
								<div class="formControls col-xs-4 col-sm-4">
									<span class="input_no_span">${log.operation}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									操作IP
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">${log.ip}</span>
								</div>
								<label class="form-label col-xs-2 col-sm-2">
									操作时间
								</label>
								<div class="formControls col-xs-4 col-sm-4">
								    <span class="input_no_span">${log.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									操作内容
								</label>
								<div class="formControls col-xs-10 col-sm-10">
								    <span class="input_no_span" style="width:780px;">${log.content}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-2 col-sm-2">
									请求参数
								</label>
								<div class="formControls col-xs-10 col-sm-10">
								    <span class="input_no_span" style="width:780px;">${log.parameter}</span>
								</div>
							</div>
						</div>
						
					</div>
				</div>
				<div class="footer_submit">
					<input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;"/>
				</div>
			</form>
		</div>
	</div>

<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/messages_zh.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
	$(function(){
		
		var $inputForm = $("#inputForm");
		
		[@flash_message /]
		
		/*通过js获取页面高度，来定义表单的高度*/
		var formHeight=$(document.body).height()-100;
		$(".form_box").css("height",formHeight);
		
		/**/
		var heightObj = $(document.body).height() - 185;
		$(".list_t_tbody").css("height",heightObj);
		
		
        //下拉列表
        $(".down_list").click(function(){
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });

        $(".downList_con").each(function(){

            var $selectDom = $(this).find("li.li_bag") ;

            var firstText = $selectDom.text();
            var firstVal = $selectDom.attr("val");

            $(this).siblings(".down_list").val(firstText);
            $(this).siblings(".downList_val").val(firstVal);
        });

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });

		// 表单验证
		$inputForm.validate({
			rules: {
			},
            messages: {
            }
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