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
		                          <input type="checkbox" name="one"/><span style="font-weight:bolder;">代理</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"  /><span>代理管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:add" /><span>添加代理</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:edit" /><span>编辑代理</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyUser:delete" /><span>删除代理</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>代理申请管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:proxyCheck:check" /><span>代理审核</span>
				                        </div>	
			                        </div>
		                       	</div>
		                    </div>  
		                    
		                    <!-- 
		                    <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">收货点</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" value="admin:setting" /><span>收货点管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:add" /><span>添加收货点</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:edit" /><span>编辑收货点</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:updateneedStatus" /><span>暂停/启用收货点</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:importMore" /><span>收货点导入</span>
				                        </div>	
			                        </div>
		                       	</div>
		                    </div> -->
		                  <!--  <div name="firstDiv">
		                      	<div class="check-box">
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">供应</span>
		                        </div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>企业供应</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:add" /><span>添加供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:view" /><span>查看供应</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:edit" /><span>编辑供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:updateStatus" /><span>暂停/启用供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:formalSupply:delete" /><span>删除供应</span>
				                        </div>
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>门店供应</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:add" /><span>添加供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:batchAddIndex" /><span>批量添加供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:view" /><span>查看供应</span>
				                        </div>
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:edit" /><span>编辑供应</span>
				                        </div>
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:needSupply:delete" /><span>删除供应</span>
				                        </div> -->
			                       <!--</div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:turnoverSupply" /><span>流水供应</span>
										</div>
									</div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>供应确认</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:supplyDistribution:view" /><span>查看分配</span>
				                        </div>	
				                       
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>供应商管理</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:supplierRelation:edit" /><span>编辑供应商</span>
				                        </div>	
			                        </div>
		                       	</div>
		                    </div>  -->
		                    
		                  
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
				                          	<input type="checkbox" name="authorities" value="admin:order:applyCancel" /><span>订单用户申请</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancel" /><span>订单取消</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:complete" /><span>订单完成</span>
				                        </div>	
				                       <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:orderShippingInfo" /><span>发货单打印</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:order:cancelShipped" /><span>发货单作废</span>
				                        </div>	-->
			                        </div>
		                       	</div>
		                       <!--	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>采购单</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:verificationDeliveryInfo" /><span>批量打印</span>
				                        </div>	
				                       <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:getOut" /><span>订单导出</span>
				                        </div>	
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:add" /><span>收货点代下单</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:addMore" /><span>多地址代下单</span>
				                        </div>	 -->
				                       <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:purchaseorder" /><span>订单打印</span>
				                        </div>	
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:updateItems" /><span>订单修改</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:applicationCancel" /><span>订单申请取消</span>
				                        </div> -->	
				                        <!--<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:ownOrder:addRemarks" /><span>订单备注</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:print:deliveryInfor" /><span>发货单打印</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	
		                       	[#if currSupplier.systemSetting.isDistributionModel]
       	                       	<div style="padding-left:70px;" name="secondDiv">
       		                       	<div class="check-box">
       		                          <input type="checkbox" name="two" /><span>分销单审核</span>
       		                        </div>
       		                        <div style="padding-left:100px;" name="thirdDiv">
	       		                        <div class="check-box">
	       		                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:checkBatchReview" /><span>批量审核</span>
	       		                        </div>	
       			                       <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:getOut" /><span>订单导出</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:print:distributionOrder" /><span>订单打印</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:review" /><span>订单审核</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:applyCancel" /><span>订单申请取消</span>
       			                        </div>	
       			                        <div class="check-box">
       			                          	<input type="checkbox" name="authorities" value="admin:distributionOrder:cancel" /><span>订单取消</span>
       			                        </div>	
       		                        </div>
       	                       	</div>
       	                       	[/#if]
       	                       	
	                           	<div style="padding-left:70px;" name="secondDiv">
	    	                       	<div class="check-box">
	    	                          <input type="checkbox" name="two" /><span>代下单</span>
	    	                        </div>
	    	                        <div style="padding-left:100px;" name="thirdDiv">
	    	                        	<div class="check-box">
	    		                          	<input type="checkbox" name="authorities" value="admin:proxyOrder:individualAdd" /><span>直营门店代下单</span>
	    		                        </div>	
	    		                       <div class="check-box">
	    		                          	<input type="checkbox" name="authorities" value="admin:proxyOrder:multipleAdd" /><span>流水门店代下单</span>
	    		                        </div>	
	    	                        </div>
	                           	</div>
		                    </div>
		                    -->
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
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:add" /><span>添加客户</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:edit" /><span>编辑客户</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:customerRelation:delete" /><span>删除客户</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>门店</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:add" /><span>添加门店</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:edit" /><span>编辑门店</span>
				                        </div>	
				                        <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:updateneedStatus" /><span>暂停、启用门店</span>
				                        </div> -->
				                       <!-- <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:need:importMore" /><span>导入门店</span>
				                        </div>
			                        </div>
		                       	</div>
		                   </div>-->
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
				                        <!--<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:orderReport:purchaseList" /><span>查看采购单报表</span>
				                        </div>	-->
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
				                        <!-- <div class="check-box" style="width:160px">
				                          	<input type="checkbox" name="authorities" value="admin:commodityReport:purchaseOrder" /><span>查看采购商品报表</span>
				                        </div>	-->
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
		                          <input type="checkbox" name="one" /><span style="font-weight:bolder;">系统</span>
		                        </div>
                                <div style="padding-left:70px;" name="secondDiv">
                                    <div class="check-box">
                                        <input type="checkbox" name="two" /><span>角色管理</span>
                                    </div>
                                    <div style="padding-left:100px;" name="thirdDiv">
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
		                       	<!--<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two" /><span>消息通知</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:add" /><span>添加接收员</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:delete" /><span>编辑订货单通知</span>
				                        </div>	
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:orderNotice" /><span>编辑采购单通知</span>
				                        </div>
				                         <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:notice:purchaseNotice" /><span>删除接收员</span>
				                        </div>
			                        </div>
		                       	</div>-->
		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:orderSetting" /><span>订单设置</span>
										</div>
                                    </div>
		                       	</div>

								[#if currSupplier.systemSetting.isDistributionModel]
                                <div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
                                        <div class="check-box">
                                            <input type="checkbox" name="authorities" value="admin:systemSetting" /><span>分销设置</span>
                                        </div>
                                    </div>
                                </div>
								[/#if]

		                       	<div style="padding-left:70px;" name="secondDiv">
                                    <input type="checkbox" name="two" class="hide"/>
                                    <div name="thirdDiv">
										<div class="check-box">
										  <input type="checkbox" name="authorities" value="admin:enterpriseInfo" /><span>企业信息</span>
										</div>
									</div>
		                       	</div>
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
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:add" /><span>添加主题</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:edit" /><span>编辑主题</span>
				                        </div>	
				                        <div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:goodDirectory:delete" /><span>删除主题</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>分享统计</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:shareStatistics" /><span>查看分享统计</span>
				                        </div>	
			                        </div>
		                       	</div>
		                       	<div style="padding-left:70px;" name="secondDiv">
			                       	<div class="check-box">
			                          <input type="checkbox" name="two"/><span>清单统计</span>
			                        </div>
			                        <div style="padding-left:100px;" name="thirdDiv">
			                        	<div class="check-box">
				                          	<input type="checkbox" name="authorities" value="admin:listStatistics" /><span>查看清单统计</span>
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