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
			.form_box{overflow: auto;}
			.tixing{width:380px;}
			.xxDialog{
				top:10%;
			}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>${message("admin.profile.edit")} </li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
					<div class="info_text">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								${message("Admin.username")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span">${admin.username}</span>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								手机号
							</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input class="input-text radius" type="text" name="bindPhoneNum" id="bindPhoneNum" value="${admin.bindPhoneNum}" readonly="readonly" />
							<!--
								[#if (admin.bindPhoneNum)??]
									<span class="replaceTell blue">更换手机号</span>
								[#else]
									<span class="bindTell blue">绑定手机号</span>
								[/#if]
							-->
							</div>
						</div>
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">
	                        	${message("Admin.name")}
	                        </label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <span class="input_no_span">${admin.name}</span>
	                        </div>
	                    </div>
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">${message("Admin.department")}</label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <span class="input_no_span">${admin.department.name}</span>
	                        </div>
	                    </div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								${message("admin.profile.currentPassword")}
							</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="password" class="input-text radius" name="currentPassword" maxlength="20" autocomplete="off" /> 
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								${message("admin.profile.password")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="password" id="password" name="password" class="input-text radius" maxlength="20" autocomplete="off" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								${message("admin.profile.rePassword")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="password" name="rePassword" class="input-text radius" maxlength="20" autocomplete="off" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								${message("Admin.email")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" name="email" class="input-text radius" value="${admin.email}" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="input_no_span tixing">${message("admin.profile.tips")}</span>
							</div>
						</div>
						
					</div>
					
					<div class="footer_submit">
						<input class="btn radius confir_S" id="yes" type="button" value="${message("admin.common.submit")}" />
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
			var $inputForm = $("#inputForm");
			
			[@flash_message /]
			
			$.validator.addMethod("requiredTo", 
					function(value, element, param) {

						var parameterValue = $(param).val();
						if ($.trim(parameterValue) == "" || ($.trim(parameterValue) != "" && $.trim(value) != "")) {
							return true;
						} else {
							return false;
						}
					},
					"${message("admin.profile.requiredTo")}"
				);
				
				// 表单验证
				$inputForm.validate({
					rules: {
						currentPassword: {
							requiredTo: "#password",
							required: true,
							remote: {
								url: "check_current_password.jhtml",
								cache: false
							}
						},
						password: {
							required: true,
							minlength: 6,
							maxlength: 20,
							pattern: /^(?![0-9]+$)/
						},
						rePassword: {
							required: true,
							equalTo: "#password"
						},
						email: {
							required: true,
							email: true
						}
					}
				});
			
			
			$("#goHome").on("click",function(){
				var nav = window.top.$(".index_nav_one");
        			nav.find("li li").removeClass('clickTo');
					nav.find("i").removeClass('click_border');
			})

			$("#yes").on("click",function(){
				console.log($inputForm.valid());
				if (!$inputForm.valid()) {
					return;
				}
				var params=$inputForm.serializeArray();
				$.ajax({
	                type: "POST",
	                url: "update.jhtml",
	                data: params,
	                dataType: "json",
	                //contentType:'application/json;charset=utf-8', //设置请求头信息
	                beforeSend: function () {

	                },
	                success: function (message) {
	                    $.message(message);
	                    if (message.type == "success") {
	                        setTimeout(function () {
	                           location.href="edit.jhtml";
	                        }, 1000);
	                    }
	                },
	                error: function (data) {
	                     $.message(data);
	                }
            	});
			});

			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height()-100;
			$(".form_box").css("height",formHeight);



            $(".replaceTell").click(function(){
                $.dialog({
                        title: "更换手机号",
                        width:600,
                        height:400,
                        content: [@compress single_line = true]
                        '<form id="replacePhoneNum" class="form form-horizontal" action="" method="">
						<div class="modelCon tellReplice" style="padding:40px 80px;">

							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />原手机号</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius" name="originalPhone" id="originalPhone" />
								</div>
							</div>


							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />验证码</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius tellCode" name="originalCode" id="originalCode" /> <button type="button" class="code_B replaceCode_B1">获取验证码<\/button>
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />新手机号</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius" name="newPhone" id="newPhone" />
								</div>
							</div>

							<div class="row cl">
								<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />验证码</label>
								<div class="formControls col-xs-8 col-sm-7">
									<input type="text" class="input-text radius tellCode" name="newCode" id="newCode" /> <button type="button" class="code_B replaceCode_B2">获取验证码<\/button>
								</div>
							</div>
						<\/div>
                	<\/form>'
                    [/@compress],
                	onOk: function() {
                		var params=[];
	                	var originalPhone = $("#originalPhone").val();
	                	var originalCode = $("#originalCode").val();
	                	var newPhone = $("#newPhone").val();
	                	var newCode = $("#newCode").val();
	                	var pattern = /^[1|2][3|4|5|7|8]\d{9}$/;
	                	if(originalPhone == "") {
	                		$.message("warn","原手机号不能为空！");
                			return false;
	                	}
	                	if(!pattern.test(originalPhone)) {
                			$.message("warn","原手机号格式不正确");
                			return false;
                		}
	                	if(originalCode == "") {
	                		$.message("warn","验证码不能为空！");
                			return false;
	                	}
	                	if(newPhone == "") {
	                		$.message("warn","新手机号不能为空！");
                			return false;
	                	}
	                	if(!pattern.test(newPhone)) {
                			$.message("warn","新手机号格式不正确");
                			return false;
                		}
	                	if(newCode == "") {
	                		$.message("warn","验证码不能为空！");
                			return false;
	                	}
	    	    		params.push(
    	    				{name:"originalPhone",value:$("#originalPhone").val()},
    	    				{name:"originalCode",value:$("#originalCode").val()},
    	    				{name:"newPhone",value:$("#newPhone").val()},
    	    				{name:"newCode",value:$("#newCode").val()}
	    	    		);
	                	$.ajax({
                			url: "replacePhoneNumber.jhtml",
    	    				type: "POST",
    	    				async: false,
    	    				data: params,
    	    				dataType: "json",
    	    				success: function(data) {
    	    					if(data.code == "0"){
    	    						$.message("warn","手机号更换成功!");
    	    						setTimeout(function () {
    	    							window.location.href="edit.jhtml";
    	        					}, 2000);
	                            }else{
	                                $.message("error", data.msg);
	                                return false;
	                            }
    	    				}
                		});
                	},
                	onShow:function(){
                		$(".replaceCode_B1").on("click",function(){
	                		var originalPhone = $("#originalPhone").val();
	                		var pattern = /^1[3|4|5|7|8]\d{9}$/;
	                		if(originalPhone == "") {
	                			$.message("warn","手机号不能为空");
	                			return;
	                		}
	                		if(!pattern.test(originalPhone)) {
	                			$.message("warn","手机号格式不正确");
	                			return;
	                		}
	                		
	                		var _this = $(this);
	                		$.ajax({
	                			url: "sendSms.jhtml",
	    	    				type: "GET",
	    	    				async: false,
	    	    				data: {"tel":$("#originalPhone").val(),"smsType":"replacePhoneNum"},
	    	    				dataType: "json",
	    	    				success: function(data) {
	    	    					if(data.code == '0') {
	    	    						$.message("warn","验证码发送成功！");
	    	    						var bindCode_B = _this;
	    		                		clickButton(bindCode_B);
	    	    					}else {
	    	    						$.message("warn",data.msg);
	    	    					}
	    	    					
	    	    				}
	                		});
	                		
	                	});
	                	
	                	$(".replaceCode_B2").on("click",function(){
	                		var newPhone = $("#newPhone").val();
	                		var pattern = /^1[3|4|5|7|8]\d{9}$/;
	                		if(newPhone == "") {
	                			$.message("warn","手机号不能为空");
	                			return;
	                		}
	                		if(!pattern.test(newPhone)) {
	                			$.message("warn","手机号格式不正确");
	                			return;
	                		}
	                		//验证手机号是否绑定
	                		var exist = true;
	                		$.ajax({
	                			url: "checkBindTel.jhtml",
	    	    				type: "GET",
	    	    				async: false,
	    	    				data: {"tel":newPhone},
	    	    				dataType: "json",
	    	    				success: function(data) {
	    	    					if(data == false) {
	    	    						exist = false;
	    	    					}
	    	    				}
	                		});
	                		if(!exist) {
	                			$.message("warn","该手机号已经绑定过账号，不能再绑定了！");
	                			return;
	                		}
	                		var _this = $(this);
	                		$.ajax({
	                			url: "sendSms.jhtml",
	    	    				type: "GET",
	    	    				async: false,
	    	    				data: {"tel":$("#newPhone").val(),"smsType":"replacePhoneNum"},
	    	    				dataType: "json",
	    	    				success: function(data) {
	    	    					if(data.code == '0') {
	    	    						$.message("warn","验证码发送成功！");
	    	    						var bindCode_B = _this;
	    		                		clickButton(bindCode_B);
	    	    					}else {
	    	    						$.message("warn",data.msg);
	    	    					}
	    	    				}
	                		});
	                		
	                		
	                	});
                	}
                })
			})





            $(".bindTell").click(function(){
                $.dialog({
                        title: "绑定手机号",
                        width:600,
                        height:280,
                        content: [@compress single_line = true]
                        '<form id="replacePhoneNum" class="form form-horizontal" action="" method="">
                    <div class="modelCon tellReplice" style="padding:40px 80px;">

                    	<div class="row cl">
                    		<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />绑定手机号</label>
                    		<div class="formControls col-xs-8 col-sm-7">
                    			<input type="text" class="input-text radius" name="phone" id="phone" />
                    		</div>
                    	</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />验证码</label>
							<div class="formControls col-xs-8 col-sm-7">
								<input type="text" class="input-text radius tellCode" name="code" id="code" /> <button type="button" class="code_B bindCode_B">获取验证码<\/button>
							</div>
						</div>

                	</div>
                <\/form>'
                    [/@compress],
                onOk: function() {
                	var code = $("#code").val();
                	var bindPhoneNum = $("#phone").val();
                	var pattern = /^1[3|4|5|7|8]\d{9}$/;
            		if(bindPhoneNum == "") {
            			$.message("warn","手机号不能为空！");
            			return false;
            		}
            		if(!pattern.test(bindPhoneNum)) {
            			$.message("warn","手机号格式不正确！");
            			return false;
            		}
                	if(code == "") {
                		$.message("error","验证码不能为空！");
                		return false;
                	}
                	$.ajax({
            			url: "checkSms.jhtml",
	    				type: "GET",
	    				async: false,
	    				data: {"tel":$("#phone").val(),"code":$("#code").val()},
	    				dataType: "json",
	    				success: function(data) {
	    					if(data.code == "0"){
	    						$.message("warn","手机号绑定成功!");
	    						setTimeout(function () {
	    							window.location.href="edit.jhtml";
	        					}, 2000);
                            }else{
                                $.message("error", data.msg);
                                return false;
                            }
	    				}
            		});
                },
                onShow:function(){
                	$(".bindCode_B").on("click",function(){
                		var bindPhoneNum = $("#phone").val();
                		var pattern = /^1[3|4|5|7|8]\d{9}$/;
                		if(bindPhoneNum == "") {
                			$.message("warn","手机号不能为空！");
                			return;
                		}
                		if(!pattern.test(bindPhoneNum)) {
                			$.message("warn","手机号格式不正确！");
                			return;
                		}
                		//验证手机号是否绑定
                		var exist = true;
                		$.ajax({
                			url: "checkBindTel.jhtml",
    	    				type: "GET",
    	    				async: false,
    	    				data: {"tel":$("#phone").val()},
    	    				dataType: "json",
    	    				success: function(data) {
    	    					if(data == false) {
    	    						exist = false;
    	    					}
    	    				}
                		});
                		if(!exist) {
                			$.message("warn","该手机号已经绑定过了！");
                			return;
                		}
                		//发送验证码
                		var _this = $(this);
                		$.ajax({
                			url: "sendSms.jhtml",
    	    				type: "GET",
    	    				async: false,
    	    				data: {"tel":$("#phone").val(),"smsType":"bindPhoneNum"},
    	    				dataType: "json",
    	    				success: function(data) {
    	    					if(data.code == '0') {
    	    						$.message("warn","验证码发送成功！");
    	    						var bindCode_B = _this;
    		                		clickButton(bindCode_B);
    	    					}else {
    	    						$.message("warn",data.msg);
    	    					}
    	    					
    	    				}
                		});
                		
                		
                	});
                }
            })
            })


		});
		</script>
	</body>
</html>
[/#escape]