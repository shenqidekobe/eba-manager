[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${message("admin.admin.add")} - Powered By DreamForYou</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/resources/admin1.0/H-ui/static/h-ui.admin/css/H-ui.admin.css" />		
		<link rel="stylesheet" href="${base}/resources/admin1.0/js/date/dateRange.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/public.css" />
		<link rel="stylesheet" href="${base}/resources/admin1.0/css/admin.css" />
		<style>
			body{background:#f9f9f9;}
			.pag_div{width:calc(100% - 30px);}
			label.error{left:300px;}
			.add_list .form_title{float:left;}
			table th{border-top:1px solid #f0f0f0;}
			.col-sm-7{width:75%}
			.xxDialog{top:40px}
			.xxDialog .dialogBottom{width:100%;position:absolute;bottom:0;border:0;}
			.dialogContent{height:calc(100% - 80px); overflow: auto; overflow-x: hidden;}
			iframe{height:calc(100% - 5px);width:100%;}
			.op_button,.tab_button{margin-top:5px;}
			.ch_condition{padding-top:0;}
			.xxDialog .dialogBottom{border-top:1px solid #eee;}
			.fenixao_div{float:left;padding:4px 40px 0 0;}
		</style>
	</head>
	<body class="bodyObj">
		<div class="child_page"><!--内容外面的大框-->	
			<div class="cus_nav">
				<ul>
					<li><a id="goHome"	href="../homePage/index.jhtml">${message("admin.breadcrumb.home")}</a></li>
        			<li><a href="index.jhtml">代下单 </a></li>
					<li>流水门店代下单</li>
				</ul>
			</div>
			<div class="form_box" style="overflow: auto;">
				<form id="inputForm" action="createMore.jhtml" method="post" class="inputForm form form-horizontal">
					<input type="hidden" name="deliveryTime" id="deliveryTime" />
					<input type="hidden" id="supplier" id="${supplier.id}" />
					<div class="pag_div" style="padding:30px 0 20px 30px;">
						<span style="font-weight:900;float:left;width:100px;font-size:16px;">销售模式</span>
						<div class="formControls col-xs-8 col-sm-7"> 
						[#if supplier.systemSetting.isDistributionModel == "true"]
							<div class="fenixao_div">
								<input type="radio" name="assignedModel" class="input-text radius" value="BRANCH" checked="checked">分销
							</div>
							<div class="fenixao_div">
								<input type="radio" name="assignedModel" class="input-text radius" value="STRAIGHT" >直销
							</div>
						[#else]
							<div class="fenixao_div">
								<input type="radio" name="assignedModel" class="input-text radius" value="STRAIGHT" checked="checked">直销
							</div>
						[/#if]
						</div>
					</div>
					<div class="pag_div">
						<h3 class="form_title">收货信息</h3>
						
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" />
								门店地址
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
								<button type="button" id="addNeed" class="op_button add_B">添加新地址</button>
								<button type="button" id="selectNeed" class="tab_button bgred">选择历史地址</button>
							</div>
						</div>
					</div>
					<div class="table_box">
						<table id="needTable" class="table table-border table-hover table_width boo">
							<thead>
								<tr class="text-l">
									<th width="5%">联系人</th>
									<th width="10%">联系电话</th>
									<th width="15%">收货地址</th>
									<th width="10%">收货时间</th>
									<th width="15%">订单备注</th>
									<th width="8%">操作</th>
								</tr>
							</thead>
						</table>	
					</div>	
					<div class="pag_div">	
						<div class="add_list" style="width:100%;height:30px;">
							<h3 class="form_title">商品信息</h3>
						</div>
						<div class="row cl">
							<label class="form-label col-xs-4 col-sm-3">
								<button type="button" id="addPro" class="op_button add_B">添加商品</button>
							</label>
							<div class="formControls col-xs-8 col-sm-7"> 
							
							</div>
						</div>
					</div>	
					<div class="table_box">
						<table id="productTable" class="table table-border table-hover table_width boo">
							<thead>
								<tr class="text-l">
									<th width="15%">${message("Goods.productCategory")}</th>
									<th width="10%">${message("Goods.name")}</th>
									<th width="15%">规格</th>
									<th width="10%">商品数量</th>
									<th width="8%">操作</th>
								</tr>
							</thead>
							
						</table>
					</div>
					<div class="footer_submit">
						<input class="btn radius confir_S" type="submit" value="${message("admin.common.submit")}">
						<input class="btn radius cancel_B" type="button" value="取消" onclick="history.back(); return false;">
					</div>
				</form>
			</div>	
		</div>
		
		<script src="${base}/resources/admin1.0/js/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui/js/H-ui.min.js"></script> 
		<script type="text/javascript" src="${base}/resources/admin1.0/H-ui/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/validate/jquery.validate.min.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/jquery.lSelect.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/ueditor/ueditor.js"></script>
		<script type="text/javascript" src="${base}/resources/admin1.0/js/common.js"></script>
		<script src="${base}/resources/admin1.0/datePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			var products = {};
		    var productsLen = 0 ;

		    var tempProducts = {} ;

		    var needsCache = {} ;
		    var tempNeedsCache = {} ;

		    //子页面调用 增加关联商品
		    /**
		     *
		     * @param obj
		     */
		    function addProduct(product){

		        var proId = product.productId ;
		        if(proId in products){
		            return false ;
		        }
		        products[proId] = product;
		        // tempProducts[proId] = product;
		    }
		    //删除关联商品
		    function delProduct(product){
		        var proId = product.productId ;
		        delete products[proId];
		        // delete tempProducts[proId];
		    }

		    //删除关联商品
		    function delProductChild(now , productId){
		        delete products[productId];
		        $(now).closest("tr").remove();

		    }

		    function getCacheFromParent(){
		        return products ;
		    }

		    /**
		     *
		     * @param obj
		     */
		    function addNeed(need){

		        var needId = need.id ;
		        if(needId in needsCache){
		            return false ;
		        }
		        needsCache[needId] = need;
		        tempNeedsCache[needId] = need;
		    }
		    //删除关联商品
		    function delNeed(need){
		        var needId = need.id ;
		        delete needsCache[needId];
		        delete tempNeedsCache[needId];
		    }

		    //删除关联商品
		    function delNeedChild(now , needId){
		        delete needsCache[needId];
		        $(now).closest("tr").remove();

		    }

		    function getNeedCacheFromParent(){
		        return needsCache ;
		    }


		    function cleanNeedCache(){
		        needsCache = {};
		        tempNeedsCache = {} ;
		        $("#needTable tr").slice(1).remove();
		    }

			$(function(){
				var exists = true;
		    	var message=null;
		    	var $supplier = $("#supplier");
				var formHeight=$(document.body).height()-100;
				$(".form_box").css("height",formHeight);

				/*下拉框*/
				$(".down_list").click(function(){
		            $(this).siblings(".downList_con").toggle();
		        });

		        $("*").click(function (event) {
		            if (!$(this).hasClass("down_list")&&!$(this).hasClass("downList_con")){
		                $(".downList_con").hide();
		            }
		            event.stopPropagation();
		        });

		        /* $(".downList_con").each(function(){
		        	$(this).find("li:eq(0)").addClass("li_bag");
		        	var firstText = $(this).find("li:eq(0)").text();
		        	var firstVal = $(this).find("li:eq(0)").attr("val");
		        	$(this).siblings(".down_list").val(firstText);
		        	$(this).siblings(".downList_val").val(firstVal);
		        }); */

		        $(".downList_con li").click(function(){
		            $(this).parent().siblings(".down_list").attr("value",$(this).text());
		          	$(this).parent().siblings(".downList_val").val($(this).attr("val"));
		            $(this).parent().siblings(".downList_val").change();
		            $(this).addClass("li_bag").siblings().removeClass("li_bag");
		        });
		        //订单设置校验
        		$.ajax({
                    type: "GET",
                    url: "manyaddressVerify.jhtml",
                    data: {"supplier": ${supplier.id}},
                    dataType: "json",
                    beforeSend: function () {

                    },
                    success: function (data) {
                    	$("#deliveryTime").val(data.minDate);
                    	if(data.exists == false) {
    						$.message("warn", data.msg);
    						message = data.msg;
    						exists = false;
    					}
                    },
                    error: function (data) {
                    }
                });


		        var dealCache = function () {
		            $.each(tempProducts , function(key , product){
		                delete products[key];
		            });
		        };

		        var dealNeedCache = function () {
		            $.each(tempNeedsCache , function(key , need){
		                delete needsCache[key];
		            });
		        };

		        var $productTable = $("#productTable");

		        var needs = {} ;
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


		        var productLen = 0 ;
		        var needLen = 0;
		        //选择商品
		        $("#addPro").click(function(){
		            if(!$inputForm.valid()){
		                return false ;
		            }
		            var contentPath = "../utils/getAssProducts.jhtml";
		            $.dialog({
		                title: "选择商品",
		                [@compress single_line=true]
		                    content: '<iframe id="sonFrame" frameborder="0" width="" height="" src="'+contentPath+'"><\/iframe>',
		                [/@compress]
		                width: $(".bodyObj").width()-20,
		                height:$(".bodyObj").height()-40,
		                ok: "${message("admin.dialog.ok")}",
		                cancel: "${message("admin.dialog.cancel")}",
		                onOk: function(){
		                	$("tr[name=goodTr]").remove();
		                    $.each(products , function(key , product){
		                    	if(product.minOrderQuantity==""){
		                    		product.minOrderQuantity=1;
		                    	}
		                        $productTable.append([@compress single_line = true]
		                            '<tr id="remove_'+product.productId+'" name="goodTr"><td>'+product.productCategory+'<\/td><td>'+product.goodsName+'<\/td><td>'+product.specificationValues+'<\/td><td><input type="number" name="ownOrderItems['+productLen+'].quantity" value="'+product.minOrderQuantity+'" min="'+product.minOrderQuantity+'" class="tdInput input-text radius quantityClass"/ ><\/td><td><a href="javascript:void(0);" onclick="javascript:delProductChild(this , '+product.productId+');" ><i class="operation_icon icon_del"></i><\/a><\/td><input type="hidden" name="ownOrderItems['+productLen+'].productId" value="'+product.productId+'"></tr>'
		                        [/@compress]);
		                        productLen++;
		                    });
		                    // tempProducts = {} ;

		                },
		                onClose:function(){
		                    // dealCache();
		                },
		                onCancel:function(){
		                    // dealCache();
		                },
		                onShow:function(){
		                    //$(".dialogContent").height("300px");
		                    //$(".dialogContent").css("overflow" , "auto");
		                }
		            });

		        });

		    	var $inputForm = $("#inputForm");
		    	var $needTable = $("#needTable");

		    	[@flash_message /]

		        $.validator.addClassRules({
		            reDateClass: {
		                required: true
		            },
		            quantityClass:{
		                required : true ,
		                digits : true
		            }
		        });



		    	// 表单验证
		    	$inputForm.validate({
		    		rules: {
		    			supplier: {
		    				required: true
		    			}
		    		},
		            submitHandler: function(form) {

		                if ($.isEmptyObject(needsCache)) {
		                    $.message("warn", "请选择地址");
		                    return false;





		                }
		                if($.isEmptyObject(products)){
		                    $.message("warn", "请选择商品");
		                    return false;
		                }

		                if(!exists) {
		            		$.message("warn", message);
		            		return false;
		            	}

		                /*$(".reDateClass").rules('add' , {required: true ,messages: {
		                    required: "Specify a valid email"
		                }});

		                $(".reDateClass").each(function(){
		                    alert($(this).val());

		    		    });*/

		                //return false ;
		                $(form).find("input:submit").prop("disabled", true);
		                form.submit();
		            }
		    	});

		    	$("#addNeed").click(function(){
		        	$.dialog({
		        		title:"添加地址",
		        		width:600,
		        		height:450,
		        		[@compress single_line=true]
		        		content:'
		        			<form id="addNeedForm" action="../utils/saveNeed.jhtml" method="post" class="addNeedForm form form-horizontal">
				        		<div class="pag_div">
					        		<div class="row cl">
					        			<label class="form-label col-xs-4 col-sm-3">
					        				<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>联系人<\/label>
					        			<div class="formControls col-xs-8 col-sm-7">
					        				<input type="text" class="input-text radius" name="userName" id="userName" maxlength="20" autocomplete="off"\/>
					        			<\/div>
					        		<\/div>
					        		<div class="row cl">
										<label class="form-label col-xs-4 col-sm-3">
											<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>联系电话<\/label>
										<div class="formControls col-xs-8 col-sm-7">
											<input type="text" class="input-text radius" name="tel" id="tel" maxlength="200" \/>
										<\/div>
									<\/div>
									<div class="row cl">
										<label class="form-label col-xs-4 col-sm-3">
											<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>收货点地址<\/label>
										<div class="formControls col-xs-8 col-sm-7">

											<span class="fieldSet">
												<input type="hidden" id="areaId" name="areaId" \/>
											<\/span>

											<div>
												<input type="text" class="input-text radius" placeholder="详细地址" name="address" id="address" maxlength="50" autocomplete="off" \/>
											<\/div>
										<\/div>
									<\/div>
									<div class="row cl">
										<label class="form-label col-xs-4 col-sm-3">
											<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>收货时间<\/label>
										<div class="formControls col-xs-8 col-sm-7">
											<input type="text" readonly="readonly" id="reDate" name="reDate" class="reDate text input-text radius" class="text Wdate reDateClass" onfocus="WdatePicker({dateFmt: \'yyyy-MM-dd\',readOnly:true,minDate: \'#F{$dp.$D(deliveryTime)}\'});" value="${order.reDate}" \/>
										<\/div>
									<\/div>
									<div class="row cl">
										<label class="form-label col-xs-4 col-sm-3">
											订单备注
										<\/label>
										<div class="formControls col-xs-8 col-sm-7">
											<textarea class="text_area1" name="memo" id="memo" rows="" cols=""><\/textarea>
										<\/div>
									<\/div>
					        	<\/div>
			        		<\/form>',
		        		[/@compress]
		        		ok: "${message("admin.dialog.ok")}",
		                cancel: "${message("admin.dialog.cancel")}",
		                onOk: function(){
		                    if(!$("#addNeedForm").valid()){
		                        return false ;
		                    }
		                    var sendData = $("#addNeedForm").serializeArray();
		                    var reDate = $("#reDate").val();
		                    var memo = $("#memo").val();
		                    $.ajax({
		                        url: "../utils/saveNeed.jhtml",
		                        type: "post",
		                        dataType: "json",
		                        data:sendData,
		                        cache: true,
		                        beforeSend: function () {

		                        },
		                        success: function(data) {
		                            if(data.code == "0"){
		                                //处理显示到列表
		                                var message = data.data ;
		                                $needTable.append([@compress single_line = true]
		                                        '<tr id="remove_"><td>'+message.userName+'<\/td><td>'+message.tel+'<\/td><td><span id="addressSpan'+needLen+'">'+message.address+'<\/span> <a title="${message("admin.common.edit")}" href="javascript:void(0);" onclick="editAddress('+needLen+')"  class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"><\/i><\/a><\/td><td><input id="rule_'+needLen+'" type="text" name="orderNeedsItems['+needLen+'].reDate" class="reDate tdInput input-text radius" onfocus="WdatePicker({dateFmt: \'yyyy-MM-dd\' , readOnly:true});" value="'+reDate+'" \/><\/td><td><input class="tdInput input-text radius memo" name="orderNeedsItems['+needLen+'].memo" value="'+memo+'" \/><\/td><td><a href="javascript:void(0);" onclick="javascript:delNeedChild(this , '+message.id+');" ><i class="operation_icon icon_del"></i><\/a><\/td><input type="hidden" name="orderNeedsItems['+needLen+'].needId" value="'+message.id+'"><input id="areaId'+needLen+'" type="hidden" name="orderNeedsItems['+needLen+'].areaId" value="'+message.areaId+'"><input id="addresses'+needLen+'" type="hidden" name="orderNeedsItems['+needLen+'].address" value="'+message.address+'"></tr>'
		                                [/@compress]);
		                                hasRemark();
		                                addNeed(message);
		                                delete tempNeedsCache[message.id];
		                                needLen++;
		                            }else{
		                                $.message("error", "添加地址失败");
		                                return false;
		                            }
		                        },
		                        complete: function() {

		                        }
		                    });
		                },
		        		onClose:function(){
			            },
			            onCancel:function(){
			            },
		        		onShow:function(){
		        			$("#addNeedForm input[name='areaId']").lSelect({
		                        url: "${base}/admin/common/area.jhtml"
		                    });

		        			$("#addNeedForm").validate({
		                        rules: {
		                            userName: {
		                                required: true
		                            },
		                            tel: {
		                                required: true,
		                                pattern: /^1[3|4|5|7|8]\d{9}$/,
		                                remote: {
		                                    url: "../utils/checkNeedTel.jhtml",
		                                    cache: false
		                                }

		                            },
		                            areaId: {
		                                required: true
		                            },
		                            reDate: {
		                                required: true
		                            },
		                            address:{
		                                required: true
		                            }
		                        },
		                        messages: {
		                            tel: {
		                                remote: "手机号已存在"
		                            }

		                        }
		                    });

		        		}
		        	});
		        });


		    	//选择收货点
		        $("#selectNeed").click(function(){

		            var contentPath = "../utils/turnOverNeedList.jhtml";

		            $.dialog({
		                title: "选择流水收货点",
		                [@compress single_line=true]
		                    content: '<iframe id="needFrame" width="" height="" frameborder="0" src="'+contentPath+'"><\/iframe>',
		                [/@compress]
		                width: $(".bodyObj").width()-20,
		                height:$(".bodyObj").height()-40,
		                ok: "${message("admin.dialog.ok")}",
		                cancel: "${message("admin.dialog.cancel")}",
		                onOk: function(){
		                    $.each(tempNeedsCache , function(key , message){
		                        $needTable.append([@compress single_line = true]
		                                '<tr><td>'+message.userName+'<\/td><td>'+message.tel+'<\/td><td><span id="addressSpan'+needLen+'">'+message.address+'<\/span> <a title="${message("admin.common.edit")}" href="javascript:void(0);" onclick="editAddress('+needLen+')"  class="ml-5" style="text-decoration:none"><i class="operation_icon icon_bianji"><\/i><\/a><\/td><td><input type="text" id="rule_'+needLen+'" name="orderNeedsItems['+needLen+'].reDate" class="reDate tdInput input-text radius reDateClass" class="text Wdate reDateClass" onfocus="WdatePicker({dateFmt: \'yyyy-MM-dd\' , readOnly:true , minDate: \'#F{$dp.$D(deliveryTime)}\'});" value="" \/><\/td><td><input class="tdInput input-text radius memo" name="orderNeedsItems['+needLen+'].memo" value="" \/><\/td><td><a href="javascript:void(0);" onclick="javascript:delNeedChild(this , '+message.id+');" ><i class="operation_icon icon_del"></i><\/a><\/td><input type="hidden" name="orderNeedsItems['+needLen+'].needId" value="'+message.id+'"><input id="areaId'+needLen+'" type="hidden" name="orderNeedsItems['+needLen+'].areaId" value="'+message.areaId+'"><input id="addresses'+needLen+'" type="hidden" name="orderNeedsItems['+needLen+'].address" value="'+message.addresses+'"></tr>'
		                        [/@compress]);
		                        needLen++;

		                    });
		                    hasRemark();
		                    tempNeedsCache = {} ;

		                },
		                onClose:function(){
		                    dealNeedCache();
		                },
		                onCancel:function(){
		                    dealNeedCache();
		                },
		                onShow:function(){
		                    //$(".dialogContent").height("300px");
		                    //$(".dialogContent").css("overflow" , "auto");
		                }
		            });

		        });

			   $("#goHome").on("click",function(){
					var nav = window.top.$(".index_nav_one");
	        			nav.find("li li").removeClass('clickTo');
						nav.find("i").removeClass('click_border');
				})

			});

			$("input[name=assignedModel]").on("click",function(){
				hasRemark();
			});

			function hasRemark(){
				if($("input[name=assignedModel]:checked").val()=="BRANCH"){
					$(".memo").val("");
					$(".memo").hide();
				}else{
					$(".memo").show();
				}
			}
			function editAddress(needLen){
				var areaId=$("#areaId"+needLen).val();
				var addresses=$("#addresses"+needLen).val();
				var treePath="";
				//加载地区数据、用于默认选择
				$.ajax({
		            type: "GET",
		            url: "../common/getTreePath.jhtml",
		            async: false,
		            data: {"id":areaId},
		            success: function (o) {
		            	treePath=o.treePath;
		            }
		        });

		        $.dialog({
		        		title:"修改收货地址",
		        		width:600,
		        		height:300,
		        		[@compress single_line=true]
		        		content:'<form id="editAddressForm" action="" method="post" class="addNeedForm form form-horizontal">
							<div class="pag_div" style="padding-top:50px;">
								<div class="row cl">
									<label class="form-label col-xs-4 col-sm-3">
										<img src="${base}/resources/admin1.0/images/bitian_icon.svg" alt="" \/>地区<\/label>
									<div class="formControls col-xs-8 col-sm-7">
										<span class="fieldSet">
											<input type="hidden" id="areaId" name="areaId" value="'+areaId+'" treePath="'+treePath+'" \/>
										<\/span>
									<\/div>
								<\/div>
								<div class="row cl">
									<label class="form-label col-xs-4 col-sm-3">详细地址<\/label>
									<div class="formControls col-xs-8 col-sm-7">
										<input type="text" class="input-text radius" placeholder="详细地址" name="address" id="address" maxlength="50" autocomplete="off" value="'+addresses+'" \/>
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
		                    areaId=$("#areaId").val();
		                    addresses=$("#address").val();
		                    $("#areaId"+needLen).val(areaId);
		                    $("#addresses"+needLen).val(addresses);
		                    //获取所选地区的全称
							$.ajax({
					            type: "GET",
					            url: "../common/getTreePath.jhtml",
					            async: false,
					            data: {"id":areaId},
					            success: function (o) {
					            	$("#addressSpan"+needLen).html(o.fullName+addresses);
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
		    }

		</script>
		
	</body>
</html>
[/#escape]