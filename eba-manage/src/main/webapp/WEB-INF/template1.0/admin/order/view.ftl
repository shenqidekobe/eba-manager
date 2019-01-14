[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />

<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

	<style>
        body {
            background: #f9f9f9;
        }

        .pag_div {
            width: 45%;
            float: left;
        }

        label.error {
            left: 300px;
        }

        .add_list .form_title {
            float: left;
        }

        table th {
            border-top: 1px solid #f0f0f0;
        }

        .col-sm-7 {
            width: 72%
        }

        .xxDialog {
            top: 40px
        }

        #addProductImage {
            margin: 10px 30px;
        }

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

        iframe {
            height: calc(100% - 5px);
            width: 100%;
        }
        
        input:disabled{
        	color:#c1cde1;
        }
        input:disabled:hover{
        	background:#fff;
        	color:#c1cde1;
        }
        .table_info th{background:#f9f9f9;border:1px solid #f0f0f0;}
        .table_info td{border:1px solid #f0f0f0; padding:0;}
        .table_info .td_list{width:100%;overflow: hidden;}
        .table_info .td_list p{float:left;border-right:1px solid #f0f0f0;height:36px;line-height:36px ; overflow:hidden;border-bottom:1px solid #f0f0f0;}
        .table_info td .td_list:nth-last-child(1) p{border-bottom:0;}
        .table_info td .td_list p:nth-last-child(1){border-right:0;}
        .table_info .td_list p.wb{width:104px;}
        .table_info .td_list p.wl{width:84px;}
        .table_info .td_list p.wy{width:144px;}
        
        .tabCon,#tab-system{overflow: auto;}
        .xxDialog .dialogBottom{height:36px;}
        .table_bb{padding:15px; text-align: right;}
        
        .table_box .tabBorder th{background:#f9f9f9;}
        .table_box .tabBorder td,.table_box .tabBorder th{
        	border:1px solid #f0f0f0;
        }
        
        .form_title{padding:10px 0;}
        .fujian{width:90%;}
        .fujian .form-label{width:80px;}.webuploader-container {
			position: relative;
		}
		.webuploader-element-invisible {
			position: absolute !important;
			clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
		    clip: rect(1px,1px,1px,1px);
		}
		.webuploader-pick {
			position: relative;
			display: inline-block;
			cursor: pointer;
			background: #4DA1FF;
			padding: 3px 15px;
			color: #fff;
			text-align: center;
			border-radius: 16px;
			overflow: hidden;
		}
		.webuploader-pick-hover {
			background: #1A73D8;
		}
		
		.webuploader-pick-disable {
			opacity: 0.6;
			pointer-events:none;
		}
        .fujianList{color:#333;}
        .fujianList .fj_attach i{margin-left:10px;}
        .fujianList .fj_attach strong{margin-left:10px; font-weight: 100;}
        .fujianList ul{overflow: hidden;}
        .fujianList ul li{float:left;margin-right:20px;line-height:34px;}
        .fujianList ul li a{color:#333;}
        .fujianList ul li a:hover{text-decoration: underline;}
        
        #thelist>div{overflow: hidden;height:30px;padding-left:10px;border-radius: 4px;}
        #thelist>div:hover{background:#f9f9f9;}
        #thelist .info{float:left;width:80%;color:#333;font-size:12px;}
        #thelist .delFil{
        	display:block;
        	height:100%;
        	float:right;
        	width:30px;
        	background:url(../../../../resources/admin1.0/images/quxiaofj_icon.svg) no-repeat center;
        	background-size:20px 20px;
        }
         #thelist .wjImg{
         	display:block;
         	float:left;
         	width:20px;
         	height:30px;
         	background:url(../../../../resources/admin1.0/images/wenjian_icon.svg) no-repeat center;
         	background-size:15px 15px;
         }
         .xxDialog .dialogBottom{border-top:1px solid #eee;}
		td input.error{border:0;}
		td label.error{opacity: 0;}
	</style>
<title>查看订单</title>
</head>
<body class="bodyObj">
<form id="receiveForm" action="receive.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<form id="completeForm" action="complete.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<form id="returnsForm" action="returns.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<form id="failForm" action="fail.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>

<form id="applyCancelForm" action="applyCancel.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
    <input type="hidden" id="passed" name="passed"/>
</form>

<form id="cancelForm" action="cancel.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>

<form id="cancelShippingForm" action="cancelShipped.jhtml" method="post">
    <input type="hidden" name="orderId" value="${order.id}"/>
    <input type="hidden" name="shippingId" id="cancelShippingId" value=""/>
</form>
<form id="editView" [#if order.supplyType == 'temporary'] action="editView.jhtml" [#else] action="editViewByFormal.jhtml" [/#if]  method="get">
    <input type="hidden" name="id" value="${order.id}"/>
</form>

<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
				<li><a href="list.jhtml">订货单</a></li>
				<li>${message("admin.order.view")}</li>
			</ul>
		</div>
		<div class="form_box">
			<form class="form form-horizontal" id="form-article-add">
				<div id="tab-system" class="HuiTab">
					<div class="tabBar cl">
						<span>${message("admin.order.orderInfo")}</span>
						<span>${message("admin.order.productInfo")}</span>
						<span>${message("admin.order.shippingInfo")}</span>
						<span>${message("admin.order.orderLog")}</span>
						<span>返佣记录</span>
					</div>
					<div class="tabCon">
						<div class="opera_butt">
							[@shiro.hasPermission name = "admin:print:order"]
								<input type="button" id="print" class="tab_button bgfff" value="打印" />
                            [/@shiro.hasPermission]
                            [#if order.supplier.id == supplierId]
                                [@shiro.hasPermission name = "admin:order:review"]
                                	<input type="button" id="reviewButton" class="tab_button bgfff" value="${message("admin.order.review")}"[#if order.hasExpired() || order.status != "pendingReview"] style="display: none" disabled="disabled"[/#if] />
                                [/@shiro.hasPermission]
                                [@shiro.hasPermission name = "admin:order:updateItems"]
                                	<input type="button" id="updateButton" class="tab_button bgfff" value="修改"[#if order.status != "pendingShipment" || isOverUpdate] style="display: none" disabled="disabled"[/#if] />
    							[/@shiro.hasPermission]
    							[@shiro.hasPermission name = "admin:order:shipping"]
                                	<input type="button" id="shippingButton" class="tab_button bgfff" value="${message("admin.order.shipping")}"[#if order.shippableQuantity <= 0] style="display: none" disabled="disabled"[/#if] />
                               	[/@shiro.hasPermission]
                               	[@shiro.hasPermission name = "admin:order:applyCancel"]
                                	<input type="button" id="applyCancelButton" class="tab_button bgfff" value="用户申请"[#if order.hasExpired() || order.status != "applyCancel"] style="display: none" disabled="disabled"[/#if] />
                                [/@shiro.hasPermission]
                                [@shiro.hasPermission name = "admin:order:cancel"]
                                	<input type="button" id="cancelButton" class="tab_button bgfff" value="取消"[#if order.status == "completed" || order.status == "canceled" || order.status == "denied" ||order.status == "applyReturns" ||order.status == "returns"] style="display: none" disabled="disabled"[/#if] />
    							[/@shiro.hasPermission]
    							[@shiro.hasPermission name = "admin:order:returns"]
                                	<input type="button" id="returnsButton" class="tab_button bgfff" value="确认退货"[#if order.status != "applyReturns"] style="display: none" disabled="disabled"[/#if] />
                                [/@shiro.hasPermission]
    							[@shiro.hasPermission name = "admin:order:complete"]
                                	<input type="button" id="completeButton" class="tab_button bgfff" value="${message("admin.order.complete")}"[#if order.status == "canceled" || order.status == "completed" || order.status == "denied" ||order.status == "returns"] style="display: none" disabled="disabled"[/#if] />
    							[/@shiro.hasPermission]
                            [/#if]
						</div>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.sn")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.sn}</span>
                                </div>
                            </div>
                            <!--
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货点</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.need.name}</span>
                                </div>
                            </div>-->
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.consignee")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.consignee}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.amount")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">
                                    [#if order.type == "billDistribution"]
                                        [#if order.supplier.id == supplierId]
                                            ${currency(order.amountToB, true)}
                                        [#else]
                                            ${currency(order.amount, true)}
                                        [/#if]
                                    [#else]
                                        ${currency(order.amount, true)}
                                    [/#if]
                                    </span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">下单人</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">
                                    ${order.childMember.nickName}
                                    <!--${order.getCreateOrderLog().operator}-->
                                </span>
                                </div>
                            </div>
                             <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.phone")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.phone}</span>
                                </div>
                            </div>
                        </div>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货地址</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="adress_no">${order.areaName} ${order.address}</span>
                                </div>
                            </div>
                            <!--
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货时间</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.reDate?string("yyyy-MM-dd")}</span>
                                </div>
                            </div>-->
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.status")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${message("Order.Status." + order.status)}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">已付金额</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${currency(order.amountPaid, true)}</span>
                                </div>
                            </div>
                            [#if order.status == "applyReturns" || order.status == "returns"]
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">退货单号</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.returnsNum}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">退货理由</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.returnsReason}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">申请退货时间</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.applyReturnsDate?string("yyyy-MM-dd HH:mm")}</span>
                                </div>
                            </div>
                            [#if order.status == "returns"]
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">退款金额</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${currency(order.refundAmount, true)}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">确认退货时间</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.confirmReturnsDate?string("yyyy-MM-dd HH:mm")}</span>
                                </div>
                            </div>
                            [/#if]
                            [/#if]
                            <!-- <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货点备注</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_text">
                                    [#noescape]${order.member.need.description}[/#noescape]
                                    </span>
                                </div>
                            </div>  -->
                        </div>
                        <div class="pag_div" style="width:96%;padding-top:0;">
                        	<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">订单备注</label>
                                <div class="formControls col-xs-8 col-sm-7" style="width:88%">
                                	[@shiro.hasPermission name = "admin:order:addRemarks"]
                                    	<button type="button" id="addFujian" class="tab_button bgred" style="margin-top:3px;margin-left:0;">添加</button>
	                            	[/@shiro.hasPermission]
	                            	[#list order.orderRemarks as orderRemark]
                                    [#if order.type != 'formal']
                                        [#if order.toSupplier == null && orderRemark.msgType=="btoc"]
                                       	<div class="fujianList">
    										<div class="fj_attach"  style="overflow: hidden;">
    											<span class="text" style="display:block;float:left;width:60%;">${orderRemark.description}</span>
    											<div style="float:right;width:38%;text-align: right;">
    												<strong>【${orderRemark.name}/${orderRemark.suppliper}</strong>
    												<i>${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</i>】
    											</div>
    										</div>
    										<ul>
    											[#list orderRemark.annex as annex]
    												<li>
    													<a href="${annex.url}" download="${annex.fileName }">${abbreviate(annex.fileName, 50, "...")}</a>
    												</li>
    											[/#list]
    										</ul>
    									</div>
                                        [#elseif order.toSupplier.id == supplierId && orderRemark.msgType=="btoc"]
                                        <div class="fujianList">
                                            <div class="fj_attach"  style="overflow: hidden;">
                                                <span class="text" style="display:block;float:left;width:60%;">${orderRemark.description}</span>
                                                <div style="float:right;width:38%;text-align: right;">
                                                    <strong>【${orderRemark.name}/${orderRemark.suppliper}</strong>
                                                    <i>${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</i>】
                                                </div>
                                            </div>
                                            <ul>
                                                [#list orderRemark.annex as annex]
                                                    <li>
                                                        <a href="${annex.url}" download="${annex.fileName }">${abbreviate(annex.fileName, 50, "...")}</a>
                                                    </li>
                                                [/#list]
                                            </ul>
                                        </div>
                                        [#elseif order.supplier.id == supplierId && orderRemark.msgType=="btob"]
                                        <div class="fujianList">
                                            <div class="fj_attach"  style="overflow: hidden;">
                                                <span class="text" style="display:block;float:left;width:60%;">${orderRemark.description}</span>
                                                <div style="float:right;width:38%;text-align: right;">
                                                    <strong>【${orderRemark.name}/${orderRemark.suppliper}</strong>
                                                    <i>${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</i>】
                                                </div>
                                            </div>
                                            <ul>
                                                [#list orderRemark.annex as annex]
                                                    <li>
                                                        <a href="${annex.url}" download="${annex.fileName }">${abbreviate(annex.fileName, 50, "...")}</a>
                                                    </li>
                                                [/#list]
                                            </ul>
                                        </div>
                                        [/#if]
                                    [#else]
                                        <div class="fujianList">
                                            <div class="fj_attach"  style="overflow: hidden;">
                                                <span class="text" style="display:block;float:left;width:60%;">${orderRemark.description}</span>
                                                <div style="float:right;width:38%;text-align: right;">
                                                    <strong>【${orderRemark.name}/${orderRemark.suppliper}</strong>
                                                    <i>${orderRemark.createDate?string("yyyy-MM-dd HH:mm:ss")}</i>】
                                                </div>
                                            </div>
                                            <ul>
                                                [#list orderRemark.annex as annex]
                                                    <li>
                                                        <a href="${annex.url}" download="${annex.fileName }">${abbreviate(annex.fileName, 50, "...")}</a>
                                                    </li>
                                                [/#list]
                                            </ul>
                                        </div>
                                    [/#if]
									[/#list]
                                </div>
                            </div>
                        </div>
                       
                       <div class="pag_div" style="width:96%;padding-top:0;">
                        	<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">订单附件</label>
                                <div class="formControls col-xs-8 col-sm-7" style="width:88%">
                                   	<div class="fujianList">
										<ul>
											[#list order.orderFiles as orderFiles]
												<li>
													<a href="${orderFiles.source}" download="${orderFiles.title}">${orderFiles.title}</a>
												</li>
											[/#list]
										</ul>
									</div>
									
                                </div>
                            </div>
                        </div>
					</div>
					
					<div class="tabCon">
						<div class="table_box" style="margin-top:20px;">
							<table class="table table-border table-hover table_width boo">
								<thead>
									<tr class="text-l">
										<th width="16%">${message("OrderItem.sn")}</th>
										<th width="15%">${message("OrderItem.name")}</th>
										<th width="15%">商品规格</th>
										<th width="8%">基本单位</th>
										<th width="10%">${message("OrderItem.price")}</th>
										<th width="10%">${message("OrderItem.quantity")}</th>
										<th width="10%">发货数量</th>
										<th width="10%">实收数量</th>
										<th width="10%">${message("OrderItem.subtotal")}</th>
									</tr>
								</thead>
							</table>
							<div class="list_t_tbody">
								<table class="table table-border table-hover table_width">
									<thead>
										<tr class="text-l">
											<th width="16%"><div class="th_div">商品编号</div></th>
											<th width="15%"><div class="th_div">商品名称</div></th>
											<th width="15%"><div class="th_div">商品规格</div></th>
											<th width="8%"><div class="th_div">基本单位</div></th>
											<th width="10%"><div class="th_div">商品价格</div></th>
											<th width="10%"><div class="th_div">商品数量</div></th>
											<th width="10%"><div class="th_div">发货数量</div></th>
											<th width="10%"><div class="th_div">实收数量</div></th>
											<th width="10%"><div class="th_div">小计</div></th>
										</tr>
									</thead>
									<tbody>
	                                    [#list order.orderItems as orderItem]
	                                    <tr class="text-l">
	                                        <td>${orderItem.sn}</td>
	                                        <td>${abbreviate(orderItem.name, 50, "...")}
	                                            </td>
	                                        <td>
	                                        	[#if orderItem.specifications?has_content]
	                                                [${orderItem.specifications?join(", ")}]
	                                            [/#if]
	                                        </td>
	                                        <td>
	                                        	[#if orderItem.product.goods.unit??]
	                                        		${message("Goods.unit."+orderItem.product.goods.unit)}
	                                        	[/#if]
	                                        </td>
	                                        <td>

	                                            [#if orderItem.type == "general" || orderItem.type == "distribution"]
                                                    [#if order.toSupplier == null]
	                                                   ${currency(orderItem.priceUnit, true)}
                                                    [#elseif order.toSupplier.id == supplierId]
                                                        ${currency(orderItem.priceUnit, true)}
                                                    [#elseif order.supplier.id == supplierId]
                                                        ${currency(orderItem.priceUnitB, true)}
                                                    [/#if]
	                                            [#else]
	                                                -
	                                            [/#if]</td>
	                                        <td>${orderItem.quantity}</td>
	                                        <td>${orderItem.shippedQuantity}</td>
	                                        <td>${order.getRealProductQuantity(orderItem.product.id)}</td>
	                                        <td>
	                                            [#if orderItem.type == "general" || orderItem.type == "distribution"]
                                                    [#if order.toSupplier == null]
                                                       ${currency(orderItem.price, true)}
                                                    [#elseif order.toSupplier.id == supplierId]
                                                        ${currency(orderItem.price, true)}
                                                    [#elseif order.supplier.id == supplierId]
                                                        ${currency(orderItem.priceB, true)}
                                                    [/#if]
	                                            [#else]
	                                                -
	                                            [/#if]
	                                        </td>
	                                    </tr>
	                                    [/#list]
									</tbody>
								</table>
							</div>
						</div>
					</div>
                    <div class="tabCon">
                    	<div class="table_box">
                    		[#list order.shippings as shipping]
                    		<div class="table_one">
                    			<input type="hidden" name="shippingItems" id="shippingItems" value="${shipping.shippingItems}">
                    			<div class="table_bb">
                    				[@shiro.hasPermission name = "admin:print:orderShippingInfo"]
                    					<button type="button" id="deliveryInfor" class="tab_button bgred" onclick="javascript:printOrder(${order.id} , ${shipping.id});">打印</button>
                    				[/@shiro.hasPermission]
                    				[@shiro.hasPermission name = "admin:order:cancelShipped"]
                    					<button type="button" class="tab_button bgred" onclick="javascript:cancelShipping(${order.id} , ${shipping.id});"[#if shipping.status == "senderChecked"] style="display: none" disabled="disabled"[/#if]>作废</button>
                    				[/@shiro.hasPermission]
                    			</div>
                    			<table class="table table-border table-hover table_width boo">	
                            		<thead>
                            			<tr class="text-l">
                            				<th>id</th>
                            				<th>${message("Shipping.sn")}</th>
                            				<th>${message("Shipping.deliveryCorp")}</th>
                            				<th>${message("Shipping.trackingNo")}</th>
                            				<th>${message("Shipping.deliveryCode")}</th>
                            				<th>${message("Shipping.consignee")}</th>
                            				<th>${message("admin.common.createDate")}</th>
                            				<th>状态</th>
                            			</tr>
                            		</thead>
                            		<tbody>
                            			<tr>
                            				<td>${shipping.id}</td>
                            				<td>${shipping.sn}</td>
                            				<td>${shipping.deliveryCorp!"-"}</td>
                            				<td>${shipping.trackingNo!"-"}</td>
                            				<td>${shipping.deliveryCode}</td>
                            				<td>${shipping.consignee!"-"}</td>
                            				<td>${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                            				<td>
                            					[#if shipping.status == 'senderChecked']
                            						已收货
                            					[#else]
                            						未收货
                            					[/#if]
                            				</td>
                            			</tr>
                            		</tbody>
                        		</table>
                        		<h3 class="form_title">商品信息</h3>
                        		<table class="tabBorder table table-border table-hover table_width boo">	
                            		<thead>
                            			<tr class="text-c">
                            				<th>序号</th>
                            				<th>商品编码</th>
                            				<th>商品名称</th>
                            				<th>商品规格</th>
                            				<th>基本单位</th>
                            				<th>发货数量</th>
                            				<th>收货数量</th>
                            			</tr>
                            		</thead>
                            		<tbody>
                            			[#list shipping.shippingItems as shippingItem]
                            			<tr class="text-c">
                            				<td>${shippingItem_index+1}</td>
                            				<td>${shippingItem.sn}</td>
                            				<td>${shippingItem.name}</td>
                            				<td>
                            					[#if shippingItem.specifications?has_content]
		                                            <span class="silver">${shippingItem.specifications?join(", ")}</span>
		                                        [/#if]
                            				</td>
                            				<td>
                            					[#if shippingItem.product.goods.unit??]
                            					${message("Goods.unit."+shippingItem.product.goods.unit)}
                            					[/#if]
                            				</td>
                            				<td>${shippingItem.quantity}</td>
                            				<td>${shippingItem.trueRealQuantity}</td>
                            			</tr>
                            			[/#list]
                            			
                            		</tbody>
                        		</table>
                        	</div>
                        	[/#list]
                    	</div>
                        
                    </div>
                    <div class="tabCon">
                    	<div class="table_box" style="margin-top:20px;">
	                        <table class="table table-border table-hover table_width boo">
	                            <thead>
	                            <tr class="text-l">
	                                <th width="16%">${message("OrderLog.type")}</th>
	                                <th width="16%">${message("OrderLog.operator")}</th>
	                                <th width="10%">${message("OrderLog.content")}</th>
	                                <th width="10%">${message("admin.common.createDate")}</th>
	                            </tr>
	                            </thead>
	                        </table>
	                        <div class="list_t_tbody">
	                            <table class="table table-border table-hover table_width">
	                                <thead>
	                                <tr class="text-l">
	                                    <th width="16%">
	                                        <div class="th_div">商品编号</div>
	                                    </th>
	                                    <th width="16%">
	                                        <div class="th_div">商品名称</div>
	                                    </th>
	                                    <th width="10%">
	                                        <div class="th_div">商品价格</div>
	                                    </th>
	                                    <th width="10%">
	                                        <div class="th_div">商品数量</div>
	                                    </th>
	
	                                </tr>
	                                </thead>
	                                <tbody>
	                                    [#list order.orderLogs as orderLog]
                                                <tr>
        	                                        <td>
        	                                        ${message("OrderLog.Type." + orderLog.type)}
        	                                        </td>
        	                                        <td>
                                                        ${orderLog.operator!"-"}
        	                                        </td>
        	                                        <td>
        	                                            [#if orderLog.content??]
        	                                                <span title="${orderLog.content}">${abbreviate(orderLog.content, 50, "...")}</span>
        	                                            [#else]
        	                                                -
        	                                            [/#if]
        	                                        </td>
        	                                        <td>
        	                                            <span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
        	                                        </td>
        	                                    </tr>
	                                    [/#list]
	
	                                </tbody>
	                            </table>
	                        </div>
                        </div>
                    </div>
                    
                    <div class="tabCon">
						<div class="table_box" style="margin-top:20px;">
						    [#if order.rakeBack]
							<table class="table table-border table-hover table_width boo">
								<thead>
									<tr class="text-l">
										<th width="30%">返佣人</th>
										<th width="20%">级别</th>
										<th width="50%">返佣金额</th>
									</tr>
								</thead>
							</table>
							<div class="list_t_tbody">
								<table class="table table-border table-hover table_width">
									<thead>
										<tr class="text-l">
											<th width="30%"><div class="th_div">返佣人</div></th>
											<th width="20%"><div class="th_div">级别</div></th>
											<th width="50%"><div class="th_div">返佣金额</div></th>
										</tr>
									</thead>
									<tbody>
	                                    <tr class="text-l">
	                                        <td>${order.done.nickName}</td>
	                                        <td>一级 </td>
	                                        <td>${order.done_score}</td>
	                                    </tr>
	                                    [#if order.dtwo??]
	                                    <tr class="text-l">
	                                        <td>${order.dtwo.nickName}</td>
	                                        <td>二级 </td>
	                                        <td>${order.dtwo_score}</td>
	                                    </tr>
	                                    [/#if]
	                                    [#if order.dthree??]
	                                    <tr class="text-l">
	                                        <td>${order.dthree.nickName}</td>
	                                        <td>三级 </td>
	                                        <td>${order.dthree_score}</td>
	                                    </tr>
	                                    [/#if]
									</tbody>
								</table>
								[#else]
								   <table class="table table-border table-hover table_width boo">
										<thead>
											<tr class="text-l">
												<th width="100%">暂无任何返佣记录</th>
											</tr>
										</thead>
									</table>
						        [/#if]
							</div>
						</div>
					</div>
				</div>


				<div class="footer_submit">
					
					<input class="btn radius cancel_B" type="button" value="返回" onclick="history.back();return false;">
				</div>
			</form>
		</div>
	</div>
    <!--_footer 作为公共模版分离出去-->
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript">
$(function(){



    var $passed = $("#passed");
    var $receiveForm = $("#receiveForm");
    var $completeForm = $("#completeForm");
    var $returnsForm = $("#returnsForm");
    var $failForm = $("#failForm");
    var $reviewButton = $("#reviewButton");
    var $paymentButton = $("#paymentButton");
    var $refundsButton = $("#refundsButton");
    var $shippingButton = $("#shippingButton");
    var $returnsButton = $("#returnsButton");
    var $receiveButton = $("#receiveButton");
    var $completeButton = $("#completeButton");
    var $transitStep = $("a.transitStep");
    var isLocked = false;

    var $applyCancelButton = $("#applyCancelButton");
    var $applyCancelForm = $("#applyCancelForm");

    var $cancelButton = $("#cancelButton");
    var $cancelForm = $("#cancelForm");
    var $updateButton = $("#updateButton");
    var $addFujian = $("#addFujian");

    [@flash_message /]

    // 检查锁定
    function checkLock() {
        if (!isLocked) {
            $.ajax({
                url: "check_lock.jhtml",
                type: "POST",
                data: {id: ${order.id}},
                dataType: "json",
                cache: false,
                success: function(message) {
                    if (message.type != "success") {
                        $.message(message);
                        $reviewButton.add($paymentButton).add($refundsButton).add($shippingButton).add($returnsButton).add($receiveButton).add($completeButton).prop("disabled", true);
                        isLocked = true;
                    }
                }
            });
        }
    }

    // 检查锁定
    checkLock();
    setInterval(function() {
        checkLock();
    }, 50000);



	/*通过js获取页面高度，来定义表单的高度*/
	var formHeight=$(document.body).height()-100;
	$(".form_box").css("height",formHeight);
	$(".tabCon").css("height",formHeight - 40);
	
	
	/*添加备注*/
	$addFujian.click(function(){
		var fileUrl = {};
		$.dialog({
			title:"订单备注",
			width:600,
			height:360,
			content:[@compress single_line = true]
			'<form id="orderRemark" class="form form-horizontal" action="addNotes.jhtml" enctype="multipart/form-data" method="post">
				<input type="hidden" name="token" value="${token}" \/>
		        <input type="hidden" name="orderId" id="orderId" value="${order.id}" \/>
				<div class="pag_div fujian" style="width:90%;">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />备注<\/label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <textarea id="description" name="description" placeholder="最多输入100个字" class="text_area1"></textarea>
                        <\/div>
                    <\/div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><\/label>
                        <div class="formControls col-xs-8 col-sm-7">
                           <div id="uploader" class="wu-example">
			                    <div class="btns">
							        <div id="picker">选择文件</div>
							    </div>
		                    	<div id="thelist" class="uploader-list"></div>
							</div>
                        <\/div>
                    <\/div>
                    
                </div>
			</form>
			'
			[/@compress],
			onOk: function() {

	           var $description = $("#description").val();
	           var $orderId = $("#orderId").val();
	            var formDate = {} ;
	            var index = 0 ;
                $.each(fileUrl,function(key,value) {
                	//fileUrl.push({'url':data.url , "fileName":file.file.name , "suffix":file.file.ext , "size": file.file.size});
                    formDate['annex[' + index +'].url'] = value.url ;
                    formDate['annex[' + index +'].fileName'] = value.fileName ;
                    formDate['annex[' + index +'].suffix'] = value.suffix ;
                    formDate['annex[' + index +'].size'] = value.size ;
                    index ++ ;
                	//formDate['annex[' + index +'].fileUrl.url'] = value.url ;
                });
                formDate['orderId'] = $orderId ;
                formDate['description'] = $description ;
                if(!$("#orderRemark").valid()){
		            return false ;
		     	}
                $.ajax({
                    url: "addRemarks.jhtml",
                    type: "POST",
                    data: formDate,
                    dataType: "json",
                    cache: false,
                    success: function (message) {
                	   if (message.type == "success") {
                            $.message(message);
                            setTimeout(function() {
                                window.location.href="view.jhtml?id=${order.id}";
                            }, 2000);
                        }else{
                           $.message(message);
                        }
                    }
                });
	        },
			onShow:function(){
				$("#orderRemark").validate({
                    rules: {
                    	description:{
                    		required:true,
                    		maxlength:100
                    	}
                    }
                });	
				var uploadUrl = "/admin/file/upload.jhtml" ;
				
				//var $progressBar = $('<div class="progressBar"><\/div>').appendTo("body");
				var uploader = WebUploader.create({
				    // swf文件路径
				   	swf: '/resources/admin1.0/flash/webuploader.swf',
				    // 文件接收服务端。
				    server:uploadUrl + (uploadUrl.indexOf('?') < 0 ? '?' : '&') + 'token=' + getCookie("token"),
				    // 选择文件的按钮。可选。
				    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
				    pick: '#picker',
				    fileSingleSizeLimit: 1024 * 1024 * 30,
				    accept: {
				    	extensions: 'jpg,jpeg,bmp,gif,png,pdf'
					},
					//验证文件总数量, 超出则不允许加入队列
					fileNumLimit: 10,
					//auto {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传
					auto: true,
				    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
				    resize: false
				}).on('uploadAccept', function(file, data) {
					//var addValue = {} ;
					fileUrl[file.file.id] = {'url':data.url , "fileName":file.file.name , "suffix":file.file.ext , "size": file.file.size} ;
					//fileUrl.push(addValue);
					/* fileUrl["url"] = data.url;
					fileUrl["fileName"] = file.file.name;
					fileUrl["suffix"] = file.file.ext;
					fileUrl["size"] = file.file.size; */
					
				}).on('error', function(type) {
					switch(type) {
					case "F_EXCEED_SIZE":
						$.message("warn", "上传文件大小超出限制");
						break;
					case "Q_TYPE_DENIED":
						$.message("warn", "上传文件格式不正确");
						break;
					case "F_DUPLICATE":
						$.message("warn", "文件重复上传");
						break;
					default:
						$.message("warn", "上传文件出现错误");
				}
			}).on('fileQueued', function(file) {
				var fileType = "image" ;
				if(file.ext == "pdf"){
					fileType = "file";
				}
				uploader.options.formData={fileType:fileType};
			});
				var  $list = $("#thelist");
				// 当有文件被添加进队列的时候
				uploader.on( 'fileQueued', function( file ) {
				    $list.append( '<div id="' + file.id + '" class="item">' +
				        '<span class="wjImg"></span>'+'<h4 class="info">' + file.name + '</h4>' + '<span class="delFil"></span>'+
				    '</div>' );
					$(".delFil").off("click").on("click",function(){
				    	var fileId = $(this).parent().attr("id");
				    	delete fileUrl[fileId];
				    	uploader.removeFile( fileId,true);
				    	$(this).parent().remove();
				    })
				});
				
			}
		});
	});
	
	
	
	
	
	
	
	
	

	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
	
    [#if order.shippableQuantity > 0]
	$("#shippingButton").click(function(){
		$.dialog({
			title:"${message("admin.order.shipping")}",
			width:$(".bodyObj").width()-20,
			height:$(".bodyObj").height()-40,
			content:[@compress single_line = true]
                     '<form id="shippingForm" class="form form-horizontal" action="shipping.jhtml" method="post" enctype="multipart\/form-data">
                            <input type="hidden" name="token" value="${token}" \/>
                            <input type="hidden" name="orderId" value="${order.id}" \/>
                            <input type="hidden" class="input-text radius" name="zipCode" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" \/>
                            <div class="pag_div">
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.shippingMethod")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius down_list"  placeholder="请选择" id="shippingMethod" name="shippingMethod" readonly="readonly" \/>
                                        <input type="text" class="downList_val" id="needId" name="shippingMethodId"\/>
                                        <ul class="downList_con">
                                            <li val="">${message("admin.common.choose")}<\/li>
                                            [#list shippingMethods as shippingMethod]
                                                [#noescape]
                                                 <li val="${shippingMethod.id}" [#if shippingMethod == order.shippingMethod] class="li_bag"[/#if]>${shippingMethod.name?html?js_string}<\/li>
                                                [/#noescape]
                                            [/#list]
                                        <\/ul>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.trackingNo")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius" placeholder="" name="trackingNo" \/>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.area")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <span class="fieldSet">
                                            <input type="hidden" id="areaId" name="areaId" value="${(order.area.id)!}"\/>
                                            <input type="text" class="input-text radius" placeholder="" readonly="readonly" value="${(order.areaName)!}" \/>
                                        <\/span>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.phone")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius" placeholder="" name="phone" readonly="readonly" value="[#noescape]${order.phone?html?js_string}[/#noescape]"\/>
                                    <\/div>
                                <\/div>
                            <\/div>
                            <div class="pag_div">
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.deliveryCorp")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius down_list"  placeholder="请选择" id="deliveryCorp" readonly="readonly" \/>
                                        <input type="text" class="downList_val" name="deliveryCorpId"\/>
                                        <ul class="downList_con need">
                                            <li val="">${message("admin.common.choose")}<\/li>
                                            [#list deliveryCorps as deliveryCorp]
                                                [#noescape]
                                                <li val="${deliveryCorp.id}"[#if order.shippingMethod?? && deliveryCorp == order.shippingMethod.defaultDeliveryCorp] class="li_bag"[/#if]>${deliveryCorp.name?html?js_string}<\/li>
                                                [/#noescape]
                                            [/#list]
                                        <\/ul>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.consignee")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" name="consignee" id="consignee" value="[#noescape]${order.consignee?html?js_string}[/#noescape]"\/>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">${message("Shipping.address")}<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" id="address" name="address" value="[#noescape]${order.address?html?js_string}[/#noescape]"\/>
                                    <\/div>
                                <\/div>
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">发货备注<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius" placeholder="" name="memo" id="memo" \/>
                                    <\/div>
                                <\/div>
                            <\/div>
                           	<div class="table_box">
	                            <table id="productTable" class="table table-border table-hover table_width boo">
	                                <tr class="text-l">
	                                    <th >${message("ShippingItem.sn")}<\/th>
	                                    <th >${message("ShippingItem.name")}<\/th>
	                                    <th >${message("ShippingItem.isDelivery")}<\/th>
	                                    <th >${message("admin.order.productStock")}<\/th>
	                                    <th >${message("admin.order.productQuantity")}<\/th>
	                                    <th >${message("admin.order.shippedQuantity")}<\/th>
	                                    <th >${message("admin.order.shippingQuantity")}<\/th>
	                                <\/tr>
	                                [#list order.orderItems as orderItem]
	                                    <tr>
	                                        <td>
	                                            <input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
	                                        ${orderItem.sn}
	                                        <\/td>
	                                        <td>
	                                        ${abbreviate(orderItem.name, 50, "...")?html?js_string}[#if orderItem.specifications?has_content][${orderItem.specifications?join(", ")?html?js_string}][/#if]
	                                        <\/td>
	                                        <td>${message(orderItem.isDelivery?string("admin.common.true", "admin.common.false"))}<\/td>
	                                        <td>${(orderItem.product.stock)!"-"}<\/td>
	                                        <td>${orderItem.quantity}<\/td>
	                                        <td>${orderItem.shippedQuantity}<\/td>
	                                        <td>
	                                            [#if orderItem.product?? && orderItem.product.stock < orderItem.shippableQuantity]
	                                                [#assign shippingQuantity = orderItem.product.stock /]
	                                            [#else]
	                                                [#assign shippingQuantity = orderItem.shippableQuantity /]
	                                            [/#if]
	                                            <input type="number" name="shippingItems[${orderItem_index}].quantity" class="input-text radius tdInput1 shippingItemsQuantity" value="${shippingQuantity}" [#if shippingQuantity <= 0] disabled="disabled"[/#if] max="${shippingQuantity}" data-is-delivery="${orderItem.isDelivery?string("true", "false")}" \/>
	                                        <\/td>
	                                    <\/tr>
	                                [/#list]
	                            <\/table>
	                        <\/div>
                            <button type="button" id="addProductImage" class="op_button add_B">添加附件<\/button>
                            <div class="table_box">
	                            <table id="productImageTable" class="table table-border table-hover table_width boo">
	                                <tr class="text-l">
	                                    <th >${message("ProductImage.file")}<\/th>
	                                    <th >${message("ProductImage.title")}<\/th>
	                                    <th >${message("admin.common.order")}<\/th>
	                                    <th >${message("admin.common.action")}<\/th>
	                                <\/tr>
	
	                            <\/table>
	                        <\/div>
                        <\/form>'
                        [/@compress],
			onShow:function(){
                var $productImageTable = $("#productImageTable");
                var $addProductImage = $("#addProductImage");
				var productImageIndex = 0;
                $addProductImage.click(function(){
                    $productImageTable.append([@compress single_line = true]
                    		'<tr>
		                <td>
		                    <input type="file" name="orderFiles['+ productImageIndex + '].file" class="productImageFile fileDate" \/>
		                <\/td>
		            	<td>
		            		<input type="text" name="orderFiles[' + productImageIndex + '].title" class="input-text radius" maxlength="200" style="width:200px" \/>
		                <\/td>
		            	<td>
		            		<input type="text" name="orderFiles[' + productImageIndex + '].order" class="input-text radius productImageOrder" maxlength="9" style="width: 80px;" \/>
		                <\/td>
		            	<td>
		            		<a href="javascript:void(0);" class="remove"><i class="operation_icon icon_del"><\/i><\/a>
		            	<\/td>
		            <\/tr>'
		            [/@compress]
		            );

					productImageIndex++;
				});

				/*删除*/
                $productImageTable.on("click", "a.remove", function() {
                    $(this).closest("tr").remove();
                });
				
                var $shippingForm = $("#shippingForm");
                var $shippingLogistics = $("#shippingLogistics");
                var $shippingItemsQuantity = $("#shippingForm input.shippingItemsQuantity");


                // $("#shippingForm input[name='areaId']").lSelect({
                //     url: "${base}/admin/common/area.jhtml",
                //     canSelect:false
                // });

                $.validator.addMethod('filesize', function(value, element, param) {
                    var size;
                    if($.browser.msie){
                        var myFSO = new ActiveXObject("Scripting.FileSystemObject");
                        var filepath = value;
                        var thefile = myFSO.getFile(filepath);
                        size = thefile.size;
                    } else {
                        size = element.files[0].size / 1024 / 1024;
                    }
                    return this.optional(element) || (size <= param);
                });

                $.validator.addClassRules({
                    shippingItemsQuantity: {
                        required: true,
                        digits: true
                    },
                    productImageFile: {
                        filesize:30,
                        extension: "jpg,jpeg,bmp,gif,png,pdf",
                        required: true
                    }
                });

                if ($.validator != null) {

                    $.extend($.validator.messages, {
                        filesize: '文件大小超过{0}M'
                    });
                }

                $shippingForm.validate({
                    rules: {
                        deliveryCorpId: "required",
                        freight: {
                            min: 0,
                            decimal: {
                                integer: 12,
                                fraction: ${setting.priceScale}
                            }
                        },
                        consignee: "required",
                        zipCode: {
                            required: true,
                            pattern: /^\d{6}$/
                        },
                        areaId: "required",
                        address: "required",
                        phone: {
                            required: true,
                            pattern: /^\d{3,4}-?\d{7,9}$/
                        }
                    }
                });

		        /*下拉框*/
		        $(".down_list").click(function () {
		            $(this).siblings(".downList_con").toggle();
		        });

		        /*$("*").click(function (event) {
		            if (!$(this).hasClass("down_list") && !$(this).hasClass("downList_con")) {
		                $(".downList_con").hide();
		            }
		            event.stopPropagation();
		        });*/

		        $(".downList_con").each(function () {
		            var curr = $(this).find("li.li_bag");
                    var firstText ;
                    var firstVal ;
                    if(curr.length == 0){
                         firstText = $(this).find("li:eq(0)").text();
                         firstVal = $(this).find("li:eq(0)").attr("val");
                        $(this).find("li:eq(0)").addClass("li_bag");
                    }else{
                        firstText = curr.text();
                        firstVal = curr.attr("val");

                    }

		            $(this).siblings(".down_list").val(firstText);
		            $(this).siblings(".downList_val").val(firstVal);
		        });

		        $(".downList_con li").click(function () {
		            $(this).parent().siblings(".down_list").attr("value", $(this).text());
		            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
		            $(this).parent().siblings(".downList_val").change();
		            if($("#deliveryCorp").val() != "" && $("#deliveryCorp").val() != '请选择...'){
		            	$("#deliveryCorpId-error").css("display","none");
		            }
		            $(this).addClass("li_bag").siblings().removeClass("li_bag");
					$(".downList_con").css("display","none");
		        });


			},
            onOk: function() {
                var total = 0;
                $("#shippingForm input.shippingItemsQuantity").each(function() {
                    var quantity = $(this).val();
                    if ($.isNumeric(quantity)) {
                        total += parseInt(quantity);
                    }
                });

                if (total <= 0) {
                    $.message("warn", "${message("admin.order.shippingQuantityPositive")}");
                } else {
                    $("#shippingForm").submit();
                }
                return false;
            }
		});
	});
    [/#if]


    [#if   order.status == "applyCancel"]
        // 用户申请取消
        $applyCancelButton.click(function() {
            var $this = $(this);
            $.dialog({
                type: "warn",
                height:190,
                content: "是否通过用户发起的订单取消申请?",
                ok: "${message("admin.common.true")}",
                cancel: "${message("admin.common.false")}",
                onOk: function() {
                    $passed.val("true");
                    $applyCancelForm.submit();
                    return false;
                },
                onCancel: function() {
                    $passed.val("false");
                    $applyCancelForm.submit();
                    return false;
                },
                onShow:function(){
                    $(".xxDialog").css("top","150px");
                }
            });
        });
    [/#if]

    $cancelButton.click(function() {
        var $this = $(this);
        $.dialog({
            type: "warn",
            height:190,
            content: "您确认取消该订单！",
            onOk: function() {
                $cancelForm.submit();
            },
            onShow:function(){
            	$(".xxDialog").css("top","150px");
            }
        });
    });

    $completeButton.click(function() {
        var $this = $(this);
        $.dialog({
            type: "warn",
            height:190,
            content: "${message("admin.order.completeConfirm")}",
            onOk: function() {
                $completeForm.submit();
            },
            onShow:function(){
            	$(".xxDialog").css("top","150px");
            }
        });
    });
    
    $returnsButton.click(function() {
        var $this = $(this);
        $.dialog({
            type: "warn",
            height:190,
            content: "确定要给当前订单退货退款嘛？",
            onOk: function() {
                $.dialog({
		            type: "warn",
		            height:190,
		            content: "确定退货会直接退还给用户：${order.amountPaid}的货款哦，确定？",
		            onOk: function() {
		                $returnsForm.submit();
		            },
		            onShow:function(){
		            	$(".xxDialog").css("top","150px");
		            }
		        });
            },
            onShow:function(){
            	$(".xxDialog").css("top","150px");
            }
        });
    });
    //打印
    $("#print").click(function() {
    	//window.location.href="../print/order.jhtml?id=${order.id}";
    	var href="../print/order.jhtml?id=${order.id}";
    	window.open(href);
    });
    


    [#if !order.hasExpired() && order.status == "pendingReview"]
    $("#reviewButton").click(function(){
        $.dialog({
            title:"审核",
            width:600,
            height:300,
            content:[@compress single_line = true]
			'<form id="reviewForm" class="form form-horizontal" action="review.jhtml" method="post">
			    <input type="hidden" name="token" value="${token}" \/>
                <input type="hidden" name="id" value="${order.id}" \/>
				<div class="pag_div1" style="margin-top:20px">
					<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">审核<\/label>
						<div class="formControls col-xs-8 col-sm-7">
							<div class="check-box">
		                         <input type="radio" name="passed" value="true" checked="checked" class="passedClass" \/><span style="margin-right:30px;">通过<\/span>
								<input type="radio" name="passed" value="false" class="passedClass" \/><span>不通过<\/span>
		                    <\/div>
						<\/div>
					<\/div>
					<div id="passedTrue">

					<\/div>
					<div id="passedFalse" style="display:none">
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">原因<\/label>
							<div class="formControls col-xs-8 col-sm-7">
			                    <textarea name="deniedReason" class="text_area1"><\/textarea>
							<\/div>
						<\/div>
					<\/div>
				<\/div>
			<\/form>'
			[/@compress],
			modal: true,
            onShow:function(){
                $(".passedClass").click(function(){

                    $("#passedTrue").hide();
                    $("#passedFalse").hide();

                    if($(this).val() == "true"){
                        $("#passedTrue").show();
                    }
                    if($(this).val() == "false"){
                        $("#passedFalse").show();
                    }
                });

                $.validator.addClassRules({
                    checkQuantityClass: {
                        required: true,
                        digits: true
                    }
                });

                $("#reviewForm").validate();

            },
            onOk: function() {
                var $reviewForm = $("#reviewForm");
                $reviewForm.submit();
                return false ;
            }
        });
    });
    [/#if]


    [#if order.status == "pendingShipment" && !isOverUpdate]
        $updateButton.click(function(){
            $("#editView").submit();
        });
    [/#if]
});

function printOrder(orderId , shippingId){
	var href='../print/orderShippingInfo.jhtml?id='+orderId+'&shippingId='+shippingId+'';
	window.open(href);
}

function cancelShipping(orderId, shippingId) {
    $.dialog({
        type: "warn",
        content: "是否作废发货？",
        ok: message("admin.dialog.ok"),
        cancel: message("admin.dialog.cancel"),
        height:190,
        onOk: function () {
            $("#cancelShippingId").val(shippingId);
            $("#cancelShippingForm").submit();
        },
        onShow:function(){
            $(".xxDialog").css("top","150px");
        }
    });
    return false;
}
$("#goHome").on("click",function(){
	var nav = window.top.$(".index_nav_one");
		nav.find("li li").removeClass('clickTo');
		nav.find("i").removeClass('click_border');
})
$("body").click(function(){
    window.top.$(".show_news").removeClass("show");
})
</script>

</body>
</html>
[/#escape]