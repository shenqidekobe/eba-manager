[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.profile.edit")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			input[type="text"],.downList_con{width:320px;}
			.form_box{overflow: auto;}
			span.input_no_span{width:300px;}
			.xxDialog{top:40px}
			.xxDialog .dialogBottom{width:100%;position:absolute;bottom:0;border:1px solid #eee;}
			.dialogContent{height:calc(100% - 80px); overflow: auto; overflow-x: hidden;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li>企业认证审核 </li>
				</ul>
			</div>
			<div class="form_box">
				<form class="form form-horizontal">
					<div class="info_text">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								用户名
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span">${admin.username}</span>
							</div>
						</div>
						
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">
	                        	企业名称
	                        </label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <span class="input_no_span">${supplier.name}</span>
	                        </div>
	                    </div>
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">所属行业</label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <span class="input_no_span">${message("admin.supplier.industry." + supplier.industry)}</span>
	                        </div>
	                    </div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								联系人
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span">${supplier.userName}</span>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								手机号
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span">${supplier.tel}</span>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								地址
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span">${supplier.area.fullName} ${supplier.address}</span>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								企业logo
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="hidden" id="imagelogo" value="${supplier.imagelogo}">
								<ul class="treeImg" id="logoImg">
									[#if supplier.imagelogo??]
										<li><a href="${supplier.imagelogo}" target='view_window'><img src="${supplier.imagelogo}" /></a></li>
                                    [/#if]
								</ul>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								工商证件
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="hidden" id="businessCard" value="${supplier.businessCard}">
								<ul class="treeImg" id="treeImg">
								</ul>
							</div>
						</div>
						
					</div>
					
					<div class="footer_submit">
						<input class="btn radius confir_S" type="button" id="review" value="审核" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
					</div>	
				</form>
			</div>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
		$().ready(function() {
			[@flash_message /]
			
			
			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height()-100;
			$(".form_box").css("height",formHeight);
			
	      	//显示企业证件
		  	 if($("#businessCard").val() != ''){
		 		var businessCardImg=eval('(' + $("#businessCard").val() + ')');
		 		for(var o in businessCardImg){  
		 	         $("#treeImg").append("<li><a href='"+businessCardImg[o].imgUrl+"' target='view_window'><img src='"+businessCardImg[o].imgUrl+"' height='100px' width='110px' /></a></li>");
		 	      } 
		 	}else{
		 		$("#treeImg").append("无");
		 	}
	      	
	      	$("#review").click(function() {
				var $this = $(this);
				$.dialog({
	        		title:"审核",
	        		width:450,
	        		height:300,
	        		[@compress single_line=true]
	        		content:'<form id="form1" action="update.jhtml" method="get">
	        			<div class="pag_div">
			        		<input type="hidden" id="id" name="id" value="${supplier.id}" \/>
							<input type="hidden" name="adminId" value="${admin.id}" \/>
			        		<div class="row cl" style="margin-top:10px;">
								<label style="text-align:right;width:20%;" class="form-label col-xs-4 col-sm-3">
									审核:
								<\/label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<input type="radio" name="status" value="verified" checked \/> 通过
			        				<input type="radio" name="status" value="authenticationFailed"\/> 不通过
								<\/div>
							<\/div>
							<div class="row cl" style="margin-top:10px;">
								<label style="text-align:right;width:20%;" class="form-label col-xs-4 col-sm-3">
									原因:
								<\/label>
								<div class="formControls col-xs-8 col-sm-7"> 
									<textarea class="text_area1" name="reasons" cols="40" rows="5" value=""><\/textarea>
								<\/div>
							<\/div>
						<\/div>
	        		<\/form>'
	        		[/@compress],
 					onOk: function() {
 						$("#form1").submit();
 					}
 				});
			});
	      

		});
		</script>
	</body>
</html>
[/#escape]