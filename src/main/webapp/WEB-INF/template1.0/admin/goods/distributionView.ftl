[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/skin/default/skin.css" id="skin" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/style.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/js/kkpager/kkpager_blue.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
    <style>
        body{background:#fff;}
        .pag_div{width:45%;float:left;}
		.col-sm-7{width:72%;}
        th{border-top:1px solid #f0f0f0;}
		.form_box{
			overflow: auto;
			overflow-x: hidden;
		}
		.input_box ul{right:0;}
        .child_page{width:100%;height:100%;padding:0;}
    </style>
</head>
<body>

    <form id="inputForm" action="" method="post" class="form form-horizontal">
        <input type="hidden" name="id" value="${goods.id}" />
        <input type="hidden" id="isDefault" name="product.isDefault" value="true" />

        [#if goods.hasSpecification()]
                    <input type="hidden" id="stock" name="product.stock" class="text" value="999999999" maxlength="9" disabled="disabled" />
        [#else]
                    <input type="hidden" id="stock" name="product.stock" class="text" value="${goods.defaultProduct.stock}" maxlength="9" readonly="readonly" />
        [/#if]

        <div class="child_page">
            <!-- <div class="cus_nav">
                <ul>
                    <li><a href="">${message("admin.breadcrumb.home")}</a></li>
                    <li><a href="list.jhtml">${message("admin.goods.list")}</a></li>
                    <li>商品查看</li>
                </ul>
            </div> -->
            <div class="form_box">

                <div class='form_baseInfo'>
                    <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                    <div class="pag_div">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">
                                <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Goods.productCategory")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">${goods.productCategory.name}</span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.sn")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">${goods.sn}</span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.caption")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">${goods.caption}</span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.packagesNum")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">${goods.packagesNum}</span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.shelfLife")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <div class="input_no_span">
                                    <span class="input_no_span">
                                    [#if goods.shelfLife??]
                                        ${goods.shelfLife}天
                                    [/#if]
                                    </span>
                                </div>

                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.weight")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <div class="input_no_span">
                                    <span class="input_no_span">
                                    [#if goods.weight??]
                                    ${goods.weight}${message("Goods.weightUnit."+goods.weightUnit)}
                                    [/#if]
                                    </span>
                                </div>

                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.nature")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">
                                [#if goods.nature??]
                                ${message("Goods.nature."+goods.nature)}
                                [/#if]
                                </span>

                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.type")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                           <input type="hidden" id="type" name="type" value="general">
                                           <span class="input_no_span in_span_bag">${message("Goods.Type.general")}</span>

                            </div>
                        </div>


                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Product.cost")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span" name="product.cost" id="cost" >${goods.defaultProduct.cost}</span>
                            </div>
                        </div>

                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Product.price")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span" id="price" name="product.price">${goods.defaultProduct.price}</span>
                            </div>
                        </div>


                    </div>
                    <div class="pag_div">
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">
                                <img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Goods.name")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">${goods.name}</span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.image")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <div class="updateImg">
                                    <div class="img_box">
                                        [#if goods.image??]
                                        <img src="${goods.image}" />
                                        [/#if]
                                    </div>
                                </div>

                            </div>
                        </div>


                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.unit")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">
                                [#if goods.unit??]
                                ${message("Goods.unit."+goods.unit)}
                                [/#if]
                                </span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.storageConditions")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span">
                                [#if goods.storageConditions??]
                                ${message("Goods.storageConditions."+goods.storageConditions)}
                                [/#if]
                                </span>
                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Goods.volume")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <div class="input_no_span">
                                    <span class="input_no_span">
                                    [#if goods.volume??]
                                        ${goods.volume}${message("Goods.volume." + goods.volumeUnit)}
                                    [/#if]
                                    </span>
                                </div>

                            </div>
                        </div>
                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">设置</label>
                            <div class="formControls skin-minimal col-xs-8 col-sm-9">
                                <div class="check-box">

                                    <input type="checkbox" name="isMarketable" value="true" checked="checked" /><span>${message("Goods.isMarketable")}</span>
                                    <input type="hidden" name="_isMarketable" value="false" />

                                    <label style="display: none">
                                        <input type="checkbox" name="isList" value="true"[#if goods.isList] checked="checked"[/#if] />${message("Goods.isList")}
                                        <input type="hidden" name="_isList" value="false" />
                                    </label>
                                    <label style="display: none">
                                        <input type="checkbox" name="isTop" value="true"[#if goods.isTop] checked="checked"[/#if] />${message("Goods.isTop")}
                                        <input type="hidden" name="_isTop" value="false" />
                                    </label>
                                    <label style="display: none">
                                        <input type="checkbox" name="isDelivery" value="true"[#if goods.isDelivery] checked="checked"[/#if] />${message("Goods.isDelivery")}
                                        <input type="hidden" name="_isDelivery" value="false" />
                                    </label>

                                </div>

                            </div>
                        </div>

                        <div class="row cl">
                            <label class="form-label col-xs-4 col-sm-3">${message("Product.marketPrice")}</label>
                            <div class="formControls col-xs-8 col-sm-7">
                                <span class="input_no_span" id="marketPrice" name="product.marketPrice">${goods.defaultProduct.marketPrice}</span>
                            </div>
                        </div>

                    </div>
                    <div style="clear:both;"></div>
                </div>
                <div class="form_spec">
                    <h3 class="form_title" style="margin:20px 0 0 20px;">商品规格</h3>
                    <!-- <button type="button" class="op_button reset_spec" id="resetSpecification" >重置规格</button> -->
                    <div id="specificationTable" class="spec_list" style="width:100%;display:none">

                        [#list goods.specificationItems as specificationItem]
                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-2">${specificationItem.name}</label>
                                <div class="formControls col-xs-8 col-sm-8">
                                    <ul class="spec_list_ul">
                                        <input type="hidden" name="specificationItems[${specificationItem_index}].name"
                                               class="text specificationItemName" value="${specificationItem.name}" style="width: 50px;"/>
                                        [#list specificationItem.entries as entry]
                                            <li>
                                                <span><input type="checkbox"
                                                       name="specificationItems[${specificationItem_index}].entries[${entry_index}].isSelected"
                                                       value="true"[#if entry.isSelected] checked="checked"[/#if]/>
                                                <input type="hidden"
                                                       name="_specificationItems[${specificationItem_index}].entries[${entry_index}].isSelected"
                                                       value="false"/>
                                                <input type="hidden"
                                                       name="specificationItems[${specificationItem_index}].entries[${entry_index}].id"
                                                       class="text specificationItemEntryId" value="${entry.id}"/>

                                                <input type="hidden"
                                                       name="specificationItems[${specificationItem_index}].entries[${entry_index}].value"
                                                       class="text specificationItemEntryValue" value="${entry.value}"
                                                       data-value="${entry.value}" style="width: 50px;"/>
                                                </span>
                                                <i class="i_padding">${entry.value}</i>
                                            </li>
                                        [/#list]

                                    </ul>
                                </div>
                            </div>

                        [/#list]

                    </div>
                    <div  class="spec_form" style="margin-bottom:50px;">
                        <div class="table_box">
                        <table id="productTable" class="table table-border table-hover table_width">

                        </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>

    </form>



<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.tools.js"></script>
<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script type="text/javascript">
    $().ready(function() {

        var $inputForm = $("#inputForm");
        var $isDefault = $("#isDefault");
        var $productCategoryId = $("#productCategoryId");
        var $price = $("#price");
        var $type = $("#type");
        var $cost = $("#cost");
        var $marketPrice = $("#marketPrice");
        var $filePicker = $("#filePicker");
        var $rewardPoint = $("#rewardPoint");
        var $exchangePoint = $("#exchangePoint");
        var $stock = $("#stock");
        var $promotionIds = $("input[name='promotionIds']");
        var $introduction = $("#introduction");
        var $productImageTable = $("#productImageTable");
        var $addProductImage = $("#addProductImage");
        var $parameterTable = $("#parameterTable");
        var $addParameter = $("#addParameter");
        var $resetParameter = $("#resetParameter");
        var $attributeTable = $("#attributeTable");
        var $specificationTable = $("#specificationTable");
        var $resetSpecification = $("#resetSpecification");
        var $productTable = $("#productTable");
        //var previousProductCategoryId = ${goods.productCategory.id};
        var productImageIndex = ${(goods.productImages?size)!0};
        var parameterIndex = ${(goods.parameterValues?size)!0};
        var specificationItemEntryId = ${(goods.specificationItemEntryIds?last + 1)!0};

        $filePicker.uploader({
            before:function(file){
                var fr = new FileReader();
                var file = file.source.source ;
                fr.onload = function () {
                    $(".updateImg .img_box").html("<img src='' />");
                    $(".updateImg img").attr("src", fr.result);
                };
                fr.readAsDataURL(file);
            },
            complete:function(){
            }
        });

        $(".in_no_all").click(function () {
            if (this.checked) {
                $(this).parent().parent().find(".in_no").attr("disabled", false);
                $(this).parent().parent().find(".no_dafault").attr("disabled", false);
                if ($("input[name='dafault']:checked")) {

                } else {
                    $(".no_dafault").eq(0);
                }
            } else {
                $(this).parent().parent().find(".in_no").attr("disabled", true);
                $(this).parent().parent().find(".no_dafault").attr("disabled", true).attr("checked", this.checked);
                ;
            }
        })

		/*单位的下拉列表*/
		$(".input_box").each(function(){
            var $selectDom = $(this).find("li.li_bag") ;

            var firstText = $selectDom.text();
            var firstVal = $selectDom.attr("val");

        	$(this).find(".weight_unit").val(firstText);
        	$(this).find(".downList_val").val(firstVal);
       	});
        $(".img_box").mouseover(function () {
            $(".img_model").css("display", "block");
        });
        $(".img_model").mouseleave(function () {
            $(".img_model").css("display", "none");
        });

        $(".updateImg .delImg").on("click", function () {
            $(".updateImg .img_box").html("");
            $(this).parent().css("display", "none");
        });

		/*/!*获取页面的高度*!/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);*/

		/*下拉框的样式*/
        $(".input_box .box_right ul li").on("click",function(){
            $(this).parent().siblings(".weight_unit").val($(this).html());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));

            $(this).parent().siblings(".downList_val").change();
  
            $(this).parent().css("display","none");
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });
        $(".input_box .box_right").mouseover(function(){
            $(this).find("ul").css("display","block");
        });
        $(".input_box .box_right").mouseout(function(){
            $(this).find("ul").css("display","none");
        });

        $(".down_list").click(function(){
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
                $(".downList_con").hide();
            }
            event.stopPropagation();
        });
        
        $(".downList_con").each(function(){

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
        
        
        
        
        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });


        [@flash_message /]

        var hasSpecification = ${goods.hasSpecification()?string("true", "false")};
        var initProductValues = {};

        [@flash_message /]

        [#if goods.hasSpecification()]
            [#list goods.products as product]
                initProductValues["${product.specificationValueIds?join(",")}"] = {
                    id: ${product.id},
                    sn: "${product.sn}",
                    price: ${product.price},
                    cost: ${product.cost!"null"},
                    marketPrice: ${product.marketPrice},
                    rewardPoint: ${product.rewardPoint},
                    exchangePoint: ${product.exchangePoint},
                    stock: ${product.stock},
                    allocatedStock: ${product.allocatedStock},
                    isDefault: ${product.isDefault?string("true", "false")},
                    isEnabled: true
                };
            [/#list]
            changeView();
            buildProductTable(initProductValues);
        [#else]
            loadSpecification();
        [/#if]

        // 商品分类
        $productCategoryId.change(function() {
            loadSpecification();
            //previousProductCategoryId = $productCategoryId.val();
        });

        // 修改视图
        function changeView() {
            if (hasSpecification) {
                $isDefault.prop("disabled", true);
                $price.add($cost).add($marketPrice).prop("disabled", true).closest("div.row").hide();
            } else {
                $isDefault.prop("disabled", false);
                $cost.add($marketPrice).add($price).prop("disabled", false).closest("div.row").show();
            }
        }


        // 重置规格
        $resetSpecification.click(function() {

            $.dialog({
                type: "warn",
                content: "${message("admin.goods.resetSpecificationConfirm")}",
                width: 450,
                onOk: function() {
                    hasSpecification = false;
                    changeView();
                    loadSpecification();
                }
            });
        });

        // 选择规格
        $specificationTable.on("change", "input:checkbox", function() {
            if ($specificationTable.find("input:checkbox:checked").size() > 0) {
                hasSpecification = true;
            } else {
                hasSpecification = false;
            }
            changeView();
            buildProductTable();
        });

        // 规格
        $specificationTable.on("change", "input:text", function() {
            var $this = $(this);
            var value = $.trim($this.val());
            if (value == "") {
                $this.val($this.data("value"));
                return false;
            }
            if ($this.hasClass("specificationItemEntryValue")) {
                var values = $this.closest("tr").find("input.specificationItemEntryValue").not($this).map(function() {
                    return $.trim($(this).val());
                }).get();
                if ($.inArray(value, values) >= 0) {
                    $.message("warn", "${message("admin.goods.specificationItemEntryValueRepeated")}");
                    $this.val($this.data("value"));
                    return false;
                }
            }
            $this.data("value", value);
            buildProductTable();
        });

        // 是否默认
        $productTable.on("change", "input.isDefault", function() {
            var $this = $(this);
            if ($this.prop("checked")) {
                $productTable.find("input.isDefault").not($this).prop("checked", false);
            } else {
                $this.prop("checked", true);
            }
        });

        // 是否启用
        $productTable.on("change", "input.isEnabled", function() {
            var $this = $(this);
            if ($this.prop("checked")) {
                $this.closest("tr").find("input:not(.isEnabled)").prop("disabled", false);
            } else {
                $this.closest("tr").find("input:not(.isEnabled)").prop("disabled", true).end().find("input.isDefault").prop("checked", false);
            }
            if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
                $productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
            }
        });

        // 生成商品表
        function buildProductTable(productValues) {
            var type = $type.val();
            var specificationItems = [];
            if (!hasSpecification) {
                $productTable.empty()
                return false;
            }
            $specificationTable.find("ul").each(function() {
                var $this = $(this);
                var $checked = $this.find("input:checkbox:checked");
                if ($checked.size() > 0) {
                    var specificationItem = {};
                    specificationItem.name = $this.find("input.specificationItemName").val();
                    specificationItem.entries = $checked.map(function() {
                        return {
                            id: $(this).siblings("input.specificationItemEntryId").val(),
                            value: $(this).siblings("input.specificationItemEntryValue").val()
                        };
                    }).get();
                    specificationItems.push(specificationItem);
                }
            });
            var products = cartesianProductOf($.map(specificationItems, function(specificationItem) {
                return [specificationItem.entries];
            }));
            if (productValues == null) {
                productValues = {};
                $productTable.find("tr:gt(0)").each(function() {
                    var $this = $(this);
                    productValues[$this.data("ids")] = {
                        price: $this.find("input.price").val(),
                        cost: $this.find("input.cost").val(),
                        marketPrice: $this.find("input.marketPrice").val(),
                        rewardPoint: $this.find("input.rewardPoint").val(),
                        exchangePoint: $this.find("input.exchangePoint").val(),
                        stock: $this.find("input.stock").val(),
                        isDefault: $this.find("input.isDefault").prop("checked"),
                        isEnabled: $this.find("input.isEnabled").prop("checked")
                    };
                });
            }
            $titleTr = $('<tr class="text-l"><\/tr>').appendTo($productTable.empty());
            $.each(specificationItems, function(i, specificationItem) {
                $titleTr.append('<th>' + escapeHtml(specificationItem.name) + '<\/th>');
            });
        $titleTr.append(
            [@compress single_line = true]
                    (type == "general" ? '<th>${message("Product.price")}<\/th>' : '') + '
                    
            
            ' + (type == "general" ? '<th class="hidden">${message("Product.rewardPoint")}<\/th>' : '') +
            (type == "exchange" ? '<th class="hidden">${message("Product.exchangePoint")}<\/th>' : '') + '
            <th class="hidden">
                    ${message("Product.stock")}
                    <\/th>
            
            '
            [/@compress]
        );
            $.each(products, function(i, entries) {
                var ids = [];
                $productTr = $('<tr class="text-l"><\/tr>').appendTo($productTable);
                $.each(entries, function(j, entry) {
                $productTr.append(
                    [@compress single_line = true]
                            '<td>
                        ' + escapeHtml(entry.value) + '
                        <input type="hidden" name="productList[' + i + '].specificationValues[' + j + '].id" value="' + entry.id + '" \/>
                        <input type="hidden" name="productList[' + i + '].specificationValues[' + j + '].value" value="' + escapeHtml(entry.value) + '" \/>
                        <\/td>'
                    [/@compress]
                    );
                    ids.push(entry.id);
                });
                var productValue = productValues[ids.join(",")];
                var price = productValue != null && productValue.price != null ? productValue.price : "";
                var cost = productValue != null && productValue.cost != null ? productValue.cost : "";
                var marketPrice = productValue != null && productValue.marketPrice != null ? productValue.marketPrice : "";
                var rewardPoint = productValue != null && productValue.rewardPoint != null ? productValue.rewardPoint : "";
                var exchangePoint = productValue != null && productValue.exchangePoint != null ? productValue.exchangePoint : "";
                var stock = productValue != null && productValue.stock != null ? productValue.stock : "";
                var isDefault = productValue != null && productValue.isDefault != null ? productValue.isDefault : false;
                var isEnabled = productValue != null && productValue.isEnabled != null ? productValue.isEnabled : false;
            $productTr.append(
                [@compress single_line = true]
                        (type == "general" ? '<td><p class="price_form_p">
                                <span>￥<\/span><input type="text" name="productList[' + i + '].price" class="text price" value="' + price + '" maxlength="16" readonly="readonly" \/><\/p><\/td>' : '') + '
                ' + (type == "general" ? '<td class="hidden"><input type="text" name="productList[' + i + '].rewardPoint" class="text rewardPoint" value="' + rewardPoint + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') +
                (type == "exchange" ? '<td class="hidden"><input type="text" name="productList[' + i + '].exchangePoint" class="text exchangePoint" value="' + exchangePoint + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') + '
                <td class="hidden">
                        <input type="text" name="productList[' + i + '].stock" class="text stock" value="99999999" maxlength="9" style="width: 50px;" \/>
                    <\/td>
                '
                [/@compress]
                ).data("ids", ids.join(","));
                if (!isEnabled) {
                    $productTr.find(":input:not(.isEnabled)").prop("disabled", true);
                }
            });
            if ($productTable.find("input.isDefault:not(:disabled):checked").size() == 0) {
                $productTable.find("input.isDefault:not(:disabled):first").prop("checked", true);
            }
        }

        // 笛卡尔积
        function cartesianProductOf(array) {
            function addTo(current, args) {
                var i, copy;
                var rest = args.slice(1);
                var isLast = !rest.length;
                var result = [];
                for (i = 0; i < args[0].length; i++) {
                    copy = current.slice();
                    copy.push(args[0][i]);
                    if (isLast) {
                        result.push(copy);
                    } else {
                        result = result.concat(addTo(copy, rest));
                    }
                }
                return result;
            }
            return addTo([], array);
        }

        // 加载规格
        function loadSpecification() {
            $.ajax({
                url: "specifications.jhtml",
                type: "GET",
                data: {productCategoryId: $productCategoryId.val()},
                dataType: "json",
                success: function(data) {
                    $specificationTable.empty();
                    $productTable.empty();
                    $.each(data, function(i, specification) {
                    var $td = $(
                        [@compress single_line = true]
                                '<div class="row cl">
                                <label class="form-label col-xs-4 col-sm-2">'+escapeHtml(specification.name)+'</label>
                                <div class="formControls col-xs-8 col-sm-8">
                                <ul class="spec_list_ul">
                                <input type="hidden" name="specificationItems[' + i + '].name" class="text specificationItemName" value="' + escapeHtml(specification.name) + '" style="width: 50px;" \/>
                            <\/ul>
                        <\/div>
                        <\/div>'
                        [/@compress]
                        ).appendTo($specificationTable).find("input").data("value", specification.name).end().find("ul");
                        $.each(specification.options, function(j, option) {
                        $(
                            [@compress single_line = true]
                                    '<li>
                                    <span><input type="checkbox" name="specificationItems[' + i + '].entries[' + j + '].isSelected" value="true" \/>
                                <input type="hidden" name="_specificationItems[' + i + '].entries[' + j + '].isSelected" value="false" \/>
                                <input type="hidden" name="specificationItems[' + i + '].entries[' + j + '].id" class="text specificationItemEntryId" value="' + specificationItemEntryId + '" \/>
                                <input type="hidden" name="specificationItems[' + i + '].entries[' + j + '].value" class="text specificationItemEntryValue" value="' + escapeHtml(option) + '" style="width: 50px;" \/></span>
                            <i style="padding:0 5px;">'+escapeHtml(option)+'</i>
                            <\/li>'
                            [/@compress]
                            ).appendTo($td).find("input.specificationItemEntryValue").data("value", option);
                            specificationItemEntryId ++;
                        });
                    });
                }
            });
        }


        $.validator.addClassRules({
            productImageFile: {
                required: true,
                extension: "${setting.uploadImageExtension}"
            },
            productImageOrder: {
                digits: true
            },
            parameterGroup: {
                required: true
            },
            price: {
                required: true,
                min: 0,
                decimal: {
                    integer: 12,
                    fraction: ${setting.priceScale}
                }
            },
            cost: {
                min: 0,
                decimal: {
                    integer: 12,
                    fraction: ${setting.priceScale}
                }
            },
            marketPrice: {
                min: 0,
                decimal: {
                    integer: 12,
                    fraction: ${setting.priceScale}
                }
            },
            rewardPoint: {
                digits: true
            },
            exchangePoint: {
                required: true,
                digits: true
            },
            stock: {
                required: true,
                digits: true
            }
        });

        // 表单验证
        $("#inputForm").validate({
            rules: {
                productCategoryId: "required",
                sn: {
                    pattern: /^[0-9a-zA-Z_-]+$/,
                    remote: {
                        url: "check_sn.jhtml",
                        cache: false
                    }
                },
                name: "required",
                "product.price": {
                    required: true,
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                "product.cost": {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                "product.marketPrice": {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                image: {
                    pattern: /^(http:\/\/|https:\/\/|\/).*$/i
                },
                weight: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                volume:{
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                "product.rewardPoint": "digits",
                "product.exchangePoint": {
                    digits: true,
                    required: true
                },
                "product.stock": {
                    required: true,
                    digits: true
                }
            },
            messages: {
                sn: {
                    pattern: "${message("admin.validate.illegal")}",
                    remote: "${message("admin.validate.exist")}"
                }
            },
            submitHandler: function(form) {
                if (hasSpecification && $productTable.find("input.isEnabled:checked").size() == 0) {
                    $.message("warn", "${message("admin.goods.specificationProductRequired")}");
                    return false;
                }
                //addCookie("previousProductCategoryId", $productCategoryId.val(), {expires: 24 * 60 * 60});
                $(form).find("input:submit").prop("disabled", true);
                form.submit();
            }
        });

    });
</script>

</body>
</html>
[/#escape]
