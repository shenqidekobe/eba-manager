[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.proxyUser.add")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:60%}
			.col-sm-7{width:72%;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">代理列表</a></li>
					<li>添加代理</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class="pag_div">
						<h3 class="form_title">基本信息</h3>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								代理名称
							</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="name" id="name" maxlength="200" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">上级代理</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius down_list" readonly placeholder="请选择" />
								<input type="text" class="downList_val" name="parentId" id="parentId"/>
								<ul class="downList_con">
									[#list proxyUserTree as proxyUser]
									<li val="${proxyUser.id}">[#if proxyUser.grade != 0][#list 1..proxyUser.grade as i]&nbsp;&nbsp;[/#list][/#if]${proxyUser.name}</li>
									[/#list]
								</ul>
							</div>
						</div>
						
					</div>
					<!--
					<div class="pag_div">
						<h3 class="form_title">关联促销</h3>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("proxyUser.seoTitle")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="seoTitle" maxlength="200"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("proxyUser.seoKeywords")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="seoKeywords" maxlength="200"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("proxyUser.seoDescription")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="seoDescription" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">${message("admin.common.order")}</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="order" maxlength="9"/>
							</div>
						</div>
					</div>
					-->
					<div class="footer_submit">
						<input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;">
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
						name: {
							required: true,
			                remote: {
			                    url: "getProxyUser.jhtml",
			                    data:{
			                    	parentId:function(){return $("#parentId").val();}
			                    },
			                    cache: false
			                },
                            pattern: /^[0-9a-zA-Z[\u4e00-\u9fa5]+$/
		                }
					},
					messages:{
						name:{
			                remote: "分类已存在",
                            pattern:'名称只能是汉字、字母、数字'
						}
					}
				});
				
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
		        
		         /* $(".downList_con").each(function(){
		        	$(this).find("li:eq(0)").addClass("li_bag");
		        	var firstText = $(this).find("li:eq(0)").text();
		        	var firstVal = $(this).find("li:eq(0)").attr("val");
		        	$(this).siblings(".down_list").val(firstText);
		        	$(this).siblings(".downList_val").val(firstVal);
		        });  */
		        
		        $(".downList_con li").click(function(){
		            $(this).parent().siblings(".down_list").attr("value",$(this).text());
		          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
		            $(this).addClass("li_bag").siblings().removeClass("li_bag");
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