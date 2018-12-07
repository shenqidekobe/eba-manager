[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>华奕优选</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.form-group{width:800px;}
			.form-control{display:inline-block;margin-right:10px;}
			.con_title span{font-weight:500;color:red;font-size:12px;margin-left:20px;}
			.imgError{
				display:inline-block;
				width:30px;
				height:120px;
				line-height: 120px;
			}
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
							认证管理 <span>
							[#if exist == 0]
								原因：
								[#if supplier.reasons ==""]
									无
								[#else]
									${supplier.reasons}
								[/#if]
							[/#if]
							</span>
						</div>
						<div class="con_form">
							<form id="certification" class="form-horizontal">
								<input type="hidden" id="id" name="id" value="${supplier.id}" />
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />企业名称</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="name" id="name" value="${supplier.name}" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />所属行业</label>
								    <div class="col-sm-9">
								     	<select name="industry" id="industry" class="form-control">
								     		<option value="">请选择</option>
								     		[#list industrys as industry]
												<option value="${industry}"[#if industry == supplier.industry] selected="selected"[/#if] >
													${message("admin.supplier.industry." + industry)}
												</option>
											[/#list]
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">企业logo</label>
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
								     		<input type="hidden" id="imagelogo" name="imagelogo" value="${supplier.imagelogo}" />
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
								     	<input class="form-control" type="text" name="legalPersonName" id="legalPersonName" value="${supplier.legalPersonName}" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />联系人</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="userName" id="userName" value="${supplier.userName}" maxlength="20" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />手机号</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="tel" id="tel" value="${supplier.tel}"  maxlength="20" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />地址</label>
								    <div class="col-sm-9">
								     	<span class="fieldSet">
											<input type="hidden" id="areaId" name="areaId" value="${supplier.area.id}" treePath="${(supplier.area.treePath)!}" />
										</span>
										<span class="areaError"></span>
										<div>
											<input class="form-control" style="width:300px" type="text" id="address" name="address" placeholder="详细地址"  maxlength="20" autocomplete="off" value="${supplier.address}" />
										</div>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">客服电话</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" name="customerServiceTel" id="customerServiceTel" value="${supplier.customerServiceTel}" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />工商证件</label>
								    <div class="col-sm-9">
								    	<input type="hidden" id="businessCard" name="businessCard" value="${supplier.businessCard}" class="form-control" />
								     	<div class="updateImg zhengjianImg" style="float:left">
								     		<div id="filePicker">请选择图片上传</div>
								     	</div>
								     	<span class="imgError"></span>
								     	<p class="gray" style="display: block;width:95%;float:left;">(若企业三证合一只需上传一张图片，否则需要上传三张证件图片)</p>
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
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript">
		var businessCards=new Array();
		$(function(){
			var $certification=$("#certification");
			var $businessCard = $("#businessCard");
			
			[@flash_message /]
			// 地区选择
		    $("#areaId").lSelect({
		        url: "${base}/admin/common/area.jhtml"
		    });
			
			//上传企业logo
			var token = getCookie("token");
			var uploadUrl = "/admin/file/upload.jhtml?fileType=image&token=" + token ;
			var fileUrl = {};
			var imgId="";
			
			
			var uploader = WebUploader.create({
			   	swf: '/resources/admin1.0/flash/webuploader.swf',
			    server:uploadUrl,
			    pick: '#picker',
			    fileSingleSizeLimit: 1024 * 1024 * 30,
			    accept: {
			    	extensions: 'jpg,png,gif,jepg,svg'
				},
				fileNumLimit: 1,
				auto: true,
			    resize: false,
                fileVal : 'file'
			}).on('uploadAccept', function(file, data) {
				
				$("#picker").before('<div class="img_box">'+
	     			'<img src="'+data.url+'" alt="" />'+
		    		'<div class="caozuoImg">'+
		    			'<a href="'+data.url+'" class="see"></a>'+
		    			'<a href="###" class="del"></a>'+
		    		'</div>'+
	     		'</div>');
	     		$("#imagelogo").val(data.url);
			
				$(".updateImg .del").on("click",function(){
					$(this).closest(".img_box").remove();
			    	uploader.removeFile( imgId,true);
			    	$("#imagelogo").val("");
				})
				
			}).on('fileQueued', function(file) {
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
			
			//上传工商证件
			if($businessCard.val() != ""){
				var businessCardImg = JSON.parse($businessCard.val());
				for(var i=0;i<businessCardImg.length;i++) {
					businessCards.push(businessCardImg[i].imgUrl);
					$(".zhengjianImg").before('<div class="imgs_box">'+'<img src="'+businessCardImg[i].imgUrl+'" alt="" />'+
				    		'<div class="caozuoImg">'+
				    			'<a href="'+businessCardImg[i].imgUrl+'" class="see"></a>'+
				    			'<a href="###" imgsId="'+imgsId+'" class="del"></a>'+
				    		'</div>'+
			     		'</div>');
				}
			}
			
			
			$(".imgs_box .del").on("click",function(){
				$(this).closest(".imgs_box").remove();
				imgUrl = $(this).siblings("a").prop("href");
				businessCards.splice($.inArray(imgUrl, businessCards), 1); 
				console.log(businessCards);
				if(!businessCards.length){
					$(".imgError .error").css("display","inline-block");
				}
				if(businessCards.length < 3){
					$(".zhengjianImg").css("display","inline-block");
				}
			});
			if(businessCards.length == 3){
				$(".zhengjianImg").css("display","none");
			}
			
			var imgsId = "";
			var uploaders = WebUploader.create({
			   	swf: '/resources/admin1.0/flash/webuploader.swf',
			    server:uploadUrl,
			    pick: '#filePicker',
			    fileSingleSizeLimit: 1024 * 1024 * 30,
			    accept: {
			    	extensions: 'jpg,png,gif,jepg,svg'
				},
				fileNumLimit: 3,
				auto: true,
			    resize: false,
                fileVal : 'file'
			}).on('uploadAccept', function(file, data) {
				$(".zhengjianImg").before('<div class="imgs_box">'+
	     			'<img src="'+data.url+'" alt="" />'+
		    		'<div class="caozuoImg">'+
		    			'<a href="'+data.url+'" class="see"></a>'+
		    			'<a href="###" imgsId="'+imgsId+'" class="del"></a>'+
		    		'</div>'+
	     		'</div>');
				businessCards.push(data.url);
				
				$("#businessCard").val(businessCards);
				
				if(businessCards.length){
					$(".imgError .error").css("display","none");
				}
				if(businessCards.length >= 3){
					$(".zhengjianImg").css("display","none");
				}else{
					$(".zhengjianImg").css("display","block");
				}
				
				$(".imgs_box .del").on("click",function(){
					$(this).closest(".imgs_box").remove();
					imgsId = $(this).attr("imgsId");
			    	uploaders.removeFile( imgsId,true);
					imgUrl = $(this).siblings("a").prop("href");
					businessCards.splice($.inArray(imgUrl, businessCards), 1); 
					if(!businessCards.length){
						$(".imgError .error").css("display","inline-block");
					}
					if(businessCards.length < 3){
						$(".zhengjianImg").css("display","inline-block");
					}
				})
				
			}).on('fileQueued', function(file) {
				imgsId = file.id;
				console.log(businessCards);
			}).on('error', function(type) {
				switch(type) {
					case "F_EXCEED_SIZE":
						$.message("warn", "上传文件大小超出限制");
						break;
					default:
						$.message("warn", "上传文件出现错误");
				}
			});
			
			
			
			/*验证*/
			$certification.validate({
				rules:{
					name: {
						required: true
					},
					industry: {
						required: true
					},
					userName: {
						required: true
					},
					tel: {
						required: true,
		                pattern: /^1[3|4|5|7|8]\d{9}$/
					},
					areaId: {
						required: true
					},
					address:{
					    required:true
					},
					businessCard:{
						required:true
					},
					legalPersonName:{
						required:true
					}
				},
				errorPlacement: function (error, element) {
					console.log(element[0].name);
                	if (element[0].name == "businessCard") {
                    	error.appendTo("span.imgError");
                	}else if(element[0].name == "areaId"){
                		error.appendTo("span.areaError");
                	}else{
                		error.insertAfter(element);
                	}
            	}
				
			});
			

	        $("#yes").on("click",function(){
	    		//验证
	        	if(!$certification.valid()){
		            return false ;
		     	}
	    		
	    		//验证企业名称
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
					$.message("warn", "企业名称已注册");
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
	    			{name:"businessCard",value: JSON.stringify(businessCardsJson)},
	    			{name:"customerServiceTel",value:$("#customerServiceTel").val()},
	    			{name:"legalPersonName",value:$("#legalPersonName").val()}
	    		);
	    		$("#yes").attr('disabled',"true");
	    		$.ajax({
	    				url: "update.jhtml",
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
		   
		})
			
		
			
			
			
			
			
			
			
			
		</script>

	</body>
</html>
[/#escape]