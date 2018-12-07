<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title>华奕优选</title>
		<link rel="stylesheet" href="${base}/resources/shop/common/config/bootstrap/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/index.css" />
		<link rel="stylesheet" href="${base}/resources/shop/common/css/member.css" />
		<style>
			.form-group{width:800px;}
			.form-control{display:inline-block;margin-right:10px;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="con_form">
						<form class="form-horizontal">
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
							    <label class="col-sm-4 control-label">企业logo</label>
							    <div class="col-sm-9">
							     	<div class="updateImg">
							     		<!--<div class="img_box">
							     			<img src="../../../../../resources/shop/common/images/5.jpg" alt="" />
								    		<div class="caozuoImg">
								    			<a href="" class="see"></a>
								    			<a href="javascript:;" class="del"></a>
								    		</div>
							     		</div>-->
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
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />地址</label>
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
							    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />工商证件</label>
							    <div class="col-sm-9">
							    	<div class="imgs_box">
							    		<img src="../../../../../resources/shop/common/images/5.jpg" alt="" />
							    		<div class="caozuoImg">
							    			<a href="" class="see"></a>
							    			<a href="javascript:;" class="del"></a>
							    		</div>
							    	</div>
							    	<div class="imgs_box">
							    		<img src="../../../../../resources/shop/common/images/5.jpg" alt="" />
							    		<div class="caozuoImg">
							    			<a href="" class="see"></a>
							    			<a href="javascript:;" class="del"></a>
							    		</div>
							    	</div>
							    	<div class="imgs_box">
							    		<img src="../../../../../resources/shop/common/images/5.jpg" alt="" />
							    		<div class="caozuoImg">
							    			<a href="" class="see"></a>
							    			<a href="javascript:;" class="del"></a>
							    		</div>
							    	</div>
							     	<div class="updateImg">
							     		<div id="">请选择图片上传</div>
							     	</div>
							     	<p class="gray">(若企业三证合一只需上传一张图片，否则需要上传三张证件图片)</p>
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
		[#include "/shop/common/foot.ftl"]
		
		
		
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/webuploader.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>		
		<script type="text/javascript" src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/jquery.lSelect.js"></script>
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
	                height:300,
	                content: [@compress single_line = true]
                	'<form id="" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:50px 0;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label">绑定手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label">验证码<\/label>
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
	                height:420,
	                content: [@compress single_line = true]
                	'<form id="" class="form form-horizontal" action="" method="">
                		<div class="modelCon" style="padding:40px 0;">
                			
                			<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label">原手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label">验证码<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" style="width:140px;" \/><button type="button" class="code_B replaceCode_B1">获取验证码<\/button>
							    <\/div>
							<\/div>
							<div class="form-group" style="margin-bottom:30px">
							    <label class="col-sm-4 control-label">新手机号<\/label>
							    <div class="col-sm-7">
							     	<input class="form-control" type="text" \/>
							    <\/div>
							<\/div>
							<div class="form-group">
							    <label class="col-sm-4 control-label">验证码<\/label>
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
			
		
		
			var uploadUrl = "viewMore.jhtml" ;
			var fileUrl = {};
				
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
				auto: false,
			    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
			    resize: false,
                fileVal : 'multipartFile'
			}).on('uploadAccept', function(file, data) {
				console.log(data);
				console.log(file);

			}).on('fileQueued', function(file) {
				console.log(file);
				var  $imgBox = $(".updateImg .img_box");
/*				$imgBox.append( '<img src="'+ file +'"/>' );*/
				$(".delFil").off("click").on("click",function(){
			    	var fileId = $(this).parent().attr("id");
			    	uploader.removeFile( fileId,true);
			    	$(this).parent().remove();
			    })
			}).on('error', function(type) {
				
			});
			
			
		</script>

	</body>
</html>
