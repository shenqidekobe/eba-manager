[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>发货</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=7; IE=EDGE"> -->
    <script src="${base}/resources/mobile/js/jquery.min.js"></script>
    <script src="${base}/resources/mobile/js/adaptive.js"></script>
    <link rel="stylesheet" href="${base}/resources/mobile/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/order.css" />
    <link rel="stylesheet" href="${base}/resources/mobile/css/mobileSelect.css" />
    <style>
        .take-phone,.logistics-cmp,.order-count{position:relative}
        label.error{position:absolute;right:0;color:red;line-height:0.5rem;font-size:0.2rem;}
        .logistics-cmp label.error{right:0.5rem;line-height:0.4rem;}
        .order-count label.error{right:0.5rem;line-height:1rem;}
    </style>
</head>
<body>
    <!--这是发货选择收货点-->
    <form id="shippingForm" action="shipping.jhtml" method="post" enctype="multipart\/form-data">
        <input type="hidden" name="orderId" id="orderId" value="" />
        <input type="hidden" id="areaId" name="areaId" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}"/>
        <input type="hidden" id="address" name="address" value="[#noescape]${order.address?html?js_string}[/#noescape]"\/>
        <input type="hidden" name="consignee" id="consignee" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" />
        <input type="hidden" name="phone" value="[#noescape]${order.phone?html?js_string}[/#noescape]" />
        <input type="hidden" name="token" value="${token}" />
        <input type="hidden" placeholder="" name="zipCode" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" \/>
    <div class="send-or">
        <div class="info-content">
            <div class="take-info">收货信息</div>
            <div class="take-na">
                <div class="take-name">收货人</div>
                <div class="take-content name">
                    [#noescape]${order.consignee?html?js_string}[/#noescape]
                    [#noescape]${order.phone?html?js_string}[/#noescape]
                </div>

            </div>
            <div class="take-phone">
                <div class="take-name">收货地址</div>
                <div class="take-content name">
                    ${order.areaName} ${order.address}
                </div>

            </div>
        </div>
        <div class="logistics">
            <div class="name">物流信息</div>
            <div class="info-logistics">
                <div class="logistics-cmp">
                    <div class="cmp-name">物流公司</div>
                    <div class="wuliuInfo" style="float:left;">
                        [#list deliveryCorps as deliveryCorp]
                        [#noescape]
                        <input type="hidden" ids="${deliveryCorp.id}" value="${deliveryCorp.name?html?js_string}" />
                        [/#noescape]
                        [/#list]
                    </div>
                    <input type="text" unselectable="on" onfocus="this.blur()" readonly="readonly" placeholder="请选择物流 必选" class="cmp-select" id="logisticsSelect" value=""  />
                    <input type="hidden" id="deliveryCorpId" name="deliveryCorpId" />
                    <span class="line-btm"></span>
                </div>

                <div class="logistics-code">
                    <div class="code-name">物流单号</div>
                    <input type="text" placeholder="请输入物流单号" name="trackingNo" class="code-select" />
                </div>
            </div>
        </div>
        <div class="or-info">
            <div class="title">商品信息</div>
            [#list order.orderItems as orderItem]
            <input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" />
                <div class="good-it">
                    <div class="good-name">
                        <div class="name">${abbreviate(orderItem.name, 50, "...")?html?js_string}</div>
                        [#if orderItem.specifications?has_content]
                            <div class="count">${orderItem.specifications?join(", ")?html?js_string}</div>
                        [/#if]
                    </div>
                    <div class="order-count">
                        <div class="count-name">发货数</div>
                        <div class="count-ipm">
                            [#if orderItem.product?? && orderItem.product.stock < orderItem.shippableQuantity]
                                [#assign shippingQuantity = orderItem.product.stock /]
                            [#else]
                                [#assign shippingQuantity = orderItem.shippableQuantity /]
                            [/#if]
                            <input class="count-txt shippingItemsQuantity" type="number" name="shippingItems[${orderItem_index}].quantity" value="${shippingQuantity}" max="${shippingQuantity}" data-is-delivery="${orderItem.isDelivery?string('true', 'false')}" [#if shippingQuantity <= 0] readonly="readonly" style="color:#999;"[/#if] placeholder="请输入订货数量" />
                        </div>
                    </div>
                </div>
            [/#list]
        </div>

        <div class="confirm">发 货</div>
    </div>
    </form>

    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/mobile/js/mobileSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>


    <script type="text/javascript">

        $(function(){

            var orderId = GetQueryString('id');
            $("#orderId").val(orderId);

            /*获取物流信息*/
            var inputs = $(".wuliuInfo").find("input");
            var $shippingForm = $("#shippingForm");

            var dada=[];
            for(var i=0; i<inputs.length; i++){
                dada.push({"id":inputs.eq(i).attr('ids'),"value":inputs.eq(i).val()});
            }


            var mobileSelect1 = new MobileSelect({
                trigger: '#logisticsSelect',
                title: '物流选择',
                wheels: [
                    {data:dada}
                ],
                callback:function(indexArr, data){
                    console.log(data);
                    $('#deliveryCorpId').val(data[0].id);
                    $('.logistics-cmp label.error').css("display",'none');
                }
            });


            $shippingForm.validate({
                rules: {
                    consignee: "required",
                    phone: {
                        required: true,
                        pattern: /^\d{3,4}-?\d{7,9}$/
                    },
                    deliveryCorpId: "required"
                }
            });


            $.validator.addClassRules({
                shippingItemsQuantity: {
                    required: true,
                    digits: true
                }
            });


            $('.confirm').on("click",function(){
                var total = 0;
                $("#shippingForm input.shippingItemsQuantity").each(function() {
                    var quantity = $(this).val();
                    if ($.isNumeric(quantity)) {
                        total += parseInt(quantity);
                    }
                });

                if (total <= 0) {
                    errorInfoFun('发货总数必须大于0');
                } else {
                    $("#shippingForm").submit();
                }


            })




        });


    </script>
</body>

</html>
[/#escape]