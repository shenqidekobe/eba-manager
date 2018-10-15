[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.productCategory.add")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:45%}
			.col-sm-7{width:72%;}
			.form_title{margin-bottom:20px;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>
						<a href="">
						[#if newMessage != null]
						查看消息
						[#else]
						发送消息
						[/#if]
						</a>
					</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="inputForm" action="/admin/messageManage/send.jhtml" method="post" class="form form-horizontal">
					<div class="pag_div">
						<h3 class="form_title">
						[#if newMessage != null]
							查看消息
						[#else]
							发送消息
						[/#if]
						</h3>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								标题
							</label>
							[#if newMessage != null]
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" readonly="readonly" class="input-text radius" name="title" id="title" maxlength="200" value="${newMessage.title }"/>
							</div>
							[#else]
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="title" id="title" maxlength="200" value="${newMessage.title }"/>
							</div>
							[/#if]
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								内容
							</label>
							[#if newMessage != null]
							<div class="formControls col-xs-8 col-sm-7">
								<textarea class="text_area" readonly="readonly" name="content" >${newMessage.content }</textarea>
							</div>
							[#else]
							<div class="formControls col-xs-8 col-sm-7">
								<textarea class="text_area" name="content" >${newMessage.content }</textarea>
							</div>
							[/#if]
						</div>
						
					</div>
					
					<div class="footer_submit">
						 [#if newMessage != null]
                         	  <input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;">
                         [#else]
                              <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
                         	  <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;">
                         [/#if]
					</div>
				</form>
			</div>	
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/messages_zh.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script>
			$().ready(function() {
				var $inputForm = $("#inputForm");
				
				[@flash_message /]
				
				/*表单验证*/
				$inputForm.validate({
					rules:{
						name:{
							required:true
						}
					},
					messages:{
						name:{
							required:"必填"
						}
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