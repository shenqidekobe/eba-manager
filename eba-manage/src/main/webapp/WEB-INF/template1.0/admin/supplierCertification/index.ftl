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
			.tishi{color:#999;vertical-align: bottom;}
			.col-sm-7{width:85%;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
					<li>企业认证</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="certification" class="form form-horizontal">
					<input type="hidden" id="id" name="id" value="${supplier.id}" />
					<div class="info_text">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								企业名称
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="name" id="name" value="${supplier.name}" />
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
	                                <a id="filePickerImg" class="file" style="display:block;width:60px;height:60px;margin:30px;opacity: 0" >kjlkjlkjjklkjjj;lkjlkjlkjlkjlkjlkj</a>
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
								手机号
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="tel" id="tel" value="${supplier.tel}"  maxlength="20" />
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								地址
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
								客服电话
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="customerServiceTel" id="customerServiceTel" value="${supplier.customerServiceTel}"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								工商证件
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								
								<ul class="treeImg">
									
								</ul>
								<span class="treeUpdate">
									<span class="fieldSet">
										<input type="text" id="image" name="image" class="text" maxlength="200" style="display:none" value="${supplier.businessCard}"/>
										<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
									</span>
								</span>
								<span style="display:inline-block;margin-top:100px;" class='tishi'>(若企业三证合一只需上传1张图片，否则需上传3张证件图片)</span>
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
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        <script type="text/javascript">
        
        
			$().ready(function() {
				var $certification=$("#certification");
				/*通过js获取页面高度，来定义表单的高度*/
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
	        	
				[@flash_message /]
				
				var businessCards=new Array();
				var $image=$("#image");
				var $filePicker = $("#filePicker");
				
				$filePicker.uploader({complete:function(){
					$(".treeImg").html("");
					businessCards.push($image.val());
					var imgTr;
					for(var i=0;i<businessCards.length;i++){
						imgTr+="<li><a href='"+businessCards[i]+"' target='view_window'><img src='"+businessCards[i]+"' height='100px' width='110px' /></a><span class='delImg'><a target='_blank' href='"+businessCards[i]+"' class='toSee'><img src='${base}/resources/admin1.0/images/chakanbai_icon.svg' /></a><img src='${base}/resources/admin1.0/images/shanchudj_icon.svg' class='todelImg' /></span></li>";
					};
					$(".treeImg").append(imgTr);
					
					var $children = $(".treeImg").children();
            		$(".treeImg").empty().append($children);
				},before:function(){
					if(businessCards.length >= 3){
						$.message("warn", "最多只能上传3张图片！");
						return false;
					}
				}});
				
				
				
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
				var $filePickerImg = $("#filePickerImg");
		        $filePickerImg.uploader({
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
						image:{
							required:true
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
							required: "所属行业不能为空"
						},
						userName:{
							required:"联系人不能为空"
						},
						tel:{
							required:"手机号不能为空"
						},
						areaId:{
							required:"必填"
						},
						address:{
							required:"地址不能为空"
						},
						image:{
							required:"企业证件不能为空"
						},
						legalPersonName:{
							required:"法人姓名不能为空"
						}
					}
					
				})
		        
				
		        var businessCards=new Array();
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
		            					//parent.location.reload();
		            					window.location.href="reviewing.jhtml";
		        					}, 2000);
		    					}else{
		    						$.message("warn","提交失败！");
		    						$('#yes').removeAttr("disabled");
		    					}
		    				}
		    			});
		    	});
		        
		        
				
				$("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})
			});
			
		</script>
	</body>
</html>
[/#escape]