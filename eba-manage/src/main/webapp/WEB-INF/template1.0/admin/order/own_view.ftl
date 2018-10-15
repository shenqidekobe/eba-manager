[#assign shiro = JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
[#escape x as x?html]
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>

    <style>
        body {
            background: #f9f9f9;
        }

        .pag_div {
            width: 45%;
            float: left;
        }

        .tabBar {
            background: #f9f9f9;
            border: 0;
        }

        .tabBar span {
            padding: 5px 24px;
            background: #f9f9f9;
            color: #999;
        }

        .tabBar span.current {
            background: #fff;
            color: #333;
        }

        .beizhu {
            height: 80px;
        }

        #tab-system {
            position: relative;
        }

        .opera_butt {
            position: absolute;
            top: 0;
            right: 0;
            margin-top: 5px;
        }
        
        .xxDialog {
            top: 40px
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
        
        
        .xxDialog .dialogBottom{height:36px;}

        table th {
            border-top: 1px solid #f0f0f0;
        }
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
			background: #f2774b;
		}
		
		.webuploader-pick-disable {
			opacity: 0.6;
			pointer-events:none;
		}
        .tabCon,#tab-system{overflow: auto;}
        .fujianList{color:#333;}
        .fujianList .fj_attach i{margin-left:10px;}
        .fujianList .fj_attach strong{margin-left:10px; font-weight: 100;}
        .fujianList ul{overflow: hidden;}
        .fujianList ul li{float:left;margin-right:20px;line-height:34px;}
        .fujianList ul li a{color:#333;}
        .fujianList ul li a:hover{text-decoration: underline;}


        .table_info th{background:#f9f9f9;border:1px solid #f0f0f0;}
        .table_info td{border:1px solid #f0f0f0; padding:0;}
        .table_info .td_list{width:100%;overflow: hidden;}
        .table_info .td_list p{float:left;border-right:1px solid #f0f0f0;height:36px;line-height:36px ;overflow:hidden;border-bottom:1px solid #f0f0f0;}
        .table_info td .td_list:nth-last-child(1) p{border-bottom:0;}
        .table_info td .td_list p:nth-last-child(1){border-right:0;}
        .table_info .td_list p.wb{width:104px;}
        .table_info .td_list p.wl{width:84px;}
        .table_info .td_list p.wy{width:144px;}
		.table_bb{padding:15px; text-align: right;}
        .table_box .tabBorder th{background:#f9f9f9;}
        .table_box .tabBorder td,.table_box .tabBorder th{
        	border:1px solid #f0f0f0;
        }
        .form_title{padding:10px 0;}
        
        
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
        
    </style>
    <title>查看订单</title>
</head>
<body>
<form id="cancelForm" action="applicationCancel.jhtml" method="post">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<form id="editView" [#if order.supplyType == 'temporary'] action="editView.jhtml" [#else] action="editViewByFormal.jhtml" [/#if] method="get">
    <input type="hidden" name="id" value="${order.id}"/>
</form>
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
            <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        	<li><a href="list.jhtml">采购单 </a></li>
            <li>查看</li>
        </ul>
    </div>
    <div class="form_box">
        <form class="form form-horizontal" id="form-article-add">
        	<input type="hidden" name="status" id="status" value="${order.status}"/>
            <div id="tab-system" class="HuiTab">
                <div class="tabBar cl">
                    <span>${message("admin.order.orderInfo")}</span>
                    <span>${message("admin.order.productInfo")}</span>
                    <span>${message("admin.order.shippingInfo")}</span>
                    <span>${message("admin.order.orderLog")}</span>
                </div>
                <div class="tabCon">
                	
                   <div class="opera_butt">
                   		[@shiro.hasPermission name = "admin:print:purchaseorder"]
                   			<input type="button" id="print" class="tab_button bgfff" value="打印" />
                   		[/@shiro.hasPermission]
                        [#if order.supplyType == 'formal']
                       		[@shiro.hasPermission name = "admin:ownOrder:updateItems"]
                           		<input type="button" id="updateButton" class="tab_button bgfff" value="修改"[#if order.status != "pendingReview" || isOverUpdate] style="display: none" disabled="disabled"[/#if] />
                       		[/@shiro.hasPermission]
                       		[@shiro.hasPermission name = "admin:ownOrder:applicationCancel"]
                       			<input type="button" id="cancelOrder" class="tab_button bgfff" value="申请取消"[#if !(order.status == "pendingReview" || order.status == "pendingShipment")] style="display: none" disabled="disabled"[/#if] />
                            [/@shiro.hasPermission]
                        [/#if]
                        <!--
                         <input type="button" class="tab_button bgfff" value="审核"/>
                        <input type="button" class="tab_button bgfff" value="发货"/>
                        <input type="button" class="tab_button bgfff" value="用户申请"/>
                        <input type="button" class="tab_button bgfff" value="取消"/>
                        <input type="button" class="tab_button bgfff" value="完成"/> -->
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
                                <span class="input_no_span">${currency(order.amountToB, true)}</span>
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
                        <!-- <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">收货点备注</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_text">[#noescape]${order.member.need.description}[/#noescape]</span>
                            </div>
                        </div>  -->
                    </div>
                    <div class="pag_div" style="width:96%;padding-top:0;">
                        	<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-3">订单备注</label>
                                <div class="formControls col-xs-8 col-sm-7" style="width:88%">
                                   	[@shiro.hasPermission name = "admin:ownOrder:addRemarks"]
                                    	<button type="button" id="addFujian" class="tab_button bgred" style="margin-top:3px;margin-left:0;">添加</button>
	                            	[/@shiro.hasPermission]
	                            	[#list order.orderRemarks as orderRemark]
                                    [#if order.type != 'formal']
                                        [#if orderRemark.msgType=="btob"]
                                       	<div class="fujianList">
    										<div class="fj_attach" style="overflow: hidden;">
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
                                            <div class="fj_attach" style="overflow: hidden;">
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
                                <th width="16%">
                                    <div class="th_div">商品编号</div>
                                </th>
                                <th width="15%">
                                    <div class="th_div">商品名称</div>
                                </th>
                                <th width="15%">
                                    <div class="th_div">商品规格</div>
                                </th>
                                <th width="8%">
                                    <div class="th_div">基本单位</div>
                                </th>
                                <th width="10%">
                                    <div class="th_div">商品价格</div>
                                </th>
                                <th width="10%">
                                    <div class="th_div">商品数量</div>
                                </th>
                                <th width="10%">
                                    <div class="th_div">发货数量</div>
                                </th>
                                <th width="10%">
                                    <div class="th_div">实收数量</div>
                                </th>
                                <th width="10%">
                                    <div class="th_div">小计</div>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                                [#list order.orderItems as orderItem]
                                <tr class="text-l">
                                    <td>
                                    ${orderItem.sn}
                                    </td>
                                    <td>
                                        [#if orderItem.product??]
                                               ${abbreviate(orderItem.name, 50, "...")}
                                        [#else]
                                            <span title="${orderItem.name}">${abbreviate(orderItem.name, 50, "...")}</span>
                                        [/#if]
                                        
                                    </td>
                                    <td>
                                    	[#if orderItem.specifications?has_content]
                                            <span class="silver">${orderItem.specifications?join(", ")}</span>
                                        [/#if]
                                    </td>
                                    <td>
                                    	[#if orderItem.product.goods.unit??]
                                    	${message("Goods.unit."+orderItem.product.goods.unit)}
                                    	[/#if]
                                    </td>
                                    <td>
                                        [#if orderItem.type == "general" || orderItem.type == "distribution"]
                                        ${currency(orderItem.priceUnitB, true)}
                                        [#else]
                                            -
                                        [/#if]
                                    </td>
                                    <td>
                                    ${orderItem.quantity}
                                    </td>
                                    <td>
                                    ${orderItem.shippedQuantity}
                                    </td>
                                    <td>
                                    ${order.getRealProductQuantity(orderItem.product.id)}
                                    </td>
                                    <td>
                                        [#if orderItem.type == "general" || orderItem.type == "distribution"]
                                        ${currency(orderItem.priceB, true)}
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
	                                <th width="10px">序号</th>
	                                <th>操作人</th>
	                                <th width="30px">类型</th>
	                                <th>记录时间</th>
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
                                    <td>
                                        ${orderitemLog.operatorName!"-"}
                                    </td>
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
                                                <p class="wl">${currency(orderItemInfo.beforePriceB, true)}</p>
                                                <p class="wl" style="width:82px;">
                                                ${currency(orderItemInfo.afterPriceB, true)}
                                                </p>
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
                    				[@shiro.hasPermission name = "admin:print:deliveryInfor"]
                    				<button type="button" id="deliveryInfor" class="tab_button bgred" onclick="javascript:printOrder(${order.id} , ${shipping.id});">打印</button>
                    				[/@shiro.hasPermission]
                    			</div>
                    			<table class="table table-border table-hover table_width boo">	
                            		<thead>
                            			<tr class="text-l">
                            				<th>${message("Shipping.sn")}</th>
                            				<th>${message("Shipping.deliveryCorp")}</th>
                            				<th>${message("Shipping.trackingNo")}</th>
                            				<th>${message("Shipping.consignee")}</th>
                            				<th>${message("admin.common.createDate")}</th>
                            				<th>状态</th>
                            			</tr>
                            		</thead>
                            		<tbody>
                            			<tr>
                            				<td>${shipping.sn}</td>
                            				<td>${shipping.deliveryCorp!"-"}</td>
                            				<td>${shipping.trackingNo!"-"}</td>
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
                            				<td>
                                                ${shippingItem.trueRealQuantity}
                                            </td>
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
                <input class="btn radius cancel_B" type="button" value="返回" onclick="history.back(); return false;">
            </div>
        </form>
    </div>
</div>
<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript">
    $(function () {
    	var $cancelOrder = $("#cancelOrder");
    	var $cancelForm = $("#cancelForm");
        var $updateButton = $("#updateButton");
        var $addFujian = $("#addFujian")
        [@flash_message /]
    	//打印
        $("#print").click(function() {
        	var href="../print/purchaseorder.jhtml?id=${order.id}";
        	window.open(href);
        });
    	
    	//申请取消订单
        $cancelOrder.click(function() {
        	if($("#status").val() == 'applyCancel') {
        		return false;
        	}
            var $this = $(this);
            $.dialog({
                type: "warn",
                height:190,
                content: "您确认申请取消该订单！",
                onOk: function() {
                    $cancelForm.submit();
                },
                onShow:function(){
                	$(".xxDialog").css("top","150px");
                }
            });
        });
    	

        /*通过js获取页面高度，来定义表单的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);
		
        $(".tabCon").css("height",formHeight - 40);


        $.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");


        [#if order.status == "pendingReview" && !isOverUpdate]
            $updateButton.click(function(){
                $("#editView").submit();
            });
        [/#if]
        
        
         /*添加备注*/
     	$addFujian.click(function(){
     		var fileUrl = {};
     		$.dialog({
     			title:"备注",
     			width:500,
     			height:360,
     			content:[@compress single_line = true]
     			'<form id="orderRemark" class="form form-horizontal" action="addRemarks.jhtml" enctype="multipart/form-data" method="post">
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
     					/* fileUrl.push({'url':data.url , "fileName":file.file.name , "suffix":file.file.ext , "size": file.file.size}); */
     					fileUrl[file.file.id] = {'url':data.url , "fileName":file.file.name , "suffix":file.file.ext , "size": file.file.size} ;
     					
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
        
		
		
		$("#goHome").on("click",function(){
			var nav = window.top.$(".index_nav_one");
    			nav.find("li li").removeClass('clickTo');
				nav.find("i").removeClass('click_border');
		})
    });
    function printOrder(orderId , shippingId){
    	var href='../print/deliveryInfor.jhtml?id='+orderId+'&shippingId='+shippingId+'';
    	window.open(href);
    }
    $("body").click(function(){
        window.top.$(".show_news").removeClass("show");
    })
</script>

</body>
</html>
[/#escape]