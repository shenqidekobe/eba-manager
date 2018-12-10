<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>微信小程序</title>
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
							<form class="form-horizontal">
								<div class="form-group">
								    <label class="col-sm-4 control-label">用户名</label>
								    <div class="col-sm-9">
								     	<span class="input_no_span">阿里巴巴</span>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">手机号</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" />
								     	<span class="bindTell blue">绑定手机号</span>
								     	<span class="replaceTell blue">更换手机号</span>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />企业名称</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />所属行业</label>
								    <div class="col-sm-9">
								     	<select class="form-control">
											<option>1</option>
											<option>2</option>
											<option>3</option>
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">logo</label>
								    <div class="col-sm-9">
								     	<div class="updateImg">
								     		<div class="img_box">
								     			<img src="../../../../../resources/shop/common/images/5.jpg" alt="" />
									    		<div class="caozuoImg">
									    			<a href="" class="see"></a>
									    			<a href="javascript:;" class="del"></a>
									    		</div>
								     		</div>
								     		<div id="picker">请选择图片上传</div>
								     		<div class="img_model">
								     			<span class="delImg"></span>
								     		</div>
								     	</div>
								     	<p class="gray">图片尺寸为<span class="orange">200px*200px</span>，每张最大<span class="orange">2M</span></p>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />联系人</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />手机号</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />公司地址</label>
								    <div class="col-sm-9">
								     	<span class="fieldSet">
											<input type="hidden" id="areaId" name="areaId" />
										</span>
										<div>
											<input class="form-control" style="width:300px" type="text" />
										</div>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />电子邮箱</label>
								    <div class="col-sm-9">
								     	<input class="form-control" type="text" />
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">公司简介</label>
								    <div class="col-sm-9">
								     	<textarea class="form-control" rows="5"></textarea>
								    </div>
								</div>
								
								
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label"></label>
								    <div class="col-sm-9" style="width:700px">
								     	<button type="button" class="hold_B">保存</button>
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
		<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
		<script type="text/javascript">
		
		$(function(){
		
			// 地区选择
		    $("#areaId").lSelect({
		        url: "${base}/admin/common/area.jhtml"
		    });
		    
		    $(".bindTell").click(function(){
		    	$.dialog({
	                title: "绑定手机号",
	                width:600,
	                height:330,
	                content: [@compress single_line = true]
                	'<form id="" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:50px 80px;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />绑定手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" style="width:140px;" \/><button type="button" class="code_B bindCode_B">获取验证码<\/button>
							    <\/div>
							<\/div>
                		<\/div>
                    <\/form>'
                [/@compress],
	                onOk: function() {
	                    
	                },
	                onShow:function(){
	                	$(".bindCode_B").on("click",function(){
	                		var bindCode_B = $(this);
	                		clickButton(bindCode_B);
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
                	'<form id="" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:40px 80px;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />原手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" style="width:140px;" \/><button type="button" class="code_B replaceCode_B1">获取验证码<\/button>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />新手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" style="width:140px;" \/><button type="button" class="code_B replaceCode_B1">获取验证码<\/button>
							    <\/div>
							<\/div>
                		<\/div>
                    <\/form>'
                [/@compress],
	                onOk: function() {
	                    
	                },
	                onShow:function(){
	                	$(".bindCode_B").on("click",function(){
	                		var bindCode_B = $(this);
	                		clickButton(bindCode_B);
	                	})
			
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
                fileVal : 'multipartFile'
			}).on('uploadAccept', function(file, data) {
				
				$("#picker").before('<div class="img_box">'+
	     			'<img src="'+data.url+'" alt="" />'+
		    		'<div class="caozuoImg">'+
		    			'<a href="'+data.url+'" class="see"></a>'+
		    			'<a href="javascript:;" class="del"></a>'+
		    		'</div>'+
	     		'</div>');
	     		//$("#imageHidden").val(data.url);
				
				$(".updateImg .del").on("click",function(){
					//alert(123);
					$(this).closest(".img_box").remove();
			    	uploader.removeFile( imgId,true);
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
			
			
		</script>

	</body>
</html>
