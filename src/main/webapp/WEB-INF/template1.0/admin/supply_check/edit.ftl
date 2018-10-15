[#escape x as x?html]
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css"/>
    <link rel="stylesheet" href="${base}/resources/admin1.0/js/element-ui/lib/theme-default/index.css">
    
    
    <style>
        body {
            background: #f9f9f9;
        }

        .pag_div {
            width: 47%;
            float: left;
        }

        .col-sm-7 {
            width: 72%;
        }

        th {
            border-top: 1px solid #f0f0f0;
        }

        .form_box {
            overflow: auto;
            overflow-x: hidden;
        }

        .type_radio {
            padding: 5px 5px;
        }

        .type_radio label {
            margin-right: 20px;
        }

        .res_price .symbol {
            display: block;
            float: left;
            width: 32px;
            height: 32px;
            background: #f9f9f9;
            border: 1px solid #cadbf3;
            text-align: center;
            line-height: 32px;
            font-size: 16px;
            color: #333;
            border-top-left-radius: 4px;
            border-bottom-left-radius: 4px;
        }

        .res_price .radio_label {
            display: block;
            float: left;
            height: 18px;
            width: 50px;
            overflow: hidden;
            padding: 6px 3px;
        }

        .res_price .radio_label input {
            float: left;
            margin-top: 3px;
        }

        #marketPriceShow {
            width: 234px;
            height: 32px;
            border: 1px solid #cadbf3;
            border-left: 0;
            border-top-right-radius: 4px;
            border-bottom-right-radius: 4px;
            text-indent: 10px;
            float: left;
        }

        #marketPriceShow-error {
            float: right;
            padding-top: 8px;
        }
        .el-cascader__label{color:#333;}
        .el-input__inner{border:1px solid #f0f0f0;}
    </style>
</head>
<body>
<form id="inputForm" action="update.jhtml" method="post" class="form form-horizontal">

    <input type="hidden" name="id" value="${goods.id}"/>
    <input type="hidden" name="status" id="status" value="${goods.status}"/>

    <div class="child_page">
        <div class="cus_nav">
            <ul>
                <li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
                <li><a href="list.jhtml">供应审核</a></li>
                <li>编辑</li>
            </ul>
        </div>
        <div class="form_box">

            <div class='form_baseInfo'>
                <h3 class="form_title" style="margin:20px 0 0 20px;">基本信息</h3>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                            <img src="${base}/resources/admin1.0/images/bitian_icon.svg"
                                 alt=""/>${message("Goods.productCategory")}</label>
                        <div class="formControls col-xs-8 col-sm-7"> 
                            <div id="app">
							  	<el-cascader
							    	:options="options"
							    	v-model="selectedOptions"
							    	@change="handleChange">
							  	</el-cascader>
							  	<input type="hidden" :value="selectedOptions | arrLast" name="categoryId" />
						  	</div>

                        </div>
                    </div>
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">货源类型</label>
                        <div class="formControls col-xs-8 col-sm-7 type_radio">
                            [#list sourceTypes as sourceType]
                                <label>
                                    <input name="sourceType" type="radio" value="${sourceType}"
                                           [#if goods.sourceType == sourceType]checked[/#if]><span>${sourceType.getName()}</span>
                                </label>
                            [/#list]
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.shelfLife")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="input_box">
                                <input type="text" name="shelfLife" class="" value="${goods.shelfLife}"/>
                                <div class="box_right">天</div>
                            </div>
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">产品规格</label>
                        <div class="formControls col-xs-8 col-sm-7">
                        [#--<span class="input_no_span">4374837447832</span>--]
                            <input type="text" name="goodsSpec" id="goodsSpec" class="input-text radius"
                                   value="${goods.goodsSpec}"/>
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.image")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <div class="updateImg">
                                <div class="img_box">
                                    [#if goods.image??]
                                        <img src="${goods.image}"/>
                                    [/#if]
                                </div>
                                <input type="text" name="image" style="display: none" value="${goods.image}"/>
                                <a id="filePicker" class="file"
                                   style="display:block;width:60px;height:60px;margin:30px;opacity: 0">gggggggggggggggggggggggggg</a>
                                <div class="img_model">
                                    <span class="delImg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="pag_div">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">
                            <img src="${base}/resources/admin1.0/images/bitian_icon.svg"
                                 alt=""/>${message("Goods.name")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius" placeholder="" name="name"
                                   value="${goods.name}"/>
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.storageConditions")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius down_list" readonly placeholder="请选择">
                            <input type="text" name="storageConditions" class="downList_val"/>
                            <ul class="downList_con">
                                <li val="">${message("admin.common.choose")}</li>
                                [#list storageConditions as storageCondition]
                                    <li val="${storageCondition}"
                                        [#if storageCondition == goods.storageConditions]class="li_bag"[/#if]>${message("Goods.storageConditions."+storageCondition)}</li>
                                [/#list]

                            </ul>
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">${message("Goods.unit")}</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <input type="text" class="input-text radius down_list" readonly placeholder="请选择">
                            <input type="text" name="unit" class="downList_val"/>
                            <ul class="downList_con">
                                <li val="">${message("admin.common.choose")}</li>
                                [#list units as unit]
                                    <li val="${unit}"
                                        [#if unit == goods.unit]class="li_bag"[/#if]>${message("Goods.unit."+unit)}</li>
                                [/#list]

                            </ul>
                        </div>
                    </div>

                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3"><img
                                src="${base}/resources/admin1.0/images/bitian_icon.svg" alt=""/>参考价格</label>
                        <div class="formControls col-xs-8 col-sm-7 res_price">
                            <div class="input-group">
                                <span class="symbol">￥</span>
                                <input name="marketPrice" id="marketPrice" type="hidden" value="${goods.marketPrice}"/>
                                <input name="marketPriceShow" id="marketPriceShow" type="text"
                                       value="[#if goods.marketPrice != -1]${goods.marketPrice}[/#if]"/>
                                <label class="radio_label">
                                    <input type="radio" id="marketPriceRadio" [#if goods.marketPrice == -1]checked[/#if]
                                           value="-1"><span>面议</span>
                                </label>
                            </div>
                        </div>
                    </div>

                </div>


                <div class="pag_div" style="width:90%;padding-top:0;">
                    <div class="row cl">
                        <label class="form-label col-xs-4 col-sm-3">产品详情</label>
                        <div class="formControls col-xs-8 col-sm-7">
                            <textarea id="description" name="introduction" class="editor" style="width: 100%;">
                            ${goods.introduction}
                            </textarea>
                        </div>
                    </div>
                </div>
                <div style="clear:both;"></div>
            </div>

        </div>
    </div>
    <div class="footer_submit">
        <input class="btn radius confir_S" type="button" id="passButton" onclick="javascript:submitForm('status_ok');"
               value="审核通过"/>
        <input class="btn radius cancel_B" type="button" id="unPassButton"
               onclick="javascript:submitForm('status_rej');" value="审核不通过"/>
    </div>
</form>


<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.tools.js"></script>
<script src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
<script src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>

<script src="${base}/resources/admin1.0/js/vue.min.js"></script>
<script src="${base}/resources/admin1.0/js/element-ui/lib/index.js"></script>


<script type="text/javascript">

    function submitForm(status) {

        if (!$("#inputForm").valid()) {
            return false;
        }

        if ($("#marketPriceRadio").prop("checked")) {
            $("#marketPrice").val("-1");
        } else {
            $("#marketPrice").val($("#marketPriceShow").val());
        }

        $("#status").val(status);

        $("#inputForm").submit();
    }

    $().ready(function () {


        var $filePicker = $("#filePicker");

        $("#description").editor();

        $filePicker.uploader({
            before: function (file) {

            },
            complete: function (file) {
                var fr = new FileReader();
                var file = file.file.source.source;
                fr.onload = function () {
                    $(".updateImg .img_box").html("<img src='' />");
                    $(".updateImg img").attr("src", fr.result);
                };
                fr.readAsDataURL(file);
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
        $(".input_box").each(function () {
            var $selectDom = $(this).find("li.li_bag");

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
            $(this).parent().siblings("input:text").val("");
        });

        /*获取页面的高度*/
        var formHeight = $(document.body).height() - 100;
        $(".form_box").css("height", formHeight);

        /*下拉框的样式*/
        $(".input_box .box_right ul li").on("click", function () {
            $(this).parent().siblings(".weight_unit").val($(this).html());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));

            $(this).parent().siblings(".downList_val").change();

            $(this).parent().css("display", "none");
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });
        $(".input_box .box_right").mouseover(function () {
            $(this).find("ul").css("display", "block");
        });
        $(".input_box .box_right").mouseout(function () {
            $(this).find("ul").css("display", "none");
        });

        $(".down_list").click(function () {
            $(this).siblings(".downList_con").toggle();
        });

        $("*").click(function (event) {
            if (!$(this).hasClass("down_list") && !$(this).hasClass("downList_con")) {
                $(".downList_con").hide();
            }
            $(".el-input__inner").removeClass("borderFocus");
            event.stopPropagation();
        });

        $(".downList_con").each(function () {

            var curr = $(this).find("li.li_bag");
            var firstText;
            var firstVal;
            if (curr.length == 0) {
                firstText = $(this).find("li:eq(0)").text();
                firstVal = $(this).find("li:eq(0)").attr("val");
                $(this).find("li:eq(0)").addClass("li_bag");
            } else {
                firstText = curr.text();
                firstVal = curr.attr("val");

            }

            $(this).siblings(".down_list").val(firstText);
            $(this).siblings(".downList_val").val(firstVal);
        });


        $(".downList_con li").click(function () {
            $(this).parent().siblings(".down_list").attr("value", $(this).text());
            $(this).parent().siblings(".downList_val").val($(this).attr("val"));
            $(this).parent().siblings(".downList_val").change();
            $(this).addClass("li_bag").siblings().removeClass("li_bag");
        });


        [@flash_message /]

        // 表单验证
        $("#inputForm").validate({
            rules: {
                categoryId: "required",
                /*sn: {
                    pattern: /^[0-9a-zA-Z_-]+$/,
                    remote: {
                        url: "check_sn.jhtml",
                        cache: false
                    }
                },*/
                name: "required",

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
                volume: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    }
                },
                marketPriceShow: {
                    min: 0,
                    decimal: {
                        integer: 12,
                        fraction: ${setting.priceScale}
                    },
                    required: {
                        required: true,
                        depends: function (element) {
                            return !$("#marketPriceRadio").prop("checked");
                        }
                    },
                }
            },
            messages: {
                sn: {
                    pattern: "${message("admin.validate.illegal")}",
                    remote: "${message("admin.validate.exist")}"
                }
            },
            submitHandler: function (form) {
                $(form).find("input:submit").prop("disabled", true);
                form.submit();
            }
        });


        $("#marketPriceShow").on("input propertychange", function () {
            if ($(this).val().length) {
                $("#marketPriceRadio").prop("checked", false);
            }
        })
        $("#marketPriceRadio").on("click", function () {
            if ($(this).prop("checked")) {
                $("#marketPriceShow").siblings(".error").css("display", "none");
                $("#marketPriceShow").val("");
            }

        })

        var trees = [] ;

        var categoryStr = [#noescape]'${categoryRoots?json_string}'[/#noescape];

        trees = JSON.parse(categoryStr) ;
        //console.log(trees);
        
        var returnData = {};
        returnData.options = trees;
  		
		var ready = [#noescape]'${categoryCurrIds?json_string}'[/#noescape];
			readerArr = JSON.parse(ready) ;
	
		returnData["selectedOptions"] = readerArr;

		new Vue({
  			el:"#app",
		    data() {
		      	return returnData;
			},
			methods: {
				handleChange(value) {
					$("#categoryId-error").css("display","none");
			    	$(".el-input__inner").removeClass("borderFocus");
				}
			},
			filters: {
			    arrLast: function (value) {
			      return value[value.length-1];
			    }
			}
		});
		
		$(".el-cascader").on("click",function(event){
			$(".el-input__inner").addClass("borderFocus");
			event.stopPropagation();
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
