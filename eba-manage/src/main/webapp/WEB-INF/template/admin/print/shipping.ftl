[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${message("admin.print.order")} - Powered By DreamForYou</title>
    <meta name="author" content="UTLZ Team" />
    <meta name="copyright" content="UTLZ" />
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <style type="text/css">
        .bar {
            height: 30px;
            line-height: 30px;
            border-bottom: 1px solid #d7e8f8;
            background-color: #eff7ff;
        }

        table {
            width: 100%;
            border: 1px solid #dde9f5;
        }

        table th {
            font-weight: bold;
            text-align: left;
        }

        table td, table th {
            line-height: 30px;
            padding: 0px 4px;
            font-size: 15pt;
            color: #000000;
            border: 1px solid #999;
        }

        .separated th, .separated td {
            border-top: 1px solid #000000;
            border-bottom: 1px solid #000000;
        }
        /*table.input1 th  {
            width: 150px;
            line-height: 28px;
            padding: 4px;
            font-weight: normal;
            text-align: center;
            white-space: nowrap;
            border: 1px solid #dde9f5;
            font-size: 11pt;
        }
        table.input1 td  {
            border: 1px solid #dde9f5;
            text-align: center;
            font-size: 11pt;
        }*/
    </style>
    <style type="text/css" media="print">
        .bar {
            display: none;
        }

    </style>
    <script type="text/javascript">
        $().ready(function() {

            var $print = $("#print");

            $print.click(function() {
                window.print();
                return false;
            });

        });
    </script>
</head>
<body>
<div class="bar">
    <a href="javascript:;" id="print" class="button">${message("admin.print.print")}</a>
</div>
<div class="content">
    <div style="text-align: center;font-size:xx-large;color: #222">配货单</div>
    <table class="input1">
        <tr>
            <td colspan="4" style="text-align: center;">
                ${order.member.need.supplier.name}
            </td>
            [#--<th>
                供应商:
            </th>
            <td width="360">
			${order.supplier.name}
            </td>
            <th>
                客户名称:
            </th>
            <td>
			${order.member.need.supplier.name}
            </td>--]
        </tr>

        <tr>
            <th>
                订单编号:
            </th>
            <td width="360">
			${order.sn}
            </td>
            <th>
                下单时间:
            </th>
            <td>
			${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
            </td>
        </tr>

        <tr>
            <th>
                收货点:
            </th>
            <td width="360">
			${order.member.need.name}
            </td>
            <th>
                收货点地址:
            </th>
            <td>
			${order.member.need.area.fullName} ${order.member.need.address}
            </td>
        </tr>


        <tr>
            <th>
                收货人:
            </th>
            <td width="360">
			${order.consignee}
            </td>
            <th>
                手机号:
            </th>
            <td>
			${order.phone}
            </td>
        </tr>


        <tr>
            <th>
                收货时间:
            </th>
            <td width="360">
			${order.reDate?string("yyyy-MM-dd")}
            </td>
            <th>
			${message("Shipping.deliveryCode")}:
            </th>
            <td>
            ${shipping.deliveryCode}<span class="silver">(此码为司机送货确认码，请勿向其他人透露)</span>
            </td>
        </tr>

    </table>


    <div style="text-align: center;font-size: xx-large;color: #222">商品信息</div>

    <table class="input1">

        <tr>
            <th>
			${message("admin.print.number")}
            </th>
            <th>
			${message("OrderItem.name")}
            </th>
            <th>
                规格
            </th>
            <th>
			${message("OrderItem.quantity")}
            </th>
        </tr>
		[#list shipping.shippingItems as sippingItem]
            <tr>
                <td>
				${sippingItem_index + 1}
                </td>
                <td>
				${abbreviate(sippingItem.name, 50, "...")}
                </td>
                <td>
					[#if sippingItem.specifications?has_content]
                        <span class="silver">[${sippingItem.specifications?join(", ")}]</span>
					[/#if]
                </td>
                <td>
				${sippingItem.quantity}
                </td>
            </tr>
		[/#list]

        <tr>
            <td colspan="4" style="text-align: right">
                共${shipping.getQuantity()}件商品
            </td>
        </tr>

        <tr>
            <td colspan="4" style="text-align: right;padding-right: 220px">
                收货人:
            </td>
        </tr>
    </table>
</div>
</body>
</html>
[/#escape]