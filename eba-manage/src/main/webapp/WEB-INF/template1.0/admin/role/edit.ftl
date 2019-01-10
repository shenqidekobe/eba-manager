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
				<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">
					<input type="hidden" name="id" value="${role.id}" />
					<div class='fole_text'>
						<div class="row cl" style="float:left;">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								${message("Role.name")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" name="name" value="${role.name}" id="name" maxlength="20" />
							</div>
						</div>
						<div class="row cl" style="float:left;">
							<label class="form-label col-xs-4 col-sm-3">
								${message("Role.description")}
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<input type="text" class="input-text radius" placeholder="" name="description" value="${role.description}" id="description" maxlength="200" />
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
			                          <input type="checkbox" name="authorities" value="admin:homePage" [#if role.authorities?seq_contains("admin:homePage")] checked="checked"[/#if]/><span style="font-weight:bolder;">首页</span>
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
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:list" [#if role.authorities?seq_contains("admin:productCategory:list")] checked="checked"[/#if] /><span>分类列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:add" [#if role.authorities?seq_contains("admin:productCategory:add")] checked="checked"[/#if] /><span>添加分类</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:edit" [#if role.authorities?seq_contains("admin:productCategory:edit")] checked="checked"[/#if] /><span>编辑分类</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:productCategory:delete" [#if role.authorities?seq_contains("admin:productCategory:delete")] checked="checked"[/#if] /><span>删除分类</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>规格管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:list" [#if role.authorities?seq_contains("admin:specification:list")] checked="checked"[/#if] /><span>规格列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:add" [#if role.authorities?seq_contains("admin:specification:add")] checked="checked"[/#if] /><span>添加规格</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:edit" [#if role.authorities?seq_contains("admin:specification:edit")] checked="checked"[/#if] /><span>编辑规格</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:specification:delete" [#if role.authorities?seq_contains("admin:specification:delete")] checked="checked"[/#if] /><span>删除规格</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>商品管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:list" [#if role.authorities?seq_contains("admin:goods:list")] checked="checked"[/#if] /><span>商品列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:add" [#if role.authorities?seq_contains("admin:goods:add")] checked="checked"[/#if] /><span>添加商品</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:edit" [#if role.authorities?seq_contains("admin:goods:edit")] checked="checked"[/#if] /><span>编辑商品</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:delete" [#if role.authorities?seq_contains("admin:goods:delete")] checked="checked"[/#if] /><span>删除商品</span>
				                        </div>
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:distribution" [#if role.authorities?seq_contains("admin:goods:distribution")] checked="checked"[/#if] /><span>分销商品</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goods:importMore" [#if role.authorities?seq_contains("admin:goods:importMore")] checked="checked"[/#if] /><span>导入商品</span>
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
			                            <!--
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:orderBatchPrint" [#if role.authorities?seq_contains("admin:print:orderBatchPrint")] checked="checked"[/#if] /><span>批量打印</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:checkBatchReview" [#if role.authorities?seq_contains("admin:order:checkBatchReview")] checked="checked"[/#if] /><span>批量审核</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:getOut" [#if role.authorities?seq_contains("admin:order:getOut")] checked="checked"[/#if] /><span>订单导出</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:order" [#if role.authorities?seq_contains("admin:print:order")] checked="checked"[/#if] /><span>订单打印</span>
				                        </div>	-->
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:list" [#if role.authorities?seq_contains("admin:order:list")] checked="checked"[/#if] /><span>订单列表</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:review" [#if role.authorities?seq_contains("admin:order:review")] checked="checked"[/#if] /><span>订单审核</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:updateItems" [#if role.authorities?seq_contains("admin:order:updateItems")] checked="checked"[/#if] /><span>订单修改</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:shipping" [#if role.authorities?seq_contains("admin:order:shipping")] checked="checked"[/#if] /><span>订单发货</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:addRemarks" [#if role.authorities?seq_contains("admin:order:addRemarks")] checked="checked"[/#if] /><span>订单备注</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancel" [#if role.authorities?seq_contains("admin:order:cancel")] checked="checked"[/#if] /><span>订单取消</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:complete" [#if role.authorities?seq_contains("admin:order:complete")] checked="checked"[/#if] /><span>订单完成</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:returns" [#if role.authorities?seq_contains("admin:order:returns")] checked="checked"[/#if] /><span>确认退货</span>
				                        </div>	
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:applyCancel" [#if role.authorities?seq_contains("admin:order:applyCancel")] checked="checked"[/#if] /><span>订单用户申请</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:orderShippingInfo" [#if role.authorities?seq_contains("admin:print:orderShippingInfo")] checked="checked"[/#if] /><span>发货单打印</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancelShipped" [#if role.authorities?seq_contains("admin:order:cancelShipped")] checked="checked"[/#if] /><span>发货单作废</span>
				                        </div>	-->
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
				                          	<input type="checkbox" name="authorities" value="admin:orderReport:orderList" [#if role.authorities?seq_contains("admin:orderReport:orderList")] checked="checked"[/#if] /><span>查看订货单报表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>商品报表</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:commodityReport:orderForm" [#if role.authorities?seq_contains("admin:commodityReport:orderForm")] checked="checked"[/#if] /><span>查看订货商品报表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<!--
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>客户报表</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:customerReport" [#if role.authorities?seq_contains("admin:customerReport")] checked="checked"[/#if] /><span>查看客户订货报表</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	-->
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
                                            <input type="checkbox" name="authorities" value="admin:ad" [#if role.authorities?seq_contains("admin:ad")] checked="checked"[/#if]/><span>广告</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>广告位管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:adPosition" [#if role.authorities?seq_contains("admin:adPosition")] checked="checked"[/#if]/><span>广告位</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
		                     <div name="firstDiv"> 
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">会员</span>
		                        </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>会员管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:list" [#if role.authorities?seq_contains("admin:member:list")] checked="checked"[/#if]/><span>会员列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:edit" [#if role.authorities?seq_contains("admin:member:edit")] checked="checked"[/#if]/><span>编辑会员</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:view" [#if role.authorities?seq_contains("admin:member:view")] checked="checked"[/#if]/><span>查看会员</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:income" [#if role.authorities?seq_contains("admin:member:income")] checked="checked"[/#if]/><span>收益记录</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>提现管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:withdraw:list" [#if role.authorities?seq_contains("admin:withdraw:list")] checked="checked"[/#if]/><span>提现列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:withdraw:edit" [#if role.authorities?seq_contains("admin:withdraw:edit")] checked="checked"[/#if]/><span>编辑提现</span>
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
                                            <input type="checkbox" name="authorities" value="admin:role:list" [#if role.authorities?seq_contains("admin:role:list")] checked="checked"[/#if]/><span>角色列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:add" [#if role.authorities?seq_contains("admin:role:add")] checked="checked"[/#if]/><span>添加角色</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:edit" [#if role.authorities?seq_contains("admin:role:edit")] checked="checked"[/#if]/><span>编辑角色</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:role:delete" [#if role.authorities?seq_contains("admin:role:delete")] checked="checked"[/#if]/><span>删除角色</span>
                                        </div>
                                    </div>
                                </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>部门管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:list" [#if role.authorities?seq_contains("admin:department:list")] checked="checked"[/#if]/><span>部门列表</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:add" [#if role.authorities?seq_contains("admin:department:add")] checked="checked"[/#if]/><span>添加部门</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:edit" [#if role.authorities?seq_contains("admin:department:edit")] checked="checked"[/#if]/><span>编辑部门</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:department:delete" [#if role.authorities?seq_contains("admin:department:delete")] checked="checked"[/#if]/><span>删除部门</span>
                                        </div>
                                    </div>
                                </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>员工管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                            <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:list" [#if role.authorities?seq_contains("admin:admin:list")] checked="checked"[/#if] /><span>员工列表</span>
				                        </div>	
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:add" [#if role.authorities?seq_contains("admin:admin:add")] checked="checked"[/#if] /><span>添加员工</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:edit" [#if role.authorities?seq_contains("admin:admin:edit")] checked="checked"[/#if] /><span>编辑员工</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:admin:delete" [#if role.authorities?seq_contains("admin:admin:delete")] checked="checked"[/#if] /><span>删除员工</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>数据配置</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:dict:edit" [#if role.authorities?seq_contains("admin:dict:edit")] checked="checked"[/#if] /><span>配置数据</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>日志管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:log" [#if role.authorities?seq_contains("admin:log")] checked="checked"[/#if] /><span>日志列表</span>
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
				
				$("input[name=authorities]").each(function(i){
					var $this=$(this);
					if($this.parent().parent().attr("name")!="thirdDiv"){
						return;
					}
					if($this.is(":checked")){
						$this.parent().parent().parent().find("input[name=two]").attr("checked",true);
						$this.parent().parent().parent().parent().find("input[name=one]").attr("checked",true);
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