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
            <li><a id="goHome"  href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            <li><a href="index.jhtml">代下单 </a></li>
            <li>直营门店代下单</li>
        </ul>
    </div>
    <div class="form_box" style="overflow: auto;">
        <form action="create.jhtml" id="inputForm" method="post" class="form form-horizontal">
            <div class="pag_div">
                <h3 class="form_title">收货信息</h3>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        选择直营门店
                    </label>
                    <input type="hidden" id="supplierId" name="supplier" value="${supplierId}">
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius down_list" readonly placeholder="请选择" id="need"/>
                        <input type="text" class="downList_val" id="needId" name="need"/>
                        <ul class="downList_con need" id="needs">
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
                        门店地址
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" readonly="readonly" class="input-text radius" placeholder="" name="allAddress"
                               id="allAddress"/>
                        <a href="javascript:void(0);"  style="text-decoration:none"><i id="areaEdit" class="operation_icon icon_bianji"></i></a>
                        <input type="hidden" name="areaIdT"
                       id="areaIdT"/>
                        <input type="hidden" name="addressT"
                       id="addressT"/>
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        收货人
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" placeholder="" name="userName"
                               id="userName">
                    </div>
                </div>
                <div class="row cl">
                    <label class="form-label col-xs-4 col-sm-3">
                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>
                        联系方式
                    </label>
                    <div class="formControls col-xs-8 col-sm-7">
                        <input type="text" class="input-text radius" placeholder="" name="tel"
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

                <div class="row cl" id="remark">
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
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
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
        // tempProducts[proId] = product;
    }
    //删除关联商品
    function delProduct(product) {
        var proId = product.productId;
        delete products[proId];
        // delete tempProducts[proId];
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
        // tempProducts = {};
        $("#productTable tr").slice(1).remove();
    }

    $(function () {
        $inputForm = $("#inputForm") ;
        var $need = $("#needId");
        var exists = true;
        var message=null;
        [@flash_message /]
        // var dealCache = function () {
        //     $.each(tempProducts , function(key , product){
        //         delete products[key];
        //     });
        // };

        var $productTable = $("#productTable");

        /*表单验证*/
        $inputForm.validate({
            rules: {
                need: {
                    required: true
                },
                tel: {
                    required: true
                },
                userName: {
                    required: true
                },
                allAddress: {
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
                $(form).find("input:submit").prop("disabled", true);
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
                    allAddress: "${need.area.fullName} ${need.address}",
                    areaIdT:"${need.area.id}",
                    addressT:"${need.address}",
                    receiverTel:"${need.receiverTel}",
                    supplyNeedType:[#if need.getSupplyNeedBySupply()??] "${need.getSupplyNeedBySupply().assignedModel}" [#else] null [/#if]
                };
            [/#list]
        [/#if]
        $(".downList_val").change(function () {
            var index = $(this).val();
            var need = needs[index];
            $("#allAddress").val("");
            $("#userName").val("");
            $("#tel").val("");
            $("#areaIdT").val("");
            $("#addressT").val("");
            $("tr[name=goodTr]").remove();

            if ("" != index) {
                $("#allAddress").val(need.allAddress);
                $("#userName").val(need.userName);
                $("#tel").val(need.receiverTel);
                $("#areaIdT").val(need.areaIdT);
                $("#addressT").val(need.addressT);
            }
            if (need.supplyNeedType != null) {
                if (need.supplyNeedType == "BRANCH") {
                    $("textarea[name=memo]").val("");
                    $("#remark").hide();
                }else{
                    $("#remark").show();
                }
            }else{
                $("#remark").show();
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
                    $("tr[name=goodTr]").remove();
                    $.each(products, function (key, product) {
                        $productTable.append([@compress single_line = true]
                                '<tr name="goodTr" id="remove_' + product.productId + '" ><td>' + product.productCategory + '<\/td><td>' + product.goodsName + '<\/td><td>' + product.specificationValues + '<\/td><td><input type="number" name="ownOrderItems[' + productLen + '].quantity" value="'+product.minOrderQuantity+'" min="'+product.minOrderQuantity+'" class="quantityClass input-text radius rdInput"/ ><\/td><td><a href="javascript:void(0);" onclick="javascript:delProductChild(this , ' + product.productId + ');" ><i class="operation_icon icon_del"></i><\/a><\/td><input type="hidden" name="ownOrderItems[' + productLen + '].productId" value="' + product.productId + '"></tr>'
                        [/@compress]);
                        productLen++;
                    });
                    // tempProducts = {};

                },
                onClose: function () {
                    // dealCache();
                },
                onCancel: function () {
                    // dealCache();
                },
                onShow: function () {
                    //$(".dialogContent").height("300px");
                    //$(".dialogContent").css("overflow" , "auto");
                }
            });
        });

        $("#needs li").on("click",function(){
            if($("#supplierId").val() == "" && $need.val() == "") {
                return false;
            }
            $.ajax({
                type: "GET",
                url: "conditionVerif.jhtml",
                data: {"supplier": $("#supplierId").val(), "need": $need.val()},
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
        });
        
        $("#areaEdit").on("click",function(){
            var areaIdT=$("#areaIdT").val();
            if (areaIdT == '') {
                $.message("warn", "请选择个体客户");
                return;
            }
            var addressT=$("#addressT").val();
            var treePath="";
            //加载地区数据、用于默认选择
            $.ajax({
                type: "GET",
                url: "../common/getTreePath.jhtml",
                async: false,
                data: {"id":areaIdT},
                success: function (o) {
                    treePath=o.treePath;
                }
            });

            $.dialog({
                    title:"修改收货地址",
                    width:600,
                    height:300,
                    [@compress single_line=true]
                    content:'<form id="editAddressForm" action=""  class="addNeedForm form form-horizontal">
                            <div class="pag_div" style="padding-top:50px;">
                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">
                                        <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>地区<\/label>
                                    <div class="formControls col-xs-8 col-sm-7"> 
                                        <span class="fieldSet">
                                            <input type="hidden" id="areaId" name="areaId" value="'+areaIdT+'" treePath="'+treePath+'" \/>
                                        <\/span>
                                    <\/div>
                                <\/div>


                                <div class="row cl">
                                    <label class="form-label col-xs-4 col-sm-3">详细地址<\/label>
                                    <div class="formControls col-xs-8 col-sm-7">
                                        <input type="text" class="input-text radius" placeholder="详细地址" name="address" id="address" maxlength="50" autocomplete="off" value="'+addressT+'" \/>
                                    <\/div>
                                <\/div>
                            <\/div>
                        <\/form>',
                    [/@compress]
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function(){
                        if(!$("#editAddressForm").valid()){
                            return false ;
                        }
                        areaIdT=$("#areaId").val();
                        addressT=$("#address").val();
                        $("#areaIdT").val(areaIdT);
                        $("#addressT").val(addressT);
                        console.log(areaIdT);
                        console.log(addressT);
                        //获取所选地区的全称
                        $.ajax({
                            type: "GET",
                            url: "../common/getTreePath.jhtml",
                            async: false,
                            data: {"id":areaIdT},
                            success: function (o) {
                                $("#allAddress").val(o.fullName+" "+addressT);
                            }
                        });
                    },
                    onClose:function(){
                    },
                    onCancel:function(){
                    },
                    onShow:function(){
                        $("#editAddressForm input[name='areaId']").lSelect({
                            url: "${base}/admin/common/area.jhtml"
                        });

                        $(".xxDialog").css("top",'100px');

                        $("#editAddressForm").validate({
                            rules: {
                                areaId: {
                                    required: true
                                },
                                address:{
                                    required: true
                                }
                            }
                        });
                    }
                });                 
            });

 });
</script>

</body>
</html>
[/#escape]

