[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>企业信息</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			input[type="text"],.downList_con{width:320px;}
			.form_box{overflow: auto;}
			.renzheng{display:inline-block; width:100px;text-indent: 5px; height:32px;line-height:32px; border-radius: 5px;}
			.rzTrue{background:#fffaeb;border:1px solid #FFE9A7;color:#343434;}
			.rzFalse{background:#eee;color:#777;}
			.col-sm-7{width:80%;}
			.tishi{color:#999;vertical-align: bottom;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a href="">${message("admin.breadcrumb.home")}</a></li>
					<li>企业信息</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="enterpriseinfo" class="form form-horizontal">
					<input type="hidden" id="id" name="id" value="${supplier.id}" />
					<input type="hidden" id="status" name="status" value="${supplier.status}"/>
					<div class="info_text">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								企业名称
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="name" id="name" value="${supplier.name}" />
								<span class="renzheng" id="renzheng"></span>
								<!--  <span class="renzheng"><img src="${base}/resources/admin1.0/images/yirenzheng_icon.svg" />已认证</span>
								<span class="renzheng"><img src="${base}/resources/admin1.0/images/weirenzheng_icon.svg" />未认证</span>
								<span class="renzheng"><img src="${base}/resources/admin1.0/images/renzhengshibai_icon.svg" />认证失败</span>
								<span class="renzheng"><img src="${base}/resources/admin1.0/images/renzhengzhong_icon.svg" />认证中</span>-->
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								邀请码
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="inviteCode" id="inviteCode" value="${supplier.inviteCode}" readonly="readonly"  />
								
							</div>
						</div>
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">
	                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
	                        	所属行业
	                        </label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <input type="text" class="input-text radius down_list" id="goodsType" name="goodsType" readonly placeholder="请选择">
	                            <input type="text" class="downList_val" name="industry" id="industry"/>
	                            <ul class="downList_con">
	                                [#list industrys as industry]
	                                    <li val="${industry}"[#if industry == supplier.industry] class="li_bag"[/#if] >${message("admin.supplier.industry." + industry)}</li>
	                                [/#list]
	
	                            </ul>
	
	                        </div>
	                    </div>
						<div class="row cl">
	                        <label class="form-label col-xs-4 col-sm-3">企业logo</label>
	                        <div class="formControls col-xs-8 col-sm-7">
	                            <div class="updateImg">
	                                <div class="img_box">
	                                	[#if supplier.imagelogo??]
                                        <img src="${supplier.imagelogo}" />
                                    	[/#if]
	                                </div>
	                                <input type="text" style="display:none" id="imagelogo" name="imagelogo" value="${supplier.imagelogo}"/>
	                                <a id="filePicker" class="file" style="display:block;width:60px;height:60px;margin:30px;opacity: 0" >kjlkjlkjjklkjjj;lkjlkjlkjlkjlkjlkj</a>
	                                <div class="img_model">
	                                    <span class="delImg"></span>
	                                </div>
	                            </div>
	                            <span class='tishi'>(建议上传的logo的尺寸为80*80)</span>
	                        </div>
	                    </div>
	                    <div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								法人姓名
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="legalPersonName" id="legalPersonName" value="${supplier.legalPersonName}" />
								
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								联系人
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="userName" id="userName" value="${supplier.userName}" maxlength="20" />
								
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								联系方式
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="tel" id="tel" value="${supplier.tel}"  maxlength="20" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								公司地址
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<span class="fieldSet">
									<input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}" treePath="${(supplier.area.treePath)!}" />
								</span>
								<div >
									<input type="text" class="input-text radius" id="address" name="address" placeholder="详细地址"  maxlength="20" autocomplete="off" value="${supplier.address}"/>
								</div>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								电子邮箱
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="email" id="email" value="${supplier.email}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								客服电话
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="customerServiceTel" id="customerServiceTel" value="${supplier.customerServiceTel}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								公司介绍
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<textarea rows="5" class="text_area" cols="50" rows="5" id="companyProfile" name="companyProfile" >${supplier.companyProfile}</textarea>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
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
						<input class="btn radius confir_S" type="button" id="yes" value="${message("admin.common.submit")}" />
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
			
			
			var $filePicker = $("#filePicker");
			var $fileimagelogo = $("#fileimagelogo");
			var businessCards=new Array();
			var $enterpriseinfo=$("#enterpriseinfo");
			var $name=$("#name");
			var $status=$("#status");
			
			//未认证
			if($status.val() == 'notCertified') {
				$("#renzheng").append("<img src='${base}/resources/admin1.0/images/weirenzheng_icon.svg' />未认证");
			}
			//已认证
			if($status.val() == 'verified') {
				$("#renzheng").append("<img src='${base}/resources/admin1.0/images/yirenzheng_icon.svg' />已认证");
				$('#name').attr("disabled",true);
			}
			//认证中
			if($status.val() == 'certification') {
				$("#renzheng").append("<img src='${base}/resources/admin1.0/images/renzhengzhong_icon.svg' />认证中");
				$('#name').attr("disabled",true);
			}
			//认证未通过
			if($status.val() == 'authenticationFailed') {
				$("#renzheng").append("<img src='${base}/resources/admin1.0/images/renzhengshibai_icon.svg' />认证失败");
			}
			
			
			
			
			/*通过js获取页面高度，来定义表单的高度*/
			var formHeight=$(document.body).height()-100;
			$(".form_box").css("height",formHeight);
			
			
			$(".treeImg").delegate('li .todelImg','click',function(){
				$(this).closest("li").remove();
				var index = $(this).closest("li").index();
				businessCards.splice(index,1)
			})
		
			// 地区选择
		    $("#areaId").lSelect({
		        url: "${base}/admin/common/area.jhtml"
		    });
			
			/*上传图片*/
			var $filePicker = $("#filePicker");
	        $filePicker.uploader({
	            before:function(file){
	                var fr = new FileReader();
	                var file = file.source.source ;
	                fr.onload = function () {
	                    $(".updateImg .img_box").html("<img src='' />");
	                    $(".updateImg img").attr("src", fr.result);
	                };
	                fr.readAsDataURL(file);
	                
	            },
	            complete:function(){
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
	        
	      	//显示工商证件
		  	 if($("#businessCard").val() != ''){
		 		var businessCardImg=eval('(' + $("#businessCard").val() + ')');
		 		for(var o in businessCardImg){  
		 	         $("#treeImg").append("<li><a href='"+businessCardImg[o].imgUrl+"' target='view_window'><img src='"+businessCardImg[o].imgUrl+"' height='100px' width='110px' /></a></li>");
		 	      } 
		 	}else{
		 		$("#treeImg").append("<p style='line-height:36px;color:#586897;'>无</p>");
		 	}
	      
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
					
				})
		        
	        
	        
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
	            					parent.location.reload();
	        					}, 2000);
	    					}else{
	    						$.message("warn","提交失败！");
	    						$('#yes').removeAttr("disabled");
	    					}
	    				}
	    			});
	    	});
            $("body").click(function(){
                window.top.$(".show_news").removeClass("show");
            })

		});
		</script>
	</body>
</html>
[/#escape]