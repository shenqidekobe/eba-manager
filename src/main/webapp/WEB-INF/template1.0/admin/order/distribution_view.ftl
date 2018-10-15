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
	</style>
<title>查看订单</title>
</head>
<body class="bodyObj">
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
<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
				<li><a href="list.jhtml">分销单</a></li>
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
					</div>
					<div class="tabCon">
						<div class="opera_butt">
							[@shiro.hasPermission name = "admin:print:distributionOrder"]
								<input type="button" id="print" class="tab_button bgfff" value="打印" />
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:distributionOrder:review"]
                            	<input type="button" id="reviewButton" class="tab_button bgfff" value="${message("admin.order.review")}"[#if order.hasExpired() || order.status != "pendingReview"] style="display: none" disabled="disabled"[/#if] />
                            [/@shiro.hasPermission]
                           	[@shiro.hasPermission name = "admin:distributionOrder:applyCancel"]
                            	<input type="button" id="applyCancelButton" class="tab_button bgfff" value="用户申请"[#if order.hasExpired() || order.status != "applyCancel"] style="display: none" disabled="disabled"[/#if] />
                            [/@shiro.hasPermission]
                            [@shiro.hasPermission name = "admin:distributionOrder:cancel"]
                            	<input type="button" id="cancelButton" class="tab_button bgfff" value="取消"[#if order.status != "pendingReview"] style="display: none" disabled="disabled"[/#if] />
							[/@shiro.hasPermission]
						</div>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.sn")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.sn}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货点</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.need.name}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.consignee")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.consignee}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.amount")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${currency(order.amount, true)}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">下单人</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">
                                    ${order.getCreateOrderLog().operator}
                                </span>
                                </div>
                            </div>
                        </div>
                        <div class="pag_div">
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货点地址</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="adress_no">${order.areaName} ${order.address}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.phone")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.phone}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货时间</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${order.reDate?string("yyyy-MM-dd")}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">${message("Order.status")}</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_span">${message("Order.Status." + order.status)}</span>
                                </div>
                            </div>
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">收货点备注</label>
                                <div class="formControls col-xs-8 col-sm-7">
                                    <span class="input_no_text">
                                    [#noescape]${order.member.need.description}[/#noescape]
                                    </span>
                                </div>
                            </div> 
                        </div>
                        <div class="pag_div" style="width:96%;padding-top:0;">
                        	<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">订单备注</label>
                                <div class="formControls col-xs-8 col-sm-7" style="width:88%">
	                            	[#list order.orderRemarks as orderRemark]
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
	                                                ${currency(orderItem.priceUnit, true)}
	                                            [#else]
	                                                -
	                                            [/#if]</td>
	                                        <td>${orderItem.quantity}</td>
	                                        <td>${orderItem.shippedQuantity}</td>
	                                        <td>${order.getRealProductQuantity(orderItem.product.id)}</td>
	                                        <td>
	                                            [#if orderItem.type == "general" || orderItem.type == "distribution"]
	                                                ${currency(orderItem.price, true)}
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
						<div class="table_box table_info">
								<h3 class="form_title">商品记录</h3>
                        		<table class="table table-border table-hover table_width boo">	
                            		<thead>
                            			<tr class="text-c">
                            				<th>序号</th>
                            				<th>操作人</th>
                            				<th width="30px">类型</th>
                            				<th width="100px">记录时间</th>
                            				<th width="80px">商品编号</th>
                            				<th width="120px">商品名称</th>
                            				<th width="120px">商品规格</th>
                            				<th width="60px">基本单位</th>
                            				<th width="60px">原订货数量</th>
                            				<th width="60px">现订货数量</th>
                            				<th width="60px">原商品小计</th>
                            				<th width="60px">现商品小计</th>
                            			</tr>
                            		</thead>
                            		<tbody>
                                    [#list order.orderItemLogs?reverse as orderitemLog]
                                        <tr class="text-c">
                                            <td>${orderitemLog_index+1}</td>
                                            <td>${orderitemLog.operatorName}</td>
                                            <td>${message("admin.orderItemLog.operatorType."+orderitemLog.operatorType)}</td>
                                            <td>${orderitemLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                                            <td colspan="8">
                                                [#list orderitemLog.orderItemInfos as orderItemInfo]
                                                    <div class="td_list">
                                                        <p class="wb">${orderItemInfo.product.sn}</p>
                                                        <p class="wy">${orderItemInfo.product.goods.name}</p>
                                                        <p class="wy">[#list orderItemInfo.product.specificationValues as specificationValue]
                                                        ${specificationValue.value}&nbsp;
                                                        [/#list]</p>
                                                        <p class="wl">
                                                        	[#if orderItemInfo.product.goods.unit??]
		                                                	${message("Goods.unit."+orderItemInfo.product.goods.unit)}
		                                                	[/#if]
                                                        </p>
                                                        <p class="wl">${orderItemInfo.beforeQuantity}</p>
                                                        <p class="wl">${orderItemInfo.afterQuantity}</p>
                                                        <p class="wl" >${currency(orderItemInfo.beforePrice, true)}</p>
                                                        <p class="wl"style="width:82px;" >${currency(orderItemInfo.afterPrice, true)}</p>
                                                    </div>
                                                [/#list]
                                            </td>
                                        </tr>
                                    [/#list]
                            		</tbody>
                        		</table>
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
    var $reviewButton = $("#reviewButton");
    var $paymentButton = $("#paymentButton");
    var $refundsButton = $("#refundsButton");
    var $returnsButton = $("#returnsButton");
    var $receiveButton = $("#receiveButton");
    var $completeButton = $("#completeButton");
    var $failButton = $("#failButton");
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
                        $reviewButton.add($paymentButton).add($refundsButton).add($returnsButton).add($receiveButton).add($completeButton).add($failButton).prop("disabled", true);
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
	

	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

    [#if   order.status == "applyCancel"]
        // 用户申请取消
        $applyCancelButton.click(function() {
            var $this = $(this);
            $.dialog({
                type: "warn",
                height:190,
                content: "是否通过用户发起的订单取消申请！",
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

    //打印
    $("#print").click(function() {
    	//window.location.href="../print/order.jhtml?id=${order.id}";
    	var href="../print/distributionOrder.jhtml?id=${order.id}";
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