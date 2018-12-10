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
			.radio_label{padding-top:5px;}
			.radio_label input{float:left;margin-top:2px;}
			.radio_label span{float:left;padding-top: 3px;font-weight: 100;}
			#needNum+.error{
				float:right;
				padding:6px 14px 0 0;
			}
			.el-input__inner{
				border:1px solid #ccc;
			}
			.el-input__icon+.el-input__inner{padding-right:44px;}
			.el-cascader-menu__item{color:333;}
		</style>
	</head>
	<body class="reg10">
		[#include "/shop/common/head.ftl"]
		<form id="saveForm" method="post" action="savePur.jhtml">
		<input name="id" value="${goods.id}" type="hidden"/>
		<div class="page_con">
			<div class="con_center">
				[#include "/shop/member/inc.ftl"]
				<div class="content">
					<div class="content_box">
						<div class="con_title">
							发布采购信息
						</div>
						<div class="con_form">
								<div class="form-group">
								    <label class="col-sm-4 control-label"><img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />产品分类</label>
								    <div class="col-sm-7">
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
								     	<input name="goodsSpec" class="form-control" type="text"  value="${goods.goodsSpec}"/>
								    </div>
								</div>
								<div class="form-group" style="width:600px">
								    <label class="col-sm-4 control-label">
								    	<img src="${base}/resources/shop/common/images/bitian_icon.svg" alt="" />
								    		采购数</label>
								    <div class="col-sm-9">
								     	<div class="input-group">
								        	<input name="needNum" id="needNum" class="form-control" type="text" value="[#if goods.needNum != -1]${goods.needNum}[/#if]" />
										    <label class="radio_label">
										      	<input type="radio" id="needNumRadio" [#if goods.needNum == -1]checked[/#if] value="-1"><span>面议</span>
										    </label>
								      	</div>
								      	 
									</div>
								</div>
							
								<div class="form-group" style="width:900px">
								    <label class="col-sm-4 control-label">采购详细</label>
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
		<script src="${base}/resources/admin1.0/js/webuploader.js"></script>
		<script src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
		<script src="${base}/resources/shop/common/js/validate/jquery.validate.min.js"></script>
		<script src="${base}/resources/shop/common/js/common.js"></script>
		<script type="text/javascript" src="${base}/resources/shop/common/js/public.js"></script>
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
			
			
			$("#subBtn").on("click", function(){
				if($("#needNumRadio").prop("checked")){
					$("#needNum").val(-1);
					$("#needNum").css("visibility","hidden");
				}
				$("#saveForm").submit();
				return false;
			});
			
			$("#needNum").on("input propertychange",function(){
				if($(this).val().length){
					$("#needNumRadio").prop("checked",false);
				}
			})
			$("#needNumRadio").on("click",function(){
				if($(this).prop("checked")){
					$("#needNum").siblings(".error").css("display","none");
					$("#needNum").val("");
				}
				
			})
			
			
			$("#saveForm").validate({
            	rules: {
            		name:{
            			required:true
            		},
            		needNum:{
            			required:{
            				depends: function(element){
	            				return !$("#needNumRadio").prop("checked");
	            			}
            			},
            			number:true
            		},
            		categoryId:{
            			required:true
            		}
            	}
			})	
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
