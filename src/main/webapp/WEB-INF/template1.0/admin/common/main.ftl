[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>微信小程序管理系统</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<style>
			.vip a{display:inline-block;width:100%;height:100%;}
			.iframe_main{overflow-y:hidden;}
			.xxDialog{top:40px;left:53%;}
			.xxDialog .dialogBottom{width:100%;position:absolute;bottom:0;border:0;}
			.dialogContent{height:calc(100% - 40px); overflow: auto; overflow-x: hidden;}
			.op_button,.tab_button{margin-top:5px;}
			.ch_condition{padding-top:0;}
			.xxDialog .dialogBottom{border-top:1px solid #eee;display:none;}
			.modelInfo iframe{height:calc(100% - 5px);width:100%;}
		</style>
	</head>
	<body class="bodyObj">
		<div class="index_page">
		<div class="index_nav_left">
			<div class="logo">
				<!--<img src="${base}/resources/admin1.0/images/logo_icon.svg" alt="" />-->
			</div>
			<ul class="index_nav_one">
			[#list ["admin:goods:add","admin:goods:edit","admin:goods:delete", "admin:stock", "admin:productCategory:add","admin:productCategory:edit","admin:productCategory:delete", "admin:parameter", "admin:attribute", "admin:specification:add","admin:specification:edit","admin:specification:delete", "admin:brand", "admin:productNotify" , "admin:commonCategory"] as permission]
			[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcsp_icon.svg" alt="" /></br>
						<span>${message("admin.main.productGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:goods:add or admin:goods:edit or admin:goods:delete or admin:goods:importMore"]
							<li>
								<a href="../goods/list.jhtml" target="iframe">${message("admin.main.goods")}</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:goodsCenter"]
							<li>
								<a href="../goodsCenter/list.jhtml" target="iframe">原料库管理</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.hasPermission name="admin:stock"]
							<li>
								<a href="../stock/log.jhtml" target="iframe">${message("admin.main.stock")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.orPermission name="admin:productCategory:add or admin:productCategory:edit or admin:productCategory:delete"]
							<li>
								<a href="../product_category/list.jhtml" target="iframe">${message("admin.main.productCategory")}</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.hasPermission name="admin:parameter"]
							<li>
								<a href="../parameter/list.jhtml" target="iframe">${message("admin.main.parameter")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:attribute"]
							<li>
								<a href="../attribute/list.jhtml" target="iframe">${message("admin.main.attribute")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.orPermission name="admin:specification:add or admin:specification:edit or admin:specification:delete"]
							<li>
								<a href="../specification/list.jhtml" target="iframe">${message("admin.main.specification")}</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.hasPermission name="admin:brand"]
							<li>
								<a href="../brand/list.jhtml" target="iframe">${message("admin.main.brand")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:productNotify"]
							<li>
								<a href="../product_notify/list.jhtml" target="iframe">${message("admin.main.productNotify")}</a>
							</li>
						[/@shiro.hasPermission]

						[@shiro.hasPermission name="admin:commonCategory"]
                            <li>
                                <a href="../common_category/list.jhtml" target="iframe">商流分类</a>
                            </li>
						[/@shiro.hasPermission]
					</ul>
				</li>
					[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				
		<!--		<ul class="index_nav_one">
			[#list ["admin:proxyUser:add", "admin:proxyUser:edit", "admin:proxyUser:delete", "admin:proxyCheck:check"] as permission]
			[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcsp_icon.svg" alt="" /></br>
						<span>代理</span>
						<i></i>
					</div>
					<ul class="nav_two">
							[@shiro.orPermission name="admin:proxyUser:add or admin:proxyUser:edit or admin:proxyUser:delete"]
							<li>
								<a href="../proxy_user/list.jhtml" target="iframe">代理列表</a>
							</li>
							[/@shiro.orPermission]
							[@shiro.orPermission name="admin:proxyCheck:check"]
							<li>
								<a href="../proxy_check/list.jhtml" target="iframe">代理申请</a>
							</li>
							[/@shiro.orPermission]
							
					</ul>
				</li>
					[#break /]
				[/@shiro.hasPermission]
				[/#list]-->
				
				
				<ul class="index_nav_one">
			[#list ["admin:goodsCenter"] as permission]
			[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcsp_icon.svg" alt="" /></br>
						<span>原料库</span>
						<i></i>
					</div>
					<ul class="nav_two">
							<li>
								<a href="../goodsCenter/list.jhtml" target="iframe">原料库商品</a>
							</li>
							<li>
								<a href="../categoryCenter/list.jhtml" target="iframe">原料库分类</a>
							</li>
							<li>
								<a href="../specificationCenter/list.jhtml" target="iframe">原料库规格</a>
							</li>
					</ul>
				</li>
					[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				<!-- [#list ["admin:need:add","admin:need:edit","admin:need:updateneedStatus","admin:need:importMore"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcshd_icon.svg" alt="" /></br>
						<span>${message("admin.main.needGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:need:add or admin:need:edit or admin:need:updateneedStatus or admin:need:importMore"]
	                        <li>
	                            <a href="../need/list.jhtml" target="iframe">${message("admin.main.need")}</a>
	                        </li>
						[/@shiro.orPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				 -->
				[#list ["admin:supplier" , "admin:supplyCheck" , "admin:purchaseCheck"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcqy_icon.svg" alt="" /></br>
						<span>审核</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.hasPermission name="admin:supplier"]
	                        <li>
	                            <a href="../supplier/list.jhtml" target="iframe">${message("admin.main.Certification")}</a>
	                        </li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:supplyCheck"]
                            <li>
                                <a href="../supplyCheck/list.jhtml" target="iframe">供应审核</a>
                            </li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:purchaseCheck"]
                            <li>
                                <a href="../purchaseCheck/list.jhtml" target="iframe">采购审核</a>
                            </li>
						[/@shiro.hasPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				<!--
				[#list ["admin:formalSupply:add","admin:formalSupply:view","admin:formalSupply:edit","admin:formalSupply:delete","admin:formalSupply:updateStatus","admin:temporary","admin:supplyDistribution:view","admin:supplyDistribution:distributionList","admin:supplierRelation:edit","admin:needSupply:add","admin:needSupply:view","admin:needSupply:edit","admin:needSupply:delete","admin:needSupply:updateStatus","admin:turnoverSupply"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcgy_icon.svg" alt="" /></br>
						<span>${message("admin.main.formalSupplys")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:formalSupply:add or admin:formalSupply:view or admin:formalSupply:edit or admin:formalSupply:delete or admin:formalSupply:updateStatus"]
	                        <li>
	                            <a href="../formalSupply/list.jhtml" target="iframe">${message("admin.main.formalSupply")}</a>
	                        </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:needSupply:add or admin:needSupply:view or admin:needSupply:edit or admin:needSupply:delete or admin:needSupply:updateStatus"]
	                        <li>
	                            <a href="../needSupply/list.jhtml" target="iframe">${message("admin.main.needSupply")}</a>
	                        </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:turnoverSupply"]
	                        <li>
	                            <a href="../turnoverSupply/selectList.jhtml" target="iframe">${message("admin.main.turnoverSupply")}</a>
	                        </li>
						[/@shiro.orPermission]
						[@shiro.hasPermission name="admin:temporary"]
	                        <li>
	                            <a href="../temporary/list.jhtml" target="iframe">${message("admin.main.temporarySupply")}</a>
	                        </li>
						[/@shiro.hasPermission]
						[@shiro.orPermission name="admin:supplyDistribution:view or admin:supplyDistribution:distributionList"]
	                        <li>
	                            <a href="../supplyDistribution/list.jhtml" target="iframe">${message("admin.main.supplyDistribution")}</a>
	                        </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:supplierRelation:edit"]
                            <li>
                                <a href="../supplierRelation/list.jhtml" target="iframe">供应商管理</a>
                            </li>
						[/@shiro.orPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				-->
				
				[#list ["admin:print:orderBatchPrint","admin:order:checkBatchReview","admin:order:getOut","admin:print:order","admin:order:review","admin:order:updateItems","admin:order:shipping","admin:order:applyCancel","admin:order:cancel","admin:order:complete","admin:order:addRemarks","admin:print:orderShippingInfo","admin:order:cancelShipped","admin:print:verificationDeliveryInfo","admin:ownOrder:add","admin:ownOrder:addMore","admin:ownOrder:getOut","admin:print:purchaseorder","admin:ownOrder:updateItems","admin:ownOrder:applicationCancel","admin:ownOrder:addRemarks","admin:print:deliveryInfor","admin:payment", "admin:refunds", "admin:shipping", "admin:returns", "admin:deliveryCenter", "admin:deliveryTemplate","admin:print:distributionOrder","admin:distributionOrder:checkBatchReview","admin:distributionOrder:getOut","admin:distributionOrder:review","admin:distributionOrder:applyCancel","admin:distributionOrder:cancel","admin:proxyOrder:individualAdd","admin:proxyOrder:multipleAdd"] as permission]
				[@shiro.hasPermission name = permission]
				<li class="orderLi">
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcdd_icon.svg" alt="" /></br>
						<span>${message("admin.main.orderGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:print:orderBatchPrint or admin:order:checkBatchReview or admin:order:getOut or admin:print:order or admin:order:review or admin:order:updateItems or admin:order:shipping or admin:order:applyCancel or admin:order:cancel or admin:order:complete or admin:order:addRemarks or admin:print:orderShippingInfo or admin:order:cancelShipped"]
							<li class="supply_B">
								<a href="../order/list.jhtml" target="iframe">订货单</a>
							</li>
						[/@shiro.orPermission]
						<!--[@shiro.orPermission name="admin:print:verificationDeliveryInfo or admin:ownOrder:add or admin:ownOrder:addMore or admin:ownOrder:getOut or admin:print:purchaseorder or admin:ownOrder:updateItems or admin:ownOrder:applicationCancel or admin:ownOrder:addRemarks or admin:print:deliveryInfor"]
	                        <li class="purchase_B">
	                            <a href="../ownOrder/list.jhtml" target="iframe">采购单</a>
	                        </li>
						[/@shiro.orPermission]
						[#if supplier.systemSetting.isDistributionModel == "true"]
							[@shiro.orPermission name="admin:print:distributionOrder or admin:distributionOrder:checkBatchReview or admin:distributionOrder:getOut or admin:distributionOrder:review or admin:distributionOrder:applyCancel or admin:distributionOrder:cancel"]
								<li>
									<a href="../distributionOrder/list.jhtml" target="iframe">分销单审核</a>
								</li>
							[/@shiro.orPermission]
						[/#if]

						[@shiro.orPermission name="admin:proxyOrder:individualAdd or admin:proxyOrder:multipleAdd"]
							<li>
								<a href="../proxyOrder/index.jhtml" target="iframe">代下单</a>
							</li>
						[/@shiro.orPermission]

						[@shiro.hasPermission name="admin:payment"]
							<li>
								<a href="../payment/list.jhtml" target="iframe">${message("admin.main.payment")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:refunds"]
							<li>
								<a href="../refunds/list.jhtml" target="iframe">${message("admin.main.refunds")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:shipping"]
							<li>
								<a href="../shipping/list.jhtml" target="iframe">${message("admin.main.shipping")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:returns"]
							<li>
								<a href="../returns/list.jhtml" target="iframe">${message("admin.main.returns")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:deliveryCenter"]
							<li>
								<a href="../delivery_center/list.jhtml" target="iframe">${message("admin.main.deliveryCenter")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:deliveryTemplate"]
							<li>
								<a href="../delivery_template/list.jhtml" target="iframe">${message("admin.main.deliveryTemplate")}</a>
							</li>
						[/@shiro.hasPermission]
	-->
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				[#list ["admin:member:add","admin:member:edit","admin:member:delete","admin:withdraw:edit", "admin:memberRank", "admin:memberAttribute", "admin:point",
				 "admin:deposit", "admin:review", "admin:consultation", "admin:messageConfig"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dchy_icon.svg" alt="" /></br>
						<span>${message("admin.main.memberGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
					    [@shiro.orPermission name="admin:member:add or admin:member:edit or admin:member:delete"]
							<li>
								<a href="../member/list.jhtml" target="iframe">用户列表</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:withdraw:edit"]
							<li>
								<a href="../member/withdraw/list.jhtml" target="iframe">提现列表</a>
							</li>
						[/@shiro.orPermission]
						
						[@shiro.hasPermission name="admin:memberRank"]
							<li>
								<a href="../member_rank/list.jhtml" target="iframe">${message("admin.main.memberRank")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:memberAttribute"]
							<li>
								<a href="../member_attribute/list.jhtml" target="iframe">${message("admin.main.memberAttribute")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:point"]
							<li>
								<a href="../point/log.jhtml" target="iframe">${message("admin.main.point")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:deposit"]
							<li>
								<a href="../deposit/log.jhtml" target="iframe">${message("admin.main.deposit")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:review"]
							<li>
								<a href="../review/list.jhtml" target="iframe">${message("admin.main.review")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:consultation"]
							<li>
								<a href="../consultation/list.jhtml" target="iframe">${message("admin.main.consultation")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:messageConfig"]
							<li>
								<a href="../message_config/list.jhtml" target="iframe">${message("admin.main.messageConfig")}</a>
							</li>
						[/@shiro.hasPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				[#list ["admin:navigation", "admin:article", "admin:articleCategory", "admin:tag", "admin:friendLink", "admin:adPosition", "admin:ad", "admin:template", "admin:theme", "admin:cache", "admin:static", "admin:index"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcny_icon.svg" alt="" /></br>
						<span>${message("admin.main.contentGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.hasPermission name="admin:navigation"]
							<li>
								<a href="../navigation/list.jhtml" target="iframe">${message("admin.main.navigation")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:article"]
							<li>
								<a href="../article/list.jhtml" target="iframe">${message("admin.main.article")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:articleCategory"]
							<li>
								<a href="../article_category/list.jhtml" target="iframe">${message("admin.main.articleCategory")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:tag"]
							<li>
								<a href="../tag/list.jhtml" target="iframe">${message("admin.main.tag")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:friendLink"]
							<li>
								<a href="../friend_link/list.jhtml" target="iframe">${message("admin.main.friendLink")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:adPosition"]
							<li>
								<a href="../ad_position/list.jhtml" target="iframe">${message("admin.main.adPosition")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:ad"]
							<li>
								<a href="../ad/list.jhtml" target="iframe">${message("admin.main.ad")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:recommendSupplier"]
							<li>
								<a href="../adSupplier/recommendlist.jhtml" target="iframe">推荐企业</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:template"]
							<li>
								<a href="../template/list.jhtml" target="iframe">${message("admin.main.template")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:theme"]
							<li>
								<a href="../theme/setting.jhtml" target="iframe">${message("admin.main.theme")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:cache"]
							<li>
								<a href="../cache/clear.jhtml" target="iframe">${message("admin.main.cache")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:static"]
							<li>
								<a href="../static/generate.jhtml" target="iframe">${message("admin.main.static")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:index"]
							<li>
								<a href="../index/generate.jhtml" target="iframe">${message("admin.main.index")}</a>
							</li>
						[/@shiro.hasPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				[#list ["admin:promotion", "admin:coupon", "admin:seo", "admin:sitemap"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcyx_icon.svg" alt="" /></br>
						<span>${message("admin.main.marketingGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.hasPermission name="admin:promotion"]
							<li>
								<a href="../promotion/list.jhtml" target="iframe">${message("admin.main.promotion")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:coupon"]
							<li>
								<a href="../coupon/list.jhtml" target="iframe">${message("admin.main.coupon")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:seo"]
							<li>
								<a href="../seo/list.jhtml" target="iframe">${message("admin.main.seo")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:sitemap"]
							<li>
								<a href="../sitemap/generate.jhtml" target="iframe">${message("admin.main.sitemap")}</a>
							</li>
						[/@shiro.hasPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				[#list ["admin:statistics", "admin:memberStatistic", "admin:orderStatistic", "admin:memberRanking", "admin:goodsRanking"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dctj_icon.svg" alt="" /></br>
						<span>${message("admin.main.statisticGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.hasPermission name="admin:statistics"]
							<li>
								<a href="../statistics/view.jhtml" target="iframe">${message("admin.main.statistics")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:statistics"]
							<li>
								<a href="../statistics/setting.jhtml" target="iframe">${message("admin.main.statisticsSetting")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:memberStatistic"]
							<li>
								<a href="../member_statistic/list.jhtml" target="iframe">${message("admin.main.memberStatistic")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:orderStatistic"]
							<li>
								<a href="../order_statistic/list.jhtml" target="iframe">${message("admin.main.orderStatistic")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:memberRanking"]
							<li>
								<a href="../member_ranking/list.jhtml" target="iframe">${message("admin.main.memberRanking")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:goodsRanking"]
							<li>
								<a href="../goods_ranking/list.jhtml" target="iframe">${message("admin.main.goodsRanking")}</a>
							</li>
						[/@shiro.hasPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				<!--
				[#list ["admin:customerRelation:add","admin:customerRelation:edit","admin:customerRelation:delete","admin:need:add","admin:need:edit","admin:need:delete","admin:need:importMore"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/kehu_icon.svg" alt="" /></br>
						<span>${message("admin.main.client")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:customerRelation:add or admin:customerRelation:edit or admin:customerRelation:delete"]
	                        <li>
	                            <a href="../customerRelation/list.jhtml" target="iframe">${message("admin.main.clientManagement")}</a>
	                        </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:need:add or admin:need:edit or admin:need:delete or admin:need:importMore"]
	                        <li>
	                            <a href="../need/list.jhtml" target="iframe">${message("admin.main.needManagement")}</a>
	                        </li>
						[/@shiro.orPermission]
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]-->
				
				[#list ["admin:commodityReport:orderForm","admin:commodityReport:purchaseOrder","admin:orderReport:orderList","admin:orderReport:purchaseList","admin:customerReport"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/baobiao_icon.svg" alt="" /></br>
						<span>${message("admin.main.report")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:commodityReport:orderForm or admin:commodityReport:purchaseOrder"]
	                        [@shiro.hasPermission name="admin:commodityReport:orderForm"]
		                        <li>
		                            <a href="../commodityReport/index.jhtml" target="iframe">${message("admin.main.commodityReport")}</a>
		                        </li>
	                        [/@shiro.hasPermission]
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:commodityReport:orderForm or admin:commodityReport:purchaseOrder"]
	                        [@shiro.lacksPermission name="admin:commodityReport:orderForm"]
		                        <li>
		                            <a href="../commodityReport/supplyIndex.jhtml" target="iframe">${message("admin.main.commodityReport")}</a>
		                        </li>
	                        [/@shiro.lacksPermission]
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:orderReport:orderList or admin:orderReport:purchaseList"]
	                        [@shiro.hasPermission name="admin:orderReport:orderList"]
		                        <li>
		                            <a href="../orderReport/index.jhtml" target="iframe">${message("admin.main.orderReport")}</a>
		                        </li>
	                        [/@shiro.hasPermission]
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:orderReport:orderList or admin:orderReport:purchaseList"]
	                        [@shiro.lacksPermission name="admin:orderReport:orderList"]
		                        <li>
		                            <a href="../orderReport/purchaseFormIndex.jhtml" target="iframe">${message("admin.main.orderReport")}</a>
		                        </li>
	                        [/@shiro.lacksPermission]
						[/@shiro.orPermission]
						<!--
						[@shiro.hasPermission name="admin:customerReport"]
	                        <li>
	                            <a href="../customerReport/index.jhtml" target="iframe">${message("admin.main.customerReport")}</a>
	                        </li>
						[/@shiro.hasPermission]-->
					</ul>
					
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				<!--
				[#list ["admin:goodDirectory:add","admin:goodDirectory:edit","admin:goodDirectory:delete","admin:shareStatistics","admin:listStatistics"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/zhuli_icon.svg" alt="" /></br>
						<span>订货助理</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:goodDirectory or admin:goodDirectory:edit or admin:goodDirectory:delete"]
							<li>
								<a href="../ass/goodDirectory/list.jhtml" target="iframe">商品目录</a>
							</li>
						[/@shiro.orPermission]

						[@shiro.hasPermission name="admin:shareStatistics"]
							<li>
								<a href="../ass/shareStatistics/list.jhtml" target="iframe">分享统计</a>
							</li>
						[/@shiro.hasPermission]

						[@shiro.hasPermission name="admin:listStatistics"]
							<li>
								<a href="../ass/listStatistics/list.jhtml" target="iframe">清单统计</a>
							</li>
						[/@shiro.hasPermission]
						
						
					</ul>
					
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				-->
				
				[#list ["admin:department:add","admin:department:edit","admin:department:delete","admin:notice:add","admin:notice:delete","admin:notice:orderNotice","admin:notice:purchaseNotice","admin:setting", "admin:area", "admin:paymentMethod", "admin:shippingMethod", "admin:deliveryCorp", "admin:paymentPlugin", "admin:storagePlugin", "admin:loginPlugin", "admin:admin:add","admin:admin:edit","admin:admin:delete", "admin:role:add","admin:role:edit","admin:role:delete", "admin:message", "admin:log","admin:enterpriseInfo" ,"admin:systemSetting" , "admin:orderSetting"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcxt_icon.svg" alt="" /></br>
						<span>${message("admin.main.systemGroup")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.hasPermission name="admin:setting"]
							<li>
								<a href="../setting/edit.jhtml" target="iframe">${message("admin.main.setting")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:area"]
							<li>
								<a href="../area/list.jhtml" target="iframe">${message("admin.main.area")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:paymentMethod"]
							<li>
								<a href="../payment_method/list.jhtml" target="iframe">${message("admin.main.paymentMethod")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:shippingMethod"]
							<li>
								<a href="../shipping_method/list.jhtml" target="iframe">${message("admin.main.shippingMethod")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:deliveryCorp"]
							<li>
								<a href="../delivery_corp/list.jhtml" target="iframe">${message("admin.main.deliveryCorp")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:paymentPlugin"]
							<li>
								<a href="../payment_plugin/list.jhtml" target="iframe">${message("admin.main.paymentPlugin")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:storagePlugin"]
							<li>
								<a href="../storage_plugin/list.jhtml" target="iframe">${message("admin.main.storagePlugin")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:loginPlugin"]
							<li>
								<a href="../login_plugin/list.jhtml" target="iframe">${message("admin.main.loginPlugin")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.orPermission name="admin:role:add or admin:role:edit or admin:role:delete"]
                            <li>
                            <a href="../role/list.jhtml" target="iframe">${message("admin.main.role")}</a>
                            </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:department:add or admin:department:edit or admin:department:delete"]
                            <li>
                            <a href="../department/list.jhtml" target="iframe">部门管理</a>
                            </li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:admin:add or admin:admin:edit or admin:admin:delete"]
                            <li>
                                <a href="../admin/list.jhtml" target="iframe">${message("admin.main.admin")}</a>
                            </li>
						[/@shiro.orPermission]
						[@shiro.hasPermission name="admin:message"]
							<li>
								<a href="../messageManage/edit.jhtml" target="iframe">${message("admin.main.send")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:message"]
							<li>
								<a href="../messageManage/list.jhtml" target="iframe">${message("admin.main.message")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:message"]
							<li>
								<a href="../message/draft.jhtml" target="iframe">${message("admin.main.draft")}</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:dict:edit"]
							<li>
								<a href="../dict/edit.jhtml" target="iframe">配置数据</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:dict:edit"]
							<li>
								<a href="../ver/list.jhtml" target="iframe">鉴真管理</a>
							</li>
						[/@shiro.hasPermission]
						[@shiro.hasPermission name="admin:log"]
							<li>
								<a href="../log/list.jhtml" target="iframe">${message("admin.main.log")}</a>
							</li>
						[/@shiro.hasPermission]
						<!--[@shiro.orPermission name="admin:notice:add or admin:notice:delete or admin:notice:orderNotice or admin:notice:purchaseNotice"]
	                        <li>
	                            <a href="../notice/list.jhtml" target="iframe">消息通知</a>
	                        </li>
						[/@shiro.orPermission]-->
						<!--
						[@shiro.hasPermission name="admin:orderSetting"]
	                        <li>
	                            <a href="../orderSetting/index.jhtml" target="iframe">订单设置</a>
	                        </li>
						[/@shiro.hasPermission]
						
	                    [@shiro.hasPermission name="admin:enterpriseInfo"]
	                        <li>
	                         <a href="../enterpriseInfo/index.jhtml" target="iframe">企业信息</a>
	                    	</li>
						[/@shiro.hasPermission]

					<!--[@shiro.hasPermission name="admin:systemSetting"]
                            <li>
                                <a href="../systemSetting/index.jhtml" target="iframe">分销设置</a>
                            </li>
						[/@shiro.hasPermission]-->
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
				
				[#list ["admin:ass","admin:assgoods:add","admin:assgoods:edit","admin:assgoods:delete"] as permission]
				[@shiro.hasPermission name = permission]
				<li>
					<div class="nav_title">
						<img src="${base}/resources/admin1.0/images/dcxt_icon.svg" alt="" /></br>
						<span>${message("admin.main.ass")}</span>
						<i></i>
					</div>
					<ul class="nav_two">
						[@shiro.orPermission name="admin:assgoods:add or admin:assgoods:edit or admin:ass:assgoods:delete"]
							<li>
								<a href="../assgoods/list.jhtml" target="iframe">${message("admin.ass.assistantGoods")}</a>
							</li>
						[/@shiro.orPermission]
						[@shiro.orPermission name="admin:assCustomerRelation:add or admin:assCustomerRelation:delete or admin:assCustomerRelation:edit"]
							<li>
								<a href="../ass/customerRelation/list.jhtml" target="iframe">${message("admin.main.assCustomerRelation")}</a>
							</li>
						[/@shiro.orPermission]
						
					</ul>
				</li>
				[#break /]
				[/@shiro.hasPermission]
				[/#list]
			</ul>
			<!--<div class="header_right">
				
			</div>-->
		</div>
		<div class="index_content">
			<div class="user_header">
				<!--
				<div class="noAuth">
					[#if status == true]
					<span>未认证企业提供60天试用</span>
					<i>${(endDate?string("yyyy-MM-dd"))!}到期</i>
					[/#if]
				</div>
				-->
				<div class="user_h_right">
					<div class="user">
						<div class="username">
							<span class="name">[@shiro.principal /]</span>
							<span class="jiantou"></span>
							<div class="setUp">
								<p class="zhHao"><a href="../profile/edit.jhtml" target="iframe">${message("admin.main.profile")}</a></p>
								<span></span>
								<p class="cancel"><a href="../logout.jhtml" target="_top">${message("admin.main.logout")}</a></p>
							</div>
						</div>
						<div class="userImg"><img src="${base}/resources/admin1.0/images/mrtx_icon.svg" alt="" /></div>
					</div>

						[#if supplier != null]
							<!-- 企业未认证 -->
							[#if supplier.status == 'notCertified' ]
							<span class="vip">
								<a title="企业未认证" id="" href="../certification/index.jhtml" target="iframe" style="color:red">
									<img src="${base}/resources/admin1.0/images/weirenzheng_icon.svg"  alt="" />
								</a>
							</span>
							[/#if]
							<!-- 认证失败 -->
							[#if supplier.status == 'authenticationFailed' ]
							<span class="vip">
							 	<a title="企业认证失败" id="" href="../certification/reviewMiss.jhtml" target="iframe" style="color:red">
							 		<img src="${base}/resources/admin1.0/images/renzhengshibai_icon.svg" alt="" />
							 	</a>
							 </span>
							[/#if]
							<!-- 认证中 -->
							[#if supplier.status == 'certification' ]
							<span class="vip">
							 	<a  title="企业认证中" id="" href="../certification/reviewing.jhtml" target="iframe" style="color:blue">
							 		<img src="${base}/resources/admin1.0/images/renzhengzhong_icon.svg" alt="" />
							 	</a>
							 </span>
							[/#if]
							<!-- 企业已认证 -->
							[#if supplier.status == 'verified' ]
							<span class="vip yirenzheng">
							 	<a title="企业已认证" href="javascript:void(0);" target="iframe" style="color:green">
							 		<!--<img src="${base}/resources/admin1.0/images/yirenzheng_icon.svg" alt="" />-->
							 	</a>
							 </span>
							[/#if]
						[/#if]
<!--
					<div class="company_code">
						<span></span>
						<div class="show_code">
							<span></span>
							<div class="trmp_code">
								<p>小程序二维码</p>
								<img src="../common/generateTwoCode.jhtml?supplierId=${supplier.id}" alt="" />
								<input type="button" id="getQRCode" value="下载二维码" onclick="javascript:downloadCode();" />
							</div>
							<div class="form_code">
								<p>供应二维码</p>
								<img src="../common/getQRCode.jhtml?supplierId=${supplier.id}&supplyType=formal" alt="" />
								<input type="button" id="getQRCode" value="下载二维码" onclick="javascript:downloadQR('formal');" />
							</div>
						</div>
					</div>-->

					<div class="user_info">
						<input type="hidden" name="ext" id="ext" />
						<div class="userInfoDiv">
							
							<span class="info_num"></span>
							
						</div>
						
						<div class="show_news">
							<span class="sanjiao"></span>
							<div class="infoType_B">
								<div typeId="order" class="supply_Bu infoChecked">
									<span class="ListNum"></span>
									订货单
								</div>
								<div typeId="withdraw" class="withdraw_Bu infoChecked">
									<span class="ListNum"></span>
									提现
								</div>
							</div>
							<div class="infoConList">
								<ul class="supplyConList">
									<li class="info_li">
										<p class="content"><i class="noRead"></i></p>
										<span class="time">
										</span>
									</li>
								</ul>
								<ul class="withdrawConList">
									<li class="info_li">
										<p class="content"><i class="noRead"></i></p>
										<span class="time">
										</span>
									</li>
								</ul>
							</div>
							<div class="infoSet">
								<span class="setRead" style="cursor:pointer">全部标记为已读</span>
								<!--<span class="seeAll" style="cursor:pointer">查看更多</span>-->
							</div>

						</div>
					</div>

				</div>
			</div>
			<div class="iframe_main">
				<iframe src="" id="iframe" name="iframe"  frameborder="0" width="" height="">
					
				</iframe>
			</div>
		</div>
		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">

		function downloadQR(supplyType){
			window.location.href="../common/downQRCode.jhtml?supplierId=${supplier.id}&supplyType=" + supplyType;
		}

		function downloadCode(){
			window.location.href="../common/downTwoCode.jhtml?supplierId=${supplier.id}";
		}
		
		function goToOrder(url){
			$(".dialogOverlay").remove();
			$(".xxDialog").remove();
			$("#iframe").attr("src" , url);
		}
		
		/*时间的转化*/
		function getMyDate(str){    
	        var oDate = new Date(str),    
	        oYear = oDate.getFullYear(),    
	        oMonth = oDate.getMonth()+1,    
	        oDay = oDate.getDate(),    
	        oHour = oDate.getHours(),    
	        oMin = oDate.getMinutes(),    
	        oSen = oDate.getSeconds();
	        var oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +' '+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);
	        return oTime;    
	    };       
	    function getzf(num){    
	        if(parseInt(num) < 10){    
	            num = '0'+num;    
	        }    
	        return num;    
	    };
	    
	    function homeIcon() {
	    	$.ajax({
				url: "/admin/orderNewsPush/homeIconData.jhtml",
				type: "GET",
				async: true,
				//data: {"id":newsPushId},
				dataType: "json",
				success: function(data) {						
					if(data.code == "0") {
						var dat = data.data;
						if(dat.orderTotal == 0) {
							$(".infoType_B .supply_Bu .ListNum").css("display","none");
						}else {
							$(".infoType_B .supply_Bu .ListNum").css("display","block");
							$(".infoType_B .supply_Bu .ListNum").html(dat.orderTotal);
						}
						if(dat.purchaseTotal == 0) {
							$(".infoType_B .withdraw_Bu .ListNum").css("display","none");
						}else {
							$(".infoType_B .withdraw_Bu .ListNum").css("display","block");
							$(".infoType_B .withdraw_Bu .ListNum").html(dat.purchaseTotal);
						}
						if(dat.orderTotal == 0 && dat.purchaseTotal == 0) {
							$(".info_num").css("display","none");
						}else {
							$(".info_num").css("display","block");
							$(".info_num").html(dat.orderTotal+dat.purchaseTotal);
						}
						$(".supplyConList").html("");
						var orderDataList = data.data.orderList;
						var infoLength= orderDataList.length;
						if(orderDataList.length>10){
							infoLength = 10;
						}
						for(var i=0;i<infoLength;i++) {
							$(".supplyConList").append('<li class="info_li">'+
 									'<a href="javascript:void(0);" onclick="jump('+orderDataList[i].orderId+','+orderDataList[i].id+','+orderDataList[i].orderType+')">'+
 									'<p class="content">'+
 									'<i class="noRead"></i>'+orderDataList[i].send+'-'+orderDataList[i].receive+
 									orderDataList[i].statusName+',订单号'+orderDataList[i].orderSn+
 									'</p>'+
 									'<span class="time">'+getMyDate(orderDataList[i].createDate)+
 									'</span>'+
 									'</a>'+
 								'</li>');
	                         /* if(orderDataList[i].operator == 'OrderParty') {
	                        	 $(".supplyConList").append('<li class="info_li">'+
	 									'<a href="javascript:void(0);" onclick="jump('+orderDataList[i].orderId+','+orderDataList[i].id+')">'+
	 									'<p class="content">'+
	 									'<i class="noRead"></i>'+orderDataList[i].supplier+'-'+
	 									orderDataList[i].statusName+',订单号'+orderDataList[i].orderSn+
	 									'</p>'+
	 									'<span class="time">'+getMyDate(orderDataList[i].createDate)+
	 									'</span>'+
	 									'</a>'+
	 								'</li>');
	                         }else if(orderDataList[i].operator == 'buyer') {
	                        	 $(".supplyConList").append('<li class="info_li">'+
	 									'<a href="javascript:void(0);" onclick="jump('+orderDataList[i].orderId+','+orderDataList[i].id+')">'+
	 									'<p class="content">'+
	 									'<i class="noRead"></i>'+orderDataList[i].buyers+'-'+orderDataList[i].need+
	 									orderDataList[i].statusName+',订单号'+orderDataList[i].orderSn+
	 									'</p>'+
	 									'<span class="time">'+getMyDate(orderDataList[i].createDate)+
	 									'</span>'+
	 									'</a>'+
	 								'</li>');
	                         }else {
	                        	 $(".supplyConList").append('<li class="info_li">'+
	 									'<a href="javascript:void(0);" onclick="jump('+orderDataList[i].orderId+','+orderDataList[i].id+')">'+
	 									'<p class="content">'+
	 									'<i class="noRead"></i>'+orderDataList[i].need+
	 									orderDataList[i].statusName+',订单号'+orderDataList[i].orderSn+
	 									'</p>'+
	 									'<span class="time">'+getMyDate(orderDataList[i].createDate)+
	 									'</span>'+
	 									'</a>'+
	 								'</li>');
	                         } */
							
						}
						//采购单-提现
						$(".withdrawConList").html("");
						var purchaseDataList = data.data.purchaseList;
						var inforleng = purchaseDataList.length;
						if(purchaseDataList.length>10) {
							inforleng = 10;
						}
						for(var i=0;i<inforleng;i++) {
							$(".withdrawConList").append('<li class="info_li">'+
									'<a href="javascript:void(0);" onclick="purchaseJump('+purchaseDataList[i].orderId+','+purchaseDataList[i].id+')">'+
									'<p class="content">'+
									'<i class="noRead"></i>'+purchaseDataList[i].send+'-'+purchaseDataList[i].receive+
									purchaseDataList[i].statusName+',订单号'+purchaseDataList[i].orderSn+
									'</p>'+
									'<span class="time">'+getMyDate(purchaseDataList[i].createDate)+
									'</span>'+
									'</a>'+
								'</li>');
							
							
                    		/* if(purchaseDataList[i].operator == 'OrderParty') {
                    			if(purchaseDataList[i].orderStatus == 'complete') {
                    				$(".withdrawConList").append('<li class="info_li">'+
    										'<a href="javascript:void(0);" onclick="purchaseJump('+purchaseDataList[i].orderId+','+purchaseDataList[i].id+')">'+
    										'<p class="content">'+
    										'<i class="noRead"></i>'+purchaseDataList[i].need+
    										purchaseDataList[i].statusName+',订单号'+purchaseDataList[i].orderSn+
    										'</p>'+
    										'<span class="time">'+getMyDate(purchaseDataList[i].createDate)+
    										'</span>'+
    										'</a>'+
    									'</li>');
                    			}else {
                    				$(".withdrawConList").append('<li class="info_li">'+
    										'<a href="javascript:void(0);" onclick="purchaseJump('+purchaseDataList[i].orderId+','+purchaseDataList[i].id+')">'+
    										'<p class="content">'+
    										'<i class="noRead"></i>'+purchaseDataList[i].supplier+'-'+
    										purchaseDataList[i].statusName+',订单号'+purchaseDataList[i].orderSn+
    										'</p>'+
    										'<span class="time">'+getMyDate(purchaseDataList[i].createDate)+
    										'</span>'+
    										'</a>'+
    									'</li>');
                    			}
                    		}else if(purchaseDataList[i].operator == 'buyer') {
                    			$(".withdrawConList").append('<li class="info_li">'+
										'<a href="javascript:void(0);" onclick="purchaseJump('+purchaseDataList[i].orderId+','+purchaseDataList[i].id+')">'+
										'<p class="content">'+
										'<i class="noRead"></i>'+purchaseDataList[i].need+
										purchaseDataList[i].statusName+',订单号'+purchaseDataList[i].orderSn+
										'</p>'+
										'<span class="time">'+getMyDate(purchaseDataList[i].createDate)+
										'</span>'+
										'</a>'+
									'</li>');
                    			
                    		}else {
                    			$(".withdrawConList").append('<li class="info_li">'+
										'<a href="javascript:void(0);" onclick="purchaseJump('+purchaseDataList[i].orderId+','+purchaseDataList[i].id+')">'+
										'<p class="content">'+
										'<i class="noRead"></i>'+purchaseDataList[i].need+
										purchaseDataList[i].statusName+',订单号'+purchaseDataList[i].orderSn+
										'</p>'+
										'<span class="time">'+getMyDate(purchaseDataList[i].createDate)+
										'</span>'+
										'</a>'+
									'</li>');
                    		} */
							
						}


					}
				}
			});
	    }
	    
		function jump(orderId,newsPushId,type) {
        	
        	$.ajax({
				url: "/admin/orderNewsPush/updateStatus.jhtml",
				type: "POST",
				async: false,
				data: {"id":newsPushId},
				dataType: "json",
				success: function(data) {

				}
			});
        	if(type == 1) {
        		goToOrder('/admin/distributionOrder/view.jhtml?id='+orderId);
        	}else {
        		goToOrder('/admin/order/view.jhtml?id='+orderId);
        	}
        	
        	//goToOrder('/admin/order/view.jhtml?id='+orderId);
        	
        }
		function purchaseJump(orderId,newsPushId) {
        	$.ajax({
				url: "/admin/orderNewsPush/updateStatus.jhtml",
				type: "POST",
				async: false,
				data: {"id":newsPushId},
				dataType: "json",
				success: function(data) {
        	      goToOrder('/admin/member/withdraw/edit.jhtml?id='+orderId);
				}
			});
        	//goToOrder('/admin/ownOrder/view.jhtml?id='+orderId);
        }

		
		$().ready(function() {
			
			var src = $(".index_nav_one>li:eq(0) ul>li:eq(0) a").attr("href");
			$("#iframe").attr("src","../homePage/index.jhtml");
			
			
			homeIcon();
			
			/*$(".index_nav_one li").eq(0).find(".nav_title i").addClass("click_border");
			$(".index_nav_one li").eq(0).find(".nav_two li").eq(0).addClass("clickTo");*/
			
			$(".index_nav_one .nav_two li a").on("click",function(){

				$(this).parents().find(".index_nav_one").find("li li").removeClass('clickTo');
				$(this).parent().addClass("clickTo");
				$(this).parents().find(".index_nav_one").find("i").removeClass('click_border');
				$(this).parent().parent().prev().find("i").addClass("click_border");
				
			});
		   	
		   	$(".vip a").on("click",function(){
		   		if($(this).attr("href").indexOf("jhtml") >= 0){
		   			$(".index_nav_one li li").removeClass("clickTo");
		   			$(".index_nav_one li i").removeClass("click_border");
		   		}
		   		
		   	});
		   	
		   	$(".zhHao a").on("click",function(){
		   		$(".index_nav_one li li").removeClass("clickTo");
		   		$(".index_nav_one li i").removeClass("click_border");
		   	});
		   	
		   	
		   	
		   	
		   	var index = 0,
				prevIndex = 0;
			$(".nextImg").on("click",function(){
				index++;
				if(index == 4){
					index = 0;
				}
				$(".LB_imgs .img_box").eq(prevIndex).animate({"left":"-100%"},1000);
				$(".LB_imgs .img_box").eq(index).css("left","100%").animate({"left":"0"},1000);
				prevIndex = index;
			});
			
			
			$(".preImg").on("click",function(){
				index--;
				if(index == -1){
					index = 3; 
				}
				$(".LB_imgs .img_box").eq(prevIndex).animate({"left":"100%"},1000);
				$(".LB_imgs .img_box").eq(index).css("left","-100%").animate({"left":"0"},1000);
				prevIndex = index;
			});
		   	
		   	$(".closeImgs").on("click",function(){
		   		var prompts = null;
		   		if($('#prompts').is(':checked')) {
		   			prompts = $("#prompts").val();
		   		}
				$.ajax({
					url: "operationTips.jhtml",
					type: "POST",
					data: {"prompts":prompts},
					dataType: "json",
					success: function(data) {
						if(data.exist == true) {
							$(".modelBox").css("display","none");
						}
						
					}
				});
		   		
		   	});
		   	
		   	$(".col").on("click",function(){
		   		var prompts = null;
		   		if($('#prompts').is(':checked')) {
		   			prompts = $("#prompts").val();
		   		}
		   		$.ajax({
					url: "operationTips.jhtml",
					type: "POST",
					data: {"prompts":prompts},
					dataType: "json",
					success: function(data) {
						if(data.exist == true) {
							$(".modelBox").css("display","none");
						}
					}
				});
		   	});

		   	$(".userInfoDiv").on("click",function(){
		   		if($(".show_news").hasClass("show")){
                    $(".show_news").removeClass("show");
		   		}else{
                    $(".show_news").addClass("show")
		   		}
		   		homeIcon();
        	});
            $(".userInfoDiv span").on("click",function(){
                if($(".show_news").hasClass("show")){
                    $(".show_news").removeClass("show");
                }else{
                    $(".show_news").addClass("show")
                }
                homeIcon();
            });


            $("*").click(function (event) {
                if (!$(this).hasClass("withdraw_Bu")&&!$(this).hasClass("setRead")&&!$(this).hasClass("supply_Bu")&&!$(this).hasClass("info_num")&&!$(this).hasClass("userInfoDiv")){
                    $(".show_news").removeClass("show");
                }
                event.stopPropagation();
            });





		   	$(".infoType_B div").on("click",function(){
		   		var type = $(this).attr('typeId');
		   		$("#ext").val(type);
		   	    $(".infoType_B div").removeClass("infoChecked");
		   	    $(this).addClass("infoChecked");
		   	    var index = $(this).index();
                $(".infoConList ul").css("display","none");
                $(".infoConList ul").eq(index).css("display","block");

			});



            var contentPath = "../orderNewsPush/supply.jhtml";
            var iframeUrl = contentPath;
            var contentPath1 = "../orderNewsPush/purchase.jhtml"
			$(".seeAll").on("click",function(){
				var ext = $("#ext").val();
				if(ext == 'purchase') {
					iframeUrl = contentPath1;
				}else {
					iframeUrl = contentPath;
				}
				
                $.dialog({
                    title: "通知中心",
                    [@compress single_line=true]
                	content: '
					<div class="modelInfo">
						<div class="model_typeB">
							<div class="supply_Bu infoChecked">
								<span class="ListNum"><\/span>
								订货单
							<\/div>
							<div class="withdraw_Bu">
								<span class="ListNum"><\/span>
								提现
							<\/div>
						<\/div>
						<iframe class="modelIframe" id="sonFrame" frameborder="0" width="" height="" src="'+iframeUrl+'"><\/iframe>
					<\/div>',
                    [/@compress]
                        width: $(".bodyObj").width()-$(".bodyObj").width()*0.08,
                    height:$(".bodyObj").height()-40,
                    ok: "",
                    cancel: "",
					onShow:function(){
						if(ext == 'purchase') {
							$(".model_typeB div").removeClass("infoChecked");
							$(".model_typeB div").eq(1).addClass("infoChecked");
						}
				
						$(".model_typeB div").on("click",function(){
							$(".model_typeB div").removeClass("infoChecked");
							$(this).addClass("infoChecked");
							if($(this).hasClass("supply_Bu")){
                                $(".modelIframe").prop("src",contentPath);
							}else{
                                $(".modelIframe").prop("src",contentPath1);
							}
						});
            		},
                    onOk: function(){

					},
					onClose:function(){

					},
					onCancel:function(){

					}
				});

			});

            $(".setRead").on("click",function(){
            	var ext = $("#ext").val();
        		if(ext == 'purchase') {
        			$.ajax({
        				url: "/admin/orderNewsPush/allMarkAsReadByPurchases.jhtml",
        				type: "POST",
        				async: false,
        				//data: {"id":newsPushId},
        				dataType: "json",
        				success: function(data) {
        					if(data.code == 0) {
        						var orderTotal = data.data.orderTotal;
        						var purchaseTotal = data.data.purchaseTotal;
        						if(orderTotal == 0) {
        							$(".infoType_B .supply_Bu .ListNum").css("display","none");
        						}
        						if(purchaseTotal == 0) {
        							$(".infoType_B .withdraw_Bu .ListNum").css("display","none");
        						}
        						if(orderTotal == 0 && purchaseTotal == 0) {
        							$(".info_num").css("display","none");
        						}
        						
       							$(".info_num").html(data.data.orderTotal+data.data.purchaseTotal);
           						$(".infoType_B .supply_Bu .ListNum").html(data.data.orderTotal);
           						$(".infoType_B .withdraw_Bu .ListNum").html(data.data.purchaseTotal);
        						
        						$(".withdrawConList").html("");
        						return;
        						
        					}
        				}
        			});
        		}else {
        			$.ajax({
        				url: "/admin/orderNewsPush/allMarkAsReadByOrders.jhtml",
        				type: "POST",
        				async: false,
        				//data: {"id":newsPushId},
        				dataType: "json",
        				success: function(data) {
        					if(data.code == "0") {
        						var orderTotal = data.data.orderTotal;
        						var purchaseTotal = data.data.purchaseTotal;
        						if(orderTotal == 0) {
        							$(".infoType_B .supply_Bu .ListNum").css("display","none");
        						}
        						if(purchaseTotal == 0) {
        							$(".infoType_B .withdraw_Bu .ListNum").css("display","none");
        						}
        						if(orderTotal == 0 && purchaseTotal == 0) {
        							$(".info_num").css("display","none");
        						}
        						
       							$(".info_num").html(data.data.orderTotal+data.data.purchaseTotal);
           						$(".infoType_B .supply_Bu .ListNum").html(data.data.orderTotal);
           						$(".infoType_B .withdraw_Bu .ListNum").html(data.data.purchaseTotal);
        						
        						$(".supplyConList").html("");
        						return;
        					}
        				}
        			});
        		}
        	});

			
		});
		</script>
		
	</body>
</html>
[/#escape]