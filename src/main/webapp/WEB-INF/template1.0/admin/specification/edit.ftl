[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.specification.edit")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:48%;float:left;}
			th{border-top:1px solid #eaeefb;}
			.form label.error{float:right;margin-top:5px;}
			input.error{background:#fff;border:1px solid red;}
			.col-sm-7{width:62%;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">${message("admin.specification.list")}</a></li>
					<li>${message("admin.specification.edit")}</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
				<input type="hidden" name="oldName" id="oldName" value="${specification.name}" />
				<input type="hidden" name="parentId" id="parentId" value="${specification.productCategory.id}" />
					<input type="hidden" name="id" value="${specification.id}" />
					<div class='form_goodsType'>
						<div class="pag_div">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">${message("Specification.productCategory")}</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									<span class="input_no_span">${specification.productCategory.name}</span>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
									${message("Specification.name")}
								</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									<input type="text" class="input-text radius" name="name" value="${specification.name}" maxlength="200"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">
									${message("admin.common.order")}
								</label>
								<div class="formControls col-xs-8 col-sm-7" style="position:relative;"> 
									<input type="text" class="input-text radius" name="order" value="${specification.order}" maxlength="9"/>
								</div>
							</div>
						</div>
						
						<div style="clear:both;"></div>
					</div>
					<div class="form_option">
						<div class="pag_div option_list">
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3">可选项</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<button type="button" id="addOptionButton" class="op_button add_B">添加</button>
								</div>
							</div>
							[#list specification.options as option]
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"></label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<input type="text" class="input-text radius" name="options" value="${option}" maxlength="200"/><span class="del_this"></span>
								</div>
							</div>
							[/#list]
						</div>
					</div>
					
					
					<div class="footer_submit">
						<input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;">
					</div>
				</form>
			</div>	
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/messages_zh.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/input.js"></script>
		<script type="text/javascript">
		$().ready(function() {
			$(".form").validate({
				rules:{
					name:{
						required:true,
						remote: {
		                    url: "getSpecification.jhtml",
		                    data:{
		                    	oldName:$("#oldName").val(),
		                    	parentId:function(){return $("#parentId").val();}
		                    },
		                    cache: false
		                }
					},
					options:{
						required:true
					}
				},
				messages:{
					name:{
						remote: "规格已存在"
					},
					options:{
						required:"必填"
					}
				}
			});
		
			/*添加可选项*/
			$("#addOptionButton").click(function(){
				$(".option_list").append(
						[@compress single_line = true]
						'<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3"></label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius" name="options" id=""><span class="del_this" maxlength="200"></span>
							</div>
						</div>'
						[/@compress]
				);
			});
	
	
			$(".form_option .option_list").delegate("div.row .del_this","click",function(){
				if($(".del_this").length == 1){
					return;
				}
				$(this).parent().parent().remove();
			})
			
			$(".del_this").on("click",function(){
				if($(".del_this").length == 1){
					return;
				}
				$(this).parent().parent().remove();
			})
			
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
	        	$(this).find("li:eq(0)").addClass("li_bag");
	        	var firstText = $(this).find("li:eq(0)").text();
	        	var firstVal = $(this).find("li:eq(0)").attr("val");
	        	$(this).siblings(".down_list").val(firstText);
	        	$(this).siblings(".downList_val").val(firstVal);
	        }); 
	        
	        $(".downList_con li").click(function(){
	            $(this).parent().siblings(".down_list").attr("value",$(this).text());
	          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
	            $(this).addClass("li_bag").siblings().removeClass("li_bag");
	        });
	        
			/*当input获得焦点时，外面的边框显示蓝色*/
			$(".focus_border").focus(function(){
				$(this).parent().addClass("add_border");
			});
			$(".focus_border").blur(function(){
				$(this).parent().removeClass("add_border");
			});
			
			/*通过js获取页面高度，来定义表单的高度*/
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