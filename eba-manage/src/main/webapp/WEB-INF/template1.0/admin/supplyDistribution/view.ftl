[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:95%; float:left;}
			.col-sm-7{width:40%;}
			.require_search,.ch_search,.update_B{border:1px solid #CADBF3;}
			.table-border th{border-top:1px solid #eaeefb;}
			#iframeList{overflow: hidden;}	
			.form_box{overflow: auto;}
		</style>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/date/dateRange.js"></script><!--时间控件-->
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/laypage/1.2/laypage.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
			function updateStatus(type , id){
				var content = "您确认此条供应信息？" ;
				if("rejected" == type){
			        content = "您决定拒绝这条供应信息？" ;
				}
			    $.dialog({
			        type: "warn",
			        content: content,
			        onOk: function(obj) {
			            $.ajax({
			                url: "updateStatus.jhtml",
			                type: "GET",
			                data: {status:type , id:id},
			                dataType: "json",
			                cache: false,
			                async: false,
			                success: function(message) {
			                    $.message(message);
			                    if (message.type == "success") {
			                        setTimeout(function () {
			                            window.location.href="list.jhtml";
			                        }, 2000);
			                    }else{
	
								}
			                }
			            });
			        }
			    });
			}
		
			$(function(){
				[@flash_message /]
				
				/*通过js获取页面高度，来定义表单的高度*/
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
	
				var formList = $(document.body).height() - 240;
				$(".selectList").css("height",formList);
			
				/*当input获得焦点时，外面的边框显示蓝色*/
				$(".focus_border").focus(function(){
					$(this).parent().addClass("add_border");
				});
				$(".focus_border").blur(function(){
					$(this).parent().removeClass("add_border");
				});
				
				
				$("#iframeList").load(function () {
					var mainheight = $(this).contents().find("body>div").height() + 40;
					if(mainheight<300){
						mainheight = 300;
					};
				 	$(this).height(mainheight);
				});
				
				
				$("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})
				
           });
		</script>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li><a href="list.jhtml">供应分配列表</a></li>
					<li>供应详情</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					
					<div class='form_baseInfo'>
						<h3 class="list_title">基本信息</h3>
						<div class="pag_div">
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">企业名称</label>
								<div class="formControls col-xs-8 col-sm-7""> 
									<span class="input_no_span" style="color:#586897">${supplierSupplier.supplier.name}</span>
								</div>
							</div>
							<div class="row cl" style="float:left;">
								<label class="form-label col-xs-4 col-sm-3">
									供应时间
								</label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<span class="input_no_span" style="color:#586897">${supplierSupplier.startDate}至${supplierSupplier.endDate}</span>
								</div>
							</div>
							<div class="clear"></div>
						</div>
					</div>
					<div class="hang_list">
						<h3 class="list_title">商品信息</h3>
						
					</div>
					
					<div class="footer_submit">
						[#if type == 'confirm']
						<input class="btn radius confir_S" type="button" id="submitButton" onclick="javascript:updateStatus('inTheSupply', ${supplierSupplier.id});" value="${message("admin.common.submit")}"/>
						<input class="btn radius cancel_B" type="button" id="submitButton" onclick="javascript:updateStatus('rejected', ${supplierSupplier.id});" value="拒绝"/>
						<input class="btn radius cancel_B" type="button" onclick="history.back(); return false;" value="${message("admin.common.back")}"/>
						[#else]
						<input class="btn radius cancel_B" type="button" onclick="history.back(); return false;" value="返回"/>
						[/#if]
					</div>
					
					<div class="selectList" style="width:100%;">
						<iframe src="viewGoods.jhtml?supplierSupplierId=${supplierSupplier.id}" id="iframeList" name="iframeList"  frameborder="0" width="100%" height=""  scrolling="no">
					</div>
				</form>
			</div>	
		</div>
	</body>
</html>
[/#escape]
