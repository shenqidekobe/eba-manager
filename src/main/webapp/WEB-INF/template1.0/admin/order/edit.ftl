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
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/element-ui/lib/theme-default/index.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />

	<style>
        body {
            background: #f9f9f9;
        }
        table th {
            border-top: 1px solid #f0f0f0;
        }
        input:disabled{
        	color:#c1cde1;
        }
        input:disabled:hover{
        	background:#fff;
        	color:#c1cde1;
        }
        .table_box .tabBorder th{background:#f9f9f9;}
        .table_box .tabBorder td,.table_box .tabBorder th{
        	border:1px solid #f0f0f0;
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
        .form_box{
            overflow: auto;
            overflow-x: hidden;
        }
        iframe{height:calc(100% - 5px);width:100%;}
        .trColor{
            background-color:#f9f9f9;
        }
        .table-hover tbody tr.trColor:hover td{background-color:#fff;color:#999;}
        .table-hover tbody tr.trColor td{background-color:#fff;color:#999;}
	</style>
<title>查看订单</title>
</head>
<body class="bodyObj">
<div class="child_page"><!--内容外面的大框-->
		<div class="cus_nav">
			<ul>
				<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
				<li><a href="list.jhtml">订货单</a></li>
                <li><a href="view.jhtml?id=${order.id}">查看订单</a></li>
				<li>修改订单</li>
			</ul>
		</div>
		<div class="form_box" id="app">
				<div id="tab-system">
					<div >
						<div class="table_box" style="margin-top:20px;">
						<form id="updateItemForm" class="form form-horizontal" action="updateItems.jhtml" method="post">
                            <input type="hidden" name="token" value="${token}" />
                            <input type="hidden" name="id" value="${order.id}" />
                            <div class="pag_div1">
                                <div class="row cl">
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <div class="check-box">
                                        </div>
                                     </div>
                                </div>
                                <div id="passedTrue">
                                    <div class="row cl">
                                        <label class="form-label col-xs-4 col-sm-3">收货时间</label>
                                        <div class="formControls col-xs-8 col-sm-9">
                                            <input type="text" readonly="readonly" id="reDate" name="reDate" class="reDate text input-text radius" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd',readOnly:true});" value="${order.reDate}" />
                                        </div>
                                    </div>
                                    <div class="row cl">
                                        <label class="form-label col-xs-4 col-sm-3">商品信息</label>
                                        <div class="formControls col-xs-8 col-sm-10">
                                            <table class="table table-border table-hover table_width" id="table">
                                                <tr class="text-l">
                                                    <th width="35%"><div class="th_div">${message("OrderItem.sn")}</div></th>
                                                    <th width="25%"><div class="th_div">${message("OrderItem.name")}</div></th>
                                                    <th width="20%"><div class="th_div">${message("OrderItem.quantity")}</div></th>
                                                    <th width="20%"><div class="th_div">确认数量</div></th>
                                                </tr>
                                            [#list order.orderItems as orderItem]
                                                <tr class="text-l" name="dataTr">
                                                    <td>
                                                        <button name="recovery" type="button" onclick="recoverys(this);"  class="restore_B">复原</button><button name="less" class="numLess" type="button" onclick="deleteTr(this);" [#if order.orderItems.size() lte 1] style="display:none;width: 20px;" [/#if] style="width: 20px"></button><button name="plus" class="numPlus" type="button" onclick="addTr(this);" style="width: 20px"></button>
                                                        ${orderItem.sn}
                                                    </td>
                                                    <td>
                                                        ${abbreviate(orderItem.name, 50, "...")}
                                                        [#if orderItem.specifications?has_content]
                                                            [${orderItem.specifications?join(", ")}]
                                                        [/#if]
                                                    </td>
                                                    <td>${orderItem.quantity}</td>
                                                    <td><input type="number" min="0" class="input-text radius tdInput1 shippingItemsQuantity" name="orderItems[${orderItem_index}].checkQuantity" style="width:50px" value="${orderItem.quantity}"  id="quantity" oninput="checkUpdateQuantity(${orderItem.product.id},this);" onpropertychange="checkUpdateQuantity(${orderItem.product.id},this);" />
                                                    <input type="hidden" id="productId" value="${orderItem.product.id}" />
                                                    <input type="hidden" name="orderItems[${orderItem_index}].id" value="${orderItem.id}" />
                                                   <input type="hidden" id="status" name="orderItems[${orderItem_index}].status" value="edit" />
                                                    </td>
                                                </tr>
                                            [/#list]

                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>	
						</div>
					</div>
				</div>
				<div class="footer_submit">
					<input class="btn radius confir_S" type="button" id="submits" onclick="submits();"  value="确认" >
					<input class="btn radius cancel_B" type="button" value="返回" onclick="history.back();return false;">
				</div>
		</div>
	</div>
    <div id="chooseDiv" style="display:none;">
        <div class="choose_div" >
            <input type="text" class="chooseGoods input-text radius" placeholder="请输入商品名称/编号" onfocus="goodsFocus(this);" oninput="search(this);" onpropertychange="search(this);" />
            <ul class="goods_ul">
                <!--<li class="noClick">200123123西瓜</li>-->
                [#list products as product]
                    <li class="canChoose" data-sn="${product.sn }" data-name="${product.goods.name }[#if product.specifications?has_content]
                                                            [${product.specifications?join(", ")}]
                                                        [/#if]" data-productid="${product.id }" data-price="${product.supplyPrice }" onclick="addTrData(this);">${product.sn }  ${abbreviate(product.goods.name, 50, "...")}
                        [#list product.specificationValues as specificationValue]
                            ${specificationValue.value}&nbsp;
                        [/#list]
                    </li>
                [/#list]
            </ul>
        </div>
    </div>
    <!--_footer 作为公共模版分离出去-->
    <script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        var dtId='${order.orderItems.size()}';
        var hasSupplierSupplier='${hasSupplierSupplier}'
        var needProducts = {};
        var needProductsLen = 0;
        //子页面调用 增加关联商品
        /**
         *
         * @param obj
         */
        function addNeedProduct(productId , products){
            needProducts[productId] = products;
            needProductsLen++ ;
        }
        //删除关联商品
        function delNeedProduct(productId){
            delete needProducts[productId];
            needProductsLen--;
        }

        function getCacheFromChild(){
            return needProducts ;
        }

        function updateOrderQuantity(productId , quantity){
            needProducts[productId].quantity = quantity ;
        }

        //多项添加商品用到的数组
        var needAddProducts = {};
        function addNeedAddProduct(productId , products){
            needAddProducts[productId] = products;
        }
        //删除关联商品
        function delNeedAddProduct(productId){
            delete needAddProducts[productId];
        }

        function getNeedAddProducts(){
            return needAddProducts ;
        }

        function updateOrderAddQuantity(productId , quantity){
            needAddProducts[productId].quantity = quantity ;
        }


        //把初始商品放入数组
        $("tr[name=dataTr]").each(function(){
           var productId=$(this).find("#productId").val();
           var product={};
           product.productId=productId;
           product.quantity=$(this).find("#quantity").val();
           addNeedProduct(productId,product);
        });

        function checkUpdateQuantity(productId,obj){
            updateOrderQuantity(productId,$(obj).val());
        }

        $(function(){
            var $updateItemForm = $("#updateItemForm");
            $.validator.addClassRules({
                shippingItemsQuantity: {
                    required: true,
                    digits: true
                }
            });
            $updateItemForm.validate({
                rules: {
                    reDate: "required",
                }
            });

            $updateItemForm.validate();

            /*获取页面的高度*/
            var formHeight = $(document.body).height() - 100;
            $(".form_box").css("height", formHeight);


        });

        function submits(){
            $("#updateItemForm").submit();
        }


        function addTr(obj){
            if (!hasSupplierSupplier) {
                $.message("error","供应关系已过期或已删除,不能添加新商品!");
                return;
            }
            var str='<tr class="text-l" name="dataTrNone">';
            str+=' <td><button name="recovery" type="button" onclick="recoverys(this);"  class="restore_B">复原</button><button name="less" class="numLess" type="button" onclick="deleteTr(this);" style="width: 20px;"></button><button name="plus" class="numPlus" type="button" style="width: 20px;" onclick="addTr(this);"></button>'+$("#chooseDiv").eq(0).html()+'</td>';
            str+='<td><a href="javascript:void(0);" onclick="popUp();">...</a></td>';
            str+='<td></td>';
            str+='<td></td>';
            str+='</tr>';
            $(obj).parent().parent().after(str);
            if (($("tr[name=dataTr]").length+$("tr[name=dataTrAdd]").length) > 1) {
                $("tr[name=dataTr] button[name=less]").show();
                $("tr[name=dataTrAdd] button[name=less]").show();
            }
            $("*").click(function (event) {
                if (!$(this).hasClass("chooseGoods")&&!$(this).hasClass("noClick")){
                    $("#table .goods_ul").hide();
                }
                event.stopPropagation();
            });
            //验证哪些商品已选
            goodExists();
        }

        function deleteTr(obj){
            var $tr=$(obj).parent().parent();
            if ($tr.attr("name")=="dataTrNone") {
                $tr.remove();
            }else{
                $tr.addClass("trColor");
                if ($tr.attr("name")=="dataTr") {
                    $tr.attr("name","dataTrDelete");
                    $tr.find("#status").val("delete");
                }else if($tr.attr("name")=="dataTrAdd"){
                    $tr.attr("name","dataTrAddDelete");
                    $tr.find("#status").val("");
                }
                $tr.find("button[name=recovery]").show();
                $tr.find("button[name=less]").hide();
                $tr.find("button[name=plus]").hide();
                $tr.find("input").attr("readonly",true);
            }
            if (($("tr[name=dataTr]").length+$("tr[name=dataTrAdd]").length) <= 1) {
                $("tr[name=dataTr] button[name=less]").hide();
                $("tr[name=dataTrAdd] button[name=less]").hide();
            }
        }

        function recoverys(obj){
            var $tr=$(obj).parent().parent();
            if ($tr.attr("name")=="dataTrDelete") {
                $tr.attr("name","dataTr");
                $tr.find("#status").val("edit");
            }else if($tr.attr("name")=="dataTrAddDelete"){
                $tr.attr("name","dataTrAdd");
                $tr.find("#status").val("add");
            }
            $tr.removeClass("trColor");
            $tr.find("button[name=recovery]").hide();
            $tr.find("button[name=less]").show();
            $tr.find("button[name=plus]").show();
            $tr.find("input").attr("readonly",false);
            if (($("tr[name=dataTr]").length+$("tr[name=dataTrAdd]").length) > 1) {
                $("tr[name=dataTr] button[name=less]").show();
                $("tr[name=dataTrAdd] button[name=less]").show();
            }
        }

        function popUp(){
            $.dialog({
                title:"选择商品",
                width:$(".bodyObj").width()-20,
                height:$(".bodyObj").height()-40,
                content:[@compress single_line = true]
                '<div class="selectList" style="width:100%;height:100%">
                        <iframe [#if order.supplyType == 'temporary'] src="getGoodList.jhtml?orderId=${order.id}" [#else] src="getGoodListByFormal.jhtml?orderId=${order.id}" [/#if] id="iframeList" name="iframeList"  frameborder="0" width="100%" height="" scrolling="no">
                    <\/div>'
                [/@compress],
                modal: true,
                onShow:function(){

                },
                onOk: function() {
                    $.each(needAddProducts,function(i,n){
                        addNeedProduct(i,n);
                        if($("tr[name=dataTrNone]").length > 0){
                            var $tr=$("tr[name=dataTrNone]").eq(0);
                            var htmlStr='<td><button name="recovery" type=  "button" onclick="recoverys(this);"  class="restore_B">复原</button><button name="less" class="numLess" type="button" onclick="deleteTr(this);" style="width: 20px;"></button><button name="plus" class="numPlus" type="button" style="width: 20px;" onclick="addTr(this);"></button>'+n.sn+'</td><td>'+n.productName+'</td><td>'+n.quantity+'</td><td><input type="number" min="0" class="input-text radius tdInput1 shippingItemsQuantity" name="orderItems['+dtId+'].checkQuantity" style="width:50px" value="'+n.quantity+'"  id="quantity" oninput="checkUpdateQuantity('+n.productId+',this);" onpropertychange="checkUpdateQuantity('+n.productId+',this);" /><input type="hidden" name="orderItems['+dtId+'].id" value="" /><input type="hidden" id="productId" name="orderItems['+dtId+'].productId" value="'+n.productId+'" /><input type="hidden" name="orderItems['+dtId+'].supplyPrice" value="'+n.supplyPrice+'" /><input type="hidden" id="status" name="orderItems['+dtId+'].status" value="add" /></td>';
                            $tr.html(htmlStr);
                            $tr.attr("name","dataTrAdd");
                        }else{
                            var htmlStr='<tr class="text-l" name="dataTrAdd"><td><button name="recovery" type=  "button" onclick="recoverys(this);"  class="restore_B">复原</button><button name="less" class="numLess" type="button" onclick="deleteTr(this);" style="width: 20px;"></button><button name="plus" class="numPlus" type="button" style="width: 20px;" onclick="addTr(this);"></button>'+n.sn+'</td><td>'+n.productName+'</td><td>'+n.quantity+'</td><td><input type="number" min="0" class="input-text radius tdInput1 shippingItemsQuantity" name="orderItems['+dtId+'].checkQuantity" style="width:50px" value="'+n.quantity+'"  id="quantity" oninput="checkUpdateQuantity('+n.productId+',this);" onpropertychange="checkUpdateQuantity('+n.productId+',this);" /><input type="hidden" name="orderItems['+dtId+'].id" value="" /><input type="hidden" id="productId" name="orderItems['+dtId+'].productId" value="'+n.productId+'" /><input type="hidden" name="orderItems['+dtId+'].supplyPrice" value="'+n.supplyPrice+'" /><input type="hidden" id="status" name="orderItems['+dtId+'].status" value="add" /></td></tr>';
                            $("#table").append(htmlStr);
                        }
                        dtId++;
                    });
                    if (($("tr[name=dataTr]").length+$("tr[name=dataTrAdd]").length) > 1) {
                        $("tr[name=dataTr] button[name=less]").show();
                        $("tr[name=dataTrAdd] button[name=less]").show();
                    }
                    goodExists();
                },
                onClose:function(){
                    needAddProducts={};
                }
            });
        }

        function goodsFocus(obj){
            $("#table .goods_ul").hide();
            $(obj).siblings(".goods_ul").show();

            var divScroll = $(".form_box").scrollTop();
            var divHeight = $(".form_box").height();
            var offSetTop = obj.closest("tr").offsetTop+100;
            var ulHeight = $(obj).siblings(".goods_ul").height()+2;
            if(divHeight-(offSetTop-divScroll)<250){
                $(obj).siblings(".goods_ul").css("top",-ulHeight);
            }else{
                $(obj).siblings(".goods_ul").css("top",36);
            }

        }

        //已有的商品不能选择，置灰
        function goodExists(){
            $.each($("#table .goods_ul li"),function(){
                var productId = $(this).data("productid");
                if (needProducts[productId] != undefined) {
                    $(this).removeClass("canChoose");
                    $(this).addClass("noClick");
                }else{
                    $(this).removeClass("noClick");
                    $(this).addClass("canChoose");
                }
            });
        }
        //输入框搜索商品
        function search(obj){
            var searchText=$(obj).val();
            $.each($(obj).parent().find("li"),function(){
                var liText=$(this).html();
                if (liText.toLowerCase().indexOf(searchText.toLowerCase()) < 0) {
                    $(this).hide();
                }else{
                    $(this).show();
                }
            });
        }

        function addTrData(obj){
            if($(obj).hasClass("canChoose")){
                var sn=$(obj).data("sn");
                var name=$(obj).data("name");
                var productId=$(obj).data("productid");
                var supplyPrice=$(obj).data("price");
                var htmlStr='<td><button name="recovery" type="button" onclick="recoverys(this);"  class="restore_B">复原</button><button name="less" class="numLess" type="button" onclick="deleteTr(this);" style="width: 20px;"></button><button name="plus" class="numPlus" type="button" style="width: 20px;" onclick="addTr(this);"></button>'+sn+'</td><td>'+name+'</td><td>1</td><td><input type="number" min="0" class="input-text radius tdInput1 shippingItemsQuantity" name="orderItems['+dtId+'].checkQuantity" style="width:50px" value="1"  id="quantity" oninput="checkUpdateQuantity('+productId+',this);" onpropertychange="checkUpdateQuantity('+productId+',this);" /><input type="hidden" name="orderItems['+dtId+'].id" value="" /><input type="hidden" id="productId" name="orderItems['+dtId+'].productId" value="'+productId+'" /><input type="hidden" name="orderItems['+dtId+'].supplyPrice" value="'+supplyPrice+'" /><input type="hidden" id="status" name="orderItems['+dtId+'].status" value="add" /></td>';
                var $tr=$(obj).parent().parent().parent().parent()
                $tr.html(htmlStr);
                $tr.attr("name","dataTrAdd");
                var product={};
                product.productId=productId;
                product.quantity=1;
                product.supplyPrice=supplyPrice;
                addNeedProduct(productId,product);
                dtId++;
                goodExists();
                if (($("tr[name=dataTr]").length+$("tr[name=dataTrAdd]").length) > 1) {
                    $("tr[name=dataTr] button[name=less]").show();
                    $("tr[name=dataTrAdd] button[name=less]").show();
                }
            }
        }
        $("body").click(function(){
            window.top.$(".show_news").removeClass("show");
        })
</script>

</body>
</html>
[/#escape]