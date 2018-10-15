[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微商平台</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>		
			.form-group{width:650px;}
			.form-control{display:inline-block;margin-right:10px;}
			.xxDialog {top: 120px;}
	        .xxDialog .dialogBottom {
	            width: 100%;
	            position: absolute;
	            bottom: 0;
	        }
	        .dialogContent {
	            height: calc(100% - 80px);
	            overflow: auto;
	            overflow-x: hidden;
	        }
	        .xxDialog .dialogBottom{height:42px;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="content_box">
						<div class="con_title">
							企业信息
						</div>
						<div class="con_form">
							<form id="enterpriseinfo" class="form-horizontal">
								<input type="hidden" id="id" name="id" value="${supplier.id}" />
								<input type="hidden" id="status" name="status" value="${supplier.status}"/>
								<div class="form-group">
								    <label class="col-sm-4 control-label">用户名</label>
								    <div class="col-sm-9">
								     	<span class="input_no_span">${admin.username}</span>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">手机号</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="bindPhoneNum" id="bindPhoneNum" value="${admin.bindPhoneNum}" readonly="readonly" />
								     	[#if admin.bindPhoneNum??]
								     	<span class="replaceTell blue">更换手机号</span>
								     	[#else]
								     	<span class="bindTell blue">绑定手机号</span>
								     	[/#if]
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />企业名称</label>
								    <div class="col-sm-9" id='renzheng'>
								     	<input class="form-control" type="text" name="name" id="name" value="${supplier.name}" />
								     	
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />所属行业</label>
								    <div class="col-sm-9">
								     	<select class="form-control" name="industry" id="industry">
								     		[#list industrys as industry]
												<option value="${industry}"[#if industry == supplier.industry] selected="selected"[/#if]>
													${message("admin.supplier.industry." + industry)}
												</option>
											[/#list]
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">logo</label>
								    <div class="col-sm-9">
								     	<div class="updateImg">
								     		[#if supplier.imagelogo??]
								     		<div class="img_box">
		                                        <img src="${supplier.imagelogo}" />
		                                        <div class="caozuoImg">
									    			<a href="${supplier.imagelogo}" class="see"></a>
									    			<a href="javascript:;" class="del"></a>
									    		</div>
								     		</div>
								     		[/#if]
								     		<div id="picker">请选择图片上传</div>
								     		<input type="hidden" class="form-control" id="imagelogo" name="imagelogo" value="${supplier.imagelogo}" />
								     		<div class="img_model">
								     			<span class="delImg"></span>
								     		</div>
								     	</div>
								     	<p class="gray">图片尺寸为<span class="orange">200px*200px</span>，每张最大<span class="orange">2M</span></p>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />法人姓名</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="legalPersonName" id="legalPersonName" value="${supplier.legalPersonName}"/>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />联系人</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="userName" id="userName" value="${supplier.userName}" maxlength="20" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />联系方式</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="tel" id="tel" value="${supplier.tel}"  maxlength="20" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />公司地址</label>
								    <div class="col-sm-9">
								     	<span class="fieldSet">
											<input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}" treePath="${(supplier.area.treePath)!}"  />
										</span>
										<div>
											<input class="form-control" style="width:300px" type="text" id="address" name="address" placeholder="详细地址"  maxlength="20" autocomplete="off" value="${supplier.address}" />
										</div>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />电子邮箱</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="email" id="email" value="${supplier.email}" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">客服电话</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="customerServiceTel" id="customerServiceTel" value="${supplier.customerServiceTel}" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">公司简介</label>
								    <div class="col-sm-9">
								     	<textarea class="form-control" rows="5" id="companyProfile" name="companyProfile">${supplier.companyProfile}</textarea>
								    </div>
								</div>
								
								
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label"></label>
								    <div class="col-sm-9" style="width:700px">
								     	<button type="button" class="hold_B" id="yes">保存</button>
								     	<button type="button" class="cancel_B" onclick="javascript:history.back(-1)">取消</button>
								    </div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		[#include "/shop/common/foot.ftl"]
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript">
		
		$(function(){
			[@flash_message /]
			
			var $name=$("#name");
			var $status=$("#status");
			var $enterpriseinfo=$("#enterpriseinfo");
			var $fileimagelogo = $("#fileimagelogo");
			var businessCards=new Array();
			
			//未认证
			if($status.val() == 'notCertified') {
				$("#renzheng").append("<img src='${base}/resources/shop/common/images/weirenzheng_icon.svg' />未认证");
			}
			//已认证
			if($status.val() == 'verified') {
				$("#renzheng").append("<img src='${base}/resources/shop/common/images/yirenzheng_icon.svg' />已认证");
				$('#name').attr("disabled",true);
			}
			//认证中
			if($status.val() == 'certification') {
				$("#renzheng").append("<img src='${base}/resources/shop/common/images/renzhengzhong_icon.svg' />认证中");
				$('#name').attr("disabled",true);
			}
			//认证未通过
			if($status.val() == 'authenticationFailed') {
				$("#renzheng").append("<img src='${base}/resources/shop/common/images/renzhengshibai_icon.svg' />认证失败");
			}
			
			// 地区选择
		    $("#areaId").lSelect({
		        url: "${base}/admin/common/area.jhtml"
		    });
			
		    /*表单验证*/
			$enterpriseinfo.validate({
				rules:{
					name:{
						required:true
					},
					industry:{
						required:true
					},
					userName:{
						required:true
					},
					tel:{
						required:true
						//pattern: /^1[3|4|5|7|8]\d{9}$/
					},
					areaId:{
						required:true
					},
					address:{
						required:true
					},
					email:{
						required:true,
						email:true
					},
					legalPersonName:{
						required:true
					}
				},
				messages:{
					name:{
						required:"企业名称不能为空"
					},
					industry:{
						required:"必填"
					},
					userName:{
						required:"用户名不能为空"
					},
					tel:{
						required: "必填"
		                //pattern: "手机号格式不正确"
					},
					areaId:{
						required:"必选"
					},
					address:{
						required: "必填"
					},
					email:{
						required:"邮箱不能为空",
						email:"请输入正确格式的电子邮件"
					},
					legalPersonName:{
						required:"法人姓名不能为空"
					}
				}
				
			});
		    
			$("#yes").on("click",function(){
	    		//验证
	        	if(!$enterpriseinfo.valid()){
		            return false ;
		     	}
	    		
	    		var exists=true;
	    		var params=[];
	    		params.push(
	    			{name:"id",value:$("#id").val()},
	    			{name:"name",value:$("#name").val()}
	    		);
	    		$.ajax({
					url: "nameExists.jhtml",
					async : false,
					type: "GET",
					data: params,
					dataType: "json",
					success: function(data) {
						exists=data.exists;
					}
				});
	    		if(exists){
					$.message("warn", "企业名称已注册,请修改企业名称！");
					return false;
				}
	    		
	    		var businessCardsJson=new Array();
	    		for(var i=0;i<businessCards.length;i++){
	    			businessCardsJson.push({
	    				imgUrl:businessCards[i]
	    			});
	    		}
	    		var params=[];
	    		params.push(
	    			{name:"id",value:$("#id").val()},
	    			{name:"name",value:$("#name").val()},
	    			{name:"industry",value:$("#industry").val()},
	    			{name:"imagelogo",value:$("#imagelogo").val()},
	    			{name:"userName",value:$("#userName").val()},
	    			{name:"tel",value:$("#tel").val()},
	    			{name:"areaId",value:$("#areaId").val()},
	    			{name:"address",value:$("#address").val()},
	    			{name:"email",value:$("#email").val()},
	    			{name:"companyProfile",value:$("#companyProfile").val()},
	    			{name:"businessCard",value: JSON.stringify(businessCardsJson)},
	    			{name:"customerServiceTel",value:$("#customerServiceTel").val()},
	    			{name:"legalPersonName",value:$("#legalPersonName").val()}
	    		);
	    		$("#yes").attr('disabled',"true");
	    		$.ajax({
	    				url: "updateEnterpriseInfo.jhtml",
	    				type: "POST",
	    				async: false,
	    				data: params,
	    				dataType: "json",
	    				success: function(data) {
	    					if(data.isSuccess){
	    						$.message("warn","提交成功！");
	    						setTimeout(function () {
	    							window.location.href="index.jhtml";
	        					}, 2000);
	    					}else{
	    						$.message("warn","提交失败！");
	    						$('#yes').removeAttr("disabled");
	    					}
	    				}
	    			});
	    	});
			
		    
		    $(".bindTell").click(function(){
		    	$.dialog({
	                title: "绑定手机号",
	                width:600,
	                height:330,
	                content: [@compress single_line = true]
                	'<form id="bindPhone" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:50px 80px;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />绑定手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" name="bindPhoneNum1" id="bindPhoneNum1" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" id="code" name="code" style="width:140px;" \/><button type="button" class="code_B bindCode_B">获取验证码<\/button>
							    <\/div>
							<\/div>
                		<\/div>
                    <\/form>'
                [/@compress],
	                onOk: function() {
	                	var code = $("#code").val();
	                	var bindPhoneNum = $("#bindPhoneNum1").val();
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
    	    				data: {"tel":$("#bindPhoneNum1").val(),"code":$("#code").val()},
    	    				dataType: "json",
    	    				success: function(data) {
    	    					if(data.code == "0"){
    	    						$.message("warn","手机号绑定成功!");
    	    						setTimeout(function () {
    	    							window.location.href="index.jhtml";
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
	                		var bindPhoneNum = $("#bindPhoneNum1").val();
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
	    	    				data: {"bindPhoneNum1":$("#bindPhoneNum1").val()},
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
	    	    				data: {"tel":$("#bindPhoneNum1").val(),"smsType":"bindPhoneNum"},
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
	                		
	                		
	                	})
			
	                },
	            });   	
		    });
		    
		   
		   
		    
		    
		    
		    
		    /*更换手机号*/
		    $(".replaceTell").click(function(){
		    	$.dialog({
	                title: "更换手机号",
	                width:600,
	                height:440,
	                content: [@compress single_line = true]
                	'<form id="replacePhoneNum" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:40px 80px;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />原手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" id="originalPhone" name="originalPhone" \/>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" id="originalCode" name="originalCode" style="width:140px;" \/><button type="button" class="code_B replaceCode_B1">获取验证码<\/button>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />新手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" id="newPhone" name="newPhone" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" id="newCode" name="newCode" style="width:140px;" \/><button type="button" class="code_B replaceCode_B2">获取验证码<\/button>
							    <\/div>
							<\/div>
                		<\/div>
                    <\/form>'
                [/@compress],
	                onOk: function() {
	                	var params=[];
	                	var originalPhone = $("#originalPhone").val();
	                	var originalCode = $("#originalCode").val();
	                	var newPhone = $("#newPhone").val();
	                	var newCode = $("#newCode").val();
	                	var pattern = /^1[3|4|5|7|8]\d{9}$/;
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
    	    							window.location.href="index.jhtml";
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
	    	    				data: {"bindPhoneNum1":newPhone},
	    	    				dataType: "json",
	    	    				success: function(data) {
	    	    					if(data == false) {
	    	    						exist = false;
	    	    					}
	    	    				}
	                		});
	                		if(!exist) {
	                			$.message("warn","该手机号已经绑定过账号，不能在绑定了！");
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
			
	                },
	            });   	
		    });
		    
		})
			
		
		
			var token = getCookie("token");
			var uploadUrl = "/admin/file/upload.jhtml?fileType=image&token=" + token ;
			var fileUrl = {};
			var imgId="";
				
			var uploader = WebUploader.create({
			    // swf文件路径
			   	swf: '/resources/admin1.0/flash/webuploader.swf',
			    // 文件接收服务端。
			    server:uploadUrl,
			    // 选择文件的按钮。可选。
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#picker',
			    fileSingleSizeLimit: 1024 * 1024 * 30,
			    accept: {
			    	extensions: 'jpg,png,gif,jepg,svg'
				},
				//验证文件总数量, 超出则不允许加入队列
				fileNumLimit: 1,
				//auto {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传
				auto: true,
			    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			    resize: false,
                fileVal : 'file'
			}).on('uploadAccept', function(file, data) {
				
				$("#picker").before('<div class="img_box">'+
	     			'<img src="'+data.url+'" alt="" />'+
		    		'<div class="caozuoImg">'+
		    			'<a href="'+data.url+'" class="see"></a>'+
		    			'<a href="javascript:;" class="del"></a>'+
		    		'</div>'+
	     		'</div>');
				$("#imagelogo").val(data.url);
	     		
				
				$(".updateImg .del").on("click",function(){
					//alert(123);
					$(this).closest(".img_box").remove();
			    	uploader.removeFile( imgId,true);
			    	$("#imagelogo").val("");
				})

			}).on('fileQueued', function(file) {
				
				console.log(file);
				imgId = file.id;
				
			}).on('error', function(type) {
				
				switch(type) {
					case "F_EXCEED_SIZE":
						alert("上传文件大小超出限制");
						break;
					default:
						alert("上传文件出现错误");
				}
				
			});
			
			$(".updateImg .del").on("click",function(){
				$(this).closest(".img_box").remove();
				$("#imagelogo").val("");
			})
		</script>

	</body>
</html>
[/#escape]