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
        body{background:#f9f9f9;}
        .pag_div{width:45%;float:left;}
		.col-sm-7{width:72%;}
        th{border-top:1px solid #eaeefb;}
		.form_box{
			overflow: auto;
			overflow-x: hidden;
		}
    </style>
</head>
<body>
<form id="inputForm" action="save.jhtml" method="post" class="form form-horizontal">
    <input type="hidden" id="isDefault" name="product.isDefault" value="true" />
    <input type="hidden" id="exchangePoint" name="product.exchangePoint" class="text" maxlength="9" disabled="disabled" />
    <input type="hidden" id="stock" name="product.stock" class="text" value="999999999" maxlength="9" />

    <div class="child_page">
        <div class="cus_nav">
            <ul>
            	<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
            	<li><a href="list.jhtml">${message("admin.goods.list")}</a></li>
                <li>${message("admin.goods.add")}</li>
            </ul>
        </div>
        <div class="form_box">

            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Goods.productCategory")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
									[#--<span class="select-box">
										<select id="productCategoryId" class="select size-L" size="1" name="productCategoryId">
                                            [#list productCategoryTree as productCategory]
                                                <option value="${productCategory.id}">
												[#if productCategory.grade != 0]
                                                    [#list 1..productCategory.grade as i]
                                                        &nbsp;&nbsp;
                                                    [/#list]
                                                [/#if]
                                                ${productCategory.name}
												</option>
                                            [/#list]
										</select>
									</span>--]
                                        <input type="text" id="productCategoryValue" class="input-text radius down_list" readonly placeholder="请选择">
                                        <input type="text" id="productCategoryId" name="productCategoryId" class="downList_val" />
                                        <ul class="downList_con">
                                            [#list productCategoryTree as productCategory]
                                                <li val="${productCategory.id}">[#if productCategory.grade != 0][#list 1..productCategory.grade as i]&nbsp;&nbsp;[/#list][/#if]${productCategory.name}</li>
                                            [/#list]
                                        </ul>

                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.sn")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                        [#--<span class="input_no_span">4374837447832</span>--]
                            <input type="text" name="sn" class="input-text" maxlength="100" title="${message("admin.goods.snTitle")}" value=""/>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.caption")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" placeholder="" name="caption" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.packagesNum")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" placeholder="" name="packagesNum" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.shelfLife")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="input_box">
                                <input type="text" name="shelfLife" class="" />
                                <div class="box_right">天</div>
                            </div>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.weight")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="input_box">
                                <input type="text" name="weight" class="" />
                                <div class="box_right input_right">
                                    <input type="text" readonly="readonly" class="weight_unit" value="" />
                                    <input type="text" name="weightUnit" class="downList_val" />
                                    <ul>
                                    [#list weightUnits as weightUnit]
                                        <li val="${weightUnit}">${message("Goods.weightUnit."+weightUnit)}</li>
                                    [/#list]
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.nature")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                        [#--<span class="select-box">
                            <select id="nature" name="nature" class="select size-L" size="1">
                                <option value="">${message("admin.common.choose")}</option>
                                [#list natures as nature]
                                    <option value="${nature}">${message("Goods.nature."+nature)}</option>
                                [/#list]

                            </select>
                        </span>--]

                            <input type="text" class="input-text radius down_list" readonly placeholder="请选择">
                            <input type="text" name="nature" class="downList_val" />
                            <ul class="downList_con">
                                <li val="">${message("admin.common.choose")}</li>
                                [#list natures as nature]
                                    <li val="${nature}">${message("Goods.nature."+nature)}</li>
                                [/#list]

                            </ul>

                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.type")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                                   [#--<span class="select-box">
										<select id="type" name="type" class="select size-L" size="1">
                                            [#list types as type]
                                                [#if type_index == 0]
                                                    <option value="${type}">${message("Goods.Type." + type)}</option>
                                                [/#if]
                                            [/#list]
										</select>
								   </span>--]
                                       <input type="hidden" name="type" id="type" value="general">
                                       <span class="input_no_span in_span_bag">${message("Goods.Type.general")}</span>
                        </div>
                    </div>


                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Product.cost")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                        [#--<span class="input_no_span">4374837447832</span>--]
                            <input type="text" name="product.cost" id="cost" class="input-text" maxlength="100" />
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Product.price")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                        [#--<span class="input_no_span">4374837447832</span>--]
                            <input type="text" id="price" name="product.price" class="input-text" maxlength="100"  value="0"/>
                        </div>
                    </div>


                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                        	<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />${message("Goods.name")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" placeholder="" name="name" />
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.image")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="updateImg">
                                <div class="img_box">
                                </div>
                                <input type="text" name="image" style="display: none" value=""/>
                                <a id="filePicker" class="file" style="display:block;width:60px;height:60px;margin:30px;opacity: 0" >gggggggggggggggggggggggggg</a>
                                <div class="img_model">
                                    <span class="delImg"></span>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.unit")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
									[#--<span class="select-box">
										<select class="select size-L" size="1" name="unit">
											<option value="">${message("admin.common.choose")}</option>
                                            [#list units as unit]
                                                <option value="${unit}">${message("Goods.unit."+unit)}</option>
                                            [/#list]
										</select>
									</span>--]
                                        <input type="text" class="input-text radius down_list" readonly placeholder="请选择">
                                        <input type="text" name="unit" class="downList_val" />
                                        <ul class="downList_con">
                                            <li val="">${message("admin.common.choose")}</li>
                                            [#list units as unit]
                                                <li val="${unit}">${message("Goods.unit."+unit)}</li>
                                            [/#list]

                                        </ul>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.storageConditions")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
									[#--<span class="select-box">
										<select class="select size-L" size="1" name="storageConditions">
											<option value="">${message("admin.common.choose")}</option>
											<option value="roomTemperature">${message("Goods.storageConditions.roomTemperature")}</option>
											<option value="refrigeration">${message("Goods.storageConditions.refrigeration")}</option>
											<option value="frozen">${message("Goods.storageConditions.frozen")}</option>
										</select>
									</span>--]
                                        <input type="text" class="input-text radius down_list" readonly placeholder="请选择">
                                        <input type="text" name="storageConditions" class="downList_val" />
                                        <ul class="downList_con">
                                            <li val="">${message("admin.common.choose")}</li>
                                            [#list storageConditions as storageCondition]
                                                <li val="${storageCondition}">${message("Goods.storageConditions."+storageCondition)}</li>
                                            [/#list]

                                        </ul>
                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.volume")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="input_box">
                                <input type="text" class="" name="volume"/>
                                <div class="box_right input_right">
                                    <input type="text" readonly="readonly" class="weight_unit" value="cm³" />
                                    <input type="text" name="volumeUnit" class="downList_val" />
                                    <ul>
                                        [#list volumeUnits as volumeUnit]
                                            <li val="${volumeUnit}">${message("Goods.volume." + volumeUnit)}</li>
                                        [/#list]
                                    </ul>
                                </div>
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
                                    <input type="checkbox" name="isList" value="true" checked="checked" />${message("Goods.isList")}
                                    <input type="hidden" name="_isList" value="false" />
                                </label>
                                <label style="display: none">
                                    <input type="checkbox" name="isTop" value="true" />${message("Goods.isTop")}
                                    <input type="hidden" name="_isTop" value="false" />
                                </label>
                                <label style="display: none">
                                    <input type="checkbox" name="isDelivery" value="true" checked="checked" />${message("Goods.isDelivery")}
                                    <input type="hidden" name="_isDelivery" value="false" />
                                </label>

                            </div>
                        [#--<div class="check-box">
                            <input type="checkbox" name=""><span>是否列出</span>
                        </div>
                        <div class="check-box">
                            <input type="checkbox" name=""><span>是否置顶</span>
                        </div>--]
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Product.marketPrice")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" id="marketPrice" name="product.marketPrice" class="input-text radius" placeholder="">
                        </div>
                    </div>

                </div>
                <div style="clear:both;"></div>
            </div>
            <div class="form_spec">
                <h3 class="form_title" style="margin:20px 0 0 20px;">商品规格</h3>
                <button type="button" class="op_button reset_spec" id="resetSpecification" >重置规格</button>
                <div id="specificationTable" class="spec_list" style="width:96%">
                [#--<div class="row cl">
								<label class="form-label col-xs-4 col-sm-2">体积</label>
								<div class="formControls col-xs-8 col-sm-8">
									 <ul class="spec_list_ul">
									 	<li>
									 		<span><input type="checkbox" /></span>
									 		<i>600g*8包</i>
									 	</li>
									 	<li>
									 		<span><input type="checkbox" /></span>
									 		<i>900g*8包</i>
									 	</li>
									 </ul>
								</div>
							</div>

                            <div class="row cl">
                                <label class="form-label col-xs-4 col-sm-2">体积</label>
                                <div class="formControls col-xs-8 col-sm-8">
                                    <ul class="spec_list_ul">
                                        <li>
                                            <span><input type="checkbox" /></span>
                                            <i>600g*8包</i>
                                        </li>
                                        <li>
                                            <span><input type="checkbox" /></span>
                                            <i>900g*8包</i>
                                        </li>
                                    </ul>
                                </div>
                            </div>--]
                </div>
                <div  class="spec_form" style="margin-bottom:50px;">
                	<div class="table_box">
                    <table id="productTable" class="table table-border table-hover table_width">
                    [#--<thead>
									<tr class="text-l">
										<th width="16%"><div class="th_div">体积</div></th>
										<th width="16%"><div class="th_div">销售价</div></th>
										<th width="16%"><div class="th_div">成本价</div></th>
										<th width="16%"><div class="th_div">市场价</div></th>
										<th width="16%"><div class="th_div">是否默认</div></th>
										<th width="16%"><div class="th_div">是否启用</div></th>
									</tr>
								</thead>
								<tbody>
									<tr class="text-l">
										<td>500g*8包/箱</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>

										<td><input type="radio" name="dafault" class="no_dafault" disabled /></span></td>
										<td><input type="checkbox" class="in_no_all" /></span></td>
									</tr>
									<tr class="text-l">
										<td>500g*8包/箱</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>
										<td>
											<p class="price_form_p">
												<span>￥</span>
												<input type="text" class="in_no" disabled />
											</p>
										</td>

										<td><input type="radio" name="dafault" class="no_dafault" disabled /></span></td>
										<td><input type="checkbox" class="in_no_all" /></span></td>
									</tr>
								</tbody>--]
                    </table>
                    </div>
                </div>
            </div>




        </div>
    </div>
    <div class="footer_submit">
        <input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
        <input class="btn radius cancel_B" type="button" value="${message("admin.common.back")}" onclick="history.back();return false;">
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
        var $type = $("#type");
        var $price = $("#price");
        var $cost = $("#cost");
        var $marketPrice = $("#marketPrice");
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
        var productImageIndex = 0;
        var parameterIndex = 0;
        var specificationItemEntryId = 0;
        var hasSpecification = false;

        [@flash_message /]

        /*var previousProductCategoryId = getCookie("previousProductCategoryId");
        previousProductCategoryId != null ? $productCategoryId.val(previousProductCategoryId) : previousProductCategoryId = $productCategoryId.val();*/

        var $filePicker = $("#filePicker");
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
            $(this).find("ul li").eq(0).addClass("li_bag");
            var firstText = $(this).find("li:eq(0)").text();
            var firstVal = $(this).find("li:eq(0)").attr("val");
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

		/*获取页面的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);

		/*下拉框的样式*/
        $(".input_box .box_right ul li").on("click",function(){
            $(this).parent().siblings(".weight_unit").val($(this).html());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
  
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
        	$(this).find("li:eq(0)").addClass("li_bag");
        	var firstText = $(this).find("li:eq(0)").text();
        	var firstVal = $(this).find("li:eq(0)").attr("val");
        	$(this).siblings(".down_list").val(firstText);
        	$(this).siblings(".downList_val").val(firstVal);
        });
        
        

        $(".downList_con li").click(function(){
            $(this).parent().siblings(".down_list").attr("value",$(this).text());
          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));

            $(this).parent().siblings(".downList_val").change();

            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });
		


        loadSpecification();

        // 商品分类
        $productCategoryId.change(function() {
            loadSpecification();
            //previousProductCategoryId = $productCategoryId.val();
        });

        // 类型
        $type.change(function() {
            changeView();
            buildProductTable();
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
        function buildProductTable() {
            var type = $type.val();
            var productValues = {};
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
            $titleTr = $('<tr class="text-l"><\/tr>').appendTo($productTable.empty());
            $.each(specificationItems, function(i, specificationItem) {
                $titleTr.append('<th>' + escapeHtml(specificationItem.name) + '<\/th>');
            });
        $titleTr.append(
            [@compress single_line = true]
                    (type == "general" ? '<th>${message("Product.price")}<\/th>' : '') + '
                    <th>
                    ${message("Product.cost")}
                    <\/th>
            <th>
            ${message("Product.marketPrice")}
            <\/th>
            ' + (type == "general" ? '<th class="hidden">${message("Product.rewardPoint")}<\/th>' : '') +
            (type == "exchange" ? '<th class="hidden">${message("Product.exchangePoint")}<\/th>' : '') + '
            <th class="hidden">
                    ${message("Product.stock")}
                    <\/th>
            <th>
            ${message("Product.isDefault")}
            <\/th>
            <th>
            ${message("admin.goods.isEnabled")}
            <\/th>'
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
                                <span>￥<\/span><input type="text" name="productList[' + i + '].price" class="text price" value="' + price + '" maxlength="16"  \/><\/p><\/td>' : '') + '
                <td>
                <p class="price_form_p">
                        <span>￥<\/span>
                <input type="text" name="productList[' + i + '].cost" class="in_no" value="' + cost + '" maxlength="16"  \/>
                    <\/p>
                <\/td>
                <td>
                <p class="price_form_p">
                        <span>￥<\/span>
                <input type="text" name="productList[' + i + '].marketPrice" class="text marketPrice" value="' + marketPrice + '" maxlength="16"  \/><\/p>
                <\/td>
                ' + (type == "general" ? '<td class="hidden"><input type="text" name="productList[' + i + '].rewardPoint" class="text rewardPoint" value="' + rewardPoint + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') +
                (type == "exchange" ? '<td class="hidden"><input type="text" name="productList[' + i + '].exchangePoint" class="text exchangePoint" value="' + exchangePoint + '" maxlength="9" style="width: 50px;" \/><\/td>' : '') + '
                <td class="hidden">
                        <input type="text" name="productList[' + i + '].stock" class="text stock" value="99999999" maxlength="9" style="width: 50px;" \/>
                    <\/td>
                <td>
                <input type="checkbox" name="productList[' + i + '].isDefault" class="isDefault" value="true"' + (isDefault ? ' checked="checked"' : '') + ' \/>
                    <input type="hidden" name="_productList[' + i + '].isDefault" value="false" \/>
                    <\/td>
                <td>
                <input type="checkbox" name="isEnabled" class="isEnabled" value="true"' + (isEnabled ? ' checked="checked"' : '') + ' \/>
                    <\/td>'
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
                            <i class="i_padding">'+escapeHtml(option)+'</i>
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
