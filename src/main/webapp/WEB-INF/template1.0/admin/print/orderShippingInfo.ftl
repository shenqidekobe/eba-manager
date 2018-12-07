[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.print.order")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			
			.print_box{position:relative;height:100%;}
			.table_width td, .table_width th{padding:5px;}
			.con_table table th, .con_table table td{border:1px solid #222;}
		</style>
		 <style type="text/css" media="print">
        .bar {
            display: none;
        }

    </style>
	</head>
	<body >
		<div style="width:595px;height:25px;text-align:center;margin:0 auto;" class="bar">
			<button type="button" class="tab_button bgred" id="print">${message("admin.print.print")}</button>
		</div>
		<div class="print_box">
			<div class="con_header">
				<div class="con_h_top">
					<div class="order_title">
						<h2>发货单</h2>
					</div>
				</div>
				<ul class="orderInfo">
					<li>
						<label class="order_lab">公司名称:</label><span>
							[#if order.toSupplier == null]
								${order.supplier.name}
							[#elseif order.toSupplier.id == supplier.id]
								${order.toSupplier.name}
							[#elseif order.supplier.id == supplier.id]
								${order.supplier.name}
							[/#if]</span>
					</li>
					<li>
						<label class="order_lab">订单编号:</label><span>${order.sn}</span>
					</li>
					<li>
						<label class="order_lab">下单时间:</label><span>${order.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
					</li>
					<li>
						<label class="order_lab">收货点:</label><span>${order.need.name}</span>
					</li>
					<li>
						<label class="order_lab">收货地址:</label><span>${order.areaName} ${order.address}</span>
					</li>
					<li>
						<label class="order_lab">收货人:</label><span>${order.consignee}</span>
					</li>
					<li>
						<label class="order_lab">手机号:</label><span>${order.phone}</span>
					</li>
					<li>
						<label class="order_lab">收货时间:</label><span>${order.reDate?string("yyyy-MM-dd")}</span>
					</li>
					<li>
						<label class="order_lab">${message("Shipping.deliveryCode")}:</label><span style="font-weight:700;">${shipping.deliveryCode }<i>(此码为司机送货确认码，请勿向其他人透露)</i></span>
					</li>
					<li>
						<label class="order_lab">发货备注:</label><span>${shipping.memo }</span>
					</li>
					<li>
						<label class="order_lab">收货点备注:</label><span>${order.need.description}</span>
					</li>
				</ul>
			</div>
			[#if order.need.type == 'general']
			<div class="qurenCode">
				<img src="../print/getQRCode.jhtml?shippingId=${shipping.id}" alt="" />
				<div>使用微信扫描二维码做收货数量确认</div>
			</div>
			[/#if]
			<div class="con_table">
				<div class="t_title">
					<h3>商品信息</h3>
				</div>
				<table class="table table-border table-hover table_width">
					<tr>
						<th>${message("admin.print.number")}</th>
						<th>${message("OrderItem.name")}</th>
						<th>商品规格</th>
						<th>基本单位</th>
						<th>${message("OrderItem.quantity")}</th>
					</tr>
					[#list shipping.shippingItems as shippingItem]
					<tr>
						<td>${shippingItem_index + 1}</td>
						<td>${abbreviate(shippingItem.name, 50, "...")}</td>
						<td>
							[#if shippingItem.specifications?has_content]
	                            <span class="silver">[${shippingItem.specifications?join(", ")}]</span>
							[/#if]
						</td>
						<td>
							[#if shippingItem.product.goods.unit??]
							${message("Goods.unit."+shippingItem.product.goods.unit)}
							[/#if]
						</td>
						<td>${shippingItem.quantity}</td>
					</tr>
					[/#list]
					
					<tr class="last_tr text-r">
						<td colspan="5">共<i>${shipping.quantity}</i>件商品</td>
					</tr>
				</table>
				
				<div class="gain_ren"><p>收货人:</p></div>
<!--				<div class="order_time"><p><span>2017/5/23</span><i>订单打印华奕优选</i></p></div>-->
			</div>

		</div>
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script src="${base}/resources/admin1.0/js/common.js"></script>
		<script type="text/javascript">
		$().ready(function() {
            var $print = $("#print");
            $print.click(function() {
                window.print();
                return false;
            });
        });
		</script>
	</body>
</html>
[/#escape]