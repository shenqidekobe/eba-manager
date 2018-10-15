[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>
    <style>
        body {
            background: #f9f9f9;
        }

        .pag_div {
            width: calc(100% - 30px);
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

        .xxDialog {
            top: 40px
        }

        .xxDialog .dialogBottom {
            width: 100%;
            position: absolute;
            bottom: 0;
            border: 0;
        }

        .dialogContent {
            height: calc(100% - 80px);
            overflow: auto;
        }
		.xxDialog .dialogBottom{border-top:1px solid #eee;}
        iframe {
            height: calc(100% - 5px);
            width: 100%;
        }
        .form-horizontal .form-label{width:150px;}
    </style>
</head>
<body class="bodyObj">
<div class="child_page"><!--内容外面的大框-->
    <div class="cus_nav">
        <ul>
        	<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        	<li><a href="list.jhtml">采购单 </a></li>
            <li>收货点代下单</li>
        </ul>
    </div>
    <div class="form_box" style="overflow: auto;">
        <form action="create.jhtml" id="inputForm" method="post" class="form form-horizontal">
            <div class="pag_div">
                <h3 class="form_title">收货信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        选择收货点
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius down_list" readonly placeholder="请选择" id="need"/>
                        <input type="text" class="downList_val" id="needId" name="need"/>
                        <ul class="downList_con need">
                            <li val="">${message("admin.common.choose")}</li>
                            [#list needs as need]
                                <li val="${need.id}">${need.name}</li>
                            [/#list]
                        </ul>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        选择供应商
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius down_list" readonly placeholder="请选择"
                               id="supplier"/>
                        <input type="text" class="downList_val" id="supplierId" name="supplier"/>
                        <ul class="downList_con supplier gongyingshang">
                        </ul>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        收货点地址
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" name="address"
                               id="address"/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        联系人
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" name="userName"
                               id="userName">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        联系电话
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" name="tel"
                               id="tel">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        收货时间
                    </label>
                    <input id="minReDate" type="hidden" value="">

                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text"  id="reDate" name="reDate" class=" text input-text radius reDate"
                               onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd',readOnly:true , minDate: '#F{$dp.$D(\'minReDate\')}'});"/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        订单备注
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <textarea class='text_area' name="memo" rows="" cols=""></textarea>
                    </div>
                </div>
                <div class="add_list" style="width:100%;height:40px">
                    <h3 class="form_title">商品信息</h3>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                    	<button type="button" id="addPro" class="op_right op_button add_B">添加商品</button>
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        
                    </div>
                </div>
            </div>
            <div class="table_box">
            	<table id="productTable" class="table table-border table-hover table_width boo">
	                <tr class="text-l">
	                    <th width="25%">${message("Goods.productCategory")}</th>
	                    <th width="20%">${message("Goods.name")}</th>
	                    <th width="20%">规格</th>
	                    <th width="20%">商品数量</th>
	                    <th width="15%">操作</th>
	                </tr>
	            </table>
            </div>
            <div class="footer_submit">
                <input class="btn radius confir_S" type="submit" value="确定">
                <input class="btn radius cancel_B" type="button" value="取消" onclick="history.back();return false;">
            </div>
        </form>
    </div>
</div>


<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
<script type="text/javascript">

    var products = {};
    var tempProducts = {};

    //子页面调用 增加关联商品
    /**
     *
     * @param obj
     */
    function addProduct(product) {

        var proId = product.productId;
        if (proId in products) {
            return false;
        }
        products[proId] = product;
        tempProducts[proId] = product;
    }
    //删除关联商品
    function delProduct(product) {
        var proId = product.productId;
        delete products[proId];
        delete tempProducts[proId];
    }

    //删除关联商品
    function delProductChild(now, productId) {
        delete products[productId];
        $(now).closest("tr").remove();

    }

    function getCacheFromParent() {
        return products;
    }


    function cleanCache() {
        products = {};
        tempProducts = {};
        $("#productTable tr").slice(1).remove();
    }

    $(function () {
        $inputForm = $("#inputForm") ;
        var $supplier = $("#supplierId");
    	var $need = $("#needId");
    	var exists = true;
    	var message=null;
        [@flash_message /]
        var dealCache = function () {
            $.each(tempProducts , function(key , product){
                delete products[key];
            });
        };

        var $productTable = $("#productTable");

        /*表单验证*/
        $inputForm.validate({
            rules: {
                need: {
                    required: true
                },
                supplier: {
                    required: true
                },
                tel: {
                    required: true
                },
                userName: {
                    required: true
                },
                address: {
                    required: true
                },
                reDate: {
                    required: true
                }
            },
            submitHandler: function (form) {
            	if(!exists) {
            		$.message("warn", message);
            		return false;
            	}
                if ($.isEmptyObject(products)) {
                    $.message("warn", "请选择商品");
                    return false;
                }
                
                form.submit();
            }
        });

        $.validator.addClassRules({
            quantityClass:{
                required : true ,
                digits : true
            }
        });

        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);


        /*下拉框*/
        $(".down_list").click(function () {
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list") && !$(this).hasClass("downList_con")) {
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });

        $(".downList_con").each(function () {
            $(this).find("li:eq(0)").addClass("li_bag");
            var firstText = $(this).find("li:eq(0)").text();
            var firstVal = $(this).find("li:eq(0)").attr("val");
            $(this).siblings(".down_list").val(firstText);
            $(this).siblings(".downList_val").val(firstVal);
        });

        $(".downList_con li").click(function () {
            $(this).parent().siblings(".down_list").attr("value", $(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");

        });

        $(".downList_con").delegate('li', "click", function () {
            $("#supplier").attr("value", $(this).text());
            if($("#supplier").val() != ""){
            	$("#supplierId-error").css("display","none");
            }
            $("#supplierId").val($(this).attr("val"));
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
            $(this).parent().css("display", "none")
        });

        var needs = {};
        [#if needs?has_content]
            [#list needs as need]
                needs[${need.id}] = {
                    id: "${need.id}",
                    name: "${need.name}",
                    tel: "${need.tel}",
                    userName: "${need.userName}",
                    address: "${need.area.fullName} ${need.address}"
                };
            [/#list]
        [/#if]

        $(".downList_val").change(function () {
            var index = $(this).val();
            var need = needs[index];
            $(".supplier").html("");
            $("#supplier").val("");
            $("#address").val("");
            $("#userName").val("");
            $("#tel").val("");

            if ("" != index) {
                $("#address").val(need.address);
                $("#userName").val(need.userName);
                $("#tel").val(need.tel);

                $.ajax({
                    type: "GET",
                    url: "getSuppliers.jhtml",
                    data: {"needId": index},
                    dataType: "json",
                    beforeSend: function () {

                    },
                    success: function (data) {
                        $.each(data, function (key, option) {
                            $(".supplier").append("<li val='" + key + "'>" + option.name + "</li>");
                        });

                        /* $("#supplier").val($(".supplier li:eq(0)").text());
                         $("#supplierId").val($(".supplier li:eq(0)").attr("val"));
                         $(".supplier li:eq(0)").addClass("li_bag").siblings().removeClass("li_bag");*/
                    },
                    error: function (data) {
                    }
                });
            }

            $inputForm.valid() ;

        });

        var productLen = 0 ;

        $("#addPro").click(function () {

            if (!$inputForm.valid()) {
                return false;
            }
            var supplierId = $("#supplierId").val();
            var needId = $("#needId").val()
            var contentPath = "../utils/proList.jhtml?supplierId=" + supplierId + "&needId=" + needId;

            $.dialog({
                title: "选择商品",
                [@compress single_line=true]
                    content: '<iframe id="sonFrame" frameborder="0" width="" height="" src="' + contentPath + '"><\/iframe>',
                [/@compress]
                width:$(".bodyObj").width()-20,
                height:$(".bodyObj").height()-40,
                ok: "${message("admin.dialog.ok")}",
                cancel: "${message("admin.dialog.cancel")}",
                onOk: function () {
                    $.each(tempProducts, function (key, product) {
                        $productTable.append([@compress single_line = true]
                                '<tr id="remove_' + product.productId + '"><td>' + product.goodsName + '<\/td><td>' + product.productCategory + '<\/td><td>' + product.specificationValues + '<\/td><td><input type="number" name="ownOrderItems[' + productLen + '].quantity" value="'+product.minOrderQuantity+'" min="'+product.minOrderQuantity+'" class="quantityClass input-text radius rdInput"/ ><\/td><td><a href="javascript:void(0);" onclick="javascript:delProductChild(this , ' + product.productId + ');" ><i class="operation_icon icon_del"></i><\/a><\/td><input type="hidden" name="ownOrderItems[' + productLen + '].productId" value="' + product.productId + '"></tr>'
                        [/@compress]);
                        productLen++;
                    });
                    tempProducts = {};

                },
                onClose: function () {
                    dealCache();
                },
                onCancel: function () {
                    dealCache();
                },
                onShow: function () {
                    //$(".dialogContent").height("300px");
                    //$(".dialogContent").css("overflow" , "auto");
                }
            });
        });

        $(".gongyingshang").delegate("li","click",function(){
        	if($supplier.val() == "" && $need.val() == "") {
        		return false;
        	}
        	$.ajax({
                type: "GET",
                url: "conditionVerif.jhtml",
                data: {"supplier": $supplier.val(), "need": $need.val()},
                dataType: "json",
                success: function (data) {
                	$("#minReDate").val(data.minDate);
					if(data.exists == false) {
						$.message("warn", data.msg);
						message = data.msg;
						exists = false;
					}
                },
                error: function (data) {
                }
            });
        });
       
        
        $("#goHome").on("click",function(){
			var nav = window.top.$(".index_nav_one");
    			nav.find("li li").removeClass('clickTo');
				nav.find("i").removeClass('click_border');
		})
        
    });


</script>

</body>
</html>
[/#escape]

