[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.role.add")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.fole_text .row{width:45%;}
			.fole_text{padding:20px 0 10px 10px;overflow:hidden;}
			.role_check{padding-left:10px;}
			.role_check .row{margin-bottom:25px;}
			.role_check .col-sm-7{width:85%;}
			.form_box{overflow: auto;}
			.form-horizontal .form-label{width:140px;}
			.check-box{width:150px;}
			.selectAll{color:#333;}
			.selectAll:hover {color:#4DA1FF;}
		</style>
	</head>
	<body >
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li>${message("admin.role.add")}</li>
				</ul>
			</div>
			<div class="form_box">
				<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
					<div class='fole_text'>
						<div class="row cl" style="float:left;">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								${message("Role.name")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="name" id="name" maxlength="20" />
							</div>
						</div>
						<div class="row cl" style="float:left;">
							<label class="form-label col-xs-4 col-sm-3">
								${message("Role.description")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" placeholder="" name="description" id="description" maxlength="200" />
							</div>
						</div>
					</div>
					<div class="role_check">
						<div class="row cl">
                        	<label class="form-label col-xs-4 col-sm-3">
                        		权限分配
                        	</label>
                        	<div class="formControls col-xs-8 col-sm-7" > 
                        		<div name="firstDiv">
			                      	<div class="check-box">
			                          <input type="checkbox" name="authorities" value="admin:homePage" /><span style="font-weight:bolder;">首页</span>
			                        </div>
		                   		</div>
                        		<div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one"/><span style="font-weight:bolder;">商品</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"  /><span>分类管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:list" /><span>分类列表</span>
				                        </div>
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:add" /><span>添加分类</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:edit" /><span>编辑分类</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:delete" /><span>删除分类</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>规格管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:list" /><span>规格列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:add" /><span>添加规格</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:edit" /><span>编辑规格</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:delete" /><span>删除规格</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>商品管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:add" /><span>商品列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:add" /><span>添加商品</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:edit" /><span>编辑商品</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:delete" /><span>删除商品</span>
				                        </div>
				                       <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:distribution" /><span>分销商品</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:importMore" /><span>导入商品</span>
				                        </div>-->
			                        </div>
		                       	</div>
		                    </div>
		                    <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">订单</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>订货单</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<!--<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:orderBatchPrint" /><span>批量打印</span>
				                        </div>	-->
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:checkBatchReview" /><span>批量审核</span>
				                        </div>	
				                       <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:getOut" /><span>订单导出</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:order" /><span>订单打印</span>
				                        </div>	-->
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:list" /><span>订单列表</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:review" /><span>订单审核</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:updateItems" /><span>订单修改</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:shipping" /><span>订单发货</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:addRemarks" /><span>订单备注</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancel" /><span>订单取消</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:complete" /><span>订单完成</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:returns" /><span>确认退货</span>
				                        </div>		
			                        </div>
		                       	</div>
		                   </div>
		                   <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one"/><span style="font-weight:bolder;">报表</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>订单报表</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:orderReport:orderList" /><span>查看订货单报表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>商品报表</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:commodityReport:orderForm" /><span>查看订货商品报表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       <!--	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>客户报表</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:customerReport" /><span>查看客户订货报表</span>
				                        </div>	
			                        </div>
		                       	</div>-->
		                    </div>
		                     <div name="firstDiv"> 
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">内容</span>
		                        </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>广告管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:ad" /><span>广告</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>广告位管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:adPosition" /><span>广告位</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
		                    <div name="firstDiv"> 
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">用户</span>
		                        </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>用户管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:list" /><span>用户列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:edit" /><span>编辑用户</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:view" /><span>查看用户</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:income" /><span>收益记录</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>提现管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:withdraw:list" /><span>提现列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:withdraw:edit" /><span>编辑提现</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
		                    <div name="firstDiv"> 
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">系统</span>
		                        </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>角色管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:list" /><span>角色列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:add" /><span>添加角色</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:edit" /><span>编辑角色</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:delete" /><span>删除角色</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>部门管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:list" /><span>部门列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:add" /><span>添加部门</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:edit" /><span>编辑部门</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:delete" /><span>删除部门</span>
                                        </div>
                                    </div>
                                </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>员工管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:list" /><span>员工列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:add" /><span>添加员工</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:edit" /><span>编辑员工</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:delete" /><span>删除员工</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>数据配置</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:dict:edit" /><span>编辑配置</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>产品鉴真管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ver:list" /><span>鉴真列表</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ver:add" /><span>添加鉴真</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ver:impl" /><span>创建TXT</span>
				                        </div>		
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>日志管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:log" /><span>日志列表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                    </div>
	                    
	                </div>
					<div class="footer_submit">
						<input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}" />
						<input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
					</div>	
				</form>
			</div>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/admin1.0/js/list.js"></script>
        <script type="text/javascript">
        
        
			$().ready(function() {
				
				/*通过js获取页面高度，来定义表单的高度*/
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);
	        
				
				[@flash_message /]
				
				//checkbox多级联动
				$("input[name=one]").on("click",function(){
					var $this=$(this);
					if($this.is(":checked")){
						$this.parent().nextAll().find("input").attr("checked",true);
					}else{
						$this.parent().nextAll().find("input").attr("checked",false);
					}
				});
				
				$("input[name=two]").on("click",function(){
					var $this=$(this);
					if($this.is(":checked")){
						$this.parent().nextAll().find("input").attr("checked",true);
						$this.parent().parent().parent().find("input[name=one]").attr("checked",true);
					}else{
						$this.parent().nextAll().find("input").attr("checked",false);
						var hasChecked=false;
						$this.parent().parent().parent().find("input[name=two]").each(function(i){
							if($(this).is(":checked")){
								hasChecked=true;
							}
						});
						if(!hasChecked){
							$this.parent().parent().parent().find("input[name=one]").attr("checked",false);
						}
					}
				});
				
				$("input[name=authorities]").on("click",function(){
					var $this=$(this);
					if($this.parent().parent().attr("name")!="thirdDiv"){
						return;
					}
					if($this.is(":checked")){
						$this.parent().parent().parent().find("input[name=two]").attr("checked",true);
						$this.parent().parent().parent().parent().find("input[name=one]").attr("checked",true);
					}else{
						var hasChecked=false;
						$this.parent().parent().find("input[name=authorities]").each(function(i){
							if($(this).is(":checked")){
								hasChecked=true;
							}
						});
						if(!hasChecked){
							$this.parent().parent().parent().find("input[name=two]").attr("checked",false);
						}
						
						hasChecked=false;
						$this.parent().parent().parent().parent().find("input[name=two]").each(function(i){
							if($(this).is(":checked")){
								hasChecked=true;
							}
						});
						if(!hasChecked){
							$this.parent().parent().parent().parent().find("input[name=one]").attr("checked",false);
						}
					}
				});
				
				var $form = $(".form");
				// 表单验证
				$form.validate({
					rules: {
						name: "required"
					}
				});
				

			});
			
		</script>
	</body>
</html>
[/#escape]