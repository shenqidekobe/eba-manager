[#escape x as x?html]
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
		<link rel="stylesheet" href="${base}/resources/shop/common/js/element-ui/lib/theme-default/index.css">
		<style>
			.form-group{width:450px;float:left;}
			.form-control{display:inline-block;margin-right:10px;}
			.form-group>label{padding-top:7px;}
			.radio label{margin-right:10px;}
			.radio_label{
				padding-top:5px;
			}
			.radio_label input{float:left;margin-top:2px;}
			.radio_label span{float:left;padding-top: 3px;font-weight: 100;}
			
			#marketPrice+.error{
				float:right;
				padding:5px 14px 0 0;
			}
			.el-input__inner{
				border:1px solid #ccc;
			}
			.el-input__icon+.el-input__inner{width:210px;}
			.el-cascader-menu__item{color:333;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<form id="saveForm" method="post" action="save.jhtml">
		<input name="id" value="${goods.id}" type="hidden"/>
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="content_box">
						<div class="con_title">
							发布供应信息
						</div>
						<div class="con_form">
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />产品分类</label>
								    <div class="col-sm-9">
								     	<div id="app">
										  	<el-cascader
										    	:options="options"
										    	v-model="selectedOptions"
										    	@change="handleChange">
										  	</el-cascader>
										  	<input type="hidden" :value="selectedOptions | arrLast" name="categoryId"/>
									  	</div>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />产品名称</label>
								    <div class="col-sm-9">
								     	<input name="name" class="form-control" type="text" value="${goods.name}"/>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">货源类型</label>
								    <div class="col-sm-9">
										<select class="form-control" name="sourceType">
											<option value="">请选择</option>
								     		[#list sourceTypes as sourceType]
											<option value="${sourceType}"
												[#if goods.sourceType == sourceType]selected[/#if]
											>${sourceType.getName()}</option>
											[/#list]
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">保值条件</label>
								    <div class="col-sm-9">
								     	<select class="form-control" name="storageConditions">
								     		<option value="">请选择</option>
								     		[#list storageConditions as storageCondition]
											<option value="${storageCondition}"
												[#if goods.storageConditions == storageCondition]selected[/#if]
											>${storageCondition.getName()}</option>
											[/#list]
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">保质期</label>
								    <div class="col-sm-9">
								     	<input name="shelfLife" class="form-control" type="text" value="${goods.shelfLife}"/>天
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">基本单位</label>
								    <div class="col-sm-9">
								     	<select class="form-control" name="unit">
								     		<option value="">请选择</option>
								     		[#list units as unit]
												<option value="${unit}"
													[#if goods.unit == unit]selected[/#if]
												>${unit.getName()}</option>
											[/#list]
										</select>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">产品规格</label>
								    <div class="col-sm-9">
								     	<input name="goodsSpec" class="form-control" type="text" value="${goods.goodsSpec}"/>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />参考价格</label>
								    <div class="col-sm-9" style="position:relative;">
								     	<div class="input-group">
								        	<span class="input-group-addon">￥</span>
								        	<input name="marketPrice" id="marketPrice" type="text" class="form-control" style="width:170px;" value="[#if goods.marketPrice != -1]${goods.marketPrice}[/#if]"/>
										    <label class="radio_label">
										      	<input type="radio" name="marketPriceRadio" [#if goods.marketPrice == -1]checked[/#if] id="marketPriceRadio" value="-1"><span>面议</span>
										    </label>
								      	</div>
								      	 <span class="priceError"></span>
									</div>
								</div>
								<div class="form-group">
								    <label class="col-sm-4 control-label">展示图片</label>
								    <div class="col-sm-9">
								     	<div class="updateImg">
								     		[#if  goods.image]
									     		<div class="img_box">
									     			<img src="${goods.image}" alt="" />
										    		<div class="caozuoImg">
										    			<a href="${goods.image}" class="see"></a>
										    			<a href="javascript:;" class="del"></a>
										    		</div>
									     		</div>
								     		[/#if]
								     		<input type="hidden" name="image" id="imageHidden" value="${goods.image}"/>
								     		<div id="picker">请选择图片上传</div>
								     		<div class="img_model">
								     			<span class="delImg"></span>
								     		</div>
								     	</div>
								     	<p class="gray">图片尺寸为<span class="orange">800px*800px</span>，每张最大<span class="orange">2M</span></p>
								    </div>
								</div>
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label">产品详细</label>
								    <div class="col-sm-9" style="width:700px">
								     	<textarea id="description" name="introduction" class="editor" style="width: 100%;">${goods.introduction}</textarea>
								    </div>
								</div>
												
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label"></label>
								    <div class="col-sm-9" style="width:700px">
								     	<button type="button" class="hold_B" id="subBtn">保存</button>
								     	<button type="button" class="cancel_B" onclick="javascript:history.back(-1)">取消</button>
								    </div>
								</div>
							
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
		[#include "/shop/common/foot.ftl"]
		
		<script src="${base}/resources/shop/common/js/jquery.min.js"></script>
		<script src="${base}/resources/shop/common/js/webuploader.js"></script>
		<script src="${base}/resources/shop/common/ueditor/ueditor.js"></script>
		<script src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script src="${base}/resources/shop/common/js/public.js"></script>
		<script src="${base}/resources/shop/common/js/vue.min.js"></script>
  		<script type="text/javascript" src="${base}/resources/shop/common/js/element-ui/lib/index.js"></script>
		
		<script type="text/javascript">
		
		$(function(){
			
			$(".el-cascader").on("click",function(event){
				$(".el-input__inner").addClass("borderFocus");
				event.stopPropagation();
			});
			$(document).on("click",function(){
				$(".el-input__inner").removeClass("borderFocus");
			})

			$("#description").editor();
			
			$("#marketPriceRadio").on("click",function(){
				$("#marketPrice-error").css("display","none");
			})
			
			$("#subBtn").on("click", function(){
				if($("#marketPriceRadio").prop("checked")){
					$("#marketPrice").val(-1);
					$("#marketPrice").css("visibility","hidden");
				}
				
				$("#saveForm").submit();
				return false;
			});
			
			$("#saveForm").validate({
            	rules: {
            		name:{
            			required:true
            		},
            		marketPrice:{
            			required:{
            				depends: function(element){
	            				return !$("#marketPriceRadio").prop("checked");
	            			}
            			},
            			number:true
            		},
            		categoryId:{
            			required:true
            		}
            	},
				errorPlacement: function (error, element) {
					console.log(element[0].name);
                	if (element[0].name == "marketPrice") {
                    	error.appendTo("span.priceError");
                	}else{
                		error.insertAfter(element);
                	}
            	}
			})
			
		})
		
		
		$("#marketPrice").on("input propertychange",function(){
			if($(this).val().length){
				$("#marketPriceRadio").prop("checked",false);
			}
		})
		$("#marketPriceRadio").on("click",function(){
			if($(this).prop("checked")){
				$("#marketPrice").siblings(".error").css("display","none");
				$("#marketPrice").val("");
			}
			
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
			    fileSingleSizeLimit: 1024 * 1024 * 2,
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
				/*上传图片，服务器返回的数据*/
				//console.log(data);
				$("#picker").before('<div class="img_box">'+
	     			'<img src="'+data.url+'" alt="" />'+
		    		'<div class="caozuoImg">'+
		    			'<a href="'+data.url+'" class="see"></a>'+
		    			'<a href="###" class="del"></a>'+
		    		'</div>'+
	     		'</div>');
	     		$("#imageHidden").val(data.url);
				
				$(".updateImg .del").on("click",function(){
					//alert(123);
					$(this).closest(".img_box").remove();
			    	uploader.removeFile( imgId,true);
			    	$("#imageHidden").val("");
				})
				
			}).on('fileQueued', function(file) {
				/*上传服务器之前，选中图片的信息*/
				//console.log(file);
				imgId = file.id;
			}).on('error', function(type) {
				switch(type) {
					case "F_EXCEED_SIZE":
						alert("上传文件大小超出限制");
						break;
					case "Q_TYPE_DENIED":
						alert("上传文件格式不正确");
						break;
					default:
						alert("上传文件出现错误");
				}
			});
			

     		$(".updateImg .del").on("click",function(){
				$(this).closest(".img_box").remove();
				$("#imageHidden").val("");
		    	//uploader.removeFile( imgId,true);
			})
     		
     		
     	var trees = [];

        var categoryStr = [#noescape]'${categoryRoots?json_string}'[/#noescape];
		
        trees = JSON.parse(categoryStr) ;
        
        var returnData = {};
        returnData.options = trees;
        
        var categoryArray = [];
        if('${ca1}'){
        	categoryArray.push(${ca1});
        }
        if('${ca2}'){
        	categoryArray.push(${ca2});
        }
        if('${ca3}'){
        	categoryArray.push(${ca3});
        }
        
        returnData["selectedOptions"] = categoryArray;
     		
     		new Vue({
	  			el:"#app",
			    data() {
			      	return returnData;
				},
				methods: {
					handleChange(value) {
				    	$("#categoryId-error").css("display","none");
				    	$(".el-input__inner").removeClass("borderFocus");
					}
				},
				filters: {
				    arrLast: function (value) {
				      return value[value.length-1];
				    }
				}
			});
     		
     		
     		
     		
     		
     		
     		
     		
     		
			
			
		</script>

	</body>
</html>
[/#escape]
