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
		                   <!-- 
		                   <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one"/><span style="font-weight:bolder;">代理</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"  /><span>代理管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:add" [#if role.authorities?seq_contains("admin:proxyUser:add")] checked="checked"[/#if] /><span>添加代理</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:edit" [#if role.authorities?seq_contains("admin:proxyUser:edit")] checked="checked"[/#if] /><span>编辑代理</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:delete" [#if role.authorities?seq_contains("admin:proxyUser:delete")] checked="checked"[/#if] /><span>删除代理</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>代理申请管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyCheck:check" [#if role.authorities?seq_contains("admin:proxyCheck:check")] checked="checked"[/#if] /><span>代理审核</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	
		                    </div> 
		                    -->
		                    
		                    <!--
		                    <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">供应</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>供应管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:add" [#if role.authorities?seq_contains("admin:formalSupply:add")] checked="checked"[/#if] /><span>添加供应</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:view" [#if role.authorities?seq_contains("admin:formalSupply:view")] checked="checked"[/#if] /><span>查看供应</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:edit" [#if role.authorities?seq_contains("admin:formalSupply:edit")] checked="checked"[/#if] /><span>编辑供应</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:updateStatus" [#if role.authorities?seq_contains("admin:formalSupply:updateStatus")] checked="checked"[/#if] /><span>暂停/启用供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:delete" [#if role.authorities?seq_contains("admin:formalSupply:delete")] checked="checked"[/#if] /><span>删除供应</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>个体供应</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:add" [#if role.authorities?seq_contains("admin:needSupply:add")] checked="checked"[/#if]/><span>添加供应</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:batchAddIndex" [#if role.authorities?seq_contains("admin:needSupply:batchAddIndex")] checked="checked"[/#if] /><span>批量添加供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:view" [#if role.authorities?seq_contains("admin:needSupply:view")] checked="checked"[/#if]/><span>查看供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:edit" [#if role.authorities?seq_contains("admin:needSupply:edit")] checked="checked"[/#if]/><span>编辑供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:delete" [#if role.authorities?seq_contains("admin:needSupply:delete")] checked="checked"[/#if]/><span>删除供应</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:turnoverSupply" [#if role.authorities?seq_contains("admin:turnoverSupply")] checked="checked"[/#if] /><span>流水供应</span>
										</div>
									</div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>供应分配</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:supplyDistribution:view" [#if role.authorities?seq_contains("admin:supplyDistribution:view")] checked="checked"[/#if] /><span>查看分配</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:supplyDistribution:distributionList" [#if role.authorities?seq_contains("admin:supplyDistribution:distributionList")] checked="checked"[/#if] /><span>收货点分配商品</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>供应商管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:supplierRelation:edit" [#if role.authorities?seq_contains("admin:supplierRelation:edit")] checked="checked"[/#if] /><span>编辑供应商</span>
				                        </div>	
			                        </div>
		                       	</div>
		                    </div>
		                    
		                    -->
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
				                        </div>	-->
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:checkBatchReview" [#if role.authorities?seq_contains("admin:order:checkBatchReview")] checked="checked"[/#if] /><span>批量审核</span>
				                        </div>	
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:getOut" [#if role.authorities?seq_contains("admin:order:getOut")] checked="checked"[/#if] /><span>订单导出</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:order" [#if role.authorities?seq_contains("admin:print:order")] checked="checked"[/#if] /><span>订单打印</span>
				                        </div>	-->
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
				                          	<input type="checkbox" name="authorities" value="admin:order:applyCancel" [#if role.authorities?seq_contains("admin:order:applyCancel")] checked="checked"[/#if] /><span>订单用户申请</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancel" [#if role.authorities?seq_contains("admin:order:cancel")] checked="checked"[/#if] /><span>订单取消</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:complete" [#if role.authorities?seq_contains("admin:order:complete")] checked="checked"[/#if] /><span>订单完成</span>
				                        </div>	
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:orderShippingInfo" [#if role.authorities?seq_contains("admin:print:orderShippingInfo")] checked="checked"[/#if] /><span>发货单打印</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancelShipped" [#if role.authorities?seq_contains("admin:order:cancelShipped")] checked="checked"[/#if] /><span>发货单作废</span>
				                        </div>	-->
			                        </div>
		                       	</div>
		                       	<!--
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>采购单</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:verificationDeliveryInfo" [#if role.authorities?seq_contains("admin:print:verificationDeliveryInfo")] checked="checked"[/#if] /><span>批量打印</span>
				                        </div>	
				                        <!--
				                       <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:getOut" [#if role.authorities?seq_contains("admin:ownOrder:getOut")] checked="checked"[/#if] /><span>订单导出</span>
				                        </div>	-->
				                       <!--  <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:add" [#if role.authorities?seq_contains("admin:ownOrder:add")] checked="checked"[/#if] /><span>收货点代下单</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:addMore" [#if role.authorities?seq_contains("admin:ownOrder:addMore")] checked="checked"[/#if] /><span>多地址代下单</span>
				                        </div>	 -->
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:purchaseorder" [#if role.authorities?seq_contains("admin:print:purchaseorder")] checked="checked"[/#if] /><span>订单打印</span>
				                        </div>	-->
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:updateItems" [#if role.authorities?seq_contains("admin:ownOrder:updateItems")] checked="checked"[/#if] /><span>订单修改</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:applicationCancel" [#if role.authorities?seq_contains("admin:ownOrder:applicationCancel")] checked="checked"[/#if] /><span>订单申请取消</span>
				                        </div>	 -->
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:addRemarks" [#if role.authorities?seq_contains("admin:ownOrder:addRemarks")] checked="checked"[/#if] /><span>订单备注</span>
				                        </div>	-->
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:deliveryInfor" [#if role.authorities?seq_contains("admin:print:deliveryInfor")] checked="checked"[/#if] /><span>发货单打印</span>
				                        </div>
			                        </div>	
		                       	</div>-->
		                       	
		                       	[#if currSupplier.systemSetting.isDistributionModel]
       	                       	<div style="padding-left:70px;" name="secondDiv">
       		                       	<div class="check-box">
       		                          <input type="checkbox" name="two" /><span>分销单审核</span>
       		                        </div>
       		                        <div style="padding-left:100px;" name="thirdDiv">
	       		                        <div class="check-box">
	       		                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:checkBatchReview" [#if role.authorities?seq_contains("admin:distributionOrder:checkBatchReview")] checked="checked"[/#if] /><span>批量审核</span>
	       		                        </div>	
       			                       <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:getOut" [#if role.authorities?seq_contains("admin:distributionOrder:getOut")]checked="checked" [/#if]/><span>订单导出</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:print:distributionOrder" [#if role.authorities?seq_contains("admin:print:distributionOrder")]checked="checked"[/#if]/><span>订单打印</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:review" [#if role.authorities?seq_contains("admin:distributionOrder:review")]checked="checked"[/#if]/><span>订单审核</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:applyCancel" [#if role.authorities?seq_contains("admin:distributionOrder:applyCancel")]checked="checked"[/#if]/><span>订单申请取消</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:cancel" [#if role.authorities?seq_contains("admin:distributionOrder:cancel")]checked="checked"[/#if]/><span>订单取消</span>
       			                        </div>	
       		                        </div>
       	                       	</div>
       	                       	[/#if]
       	                       	
	                           <!--	<div style="padding-left:70px;" name="secondDiv">
	    	                       	<div class="check-box">
	    	                          <input type="checkbox" name="two" /><span>代下单</span>
	    	                        </div>
	    	                        <div style="padding-left:100px;" name="thirdDiv">
	    	                        	<div class="check-box">
	    		                          	<input type="checkbox" name="authorities" value="admin:proxyOrder:individualAdd" [#if role.authorities?seq_contains("admin:proxyOrder:individualAdd")]checked="checked"[/#if] /><span>个体客户代下单</span>
	    		                        </div>	
	    		                       <div class="check-box">
	    		                          	<input type="checkbox" name="authorities" value="admin:proxyOrder:multipleAdd" [#if role.authorities?seq_contains("admin:proxyOrder:multipleAdd")]checked="checked"[/#if] /><span>流水客户代下单</span>
	    		                        </div>	
	    	                        </div>
	                           	</div>
	                           	
	                           	-->
		                    </div>
		                    
		                    <!--
		                    <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">客户</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>企业客户</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:add" [#if role.authorities?seq_contains("admin:customerRelation:add")] checked="checked"[/#if] /><span>添加客户</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:edit" [#if role.authorities?seq_contains("admin:customerRelation:edit")] checked="checked"[/#if] /><span>编辑客户</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:delete" [#if role.authorities?seq_contains("admin:customerRelation:delete")] checked="checked"[/#if] /><span>删除客户</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>个体管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:add" [#if role.authorities?seq_contains("admin:need:add")] checked="checked"[/#if]/><span>添加个体</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:edit" [#if role.authorities?seq_contains("admin:need:edit")] checked="checked"[/#if]/><span>编辑个体</span>
				                        </div>	
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:updateneedStatus" [#if role.authorities?seq_contains("admin:need:updateneedStatus")] checked="checked"[/#if]/><span>暂停、启用个体</span>
				                        </div> -->
				                        <!--<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:importMore" [#if role.authorities?seq_contains("admin:need:importMore")] checked="checked"[/#if]/><span>个体导入</span>
				                        </div>
			                        </div>
		                       	</div>
		                   </div>
		                   -->
		                   
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
				                        <!--
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:orderReport:purchaseList" [#if role.authorities?seq_contains("admin:orderReport:purchaseList")] checked="checked"[/#if] /><span>查看采购单报表</span>
				                        </div>	-->
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
				                        <!--
				                        <div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:commodityReport:purchaseOrder" [#if role.authorities?seq_contains("admin:commodityReport:purchaseOrder")] checked="checked"[/#if] /><span>查看采购商品报表</span>
				                        </div>	-->
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
                                            <input type="checkbox" name="authorities" value="admin:member:add" [#if role.authorities?seq_contains("admin:member:add")] checked="checked"[/#if]/><span>添加会员</span>
                                        </div>
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:member:edit" [#if role.authorities?seq_contains("admin:member:edit")] checked="checked"[/#if]/><span>编辑会员</span>
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
		                       	<!--
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>消息通知</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:add" [#if role.authorities?seq_contains("admin:notice:add")] checked="checked"[/#if] /><span>添加接收员</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:delete" [#if role.authorities?seq_contains("admin:notice:delete")] checked="checked"[/#if] /><span>编辑订货单通知</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:orderNotice" [#if role.authorities?seq_contains("admin:notice:orderNotice")] checked="checked"[/#if] /><span>编辑采购单通知</span>
				                        </div>
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:purchaseNotice" [#if role.authorities?seq_contains("admin:notice:purchaseNotice")] checked="checked"[/#if] /><span>删除接收员</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:orderSetting" [#if role.authorities?seq_contains("admin:orderSetting")] checked="checked"[/#if] /><span>订单设置</span>
										</div>
									</div>
		                       	</div>

								[#if currSupplier.systemSetting.isDistributionModel]
                                <div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:systemSetting" [#if role.authorities?seq_contains("admin:systemSetting")] checked="checked"[/#if] /><span>分销设置</span>
                                        </div>
                                    </div>
                                </div>
								[/#if]

		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:enterpriseInfo" [#if role.authorities?seq_contains("admin:enterpriseInfo")] checked="checked"[/#if] /><span>企业信息</span>
										</div>
									</div>
		                       	</div>
		                       	-->
		                       	
		                    </div>
		                    
		                  <!--
		                    <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">订货助理</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>商品目录</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:add" [#if role.authorities?seq_contains("admin:goodDirectory:add")] checked="checked"[/#if] /><span>添加主题</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:edit" [#if role.authorities?seq_contains("admin:goodDirectory:edit")] checked="checked"[/#if] /><span>编辑主题</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:delete" [#if role.authorities?seq_contains("admin:goodDirectory:delete")] checked="checked"[/#if] /><span>删除主题</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>分享统计</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:shareStatistics" [#if role.authorities?seq_contains("admin:shareStatistics")] checked="checked"[/#if] /><span>查看分享统计</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>清单统计</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:listStatistics" [#if role.authorities?seq_contains("admin:listStatistics")] checked="checked"[/#if] /><span>查看清单统计</span>
				                        </div>	
			                        </div>
		                       	</div>
		                   </div>
	                    </div>-->
	                    
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